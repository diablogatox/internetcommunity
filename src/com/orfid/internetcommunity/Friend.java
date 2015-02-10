package com.orfid.internetcommunity;

public class Friend {

	private String uid;
	private String username;
	private String photo;
	private String sex;
	private String signature;
	private String birthday;
	private String latitude;
	private String longitude;
	
	public Friend(String uid, String username, String photo, String sex,
			String signature, String birthday, String latitude, String longitude) {
		super();
		this.uid = uid;
		this.username = username;
		this.photo = photo;
		this.sex = sex;
		this.signature = signature;
		this.birthday = birthday;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public Friend(String uid, String username, String photo) {
		super();
		this.uid = uid;
		this.username = username;
		this.photo = photo;
	}
	
	public Friend() {}

	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
}
