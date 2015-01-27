package cn.wislight.meetingsystem.ui.conference;

import android.view.View;
import android.view.View.OnClickListener;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.util.IntentUtil;

/**
 * @author Administrator
 *	会议发起
 */
public class ConBeginActivity extends BaseActivity implements OnClickListener{
	@Override
	public void initView() {
		findViewById(R.id.conf_btn_up).setOnClickListener(this);
		findViewById(R.id.conf_btn_next).setOnClickListener(this);
	}

	@Override
	public void setupView() {
		setContentView(R.layout.conference_begin);


	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.conf_btn_up:
			IntentUtil.startActivity(this,ConBeginOneActivity.class);
			break;
		case R.id.conf_btn_next:
			IntentUtil.startActivity(this,ConBeginTwoActivity.class);
			break;	
		default:
			break;
		}
	}

}
