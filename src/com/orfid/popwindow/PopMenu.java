package com.orfid.popwindow;

import java.util.ArrayList;

import com.orfid.internetcommunity.R;

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

public class PopMenu {
	private ArrayList<Integer> itemList;
	private Context context;
	private PopupWindow popupWindow;
	private GridView gridView;
	
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

		// ���� gridView
		gridView = (GridView) view.findViewById(R.id.gridView);
		gridView.setAdapter(new PopAdapter());
		gridView.setFocusableInTouchMode(true);
		gridView.setFocusable(true);

		popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		// �����Ϊ�˵��������Back��Ҳ��ʹ����ʧ�����Ҳ�����Ӱ����ı�����������ģ�
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
	}

	// ���ò˵�����������
		public void setOnItemClickListener(OnItemClickListener listener) {
			gridView.setOnItemClickListener(listener);
		}

	// ����ʽ ���� pop�˵� parent ���½�
	@SuppressWarnings("deprecation")
	public void showAsDropDown(View parent) {

		popupWindow.showAsDropDown(parent, parent.getWidth(), 0);

		// ʹ��ۼ�
		popupWindow.setFocusable(true);
		// ����������������ʧ
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// ˢ��״̬
		popupWindow.update();
	}

	// ���ز˵�
	public void dismiss() {
		popupWindow.dismiss();
	}

	// ������
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

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.bt_item.setImageResource(itemList.get(position));
			return convertView;
		}

		private final class ViewHolder {
			ImageView bt_item;
		}
	}
}
