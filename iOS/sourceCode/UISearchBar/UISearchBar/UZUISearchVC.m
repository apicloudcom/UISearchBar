
/**
  * APICloud Modules
  * Copyright (c) 2014-2015 by APICloud, Inc. All Rights Reserved.
  * Licensed under the terms of the The MIT License (MIT).
  * Please see the license.html included with this distribution for details.
  */

#import "UZUISearchVC.h"
#import "UZUINavigationView.h"
#import <QuartzCore/QuartzCore.h>

@interface UZUISearchVC ()
{
    NSString *dataName;
}
@end

@implementation UZUISearchVC
@synthesize dataUISource = _dataUISource;
@synthesize delegate;
@synthesize textUIField = _textUIField;
@synthesize placeholderUI, bgImgUI, cancelUISize, cancelColorUI, textFieldHeight, textUIFieldWidth;
@synthesize textUIColor, barUIBgColor, lisUItBgColor, cleanUISize, cleanUIColor, recordUICount;
@synthesize isUIAnimation, listUIColor, listUISize;
@synthesize showRecord;

- (void)dealloc {
    if (_dataUISource) {
        self.dataUISource = nil;
    }
    if (_textUIField) {
        self.textUIField = nil;
    }
    if (placeholderUI) {
        self.placeholderUI = nil;
    }
    if (bgImgUI) {
        self.bgImgUI = nil;
    }
    if (cancelColorUI) {
        self.cancelColorUI = nil;
    }
    if (textUIColor) {
        self.textUIColor = nil;
    }
    if (barUIBgColor) {
        self.barUIBgColor = nil;
    }
    if (lisUItBgColor) {
        self.lisUItBgColor = nil;
    }
    if (cleanUIColor) {
        self.cleanUIColor = nil;
    }
    if (listUIColor) {
        self.listUIColor = nil;
    }
}

- (id)initWithName:(NSString *)storeName {
    self = [super init];
    if (self) {
        dataName = storeName;
        NSUserDefaults *ud = [NSUserDefaults standardUserDefaults];
        NSArray *dataSource = [ud objectForKey:storeName];
        if (dataSource) {
            _dataUISource = [NSMutableArray arrayWithArray:dataSource];
        } else {
            _dataUISource = [NSMutableArray array];
        }
    }
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    int num = (int)_dataUISource.count;
    for (int i = 0; i < num; i++) {
        if (_dataUISource.count > self.recordUICount) {
            [_dataUISource removeLastObject];
        } else {
            break;
        }
    }
    float mainScreenWidth = [UIScreen mainScreen].bounds.size.width;
    float mainScreenHeight = [UIScreen mainScreen].bounds.size.height;
    float nvcBgHeight ;
    if ([[UIDevice currentDevice].systemVersion floatValue] >= 7.0) {
        nvcBgHeight = self.textFieldHeight+20;
    } else {
        nvcBgHeight = self.textFieldHeight;
    }
    
    //导航条背景
    UZUINavigationView *nvcBg = [[UZUINavigationView alloc]init];
    if ([[UIDevice currentDevice].systemVersion floatValue] >= 7.0) {
        nvcBg.frame = CGRectMake(0, 0, mainScreenWidth, self.textFieldHeight+1+20);
    } else {
        nvcBg.frame = CGRectMake(0, 0, mainScreenWidth, self.textFieldHeight+1);
    }
    nvcBg.backgroundColor = [UZAppUtils colorFromNSString:self.barUIBgColor];
    [self.view addSubview:nvcBg];
    
    UILabel *lineLabel = [[UILabel alloc] initWithFrame:CGRectMake(0,nvcBg.bounds.size.height-1, mainScreenWidth, 1)];
    lineLabel.backgroundColor = [UZAppUtils colorFromNSString:self.navBC];
    [nvcBg addSubview:lineLabel];
    self.view.backgroundColor = [UZAppUtils colorFromNSString:@"#D3D3D3"];
    
    //输入框
    UIImageView *bgField = [[UIImageView alloc] init];
    bgField.image = [UIImage imageWithContentsOfFile:self.bgImgUI];
    bgField.frame = CGRectMake(5, 25, self.textUIFieldWidth-5, self.textFieldHeight-10);
    [self.view addSubview:bgField];
    _textUIField= [[UITextField alloc] init];
    _textUIField.frame = CGRectMake(15, 25, mainScreenWidth*0.75-15, self.textFieldHeight-10);
    _textUIField.placeholder = self.placeholderUI;
    _textUIField.clearButtonMode = UITextFieldViewModeWhileEditing;
    _textUIField.delegate = self;
    _textUIField.textColor = [UZAppUtils colorFromNSString:self.textUIColor];
    _textUIField.returnKeyType = UIReturnKeySearch;
    [self.view addSubview:self.textUIField];
    
    //录音按钮
    record = [UIButton buttonWithType:UIButtonTypeCustom];
    float recordWidth = self.textFieldHeight-20;
    record.frame = CGRectMake(_textUIField.bounds.size.width-recordWidth-10, 5, recordWidth, recordWidth);
    NSString *recordImgPath1 = [[NSBundle mainBundle]pathForResource:@"res_searchBar/record_normal" ofType:@"png"];
    NSString *recordImgPath2 = [[NSBundle mainBundle] pathForResource:@"res_searchBar/record_selected" ofType:@"png"];
    [record setImage:[UIImage imageWithContentsOfFile:recordImgPath1] forState:UIControlStateNormal];
    [record setImage:[UIImage imageWithContentsOfFile:recordImgPath2] forState:UIControlStateSelected];
    [record addTarget:self action:@selector(recordClick:) forControlEvents:UIControlEventTouchUpInside];
    if (self.showRecord) {
        [_textUIField addSubview:record];
    }
    
    //取消按钮
    UIButton *cancel = [UIButton buttonWithType:UIButtonTypeCustom];
    cancel.frame = CGRectMake(mainScreenWidth*0.775, 25, mainScreenWidth*0.2, self.textFieldHeight-10);
    [cancel setTitleColor:[UZAppUtils colorFromNSString:self.cancelColorUI] forState:UIControlStateNormal];
    NSString *cancelText = [self.textsUI stringValueForKey:@"cancelText" defaultValue:@"取消"];
    [cancel setTitle:cancelText forState:UIControlStateNormal];
    cancel.titleLabel.font = [UIFont systemFontOfSize:self.cancelUISize];
    if ([UZAppUtils isValidColor:self.cancelUIBgColor]) {
        cancel.backgroundColor = [UZAppUtils colorFromNSString:self.cancelUIBgColor];
    } else {
        [cancel setBackgroundImage:[UIImage imageWithContentsOfFile:[self.delegate getPath:self.cancelUIBgColor]] forState:UIControlStateNormal];
    }
    [cancel addTarget:self action:@selector(close:) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:cancel];
    
    cellHeight = 44;
    float tableHeight = ([self.dataUISource count] + 1)*cellHeight;
    self.historyUI = [[UITableView alloc] init];
    _historyUI.scrollEnabled = NO;
    if (tableHeight > mainScreenHeight-nvcBgHeight) {
        tableHeight = mainScreenHeight-nvcBgHeight;
        _historyUI.scrollEnabled = YES;
    }
    _historyUI.frame = CGRectMake(0, self.textFieldHeight+21, mainScreenWidth, tableHeight);
    _historyUI.delegate = self;
    _historyUI.dataSource = self;
    _historyUI.backgroundColor = [UZAppUtils colorFromNSString:self.lisUItBgColor];
    _historyUI.separatorStyle = NO;
    [self.view addSubview:_historyUI];
    
    // 监听点击
    CGRect gestureRect  = _historyUI.frame;
    gestureRect.size.height = mainScreenHeight-nvcBgHeight;
    gesture = [[UIView alloc]initWithFrame:gestureRect];
    gesture.backgroundColor = [UIColor clearColor];
    [self.view addSubview:gesture];
    gesture.hidden = YES;
    
    UITapGestureRecognizer *singleTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleSingleTap:)];
    singleTap.delaysTouchesBegan = YES;
    singleTap.numberOfTapsRequired = 1;
    [gesture addGestureRecognizer:singleTap];
    
    [[NSNotificationCenter defaultCenter]  addObserver:self selector:@selector(keyboardWasHidden:) name:UIKeyboardDidHideNotification object:nil];
   [[NSNotificationCenter defaultCenter]  addObserver:self selector:@selector(keyboardWasShow:) name:UIKeyboardDidShowNotification object:nil];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewDidAppear:(BOOL)animated {
    [self.textUIField becomeFirstResponder];
}

#pragma mark-
#pragma mark keyboard
#pragma mark-

- (void)keyboardWasHidden:(id)param {
    gesture.hidden = YES;
}

- (void)keyboardWasShow:(id)param {
    gesture.hidden = NO;
}

#pragma mark-
#pragma mark UITableViewDataSource
#pragma mark-

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.dataUISource.count + 1;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *idStr = @"cell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:idStr];
    if (!cell) {
        cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:idStr];
        cell.selectionStyle = UITableViewCellSelectionStyleGray;
        cell.contentView.backgroundColor = [UZAppUtils colorFromNSString:self.lisUItBgColor];
        UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(0, cellHeight-1, [UIScreen mainScreen].bounds.size.width, 1)];
        label.backgroundColor = [UIColor clearColor];
        label.tag = 10086;
        [cell.contentView addSubview:label];
    }
    if (indexPath.row == [_dataUISource count]) {
        NSString *clearText = [self.textsUI stringValueForKey:@"clearText" defaultValue:@"清除搜索记录"];
        if ([clearText isEqualToString:@""]) {
            clearText = @"清除搜索记录";
        }
        cell.textLabel.text = clearText;
        cell.textLabel.textAlignment = NSTextAlignmentCenter;
        cell.textLabel.textColor = [UZAppUtils colorFromNSString:self.cleanUIColor];
        cell.textLabel.font = [UIFont systemFontOfSize:self.cleanUISize];
        UILabel *label = (UILabel *)[cell.contentView viewWithTag:10086];
        label.backgroundColor = [UZAppUtils colorFromNSString:self.clearBC];
    } else {
        cell.textLabel.text = [self.dataUISource objectAtIndex:indexPath.row];
        cell.textLabel.font = [UIFont systemFontOfSize:self.listUISize];
        cell.textLabel.textAlignment = NSTextAlignmentLeft;
        cell.textLabel.textColor = [UZAppUtils colorFromNSString:self.listUIColor];
        UILabel *label = (UILabel *)[cell.contentView viewWithTag:10086];
        label.backgroundColor = [UZAppUtils colorFromNSString:self.listBC];
    }
    return cell;
}

#pragma mark-
#pragma mark UITableViewDelegate
#pragma mark-

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return cellHeight;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:NO];
    [self.textUIField resignFirstResponder];
    if (indexPath.row==[_dataUISource count]) {
        [_dataUISource removeAllObjects];
        NSUserDefaults * ud = [NSUserDefaults standardUserDefaults];
        [ud removeObjectForKey:dataName];
        [ud synchronize];
        [tableView reloadData];
        float tableHeight = ([self.dataUISource count]+1)*cellHeight;
        tableView.scrollEnabled = NO;
        CGRect newRect = tableView.frame;
        newRect.size.height = tableHeight;
        tableView.frame = newRect;
        return;
    }
    [self dismissViewControllerAnimated:isUIAnimation completion:nil];
    [self.delegate backSearchText:[self.dataUISource objectAtIndex:indexPath.row] type:@"history"];
}

#pragma mark-
#pragma mark method
#pragma mark-

- (void)handleSingleTap:(UITapGestureRecognizer *)tap {
    [self.textUIField resignFirstResponder];
}

- (void)close:(id)btn {
    [self dismissViewControllerAnimated:isUIAnimation completion:nil];
}

- (void)recordClick:(id)btn {
    [self.delegate record:nil];
}

#pragma mark-
#pragma mark textFieldDelegate
#pragma mark-

- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField {
    return YES;
}

- (void)textFieldDidBeginEditing:(UITextField *)textField {
    if ([textField.text  isEqual: @""]) {
        record.hidden = NO;
    } else {
        record.hidden = YES;
    }
    [textField bringSubviewToFront:record];
}

- (BOOL)textFieldShouldEndEditing:(UITextField *)textField {
    return YES;
}

- (void)textFieldDidEndEditing:(UITextField *)textField {
    record.hidden = NO;
}

- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string {
    record.hidden = YES;
    return YES;
}

- (BOOL)textFieldShouldClear:(UITextField *)textField {
    record.hidden = NO;
    [textField bringSubviewToFront:record];
    return YES;
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    if ([textField.text isEqualToString:@""]) {
        return YES;
    }
    [textField resignFirstResponder];
    if (textField.text.length>0) {
        [_dataUISource insertObject:textField.text atIndex:0];
        if ([_dataUISource count]>self.recordUICount) {
            [_dataUISource removeLastObject];
        }
        NSUserDefaults *ud = [NSUserDefaults standardUserDefaults];
        [ud setObject:_dataUISource forKey:dataName];
        [ud synchronize];
        [self.delegate backSearchText:textField.text type:@"search"];
    }
    [self dismissViewControllerAnimated:isUIAnimation completion:nil];
    [_historyUI reloadData];
    
    float tableHeight = ([self.dataUISource count]+1) * cellHeight;
    float mainScreenHeight = [UIScreen mainScreen].bounds.size.height;
    float nvcBgHeight = self.textFieldHeight + 26;
    if (tableHeight > mainScreenHeight-nvcBgHeight) {
        tableHeight = mainScreenHeight-nvcBgHeight;
        _historyUI.scrollEnabled = YES;
    }
    CGRect newRect = _historyUI.frame;
    newRect.size.height = tableHeight;
    _historyUI.frame = newRect;
    self.textUIField.text = @"";
    return YES;
}

- (UIImage *)createImageWithColor: (UIColor *)color{
    CGRect rect=CGRectMake(0.0f, 0.0f, 1.0f, 1.0f);
    UIGraphicsBeginImageContext(rect.size);
    CGContextRef context = UIGraphicsGetCurrentContext();
    CGContextSetFillColorWithColor(context, [color CGColor]);
    CGContextFillRect(context, rect);
    UIImage *theImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return theImage;
}

- (void)reloadFrame {
    float tableHeight = ([self.dataUISource count]+1)*cellHeight;
    float mainScreenHeight = [UIScreen mainScreen].bounds.size.height;
    float nvcBgHeight = self.textFieldHeight+26;
    float mainScreenWidth = [UIScreen mainScreen].bounds.size.width;
    _historyUI.scrollEnabled = NO;
    if (tableHeight > mainScreenHeight-nvcBgHeight) {
        tableHeight = mainScreenHeight-nvcBgHeight;
        _historyUI.scrollEnabled = YES;
    }
    _historyUI.frame = CGRectMake(0, nvcBgHeight, mainScreenWidth, tableHeight);
}

@end
