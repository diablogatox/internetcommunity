package com.orfid.internetcommunity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyJSONParser {

	List<FriendRequest> listArray;
	
	public List<FriendRequest> parse(JSONObject jObject) {
		
		JSONArray jFriendRequests = null;
		try {			
			/** Retrieves all the elements */
			jFriendRequests = jObject.getJSONArray("data");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return getFriendRequests(jFriendRequests);
	}
	
	
	private List<FriendRequest> getFriendRequests(JSONArray jFriendRequests) {
		int friendRequestCount = jFriendRequests.length();
		List<FriendRequest> friendRequestList = new ArrayList<FriendRequest>();
		FriendRequest club = null;

		/** Taking each club, parses and adds to list object */
		for(int i=0; i<friendRequestCount;i++) {
			try {
				club = getFriendRequest((JSONObject)jFriendRequests.get(i));
				friendRequestList.add(club);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return friendRequestList;
	}
	
	private FriendRequest getFriendRequest(JSONObject jFriendRequest) {

		FriendRequest friendRequest = new FriendRequest();
		String msgid = "";
		String username = "";
		String text= "";
		String photo = "";
		String type = "";
		String action = "";
		String signature = "";
		
		try {
			msgid = jFriendRequest.getString("id");
			type = jFriendRequest.getString("type");
			text = jFriendRequest.getString("text");
			JSONObject user = jFriendRequest.getJSONObject("user");
			username = user.getString("username");
			photo = user.getString("photo");
			signature = user.getString("signature");
			action = jFriendRequest.getString("action");
			

			friendRequest.setMsgid(Integer.parseInt(msgid));
			friendRequest.setType(Integer.parseInt(type));
			friendRequest.setText(text);
			friendRequest.setUsername(username);
			friendRequest.setAction(Integer.parseInt(action));
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return friendRequest;
	}
}



