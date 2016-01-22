package com.ace.utility;

import android.content.Context;  
import android.text.Spannable;  
import android.text.SpannableStringBuilder;  
import android.text.style.ImageSpan;  

import java.util.HashMap;  
import java.util.regex.Matcher;  
import java.util.regex.Pattern;  

import com.ace.chartonlinedemo.R;
  
public class SmileyParser {  
    private Context mContext;  
    public String[] mSmileyTexts;  
    private Pattern mPattern;  
    private HashMap<String, Integer> mSmileyToRes;  
    public static final int[] DEFAULT_SMILEY_RES_IDS = {  
    	R.drawable.ue056,R.drawable.ue057,R.drawable.ue058,R.drawable.ue059,R.drawable.ue105,
		R.drawable.ue106,R.drawable.ue107,R.drawable.ue108,R.drawable.ue401,R.drawable.ue402,R.drawable.ue403,
		R.drawable.ue404,R.drawable.ue405,R.drawable.ue406,R.drawable.ue407,R.drawable.ue408,R.drawable.ue409,
		R.drawable.ue40a,R.drawable.ue40b,R.drawable.ue40c,R.drawable.ue40d,R.drawable.ue40e,R.drawable.ue40f,
		R.drawable.ue410,R.drawable.ue411,R.drawable.ue412,R.drawable.ue413,R.drawable.ue414,R.drawable.ue415,
		R.drawable.ue416,R.drawable.ue417,R.drawable.ue418,R.drawable.ue41f,R.drawable.ue421
    };  
  
    public SmileyParser(Context context) {  
        mContext = context;  
        mSmileyTexts = mContext.getResources().getStringArray(DEFAULT_SMILEY_TEXTS);  
        mSmileyToRes = buildSmileyToRes();  
        mPattern = buildPattern();  
    }  
  
    public static final int DEFAULT_SMILEY_TEXTS = R.array.default_smiley_texts;  
  
    private HashMap<String, Integer> buildSmileyToRes() {  
        if (DEFAULT_SMILEY_RES_IDS.length != mSmileyTexts.length) {  
//          Log.w("SmileyParser", "Smiley resource ID/text mismatch");  
            //表情的数量需要和数组定义的长度一致！  
            throw new IllegalStateException("Smiley resource ID/text mismatch");  
        }  
  
        HashMap<String, Integer> smileyToRes = new HashMap<String, Integer>(mSmileyTexts.length);  
        for (int i = 0; i < mSmileyTexts.length; i++) {  
            smileyToRes.put(mSmileyTexts[i], DEFAULT_SMILEY_RES_IDS[i]);  
        }  
  
        return smileyToRes;  
    }  
  
    //构建正则表达式  
    private Pattern buildPattern() {  
        StringBuilder patternString = new StringBuilder(mSmileyTexts.length * 3);  
        patternString.append('(');  
        for (String s : mSmileyTexts) {  
            patternString.append(Pattern.quote(s));  
            patternString.append('|');  
        }  
        patternString.replace(patternString.length() - 1, patternString.length(), ")");  
  
        return Pattern.compile(patternString.toString());  
    }  
  
    //根据文本替换成图片  
    public CharSequence replace(CharSequence text) {  
        SpannableStringBuilder builder = new SpannableStringBuilder(text);  
        Matcher matcher = mPattern.matcher(text);  
        while (matcher.find()) {  
            int resId = mSmileyToRes.get(matcher.group());  
            builder.setSpan(new ImageSpan(mContext, resId),matcher.start(), matcher.end(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
        }  
        return builder;  
    }  
}  