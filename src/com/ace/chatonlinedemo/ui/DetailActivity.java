package com.ace.chatonlinedemo.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;





import com.ace.adapter.DetailAdapter;
import com.ace.chartonlinedemo.R;
import com.ace.chatonlinedemo.MyApplication;
import com.ace.model.SortModel;
import com.ace.utility.Constant;
import com.baidu.platform.comapi.map.s;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class DetailActivity extends BaseActivity {
	
	ListView list_detial;
	MyApplication myApplication;
	List<Map<String, Object>> list;
	Map<String, Object> map;
	SortModel sortModel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detial);
		initResource();
		initList();
	}

	private void initList() {
	    map = new HashMap<String, Object>();
		map.put("info", "");
		list.add(map);
		
	    map = new HashMap<String, Object>();
		map.put("info", sortModel.getName());
		list.add(map);
		
	    map = new HashMap<String, Object>();
		map.put("info", sortModel.getName());
		list.add(map);
		
	    map = new HashMap<String, Object>();
		map.put("info", "ÄÐ");
		list.add(map);
		list_detial.setAdapter(new DetailAdapter(this, list));
	}

	private void initResource() {
		myApplication = new MyApplication();
		list_detial = (ListView) findViewById(R.id.list_detials);
		sortModel = Constant.sortModel;
		list = new ArrayList<Map<String,Object>>();
	}
	
	public void beginChat(View view){
		StartIntent(ChatActivity.class);
	}
}
