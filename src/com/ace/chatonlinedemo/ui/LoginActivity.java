package com.ace.chatonlinedemo.ui;

import java.util.List;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;

import com.ace.chartonlinedemo.R;
import com.ace.chatonlinedemo.MyApplication;
import com.ace.model.MyUser;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends BaseActivity {
	EditText et_name;
	EditText et_key;
	MyUser myUser;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initResource();
	}
	
	private void initResource() {
		et_name = (EditText) findViewById(R.id.et_name);
		et_key = (EditText) findViewById(R.id.et_key);
		
	}

	public void login(View view){
		myUser = new MyUser();
		String name = et_name.getText().toString().toLowerCase();
		String key = et_key.getText().toString();
		myUser.setUsername(name);
		myUser.setPassword(key);
		UserManager.login(myUser, new SaveListener() {
			
			@Override
			public void onSuccess() {
				showToast("登陆成功");
				Log.i("smile","用户登陆成功");
				updateUsersInfo();
				StartIntent(MainActivity.class);
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				showToast(arg1);
			}
		});
	}
	
	public void register(View view){
		StartIntent(RegisterActivity.class);
	}
	
	private static long firstTime;
	/**
	 * 连续按两次返回键就退出
	 */
	@Override
	public void onBackPressed() {
		if (firstTime + 2000 > System.currentTimeMillis()) {
			super.onBackPressed();
		} else {
			showToast("再按一次退出程序");
		}
		firstTime = System.currentTimeMillis();
	}
}
