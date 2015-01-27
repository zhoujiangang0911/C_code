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
 * @author Administrator
 * 主页  新增议题第一个
 */
public class TopicEditOneActivity extends BaseActivity {
	private SharedPreferences etopic;
	private EditText etSummary;
	private EditText etKeywords;
	private boolean bEditMode = false;
	private LoadingDialog loadingdiag;
	private CheckBox cbIsVote;
	private CheckBox cbIsMeetcall;
	private CheckBox cbIsAbstain;
	private RadioGroup rgVoteType; 
	private RadioButton rbVoteType1; 
	private RadioButton rbVoteType2; 
	private LinearLayout  llVote;
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		etopic = this.getSharedPreferences(Constants.TOPIC_E_NODE+ Variables.loginname, MODE_PRIVATE);
		etSummary = (EditText)findViewById(R.id.et_summary);
		etKeywords = (EditText)findViewById(R.id.et_keywords);
		cbIsVote = (CheckBox)findViewById(R.id.cb_is_vote);
		cbIsMeetcall = (CheckBox)findViewById(R.id.cb_is_meetcall);
		cbIsAbstain = (CheckBox)findViewById(R.id.cb_is_abstain);
		llVote = (LinearLayout)findViewById(R.id.ll_vote);
		rgVoteType = (RadioGroup)findViewById(R.id.rg_vote_type);
		rbVoteType1 = (RadioButton)findViewById(R.id.rg_rb1);
		rbVoteType2 = (RadioButton)findViewById(R.id.rg_rb2);
		
		cbIsVote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ 
            @Override
            public void onCheckedChanged(CompoundButton buttonView, 
                    boolean isChecked) { 
                // TODO Auto-generated method stub 
                if(isChecked){ 
                	llVote.setVisibility(View.VISIBLE);
                } else { 
                	llVote.setVisibility(View.GONE); 
                } 
            } 
        });
		
		String topic_keys = etopic.getString(Constants.TOPIC_E_KEYWORDS, "");
		String topic_summary = etopic.getString(Constants.TOPIC_E_SUMMARY, "");	
		String is_needvote = etopic.getString(Constants.TOPIC_E_IS_NEEDVOTE, "");
		String is_needmeetcall = etopic.getString(Constants.TOPIC_E_IS_NEEDMEETCALL, "");
 		String is_abstain = etopic.getString(Constants.TOPIC_E_IS_ABSTAIN, "");
		String vote_type = etopic.getString(Constants.TOPIC_E_VOTETYPE, "");
		
		etSummary.setText(topic_summary);
		etKeywords.setText(topic_keys);
		if ("1".equals(is_needvote)){
			llVote.setVisibility(View.VISIBLE);
			cbIsVote.setChecked(true);
		} else {
			llVote.setVisibility(View.GONE);
			cbIsVote.setChecked(false);
		}
		if ("1".equals(is_needmeetcall)){
			cbIsMeetcall.setChecked(true);
		} else {
			cbIsMeetcall.setChecked(false);
		}
		if ("1".equals(is_abstain)){
			cbIsAbstain.setChecked(true);
		} else {
			cbIsAbstain.setChecked(false);
		}
		if ("1".equals(vote_type)){
			rbVoteType1.setChecked(true);
		} else {
			rbVoteType2.setChecked(true);
		}
		
		
		Intent intent = getIntent();
		String tpid = intent.getStringExtra(Constants.ID);
		boolean need_reload = intent.getBooleanExtra("need_reload", true);
		if (need_reload){
			Editor editor = etopic.edit();
			if (null == tpid || "".equals(tpid)){
				 bEditMode = false;
				 editor.putString(Constants.TOPIC_E_MODE, Constants.TOPIC_E_MODE_CREATE);
			} else {
				 editor.putString(Constants.TOPIC_E_MODE, Constants.TOPIC_E_MODE_EDIT);
				 bEditMode = true;
			}
			
			editor.commit();	
			if (bEditMode){
				etSummary.setEnabled(false);
				etKeywords.setEnabled(false);
				getTopicDetailedData(tpid);
			}
		} else {
			String mode = etopic.getString(Constants.TOPIC_E_MODE, "");
			if (Constants.TOPIC_E_MODE_EDIT.equals(mode)){
				bEditMode = true;
				etSummary.setEnabled(false);
				etKeywords.setEnabled(false);
			}
		}
		

	}

	private void getTopicDetailedData(String id) {
		loadingdiag = new LoadingDialog(this);  
		loadingdiag.setCanceledOnTouchOutside(false); 
		loadingdiag.setText(getString(R.string.loading));
		
		String url = "MeetingManage/mobile/findTopicDetailExtById.action?tpid="
				+ id;
		loadingdiag.show();  
		System.out.println("wangting:"+url);
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler() {


			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();  
				Toast.makeText(TopicEditOneActivity.this,
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
					Toast.makeText(TopicEditOneActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				// org_listview.setVisibility(View.VISIBLE);
				// loading.setVisibility(View.GONE);
				System.out.println("wangting:"+response);
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(response)
							.getJSONObject("tpDetail");
					
					String topic_keys = jsonObject.getString("keywords");
					String topic_summary = jsonObject.getString("summary");
					String topic_start_date = jsonObject.getString("starttime");
					String topic_end_date = jsonObject.getString("endtime");
					String topic_target_org_name = jsonObject.getString("target_org");
					String topic_target_org_no = jsonObject.getString("target_org_no");
					String topic_suggested_attender_names = jsonObject
							.getString("suggested_attender");
					String topic_checker = jsonObject.getString("checker");
					String topic_creator = jsonObject.getString("creator");
					String topic_check_reason = jsonObject.getString("check_reason");
					String check_state = jsonObject.getString("check_state");
					
					String topic_no = jsonObject.getString("topic_no");
					String meet_no = jsonObject.getString("meet_no");
					String votestart_date = jsonObject.getString("votestart_date");
					String voteend_date = jsonObject.getString("voteend_date");
					// String state = jsonObject.getString("state");
					String is_needvote = jsonObject.getString("is_needvote");
					String is_needmeetcall = jsonObject.getString("is_needmeetcall");
					String vote_type = jsonObject.getString("vote_type");
					String is_abstain = jsonObject.getString("is_abstain");                    
					// String is_pass = jsonObject.getString("is_pass");                    
					// String agree_count = jsonObject.getString("agree_count");                    
					// String disagree_count = jsonObject.getString("disagree_count");                    
					// String abstain_count = jsonObject.getString("abstain_count");                    
					// String join_rate = jsonObject.getString("join_rate");                    
					// String mute_rate = jsonObject.getString("mute_rate");                    
					// String pass_rate = jsonObject.getString("pass_rate");                    
					// String vote_state = jsonObject.getString("vote_state");
					// String sum_state = jsonObject.getString("sum_state");
					// String document_state = jsonObject.getString("document_state");                    
					String manager_id = jsonObject.getString("manager_id");                    
					String votemng_id = jsonObject.getString("votemng_id");                    
					String summng_id = jsonObject.getString("summng_id");                    
					// String documentmng_id = jsonObject.getString("documentmng_id");		
					
					String manager_name = jsonObject.getString("manager_name");
					String votemng_name = jsonObject.getString("votemng_name");
					String summng_name = jsonObject.getString("summng_name");
					String source = jsonObject.getString("source");
					
					//String topic_attender_list = jsonObject.getString("summng_name");
					String id = jsonObject.getString("id");
					
			        Editor editor = etopic.edit();
			        editor.putString(Constants.TOPIC_E_KEYWORDS, topic_keys);
			        editor.putString(Constants.TOPIC_E_SUMMARY, topic_summary);
			        editor.putString(Constants.TOPIC_E_START_DATE, topic_start_date);
			        editor.putString(Constants.TOPIC_E_END_DATE, topic_end_date);
			        editor.putString(Constants.TOPIC_E_TARGET_ORG_NAME, topic_target_org_name);
			        editor.putString(Constants.TOPIC_E_TARGET_ORG_NO, topic_target_org_no);
			        //editor.putString(Constants.TOPIC_E_SUGGESTED_ATTENDER_LIST, topic_attender_list);
			        
			        
			        editor.putString(Constants.TOPIC_E_SUGGESTED_ATTENDER_NAMES, topic_suggested_attender_names);
			        editor.putString(Constants.TOPIC_E_CHECKER_NAME, topic_checker);
			        editor.putString(Constants.TOPIC_E_CREATOR_NAME, topic_creator);
			        editor.putString(Constants.TOPIC_E_CHECK_RESON, topic_check_reason);
			        editor.putString(Constants.TOPIC_E_CHECK_STATE, check_state);
			        editor.putString(Constants.TOPIC_E_TOPIC_NO, topic_no);
			        editor.putString(Constants.TOPIC_E_MEET_NO, meet_no);
			        editor.putString(Constants.TOPIC_E_VOTE_STARTTIME, votestart_date);
			        editor.putString(Constants.TOPIC_E_VOTE_ENDTIME, voteend_date);
			        editor.putString(Constants.TOPIC_E_IS_NEEDVOTE, is_needvote);
			        editor.putString(Constants.TOPIC_E_IS_NEEDMEETCALL, is_needmeetcall);
			        editor.putString(Constants.TOPIC_E_VOTETYPE, vote_type);
			        editor.putString(Constants.TOPIC_E_IS_ABSTAIN, is_abstain);
			        editor.putString(Constants.TOPIC_E_MANAGER_ID, manager_id);
			        editor.putString(Constants.TOPIC_E_VOTEMNG_ID, votemng_id);
			        editor.putString(Constants.TOPIC_E_SUMMNG_ID, summng_id);
			        editor.putString(Constants.TOPIC_E_MANAGER_NAME, manager_name);
			        editor.putString(Constants.TOPIC_E_VOTEMNG_NAME, votemng_name);
			        editor.putString(Constants.TOPIC_E_SUMMNG_NAME, summng_name);
			        editor.putString(Constants.TOPIC_E_ID, id);
			        editor.putString(Constants.TOPIC_E_RNO, topic_no);
			        editor.putString(Constants.TOPIC_E_SOURCE, source);

			        editor.commit();
					
			        etKeywords.setText(topic_keys);
			        etSummary.setText(topic_summary);
					if ("1".equals(is_needvote)){
						cbIsVote.setChecked(true);
					} else {
						cbIsVote.setChecked(false);
					}
					if ("1".equals(is_needmeetcall)){
						cbIsMeetcall.setChecked(true);
					} else {
						cbIsMeetcall.setChecked(false);
					}
					if ("1".equals(is_abstain)){
						cbIsAbstain.setChecked(true);
					} else {
						cbIsAbstain.setChecked(false);
					}
					if ("1".equals(vote_type)){
						rbVoteType1.setChecked(true);
					} else {
						rbVoteType2.setChecked(true);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(TopicEditOneActivity.this,
							getString(R.string.error_dataabout),
							Toast.LENGTH_SHORT).show();
					finish();

				}
			}
		});
	
	}

	@Override
	public void setupView() {
		setContentView(R.layout.topic_editone_main);
		
	}
	
	protected void onPause(){
		super.onPause();
        Editor editor = etopic.edit();

        String topic_keys = etKeywords.getText().toString();
        String topic_summary = etSummary.getText().toString();
        editor.putString(Constants.TOPIC_E_KEYWORDS, topic_keys);
        editor.putString(Constants.TOPIC_E_SUMMARY, topic_summary);
        editor.putString(Constants.TOPIC_E_IS_NEEDVOTE, cbIsVote.isChecked() ? "1" : "0");
        editor.putString(Constants.TOPIC_E_IS_NEEDMEETCALL, cbIsMeetcall.isChecked() ? "1" : "0");
        editor.putString(Constants.TOPIC_E_IS_ABSTAIN, cbIsAbstain.isChecked() ? "1" : "0");
        if (R.id.rg_rb1 == rgVoteType.getCheckedRadioButtonId()){
        	editor.putString(Constants.TOPIC_E_VOTETYPE, "1");
        } else {
        	editor.putString(Constants.TOPIC_E_VOTETYPE, "0");
        }
        
        editor.commit();
	}
	
	public void  clickNext(View view){
		if (etKeywords.getText().toString().length() == 0){
			Toast.makeText(TopicEditOneActivity.this, getString(R.string.error_no_topic_keys), Toast.LENGTH_SHORT).show();
			return;
		}
		if (etSummary.getText().toString().length() == 0){
			Toast.makeText(TopicEditOneActivity.this, getString(R.string.error_no_topic_summary), Toast.LENGTH_SHORT).show();
			return;
		}
		IntentUtil.startActivity(this,TopicEditTwoActivity.class);
		this.finish();
	}

}
