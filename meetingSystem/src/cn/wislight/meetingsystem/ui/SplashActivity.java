package cn.wislight.meetingsystem.ui;

import android.content.Intent;
import android.os.Handler;
import cn.wislight.meetingsystem.MainActivity;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.service.SettingsConfig;
import cn.wislight.meetingsystem.ui.vote.VoteActivity;

public class SplashActivity extends BaseActivity {
	private final int SPLASH_DISPLAY_LENGHT = 1500; // 延迟1.5秒  
	@Override
	public void setupView() {
		setContentView(R.layout.splash_main);  
	}  




	@Override
	public void initView() {
		SettingsConfig config = new SettingsConfig(SplashActivity.this);
		config.InitConfig();
		new Handler().postDelayed(new Runnable() {  
			public void run() {  
				Intent mainIntent = new Intent(SplashActivity.this,  
						LoginActivity.class);  
				SplashActivity.this.startActivity(mainIntent);  
				SplashActivity.this.finish();  
			}  

		}, SPLASH_DISPLAY_LENGHT);
	}
}
