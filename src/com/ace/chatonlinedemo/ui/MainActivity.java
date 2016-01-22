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
		
		//������ʱ�����񣨵�λΪ�룩-���������̨�Ƿ���δ������Ϣ���еĻ���ȡ����
		//�������ü�����ȽϺ������͵�������Ҳ����ȥ����仰-ͬʱ����onDestory���������stopPollService����
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
		tv_title.setText("�Ự");
		img_plus.setVisibility(View.GONE);
		replaceFragment(mCommunication);
	}
	
	public void contacts(View view){
		currentTab = contacts;
		tv_title.setText("��ϵ��");
		img_plus.setVisibility(View.VISIBLE);
		replaceFragment(mContacts);
	}
	
	public void setting(View view){
		currentTab = settings;
		tv_title.setText("����");
		img_plus.setVisibility(View.GONE);
		replaceFragment(mSettings);
	}
	
	public void addFriend(View view){
		showToast("addFriend");
		StartIntent(FindFriendActivity.class);
	}
	
	NewBroadcastReceiver newReceiver;
	
	public void initNewMessageBroadCast(){
		//ע�������Ϣ�㲥
		newReceiver = new NewBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(BmobConfig.BROADCAST_NEW_MESSAGE);
		//���ȼ�����
		intentFilter.setPriority(3);
		registerReceiver(newReceiver, intentFilter);
	}
	
	public class NewBroadcastReceiver extends BroadcastReceiver{
		//����Ϣ�㲥������
		@Override
		public void onReceive(Context context, Intent intent) {
			//ˢ�½���
			refreshNewMsg(null);
			showToast("new message");
			//�ս�㲥
			abortBroadcast();
		}
	}
	
	TagBroadcastReceiver userReceiver;
	
	public void initTagMessageBroadCast(){
		// ע�������Ϣ�㲥
		userReceiver = new TagBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(BmobConfig.BROADCAST_ADD_USER_MESSAGE);
		//���ȼ�����
		intentFilter.setPriority(3);
		registerReceiver(userReceiver, intentFilter);
	}
	
	public class TagBroadcastReceiver extends BroadcastReceiver{
		/**
		 * ��ǩ��Ϣ�㲥������
		 */
		@Override
		public void onReceive(Context context, Intent intent) {
			BmobInvitation message = (BmobInvitation) intent.getSerializableExtra("invite");
			refreshInvite(message);
			abortBroadcast();			
		}
	}
	
	public void refreshNewMsg(BmobMsg message) {
		//������ʾ
	}

	public void refreshInvite(BmobInvitation message) {
		list.add(message);
		myApplication.setList_invite(list);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MyMessageReceiver.ehList.remove(this);// ȡ���������͵���Ϣ
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
		//ȡ����ʱ������
		BmobChat.getInstance(this).stopPollService();
	}
	
	private static long firstTime;
	/**
	 * ���������η��ؼ����˳�
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (firstTime + 2000 > System.currentTimeMillis()) {
			super.onBackPressed();
		} else {
			showToast("�ٰ�һ���˳�����");
		}
		firstTime = System.currentTimeMillis();
	}
	
}
