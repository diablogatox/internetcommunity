package com.orfid.internetcommunity;

import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PersonalActivity extends Activity implements OnClickListener,Runnable{
	private ImageView personal_back;
	private ImageView iv_personal_pic;
	private ImageButton ib_icon_signature;
	private ImageButton ib_personal_arrow1;
	private ImageButton ib_personal_arrow2;
	private ImageButton ib_personal_arrow3;
	private RelativeLayout rl_personal3;
	private RelativeLayout rl_personal4;
	private RelativeLayout rl_personal5;
	private ImageButton ib_add_game_yuan2;
	private TextView tv_personal_nickname;
	private TextView tv_personal_sex;
	private TextView tv_personal_age;
	private TextView tv_personal_signature;
	private RelativeLayout rl_personal7;
	public static int screenWeight, screenHeight;
	private GridView gv_personal;
	SharedPreferences preferences;
	Editor editor;
	SharedPreferences sp_sexy_info;
	Editor ed_sexy_info;
	SharedPreferences sp_nickname_info;
	Editor ed_nickname_info;
	SharedPreferences sp_birthday_info;
	Editor ed_birthday_info;
	
	int year1,month1,day1;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		screenWeight = getWindowManager().getDefaultDisplay().getWidth(); // ��Ļ�����أ��磺480px��
		screenHeight = getWindowManager().getDefaultDisplay().getHeight(); // ��Ļ�ߣ����أ��磺800px��
		setContentView(R.layout.personal);
		
		findID();
		inintEditor();
		gv_personal.setAdapter(new MyAdapter());
		gv_personal.setFocusable(false);
		
		new Thread(PersonalActivity.this).start();
	}
	@Override
	public void run() {
		URL url;
		String result = null;
		try {
			url = new URL("http://sww.yxkuaile.com/user/SaveInfo");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("POST");
			conn.setDoOutput(true);

			Writer writer = new OutputStreamWriter(conn.getOutputStream());

			writer.flush();

			Reader is = new InputStreamReader(conn.getInputStream());

			StringBuilder sb = new StringBuilder();
			char c[] = new char[1024];
			int len;

			while ((len = is.read(c)) != -1) {
				sb.append(c, 0, len);
			}
			result = sb.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}

		Message msg = handler.obtainMessage();
		msg.what = 0x11;
		msg.obj = result;
		msg.sendToTarget();
	}
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			Log.i("TEST", "�����û���Ϣ����---" + result);
			JSONObject object = null;
			if (!result.equals("")) {
				try {
					object = new JSONObject(result);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			switch (msg.what) {
			case 0x11:
				if (object != null) {
					try {
						if (1==object.getInt("status")) {
							Toast.makeText(PersonalActivity.this,
									object.getString("text"),
									Toast.LENGTH_SHORT).show();
						}else if(0==object.getInt("status")){
							Toast.makeText(PersonalActivity.this,object.getString("text"),Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}
	};
	
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return 5;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int arg0, View convertView, ViewGroup parent) {
			PictureViewHolder viewHolder = null;
			if (convertView == null) {
				viewHolder = new PictureViewHolder();
				convertView = LayoutInflater.from(PersonalActivity.this).inflate(
						R.layout.gridview_hf, parent, false);
				viewHolder.tv_game_bg = (TextView) convertView
						.findViewById(R.id.tv_game_bg);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (PictureViewHolder) convertView.getTag();
			}
//			viewHolder.tv_game_bg.setText("Ӣ������");
			return convertView;
		}
		public class PictureViewHolder{
			TextView tv_game_bg;
		}
	}
	private void inintEditor() {
		preferences = getSharedPreferences("user", MODE_PRIVATE);
		editor = preferences.edit();
		
		sp_sexy_info = getSharedPreferences("sexy", MODE_PRIVATE);
		ed_sexy_info = sp_sexy_info.edit();
		
		sp_nickname_info = getSharedPreferences("nick", MODE_PRIVATE);
		ed_nickname_info = sp_nickname_info.edit();
		
		sp_birthday_info = getSharedPreferences("birthday", MODE_PRIVATE);
		ed_birthday_info = sp_birthday_info.edit();
	}
	@Override
	protected void onResume() {
		if(preferences.getString("user_sign", "")==null||preferences.getString("user_sign", "").equals("")||preferences.getString("user_sign", "").equals("null")){
			tv_personal_signature.setText("�༭����ǩ��");
			tv_personal_signature.setTextColor(Color.parseColor("#55000000"));
		}else{
			tv_personal_signature.setText(preferences.getString("user_sign", ""));
			tv_personal_signature.setTextColor(Color.parseColor("#000000"));
		}
		if("1".equals(sp_sexy_info.getString("nan", ""))){
			tv_personal_sex.setText("��");
		}else {
			tv_personal_sex.setText("Ů");
		}
		if(sp_nickname_info.getString("nickname", "")==null||sp_nickname_info.getString("nickname", "").equals("")||sp_nickname_info.getString("nickname", "").equals("null")){
			tv_personal_nickname.setText("");
		}else{
			tv_personal_nickname.setText(sp_nickname_info.getString("nickname", ""));
		}
		tv_personal_age.setText(sp_birthday_info.getString("birthday1", ""));
		
		Bitmap bitmap =getDiskBitmap();
		iv_personal_pic.setImageBitmap(bitmap);
		super.onResume();
	}
	private Bitmap getDiskBitmap() {
		Bitmap bitmap = null;
		try {
			File pictureFileDir = new File(
					Environment.getExternalStorageDirectory(), "/upload");
			if (!pictureFileDir.exists()) {
				pictureFileDir.mkdirs();
			}
			File picFile = new File(pictureFileDir, "upload.jpeg");
			if (!picFile.exists()) {//����ļ������ڣ�������Ŀ��ԴͼƬ
				bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.my_qq_pic);
			}else{//����ļ����ڼ���SD��ͼƬ
				String pathString=picFile.getAbsolutePath();//�ļ��ľ���·��
				File file = new File(pathString);
				if (file.exists()) {
					bitmap = BitmapFactory.decodeFile(pathString);
				}
			}
			
		} catch (Exception e) {
			
		}
		return bitmap;
	}
	@Override
	public void onClick(View arg0) {
		
		switch (arg0.getId()) {
		case R.id.personal_back://�����˳�
			writeBirthdayData();
			finish();
			break;
		case R.id.iv_personal_pic://�༭����ͷ��
			Intent intent = new Intent(PersonalActivity.this,SelectPicActivity.class);
			startActivityForResult(intent, RESULT_OK);
			break;
		case R.id.ib_icon_signature://�༭����ǩ��
			startActivity(new Intent(PersonalActivity.this,MaoPaoActivity.class));
			break;
		case R.id.ib_personal_arrow1://�༭�ǳ�
			startActivity(new Intent(PersonalActivity.this,NickNameActivity.class));
			break;
		case R.id.rl_personal3://�༭�ǳ�
			startActivity(new Intent(PersonalActivity.this,NickNameActivity.class));
			break;
		case R.id.ib_personal_arrow2://�༭�Ա�
			Intent intent1 = new Intent(PersonalActivity.this,SexyActivity.class);
			startActivity(intent1);
			break;
		case R.id.rl_personal4://�༭�Ա�
			Intent intent2 = new Intent(PersonalActivity.this,SexyActivity.class);
			startActivity(intent2);
			break;
		case R.id.ib_personal_arrow3://�༭����
			showBirthDayDialog();
			break;
		case R.id.rl_personal5://�༭����
			showBirthDayDialog();
			break;
		case R.id.rl_personal7://�����Ϸ
			startActivity(new Intent(PersonalActivity.this,AddGamesActivity.class));
			break;
		case R.id.ib_add_game_yuan2://�����Ϸ
			startActivity(new Intent(PersonalActivity.this,AddGamesActivity.class));
			break;
		}
	
	}
	/**
	 * �������ڼ�ʱ��ѡ��Ի��� ����һ��
	 */
	protected Calendar showBirthDayDialog() {
		Calendar c = Calendar.getInstance();

		MyDatePickerDialog mPickerDialog = new MyDatePickerDialog(
				this, R.style.MyDialogStyleBottom, 
				new MyDatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						Log.i("TEST", "��---"+year);
						Log.i("TEST", "��---"+(monthOfYear+1));
						Log.i("TEST", "��---"+dayOfMonth);
						//��������
						tv_personal_age.setText(year+"."+(monthOfYear+1)+"."+dayOfMonth);
						year1=year;
						month1=monthOfYear;
						day1=dayOfMonth;
					}
				}, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH));
		mPickerDialog.myShow();
		return c;
	}
	private void findID() {
		personal_back = (ImageView) findViewById(R.id.personal_back);
		iv_personal_pic = (ImageView) findViewById(R.id.iv_personal_pic);
		ib_icon_signature = (ImageButton) findViewById(R.id.ib_icon_signature);
		ib_personal_arrow1 = (ImageButton) findViewById(R.id.ib_personal_arrow1);
		ib_personal_arrow2 = (ImageButton) findViewById(R.id.ib_personal_arrow2);
		ib_personal_arrow3 = (ImageButton) findViewById(R.id.ib_personal_arrow3);
		rl_personal3 = (RelativeLayout) findViewById(R.id.rl_personal3);
		rl_personal4 = (RelativeLayout) findViewById(R.id.rl_personal4);
		rl_personal5 = (RelativeLayout) findViewById(R.id.rl_personal5);
		ib_add_game_yuan2 = (ImageButton) findViewById(R.id.ib_add_game_yuan2);
		tv_personal_nickname = (TextView) findViewById(R.id.tv_personal_nickname);
		tv_personal_signature = (TextView) findViewById(R.id.tv_personal_signature);
		tv_personal_sex = (TextView) findViewById(R.id.tv_personal_sex);
		tv_personal_age = (TextView) findViewById(R.id.tv_personal_age);
		rl_personal7 = (RelativeLayout) findViewById(R.id.rl_personal7);
		gv_personal = (GridView) findViewById(R.id.gv_personal);
		personal_back.setOnClickListener(this);
		iv_personal_pic.setOnClickListener(this);
		ib_icon_signature.setOnClickListener(this);
		ib_personal_arrow1.setOnClickListener(this);
		ib_personal_arrow2.setOnClickListener(this);
		ib_personal_arrow3.setOnClickListener(this);
		rl_personal3.setOnClickListener(this);
		rl_personal4.setOnClickListener(this);
		rl_personal5.setOnClickListener(this);
		rl_personal7.setOnClickListener(this);
		ib_add_game_yuan2.setOnClickListener(this);
	}
	//������д�뱾��
	public void writeBirthdayData(){
		ed_birthday_info.clear();
		ed_birthday_info.commit();
			
		ed_birthday_info.putString("birthday1",year1+"."+(month1+1)+"."+day1);
			
		ed_birthday_info.commit();
	}
	
}
