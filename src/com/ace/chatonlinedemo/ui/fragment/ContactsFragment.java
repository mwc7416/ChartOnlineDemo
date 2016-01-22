package com.ace.chatonlinedemo.ui.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.im.bean.BmobChatUser;

import com.ace.adapter.SortAdapter;
import com.ace.chartonlinedemo.R;
import com.ace.chatonlinedemo.MyApplication;
import com.ace.chatonlinedemo.ui.DetailActivity;
import com.ace.chatonlinedemo.ui.NewFriendManagerActivity;
import com.ace.model.SortModel;
import com.ace.utility.CharacterParser;
import com.ace.utility.Constant;
import com.ace.utility.PinyinComparator;
import com.ace.view.ClearEditText;
import com.ace.view.SideBar;
import com.ace.view.SideBar.OnTouchingLetterChangedListener;

import android.R.string;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ContactsFragment extends Fragment {
	MyApplication myApplication;
	
	LinearLayout contacts;
	LinearLayout request;
	
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	private ClearEditText mClearEditText;
	
	String emptyStr[] = {""};
	
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;
	private List<BmobChatUser> ContactsList;
	
	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contacts = (LinearLayout) inflater.inflate(R.layout.frg_contacts, container,false);
		initViews();
		return contacts;
	}
	
	private void initViews() {
		myApplication = (MyApplication) getActivity().getApplicationContext();
		ContactsList = myApplication.getList_contacts();
		//实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		
		pinyinComparator = new PinyinComparator();
		
		sideBar = (SideBar) contacts.findViewById(R.id.sidrbar);
		dialog = (TextView) contacts.findViewById(R.id.dialog);
		request = (LinearLayout) contacts.findViewById(R.id.newFriendRequest);
		sideBar.setTextView(dialog);
		
		//设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			
			@Override
			public void onTouchingLetterChanged(String s) {
				//该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if(position != -1){
					sortListView.setSelection(position);
				}
				
			}
		});
		
		sortListView = (ListView) contacts.findViewById(R.id.country_lvcountry);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//这里要利用adapter.getItem(position)来获取当前position所对应的对象
//				Toast.makeText(getActivity(),ContactsList.get(position).getUsername(), 0).show();
				Constant.sortModel = (SortModel)adapter.getItem(position);
				for (int i = 0; i < ContactsList.size(); i++) {
					BmobChatUser chatUser = new BmobChatUser();
					chatUser = ContactsList.get(i);
					if (chatUser.getUsername().equals(Constant.sortModel.getName())) {
						Constant.targetUser = chatUser;
						break;
					}
				}
				Intent intent = new Intent(getActivity(),DetailActivity.class);
				startActivity(intent);
			}
		});
		int size = ContactsList.size();
		String[] contacts_data = new String[size];
		for (int i = 0; i < ContactsList.size(); i++) {
			contacts_data[i] = ContactsList.get(i).getUsername();
		}
//		
		SourceDateList = filledData(contacts_data);
		// 根据a-z进行排序源数据
		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new SortAdapter(getActivity(), SourceDateList);
		sortListView.setAdapter(adapter);
		
		
		mClearEditText = (ClearEditText) contacts.findViewById(R.id.filter_edit);
		
		//根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		request.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent =  new Intent(getActivity(),NewFriendManagerActivity.class);
				startActivity(intent);
			}
		});
	}


	/**
	 * 为ListView填充数据
	 * @param date
	 * @return
	 */
	private List<SortModel> filledData(String [] date){
		List<SortModel> mSortList = new ArrayList<SortModel>();
		
		for(int i=0; i<date.length; i++){
			SortModel sortModel = new SortModel();
			sortModel.setName(date[i]);
			//汉字转换成拼音
			String pinyin = characterParser.getSelling(date[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();
			
			// 正则表达式，判断首字母是否是英文字母
			if(sortString.matches("[A-Z]")){
				sortModel.setSortLetters(sortString.toUpperCase());
			}else{
				sortModel.setSortLetters("#");
			}
			
			mSortList.add(sortModel);
		}
		return mSortList;
		
	}
	
	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * @param filterStr
	 */
	private void filterData(String filterStr){
		List<SortModel> filterDateList = new ArrayList<SortModel>();
		
		if(TextUtils.isEmpty(filterStr)){
			filterDateList = SourceDateList;
		}else{
			filterDateList.clear();
			for(SortModel sortModel : SourceDateList){
				String name = sortModel.getName();
				if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
					filterDateList.add(sortModel);
				}
			}
		}
		
		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}

	public void refresh() {
		
	}
	
}
