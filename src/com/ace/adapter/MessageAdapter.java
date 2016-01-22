package com.ace.adapter;

import java.util.List;

import com.ace.chartonlinedemo.R;
import com.ace.model.ChatInfo;
import com.ace.utility.AudioUtil;
import com.ace.utility.SmileyParser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MessageAdapter extends BaseAdapter implements OnClickListener {

	LayoutInflater mInflater;
	List<ChatInfo> list;
    SmileyParser parser ;  
    AudioUtil mAudioUtil;
	
	public MessageAdapter(Context context , List<ChatInfo> list,Callback callback) {
		mInflater = LayoutInflater.from(context);
		this.list = list;
		parser = new SmileyParser(context);
		mCallback = callback;
		mAudioUtil = new AudioUtil();
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("unused")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (viewHolder == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.list_message_item, null);
			initResource(convertView,viewHolder);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		ChatInfo chatInfo = new ChatInfo();
		chatInfo = list.get(position);
		showWhich(viewHolder,chatInfo.getFromWhere(),chatInfo.getChatType());
		if(chatInfo.getFromWhere() == ChatInfo.FROMFRIEND){
			switch (chatInfo.getChatType()) {
			case ChatInfo.TEXT:
				viewHolder.tv_friend_text.setText(parser.replace(chatInfo.getInfo()));
				break;
			case ChatInfo.PIC:
//				viewHolder.img_friend_pic.setImageResource(chatInfo.getPicId());
				break;
			case ChatInfo.AUDIO:
				viewHolder.bg_friend_voice.setOnClickListener(this);
				viewHolder.bg_friend_voice.setTag(chatInfo);
				float time = (float) (((double)mAudioUtil.getVoiceLength(chatInfo.getInfo()))/1000);
				viewHolder.tv_friend_voice.setText(time+"'");
				break;
			case ChatInfo.LOCATION:
				
				break;
			default:
				break;
			}
		}else if(chatInfo.getFromWhere() == ChatInfo.FROMSELF){
			switch (chatInfo.getChatType()) {
			case ChatInfo.TEXT:
				viewHolder.tv_self_text.setText(parser.replace(chatInfo.getInfo()));
				break;
			case ChatInfo.PIC:
				
				break;
			case ChatInfo.AUDIO:
				viewHolder.bg_self_voice.setOnClickListener(this);
				viewHolder.bg_self_voice.setTag(chatInfo);
				float time = (float) (((double)mAudioUtil.getVoiceLength(chatInfo.getInfo()))/1000);
				viewHolder.tv_self_voice.setText(time+"'");
				break;
			case ChatInfo.LOCATION:
				
				break;
			default:
				break;
			}
		}
		return convertView;
	}

	private void showWhich(ViewHolder viewHolder, int fromWhere, int chatType) {
		viewHolder.layout_friend.setVisibility(View.GONE);
		viewHolder.layout_self.setVisibility(View.GONE);
		if (fromWhere == ChatInfo.FROMFRIEND) {
			viewHolder.layout_friend.setVisibility(View.VISIBLE);
			
			viewHolder.bg_friend_text.setVisibility(View.GONE);
			viewHolder.bg_friend_pic.setVisibility(View.GONE);
			viewHolder.bg_friend_voice.setVisibility(View.GONE);
			viewHolder.tv_friend_voice.setVisibility(View.GONE);
			viewHolder.bg_friend_location.setVisibility(View.GONE);
			viewHolder.tv_friend_location.setVisibility(View.GONE);
			switch (chatType) {
			case ChatInfo.TEXT:
				viewHolder.bg_friend_text.setVisibility(View.VISIBLE);
				break;
			case ChatInfo.AUDIO:
				viewHolder.bg_friend_voice.setVisibility(View.VISIBLE);
				viewHolder.tv_friend_voice.setVisibility(View.VISIBLE);
				break;
			case ChatInfo.PIC:
				viewHolder.bg_friend_pic.setVisibility(View.VISIBLE);
				break;
			case ChatInfo.LOCATION:
				viewHolder.bg_friend_location.setVisibility(View.VISIBLE);
				viewHolder.tv_friend_location.setVisibility(View.VISIBLE);
				break;
			}
		}else if(fromWhere == ChatInfo.FROMSELF){
			viewHolder.layout_self.setVisibility(View.VISIBLE);
			
			viewHolder.bg_self_text.setVisibility(View.GONE);
			
			viewHolder.bg_self_pic.setVisibility(View.GONE);
			
			viewHolder.bg_self_voice.setVisibility(View.GONE);
			
			viewHolder.bg_self_location.setVisibility(View.GONE);
			switch (chatType) {
			case ChatInfo.TEXT:
				viewHolder.bg_self_text.setVisibility(View.VISIBLE);
				break;
			case ChatInfo.AUDIO:
				viewHolder.bg_self_voice.setVisibility(View.VISIBLE);
				break;
			case ChatInfo.PIC:
				viewHolder.bg_self_pic.setVisibility(View.VISIBLE);
				break;
			case ChatInfo.LOCATION:
				viewHolder.bg_self_location.setVisibility(View.VISIBLE);
				break;
			}
		}
	}

	private void initResource(View convertView, ViewHolder viewHolder) {
		viewHolder.layout_friend = (LinearLayout) convertView.findViewById(R.id.message_friend);
		
		viewHolder.img_friend_icon = (ImageView) convertView.findViewById(R.id.message_friend_icon_img);
		
		viewHolder.tv_friend_text =  (TextView) convertView.findViewById(R.id.message_friend_text_bt);
		viewHolder.bg_friend_text =  (LinearLayout) convertView.findViewById(R.id.message_friend_text);
		
		viewHolder.img_friend_pic = (ImageView) convertView.findViewById(R.id.message_friend_pic_img);
		viewHolder.bg_friend_pic =  (LinearLayout) convertView.findViewById(R.id.message_friend_pic);
		
		viewHolder.img_friend_voice = (ImageView) convertView.findViewById(R.id.message_friend_audio_img);
		viewHolder.tv_friend_voice = (TextView) convertView.findViewById(R.id.message_friend_audio_tv);
		viewHolder.bg_friend_voice =  (LinearLayout) convertView.findViewById(R.id.message_friend_audio);
		
		viewHolder.img_friend_location = (ImageView) convertView.findViewById(R.id.message_friend_location_img);
		viewHolder.tv_friend_location = (TextView) convertView.findViewById(R.id.message_friend_location_text);
		viewHolder.bg_friend_location =  (LinearLayout) convertView.findViewById(R.id.message_friend_location_bg);
		
		
		
		viewHolder.layout_self = (LinearLayout) convertView.findViewById(R.id.message_self);
		
		viewHolder.img_self_icon = (ImageView) convertView.findViewById(R.id.message_self_icon_img);
		
		viewHolder.tv_self_text =  (TextView) convertView.findViewById(R.id.message_self_text_bt);
		viewHolder.bg_self_text =  (LinearLayout) convertView.findViewById(R.id.message_self_text);
		
		viewHolder.img_self_pic = (ImageView) convertView.findViewById(R.id.message_self_pic_img);
		viewHolder.bg_self_pic =  (LinearLayout) convertView.findViewById(R.id.message_self_pic);
		
		viewHolder.img_self_voice = (ImageView) convertView.findViewById(R.id.message_self_audio_img);
		viewHolder.tv_self_voice = (TextView) convertView.findViewById(R.id.message_self_audio_text);
		viewHolder.bg_self_voice =  (LinearLayout) convertView.findViewById(R.id.message_self_audio_bg);
		
		viewHolder.img_self_location = (ImageView) convertView.findViewById(R.id.message_self_location_img);
		viewHolder.tv_self_location = (TextView) convertView.findViewById(R.id.message_self_location_text);
		viewHolder.bg_self_location =  (FrameLayout) convertView.findViewById(R.id.message_self_location);
	}

	@SuppressWarnings("unused")
	private ViewHolder viewHolder;
	
	public class ViewHolder{
		LinearLayout layout_friend;
		ImageView img_friend_icon;
		TextView tv_friend_text;
		LinearLayout bg_friend_text;
		ImageView img_friend_pic; 
		LinearLayout bg_friend_pic;
		ImageView img_friend_voice;
		TextView tv_friend_voice;
		LinearLayout bg_friend_voice;
		ImageView img_friend_location;
		TextView tv_friend_location;
		LinearLayout bg_friend_location;
		
		LinearLayout layout_self;
		ImageView img_self_icon;
		TextView tv_self_text;
		LinearLayout bg_self_text;
		ImageView img_self_pic; 
		LinearLayout bg_self_pic;
		ImageView img_self_voice;
		TextView tv_self_voice;
		LinearLayout bg_self_voice;
		ImageView img_self_location;
		TextView tv_self_location;
		FrameLayout bg_self_location;
	}
	
	public interface Callback{
		public void click(View view);
	}

	private Callback mCallback;
	
	@Override
	public void onClick(View v) {
		mCallback.click(v);
	}
}
