package com.ace.chatonlinedemo.ui;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.im.bean.BmobMsg;

import com.ace.adapter.EmojiAdapter;
import com.ace.adapter.MessageAdapter;
import com.ace.adapter.MessageAdapter.Callback;
import com.ace.chartonlinedemo.R;
import com.ace.model.ChatInfo;
import com.ace.utility.AudioUtil;
import com.ace.utility.Constant;
import com.ace.utility.SmileyParser;
import com.ace.view.AudioButton;

import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ChatActivity extends BaseActivity implements Callback {
	
	private Button bt_voice;
	private EditText et_keyboard;
	private ImageView img_event;
	private GridView gv_emoji;
	private LinearLayout layout_message;
	private LinearLayout layout_add;
	private ListView list_message;
	private TextView tv_title;
	private AudioButton mAudioButton;
	
	private EmojiAdapter emojiAdapter;
	private MessageAdapter messageAdapter;
	
	private static final int VOICE = 1;
	private static final int KEYBOARD = 2;
	private static final int KEYBOARDPRESS = 3;
	
	private static int currentMode = KEYBOARD;
	
	private List<ChatInfo> list;
	private String SelfID ;
	private String FriendName;
	private String FriendID;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				messageAdapter = new MessageAdapter(ChatActivity.this, list,ChatActivity.this);
				list_message.setAdapter(messageAdapter);
				try {
					list_message.setSelection(list.size()-1);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 2:
				messageAdapter.notifyDataSetChanged();
				list_message.setSelection(list.size()-1);
				break;
			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		initView();
		initList();
		initGrid();
		initEvent();
	}

	@SuppressWarnings("static-access")
	private void initList() {
		getContentResolver().registerContentObserver(MySqlHelper.CONTENT_URI , true , cob);
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				list = new ArrayList<ChatInfo>();
				list = getChatInfo(SelfID, FriendID);
				mHandler.sendEmptyMessage(1);
			}
		});
		thread.start();
	}


	private void initEvent() {
		gv_emoji.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				setEmoji2Edit(position);
				currentMode = KEYBOARDPRESS;
				img_event.setBackground(getResources().getDrawable(R.drawable.send));
			}
		});
		et_keyboard.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (gv_emoji.getVisibility() == View.VISIBLE) {
					gv_emoji.setVisibility(View.GONE);
				}
			}
		});
		layout_message.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				gv_emoji.setVisibility(View.GONE);
				layout_add.setVisibility(View.GONE);
			}
		});
	}

	protected void setEmoji2Edit(int id) {
        SmileyParser parser = new SmileyParser(this);  
        String text = parser.mSmileyTexts[id];  
        et_keyboard.append(parser.replace(text));
	}

	private void initGrid() {
		emojiAdapter = new EmojiAdapter(this);
		gv_emoji.setAdapter(emojiAdapter);
	}


	private void initView() {
		bt_voice = (Button) findViewById(R.id.bt_voice);
		et_keyboard = (EditText) findViewById(R.id.et_keyboard);
		img_event = (ImageView) findViewById(R.id.img_event);
		gv_emoji = (GridView) findViewById(R.id.grid_emoji);
		layout_message = (LinearLayout) findViewById(R.id.layout_message);
		layout_add = (LinearLayout) findViewById(R.id.layout_add);
		list_message = (ListView) findViewById(R.id.list_message);
		tv_title = (TextView) findViewById(R.id.chatView_title);
		mAudioButton = (AudioButton) findViewById(R.id.bt_voice);
		
		et_keyboard.addTextChangedListener(watcher);
		
		SelfID = UserManager.getCurrentUser().getObjectId();
		FriendID = Constant.targetUser.getObjectId();
		FriendName = Constant.sortModel.getName();
		tv_title.setText("与"+FriendName+"对话");
		mAudioButton.setTargetUserId(FriendID);
	}

	public void switchPattern(View view){
		gv_emoji.setVisibility(View.GONE);
		layout_add.setVisibility(View.GONE);
		switch (currentMode) {
		case VOICE:
			currentMode = KEYBOARD;
			bt_voice.setVisibility(View.GONE);
			et_keyboard.setVisibility(View.VISIBLE);
			img_event.setBackground(getResources().getDrawable(R.drawable.voice));		
			break;
		case KEYBOARD:
			currentMode = VOICE;
			bt_voice.setVisibility(View.VISIBLE);
			et_keyboard.setVisibility(View.GONE);
			img_event.setBackground(getResources().getDrawable(R.drawable.keyboard));
			if (gv_emoji.getVisibility() == View.VISIBLE) {
				gv_emoji.setVisibility(View.GONE);
			}
			break;
		case KEYBOARDPRESS:
			//發送文字
			String TextMsg = et_keyboard.getText().toString();
			sendMsg(TextMsg);
			et_keyboard.setText("");
			break;
		default:
			break;
		}
	}
	
	private void sendMsg(String msg) {
		ChatInfo chatInfo = new ChatInfo();
		chatInfo.setSelfId(SelfID);
		chatInfo.setFriendId(FriendID);
		chatInfo.setChatType(ChatInfo.TEXT);
		chatInfo.setFromWhere(ChatInfo.FROMSELF);
		chatInfo.setInfo(msg);
		chatInfo.setMessageTime("2016");
		MySqlHelper.insertChatInfo(chatInfo);
		BmobMsg message = BmobMsg.createTextSendMsg(this, Constant.targetUser.getObjectId(), msg);
		message.setExtra("message");
		ChatManager.sendTextMessage(Constant.targetUser, message);
	}

	public void imgAdd(View view){
		if (gv_emoji.getVisibility() == View.VISIBLE) {
			gv_emoji.setVisibility(View.GONE);
		}
		if (layout_add.getVisibility() == View.GONE) {
			layout_add.setVisibility(View.VISIBLE);
		}else {
			layout_add.setVisibility(View.GONE);
		}
	}
	
	public void imgEmoji(View view){
		layout_add.setVisibility(View.GONE);
		if (et_keyboard.getVisibility() == View.GONE) {
			currentMode = KEYBOARD;
			bt_voice.setVisibility(View.GONE);
			et_keyboard.setVisibility(View.VISIBLE);
			img_event.setBackground(getResources().getDrawable(R.drawable.voice));		
		}
		if (gv_emoji.getVisibility() == View.GONE) {
			gv_emoji.setVisibility(View.VISIBLE);
		}else {
			gv_emoji.setVisibility(View.GONE);
		}
	}
	
	private TextWatcher watcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			int length = et_keyboard.getText().toString().length();
			if (length >= 1) {
				currentMode = KEYBOARDPRESS;
				img_event.setBackground(getResources().getDrawable(R.drawable.send));
			}else if (length == 0) {
				currentMode = KEYBOARD;
				img_event.setBackground(getResources().getDrawable(R.drawable.voice));
			}	
		}
	};
	
	@Override
	public void onBackPressed() {
		if (gv_emoji.getVisibility() == View.VISIBLE) {
			gv_emoji.setVisibility(View.GONE);
		}else if (layout_add.getVisibility() == View.VISIBLE) {
			layout_add.setVisibility(View.GONE);
		}else {
			this.finish();
		}
	}
	
	public void add_camera(View view){
		showToast("camera");
	}
	public void add_location(View view){
		showToast("location");
	}
	public void add_picture(View view){
		showToast("picture");
	}
	
	public List<ChatInfo> getChatInfo(String selfId,String friendId){
		/**
		 * get user's chatLog
		 * */
		List<ChatInfo> list = new ArrayList<ChatInfo>();
		Cursor cursor = MySqlHelper.searchChatInfo(selfId,friendId);
		if (cursor != null) {
			if (cursor.getCount() != 0) {
				while(cursor.moveToNext()){
					ChatInfo chatInfo = new ChatInfo();
					chatInfo.setSelfId(cursor.getString(1));
					chatInfo.setFriendId(cursor.getString(2));
					chatInfo.setChatType(cursor.getInt(3));
					chatInfo.setInfo(cursor.getString(4));
					chatInfo.setFromWhere(cursor.getInt(5));
					chatInfo.setMessageTime(cursor.getString(6));
					list.add(chatInfo);
				}
			}
		}
		return list;
		}
	
	private ContentObserver cob = new ContentObserver(new Handler()) {
		public boolean deliverSelfNotifications() {
			return super.deliverSelfNotifications();
		};
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			//data changing
			 refreshChatInfo();
		};
	};
	
	public void refreshChatInfo(){
		Thread msgThread = new Thread(new Runnable() {
			public void run() {
				List<ChatInfo> list_info = getAllChatInfo();
				list.clear();
				list.addAll(list_info);
				mHandler.sendEmptyMessage(2);
			}
		});
		msgThread.start();
	}
	
	public List<ChatInfo> getAllChatInfo(){
		List<ChatInfo> list_info = null;
		list_info = new ArrayList<ChatInfo>();
		list_info = getChatInfo(SelfID, FriendID);
		return list_info;
	}

	@Override
	public void click(View view) {
		ChatInfo mChatInfo = (ChatInfo) view.getTag();
		AudioUtil mAudioUtil = new AudioUtil();
		mAudioUtil.playAudioByUri(mChatInfo.getInfo());
	}
}
