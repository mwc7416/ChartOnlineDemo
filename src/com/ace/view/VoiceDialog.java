package com.ace.view;

import com.ace.chartonlinedemo.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class VoiceDialog extends Dialog {
	private ImageView imageView;
	private TextView textView;

	public VoiceDialog(Context context) {
		super(context,R.style.CustomDialog);
		setCustomView();
	}
	
	public VoiceDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context,R.style.CustomDialog);
		this.setCancelable(cancelable);
		this.setOnCancelListener(cancelListener);
		setCustomView();
	}

	public VoiceDialog(Context context, int theme) {
		super(context,R.style.CustomDialog);
		setCustomView();
	}

	private void setCustomView() {
		View mView = LayoutInflater.from(getContext()).inflate(R.layout.voice_dialog, null);
		imageView = (ImageView) mView.findViewById(R.id.dialog_voice_icon);
		textView = (TextView) mView.findViewById(R.id.dialog_voice_info);
		super.setContentView(mView);
	}
	
	@Override
	public void setContentView(View view) {
		super.setContentView(view);
	}
	
	public void setDialogImageResouce(int id){
		imageView.setImageResource(id);
	}
	
	public void setDialogText(String str){
		textView.setText(str);
	}
	
	public void setDialogTextColor(int color){
		textView.setTextColor(getContext().getResources().getColor(color));
	}
}
