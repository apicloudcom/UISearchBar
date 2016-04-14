/**
  * APICloud Modules
  * Copyright (c) 2014-2015 by APICloud, Inc. All Rights Reserved.
  * Licensed under the terms of the The MIT License (MIT).
  * Please see the license.html included with this distribution for details.
  */

#import "UZUINavigationView.h"

@implementation UZUINavigationView

- (id)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
    }
    return self;
}

//- (void)drawRect:(CGRect)rect
//{
//    // Drawing code
//    //横线
//    UIColor *c=[UIColor whiteColor];
//    UIColor * colorForMark = [UIColor darkGrayColor];
//    CGContextRef context =UIGraphicsGetCurrentContext();
//    CGContextSetLineWidth(context, 0.4);//画笔粗细
//    CGContextSetStrokeColorWithColor(context, c.CGColor);//画笔颜色
//    //        CGContextSetRGBStrokeColor(context, 132/255.0, 132/255.0, 132/255.0,0.5);//画笔颜色
//  
//    CGPoint cc = CGPointMake(0, rect.size.height-0.5);
//    CGPoint dd = CGPointMake(rect.size.width, rect.size.height-0.5);
//    CGContextMoveToPoint(context, cc.x, cc.y);
//    CGContextAddLineToPoint(context, dd.x, dd.y);
//    CGContextStrokePath(context);
//    
//    CGContextSetStrokeColorWithColor(context, colorForMark.CGColor);//画笔颜色
//    CGPoint ccc = CGPointMake(0, rect.size.height-1.0);
//    CGPoint ddd = CGPointMake(rect.size.width, rect.size.height-1.0);
//    CGContextMoveToPoint(context, ccc.x, ccc.y);
//    CGContextAddLineToPoint(context, ddd.x, ddd.y);
//    CGContextStrokePath(context);
//}

@end
