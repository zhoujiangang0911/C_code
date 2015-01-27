package cn.wislight.publicservice.ui.commercialtenant;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpResponseHandler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.wislight.publicservice.R;
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.entity.BusinessAffair;
import cn.wislight.publicservice.entity.Negotiation;
import cn.wislight.publicservice.ui.user.DetailNegotiateForSenderActivity;
import cn.wislight.publicservice.util.Constants;
import cn.wislight.publicservice.util.PublicServiceClient;

/**
 * 抢单详细页界面
 * @author Administrator
 *
 */
public class DetailNegotiateActivity extends BaseActivity implements OnClickListener{
	private TextView txtBusinessaffairSenderName;
	private TextView txtBusinessaffairSendtime;
	private TextView txtBusinessaffairSenderAddress;
	private TextView txtBusinessaffairContent;
	private TextView txtBusinessaffairPrice;
	private ImageView txtBusinessaffairVoicecontent;
	private TextView txtBusinessaffairSenderVOIP;
	private TextView txtBusinessaffairSenderPhone;
	private TextView txtNegotiationPrice;
	private TextView txtNegotiationState;
	private TextView txtBusinessaffairLog;
	
	private ImageView btnNegotiationDeliverButton;
	private LinearLayout layoutNegotiationDeliver;
	private ImageView btnNegotiationVoiceConfirmButton;
	private LinearLayout layoutNegotiationVoiceConfirm;
	
	private String negotiationId;
	private String businessaffairId;

	@Override
	public void setUpView() {
		setContentView(R.layout.activity_detail_commercialtenant_negotiation);
		
		txtBusinessaffairSenderName = (TextView) findViewById(R.id.businessaffairSenderName);
		txtBusinessaffairSendtime = (TextView) findViewById(R.id.businessaffairSendtime);
		txtBusinessaffairSenderAddress = (TextView) findViewById(R.id.businessaffairSenderAddress);
		txtBusinessaffairContent = (TextView) findViewById(R.id.businessaffairContent);
		txtBusinessaffairPrice =(TextView) findViewById(R.id.businessaffairPrice);
		txtBusinessaffairVoicecontent = (ImageView) findViewById(R.id.businessaffairVoicecontent);
		txtBusinessaffairSenderVOIP = (TextView) findViewById(R.id.businessaffairSenderVOIP);
		txtBusinessaffairSenderPhone = (TextView) findViewById(R.id.businessaffairSenderPhone);
		txtNegotiationPrice = (TextView) findViewById(R.id.negotiationPrice);
		txtNegotiationState = (TextView) findViewById(R.id.negotiationState);
		txtBusinessaffairLog = (TextView) findViewById(R.id.businessaffairLog);
		
		btnNegotiationDeliverButton = (ImageView) findViewById(R.id.negotiationDeliverButton);
		layoutNegotiationDeliver = (LinearLayout) findViewById(R.id.negotiationDeliver);
		
		btnNegotiationVoiceConfirmButton = (ImageView) findViewById(R.id.negotiationVoiceConfirmButton);
		layoutNegotiationVoiceConfirm = (LinearLayout) findViewById(R.id.negotiationVoiceConfirm);
		
		btnNegotiationDeliverButton.setOnClickListener(this);
		btnNegotiationVoiceConfirmButton.setOnClickListener(this);
		
		negotiationId = getIntent().getStringExtra(Constants.ID);		
		
		fillData(negotiationId);
	}

	@Override
	public void setListener() {
		
	}
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.negotiationDeliverButton:
			businessaffairDeliver(businessaffairId);
			break;
		case R.id.negotiationVoiceConfirmButton:
			negotiationVoiceConfirm(negotiationId);
			break;
			
		default:
			break;
		}
	}
	public void fillData(String negotiationId){		
		String url = "publicservice/negotiation_detail.htm?json=true&negotiation.id="+negotiationId;
		System.out.println("wangting:"+url);
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				// loadingdiag.hide();
				Toast.makeText(DetailNegotiateActivity.this,
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
						JSONObject negotiation = jsonObject.getJSONObject("negotiation");
						JSONObject businessaffair = negotiation.getJSONObject("businessaffair");	
						businessaffairId = businessaffair.getString("id");
						txtBusinessaffairSenderName.setText(negotiation.getJSONObject("sender").getString("loginname"));
						txtBusinessaffairSendtime.setText(businessaffair.getString("sendtime"));
						txtBusinessaffairContent.setText(businessaffair.getString("content"));
						txtBusinessaffairPrice.setText(businessaffair.getString("price"));
						txtBusinessaffairSenderAddress.setText(businessaffair.getJSONObject("sender").getString("address"));						
						//txtBusinessaffairVoicecontent.setText(businessaffair.getString("voicecontentid"));
						//txtBusinessaffairSenderVOIP.setText(businessaffair.getJSONObject("sender").getString("voip"));
						txtBusinessaffairSenderPhone.setText(businessaffair.getJSONObject("sender").getString("phone"));
						txtNegotiationPrice.setText(negotiation.getString("price"));
						String negotiationState = negotiation.getString("state");
						String businessAffairState = businessaffair.getString("state");
						if(Negotiation.STATE.DAIYIJIA.equals(negotiationState)){
							txtNegotiationState.setText("待议价");
						} else if(Negotiation.STATE.YICHUJIA.equals(negotiationState)){
							txtNegotiationState.setText("已出价");
							if("IVR确认".equals(negotiation.getString("confirmtype"))){
								layoutNegotiationVoiceConfirm.setVisibility(View.VISIBLE);
							}
						}else if(Negotiation.STATE.YICHENGJIAO.equals(negotiationState)){
							if (BusinessAffair.STATE.DAIFUKUAN.equals(businessAffairState)){								
								txtNegotiationState.setText("待付款");
							}else if (BusinessAffair.STATE.DAIFAHUO.equals(businessAffairState)){
								txtNegotiationState.setText("已付款");
								layoutNegotiationDeliver.setVisibility(View.VISIBLE);
							}else if (BusinessAffair.STATE.DAISHOUHUO.equals(businessAffairState)){
								txtNegotiationState.setText("待收货");
							}else if (BusinessAffair.STATE.YISHOUHUO.equals(businessAffairState)){
								txtNegotiationState.setText("已收货");								
							}else if (BusinessAffair.STATE.YIGUIDANG.equals(businessAffairState)){
								txtNegotiationState.setText("已完结");
							}
							
						} else if(Negotiation.STATE.SENDER_GIVIUP.equals(negotiationState)){
							txtNegotiationState.setText("发单方终止");
						} else if(Negotiation.STATE.RECEIVER_GIVEUP.equals(negotiationState)){
							txtNegotiationState.setText("接单方终止");
						} else if(Negotiation.STATE.OTHERS_DEALED.equals(negotiationState)){
							txtNegotiationState.setText("其他人已成交");
						}
						
						txtBusinessaffairLog.setText("无记录");
											
					}else {
						Toast.makeText(DetailNegotiateActivity.this,"初始化失败", Toast.LENGTH_SHORT)
								.show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
					// loadingdiag.hide();
					Toast.makeText(DetailNegotiateActivity.this,
							getString(R.string.error_network),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	
	}
	
	public void businessaffairDeliver(String businessaffairId){
		String url = "publicservice/businessaffair_delivergoods.htm?json=true&businessaffair.id="+businessaffairId;
		System.out.println("wangting:"+url);
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				// loadingdiag.hide();
				Toast.makeText(DetailNegotiateActivity.this,
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
						Toast.makeText(DetailNegotiateActivity.this,"发货成功", Toast.LENGTH_SHORT)
						.show();
						finish();
					}else {
						Toast.makeText(DetailNegotiateActivity.this,"发货失败", Toast.LENGTH_SHORT)
								.show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
					// loadingdiag.hide();
					Toast.makeText(DetailNegotiateActivity.this,
							getString(R.string.error_network),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	
	}
	
	public void negotiationVoiceConfirm(String negotiationId){
		String url = "publicservice/negotiation_voiceconfirm.htm?json=true&negotiation.id="+negotiationId;
		System.out.println("wangting:"+url);
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				// loadingdiag.hide();
				Toast.makeText(DetailNegotiateActivity.this,
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
						Toast.makeText(DetailNegotiateActivity.this,"发起语音成功", Toast.LENGTH_SHORT)
						.show();
						finish();
					}else {
						Toast.makeText(DetailNegotiateActivity.this,"发起语音失败", Toast.LENGTH_SHORT)
								.show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
					// loadingdiag.hide();
					Toast.makeText(DetailNegotiateActivity.this,
							getString(R.string.error_network),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	
	}
}
