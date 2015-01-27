package cn.wislight.publicservice.ui.user;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpResponseHandler;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.wislight.publicservice.R;
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.entity.BusinessAffair;
import cn.wislight.publicservice.entity.Negotiation;
import cn.wislight.publicservice.ui.commercialtenant.BuyserNegPriceActivity;
import cn.wislight.publicservice.ui.commercialtenant.NegotiatePriceActivity;
import cn.wislight.publicservice.ui.commercialtenant.PayForActivity;
import cn.wislight.publicservice.ui.user.goverment.UserEndGovermentAffairDetailActivity;
import cn.wislight.publicservice.util.Constants;
import cn.wislight.publicservice.util.PublicServiceClient;
import cn.wislight.publicservice.util.StringToChange;

/**
 * 抢单详细页界面
 * @author Administrator
 *
 */
public class DetailNegotiateForSenderActivity extends BaseActivity implements OnClickListener{
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
	private TextView txtSetCommonlyBusinessman; 
	private TextView txtRectiveName;
	private ImageView txtNegotiationDeal;
	private ImageView txtNegotiationGiveup;
	private ImageView btnNegotiationPayButton;
	private ImageView btnNegotiationReceiptButton;
	private ImageView btnNegotiationEndButton;
	private LinearLayout layoutNegotiationDealGiveup;
	private LinearLayout layoutNegotiationPay;
	private LinearLayout layoutNegotiationReceipt;
	private LinearLayout layoutNegotiationEnd;
	
	private ImageButton imgbtnCallPhone;
	private ImageButton imgbtnCallIntelPhone;
	
	private String negotiationId;
	private String businessaffairId;

	private String receiverId;

	@Override
	public void setUpView() {
		setContentView(R.layout.activity_detail_user_negotiation);
		txtRectiveName = (TextView) findViewById(R.id.tv_receivename);
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
		txtSetCommonlyBusinessman = (TextView) findViewById(R.id.setCommonlyBusinessman);
		
		txtNegotiationDeal = (ImageView) findViewById(R.id.negotiationDeal);
		txtNegotiationGiveup = (ImageView) findViewById(R.id.negotiationGiveup);
		btnNegotiationPayButton = (ImageView) findViewById(R.id.negotiationPayButton);
		btnNegotiationReceiptButton = (ImageView) findViewById(R.id.negotiationReceiptButton);
		btnNegotiationEndButton = (ImageView) findViewById(R.id.negotiationEndButton);
		layoutNegotiationDealGiveup = (LinearLayout) findViewById(R.id.negotiationDealGiveup);		
		layoutNegotiationPay = (LinearLayout) findViewById(R.id.negotiationPay);
		layoutNegotiationReceipt = (LinearLayout) findViewById(R.id.negotiationReceipt);
		layoutNegotiationEnd = (LinearLayout) findViewById(R.id.negotiationEnd);
		
		imgbtnCallPhone = (ImageButton) findViewById(R.id.imagebtn_callphone);
		imgbtnCallIntelPhone = (ImageButton) findViewById(R.id.imagebtn_callintentphone);
		
		
		imgbtnCallPhone.setOnClickListener(this);
		imgbtnCallIntelPhone.setOnClickListener(this);
		
		txtNegotiationDeal.setOnClickListener(this);
		txtNegotiationGiveup.setOnClickListener(this);
		btnNegotiationPayButton.setOnClickListener(this);
		btnNegotiationReceiptButton.setOnClickListener(this);
		btnNegotiationEndButton.setOnClickListener(this);
		txtSetCommonlyBusinessman.setOnClickListener(this);
		
		negotiationId = getIntent().getStringExtra(Constants.ID);		
		
		fillData(negotiationId);
	}

	@Override
	public void setListener() {
		
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.negotiationDeal:
			negotiationDeal(negotiationId);
			break;
		case R.id.negotiationGiveup:
			negotiationGiveup(negotiationId);
			break;
		case R.id.negotiationPayButton:
			Intent intent = new Intent(this, PayForActivity.class);
			intent.putExtra("businessaffairId", businessaffairId);
			startActivity(intent);
			break;
		case R.id.negotiationReceiptButton:
			businessaffairReceiptGoods(businessaffairId);
			break;
		case R.id.negotiationEndButton:
			businessaffairEnd(businessaffairId);
			break;
		case R.id.setCommonlyBusinessman:
			setCommonlyBusinessman();
			break;
		case R.id.imagebtn_callphone:
			Intent intent2 = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+txtBusinessaffairSenderPhone.getText()));
			startActivity(intent2);
			break;
		case R.id.imagebtn_callintentphone:

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
				Toast.makeText(DetailNegotiateForSenderActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				try {
					JSONObject jsonObject = new JSONObject(response);
					String result = jsonObject.getString("result");
					Log.i("----zhoujg", "----result:"+result);
					if (result.equals("success")){
						JSONObject negotiation = jsonObject.getJSONObject("negotiation");
						Log.i("----zhoujg", "----negotiation:"+negotiation);
						JSONObject businessaffair = negotiation.getJSONObject("businessaffair");
						Log.i("----zhoujg", "----businessaffair:"+businessaffair);
						businessaffairId = businessaffair.getString("id");
						txtBusinessaffairSenderName.setText(negotiation.getJSONObject("sender").getString("loginname"));
						
						receiverId = negotiation.getJSONObject("receiver").getString("id");
						txtRectiveName.setText(negotiation.getJSONObject("receiver").getString("loginname"));
						txtBusinessaffairSendtime.setText(StringToChange.toNoT(businessaffair.getString("sendtime")));
						txtBusinessaffairContent.setText(businessaffair.getString("content"));
						txtBusinessaffairPrice.setText(businessaffair.getString("price"));
						txtBusinessaffairSenderAddress.setText(StringToChange.toKong(businessaffair.getJSONObject("address").getString("name")));						
						//txtBusinessaffairVoicecontent.setText(businessaffair.getString("voicecontentid"));
						//txtBusinessaffairSenderVOIP.setText(businessaffair.getJSONObject("sender").getString("voip"));
						txtBusinessaffairSenderPhone.setText(businessaffair.getJSONObject("sender").getString("phone"));
						txtNegotiationPrice.setText(negotiation.getString("price"));
						String negotiationState = negotiation.getString("state");
						String businessAffairState = businessaffair.getString("state");
						String businessAffairBuyOrSell = businessaffair.getString("buyorsell");
						if(Negotiation.STATE.DAIYIJIA.equals(negotiationState)){
							txtNegotiationState.setText("待议价");
						} else if(Negotiation.STATE.YICHUJIA.equals(negotiationState)){
							txtNegotiationState.setText("已出价");
							layoutNegotiationDealGiveup.setVisibility(View.VISIBLE);
						}else if(Negotiation.STATE.YICHENGJIAO.equals(negotiationState) ){							
							if (BusinessAffair.STATE.DAIFUKUAN.equals(businessAffairState)){
								layoutNegotiationPay.setVisibility(View.VISIBLE);
								txtNegotiationState.setText("已成交");
							}else if (BusinessAffair.STATE.DAIFAHUO.equals(businessAffairState)){
								txtNegotiationState.setText("待发货");
							}else if (BusinessAffair.STATE.DAISHOUHUO.equals(businessAffairState)){
								txtNegotiationState.setText("已发货");
								layoutNegotiationReceipt.setVisibility(View.VISIBLE);
							}else if (BusinessAffair.STATE.YISHOUHUO.equals(businessAffairState)){
								txtNegotiationState.setText("已收货");
								layoutNegotiationEnd.setVisibility(View.VISIBLE);
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
						Toast.makeText(DetailNegotiateForSenderActivity.this,"初始化失败", Toast.LENGTH_SHORT)
								.show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
					// loadingdiag.hide();
					Toast.makeText(DetailNegotiateForSenderActivity.this,
							getString(R.string.error_network),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	
	}
	
	
	public void negotiationDeal(String negotiationId){
		String url = "publicservice/negotiation_deal.htm?json=true&negotiation.id="+negotiationId;
		System.out.println("wangting:"+url);
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				// loadingdiag.hide();
				Toast.makeText(DetailNegotiateForSenderActivity.this,
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
						Toast.makeText(DetailNegotiateForSenderActivity.this,"成交完成", Toast.LENGTH_SHORT)
						.show();
						finish();
					}else {
						Toast.makeText(DetailNegotiateForSenderActivity.this,"成交失败", Toast.LENGTH_SHORT)
								.show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
					// loadingdiag.hide();
					Toast.makeText(DetailNegotiateForSenderActivity.this,
							getString(R.string.error_network),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	
	}
	
	public void negotiationGiveup(String negotiationId){
		String url = "publicservice/negotiation_giveup.htm?json=true&negotiation.id="+negotiationId;
		System.out.println("wangting:"+url);
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				// loadingdiag.hide();
				Toast.makeText(DetailNegotiateForSenderActivity.this,
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
						Toast.makeText(DetailNegotiateForSenderActivity.this,"放弃此单成功", Toast.LENGTH_SHORT)
						.show();
						finish();
					}else {
						Toast.makeText(DetailNegotiateForSenderActivity.this,"放弃此单失败", Toast.LENGTH_SHORT)
								.show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
					// loadingdiag.hide();
					Toast.makeText(DetailNegotiateForSenderActivity.this,
							getString(R.string.error_network),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	
	}
	
	public void businessaffairReceiptGoods(String businessaffairId){
		String url = "publicservice/businessaffair_receiptgoods.htm?json=true&businessaffair.id="+businessaffairId;
		System.out.println("wangting:"+url);
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				// loadingdiag.hide();
				Toast.makeText(DetailNegotiateForSenderActivity.this,
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
						Toast.makeText(DetailNegotiateForSenderActivity.this,"收货成功", Toast.LENGTH_SHORT)
						.show();
						finish();
					}else {
						Toast.makeText(DetailNegotiateForSenderActivity.this,"收货失败", Toast.LENGTH_SHORT)
								.show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
					// loadingdiag.hide();
					Toast.makeText(DetailNegotiateForSenderActivity.this,
							getString(R.string.error_network),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	
	}
	public void businessaffairEnd(String businessaffairId){
		String url = "publicservice/businessaffair_endbusinessaffair.htm?json=true&businessaffair.id="+businessaffairId;
		System.out.println("wangting:"+url);
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				// loadingdiag.hide();
				Toast.makeText(DetailNegotiateForSenderActivity.this,
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
						Toast.makeText(DetailNegotiateForSenderActivity.this,"结单成功", Toast.LENGTH_SHORT)
						.show();
						finish();
					}else {
						Toast.makeText(DetailNegotiateForSenderActivity.this,"结单失败", Toast.LENGTH_SHORT)
								.show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
					// loadingdiag.hide();
					Toast.makeText(DetailNegotiateForSenderActivity.this,
							getString(R.string.error_network),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	
	}
	
	public void setCommonlyBusinessman(){		
		if(receiverId == null || receiverId.trim().length()<1){
			Toast.makeText(DetailNegotiateForSenderActivity.this,"无处理人", Toast.LENGTH_SHORT).show();
			return;
		}
		String url = "publicservice/commonlybusinessman_create.htm?json=true&commonlyperson.personid="+receiverId;
		System.out.println("wangting:"+url);
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler() {		

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				// loadingdiag.hide();
				Toast.makeText(DetailNegotiateForSenderActivity.this,
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
						Toast.makeText(DetailNegotiateForSenderActivity.this,"设置成功", Toast.LENGTH_SHORT)
						.show();										
					}else if (result.equals("exist")){
						Toast.makeText(DetailNegotiateForSenderActivity.this,"已设置", Toast.LENGTH_SHORT)
						.show();										
					}else{
						Toast.makeText(DetailNegotiateForSenderActivity.this,"设置失败", Toast.LENGTH_SHORT)
								.show();
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
					// loadingdiag.hide();
					Toast.makeText(DetailNegotiateForSenderActivity.this,
							getString(R.string.error_network),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}
