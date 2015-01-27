package cn.wislight.meetingsystem.ui.home;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.ui.conference.ConCheckActivity;
import cn.wislight.meetingsystem.ui.conference.ConControllerActivity;
import cn.wislight.meetingsystem.ui.conference.ConJoinActivity;
import cn.wislight.meetingsystem.ui.conference.ConPublishActivity;
import cn.wislight.meetingsystem.ui.topic.TopicControlActivity;
import cn.wislight.meetingsystem.ui.topic.TopicStayActivity;
import cn.wislight.meetingsystem.ui.vote.VoteMyStayActivity;
import cn.wislight.meetingsystem.util.IntentUtil;
import cn.wislight.meetingsystem.util.LoadingDialog;
import cn.wislight.meetingsystem.util.MeetingSystemClient;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * @author Administrator 议题库
 */
public class WaitMyHandleJobsActivity extends BaseActivity {
	TextView tvCheckTopic;
	TextView tvCtrlTopic;
	TextView tvCheckMeet;
	TextView tvPublishMeet;
	TextView tvCtrlMeet;
	TextView tvAttendMeet;
	TextView tvVote;
	LoadingDialog loadingdiag;

	@Override
	public void initView() {

		tvCheckTopic = (TextView) findViewById(R.id.tv_check_topic);
		tvCtrlTopic = (TextView) findViewById(R.id.tv_ctrl_topic);
		tvCheckMeet = (TextView) findViewById(R.id.tv_check_meet);
		tvPublishMeet = (TextView) findViewById(R.id.tv_publish_meet);
		tvCtrlMeet = (TextView) findViewById(R.id.tv_ctrl_meet);
		tvAttendMeet = (TextView) findViewById(R.id.tv_attend_meet);
		tvVote = (TextView) findViewById(R.id.tv_vote);

		loadingdiag = new LoadingDialog(this);
		loadingdiag.setCanceledOnTouchOutside(false);
		loadingdiag.setText(getString(R.string.loading));

		getAllJobs();
	}

	@Override
	public void setupView() {
		setContentView(R.layout.wait_myhandle_jobs);
	}

	protected void onResume(){
		getAllJobs();
		super.onResume();
	}
	
	private void getAllJobs() {
		// TODO Auto-generated method stub
		loadingdiag.show();

		tvCheckTopic.setVisibility(View.GONE);
		tvCtrlTopic.setVisibility(View.GONE);
		tvCheckMeet.setVisibility(View.GONE);
		tvPublishMeet.setVisibility(View.GONE);
		tvCtrlMeet.setVisibility(View.GONE);
		tvAttendMeet.setVisibility(View.GONE);
		tvVote.setVisibility(View.GONE);
		
		String url = "MeetingManage/mobile/findWaitMyHandle.action";
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();
				Toast.makeText(WaitMyHandleJobsActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
				finish();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);

				loadingdiag.hide();
				if (response.contains("用户未登陆")){
					Toast.makeText(WaitMyHandleJobsActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				try {
					JSONObject jso = new JSONObject(response);
					int cntCheckTopic = jso.getInt("cntCheckTopic");
					int cntCtrlTopic = jso.getInt("cntCtrlTopic");
					int cntCheckMeet = jso.getInt("cntCheckMeet");
					int cntPublicMeet = jso.getInt("cntPublicMeet");
					int cntCtrlMeet = jso.getInt("cntCtrlMeet");
					int cntAttendMeet = jso.getInt("cntAttendMeet");
					int cntVote = jso.getInt("cntVote");
					
					if (cntCheckTopic > 0){
						tvCheckTopic.setVisibility(View.VISIBLE);
						tvCheckTopic.setText(getString(R.string.job_check_topic) + cntCheckTopic);
					}
					
					if (cntCtrlTopic > 0){
						tvCtrlTopic.setVisibility(View.VISIBLE);
						tvCtrlTopic.setText(getString(R.string.job_ctrl_topic) + cntCtrlTopic);
					}
					
					if (cntCheckMeet > 0){
						tvCheckMeet.setVisibility(View.VISIBLE);
						tvCheckMeet.setText(getString(R.string.job_check_meet) + cntCheckMeet);
					}
					
					if (cntPublicMeet > 0){
						tvPublishMeet.setVisibility(View.VISIBLE);
						tvPublishMeet.setText(getString(R.string.job_publish_meet) + cntPublicMeet);
					}
					
					if (cntCtrlMeet > 0){
						tvCtrlMeet.setVisibility(View.VISIBLE);
						tvCtrlMeet.setText(getString(R.string.job_ctrl_meet) + cntCtrlMeet);
					}
					
					if (cntAttendMeet > 0){
						tvAttendMeet.setVisibility(View.VISIBLE);
						tvAttendMeet.setText(getString(R.string.job_attend_meet) + cntAttendMeet);
					}
					
					if (cntVote > 0){
						tvVote.setVisibility(View.VISIBLE);
						tvVote.setText(getString(R.string.job_vote) + cntVote);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					loadingdiag.hide();
					Toast.makeText(WaitMyHandleJobsActivity.this,
							getString(R.string.error_dataabout),
							Toast.LENGTH_SHORT).show();
					finish();
				}

			}
		});
	}

	public void clickRefresh(View view){
		getAllJobs();
	}
	
	public void clickCheckTopic(View view){
		IntentUtil.startActivity(this, TopicStayActivity.class);
	}
	
	public void clickCtrlTopic(View view){
		IntentUtil.startActivity(this, TopicControlActivity.class);
	}

	public void clickCheckMeet(View view){
		IntentUtil.startActivity(this,ConCheckActivity.class);
	}

	public void clickPublishMeet(View view){
		IntentUtil.startActivity(this, ConPublishActivity.class);
	}

	public void clickCtrlMeet(View view){
		IntentUtil.startActivity(this, ConControllerActivity.class);
	}
	
	public void clickAttendMeet(View view){
		IntentUtil.startActivity(this,ConJoinActivity.class);
	}
	
	public void clickVote(View view){
		IntentUtil.startActivity(this, VoteMyStayActivity.class);
	}	
}
