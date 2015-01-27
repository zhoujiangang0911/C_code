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
public class TopicVoteCtrlDetailActivity extends BaseActivity {
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
		tvTopicDetailStartvote, 
		tvTopicDetailEndvote, 
		tvTopicDetailVoteresult,
		tvVotemanage;
	
	private TextView tvTopicAttribute;

	
	private TextView tvTopicAvrVote;
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
		
		tvTopicDetailStartvote = (TextView)findViewById(R.id.topic_detail_startvote);
		tvTopicDetailEndvote = (TextView)findViewById(R.id.topic_detail_endvote);
		tvTopicDetailVoteresult = (TextView)findViewById(R.id.topic_detail_voteresult);
		tvVotemanage = (TextView)findViewById(R.id.vote_votemanage);
		tvTopicStaticcer = (TextView)findViewById(R.id.tv_topic_staticcer);
		tvTopicVoteCtrl = (TextView)findViewById(R.id.tv_topic_vote_ctrl);
		tvTopicCtrl = (TextView)findViewById(R.id.tv_topic_ctrl);
		
		tvTopicAvrVote = (TextView)findViewById(R.id.topic_avr_vote);
		tvTopicConclusion = (TextView)findViewById(R.id.topic_conclusion);
		llTpConclusion = (LinearLayout)findViewById(R.id.ll_topic_conclusion);
		
		tvTopicAttribute = (TextView)findViewById(R.id.tv_topic_attribute);
		

		
		hideAllController();
		getTopicDetail(topicId);
	}

	private void hideAllController() {
		// TODO Auto-generated method stub
		tvTopicDetailStartvote.setVisibility(View.GONE);
		tvTopicDetailEndvote.setVisibility(View.GONE);
		tvTopicDetailVoteresult.setVisibility(View.GONE);
		tvVotemanage.setVisibility(View.GONE);
		llTpConclusion.setVisibility(View.GONE);
		tvTopicAvrVote.setVisibility(View.GONE);

	}

	@Override
	public void setupView() {
		setContentView(R.layout.topic_vote_control_detail);
	}
	public void clickAttachmentList(View view){
		Intent data=new Intent();  
        data.putExtra(Constants.ID, topicId);
        
        IntentUtil.startActivity(TopicVoteCtrlDetailActivity.this, AttachmentListActivity.class,  data);
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
				Toast.makeText(TopicVoteCtrlDetailActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(int stautsCode, Header[] arg1, byte[] body) {
				String response = new String(body);
				loadingdiag.hide();
				if (response.contains("用户未登陆")){
					Toast.makeText(TopicVoteCtrlDetailActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				System.out.println("wangting:"+response);
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
					if (null != topic_source && topic_source.contains("inmeetcreate")){
							tvTopicAttribute.setText(R.string.topic_Attribute_inmeetcreate);
						}else{
							tvTopicAttribute.setText(R.string.topic_Attribute_topicdb);
						}
					if ("true".equals(topicConcluded)){
						hasConcluded = 1;
					} else {
						hasConcluded = 0;
					}
					
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
					
					updateUi();	
				}catch (Exception e){
					e.printStackTrace();
					Toast.makeText(TopicVoteCtrlDetailActivity.this, getString(R.string.error_dataabout), Toast.LENGTH_SHORT).show();
					
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
			}
			break;
		case Constants.TOPIC_CTRL_STATE_STARTED:
			if (1 == authority_control){

				if (0 == hasConcluded){
					llTpConclusion.setVisibility(View.VISIBLE);
					tvTopicConclusion.setEnabled(false);
				} else {
					llTpConclusion.setVisibility(View.VISIBLE);
					tvTopicConclusion.setEnabled(false);
				}
			}
			if (topicNeedVote == 1){
				if (topicVoteState == 0){
					if (1 == authority_vote_ctrl){
						if (1 == hasConcluded){
							llTpConclusion.setVisibility(View.VISIBLE);
							tvTopicConclusion.setEnabled(false);
							
							tvTopicDetailStartvote.setVisibility(View.VISIBLE);
						}
					} 
						
				} else if (topicVoteState == 1) {
					
					if (1 == authority_vote_ctrl){
						llTpConclusion.setVisibility(View.VISIBLE);
						tvTopicConclusion.setEnabled(false);
						
						tvTopicDetailEndvote.setVisibility(View.VISIBLE);
						
						tvTopicAvrVote.setVisibility(View.VISIBLE);
					}
					if (1 == authority_vote_sum){
						llTpConclusion.setVisibility(View.VISIBLE);
						tvTopicConclusion.setEnabled(false);
						
					}
						
				} else if (topicVoteState == 2) {
					llTpConclusion.setVisibility(View.VISIBLE);
					tvTopicConclusion.setEnabled(false);
					if (1 == authority_vote_sum){
						tvTopicDetailVoteresult.setVisibility(View.VISIBLE);
						tvVotemanage.setVisibility(View.VISIBLE);
					}
					tvTopicDetailVoteresult.setVisibility(View.VISIBLE);

				}
			}
			if (1 == authority_control){

			}	

			break;
		case Constants.TOPIC_CTRL_STATE_ENDED:
			if (topicNeedVote == 1){
					tvTopicDetailVoteresult.setVisibility(View.VISIBLE);
			}
			llTpConclusion.setVisibility(View.VISIBLE);
			tvTopicConclusion.setEnabled(false);
			break;
		default:
			break;
		}
	}	
	
	public void  clickTimeupdate(View view){
		IntentUtil.startActivity(this,TopicUpdatetimeActivity.class);
	}
	
	
	
	public void clickAvrVote(View view){
		Intent data=new Intent();  
        data.putExtra(Constants.ID, topnoId);  
        IntentUtil.startActivityForResult(TopicVoteCtrlDetailActivity.this, TopicAvrVoteActivity.class, Constants.CODE_MENTION ,data);		
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
				Toast.makeText(TopicVoteCtrlDetailActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				System.out.println("wangting, success response="+response);
				// Toast.makeText(TopicSelectApplicatorActivity.this, new String(response)+"wangting"+response.toString(), 100).show();
				if (response.contains("用户未登陆")){
					Toast.makeText(TopicVoteCtrlDetailActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				if(response.contains("true")){
					topicVoteState = 1;
					updateUi();
					Toast.makeText(TopicVoteCtrlDetailActivity.this, "开启投票成功", 100).show();
					
				}else{
					String msg = "";
					try {
						msg = new JSONObject(response)
								.getString("msg");
					}catch(Exception e){
						Toast.makeText(TopicVoteCtrlDetailActivity.this, "开启投票失败", 100).show();
					}
					Toast.makeText(TopicVoteCtrlDetailActivity.this, msg, 100).show();
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
				Toast.makeText(TopicVoteCtrlDetailActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				System.out.println("wangting, success response="+response);
				// Toast.makeText(TopicSelectApplicatorActivity.this, new String(response)+"wangting"+response.toString(), 100).show();
				if (response.contains("用户未登陆")){
					Toast.makeText(TopicVoteCtrlDetailActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				if(response.contains("true")){
					topicVoteState = 2;
					updateUi();
					Toast.makeText(TopicVoteCtrlDetailActivity.this, "结束投票成功", 100).show();
				}else{
					String msg = "";
					try {
						msg = new JSONObject(response)
								.getString("msg");
					}catch(Exception e){
						Toast.makeText(TopicVoteCtrlDetailActivity.this, "结束投票失败", 100).show();
					}
					Toast.makeText(TopicVoteCtrlDetailActivity.this, msg, 100).show();
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
	}
}
