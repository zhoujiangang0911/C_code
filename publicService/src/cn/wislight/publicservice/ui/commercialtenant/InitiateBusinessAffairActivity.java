package cn.wislight.publicservice.ui.commercialtenant;

import java.io.IOException;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cn.wislight.publicservice.R;
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.ui.LoginActivity;
import cn.wislight.publicservice.ui.RegisterPersonalActivity;
import cn.wislight.publicservice.util.Constants;
import cn.wislight.publicservice.util.FindCurrentLocation;
import cn.wislight.publicservice.util.MD5Str;
import cn.wislight.publicservice.util.PublicServiceClient;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.text.Layout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Administrator
 *  发起事物
 */
public class InitiateBusinessAffairActivity extends BaseActivity implements OnClickListener {
	private static final String LOG_TAG = "AudioRecordTest";
	private TextView txtPointselect;
	private EditText txtBusinessaffairContent;
	private EditText txtBusinessaffairPrice;
	private EditText editBusinessaffairAddress;
	
	private LinearLayout layoutSelectservicetype;
	private LinearLayout layoutVoiceRecord;
	private TextView txtServicetype;
	
	private ImageButton btnBusinessaffairSpeak;
	private ImageButton btnBusinessaffairVoiceRecord;
	private ImageButton btnCreateBusinessAffair;
	
	private String addressName;
	private String longitude;
	private String latitude;
	private TextView txtCommonlyAddressSelect;
	private String servicetypeId;
	
	//语音操作对象  
    private MediaPlayer mPlayer = null;  
    private MediaRecorder mRecorder = null; 
	
    //语音文件保存路径  
    private String fileName = null;
    
	@Override
	public void setUpView() {
		setContentView(R.layout.activity_initiate_businessaffair_commerce);
		
		txtPointselect = (TextView) findViewById(R.id.pointselect);
		txtCommonlyAddressSelect = (TextView) findViewById(R.id.commonlyAddressSelect);
		
		txtBusinessaffairContent = (EditText) findViewById(R.id.businessaffairContent);
		txtBusinessaffairPrice = (EditText) findViewById(R.id.businessaffairPrice);
		editBusinessaffairAddress  = (EditText) findViewById(R.id.businessaffairAddress);
		
		layoutSelectservicetype  = (LinearLayout) findViewById(R.id.selectservicetype);
		layoutVoiceRecord  = (LinearLayout) findViewById(R.id.layoutVoiceRecord);
		txtServicetype  =  (TextView) findViewById(R.id.servicetype);
		
		btnBusinessaffairSpeak = (ImageButton) findViewById(R.id.businessaffairSpeak);
		btnBusinessaffairVoiceRecord = (ImageButton) findViewById(R.id.businessaffairVoiceRecord);
		btnCreateBusinessAffair = (ImageButton) findViewById(R.id.createBusinessAffair);
		
		txtPointselect.setOnClickListener(this);
		txtCommonlyAddressSelect.setOnClickListener(this);
		btnCreateBusinessAffair.setOnClickListener(this);
		btnBusinessaffairVoiceRecord.setOnClickListener(this);
		layoutSelectservicetype.setOnClickListener(this);
		
		btnBusinessaffairSpeak.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:
					Toast.makeText(InitiateBusinessAffairActivity.this, "请说话", 100).show();
					InitiateBusinessAffairActivity.this.startRecord();
					break;
				case MotionEvent.ACTION_UP:
					Toast.makeText(InitiateBusinessAffairActivity.this, "录音完毕", 100).show();
					InitiateBusinessAffairActivity.this.stopRecord();
					break;
				
				}
				return true;
			}
			
		});
		
		 //设置sdcard的路径  
        fileName = Environment.getExternalStorageDirectory().getAbsolutePath();  
        fileName += "/audiorecordtest.3gp";
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.createBusinessAffair:
			createBusinessAffair();
			break;
		case R.id.selectservicetype:
			Intent intentServiceType = new Intent(this, SelectServiceTypeActivity.class);
			startActivityForResult(intentServiceType, Constants.REQUEST_SERVICETYPE);
			break;			
		case R.id.pointselect:
			Toast.makeText(InitiateBusinessAffairActivity.this,
					"select position", Toast.LENGTH_SHORT)
					.show();
			//Intent intent = new Intent(this, BaiduMapSelectPositionActivity.class);
			//this.startActivityForResult(intent, 1);
			//this.startActivity(intent);
			
			Intent intent = new Intent(this,BaiduMapSelectPositionActivity.class);
			//intent.putExtra("negotiationId", negotiationId);
			//startActivity(intent);
			startActivityForResult(intent, Constants.REQUEST_ADDRESS);
			
			break;	
		case R.id.commonlyAddressSelect:
			Intent intentCommonAddress = new Intent(this, FindCurrentLocation.class);
			//intent.putExtra("negotiationId", negotiationId);
			startActivity(intentCommonAddress);
			//startActivityForResult(intentCommonAddress, Constants.REQUEST_ADDRESS);
			break;	
		case R.id.businessaffairVoiceRecord:
			Toast.makeText(InitiateBusinessAffairActivity.this,
					"开始放音", Toast.LENGTH_SHORT)
					.show();
			startPlay();
			break;	
			
		default:
			break;
		}
	}
	
	private void createBusinessAffair(){
		String url = "publicservice/businessaffair_create.htm?json=true";
		RequestParams params = new RequestParams();
		String content = txtBusinessaffairContent.getText().toString();
		String price = txtBusinessaffairPrice.getText().toString();

		if(content != null && content.trim().length() <1){
			Toast.makeText(this, "内容不能为空", 100).show();
			return;
		}

		
		params.put("businessaffair.content", content);
		params.put("businessaffair.price ", price);

		params.put("businessaffair.address.name", addressName);
		params.put("businessaffair.address.longitude ", longitude);
		params.put("businessaffair.address.latitude ", latitude);
		
		params.put("businessaffair.servicetype.id", servicetypeId);
		
		
		///loadingdiag.setText(getString(R.string.uploading));
		//loadingdiag.show();
		PublicServiceClient.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] arg1, byte[] body,
					Throwable error) {
				//loadingdiag.hide();
				Toast.makeText(InitiateBusinessAffairActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onSuccess(int stautsCode, Header[] arg1, byte[] body) {
				// loadingdiag.hide();
				String response = new String(body);
				System.out.println("wangting: response" + response);
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(response);

					String result = jsonObject.getString("result");
					if (result.equals("success")) {
						Toast.makeText(InitiateBusinessAffairActivity.this, "发单成功",
								100).show();
						//gotoActivity(LoginActivity.class, false);	
						finish();
					} else{
						Toast.makeText(InitiateBusinessAffairActivity.this, "发单失败",
								100).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        //可以根据多个请求代码来作相应的操作  
		if(resultCode == Activity.RESULT_OK){
			if(requestCode == Constants.REQUEST_ADDRESS){
				addressName = data.getStringExtra("addressName");
				longitude = data.getStringExtra("longitude");
				latitude = data.getStringExtra("latitude");
				editBusinessaffairAddress.setText(addressName+"("+longitude+","+latitude+")");
			}
			if(requestCode == Constants.REQUEST_SERVICETYPE){
				String servicetypeContent = data.getStringExtra("servicetypeContent");
				servicetypeId = data.getStringExtra("servicetypeId");				
				txtServicetype.setText(servicetypeContent);
			}
		}
       
        super.onActivityResult(requestCode, resultCode, data);  
    }
	
	
	// 开始录音
	private void startRecord() {	
		if(mRecorder == null){
			mRecorder = new MediaRecorder();	
		}
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mRecorder.setOutputFile(fileName);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		try {
			mRecorder.prepare();
			mRecorder.start();
		} catch (IOException e) {
			Log.e(LOG_TAG, "prepare() failed");
		}

	}

	// 停止录音
	private void stopRecord() {
		if(mRecorder != null){
			mRecorder.stop();
			mRecorder.release();
			mRecorder = null;
		}
		
		stopPlay();//重置mediaPlayer
	}

	// 播放录音
	private void startPlay() {
		try {
			if(mPlayer == null){
				mPlayer = new MediaPlayer();
				mPlayer.setDataSource(fileName);				
			}else {
				mPlayer.stop();
			}
			mPlayer.prepare();
			mPlayer.start();
		} catch (IOException e) {
			Log.e(LOG_TAG, "播放失败");
		}
	}

	// 停止播放录音
	private void stopPlay() {
		if(mPlayer !=null){
			mPlayer.stop();
			mPlayer.release();
			mPlayer = null;
		}
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopPlay();
	}
}

