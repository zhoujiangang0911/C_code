package cn.wislight.publicservice.ui;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import cn.wislight.publicservice.R;
import cn.wislight.publicservice.base.BaseActivity;

/**
 * 
 * 服务者注册
 * @author Administrator
 *
 */
public class RegisterActivity extends BaseActivity implements OnClickListener{
	private ImageButton register;
	@Override
	public void setUpView() {
		setContentView(R.layout.activity_register_servant);
		
		
		register=(ImageButton)findViewById(R.id.register);
	}

	@Override
	public void setListener() {
		register.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register:
			gotoActivity(LoginActivity.class, false);
			break;

		default:
			break;
		}
	}
	
}
