package cn.wislight.meetingsystem.ui.topic;

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
import cn.wislight.meetingsystem.util.AttachmentListActivity;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.IntentUtil;
import cn.wislight.meetingsystem.util.LoadingDialog;
import cn.wislight.meetingsystem.util.MeetingSystemClient;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * @author Administrator 我的议题 -> 议题详情
 */
public class TopicMyTopicDetailActivity extends BaseActivity {
	private TextView tvSummary, tvKeywords, tvStartdate, tvEnddate,
			tvTargetOrg;
	private TextView tvChecker, tvSuggestedAttender, tvCreator;
	private TextView tvReason, tvState;
	
	private LoadingDialog loadingdiag;
	String topicNo;
	String tpid;
	@Override
	public void initView() {

		tvSummary = (TextView) findViewById(R.id.tv_summary);
		tvKeywords = (TextView) findViewById(R.id.tv_keywords);
		tvStartdate = (TextView) findViewById(R.id.tv_starttime);
		tvEnddate = (TextView) findViewById(R.id.tv_endtime);
		tvTargetOrg = (TextView) findViewById(R.id.tv_target_org);
		tvChecker = (TextView) findViewById(R.id.tv_checker);
		tvSuggestedAttender = (TextView) findViewById(R.id.tv_suggested_attender);
		tvCreator = (TextView) findViewById(R.id.tv_creator);
		tvReason = (TextView) findViewById(R.id.tv_topic_check_reason);
		tvState = (TextView) findViewById(R.id.tv_title_state);

		Intent intent = getIntent();
	    tpid = intent.getStringExtra("id");
		
		loadingdiag = new LoadingDialog(this);  
		loadingdiag.setCanceledOnTouchOutside(false); 
		loadingdiag.setText(getString(R.string.loading));
		
		
		getTopicDetail(tpid);
	}

	@Override
	public void setupView() {
		setContentView(R.layout.topic_main_detail);
	}

	protected void onPause() {
		super.onPause();

	}

	public void clickAttachmentList(View view){
		Intent data=new Intent();  
        data.putExtra(Constants.ID, topicNo);
        
        IntentUtil.startActivity(TopicMyTopicDetailActivity.this, AttachmentListActivity.class,  data);
	}

	private void getTopicDetail(String id) {
		// TODO Auto-generated method stub
		String url = "MeetingManage/mobile/findTopicDetailById.action?tpid="
				+ id;
		loadingdiag.show();  
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();  
				Toast.makeText(TopicMyTopicDetailActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
				finish();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				loadingdiag.hide();
				String response = new String(body);
				// Toast.makeText(TopicSelectApplicatorActivity.this, new
				// String(response)+"wangting"+response.toString(), 100).show();
				if (response.contains("用户未登陆")){
					Toast.makeText(TopicMyTopicDetailActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				// org_listview.setVisibility(View.VISIBLE);
				// loading.setVisibility(View.GONE);
				
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(response)
							.getJSONObject("tpDetail");
					
					String topic_keys = jsonObject.getString("keywords");
					String topic_summary = jsonObject.getString("summary");
					String topic_start_date = jsonObject.getString("starttime");
					String topic_end_date = jsonObject.getString("endtime");
					String topic_target_org_name = jsonObject.getString("target_org");
					String topic_suggested_attender_names = jsonObject
							.getString("suggested_attender");
					String topic_checker = jsonObject.getString("checker");
					String topic_creator = jsonObject.getString("creator");
					String topic_check_reason = jsonObject.getString("check_reason");
					topicNo = jsonObject.getString("topicno");

					int state = Integer.parseInt(jsonObject.getString("check_state"));
					int[] states= {
							R.string.topic_status_checkreject,
							R.string.topic_status_tocheck,
							R.string.topic_status_checkpass
						};
					tvState.setText(states[state]);
					
					tvSummary.setText(topic_summary);
					tvKeywords.setText(topic_keys);
					tvStartdate.setText(topic_start_date);
					tvEnddate.setText(topic_end_date);
					tvTargetOrg.setText(topic_target_org_name);
					tvChecker.setText(topic_checker);
					tvSuggestedAttender.setText(topic_suggested_attender_names);
					tvCreator.setText(topic_creator);
					tvReason.setText(topic_check_reason);

				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(TopicMyTopicDetailActivity.this,
							getString(R.string.error_dataabout),
							Toast.LENGTH_SHORT).show();
					finish();

				}
			}
		});
	}

}
