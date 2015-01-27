package cn.wislight.meetingsystem.ui.setting;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.bean.OrganizeSmpBean;
import cn.wislight.meetingsystem.service.DbAdapter;
import cn.wislight.meetingsystem.service.DictionaryUpdater;
import cn.wislight.meetingsystem.service.restartApp;
import cn.wislight.meetingsystem.ui.LoginActivity;
import cn.wislight.meetingsystem.util.Authorize;
import cn.wislight.meetingsystem.util.IntentUtil;
import cn.wislight.meetingsystem.util.LoadingDialog;
import cn.wislight.meetingsystem.util.MeetingSystemClient;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * @author Administrator
 * 设置界面
 */
public class SettingActivity extends BaseActivity implements OnClickListener{
	private LinearLayout system_setting_myinformation;
	private LinearLayout system_setting_networkset;
	private LinearLayout system_setting_updatepwd;
	private LinearLayout system_setting_update;
	private LinearLayout system_setting_about;
	private LinearLayout system_setting_relogin;
	private LinearLayout system_setting_exit;
	private LinearLayout system_setting_commongroup;
	private LinearLayout system_setting_update_dictionary;
	private long mExitTime;
	private LoadingDialog loadingdiag;
	private String rspOrg;
	
	DictionaryUpdateReceiver receiver = null;
	boolean updatingDictionary = false;
	@Override
	public void initView() {
		system_setting_myinformation=(LinearLayout)findViewById(R.id.system_setting_myinformation);
		system_setting_myinformation.setOnClickListener(this);	
		system_setting_networkset=(LinearLayout)findViewById(R.id.system_setting_networkset);
		system_setting_networkset.setOnClickListener(this);
		system_setting_updatepwd=(LinearLayout)findViewById(R.id.system_setting_updatepwd);
		system_setting_updatepwd.setOnClickListener(this);
		system_setting_update=(LinearLayout)findViewById(R.id.system_setting_update);
		system_setting_update.setOnClickListener(this);
		system_setting_about=(LinearLayout)findViewById(R.id.system_setting_about);
		system_setting_about.setOnClickListener(this);
		system_setting_relogin=(LinearLayout)findViewById(R.id.system_setting_relogin);
		system_setting_relogin.setOnClickListener(this);
		system_setting_exit=(LinearLayout)findViewById(R.id.system_setting_exit);
		system_setting_exit.setOnClickListener(this);
		system_setting_commongroup=(LinearLayout)findViewById(R.id.system_setting_commongroup);
		system_setting_commongroup.setOnClickListener(this);
		if(Authorize.hasAuth(Authorize.AUTH_CONF_NEW)){
			system_setting_commongroup.setVisibility(View.VISIBLE);
		} else {
			system_setting_commongroup.setVisibility(View.GONE);
		}

		system_setting_update_dictionary = (LinearLayout)findViewById(R.id.system_setting_update_dictionary);
		system_setting_update_dictionary.setOnClickListener(this);

		loadingdiag = new LoadingDialog(this);  
		loadingdiag.setCanceledOnTouchOutside(false); 
		loadingdiag.setText(getString(R.string.updating));
		
		receiver=new DictionaryUpdateReceiver();
		IntentFilter filter=new IntentFilter();
		filter.addAction("android.intent.action.updatedictionary");
		this.registerReceiver(receiver,filter);

	}

	@Override
	public void setupView() {
		setContentView(R.layout.system_setting);
	}

	protected void onDestroy(){
		if (null != receiver){
			unregisterReceiver(receiver);
		}
		super.onDestroy();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.system_setting_myinformation:
			IntentUtil.startActivity(SettingActivity.this, MyInformationActivity.class);
			break;
		case R.id.system_setting_networkset:
			IntentUtil.startActivity(SettingActivity.this, NetworkSetActivity.class);
			break;
		case R.id.system_setting_update_dictionary:
			Intent intent = new Intent();
			intent.setClass(SettingActivity.this, DictionaryUpdater.class);
			startService(intent); 	
			loadingdiag.setbUpdatingDictionaty(true);
			loadingdiag.show();
			updatingDictionary = true;
			break;
		case R.id.system_setting_updatepwd:
			IntentUtil.startActivity(SettingActivity.this, ChangePasswordActivity.class);
			break;
		case R.id.system_setting_update:
			IntentUtil.startActivity(SettingActivity.this, UpdateActivity.class);
			break;
		case R.id.system_setting_commongroup:
			IntentUtil.startActivity(SettingActivity.this, CommonGroupActivity.class);
			break;
		case R.id.system_setting_about:
			IntentUtil.startActivity(SettingActivity.this, AboutActivity.class);
			break;	
		case R.id.system_setting_relogin:
		{			
			finish();
			Intent intentRestart = new Intent();
			intentRestart.setClass(SettingActivity.this, restartApp.class);
			startService(intentRestart);			
			//IntentUtil.startActivity(SettingActivity.this, LoginActivity.class);
		}
			break;
		case R.id.system_setting_exit:
			System.exit(0);
			finish();
			break;

		default:
			break;
		}
	}
	
	public class DictionaryUpdateReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent intent) {
			// TODO Auto-generated method stub
			loadingdiag.hide();
			updatingDictionary = false;
			Bundle bundle=intent.getExtras();
			int a=bundle.getInt("i");
			switch (a) {
			case 1:
				Toast.makeText(SettingActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(SettingActivity.this, "网络状况不好，更新失败", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}

		}
		

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Object mHelperUtils;
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
