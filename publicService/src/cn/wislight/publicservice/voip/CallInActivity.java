package cn.wislight.publicservice.voip;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.wislight.publicservice.base.BaseActivity;

import com.CCP.phone.CameraInfo;
import com.hisun.phone.core.voice.Device;

import com.hisun.phone.core.voice.Device.CallType;
import com.hisun.phone.core.voice.DeviceListener.Reason;
import com.hisun.phone.core.voice.listener.OnProcessOriginalAudioDataListener;
import com.hisun.phone.core.voice.util.Log4Util;
import com.hisun.phone.core.voice.util.VoiceUtil;


import cn.wislight.publicservice.R;

/**
 * 
 * Voip incoming interface, called for display and operation process of the
 * call��Oo
 * 
 * @version 1.0.0
 */
public class CallInActivity extends BaseActivity implements OnClickListener {
	// public class CallInActivity extends AudioVideoCallActivity implements
	// OnClickListener {

	// 键盘
	private ImageView mDiaerpadBtn;
	// 键盘区
	private LinearLayout mDiaerpad;
	// 挂机按钮
	private ImageView mVHangUp;
	// 输入号码
	private LinearLayout mInputNumberArea;
	private EditText mVoipInputEt;
	private Button mBMakecall;

	// 呼叫区域
	private LinearLayout mCallingArea;
	// 挂断区域
	private LinearLayout mHangupArea;
	// 动态状态显示区
	private TextView mCallStateTips;
	private Chronometer mChronometer;
	// 名称显示区
	private TextView mVtalkName;
	// 号码显示区
	// private TextView mVtalkNumber;

	private TextView mCallStatus;
	// 号码
	private String mPhoneNumber;
	// 名称
	private String mNickName;
	// 通话 ID
	// private String mCurrentCallId;
	// voip 账号
	private String mVoipAccount;
	// 透传号码参数
	private static final String KEY_TEL = "tel";
	// 透传名称参数
	private static final String KEY_NAME = "nickname";
	private static final int REQUEST_CODE_VOIP_CALL = 120;
	private static final int REQUEST_CODE_CALL_TRANSFER = 130;
	private boolean isDialerShow = false;

	private Button mVideoStop;
	private Button mVideoBegin;
	private ImageView mVideoIcon;
	private RelativeLayout mVideoTipsLy;

	private TextView mVideoTopTips;
	// private TextView mVideoCallTips;
	private SurfaceView mVideoView;
	// Remote Video
	private FrameLayout mVideoLayout;

	private ImageButton mCameraSwitch;

	CameraInfo[] cameraInfos;
	Intent currIntent;

	int numberOfCameras;
	private String mCurrentCallId;
	private CallType mCallType;
	private boolean isIncomingCall;
	private boolean isConnect;
	private TextView mVtalkNumber;
	private ImageView mCallTransferBtn;
	private VOIPCallHandle mVOIPCallHandle;

	private ImageButton button_callin_reject;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("wangting: enter callinactivity");
		mVOIPCallHandle = new VOIPCallHandle(this);
		CCPHelper.getInstance().setHandler(mVOIPCallHandle);

		//isIncomingCall = true;

		currIntent = getIntent();
		initialize(currIntent);
		checkmakecall(currIntent);//查询是否发起会议
		initResourceRefs();
		
		
		// registerReceiver(new String[] { CCPIntentUtils.INTENT_P2P_ENABLED });
	}

	private void initCallinViewItems(){
		
	}
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		if (!isConnect) {

			// if (getCallHandler() != null) {
			// getCallHandler().removeCallbacks(finish);
			// }
			releaseCurrCall(false);
			currIntent = intent;
			initialize(currIntent);
			initResourceRefs();
		}

	}

	// Initialize all UI elements from resources.

	private void initResourceRefs() {
		isConnect = false;
		if (mCallType == Device.CallType.VIDEO) {
			// video ..
			/*
			 * setContentView(R.layout.layout_video_activity);
			 * 
			 * findViewById(R.id.video_botton_cancle).setVisibility(View.GONE);
			 * 
			 * mVideoTipsLy = (RelativeLayout)
			 * findViewById(R.id.video_call_in_ly);
			 * mVideoTipsLy.setVisibility(View.VISIBLE);
			 * 
			 * mVideoIcon = (ImageView) findViewById(R.id.video_icon);
			 * 
			 * mVideoTopTips = (TextView) findViewById(R.id.notice_tips); // Top
			 * tips view invited ...
			 * mVideoTopTips.setText(getString(R.string.str_video_invited_recivie
			 * , mVoipAccount.substring(mVoipAccount.length() - 3,
			 * mVoipAccount.length()))); // 底部时间 mCallStateTips = (TextView)
			 * findViewById(R.id.video_call_tips); // 接受 mVideoBegin = (Button)
			 * findViewById(R.id.video_botton_begin);
			 * mVideoBegin.setVisibility(View.VISIBLE); // 拒绝 mVideoStop =
			 * (Button) findViewById(R.id.video_stop);
			 * mVideoStop.setEnabled(true);
			 * mVideoBegin.setOnClickListener(this);
			 * mVideoStop.setOnClickListener(this);
			 * 
			 * mVideoView = (SurfaceView) findViewById(R.id.video_view);
			 * mVideoView.getHolder().setFixedSize(240, 320); mLoaclVideoView =
			 * (RelativeLayout) findViewById(R.id.localvideo_view); mVideoLayout
			 * = (FrameLayout) findViewById(R.id.Video_layout);
			 * 
			 * findViewById(R.id.video_switch).setOnClickListener(this); ;
			 * 
			 * mCameraSwitch = (ImageButton) findViewById(R.id.camera_switch);
			 * mCameraSwitch.setOnClickListener(this);
			 * 
			 * mCallStatus = (TextView) findViewById(R.id.call_status);
			 * mCallStatus.setVisibility(View.GONE);
			 * 
			 * if (checkeDeviceHelper()) {
			 * 
			 * cameraInfos = getDeviceHelper().getCameraInfo();
			 * 
			 * // Find the ID of the default camera if (cameraInfos != null) {
			 * numberOfCameras = cameraInfos.length; }
			 * 
			 * // Find the total number of cameras available for (int i = 0; i <
			 * numberOfCameras; i++) { if (cameraInfos[i].index ==
			 * android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT) {
			 * defaultCameraId = i; comportCapbilityIndex(cameraInfos[i].caps);
			 * } }
			 * 
			 * getDeviceHelper().setVideoView(mVideoView, null); }
			 * 
			 * DisplayLocalSurfaceView();
			 */

		} else {
			setContentView(R.layout.layout_callin);
			if (!isIncomingCall) {
				mCallingArea = (LinearLayout) findViewById(R.id.layout_callin_bottom_show);
				mHangupArea = (LinearLayout) findViewById(R.id.layout_callin_bottom_show2);
				button_callin_reject = (ImageButton) findViewById(R.id.layout_callin_reject);
				button_callin_reject.setOnClickListener(this);
				
				mInputNumberArea = (LinearLayout) findViewById(R.id.layout_callin_bottom_show3);

				mCallingArea.setVisibility(View.GONE);
				mHangupArea.setVisibility(View.GONE);
				mInputNumberArea.setVisibility(View.VISIBLE);
				mVoipInputEt = (EditText) findViewById(R.id.voip_input);
				mBMakecall = (Button) findViewById(R.id.netphone_voip_call);
				return;
			}

			mVtalkName = (TextView) findViewById(R.id.layout_callin_name);
			mVtalkNumber = (TextView) findViewById(R.id.layout_callin_number);
			((ImageButton) findViewById(R.id.layout_callin_cancel))
					.setOnClickListener(this);
			((ImageButton) findViewById(R.id.layout_callin_accept))
					.setOnClickListener(this);
			setDisplayNameNumber();

		}
	}

	private void setDisplayNameNumber() {
		if (mCallType == Device.CallType.VOICE) {
			// viop call ...
			if (!TextUtils.isEmpty(mVoipAccount)) {
				mVtalkNumber.setText(mVoipAccount);
			}
		} else {
			// viop call ...
			if (!TextUtils.isEmpty(mPhoneNumber)) {
				mVtalkNumber.setText(mPhoneNumber);
				Log4Util.d(CCPHelper.DEMO_TAG, "[CallInActivity] mPhoneNumber "
						+ mPhoneNumber);
			}
			if (!TextUtils.isEmpty(mNickName)) {
				mVtalkName.setText(mNickName);
				Log4Util.d(CCPHelper.DEMO_TAG, "[CallInActivity] VtalkName"
						+ mVtalkName);
			} else {
				mVtalkName.setText(R.string.voip_unknown_user);
			}
		}
	}

	private void checkmakecall(Intent intent){
		Bundle extras = intent.getExtras();
		if(extras!=null &&!extras.getBoolean("isIncomingCall")){			
			//mVoipAccount = extras.getString("voip");
			mCallType = CallType.VOICE;
			Device device = CCPHelper.getInstance().getDeviceHelper();
			if(device == null){
				Toast.makeText(this, "device is null", 100).show();
				System.out.println("wangting: device is null");
				finish();
			}
			mCurrentCallId = CCPHelper.getInstance().getDeviceHelper()
					.makeCall(mCallType, extras.getString("voip"));
			mVoipAccount = extras.getString("voip");
			System.out.println("wangting: make call to " + extras.getString("voip"));
			return;
		}
	}
	private void initialize(Intent intent) {
		Bundle extras = intent.getExtras();
		if (extras == null) {
			// finish();
			return;
		}		
		if(extras.getBoolean("isIncomingCall") == false){		
			return;
		}
		isIncomingCall = true;
		mVoipAccount = extras.getString(Device.CALLER);
		mCurrentCallId = extras.getString(Device.CALLID);
		mCallType = (CallType) extras.get(Device.CALLTYPE);
		// 传入数据是否有误
		if (mVoipAccount == null || mCurrentCallId == null) {
			finish();
			return;
		}
		// 透传信息
		String[] infos = extras.getStringArray(Device.REMOTE);
		if (infos != null && infos.length > 0) {
			for (String str : infos) {
				if (str.startsWith(KEY_TEL)) {
					mPhoneNumber = VoiceUtil.getLastwords(str, "=");
				} else if (str.startsWith(KEY_NAME)) {
					mNickName = VoiceUtil.getLastwords(str, "=");
				}
			}
		}

		// if(checkeDeviceHelper()) {
		// getDeviceHelper().setProcessDataEnabled(mCurrentCallId, false, new
		// OnCallProcessDataListener() {
		//
		// @Override
		// public byte[] onCallProcessData(byte[] b, int transDirection) {
		// // decode.
		// String base64qes =
		// Cryptos.toBase64QES("afnronvornvocnxwornvownefwvnv", new String(b));
		// return base64qes.getBytes();
		// }
		// });
		// }

	}

	public void initCallHold() {
		Log4Util.d(CCPHelper.DEMO_TAG,
				"[CallInActivity] initCallHold.收到呼叫连接，初始化通话界面.");
		isConnect = true;
		setContentView(R.layout.layout_call_interface);
		mCallTransferBtn = (ImageView) findViewById(R.id.layout_callin_transfer);
		mCallTransferBtn.setOnClickListener(this);
		mCallStateTips = (TextView) findViewById(R.id.layout_callin_duration);
		mCallMute = (ImageView) findViewById(R.id.layout_callin_mute);
		mCallHandFree = (ImageView) findViewById(R.id.layout_callin_handfree);
		mVHangUp = (ImageView) findViewById(R.id.layout_call_reject);
		mVtalkName = (TextView) findViewById(R.id.layout_callin_name);
		mVtalkName.setVisibility(View.VISIBLE);
		mVtalkNumber = (TextView) findViewById(R.id.layout_callin_number);

		mCallStatus = (TextView) findViewById(R.id.call_status);
		// mCallStatus.setVisibility(View.VISIBLE);

		// 显示时间，隐藏状态
		// 2013/09/23
		// Show call state position is used to display the packet loss rate and
		// delay
		// mCallStateTips.setVisibility(View.GONE);

		// 键盘
		mDiaerpadBtn = (ImageView) findViewById(R.id.layout_callin_diaerpad);
		mDiaerpad = (LinearLayout) findViewById(R.id.layout_diaerpad);

		setupKeypad();
		mDmfInput = (EditText) findViewById(R.id.dial_input_numer_TXT);

		mDiaerpadBtn.setOnClickListener(this);
		mCallMute.setOnClickListener(this);
		mCallHandFree.setOnClickListener(this);
		mVHangUp.setOnClickListener(this);

		setDisplayNameNumber();
		initCallTools();
	}

	public void clickMakecall(View v) {
		System.out.println("wangting: make call number="
				+ mVoipInputEt.getText().toString());
		mCurrentCallId = CCPHelper.getInstance().getDeviceHelper()
				.makeCall(CallType.VOICE, mVoipInputEt.getText().toString());
		//mVoipAccount = mVoipInputEt.getText().toString();
		mCallType = CallType.VOICE;

		System.out.println("wangting: callid="+mCurrentCallId);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// case R.id.btn_select_voip: // select voip ...
		// Intent intent = new Intent(this, InviteInterPhoneActivity.class);
		// intent.putExtra("create_to",
		// InviteInterPhoneActivity.CREATE_TO_VOIP_CALL);
		// startActivityForResult(intent, REQUEST_CODE_VOIP_CALL);

		// keypad
		case R.id.zero: {
			keyPressed(KeyEvent.KEYCODE_0);
			return;
		}
		case R.id.one: {
			keyPressed(KeyEvent.KEYCODE_1);
			return;
		}
		case R.id.two: {
			keyPressed(KeyEvent.KEYCODE_2);
			return;
		}
		case R.id.three: {
			keyPressed(KeyEvent.KEYCODE_3);
			return;
		}
		case R.id.four: {
			keyPressed(KeyEvent.KEYCODE_4);
			return;
		}
		case R.id.five: {
			keyPressed(KeyEvent.KEYCODE_5);
			return;
		}
		case R.id.six: {
			keyPressed(KeyEvent.KEYCODE_6);
			return;
		}
		case R.id.seven: {
			keyPressed(KeyEvent.KEYCODE_7);
			return;
		}
		case R.id.eight: {
			keyPressed(KeyEvent.KEYCODE_8);
			return;
		}
		case R.id.nine: {
			keyPressed(KeyEvent.KEYCODE_9);
			return;
		}
		case R.id.star: {
			keyPressed(KeyEvent.KEYCODE_STAR);
			return;
		}
		case R.id.pound: {
			keyPressed(KeyEvent.KEYCODE_POUND);
			return;
		}
		// case R.id.netphone_voip_call:
		// System.out.println("wangting: make call number="+mVoipInputEt.getText().toString());
		// CCPHelper.getInstance().getDeviceHelper().makeCall(CallType.VOICE,
		// mVoipInputEt.getText().toString());

		// break;
		case R.id.layout_callin_accept:
			// case R.id.video_botton_begin: // video ..
			// 接受呼叫
			// mTime = 0;
			try {
				if (CCPHelper.getInstance().getDeviceHelper() != null
						&& mCurrentCallId != null) {
					CCPHelper.getInstance().getDeviceHelper()
							.acceptCall(mCurrentCallId);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			Log4Util.d(CCPHelper.DEMO_TAG, "[CallInActivity] acceptCall...");
			break;
		case R.id.layout_callin_mute:
			// 设置静音
			setMuteUI();
			break;
		case R.id.layout_callin_handfree:
			// 设置免提
			sethandfreeUI();
			break;

		case R.id.layout_callin_diaerpad:

			// 设置键盘
			setDialerpadUI();
			break;
		case R.id.layout_callin_cancel:
		case R.id.layout_call_reject:
			doHandUpReleaseCall();
			break;
		case R.id.layout_callin_reject:
			getDeviceHelper().releaseCall(mCurrentCallId);//中断呼出
			break;

		// video back ...
		/*
		 * case R.id.video_stop: mVideoStop.setEnabled(false);
		 * doHandUpReleaseCall();
		 * 
		 * break;
		 * 
		 * case R.id.video_switch: if (checkeDeviceHelper()) { CallType callType
		 * = getDeviceHelper().getCallType(mCurrentCallId); if (callType ==
		 * Device.CallType.VOICE) {
		 * getDeviceHelper().updateCallType(mCurrentCallId,
		 * Device.CallType.VIDEO); } else {
		 * getDeviceHelper().updateCallType(mCurrentCallId,
		 * Device.CallType.VOICE); } }
		 * 
		 * break; case R.id.camera_switch: // check for availability of multiple
		 * cameras if (cameraInfos.length == 1) { AlertDialog.Builder builder =
		 * new AlertDialog.Builder(this);
		 * builder.setMessage(this.getString(R.string
		 * .camera_alert)).setNeutralButton(R.string.dialog_alert_close, null);
		 * AlertDialog alert = builder.create(); alert.show(); return; }
		 * mCameraSwitch.setEnabled(false);
		 * 
		 * // OK, we have multiple cameras. // Release this camera ->
		 * cameraCurrentlyLocked cameraCurrentlyLocked = (cameraCurrentlyLocked
		 * + 1) % numberOfCameras;
		 * comportCapbilityIndex(cameraInfos[cameraCurrentlyLocked].caps);
		 * 
		 * if (checkeDeviceHelper()) {
		 * 
		 * getDeviceHelper().selectCamera(cameraCurrentlyLocked,
		 * mCameraCapbilityIndex, 15, Rotate.Rotate_Auto, false);
		 * 
		 * if (cameraCurrentlyLocked ==
		 * android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT) {
		 * defaultCameraId =
		 * android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT;
		 * Toast.makeText(CallInActivity.this, R.string.camera_switch_front,
		 * Toast.LENGTH_SHORT).show(); } else { defaultCameraId =
		 * android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK;
		 * Toast.makeText(CallInActivity.this, R.string.camera_switch_back,
		 * Toast.LENGTH_SHORT).show(); } } mCameraSwitch.setEnabled(true);
		 * break;
		 * 
		 * case R.id.layout_callin_transfer: // select voip ... Intent i = new
		 * Intent(this, GetNumberToTransferActivity.class);
		 * startActivityForResult(i, REQUEST_CODE_CALL_TRANSFER);
		 * 
		 * break;
		 */
		default:
			break;
		}
	}

	protected void doHandUpReleaseCall() {
		// super.doHandUpReleaseCall();
		try {
			if (isConnect) {
				// 通话中挂断
				if (checkeDeviceHelper() && mCurrentCallId != null) {
					getDeviceHelper().releaseCall(mCurrentCallId);
					// stopVoiceRecording(mCurrentCallId);
				}
			} else {
				// 呼入拒绝 
				if (checkeDeviceHelper() && mCurrentCallId != null) {
					getDeviceHelper().rejectCall(mCurrentCallId, 6);	
					
				}
				finish();
			}

			Log4Util.d(CCPHelper.DEMO_TAG,
					"[CallInActivity] call stop isConnect: " + isConnect);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 延时关闭界面

	final Runnable finish = new Runnable() {
		public void run() {
			finish();
		}
	};

	@Override
	protected void onDestroy() {
		releaseCurrCall(true);
		super.onDestroy();
	}

	private void releaseCurrCall(boolean releaseAll) {
		currIntent = null;
		// if (getCallHandler() != null && releaseAll) {
		// setCallHandler(null);
		// }
		mCallTransferBtn = null;
		mCallMute = null;
		mCallHandFree = null;
		mVHangUp = null;
		mCallStateTips = null;
		mVtalkName = null;
		mVtalkNumber = null;

		if (checkeDeviceHelper()) {
			if (isMute) {
				getDeviceHelper().setMute(!isMute);
			}
			if (isHandsfree) {
				getDeviceHelper().enableLoudsSpeaker(!isMute);
			}
			if (TextUtils.isEmpty(mCurrentCallId))
				stopVoiceRecording(mCurrentCallId);
		}
		mPhoneNumber = null;
		setAudioMode(AudioManager.MODE_NORMAL);
	}

	//
	// 用于挂断时修改按钮属性及关闭操作
	//
	private void finishCalling() {
		try {
			if (isConnect) {
				mChronometer.stop();
				mCallStateTips.setVisibility(View.VISIBLE);

				isConnect = false;

				if (mCallType == Device.CallType.VIDEO) {
					/*
					 * mVideoLayout.setVisibility(View.GONE);
					 * mVideoIcon.setVisibility(View.VISIBLE);
					 * mVideoTopTips.setVisibility(View.VISIBLE);
					 * mCameraSwitch.setVisibility(View.GONE);
					 * 
					 * mLoaclVideoView.removeAllViews();
					 * mLoaclVideoView.setVisibility(View.GONE);
					 * 
					 * 
					 * //if(mVideoStop.isEnabled()) { //
					 * mVideoTopTips.setText(getString //
					 * (R.string.str_video_call_end,
					 * //CCPConfig.VoIP_ID.substring(CCPConfig.VoIP_ID.length()
					 * - //3, CCPConfig.VoIP_ID.length()))); } else {
					 * 
					 * mVideoTopTips.setText(R.string.voip_calling_finish); // }
					 * // bottom can't click ...
					 */
				} else {
					mChronometer.setVisibility(View.GONE);
					mCallStateTips.setText(R.string.voip_calling_finish);
					mCallHandFree.setClickable(false);
					mCallMute.setClickable(false);
					// mCallTransferBtn.setClickable(false);
					mVHangUp.setClickable(false);
					mDiaerpadBtn.setClickable(false);
					mDiaerpadBtn
							.setImageResource(R.drawable.call_interface_diaerpad);
					mCallHandFree
							.setImageResource(R.drawable.call_interface_hands_free);
					mCallMute.setImageResource(R.drawable.call_interface_mute);
					mVHangUp.setBackgroundResource(R.drawable.call_interface_non_red_button);
				}

				// 延时关闭
				getCallHandler().postDelayed(finish, 3000);
			} else {
				finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void finishCalling(Reason reason) {
		try {
			isConnect = false;
			mChronometer.stop();
			mChronometer.setVisibility(View.GONE);
			mCallStateTips.setVisibility(View.VISIBLE);

			mCallHandFree.setClickable(false);
			mCallMute.setClickable(false);
			mVHangUp.setClickable(false);
			mDiaerpadBtn.setClickable(false);
			mDiaerpadBtn.setImageResource(R.drawable.call_interface_diaerpad);
			mCallHandFree
					.setImageResource(R.drawable.call_interface_hands_free);
			mCallMute.setImageResource(R.drawable.call_interface_mute);
			mVHangUp.setBackgroundResource(R.drawable.call_interface_non_red_button);
			getCallHandler().postDelayed(finish, 3000);
			// 处理通话结束状态
			if (reason == Reason.DECLINED || reason == Reason.BUSY) {
				mCallStateTips.setText(getString(R.string.voip_calling_refuse));
				getCallHandler().removeCallbacks(finish);
			} else {
				if (reason == Reason.CALLMISSED) {
					mCallStateTips
							.setText(getString(R.string.voip_calling_timeout));
				} else if (reason == Reason.MAINACCOUNTPAYMENT) {
					mCallStateTips
							.setText(getString(R.string.voip_call_fail_no_cash));
				} else if (reason == Reason.UNKNOWN) {
					mCallStateTips
							.setText(getString(R.string.voip_calling_finish));
				} else if (reason == Reason.NOTRESPONSE) {
					mCallStateTips.setText(getString(R.string.voip_call_fail));
				} else if (reason == Reason.VERSIONNOTSUPPORT) {
					mCallStateTips
							.setText(getString(R.string.str_voip_not_support));
				} else if (reason == Reason.OTHERVERSIONNOTSUPPORT) {
					mCallStateTips
							.setText(getString(R.string.str_other_voip_not_support));
				} else {

					// ThirdSquareError(reason);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void setDialerpadUI() {
		if (isDialerShow) {
			mDiaerpadBtn.setImageResource(R.drawable.call_interface_diaerpad);
			mDiaerpad.setVisibility(View.GONE);
			isDialerShow = false;
		} else {
			mDiaerpadBtn
					.setImageResource(R.drawable.call_interface_diaerpad_on);
			mDiaerpad.setVisibility(View.VISIBLE);
			isDialerShow = true;
		}

	}


	/*
	 * @Override protected void onCallAnswered(String callid) {
	 * super.onCallAnswered(callid); // answer Log4Util.d(CCPHelper.DEMO_TAG,
	 * "[CallInActivity] voip on call answered!!"); if (callid != null &&
	 * callid.equals(mCurrentCallId)) { if (mCallType == Device.CallType.VIDEO)
	 * { initResVideoSuccess(); getDeviceHelper().enableLoudsSpeaker(true); }
	 * else { initialize(currIntent); // voip other .. initCallHold(); }
	 * 
	 * mChronometer = (Chronometer) findViewById(R.id.chronometer);
	 * mChronometer.setBase(SystemClock.elapsedRealtime());
	 * mChronometer.setVisibility(View.VISIBLE); mChronometer.start(); if
	 * (getCallHandler() != null) {
	 * getCallHandler().sendMessage(getCallHandler()
	 * .obtainMessage(VideoActivity.WHAT_ON_CODE_CALL_STATUS)); }
	 * 
	 * startVoiceRecording(callid); } }
	 * 
	 * @Override protected void onCallReleased(String callid) {
	 * super.onCallReleased(callid); // 挂断 Log4Util.d(CCPHelper.DEMO_TAG,
	 * "[CallInActivity] voip on call released!!"); try { if (callid != null &&
	 * callid.equals(mCurrentCallId)) { stopVoiceRecording(callid);
	 * finishCalling(); // finish(); } } catch (Exception e) {
	 * e.printStackTrace(); } }
	 * 
	 * @Override protected void handleNotifyMessage(Message msg) {
	 * super.handleNotifyMessage(msg); switch (msg.what) { case
	 * VideoActivity.WHAT_ON_CODE_CALL_STATUS: if (!checkeDeviceHelper()) {
	 * return; }
	 * 
	 * if (!isConnect) { return; }
	 * 
	 * CallStatisticsInfo callStatistics =
	 * getDeviceHelper().getCallStatistics(mCallType); NetworkStatistic
	 * trafficStats = null; if (mCallType == CallType.VOICE || mCallType ==
	 * CallType.VIDEO) { trafficStats =
	 * getDeviceHelper().getNetworkStatistic(mCurrentCallId); } if
	 * (callStatistics != null) { int fractionLost =
	 * callStatistics.getFractionLost(); int rttMs = callStatistics.getRttMs();
	 * if (mCallType == CallType.VOICE && mCallStateTips != null) { if
	 * (trafficStats != null) {
	 * mCallStateTips.setText(getString(R.string.str_call_traffic_status, rttMs,
	 * (fractionLost / 255), trafficStats.getTxBytes() / 1024,
	 * trafficStats.getRxBytes() / 1024)); } else {
	 * 
	 * mCallStateTips.setText(getString(R.string.str_call_status, rttMs,
	 * (fractionLost / 255))); } } else { if (trafficStats != null) {
	 * addNotificatoinToView(getString(R.string.str_call_traffic_status, rttMs,
	 * (fractionLost / 255), trafficStats.getTxBytes() / 1024,
	 * trafficStats.getRxBytes() / 1024)); } else {
	 * addNotificatoinToView(getString(R.string.str_call_status, rttMs,
	 * (fractionLost / 255))); } } }
	 * 
	 * if (getCallHandler() != null) { Message callMessage =
	 * getCallHandler().obtainMessage(VideoActivity.WHAT_ON_CODE_CALL_STATUS);
	 * getCallHandler().sendMessageDelayed(callMessage, 4000); } break;
	 * 
	 * // This call may be redundant, but to ensure compatibility system event
	 * // more, // not only is the system call case
	 * CCPHelper.WHAT_ON_RECEIVE_SYSTEM_EVENTS:
	 * 
	 * doHandUpReleaseCall(); break; default: break; } }
	 * 
	 * // video .. private void initResVideoSuccess() {
	 * 
	 * mVideoLayout.setVisibility(View.VISIBLE);
	 * mVideoIcon.setVisibility(View.GONE);
	 * mCallStateTips.setText(getString(R.string.str_video_bottom_time,
	 * mVoipAccount.substring(mVoipAccount.length() - 3,
	 * mVoipAccount.length()))); mCallStateTips.setVisibility(View.VISIBLE);
	 * mVideoTopTips.setVisibility(View.GONE);
	 * mCameraSwitch.setVisibility(View.VISIBLE);
	 * 
	 * mVideoBegin.setVisibility(View.GONE); isConnect = true; }
	 */

	private EditText mDmfInput;
	private boolean isHandsfree;
	private ImageView mCallHandFree;
	private boolean isMute;
	private ImageView mCallMute;
	private boolean callRecordEnabled;

	void keyPressed(int keyCode) {
		if (!checkeDeviceHelper()) {
			return;
		}
		KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
		mDmfInput.getText().clear();
		mDmfInput.onKeyDown(keyCode, event);
		getDeviceHelper().sendDTMF(mCurrentCallId,
				mDmfInput.getText().toString().toCharArray()[0]);
	}

	private void setupKeypad() {
		// Setup the listeners for the buttons//
		findViewById(R.id.zero).setOnClickListener(this);
		findViewById(R.id.one).setOnClickListener(this);
		findViewById(R.id.two).setOnClickListener(this);
		findViewById(R.id.three).setOnClickListener(this);
		findViewById(R.id.four).setOnClickListener(this);
		findViewById(R.id.five).setOnClickListener(this);
		findViewById(R.id.six).setOnClickListener(this);
		findViewById(R.id.seven).setOnClickListener(this);
		findViewById(R.id.eight).setOnClickListener(this);
		findViewById(R.id.nine).setOnClickListener(this);
		findViewById(R.id.star).setOnClickListener(this);
		findViewById(R.id.pound).setOnClickListener(this);
	}

	/*
	 * @Override protected void onActivityResult(int requestCode, int
	 * resultCode, Intent data) { super.onActivityResult(requestCode,
	 * resultCode, data);
	 * 
	 * Log4Util.d(CCPHelper.DEMO_TAG,
	 * "[SelectVoiceActivity] onActivityResult: requestCode=" + requestCode +
	 * ", resultCode=" + resultCode + ", data=" + data);
	 * 
	 * // If there's no data (because the user didn't select a number and //
	 * just hit BACK, for example), there's nothing to do. if (requestCode !=
	 * REQUEST_CODE_VOIP_CALL) { if (data == null) { return; } } else if
	 * (resultCode != RESULT_OK) { Log4Util.d(CCPHelper.DEMO_TAG,
	 * "[SelectVoiceActivity] onActivityResult: bail due to resultCode=" +
	 * resultCode); return; }
	 * 
	 * String phoneStr = ""; if (data.hasExtra("VOIP_CALL_NUMNBER")) { Bundle
	 * extras = data.getExtras(); if (extras != null) { phoneStr =
	 * extras.getString("VOIP_CALL_NUMNBER"); } } if (mCurrentCallId != null) {
	 * int setCallTransfer = setCallTransfer(mCurrentCallId, phoneStr);
	 * if(setCallTransfer!=0){ Toast.makeText(getApplicationContext(),
	 * "呼转发起失败！", 1).show(); // mVtalkNumber.setText(phoneStr); } } else {
	 * Toast.makeText(getApplicationContext(), "通话已经不存在", 1).show(); } }
	 */

	private boolean checkeDeviceHelper() {
		return CCPHelper.getInstance().getDeviceHelper() != null;
	}

	private Device getDeviceHelper() {
		return CCPHelper.getInstance().getDeviceHelper();
	}

	/**
	 * 设置免提
	 */
	private void sethandfreeUI() {
		try {
			if (checkeDeviceHelper()) {
				getDeviceHelper().enableLoudsSpeaker(!isHandsfree);
				isHandsfree = getDeviceHelper().getLoudsSpeakerStatus();
			}

			if (isHandsfree) {
				mCallHandFree
						.setImageResource(R.drawable.call_interface_hands_free_on);
			} else {
				mCallHandFree
						.setImageResource(R.drawable.call_interface_hands_free);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置静音
	 */
	private void setMuteUI() {
		try {
			if (checkeDeviceHelper()) {
				getDeviceHelper().setMute(!isMute);
			}
			isMute = getDeviceHelper().getMuteStatus();

			if (isMute) {
				mCallMute.setImageResource(R.drawable.call_interface_mute_on);
			} else {
				mCallMute.setImageResource(R.drawable.call_interface_mute);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initCallTools() {
		if (checkeDeviceHelper()) {
			try {
				isMute = getDeviceHelper().getMuteStatus();
				isHandsfree = getDeviceHelper().getLoudsSpeakerStatus();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void stopVoiceRecording(String callid) {
		if (getDeviceHelper() != null && callRecordEnabled) {
			getDeviceHelper().stopVoiceCallRecording(callid);
		}
	}

	public void setAudioMode(int mode) {
		AudioManager audioManager = (AudioManager) getApplicationContext()
				.getSystemService(Context.AUDIO_SERVICE);
		if (audioManager != null) {
			audioManager.setMode(mode);
		}
	}

	/**
	 * 设置呼叫转接
	 */
	public int setCallTransfer(String mCurrentCallId, String transToNumber) {
		if (checkeDeviceHelper()) {
			int transferCall = getDeviceHelper().transferCall(mCurrentCallId,
					transToNumber);
			if (transferCall == 0) {
				mCallMute.setImageResource(R.drawable.call_interface_mute_on);
			} else {
				// 呼叫转接失败
				Toast.makeText(getApplicationContext(),
						"转接失败 返回码" + transferCall, 100).show();
			}
			return transferCall;
		}
		return -1;
	}

	public class VOIPCallHandle extends Handler {

		WeakReference<CallInActivity> mActivity;

		public VOIPCallHandle(CallInActivity activity) {
			mActivity = new WeakReference<CallInActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			CallInActivity mCallActivity = mActivity.get();

			if (mCallActivity == null) {
				return;
			}
			String callid = null;
			Reason reason = Reason.UNKNOWN;
			Bundle b = null;
			// 获取通话ID
			if (msg.obj instanceof String) {
				callid = (String) msg.obj;
			} else if (msg.obj instanceof Bundle) {
				b = (Bundle) msg.obj;

				if (b.containsKey(Device.CALLID)) {
					callid = b.getString(Device.CALLID);
				}

				if (b.containsKey(Device.REASON)) {
					try {
						reason = (Reason) b.get(Device.REASON);
					} catch (Exception e) {
						Log.e(this.getClass().getName(), "b.get(Device.REASON)");
					}
				}
			}
			System.out.println("wangting: Device.CALLID="+callid);
			switch (msg.what) {
			case CCPHelper.WHAT_ON_CALL_ALERTING:
				mCallActivity.onCallAlerting(callid);
				break;
			case CCPHelper.WHAT_ON_CALL_PROCEEDING:
				mCallActivity.onCallProceeding(callid);
				break;
			case CCPHelper.WHAT_ON_CALL_ANSWERED:
				mCallActivity.onCallAnswered(callid);
				if (mCallActivity.mCallTransferBtn != null)
					mCallActivity.mCallTransferBtn.setEnabled(true);
				break;

			case CCPHelper.WHAT_ON_CALL_RELEASED:
				 mCallActivity.onCallReleased(callid);
				 mCurrentCallId=null;
				if (mCallActivity.mCallTransferBtn != null)
					mCallActivity.mCallTransferBtn.setEnabled(false);
				break;
			case CCPHelper.WHAT_ON_CALL_MAKECALL_FAILED:
				mCallActivity.onMakeCallFailed(callid, reason);
				break;
			// case WHAT_ON_CODE_CALL_STATUS:
			case CCPHelper.WHAT_ON_RECEIVE_SYSTEM_EVENTS:
				mCallActivity.handleNotifyMessage(msg);
				break;

			case CCPHelper.WHAT_ON_CALL_BACKING:

				if (b == null) {
					return;
				}
				int status = -1;
				if (b.containsKey(Device.CBSTATE)) {
					status = b.getInt(Device.CBSTATE);
				}
				String self = "";
				if (b.containsKey(Device.SELFPHONE)) {
					self = b.getString(Device.SELFPHONE);
				}
				String dest = "";
				if (b.containsKey(Device.DESTPHONE)) {
					dest = b.getString(Device.DESTPHONE);
				}
				// mCallActivity.onCallback(status, self, dest);
				break;
			case CCPHelper.WHAT_ON_CALL_TRANSFERSTATESUCCEED:
				String callId = (String) msg.obj;
				// if(mCurrentCallId.equals(callId)){
				// Toast.makeText(mCallActivity, "呼转成功！", 1).show();
				// }
				break;

			default:
				break;
			}

		}
	}

	public VOIPCallHandle getCallHandler() {
		return mVOIPCallHandle;
	}

	// start call event handle
	private void onCallAlerting(String callid) {
		// 连接到对端用户，播放铃音
		Log4Util.d(CCPHelper.DEMO_TAG, "[CallOutActivity] voip alerting!!");
		if (callid != null && callid.equals(mCurrentCallId)) {
			mCallStateTips.setText(getString(R.string.voip_calling_wait));
		}
	}

	private void onCallProceeding(String callid) {
		// 连接到服务器
		Log4Util.d(CCPHelper.DEMO_TAG,
				"[CallOutActivity] voip on call proceeding!!"+ callid);
		mCallingArea.setVisibility(View.GONE);
		mHangupArea.setVisibility(View.VISIBLE);
		mInputNumberArea.setVisibility(View.GONE);
		/*
		if (callid != null && callid.equals(mCurrentCallId)) {
			mCallStateTips.setText(getString(R.string.voip_call_connect));
		}

		if (checkeDeviceHelper()) {
			getDeviceHelper().setProcessOriginalDataEnabled(mCurrentCallId,
					true, new OnProcessOriginalAudioDataListener() {

						@Override
						public void onProcessOriginalAudioData(String callid,
								byte[] b, int length) {

							if (b != null) {
								Log4Util.d("onProcessOriginalAudioData audio length"
										+ b.length);
								return;
							}
							Log4Util.d("onProcessOriginalAudioData audio length 0");
						}
					});
		}
		*/
	}

	/*
	 * protected void onCallAnswered(String callid) { // 对端应答
	 * Log4Util.d(CCPHelper.DEMO_TAG,
	 * "[CallOutActivity] voip on call answered!!"); if (callid != null &&
	 * callid.equals(mCurrentCallId)) { isConnect = true;
	 * mCallMute.setEnabled(true); initCallTools(); // 2013/09/23 // Show call
	 * state position is used to display the packet loss rate // and delay //
	 * mCallStateTips.setVisibility(View.GONE);
	 * mChronometer.setBase(SystemClock.elapsedRealtime());
	 * mChronometer.setVisibility(View.VISIBLE); mChronometer.start();
	 * 
	 * mCallStateTips.setText("");
	 * 
	 * if (getCallHandler() != null) {
	 * getCallHandler().sendMessage(getCallHandler
	 * ().obtainMessage(VideoActivity.WHAT_ON_CODE_CALL_STATUS)); }
	 * 
	 * startVoiceRecording(callid);
	 * 
	 * } }
	 */

	protected void onCallAnswered(String callid) {

		// 对端应答
		Log4Util.d(CCPHelper.DEMO_TAG,
				"[CallInActivity] voip on call answered!!");
		//if (callid != null && callid.equals(mCurrentCallId)) 
		{
			if (mCallType == Device.CallType.VIDEO) {
				// /initResVideoSuccess();
				// getDeviceHelper().enableLoudsSpeaker(true);
			} else {
				initialize(currIntent);
				// voip other ..
				initCallHold();
			}

			mChronometer = (Chronometer) findViewById(R.id.chronometer);
			mChronometer.setBase(SystemClock.elapsedRealtime());
			mChronometer.setVisibility(View.VISIBLE);
			mChronometer.start();
			/*
			 * if (getCallHandler() != null) {
			 * getCallHandler().sendMessage(getCallHandler
			 * ().obtainMessage(VideoActivity.WHAT_ON_CODE_CALL_STATUS)); }
			 * 
			 * startVoiceRecording(callid);
			 */
		}
	}

	protected void onCallReleased(String callid) {

		// 远端挂断，本地挂断在onClick中处理
		Log4Util.d(CCPHelper.DEMO_TAG,
				"[CallOutActivity] voip on call released!!");
		if (callid != null && callid.equals(mCurrentCallId)) {
			// stopVoiceRecording(callid);
			finishCalling();
		}
	}

	protected void onMakeCallFailed(String callid, Reason reason) {

		// 发起通话失败
		Log4Util.d(CCPHelper.DEMO_TAG,
				"[CallOutActivity] voip on call makecall failed!!");
		Toast.makeText(this, "呼叫失败", 100).show();
		if (callid != null && callid.equals(mCurrentCallId)) {
			finishCalling();

		}
	}

	protected void onCallback(int status, String self, String dest) {

		// 回拨通话回调
		Log4Util.d(CCPHelper.DEMO_TAG,
				"[CallOutActivity] voip on callback status : " + status);
		if (status == 170010) {
			mCallStateTips.setText(getString(R.string.voip_call_back_connect));
		} else {
			getCallHandler().postDelayed(finish, 5000);
			if (status == 0) {
				mCallStateTips
						.setText(getString(R.string.voip_call_back_success));
			} else if (status == 121002) {
				mCallStateTips
						.setText(getString(R.string.voip_call_fail_no_cash));
			} else {
				mCallStateTips.setText(getString(R.string.voip_call_fail));
			}
			mVHangUp.setClickable(false);
			mVHangUp.setBackgroundResource(R.drawable.call_interface_non_red_button);
		}
	}

	protected void handleNotifyMessage(Message msg) {

		switch (msg.what) {
		/*
		 * case VideoActivity.WHAT_ON_CODE_CALL_STATUS:
		 * 
		 * if (!checkeDeviceHelper()) { return; }
		 * 
		 * if (!isConnect) { return; } CallStatisticsInfo callStatistics =
		 * getDeviceHelper().getCallStatistics(Device.CallType.VOICE);
		 * 
		 * NetworkStatistic trafficStats = null; if (mCallType ==
		 * CallType.VOICE) { trafficStats =
		 * getDeviceHelper().getNetworkStatistic(mCurrentCallId); }
		 * 
		 * if (callStatistics != null) { int fractionLost =
		 * callStatistics.getFractionLost(); int rttMs =
		 * callStatistics.getRttMs(); if (mCallStateTips != null) { if
		 * (trafficStats != null) {
		 * mCallStateTips.setText(getString(R.string.str_call_traffic_status,
		 * rttMs, (fractionLost / 255), trafficStats.getTxBytes() / 1024,
		 * trafficStats.getRxBytes() / 1024)); } else {
		 * 
		 * mCallStateTips.setText(getString(R.string.str_call_status, rttMs,
		 * (fractionLost / 255))); } } }
		 * 
		 * if (getCallHandler() != null) { Message callMessage =
		 * getCallHandler()
		 * .obtainMessage(VideoActivity.WHAT_ON_CODE_CALL_STATUS);
		 * getCallHandler().sendMessageDelayed(callMessage, 4000); } break;
		 */
		// This call may be redundant, but to ensure compatibility system event
		// more,
		// not only is the system call
		case CCPHelper.WHAT_ON_RECEIVE_SYSTEM_EVENTS:

			doHandUpReleaseCall();
			break;

		default:
			break;
		}
	}

	// end listerner event handle


    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK ) {  
            finish();  
        }            
        return false;            
    }

	@Override
	public void setUpView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		
	}
}
