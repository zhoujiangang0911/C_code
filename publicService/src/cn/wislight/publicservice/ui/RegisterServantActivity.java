package cn.wislight.publicservice.ui;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.wislight.publicservice.R;
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.entity.User;
import cn.wislight.publicservice.ui.commercialtenant.BaiduMapSelectPositionActivity;
import cn.wislight.publicservice.ui.commercialtenant.SelectServiceTypeActivity;
import cn.wislight.publicservice.ui.user.CreateBusinessAffairActivity;
import cn.wislight.publicservice.util.Constants;
import cn.wislight.publicservice.util.MD5Str;
import cn.wislight.publicservice.util.PublicServiceClient;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 
 * 服务者注册界面
 * 
 * @author Administrator
 * 
 */
public class RegisterServantActivity extends BaseActivity implements
		OnClickListener {
	private ImageButton register;
	private EditText txtUsername;
	private EditText txtPhone;
	private EditText txtVerifyCode;
	private EditText txtPassword;
	private EditText txtPasswordAgain;
	private Button btnGetVerifyCode;
	private Button choicepost;
	private Button btnChoiceAddressDetails;
	private TextView textvietchiocepost;
	private TextView textAddressDetails;
	private int  time = 60;
	private Handler mHandler = new Handler();
	private MyRunable  myrunable = new MyRunable();
	private int chiocePostTypr = 0;
	private Button btnChoiceAddress;
	private TextView textViewAddress;
	private String administrativediversionid ="";
	private String serviceId="";
	private String addressName;
	private String longitude;
	private String latitude;
	private RelativeLayout relativeLayout;
	@Override
	public void setUpView() {
		setContentView(R.layout.activity_register_servant);
		register = (ImageButton) findViewById(R.id.register);
		relativeLayout = (RelativeLayout) findViewById(R.id.rl_servicetypelayout);
		txtUsername = (EditText) findViewById(R.id.loginname);
		txtPhone = (EditText) findViewById(R.id.phone);
		txtVerifyCode = (EditText) findViewById(R.id.verifycode);
		txtPassword = (EditText) findViewById(R.id.password);
		txtPasswordAgain = (EditText) findViewById(R.id.passwordagain);
		btnGetVerifyCode = (Button) findViewById(R.id.getVerifyCode);
		choicepost = (Button) findViewById(R.id.imgbtn_choicepost);
		textvietchiocepost = (TextView) findViewById(R.id.tv_chiocepost);
		btnChoiceAddress = (Button) findViewById(R.id.btn_choiceaddress);
		textViewAddress = (TextView) findViewById(R.id.tv_address);
		btnChoiceAddressDetails = (Button) findViewById(R.id.btn_choiceaddressdetails);
		textAddressDetails = (TextView) findViewById(R.id.tv_addressdetails);
		
		btnChoiceAddressDetails.setOnClickListener(this);
		choicepost.setOnClickListener(this);
		btnGetVerifyCode.setOnClickListener(this);
		btnChoiceAddress.setOnClickListener(this);

	}

	@Override
	public void setListener() {
		register.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.getVerifyCode:
			String phone = txtPhone.getText().toString();
			if (phone == null || phone.length() <8) {
				Toast.makeText(this, "手机号码错误", 100).show();
				return;
			}
			getVerifyCode(phone);
			break;
		case R.id.register:
			// gotoActivity(LoginActivity.class, false);
			register();
			break;
		case R.id.imgbtn_choicepost:
			Intent intent2 = new Intent(this,RegisterSelectServiceTypeActivity.class);
			startActivityForResult(intent2,103);
			break;
		case R.id.btn_choiceaddress:
			Intent intent = new Intent(this,RegisterPersonChoiceAddress.class);
			startActivityForResult(intent,102);
			break;
		case R.id.btn_choiceaddressdetails:
			Intent intent3 = new Intent(this,BaiduMapSelectPositionActivity.class);
			startActivityForResult(intent3, Constants.REQUEST_ADDRESS);
			break;
		default:
			break;
		}
	}



	private void register() {
		String url = "publicservice/publicServiceAction_register.htm?json=true";
		RequestParams params = new RequestParams();
		String username = txtUsername.getText().toString();
		String phone = txtPhone.getText().toString();
		String verifyCode = txtVerifyCode.getText().toString();
		String password = txtPassword.getText().toString();
		String passwordAgain = txtPasswordAgain.getText().toString();
		String passwordMd5 = MD5Str.MD5(password);
		String address = textViewAddress.getText().toString();
		if (address != null && address.trim().length() < 6) {
			Toast.makeText(this, "请选择地址", 100).show();
			return;
		}
		if (phone != null && phone.trim().length() < 8) {
			Toast.makeText(this, "手机号码错误", 100).show();
			return;
		}
		if (username != null && username.trim().length() < 6) {
			Toast.makeText(this, "用户名长度不够", 100).show();
			return;
		}
		if (verifyCode != null && verifyCode.trim().length() < 1) {
			Toast.makeText(this, "验证码不能为空", 100).show();
			return;
		}
		if (password != null && password.trim().length() < 6) {
			Toast.makeText(this, "密码长度不够", 100).show();
			return;
		}
		if (!password.equals(passwordAgain)) {
			Toast.makeText(this, "密码不一致", 100).show();
			return;
		}
		params.put("loginname", username);
		params.put("username", username);
		params.put("password ", passwordMd5);
		params.put("phone", phone);
		params.put("usertype", User.USERTYPE.SERVANT);//服务者
		params.put("servicetypeId", serviceId);
		params.put("user.administrativediversionid", administrativediversionid);
		params.put("user.address.name", addressName);
		params.put("user.address.longitude ", Double.parseDouble(longitude));
		params.put("user.address.latitude ", Double.parseDouble(latitude));
		// params.put("岗位", chiocePostTypr);

		// /loadingdiag.setText(getString(R.string.uploading));
		// loadingdiag.show();
		PublicServiceClient.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] arg1, byte[] body,
					Throwable error) {
				// loadingdiag.hide();
				Toast.makeText(RegisterServantActivity.this,
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
						Toast.makeText(RegisterServantActivity.this, "注册成功",
								100).show();
						gotoActivity(LoginActivity.class, false);
					} else if (result.equals("loginname_exist")) {
						Toast.makeText(RegisterServantActivity.this,
								"注册失败，用户名已存在", 100).show();

					} else {
						Toast.makeText(RegisterServantActivity.this, "注册失败",
								100).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}


	private void getVerifyCode(String phone){
//		btnGetVerifyCode.setText("60S后重新发送");
//		btnGetVerifyCode.setTextColor(Color.RED);
//		btnGetVerifyCode.setEnabled(true);
	
		String url = "publicservice/publicServiceAction_getCodes.htm?json=true&phone=" +phone;
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] arg1, byte[] body,
					Throwable error) {
				//loadingdiag.hide();
				btnGetVerifyCode.setText("发送验证码");
				btnGetVerifyCode.setTextColor(Color.WHITE);
				btnGetVerifyCode.setEnabled(true);
				Toast.makeText(RegisterServantActivity.this,
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
						Toast.makeText(RegisterServantActivity.this, "验证码已发送，请注意查收",
								100).show();
						btnGetVerifyCode.setText("60S后重新发送");
						btnGetVerifyCode.setTextColor(Color.RED);
						btnGetVerifyCode.setEnabled(false);
						mHandler.postDelayed(myrunable, 1000);
					} else{
						Toast.makeText(RegisterServantActivity.this, "验证码发送失败",
								100).show();
						btnGetVerifyCode.setText("发送验证码");
						btnGetVerifyCode.setTextColor(Color.WHITE);
						btnGetVerifyCode.setEnabled(true);
						
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}
	
	class MyRunable implements Runnable{
		@Override
		public void run() {
				time--;
				btnGetVerifyCode.setText(time+"S后重新发送");
				btnGetVerifyCode.setTextColor(Color.RED);
				btnGetVerifyCode.setEnabled(false);
				mHandler.postDelayed(myrunable, 1000);
				if (time==0) {
					mHandler.removeCallbacks(myrunable);
					btnGetVerifyCode.setText("发送验证码");
					btnGetVerifyCode.setTextColor(Color.WHITE);
					btnGetVerifyCode.setEnabled(true);
					time=60;
				}
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	            switch (requestCode) {
	    		case 102:
	    			if(resultCode == Activity.RESULT_OK){
	    				String addressName =data.getStringExtra("address");
	    				textViewAddress.setText("地址："+addressName);
	    				administrativediversionid = data.getStringExtra("id");
	    			}
	    			break;
	    		case 103:
	    			if(resultCode == Activity.RESULT_OK){
	    				 serviceId = data.getStringExtra("servicetypeid");
	    				textvietchiocepost.setText(data.getStringExtra("servicetypename"));
	    				relativeLayout.setVisibility(View.VISIBLE);
	    			}
	    			break;
	    		case 2:
	    			if(resultCode == Activity.RESULT_OK){
	    				addressName = data.getStringExtra("addressName");
	    				longitude = data.getStringExtra("longitude");
	    				latitude = data.getStringExtra("latitude");
	    				textAddressDetails.setText(addressName+"("+longitude+","+latitude+")");
	    			}
	    			break;
	    		default:
	    			break;
	    		}
		
	}
	
}
