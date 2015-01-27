package cn.wislight.meetingsystem.ui.topic;

import java.util.ArrayList;

import com.google.gson.Gson;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.domain.GroupElement;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.DpTpActivity;
import cn.wislight.meetingsystem.util.Element;
import cn.wislight.meetingsystem.util.IntentUtil;
import cn.wislight.meetingsystem.util.Variables;
/**
 * @author Administrator
 * 主页  新增议题第二个
 */
public class TopicMeetAddTwoActivity extends BaseActivity implements OnClickListener{
	private SharedPreferences topic;
	private TextView textSuggetedAttender;
	private TextView textStarttime;
	private TextView textEndtime;
	private TextView textVoteStarttime;
	private TextView textVoteEndtime;
	private TextView textTopicController;
	private TextView textTopicVoteController;
	private TextView textTopicVoteStaticcer;
	private TextView textGroup;
	private TextView textOrg;
	private LinearLayout llVoteTime;
	private String orgNO;

	
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		textSuggetedAttender=(TextView)findViewById(R.id.suggsted_attender);
		textStarttime = (TextView)findViewById(R.id.starttime);
		textEndtime = (TextView)findViewById(R.id.endtime);
		textVoteStarttime = (TextView)findViewById(R.id.vote_starttime);
		textVoteEndtime = (TextView)findViewById(R.id.vote_endtime);
		textTopicController = (TextView)findViewById(R.id.tv_topic_controller);
		textTopicVoteController = (TextView)findViewById(R.id.tv_topic_vote_controller);
		textTopicVoteStaticcer = (TextView)findViewById(R.id.tv_topic_vote_staticer);
		textGroup = (TextView)findViewById(R.id.suggsted_group);
		textOrg = (TextView)findViewById(R.id.tv_org);
		llVoteTime = (LinearLayout)findViewById(R.id.ll_vote_time);
		
		
		
		textStarttime.setOnClickListener(this);
		textEndtime.setOnClickListener(this);
		textSuggetedAttender.setOnClickListener(this);
		textVoteStarttime.setOnClickListener(this);
		textVoteEndtime.setOnClickListener(this);
		textTopicController.setOnClickListener(this);
		textTopicVoteController.setOnClickListener(this);
		textTopicVoteStaticcer.setOnClickListener(this);
		textGroup.setOnClickListener(this);
		textOrg.setOnClickListener(this);
		
		topic = this.getSharedPreferences(Constants.TOPIC_ME_NODE+ Variables.loginname, MODE_PRIVATE);
		String topic_start_date = topic.getString(Constants.TOPIC_E_START_DATE, "");
		String topic_end_date = topic.getString(Constants.TOPIC_E_END_DATE, "");
		String topic_vote_starttime = topic.getString(Constants.TOPIC_E_VOTE_STARTTIME, "");
		String topic_vote_endtime = topic.getString(Constants.TOPIC_E_VOTE_ENDTIME, "");		
		String topic_controller = topic.getString(Constants.TOPIC_E_MANAGER_NAME, "");
		String topic_vote_controller = topic.getString(Constants.TOPIC_E_VOTEMNG_NAME, "");
		String topic_vote_staticer = topic.getString(Constants.TOPIC_E_SUMMNG_NAME, "");
		String topic_needvote = topic.getString(Constants.TOPIC_E_IS_NEEDVOTE, "");
		String topic_suggested_attender_names = topic.getString(Constants.TOPIC_E_SUGGESTED_ATTENDER_NAMES, "");
		String topic_target_orgname = topic.getString(Constants.TOPIC_E_TARGET_ORG_NAME, "");
		String topic_group_name = topic.getString(Constants.TOPIC_E_SUGGESTED_GROUP_NAMES, "");
		
		if("1".equals(topic_needvote)){
			llVoteTime.setVisibility(View.VISIBLE);
			textVoteStarttime.setText(topic_vote_starttime);
			textVoteEndtime.setText(topic_vote_endtime);
		} else {
			llVoteTime.setVisibility(View.GONE);
		}
		
		if (topic_controller == null || "".equals(topic_controller)){
			textTopicController.setText(R.string.topic_controller);
		}else{
			textTopicController.setText(getString(R.string.topic_controller) + ":    " 
					+ topic_controller);
		}
		
		if (topic_vote_controller == null || "".equals(topic_vote_controller)){
			textTopicVoteController.setText(R.string.topic_vote_controller);
		}else{
			textTopicVoteController.setText(getString(R.string.topic_vote_controller) + ":    " 
					+ topic_vote_controller);
		}
		
		if (topic_vote_staticer == null || "".equals(topic_vote_staticer)){
			textTopicVoteStaticcer.setText(R.string.topic_vote_staticcer);
		}else{
			textTopicVoteStaticcer.setText(getString(R.string.topic_vote_staticcer) + ":    " 
					+ topic_vote_staticer);
		}
		
		if (topic_start_date == null || "".equals(topic_start_date)){
			textStarttime.setText(R.string.topic_starttime);
		}else{
			textStarttime.setText(topic_start_date);
		}
		
		if (topic_end_date == null || "".equals(topic_end_date)){
			textEndtime.setText(R.string.topic_endtime);
		}else{
			textEndtime.setText(topic_end_date);
		}
		
		if (topic_suggested_attender_names == null || "".equals(topic_suggested_attender_names)){
			textSuggetedAttender.setText(R.string.topic_joinmember);
		}else{
			textSuggetedAttender.setText(getString(R.string.topic_joinmember) + ":    " 
					+ topic_suggested_attender_names);
		}

		if (topic_target_orgname == null || "".equals(topic_target_orgname)){
			textOrg.setText(R.string.topic_targetdepartment);
		}else{
			textOrg.setText(getString(R.string.topic_targetdepartment) + ":    " 
					+ topic_target_orgname);
		}
		
		if (topic_group_name == null || "".equals(topic_group_name)){
			textGroup.setText(R.string.topic_joingroup);
		}else{
			textGroup.setText(getString(R.string.topic_joingroup) + ":    " 
					+ topic_group_name);
		}
		
		
	}

	@Override
	public void setupView() {
		setContentView(R.layout.topic_me_addtwo);
	}
	
	protected void onPause(){
		super.onPause();
        Editor editor = topic.edit();

        editor.commit();
	}
	
	
	
	public void  clickPre(View view){
		Intent intent = new Intent();
		intent.putExtra("need_reload", false);
		IntentUtil.startActivity(this,TopicMeetAddOneActivity.class, intent);
		this.finish();
	}

	public void  clickNext(View view){
		String topic_start_date = topic.getString(Constants.TOPIC_E_START_DATE, "");
		String topic_end_date = topic.getString(Constants.TOPIC_E_END_DATE, "");
		String topic_suggested_attender_names = topic.getString(Constants.TOPIC_E_SUGGESTED_ATTENDER_NAMES, "");
		String topic_vote_starttime = topic.getString(Constants.TOPIC_E_VOTE_STARTTIME, "");
		String topic_vote_endtime = topic.getString(Constants.TOPIC_E_VOTE_ENDTIME, "");		
		String topic_controller = topic.getString(Constants.TOPIC_E_MANAGER_NAME, "");
		String topic_vote_controller = topic.getString(Constants.TOPIC_E_VOTEMNG_NAME, "");
		String topic_vote_staticer = topic.getString(Constants.TOPIC_E_SUMMNG_NAME, "");
		String topic_needvote = topic.getString(Constants.TOPIC_E_IS_NEEDVOTE, "");
		
		if ("1".equals(topic_needvote)){
			
			if (topic_vote_starttime.length() == 0){
				Toast.makeText(TopicMeetAddTwoActivity.this, getString(R.string.error_no_topic_vote_starttime), Toast.LENGTH_SHORT).show();
				return;
			}
			
			if (topic_vote_endtime.length() == 0){
				Toast.makeText(TopicMeetAddTwoActivity.this, getString(R.string.error_no_topic_vote_endtime), Toast.LENGTH_SHORT).show();
				return;
			}
			
			if (topic_vote_endtime.compareTo(topic_vote_starttime) < 0){
				Toast.makeText(TopicMeetAddTwoActivity.this, getString(R.string.error_topic_vote_timeerror), Toast.LENGTH_SHORT).show();
				return;
			}
			
			if (!(topic_vote_endtime.compareTo(topic_start_date) >= 0 && topic_vote_endtime.compareTo(topic_end_date) <= 0 )){
				Toast.makeText(TopicMeetAddTwoActivity.this, getString(R.string.error_topic_vote_timeerror2), Toast.LENGTH_SHORT).show();
				return;
			}
			
			if (!(topic_vote_starttime.compareTo(topic_start_date) >= 0 && topic_vote_starttime.compareTo(topic_end_date) <= 0 )){
				Toast.makeText(TopicMeetAddTwoActivity.this, getString(R.string.error_topic_vote_timeerror1), Toast.LENGTH_SHORT).show();
				return;
			}
		}
		
		if (topic_start_date.length() == 0){
			Toast.makeText(TopicMeetAddTwoActivity.this, getString(R.string.error_no_topic_starttime), Toast.LENGTH_SHORT).show();
			return;
		}
		if (topic_end_date.length() == 0){
			Toast.makeText(TopicMeetAddTwoActivity.this, getString(R.string.error_no_topic_endtime), Toast.LENGTH_SHORT).show();
			return;
		}
		if (topic_suggested_attender_names.length() == 0){
			// Toast.makeText(TopicMeetAddTwoActivity.this, getString(R.string.error_no_topic_attender), Toast.LENGTH_SHORT).show();
			// return;
		}
		if (topic_controller.length() == 0){
			Toast.makeText(TopicMeetAddTwoActivity.this, getString(R.string.error_no_topic_controller), Toast.LENGTH_SHORT).show();
			return;
		}
		if (topic_vote_controller.length() == 0){
			Toast.makeText(TopicMeetAddTwoActivity.this, getString(R.string.error_no_topic_vote_controller), Toast.LENGTH_SHORT).show();
			return;
		}
		if (topic_vote_staticer.length() == 0){
			Toast.makeText(TopicMeetAddTwoActivity.this, getString(R.string.error_no_topic_vote_staticcer), Toast.LENGTH_SHORT).show();
			return;
		}
		if (topic_end_date.compareTo(topic_start_date) < 0){
			Toast.makeText(TopicMeetAddTwoActivity.this, getString(R.string.error_topic_timeerror), Toast.LENGTH_SHORT).show();
			return;
		}
		SharedPreferences data = this.getSharedPreferences(Constants.CONF_TIME_NODE, MODE_PRIVATE);
		String start = data.getString(Constants.CONF_START_TIME, "");
		String end = data.getString(Constants.CONF_END_TIME, "");
		if (topic_start_date.compareTo(start) < 0 || topic_start_date.compareTo(end) > 0 ){
			Toast.makeText(TopicMeetAddTwoActivity.this, getString(R.string.error_topic_starttime), Toast.LENGTH_SHORT).show();
			return;
		}
		if (topic_end_date.compareTo(start) < 0 || topic_end_date.compareTo(end) > 0 ){
			Toast.makeText(TopicMeetAddTwoActivity.this, getString(R.string.error_topic_endtime), Toast.LENGTH_SHORT).show();
			return;
		} 
		
		IntentUtil.startActivity(this,TopicMeetAddThreeActivity.class);
		this.finish();
	}

	@SuppressWarnings("unchecked")
	protected void onActivityResult(int requestCode, int resultCode, Intent data)  
    {  
        Editor editor = topic.edit();

       
        if(Constants.CODE_TOPIC_CONTROLLER == requestCode)  
        {  
        	if (Constants.OK == resultCode && null != data){
        		String name=data.getExtras().getString(Constants.NAME);
        		String id=data.getExtras().getString(Constants.ID);
        		
        		textTopicController.setText(getString(R.string.topic_controller) + ":    " + name);
        		
        		editor.putString(Constants.TOPIC_E_MANAGER_ID, id);
        		editor.putString(Constants.TOPIC_E_MANAGER_NAME, name);
        	}
            
        }

        if(Constants.CODE_TOPIC_VOTE_CONTROLLER == requestCode)  
        {  
        	if (Constants.OK == resultCode && null != data){
        		String name=data.getExtras().getString(Constants.NAME);
        		String id=data.getExtras().getString(Constants.ID);
        		
        		textTopicVoteController.setText(getString(R.string.topic_vote_controller) + ":    " + name);
        		
        		editor.putString(Constants.TOPIC_E_VOTEMNG_ID, id);
        		editor.putString(Constants.TOPIC_E_VOTEMNG_NAME, name);
        	}
            
        }
        
        if(Constants.CODE_TOPIC_VOTE_STATICCER == requestCode)  
        {  
        	if (Constants.OK == resultCode && null != data){
        		String name=data.getExtras().getString(Constants.NAME);
        		String id=data.getExtras().getString(Constants.ID);
        		
        		textTopicVoteStaticcer.setText(getString(R.string.topic_vote_staticcer) + ":    " + name);
        		
        		editor.putString(Constants.TOPIC_E_SUMMNG_NAME, name);
        		editor.putString(Constants.TOPIC_E_SUMMNG_ID, id);
        	}
            
        }        
        if(Constants.CODE_START_TIME == requestCode)  
        {  
        	if (Constants.OK == resultCode && null != data){
        		String dt = data.getExtras().getString(Constants.DATETIME);  
        		textStarttime.setText(dt);
        		editor.putString(Constants.TOPIC_E_START_DATE, dt);
        	}
        }
        if(Constants.CODE_END_TIME == requestCode )  
        {  
        	if (Constants.OK == resultCode && null != data){
        		String dt = data.getExtras().getString(Constants.DATETIME);  
        		textEndtime.setText(dt);
        		editor.putString(Constants.TOPIC_E_END_DATE, dt);
        	}
        }
        if(Constants.CODE_VOTE_END_TIME == requestCode )  
        {  
        	if (Constants.OK == resultCode && null != data){
        		String dt = data.getExtras().getString(Constants.DATETIME);  
        		textVoteEndtime.setText(dt);
        		editor.putString(Constants.TOPIC_E_VOTE_ENDTIME, dt);
        	}
        }
 
        if(Constants.CODE_VOTE_START_TIME == requestCode)  
        {  
        	if (Constants.OK == resultCode && null != data){
        		String dt = data.getExtras().getString(Constants.DATETIME);  
        		textVoteStarttime.setText(dt);
        		editor.putString(Constants.TOPIC_E_VOTE_STARTTIME, dt);
        	}
        }
        

        if(Constants.CODE_SUGGUSTED_ATTENDER == requestCode )  
        {  
        	if (Constants.OK == resultCode && null != data){
        		ArrayList<Element> selList = (ArrayList<Element>)data.getSerializableExtra(Constants.ATTENDERLIST);
        		String temp = "";
        		String ids = "";
        		for (Element ele : selList){
        			temp += ele.getName() + ",";
        			ids += ele.getId() + ",";
        		}
        		textSuggetedAttender.setText(getString(R.string.topic_joinmember) + ":    "  + temp);
        		editor.putString(Constants.TOPIC_E_SUGGESTED_ATTENDER_NAMES, temp);
        		editor.putString(Constants.TOPIC_E_SUGGESTED_ATTENDER_IDS, ids);
        		
                Gson gson = new Gson();
                String beanListToJson = gson.toJson(selList);   
        		editor.putString(Constants.TOPIC_E_SUGGESTED_ATTENDER_LIST, beanListToJson);
        	}
        }   
        if(Constants.CODE_SUGGUSTED_GROUP == requestCode )  
        {  
        	if (Constants.OK == resultCode && null != data){
        		ArrayList<GroupElement> selList = (ArrayList<GroupElement>)data.getSerializableExtra(Constants.GROUPLIST);
        		String temp = "";
        		String ids = "";
        		for (GroupElement ele : selList){
        			temp += ele.getGroupName() + ",";
        			ids += ele.getId() + ",";
        		}
        		textGroup.setText(getString(R.string.topic_joingroup) + ":    "  + temp);
        		editor.putString(Constants.TOPIC_E_SUGGESTED_GROUP_NAMES, temp);
        		editor.putString(Constants.TOPIC_E_SUGGESTED_GROUP_IDS, ids);

        	}
            
        }
        if(Constants.CODE_SELECT_ORG == requestCode )  
        {  
        	if (Constants.OK == resultCode && null != data){
        		String fname = data.getExtras().getString(Constants.ORG_FNAME); 
        		orgNO = data.getExtras().getString(Constants.ORG_NO);
        		textOrg.setText(getString(R.string.topic_targetdepartment) + ":    "  + fname);
        		
        		editor.putString(Constants.TOPIC_E_TARGET_ORG_NAME, fname);
        		editor.putString(Constants.TOPIC_E_TARGET_ORG_NO, orgNO);
        	}
            
        }  
        editor.commit();
        super.onActivityResult(requestCode, resultCode, data);  
    }	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.applicatemember:
			orgNO = topic.getString(Constants.TOPIC_E_TARGET_ORG_NO, "");
			if (orgNO.equals("")){
				Toast.makeText(TopicMeetAddTwoActivity.this, getString(R.string.error_empty_orgno), 
						Toast.LENGTH_SHORT).show();
				return;
			}
			Intent intent = new Intent();
			intent.putExtra(Constants.ORG_NO, orgNO);
			intent.putExtra("type", "1");

			IntentUtil.startActivityForResult(this, TopicSelectApplicatorActivity.class, 
					Constants.CODE_CHECKER, intent);
			break;
		case R.id.suggsted_attender:
			orgNO = topic.getString(Constants.TOPIC_E_TARGET_ORG_NO, "");
			if (orgNO.equals("")){
				Toast.makeText(TopicMeetAddTwoActivity.this, getString(R.string.error_empty_orgno), 
						Toast.LENGTH_SHORT).show();
				return;
			}
			Intent intent1 = new Intent();
			intent1.putExtra(Constants.ORG_NO, orgNO);
			IntentUtil.startActivityForResult(this, TopicSelectSuggetedAttenderActivity.class, 
					Constants.CODE_SUGGUSTED_ATTENDER, intent1);
			break;
		case R.id.starttime:
			Intent intentTime1 = new Intent();
			intentTime1.putExtra(Constants.IN_MEET_TOPIC_FLAG, true);
			IntentUtil.startActivityForResult(this, DpTpActivity.class, Constants.CODE_START_TIME, intentTime1);
			break;
		case R.id.endtime:
			Intent intentTime2 = new Intent();
			intentTime2.putExtra(Constants.IN_MEET_TOPIC_FLAG, true);
			IntentUtil.startActivityForResult(this, DpTpActivity.class, Constants.CODE_END_TIME, intentTime2);
			break;
		case R.id.vote_starttime:
			IntentUtil.startActivityForResult(this, DpTpActivity.class, Constants.CODE_VOTE_START_TIME);
			break;
		case R.id.vote_endtime:
			IntentUtil.startActivityForResult(this, DpTpActivity.class, Constants.CODE_VOTE_END_TIME);
			break;
		case R.id.tv_topic_controller:
			orgNO = topic.getString(Constants.TOPIC_E_TARGET_ORG_NO, "");
			if (orgNO.equals("")){
				Toast.makeText(TopicMeetAddTwoActivity.this, getString(R.string.error_empty_orgno), 
						Toast.LENGTH_SHORT).show();
				return;
			}
			Intent intent4 = new Intent();
			intent4.putExtra(Constants.ORG_NO, orgNO);
			intent4.putExtra("type", "2");

			IntentUtil.startActivityForResult(this, TopicSelectApplicatorActivity.class, 
					Constants.CODE_TOPIC_CONTROLLER, intent4);
			break;
		case R.id.tv_topic_vote_controller:
			orgNO = topic.getString(Constants.TOPIC_E_TARGET_ORG_NO, "");
			if (orgNO.equals("")){
				Toast.makeText(TopicMeetAddTwoActivity.this, getString(R.string.error_empty_orgno), 
						Toast.LENGTH_SHORT).show();
				return;
			}
			Intent intent5 = new Intent();
			intent5.putExtra("type", "3");

			intent5.putExtra(Constants.ORG_NO, orgNO);
			IntentUtil.startActivityForResult(this, TopicSelectApplicatorActivity.class, 
					Constants.CODE_TOPIC_VOTE_CONTROLLER, intent5);
			break;
		case R.id.tv_topic_vote_staticer:
			orgNO = topic.getString(Constants.TOPIC_E_TARGET_ORG_NO, "");
			if (orgNO.equals("")){
				Toast.makeText(TopicMeetAddTwoActivity.this, getString(R.string.error_empty_orgno), 
						Toast.LENGTH_SHORT).show();
				return;
			}
			Intent intent6 = new Intent();
			intent6.putExtra(Constants.ORG_NO, orgNO);
			intent6.putExtra("type", "4");
			IntentUtil.startActivityForResult(this, TopicSelectApplicatorActivity.class, 
					Constants.CODE_TOPIC_VOTE_STATICCER, intent6);
			break;
		case R.id.suggsted_group:
			IntentUtil.startActivityForResult(this, TopicSelectSuggestedGroupActivity.class, 
					Constants.CODE_SUGGUSTED_GROUP);
			break;	
		case R.id.tv_org:
			String orgname = topic.getString(Constants.TOPIC_E_TARGET_ORG_NAME, "");
			Intent intent2 = new Intent();
			intent2.putExtra(Constants.ORG_FNAME, orgname);
			
			IntentUtil.startActivityForResult(this, TopicSelectOrgActivity.class, Constants.CODE_SELECT_ORG, intent2);	
			
		default:
			break;
		}
		
	}
}
