package com.mofang.pb;

public class Contacts {
	
	public Contacts(int icon, String name, String info, String py, boolean check) {
		// TODO Auto-generated constructor stub
		this.icon = icon;
		this.name = name;
		this.info = info;
		this.py = py;
		this.check = check;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public String getPy() {
		return py;
	}

	public void setPy(String py) {
		this.py = py;
	}
	
	public boolean getCheck() {
		return check;
	}
	
	public void setCheck(boolean check) {
		this.check = check;
	}

	private String name;
	private String info;
	private int icon;
	private String py; //首字母拼音
	@SuppressWarnings("unused")
	private boolean check;
}
