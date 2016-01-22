package com.ace.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.ace.model.ChatInfo;
import com.ace.model.Config;

import cn.bmob.im.BmobChat;
import cn.bmob.im.BmobUserManager;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class CommonUtility {
	Context mContext;
	String PATH;
	
	public String getPATH() {
		return PATH;
	}

	public void setPATH(String pATH) {
		PATH = pATH;
	}

	public CommonUtility(Context mContext){
		this.mContext = mContext;
	}
	
	public CommonUtility(){
		
	}
	
	public void showToast(String str){
		Toast.makeText(mContext,str,0).show();
	}
	
	public void flagTag(String TAG,String str){
		Log.i(TAG, str);
	}
	
	public void initBmob(){
        //�����õ���ģʽ����Ϊtrue��ʱ�򣬻���logcat��BmobChat�����һЩ��־���������ͷ����Ƿ��������У��������˷��ش���Ҳ��һ����ӡ���������㿪���ߵ��ԣ���ʽ����Ӧע�ʹ˾䡣
		BmobChat.DEBUG_MODE = true;
		//BmobIM SDK��ʼ��--ֻ��Ҫ��һ�δ��뼴����ɳ�ʼ��
		BmobChat.getInstance(mContext).init(Config.applicationId);
	}

	public void downloadFile(String uri,int type){
		switch (type) {
		case ChatInfo.AUDIO:
			String fileName = "record"+getNum();
			FileOutputStream output = null;
			try {
				URL url = new URL(uri);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
				PATH += "/MyChatDemo";
				CreateFolderifNotExist(PATH);
				PATH += "/"+BmobUserManager.getInstance(mContext).getCurrentUserName();
				CreateFolderifNotExist(PATH);
				PATH += "/recevie";
				CreateFolderifNotExist(PATH);
				PATH += "/"+fileName+".amr";
				File file = new File(PATH);
				InputStream input = conn.getInputStream();
				if (file.exists()) {
					return;
				}else {
					file.createNewFile();
					output = new FileOutputStream(file);
					byte[] buffer = new byte[4*1024];
					int len;
					while ((len=input.read(buffer))!= -1) {
						output.write(buffer,0,len);
						}
					output.flush();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					output.close();
					Log.i("AudioButton", "download success!");
				} catch (IOException e) {
					System.out.println("fail");
					e.printStackTrace();
				}
			}
			break;
		case ChatInfo.PIC:
			
			break;
		default:
			break;
		}
	}
	
	public long getNum(){
		return System.currentTimeMillis();
	}
	
	public void CreateFolderifNotExist(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdir();
		}
	}
}
