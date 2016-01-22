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
        //可设置调试模式，当为true的时候，会在logcat的BmobChat下输出一些日志，包括推送服务是否正常运行，如果服务端返回错误，也会一并打印出来。方便开发者调试，正式发布应注释此句。
		BmobChat.DEBUG_MODE = false;
		//BmobIM SDK初始化--只需要这一段代码即可完成初始化
		BmobChat.getInstance(this).init(Config.applicationId);
	    //开启定时检测服务（单位为秒）-在这里检测后台是否还有未读的消息，有的话就取出来
	    //如果你觉得检测服务比较耗流量和电量，你也可以去掉这句话-同时还有onDestory方法里面的stopPollService方法
		BmobChat.getInstance(this).startPollService(30);
	}
	
	@SuppressWarnings("rawtypes")
	public void StartIntent(Class class1){
		Intent intent = new Intent(this,class1);
		startActivity(intent);
	};
	
	public String String2UTF8(String str){
		//将String进行UTF8编码
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
		  //取消定时检测服务
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
