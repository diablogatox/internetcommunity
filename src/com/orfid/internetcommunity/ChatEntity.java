package com.orfid.internetcommunity;

public class ChatEntity {

	private String userName;
	private String userImage;
	private String content;
//	private String chatTime;
	private boolean isComeMsg;
//	private boolean isNofityMsg;

	public String getUserImage() {
		return userImage;
	}
	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
//	public String getChatTime() {
//		return chatTime;
//	}
//	public void setChatTime(String chatTime) {
//		this.chatTime = chatTime;
//	}
	public boolean isComeMsg() {
		return isComeMsg;
	}
	public void setComeMsg(boolean isComeMsg) {
		this.isComeMsg = isComeMsg;
	}
//	public boolean isNofityMsg() {
//		return isNofityMsg;
//	}
//	public void setNofityMsg(boolean isNofityMsg) {
//		this.isNofityMsg = isNofityMsg;
//	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}