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
	 * ����ת����ƴ������
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;
	private List<BmobChatUser> ContactsList;
	
	/**
	 * ����ƴ��������ListView�����������
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
		//ʵ��������תƴ����
		characterParser = CharacterParser.getInstance();
		
		pinyinComparator = new PinyinComparator();
		
		sideBar = (SideBar) contacts.findViewById(R.id.sidrbar);
		dialog = (TextView) contacts.findViewById(R.id.dialog);
		request = (LinearLayout) contacts.findViewById(R.id.newFriendRequest);
		sideBar.setTextView(dialog);
		
		//�����Ҳഥ������
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			
			@Override
			public void onTouchingLetterChanged(String s) {
				//����ĸ�״γ��ֵ�λ��
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
				//����Ҫ����adapter.getItem(position)����ȡ��ǰposition����Ӧ�Ķ���
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
		// ����a-z��������Դ����
		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new SortAdapter(getActivity(), SourceDateList);
		sortListView.setAdapter(adapter);
		
		
		mClearEditText = (ClearEditText) contacts.findViewById(R.id.filter_edit);
		
		//�������������ֵ�ĸı�����������
		mClearEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//������������ֵΪ�գ�����Ϊԭ�����б�����Ϊ���������б�
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
	 * ΪListView�������
	 * @param date
	 * @return
	 */
	private List<SortModel> filledData(String [] date){
		List<SortModel> mSortList = new ArrayList<SortModel>();
		
		for(int i=0; i<date.length; i++){
			SortModel sortModel = new SortModel();
			sortModel.setName(date[i]);
			//����ת����ƴ��
			String pinyin = characterParser.getSelling(date[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();
			
			// ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
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
	 * ����������е�ֵ���������ݲ�����ListView
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
		
		// ����a-z��������
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}

	public void refresh() {
		
	}
	
}
