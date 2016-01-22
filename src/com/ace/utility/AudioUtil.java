package com.ace.utility;

import java.io.File;
import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;

public class AudioUtil {
	
	private static String PATH = "";
	private CommonUtility mCommonUtility;
	
	public String getPATH() {
		return PATH;
	}

	MediaRecorder mediaRecorder;
	String username ;
	
	public AudioUtil(String username){
		this.username = username;
		mCommonUtility = new CommonUtility();
	}
	
	public AudioUtil(){}
	
	public void initAudio(String recordName){
		PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
		PATH += "/MyChatDemo";
		mCommonUtility.CreateFolderifNotExist(PATH);
		PATH += "/"+username;
		mCommonUtility.CreateFolderifNotExist(PATH);
		PATH += "/record";
		mCommonUtility.CreateFolderifNotExist(PATH);
		PATH += "/"+recordName+".amr";
		mediaRecorder = new MediaRecorder();
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mediaRecorder.setOutputFile(PATH);
	}
	

	public void start(){
		try {
			mediaRecorder.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mediaRecorder.start();

	}
	
	public void stop(){
		mediaRecorder.stop();
		mediaRecorder.reset();
		mediaRecorder.release();
	}
	
	public void cancle(){
		stop();
		mediaRecorder = null;
		
	}
	
	public int getVoiceLevel(){
		int level = 0;
		int db = 0;
		if (mediaRecorder != null) {
			int ratio = mediaRecorder.getMaxAmplitude()/600;
			if (ratio > 1) {
				db = (int) (20 * Math.log10(ratio));  
			}
		}
		if (db/4 > 5) {
			level = 5;
		}else if (db/4 < 0) {
			level = 0;
		}else {
			level =db/4;
		}
		return level;
	}
	
	public void playAudioByUri(String uri){
		MediaPlayer mp = new MediaPlayer();
		try {
			mp.setDataSource(uri);
			mp.prepare();
			mp.start();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getVoiceLength(String uri){
		int duration = 0;
		MediaPlayer mp = new MediaPlayer();
		try {
			mp.setDataSource(uri);
			mp.prepare();
			duration = mp.getDuration();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return duration;
	}
	
}
