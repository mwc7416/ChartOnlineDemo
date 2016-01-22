package com.ace.chatonlinedemo;

import java.util.ArrayList;
import java.util.List;

import com.ace.model.SortModel;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.bean.BmobInvitation;
import android.app.Application;

public class MyApplication extends Application {
	
	private List<BmobInvitation> list_invite = new ArrayList<BmobInvitation>();
	private List<BmobChatUser> list_contacts = new ArrayList<BmobChatUser>();
	private SortModel sortModel = new SortModel();
	
	private static MyApplication myApplication;
	
	public static MyApplication getInstance(){
		return myApplication;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		myApplication = this;
	}
	
	public SortModel getSortModel() {
		return sortModel;
	}

	public void setSortModel(SortModel sortModel) {
		this.sortModel = sortModel;
	}

	public List<BmobChatUser> getList_contacts() {
		return list_contacts;
	}

	public void setList_contacts(List<BmobChatUser> list_contacts) {
		this.list_contacts = list_contacts;
	}

	public List<BmobInvitation> getList_invite() {
		return list_invite;
	}

	public void setList_invite(List<BmobInvitation> list_invite) {
		this.list_invite = list_invite;
	}
	
	
}
