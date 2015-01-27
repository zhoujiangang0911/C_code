package cn.wislight.publicservice.ui.user;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import cn.wislight.publicservice.R;
import cn.wislight.publicservice.base.BaseActivity;

/**
 * 常用联系人
 * @author Administrator
 *
 */
public class ContactActivity extends BaseActivity implements OnClickListener{
	private RelativeLayout detail;
	@Override
	public void setUpView() {
		setContentView(R.layout.activity_contact);
		
		detail=(RelativeLayout)findViewById(R.id.detail);
	}

	@Override
	public void setListener() {
		detail.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.detail:
			gotoActivity(DetailUserActivity.class, false);
			break;

		default:
			break;
		}
	}

}
