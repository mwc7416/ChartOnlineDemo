package com.ace.chatonlinedemo.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ace.chartonlinedemo.R;
import com.ace.model.MessageInfo;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class CommunicationFragment extends Fragment {
	
	LinearLayout communication;
	ListView List_message_detail;
	
	List<Map<String, Object>> listInfo;
	
	private int friendIconId[] = {R.drawable.ic_launcher,R.drawable.ic_launcher
								 ,R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher};
	private String friendName[] = {"С��","С��","����","˪˪","��ʦ"};
	private String friendMsg[] = {"��ʶ��ܸ���!","��ĩ�п���","С����ʵ���Ҷ���","Լ��","������"};
	private String msgTime[] = {"���� 19:55","���� 10:32","��һ 18:30","���� 19:55","���� 21:30"};
	private int msgCount[] = {1,2,3,1,8};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		communication = (LinearLayout) inflater.inflate(R.layout.frg_communication, container,false);
		initResouce();
		initList();
		return communication;
	}
	
	private void initList() {
		listInfo = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < friendIconId.length; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("iconId", friendIconId[i]);
				map.put("friendName", friendName[i]);
				map.put("recordTime", msgTime[i]);
				map.put("friendMsg", friendMsg[i]);
				map.put("msgCount", msgCount[i]);
				listInfo.add(map);
			}
		SimpleAdapter adapter = new SimpleAdapter(getActivity(), listInfo, R.layout.list_communication_item, 
				new String[]{"iconId","friendName","recordTime","friendMsg","msgCount"}, 
				new int[]{R.id.user_head,R.id.friend_name,R.id.message_time,R.id.message_content,R.id.message_count});
		List_message_detail.setAdapter(adapter);
	}

	private void initResouce() {
		List_message_detail = (ListView) communication.findViewById(R.id.list_communication);
	}

	public CommunicationFragment getInstance(){
		return new CommunicationFragment();
	}
}
