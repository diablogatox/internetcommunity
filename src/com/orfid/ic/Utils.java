package com.orfid.ic;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;

public class Utils {

	public static int getAge (long timestamp) {

		if (timestamp == 0) return 0;
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timestamp);
		int _year = cal.get(Calendar.YEAR);
		int _month = cal.get(Calendar.MONTH);
		int _day = cal.get(Calendar.DAY_OF_MONTH);

        GregorianCalendar cal2 = new GregorianCalendar();
        int y, m, d, a;         

        y = cal2.get(Calendar.YEAR);
        m = cal2.get(Calendar.MONTH);
        d = cal2.get(Calendar.DAY_OF_MONTH);
        cal2.set(_year, _month, _day);
        a = y - cal2.get(Calendar.YEAR);
        if ((m < cal2.get(Calendar.MONTH))
                        || ((m == cal2.get(Calendar.MONTH)) && (d < cal2
                                        .get(Calendar.DAY_OF_MONTH)))) {
                --a;
        }
        if(a < 0)
        	a = 0;
//                throw new IllegalArgumentException("Age < 0");
        return a;
	}
	
	public static int componentTimeToTimestamp(int year, int month, int day, int hour, int minute) {

	    Calendar c = Calendar.getInstance();
	    c.set(Calendar.YEAR, year);
	    c.set(Calendar.MONTH, month);
	    c.set(Calendar.DAY_OF_MONTH, day);
	    c.set(Calendar.HOUR, hour);
	    c.set(Calendar.MINUTE, minute);
	    c.set(Calendar.SECOND, 0);
//	    c.set(Calendar.MILLISECOND, 0);

	    return (int) (c.getTimeInMillis() / 1000L);
	}
	
	public static String covertTimestampToDate(long timeStamp){

		String dateStr = null;
	    try{
	        DateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
	        Date netDate = (new Date(timeStamp));
	        dateStr = sdf.format(netDate);
	    }
	    catch(Exception ex){
	        ex.printStackTrace();
	    }
	    return dateStr;
	}
	
//	public static long getAudioDuriation(String audioUrl) {
//		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//		retriever.setDataSource(audioUrl);
//		String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//		long timeInmillisec = Long.parseLong( time );
//		long duration = timeInmillisec / 1000;
//		long hours = duration / 3600;
//		long minutes = (duration - hours * 3600) / 60;
//		long seconds = duration - (hours * 3600 + minutes * 60);
//		
//		return seconds;
//	}
	
	public static MediaPlayer createNetAudio(String audioUrl) {
        MediaPlayer mp=new MediaPlayer();  
        try {  
            mp.setDataSource(audioUrl);  
        } catch (IllegalArgumentException e) {  
            return null;  
        } catch (IllegalStateException e) {  
            return null;  
        } catch (IOException e) {  
            return null;  
        }  
        return mp;  
    }
}
