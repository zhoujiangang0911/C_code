package cn.wislight.publicservice.ui.user;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cn.wislight.publicservice.R;
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.entity.BusinessAffair;
import cn.wislight.publicservice.ui.LoginActivity;
import cn.wislight.publicservice.ui.RegisterPersonalActivity;
import cn.wislight.publicservice.ui.commercialtenant.BaiduMapSelectPositionActivity;
import cn.wislight.publicservice.ui.commercialtenant.SelectServiceTypeActivity;
import cn.wislight.publicservice.ui.user.goverment.CreateUserGovermentActivity;
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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Administrator
 *  发起商务详页
 */
public class CreateBusinessAffairActivity extends BaseActivity implements OnClickListener {
	private static final String LOG_TAG = "AudioRecordTest";
	private TextView txtPointselect;
	private EditText txtBusinessaffairContent;
	private EditText txtBusinessaffairPrice;
	private TextView editBusinessaffairAddress;
	
	private LinearLayout layoutSelectservicetype;
	private LinearLayout layoutVoiceRecord;
	private TextView txtServicetype;
	private TextView textDetailsServiceType;
	
	
	
	private ImageButton btnBusinessaffairSpeak;
	private ImageButton btnBusinessaffairVoiceRecord;
	private ImageButton btnCreateBusinessAffair;
	
	private String addressName;
	private String longitude;
	private String latitude;
	private TextView txtCommonlyAddressSelect;
	private String servicetypeId;
	
	private RadioButton radioButtonBuy;
	private RadioButton radioButtonSell;
	private String buyOrSell;
	
	//语音操作对象  
    private MediaPlayer mPlayer = null;  
    private MediaRecorder mRecorder = null; 
	
    //语音文件保存路径  
    private String fileName = null;
    	
	@Override
	public void setUpView() {
		setContentView(R.layout.activity_user_create_businessaffair);
		radioButtonBuy = (RadioButton) findViewById(R.id.radiobut_buy);
		radioButtonSell = (RadioButton) findViewById(R.id.radiobut_sell);
		
		txtPointselect = (TextView) findViewById(R.id.pointselect);
		txtCommonlyAddressSelect = (TextView) findViewById(R.id.commonlyAddressSelect);
		txtBusinessaffairContent = (EditText) findViewById(R.id.businessaffairContent);
		txtBusinessaffairPrice = (EditText) findViewById(R.id.et_businessaffairPrice);
		editBusinessaffairAddress  = (TextView) findViewById(R.id.tv_businessaffairAddress);
		
		layoutSelectservicetype  = (LinearLayout) findViewById(R.id.selectservicetype);
		layoutVoiceRecord  = (LinearLayout) findViewById(R.id.layoutVoiceRecord);
		txtServicetype  =  (TextView) findViewById(R.id.servicetype);
		textDetailsServiceType = (TextView) findViewById(R.id.servicetype);
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
				case MotionEvent.ACTION_DOWN://按钮按下开始录音弹出对话框
					Toast.makeText(CreateBusinessAffairActivity.this, "请说话", 100).show();
					
					CreateBusinessAffairActivity.this.startRecord();
					break;
				case MotionEvent.ACTION_UP: //按钮抬起时对话框消失
					Toast.makeText(CreateBusinessAffairActivity.this, "录音完毕", 100).show();
					CreateBusinessAffairActivity.this.stopRecord();
					break;
				
				}
				return true;
			}
			
		});
		
		 //设置sdcard的路径  
        fileName = Environment.getExternalStorageDirectory().getAbsolutePath();  
        fileName += "/businessaudiorecord.3gp";
        File tempFile = new File(fileName);
        if(tempFile.exists()){
        	tempFile.delete();
        }
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
			Intent intent = new Intent(this,BaiduMapSelectPositionActivity.class);
			startActivityForResult(intent, Constants.REQUEST_ADDRESS);			
			break;	
		case R.id.commonlyAddressSelect:
			Intent intentCommonlyAddress = new Intent(this, UserBusinessSelectCommonlyAddressActivity.class);
			startActivityForResult(intentCommonlyAddress, Constants.REQUEST_COMMONLYADDRESS);
			break;	
		case R.id.businessaffairVoiceRecord:
			Toast.makeText(CreateBusinessAffairActivity.this,
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
		String ServiceType =textDetailsServiceType.getText().toString();
		addressName = editBusinessaffairAddress.getText().toString();
		Log.i("----", "---"+ServiceType);
		if(addressName != null && addressName.equals("")){
			Toast.makeText(this, "请选择地理位置", 100).show();
			return;
		}
		if(ServiceType != null && ServiceType.equals("")){
			Toast.makeText(this, "请选择服务类型", 100).show();
			return;
		}
		if(content != null && content.trim().length() <1){
			Toast.makeText(this, "内容不能为空", 100).show();
			return;
		}

		if(price != null && price.trim().length() <1){
			Toast.makeText(this, "价格不能为空", 100).show();
			return;
		}
		
		
		
		params.put("businessaffair.content", content);
		params.put("businessaffair.price ", price);

		params.put("businessaffair.address.name", addressName);
		params.put("businessaffair.address.longitude ", Double.parseDouble(longitude));
		params.put("businessaffair.address.latitude ", Double.parseDouble(latitude));	

		params.put("businessaffair.servicetypeid", servicetypeId);
		
		//设置发其的买卖类型
		if (radioButtonBuy.isChecked()) {
			buyOrSell= BusinessAffair.BUYORSELL.BUY;
		}else {
			buyOrSell=BusinessAffair.BUYORSELL.SELL;
		}				
		params.put("businessaffair.buyorsell", buyOrSell);		
		
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
				Toast.makeText(CreateBusinessAffairActivity.this,
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
						Toast.makeText(CreateBusinessAffairActivity.this, "发起商务成功",
								100).show();
						//gotoActivity(LoginActivity.class, false);	
						finish();
					} else{
						Toast.makeText(CreateBusinessAffairActivity.this, "发起商务失败"+result,
								100).show();
					}
				} catch (JSONException e) {
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
			if(requestCode == Constants.REQUEST_COMMONLYADDRESS){
				addressName = data.getStringExtra("addressName");
				longitude = data.getStringExtra("longitude");
				latitude = data.getStringExtra("latitude");
				if(longitude!=null && longitude.length()>0 && longitude!=null && longitude.length()>0){
					editBusinessaffairAddress.setText(addressName+"("+longitude+","+latitude+")");
				} else {
					editBusinessaffairAddress.setText(addressName);
				}
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

