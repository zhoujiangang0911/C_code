package cn.wislight.publicservice.ui.user.goverment;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import cn.wislight.publicservice.R;
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.entity.GovermentAffair;
import cn.wislight.publicservice.ui.commercialtenant.BaiduMapShowPositionActivity;
import cn.wislight.publicservice.util.Constants;
import cn.wislight.publicservice.util.PublicServiceClient;
import cn.wislight.publicservice.util.StringToChange;

/**
 * 完结详细页界面
 * @author Administrator
 *
 */
public class UserEndGovermentAffairDetailActivity extends BaseActivity implements OnClickListener{
	private TextView txtGovermentaffairSendtime;
	private TextView txtGovermentaffairContent;	
	private TextView txtGovermentaffairReceiverName;
	private TextView txtGovermentaffairState;
	private TextView txtGovermentaffairSummary;
	private TextView txtGovermentaffairScore;
	private TextView txtSetCommonlyServant;
	private Button btnGovermentaffairSubmit;
	
	
	private TextView txtBusinessaffairAddress;	
	private TextView txtBusinessaffairPrice;
	private ImageView txtBusinessaffairVoicecontent;
	private TextView txtGovermentaffairReceiverVOIP;
	private TextView txtGovermentaffairReceiverPhone;	
	private TextView txtGovermentAffairProcedureLog;	
	private Button btnGovermentaffairAccept;
	private Button btnGovermentaffairComplete;
	private Button btnGovermentaffairEnd;
	
	private LinearLayout govermentaffairAcceptLayout;
	private LinearLayout govermentaffairCompleteLayout;
	private LinearLayout govermentaffairEndLayout;

	private String govermentAffairId;
	private String receiverId;
	
	private String longitude;
	private String latitude;
	private String addressName;
	
	private String voiceContentId;
	@Override
	public void setUpView() {
		setContentView(R.layout.activity_user_end_govermentaffair_detail);
		
		txtGovermentaffairSendtime = (TextView) findViewById(R.id.govermentaffairSendtime);
		txtGovermentaffairContent = (TextView) findViewById(R.id.govermentaffairContent);
		txtGovermentaffairReceiverName = (TextView) findViewById(R.id.govermentaffairReceiverName);
		txtGovermentaffairState = (TextView) findViewById(R.id.govermentaffairState);
		txtGovermentaffairSummary = (TextView) findViewById(R.id.govermentaffairSummary);
		txtGovermentaffairScore = (TextView) findViewById(R.id.govermentaffairScore);
		txtSetCommonlyServant = (TextView) findViewById(R.id.setCommonlyServant);
		
		btnGovermentaffairSubmit = (Button) findViewById(R.id.govermentaffairSubmit);	
		
		govermentAffairId = getIntent().getStringExtra(Constants.ID);
		fillData(govermentAffairId);

	}

	@Override
	public void setListener() {
		txtSetCommonlyServant.setOnClickListener(this);
		btnGovermentaffairSubmit.setOnClickListener(this);
	}
	
	public void fillData(String govermentAffairId){		
		String url = "publicservice/govermentaffair_detail.htm?json=true&govermentaffair.id="+govermentAffairId;
		System.out.println("wangting:"+url);
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler() {
			

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				// loadingdiag.hide();
				Toast.makeText(UserEndGovermentAffairDetailActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				System.out.println("wangting: response="+response);
				try {
					JSONObject jsonObject = new JSONObject(response);
					String result = jsonObject.getString("result");
					if (result.equals("success")){
						JSONObject govermentaffair = jsonObject.getJSONObject("govermentaffair");	
						
						txtGovermentaffairSendtime.setText(StringToChange.toNoT(govermentaffair.getString("sendtime")));
						txtGovermentaffairContent.setText(govermentaffair.getString("content"));
						
						if(!govermentaffair.isNull("receiver")){
							JSONObject receiver = govermentaffair.getJSONObject("receiver");
							if(!receiver.isNull("username")){
								txtGovermentaffairReceiverName.setText(receiver.getString("username"));
							}else{
								txtGovermentaffairReceiverName.setText(receiver.getString("loginname"));
							}
							receiverId = receiver.getString("id");
							/*
							if(!receiver.isNull("voip")){
								txtGovermentaffairReceiverVOIP.setText(receiver.getString("voip"));
							}
							if(!receiver.isNull("phone")){
								txtGovermentaffairReceiverPhone.setText(receiver.getString("phone"));
							}
							*/
						}
	
						String state = govermentaffair.getString("state");
												
						
						
						if(GovermentAffair.STATE.DRAFT.equals(state)){
							txtGovermentaffairState.setText("待编辑");
							
						}else if(GovermentAffair.STATE.DAIJIESHOU.equals(state)){
							txtGovermentaffairState.setText("待接受");
							
						} else if(GovermentAffair.STATE.CHULIZHONG.equals(state)){
							txtGovermentaffairState.setText("处理中");
							
						} else if(GovermentAffair.STATE.YIWANJIE.equals(state)){
							txtGovermentaffairState.setText("已完结");
							btnGovermentaffairSubmit.setVisibility(View.VISIBLE);
						} else if(GovermentAffair.STATE.YIPINGJIA.equals(state)){
							txtGovermentaffairState.setText("已评价");
							
						} else if(GovermentAffair.STATE.YIGUIDANG.equals(state)){
							txtGovermentaffairState.setText("已归档");							
						} 
						
											
					}else {
						Toast.makeText(UserEndGovermentAffairDetailActivity.this,"初始化失败", Toast.LENGTH_SHORT)
								.show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
					// loadingdiag.hide();
					Toast.makeText(UserEndGovermentAffairDetailActivity.this,
							getString(R.string.error_network),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	
	}

	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.govermentaffairSubmit:
			evaluateGovermentAffair(govermentAffairId);			
			break;
		case R.id.setCommonlyServant:
			Toast.makeText(UserEndGovermentAffairDetailActivity.this,
					"设为专职代办", Toast.LENGTH_SHORT).show();
			setCommonlyServant(govermentAffairId);
			break;
			
			
		default:
				break;
		}
	}
		
	public void evaluateGovermentAffair(String govermentAffairId){		
		String score = txtGovermentaffairScore.getText().toString();
		String summary = txtGovermentaffairSummary.getText().toString();;
		String evaluateVoiceId = "";
		String url = "publicservice/govermentaffair_evaluate.htm?json=true&govermentaffair.id="+govermentAffairId+"&govermentaffair.score="+score
				+"&govermentaffair.summary="+summary+"&evaluateVoiceId="+evaluateVoiceId;
		System.out.println("wangting:"+url);
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler() {		

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				// loadingdiag.hide();
				Toast.makeText(UserEndGovermentAffairDetailActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				try {
					JSONObject jsonObject = new JSONObject(response);
					String result = jsonObject.getString("result");
					if (result.equals("success")){
						Toast.makeText(UserEndGovermentAffairDetailActivity.this,"评价事务成功", Toast.LENGTH_SHORT)
						.show();
						finish();				
					}else {
						Toast.makeText(UserEndGovermentAffairDetailActivity.this,"评价事务失败", Toast.LENGTH_SHORT)
								.show();
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
					// loadingdiag.hide();
					Toast.makeText(UserEndGovermentAffairDetailActivity.this,
							getString(R.string.error_network),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	
	}
	public void setCommonlyServant(String govermentAffairId){		
		if(receiverId == null || receiverId.trim().length()<1){
			Toast.makeText(UserEndGovermentAffairDetailActivity.this,"无处理人", Toast.LENGTH_SHORT).show();
			return;
		}
		String url = "publicservice/commonlyperson_create.htm?json=true&commonlyperson.personid="+receiverId;
		System.out.println("wangting:"+url);
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler() {		

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				// loadingdiag.hide();
				Toast.makeText(UserEndGovermentAffairDetailActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				try {
					JSONObject jsonObject = new JSONObject(response);
					String result = jsonObject.getString("result");
					if (result.equals("success")){
						Toast.makeText(UserEndGovermentAffairDetailActivity.this,"设置成功", Toast.LENGTH_SHORT)
						.show();										
					}else if (result.equals("exist")){
						Toast.makeText(UserEndGovermentAffairDetailActivity.this,"已设置", Toast.LENGTH_SHORT)
						.show();										
					}else{
						Toast.makeText(UserEndGovermentAffairDetailActivity.this,"设置失败", Toast.LENGTH_SHORT)
								.show();
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
					// loadingdiag.hide();
					Toast.makeText(UserEndGovermentAffairDetailActivity.this,
							getString(R.string.error_network),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	
	}
}
