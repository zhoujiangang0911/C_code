package cn.wislight.meetingsystem.ui.vote;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.TextView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.MeetingSystemClient;

/**
 * @author Administrator 我的表决
 */
public class VoteResultActivity extends BaseActivity {
	private String topicId;
	private TextView etAgree;
	private TextView etDisagree;
	private TextView etGiveup;
	private TextView tvVotePeopleNum;

	private TextView etPhoneAgree;
	private TextView etPhoneDisagree;
	private TextView etPhoneGiveup;

	private TextView etSceneAgree;
	private TextView etSceneDisagree;
	private TextView etSceneGiveup;

	private TextView etPcAgree;
	private TextView etPcDisagree;
	private TextView etPcGiveup;

	private TextView etAvrAgree;
	private TextView etAvrDisagree;
	private TextView etAvrGiveup;

	@Override
	public void initView() {
		topicId = this.getIntent().getStringExtra(Constants.ID);
		String url = "MeetingManage/mobile/getTopicVoteResult.action?&meetingProcId="
				+ topicId;
		System.out.println("wangting:" + url);
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				String response = new String(body);
				System.out.println("wangting, success response=" + response);
				Toast.makeText(VoteResultActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();

			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				System.out.println("wangting, success response=" + response);
				// Toast.makeText(TopicSelectApplicatorActivity.this, new
				// String(response)+"wangting"+response.toString(), 100).show();
				
				if (response.contains("用户未登陆")){
					Toast.makeText(VoteResultActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				try {
					JSONObject json = new JSONObject(response);
					String agrcount = json.getString("agrcount");
					String discount = json.getString("discount");
					String abscount = json.getString("abscount");
					etAgree.setText(agrcount);
					etDisagree.setText(discount);
					etGiveup.setText(abscount);
					
					String perCount = json.getString("percount");
					tvVotePeopleNum.setText(perCount);
					
					etPhoneAgree.setText(Integer.parseInt(json.getString("smsAgree")) 
							+ Integer.parseInt(json.getString("phoneClientAgree")) + "");
					etPhoneDisagree.setText(Integer.parseInt(json.getString("smsDisAgree"))
							+ Integer.parseInt(json.getString("phoneClientDisAgree")) + "");
					etPhoneGiveup.setText(Integer.parseInt(json.getString("smsGiveUp")) + 
							Integer.parseInt(json.getString("smsGiveUp")) + "");
					
					etSceneAgree.setText(json.getString("sceneAgree"));
					etSceneDisagree.setText(json.getString("sceneDisAgree"));
					etSceneGiveup.setText(json.getString("sceneGiveUp"));
					 
					etPcAgree.setText(json.getString("pcClientAgree"));
					etPcDisagree.setText(json.getString("pcClientDisAgree"));
					etPcGiveup.setText(json.getString("pcClientGiveUp"));
					
					etAvrAgree.setText(json.getString("avrAgree"));
					etAvrDisagree.setText(json.getString("avrDisAgree"));
					etAvrGiveup.setText(json.getString("avrGiveUp"));
					
				} catch (JSONException e) {
					Toast.makeText(VoteResultActivity.this,
							getString(R.string.error_dataabout), Toast.LENGTH_SHORT)
							.show();
					e.printStackTrace();
				}
			}
		});

	}

	@Override
	public void setupView() {
		setContentView(R.layout.vote_result);
		etAgree = (TextView) findViewById(R.id.vote_result_agreee);
		etDisagree = (TextView) findViewById(R.id.vote_result_disagreee);
		etGiveup = (TextView) findViewById(R.id.vote_result_giveup);
		//
		etPhoneAgree = (TextView) findViewById(R.id.vote_result_phone_agreee);
		etPhoneDisagree = (TextView) findViewById(R.id.vote_result_phone_disagreee);
		etPhoneGiveup = (TextView) findViewById(R.id.vote_result_phone_giveup);

		etSceneAgree = (TextView) findViewById(R.id.vote_result_scene_agreee);
		etSceneDisagree = (TextView) findViewById(R.id.vote_result_scene_disagreee);
		etSceneGiveup = (TextView) findViewById(R.id.vote_result_scene_giveup);

		etPcAgree = (TextView) findViewById(R.id.vote_result_pc_agreee);
		etPcDisagree = (TextView) findViewById(R.id.vote_result_pc_disagreee);
		etPcGiveup = (TextView) findViewById(R.id.vote_result_pc_giveup);

		etAvrAgree = (TextView) findViewById(R.id.vote_result_avr_agreee);
		etAvrDisagree = (TextView) findViewById(R.id.vote_result_avr_disagreee);
		etAvrGiveup = (TextView) findViewById(R.id.vote_result_avr_giveup);

		tvVotePeopleNum = (TextView) findViewById(R.id.tv_vote_people_num);
	}

}
