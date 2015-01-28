package com.orfid.internetcommunity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.media.MediaPlayer;

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

		Bubble bubble = new Bubble();
		String uid;
		String username;
		String photo;
		String bubble_type;
		String bubble_content;
		String utime;
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
				mp = Utils.createNetAudio(bubble_content);
				try {
					mp.prepare();
					duration = (mp.getDuration()/1000)+"";
//					mp.release();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}

			bubble.setUid(uid);
			bubble.setUsername(username);
			bubble.setPhoto(photo);
			bubble.setBubble_type(bubble_type);
			bubble.setBubble_content(bubble_content);
			bubble.setUtime(utime);
			bubble.setDuration(duration);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return bubble;
	}
}



