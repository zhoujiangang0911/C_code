package cn.wislight.meetingsystem.ui.conference;

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
import cn.wislight.meetingsystem.ui.topic.TopicStayActivity;
import cn.wislight.meetingsystem.util.AttachmentListActivity;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.IntentUtil;
import cn.wislight.meetingsystem.util.LoadingDialog;
import cn.wislight.meetingsystem.util.MeetingSystemClient;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * @author Administrator 主页 新增议题第三个
 */
public class ConfJoinDetailActivity extends BaseActivity {
	private TextView tvTitle,tvRemark, tvAddress, tvStartdate, tvEnddate;
	//private TextView tvChecker, tvSuggestedAttender, tvCreator;
	private LoadingDialog loadingdiag;

	String meetid;
	@Override
	public void initView() {

		tvTitle = (TextView) findViewById(R.id.conference_title);
		tvRemark = (TextView) findViewById(R.id.conference_remark);
		tvAddress = (TextView) findViewById(R.id.conference_address);
		tvStartdate = (TextView) findViewById(R.id.tv_starttime);
		tvEnddate = (TextView) findViewById(R.id.tv_endtime);

		//tvChecker = (TextView) findViewById(R.id.tv_checker);
		//tvSuggestedAttender = (TextView) findViewById(R.id.tv_suggested_attender);
		//tvCreator = (TextView) findViewById(R.id.tv_creator);

		Intent intent = getIntent();
	    meetid = intent.getStringExtra("id");
		
		loadingdiag = new LoadingDialog(this);  
		loadingdiag.setCanceledOnTouchOutside(false); 
		loadingdiag.setText(getString(R.string.loading));
		
		
		getConferenceDetail(meetid);
	}

	@Override
	public void setupView() {
		setContentView(R.layout.conference_join_detail);
	}

	protected void onPause() {
		super.onPause();

	}
	
	public void clickAttachmentList(View view){
		Intent data=new Intent();  
        data.putExtra(Constants.ID, meetid);
        
        IntentUtil.startActivity(ConfJoinDetailActivity.this, AttachmentListActivity.class,  data);
	}	
	
	public void clickJoinMember(View view) {
		Intent data=new Intent();  
        data.putExtra(Constants.ID, meetid);  
        IntentUtil.startActivity(ConfJoinDetailActivity.this, ConfJoinMemberListActivity.class,  data);
  
	}
	public void clickConfJoinMember(View view) {
		Intent data=new Intent();  
        data.putExtra(Constants.ID, meetid);  
        IntentUtil.startActivity(ConfJoinDetailActivity.this, ConfJoinConfMemberListActivity.class,  data);
	}
	
	public void clickTopicList(View view) {
		Intent data=new Intent();  
        data.putExtra(Constants.ID, meetid);  
        IntentUtil.startActivity(ConfJoinDetailActivity.this, ConfTopicListActivity.class,  data);
  
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
				Toast.makeText(ConfJoinDetailActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
				System.out.println("wangting"+error.getMessage());
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {				
				loadingdiag.hide();
				String response = new String(body);				
				System.out.println("wangting"+response);
				if (response.contains("用户未登陆")){
					Toast.makeText(ConfJoinDetailActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(response);				               
					tvTitle.setText(jsonObject.getString("title"));
					tvRemark.setText(jsonObject.getString("remark"));
					tvAddress.setText(jsonObject.getString("address"));
					tvStartdate.setText(jsonObject.getString("startdate"));
					tvEnddate.setText(jsonObject.getString("enddate"));

				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ConfJoinDetailActivity.this,
							getString(R.string.error_dataabout),
							Toast.LENGTH_SHORT).show();
					
				}
			}
		});
	}

}
