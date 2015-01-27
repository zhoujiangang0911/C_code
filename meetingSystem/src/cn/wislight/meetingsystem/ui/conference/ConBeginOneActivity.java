package cn.wislight.meetingsystem.ui.conference;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.ui.topic.TopicAddOneActivity;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.IntentUtil;
import cn.wislight.meetingsystem.util.Variables;

/**
 * @author Administrator
 *	会议发起
 */
public class ConBeginOneActivity extends BaseActivity{
	private SharedPreferences conference;
	private EditText edittextConferenceTitle;
	private EditText edittextConferenceContent;
	private TextView edittextConferenceAddress;
	private TextView textConferenceType;
	@Override
	public void initView() {
		conference = this.getSharedPreferences("conference"+ Variables.loginname, MODE_PRIVATE);		

		edittextConferenceTitle = (EditText)findViewById(R.id.conf_conftitle);
		edittextConferenceContent = (EditText)findViewById(R.id.conf_confcontent);
		edittextConferenceAddress = (TextView)findViewById(R.id.conf_confaddress);
		textConferenceType = (TextView)findViewById(R.id.conf_conftype);
		
		
        edittextConferenceTitle.setText(conference.getString("conference_title", ""));       
        edittextConferenceContent.setText(conference.getString("conference_content", ""));       
        edittextConferenceAddress.setText(conference.getString("conference_address", ""));       
        textConferenceType.setText(getString(R.string.conf_type) + ":    " +conference.getString("conference_conftype", ""));       
	
	}

	@Override
	public void setupView() {
		setContentView(R.layout.conference_begin_one);
	}
	
	protected void onPause(){
		super.onPause();
        Editor editor = conference.edit();
        editor.putString("conference_title", edittextConferenceTitle.getText().toString());       
        editor.putString("conference_content", edittextConferenceContent.getText().toString());
        //editor.putString("conference_conftype", textConferenceType.getText().toString());
        editor.commit();
	}
	public void  clickConfType(View view){
		System.out.println("wangting:clickConfType");
		IntentUtil.startActivityForResult(this, ConfSelectConfTypeActivity.class, Constants.CODE_CONF_CONFTYPE);

	}

	public void clickSelectMeetHouse(View view){
		IntentUtil.startActivityForResult(this, ConfSelectHourseActivity.class, Constants.CODE_CONF_HOURSE);
	}
	
	public void  clickNext(View view){
		if (edittextConferenceTitle.getText().toString().length() == 0){
			Toast.makeText(ConBeginOneActivity.this, getString(R.string.error_no_conf_title), Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (edittextConferenceContent.getText().toString().length() == 0){
			Toast.makeText(ConBeginOneActivity.this, getString(R.string.error_no_conf_content), Toast.LENGTH_SHORT).show();
			return;
		}

		if (edittextConferenceAddress.getText().toString().length() == 0){
			Toast.makeText(ConBeginOneActivity.this, getString(R.string.error_no_conf_addr), Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (conference.getString("conference_conftype", "").length() == 0){
			Toast.makeText(ConBeginOneActivity.this, getString(R.string.error_no_conf_type), Toast.LENGTH_SHORT).show();
			return;
		}
		
		IntentUtil.startActivity(this,ConBeginTwoActivity.class);
		finish();
	}
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        //可以根据多个请求代码来作相应的操作  
        if(Constants.CODE_CONF_CONFTYPE == requestCode)        {  
        	if (Constants.OK == resultCode){
        		if (data != null){
        			String typename=data.getExtras().getString("typename");  
        			String typeid = data.getExtras().getString("id");
        			Editor editor = conference.edit();
        			editor.putString("conference_conftype", typename);
        			editor.putString("conference_conftype_id", typeid);
        			editor.commit();
        			textConferenceType.setText(getString(R.string.conf_type) + ":    " + typename);
        		}
        	}            
        } 
        if (Constants.CODE_CONF_HOURSE == requestCode && Constants.OK == resultCode && data != null){
			String fullAddr=data.getExtras().getString("fullAddr");  
			String addrId = data.getExtras().getString("id");
			Editor editor = conference.edit();
			editor.putString("conference_address", fullAddr);
			editor.putString("conference_address_id", addrId);
			editor.commit();
			
			edittextConferenceAddress.setText(fullAddr);
        }
        
        super.onActivityResult(requestCode, resultCode, data);  
    }
}
