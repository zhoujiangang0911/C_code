package cn.wislight.publicservice.ui.governmentaffairs;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import cn.wislight.publicservice.R;
import cn.wislight.publicservice.base.BaseActivity;


/**
 * 代办
 * @author Administrator
 *
 */
public class ProxyHandleActivity extends BaseActivity implements OnClickListener{
	private RadioGroup group;
	private ImageView detail;
	@Override
	public void setUpView() {
		setContentView(R.layout.activity_proxyhandle_two);

		group=(RadioGroup)findViewById(R.id.group);
		
		detail=(ImageView)findViewById(R.id.detail);
	}
	@Override
	public void setListener() {
		detail.setOnClickListener(this);
		
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				
				switch (checkedId) {
				case 0:
					
					break;
				case 1:
					
					break;
					
				default:
					break;
				}
				
			}
		});
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.detail:
			gotoActivity(DetailGovermentActivity.class, false);
			break;

		default:
			break;
		}
	}

}
