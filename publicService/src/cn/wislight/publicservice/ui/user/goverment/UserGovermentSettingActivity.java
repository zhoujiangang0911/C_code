package cn.wislight.publicservice.ui.user.goverment;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.wislight.publicservice.R;
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.ui.LoginActivity;
import cn.wislight.publicservice.ui.user.LocationPointActivity;

/**
 * 设置界面
 * @author Administrator
 *
 */
public class UserGovermentSettingActivity extends BaseActivity implements OnClickListener{
	private RelativeLayout point;
	private RelativeLayout aboutus;
	private RelativeLayout checkupdate;
	@Override
	public void setUpView() {
		setContentView(R.layout.activity_user_goverment_setting);
		
		point=(RelativeLayout)findViewById(R.id.point);
		aboutus=(RelativeLayout)findViewById(R.id.aboutus);
		checkupdate=(RelativeLayout)findViewById(R.id.checkupdate);
	}

	@Override
	public void setListener() {
		point.setOnClickListener(this);
		aboutus.setOnClickListener(this);
		checkupdate.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.point:
			//gotoActivity(LocationPointActivity.class, false);
			gotoActivity(UserAddCommonlyAddressActivity.class, false);
			break;
		case R.id.aboutus:
			gotoActivity(UserGovermentAboutActivity.class, false);
			break;
		case R.id.checkupdate:
			gotoActivity(UserGovermentUpdateActivity.class, false);
			break;
		default:
			break;
		}
	}

}
