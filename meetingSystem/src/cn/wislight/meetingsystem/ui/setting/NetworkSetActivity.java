package cn.wislight.meetingsystem.ui.setting;

import org.apache.http.Header;

import com.example.qr_codescan.MipcaActivityCapture;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.service.SettingsConfig;
import cn.wislight.meetingsystem.ui.topic.TopicAddTwoActivity;
import cn.wislight.meetingsystem.ui.topic.TopicHouseActivity;
import cn.wislight.meetingsystem.util.MeetingSystemClient;


/**
 * @author Administrator
 * NetworkSetting
 */
public class NetworkSetActivity extends SettingActivity {
	
	EditText etUrl = null;
	SettingsConfig config = null;
	private final static int SCANNIN_GREQUEST_CODE = 10;
	private EditText updateRecord;
	
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		etUrl = (EditText)findViewById(R.id.et_uri);
		config = new SettingsConfig(this);
		etUrl.setText(config.getBaseUrl());
	}

	public void clickTest(View view){
		if ( etUrl.getText().toString().length() <= 0 ){
			Toast.makeText(NetworkSetActivity.this, getString(R.string.setting_input_url), Toast.LENGTH_SHORT).show();
			return;
		}		
		
		Toast.makeText(NetworkSetActivity.this, getString(R.string.setting_test_begin), Toast.LENGTH_SHORT).show();
		testNetwork(etUrl.getText().toString());
	}
	


	public void clickSave(View view){
		if ( etUrl.getText().toString().length() <= 0 ){
			Toast.makeText(NetworkSetActivity.this, getString(R.string.setting_input_url), Toast.LENGTH_SHORT).show();
			return;
		}
		config.setBaseUrl(etUrl.getText().toString());
		Toast.makeText(NetworkSetActivity.this, getString(R.string.setting_url_saved), Toast.LENGTH_SHORT).show();
	}	

	public void scanQcode(View view) {
		// TODO Auto-generated method stub
		updateRecord = null;

		Intent intent = new Intent();
		intent.setClass(NetworkSetActivity.this, MipcaActivityCapture.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		 if (SCANNIN_GREQUEST_CODE == requestCode) {

			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();

				String result = bundle.getString("result");
				System.out.println("wangting erweima code is:" + result);
				
			/*	if (!result.startsWith("Meeting")) {
					Toast toast = Toast.makeText(getApplicationContext(),
							getString(R.string.qcode_invalid),
							Toast.LENGTH_SHORT);
					toast.show();
					return;
				}
*/			
				updateRecord = (EditText)findViewById(R.id.et_uri);
				updateRecord.setText(result);
				
				/*
				
	        	String strmsg = "»ñÈ¡³É¹¦£¡";
				Toast toast = Toast.makeText(getApplicationContext(), strmsg,
						Toast.LENGTH_SHORT);
				toast.show();
				*/

			} 
		}
	}

	private void testNetwork(String string) {
		// TODO Auto-generated method stub
		final String temp = MeetingSystemClient.getBASE_URL();
		MeetingSystemClient.setBASE_URL(string);
		
		String url = "MeetingManage/mobile/testConnect.action";
			
			MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler(){

				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] body,
						Throwable error) {
					MeetingSystemClient.setBASE_URL(temp);
					Toast.makeText(NetworkSetActivity.this, getString(R.string.setting_test_fail), Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] body) {
					MeetingSystemClient.setBASE_URL(temp);
					String response = new String(body);
					

					if (response.contains("Hello")){
						Toast.makeText(NetworkSetActivity.this, getString(R.string.setting_test_ok), Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(NetworkSetActivity.this, getString(R.string.setting_test_fail), Toast.LENGTH_SHORT).show();
					}
					
				}
			});
	}	
	@Override
	public void setupView() {
		setContentView(R.layout.system_setting_networkset);
	}

}
