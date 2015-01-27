package cn.wislight.publicservice.ui.user.goverment;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpResponseHandler;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.wislight.publicservice.R;
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.entity.GovermentAffair;
import cn.wislight.publicservice.ui.commercialtenant.BaiduMapShowPositionActivity;
import cn.wislight.publicservice.util.Constants;
import cn.wislight.publicservice.util.PublicServiceClient;
import cn.wislight.publicservice.util.StringToChange;

/**
 * 事务详情页面
 * @author Administrator
 *
 */
public class UserHandlingGovermentAffairDetailActivity extends BaseActivity implements OnClickListener{
	private TextView txtGovermentaffairReceiverName;
	private TextView txtBusinessaffairSendtime;
	private TextView txtBusinessaffairAddress;
	private TextView txtBusinessaffairContent;
	private TextView txtBusinessaffairPrice;
	private ImageView txtBusinessaffairVoicecontent;
	private TextView txtGovermentaffairReceiverVOIP;
	private TextView txtGovermentaffairReceiverPhone;
	private TextView txtGovermentaffairState;
	
	private TextView txtGovermentAffairProcedureLog;
	
	private Button btnGovermentaffairAccept;
	private Button btnGovermentaffairComplete;
	private Button btnGovermentaffairEnd;
	
	private ImageButton imagebtnCallPhone;
	private ImageButton imagebtnCallvoip;
	
	
	private LinearLayout govermentaffairAcceptLayout;
	private LinearLayout govermentaffairCompleteLayout;
	private LinearLayout govermentaffairEndLayout;

	private String govermentAffairId;
	//private Bundle businessAffair;
	
	private String longitude;
	private String latitude;
	private String addressName;
	
	private String voiceContentId;
	@Override
	public void setUpView() {
		setContentView(R.layout.activity_user_detail_govermentaffair);
		
		txtGovermentaffairReceiverName = (TextView) findViewById(R.id.govermentaffairReceiverName);
		txtBusinessaffairSendtime = (TextView) findViewById(R.id.businessaffairSendtime);
		txtBusinessaffairAddress = (TextView) findViewById(R.id.businessaffairAddress);
		txtBusinessaffairContent = (TextView) findViewById(R.id.businessaffairContent);
		txtBusinessaffairPrice = (TextView) findViewById(R.id.businessaffairPrice);
		txtBusinessaffairVoicecontent = (ImageView) findViewById(R.id.businessaffairVoicecontent);
		txtGovermentaffairReceiverVOIP = (TextView) findViewById(R.id.tv_voipnumber);
		txtGovermentaffairReceiverPhone = (TextView) findViewById(R.id.tv_phonenumber);
		txtGovermentaffairState = (TextView) findViewById(R.id.govermentaffairState);
		txtGovermentAffairProcedureLog = (TextView) findViewById(R.id.govermentaffairProcedureLog);
		
		btnGovermentaffairAccept = (Button) findViewById(R.id.govermentaffairAccept);
		btnGovermentaffairComplete = (Button) findViewById(R.id.govermentaffairComplete);
		btnGovermentaffairEnd = (Button) findViewById(R.id.govermentaffairEnd);
		
		govermentaffairAcceptLayout = (LinearLayout) findViewById(R.id.govermentaffairAcceptLayout);
		govermentaffairCompleteLayout = (LinearLayout) findViewById(R.id.govermentaffairCompleteLayout);
		govermentaffairEndLayout = (LinearLayout) findViewById(R.id.govermentaffairEndLayout);
		
		govermentAffairId = getIntent().getStringExtra(Constants.ID);
		//businessAffair = getIntent().getBundleExtra("businessaffair");		
		
		imagebtnCallPhone = (ImageButton) findViewById(R.id.imagebtn_callphone);
		imagebtnCallvoip = (ImageButton) findViewById(R.id.imagebtn_callintentphone);
	
		imagebtnCallPhone.setOnClickListener(this);
		imagebtnCallvoip.setOnClickListener(this);
		
		txtBusinessaffairAddress.setOnClickListener(this);
		
		btnGovermentaffairAccept.setOnClickListener(this);
		btnGovermentaffairComplete.setOnClickListener(this);
		btnGovermentaffairEnd.setOnClickListener(this);
		
		fillData(govermentAffairId);
		fillProcedureData(govermentAffairId);
	}

	@Override
	public void setListener() {
		
	}
	
	public void fillData(String govermentAffairId){		
		String url = "publicservice/govermentaffair_detail.htm?json=true&govermentaffair.id="+govermentAffairId;
		System.out.println("wangting:"+url);
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler() {
			

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				// loadingdiag.hide();
				Toast.makeText(UserHandlingGovermentAffairDetailActivity.this,
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
						if(!govermentaffair.isNull("receiver")){
							JSONObject receiver = govermentaffair.getJSONObject("receiver");
							if(!receiver.isNull("username")){
								txtGovermentaffairReceiverName.setText(receiver.getString("username"));
							}else{
								txtGovermentaffairReceiverName.setText(receiver.getString("loginname"));
							}
							if(!receiver.isNull("voip")){
								txtGovermentaffairReceiverVOIP.setText(receiver.getString("voip"));
							}
							if(!receiver.isNull("phone")){
								txtGovermentaffairReceiverPhone.setText(receiver.getString("phone"));
							}
						}
						txtBusinessaffairContent.setText(govermentaffair.getString("content"));
						//txtBusinessaffairPrice.setText(govermentaffair.getString("price"));
						txtBusinessaffairSendtime.setText(StringToChange.toNoT(govermentaffair.getString("sendtime")));						
						txtBusinessaffairAddress.setText(govermentaffair.getJSONObject("address").getString("name"));	
						addressName = govermentaffair.getJSONObject("address").getString("name");
						longitude = govermentaffair.getJSONObject("address").getString("longitude");
						latitude = govermentaffair.getJSONObject("address").getString("latitude");
						//txtBusinessaffairVoicecontent.setText(govermentaffair.getString("voicecontentid"));
						voiceContentId = govermentaffair.getString("voicecontentid");
						//txtGovermentAffairProcedureLog.setText("无记录");
						
						String state = govermentaffair.getString("state");
						
						if(GovermentAffair.STATE.DRAFT.equals(state)){
							txtGovermentaffairState.setText("待编辑");
							//govermentaffairAcceptLayout.setVisibility(View.VISIBLE);
							
						}else if(GovermentAffair.STATE.DAIJIESHOU.equals(state)){
							txtGovermentaffairState.setText("待接受");
							//govermentaffairAcceptLayout.setVisibility(View.VISIBLE);
							
						} else if(GovermentAffair.STATE.CHULIZHONG.equals(state)){
							txtGovermentaffairState.setText("处理中");
							//govermentaffairCompleteLayout.setVisibility(View.VISIBLE);
							
						} else if(GovermentAffair.STATE.YIWANJIE.equals(state)){
							txtGovermentaffairState.setText("已完结");
							//govermentaffairEndLayout.setVisibility(View.VISIBLE);
							
						} else if(GovermentAffair.STATE.YIGUIDANG.equals(state)){
							txtGovermentaffairState.setText("已归档");
							//govermentaffairEndLayout.setVisibility(View.VISIBLE);
							
						} 
						
											
					}else {
						Toast.makeText(UserHandlingGovermentAffairDetailActivity.this,"初始化失败", Toast.LENGTH_SHORT)
								.show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
					// loadingdiag.hide();
					Toast.makeText(UserHandlingGovermentAffairDetailActivity.this,
							getString(R.string.error_network),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	
	}

	public void fillProcedureData(String govermentAffairId) {
		String url = "publicservice/govermentaffairprocedure_findByGovermentAffairId.htm?json=true&govermentaffairprocedure.govermentaffairid="
				+ govermentAffairId;
		System.out.println("wangting:" + url);
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				// loadingdiag.hide();
				Toast.makeText(UserHandlingGovermentAffairDetailActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				System.out.println("wangting: response=" + response);
				try {
					JSONArray jsonArray = new JSONArray(response);
					if (jsonArray.length() == 0) {
						txtGovermentAffairProcedureLog.setText("无记录");
					} else {
						String log = "";
						int length = jsonArray.length();
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObject = (JSONObject) jsonArray
									.get(i);
							// log += jsonObject.getString("王五");
							String loginname = jsonObject.getJSONObject("handler").getString("loginname");
							log += loginname;
							log += " ";
							log += jsonObject.getString("comment");
							log += "\n";
							log += jsonObject.getString("createtime");
							if (i < length - 1) {
								log += "\n";
							}
						}
						txtGovermentAffairProcedureLog.setText(StringToChange.toNoT(log));
					}
				} catch (JSONException e) {
					e.printStackTrace();
					// loadingdiag.hide();
					Toast.makeText(UserHandlingGovermentAffairDetailActivity.this,
							getString(R.string.error_network),
							Toast.LENGTH_SHORT).show();
				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.businessaffairAddress:
			Intent intent = new Intent(this, BaiduMapShowPositionActivity.class);
			intent.putExtra("addressName", addressName);
			intent.putExtra("longitude", longitude);
			intent.putExtra("latitude", latitude);
			this.startActivity(intent);
			break;
		case R.id.govermentaffairAccept:
			acceptGovermentAffair(govermentAffairId);			
			break;
		case R.id.govermentaffairComplete:
			completeGovermentAffair(govermentAffairId);			
			break;
		case R.id.govermentaffairEnd:
			endGovermentAffair(govermentAffairId);			
			break;
		case R.id.imagebtn_callphone:
			Intent intent2 = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+txtGovermentaffairReceiverPhone.getText()));
			startActivity(intent2);
			break;
		default:
				break;
		}
	}
	
	public void acceptGovermentAffair(String govermentAffairId){		
		String url = "publicservice/govermentaffair_accept.htm?json=true&govermentaffair.id="+govermentAffairId;
		System.out.println("wangting:"+url);
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler() {		

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				// loadingdiag.hide();
				Toast.makeText(UserHandlingGovermentAffairDetailActivity.this,
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
						Toast.makeText(UserHandlingGovermentAffairDetailActivity.this,"接受事务成功", Toast.LENGTH_SHORT)
						.show();											
					}else {
						Toast.makeText(UserHandlingGovermentAffairDetailActivity.this,"接受事务失败", Toast.LENGTH_SHORT)
								.show();
					}
					finish();

				} catch (JSONException e) {
					e.printStackTrace();
					// loadingdiag.hide();
					Toast.makeText(UserHandlingGovermentAffairDetailActivity.this,
							getString(R.string.error_network),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	
	}
	public void completeGovermentAffair(String govermentAffairId){		
		String url = "publicservice/govermentaffair_complete.htm?json=true&govermentaffair.id="+govermentAffairId;
		System.out.println("wangting:"+url);
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler() {		

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				// loadingdiag.hide();
				Toast.makeText(UserHandlingGovermentAffairDetailActivity.this,
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
						Toast.makeText(UserHandlingGovermentAffairDetailActivity.this,"完成事务成功", Toast.LENGTH_SHORT)
						.show();											
					}else {
						Toast.makeText(UserHandlingGovermentAffairDetailActivity.this,"完成事务失败", Toast.LENGTH_SHORT)
								.show();
					}
					finish();

				} catch (JSONException e) {
					e.printStackTrace();
					// loadingdiag.hide();
					Toast.makeText(UserHandlingGovermentAffairDetailActivity.this,
							getString(R.string.error_network),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	
	}
	public void endGovermentAffair(String govermentAffairId){		
		String url = "publicservice/govermentaffair_end.htm?json=true&govermentaffair.id="+govermentAffairId;
		System.out.println("wangting:"+url);
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler() {		

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				// loadingdiag.hide();
				Toast.makeText(UserHandlingGovermentAffairDetailActivity.this,
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
						Toast.makeText(UserHandlingGovermentAffairDetailActivity.this,"归档事务成功", Toast.LENGTH_SHORT)
						.show();											
					}else {
						Toast.makeText(UserHandlingGovermentAffairDetailActivity.this,"归档事务失败", Toast.LENGTH_SHORT)
								.show();
					}
					finish();
				} catch (JSONException e) {
					e.printStackTrace();
					// loadingdiag.hide();
					Toast.makeText(UserHandlingGovermentAffairDetailActivity.this,
							getString(R.string.error_network),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	
	}
}
