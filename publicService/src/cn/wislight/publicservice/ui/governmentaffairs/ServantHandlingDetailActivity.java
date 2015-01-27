package cn.wislight.publicservice.ui.governmentaffairs;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpResponseHandler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
 * 抢单详细页界面
 * @author Administrator
 *
 */
public class ServantHandlingDetailActivity extends BaseActivity implements OnClickListener{
	private TextView txtBusinessaffairSenderName;
	private TextView txtBusinessaffairSendtime;
	private TextView txtBusinessaffairAddress;
	private TextView txtBusinessaffairContent;
	private TextView txtBusinessaffairPrice;
	private ImageView txtBusinessaffairVoicecontent;
	private TextView txtBusinessaffairSenderVOIP;
	private TextView txtBusinessaffairSenderPhone;
	private TextView txtGovermentaffairState;
	private TextView txtGovermentAffairProcedureLog;
	
	private Button btnGovermentaffairComplete;
	private Button btnGovermentaffairDelay;
	
	private LinearLayout govermentaffairCompleteLayout;
	private LinearLayout govermentaffairDelayLayout;
	
	

	private String govermentAffairId;
	//private Bundle businessAffair;
	
	private String longitude;
	private String latitude;
	private String addressName;
	/* (non-Javadoc)
	 * @see cn.wislight.publicservice.base.BaseActivity#setUpView()
	 */
	@Override
	public void setUpView() {
		setContentView(R.layout.activity_servant_handling_detail);
		
		govermentaffairCompleteLayout = (LinearLayout) findViewById(R.id.govermentaffairCompleteLayout);
		govermentaffairDelayLayout = (LinearLayout) findViewById(R.id.govermentaffairDelayLayout);
		
		txtBusinessaffairSenderName = (TextView) findViewById(R.id.businessaffairSenderName);
		txtBusinessaffairSendtime = (TextView) findViewById(R.id.businessaffairSendtime);
		txtBusinessaffairAddress = (TextView) findViewById(R.id.businessaffairAddress);
		txtBusinessaffairContent = (TextView) findViewById(R.id.businessaffairContent);
		txtBusinessaffairPrice = (TextView) findViewById(R.id.businessaffairPrice);
		txtBusinessaffairVoicecontent = (ImageView) findViewById(R.id.businessaffairVoicecontent);
		txtBusinessaffairSenderVOIP = (TextView) findViewById(R.id.businessaffairSenderVOIP);
		txtBusinessaffairSenderPhone = (TextView) findViewById(R.id.businessaffairSenderPhone);
		
		txtGovermentaffairState = (TextView) findViewById(R.id.govermentaffairState);
		txtGovermentAffairProcedureLog = (TextView) findViewById(R.id.govermentaffairProcedureLog);
		
		btnGovermentaffairComplete = (Button) findViewById(R.id.govermentaffairComplete);
		btnGovermentaffairDelay = (Button) findViewById(R.id.govermentaffairDelay);
				
		txtBusinessaffairAddress.setOnClickListener(this);		
		btnGovermentaffairComplete.setOnClickListener(this);
		btnGovermentaffairDelay.setOnClickListener(this);
		

		govermentAffairId = getIntent().getStringExtra(Constants.ID);
		
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
				Toast.makeText(ServantHandlingDetailActivity.this,
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
						JSONObject govermentaffair = jsonObject.getJSONObject("govermentaffair");						
						txtBusinessaffairSenderName.setText(govermentaffair.getJSONObject("sender").getString("loginname"));
						txtBusinessaffairContent.setText(govermentaffair.getString("content"));
						//txtBusinessaffairPrice.setText(govermentaffair.getString("price"));
						txtBusinessaffairSendtime.setText(govermentaffair.getString("sendtime"));						
						txtBusinessaffairAddress.setText(govermentaffair.getJSONObject("address").getString("name"));	
						addressName = govermentaffair.getJSONObject("address").getString("name");
						longitude = govermentaffair.getJSONObject("address").getString("longitude");
						latitude = govermentaffair.getJSONObject("address").getString("latitude");
						//txtBusinessaffairVoicecontent.setText(businessaffair.getString("voicecontentid"));
						//txtBusinessaffairSenderVOIP.setText(businessaffair.getJSONObject("sender").getString("voip"));
						txtBusinessaffairSenderPhone.setText(govermentaffair.getJSONObject("sender").getString("phone"));
						//txtGovermentAffairProcedureLog.setText("无记录");	
						String state = govermentaffair.getString("state");
						if(state.equals(GovermentAffair.STATE.CHULIZHONG)){
							txtGovermentaffairState.setText("处理中");
							govermentaffairCompleteLayout.setVisibility(View.VISIBLE);
							govermentaffairDelayLayout.setVisibility(View.VISIBLE);
						} else {
							txtGovermentaffairState.setText("");
						}											
					}else {
						Toast.makeText(ServantHandlingDetailActivity.this,"初始化失败", Toast.LENGTH_SHORT)
								.show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
					// loadingdiag.hide();
					Toast.makeText(ServantHandlingDetailActivity.this,
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
				Toast.makeText(ServantHandlingDetailActivity.this,
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
					Toast.makeText(ServantHandlingDetailActivity.this,
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
		case R.id.govermentaffairComplete:
			completeGovermentAffair(govermentAffairId);			
			break;

		case R.id.govermentaffairDelay:
			Intent intentDelay = new Intent(this, ServantGovermentAffairDelayActivity.class);
			intentDelay.putExtra("govermentAffairId", govermentAffairId);
			startActivity(intentDelay);
			break;
		default:
				break;
		}
	}
	
	public void completeGovermentAffair(String govermentAffairId){		
		String url = "publicservice/govermentaffair_complete.htm?json=true&govermentaffair.id="+govermentAffairId;
		System.out.println("wangting:"+url);
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler() {		

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				// loadingdiag.hide();
				Toast.makeText(ServantHandlingDetailActivity.this,
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
						Toast.makeText(ServantHandlingDetailActivity.this,"接受事务成功", Toast.LENGTH_SHORT)
						.show();											
					}else {
						Toast.makeText(ServantHandlingDetailActivity.this,"接受事务失败", Toast.LENGTH_SHORT)
								.show();
					}
					finish();

				} catch (JSONException e) {
					e.printStackTrace();
					// loadingdiag.hide();
					Toast.makeText(ServantHandlingDetailActivity.this,
							getString(R.string.error_network),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	
	}
	
}
