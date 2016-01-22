package com.ace.chatonlinedemo.ui;

import java.util.List;

import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.v3.listener.UpdateListener;

import com.ace.adapter.FriendManagerAdapter;
import com.ace.adapter.FriendManagerAdapter.Callback;
import com.ace.chartonlinedemo.R;
import com.ace.chatonlinedemo.MyApplication;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class NewFriendManagerActivity extends BaseActivity implements OnItemClickListener,Callback {
	ListView list_request;
//	List<Map<String,Object>> list;	
	List<BmobInvitation> list;
	MyApplication myApplication;
//	String request_name[] = {"小明","Smith"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_findfriend_request);
		initView();
		initList();
	}
	
	
	
	private void initList() {
		list = myApplication.getList_invite();
		list_request.setAdapter(new FriendManagerAdapter(this, list, this));
		list_request.setOnItemClickListener(this);
		
	}
	
	private void initView() {
		list_request = (ListView) findViewById(R.id.list_friend_request);
		myApplication = (MyApplication) getApplicationContext();
	}



	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		showToast(position+"");
	}



	@Override
	public void click(View view) {
		final View myView = view;
		BmobInvitation message = list.get((Integer) view.getTag());
		BmobUserManager.getInstance(this).agreeAddContact(message, new UpdateListener() {
			@Override
			public void onSuccess() {
				showToast("添加成功" );
				myView.setVisibility(View.GONE);
			}
			@Override
			public void onFailure(int arg0, String arg1) {
				showToast("添加失败" );
			}
		});
	}	
	
}
