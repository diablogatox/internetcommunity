package com.orfid.internetcommunity;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.Cinema;
import com.amap.api.services.poisearch.Dining;
import com.amap.api.services.poisearch.Hotel;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.poisearch.PoiSearch.SearchBound;
import com.amap.api.services.poisearch.Scenic;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.orfid.popwindow.PopMenu;

public class HomeActivity extends Activity implements OnMapClickListener,AMapLocationListener, LocationSource, OnPoiSearchListener, Runnable  {
	private ImageView iv_home_news;
	private ImageView iv_home_menu;
	private ImageView iv_home_back;
	private RelativeLayout rl_home;
	private ListView lv;
	private PopMenu circlePopMenu;
	public static int screenWeight, screenHeight;
	public static Double Longitude, Latitude;//经度，纬度
	private int searchType = 0;// 搜索类型
	
	private LocationManagerProxy locationManager;
	private MapView mapView;
	private AMap aMap;
	
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	
	private String[] searchItems = {"网吧"};
	private PoiSearch.Query query;// Poi查询条件类
	private LatLonPoint lp;
	private PoiSearch poiSearch;
	private PoiResult poiResult; // poi返回的结果
	private List<PoiItem> poiItems;// poi数据
	
	private MyAdapter adapter;
	private SharedPreferences sp;
	private String token;
	
	ImageLoader imageLoader;
	private DisplayImageOptions options;
	
	List<Bubble> bubbleItems = new ArrayList<Bubble>();
	List<Friend> nearbyUserItems = new ArrayList<Friend>();
	
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		screenWeight = getWindowManager().getDefaultDisplay().getWidth(); // 屏幕宽（像素，如：480px）
		screenHeight = getWindowManager().getDefaultDisplay().getHeight(); // 屏幕高（像素，如：800px）
		setContentView(R.layout.home);

		imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration
				.createDefault(HomeActivity.this));
        
		mapView = (MapView) findViewById(R.id.iv_home_map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams)mapView.getLayoutParams();
        mapView.getLayoutParams();
        lParams.width=LayoutParams.MATCH_PARENT;
        lParams.height=(int) (HomeActivity.screenHeight * 0.5);
        mapView.setLayoutParams(lParams);
        sp = this.getSharedPreferences("icsp", Context.MODE_WORLD_READABLE);
        token = sp.getString("token", "");
        Log.d("【token(test only)】===========>", token);
		init();
		locationInit();
		iv_home_news = (ImageView) findViewById(R.id.iv_home_news);
		iv_home_menu = (ImageView) findViewById(R.id.iv_home_menu);
		iv_home_back = (ImageView) findViewById(R.id.iv_home_back);
		rl_home = (RelativeLayout) findViewById(R.id.rl_home);
		circlePopMenu = new PopMenu(this);//实例化一个PopMenu对象
		circlePopMenu.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long id) {
				switch (position) {
				case 0://加好友
					startActivity(new Intent(HomeActivity.this,AddFriendsActivity.class));
					circlePopMenu.dismiss();
					break;
				case 1://发起群聊
					startActivity(new Intent(HomeActivity.this,SelectFriendsActivity.class));
					circlePopMenu.dismiss();
					break;
				case 2://冒泡
					startActivity(new Intent(HomeActivity.this,MaoPaoActivity.class));
					circlePopMenu.dismiss();
					break;
				case 3://活动
					circlePopMenu.dismiss();
					break;
				case 4://我的
					startActivity(new Intent(HomeActivity.this,PersonalActivity.class));
					circlePopMenu.dismiss();
					break;
				case 5://设置
					startActivity(new Intent(HomeActivity.this,SettingActivity.class));
					circlePopMenu.dismiss();
					break;
				}
			}
		});
		
		TextView emptyView = (TextView) findViewById(R.id.empty);
		lv = (ListView) findViewById(R.id.lv_home);
		lv.setEmptyView(emptyView);
		
		lv.setCacheColorHint(Color.TRANSPARENT);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("id==========>", id+"");
				String type = bubbleItems.get(position).getBubble_type();
				if (type.equals("0")) {
					Intent intent = new Intent();
				    intent.setClass(HomeActivity.this, MusicLyricActivity.class);
					intent.putExtra("content", bubbleItems.get(position).getBubble_content());
					intent.putExtra("time", bubbleItems.get(position).getUtime());
					startActivity(intent);
				} else if (type.equals("2")) {
					Intent intent2 = new Intent(HomeActivity.this, VoiceStartActivity.class);
					intent2.putExtra("audioUrl", bubbleItems.get(position).getBubble_content());
					startActivity(intent2);
				}
			}
			
		});
//		lv.setOnItemClickListener(new OnItemClickListener() {
//			
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				arg1.setBackgroundColor(Color.parseColor("#ffffff")); 
//			}
//		});

		//消息
		iv_home_news.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(HomeActivity.this,
						MessageActivity.class));
			}
		});
		//加好友、发起群聊、冒泡、活动、我的、设置
		iv_home_menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//菜单弹出，在rl_home的下面
				circlePopMenu.showAsDropDown(rl_home);
			}
		});
		//首页的返回键
		iv_home_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				view.setVisibility(View.GONE);
				LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams)mapView.getLayoutParams();
		        mapView.getLayoutParams();
		        lParams.width=LayoutParams.MATCH_PARENT;
		        lParams.height=(int) (HomeActivity.screenHeight * 0.45);
		        mapView.setLayoutParams(lParams);
				lv.setVisibility(View.VISIBLE);//显示listview
			}
		});
		
		new BubbleListTask().execute();
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
	}
	 /**      * amap添加一些事件监听器      */    
	private void setUpMap() {                   
		aMap.setOnMapClickListener(this);// 对amap添加单击地图事件监听器     
		aMap.moveCamera(CameraUpdateFactory.zoomTo(20)); // 设置地图的缩放级别
//		aMap.setLocationSource(this);// 设置定位监听
//		aMap.setMylocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false 
//		aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);// 设置定位的类型为 跟随模式 
//		// 自定义系统定位蓝点         
//		MyLocationStyle myLocationStyle = new MyLocationStyle();         
//		// 自定义定位蓝点图标         
//		myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.picture_my)); 
//		// 将自定义的 myLocationStyle 对象添加到地图上         
//		aMap.setMyLocationStyle(myLocationStyle); 
		
		// 自定义系统定位小蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.picture_my));// 设置小蓝点的图标
		myLocationStyle.strokeColor(Color.TRANSPARENT);
		myLocationStyle.radiusFillColor(Color.TRANSPARENT);
//		myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
//		myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
		// myLocationStyle.anchor(int,int)//设置小蓝点的锚点
//		myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false

	}
	/**      * 对单击地图事件回调      */    
	@Override    
	public void onMapClick(LatLng point) {    //point返回的是 经纬度     
//		tv_map_point.setText(point+"");
		iv_home_back.setVisibility(View.VISIBLE);//显示首页的返回键
        LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams)mapView.getLayoutParams();
        mapView.getLayoutParams();
        lParams.width=LayoutParams.MATCH_PARENT;
        lParams.height=LayoutParams.MATCH_PARENT;
        mapView.setLayoutParams(lParams);
		lv.setVisibility(View.GONE);//隱藏listview
	}
	
	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@SuppressWarnings("deprecation")
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		if (locationManager != null) {
			locationManager.removeUpdates(this);
			locationManager.destory();
		}
		locationManager = null;
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@SuppressWarnings("deprecation")
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
		if (locationManager != null) {
			locationManager.removeUpdates(this);
			locationManager.destory();
		}
		locationManager = null;
	}
	
	
	class MyAdapter extends ArrayAdapter<Bubble>{
		
		private List<Bubble> items;
		private Bubble objBean;
		
		public MyAdapter(Context context, int resource, List<Bubble> arrayList) {
			super(context, resource, arrayList);
			this.items = arrayList;
		}

		
		@Override
		public int getCount() {
			return items == null ? 0: items.size();
		}


		@Override
		public Bubble getItem(int position) {
			return items.get(position);
		}

		HashMap<Integer,View> lmap = new HashMap<Integer,View>();
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			PictureViewHolder viewHolder = null;
			if (lmap.get(position)==null) {
				viewHolder = new PictureViewHolder();
				convertView = LayoutInflater.from(HomeActivity.this).inflate(
						R.layout.home_friends, parent, false);
				viewHolder.iv_friends_pic = (ImageView) convertView
						.findViewById(R.id.iv_friends_pic);
				viewHolder.tv_friends_name = (TextView) convertView
						.findViewById(R.id.tv_friends_name);
				viewHolder.tv_music_content = (TextView) convertView
						.findViewById(R.id.tv_music_content);
				viewHolder.tv_distance = (TextView) convertView
						.findViewById(R.id.tv_distance);
				viewHolder.btn_voice = (Button) convertView
						.findViewById(R.id.btn_voice);
				lmap.put(position, convertView);  
				convertView.setTag(viewHolder);
			} else {
				convertView = lmap.get(position);  
				viewHolder = (PictureViewHolder) convertView.getTag();
			}

			objBean = items.get(position);
//			viewHolder.iv_friends_pic.setBackgroundResource(R.drawable.my_qq_pic);//头像
			if (!objBean.getPhoto().trim().equals("null"))
				imageLoader.displayImage(AppConstants.MAIN_DOMAIN + "/" + objBean.getPhoto(), viewHolder.iv_friends_pic,
						options, null);
			viewHolder.iv_friends_pic.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent(HomeActivity.this,
							HomeFriendsPicActivity.class);
					i.putExtra("uid", objBean.getUid());
					Log.d("uid======>", objBean.getUid());
//					startActivity(i);
				}
			});
			viewHolder.tv_friends_name.setText(objBean.getUsername());//名字
			viewHolder.tv_friends_name.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent(HomeActivity.this,
							HomeFriendsPicActivity.class);
					i.putExtra("uid", objBean.getUid());
					Log.d("uid======>", objBean.getUid());
//					startActivity(i);
				}
			});
			viewHolder.tv_distance.setText(500 + "m"); //距离
			// 在下面进行判断，并显示或隐藏歌词和语音，实现相应的功能
			
			if (objBean.getBubble_type().equals("0")) {
				viewHolder.tv_music_content.setText(objBean.getBubble_content()); // 歌词
//				viewHolder.tv_music_content.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						//显示歌词
//						Log.d("点击的是哪个====>", objBean.getUsername()+"<<<"+objBean.getBubble_content());
////					    Intent intent = new Intent();
////					    intent.setClass(HomeActivity.this, MusicLyricActivity.class);
////						intent.putExtra("content", objBean.getBubble_content());
////						intent.putExtra("time", objBean.getUtime());
////					    startActivity(intent);
//					}
//				});
			} else if (objBean.getBubble_type().equals("2")) {
				viewHolder.tv_music_content.setVisibility(View.GONE);
				viewHolder.btn_voice.setText(objBean.getDuration());
				viewHolder.btn_voice.setVisibility(View.VISIBLE);
//				viewHolder.btn_voice.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						// 语音
////						Log.d("音频时长=======>", objBean.getDuration());
////						startActivity(new Intent(HomeActivity.this,VoiceStartActivity.class));
//					}
//				});
			}
			
			
			return convertView;
		}
		public class PictureViewHolder {
			ImageView iv_friends_pic;
			TextView tv_friends_name;
			TextView tv_music_content;
			TextView tv_distance;
			Button btn_voice;
		}
		
	}

//	class MyAdapter extends BaseAdapter {
//
//		private List<PoiItem> list;
//		
//		public MyAdapter(List<PoiItem> list) {
//			this.list = list;
//		}
//		
//		public MyAdapter() {}
//		
//		@Override
//		public int getCount() {
//			return 5;
//		}
//
//		@Override
//		public Object getItem(int position) {
//			return null;
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return position;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			PictureViewHolder viewHolder = null;
//			if (convertView == null) {
//				viewHolder = new PictureViewHolder();
//				convertView = LayoutInflater.from(HomeActivity.this).inflate(
//						R.layout.home_friends, parent, false);
//				viewHolder.iv_friends_pic = (ImageView) convertView
//						.findViewById(R.id.iv_friends_pic);
//				viewHolder.tv_friends_name = (TextView) convertView
//						.findViewById(R.id.tv_friends_name);
//				viewHolder.tv_music_content = (TextView) convertView
//						.findViewById(R.id.tv_music_content);
//				viewHolder.tv_distance = (TextView) convertView
//						.findViewById(R.id.tv_distance);
//				viewHolder.btn_voice = (Button) convertView
//						.findViewById(R.id.btn_voice);
//				convertView.setTag(viewHolder);
//			} else {
//				viewHolder = (PictureViewHolder) convertView.getTag();
//			}
//
////			viewHolder.iv_friends_pic.setBackgroundResource(R.drawable.my_qq_pic);//头像
//			viewHolder.iv_friends_pic.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					startActivity(new Intent(HomeActivity.this,
//							HomeFriendsPicActivity.class));
//				}
//			});
//			viewHolder.tv_friends_name.setText("test");//名字
//			viewHolder.tv_friends_name.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					startActivity(new Intent(HomeActivity.this,
//							HomeFriendsPicActivity.class));
//				}
//			});
//			viewHolder.tv_distance.setText(500 + "m"); //距离
//			// 在下面进行判断，并显示或隐藏歌词和语音，实现相应的功能
//			viewHolder.tv_music_content.setText("她静悄悄的来过，她慢慢带走沉默。只是最后的承诺，还是没有带走了"); // 歌词
//			viewHolder.tv_music_content.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					//显示歌词
//				    Intent intent = new Intent();
//				    intent.putExtra("one", "她静悄悄的来过，她慢慢带走沉默。只是最后的承诺，还是没有带走了");
//				    intent.setClass(HomeActivity.this,MusicLyricActivity.class);
//				    startActivity(intent);
//				}
//			});
//			viewHolder.btn_voice.setVisibility(View.GONE);
//			viewHolder.btn_voice.setOnClickListener(new OnClickListener() {// ����
//
//				@Override
//				public void onClick(View v) {
//					// 语音
//					startActivity(new Intent(HomeActivity.this,VoiceStartActivity.class));
//				}
//			});
//			return convertView;
//		}
//
//		public class PictureViewHolder {
//			ImageView iv_friends_pic;
//			TextView tv_friends_name;
//			TextView tv_music_content;
//			TextView tv_distance;
//			Button btn_voice;
//		}
//
//	}
	/**
	 * 此方法已经废弃
	 */
	@Override
	public void onLocationChanged(Location arg0) {
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		
	}
	 /**
     * 初始化定位
     */
    @SuppressWarnings("deprecation")
	private void locationInit() {
		locationManager = LocationManagerProxy
				.getInstance(HomeActivity.this);
		// API定位采用GPS定位方式，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
		locationManager.requestLocationUpdates(
				LocationManagerProxy.GPS_PROVIDER, 2000, 10, this);
    }
    int flag = 1;
	/**
	 * gps定位回调方法
	 */
	@Override
	public void onLocationChanged(AMapLocation location) {
		if (location != null) {
			Double geoLat = location.getLatitude();
			Double geoLng = location.getLongitude();
			Longitude = geoLng;//获得经度
			Latitude = geoLat;//获得纬度
//			String str = ("定位成功:(" + geoLng + "," + geoLat + ")"
//					+ "\n精    度    :" + location.getAccuracy() + "米"
//					+ "\n定位方式:" + location.getProvider() + "\n定位时间:" 
//					+ AMapUtil.convertToTime(location.getTime())
//					);
//			myLocation.setText(str);
			
//			lp = new LatLonPoint(Latitude, Longitude);
			if (flag == 1) {
				new Thread(HomeActivity.this).start();
			}
			flag = 0;
//			doSearchQuery();
		}
		
		if (mListener != null && location != null) {
			mListener.onLocationChanged(location);// 显示系统小蓝点
		}
	}



	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			/*
			 * mAMapLocManager.setGpsEnable(false);
			 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
			 * API定位采用GPS和网络混合定位方式
			 * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
			 */
			mAMapLocationManager.requestLocationUpdates(
					LocationProviderProxy.AMapNetwork, 0, 10, this);
		}
	}

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
	}
	
	/**
	 * 开始进行poi搜索
	 */
	protected void doSearchQuery() {
		query = new PoiSearch.Query("", searchItems[0], "上海市");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
		query.setPageSize(10);// 设置每页最多返回多少条poiitem
		query.setPageNum(0);// 设置查第一页

		searchType = 0;

		switch (searchType) {
		case 0: {// 所有poi
			query.setLimitDiscount(false);
			query.setLimitGroupbuy(false);
		}
			break;
		case 1: {// 有团购
			query.setLimitGroupbuy(true);
			query.setLimitDiscount(false);
		}
			break;
		case 2: {// 有优惠
			query.setLimitGroupbuy(false);
			query.setLimitDiscount(true);
		}
			break;
		case 3: {// 有团购或者优惠
			query.setLimitGroupbuy(true);
			query.setLimitDiscount(true);
		}
			break;
		}

		if (lp != null) {
			poiSearch = new PoiSearch(this, query);
			poiSearch.setOnPoiSearchListener(this);
			poiSearch.setBound(new SearchBound(lp, 2000, true));//
			// 设置搜索区域为以lp点为圆心，其周围2000米范围
			/*
			 * List<LatLonPoint> list = new ArrayList<LatLonPoint>();
			 * list.add(lp);
			 * list.add(AMapUtil.convertToLatLonPoint(Constants.BEIJING));
			 * poiSearch.setBound(new SearchBound(list));// 设置多边形poi搜索范围
			 */
			poiSearch.searchPOIAsyn();// 异步搜索
		}
	}

	/**
	 * POI详情回调
	 */
	@Override
	public void onPoiItemDetailSearched(PoiItemDetail result, int rCode) {
//		dissmissProgressDialog();// 隐藏对话框
		if (rCode == 0) {
			if (result != null) {// 搜索poi的结果
//				if (detailMarker != null) {
					StringBuffer sb = new StringBuffer(result.getSnippet());
					if ((result.getGroupbuys() != null && result.getGroupbuys()
							.size() > 0)
							|| (result.getDiscounts() != null && result
									.getDiscounts().size() > 0)) {

						if (result.getGroupbuys() != null
								&& result.getGroupbuys().size() > 0) {// 取第一条团购信息
							sb.append("\n团购："
									+ result.getGroupbuys().get(0).getDetail());
						}
						if (result.getDiscounts() != null
								&& result.getDiscounts().size() > 0) {// 取第一条优惠信息
							sb.append("\n优惠："
									+ result.getDiscounts().get(0).getDetail());
						}
					} else {
						sb = new StringBuffer("地址：" + result.getSnippet()
								+ "\n电话：" + result.getTel() + "\n类型："
								+ result.getTypeDes());
					}
					// 判断poi搜索是否有深度信息
					if (result.getDeepType() != null) {
						sb = getDeepInfo(result, sb);
//						detailMarker.setSnippet(sb.toString());
					} else {
//						ToastUtil.show(PoiAroundSearchActivity.this,
//								"此Poi点没有深度信息");
					}
					
//					Toast.makeText(this, sb.toString(), Toast.LENGTH_LONG).show();
//				}

			} else {
//				ToastUtil
//						.show(PoiAroundSearchActivity.this, R.string.no_result);
			}
		} else if (rCode == 27) {
//			ToastUtil
//					.show(PoiAroundSearchActivity.this, R.string.error_network);
		} else if (rCode == 32) {
//			ToastUtil.show(PoiAroundSearchActivity.this, R.string.error_key);
		} else {
//			ToastUtil.show(PoiAroundSearchActivity.this,getString(R.string.error_other) + rCode);
		}
	}

	/**
	 * POI深度信息获取
	 */
	private StringBuffer getDeepInfo(PoiItemDetail result,
			StringBuffer sbuBuffer) {
		switch (result.getDeepType()) {
		// 餐饮深度信息
		case DINING:
			if (result.getDining() != null) {
				Dining dining = result.getDining();
				sbuBuffer
						.append("\n菜系：" + dining.getTag() + "\n特色："
								+ dining.getRecommend() + "\n来源："
								+ dining.getDeepsrc());
			}
			break;
		// 酒店深度信息
		case HOTEL:
			if (result.getHotel() != null) {
				Hotel hotel = result.getHotel();
				sbuBuffer.append("\n价位：" + hotel.getLowestPrice() + "\n卫生："
						+ hotel.getHealthRating() + "\n来源："
						+ hotel.getDeepsrc());
			}
			break;
		// 景区深度信息
		case SCENIC:
			if (result.getScenic() != null) {
				Scenic scenic = result.getScenic();
				sbuBuffer
						.append("\n价钱：" + scenic.getPrice() + "\n推荐："
								+ scenic.getRecommend() + "\n来源："
								+ scenic.getDeepsrc());
			}
			break;
		// 影院深度信息
		case CINEMA:
			if (result.getCinema() != null) {
				Cinema cinema = result.getCinema();
				sbuBuffer.append("\n停车：" + cinema.getParking() + "\n简介："
						+ cinema.getIntro() + "\n来源：" + cinema.getDeepsrc());
			}
			break;
		default:
			break;
		}
		return sbuBuffer;
	}

	/**
	 * POI搜索回调方法
	 */
	@Override
	public void onPoiSearched(PoiResult result, int rCode) {
//		Log.d("result=====>", result.toString());
//		Toast.makeText(this, result.toString(), Toast.LENGTH_LONG).show();
//		dissmissProgressDialog();// 隐藏对话框
		if (rCode == 0) {
			if (result != null && result.getQuery() != null) {// 搜索poi的结果
				if (result.getQuery().equals(query)) {// 是否是同一条
					poiResult = result;
					poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
//					adapter = new MyAdapter(poiItems);
//					lv.setAdapter(adapter);
//					Log.d("count", adapter.getCount()+"");
//					adapter.notifyDataSetChanged();
//					Log.d("test===>", poiItems.get(0).getTitle());
//					for(int i = 0;i<poiItems.size();i++){
////						Log.d("poiId====>", poiItems.get(i).getPoiId());
//						doSearchPoiDetail(poiItems.get(i).getPoiId());
//					}
//					
//					adapter = new MyAdapter(list);
//					Toast.makeText(this, poiItems.size()+"", Toast.LENGTH_LONG).show();
					
					List<SuggestionCity> suggestionCities = poiResult
							.getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
//					if (poiItems != null && poiItems.size() > 0) {
//						aMap.clear();// 清理之前的图标
//						poiOverlay = new PoiOverlay(aMap, poiItems);
//						poiOverlay.removeFromMap();
//						poiOverlay.addToMap();
//						poiOverlay.zoomToSpan();
//
//						nextButton.setClickable(true);// 设置下一页可点
//					} else if (suggestionCities != null
//							&& suggestionCities.size() > 0) {
//						showSuggestCity(suggestionCities);
//					} else {
//						ToastUtil.show(PoiAroundSearchActivity.this,
//								R.string.no_result);
//					}
				}
			} else {
//				ToastUtil
//						.show(PoiAroundSearchActivity.this, R.string.no_result);
			}
		} else if (rCode == 27) {
//			ToastUtil
//					.show(PoiAroundSearchActivity.this, R.string.error_network);
		} else if (rCode == 32) {
//			ToastUtil.show(PoiAroundSearchActivity.this, R.string.error_key);
		} else {
//			ToastUtil.show(PoiAroundSearchActivity.this,getString(R.string.error_other) + rCode);
		}
	}
	
	/**
	 * 查单个poi详情
	 * 
	 * @param poiId
	 */
	public void doSearchPoiDetail(String poiId) {
		if (poiSearch != null && poiId != null) {
			poiSearch.searchPOIDetailAsyn(poiId);
		}
	}

	@Override
	public void run() {
		URL url=null;
		String result = "";
		try {
			url = new URL(AppConstants.USER_GPS_LOCATION);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("POST");
			conn.setDoOutput(true);

			Writer writer = new OutputStreamWriter(conn.getOutputStream());

			String str = "token=" + token + "&latitude=" + Latitude + "&longitude=" + Longitude;
			writer.write(str);
			writer.flush();

			Reader is = new InputStreamReader(conn.getInputStream());

			StringBuilder sb = new StringBuilder();
			char c[] = new char[1024];
			int len=0;

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
			Log.d("TEST", "用户GPS定位信息JSON---" + result);
			JSONObject object = null;
			if (!result.equals("")) {
				try {
					object = new JSONObject(result);
//					Log.i("TEST", "登录信息token---" + object.getInt("token"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			switch (msg.what) {
			case 0x11:
				if (object != null) {
					try {
						if (1==object.getInt("status")) {
//							Toast.makeText(LoginMyActivity.this,object.getString("text"),Toast.LENGTH_SHORT).show();
//							startActivity(new Intent(LoginMyActivity.this,HomeActivity.class));
							// 加载附近用户列表
							new LoadNearbyUsersTask().execute();
						}else if(0==object.getInt("status")){
							Toast.makeText(HomeActivity.this,object.getString("text"),Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}
	};
	
	int distance = 2000;
	class LoadNearbyUsersTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			URL url=null;
			String result = "";
			try {
				url = new URL(AppConstants.FIND_USERS_BY_DISTANCE);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();

				conn.setRequestMethod("POST");
				conn.setDoOutput(true);

				Writer writer = new OutputStreamWriter(conn.getOutputStream());

				String str = "token=" + token + "&distance=" + distance;
				writer.write(str);
				writer.flush();

				Reader is = new InputStreamReader(conn.getInputStream());

				StringBuilder sb = new StringBuilder();
				char c[] = new char[1024];
				int len=0;

				while ((len = is.read(c)) != -1) {
					sb.append(c, 0, len);
				}
				result = sb.toString();

			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			Log.d("TEST", "查找用户根据距离JSON---" + result);
			JSONObject obj;
			try {
				obj = new JSONObject(result);
				if (1==obj.getInt("status")) {
//					Toast.makeText(HomeActivity.this,obj.getString("text"),Toast.LENGTH_SHORT).show();
					FriendJSONParser parser = new FriendJSONParser();
					nearbyUserItems = parser.parse(obj);
					for (Friend friend: nearbyUserItems) {
						MarkerOptions markerOption = new MarkerOptions();
						markerOption.position(new LatLng(Float.parseFloat(friend.getLatitude()), Float.parseFloat(friend.getLongitude())));
						markerOption.title(friend.getUsername()).snippet(friend.getUsername());
						markerOption.draggable(false);
//						markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon1));
						Log.d("photo=============ddddd===>", friend.getPhoto());
						if (!friend.getPhoto().trim().equals("null")) {
							imageLoader.loadImage(AppConstants.MAIN_DOMAIN + "/" + friend.getPhoto(), new ImageLoadingListener() {
	
								@Override
								public void onLoadingCancelled(String arg0,
										View arg1) {
									// TODO Auto-generated method stub
									
								}
	
								@Override
								public void onLoadingComplete(String arg0,
										View arg1, Bitmap loadedImage) {
									Log.d("image loading complete=====>", "yes");
									Log.d("arg0=====>", arg0);
//									markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon1));
//									markerOption.icon(BitmapDescriptorFactory.fromBitmap(loadedImage));
//									markerOption.icon(BitmapDescriptorFactory.fromBitmap(loadedImage));
									
								}
	
								@Override
								public void onLoadingFailed(String arg0, View arg1,
										FailReason arg2) {
									// TODO Auto-generated method stub
									
								}
	
								@Override
								public void onLoadingStarted(String arg0, View arg1) {
									// TODO Auto-generated method stub
									
								}
								
							});
						}
						
						aMap.addMarker(markerOption);
					}
				}else if(0==obj.getInt("status")){
					Toast.makeText(HomeActivity.this,obj.getString("text"),Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		
	}
	
	private class BubbleListTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			URL url=null;
			String result = "";
			try {
				url = new URL(AppConstants.BUBBLE_LIST);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();

				conn.setRequestMethod("POST");
				conn.setDoOutput(true);

				Writer writer = new OutputStreamWriter(conn.getOutputStream());

				String str = "token=" + token;
				writer.write(str);
				writer.flush();

				Reader is = new InputStreamReader(conn.getInputStream());

				StringBuilder sb = new StringBuilder();
				char c[] = new char[1024];
				int len=0;

				while ((len = is.read(c)) != -1) {
					sb.append(c, 0, len);
				}
				result = sb.toString();

			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			Log.d("TEST", "气泡列表JSON---" + result);
			JSONObject obj;
			try {
				obj = new JSONObject(result);
				if (1==obj.getInt("status")) {
					BubbleJSONParser parser = new BubbleJSONParser();
					bubbleItems = parser.parse(obj);
					adapter = new MyAdapter(HomeActivity.this, R.layout.home_friends, bubbleItems);
					lv.setAdapter(adapter);
//					Log.d("bubbleItemsSize=======>", bubbleItems.size()+"");
//					adapter.notifyDataSetChanged();
				}else if(0==obj.getInt("status")){
					Toast.makeText(HomeActivity.this,obj.getString("text"),Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		
	}
	
}
