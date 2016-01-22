package com.ace.adapter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ace.chartonlinedemo.R;

import cn.bmob.im.bean.BmobChatUser;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailAdapter extends BaseAdapter {
	
	List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
	public LayoutInflater mInflater;
	Map<String, Object> map;
	
	public DetailAdapter(Context mContext,List<Map<String, Object>> list) {
		this.list = list;
		mInflater = LayoutInflater.from(mContext);
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressWarnings("unused")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (holder == null) {
			convertView = mInflater.inflate(R.layout.list_detial_item, null);
			holder = new ViewHolder();
			holder.textView_title = (TextView) convertView.findViewById(R.id.tv_friend_title);
			holder.textView_info = (TextView) convertView.findViewById(R.id.tv_friend_info);
			holder.imageView = (ImageView) convertView.findViewById(R.id.img_friend_icon);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		BmobChatUser user = new BmobChatUser();
 		
 		switch (position) {
 		
		case 0:
			holder.textView_title.setText("Í·Ïñ");
			holder.textView_info.setVisibility(View.GONE);
			break;
		case 1:
			map = new HashMap<String, Object>();
			map = list.get(position);
			holder.textView_title.setText("êÇ³Æ");
			holder.imageView.setVisibility(View.GONE);
			holder.textView_info.setText((CharSequence) map.get("info"));
			break;
		case 2:
			map = new HashMap<String, Object>();
			map = list.get(position);
			holder.textView_title.setText("ÕËºÅ");
			holder.imageView.setVisibility(View.GONE);
			holder.textView_info.setText((CharSequence) map.get("info"));
			break;
		case 3:
			map = new HashMap<String, Object>();
			map = list.get(position);
			holder.textView_title.setText("ÐÔ±ð");
			holder.textView_info.setText((CharSequence) map.get("info"));
			holder.imageView.setVisibility(View.GONE);
			break;

		default:
			break;
		}
		return convertView;
	}

	public class ViewHolder{
		public TextView textView_title;
		public TextView textView_info;
		public ImageView imageView;
	}
}
   