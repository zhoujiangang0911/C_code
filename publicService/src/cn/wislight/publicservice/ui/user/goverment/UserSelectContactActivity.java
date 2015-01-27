package cn.wislight.publicservice.ui.user.goverment;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.wislight.publicservice.R;
import cn.wislight.publicservice.base.BaseActivity;

/**
 * 常用联系人
 * @author Administrator
 *
 */
public class UserSelectContactActivity extends BaseActivity implements OnClickListener{
//	private RelativeLayout detail;
	private LinearLayout userSelectContactPeople_1;
	private LinearLayout userSelectContactPeople_2;
	private LinearLayout userSelectContactPeople_3;
	private TextView contactName_1;
	private TextView contactName_2;
	private TextView contactName_3;
	@Override
	public void setUpView() {
		setContentView(R.layout.activity_user_select_contact);
		
//		detail=(RelativeLayout)findViewById(R.id.detail);
		userSelectContactPeople_1=(LinearLayout)findViewById(R.id.userSelectContactPeople_1);
		userSelectContactPeople_2=(LinearLayout)findViewById(R.id.userSelectContactPeople_2);
		userSelectContactPeople_3=(LinearLayout)findViewById(R.id.userSelectContactPeople_3);
	 
		contactName_1 = (TextView)findViewById(R.id.contactName_1);
		contactName_2 = (TextView)findViewById(R.id.contactName_2);
		contactName_3 = (TextView)findViewById(R.id.contactName_3);
	
	}

	@Override
	public void setListener() {
//		detail.setOnClickListener(this);
		userSelectContactPeople_1.setOnClickListener(this);
		userSelectContactPeople_2.setOnClickListener(this);
		userSelectContactPeople_3.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		String name = "";
		Intent intent = null;
		switch (v.getId()) {
//		case R.id.detail:
//			gotoActivity(DetailUserActivity.class, false);
//			break;
		case R.id.userSelectContactPeople_1:
			name = contactName_1.getText().toString();
			intent = new Intent();
			intent.putExtra("name", name);
			setResult(Activity.RESULT_OK, intent);
			finish();
			break;
		case R.id.userSelectContactPeople_2:
			name = contactName_2.getText().toString();
			intent = new Intent();
			intent.putExtra("name", name);
			setResult(Activity.RESULT_OK, intent);
			finish();
			break;
		case R.id.userSelectContactPeople_3:
			name = contactName_3.getText().toString();
			intent = new Intent();
			intent.putExtra("name", name);
			setResult(Activity.RESULT_OK, intent);
			finish();
			break;

		default:
			break;
		}
	}

}
