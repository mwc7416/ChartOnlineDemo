package com.ace.chatonlinedemo.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.im.BmobUserManager;

import com.ace.chartonlinedemo.R;
import com.ace.utility.CommonUtility;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class SettingsFragment extends Fragment {
	
	CommonUtility mUtility ;
	
	LinearLayout settings;
	ListView list_top,list_bottom;
	List<Map<String, Object>> top_list,bottom_list;
	String str_title_top[] = {"个人资料","黑名单"},str_title_bottom[] = {"接收新消息通知","声音","震动"};
	
	Button bt_exit;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		settings = (LinearLayout) inflater.inflate(R.layout.frg_settings, container,false);
		initResource();
		initListTop();
		initListBottom();
		myListener();
		return settings;
	}
	
	private void myListener() {
		bt_exit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				logout();
				getActivity().finish();
			}
		});
	}

	private void initListBottom() {
		bottom_list = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < str_title_bottom.length; i++) {
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("title", str_title_bottom[i]);
			bottom_list.add(map);
		}
		SimpleAdapter adapter = new SimpleAdapter(getActivity(), bottom_list, 
				R.layout.list_setting_bottom_item, new String[]{"title"}, new int[]{R.id.tv_settings_title_bottom});
		list_bottom.setAdapter(adapter);
	}

	private void initListTop() {
		top_list = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < str_title_top.length; i++) {
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("title", str_title_top[i]);
			if (i == 0) {
				map.put("name", BmobUserManager.getInstance(getActivity()).getCurrentUser().getUsername());
			}else {
				map.put("name", "");
			}
			top_list.add(map);
		}
		SimpleAdapter adapter = new SimpleAdapter(getActivity(), top_list, 
				R.layout.list_setting_top_item, new String[]{"title","name"}, new int[]{R.id.tv_settings_title,R.id.tv_settings_username});
		list_top.setAdapter(adapter);
	}
	
	private void initResource() {
		list_top = (ListView) settings.findViewById(R.id.list_settings_top);
		list_bottom = (ListView) settings.findViewById(R.id.list_settings_bottom);
		bt_exit = (Button) settings.findViewById(R.id.bt_exit);
		mUtility = new CommonUtility(getActivity());
	}
	
	public void logout(){
		BmobUserManager.getInstance(getActivity()).logout();
	}
	
}
