package com.orfid.ic;

public class Message {
	private String id;
	private String sendtime;
	private String text;
	private String file;
	private Friend user;
	public Message(String id, String sendtime, String text, String file, Friend user) {
		this.id = id;
		this.sendtime = sendtime;
		this.text = text;
		this.file = file;
		this.user = user;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSendtime() {
		return sendtime;
	}
	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public Friend getUser() {
		return user;
	}
	public void setUser(Friend user) {
		this.user = user;
	}
}
