package cn.wislight.publicservice.ui.commercialtenant;

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
import android.widget.TextView;
import android.widget.Toast;
import cn.wislight.publicservice.R;
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.util.Constants;
import cn.wislight.publicservice.util.PublicServiceClient;
import cn.wislight.publicservice.util.StringToChange;

/**
 * 抢单详细页界面
 * @author Administrator
 *
 */
public class DetailCommercialtenantActivity extends BaseActivity implements OnClickListener{
	private TextView txtBusinessaffairSenderName;
	private TextView txtBusinessaffairSendtime;
	private TextView txtBusinessaffairAddress;
	private TextView txtBusinessaffairContent;
	private TextView txtBusinessaffairPrice;
	private ImageView txtBusinessaffairVoicecontent;
	private TextView txtBusinessaffairSenderVOIP;
	private TextView txtBusinessaffairSenderPhone;
	private TextView txtBusinessaffairLog;
	
	private ImageButton imgBtnCallPhone;
	private String businessAffairId;
	private Bundle businessAffair;
	
	private String longitude;
	private String latitude;
	private String addressName;
	@Override
	public void setUpView() {
		setContentView(R.layout.activity_detail_commercialtenant);
		
		txtBusinessaffairSenderName = (TextView) findViewById(R.id.govermentaffairReceiverName);
		txtBusinessaffairSendtime = (TextView) findViewById(R.id.businessaffairSendtime);
		txtBusinessaffairAddress = (TextView) findViewById(R.id.businessaffairAddress);
		txtBusinessaffairContent = (TextView) findViewById(R.id.businessaffairContent);
		txtBusinessaffairPrice = (TextView) findViewById(R.id.businessaffairPrice);
		txtBusinessaffairVoicecontent = (ImageView) findViewById(R.id.businessaffairVoicecontent);
		txtBusinessaffairSenderVOIP = (TextView) findViewById(R.id.businessaffairSenderVOIP);
		txtBusinessaffairSenderPhone = (TextView) findViewById(R.id.businessaffairSenderPhone);
		txtBusinessaffairLog = (TextView) findViewById(R.id.businessaffairLog);
		
		imgBtnCallPhone = (ImageButton) findViewById(R.id.img_callphone);
		imgBtnCallPhone.setOnClickListener(this);
		
		businessAffairId = getIntent().getStringExtra(Constants.ID);
		businessAffair = getIntent().getBundleExtra("businessaffair");		
		txtBusinessaffairAddress.setOnClickListener(this);
		fillData(businessAffairId);
	}

	@Override
	public void setListener() {
		
	}
	
	public void fillData(String businessAffairId){		
		String url = "publicservice/businessaffair_detail.htm?json=true&businessaffair.id="+businessAffairId;
		System.out.println("wangting:"+url);
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler() {
			

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				// loadingdiag.hide();
				Toast.makeText(DetailCommercialtenantActivity.this,
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
						
						JSONObject businessaffair = jsonObject.getJSONObject("businessaffair");	
						String SenderName = businessaffair.getJSONObject("sender").getString("loginname");
						txtBusinessaffairSenderName.setText(SenderName);
					//	txtBusinessaffairSenderName.setText(negotiation.getJSONObject("sender").getString("loginname"));
						txtBusinessaffairContent.setText(businessaffair.getString("content"));
						txtBusinessaffairPrice.setText(businessaffair.getString("price"));
						//txtBusinessaffairPrice.setText(negotiation.getString("price"));
						txtBusinessaffairSendtime.setText(StringToChange.toNoT(businessaffair.getString("sendtime")));						
						txtBusinessaffairAddress.setText(businessaffair.getJSONObject("address").getString("name"));	
//						addressName = businessaffair.getJSONObject("address").getString("name");
//						longitude = businessaffair.getJSONObject("address").getString("longitude");
//						latitude = businessaffair.getJSONObject("address").getString("latitude");
						//txtBusinessaffairVoicecontent.setText(businessaffair.getString("voicecontentid"));
						//txtBusinessaffairSenderVOIP.setText(businessaffair.getJSONObject("sender").getString("voip"));
						txtBusinessaffairSenderPhone.setText(businessaffair.getJSONObject("sender").getString("phone"));
						txtBusinessaffairLog.setText("无记录");
											
					}else {
						Toast.makeText(DetailCommercialtenantActivity.this,"初始化失败", Toast.LENGTH_SHORT)
								.show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
					// loadingdiag.hide();
					Toast.makeText(DetailCommercialtenantActivity.this,
							getString(R.string.error_network),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.businessaffairAddress:
			Intent intent = new Intent(this, BaiduMapShowPositionActivity.class);
			intent.putExtra("addressName", addressName);
			intent.putExtra("longitude", longitude);
			intent.putExtra("latitude", latitude);
			this.startActivity(intent);
			break;
		case R.id.img_callphone:
			//Toast.makeText(getApplicationContext(), "电话号码"+txtBusinessaffairSenderPhone.getText(),Toast.LENGTH_LONG ).show();
			Intent intent2 = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+txtBusinessaffairSenderPhone.getText()));
			startActivity(intent2);
			break;		
			
			default:
				break;
		}
	}
}
