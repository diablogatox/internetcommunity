package com.orfid.internetcommunity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;

public class BubbleJSONParser {

	static MediaPlayer mp;
	List<Bubble> listArray;
	
	public List<Bubble> parse(JSONObject jObject) {
		
		JSONArray jBubbles = null;
		try {			
			jBubbles = jObject.getJSONArray("data");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return getBubbles(jBubbles);
	}
	
	
	private List<Bubble> getBubbles(JSONArray jBubbles) {
		int bubbleCount = jBubbles.length();
		List<Bubble> bubbleList = new ArrayList<Bubble>();
		Bubble Bubble = null;

		for(int i=0; i<bubbleCount;i++) {
			try {
				Bubble = getBubble((JSONObject)jBubbles.get(i));
				bubbleList.add(Bubble);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return bubbleList;
	}
	
	private Bubble getBubble(JSONObject jBubble) {

		final Bubble bubble = new Bubble();
		final String uid;
		final String username;
		final String photo;
		final String bubble_type;
		final String bubble_content;
		final String utime;
		String duration = null;
		
		try {
			JSONObject jUser = new JSONObject(jBubble.getString("user"));
			uid = jUser.getString("uid");
			username = jUser.getString("username");
			photo = jUser.getString("photo");
			bubble_type = jBubble.getString("bubble_type");
			bubble_content = jBubble.getString("bubble_content");
			utime = jBubble.getString("utime");
			if (bubble_type != null && bubble_type.equals("2")) {
				bubble.setUid(uid);
				bubble.setUsername(username);
				bubble.setPhoto(photo);
				bubble.setBubble_type(bubble_type);
				bubble.setBubble_content(bubble_content);
				bubble.setUtime(utime);
				bubble.setDuration("0");
				mp = Utils.createNetAudio(bubble_content);
				mp.prepareAsync();
				mp.setOnPreparedListener(new OnPreparedListener() {
					
					@Override
					public void onPrepared(MediaPlayer mp) {
						bubble.setDuration((mp.getDuration()/1000)+"");
						
					}
				});
//				duration = (mp.getDuration()/1000)+"";
				
			} else {
				bubble.setUid(uid);
				bubble.setUsername(username);
				bubble.setPhoto(photo);
				bubble.setBubble_type(bubble_type);
				bubble.setBubble_content(bubble_content);
				bubble.setUtime(utime);
			}

			
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return bubble;
	}
}



