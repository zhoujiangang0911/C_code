package cn.wislight.meetingsystem.ui.home;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.ui.topic.TopicAddOneActivity;
import cn.wislight.meetingsystem.ui.topic.TopicHouseActivity;
import cn.wislight.meetingsystem.util.Authorize;
import cn.wislight.meetingsystem.util.IntentUtil;


public class HomeActivity extends BaseActivity implements OnClickListener{
	private ImageView home_main_topiichourse,home_main_TSA, home_main_newtopic, home_main_willtopic;
	private long mExitTime;
	@Override
	public void initView() {
		home_main_topiichourse=(ImageView)findViewById(R.id.home_main_topichourse);
		home_main_TSA=(ImageView)findViewById(R.id.home_main_TSA);
		home_main_newtopic = (ImageView)findViewById(R.id.home_main_newtopic);
		home_main_willtopic = (ImageView)findViewById(R.id.home_main_willtopic);
		
		home_main_topiichourse.setOnClickListener(this);
		home_main_TSA.setOnClickListener(this);
		home_main_newtopic.setOnClickListener(this);
		home_main_willtopic.setOnClickListener(this);
		
	}

	@Override
	public void setupView() {
		setContentView(R.layout.home_main);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home_main_topichourse:
			if(Authorize.hasAuth(Authorize.AUTH_TOPIC_NEW_DRAFT)){
				IntentUtil.startActivity(HomeActivity.this, TopicHouseActivity.class);
			} else {
				Toast.makeText(HomeActivity.this, getString(R.string.error_no_auth), Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.home_main_TSA:
			if(Authorize.hasAuth(Authorize.AUTH_TOPIC_NEW_DRAFT)){
				IntentUtil.startActivity(HomeActivity.this, TodayScheduleActivity.class);
			} else {
				Toast.makeText(HomeActivity.this, getString(R.string.error_no_auth), Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.home_main_newtopic:
			if(Authorize.hasAuth(Authorize.AUTH_TOPIC_NEW_DRAFT)){
				IntentUtil.startActivity(HomeActivity.this, TopicAddOneActivity.class);
			} else {
				Toast.makeText(HomeActivity.this, getString(R.string.error_no_auth), Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.home_main_willtopic:
			if(Authorize.hasAuth(Authorize.AUTH_TOPIC_NEW_DRAFT)){
				IntentUtil.startActivity(HomeActivity.this, WaitMyHandleJobsActivity.class);
			} else {
				Toast.makeText(HomeActivity.this, getString(R.string.error_no_auth), Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Object mHelperUtils;
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			} else {
				finish();
				//Toast.makeText(this, "退出程序", Toast.LENGTH_SHORT).show();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
