package cn.wislight.meetingsystem.ui.topic;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.ui.vote.VoteManagementActivity;
import cn.wislight.meetingsystem.ui.vote.VoteResultActivity;
import cn.wislight.meetingsystem.ui.vote.VoteWillActivity;
import cn.wislight.meetingsystem.util.AttachmentListActivity;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.FileChooserActivity;
import cn.wislight.meetingsystem.util.IntentUtil;
import cn.wislight.meetingsystem.util.LoadingDialog;
import cn.wislight.meetingsystem.util.MeetingSystemClient;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


/**
 * @author Administrator
 *	议题详情
 */
public class TopicDetailActivity extends BaseActivity {
	private String topicId;
	private LoadingDialog loadingdiag;
	private TextView tvTopicTime;
	private TextView tvTopicSummary;
	private TextView tvTopicControlState;
	private TextView tvTopicVoteTime;
	private TextView tvTopicIsVote;
	private TextView tvTopicConclusion;
	private TextView tvTopicStaticcer;
	private TextView tvTopicVoteCtrl;
	private TextView tvTopicCtrl;
	private LinearLayout llTpConclusion;
	private ImageButton btnUploadConclusion;
	
	private int topicControlState;
	private int topicVoteState;
	private int topicNeedVote;
	private int authority_control;
	private int authority_vote_ctrl;
	private int authority_vote_sum;
	private int hasConcluded;
	private int hasMentioned = 0;
	private int voteType;
	private String topnoId;
	private TextView 
		tvTopicDetailStart,
		tvTopicDetailDianming, 
		tvTopicDetailStartvote, 
		tvTopicDetailEndvote, 
		tvTopicDetailVoteresult,
		tvVotemanage,
		tvTopicVirtual;
	private Button ibtnTopicEnd;
	
	private TextView tvAppendix;
	private TextView tvAppendixUploaded;
	private LinearLayout llConfAppendix;
	private int upLoadIndex;
	private ArrayList<UpLoadBundle> uploadList;
	
	private TextView tvTopicAvrVote;
	private TextView tvTopicAttribute;
	String IMgroupid = "";
	@Override
	public void initView() {
		topicId = this.getIntent().getStringExtra(Constants.ID);
		
		loadingdiag = new LoadingDialog(this);  
		loadingdiag.setCanceledOnTouchOutside(false); 
		loadingdiag.setText(getString(R.string.loading));
		
		tvTopicTime = (TextView)findViewById(R.id.tv_topic_time);
		tvTopicSummary = (TextView)findViewById(R.id.tv_topic_summary);
		tvTopicControlState = (TextView)findViewById(R.id.tv_topic_control_state);
		tvTopicIsVote = (TextView)findViewById(R.id.tv_topic_is_vote);
		tvTopicVoteTime = (TextView)findViewById(R.id.tv_topic_vote_time);
		
		tvTopicDetailStart = (TextView)findViewById(R.id.topic_detail_start);
		tvTopicDetailDianming = (TextView)findViewById(R.id.topic_detail_dianming);
		tvTopicDetailStartvote = (TextView)findViewById(R.id.topic_detail_startvote);
		tvTopicDetailEndvote = (TextView)findViewById(R.id.topic_detail_endvote);
		tvTopicDetailVoteresult = (TextView)findViewById(R.id.topic_detail_voteresult);
		tvVotemanage = (TextView)findViewById(R.id.vote_votemanage);
		tvTopicVirtual = (TextView)findViewById(R.id.topic_virtual);		
		ibtnTopicEnd = (Button)findViewById(R.id.topic_end);
		tvTopicStaticcer = (TextView)findViewById(R.id.tv_topic_staticcer);
		tvTopicVoteCtrl = (TextView)findViewById(R.id.tv_topic_vote_ctrl);
		tvTopicCtrl = (TextView)findViewById(R.id.tv_topic_ctrl);
		
		tvTopicAvrVote = (TextView)findViewById(R.id.topic_avr_vote);
		tvTopicConclusion = (TextView)findViewById(R.id.topic_conclusion);
		llTpConclusion = (LinearLayout)findViewById(R.id.ll_topic_conclusion);
		tvTopicAttribute = (TextView)findViewById(R.id.tv_topic_attribute);
		btnUploadConclusion =  (ImageButton)findViewById(R.id.btn_upload_topic_concusion);
		
		llConfAppendix = (LinearLayout) findViewById(R.id.ll_conf_appendix);
		tvAppendix = (TextView)findViewById(R.id.tv_appendix);
		tvAppendixUploaded = (TextView)findViewById(R.id.tv_appendix_uploaded);
		uploadList = new ArrayList<UpLoadBundle>();
		
		hideAllController();
		getTopicDetail(topicId);
	}

	private void hideAllController() {
		// TODO Auto-generated method stub
		tvTopicDetailStart.setVisibility(View.GONE);
		tvTopicDetailDianming.setVisibility(View.GONE);
		tvTopicDetailStartvote.setVisibility(View.GONE);
		tvTopicDetailEndvote.setVisibility(View.GONE);
		tvTopicDetailVoteresult.setVisibility(View.GONE);
		tvVotemanage.setVisibility(View.GONE);
		tvTopicVirtual.setVisibility(View.GONE);		
		ibtnTopicEnd.setVisibility(View.GONE);
		llTpConclusion.setVisibility(View.GONE);
		llConfAppendix.setVisibility(View.GONE);
		tvTopicAvrVote.setVisibility(View.GONE);

	}

	@Override
	public void setupView() {
		setContentView(R.layout.topic_detail_main);
	}
	public void clickAttachmentList(View view){
		Intent data=new Intent();  
        data.putExtra(Constants.ID, topicId);
        
        IntentUtil.startActivity(TopicDetailActivity.this, AttachmentListActivity.class,  data);
	}
	private void getTopicDetail(String topicId2) {
		// TODO Auto-generated method stub
		String url = "MeetingManage/mobile/getMeetingProcDetail.action?type=0&meetingProcId="
				+ topicId2;
		loadingdiag.show();
		
		System.out.println("wangting:"+url);
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler(){
			@Override
			public void onFailure(int statusCode, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();
				Toast.makeText(TopicDetailActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(int stautsCode, Header[] arg1, byte[] body) {
				String response = new String(body);
				loadingdiag.hide();
				System.out.println("wangting:"+response);
				
				if (response.contains("用户未登陆")){
					Toast.makeText(TopicDetailActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				try{
				JSONObject jsonobject = new JSONObject(response);
				
					String topic_keys = jsonobject.getString("content");
					String topic_summary = jsonobject.getString("name");
					String topic_start_date = jsonobject.getString("startTime");
					String topic_end_date = jsonobject.getString("endTime");
					String topic_checker_name = jsonobject.getString("checkername");
					
					String topic_attender = jsonobject.getString("attenders");
					String topic_vote_staticer = jsonobject.getString("summanageuser");
					String topic_vote_controller = jsonobject.getString("vomanageuser");
					String topic_controller = jsonobject.getString("manageuser");
					String topic_vote_endtime = jsonobject.getString("voendTime");
					String topic_vote_starttime = jsonobject.getString("vostaTime");
					String topic_vote_abstain = jsonobject.getString("isabstain");
					String topic_vote_type = jsonobject.getString("votetype");
					String topic_is_vote = jsonobject.getString("isneedvot");
					voteType = Integer.parseInt(topic_vote_type);
					String topic_staticcer = jsonobject.getString("summanageuser");
					String topic_vote_ctrl = jsonobject.getString("vomanageuser");
					String topic_ctrl = jsonobject.getString("manageuser");
					topnoId = jsonobject.getString("topnoId");
					String topicConclusion = jsonobject.getString("conclusion");
					String topicConcluded = jsonobject.getString("hasConcluded");
					String topic_source = jsonobject.getString("source");
					if ("true".equals(topicConcluded)){
						hasConcluded = 1;
					} else {
						hasConcluded = 0;
					}
					
					IMgroupid = jsonobject.getString("IMgroupid");
					authority_control = Integer.parseInt(jsonobject.getString("authority_control"));
					authority_vote_ctrl = Integer.parseInt(jsonobject.getString("authority_vote_ctrl"));
					authority_vote_sum = Integer.parseInt(jsonobject.getString("authority_vote_sum"));
					
					topicControlState = Integer.parseInt(jsonobject.getString("status"));
					topicNeedVote = Integer.parseInt(topic_is_vote);
					topicVoteState = Integer.parseInt(jsonobject.getString("votestate"));
					
					tvTopicStaticcer.setText(topic_staticcer);
					tvTopicVoteCtrl.setText(topic_vote_ctrl);
					tvTopicCtrl.setText(topic_ctrl);
					tvTopicConclusion.setText(topicConclusion);

					
					if ("1".equals(topic_is_vote)){
						tvTopicIsVote.setText(R.string.common_yes);
						tvTopicVoteTime.setText(topic_vote_starttime + "--" + topic_vote_endtime);
					} else {
						tvTopicIsVote.setText(R.string.common_no);
						tvTopicVoteTime.setText("");
					}
					
					tvTopicSummary.setText(topic_summary);
					tvTopicTime.setText(topic_start_date + "--" + topic_end_date);
					
					if (null != topic_source && topic_source.contains("inmeetcreate")){
						tvTopicAttribute.setText(R.string.topic_Attribute_inmeetcreate);
					}else if ("".equals(topic_source)){
						tvTopicAttribute.setText(R.string.topic_Attribute_inmeetcreate);
					}else{
						tvTopicAttribute.setText(R.string.topic_Attribute_topicdb);
					}
					
					updateUi();
					
					
				}catch (Exception e){
					e.printStackTrace();
					Toast.makeText(TopicDetailActivity.this, getString(R.string.error_dataabout), Toast.LENGTH_SHORT).show();
					
				}
			}
		
		});		
	}
	
	private void updateUi() {
		// TODO Auto-generated method stub
		int state[] = {R.string.topic_control_status_tostart,
				   R.string.topic_control_status_started,
				   R.string.topic_control_status_ended	
					};
		tvTopicControlState.setText(state[topicControlState]);
		hideAllController();
		

		
		switch (topicControlState){
		case Constants.TOPIC_CTRL_STATE_TOSTART:
			if (1 == authority_control){
					tvTopicDetailStart.setVisibility(View.VISIBLE);
					tvTopicDetailDianming.setVisibility(View.VISIBLE);
			}
			break;
		case Constants.TOPIC_CTRL_STATE_STARTED:
			if (1 == authority_control){
				tvTopicDetailDianming.setVisibility(View.VISIBLE);

				if (0 == hasConcluded){
					llTpConclusion.setVisibility(View.VISIBLE);
					tvTopicConclusion.setEnabled(true);
					btnUploadConclusion.setVisibility(View.VISIBLE);
				} else {
					llTpConclusion.setVisibility(View.VISIBLE);
					tvTopicConclusion.setEnabled(false);
					btnUploadConclusion.setVisibility(View.GONE);
				}
			}
			if (topicNeedVote == 1){
				if (topicVoteState == 0){
					if (1 == authority_vote_ctrl){
						if (1 == hasConcluded){
							llTpConclusion.setVisibility(View.VISIBLE);
							tvTopicConclusion.setEnabled(false);
							btnUploadConclusion.setVisibility(View.GONE);
							
							tvTopicDetailStartvote.setVisibility(View.VISIBLE);
						}
					} 
						
				} else if (topicVoteState == 1) {
					
					if (1 == authority_vote_ctrl){
						llTpConclusion.setVisibility(View.VISIBLE);
						tvTopicConclusion.setEnabled(false);
						btnUploadConclusion.setVisibility(View.GONE);
						
						tvTopicDetailEndvote.setVisibility(View.VISIBLE);
						
						tvTopicAvrVote.setVisibility(View.VISIBLE);
					}
					if (1 == authority_vote_sum){
						llTpConclusion.setVisibility(View.VISIBLE);
						tvTopicConclusion.setEnabled(false);
						btnUploadConclusion.setVisibility(View.GONE);
						
					}
						
				} else if (topicVoteState == 2) {
					llTpConclusion.setVisibility(View.VISIBLE);
					tvTopicConclusion.setEnabled(false);
					btnUploadConclusion.setVisibility(View.GONE);
					if (1 == authority_vote_sum){
						tvTopicDetailVoteresult.setVisibility(View.VISIBLE);
						tvVotemanage.setVisibility(View.VISIBLE);
					}
					tvTopicDetailVoteresult.setVisibility(View.VISIBLE);

				}
			}
			tvTopicVirtual.setVisibility(View.VISIBLE);	
			if (1 == authority_control){
				ibtnTopicEnd.setVisibility(View.VISIBLE);
				llConfAppendix.setVisibility(View.VISIBLE);

			}	

			break;
		case Constants.TOPIC_CTRL_STATE_ENDED:
			if (topicNeedVote == 1){
					tvTopicDetailVoteresult.setVisibility(View.VISIBLE);
			}
			llTpConclusion.setVisibility(View.VISIBLE);
			tvTopicConclusion.setEnabled(false);
			btnUploadConclusion.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}	
	
	public void  clickTimeupdate(View view){
		IntentUtil.startActivity(this,TopicUpdatetimeActivity.class);
	}
	
	public void clickUpdateConclusion(View view){
		if (tvTopicConclusion.getText().length() < 1){
			Toast.makeText(TopicDetailActivity.this, getString(R.string.error_no_topic_conclusion), Toast.LENGTH_SHORT).show();
		}
		
		String url="MeetingManage/mobile/doTopicConcusion.action?topicNo="+topicId+"&conclusion=" + tvTopicConclusion.getText();

		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				String response = error.getMessage();
				System.out.println("wangting, fail response="+response);
				Toast.makeText(TopicDetailActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				// Toast.makeText(TopicSelectApplicatorActivity.this, new String(response)+"wangting"+response.toString(), 100).show();
				if (response.contains("用户未登陆")){
					Toast.makeText(TopicDetailActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				if(response.contains("success")){
					hasConcluded = 1;
					updateUi();
					Toast.makeText(TopicDetailActivity.this, "上传成功", 100).show();
				}else{
					Toast.makeText(TopicDetailActivity.this, "上传失败", 100).show();
				}			
			}
		});			
	}
	
	public void  clickStart(View view){
		//IntentUtil.startActivity(this,TopicAddOneActivity.class);

		String url="MeetingManage/mobile/doManageProc.action?procId="+topicId+"&action=1";

		System.out.println("wangting:"+url);
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				String response = error.getMessage();
				System.out.println("wangting, fail response="+response);
				Toast.makeText(TopicDetailActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				System.out.println("wangting, success response="+response);
				// Toast.makeText(TopicSelectApplicatorActivity.this, new String(response)+"wangting"+response.toString(), 100).show();
				if (response.contains("用户未登陆")){
					Toast.makeText(TopicDetailActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				if(response.contains("true")){
					topicControlState = Constants.TOPIC_CTRL_STATE_STARTED;
					
					
					hideAllController();
					getTopicDetail(topicId);
					
					Toast.makeText(TopicDetailActivity.this, "议题开启成功", 100).show();
				}else{
					String msg = "";
					try {
						msg = new JSONObject(response)
								.getString("msg");
					}catch(Exception e){
						Toast.makeText(TopicDetailActivity.this, "议题开启失败", 100).show();
					}
					Toast.makeText(TopicDetailActivity.this, msg, 100).show();
				}			
			}
		});			
	}
	
	public void clickAvrVote(View view){
		Intent data=new Intent();  
        data.putExtra(Constants.ID, topnoId);  
        IntentUtil.startActivityForResult(TopicDetailActivity.this, TopicAvrVoteActivity.class, Constants.CODE_AVR_VOTE ,data);		
	}
	
	public void  clickDianming(View view){
		//IntentUtil.startActivity(this,TopicUnArrivalActivity.class);
		Intent data=new Intent();  
        data.putExtra(Constants.ID, topnoId);  
        IntentUtil.startActivityForResult(TopicDetailActivity.this, TopicCallOverHistoryActivity.class, Constants.CODE_MENTION ,data);
	}
	
	
	public void  clickStartvote(View view){
		//IntentUtil.startActivity(this,VoteWillActivity.class);

		String url="MeetingManage/mobile/doAdminVoteMeetingContr.action?meetingProcId="+topicId+"&type=1";

		System.out.println("wangting:"+url);
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				String response = error.getMessage();
				System.out.println("wangting, fail response="+response);
				Toast.makeText(TopicDetailActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				System.out.println("wangting, success response="+response);
				// Toast.makeText(TopicSelectApplicatorActivity.this, new String(response)+"wangting"+response.toString(), 100).show();
				if (response.contains("用户未登陆")){
					Toast.makeText(TopicDetailActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				if(response.contains("true")){
					topicVoteState = 1;
					updateUi();
					Toast.makeText(TopicDetailActivity.this, "开启投票成功", 100).show();
					
				}else{
					String msg = "";
					try {
						msg = new JSONObject(response)
								.getString("msg");
					}catch(Exception e){
						Toast.makeText(TopicDetailActivity.this, "开启投票失败", 100).show();
					}
					Toast.makeText(TopicDetailActivity.this, msg, 100).show();
				}			
			}
		});			
	
	}
	public void  clickDoVote(View view){
		Intent intent = new Intent();
		intent.putExtra(Constants.ID, topicId);
		IntentUtil.startActivity(this, VoteWillActivity.class, intent);
		//IntentUtil.startActivity(this,VoteWillActivity.class);
	
	}
	public void  clickEndvote(View view){
		//IntentUtil.startActivity(this,VoteWillActivity.class);
		String url="MeetingManage/mobile/doAdminVoteMeetingContr.action?meetingProcId="+topicId+"&type=2";

		System.out.println("wangting:"+url);
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				String response = error.getMessage();
				System.out.println("wangting, fail response="+response);
				Toast.makeText(TopicDetailActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				System.out.println("wangting, success response="+response);
				// Toast.makeText(TopicSelectApplicatorActivity.this, new String(response)+"wangting"+response.toString(), 100).show();
				if (response.contains("用户未登陆")){
					Toast.makeText(TopicDetailActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				if(response.contains("true")){
					topicVoteState = 2;
					updateUi();
					Toast.makeText(TopicDetailActivity.this, "结束投票成功", 100).show();
				}else{
					String msg = "";
					try {
						msg = new JSONObject(response)
								.getString("msg");
					}catch(Exception e){
						Toast.makeText(TopicDetailActivity.this, "结束投票失败", 100).show();
					}
					Toast.makeText(TopicDetailActivity.this, msg, 100).show();
				}			
			}
		});			
	
	}
	public void  clickVoteresult(View view){
		Intent intent = new Intent();
		intent.putExtra(Constants.ID, topicId);
		IntentUtil.startActivity(this, VoteResultActivity.class, intent);
		//IntentUtil.startActivity(this,VoteResultActivity.class);
	}	
	
	
	
	public void  clickVotemanager(View view){
		IntentUtil.startActivity(this,VoteManagementActivity.class);
	}
	
	public void  clickVoteacticity(View view){
		Intent intent = new Intent();
		intent.putExtra(Constants.ID, topnoId);
		intent.putExtra(Constants.VOTE_TYPE, voteType);
		//IntentUtil.startActivity(this, TopicAdminVoteAcitivity.class, intent);
		IntentUtil.startActivity(this, TopicAdminControlVoteActivity.class, intent);
		
		//IntentUtil.startActivity(this,VoteAcitivity.class);
	}
	
	public void  clickMeetingCall(View view){
		String url="MeetingManage/mobile/joinInRoom.action?topicno="+topicId;
		System.out.println("wangting:"+url);
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler(){
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				String response = error.getMessage();
				System.out.println("wangting, fail response="+response);
				Toast.makeText(TopicDetailActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				System.out.println("wangting, success response="+response);	
				
				if (response.contains("用户未登陆")){
					Toast.makeText(TopicDetailActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				if(response.contains("true")){
					Toast.makeText(TopicDetailActivity.this, "申请成功", 100).show();
				}else{
					Toast.makeText(TopicDetailActivity.this, "申请失败", 100).show();					
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
	
	
	
	
	public void  clickEnd(View view){
		//IntentUtil.startActivity(this,VoteManagementActivity.class);
		loadingdiag.show();  

		String url="MeetingManage/mobile/doManageProc.action?procId="+topicId+"&action=0";

		System.out.println("wangting:"+url);
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();
				String response = error.getMessage();
				System.out.println("wangting, fail response="+response);
				Toast.makeText(TopicDetailActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				loadingdiag.hide();
				String response = new String(body);
				System.out.println("wangting, success response="+response);
				// Toast.makeText(TopicSelectApplicatorActivity.this, new String(response)+"wangting"+response.toString(), 100).show();
				if (response.contains("用户未登陆")){
					Toast.makeText(TopicDetailActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				if(response.contains("true")){
					topicControlState = Constants.TOPIC_CTRL_STATE_ENDED;
					updateUi();
					Toast.makeText(TopicDetailActivity.this, "议题结束成功", 100).show();
				}else{
					String msg = "";
					try {
						msg = new JSONObject(response)
								.getString("msg");
					}catch(Exception e){
						Toast.makeText(TopicDetailActivity.this, "议题结束失败", 100).show();
					}
					Toast.makeText(TopicDetailActivity.this, msg, 100).show();
				}			
			}
		});		
	}
	

	public void clickUpLoadFile(View view){
		
		int count = 0;
		for (UpLoadBundle b: uploadList){
			if (b.uploadState <= 0){
				count ++;
			}
		}
		
		if (count == 0){
			Toast.makeText(TopicDetailActivity.this,
					getString(R.string.error_no_document), Toast.LENGTH_SHORT).show();
			return;
		}
		upLoadIndex = 0;
		
		loadingdiag.setText(getString(R.string.uploading));
		loadingdiag.show();
		upLoadFile();
	}
	
	private void upLoadFile() {
		// TODO Auto-generated method stub
		
		updateAppendixText();
		
		if ( uploadList.get(upLoadIndex).uploadState > 0){
			upLoadIndex++;
			if (upLoadIndex > uploadList.size() - 1){
				// 上传完毕
				upLoadIndex = 0;
				updateAppendixText();
				loadingdiag.hide();
				Toast.makeText(TopicDetailActivity.this,
						getString(R.string.result_success), Toast.LENGTH_SHORT).show();		
				return;
			} else {
				upLoadFile();
				return;
			}
			
		}
		
		loadingdiag.setText(getString(R.string.uploading) + ":  " 
				+ uploadList.get(upLoadIndex).filename);
		loadingdiag.show();

		String url = "MeetingManage/mobile/MobileUpLoadFileServlet?no=";
		url += topicId;
		RequestParams params = new RequestParams();
		File file = new File(uploadList.get(upLoadIndex).filepath);
		try {
			params.put("filedata", file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Toast.makeText(TopicDetailActivity.this,
					getString(R.string.error_fileabout),
					Toast.LENGTH_SHORT).show();
			return;
		}

		
		MeetingSystemClient.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();
				Toast.makeText(TopicDetailActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onSuccess(int stautsCode, Header[] arg1, byte[] body) {
				String result = new String(body);
				if (result.contains("用户未登陆")){
					Toast.makeText(TopicDetailActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				if (result.contains("success")) {
					uploadList.get(upLoadIndex).uploadState = 1;
					upLoadFile();
				} else {
					loadingdiag.hide();
					if (result.contains("errorMessage")){
						try {
							JSONObject jsob =  new JSONObject(result);
							String msg = jsob.getString("errorMessage");
							Toast.makeText(TopicDetailActivity.this,
									msg,
									Toast.LENGTH_SHORT).show();
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Toast.makeText(TopicDetailActivity.this,
									getString(R.string.error_upload_failed),
									Toast.LENGTH_SHORT).show();
						}
						
					}else{
						Toast.makeText(TopicDetailActivity.this,
							getString(R.string.error_upload_failed),
							Toast.LENGTH_SHORT).show();
					}
					
				}
			}
		});
		
	}

	public void clickSelectMediaFile(View view){
	    Intent intent = new Intent(Intent.ACTION_GET_CONTENT); 
	    intent.setType("*/*"); 
	    intent.addCategory(Intent.CATEGORY_OPENABLE);
	 
	    try {
	        startActivityForResult( Intent.createChooser(intent, "选择文件"), Constants.CODE_FILE_SELECT);
	    } catch (android.content.ActivityNotFoundException ex) {
	        Toast.makeText(this, "无匹配的文件选择器",  Toast.LENGTH_SHORT).show();
	    }
	}
	
	public void clickSelectOrdinaryFile(View view){
    	if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		    startActivityForResult(new Intent(this , FileChooserActivity.class) , Constants.CODE_FILE_SELECT_COMMON);
    	else
    		Toast.makeText(this, getText(R.string.sdcard_unmonted_hint),  Toast.LENGTH_SHORT).show();		
	}
	
	public void clickClear(View view){
		
		for (int i = uploadList.size() - 1; i >= 0; i--){
			if (uploadList.get(i).uploadState <= 0){
				uploadList.remove(i);
			}
		}
		updateAppendixText();

	}
	
	public void onActivityResult(int requestCode , int resultCode , Intent data){
		
		if(resultCode == RESULT_CANCELED){
			return ;
		}
		if(resultCode == RESULT_OK && requestCode == Constants.CODE_FILE_SELECT_COMMON){
			//获取路径名
			String filePath = data.getStringExtra(Constants.EXTRA_FILE_CHOOSER);
			if(filePath != null){
				File f = new File(filePath);
				if (f.length() > Constants.MAX_UPLOAD_FILE_SIZE){
					Toast.makeText(TopicDetailActivity.this,
							getString(R.string.error_filesize_toolarge),
							Toast.LENGTH_SHORT).show();
					return;
				}
				
				UpLoadBundle b = new UpLoadBundle();
				String fileName = filePath.substring(filePath.lastIndexOf("/")+1); 
				b.filename = fileName;
				b.filepath = filePath;
				b.uploadState = 0;
				addToUploadList(b);
				updateAppendixText();
				
			}
		}
		if(resultCode == RESULT_OK && requestCode == Constants.CODE_FILE_SELECT){
			//获取路径名
			Uri uri = data.getData();
			String filePath = getPath(this, uri);
			String fileName = filePath.substring(filePath.lastIndexOf("/")+1); 
			
			File f = new File(filePath);
			if (f.length() > Constants.MAX_UPLOAD_FILE_SIZE){
				Toast.makeText(TopicDetailActivity.this,
						getString(R.string.error_filesize_toolarge),
						Toast.LENGTH_SHORT).show();
				return;
			}
			
			UpLoadBundle b = new UpLoadBundle();
			b.filename = fileName;
			b.filepath = filePath;
			b.uploadState = 0;
			addToUploadList(b);
			updateAppendixText();
		}
		
        if(Constants.CODE_MENTION == requestCode)  
        {  
			hasMentioned = 1;
			updateUi();
			/*
        	if (Constants.OK == resultCode && null != data){
        		String result=data.getExtras().getString(Constants.RESULT);
        		if (null != result && "success".equals(result)){
        			hasMentioned = 1;
        			updateUi();
        		}
        	}*/
            
        }
	}
	
	private String getPath(Context context, Uri uri) {
		 
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;
 
            try {
                cursor = context.getContentResolver().query(uri, projection,null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }
 
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
 
        return null;
    }
	
	private void addToUploadList(UpLoadBundle ub) {
		boolean existed = false;
		for (UpLoadBundle b : uploadList){
			if (b.filepath.equals(ub.filepath)){
				existed = true;
				break;
			}
		}
		if (!existed){
			uploadList.add(ub);
		}
	}

	private void updateAppendixText() {
		String temp = getString(R.string.to_upload);
		String temp2 = getString(R.string.already_uploaded);
		for (UpLoadBundle b : uploadList){
			if (b.uploadState <= 0){
				temp += b.filename;
				temp += "    ";
			} else {
				temp2 += b.filename;
				temp2 += "    ";
			}
		}
		
		tvAppendix.setText(temp);
		tvAppendixUploaded.setText(temp2);
	}

	class UpLoadBundle{
		String filename;
		String filepath;
		int uploadState = 0;
	}
}
