package com.ace.model;

import cn.bmob.v3.BmobObject;

@SuppressWarnings("serial")
public class ChatInfo extends BmobObject {
	//消息來源
	public static final int FROMFRIEND = 5;
	public static final int FROMSELF = 6;
	//消息類型
	public static final int TEXT = 1;
	public static final int PIC = 2;
	public static final int AUDIO = 3;
	public static final int LOCATION = 4;
	
	String friendId;
	String selfId;
	
	int fromWhere;
	int chatType;
	String MessageTime;
	String Info;
	
	
	public String getInfo() {
		return Info;
	}
	public void setInfo(String info) {
		Info = info;
	}
	public String getFriendId() {
		return friendId;
	}
	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}
	public String getSelfId() {
		return selfId;
	}
	public void setSelfId(String selfId) {
		this.selfId = selfId;
	}
	public int getFromWhere() {
		return fromWhere;
	}
	public void setFromWhere(int fromWhere) {
		this.fromWhere = fromWhere;
	}
	public int getChatType() {
		return chatType;
	}
	public void setChatType(int chatType) {
		this.chatType = chatType;
	}
	public String getMessageTime() {
		return MessageTime;
	}
	public void setMessageTime(String messageTime) {
		MessageTime = messageTime;
	}

}
