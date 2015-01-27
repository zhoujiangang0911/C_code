package cn.wislight.publicservice.ui.commercialtenant;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import cn.wislight.publicservice.R;
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.ui.user.DetailNegotiateForSenderActivity;
import cn.wislight.publicservice.util.Constants;
import cn.wislight.publicservice.util.PublicServiceClient;

/**
 * 支付界面
 * @author Administrator
 *
 */
public class PayForActivity extends BaseActivity implements OnClickListener {
	private ImageView btnPayalipay;
	private ImageView btnPayebank;
	private ImageView btnPayface2face;
	private String businessaffairId;
	
	@Override
	public void setUpView() {
		setContentView(R.layout.activity_payfor);
		
		btnPayalipay = (ImageView) findViewById(R.id.payalipay);
		btnPayebank = (ImageView) findViewById(R.id.payebank);
		btnPayface2face = (ImageView) findViewById(R.id.payface2face);
		
		btnPayalipay.setOnClickListener(this);
		btnPayebank.setOnClickListener(this);
		btnPayface2face.setOnClickListener(this);
		
		businessaffairId = getIntent().getStringExtra("businessaffairId");

	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub

	}
	public void negotiationPay(String businessaffairId){
		String url = "publicservice/businessaffair_pay.htm?json=true&businessaffair.id="+businessaffairId;
		System.out.println("wangting:"+url);
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				// loadingdiag.hide();
				Toast.makeText(PayForActivity.this,
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
						Toast.makeText(PayForActivity.this,"支付成功", Toast.LENGTH_SHORT)
						.show();
						finish();
					}else {
						Toast.makeText(PayForActivity.this,"支付失败", Toast.LENGTH_SHORT)
								.show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
					// loadingdiag.hide();
					Toast.makeText(PayForActivity.this,
							getString(R.string.error_network),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.payalipay:
			break;
		case R.id.payebank:
			break;
		case R.id.payface2face:
			negotiationPay(businessaffairId);
			break;
			
		}
		
	}
}
