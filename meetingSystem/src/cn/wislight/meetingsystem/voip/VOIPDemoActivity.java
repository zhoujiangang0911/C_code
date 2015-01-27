package cn.wislight.meetingsystem.voip;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.util.Constants;

import com.hisun.phone.core.voice.CCPCall;
import com.hisun.phone.core.voice.CCPCall.InitListener;
import com.hisun.phone.core.voice.Device;
import com.hisun.phone.core.voice.Device.CallType;
import com.hisun.phone.core.voice.Device.Codec;
import com.hisun.phone.core.voice.DeviceListener.Reason;
import com.hisun.phone.core.voice.listener.OnVoIPListener;

public class VOIPDemoActivity extends Activity implements OnClickListener,OnVoIPListener  {
	private String	callingId;
	private String calledId;
	private EditText et_calloutphone;
	private EditText et_myphone;
	private Device device;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.voip_main);
		initViews();	 
			
		device = CCPHelper.getInstance().getDeviceHelper();
		
		calledId = getIntent().getStringExtra(Device.CALLID);
		System.out.println("wangting: on create voipdemoactivity"+calledId);
	}

	@Override
	protected void onResume() {
		 
		super.onResume();
		calledId = getIntent().getStringExtra(Device.CALLID);
		System.out.println("wangting: on create voipdemoactivity onresume"+calledId);
	}
	@Override
	protected void onNewIntent(Intent intent) {
		
		super.onNewIntent(intent);
		if(intent!=null&&intent.getExtras()!=null){
			calledId = intent.getExtras().getString(Device.CALLID);
			String mVoipAccount =  intent.getExtras().getString(Device.CALLER);
		}
		System.out.println("wangting: voipdemoactivity onNewInstent:"+calledId);
	}
	
	private void initViews() {
		findViewById(R.id.call).setOnClickListener(this);
		findViewById(R.id.listen).setOnClickListener(this);
		findViewById(R.id.off).setOnClickListener(this);
		findViewById(R.id.btn_refuse).setOnClickListener(this);
		findViewById(R.id.callback).setOnClickListener(this);
		
		et_calloutphone = (EditText) findViewById(R.id.et_calloutphone);
		et_myphone = (EditText) findViewById(R.id.et_myphone);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.call:
			 
			if(device!=null && !TextUtils.isEmpty(et_calloutphone.getText()))
				callingId= device.makeCall(Device.CallType.VOICE, et_calloutphone.getText().toString());
			break;
		case R.id.listen:
			if(device!=null && !TextUtils.isEmpty(calledId)){
				device.acceptCall(calledId);
			}
			break;
		case R.id.btn_refuse:
			if(device!=null && !TextUtils.isEmpty(calledId)){
				device.rejectCall(calledId,1001);
			}
			break;
		case R.id.off:
			if(device!=null){
				try {
					if(!TextUtils.isEmpty(calledId))
						device.releaseCall(calledId);
					if(!TextUtils.isEmpty(callingId))
						device.releaseCall(callingId);
				} catch (Exception e) {
					Log.i(this.getClass().getName(), e.getMessage());
				}
			}
			break;
		case R.id.callback:
			if(device!=null &&!TextUtils.isEmpty(et_calloutphone.getText())&&!TextUtils.isEmpty(et_myphone.getText()))
			device.makeCallback(et_myphone.getText().toString(),et_calloutphone.getText().toString(),"","");
			break;
		}
	}

	@Override
	public void onCallProceeding(String callid) {
		Log.i(this.getClass().getName(),"onCallProceeding");
	}


	@Override
	public void onCallAlerting(String callid) {
		Log.i(this.getClass().getName(),"onCallAlerting");
	}

	@Override
	public void onCallReleased(String callid) {
		Log.i(this.getClass().getName(),"onCallReleased");
		if(callid.equals(callingId))
			callingId=null;
		if(callid.equals(calledId))
			calledId=null;
	}

	@Override
	public void onCallAnswered(String callid) {
		Log.i(this.getClass().getName(),"onCallAnswered");
	}

	@Override
	public void onCallPaused(String callid) {
		Log.i(this.getClass().getName(),"onCallPaused");
	}

	@Override
	public void onCallPausedByRemote(String callid) {
		Log.i(this.getClass().getName(),"onCallPausedByRemote");
	}

	@Override
	public void onMakeCallFailed(String callid, Reason reason) {
		Log.i(this.getClass().getName(),"onMakeCallFailed reason "+reason.getStatus());
	}

	@Override
	public void onCallTransfered(String callid, String destination) {
		Log.i(this.getClass().getName(),"onCallTransfered");
	}

	@Override
	public void onCallback(int reason, String src, String dest) {
		Log.i(this.getClass().getName(),"onCallback");
	}

	@Override
	public void onCallMediaUpdateRequest(String callid, int reason) {
	}

	@Override
	public void onCallMediaUpdateResponse(String callid, int reason) {
	}

	@Override
	public void onCallVideoRatioChanged(String callid, String resolution) {
	}

	@Override
	public void onCallMediaInitFailed(String callid, int reason) {
	}

	public void onTransferStateSucceed(String callid, boolean result) {
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//CCPCall.shutdown();
	}

	@Override
	public void onCallMediaInitFailed(String arg0, CallType arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSwitchCallMediaTypeRequest(String arg0, CallType arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSwitchCallMediaTypeResponse(String arg0, CallType arg1) {
		// TODO Auto-generated method stub
		
	}
}

