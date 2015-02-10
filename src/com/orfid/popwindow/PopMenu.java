package com.orfid.popwindow;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.orfid.internetcommunity.R;

public class PopMenu {
	private ArrayList<Integer> itemList;
	private Context context;
	private PopupWindow popupWindow;
	private GridView gridView;
	private int friendReqCount = 0;
	private boolean isClearable = false;
	
	@SuppressWarnings("deprecation")
	public PopMenu(Context context) {
		this.context = context;

		itemList = new ArrayList<Integer>();
		itemList.add(R.drawable.menu1_selector);
		itemList.add(R.drawable.menu2_selector);
		itemList.add(R.drawable.menu3_selector);
		itemList.add(R.drawable.menu4_selector);
		itemList.add(R.drawable.menu5_selector);
		itemList.add(R.drawable.menu6_selector);

		View view = LayoutInflater.from(context).inflate(R.layout.function,
				null);

		// 设置 gridView
		gridView = (GridView) view.findViewById(R.id.gridView);
		gridView.setAdapter(new PopAdapter());
		gridView.setFocusableInTouchMode(true);
		gridView.setFocusable(true);

		popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景（很神奇的）
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
	}

	// 设置菜单项点击监听器
		public void setOnItemClickListener(OnItemClickListener listener) {
			gridView.setOnItemClickListener(listener);
		}

	// 下拉式 弹出 pop菜单 parent 右下角
	@SuppressWarnings("deprecation")
	public void showAsDropDown(View parent) {

		if (isClearable) friendReqCount = 0;
		
		popupWindow.showAsDropDown(parent, parent.getWidth(), 0);

		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// 刷新状态
		popupWindow.update();
	}

	// 隐藏菜单
	public void dismiss() {
		popupWindow.dismiss();
	}

	public int getFriendReqCount() {
		return friendReqCount;
	}

	public void setFriendReqCount(int friendReqCount) {
		this.friendReqCount = friendReqCount;
	}
	
	public void setIsClearable(boolean isClearable) {
		this.isClearable = isClearable;
	}

	public boolean getIsClearable() {
		return isClearable;
	}

	// 适配器
	private final class PopAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return itemList.size();
		}

		@Override
		public Object getItem(int position) {
			return itemList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.pomenu_item, null);
				holder = new ViewHolder();

				convertView.setTag(holder);

				holder.bt_item = (ImageView) convertView
						.findViewById(R.id.bt_item);
				holder.unread_msg_count = convertView.findViewById(R.id.unread_msg_count);
				holder.count = (TextView) convertView.findViewById(R.id.count);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.bt_item.setImageResource(itemList.get(position));
			if (friendReqCount > 0 && itemList.get(position) == R.drawable.menu1_selector) {
				holder.unread_msg_count.setVisibility(View.VISIBLE);
				holder.count.setText(friendReqCount+"");
			}
			return convertView;
		}

		private final class ViewHolder {
			ImageView bt_item;
			View unread_msg_count;
			TextView count;
		}
	}
}
