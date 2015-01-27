package cn.wislight.publicservice.ui.user.goverment;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.wislight.publicservice.R;
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.ui.LoginActivity;
import cn.wislight.publicservice.util.PublicServiceClient;
import cn.wislight.publicservice.util.StringToChange;

import com.loopj.android.http.AsyncHttpResponseHandler;
/**
 * 个人详细信息页面
 */
public class UserCenterSetMessageActivity extends BaseActivity implements OnClickListener{

	private LinearLayout userChangePasswordLinearLayout;

	private TextView txtLoginname;
	private TextView txtUsername;
	private TextView txtIdentity;
	private TextView txtPhone;
	private TextView txtFixphone;
	private TextView txtVoip;
	private TextView txtAddress;
	private Button backloginactivity;
	private String address;
	@Override
	public void setUpView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_usercenterset);
		userChangePasswordLinearLayout = (LinearLayout)findViewById(R.id.userChangePasswordLinearLayout);
		
		txtLoginname =  (TextView)findViewById(R.id.loginname);
		txtUsername =  (TextView)findViewById(R.id.username);
		txtIdentity =  (TextView)findViewById(R.id.identity);
		txtPhone =  (TextView)findViewById(R.id.phone);
		txtFixphone =  (TextView)findViewById(R.id.fixphone);
		txtVoip =  (TextView)findViewById(R.id.voip);
		txtAddress =  (TextView)findViewById(R.id.address);
		backloginactivity =(Button) findViewById(R.id.btn_backloginactivity);
		
		getLoginUserInfo();
	}
	
	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		userChangePasswordLinearLayout.setOnClickListener(this);
		backloginactivity.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.userChangePasswordLinearLayout:
			Intent intent = new Intent(this,UserChangePwdActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_backloginactivity:
			Intent intent2 = new Intent(this,LoginActivity.class);
			startActivity(intent2);
			break;
		default:
			break;
		}
	}

	private void getLoginUserInfo(){
		String url = "publicservice/publicServiceAction_getLoginUserInfo.htm?json=true";

					PublicServiceClient.get(url, null, new AsyncHttpResponseHandler() {
						@Override
						public void onFailure(int statusCode, Header[] arg1, byte[] body,
								Throwable error) {
							//loadingdiag.hide();
							Toast.makeText(UserCenterSetMessageActivity.this,
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
									JSONObject user = jsonObject.getJSONObject("user");
									if (!(user.isNull("administrativediversion"))) {
										JSONObject useraddress = user.getJSONObject("administrativediversion");
										address = useraddress.getString("fullname");
									}else {
										address="";
									}
									txtLoginname.setText(StringToChange.toKong(user.getString("loginname")));
									txtUsername.setText(StringToChange.toKong(user.getString("username")));
									txtIdentity.setText(StringToChange.toKong(user.getString("identity")));
									txtPhone.setText(StringToChange.toKong(user.getString("phone")));
									txtFixphone.setText(StringToChange.toKong(user.getString("fixphone")));
									txtVoip.setText(StringToChange.toKong(user.getString("voip")));
									txtAddress.setText(address);
									
								} else{
									Toast.makeText(UserCenterSetMessageActivity.this, "获取个人信息失败",
											100).show();
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});

	}
}
