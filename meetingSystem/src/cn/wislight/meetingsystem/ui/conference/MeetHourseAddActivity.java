package cn.wislight.meetingsystem.ui.conference;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpResponseHandler;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.ui.topic.TopicCheckDetailActivity;
import cn.wislight.meetingsystem.ui.topic.TopicStayActivity;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.MeetingSystemClient;

/**
 * @author Administrator 创建群组 step1
 */
public class MeetHourseAddActivity extends BaseActivity {
	private SharedPreferences room;
	private EditText etSitsCount;
	private EditText etDetail;
	private EditText etAddr;

	private Spinner mSpinner;
	private ArrayAdapter<String> mSpAdapter;
	private String[] mStringArray;

	@Override
	public void initView() {
		// TODO Auto-generated method stub

		etSitsCount = (EditText) findViewById(R.id.et_sits_count);
		etDetail = (EditText) findViewById(R.id.et_detail);
		etAddr = (EditText) findViewById(R.id.et_addr);
		mSpinner = (Spinner) findViewById(R.id.sp_room_type);
		
		mStringArray = getResources().getStringArray(
				R.array.meet_hourse_type_array);
		mSpAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, mStringArray);
		mSpAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner.setAdapter(mSpAdapter);
		/*
		 * room = this.getSharedPreferences(Constants.GROUP_NODE+
		 * Variables.loginname, MODE_PRIVATE);
		 * 
		 * 
		 * String group_name = room.getString(Constants.GROUP_NAME, ""); String
		 * group_remark = room.getString(Constants.GROUP_REMARK, "");
		 * 
		 * etSitsCount.setText(group_name); etGremark.setText(group_remark);
		 */
	}

	@Override
	public void setupView() {
		setContentView(R.layout.meet_hourse_add);
	}

	protected void onPause() {
		super.onPause();

		/*
		 * Editor editor = room.edit();
		 * 
		 * String group_name = etGname.getText().toString(); String group_remark
		 * = etGremark.getText().toString();
		 * editor.putString(Constants.GROUP_NAME, group_name);
		 * editor.putString(Constants.GROUP_REMARK, group_remark);
		 * editor.commit();
		 */
	}

	public void clickSubmit(View view) {
		if (etAddr.getText().toString().length() <= 0) {
			Toast.makeText(MeetHourseAddActivity.this,
					getString(R.string.error_no_room_addr), Toast.LENGTH_SHORT)
					.show();
			return;
		}
		if (etDetail.getText().toString().length() <= 0) {
			Toast.makeText(MeetHourseAddActivity.this,
					getString(R.string.error_no_room_detail),
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (etSitsCount.getText().toString().length() <= 0) {
			Toast.makeText(MeetHourseAddActivity.this,
					getString(R.string.error_no_room_sits), Toast.LENGTH_SHORT)
					.show();
			return;
		}

		int type = mSpinner.getSelectedItemPosition();

		String url = "MeetingManage/mobile/addMeetPlce.action?";
		url += "hourseAddr=" + etAddr.getText().toString();
		url += "&hourseDetail=" + etDetail.getText().toString();
		url += "&hourseSitsCount=" + etSitsCount.getText().toString();
		url += "&commonType=" + type;

		System.out.println("wangting:" + url);
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				System.out.println("wangting, success response="
						+ error.getMessage());
				Toast.makeText(MeetHourseAddActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				System.out.println("wangting, success response=" + response);
				// Toast.makeText(TopicSelectApplicatorActivity.this, new
				// String(response)+"wangting"+response.toString(), 100).show();
				if (response.contains("用户未登陆")){
					Toast.makeText(MeetHourseAddActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				if (response.contains("true")) {
					Toast.makeText(MeetHourseAddActivity.this,
							getString(R.string.result_success),
							Toast.LENGTH_SHORT).show();
					finish();
					
					Intent intent = new Intent();    
					intent.setClass((Context)MeetHourseAddActivity.this, ConfSelectHourseActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);   
					startActivity(intent);  
					
				} else {
					Toast.makeText(MeetHourseAddActivity.this,
							getString(R.string.error_upload_failed),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}
