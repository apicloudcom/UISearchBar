/**
 * APICloud Modules
 * Copyright (c) 2014-2015 by APICloud, Inc. All Rights Reserved.
 * Licensed under the terms of the The MIT License (MIT).
 * Please see the license.html included with this distribution for details.
 */
package com.uzmap.pkg.uzmodules.UISearchBar;

import com.uzmap.pkg.uzcore.uzmodule.UZModuleContext;
import com.uzmap.pkg.uzkit.UZUtility;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;

public class XEditText extends EditText {
	private UZModuleContext moduleContext;

	public XEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.moduleContext = Constans.mModuleContext;
		init();
	}

	private void init() {
		String placeholder = moduleContext.optString("placeholder");
		if (TextUtils.isEmpty(placeholder)) {
			placeholder = "请输入搜索关键字";
		}
		String color = moduleContext.optString("textColor");
		if (TextUtils.isEmpty(color)) {
			color = "#000000";
		}
		setHint(placeholder);
		setTextColor(UZUtility.parseCssColor(color));
	}
}
