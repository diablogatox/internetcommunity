package com.mofang.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Utils {

//	private static final String SET_COOKIE = "Set-Cookie";
//    private static final String COOKIE_VALUE_DELIMITER = ";";
//    private static final char NAME_VALUE_SEPARATOR = '=';
//    private static final String COOKIE_PATH = "/";
//
//
//	private static SharedPreferences sp;
//	private static SharedPreferences.Editor editor;
//	
	
	public static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
		    while ((line = reader.readLine()) != null) {
		        sb.append(line + "\n");
		    }
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    try {
		        is.close();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
		return sb.toString();
	}
	
	public static String extractFileName (String fullName) {  
	      Pattern p = Pattern.compile(".*?([^\\\\/]+)$");  
	      Matcher m = p.matcher(fullName);  
	   
	      return (m.find()) ? m.group(1) : "";
	}

}
