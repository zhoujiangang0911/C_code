package cn.wislight.meetingsystem.service;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;
import cn.wislight.meetingsystem.R;

public class ConnectionChangeReceiver extends BroadcastReceiver {

	private int times = 0;
	private boolean bconnected = false;
    Context context;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null) {
			boolean available = activeNetInfo.isAvailable();

			if (available) {
				if (times == 0) {
					times++;
					bconnected = true;
					return;
				}
				
				if (bconnected){
					return;
				}
				
				bconnected = true;
				if (isTopActivity()){
				Toast.makeText(context,
						context.getString(R.string.network_accessable),
						Toast.LENGTH_SHORT).show();
				}
			} else {
				
				if (!bconnected){
					return;
				}
				bconnected = false;
				if (isTopActivity()){
				Toast.makeText(context,
						context.getString(R.string.network_unaccessable),
						Toast.LENGTH_SHORT).show();
				}
			}
		} else {
			if (!bconnected){
				return;
			}
			bconnected = false;
			
			if (isTopActivity()){
				Toast.makeText(context,
						context.getString(R.string.network_unaccessable),
						Toast.LENGTH_SHORT).show();	
			}
			
		}

	}
	
	protected  boolean isTopActivity(){
        String packageName = "cn.wislight.meetingsystem";
		ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo>  tasksInfo = activityManager.getRunningTasks(1);  
        if(tasksInfo.size() > 0){  
            if(packageName.equals(tasksInfo.get(0).topActivity.getPackageName())){  
                return true;  
            }  
        }  
        return false;
    }
}