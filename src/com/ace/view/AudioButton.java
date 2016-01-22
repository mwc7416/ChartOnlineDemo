package com.ace.view;



import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.inteface.UploadListener;

import com.ace.chartonlinedemo.R;
import com.ace.model.ChatInfo;
import com.ace.utility.AudioUtil;
import com.ace.utility.ChatSqlUtil;
import com.ace.utility.CommonUtility;
import com.ace.utility.Constant;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

public class AudioButton extends Button {
	private static final String TAG = "AudioButton";
	
	private static final boolean CANCLEVOICE = true;
	private static final boolean CONTINUEVOICE = false; 
	public static boolean isCancleVoice = CONTINUEVOICE;
	private String fileName;
	private String targetUserId;
	private String selfId;
	
	public void setTargetUserId(String targetUserId) {
		this.targetUserId = targetUserId;
	}

	private VoiceDialog voiceDialog;
	private AudioUtil audioUtil;
	private CommonUtility mCommonUtility;
	
	private long downTime,upTime,touchTime;
	private	int voiceLevel[] = {R.drawable.chat_icon_voice1,R.drawable.chat_icon_voice2,
								R.drawable.chat_icon_voice3,R.drawable.chat_icon_voice4,
								R.drawable.chat_icon_voice5,R.drawable.chat_icon_voice6};

	public AudioButton(Context context) {
		this(context, null);
	}

	public AudioButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		voiceDialog = new VoiceDialog(context);
		audioUtil = new AudioUtil(BmobUserManager.getInstance(context).getCurrentUserName());
		selfId = BmobUserManager.getInstance(context).getCurrentUser().getObjectId();
		mCommonUtility = new CommonUtility(context);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			fileName = "record"+mCommonUtility.getNum();
			audioUtil.initAudio(fileName);
			voiceDialog.show();
			downTime = System.currentTimeMillis();
			audioUtil.start();
			break;
		case MotionEvent.ACTION_UP:
			try {
				audioUtil.stop();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			if (!isCancleVoice) {
				//完成语音事件
				upTime = System.currentTimeMillis();
				touchTime = upTime - downTime;
				showLog("触摸时间："+touchTime);
				if (touchTime < 600) {
					showLog("录制时间过短，不保存录制");
					voiceDialog.setDialogText("录音时间太短");
					voiceDialog.setDialogTextColor(R.color.red);
					voiceDialog.setDialogImageResouce(R.drawable.chat_icon_voice_short);
					Thread thread = new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								Thread.sleep(300);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							if (voiceDialog != null) {
								voiceDialog.dismiss();
							}
						}
					});
					thread.start();
				}else {
					voiceDialog.setDialogImageResouce(R.drawable.chat_icon_voice3);
					showLog("合理录制，可以保存");
					sendVoice();
				}
			}else {
				//取消语音事件
				voiceDialog.setDialogImageResouce(R.drawable.chat_icon_voice3);
				showLog("取消语音发送");
				voiceDialog.dismiss();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			int level = audioUtil.getVoiceLevel();
			voiceDialog.setDialogImageResouce(voiceLevel[level]);
			double y = event.getY();
			if (y<0) {
				isCancleVoice = CANCLEVOICE;
				voiceDialog.setDialogText("松开手指，取消发送");
				voiceDialog.setDialogTextColor(R.color.red);
			}else {
				isCancleVoice = CONTINUEVOICE;
				voiceDialog.setDialogText("手指上滑，取消发送");
				voiceDialog.setDialogTextColor(R.color.white);
			}
			break;	
		default:
			break;
		}
		return true;
	}
	
	private void sendVoice() {
		try {
			ChatInfo mChatInfo = new ChatInfo();
			mChatInfo.setFriendId(targetUserId);
			mChatInfo.setSelfId(selfId);
			mChatInfo.setFromWhere(ChatInfo.FROMSELF);
			mChatInfo.setChatType(ChatInfo.AUDIO);
			mChatInfo.setInfo(audioUtil.getPATH());
			mChatInfo.setMessageTime("2016");
			voiceDialog.dismiss();
			ChatSqlUtil.getInstance(getContext()).insertChatInfo(mChatInfo);
			BmobChatManager manager = BmobChatManager.getInstance(getContext());
			manager.sendVoiceMessage(Constant.targetUser, audioUtil.getPATH(),audioUtil.getVoiceLength(audioUtil.getPATH()),"voice",new UploadListener() {
				
				@Override
				public void onSuccess() {
					showLog("voice success!");
				}
				
				@Override
				public void onStart(BmobMsg message) {
					showLog("voice success! belong:"+message.getBelongUsername()+"extra:"+message.getExtra());
				}
				
				@Override
				public void onFailure(int code, String info) {
					showLog("failed! details:"+info);
				}
			});
			showLog("send voice");
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	private void showLog(String tag){
		Log.i(TAG, tag);
	}
	
}
