package cn.wislight.meetingsystem;



import com.networkbench.agent.impl.NBSAppAgent;

import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.constant.Constant;
import cn.wislight.meetingsystem.service.ConnectionChangeReceiver;
import cn.wislight.meetingsystem.ui.conference.ConfenenceManagementActivity;
import cn.wislight.meetingsystem.ui.home.HomeActivity;
import cn.wislight.meetingsystem.ui.setting.SettingActivity;
import cn.wislight.meetingsystem.ui.topic.TopicActivity;
import cn.wislight.meetingsystem.ui.vote.VoteActivity;
import cn.wislight.meetingsystem.voip.CCPHelper;


import android.net.ConnectivityManager;
import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.TabHost;


public class MainActivity extends TabActivity implements OnClickListener {
	private TabHost tabHost;
	//首页，议题，会议，表决，设置
	private  RadioButton mRbtn_home,mRbtn_conference,mRbtn_topic;
	private  RadioButton mRbtn_vote,mRbtn_setting;
	private  ConnectionChangeReceiver   mReceiver;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		mReceiver = new ConnectionChangeReceiver();
		this.registerReceiver(mReceiver, filter);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		setupView();
		//voip and im feature
		CCPHelper.getInstance().createDevice();
		
		//networkbench feature
		NBSAppAgent.setLicenseKey("e0493f56dcb344c8a99f87c39506a911").start(this);
	}

	private void setupView() {
		setSwitchTab();
		initView();
		addTabs();	
		initListener();
	}

	private void setSwitchTab() {
		//bundle=getIntent().getExtras();
		mRbtn_home=(RadioButton)findViewById(R.id.rbtn_menu_homes);
		mRbtn_topic=(RadioButton) findViewById(R.id.rbtn_menu_topic);
		mRbtn_conference=(RadioButton) findViewById(R.id.rbtn_menu_conference);
		mRbtn_vote=(RadioButton) findViewById(R.id.rbtn_menu_vote);
		mRbtn_setting=(RadioButton)findViewById(R.id.rbtn_menu_setting);
		tabHost = getTabHost();
	}

	private void initListener() {
		mRbtn_home.setOnClickListener(this);
		mRbtn_topic.setOnClickListener(this);
		mRbtn_conference.setOnClickListener(this);
		mRbtn_vote.setOnClickListener(this);
		mRbtn_setting.setOnClickListener(this);
	}

	private void addTabs() {
		addtab(HomeActivity.class, Constant.TAB_HOME);
		addtab(TopicActivity.class, Constant.TAB_TOPIC);
		addtab(ConfenenceManagementActivity.class, Constant.TAB_CONFERENCE);
		addtab(VoteActivity.class, Constant.TAB_VOTE);
		addtab(SettingActivity.class, Constant.TAB_SETTING);
	}
	private void addtab(Class<?> clazz,String swichTab) {
				Intent intent = new Intent(this, clazz);
		//		intent.putExtras(bundle);
		tabHost.addTab(tabHost.newTabSpec(swichTab).setIndicator(swichTab).setContent(intent));
	}
	private void initView() {
		//String switchTab = getIntent().getStringExtra(Constant.PARAM_SWITCH_TAB);
		mRbtn_home.setChecked(true);
		switchTabByAction(Constant.TAB_HOME);
		/*
		if (TextUtils.isEmpty(switchTab))
		{
			onClick(mRbtn_home);
		} else
		{
			switchTabByAction(switchTab);
		}
		*/
	}


	private void switchTabByAction(String switchTab)
	{
		if (TextUtils.equals(switchTab, Constant.TAB_HOME))
		{
			onClick(mRbtn_home);

		}else if(TextUtils.equals(switchTab,Constant.TAB_TOPIC)){ 
			onClick(mRbtn_topic);
		}
		
		else if (TextUtils.equals(switchTab, Constant.TAB_CONFERENCE))
		{
			onClick(mRbtn_conference);
		} else if (TextUtils.equals(switchTab, Constant.TAB_VOTE))
		{
			onClick(mRbtn_vote);
		}else  {
			onClick(mRbtn_setting);
		} 
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rbtn_menu_homes:
			tabHost.setCurrentTabByTag(Constant.TAB_HOME);
			break;
		case R.id.rbtn_menu_topic:
			tabHost.setCurrentTabByTag(Constant.TAB_TOPIC);
			break;
		case R.id.rbtn_menu_conference:

			tabHost.setCurrentTabByTag(Constant.TAB_CONFERENCE);
			break;
		case R.id.rbtn_menu_vote:

			tabHost.setCurrentTabByTag(Constant.TAB_VOTE);
			break;
		case R.id.rbtn_menu_setting:
			tabHost.setCurrentTabByTag(Constant.TAB_SETTING);
			break;

		default:
			break;
		}
	}

	protected void onDestroy(){
		this.unregisterReceiver(mReceiver);
		MeetingSystemApplication.exitCCPDemo();
		super.onDestroy();
	}
}
