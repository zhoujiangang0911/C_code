package cn.wislight.meetingsystem;

import java.util.HashMap;
import java.util.Map;

import cn.wislight.meetingsystem.util.MeetingSystemClient;
import cn.wislight.meetingsystem.voip.CCPHelper;

import com.hisun.phone.core.voice.CCPCall;
import com.loopj.android.http.PersistentCookieStore;


import android.app.Application;

public class MeetingSystemApplication extends Application {
	private Map<String,Object> map;
	private PersistentCookieStore cookieStore;
	private static MeetingSystemApplication instance;
	
	public void onCreate(){
		super.onCreate();
		instance = this;
		
		map = new HashMap<String,Object>();
		
		cookieStore = new PersistentCookieStore(this);		
		MeetingSystemClient.setCookieStore(cookieStore);
		
		CCPHelper.getInstance().initCCP();
	}
	public Map<String, Object> getMap(){
		return map;
	}
	
	public PersistentCookieStore getCookieStore(){
		return cookieStore;
	}

	public static MeetingSystemApplication getInstance(){
		return instance;
	}
	
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
		exitCCPDemo();
		System.out.println("wangting: application onTerminate()");
	}
	public static void exitCCPDemo() {
		System.out.println("wangting: application exitCCPDemo()");
		CCPHelper.getInstance().release();
		CCPCall.shutdown();
	}
	
}
