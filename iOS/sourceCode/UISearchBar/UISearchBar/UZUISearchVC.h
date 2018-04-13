/**
  * APICloud Modules
  * Copyright (c) 2014-2015 by APICloud, Inc. All Rights Reserved.
  * Licensed under the terms of the The MIT License (MIT).
  * Please see the license.html included with this distribution for details.
  */

#import <UIKit/UIKit.h>
#import "UZAppUtils.h"
#import "NSDictionaryUtils.h"

@protocol SearchUIViewDelegate <NSObject>

- (void)record:(id)params;
- (void)backSearchText:(NSString *)text type:(NSString *)type;
- (NSString *)getPath:(NSString *)path;

@optional
- (void)close;

@end

@interface UZUISearchVC : UIViewController
<UITextFieldDelegate, UITableViewDataSource, UITableViewDelegate>
{
    UIButton *record;
    UIView *gesture;
    float cellHeight;
}

@property (nonatomic, strong) NSMutableArray *dataUISource;
@property (nonatomic, assign) id <SearchUIViewDelegate> delegate;
@property (nonatomic, strong) UITextField *textUIField;;
@property (nonatomic, assign) BOOL showRecord;

@property (nonatomic, strong) UITableView *historyUI;
@property (nonatomic, strong) NSString *placeholderUI;
@property (nonatomic, strong) NSString *bgImgUI;
@property (nonatomic, strong) NSString *cancelColorUI;
@property (nonatomic, strong) NSString *textUIColor;
@property (nonatomic, strong) NSString *barUIBgColor;
@property (nonatomic, strong) NSString *lisUItBgColor;
@property (nonatomic, strong) NSString *cleanUIColor;
@property (nonatomic, strong) NSString *listUIColor;
@property (nonatomic, assign) float listUISize;
@property (nonatomic, assign) float cleanUISize;
@property (nonatomic, assign) float textUIFieldWidth,inputMarginL, cancelBtnWidth, cancelBtnMarginR;
@property (nonatomic, assign) float textFieldHeight;
@property (nonatomic, assign) float cancelUISize;
@property (nonatomic, assign) NSInteger recordUICount;
@property (nonatomic, assign) BOOL isUIAnimation;
@property (nonatomic, strong) NSDictionary *textsUI;
@property (nonatomic, strong) NSArray *stylesUI;
@property (nonatomic, strong) NSString *cancelUIBgColor;
@property (nonatomic, strong) NSString *navBC;
@property (nonatomic, strong) NSString *listBC;
@property (nonatomic, strong) NSString *clearBC;

- (void)reloadFrame;

- (id)initWithName:(NSString *)storeName;

@end
