package cn.wislight.meetingsystem.ui.conference;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.ui.topic.TopicControlActivity;
import cn.wislight.meetingsystem.ui.topic.TopicVoteCtrlListActivity;
import cn.wislight.meetingsystem.util.Authorize;
import cn.wislight.meetingsystem.util.IntentUtil;


/**
 * @author Administrator 会议管理界面
 */
public class ConfenenceManagementActivity extends BaseActivity implements OnClickListener{
	private TextView beginCon,checkCon,publishCon,changeCon,controllerCon,myStageCon,goingCon,myBeginCon,topicControl;
	private long mExitTime;
	@Override
	public void initView() {
		beginCon.setOnClickListener(this);
		checkCon.setOnClickListener(this);
		publishCon.setOnClickListener(this);
		changeCon.setOnClickListener(this);
		controllerCon.setOnClickListener(this);
		myStageCon.setOnClickListener(this);
		goingCon.setOnClickListener(this);
		myBeginCon.setOnClickListener(this);
		topicControl.setOnClickListener(this);
	}

	@Override
	public void setupView() {
	   setContentView(R.layout.conference_management_main);
		beginCon=(TextView) findViewById(R.id.tv_begin_con);
		checkCon=(TextView) findViewById(R.id.tv_check_con);
		publishCon=(TextView) findViewById(R.id.tv_publish_con);
		changeCon=(TextView) findViewById(R.id.tv_change_con);
		controllerCon=(TextView) findViewById(R.id.tv_controller_con);
		myStageCon=(TextView) findViewById(R.id.tv_mystage_con);
		goingCon=(TextView) findViewById(R.id.tv_vote_con);
		myBeginCon=(TextView) findViewById(R.id.tv_conf_history);
		topicControl=(TextView) findViewById(R.id.tv_controller_topic);

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_begin_con:
			if(Authorize.hasAuth(Authorize.AUTH_CONF_NEW)){
				IntentUtil.startActivity(this,ConBeginOneActivity.class);	
			} else {
				Toast.makeText(ConfenenceManagementActivity.this, getString(R.string.error_no_auth), Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.tv_check_con:
			if(Authorize.hasAuth(Authorize.AUTH_CONF_CHECK)){
				IntentUtil.startActivity(this,ConCheckActivity.class);
			} else {
				Toast.makeText(ConfenenceManagementActivity.this, getString(R.string.error_no_auth), Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.tv_publish_con:
			if(Authorize.hasAuth(Authorize.AUTH_CONF_PUBLISH)){
				IntentUtil.startActivity(this, ConPublishActivity.class);
			} else {
				Toast.makeText(ConfenenceManagementActivity.this, getString(R.string.error_no_auth), Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.tv_change_con:
			if(Authorize.hasAuth(Authorize.AUTH_CONF_CHANGE)){
				IntentUtil.startActivity(this, ConChangeActivity.class);
			} else {
				Toast.makeText(ConfenenceManagementActivity.this, getString(R.string.error_no_auth), Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.tv_controller_con:
			if(Authorize.hasAuth(Authorize.AUTH_CONF_CONTROL)){
				IntentUtil.startActivity(this, ConControllerActivity.class);
			} else {
				Toast.makeText(ConfenenceManagementActivity.this, getString(R.string.error_no_auth), Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.tv_mystage_con:
			if(Authorize.hasAuth(Authorize.AUTH_CONF_SCHEDULE)){
				IntentUtil.startActivity(this,ConJoinActivity.class);
			} else {
				Toast.makeText(ConfenenceManagementActivity.this, getString(R.string.error_no_auth), Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.tv_vote_con:
			if(Authorize.hasAuth(Authorize.AUTH_CONF_VOTE_CTRL)){
				IntentUtil.startActivity(this, TopicVoteCtrlListActivity.class);
			} else {
				Toast.makeText(ConfenenceManagementActivity.this, getString(R.string.error_no_auth), Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.tv_conf_history:
			if(Authorize.hasAuth(Authorize.AUTH_CONF_HISTORY)){
				IntentUtil.startActivity(this, ConferenceMyStart.class);	
			} else {
				Toast.makeText(ConfenenceManagementActivity.this, getString(R.string.error_no_auth), Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.tv_controller_topic:
			if(Authorize.hasAuth(Authorize.AUTH_CONF_TOPIC_CTRL)){
				IntentUtil.startActivity(this, TopicControlActivity.class);	
			} else {
				Toast.makeText(ConfenenceManagementActivity.this, getString(R.string.error_no_auth), Toast.LENGTH_SHORT).show();
			}
			break;
			
		default:
			break;
		}
	}

}
