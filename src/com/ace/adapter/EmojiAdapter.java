package com.ace.adapter;

import com.ace.chartonlinedemo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class EmojiAdapter extends BaseAdapter {
	LayoutInflater inflater;

	private Integer[] emojis = {R.drawable.ue056,R.drawable.ue057,R.drawable.ue058,R.drawable.ue059,R.drawable.ue105,
			R.drawable.ue106,R.drawable.ue107,R.drawable.ue108,R.drawable.ue401,R.drawable.ue402,R.drawable.ue403,
			R.drawable.ue404,R.drawable.ue405,R.drawable.ue406,R.drawable.ue407,R.drawable.ue408,R.drawable.ue409,
			R.drawable.ue40a,R.drawable.ue40b,R.drawable.ue40c,R.drawable.ue40d,R.drawable.ue40e,R.drawable.ue40f,
			R.drawable.ue410,R.drawable.ue411,R.drawable.ue412,R.drawable.ue413,R.drawable.ue414,R.drawable.ue415,
			R.drawable.ue416,R.drawable.ue417,R.drawable.ue418,R.drawable.ue41f,R.drawable.ue421
		};
	
	public EmojiAdapter(Context context) {
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return emojis.length;
	}

	@Override
	public Object getItem(int position) {
		return emojis[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public int getIconID(int position){
		return emojis[position];
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		holder = null;
		if (holder == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.grid_emoji_item, null);
			holder.imageView = (ImageView) convertView.findViewById(R.id.img_emoji);
			convertView.setTag(holder);
		}else {
			convertView.getTag();
		}
		holder.imageView.setImageResource(emojis[position]);
		return convertView;
	}
	private ViewHolder holder;
	
	public class ViewHolder{
		ImageView imageView;
	}

}
