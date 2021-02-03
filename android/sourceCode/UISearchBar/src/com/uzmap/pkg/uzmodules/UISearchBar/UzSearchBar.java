/**
 * APICloud Modules
 * Copyright (c) 2014-2015 by APICloud, Inc. All Rights Reserved.
 * Licensed under the terms of the The MIT License (MIT).
 * Please see the license.html included with this distribution for details.
 */
package com.uzmap.pkg.uzmodules.UISearchBar;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.widget.EditText;

import com.uzmap.pkg.uzcore.UZWebView;
import com.uzmap.pkg.uzcore.uzmodule.UZModule;
import com.uzmap.pkg.uzcore.uzmodule.UZModuleContext;

public class UzSearchBar extends UZModule {

	public UzSearchBar(UZWebView webView) {
		super(webView);
	}

	public void jsmethod_open(UZModuleContext moduleContext) {
		Constans.mModuleContext = moduleContext;
		Constans.mWidgetInfo = getWidgetInfo();
		SearchBarActivity.enter(this.getContext());
	}

	public void jsmethod_setText(UZModuleContext moduleContext) {
		String text = moduleContext.optString("text");

		if (!TextUtils.isEmpty(text)) {
			EditText editText = Constans.mEditText;
			if (editText != null) {
				editText.setText(text);
				editText.setSelection(text.length());
			} else {
				SharedPreferences preferences = mContext.getSharedPreferences("text", Context.MODE_PRIVATE);
				Editor editor = preferences.edit();
				editor.putString("text", text);
				editor.commit();
			}

		} else {
			EditText editText = Constans.mEditText;
			if (editText != null) {
				editText.setText("");
			} else {
				SharedPreferences preferences = mContext.getSharedPreferences("text", Context.MODE_PRIVATE);
				Editor editor = preferences.edit();
				editor.putString("text", "");
				editor.commit();
			}
		}
	}

	@Override
	protected void onClean() {
		SharedPreferences preferences = mContext.getSharedPreferences("text", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.remove("text");
		editor.commit();
		Constans.mEditText = null;

		super.onClean();
	}

	public void jsmethod_close(UZModuleContext moduleContext) {
		SearchBarActivity searchBarActivity = Constans.mSearchBarActivity;
		
		if (searchBarActivity != null) {
			searchBarActivity.finish();
		}
		
		SharedPreferences preferences = mContext.getSharedPreferences("text", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.remove("text");
		editor.commit();
		Constans.mEditText = null;
	}

	public void jsmethod_clearHistory(UZModuleContext moduleContext) {
		SearchBarActivity.cleanUp();
	}
}
