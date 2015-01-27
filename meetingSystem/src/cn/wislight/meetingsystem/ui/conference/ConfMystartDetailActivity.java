package cn.wislight.meetingsystem.ui.conference;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.ui.topic.TopicStayActivity;
import cn.wislight.meetingsystem.ui.topic.TopicUnArrivalActivity;
import cn.wislight.meetingsystem.util.AttachmentListActivity;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.IntentUtil;
import cn.wislight.meetingsystem.util.LoadingDialog;
import cn.wislight.meetingsystem.util.MeetingSystemClient;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * @author Administrator 主页 新增议题第三个
 */




public class ConfMystartDetailActivity extends BaseActivity {
	private TextView tvTitle,tvRemark, tvAddress, tvStartdate, tvEnddate;
	private TextView tvConfType;
	private TextView tvConfDepartment;
	//private TextView tvChecker, tvSuggestedAttender, tvCreator;
	private TextView tvJoinMember, tvTopicList;
	private EditText etReason;
	private LoadingDialog loadingdiag;
	private String checkReason;
	String meetid;
	private int meetstatu;
	private int auth_control;
	private TextView tvShowConclusion;
	@Override
	public void initView() {
		
		tvJoinMember=(TextView) findViewById(R.id.conf_joinmember);
		tvTopicList=(TextView) findViewById(R.id.conf_topic_list);
		tvJoinMember.setVisibility(View.VISIBLE);
		tvTopicList.setVisibility(View.VISIBLE);

		tvTitle = (TextView) findViewById(R.id.conference_title);
		tvRemark = (TextView) findViewById(R.id.conference_remark);
		tvAddress = (TextView) findViewById(R.id.conference_address);
		tvStartdate = (TextView) findViewById(R.id.tv_starttime);
		tvEnddate = (TextView) findViewById(R.id.tv_endtime);
		tvConfType = (TextView) findViewById(R.id.tv_conf_type);
		tvConfDepartment = (TextView) findViewById(R.id.tv_conf_department);
		tvShowConclusion = (TextView) findViewById(R.id.conference_showconclusion);
		//tvChecker = (TextView) findViewById(R.id.tv_checker);
		//tvSuggestedAttender = (TextView) findViewById(R.id.tv_suggested_attender);
		//tvCreator = (TextView) findViewById(R.id.tv_creator);
		etReason = (EditText) findViewById(R.id.conference_conclusion);
		Intent intent = getIntent();
	    meetid = intent.getStringExtra("id");
		
		loadingdiag = new LoadingDialog(this);  
		loadingdiag.setCanceledOnTouchOutside(false); 
		loadingdiag.setText(getString(R.string.loading));
		
		
		getConferenceDetail(meetid);
	}

	@Override
	public void setupView() {
		setContentView(R.layout.conference_mystarted_conf_detail);
	}

	protected void onPause() {
		super.onPause();

	}
	public void clickJoinMember(View view) {
		Intent data=new Intent();  
        data.putExtra(Constants.ID, meetid);  
        IntentUtil.startActivity(ConfMystartDetailActivity.this, ConfJoinMemberListActivity.class,  data);
  
	}
	public void clickConfJoinMember(View view) {
		Intent data=new Intent();  
        data.putExtra(Constants.ID, meetid);  
        IntentUtil.startActivity(ConfMystartDetailActivity.this, ConfJoinConfMemberListActivity.class,  data);
	}
	public void clickTopicList(View view) {
		Intent data=new Intent();  
        data.putExtra(Constants.ID, meetid);
        data.putExtra(Constants.CONF_CONTROL,   "no");
        data.putExtra(Constants.CONF_VIEW_HISTORY,   "yes");

        IntentUtil.startActivity(ConfMystartDetailActivity.this, ConfTopicListActivity.class,  data);
  
	}
	
	public void clickAttachmentList(View view){
		Intent data=new Intent();  
        data.putExtra(Constants.ID, meetid);
        
        IntentUtil.startActivity(ConfMystartDetailActivity.this, AttachmentListActivity.class,  data);
	}
	
	public void clickRefuse(View view) {
		/*
		if (etReason.getText().toString().length() <= 0){
			Toast.makeText(ConfMystartDetailActivity.this,
				getString(R.string.error_topic_check_noreason), Toast.LENGTH_SHORT).show();
			return;
		}		
		checkReason = etReason.getText().toString();
		StorageConference(checkReason);
		*/
		Intent data=new Intent();  
        data.putExtra(Constants.ID, 5);  
        IntentUtil.startActivity(ConfMystartDetailActivity.this, TopicUnArrivalActivity.class,  data);
		
	}



	public void clickPass(View view) {
		if (etReason.getText().toString().length() <= 0){
			Toast.makeText(ConfMystartDetailActivity.this,
				getString(R.string.error_meet_noconclusion), Toast.LENGTH_SHORT).show();
			return;
		}
		checkReason = etReason.getText().toString();
		StorageConference(checkReason);
	}

	private void StorageConference(String checkReason) {
		String url = "MeetingManage/mobile/doStorageMeeting.action?meetingId="+ meetid				     
				     + "&conclusion=" + checkReason;
		loadingdiag.setText(getString(R.string.uploading));
		loadingdiag.show();  
		System.out.println("wangting"+url);
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();  
				System.out.println("wangting"+error.getMessage());
				Toast.makeText(ConfMystartDetailActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
				//finish();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				loadingdiag.hide();
				String response = new String(body);
				System.out.println("wangting"+response);
				if (response.contains("用户未登陆")){
					Toast.makeText(ConfMystartDetailActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				if (response.contains("true")){
					Toast.makeText(ConfMystartDetailActivity.this, "归档成功", Toast.LENGTH_SHORT).show();
					finish();
					/*
					Intent intent = new Intent();    
					intent.setClass((Context)ConfCheckDetailActivity.this, TopicStayActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);   
					startActivity(intent);  
					*/
				}else{
					Toast.makeText(ConfMystartDetailActivity.this, "归档失败", Toast.LENGTH_SHORT).show();
				}
			}
		});		
	}
	
	private void getConferenceDetail(String id) {
		// TODO Auto-generated method stub
		String url = "MeetingManage/mobile/findMeetingDetailById.action?type=1&meetid="+id;
		loadingdiag.show();  
		System.out.println("wangting"+url);
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();  
				Toast.makeText(ConfMystartDetailActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
				System.out.println("wangting"+error.getMessage());
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {				
				loadingdiag.hide();
				String response = new String(body);			
				
				if (response.contains("用户未登陆")){
					Toast.makeText(ConfMystartDetailActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				System.out.println("wangting"+response);
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(response);				               
					tvTitle.setText(jsonObject.getString("title"));
					tvRemark.setText(jsonObject.getString("remark"));
					tvAddress.setText(jsonObject.getString("address"));
					tvStartdate.setText(jsonObject.getString("startdate"));
					tvEnddate.setText(jsonObject.getString("enddate"));
					tvConfType.setText(jsonObject.getString("typename"));
					tvConfDepartment.setText(jsonObject.getString("createoganize"));
					tvShowConclusion.setText(jsonObject.getString("conclusion"));
					
					meetstatu = Integer.parseInt(jsonObject.getString("meetstatu")); 
					auth_control = Integer.parseInt(jsonObject.getString("auth_control"));
					
					
				
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ConfMystartDetailActivity.this,
							getString(R.string.error_dataabout),
							Toast.LENGTH_SHORT).show();
					
				}
			}
		});
	}

}
