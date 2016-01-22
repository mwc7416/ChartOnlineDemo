package com.ace.chatonlinedemo.ui;


import java.util.ArrayList;
import java.util.List;

import cn.bmob.im.BmobChat;
import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.config.BmobConfig;

import com.ace.chartonlinedemo.R;
import com.ace.chatonlinedemo.MyApplication;
import com.ace.chatonlinedemo.MyMessageReceiver;
import com.ace.chatonlinedemo.ui.fragment.CommunicationFragment;
import com.ace.chatonlinedemo.ui.fragment.ContactsFragment;
import com.ace.chatonlinedemo.ui.fragment.SettingsFragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends BaseActivity {
	TextView tv_title;
	ImageView img_plus;
		
	CommunicationFragment mCommunication;
	ContactsFragment mContacts ;
	SettingsFragment mSettings;
	
	private static final int communication = 1;
	private static final int contacts = 2;
	private static final int settings = 3;
	
	private static int currentTab = communication;
	
	MyApplication myApplication;
	List<BmobInvitation> list;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		
		//开启定时检测服务（单位为秒）-在这里检测后台是否还有未读的消息，有的话就取出来
		//如果你觉得检测服务比较耗流量和电量，你也可以去掉这句话-同时还有onDestory方法里面的stopPollService方法
		BmobChat.getInstance(this).startPollService(10);
		initNewMessageBroadCast();
		initTagMessageBroadCast();
	}
	
	private void initView() {
		myApplication = MyApplication.getInstance();
		list = new ArrayList<BmobInvitation>();
		
		tv_title = (TextView) findViewById(R.id.tv_title);
		img_plus = (ImageView) findViewById(R.id.img_plus);
		
		mCommunication = new CommunicationFragment();
		mContacts = new ContactsFragment();
		mSettings = new SettingsFragment();
		
		addFragment(mCommunication);
	}

	public void communication(View view){
		currentTab = communication;
		tv_title.setText("会话");
		img_plus.setVisibility(View.GONE);
		replaceFragment(mCommunication);
	}
	
	public void contacts(View view){
		currentTab = contacts;
		tv_title.setText("联系人");
		img_plus.setVisibility(View.VISIBLE);
		replaceFragment(mContacts);
	}
	
	public void setting(View view){
		currentTab = settings;
		tv_title.setText("设置");
		img_plus.setVisibility(View.GONE);
		replaceFragment(mSettings);
	}
	
	public void addFriend(View view){
		showToast("addFriend");
		StartIntent(FindFriendActivity.class);
	}
	
	NewBroadcastReceiver newReceiver;
	
	public void initNewMessageBroadCast(){
		//注册接收消息广播
		newReceiver = new NewBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(BmobConfig.BROADCAST_NEW_MESSAGE);
		//优先级低于
		intentFilter.setPriority(3);
		registerReceiver(newReceiver, intentFilter);
	}
	
	public class NewBroadcastReceiver extends BroadcastReceiver{
		//新消息广播接收者
		@Override
		public void onReceive(Context context, Intent intent) {
			//刷新界面
			refreshNewMsg(null);
			showToast("new message");
			//终结广播
			abortBroadcast();
		}
	}
	
	TagBroadcastReceiver userReceiver;
	
	public void initTagMessageBroadCast(){
		// 注册接收消息广播
		userReceiver = new TagBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(BmobConfig.BROADCAST_ADD_USER_MESSAGE);
		//优先级低于
		intentFilter.setPriority(3);
		registerReceiver(userReceiver, intentFilter);
	}
	
	public class TagBroadcastReceiver extends BroadcastReceiver{
		/**
		 * 标签消息广播接收者
		 */
		@Override
		public void onReceive(Context context, Intent intent) {
			BmobInvitation message = (BmobInvitation) intent.getSerializableExtra("invite");
			refreshInvite(message);
			abortBroadcast();			
		}
	}
	
	public void refreshNewMsg(BmobMsg message) {
		//声音提示
	}

	public void refreshInvite(BmobInvitation message) {
		list.add(message);
		myApplication.setList_invite(list);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MyMessageReceiver.ehList.remove(this);// 取消监听推送的消息
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			unregisterReceiver(newReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			unregisterReceiver(userReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//取消定时检测服务
		BmobChat.getInstance(this).stopPollService();
	}
	
	private static long firstTime;
	/**
	 * 连续按两次返回键就退出
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (firstTime + 2000 > System.currentTimeMillis()) {
			super.onBackPressed();
		} else {
			showToast("再按一次退出程序");
		}
		firstTime = System.currentTimeMillis();
	}
	
}
