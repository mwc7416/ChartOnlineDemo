package com.ace.adapter;

import java.util.List;
import java.util.Map;

import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.config.BmobConfig;

import com.ace.chartonlinedemo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class FriendManagerAdapter extends BaseAdapter implements OnClickListener{

	public static final String TAG = "FriendManagerAdapter";
	public List<BmobInvitation> list;
	public Callback mCallback;
	public LayoutInflater mInflater;
	
	
	public interface Callback{
		//自定义接口，用户回调按钮点击事件到Activity
		public void click(View view);
	}
	
	public FriendManagerAdapter(Context context,List<BmobInvitation> list,Callback callback) {
		this.list = list;
		mInflater = LayoutInflater.from(context);
		mCallback = callback;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_request_item, null);
			holder = new ViewHolder();
			holder.textView = (TextView) convertView.findViewById(R.id.list_request_item_id);
			holder.button = (Button) convertView.findViewById(R.id.bt_agree);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		BmobInvitation invitation = list.get(position);
		int status = invitation.getStatus();
		holder.textView.setText(invitation.getFromname());
		if (status == BmobConfig.INVITE_ADD_NO_VALIDATION||status==BmobConfig.INVITE_ADD_NO_VALI_RECEIVED) {
			holder.button.setOnClickListener(this);
			holder.button.setTag(position);
		}else if(status==BmobConfig.INVITE_ADD_AGREE){
			holder.button.setVisibility(View.GONE);
			
		}
		return convertView;
	}

	@Override
	public void onClick(View v) {
		//响应按钮点击事件，调用子定义接口
		mCallback.click(v);
	}
	
	public class ViewHolder{
		public TextView textView;
		public Button button;
	}
}
