/**
  * APICloud Modules
  * Copyright (c) 2014-2015 by APICloud, Inc. All Rights Reserved.
  * Licensed under the terms of the The MIT License (MIT).
  * Please see the license.html included with this distribution for details.
  */

#import "UZUISearchBar.h"
#import "NSDictionaryUtils.h"
#import "UZAppUtils.h"
#import "UZUISearchVC.h"

@interface UZUISearchBar ()
<SearchUIViewDelegate> {
    NSInteger cbIDUI;
    BOOL animationUI;
    NSString *dataBaseName;
    UZUISearchVC *_searchUIView;
}

@property (nonatomic, strong) UZUISearchVC *searchUIView;
@property (nonatomic, strong) NSString *searchTextStr;

@end

@implementation UZUISearchBar

@synthesize searchUIView = _searchUIView;

- (void)dispose {
    if (_searchUIView) {
        _searchUIView.delegate = nil;
        self.searchUIView = nil;
    }
}

#pragma mark -
#pragma mark interface
#pragma mark -

- (void)open:(NSDictionary *)paramsDict {
    cbIDUI = [paramsDict integerValueForKey:@"cbId" defaultValue:-1];
    NSString *placeholder = [paramsDict stringValueForKey:@"placeholder" defaultValue:@"请输入搜索关键字"];
    NSDictionary *styles = [NSDictionary dictionaryWithDictionary:[paramsDict dictValueForKey:@"styles" defaultValue:@{}]];
    NSDictionary *searchBox = [styles dictValueForKey:@"searchBox" defaultValue:@{}];
    NSString *bgImg = [searchBox stringValueForKey:@"bgImg" defaultValue:nil];
    if (!bgImg || bgImg.length<=0) {
        bgImg = [[NSBundle mainBundle]pathForResource:@"res_searchBar/searchBar_bg" ofType:@"png"];
    } else {
        bgImg = [self getPathWithUZSchemeURL:bgImg];
    }
    NSString *textColor = [searchBox stringValueForKey:@"color" defaultValue:@"#000000"];
    float textSize = [searchBox floatValueForKey:@"size" defaultValue:14];
    float  textFieldHeight = [searchBox floatValueForKey:@"height" defaultValue:44];
    NSDictionary *cancel = [styles dictValueForKey:@"cancel" defaultValue:@{}];
    NSString *cancelColor = [cancel stringValueForKey:@"color" defaultValue:@"#D2691E"];
    float  cancelSize = [cancel floatValueForKey:@"size" defaultValue:16];
    NSString *cancelBgColor = [cancel stringValueForKey:@"bg" defaultValue:@"rgba(0,0,0,0)"];
    NSDictionary *navBar = [styles dictValueForKey:@"navBar" defaultValue:@{}];
    NSString *barBgColor = [navBar stringValueForKey:@"bgColor" defaultValue:@"#FFFFFF"];
    NSString *borderColor = [navBar stringValueForKey:@"borderColor" defaultValue:@"#ccc"];
    NSDictionary *clear = [styles dictValueForKey:@"clear" defaultValue:@{}];
    NSString *cleanColor = [clear stringValueForKey:@"color" defaultValue:@"#000000"];
    float  cleanSize = [clear floatValueForKey:@"size" defaultValue:16];
    NSString *clearBC = [clear stringValueForKey:@"borderColor" defaultValue:@"#ccc"];
    NSDictionary *list = [styles dictValueForKey:@"list" defaultValue:@{}];
    NSString *listColor = [list stringValueForKey:@"color" defaultValue:@"#696969"];
    NSString *listBgColor = [list stringValueForKey:@"bgColor" defaultValue:@"#FFFFFF"];
    float listSize = [list floatValueForKey:@"size" defaultValue:16];
    NSString *listBC = [list stringValueForKey:@"borderColor" defaultValue:@"#eee"];
    NSInteger  recordCount = [paramsDict integerValueForKey:@"historyCount" defaultValue:10];
    animationUI = [paramsDict boolValueForKey:@"animation" defaultValue:YES];
    NSString *customName = [paramsDict stringValueForKey:@"dataBase" defaultValue:@"UISearchBarData"];
    dataBaseName = [NSString stringWithFormat:@"UISearchBar%@",customName];
    float mainScreenWidth =[UIScreen mainScreen].bounds.size.width;
    float inputMarginL = [searchBox floatValueForKey:@"marginL" defaultValue:5];
    float inputWidth = [searchBox floatValueForKey:@"width" defaultValue:mainScreenWidth*0.75];
    float cancelWidth = [cancel floatValueForKey:@"width" defaultValue:mainScreenWidth*0.2];
    float cencalMarginR = [cancel floatValueForKey:@"marginR" defaultValue:mainScreenWidth*0.025];
    //打开搜索视图
    _searchUIView = [[UZUISearchVC alloc] initWithName:dataBaseName];
    _searchUIView.textUIFieldWidth = inputWidth;
    _searchUIView.inputMarginL = inputMarginL;
    _searchUIView.cancelBtnWidth = cancelWidth;
    _searchUIView.cancelBtnMarginR = cencalMarginR;
    _searchUIView.delegate = self;
    _searchUIView.placeholderUI = placeholder;
    _searchUIView.bgImgUI = bgImg;
    _searchUIView.cancelUISize = cancelSize;
    _searchUIView.cancelColorUI = cancelColor;
    _searchUIView.textUIColor = textColor;
    _searchUIView.textSize = textSize;
    _searchUIView.barUIBgColor = barBgColor;
    _searchUIView.lisUItBgColor = listBgColor;
    _searchUIView.cleanUISize = cleanSize;
    _searchUIView.cleanUIColor = cleanColor;
    _searchUIView.isUIAnimation = animationUI;
    _searchUIView.recordUICount = recordCount;
    _searchUIView.textFieldHeight = textFieldHeight;
    _searchUIView.listUIColor = listColor;
    _searchUIView.listUISize = listSize;
    _searchUIView.cancelUIBgColor = cancelBgColor;
    _searchUIView.textsUI = [NSDictionary dictionaryWithDictionary:[paramsDict dictValueForKey:@"texts" defaultValue:@{}]];
    _searchUIView.showRecord = [paramsDict boolValueForKey:@"showRecordBtn" defaultValue:YES];
    _searchUIView.navBC = borderColor;
    _searchUIView.listBC = listBC;
    _searchUIView.clearBC = clearBC;
    if (self.searchTextStr) {
        [_searchUIView.textUIField setText:self.searchTextStr];
        self.searchTextStr = nil;
    }
    [self.viewController presentViewController:_searchUIView animated:animationUI completion:nil];
}

- (void)close:(NSDictionary*)paramsDict {
    [self.searchUIView dismissViewControllerAnimated:animationUI completion:nil];
}

- (void)setText:(NSDictionary*)paramaDict {
    self.searchTextStr = [paramaDict stringValueForKey:@"text" defaultValue:@""];
    //[_searchUIView.textUIField setText:text];
}

- (void)clearHistory:(NSDictionary*)paramsDict {
    NSUserDefaults *ud = [NSUserDefaults standardUserDefaults];
    [ud removeObjectForKey:dataBaseName];
    [self.searchUIView.dataUISource removeAllObjects];
    [self.searchUIView.historyUI reloadData];
    [self.searchUIView reloadFrame];
}

#pragma mark -
#pragma mark SearchViewDelegate
#pragma mark -

- (void)record:(id)params {
    [self sendResultEventWithCallbackId:cbIDUI dataDict:[NSDictionary dictionaryWithObject:@"record" forKey:@"eventType"] errDict:nil doDelete:NO];
}

- (void)backSearchText:(NSString *)text type:(NSString *)type {
    NSMutableDictionary *send= [NSMutableDictionary dictionaryWithCapacity:2];
    [send setObject:text forKey:@"text"];
    [send setObject:type forKey:@"eventType"];
    [self sendResultEventWithCallbackId:cbIDUI dataDict:send errDict:nil doDelete:NO];
}

- (NSString *)getPath:(NSString *)path {
    return [self getPathWithUZSchemeURL:path];
}

@end
