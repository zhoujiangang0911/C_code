package cn.wislight.meetingsystem.util;


import cn.wislight.meetingsystem.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;

public class Bottommenu {
	private  RadioButton mRbtn_Home,mRbtn_topic,mRbtn_Conference,mRbtn_Vote,mRbtn_Setting;
	private Activity activity;

	public Bottommenu(Activity activity) {
		this.activity=activity;
		initMenu(activity);
		addListener(activity);
	}

	public  void  initMenu(Activity activity) {
		View view=LayoutInflater.from(activity).inflate(R.layout.bottommenu, null);
		mRbtn_Home=(RadioButton) view.findViewById(R.id.rbtn_menu_home);
		mRbtn_topic=(RadioButton) view.findViewById(R.id.rbtn_menu_topic);
		
		mRbtn_Conference=(RadioButton) view.findViewById(R.id.rbtn_menu_conference);
		mRbtn_Vote=(RadioButton) view.findViewById(R.id.rbtn_menu_vote);
		mRbtn_Setting=(RadioButton) view.findViewById(R.id.rbtn_menu_setting);
	}

	private  void addListener(Activity activity) {
		mRbtn_Home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});
		mRbtn_topic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
		mRbtn_Conference.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});
		mRbtn_Vote.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
		mRbtn_Setting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
	}

}
