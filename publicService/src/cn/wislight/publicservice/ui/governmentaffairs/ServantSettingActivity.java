package cn.wislight.publicservice.ui.governmentaffairs;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import cn.wislight.publicservice.R;
import cn.wislight.publicservice.R.id;
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.ui.LoginActivity;

/**
 * 设置界面
 * @author Administrator
 *
 */
public class ServantSettingActivity extends BaseActivity implements OnClickListener{
	private TextView point;
	private ImageButton logout;
	
	private TextView txtAbout;
	private TextView txtUpdate;
	
	@Override
	public void setUpView() {
		setContentView(R.layout.activity_servant_setting);
		
		point=(TextView)findViewById(R.id.point);
		logout=(ImageButton)findViewById(R.id.logout);
		
		txtAbout=(TextView)findViewById(R.id.about);
		txtUpdate=(TextView)findViewById(R.id.update);
	}

	@Override
	public void setListener() {
		point.setOnClickListener(this);
		logout.setOnClickListener(this);
		
		txtAbout.setOnClickListener(this);
		txtUpdate.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.point:
			//gotoActivity(LocationPointActivity.class, false);
			break;
		case R.id.logout:
			gotoActivity(LoginActivity.class, false);
			break;
			
		case R.id.about:
			gotoActivity(ServantAboutActivity.class, false);
			break;
		case R.id.update:
			gotoActivity(ServantUpdateActivity.class, false);
			break;
		default:
			break;
		}
	}

}
