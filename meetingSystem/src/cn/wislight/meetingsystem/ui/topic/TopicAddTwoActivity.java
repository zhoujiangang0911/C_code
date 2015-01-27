package cn.wislight.meetingsystem.ui.topic;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.domain.GroupElement;
import cn.wislight.meetingsystem.ui.conference.ConBeginOneActivity;
import cn.wislight.meetingsystem.ui.conference.ConfenenceManagementActivity;
import cn.wislight.meetingsystem.util.Authorize;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.DpTpActivity;
import cn.wislight.meetingsystem.util.Element;
import cn.wislight.meetingsystem.util.IntentUtil;
import cn.wislight.meetingsystem.util.Variables;

import com.google.gson.Gson;
/**
 * @author Administrator
 * 主页  新增议题第二个
 */
public class TopicAddTwoActivity extends BaseActivity implements OnClickListener{
	private SharedPreferences topic;
	private TextView textApplicateMember;
	private TextView textSuggetedAttender;
	private TextView textStarttime;
	private TextView textEndtime;
	private TextView textOrg;
	private TextView textGroup;
	private String orgNO="";
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		textApplicateMember=(TextView)findViewById(R.id.applicatemember);
		textSuggetedAttender=(TextView)findViewById(R.id.suggsted_attender);
		textStarttime = (TextView)findViewById(R.id.starttime);
		textEndtime = (TextView)findViewById(R.id.endtime);
		textOrg = (TextView)findViewById(R.id.tv_org);
		textGroup = (TextView)findViewById(R.id.suggsted_group);
		
		textStarttime.setOnClickListener(this);
		textEndtime.setOnClickListener(this);
		textApplicateMember.setOnClickListener(this);
		textSuggetedAttender.setOnClickListener(this);
		textOrg.setOnClickListener(this);
		textGroup.setOnClickListener(this);
		if(Authorize.hasAuth(Authorize.AUTH_CONF_NEW)){
			textGroup.setVisibility(View.VISIBLE);
		} else {
			textGroup.setVisibility(View.GONE);
		}
		topic = this.getSharedPreferences(Constants.TOPIC_NODE + Variables.loginname, MODE_PRIVATE);
		String topic_start_date = topic.getString(Constants.TOPIC_START_DATE, "");
		String topic_end_date = topic.getString(Constants.TOPIC_END_DATE, "");
		String topic_target_org_name = topic.getString(Constants.TOPIC_TARGET_ORG_NAME, "");
		String topic_suggested_attender_names = topic.getString(Constants.TOPIC_SUGGESTED_ATTENDER_NAMES, "");
		String topic_checker_name = topic.getString(Constants.TOPIC_CHECKER_NAME, "");
		
		String topic_group_names = topic.getString(Constants.TOPIC_SUGGESTED_GROUP_NAMES, "");
		
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
		
		if (topic_target_org_name == null || "".equals(topic_target_org_name)){
			textOrg.setText(R.string.topic_targetdepartment);
		}else{
			textOrg.setText(getString(R.string.topic_targetdepartment) + ":    "
					+ topic_target_org_name);
		}
		
		if (topic_suggested_attender_names == null || "".equals(topic_suggested_attender_names)){
			textSuggetedAttender.setText(R.string.topic_joinmember);
		}else{
			textSuggetedAttender.setText(getString(R.string.topic_joinmember) + ":    " 
					+ topic_suggested_attender_names);
		}
		
		if (topic_group_names == null || "".equals(topic_group_names)){
			textGroup.setText(R.string.topic_joingroup);
		}else{
			textGroup.setText(getString(R.string.topic_joingroup) + ":    " 
					+ topic_group_names);
		}
		
		
		if (topic_checker_name == null || "".equals(topic_checker_name)){
			textApplicateMember.setText(R.string.conf_accessmeb);
		}else{
			textApplicateMember.setText(getString(R.string.conf_accessmeb) + ":    "
					+ topic_checker_name);
		}
		
	}

	@Override
	public void setupView() {
		setContentView(R.layout.topic_addtwo_main);
	}
	
	protected void onPause(){
		super.onPause();
	}
	
	
	
	public void  clickPre(View view){
		Intent intent = new Intent();
		intent.putExtra("loaddata", false);
		IntentUtil.startActivity(this,TopicAddOneActivity.class);
		this.finish();
	}

	public void  clickNext(View view){
		String topic_start_date = topic.getString(Constants.TOPIC_START_DATE, "");
		String topic_end_date = topic.getString(Constants.TOPIC_END_DATE, "");
		String topic_target_org_name = topic.getString(Constants.TOPIC_TARGET_ORG_NAME, "");
		String topic_suggested_attender_names = topic.getString(Constants.TOPIC_SUGGESTED_ATTENDER_NAMES, "");
		String topic_checker_name = topic.getString(Constants.TOPIC_CHECKER_NAME, "");
		String topic_group_names = topic.getString(Constants.TOPIC_SUGGESTED_GROUP_NAMES, "");
		
		if (topic_start_date.length() == 0){
			Toast.makeText(TopicAddTwoActivity.this, getString(R.string.error_no_topic_starttime), Toast.LENGTH_SHORT).show();
			return;
		}
		if (topic_end_date.length() == 0){
			Toast.makeText(TopicAddTwoActivity.this, getString(R.string.error_no_topic_endtime), Toast.LENGTH_SHORT).show();
			return;
		}

		if (topic_target_org_name.length() == 0){
			Toast.makeText(TopicAddTwoActivity.this, getString(R.string.error_no_topic_org), Toast.LENGTH_SHORT).show();
			return;
		}
		if(Authorize.hasAuth(Authorize.AUTH_CONF_NEW)){
			if (!(topic_suggested_attender_names.length() > 0 | topic_group_names.length() > 0)){
				Toast.makeText(TopicAddTwoActivity.this, getString(R.string.error_no_group_or_member), Toast.LENGTH_SHORT).show();
				return;
			}
		} else {
			if (!(topic_suggested_attender_names.length() > 0)){
				Toast.makeText(TopicAddTwoActivity.this, getString(R.string.error_no_suggested_atttender), Toast.LENGTH_SHORT).show();
				return;
			}
		}
		
		if (topic_checker_name.length() == 0){
			Toast.makeText(TopicAddTwoActivity.this, getString(R.string.error_no_topic_checker), Toast.LENGTH_SHORT).show();
			return;
		}
		if (checkTime() == false){
			return;
		}
		if (topic_end_date.compareTo(topic_start_date) <= 0){
			Toast.makeText(TopicAddTwoActivity.this, getString(R.string.error_topic_timeerror), Toast.LENGTH_SHORT).show();
			return;
		}

		IntentUtil.startActivity(this,TopicAddThreeActivity.class);
		this.finish();
	}
	private boolean checkTime(){
		boolean result = true;
		String topic_start_date = topic.getString(Constants.TOPIC_START_DATE, "");

		String starttime = "";
        starttime = topic_start_date.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");

        String topic_end_date = topic.getString(Constants.TOPIC_END_DATE, "");
        String endtime = "";
        endtime = topic_end_date.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");

        
        Calendar calendar = Calendar.getInstance();
        Integer current_year=calendar.get(Calendar.YEAR);
        Integer current_monthOfYear = calendar.get(Calendar.MONTH)+1;
        Integer current_dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        Integer current_hour = calendar.get(Calendar.HOUR_OF_DAY);
        Integer current_minute = calendar.get(Calendar.MINUTE); 
        String currenttime = formatDateTime(current_year,current_monthOfYear,current_dayOfMonth,current_hour,current_minute);
        //System.out.println("wangting current time="+currenttime+"#");
        //System.out.println("wangting   start time="+starttime+"#");
        //System.out.println("wangting     end time="+endtime+"#");
        
        if (endtime.compareTo(currenttime) <= 0){
        	Toast.makeText(TopicAddTwoActivity.this, getString(R.string.error_no_topic_endtime_too_small), Toast.LENGTH_SHORT).show();
			return false;
        }   
        /*
        if (endtime.compareTo(starttime) != 1){
        	Toast.makeText(TopicAddTwoActivity.this, getString(R.string.error_no_topic_endtime_too_small), Toast.LENGTH_SHORT).show();
			return false;
        }
         */

		return result;
	}
	private String formatDateTime(int year,int month, int date, int hour, int minute){
        String datetime = String.format("%1$04d", year)
        		+ String.format("%1$02d", month) 
        		+ String.format("%1$02d", date)
        		+ String.format("%1$02d", hour)
        		+ String.format("%1$02d",minute) 
        		+ "00";        
        return datetime;
	}
	@SuppressWarnings("unchecked")
	protected void onActivityResult(int requestCode, int resultCode, Intent data)  
    {  
        Editor editor = topic.edit();

       
        if(Constants.CODE_CHECKER == requestCode)  
        {  
        	if (Constants.OK == resultCode && null != data){
        		String name=data.getExtras().getString(Constants.NAME);
        		String id=data.getExtras().getString(Constants.ID);
        		
        		textApplicateMember.setText(getString(R.string.conf_accessmeb) + ":    " + name);
        		
        		editor.putString(Constants.TOPIC_CHECKER_NAME, name);
        		editor.putString(Constants.TOPIC_CHECKER_ID, id);
        	}
            
        }

        if(Constants.CODE_START_TIME == requestCode)  
        {  
        	if (Constants.OK == resultCode && null != data){
        		String dt = data.getExtras().getString(Constants.DATETIME);  
        		textStarttime.setText(dt);
        		editor.putString(Constants.TOPIC_START_DATE, dt);
        	}
        }
        
        if(Constants.CODE_END_TIME == requestCode )  
        {  
        	if (Constants.OK == resultCode && null != data){
        		String dt = data.getExtras().getString(Constants.DATETIME);  
        		textEndtime.setText(dt);
        		editor.putString(Constants.TOPIC_END_DATE, dt);
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
        		editor.putString(Constants.TOPIC_SUGGESTED_ATTENDER_NAMES, temp);
        		editor.putString(Constants.TOPIC_SUGGESTED_ATTENDER_IDS, ids);
        		
                Gson gson = new Gson();
                String beanListToJson = gson.toJson(selList);   
        		editor.putString(Constants.TOPIC_SUGGESTED_ATTENDER_LIST, beanListToJson);
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
        		editor.putString(Constants.TOPIC_SUGGESTED_GROUP_NAMES, temp);
        		editor.putString(Constants.TOPIC_SUGGESTED_GROUP_IDS, ids);

        	}
            
        }
        if(Constants.CODE_SELECT_ORG == requestCode )  
        {  
        	if (Constants.OK == resultCode && null != data){
        		String fname = data.getExtras().getString(Constants.ORG_FNAME); 
        		orgNO = data.getExtras().getString(Constants.ORG_NO);
        		textOrg.setText(getString(R.string.topic_targetdepartment) + ":    "  + fname);
        		
        		editor.putString(Constants.TOPIC_TARGET_ORG_NAME, fname);
        		editor.putString(Constants.TOPIC_TARGET_ORG_NO, orgNO);
        	}
            
        }  
        
        editor.commit();
        super.onActivityResult(requestCode, resultCode, data);  
    }	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.applicatemember:
			orgNO = topic.getString(Constants.TOPIC_TARGET_ORG_NO, "");
			if (orgNO.equals("")){
				Toast.makeText(TopicAddTwoActivity.this, getString(R.string.error_empty_orgno), 
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
			orgNO = topic.getString(Constants.TOPIC_TARGET_ORG_NO, "");
			if (orgNO.equals("")){
				Toast.makeText(TopicAddTwoActivity.this, getString(R.string.error_empty_orgno), 
						Toast.LENGTH_SHORT).show();
				return;
			}
			Intent intent1 = new Intent();
			intent1.putExtra(Constants.ORG_NO, orgNO);
			intent1.putExtra("isTopicDraft", "true");
			IntentUtil.startActivityForResult(this, TopicSelectSuggetedAttenderActivity.class, 
					Constants.CODE_SUGGUSTED_ATTENDER, intent1);
			break;
			
		case R.id.suggsted_group:

			IntentUtil.startActivityForResult(this, TopicSelectSuggestedGroupActivity.class, 
					Constants.CODE_SUGGUSTED_GROUP);
			break;
			
		case R.id.starttime:
			Intent starttime = new Intent();
			String tempStarttime = "";
			if (textStarttime.getText() != null){
				tempStarttime = textStarttime.getText().toString();
			}
			starttime.putExtra(Constants.INIT_TIME, tempStarttime);
			IntentUtil.startActivityForResult(this, DpTpActivity.class, Constants.CODE_START_TIME, starttime);
			break;
		case R.id.endtime:
			Intent endtime = new Intent();
			String tempEndtime = "";
			if (textEndtime.getText() != null){
				tempEndtime = textEndtime.getText().toString();
			}
			endtime.putExtra(Constants.INIT_TIME, tempEndtime);
			IntentUtil.startActivityForResult(this, DpTpActivity.class, Constants.CODE_END_TIME,endtime);
			break;
		case R.id.tv_org:
			String orgname = topic.getString(Constants.TOPIC_TARGET_ORG_NAME, "");
			Intent intent2 = new Intent();
			intent2.putExtra(Constants.ORG_FNAME, orgname);
			
			IntentUtil.startActivityForResult(this, TopicSelectOrgActivity.class, Constants.CODE_SELECT_ORG, intent2);
		default:
			break;
		}
		
	}
}
