package cn.wislight.publicservice.ui;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import cn.wislight.publicservice.R;
import cn.wislight.publicservice.base.BaseActivity;

/**
 * 
 * 注册主界面
 * @author Administrator
 *
 */
public class RegisgerMainActivity extends BaseActivity implements OnClickListener{

	private RelativeLayout personregister,govermentregister,serverregister,managerregister,customercommregister,enterpriceRegister;
	@Override
	public void setUpView() {
		
		setContentView(R.layout.activity_register);

		personregister=(RelativeLayout)findViewById(R.id.personregister);
		govermentregister=(RelativeLayout)findViewById(R.id.govermentregister);
		serverregister=(RelativeLayout)findViewById(R.id.servantregister);
		managerregister=(RelativeLayout)findViewById(R.id.managerregister);
		customercommregister=(RelativeLayout)findViewById(R.id.customercommregister);
		enterpriceRegister=(RelativeLayout)findViewById(R.id.enterpriceRegister);
	}

	@Override
	public void setListener() {
		personregister.setOnClickListener(this);
		govermentregister.setOnClickListener(this);
		serverregister.setOnClickListener(this);
		managerregister.setOnClickListener(this);
		customercommregister.setOnClickListener(this);
		enterpriceRegister.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.personregister:
			gotoActivity(RegisterPersonalActivity.class, false);
			break;
		case R.id.govermentregister:
			//Toast.makeText(this, "暂未开通", 100).show();
			//gotoActivity(RegisterOrganizationActivity.class, false);
			break;
		case R.id.servantregister:
			//Toast.makeText(this, "暂未开通", 100).show();
			gotoActivity(RegisterServantActivity.class, false);
			break;
		case R.id.managerregister:
			//Toast.makeText(this, "暂未开通", 100).show();
//			gotoActivity(RegisterPersonalActivity.class, false);
			break;
		case R.id.customercommregister:
			gotoActivity(RegisterBusinessPersonalActivity.class, false);
			break;
		case R.id.enterpriceRegister:
			gotoActivity(RegisterBusinessCompanyActivity.class, false);
			break;
		default:
			break;
		}
	}

}
