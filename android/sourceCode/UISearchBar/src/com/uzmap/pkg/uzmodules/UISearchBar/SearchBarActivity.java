/**
 * APICloud Modules
 * Copyright (c) 2014-2015 by APICloud, Inc. All Rights Reserved.
 * Licensed under the terms of the The MIT License (MIT).
 * Please see the license.html included with this distribution for details.
 */

package com.uzmap.pkg.uzmodules.UISearchBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.uzmap.pkg.uzcore.UZResourcesIDFinder;
import com.uzmap.pkg.uzcore.uzmodule.UZModuleContext;
import com.uzmap.pkg.uzkit.UZUtility;

public class SearchBarActivity extends Activity {

	private XEditText mEditText;
	private ListView mListView;
	private ImageView mRecordImage;
	private TextView mTextView;
	private UZModuleContext mUZContext;
	private TextView mCleanTV;
	private SharedPreferences mPref;
	private int mCleanTextColor;
	private int mCleanTextSize;
	private RelativeLayout mRelativeLayout;
	private RelativeLayout mNavigationLayout;
	
	private static LinkedList<LinearLayout> list = new LinkedList<LinearLayout>();
	private ConfigParam config = new ConfigParam();

	private ImageView deleteTextImg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUZContext = Constans.mModuleContext;
		int layoutId = UZResourcesIDFinder.getResLayoutID("mo_uisearchbar_main");
		setContentView(layoutId);
		config();
		initView();
		initData();
		setOnclick();
		Constans.mSearchBarActivity = this;
	}

	public void config() {

		if (!mUZContext.isNull("placeholder")) {
			config.placeHolder = mUZContext.optString("placeholder");
		}
		if (!mUZContext.isNull("historyCount")) {
			config.historyCount = mUZContext.optInt("historyCount");
		}
		if (!mUZContext.isNull("animation")) {
			config.animation = mUZContext.optBoolean("animation");
		}
		if (!mUZContext.isNull("showRecordBtn")) {
			config.showRecordBtn = mUZContext.optBoolean("showRecordBtn");
		}
		if (!mUZContext.isNull("dataBase")) {
			config.database = mUZContext.optString("dataBase");
		}

		JSONObject textsObj = mUZContext.optJSONObject("texts");
		if (textsObj != null) {
			if (!textsObj.isNull("cancelText")) {
				config.cancelTxt = textsObj.optString("cancelText");
			}
			if (!textsObj.isNull("clearText")) {
				config.clearText = textsObj.optString("clearText");
			}
		}

		JSONObject stylesObj = mUZContext.optJSONObject("styles");
		if (stylesObj != null) {

			JSONObject searchBoxObj = stylesObj.optJSONObject("searchBox");
			if (searchBoxObj != null && !searchBoxObj.isNull("bgImg")) {
				config.searchBoxBgImg = searchBoxObj.optString("bgImg");
			}
			if (searchBoxObj != null && !searchBoxObj.isNull("color")) {
				config.searchBoxColor = UZUtility.parseCssColor(searchBoxObj.optString("color"));
			}
			if (searchBoxObj != null && !searchBoxObj.isNull("width")) {
				config.searchBoxWidth = searchBoxObj.optInt("width");
			}
			if (searchBoxObj != null && !searchBoxObj.isNull("height")) {
				config.searchBoxHeight = searchBoxObj.optInt("height");
			}
			
			if(searchBoxObj != null){
				config.searchBoxSize = searchBoxObj.optInt("size", 18);
			}
			
			JSONObject cancelObj = stylesObj.optJSONObject("cancel");
			if (cancelObj != null) {
				
				if (!cancelObj.isNull("bg")) {
					String cancelBgStr = cancelObj.optString("bg");
					Bitmap cancelBgBitmap = generateBitmap(cancelBgStr);
					if (cancelBgBitmap != null) {
						config.cancel_bg_bitmap = cancelBgBitmap;
					} else {
						config.cancel_bg_color = UZUtility.parseCssColor(cancelObj.optString("bg"));
					}
				}

				if (!cancelObj.isNull("color")) {
					config.cancal_color = UZUtility.parseCssColor(cancelObj.optString("color"));
				}

				if (!cancelObj.isNull("size")) {
					config.cancel_size = cancelObj.optInt("size");
				}
				
				if(!cancelObj.isNull("width")){
					config.cancel_width = UZUtility.dipToPix(cancelObj.optInt("width"));
				}
				
				if(!cancelObj.isNull("marginR")){
					config.cancel_marginR = UZUtility.dipToPix(cancelObj.optInt("marginR"));
				}
				
			}

			JSONObject navBarObj = stylesObj.optJSONObject("navBar");
			if (navBarObj != null) {
				if (!navBarObj.isNull("bgColor")) {
					config.navBgColor = UZUtility.parseCssColor(navBarObj.optString("bgColor"));
				}
				if (!navBarObj.isNull("borderColor")) {
					config.navBorderColor = UZUtility.parseCssColor(navBarObj.optString("borderColor"));
				}
			}

			JSONObject listObj = stylesObj.optJSONObject("list");
			if (listObj != null) {
				if (!listObj.isNull("color")) {
					config.list_color = UZUtility.parseCssColor(listObj.optString("color"));
				}
				if (!listObj.isNull("bgColor")) {
					config.list_bg_color = UZUtility.parseCssColor(listObj.optString("bgColor"));
				}
				if (!listObj.isNull("size")) {
					config.list_size = listObj.optInt("size");
				}
				if (!listObj.isNull("borderColor")) {
					config.list_border_color = UZUtility.parseCssColor(listObj.optString("borderColor"));
				}
				if (!listObj.isNull("activeBgColor")) {
					config.list_item_active_bg_color = UZUtility.parseCssColor(listObj.optString("activeBgColor"));
				}
			}

			JSONObject clearObj = stylesObj.optJSONObject("clear");
			if (clearObj != null) {
				if (!clearObj.isNull("color")) {
					config.clear_font_color = UZUtility.parseCssColor(clearObj.optString("color"));
				}
				if (!clearObj.isNull("size")) {
					config.clear_font_size = clearObj.optInt("size");
				}
				if (!clearObj.isNull("borderColor")) {
					config.clear_border_color = UZUtility.parseCssColor(clearObj.optString("borderColor"));
				}
				if (!clearObj.isNull("bgColor")) {
					config.clear_bg_color = UZUtility.parseCssColor(clearObj.optString("bgColor"));
				}
				if (!clearObj.isNull("activeBgColor")) {
					config.clear_active_bg_color = UZUtility.parseCssColor(clearObj.optString("activeBgColor"));
				}
			}
		}
	}

	private int id;

	private void clearText() {

		if (config == null) {
			return;
		}
		SharedPreferences preferences = getSharedPreferences("text" + config.database, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.remove("text");
		editor.commit();
		Constans.mEditText = null;

	}

	private void setOnclick() {
		mRecordImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					JSONObject ret = new JSONObject();
					ret.put("eventType", "record");
					mUZContext.success(ret, false);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

		deleteTextImg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mEditText.setText("");
				if (config.showRecordBtn) {
					mRecordImage.setVisibility(View.VISIBLE);
				} else {
					mRecordImage.setVisibility(View.GONE);
				}
			}
		});

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long longid) {
				if (position == list.size() - 1) {
					hide();
					id = 0;
					list.clear();
					list.add(relativeLayoutClean);
					adapter.notifyDataSetChanged();
					editor.clear();
					editor.commit();
				} else {
					int tv_listId = UZResourcesIDFinder.getResIdID("tv_listview");
					TextView tv = (TextView) view.findViewById(tv_listId);
					String searchText = tv.getText().toString();
					try {
						JSONObject ret = new JSONObject();
						ret.put("eventType", "history");
						ret.put("text", searchText);
						mUZContext.success(ret, false);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					SearchBarActivity.this.finish();
				}
			}
		});

		mEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				if (!TextUtils.isEmpty(arg0)) {
					mRecordImage.setVisibility(View.GONE);
					deleteTextImg.setVisibility(View.VISIBLE);
				} else {
					deleteTextImg.setVisibility(View.GONE);
					if (config.showRecordBtn) {
						mRecordImage.setVisibility(View.VISIBLE);
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});

		mEditText.setOnEditorActionListener(new OnEditorActionListener() {

			@SuppressWarnings("unchecked")
			@Override
			public boolean onEditorAction(TextView arg0, int actionId, KeyEvent event) {

				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					id++;
					String text = mEditText.getText().toString();
					if (!TextUtils.isEmpty(text)) {

						if (recordCount <= 0) {

							try {
								JSONObject ret = new JSONObject();
								ret.put("eventType", "search");
								ret.put("text", text);
								mUZContext.success(ret, false);
							} catch (JSONException e) {
								e.printStackTrace();
							}
							SearchBarActivity.this.finish();
							clearText();

							return false;
						}

						int index = id % recordCount;
						if (id > recordCount) {

							editor.remove(1 + "");
							editor.commit();
							Map<String, String> map = (Map<String, String>) mPref.getAll();
							if (map != null) {
								if (map.size() != 0) {
									for (int i = 1; i <= (map.size() + 1); i++) {
										for (Entry<String, String> iterable_element : map.entrySet()) {
											String key = iterable_element.getKey();
											if ((i + "").equals(key)) {
												editor.putString((i - 1) + "", iterable_element.getValue());
												editor.commit();
											}
										}
									}
								}
							}
							editor.putString(recordCount + "", text);
							editor.commit();
						} else {
							editor.putString((index == 0 ? recordCount : index) + "", text);
							editor.commit();
						}

						try {
							JSONObject ret = new JSONObject();
							ret.put("eventType", "search");
							ret.put("text", text);
							mUZContext.success(ret, false);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						SearchBarActivity.this.finish();
						clearText();
					}

				}
				return false;
			}
		});

		mTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				SearchBarActivity.this.finish();
				clearText();
				hide();
				
				JSONObject ret = new JSONObject();
				try {
					ret.put("eventType", "cancel");
					mUZContext.success(ret, false);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
	}

	private void hide() {
		mRelativeLayout.setFocusable(true);
		mRelativeLayout.requestFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mRelativeLayout.getWindowToken(), 0);
	}

	private void initView() {
		int editId = UZResourcesIDFinder.getResIdID("edt_input");
		mEditText = (XEditText) findViewById(editId);
		Constans.mEditText = mEditText;
		int listviewId = UZResourcesIDFinder.getResIdID("listview");
		mListView = (ListView) findViewById(listviewId);
		int imageId = UZResourcesIDFinder.getResIdID("img_record");
		mRecordImage = (ImageView) findViewById(imageId);

		int deleteImgId = UZResourcesIDFinder.getResIdID("img_delete");
		deleteTextImg = (ImageView) findViewById(deleteImgId);

		mEditText.setHint(config.placeHolder);
		mEditText.setTextColor(config.searchBoxColor);

		if (!config.showRecordBtn) {
			mRecordImage.setVisibility(View.GONE);
		}
		
		mEditText.setTextSize(config.searchBoxSize);

		int textId = UZResourcesIDFinder.getResIdID("tv_cancel");
		mTextView = (TextView) findViewById(textId);
		mTextView.setText(config.cancelTxt);

		int rl_navigationId = UZResourcesIDFinder.getResIdID("rl_navigation");
		mNavigationLayout = (RelativeLayout) findViewById(rl_navigationId);
		int rl_editId = UZResourcesIDFinder.getResIdID("rl_edit");
		mRelativeLayout = (RelativeLayout) findViewById(rl_editId);

		int navBarLine = UZResourcesIDFinder.getResIdID("nav_bar_line");
		View navBarLineView = findViewById(navBarLine);
		navBarLineView.setBackgroundColor(config.navBorderColor);

		int listBottomLine = UZResourcesIDFinder.getResIdID("list_bottom_line");
		findViewById(listBottomLine).setBackgroundColor(config.clear_border_color);
	}

	private static MyAdapter adapter;
	private int listSize;
	private static LinearLayout relativeLayoutClean;
	private static Editor editor;
	private int recordCount;

	@SuppressWarnings("deprecation")
	private void initData() {

		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		mPref = getSharedPreferences("text", Context.MODE_PRIVATE);
		String text = mPref.getString("text", "");
		if (!TextUtils.isEmpty(text)) {
			mEditText.setText(text);
			mEditText.setSelection(text.length());
		}

		mNavigationLayout.setBackgroundColor(config.navBgColor);

		if (TextUtils.isEmpty(config.searchBoxBgImg)) {
			BitmapDrawable bitmapDrawable = new BitmapDrawable(BitmapFactory.decodeResource(getResources(), UZResourcesIDFinder.getResDrawableID("mo_searchbar_bg")));
			mRelativeLayout.setBackgroundDrawable(bitmapDrawable);
		} else {
			BitmapDrawable bitmapDrawable = new BitmapDrawable(generateBitmap(config.searchBoxBgImg));
			mRelativeLayout.setBackgroundDrawable(bitmapDrawable);
		}

		if (config.cancel_bg_bitmap != null) {
			mTextView.setBackgroundDrawable(new BitmapDrawable(config.cancel_bg_bitmap));
		} else {
			mTextView.setBackgroundColor(config.cancel_bg_color);
		}

		mTextView.setTextSize(config.cancel_size);
		mTextView.setTextColor(config.cancal_color);

		LayoutParams params = new LayoutParams(UZUtility.dipToPix(config.searchBoxWidth), UZUtility.dipToPix(config.searchBoxHeight));
		params.setMargins(UZUtility.dipToPix(10), 0, 0, 0);

		params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		mEditText.setLayoutParams(params);

		WindowManager wm = this.getWindowManager();
		int width = wm.getDefaultDisplay().getWidth();

		double realWidth = width * 0.80;
		LayoutParams layoutParams = new LayoutParams((int) realWidth, UZUtility.dipToPix(config.searchBoxHeight));

		layoutParams.setMargins(UZUtility.dipToPix(5), 0, 0, 0);
		layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

		layoutParams.topMargin = UZUtility.dipToPix(8);
		layoutParams.bottomMargin = UZUtility.dipToPix(8);

		mRelativeLayout.setLayoutParams(layoutParams);

		double cancelRealWidth = width * 0.15;

		int space = (width - (int) realWidth - (int) cancelRealWidth - UZUtility.dipToPix(5)) / 2;
		
		int cancelWidth = (int)cancelRealWidth;
		
		if(config.cancel_width > 0){
			cancelWidth = config.cancel_width;
		}
		
		
		
		LayoutParams cancalTxtParam = new LayoutParams(cancelWidth, UZUtility.dipToPix(config.searchBoxHeight));

		cancalTxtParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		cancalTxtParam.addRule(RelativeLayout.CENTER_VERTICAL);

		cancalTxtParam.rightMargin = space;
		cancalTxtParam.leftMargin = space;
		
		if(config.cancel_marginR > 0){
			cancalTxtParam.rightMargin = config.cancel_marginR;
		}

		mTextView.setLayoutParams(cancalTxtParam);
		
		mTextView.setSingleLine();

		mListView.setBackgroundColor(config.list_bg_color);

		listSize = config.list_size;
		mCleanTextColor = config.clear_font_color;
		mCleanTextSize = config.clear_font_size;

		recordCount = config.historyCount;

		/**
		 * add clean list item
		 */
		int relativeLayoutCleanId = UZResourcesIDFinder.getResLayoutID("mo_searchbar_clean_item");
		relativeLayoutClean = (LinearLayout) View.inflate(getApplicationContext(), relativeLayoutCleanId, null);
		int tv_cleanId = UZResourcesIDFinder.getResIdID("tv_clean");
		mCleanTV = (TextView) relativeLayoutClean.findViewById(tv_cleanId);
		mCleanTV.setTextSize(mCleanTextSize);
		mCleanTV.setTextColor(mCleanTextColor);

		mCleanTV.setText(config.clearText);

		relativeLayoutClean.setBackgroundDrawable(addStateDrawable(config.clear_bg_color, config.clear_active_bg_color));
		list.clear();

		list.add(relativeLayoutClean);
		mPref = getSharedPreferences("history" + config.database, Context.MODE_PRIVATE);

		editor = mPref.edit();

		trimHistroyList(config.historyCount);

		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) mPref.getAll();
		if (map != null) {
			if (map.size() != 0) {
				for (int i = 1; i <= map.size(); i++) {

					int listview_item = UZResourcesIDFinder.getResLayoutID("mo_searchbar_listview_item");
					LinearLayout linearLayout = (LinearLayout) View.inflate(SearchBarActivity.this, listview_item, null);
					int tv_listId = UZResourcesIDFinder.getResIdID("tv_listview");
					TextView tv = (TextView) linearLayout.findViewById(tv_listId);
					tv.setTextSize(listSize);
					tv.setTextColor(config.list_color);

					linearLayout.setBackgroundDrawable(addStateDrawable(config.list_bg_color, config.list_item_active_bg_color));

					int itemBorderLineId = UZResourcesIDFinder.getResIdID("item_border_line");
					linearLayout.findViewById(itemBorderLineId).setBackgroundColor(config.list_border_color);

					for (Entry<String, String> iterable_element : map.entrySet()) {
						String key = iterable_element.getKey();
						if ((i + "").equals(key)) {
							tv.setText(iterable_element.getValue());
						}
					}
					list.addFirst(linearLayout);
				}
				id = map.size();
			}
		}

		adapter = new MyAdapter();
		mListView.setAdapter(adapter);
	}
	
	public static void cleanUp() {
		if (list != null && editor != null) {

			list.clear();
			adapter.notifyDataSetChanged();
			editor.clear();
			editor.commit();

			list.add(relativeLayoutClean);
			adapter.notifyDataSetChanged();

		}
	}

	public boolean isBlank(CharSequence cs) {
		int strLen;
		if ((cs == null) || ((strLen = cs.length()) == 0))
			return true;
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(cs.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	@SuppressLint("DefaultLocale")
	public Bitmap generateBitmap(String path) {
		String pathname = UZUtility.makeRealPath(path, Constans.mWidgetInfo);
		String sharePath;
		File file;
		try {
			if (pathname.contains("android_asset")) {
				int dotPosition = pathname.lastIndexOf('/');
				String ext = pathname.substring(dotPosition + 1, pathname.length()).toLowerCase();
				file = new File(getExternalCacheDir(), ext);
				sharePath = file.getAbsolutePath();
				InputStream input = UZUtility.guessInputStream(pathname);
				copy(input, file);
			} else if (pathname.contains("file://")) {
				sharePath = substringAfter(pathname, "file://");
			} else {
				sharePath = path;
			}
			InputStream input = UZUtility.guessInputStream(sharePath);
			return BitmapFactory.decodeStream(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String substringAfter(String str, String separator) {
		if (isEmpty(str)) {
			return str;
		}
		if (separator == null) {
			return "";
		}
		int pos = str.indexOf(separator);
		if (pos == -1) {
			return "";
		}
		return str.substring(pos + separator.length());
	}

	private void copy(InputStream inputStream, File output) throws IOException {
		OutputStream outputStream = null;
		try {
			if (!output.exists()) {
				output.createNewFile();
			}
			outputStream = new FileOutputStream(output);
			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} finally {
				if (outputStream != null) {
					outputStream.close();
				}
			}
		}
	}

	public boolean isEmpty(CharSequence cs) {
		return (cs == null) || (cs.length() == 0);
	}

	@Override
	protected void onPause() {
		super.onPause();
		clearText();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		isEntered = false;

		clearText();
		list.clear();
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return list.get(position);
		}
	}

	public void trimHistroyList(int historyCount) {

		ArrayList<String> tmpList = new ArrayList<String>();

		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) mPref.getAll();
		if (map != null) {
			if (map.size() != 0) {
				for (int i = 0; i <= map.size(); i++) {
					for (Entry<String, String> iterable_element : map.entrySet()) {
						String key = iterable_element.getKey();
						if ((i + "").equals(key)) {
							String txt = iterable_element.getValue();
							tmpList.add(txt);
						}
					}
				}
			}
		}

		int tmpSize = tmpList.size();
		if (config.historyCount > tmpSize) {
			return;
		}

		List<String> subList = tmpList.subList(tmpSize - config.historyCount, tmpSize);

		editor.clear();
		editor.commit();

		for (int i = 0; i < subList.size(); i++) {
			editor.putString(i + 1 + "", subList.get(i));
			editor.commit();
		}

	}

	private static boolean isEntered;

	public static void enter(Context context) {
		if (!isEntered) {

			isEntered = true;
			Intent intent = new Intent(context, SearchBarActivity.class);
			context.startActivity(intent);
		}
	}

	public static StateListDrawable addStateDrawable(int nomalColor, int pressColor) {
		StateListDrawable sd = new StateListDrawable();
		sd.addState(new int[] { android.R.attr.state_pressed }, new ColorDrawable(pressColor));
		sd.addState(new int[] { android.R.attr.state_focused }, new ColorDrawable(nomalColor));
		sd.addState(new int[] {}, new ColorDrawable(nomalColor));
		return sd;
	}

}
