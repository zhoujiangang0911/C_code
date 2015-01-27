package cn.wislight.publicservice.voip;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

import cn.wislight.publicservice.PublicServiceApplication;

import com.hisun.phone.core.voice.CCPCall;
import com.hisun.phone.core.voice.Device;
import com.hisun.phone.core.voice.Device.CallType;
import com.hisun.phone.core.voice.Device.Codec;
import com.hisun.phone.core.voice.DeviceListener.CCPEvents;
import com.hisun.phone.core.voice.DeviceListener.Reason;
import com.hisun.phone.core.voice.DeviceListener;
import com.hisun.phone.core.voice.CCPCall.InitListener;
import com.hisun.phone.core.voice.listener.OnChatroomListener;
import com.hisun.phone.core.voice.listener.OnIMListener;
import com.hisun.phone.core.voice.listener.OnVoIPListener;
import com.hisun.phone.core.voice.model.CloopenReason;
import com.hisun.phone.core.voice.model.chatroom.Chatroom;
import com.hisun.phone.core.voice.model.chatroom.ChatroomMember;
import com.hisun.phone.core.voice.model.chatroom.ChatroomMsg;
import com.hisun.phone.core.voice.model.im.IMTextMsg;
import com.hisun.phone.core.voice.model.im.InstanceMsg;
import com.hisun.phone.core.voice.model.setup.UserAgentConfig;
import com.hisun.phone.core.voice.util.Log4Util;


public class CCPHelper implements DeviceListener, OnVoIPListener, OnChatroomListener, InitListener, OnIMListener {
	/**
	 * handler 转换消息id
	 */
	public static final int WHAT_ON_CONNECT = 0x2000;
	public static final int WHAT_ON_DISCONNECT = 0x2001;
	public static final int WHAT_INIT_ERROR = 0x200A;
	public static final int WHAT_ON_CALL_ALERTING = 0x2002;
	public static final int WHAT_ON_CALL_ANSWERED = 0x2003;
	public static final int WHAT_ON_CALL_PAUSED = 0x2004;
	public static final int WHAT_ON_CALL_PAUSED_REMOTE = 0x2005;
	public static final int WHAT_ON_CALL_RELEASED = 0x2006;
	public static final int WHAT_ON_CALL_PROCEEDING = 0x2007;
	public static final int WHAT_ON_CALL_TRANSFERED = 0x2008;
	public static final int WHAT_ON_CALL_MAKECALL_FAILED = 0x2009;
	public static final int WHAT_ON_CALL_BACKING = 0x201B;
	public static final int WHAT_ON_CALL_TRANSFERSTATESUCCEED = 0x208d;
	public static final int WHAT_ON_RECEIVE_SYSTEM_EVENTS = 0x2090;
	
	//IM message
	public static final int WHAT_ON_IM_SEND_MESSAGE_RESULT = 0x3001;
	public static final int WHAT_ON_IM_RECEIVE_MESSAGE = 0x3002;
	/**
	 * im 测试demo 必须填 正确不然报异常
	 *  VOIP_ID 自己的voip账号
	 *  VOIP_PSW voip密码
	 *  SUB_ID 子账号 
	 *  SUB_PWD子账号密码
	 * 
	 */

	//private static String KEY_IP = "sandboxapp.cloopen.com";
	//private static String KEY_PORT = "8883";
	//private static String VOIP_ID = "81898100000001";
	//private static String VOIP_PSW = "3cgk9bro";
	//private static String SUB_ID = "8a48b5514767145d01477c5470bc0716";
	//private static String SUB_PWD = "848a23c6b63b4db3b9e5cd993bb7e74d";
	
	//private static String VOIP_ID = "81898100000003";
	//private static String VOIP_PSW = "6bcexcfc";
	//private static String SUB_ID = "8a48b5514767145d01477c5470cd0718";
	//private static String SUB_PWD = "f71b5e1785f44fcf8631a5040777d358";
	
	/* 
	 * 私有云配置
	private static String COMPANY_ID = "aisitong1010";
	private static String REST_ADDRESS = "125.71.235.132";
	
	private static String KEY_IP = "192.168.168.225";
	private static String KEY_PORT = "8883";
	
	private static String VOIP_ID = "80016800000024";
	private static String VOIP_PSW = "dyWSn8op";
	private static String SUB_ID = "48de7703486411e4b1b7d4ae526c8f74";
	private static String SUB_PWD = "8e6a942c1809da6f3bd7f01d2c085bf9";
	*/
	
	//private static String KEY_IP = "sandboxapp.cloopen.com";
	private static String KEY_IP = "app.cloopen.com";
	private static String KEY_PORT = "8883";
	
	private static String VOIP_ID = "81553500000001";
	private static String VOIP_PSW = "aywd1ab1";
	private static String SUB_ID = "aaf98f894627423201464163e1671130";
	private static String SUB_PWD = "0b030f2d23244fd4a78dde075234c8d1";		

	public static void setKEY_IP(String kEY_IP) {
		KEY_IP = kEY_IP;
	}

	public static void setKEY_PORT(String kEY_PORT) {
		KEY_PORT = kEY_PORT;
	}

	public static void setVOIP_ID(String vOIP_ID) {
		VOIP_ID = vOIP_ID;
	}

	public static void setVOIP_PSW(String vOIP_PSW) {
		VOIP_PSW = vOIP_PSW;
	}

	public static void setSUB_ID(String sUB_ID) {
		SUB_ID = sUB_ID;
	}

	public static void setSUB_PWD(String sUB_PWD) {
		SUB_PWD = sUB_PWD;
	}
	/*
	public static final String KEY_IP = "125.71.235.132";
	public static final String KEY_PORT = "8883";	
	public static final String VOIP_ID = "80016800000001";
	public static final String VOIP_PSW = "C6uYhkCV";
	public static final String SUB_ID = "1e58032aca9011e3b1d00050568e62f2";
	public static final String SUB_PWD = "16ab869d319824d87e1438b2cf7430c2";
	*/
	public static final String DEMO_TAG = "MeetingSystem_CCP_Demo";
	
	private Context context;
	private static CCPHelper instance;
	private Device device;
	
	
	private Handler IMHandler;
	public void setIMHandler(final Handler handler) {
		this.IMHandler = handler;
	}
	/**
	 * handler for update activity
	 */
	private Handler handler;

	/**
	 * set handler.
	 * 
	 * @param handler
	 *            activity handler
	 */
	public void setHandler(final Handler handler) {
		this.handler = handler;
	}
	
	long t = 0;
	/**
	 * send object to activity by handler.
	 * 
	 * @param what
	 *            message id of handler
	 * @param obj
	 *            message of handler
	 */
	private void sendTarget(int what, Object obj) {
		t = System.currentTimeMillis();
		// for kinds of mobile phones
		while (handler == null && (System.currentTimeMillis() - t < 3200)) {
			Log4Util.d(DEMO_TAG , "[VoiceHelper] handler is null, activity maybe destory, wait...");
			try {
				Thread.sleep(80L);
			} catch (InterruptedException e) {
			}
		}

		if (handler == null) {
			Log4Util.d(DEMO_TAG , "[VoiceHelper] handler is null, need adapter it.");
			return;
		}

		Message msg = Message.obtain(handler);
		msg.what = what;
		msg.obj = obj;
		msg.sendToTarget();
	}
	
	
	
	private CCPHelper(Context context) {
		this.context = context;
	}
	public static CCPHelper getInstance() {
		if (instance == null) {
			instance = new CCPHelper(PublicServiceApplication.getInstance());
		}
		return instance;
	}
	 
	public Device getDeviceHelper(){
		 return device;
	}
	public void initCCP() {
		//setRegistCallback(rcb);

		Log4Util.init(true);
		CCPCall.init(context, this);
		Log4Util.d(DEMO_TAG , "[VoiceHelper] CCPCallService init");
		
	}
	public static void shutdownCCP() {
		//setRegistCallback(rcb);

		//Log4Util.init(true);
		getInstance().release();
		System.out.println("wangting: shutdownCCP");
		CCPCall.shutdown();
		Log4Util.d(DEMO_TAG , "[VoiceHelper] CCPCallService shutdown");
		
	}
	/**
	 * 账号密码错误会抛异常
	 */
	public void createDevice() {

		Map<String, String> params = new HashMap<String, String>();
		params.put(UserAgentConfig.KEY_IP, KEY_IP);
		params.put(UserAgentConfig.KEY_PORT, KEY_PORT);
		params.put(UserAgentConfig.KEY_SID, VOIP_ID);
		params.put(UserAgentConfig.KEY_PWD, VOIP_PSW);
		params.put(UserAgentConfig.KEY_SUBID, SUB_ID);
		params.put(UserAgentConfig.KEY_SUBPWD, SUB_PWD);
		// User-Agent
		params.put(UserAgentConfig.KEY_UA, getUser_Agent());

		// 创建Device
		if(device != null){
			//releaseDevice();
		}
				device = CCPCall.createDevice(this /* DeviceListener */, params);
				if(device == null){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(device == null){
						device = CCPCall.createDevice(this /* DeviceListener */, params);
					}
				}
				
				// 设置当呼入请求到达时, 唤起的界面
				Intent intent = new Intent(context, CallInActivity.class);
				//Intent intent = new Intent(context, VOIPDemoActivity.class);
				PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				if (device == null){
					System.out.println("wangting: device == null");
					Toast.makeText(context, "VOIP初始化失败", 100).show();
					return;
				}
				if (pendingIntent == null){
					System.out.println("wangting: pendingIntent == null");
				}
				device.setIncomingIntent(pendingIntent);
				
				
				// set Listener ...
				// In a later version of SDK 3.5, SDK will implement the Interphone, VOIP, voice Chatroom, 
				// completely separate from the IM instant messaging function, if the need to use a function, 
				// only need to set the listener, do not need to ignore 
				// for SDK version 3.5 above
				
				//device.setPrivateCloud(COMPANY_ID, REST_ADDRESS, false);
				device.setOnVoIPListener(this);
				device.setOnIMListener(this);
				//device.setOnInterphoneListener(this);
				//device.setOnChatroomListener(this);
				
//				device.setNativeLog(true);//
				
				device.setCodecEnabled(Codec.Codec_iLBC, false);
				device.setCodecEnabled(Codec.Codec_SILK8K, false);
				device.setCodecEnabled(Codec.Codec_G729, true);
				
				Log4Util.d(DEMO_TAG, "[onInitialized] sdk init success. done");
	
		// ------------- Video /Audio call ----------------
	}
	public void release(){
		System.out.println("wangting: CCPHelper.release()");
		releaseDevice();
		instance = null;
		context = null;
	}
	private void releaseDevice(){
		System.out.println("wangting: CCPHelper.releaseDevice()");
		handler = null;
		IMHandler = null;
		if(device != null){
			device.release();
		}
		device = null;
	}
	public String getUser_Agent() {
		String ua = "Android;" + Build.VERSION.RELEASE + ";"
				+ com.hisun.phone.core.voice.Build.SDK_VERSION + ";"
				+ com.hisun.phone.core.voice.Build.LIBVERSION.FULL_VERSION
				+ ";" + Build.BRAND + "-" + Build.MODEL + ";";
		ua = ua + getDevicNO() + ";" + System.currentTimeMillis() + ";";

		return ua;
	}
	public String getDevicNO() {
		if (!TextUtils.isEmpty(getDeviceId())) {
			return getDeviceId();
		}

		if (!TextUtils.isEmpty(getMacAddress())) {
			return getMacAddress();
		}
		return " ";
	}
	
	public String getDeviceId() {
		TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		if (telephonyManager != null) {
			return telephonyManager.getDeviceId();
		}

		return null;

	}

	public String getMacAddress() {
		// start get mac address
		WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (wifiMan != null) {
			WifiInfo wifiInf = wifiMan.getConnectionInfo();
			if (wifiInf != null && wifiInf.getMacAddress() != null) {
				// 34:7C:6D:E4:D7
				return wifiInf.getMacAddress();
			}
		}
		return null;
	}
	@Override
	public void onConnected() {
		// TODO Auto-generated method stub
		System.out.println("wangting: device onConnected.");
	}
	@Override
	public void onDisconnect(Reason arg0) {
		// TODO Auto-generated method stub
		System.out.println("wangting:device onDisconnect."+arg0.getValue()+";"+arg0.name()+";"+arg0.getStatus());
	}
	@Override
	public void onFirewallPolicyEnabled() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onReceiveEvents(CCPEvents events/*, APN network, NetworkState ns*/) {
		Log4Util.d(DEMO_TAG, "Receive CCP events , " + events)  ;
		if(events == CCPEvents.SYSCallComing) {
			Bundle b = new Bundle();
			sendTarget(WHAT_ON_RECEIVE_SYSTEM_EVENTS, b);
		}
	}
	@Override
	public void onChatroomDismiss(CloopenReason arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onChatroomInviteMembers(CloopenReason arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onChatroomMembers(CloopenReason arg0, List<ChatroomMember> arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onChatroomRemoveMember(CloopenReason arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onChatroomState(CloopenReason arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onChatrooms(CloopenReason arg0, List<Chatroom> arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onReceiveChatroomMsg(ChatroomMsg arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onSetMemberSpeakOpt(CloopenReason arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	/**
	 * Callback this method when call arrived in remote.
	 * 
	 * @param callid
	 */
	@Override
	public void onCallAlerting(String callid) {
		System.out.println("wangting: onCallAlerting()");
		sendTarget(WHAT_ON_CALL_ALERTING, callid);
	}

	/**
	 * Callback this method when remote answered.
	 * 
	 * @param callid
	 *           calling id
	 */
	@Override
	public void onCallAnswered(String callid) {
		System.out.println("wangting: onCallAnswered()");
		sendTarget(WHAT_ON_CALL_ANSWERED, callid);
	}

	/**
	 * Callback this method when call arrived in soft-switch platform.
	 * 
	 * @param callid
	 *            calling id
	 */
	@Override
	public void onCallProceeding(String callid) {
		System.out.println("wangting: onCallProceeding()");
		sendTarget(WHAT_ON_CALL_PROCEEDING, callid);
	}

	/**
	 * Callback this method when remote hangup call.
	 * 
	 * @param callid
	 *            calling id
	 */
	@Override
	public void onCallReleased(String callid) {
		System.out.println("wangting: onCallReleased()");
		sendTarget(WHAT_ON_CALL_RELEASED, callid);
	}

	/**
	 * Callback this method when make call failed.
	 * 
	 * @param callid
	 *            calling id
	 * @param destionation
	 *            destionation account
	 */
	@Override
	public void onCallTransfered(String callid, String destionation) {
		System.out.println("wangting: onCallTransfered()");
		Bundle b = new Bundle();
		b.putString(Device.CALLID, callid);
		b.putString(Device.DESTIONATION, destionation);
		sendTarget(WHAT_ON_CALL_TRANSFERED, b);
	}

	/**
	 * Callback this method when make call failed.
	 * 
	 * @param callid
	 *            calling id
	 * @param reason
	 *            failed reason
	 */
	@Override
	public void onMakeCallFailed(String callid, Reason reason) {
		System.out.println("wangting: onMakeCallFailed()");
		Bundle b = new Bundle();
		b.putString(Device.CALLID, callid);
		b.putSerializable(Device.REASON, reason);
		sendTarget(WHAT_ON_CALL_MAKECALL_FAILED, b);
	}

	
	
	
	
	
	
	


	@Override
	@Deprecated
	public void onCallMediaInitFailed(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onCallMediaInitFailed(String arg0, CallType arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	@Deprecated
	public void onCallMediaUpdateRequest(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	@Deprecated
	public void onCallMediaUpdateResponse(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onCallPaused(String arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onCallPausedByRemote(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCallVideoRatioChanged(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onCallback(int arg0, String arg1, String arg2) {
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
	@Override
	public void onTransferStateSucceed(String arg0, boolean arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onError(Exception arg0) {
		// TODO Auto-generated method stub
		shutdownCCP();
		System.out.println("wangting:initialized failed.");
	}
	@Override
	public void onInitialized() {
		// TODO Auto-generated method stub
		System.out.println("wangting: initialized." );
		
	}

	@Override
	public void onConfirmIntanceMessage(CloopenReason arg0) {
		// TODO Auto-generated method stub
		System.out.println("wangting: onConfirmIntanceMessage="+arg0.getMessage());
	}

	@Override
	public void onDownloadAttached(CloopenReason arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFinishedPlaying() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReceiveInstanceMessage(InstanceMsg arg0) {
		// TODO Auto-generated method stub
		System.out.println("wangting: onReceiveInstanceMessage="+((IMTextMsg)arg0).getMessage());
		if(IMHandler == null){
			System.out.println("wangting: IMHandler is null.");
			return;
		}
		Message msg = IMHandler.obtainMessage();
		msg.what = WHAT_ON_IM_RECEIVE_MESSAGE;
		msg.obj = arg0;
		msg.sendToTarget();

		
	}

	@Override
	public void onRecordingAmplitude(double arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRecordingTimeOut(long arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSendInstanceMessage(CloopenReason arg0, InstanceMsg arg1) {
		// TODO Auto-generated method stub
		System.out.println("wangting: onSendInstanceMessage="+arg0.getMessage()+";stauts msg="+arg1.getStatusMsg()+";messageId="+((IMTextMsg)arg1).getMsgId()
				+";message="+((IMTextMsg)arg1).getMessage());
		if(IMHandler == null){
			System.out.println("wangting: IMHandler is null.");
			return;
		}
		Message msg = IMHandler.obtainMessage();
		JSONObject object = new JSONObject();
		try {
			object.put("CloopenReason", arg0);
			object.put("InstanceMsg", arg1);
			msg.what = WHAT_ON_IM_SEND_MESSAGE_RESULT;
			msg.obj = object;
			msg.sendToTarget();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
