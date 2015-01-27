package cn.wislight.publicservice.ui.user;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import cn.wislight.publicservice.R;
import cn.wislight.publicservice.base.BaseActivity;

/**
 * 常用地点
 * @author Administrator
 *
 */
public class LocationPointActivity extends BaseActivity implements OnClickListener{
	private ImageButton addlocation;
	@Override
	public void setUpView() {
		setContentView(R.layout.activity_locationpoint);
		
		addlocation=(ImageButton) findViewById(R.id.addlocation);
	}

	@Override
	public void setListener() {
		addlocation.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.addlocation:
			gotoActivity(AddNewLocationActivity.class, false);
			break;

		default:
			break;
		}
	}

}
