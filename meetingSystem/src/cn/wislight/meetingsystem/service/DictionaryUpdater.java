package cn.wislight.meetingsystem.service;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.RemoteViews;
import android.widget.Toast;
import cn.wislight.meetingsystem.MainActivity;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.bean.OrganizeSmpBean;
import cn.wislight.meetingsystem.ui.conference.ConfGoingDetailActivity;
import cn.wislight.meetingsystem.util.MeetingSystemClient;

import com.loopj.android.http.AsyncHttpResponseHandler;

public  class DictionaryUpdater extends Service{
	private static int maxRetryTimes = 3;
	private int retryTimes = 0;
	private boolean updating = false;

	private String rspOrg;
	private int rspOrgState = 0;
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		startUpdate();
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void sendBroadCastToActivity(int state){
			Intent intent=new Intent();
			intent.putExtra("i",state);
			intent.setAction("android.intent.action.updatedictionary");
			sendBroadcast(intent);
	}                                   

	
	private void startUpdate() {
		if (updating){
			return;
		}
		
		updating = true;
		// DisplayNotification("开始更新数据字典", false);
		
		updateOrg();
	}
	
	private void updateDb() {
		/* check all responced? */
		if (rspOrgState == 0
				/* ||  */ ){
			return;
		}
		
		/* check network errors */
		if (rspOrgState == -1){
			retryTimes++;
			if (retryTimes > maxRetryTimes){
				// DisplayNotification("更新失败，请在网络状态良好时更新！", true);
				sendBroadCastToActivity(2);
				return;
			}
			updateOrg();
			return;
		}
		
		/* update database */
		Thread thr = new Thread() {			
			public void run() {
				 DbAdapter dbHandle = new DbAdapter(getApplicationContext()); 
				 dbHandle.open();
				 
				try {
					 JSONArray jsonArray=new JSONObject(rspOrg).getJSONArray("orgList");

					 dbHandle.deleteAllOrgCache();
					 OrganizeSmpBean bean = null;   
					 for(int i=0;i<jsonArray.length();i++){
			               JSONObject jsonObject=(JSONObject)jsonArray.get(i);
			               bean = new OrganizeSmpBean();
			               bean.setFullname(jsonObject.getString("fullname") == null ? "" : jsonObject.getString("fullname"));
			               bean.setName(jsonObject.getString("name") == null ? "" : jsonObject.getString("name"));
			               bean.setNo(jsonObject.getString("no") == null ? "" : jsonObject.getString("no"));
			               bean.setMa_no(jsonObject.getString("ma_no") == null ? "" : jsonObject.getString("ma_no"));
			               dbHandle.insertOrgCache(bean);
					 }
					 
					
				} catch (JSONException e) {
					e.printStackTrace();
					
				}

				dbHandle.close();
				updating = false;
				// DisplayNotification("数据字典更新完成！", true);
				sendBroadCastToActivity(1);
				
			}
		};
		
		thr.start();
		
	}
	
	private void updateOrg() {
		// TODO Auto-generated method stub
		String url = "MeetingManage/mobile/getAllOrg.action";
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler(){
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				setRspOrgState(-1);
				updateDb();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				
				if (response.contains("用户未登陆")){
					Toast.makeText(DictionaryUpdater.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}
				
				setRspOrgState(1);
				setRspOrg(response);
				updateDb();
			}
		});		
		
	}


	public String getRspOrg() {
		return rspOrg;
	}


	public void setRspOrg(String rspOrg) {
		this.rspOrg = rspOrg;
	}


	public int getRspOrgState() {
		return rspOrgState;
	}


	public void setRspOrgState(int rspOrgState) {
		this.rspOrgState = rspOrgState;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}
