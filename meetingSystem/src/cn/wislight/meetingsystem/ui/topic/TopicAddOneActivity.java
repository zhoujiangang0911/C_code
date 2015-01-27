package cn.wislight.meetingsystem.ui.topic;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.constant.Constant;
import cn.wislight.meetingsystem.service.DbAdapter;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.IntentUtil;
import cn.wislight.meetingsystem.util.Variables;

/**
 * @author Administrator
 * 主页  新增议题第一个
 */
public class TopicAddOneActivity extends BaseActivity {
	private SharedPreferences topic;
	private EditText etSummary;
	private EditText etKeywords;
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		String draft_id = getIntent().getStringExtra(Constants.ID);
		
		topic = this.getSharedPreferences(Constants.TOPIC_NODE + Variables.loginname, MODE_PRIVATE);
		
		if (null != draft_id && !"".equals(draft_id)){
			loadDraft(draft_id);
		}

		etSummary = (EditText)findViewById(R.id.et_summary);
		etKeywords = (EditText)findViewById(R.id.et_keywords);
		
		String topic_keys = topic.getString(Constants.TOPIC_KEYWORDS, "");
		String topic_summary = topic.getString(Constants.TOPIC_SUMMARY, "");	
		
		etSummary.setText(topic_summary);
		etKeywords.setText(topic_keys);
		
	}

	private void loadDraft(String draft_id) {
		// TODO Auto-generated method stub
		DbAdapter dbHandle = new DbAdapter(this);
		dbHandle.open();
		Cursor cursor = dbHandle.getTopicDraftById(draft_id);
		
		if (cursor.moveToFirst())
		{	
			String topic_keys = cursor.getString(2);
			String topic_summary = cursor.getString(3);
			String topic_end_date = cursor.getString(4);
			String topic_start_date = cursor.getString(5);
			String topic_checker_id = cursor.getString(6);
			String topic_checker_name = cursor.getString(7);
			String topic_org_no = cursor.getString(8);
			String topic_target_org_name = cursor.getString(9);
			String attenderList = cursor.getString(10);
			String topic_suggested_attender_names = cursor.getString(11);
			String topic_group = cursor.getString(12);
			String topic_group_id = cursor.getString(13);
			String topic_no = cursor.getString(17);
			String topic_attachment = cursor.getString(18);
	        Editor editor = topic.edit();

	        editor.putString(Constants.TOPIC_KEYWORDS, topic_keys);
	        editor.putString(Constants.TOPIC_SUMMARY, topic_summary);
	        editor.putString(Constants.TOPIC_CHECKER_NAME, topic_checker_name);
	        editor.putString(Constants.TOPIC_CHECKER_ID, topic_checker_id);
	        editor.putString(Constants.TOPIC_START_DATE, topic_start_date);
	        editor.putString(Constants.TOPIC_END_DATE, topic_end_date);
	        editor.putString(Constants.TOPIC_SUGGESTED_ATTENDER_NAMES, topic_suggested_attender_names);
	        editor.putString(Constants.TOPIC_SUGGESTED_ATTENDER_LIST, attenderList);
	        editor.putString(Constants.TOPIC_SUGGESTED_GROUP_NAMES, topic_group);
	        editor.putString(Constants.TOPIC_SUGGESTED_GROUP_IDS, topic_group_id);
	        editor.putString(Constants.TOPIC_TARGET_ORG_NAME, topic_target_org_name);
	        editor.putString(Constants.TOPIC_TARGET_ORG_NO, topic_org_no);
	        editor.putString(Constants.TOPIC_DRAFT_ID, draft_id);
	        editor.putString(Constants.TOPIC_RNO, topic_no);
	        editor.putString(Constants.TOPIC_ATTACHMENT, topic_attachment);
	        editor.commit();
		}
		dbHandle.close();
	}

	@Override
	public void setupView() {
		setContentView(R.layout.topic_addone_main);
		
	}
	
	protected void onPause(){
		super.onPause();
        Editor editor = topic.edit();

        String topic_keys = etKeywords.getText().toString();
        String topic_summary = etSummary.getText().toString();
        editor.putString(Constants.TOPIC_KEYWORDS, topic_keys);
        editor.putString(Constants.TOPIC_SUMMARY, topic_summary);
        editor.commit();
	}
	
	public void  clickNext(View view){
		if (etKeywords.getText().toString().length() == 0){
			Toast.makeText(TopicAddOneActivity.this, getString(R.string.error_no_topic_keys), Toast.LENGTH_SHORT).show();
			return;
		}
		if (etSummary.getText().toString().length() == 0){
			Toast.makeText(TopicAddOneActivity.this, getString(R.string.error_no_topic_summary), Toast.LENGTH_SHORT).show();
			return;
		}
		IntentUtil.startActivity(this,TopicAddTwoActivity.class);
		this.finish();
	}
	
	public void clickDraftBox(View view){
		IntentUtil.startActivity(this,TopicDraftBoxActivity.class);
		this.finish();
	}

}
