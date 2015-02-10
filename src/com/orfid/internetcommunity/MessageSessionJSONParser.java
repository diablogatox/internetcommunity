package com.orfid.internetcommunity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MessageSessionJSONParser {

	List<MessageSession> listArray;
	
	public List<MessageSession> parse(JSONObject jObject) {
		
		JSONArray jMessageSession = null;
		try {			
			/** Retrieves all the elements */
			jMessageSession = jObject.getJSONArray("sessions");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return getMessageSessions(jMessageSession);
	}
	
	
	private List<MessageSession> getMessageSessions(JSONArray jMessageSession) {
		int messageSessionCount = jMessageSession.length();
		List<MessageSession> messageSessionList = new ArrayList<MessageSession>();
		MessageSession MessageSession = null;

		for(int i=0; i<messageSessionCount;i++) {
			try {
				MessageSession = getMessageSession((JSONObject)jMessageSession.get(i));
				messageSessionList.add(MessageSession);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return messageSessionList;
	}
	
	private MessageSession getMessageSession(JSONObject jMessageSession) {

		MessageSession messageSession = new MessageSession();
		
		String id;
		String type;
		String newmsg;
		Friend[] users = null;
		List<Friend> list = new ArrayList<Friend>();
		
		try {
			id = jMessageSession.getString("sid");
			type = jMessageSession.getString("type");
			newmsg = jMessageSession.getString("newmsg");
			
			Message msg = null;
			if (!jMessageSession.getString("lastMessage").equals("[]")) {
				JSONObject msgObj = new JSONObject(jMessageSession.getString("lastMessage"));
				JSONObject userObj = new JSONObject(msgObj.getString("user"));
				msg = new Message(
				msgObj.getString("id"),
				msgObj.getString("sendtime"),
				msgObj.getString("text"),
				msgObj.getString("files"),
				new Friend(userObj.getString("uid"), userObj.getString("username"), userObj.getString("photo"))
				);
			}
			
			JSONArray jUsers = jMessageSession.getJSONArray("users");
			for (int i=0; i<jUsers.length(); i++) {
				JSONObject user = (JSONObject) jUsers.get(i);
				Friend f = new Friend(user.getString("uid"), user.getString("username"), user.getString("photo"));
				list.add(f);
			}

			users = list.toArray(new Friend[list.size()]);
			
			messageSession.setId(id);
			messageSession.setMessage(msg);
			messageSession.setNewmsg(newmsg);
			messageSession.setType(type);
			messageSession.setUsers(users);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return messageSession;
	}
}



