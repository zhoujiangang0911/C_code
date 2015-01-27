package cn.wislight.publicservice.ui.user.goverment;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import cn.wislight.publicservice.R;
import cn.wislight.publicservice.R.id;
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.ui.LoginActivity;
import cn.wislight.publicservice.ui.user.LocationPointActivity;

/**
 * 设置界面
 * @author Administrator
 *
 */
public class UserSettingActivity extends BaseActivity implements OnClickListener{
	private TextView point;
	private ImageButton logout;
	@Override
	public void setUpView() {
		setContentView(R.layout.activity_user_setting);
		
		point=(TextView)findViewById(R.id.point);
		logout=(ImageButton)findViewById(R.id.logout);
	}

	@Override
	public void setListener() {
		point.setOnClickListener(this);
		logout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.point:
			gotoActivity(LocationPointActivity.class, false);
			break;
		case R.id.logout:
			gotoActivity(LoginActivity.class, false);
			break;
		default:
			break;
		}
	}

}
