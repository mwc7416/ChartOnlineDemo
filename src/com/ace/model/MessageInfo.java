package com.ace.model;

import cn.bmob.v3.BmobObject;

public class MessageInfo extends BmobObject {
	
	private String friendName;
	private int friendIconId;
	private String recordTime;
	private String messageInfo;
	private int unreadMsg;
	
	public String getFriendName() {
		return friendName;
	}
	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}
	public int getFriendIconId() {
		return friendIconId;
	}
	public void setFriendIconId(int friendIconId) {
		this.friendIconId = friendIconId;
	}
	public String getRecordTime() {
		return recordTime;
	}
	public void setRecordTime(String recordTime) {
		this.recordTime = recordTime;
	}
	public String getMessageInfo() {
		return messageInfo;
	}
	public void setMessageInfo(String messageInfo) {
		this.messageInfo = messageInfo;
	}
	public int getUnreadMsg() {
		return unreadMsg;
	}
	public void setUnreadMsg(int unreadMsg) {
		this.unreadMsg = unreadMsg;
	}	
	
}
