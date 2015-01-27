package cn.wislight.publicservice;

import java.util.HashMap;
import java.util.Map;
import cn.wislight.publicservice.util.PublicServiceClient;
import cn.wislight.publicservice.voip.CCPHelper;

import com.baidu.mapapi.SDKInitializer;
import com.hisun.phone.core.voice.CCPCall;
import com.loopj.android.http.PersistentCookieStore;
import android.app.Application;

public class PublicServiceApplication extends Application{

	private Map<String,Object> map;
	private PersistentCookieStore cookieStore;
	private static PublicServiceApplication instance;
	
	public void onCreate(){
		super.onCreate();
		instance = this;
		
		map = new HashMap<String,Object>();
		
		cookieStore = new PersistentCookieStore(this);		
		PublicServiceClient.setCookieStore(cookieStore);
		

		//init baidu map sdk
		//SDKInitializer.initialize(getApplicationContext()); 
		SDKInitializer.initialize(this);
		
		//init yuntongxun sdk 		
		CCPHelper.getInstance().initCCP();
		
	}
	public Map<String, Object> getMap(){
		return map;
	}
	
	public PersistentCookieStore getCookieStore(){
		return cookieStore;
	}

	public static PublicServiceApplication getInstance(){
		return instance;
	}
	
	@Override
	public void onTerminate() {
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
