package com.ace.chatonlinedemo.ui;

import java.util.List;

import cn.bmob.im.BmobChatManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.PushListener;

import com.ace.chartonlinedemo.R;
import com.ace.view.ClearEditText;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FindFriendActivity extends BaseActivity {
	
	ClearEditText et_FriendID;
	ImageView Friend_icon;
	TextView Friend_id;
	LinearLayout Search_result;
	
	BmobChatUser bChatUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_findfriend);
		initView();
	}

	private void initView() {
		et_FriendID = (ClearEditText) findViewById(R.id.et_friend_id);
		Friend_icon = (ImageView) findViewById(R.id.friend_icon);
		Friend_id = (TextView) findViewById(R.id.friend_id);
		Search_result = (LinearLayout) findViewById(R.id.searchResult);
	}
	
	public void search(View view){
		String friendId = et_FriendID.getText().toString();
		friendId = String2UTF8(friendId);
		if (!friendId.equals("")) {
			UserManager.queryUserByName(friendId, new FindListener<BmobChatUser>() {
				
				@Override
				public void onSuccess(List<BmobChatUser> bmobList) {
					
					try {
						bChatUser = new BmobChatUser();
						bChatUser = bmobList.get(0);
						Search_result.setVisibility(View.VISIBLE);
						Friend_id.setText(bChatUser.getUsername());
//						showToast(bChatUser.getObjectId());
					} catch (Exception e) {
						showToast("用户不存在");
						e.printStackTrace();
					}
				}
				
				@Override
				public void onError(int errorCode, String errorStr) {
					showToast("errorCode"+errorCode+"/n errorStr"+errorStr);
				}
			});
		}
	}
	
	public void addfriend(View view){
		try {
			BmobChatManager.getInstance(this).sendTagMessage(BmobConfig.TAG_ADD_CONTACT,bChatUser.getObjectId(),new PushListener() {
				
				@Override
				public void onSuccess() {
					showToast("发送请求成功，等待对方验证!");
				}
				@Override
				public void onFailure(int arg0, String arg1) {
					showToast("发送请求失败，请重新添加!");
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void frienddetial(View view){
		StartIntent(FriendDetailActivity.class);
	}
	
}
