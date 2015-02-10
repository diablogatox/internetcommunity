package com.orfid.ic;

public class MessageSession {
	
	private String id;
	private String newmsg;
	private Friend[] users;
	private String type;
	private Message message;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getNewmsg() {
		return newmsg;
	}
	public void setNewmsg(String newmsg) {
		this.newmsg = newmsg;
	}
	public Friend[] getUsers() {
		return users;
	}
	public void setUsers(Friend[] users) {
		this.users = users;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}

}
