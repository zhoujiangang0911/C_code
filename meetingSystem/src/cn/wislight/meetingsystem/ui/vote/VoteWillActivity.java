package cn.wislight.meetingsystem.ui.vote;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpResponseHandler;

import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.LoadingDialog;
import cn.wislight.meetingsystem.util.MeetingSystemClient;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 即将表决
 * @author sun
 *
 */
public class VoteWillActivity extends BaseActivity{
	private String topicId;
	private String topicNo;
	private TextView tvSummary;
	private TextView tvKeywords;
	private TextView tvVoteType;

	private TextView tvTopicResult;
	
	private LoadingDialog loadingdiag;
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		tvSummary = (TextView) findViewById(R.id.tv_vote_summary);
		tvKeywords = (TextView) findViewById(R.id.tv_vote_keywords);
		tvTopicResult = (TextView) findViewById(R.id.tv_vote_topic_result);
		tvVoteType = (TextView) findViewById(R.id.tv_vote_type);
		
		topicId = getIntent().getStringExtra(Constants.ID);
		topicNo = getIntent().getStringExtra(Constants.TOPIC_NO);
		loadingdiag = new LoadingDialog(this);  
		loadingdiag.setCanceledOnTouchOutside(false); 
		
		getTopicDetailByTopicNo(topicNo);
	}

	@Override
	public void setupView() {
		setContentView(R.layout.vote_will_main);
	}

	public void  clickAgree(View view){
		doVote("1");	
	}
	public void  clickUnagree(View view){
		doVote("2");	
	}
	public void  clickGiveup(View view){
		doVote("0");	
	}
	private void doVote(String voteFlag){
		
		
		String url = "MeetingManage/mobile/doMeetingProcVote.action?meetingProcId="+topicNo+"&voteFlag="+voteFlag;
		loadingdiag.setText(getString(R.string.do_vote));
		System.out.println("wangting:"+url);
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();
				String response = error.getMessage();
				System.out.println("wangting, fail response="+response);
				Toast.makeText(VoteWillActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				System.out.println("wangting, success response="+response);
				// Toast.makeText(TopicSelectApplicatorActivity.this, new String(response)+"wangting"+response.toString(), 100).show();
				loadingdiag.hide();
				
				if (response.contains("用户未登陆")){
					Toast.makeText(VoteWillActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				if(response.contains("true")){
					Toast.makeText(VoteWillActivity.this, "投票成功", 100).show();
					finish();
					
					Intent intent = new Intent();    
					intent.setClass((Context)VoteWillActivity.this, VoteMyStayActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);   
					startActivity(intent);  
					
				}else{
					try {
						JSONObject json = new JSONObject(response);
						String msg = json.getString("msg");
						Toast.makeText(VoteWillActivity.this, msg, 100).show();
						return;
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}			
					Toast.makeText(VoteWillActivity.this, "投票失败", 100).show();
				}			
			}
		});				
	}
	
	
	private void getTopicDetailByTopicNo(String id) {
		// TODO Auto-generated method stub
		//String url = "MeetingManage/mobile/findTopicDetailById.action?tpid="+ id;
		//String url = "MeetingManage/mobile/findTopicDetailById.action?tpid="+ id;
		String url = "MeetingManage/mobile/getMeetingProcDetail.action?type=0&meetingProcId="+ id;
		loadingdiag.setText(getString(R.string.loading));
		loadingdiag.show();  
		System.out.println("wangting:url="+url);
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();  
				Toast.makeText(VoteWillActivity.this,
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
					Toast.makeText(VoteWillActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				// org_listview.setVisibility(View.VISIBLE);
				// loading.setVisibility(View.GONE);
				System.out.println("wangting"+response);
				JSONObject jsonObject;
				try {
					JSONObject jsonobject = new JSONObject(response);
					
					String topic_keys = jsonobject.getString("content");
					String topic_summary = jsonobject.getString("name");
					String topic_start_date = jsonobject.getString("startTime");
					String topic_end_date = jsonobject.getString("endTime");
					String topic_checker_name = jsonobject.getString("checkername");
					//String topic_result = jsonobject.getString("topic_result");
					String votestate = jsonobject.getString("votestate");
					String conclusion = jsonobject.getString("conclusion");
					//String topic_target_org_name = jsonobject.getString("target_org");
					int votetype = Integer.parseInt(jsonobject.getString("votetype"));

					tvSummary.setText(topic_summary);
					tvKeywords.setText(topic_keys);
					tvTopicResult.setText(conclusion);
					String VoteTypes[] = {"匿名","记名"};
					
					tvVoteType.setText(VoteTypes[votetype]);
					//tvEnddate.setText(topic_end_date);
					//tvTargetOrg.setText(topic_target_org_name);
					//tvChecker.setText(topic_checker);
					//tvSuggestedAttender.setText(topic_suggested_attender_names);
					//tvCreator.setText(topic_creator);

				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(VoteWillActivity.this,
							getString(R.string.error_dataabout),
							Toast.LENGTH_SHORT).show();
					finish();

				}
			}
		});
	}
}
