/**
 * APICloud Modules
 * Copyright (c) 2014-2015 by APICloud, Inc. All Rights Reserved.
 * Licensed under the terms of the The MIT License (MIT).
 * Please see the license.html included with this distribution for details.
 */

package com.uzmap.pkg.uzmodules.UISearchBar;
import android.graphics.Bitmap;

public class ConfigParam {
	
	public static final String CANCEL_TEXT = "取消";
	public static final String CLEAR_TEXT = "清除搜索记录";
	public static final String PLACE_HOLDER_TEXT = "请输入搜索关键字";

	public String placeHolder = PLACE_HOLDER_TEXT;
	public int historyCount = 10;

	public boolean animation = true;
	public boolean showRecordBtn = true;
	
	public String database = "";

	public String cancelTxt = CANCEL_TEXT;
	public String clearText = CLEAR_TEXT;

	public String searchBoxBgImg = null;
	public int searchBoxColor = 0xFF000000;

	public int searchBoxWidth = 250;
	public int searchBoxHeight = 44;

	public int cancel_bg_color = 0x00000000;
	public Bitmap cancel_bg_bitmap;
	public int cancal_color = 0xFFD2691E;
	public int cancel_size = 16;
	public int cancel_width;
	
	public int cancel_marginR;

	public int navBgColor = 0xFFFFFFFF;
	public int navBorderColor = 0xFFCCCCCC;

	public int list_color = 0xFF696969;
	public int list_bg_color = 0xFFFFFFFF;
	public int list_size = 16;
	public int list_border_color = 0xFFEEEEEE;
	public int list_item_active_bg_color = 0xFFEEEEEE;

	public int clear_font_color = 0xFF000000;
	public int clear_font_size = 16;
	public int clear_border_color = 0xFFCCCCCC;
	public int clear_active_bg_color = 0xFFEEEEEE;
	public int clear_bg_color = 0xFFFFFFFF;

}
