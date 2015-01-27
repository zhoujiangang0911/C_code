package cn.wislight.meetingsystem.ui.topic;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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
 * @author Administrator 主页 新增议题第三个
 */
public class TopicDetailComplexViewActivity extends BaseActivity {
	private SharedPreferences topic;
	private TextView tvSummary, tvKeywords, tvStartdate, tvEnddate;
	private TextView tvChecker;
	private String topicNo;
	private String topicId;
	private TextView tvAttender, tvTopicVoteStaticer, tvTopicVoteController,
			tvTopicController, tvVoteEndtime, tvVoteStarttime,
			tvTopicVoteAbstain, tvTopicVoteType, tvIsMeetcall, tvIsVote;
	private LinearLayout llVote;
	private LoadingDialog loadingdiag;
	private TextView tvTopicAttribute;
	
	String IMgroupid = "";

	@Override
	public void initView() {

		topicNo = this.getIntent().getStringExtra(Constants.ID);
		topicId = this.getIntent().getStringExtra(Constants.TOPIC_ID);

		loadingdiag = new LoadingDialog(this);
		loadingdiag.setCanceledOnTouchOutside(false);
		loadingdiag.setText(getString(R.string.loading));

		tvSummary = (TextView) findViewById(R.id.tv_summary);
		tvKeywords = (TextView) findViewById(R.id.tv_keywords);
		tvStartdate = (TextView) findViewById(R.id.tv_starttime);
		tvEnddate = (TextView) findViewById(R.id.tv_endtime);
		// tvTargetOrg = (TextView)findViewById(R.id.tv_target_org);
		tvChecker = (TextView) findViewById(R.id.tv_checker);

		tvAttender = (TextView) findViewById(R.id.tv_attender);
		tvTopicVoteStaticer = (TextView) findViewById(R.id.tv_topic_vote_staticer);
		tvTopicVoteController = (TextView) findViewById(R.id.tv_topic_vote_controller);
		tvTopicController = (TextView) findViewById(R.id.tv_topic_controller);
		tvVoteEndtime = (TextView) findViewById(R.id.tv_vote_endtime);
		tvVoteStarttime = (TextView) findViewById(R.id.tv_vote_starttime);
		tvTopicVoteAbstain = (TextView) findViewById(R.id.tv_topic_vote_abstain);
		tvTopicVoteType = (TextView) findViewById(R.id.tv_topic_vote_type);
		tvIsVote = (TextView) findViewById(R.id.tv_is_vote);
		tvIsMeetcall = (TextView) findViewById(R.id.tv_is_meetcall);
		llVote = (LinearLayout) findViewById(R.id.ll_vote);
		tvTopicAttribute = (TextView) findViewById(R.id.tv_topic_attribute);

		getTopicComplexDetail(topicNo);
		// getTopicDetailById(topicId);

	}

	private void getTopicDetailById(String topicId) {

		// TODO Auto-generated method stub
		String url = "MeetingManage/mobile/findTopicDetailExtById.action?tpid="
				+ topicId;
		loadingdiag.show();
		System.out.println("wangting:" + url);

		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();
				Toast.makeText(TopicDetailComplexViewActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onSuccess(int stautsCode, Header[] arg1, byte[] body) {
				String response = new String(body);
				loadingdiag.hide();
				if (response.contains("用户未登陆")){
					Toast.makeText(TopicDetailComplexViewActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				System.out.println("wangting:" + response);
				try {
					JSONObject json = new JSONObject(response);
					JSONObject jsonobject = json.getJSONObject("tpDetail");

					String topic_keys = jsonobject.getString("keywords");
					String topic_summary = jsonobject.getString("summary");
					String topic_start_date = jsonobject.getString("starttime");
					String topic_end_date = jsonobject.getString("endtime");
					String topic_checker_name = jsonobject.getString("checker");
					
					IMgroupid = jsonobject.getString("IMgroupid");
					
					String topic_attender = jsonobject
							.getString("suggested_attender");
					String topic_vote_staticer = jsonobject
							.getString("summng_name");
					String topic_vote_controller = jsonobject
							.getString("votemng_name");
					String topic_controller = jsonobject
							.getString("manager_name");
					String topic_vote_endtime = jsonobject
							.getString("voteend_date");
					String topic_vote_starttime = jsonobject
							.getString("votestart_date");
					String topic_vote_abstain = jsonobject
							.getString("is_abstain");
					String topic_vote_type = jsonobject.getString("vote_type");
					String topic_is_vote = jsonobject.getString("is_needvote");
					String topic_is_meetcall = jsonobject
							.getString("is_needmeetcall");
					String topic_source = jsonobject.getString("source");

					if ("1".equals(topic_is_vote)) {
						llVote.setVisibility(View.VISIBLE);
						tvIsVote.setText(R.string.common_yes);
					} else {
						llVote.setVisibility(View.GONE);
						tvIsVote.setText(R.string.common_no);
					}
					if ("1".equals(topic_is_meetcall)) {
						tvIsMeetcall.setText(R.string.common_yes);
					} else {
						tvIsMeetcall.setText(R.string.common_no);
					}
					tvTopicVoteStaticer.setText(topic_vote_staticer);
					tvTopicVoteController.setText(topic_vote_controller);
					tvTopicController.setText(topic_controller);
					tvVoteEndtime.setText(topic_vote_endtime);
					tvVoteStarttime.setText(topic_vote_starttime);
					if ("1".equals(topic_vote_abstain)) {
						tvTopicVoteAbstain.setText(R.string.common_yes);
					} else {
						tvTopicVoteAbstain.setText(R.string.common_no);
					}

					if ("1".equals(topic_vote_type)) {
						tvTopicVoteType.setText(R.string.topic_vote_withname);
					} else {
						tvTopicVoteType
								.setText(R.string.topic_vote_withoutname);
					}

					if (null != topic_source
							&& topic_source.contains("inmeetcreate")) {
						tvTopicAttribute
								.setText(R.string.topic_Attribute_inmeetcreate);
					} else {
						tvTopicAttribute
								.setText(R.string.topic_Attribute_topicdb);
					}
					tvSummary.setText(topic_summary);
					tvKeywords.setText(topic_keys);
					tvStartdate.setText(topic_start_date);
					tvEnddate.setText(topic_end_date);
					tvChecker.setText(topic_checker_name);
					tvAttender.setText(topic_attender);

				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(TopicDetailComplexViewActivity.this,
							getString(R.string.error_dataabout),
							Toast.LENGTH_SHORT).show();

				}
			}
		});

	}

	private void getTopicComplexDetail(String topicId2) {
		// TODO Auto-generated method stub
		String url = "MeetingManage/mobile/getMeetingProcDetail.action?type=0&meetingProcId="
				+ topicId2;
		loadingdiag.show();
		System.out.println("wangting:" + url);

		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();
				Toast.makeText(TopicDetailComplexViewActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onSuccess(int stautsCode, Header[] arg1, byte[] body) {
				String response = new String(body);
				loadingdiag.hide();
				if (response.contains("用户未登陆")){
					Toast.makeText(TopicDetailComplexViewActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				System.out.println("wangting:" + response);
				try {
					JSONObject jsonobject = new JSONObject(response);

					String topic_keys = jsonobject.getString("content");
					String topic_summary = jsonobject.getString("name");
					String topic_start_date = jsonobject.getString("startTime");
					String topic_end_date = jsonobject.getString("endTime");
					String topic_checker_name = jsonobject
							.getString("checkername");
					
					IMgroupid = jsonobject.getString("IMgroupid");

					String topic_attender = jsonobject.getString("attenders");
					String topic_vote_staticer = jsonobject
							.getString("summanageuser");
					String topic_vote_controller = jsonobject
							.getString("vomanageuser");
					String topic_controller = jsonobject
							.getString("manageuser");
					String topic_vote_endtime = jsonobject
							.getString("voendTime");
					String topic_vote_starttime = jsonobject
							.getString("vostaTime");
					String topic_vote_abstain = jsonobject
							.getString("isabstain");
					String topic_vote_type = jsonobject.getString("votetype");
					String topic_is_vote = jsonobject.getString("isneedvot");
					String topic_is_meetcall = jsonobject
							.getString("isneetmeetcall");
					String topic_source = jsonobject.getString("source");

					if ("1".equals(topic_is_vote)) {
						llVote.setVisibility(View.VISIBLE);
						tvIsVote.setText(R.string.common_yes);
					} else {
						llVote.setVisibility(View.GONE);
						tvIsVote.setText(R.string.common_no);
					}
					if ("1".equals(topic_is_meetcall)) {
						tvIsMeetcall.setText(R.string.common_yes);
					} else {
						tvIsMeetcall.setText(R.string.common_no);
					}
					tvTopicVoteStaticer.setText(topic_vote_staticer);
					tvTopicVoteController.setText(topic_vote_controller);
					tvTopicController.setText(topic_controller);
					tvVoteEndtime.setText(topic_vote_endtime);
					tvVoteStarttime.setText(topic_vote_starttime);
					if ("1".equals(topic_vote_abstain)) {
						tvTopicVoteAbstain.setText(R.string.common_yes);
					} else {
						tvTopicVoteAbstain.setText(R.string.common_no);
					}

					if ("1".equals(topic_vote_type)) {
						tvTopicVoteType.setText(R.string.topic_vote_withname);
					} else {
						tvTopicVoteType
								.setText(R.string.topic_vote_withoutname);
					}

					tvSummary.setText(topic_summary);
					tvKeywords.setText(topic_keys);
					tvStartdate.setText(topic_start_date);
					tvEnddate.setText(topic_end_date);
					tvChecker.setText(topic_checker_name);
					tvAttender.setText(topic_attender);
					if (null != topic_source
							&& topic_source.contains("inmeetcreate")) {
						tvTopicAttribute
								.setText(R.string.topic_Attribute_inmeetcreate);
					} else {
						tvTopicAttribute
								.setText(R.string.topic_Attribute_topicdb);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(TopicDetailComplexViewActivity.this,
							getString(R.string.error_dataabout),
							Toast.LENGTH_SHORT).show();

				}
			}
		});
	}

	@Override
	public void setupView() {
		setContentView(R.layout.topic_detail_view_complex);
	}

	protected void onPause() {
		super.onPause();

	}

	public void clickAttachmentList(View view) {
		Intent data = new Intent();
		data.putExtra(Constants.ID, topicNo);

		IntentUtil.startActivity(TopicDetailComplexViewActivity.this,
				AttachmentListActivity.class, data);
	}

	public void clickPre(View view) {
		IntentUtil.startActivity(this, TopicEditTwoActivity.class);
		this.finish();
	}

	public void clickSave(View view) {
		Toast.makeText(this, "save", 100).show();
		this.finish();
	}

	public void clickUpload(View view) {
		// IntentUtil.startActivity(this,TopicAddOneActivity.class);

		uploadTopic();

	}
	
	public void  clickMeetingCall(View view){
		String url="MeetingManage/mobile/joinInRoom.action?topicno="+topicNo;
		System.out.println("wangting:"+url);
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler(){
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				String response = error.getMessage();
				System.out.println("wangting, fail response="+response);
				Toast.makeText(TopicDetailComplexViewActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				
				if (response.contains("用户未登陆")){
					Toast.makeText(TopicDetailComplexViewActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				System.out.println("wangting, success response="+response);			
				if(response.contains("true")){
					Toast.makeText(TopicDetailComplexViewActivity.this, "申请成功", 100).show();
				}else{
					Toast.makeText(TopicDetailComplexViewActivity.this, "申请失败", 100).show();					
				}			
			}
		});		
	}
	
	public void  clickTopicvirtual(View view){
		Intent intent = new Intent();
		intent.putExtra("IMgroupid", IMgroupid);
		IntentUtil.startActivity(this,TopicVirtualActivity.class, intent);
		Toast.makeText(this, "manager", 100).show();
	}
	

	private void uploadTopic() {

		String topic_start_date = topic.getString(Constants.TOPIC_E_START_DATE,
				"");
		String topic_end_date = topic.getString(Constants.TOPIC_E_END_DATE, "");
		String topic_suggested_attender_ids = topic.getString(
				Constants.TOPIC_E_SUGGESTED_ATTENDER_IDS, "");
		String topic_vote_staticer_id = topic.getString(
				Constants.TOPIC_E_SUMMNG_ID, "");
		String topic_vote_controller_id = topic.getString(
				Constants.TOPIC_E_VOTEMNG_ID, "");
		String topic_controller_id = topic.getString(
				Constants.TOPIC_E_MANAGER_ID, "");
		String topic_vote_endtime = topic.getString(
				Constants.TOPIC_E_VOTE_ENDTIME, "");
		String topic_vote_starttime = topic.getString(
				Constants.TOPIC_E_VOTE_STARTTIME, "");
		String topic_vote_abstain = topic.getString(
				Constants.TOPIC_E_IS_ABSTAIN, "");
		String topic_vote_type = topic
				.getString(Constants.TOPIC_E_VOTETYPE, "");
		String topic_is_vote = topic.getString(Constants.TOPIC_E_IS_NEEDVOTE,
				"");
		String topic_is_meetcall = topic.getString(
				Constants.TOPIC_E_IS_NEEDMEETCALL, "");
		String topic_id = topic.getString(Constants.TOPIC_E_ID, "");

		if ("0".equals(topic_is_vote)) {
			topic_vote_starttime = topic_start_date;
			topic_vote_endtime = topic_end_date;
		}

		String url = "MeetingManage/mobile/updateTopic.action?";
		url += "&summng_id=" + topic_vote_staticer_id;
		url += "&votemng_id=" + topic_vote_controller_id;
		url += "&mng_id=" + topic_controller_id;
		url += "&vote_starttime=" + topic_vote_starttime;
		url += "&vote_endtime=" + topic_vote_endtime;
		url += "&is_abstain=" + topic_vote_abstain;
		url += "&votetype=" + topic_vote_type;
		url += "&is_vote=" + topic_is_vote;
		url += "&is_meetcall=" + topic_is_meetcall;
		url += "&starttime=" + topic_start_date;
		url += "&endtime=" + topic_end_date;
		url += "&attender=" + topic_suggested_attender_ids;
		url += "&topic_id=" + topic_id;

		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] arg1, byte[] body,
					Throwable error) {
				Toast.makeText(TopicDetailComplexViewActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onSuccess(int stautsCode, Header[] arg1, byte[] body) {
				String result = new String(body);
				
				if (result.contains("用户未登陆")){
					Toast.makeText(TopicDetailComplexViewActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				if (result.contains("success")) {
					Toast.makeText(TopicDetailComplexViewActivity.this,
							getString(R.string.result_success),
							Toast.LENGTH_SHORT).show();
					finish();
					Editor editor = topic.edit();
					editor.clear();
					editor.commit();

				} else {
					Toast.makeText(TopicDetailComplexViewActivity.this,
							getString(R.string.error_upload_failed),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

}
