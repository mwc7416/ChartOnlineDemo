package com.ace.utility;

import com.ace.model.ChatInfo;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;

public class ChatSqlUtil {

	private static final String TAG = "CHATLOG";
	
	private Context context;
	private static ChatSqlUtil mChatSqlUtil = null;
	private SQLiteDatabase db;
	private String dbDir;
	
	public static final Uri CONTENT_URI = Uri.parse("content://com.ace.chartonlinedemo/chartonlinedemo");
	
	public ChatSqlUtil(Context context){
		this.context = context;
		create();
	}
	
	public void create(){
		createSql();
		createTable();
	}
	
	public static ChatSqlUtil getInstance(Context context){
		if (mChatSqlUtil == null) 
			mChatSqlUtil = new ChatSqlUtil(context);
		return mChatSqlUtil;
	}
	
	private void createSql() {
		try {
			dbDir = context.getFilesDir().toString();
			db = context.openOrCreateDatabase(dbDir+"/ChatSource.db", Context.MODE_PRIVATE, null);
			Log.i(TAG, "ChatSource.db create success!");
		} catch (Exception e) {
			e.printStackTrace();
			Log.i(TAG, "ChatSource.db create failed!");
		}
	}

	private void createTable(){
		try {
			/**
			 * @author ACE
			 * create user's chatLog table for save the ChatLog
			 * selfId: user's Id
			 * friendId:friend's Id
			 * type:the type of chatLog(just like :message,picture,audio,location)
			 * info:the detail info of your message(text or other's id)
			 * source: send or receive
			 * time :the message's transmission time
			 **/
			db.execSQL("CREATE TABLE CS_CHATLOG (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
											+ "selfId VARCHAR NOT NULL,"
											+ "friendId VARCHAR NOT NULL,"
											+ "type INTEGER NOT NULL,"
											+ "info VARCHAR NOT NULL,"
											+ "source TINYINT NOT NULL,"
											+ "time timestamp NOT NULL)");
			Log.i(TAG, "CS_CHASLOG create success!");
		} catch (SQLException e) {
			e.printStackTrace();
			Log.i(TAG, "CS_CHASLOG create failed!");
		}
	}
	
	public boolean insertChatInfo(ChatInfo chatInfo){
		/**
		 * save message to sqlite
		 * */
		try {
			db.execSQL("insert into CS_CHATLOG values(null,?,?,?,?,?,?)",new Object[]{chatInfo.getSelfId(),
					chatInfo.getFriendId(),chatInfo.getChatType(),chatInfo.getInfo(),chatInfo.getFromWhere(),chatInfo.getMessageTime()});
			Log.i(TAG, "insert CS_CHATLOG success!");
			context.getContentResolver().notifyChange(CONTENT_URI, null);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			Log.i(TAG, "insert CS_CHATLOG failed!");
			return false;
		}
	}
	
	public Cursor searchChatInfo(String selfId,String friendId){
		Cursor cursor = null;
		try {
			String sql = "SELECT * FROM CS_CHATLOG WHERE selfId = ? AND friendId = ?";
			cursor = db.rawQuery(sql,new String[]{selfId,friendId});
			Log.i(TAG, "select CS_CHATLOG success!");
		} catch (SQLiteException e) {
			e.printStackTrace();
			Log.i(TAG, "select CS_CHATLOG failed!");
		}
		return cursor;
	}
	
	public boolean close(){
		try {
			db.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
}
