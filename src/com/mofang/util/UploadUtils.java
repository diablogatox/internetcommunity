package com.mofang.util;


import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class UploadUtils {
	private static final String TAG = "uploadFile";
	private static final int TIME_OUT = 10*10000000; 
	private static final String CHARSET = "utf-8";
	public static final String SUCCESS="1";
	public static final String FAILURE="0";
	private static SharedPreferences sp;

	public static String uploadFile(Context context, File file,String RequestURL)
	{
		sp = context.getSharedPreferences("icsp", Context.MODE_WORLD_READABLE);
		
		String  BOUNDARY =  UUID.randomUUID().toString(); 
		String PREFIX = "--" , LINE_END = "\r\n"; 
		String CONTENT_TYPE = "multipart/form-data";
		
		try {
			URL url = new URL(RequestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(TIME_OUT);
			conn.setConnectTimeout(TIME_OUT);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Charset", CHARSET);
			conn.setRequestProperty("connection", "keep-alive");   
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY); 
//			conn.setRequestProperty("Cookie", "__lmwhg=" + sp.getString("__lmwhg", "") + 
//					"; __wklqj=" + sp.getString("__wklqj", "") + 
//					"; PHPSESSID=" + sp.getString("PHPSESSID", "") + 
//					"; tz=" + sp.getString("tz", "") + "");
			if(file!=null)
			{

				OutputStream outputSteam=conn.getOutputStream();
				
				DataOutputStream dos = new DataOutputStream(outputSteam);
				
				
				StringBuilder sbParam=new StringBuilder();
	            sbParam.append(PREFIX);
	            sbParam.append(BOUNDARY);
	            sbParam.append(LINE_END);
	            sbParam.append("Content-Disposition:form-data;name=\""+ "token"+"\""+LINE_END);
	            sbParam.append("Content-Transfer-Encoding: 8bit" + LINE_END);
	            sbParam.append(LINE_END);
	            sbParam.append(sp.getString("token", ""));
	            sbParam.append(LINE_END);
	            dos.write(sbParam.toString().getBytes());
	            
	            
	            
				StringBuffer sb = new StringBuffer();
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINE_END);
				
				sb.append("Content-Disposition: form-data; name=\"file\"; filename=\""+file.getName()+"\""+LINE_END);
				sb.append("Content-Type: application/octet-stream; charset="+CHARSET+LINE_END);
				sb.append(LINE_END);
				Log.d("sb====>", sb.toString());
				dos.write(sb.toString().getBytes());
				InputStream is = new FileInputStream(file);
				byte[] bytes = new byte[1024];
				int len = 0;
				while((len=is.read(bytes))!=-1)
				{
					dos.write(bytes, 0, len);
				}
				is.close();
				dos.write(LINE_END.getBytes());
				byte[] end_data = (PREFIX+BOUNDARY+PREFIX+LINE_END).getBytes();
				dos.write(end_data);
				dos.flush();

				int res = conn.getResponseCode();  
				Log.e(TAG, "response code:"+res);
				if(res==200)
				{
					return Utils.convertStreamToString(conn.getInputStream());
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return FAILURE;
	}
}