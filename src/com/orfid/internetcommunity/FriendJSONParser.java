package com.orfid.internetcommunity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendJSONParser {

	List<Friend> listArray;
	
	public List<Friend> parse(JSONObject jObject) {
		
		JSONArray jFriends = null;
		try {			
			/** Retrieves all the elements */
			jFriends = jObject.getJSONArray("data");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return getFriends(jFriends);
	}
	
	
	private List<Friend> getFriends(JSONArray jFriends) {
		int friendCount = jFriends.length();
		List<Friend> friendList = new ArrayList<Friend>();
		Friend friend = null;

		for(int i=0; i<friendCount;i++) {
			try {
				friend = getFriend((JSONObject)jFriends.get(i));
				friendList.add(friend);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return friendList;
	}
	
	private Friend getFriend(JSONObject jFriend) {

		Friend friend = new Friend();
		String uid;
		String username;
		String photo;
		
		try {
			uid = jFriend.getString("uid");
			username = jFriend.getString("username");
			photo = jFriend.getString("photo");

			friend.setUid(uid);
			friend.setUsername(username);
			friend.setPhoto(photo);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return friend;
	}
}



