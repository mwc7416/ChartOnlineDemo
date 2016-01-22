package com.ace.chatonlinedemo.ui;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import cn.bmob.im.BmobChat;
import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.listener.FindListener;

import com.ace.chartonlinedemo.R;
import com.ace.chatonlinedemo.MyApplication;
import com.ace.model.Config;
import com.ace.utility.ChatSqlUtil;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class BaseActivity extends Activity {
	
	FragmentManager fragmentManager;
	FragmentTransaction fragmentTransaction;
	BmobUserManager UserManager;
	BmobChatManager ChatManager;
	ChatSqlUtil MySqlHelper;
	MyApplication myApplication;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		initBmob();
	}
	
	private void init() {
		UserManager = BmobUserManager.getInstance(this);
		ChatManager = BmobChatManager.getInstance(this);
		fragmentManager = getFragmentManager();
		MySqlHelper = ChatSqlUtil.getInstance(this);
		MySqlHelper.create();
		myApplication = (MyApplication) getApplicationContext();
	}
	
	public void addFragment(Fragment fragment){
		fragmentTransaction = fragmentManager.beginTransaction();	
		fragmentTransaction.add(R.id.container, fragment);
		fragmentTransaction.commit();
	}
	
	public void replaceFragment(Fragment fragment){
		fragmentTransaction = fragmentManager.beginTransaction();	
		fragmentTransaction.replace(R.id.container, fragment);
		fragmentTransaction.commit();
	}
	
	public void showToast(String str){
		Toast.makeText(this,str,0).show();
	}
	
	public void flagTag(String TAG,String str){
		Log.i(TAG, str);
	}
	
	public void initBmob(){
        //�����õ���ģʽ����Ϊtrue��ʱ�򣬻���logcat��BmobChat�����һЩ��־���������ͷ����Ƿ��������У��������˷��ش���Ҳ��һ����ӡ���������㿪���ߵ��ԣ���ʽ����Ӧע�ʹ˾䡣
		BmobChat.DEBUG_MODE = false;
		//BmobIM SDK��ʼ��--ֻ��Ҫ��һ�δ��뼴����ɳ�ʼ��
		BmobChat.getInstance(this).init(Config.applicationId);
	    //������ʱ�����񣨵�λΪ�룩-���������̨�Ƿ���δ������Ϣ���еĻ���ȡ����
	    //�������ü�����ȽϺ������͵�������Ҳ����ȥ����仰-ͬʱ����onDestory���������stopPollService����
		BmobChat.getInstance(this).startPollService(30);
	}
	
	@SuppressWarnings("rawtypes")
	public void StartIntent(Class class1){
		Intent intent = new Intent(this,class1);
		startActivity(intent);
	};
	
	public String String2UTF8(String str){
		//��String����UTF8����
		StringBuffer sb = new StringBuffer();
		sb.append(str);
		String result ="";
		String xmString = "";
		String xmlUTF8="";
		try {
			xmString = new String(sb.toString().getBytes("UTF-8"));
			xmlUTF8 = URLEncoder.encode(xmString,"UTF-8");
			result = xmlUTF8;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void imgBack(View view){
		this.finish();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MySqlHelper.close();
		  //ȡ����ʱ������
	    BmobChat.getInstance(this).stopPollService();
	}
	
	public void updateUsersInfo() {
        UserManager.queryCurrentContactList(new FindListener<BmobChatUser>() {
			
			@Override
			public void onSuccess(List<BmobChatUser> list_contacts) {
				myApplication.setList_contacts(list_contacts);
//				showToast(list_contacts.size()+"");
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				
			}
		});		
	}
	
	
}
