package cn.wislight.meetingsystem.ui.topic;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.util.Authorize;
import cn.wislight.meetingsystem.util.IntentUtil;

/**
 * @author Administrator 议题主界面
 */
public class TopicActivity extends BaseActivity implements OnClickListener {
	private ImageButton topic_new, topic_mine, topic_stay, topic_hourse;
	private long mExitTime;

	@Override
	public void initView() {
		topic_new=(ImageButton)findViewById(R.id.topic_new);
		topic_mine=(ImageButton)findViewById(R.id.topic_mine);
		topic_stay = (ImageButton)findViewById(R.id.topic_stay);
		topic_hourse = (ImageButton)findViewById(R.id.topic_hourse);
		
		topic_new.setOnClickListener(this);
		topic_mine.setOnClickListener(this);
		topic_stay.setOnClickListener(this);
		topic_hourse.setOnClickListener(this);
	}

	@Override
	public void setupView() {
		setContentView(R.layout.topic_main);
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
		case R.id.topic_new:
			if(Authorize.hasAuth(Authorize.AUTH_TOPIC_NEW_DRAFT)){
				IntentUtil.startActivity(this, TopicAddOneActivity.class);	
			} else {
				Toast.makeText(TopicActivity.this, getString(R.string.error_no_auth), Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.topic_mine:
			if(Authorize.hasAuth(Authorize.AUTH_TOPIC_MY_CREATED)){
				IntentUtil.startActivity(this, TopicMineActivity.class);	
			} else {
				Toast.makeText(TopicActivity.this, getString(R.string.error_no_auth), Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.topic_stay:
			if(Authorize.hasAuth(Authorize.AUTH_TOPIC_CHECK)){
				IntentUtil.startActivity(this, TopicStayActivity.class);	
			} else {
				Toast.makeText(TopicActivity.this, getString(R.string.error_no_auth), Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.topic_hourse:
			if(Authorize.hasAuth(Authorize.AUTH_TOPIC_DRAFT_DB)){
				IntentUtil.startActivity(this, TopicHouseActivity.class);	
			} else {
				Toast.makeText(TopicActivity.this, getString(R.string.error_no_auth), Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}

}
