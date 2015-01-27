package cn.wislight.meetingsystem.service;

import org.apache.http.Header;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.RemoteViews;
import android.widget.Toast;
import cn.wislight.meetingsystem.MainActivity;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.ui.LoginActivity;
import cn.wislight.meetingsystem.ui.SplashActivity;
import cn.wislight.meetingsystem.util.IntentUtil;
import cn.wislight.meetingsystem.util.MeetingSystemClient;

public class restartApp extends Service {

	private boolean isCur = true;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Thread threadUse = new Thread() {
			public void run() {
				while (isCur) {
					System.out
							.println("print out restartApp service onCreate run------------------------------------");
					try {
						Thread.sleep(100);
						System.exit(0);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
		}; 
		threadUse.start();
		
		Intent dialogIntent = new Intent(getBaseContext(), SplashActivity.class);   
		dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
		getApplication().startActivity(dialogIntent); 		
		onDestroy();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		isCur = false;
		System.out
				.println(" restartApp service close ---------------------------------------------------------");
		super.onDestroy();

	}

}
