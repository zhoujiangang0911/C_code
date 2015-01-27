package cn.wislight.publicservice;

import android.view.Menu;
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.ui.LoginActivity;

public class StartActivity extends BaseActivity {


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void setUpView() {
		setContentView(R.layout.activity_start);
		gotoActivity(LoginActivity.class, false);
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		
	}

}
