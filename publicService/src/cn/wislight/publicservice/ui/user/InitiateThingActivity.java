package cn.wislight.publicservice.ui.user;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cn.wislight.publicservice.R;
import cn.wislight.publicservice.ui.LoginActivity;
import cn.wislight.publicservice.ui.RegisterPersonalActivity;
import cn.wislight.publicservice.ui.commercialtenant.BaiduMapSelectPositionActivity;
import cn.wislight.publicservice.ui.commercialtenant.SelectServiceTypeActivity;
import cn.wislight.publicservice.util.Constants;
import cn.wislight.publicservice.util.FindCurrentLocation;
import cn.wislight.publicservice.util.MD5Str;
import cn.wislight.publicservice.util.PublicServiceClient;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Administrator
 *  发起事物
 */
public class InitiateThingActivity extends Activity implements OnClickListener {
	private TextView txtPointselect;
	private EditText txtBusinessaffairContent;
	private EditText txtBusinessaffairPrice;
	private EditText editBusinessaffairAddress;
	
	private ImageButton btnCreateBusinessAffair;
	
	private String addressName;
	private String longitude;
	private String latitude;
	private TextView txtCommonlyAddressSelect;
	private LinearLayout btnSelectServiceType;
	private TextView txtServicetype;
	private String serviceTypeId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_initiate_businessaffair_commerce);
		
		txtPointselect = (TextView) findViewById(R.id.pointselect);
		txtCommonlyAddressSelect = (TextView) findViewById(R.id.commonlyAddressSelect);
		txtServicetype = (TextView)findViewById(R.id.servicetype);
		
		txtBusinessaffairContent = (EditText) findViewById(R.id.businessaffairContent);
		txtBusinessaffairPrice = (EditText) findViewById(R.id.businessaffairPrice);
		editBusinessaffairAddress  = (EditText) findViewById(R.id.businessaffairAddress);
		btnSelectServiceType = (LinearLayout)findViewById(R.id.selectservicetype);
		
		btnCreateBusinessAffair = (ImageButton) findViewById(R.id.createBusinessAffair);
		
		txtPointselect.setOnClickListener(this);
		txtCommonlyAddressSelect.setOnClickListener(this);
		btnCreateBusinessAffair.setOnClickListener(this);
		btnSelectServiceType.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.createBusinessAffair:
			createBusinessAffair();			
			break;
		case R.id.start_thing:
			break;		
		case R.id.pointselect:
			Toast.makeText(InitiateThingActivity.this,
					"select position", Toast.LENGTH_SHORT)
					.show();
			//Intent intent = new Intent(this, BaiduMapSelectPositionActivity.class);
			//this.startActivityForResult(intent, 1);
			//this.startActivity(intent);
			
			Intent intent = new Intent(this,BaiduMapSelectPositionActivity.class);
			//intent.putExtra("negotiationId", negotiationId);
			//startActivity(intent);
			startActivityForResult(intent, Constants.REQUEST_ADDRESS);
			
			break;	
		case R.id.commonlyAddressSelect:
			Intent intentCommonAddress = new Intent(this, FindCurrentLocation.class);
			//intent.putExtra("negotiationId", negotiationId);
			startActivity(intentCommonAddress);
			//startActivityForResult(intentCommonAddress, Constants.REQUEST_ADDRESS);
			break;	
		case R.id.selectservicetype:
			Intent intentServiceType = new Intent(this, SelectServiceTypeActivity.class);
			startActivityForResult(intentServiceType, Constants.REQUEST_SERVICETYPE); 
			break;
		default:
			break;
		}
	}
	
	private void createBusinessAffair(){
		String url = "publicservice/businessaffair_create.htm?json=true";
		RequestParams params = new RequestParams();
		String content = txtBusinessaffairContent.getText().toString();
		String price = txtBusinessaffairPrice.getText().toString();
        if(TextUtils.isEmpty(price)){
        	txtBusinessaffairPrice.setError("价格不能为空!");
        	return ;
        }
		if(content != null && content.trim().length() <1){
			Toast.makeText(this, "内容不能为空", 100).show();
			return;
		}

		
		params.put("businessaffair.content", content);
		params.put("businessaffair.price ", price);
		params.put("businessaffair.servicetype.id", serviceTypeId);

		String fulladdress = editBusinessaffairAddress.getText().toString();
		int index = fulladdress.indexOf("(");
		String address = "";
		if(index<0){
			address = fulladdress;
		}else{
			address = fulladdress.substring(0,index);
		}
		params.put("businessaffair.address.name", address);
		params.put("businessaffair.address.longitude ", longitude);
		params.put("businessaffair.address.latitude ", latitude);
		
		
		///loadingdiag.setText(getString(R.string.uploading));
		//loadingdiag.show();
		PublicServiceClient.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] arg1, byte[] body,
					Throwable error) {
				//loadingdiag.hide();
				Toast.makeText(InitiateThingActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onSuccess(int stautsCode, Header[] arg1, byte[] body) {
				// loadingdiag.hide();
				String response = new String(body);
				System.out.println("wangting: response" + response);
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(response);

					String result = jsonObject.getString("result");
					if (result.equals("success")) {
						Toast.makeText(InitiateThingActivity.this, "发单成功",
								100).show();
						//gotoActivity(LoginActivity.class, false);	
						finish();
					} else{
						Toast.makeText(InitiateThingActivity.this, "发单失败",
								100).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        //可以根据多个请求代码来作相应的操作  
		if(resultCode == Activity.RESULT_OK){
			if(requestCode == Constants.REQUEST_ADDRESS){
				addressName = data.getStringExtra("addressName");
				longitude = data.getStringExtra("longitude");
				latitude = data.getStringExtra("latitude");
				editBusinessaffairAddress.setText(addressName+"("+longitude+","+latitude+")");
			}
			if(requestCode == Constants.REQUEST_SERVICETYPE){
				String servicetypeContent = data.getStringExtra("servicetypeContent");
				serviceTypeId = data.getStringExtra("servicetypeId");
				txtServicetype.setText(servicetypeContent);
			}
		}
       
        super.onActivityResult(requestCode, resultCode, data);  
    }
}
