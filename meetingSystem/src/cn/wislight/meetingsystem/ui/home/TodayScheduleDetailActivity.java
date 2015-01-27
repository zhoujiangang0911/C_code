package cn.wislight.meetingsystem.ui.home;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.ui.conference.ConfControllerDetailActivity;
import cn.wislight.meetingsystem.ui.conference.ConfJoinConfMemberListActivity;
import cn.wislight.meetingsystem.ui.conference.ConfJoinMemberListActivity;
import cn.wislight.meetingsystem.ui.conference.ConfTopicListActivity;
import cn.wislight.meetingsystem.ui.topic.TopicStayActivity;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.IntentUtil;
import cn.wislight.meetingsystem.util.LoadingDialog;
import cn.wislight.meetingsystem.util.MeetingSystemClient;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * @author Administrator 主页 新增议题第三个
 */
public class TodayScheduleDetailActivity extends BaseActivity {
	private TextView tvTitle,tvRemark, tvAddress, tvStartdate, tvEnddate;
	private TextView tvJoinMember, tvTopicList;
	private TextView tvConfState;
	//private TextView tvChecker, tvSuggestedAttender, tvCreator;
	private EditText etReason;
	private LoadingDialog loadingdiag;

	private String checkReason;
	String meetid;
	@Override
	public void initView() {
		tvJoinMember=(TextView) findViewById(R.id.conf_joinmember);
		tvTopicList=(TextView) findViewById(R.id.conf_topic_list);
		tvJoinMember.setVisibility(View.VISIBLE);
		tvTopicList.setVisibility(View.VISIBLE);
		

		tvTitle = (TextView) findViewById(R.id.conference_title);
		tvRemark = (TextView) findViewById(R.id.conference_remark);
		tvAddress = (TextView) findViewById(R.id.conference_address);
		tvStartdate = (TextView) findViewById(R.id.tv_starttime);
		tvEnddate = (TextView) findViewById(R.id.tv_endtime);

		tvConfState = (TextView) findViewById(R.id.conf_state);
		//tvChecker = (TextView) findViewById(R.id.tv_checker);
		//tvSuggestedAttender = (TextView) findViewById(R.id.tv_suggested_attender);
		//tvCreator = (TextView) findViewById(R.id.tv_creator);
		etReason = (EditText) findViewById(R.id.conference_check_reason);

		Intent intent = getIntent();
	    meetid = intent.getStringExtra("id");
		
		loadingdiag = new LoadingDialog(this);  
		loadingdiag.setCanceledOnTouchOutside(false); 
		loadingdiag.setText(getString(R.string.loading));
		
		
		getConferenceDetail(meetid);
	}

	@Override
	public void setupView() {
		setContentView(R.layout.today_schedule_detail);
	}

	protected void onPause() {
		super.onPause();

	}
	public void clickJoinMember(View view) {
		Intent data=new Intent();  
        data.putExtra(Constants.ID, meetid);  
        IntentUtil.startActivity(TodayScheduleDetailActivity.this, ConfJoinMemberListActivity.class,  data);
  
	}
	
	public void clickConfJoinMember(View view) {
		Intent data=new Intent();  
        data.putExtra(Constants.ID, meetid);  
        IntentUtil.startActivity(TodayScheduleDetailActivity.this, ConfJoinConfMemberListActivity.class,  data);
	}
	
	public void clickTopicList(View view) {
		Intent data=new Intent();  
        data.putExtra(Constants.ID, meetid);  
        IntentUtil.startActivity(TodayScheduleDetailActivity.this, ConfTopicListActivity.class,  data);
  
	}
	/*
	public void clickRefuse(View view) {
		if (etReason.getText().toString().length() <= 0){
			Toast.makeText(DayScheduleDetailActivity.this,
				getString(R.string.error_topic_check_noreason), Toast.LENGTH_SHORT).show();
			return;
		}		
		checkReason = etReason.getText().toString();
		checkConference(0, checkReason);
	}

	

	public void clickPass(View view) {
		if (etReason.getText().toString().length() <= 0){
			Toast.makeText(DayScheduleDetailActivity.this,
				getString(R.string.error_topic_check_noreason), Toast.LENGTH_SHORT).show();
			return;
		}
		checkReason = etReason.getText().toString();
		checkConference(1, checkReason);
	}

	private void checkConference(int checkState, String checkReason) {
		String url = "MeetingManage/mobile/doVerifyMeeting.action?meetingId="+ meetid
				     + "&action=" + checkState
				     + "&verifyMsg=" + checkReason;
		loadingdiag.setText(getString(R.string.uploading));
		loadingdiag.show();  
		System.out.println("wangting"+url);
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();  
				System.out.println("wangting"+error.getMessage());
				Toast.makeText(DayScheduleDetailActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
				//finish();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				loadingdiag.hide();
				String response = new String(body);
				System.out.println("wangting"+response);
				if (response.contains("\"SUCCESS\":true")){
					Toast.makeText(DayScheduleDetailActivity.this, "审核成功", Toast.LENGTH_SHORT).show();
					finish();
					
					//Intent intent = new Intent();    
					//intent.setClass((Context)ConfCheckDetailActivity.this, TopicStayActivity.class);
					//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);   
					//startActivity(intent);  
					
				}else{
					Toast.makeText(DayScheduleDetailActivity.this, "审核失败", Toast.LENGTH_SHORT).show();
				}
			}
		});		
	}
	*/
	private void getConferenceDetail(String id) {
		// TODO Auto-generated method stub
		String url = "MeetingManage/mobile/findMeetingDetailById.action?type=1&meetid="+id;//type=1 审核通过的
		loadingdiag.show();  
		System.out.println("wangting"+url);
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();  
				Toast.makeText(TodayScheduleDetailActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
				System.out.println("wangting"+error.getMessage());
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {				
				loadingdiag.hide();
				String response = new String(body);				
				System.out.println("wangting"+response);
				
				if (response.contains("用户未登陆")){
					Toast.makeText(TodayScheduleDetailActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(response);				               
					tvTitle.setText(jsonObject.getString("title"));
					tvRemark.setText(jsonObject.getString("remark"));
					tvAddress.setText(jsonObject.getString("address"));
					tvStartdate.setText(jsonObject.getString("startdate"));
					tvEnddate.setText(jsonObject.getString("enddate"));
					try{
					int state = Integer.parseInt(jsonObject.getString("meetstatu"));
					String states[] = {"待审核", "待发布",  "待参加", "进行中", "已归档"};
					tvConfState.setText(states[state]);
					}catch(Exception e){
						e.printStackTrace();
					}

				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(TodayScheduleDetailActivity.this,
							getString(R.string.error_dataabout),
							Toast.LENGTH_SHORT).show();
					
				}
			}
		});
	}

}
