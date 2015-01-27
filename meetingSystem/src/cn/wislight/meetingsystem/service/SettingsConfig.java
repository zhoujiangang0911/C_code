package cn.wislight.meetingsystem.service;

import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.MeetingSystemClient;
import android.content.Context;



public class SettingsConfig {
	private DbAdapter dbHandle; 
	
	public SettingsConfig(Context ctx){
		dbHandle = new DbAdapter(ctx);
	}
	
	public void InitConfig(){
		dbHandle.open();
		if (null == dbHandle.getConfigUrl()){
			dbHandle.insertConfig("", "", "", "");
		}
		String url = dbHandle.getConfigUrl();
		if ("".equals(url)){
			boolean b = dbHandle.setConfigUrl(Constants.BASE_URL);
			MeetingSystemClient.setBASE_URL(Constants.BASE_URL);
		}else{
			MeetingSystemClient.setBASE_URL(url);
		}
		
		dbHandle.close();
	}
	
	public String getBaseUrl() {
		dbHandle.open();
		String url = dbHandle.getConfigUrl();
		dbHandle.close();
		return url;
	}
	
	public void setBaseUrl(String url) {
		dbHandle.open();
		dbHandle.setConfigUrl(url);
		dbHandle.close();
		
		MeetingSystemClient.setBASE_URL(url);
	}
	
	public String getUsername(){
		dbHandle.open();
		String temp = dbHandle.getConfigUsername();
		dbHandle.close();
		return temp;
	}
	
	public String getPassword(){
		dbHandle.open();
		String temp = dbHandle.getConfigPassword();
		dbHandle.close();
		return temp;
	}
	
	public void setUsername(String username){
		dbHandle.open();
		dbHandle.setConfigUsername(username);
		dbHandle.close();
		return ;
	}
	
	public void setPassword(String password){
		dbHandle.open();
		dbHandle.setConfigPassword(password);
		dbHandle.close();
		return ;
	}
}
