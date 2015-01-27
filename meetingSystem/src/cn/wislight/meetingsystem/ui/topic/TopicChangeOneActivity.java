package cn.wislight.meetingsystem.ui.topic;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.IntentUtil;
import cn.wislight.meetingsystem.util.LoadingDialog;
import cn.wislight.meetingsystem.util.MeetingSystemClient;
import cn.wislight.meetingsystem.util.Variables;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * @author Administrator 主页 新增议题第一个
 */
public class TopicChangeOneActivity extends BaseActivity {
	private SharedPreferences etopic;
	private EditText etSummary;
	private EditText etKeywords;
	private LoadingDialog loadingdiag;
	private CheckBox cbIsVote;
	private CheckBox cbIsMeetcall;
	private CheckBox cbIsAbstain;
	private RadioGroup rgVoteType;
	private RadioButton rbVoteType1;
	private RadioButton rbVoteType2;
	private LinearLayout llVote;

	private String topicNo;
	private String topicId;
	private Boolean loadData = true;

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		etopic = this.getSharedPreferences(Constants.TOPIC_C_NODE
				+ Variables.loginname, MODE_PRIVATE);
		etSummary = (EditText) findViewById(R.id.et_summary);
		etKeywords = (EditText) findViewById(R.id.et_keywords);
		cbIsVote = (CheckBox) findViewById(R.id.cb_is_vote);
		cbIsMeetcall = (CheckBox) findViewById(R.id.cb_is_meetcall);
		cbIsAbstain = (CheckBox) findViewById(R.id.cb_is_abstain);
		llVote = (LinearLayout) findViewById(R.id.ll_vote);
		rgVoteType = (RadioGroup) findViewById(R.id.rg_vote_type);
		rbVoteType1 = (RadioButton) findViewById(R.id.rg_rb1);
		rbVoteType2 = (RadioButton) findViewById(R.id.rg_rb2);

		cbIsVote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					llVote.setVisibility(View.VISIBLE);
				} else {
					llVote.setVisibility(View.GONE);
				}
			}
		});

		topicNo = this.getIntent().getStringExtra(Constants.TOPIC_NO);
		topicId = this.getIntent().getStringExtra(Constants.TOPIC_ID);
		loadData = this.getIntent().getBooleanExtra("need_reload", true);
		if (loadData) {
			getTopicDetailedData();
		}

		updateUi();
	}

	private void updateUi() {
		// TODO Auto-generated method stub

		String topic_keys = etopic.getString(Constants.TOPIC_E_KEYWORDS, "");
		String topic_summary = etopic.getString(Constants.TOPIC_E_SUMMARY, "");
		String is_needvote = etopic
				.getString(Constants.TOPIC_E_IS_NEEDVOTE, "");
		String is_needmeetcall = etopic.getString(
				Constants.TOPIC_E_IS_NEEDMEETCALL, "");
		String is_abstain = etopic.getString(Constants.TOPIC_E_IS_ABSTAIN, "");
		String vote_type = etopic.getString(Constants.TOPIC_E_VOTETYPE, "");

		etKeywords.setText(topic_keys);
		etSummary.setText(topic_summary);
		if ("1".equals(is_needvote)) {
			cbIsVote.setChecked(true);
		} else {
			cbIsVote.setChecked(false);
		}
		if ("1".equals(is_needmeetcall)) {
			cbIsMeetcall.setChecked(true);
		} else {
			cbIsMeetcall.setChecked(false);
		}
		if ("1".equals(is_abstain)) {
			cbIsAbstain.setChecked(true);
		} else {
			cbIsAbstain.setChecked(false);
		}
		if ("1".equals(vote_type)) {
			rbVoteType1.setChecked(true);
		} else {
			rbVoteType2.setChecked(true);
		}
	}

	private void getTopicDetailedData() {
		loadingdiag = new LoadingDialog(this);
		loadingdiag.setCanceledOnTouchOutside(false);
		loadingdiag.setText(getString(R.string.loading));

		String url = "MeetingManage/mobile/getTopicInfoByID.action?TopicNo="
				+ topicNo + "&TopicId=" + topicId;
		loadingdiag.show();
		System.out.println("wangting:" + url);
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();
				Toast.makeText(TopicChangeOneActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
				finish();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				loadingdiag.hide();
				String response = new String(body);
				if (response.contains("用户未登陆")){
					Toast.makeText(TopicChangeOneActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				// Toast.makeText(TopicSelectApplicatorActivity.this, new
				// String(response)+"wangting"+response.toString(), 100).show();

				// org_listview.setVisibility(View.VISIBLE);
				// loading.setVisibility(View.GONE);
				System.out.println("wangting:" + response);
				JSONObject jsonObject;
				try {
					String topic_suggested_attender_names = new JSONObject(
							response).getString("attenders");
					jsonObject = new JSONObject(response)
							.getJSONObject("meetTopicEntity");

					String topic_keys = jsonObject.getString("remark"); //
					String topic_summary = jsonObject.getString("title"); //
					String topic_start_date = jsonObject.getString("staTime"); //
					String topic_end_date = jsonObject.getString("endTime"); //
					String topic_checker = jsonObject.getString("checkname"); //
					String topic_check_reason = jsonObject
							.getString("check_reason"); //
					String check_state = jsonObject.getString("check_state"); //
					String check_ppl_id = jsonObject.getString("check_ppl_id"); //

					String target_org_no = jsonObject.getString("target_org");
					String target_org_name = jsonObject.getString("orgName");

					String topic_no = jsonObject.getString("topicno"); //
					String meet_no = jsonObject.getString("meetno"); //
					String votestart_date = jsonObject.getString("votstaTime"); //
					String voteend_date = jsonObject.getString("votendTime"); //
					// String state = jsonObject.getString("state");
					String is_needvote = jsonObject.getString("isneedvote"); //
					String is_needmeetcall = jsonObject
							.getString("is_needmeetcall"); //
					String vote_type = jsonObject.getString("votetype"); //
					String is_abstain = jsonObject.getString("isabstain"); //
					// String is_pass = jsonObject.getString("is_pass");
					// String agree_count = jsonObject.getString("agree_count");
					// String disagree_count =
					// jsonObject.getString("disagree_count");
					// String abstain_count =
					// jsonObject.getString("abstain_count");
					// String join_rate = jsonObject.getString("join_rate");
					// String mute_rate = jsonObject.getString("mute_rate");
					// String pass_rate = jsonObject.getString("pass_rate");
					// String vote_state = jsonObject.getString("vote_state");
					// String sum_state = jsonObject.getString("sum_state");
					// String document_state =
					// jsonObject.getString("document_state");
					String manager_id = jsonObject.getString("mngerid");
					String votemng_id = jsonObject.getString("votemngerid");
					String summng_id = jsonObject.getString("summngerid");
					// String documentmng_id =
					// jsonObject.getString("documentmng_id");

					String manager_name = jsonObject.getString("mngerfullname");
					String votemng_name = jsonObject
							.getString("votemngerfullname");
					String summng_name = jsonObject
							.getString("summngerfullname");
					String source = jsonObject.getString("source");

					// String topic_attender_list =
					// jsonObject.getString("summng_name");
					String id = jsonObject.getString("id");
					String meetNo = jsonObject.getString("meetno");

					Editor editor = etopic.edit();
					editor.putString(Constants.TOPIC_E_KEYWORDS, topic_keys);
					editor.putString(Constants.TOPIC_E_SUMMARY, topic_summary);
					editor.putString(Constants.TOPIC_E_START_DATE,
							topic_start_date);
					editor.putString(Constants.TOPIC_E_END_DATE, topic_end_date);
					// editor.putString(Constants.TOPIC_E_SUGGESTED_ATTENDER_LIST,
					// topic_attender_list);
					editor.putString(Constants.TOPIC_E_TARGET_ORG_NO,
							target_org_no);
					editor.putString(Constants.TOPIC_E_TARGET_ORG_NAME,
							target_org_name);

					editor.putString(
							Constants.TOPIC_E_SUGGESTED_ATTENDER_NAMES,
							topic_suggested_attender_names);
					editor.putString(Constants.TOPIC_E_CHECKER_NAME,
							topic_checker);
					editor.putString(Constants.TOPIC_E_CHECK_RESON,
							topic_check_reason);
					editor.putString(Constants.TOPIC_E_CHECK_STATE, check_state);
					editor.putString(Constants.TOPIC_E_TOPIC_NO, topic_no);
					editor.putString(Constants.TOPIC_E_MEET_NO, meet_no);
					editor.putString(Constants.TOPIC_E_VOTE_STARTTIME,
							votestart_date);
					editor.putString(Constants.TOPIC_E_VOTE_ENDTIME,
							voteend_date);
					editor.putString(Constants.TOPIC_E_IS_NEEDVOTE, is_needvote);
					editor.putString(Constants.TOPIC_E_IS_NEEDMEETCALL,
							is_needmeetcall);
					editor.putString(Constants.TOPIC_E_VOTETYPE, vote_type);
					editor.putString(Constants.TOPIC_E_IS_ABSTAIN, is_abstain);
					editor.putString(Constants.TOPIC_E_MANAGER_ID, manager_id);
					editor.putString(Constants.TOPIC_E_VOTEMNG_ID, votemng_id);
					editor.putString(Constants.TOPIC_E_SUMMNG_ID, summng_id);
					editor.putString(Constants.TOPIC_E_MANAGER_NAME,
							manager_name);
					editor.putString(Constants.TOPIC_E_VOTEMNG_NAME,
							votemng_name);
					editor.putString(Constants.TOPIC_E_SUMMNG_NAME, summng_name);
					editor.putString(Constants.TOPIC_E_ID, id);
					editor.putString(Constants.TOPIC_E_RNO, topic_no);
					editor.putString(Constants.TOPIC_E_SOURCE, source);
					editor.putString(Constants.TOPIC_E_MEET_NO, meetNo);
					editor.commit();
					updateUi();

				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(TopicChangeOneActivity.this,
							getString(R.string.error_dataabout),
							Toast.LENGTH_SHORT).show();
					finish();

				}
			}
		});

	}

	@Override
	public void setupView() {
		setContentView(R.layout.topic_changeone_main);

	}

	protected void onPause() {
		super.onPause();
		Editor editor = etopic.edit();

		String topic_keys = etKeywords.getText().toString();
		String topic_summary = etSummary.getText().toString();
		editor.putString(Constants.TOPIC_E_KEYWORDS, topic_keys);
		editor.putString(Constants.TOPIC_E_SUMMARY, topic_summary);
		editor.putString(Constants.TOPIC_E_IS_NEEDVOTE,
				cbIsVote.isChecked() ? "1" : "0");
		editor.putString(Constants.TOPIC_E_IS_NEEDMEETCALL,
				cbIsMeetcall.isChecked() ? "1" : "0");
		editor.putString(Constants.TOPIC_E_IS_ABSTAIN,
				cbIsAbstain.isChecked() ? "1" : "0");
		if (R.id.rg_rb1 == rgVoteType.getCheckedRadioButtonId()) {
			editor.putString(Constants.TOPIC_E_VOTETYPE, "1");
		} else {
			editor.putString(Constants.TOPIC_E_VOTETYPE, "0");
		}

		editor.commit();
	}

	public void clickNext(View view) {
		if (etKeywords.getText().toString().length() == 0) {
			Toast.makeText(TopicChangeOneActivity.this,
					getString(R.string.error_no_topic_keys), Toast.LENGTH_SHORT)
					.show();
			return;
		}
		if (etSummary.getText().toString().length() == 0) {
			Toast.makeText(TopicChangeOneActivity.this,
					getString(R.string.error_no_topic_summary),
					Toast.LENGTH_SHORT).show();
			return;
		}
		IntentUtil.startActivity(this, TopicChangeTwoActivity.class);
		this.finish();
	}

}
