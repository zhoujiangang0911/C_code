package cn.wislight.publicservice.ui.commercialtenant;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.hisun.phone.core.voice.Device;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.wislight.publicservice.R;
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.util.Constants;
import cn.wislight.publicservice.util.PublicServiceClient;
import cn.wislight.publicservice.util.StringToChange;
import cn.wislight.publicservice.voip.CallInActivity;

/**
 * 议价界面
 * @author Administrator
 *
 */
public class NegotiatePriceActivity extends BaseActivity implements OnClickListener {

	private ImageView btnNegotiationDetail;
	private ImageView btnNegotiationVOIP;
	private ImageView btnNegotiationIM;
	private ImageView btnNegotiationOfferpriceAndConfirmtype;
	private ImageView btnNegotiationGiveup;	

	private TextView txtNegotiationSenderName;
	private TextView txtNegotiationContent;
	private TextView txtNegotiationCreatetime;
	private TextView txtNegotiationDistance;
	private TextView txtNegotiationPrice;
	private TextView txtNegotiationHandoverTime;
	private TextView txtNegotiationConfirmType;	
	
	private String negotiationId;
	
	private String senderPhone;
	private String senderVOIP;
	
	private String receiverPhone;
	private String receiverVOIP;
	private String receiverName;

	
	@Override
	public void setUpView() {
		setContentView(R.layout.activity_negotiateprice);
		
		btnNegotiationDetail = (ImageView) findViewById(R.id.negotiationDetail);
		btnNegotiationVOIP = (ImageView) findViewById(R.id.negotiationVOIP);
		btnNegotiationIM = (ImageView) findViewById(R.id.negotiationIM);
		btnNegotiationOfferpriceAndConfirmtype = (ImageView) findViewById(R.id.negotiationOfferpriceAndConfirmtype);		
		btnNegotiationGiveup = (ImageView) findViewById(R.id.negotiationGiveup);
		
		btnNegotiationVOIP.setOnClickListener(this);
		btnNegotiationIM.setOnClickListener(this);
		btnNegotiationOfferpriceAndConfirmtype.setOnClickListener(this);
		btnNegotiationGiveup.setOnClickListener(this);
		btnNegotiationDetail.setOnClickListener(this);		

		txtNegotiationSenderName = (TextView) findViewById(R.id.negotiationSenderName);
		txtNegotiationContent = (TextView) findViewById(R.id.negotiationContent);
		txtNegotiationCreatetime = (TextView) findViewById(R.id.negotiationCreatetime);
		txtNegotiationDistance = (TextView) findViewById(R.id.negotiationDistance);
		txtNegotiationPrice = (TextView) findViewById(R.id.negotiationPrice);
		txtNegotiationHandoverTime = (TextView) findViewById(R.id.negotiationHandoverTime);
		
		txtNegotiationConfirmType = (TextView) findViewById(R.id.negotiationConfirmType);
		
		negotiationId = getIntent().getStringExtra("negotiationId");
		
		fillData(negotiationId);
		
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub

	}	

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.negotiationDetail:
			Toast.makeText(NegotiatePriceActivity.this, "click detail content", 100).show();
			Intent intent = new Intent(this,BuyserNegPriceActivity.class);
			intent.putExtra("negotiationId", negotiationId);
			//startActivity(intent);
			startActivityForResult(intent, Constants.REQUEST_PRICE_CONFIRMTYPE);
			//gotoActivity(BuyserNegPriceActivity.class, false);	
			break;
		case R.id.negotiationVOIP:
			/*
			if(senderPhone == null &&senderPhone.trim().length()==0){
				Toast.makeText(this, "发单者电话不对", 100).show();
				return;
			}

			Intent intentVOIP = new Intent(Intent.ACTION_CALL, Uri.parse("tel://"+senderPhone));    
            startActivity(intentVOIP); 
			break;	
			*/
			//if(device!=null && !TextUtils.isEmpty(senderVOIP)){
			//	callingId= device.makeCall(Device.CallType.VOICE, senderVIOP);
			//}
			if(TextUtils.isEmpty(senderVOIP) && TextUtils.isEmpty(senderPhone)){
				Toast.makeText(this, "发单者VOIP电话不对", 100).show();
			} else {			
				String voip = senderVOIP;
				//if(TextUtils.isEmpty(voip) || "null".equals(voip))
				{
					voip = senderPhone;
				}
				
				Intent intentVOIP = new Intent(this, CallInActivity.class);   
				intentVOIP.putExtra("isIncomingCall", false);
				intentVOIP.putExtra("voip", voip);
				//intentVOIP.putExtra("voip", "15691999278");
	            startActivity(intentVOIP); 
			}
			break;
		case R.id.negotiationIM:
			Intent intentIM = new Intent(this,IMChatingActivity.class);
			intentIM.putExtra("negotiationId", negotiationId);
			startActivity(intentIM);
			//startActivityForResult(intent, Constants.REQUEST_PRICE_CONFIRMTYPE);
			break;	
		case R.id.negotiationOfferpriceAndConfirmtype:
			String price =  txtNegotiationPrice.getText().toString();
			String confirmType = txtNegotiationConfirmType.getText().toString();
			negotiationOfferpriceAndConfirmtype(negotiationId, price, confirmType);
			//Toast.makeText(NegotiatePriceActivity.this, "no use button", 100).show();
			break;	
		case R.id.negotiationGiveup:
			negotiationGiveup(negotiationId);
			break;	

		default:
			break;
		}
	}
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        //可以根据多个请求代码来作相应的操作  
		if(resultCode == Activity.RESULT_OK){
			if(requestCode == Constants.REQUEST_PRICE_CONFIRMTYPE){
				txtNegotiationPrice.setText(data.getStringExtra("price"));
				txtNegotiationConfirmType.setText(data.getStringExtra("confirmType"));
				txtNegotiationHandoverTime.setText(data.getStringExtra("handoverTime"));

			}
		}
       
        super.onActivityResult(requestCode, resultCode, data);  
    }
	public void fillData(String negotiationId){		
		String url = "publicservice/negotiation_detail.htm?json=true&negotiation.id="+negotiationId;
		System.out.println("wangting:"+url);
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				// loadingdiag.hide();
				Toast.makeText(NegotiatePriceActivity.this,
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
						
						txtNegotiationSenderName.setText(negotiation.getJSONObject("sender").getString("loginname"));
						txtNegotiationContent.setText(negotiation.getJSONObject("businessaffair").getString("content"));
						senderPhone = negotiation.getJSONObject("sender").getString("phone");
						senderVOIP = negotiation.getJSONObject("sender").getString("voip");
						receiverPhone = negotiation.getJSONObject("receiver").getString("phone");
						receiverVOIP = negotiation.getJSONObject("receiver").getString("voip");
						receiverName = negotiation.getJSONObject("receiver").getString("loginname");
						txtNegotiationCreatetime.setText(StringToChange.toNoT(negotiation.getString("createtime")));
						txtNegotiationDistance.setText("2公里");
						txtNegotiationPrice.setText(negotiation.getString("price"));
						//txtNegotiationHandoverTime.setText(negotiation.getString("handoverTime"));			
												
					}else {
						Toast.makeText(NegotiatePriceActivity.this,"初始化失败", Toast.LENGTH_SHORT)
								.show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
					// loadingdiag.hide();
					Toast.makeText(NegotiatePriceActivity.this,
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
				Toast.makeText(NegotiatePriceActivity.this,
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
						Toast.makeText(NegotiatePriceActivity.this,"放弃此单成功", Toast.LENGTH_SHORT)
						.show();
						finish();
					}else {
						Toast.makeText(NegotiatePriceActivity.this,"放弃此单成功", Toast.LENGTH_SHORT)
								.show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
					// loadingdiag.hide();
					Toast.makeText(NegotiatePriceActivity.this,
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
				Toast.makeText(NegotiatePriceActivity.this,
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
						Toast.makeText(NegotiatePriceActivity.this,"放弃此单成功", Toast.LENGTH_SHORT)
						.show();
						finish();
					}else {
						Toast.makeText(NegotiatePriceActivity.this,"放弃此单成功", Toast.LENGTH_SHORT)
								.show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
					// loadingdiag.hide();
					Toast.makeText(NegotiatePriceActivity.this,
							getString(R.string.error_network),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	
	}
	
	
	public void negotiationOfferpriceAndConfirmtype(String negotiationId, String price, String confirmType){
		String url = "publicservice/negotiation_offerpriceandconfirmtype.htm?json=true&negotiation.id="+negotiationId+"&negotiation.price="+price+"&negotiation.confirmtype="+confirmType;
		System.out.println("wangting:"+url);
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				// loadingdiag.hide();
				Toast.makeText(NegotiatePriceActivity.this,
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
						Toast.makeText(NegotiatePriceActivity.this,"出价成功", Toast.LENGTH_SHORT)
						.show();
						finish();
					}else {
						Toast.makeText(NegotiatePriceActivity.this,"出价成功", Toast.LENGTH_SHORT)
								.show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
					// loadingdiag.hide();
					Toast.makeText(NegotiatePriceActivity.this,
							getString(R.string.error_network),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	
	}
	
}
