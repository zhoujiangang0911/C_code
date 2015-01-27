package cn.wislight.publicservice.ui.user.goverment;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
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
import cn.wislight.publicservice.voip.CallInActivity;

/**
 * 处理中详细页界面
 * @author Administrator
 *
 */
public class UserCommonlyPersonDetailActivity extends BaseActivity implements OnClickListener{
	private TextView txtName;
	private TextView txtPosition;	
	private TextView txtPhone;
	private TextView txtFixphone;
	private TextView txtVOIP;
	private TextView txtDelete;
	private TextView txtCallPhone;
	private TextView txtCallFixphone;
	private TextView txtCallVOIP;

	private String commonlypersonId;
	
	@Override
	public void setUpView() {
		setContentView(R.layout.activity_user_commonlyperson_detail);
		
		txtName = (TextView) findViewById(R.id.name);
		txtPosition = (TextView) findViewById(R.id.position);
		txtPhone = (TextView) findViewById(R.id.phone);
		txtFixphone = (TextView) findViewById(R.id.fixphone);
		txtVOIP = (TextView) findViewById(R.id.voip);
		
		txtDelete = (TextView) findViewById(R.id.delete);
		txtCallPhone = (TextView) findViewById(R.id.callphone);
		txtCallFixphone = (TextView) findViewById(R.id.callfixphone);
		txtCallVOIP = (TextView) findViewById(R.id.callvoip);		
		
		commonlypersonId = getIntent().getStringExtra(Constants.ID);
		fillData(commonlypersonId);

	}

	@Override
	public void setListener() {
		txtDelete.setOnClickListener(this);
		txtCallPhone.setOnClickListener(this);
		txtCallFixphone.setOnClickListener(this);
		txtCallVOIP.setOnClickListener(this);
	}
	
	public void fillData(String commonlypersonId){		
		String url = "publicservice/commonlyperson_detail.htm?json=true&commonlyperson.id="+commonlypersonId;
		System.out.println("wangting:"+url);
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler() {
			

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				// loadingdiag.hide();
				Toast.makeText(UserCommonlyPersonDetailActivity.this,
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
						JSONObject commonlyperson = jsonObject.getJSONObject("commonlyperson");	
						JSONObject person = commonlyperson.getJSONObject("person");
						if(!person.isNull("username")){
							txtName.setText(person.getString("username"));
						} else {
							txtName.setText(person.getString("loginname"));
						}
						if(!person.isNull("position")){
							txtPosition.setText(person.getString("position"));
						}else {
							txtPosition.setText("");
						}
						if(!person.isNull("phone")){
							txtPhone.setText(person.getString("phone"));
						}else {
							txtPhone.setText("");
						}
						if(!person.isNull("fixphone")){
							txtFixphone.setText(person.getString("fixphone"));
						}else {
							txtFixphone.setText("");
						}
						if(!person.isNull("voip")){
							txtVOIP.setText(person.getString("voip"));
						}else {
							txtVOIP.setText("");
						}	
											
					}else {
						Toast.makeText(UserCommonlyPersonDetailActivity.this,"未查询信息", Toast.LENGTH_SHORT)
								.show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
					// loadingdiag.hide();
					Toast.makeText(UserCommonlyPersonDetailActivity.this,
							getString(R.string.error_network),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	
	}

	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.delete:
			deleteCommonlyPerson(commonlypersonId);			
			break;
		case R.id.callphone:
			String phonenumber = txtPhone.getText().toString();
			if(phonenumber ==null || phonenumber.trim().length()<1){
				Toast.makeText(UserCommonlyPersonDetailActivity.this,
						"电话号码为空", Toast.LENGTH_SHORT).show();
				return;
			}
			//用intent启动拨打电话  
            Intent intentPhone = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phonenumber));  
            startActivity(intentPhone);
			break;
		case R.id.callfixphone:
			String fixphonenumber = txtFixphone.getText().toString();
			if(fixphonenumber ==null || fixphonenumber.trim().length()<1){
				Toast.makeText(UserCommonlyPersonDetailActivity.this,
						"电话号码为空", Toast.LENGTH_SHORT).show();
				return;
			}
			//用intent启动拨打电话  
            Intent intentfixPhone = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+fixphonenumber));  
            startActivity(intentfixPhone);
			break;
		case R.id.callvoip:
			String voipnumber = txtVOIP.getText().toString();
			Toast.makeText(UserCommonlyPersonDetailActivity.this,
					voipnumber, Toast.LENGTH_SHORT).show();
			if(voipnumber ==null || voipnumber.trim().length()<1){
				Toast.makeText(UserCommonlyPersonDetailActivity.this,
						"网络电话号码为空", Toast.LENGTH_SHORT).show();
				return;
			}			
			Intent intentVOIP = new Intent(this, CallInActivity.class);   
			intentVOIP.putExtra("isIncomingCall", false);
			intentVOIP.putExtra("voip", voipnumber);
			//intentVOIP.putExtra("voip", "15691999278");
	        startActivity(intentVOIP); 			
			break;
			
			
		default:
				break;
		}
	}
		
	public void deleteCommonlyPerson(String commonlypersonId){
		String url = "publicservice/commonlyperson_delete.htm?json=true&commonlyperson.id="+commonlypersonId;
		System.out.println("wangting:"+url);
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler() {		

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				// loadingdiag.hide();
				Toast.makeText(UserCommonlyPersonDetailActivity.this,
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
						Toast.makeText(UserCommonlyPersonDetailActivity.this,"取消常用联系人成功", Toast.LENGTH_SHORT)
						.show();
						finish();				
					}else {
						Toast.makeText(UserCommonlyPersonDetailActivity.this,"取消常用联系人失败", Toast.LENGTH_SHORT)
								.show();
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
					// loadingdiag.hide();
					Toast.makeText(UserCommonlyPersonDetailActivity.this,
							getString(R.string.error_network),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	
	}
	
}
