package cn.wislight.publicservice.ui.user.goverment;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.wislight.publicservice.R;
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.ui.commercialtenant.BaiduMapSelectPositionActivity;
import cn.wislight.publicservice.ui.commercialtenant.SelectServiceTypeActivity;
import cn.wislight.publicservice.util.PublicServiceClient;
/**
 * 发起政务主页
 */
public class CreateUserGovermentActivity extends BaseActivity implements OnClickListener{
	private static final String LOG_TAG = "CreateUserGovermentActivity";
	
	private Button commitGovermentAffair;
	private ImageButton govermentffairSpeak;
	private Button btnSpeakAgainGoverment,btnSpeakSendGoverment;
	private TextView user_goverment_text;
	private ImageButton userCenterImageButton;
	private LinearLayout userSelectLocation;
	private LinearLayout userSelectPeople;
	private TextView showUserSelectLocation;
	private EditText showUserSelectPeople;

	private EditText txtGovermentaffairContent;
	private EditText txtGovermentaffairPrice;
	private String longitude = "34.218356";
	private String latitude = "108.89792";
	private TextView userServiceType;
	private LinearLayout selectservicetype;
	private String serviceTypeId;
	
	private TextView textDetailsServiceType;
	private LinearLayout layoutgovermentffairSpeak; 
	private LinearLayout layoutgovermentffairPlayRecord;
	private ImageButton govermentffairPlayRecord;
	
	//语音操作对象  
    private MediaPlayer mPlayer = null;  
    private MediaRecorder mRecorder = null; 
    //语音文件保存路径  
    private String fileName = null;
    
    private String address;
    
	@Override
	public void setUpView() {
		setContentView(R.layout.activity_create_govermentaffair_commerce);
		
		commitGovermentAffair = (Button)findViewById(R.id.createGovermentAffair);
		govermentffairSpeak = (ImageButton)findViewById(R.id.govermentffairSpeak);
		
		btnSpeakAgainGoverment = (Button)findViewById(R.id.btnSpeakAgainGoverment);
		btnSpeakSendGoverment = (Button)findViewById(R.id.btnSpeakSendGoverment);
		user_goverment_text = (TextView)findViewById(R.id.user_goverment_text);
		userServiceType = (TextView)findViewById(R.id.servicetype);
		showUserSelectLocation = (TextView)findViewById(R.id.showUserSelectLocation);
		showUserSelectPeople = (EditText)findViewById(R.id.showUserSelectPeople);
		textDetailsServiceType = (TextView) findViewById(R.id.servicetype);
		userSelectLocation = (LinearLayout)findViewById(R.id.userSelectLocation);
		userSelectPeople = (LinearLayout)findViewById(R.id.userSelectPeople);
		selectservicetype = (LinearLayout)findViewById(R.id.selectservicetype);
	    
		txtGovermentaffairContent = (EditText)findViewById(R.id.govermentaffairContent);
		txtGovermentaffairPrice = (EditText)findViewById(R.id.govermentaffairPrice);
		
		layoutgovermentffairSpeak = (LinearLayout)findViewById(R.id.layoutgovermentffairSpeak);		
		layoutgovermentffairPlayRecord = (LinearLayout)findViewById(R.id.layoutgovermentffairPlayRecord);
		govermentffairPlayRecord = (ImageButton) findViewById(R.id.govermentffairPlayRecord);
		
		 //设置sdcard的路径  
        fileName = Environment.getExternalStorageDirectory().getAbsolutePath();  
        fileName += "/audiorecordtest.3gp";
        File tempFile = new File(fileName);
        if(tempFile.exists()){
        	tempFile.delete();
        }
	}
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case 101:
			if(resultCode == Activity.RESULT_OK){
			
				String addressName =data.getStringExtra("addressName");
				String longitude =data.getStringExtra("longitude");
				String latitude =data.getStringExtra("latitude");
				showUserSelectLocation.setText(addressName+"("+longitude+","+latitude+")");
			}
			break;
		case 102:
			if(resultCode == Activity.RESULT_OK){
				String nameContact =data.getStringExtra("name");
				showUserSelectPeople.setText(nameContact);
			}
		case 103:
			if(resultCode == Activity.RESULT_OK){
					String servicetypeContent = data.getStringExtra("servicetypeContent");
					serviceTypeId = data.getStringExtra("servicetypeId");
					userServiceType.setText(servicetypeContent);
			}
			break;

		default:
			break;
		}
	}
	@Override
	public void setListener() {
		commitGovermentAffair.setOnClickListener(this);


		btnSpeakAgainGoverment.setOnClickListener(this);
		btnSpeakSendGoverment.setOnClickListener(this);
			
		userSelectPeople.setOnClickListener(this);
		userSelectLocation.setOnClickListener(this);
		selectservicetype.setOnClickListener(this);
		
		
		govermentffairSpeak.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:
					Toast.makeText(CreateUserGovermentActivity.this, "请说话", 100).show();
					CreateUserGovermentActivity.this.startRecord();
					break;
				case MotionEvent.ACTION_UP:
					Toast.makeText(CreateUserGovermentActivity.this, "录音完毕", 100).show();
					CreateUserGovermentActivity.this.stopRecord();
					
					layoutgovermentffairSpeak.setVisibility(View.GONE);
					layoutgovermentffairPlayRecord.setVisibility(View.VISIBLE);
					break;
				
				}
				return true;
			}
			
		});
		govermentffairPlayRecord.setOnClickListener(this);
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.createGovermentAffair:
			createGovermentAffair();
			break;

		case R.id.btnSpeakAgainGoverment:
			layoutgovermentffairSpeak.setVisibility(View.VISIBLE);
			layoutgovermentffairPlayRecord.setVisibility(View.GONE);
			break;
		case R.id.btnSpeakSendGoverment:
			btnSpeakAgainGoverment.setVisibility(View.GONE);
			btnSpeakSendGoverment.setVisibility(View.GONE);
			user_goverment_text.setVisibility(View.VISIBLE);
			Toast.makeText(this,"发送成功！", 2).show();
			break;
		case R.id.govermentffairPlayRecord:
			Toast.makeText(CreateUserGovermentActivity.this,"开始放音", Toast.LENGTH_SHORT)
					.show();
			startPlay();
			break;
			
		case R.id.userSelectPeople:
			Intent inten = new Intent(this,UserSelectContactActivity.class);
			startActivityForResult(inten,102);
			break;
		case R.id.userSelectLocation:
			Intent intent = new Intent(this,UserSelectBaiduMapSelectPositionActivity.class);
			startActivityForResult(intent,101);
			break;
		case R.id.selectservicetype:
			Intent intentServiceType = new Intent(this, SelectServiceTypeActivity.class);
			startActivityForResult(intentServiceType,103); 
			break;
		default:
			break;
		}
	}
	
	private void createGovermentAffair(){
		String url = "publicservice/govermentaffair_create.htm?json=true";
		RequestParams params = new RequestParams();
		String content = txtGovermentaffairContent.getText().toString();
		String price = txtGovermentaffairPrice.getText().toString();
		String ServiceType =textDetailsServiceType.getText().toString();
		address = showUserSelectLocation.getText().toString();
		if(address != null && address.equals("")){
			Toast.makeText(this, "请选择地理位置", 100).show();
			return;
		}
		if(ServiceType != null && content.trim().length() <1){
			Toast.makeText(this, "请选择服务类型", 100).show();
			return;
		}
		
		if(content != null && content.trim().length() <1){
			Toast.makeText(this, "内容不能为空", 100).show();
			return;
		}

		if(price != null && content.trim().length() <1){
			Toast.makeText(this, "价格不能为空", 100).show();
			return;
		}
		
		params.put("govermentaffair.content", content);
		params.put("govermentaffair.price", price);
		params.put("govermentaffair.servicetypeid", serviceTypeId);

		//String fulladdress = editBusinessaffairAddress.getText().toString();
		String fulladdress = "橡树街区(34.218356;108.89792)";
		int index = fulladdress.indexOf("(");
		 address = "";
		if(index<0){
			address = fulladdress;
		}else{
			address = fulladdress.substring(0,index);
		}
		params.put("govermentaffair.address.name", address);
		params.put("govermentaffair.address.longitude", Double.parseDouble(longitude));
		params.put("govermentaffair.address.latitude", Double.parseDouble(latitude));
		
		if(fileName != null){
			File voiceFile = new File(fileName);
			if(voiceFile != null && voiceFile.length()>0){
				try {
					params.put("upload", voiceFile);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		///loadingdiag.setText(getString(R.string.uploading));
		//loadingdiag.show();
		PublicServiceClient.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] arg1, byte[] body,
					Throwable error) {
				//loadingdiag.hide();
				Toast.makeText(CreateUserGovermentActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onSuccess(int stautsCode, Header[] arg1, byte[] body) {
				// loadingdiag.hide();
				String response = new String(body);
				System.out.println("wangting: response" + response);
				Log.i(LOG_TAG, "---->>>>>"+response);
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(response);
                       
					String result = jsonObject.getString("result");
					if (result.equals("success")) {
						Toast.makeText(CreateUserGovermentActivity.this, "发起政务成功",
								100).show();
						//gotoActivity(LoginActivity.class, false);	
						finish();
					} else{
						Toast.makeText(CreateUserGovermentActivity.this, "发起政务失败"+result,
								100).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}
	
	
	// 开始录音
	private void startRecord() {
		if (mRecorder == null) {
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
		if (mRecorder != null) {
			mRecorder.stop();
			mRecorder.release();
			mRecorder = null;
		}

		stopPlay();// 重置mediaPlayer
	}

	// 播放录音
	private void startPlay() {
		try {
			if (mPlayer == null) {
				mPlayer = new MediaPlayer();
				mPlayer.setDataSource(fileName);
			} else {
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
		if (mPlayer != null) {
			mPlayer.stop();
			mPlayer.release();
			mPlayer = null;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopPlay();
	}
}
