package com.orfid.ic;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TextView;

public class MyDatePickerDialog extends AlertDialog implements
		OnDateChangedListener {

	private static final String YEAR = "year";
	private static final String MONTH = "month";
	private static final String DAY = "day";

	private final DatePicker mDatePicker;//DatePicker
	private final OnDateSetListener mCallBack;//设置监听
	private View view;

	/**
	 * The callback used to indicate the user is done filling in the date.
	 */
	
	public interface OnDateSetListener {
		void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth);
	}

	public MyDatePickerDialog(Context context, OnDateSetListener callBack,
			int year, int monthOfYear, int dayOfMonth) {
		this(context, 0, callBack, year, monthOfYear, dayOfMonth);
	}

	// 判断是否是平板
	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	public MyDatePickerDialog(Context context, int theme,
			OnDateSetListener callBack, int year, int monthOfYear,
			int dayOfMonth) {
		super(context, theme);
		mCallBack = callBack;
		Context themeContext = getContext();
		LayoutInflater inflater = (LayoutInflater) themeContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// 注意，其实datepickerdialog就是把系统提供的日期选择器DatePicker包装�?��而已，所以只�?��实现自己�?��的布�?���?
		// 然后把DatePicker嵌套进去就可以了
		view = inflater.inflate(R.layout.date_picker_dialog, null);//找到布局
		mDatePicker = (DatePicker) view.findViewById(R.id.datePicker);
		mDatePicker.init(year, monthOfYear, dayOfMonth, this);  

		// 实现自己的标题和ok按钮
//		setTitle("请�?择年月日");
		setButton();
	}

	public void myShow() {
		// 自己实现show方法，主要是为了把setContentView方法放到show方法后面，否则会报错
		show();
		// 定义尺寸
		// 设置params 参数 主要是设置布局尺寸
		LayoutParams params = null;
		if (isTablet(getContext())) {//平板尺寸
			params = new LayoutParams(
					(int) (PersonalActivity.screenWeight * 0.6 + 0.5f),
					(int) (PersonalActivity.screenHeight * 0.4 + 0.5f));
		} else {//手机尺寸

			params = new LayoutParams(
					(int) (PersonalActivity.screenWeight + 0.5f ),
					(int) (PersonalActivity.screenHeight*0.99+0.5f));
//			params = new LayoutParams(
//					(int) (PersonalActivity.screenWeight * 0.83 + 0.5f),
//					(int) (PersonalActivity.screenHeight * 0.7 + 0.5f));
		}

		setContentView(view, params);

	}

	private void setButton() {
		// 获取自己定义的响应按钮并设置监听，直接调用构造时传进来的CallBack接口（为了省劲，没有自己写接口，直接用之前本类定义好的）同时关闭对话框�?
		TextView tv_date_pick_ok = (TextView) view.findViewById(R.id.tv_date_pick_ok);
		tv_date_pick_ok.setText("完成");
		tv_date_pick_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mCallBack != null) {
					mDatePicker.clearFocus();
					mCallBack.onDateSet(mDatePicker, mDatePicker.getYear(),
							mDatePicker.getMonth(), mDatePicker.getDayOfMonth());
				}
				dismiss();
			}
		});
		TextView tv_date_pick_cancel = (TextView) view.findViewById(R.id.tv_date_pick_cancel);
		tv_date_pick_cancel.setText("取消");
		tv_date_pick_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}
    //重写onDateChanged方法，在方法中再调用你要实现的方法setDateTime()
	public void onDateChanged(DatePicker view, int year, int month, int day) {
		mDatePicker.init(year, month, day, null);
	}
	

	/**
	 * Gets the {@link DatePicker} contained in this dialog.
	 * 
	 * @return The calendar view.
	 */
	public DatePicker getDatePicker() {
		return mDatePicker;
	}

	public void updateDate(int year, int monthOfYear, int dayOfMonth) {
		mDatePicker.updateDate(year, monthOfYear, dayOfMonth);
	}

	@Override
	public Bundle onSaveInstanceState() {
		Bundle state = super.onSaveInstanceState();
		state.putInt(YEAR, mDatePicker.getYear());
		state.putInt(MONTH, mDatePicker.getMonth());
		state.putInt(DAY, mDatePicker.getDayOfMonth());
		return state;
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		int year = savedInstanceState.getInt(YEAR);
		int month = savedInstanceState.getInt(MONTH);
		int day = savedInstanceState.getInt(DAY);
		mDatePicker.init(year, month, day, this);
	}
}
