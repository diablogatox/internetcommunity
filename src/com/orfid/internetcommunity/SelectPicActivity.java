package com.orfid.internetcommunity;

import java.io.File;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class SelectPicActivity extends Activity {
	private Button btn_take_photo, btn_pick_photo, btn_cancel;   
	private LinearLayout layout;
	SharedPreferences sp_pic_info;
	Editor ed_pic_info;
	
	private Uri photoUri;
	
	private final int PIC_FROM_CAMERA = 1;
	private final int PIC_FROM＿LOCALPHOTO = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_pic);
		
		btn_take_photo = (Button) this.findViewById(R.id.btn_take_photo);   
		btn_pick_photo = (Button) this.findViewById(R.id.btn_pick_photo);   
		btn_cancel = (Button) this.findViewById(R.id.btn_cancel);   
		layout=(LinearLayout)findViewById(R.id.pop_layout); 
		sp_pic_info = getSharedPreferences("pic", MODE_PRIVATE);
		ed_pic_info = sp_pic_info.edit();
		//添加选择窗口范围监听可以优先获取触点，即不再执行onTouchEvent()函数，点击其他地方时执行onTouchEvent()函数销毁Activity   
		layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
 
			}
		 });   
		 //取消   
		 btn_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});  
		 //从手机相册选择
		 btn_pick_photo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				doHandlerPhoto(PIC_FROM＿LOCALPHOTO);// 从相册中去获取
			}
		});   
		 //拍照
		 btn_take_photo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				doHandlerPhoto(PIC_FROM_CAMERA);// 用户点击了从照相机获取
			}
		});

	}
	//实现onTouchEvent触屏函数但点击屏幕时销毁本Activity   
	@Override  
	public boolean onTouchEvent(MotionEvent event){   
	    finish();   
	    return true;   
	} 
	/**
	 * 根据不同方式选择图片设置ImageView
	 * 
	 * @param type
	 *            0-本地相册选择，非0为拍照
	 */
	private void doHandlerPhoto(int type) {
		try {
			// 保存裁剪后的图片文件
			File pictureFileDir = new File(
					Environment.getExternalStorageDirectory(), "/upload");
			if (!pictureFileDir.exists()) {
				pictureFileDir.mkdirs();
			}
			File picFile = new File(pictureFileDir, "upload.jpeg");
			if (!picFile.exists()) {
				picFile.createNewFile();
			}
			photoUri = Uri.fromFile(picFile);
			Log.i("TEST", "photoUri-create-----" + photoUri);

			if (type == PIC_FROM＿LOCALPHOTO) {// 相册
				Intent intent = getCropImageIntent();
				startActivityForResult(intent, PIC_FROM＿LOCALPHOTO);
			} else {
				Intent cameraIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
				startActivityForResult(cameraIntent, PIC_FROM_CAMERA);
			}

		} catch (Exception e) {
			Log.i("HandlerPicError", "处理图片出现错误");
		}
	}

	/**
	 * 调用图片剪辑程序
	 */
	public Intent getCropImageIntent() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		setIntentParams(intent);
		return intent;
	}

	/**
	 * 设置公用参数
	 */
	private void setIntentParams(Intent intent) {
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 600);
		intent.putExtra("outputY", 600);
		intent.putExtra("noFaceDetection", true); // no face detection
		intent.putExtra("scale", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
	}
	/**
	 * 得到结果后返回给上一个页面
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PIC_FROM_CAMERA: // 拍照
			try {
				cropImageUriByTakePhoto();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case PIC_FROM＿LOCALPHOTO:// 相册
			try {
				if (photoUri != null) {
					Log.i("TEST", "start-----" + photoUri);
					Bitmap bitmap = decodeUriAsBitmap(photoUri);
//					Intent intent = new Intent();
//					intent.putExtra("bitmap", bitmap);
//					setResult(RESULT_OK, intent);
//					 SelectPicActivity.this.finish();
//					Log.i("TEST", "end-----" + photoUri);
					finish();
				} else {
					SelectPicActivity.this.finish();
					Log.i("TEST", "空photoUri-----" + photoUri);
				}
			} catch (Exception e) {
				Log.i("TEST", "异常-----" + photoUri);
				e.printStackTrace();
			}
			break;
		}
	}
	/**
	 * 将相机拍成的图片裁剪处理
	 */
	private void cropImageUriByTakePhoto() {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(photoUri, "image/*");
		setIntentParams(intent);
		startActivityForResult(intent, PIC_FROM＿LOCALPHOTO);
	}

	/**
	 *讲本地的图片转换成bitmap对象
	 * @param uri
	 * @return
	 */
	private Bitmap decodeUriAsBitmap(Uri uri) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(getContentResolver()
					.openInputStream(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

}
