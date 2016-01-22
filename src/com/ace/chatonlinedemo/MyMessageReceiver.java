package com.ace.chatonlinedemo;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.ace.model.ChatInfo;
import com.ace.utility.ChatSqlUtil;
import com.ace.utility.CommonUtility;

import cn.bmob.im.BmobUserManager;
import cn.bmob.im.config.BmobConstant;
import cn.bmob.im.inteface.EventListener;
import cn.bmob.im.util.BmobJsonUtil;
import cn.bmob.im.util.BmobLog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyMessageReceiver extends BroadcastReceiver {

	public static ArrayList<EventListener> ehList = new ArrayList<EventListener>();
	private String tag,message,targetId,fromId,time;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String json = intent.getStringExtra("msg");
		BmobLog.i("ÊÕµ½µÄmessage=", json);
		Log.i("AudioButton", json);
		resolveMsg(context,json);
	}

	private void resolveMsg(final Context context,String json) {
		JSONObject jo ;
		try {
			jo = new JSONObject(json);
			tag = BmobJsonUtil.getString(jo, BmobConstant.PUSH_KEY_EXTRA);
			message = BmobJsonUtil.getString(jo, BmobConstant.PUSH_KEY_CONTENT);
			targetId = BmobJsonUtil.getString(jo, BmobConstant.PUSH_KEY_TOID);
			fromId = BmobJsonUtil.getString(jo, BmobConstant.PUSH_KEY_TARGETID);
			time = BmobJsonUtil.getString(jo, BmobConstant.PUSH_KEY_MSGTIME);
			if (targetId.equals(BmobUserManager.getInstance(context).getCurrentUser().getObjectId())) {
				if (tag.equals("message")) {
					ChatInfo recChatInfo = new ChatInfo();
					recChatInfo.setChatType(ChatInfo.TEXT);
					recChatInfo.setFromWhere(ChatInfo.FROMFRIEND);
					recChatInfo.setSelfId(targetId);
					recChatInfo.setFriendId(fromId);
					recChatInfo.setMessageTime(time);
					recChatInfo.setInfo(message);
					ChatSqlUtil.getInstance(context).insertChatInfo(recChatInfo);
				}else if (tag.equals("voice")) {
					Log.i("AudioButton", message);
					int pos = message.lastIndexOf("&");
					message = message.substring(0, pos);
					final String uri = message;
					Thread thread = new Thread(new Runnable() {
						@Override
						public void run() {
							CommonUtility mCommonUtility = new CommonUtility();
							mCommonUtility.downloadFile(uri, ChatInfo.AUDIO);
							ChatInfo recChatInfo = new ChatInfo();
							recChatInfo.setChatType(ChatInfo.AUDIO);
							recChatInfo.setFromWhere(ChatInfo.FROMFRIEND);
							recChatInfo.setSelfId(targetId);
							recChatInfo.setFriendId(fromId);
							recChatInfo.setMessageTime(time);
							recChatInfo.setInfo(mCommonUtility.getPATH());
							ChatSqlUtil.getInstance(context).insertChatInfo(recChatInfo);
						}
					});
					thread.start();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}		
	}

}
