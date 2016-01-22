package com.ace.chatonlinedemo.ui;

import cn.bmob.v3.listener.SaveListener;

import com.ace.chartonlinedemo.R;
import com.ace.model.MyUser;
import com.ace.utility.CommonUtility;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;      

public class RegisterActivity extends BaseActivity {
	EditText et_register_name;
	EditText et_register_key;
	EditText et_register_key_verify;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitivity_register);
		initResource();
	}
	
	private void initResource() {
		et_register_name = (EditText) findViewById(R.id.et_register_name);
		et_register_key = (EditText) findViewById(R.id.et_register_key);
		et_register_key_verify = (EditText) findViewById(R.id.et_register_key_verify);
	}
	
	private boolean isMatch(String username,String key,String key_verify){
		if ((username.length() == 0)|| (key.length() == 0)) {
			showToast("密码或账号不能为空");
			return false;
		}
		if (!key.equals(key_verify)) {
			showToast("两次输入的密码不一致");
			return false;
		}
		if ((username.length() < 6) || (key.length() < 6)) {
			showToast("密码或账号的长度过短");
			return false;
		}
		return true;
	}
	
	public void btregister(View view){
		String name = et_register_name.getText().toString().toLowerCase();
		String key = et_register_key.getText().toString();
		String key_verify = et_register_key_verify.getText().toString();
		if (isMatch(name, key, key_verify)) {
			final MyUser myUser = new MyUser();
			myUser.setUsername(name);
			myUser.setPassword(key);
			myUser.signUp(RegisterActivity.this, new SaveListener() {
				
				@Override
				public void onSuccess() {
					showToast("注册成功");
					UserManager.bindInstallationForRegister(myUser.getUsername());
					finish();
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					if (arg0 == 202) {
						showToast("用户名已经存在");
					}
					
				}
			});
		}
	}
}
