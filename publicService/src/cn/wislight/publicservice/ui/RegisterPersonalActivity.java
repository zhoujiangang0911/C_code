package cn.wislight.publicservice.ui;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.wislight.publicservice.R;
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.entity.User;
import cn.wislight.publicservice.util.ConvertString;
import cn.wislight.publicservice.util.MD5Str;
import cn.wislight.publicservice.util.PublicServiceClient;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 
 * 个人注册界面
 * 
 * @author Administrator
 * 
 */
public class RegisterPersonalActivity extends BaseActivity implements
		OnClickListener {
	private ImageButton register;
	private EditText txtUsername;
	private EditText txtPhone;
	private EditText txtVerifyCode;
	private EditText txtPassword;
	private EditText txtPasswordAgain;
	private Button btnGetVerifyCode;
	private Button btnChoiceAddress;
	private int time = 60;
	private Handler mHandler = new Handler();
	private MyRunable myrunable = new MyRunable();
	private TextView tvAddress;
	private ListView addressList;
	private TextView textViewAddress;
	private String administrativediversionid ="";

	@Override
	public void setUpView() {
		setContentView(R.layout.activity_register_personal);
		register = (ImageButton) findViewById(R.id.register);
		
		
		textViewAddress = (TextView) findViewById(R.id.tv_address);
		txtUsername = (EditText) findViewById(R.id.loginname);
		txtPhone = (EditText) findViewById(R.id.phone);
		txtVerifyCode = (EditText) findViewById(R.id.verifycode);
		txtPassword = (EditText) findViewById(R.id.password);
		txtPasswordAgain = (EditText) findViewById(R.id.passwordagain);
		textViewAddress = (TextView) findViewById(R.id.tv_address);
		btnGetVerifyCode = (Button) findViewById(R.id.getVerifyCode);
		btnChoiceAddress = (Button) findViewById(R.id.btn_choiceaddress);
		btnChoiceAddress.setOnClickListener(this);
		btnGetVerifyCode.setOnClickListener(this);

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
				Toast.makeText(this, "手机号码为空或号码不正确", 100).show();
				return;
			}
			getVerifyCode(phone);
			break;
		case R.id.register:
			register();
			break;
		case R.id.btn_choiceaddress:
			Intent intent = new Intent(this,RegisterPersonChoiceAddress.class);
			startActivityForResult(intent,101);
			break;
		default:
			break;
		}
	}


	private void register() {
		String url = "publicservice/publicServiceAction_register.htm?json=true";
		RequestParams params = new RequestParams();
		String username = txtUsername.getText().toString();
		String convertusername = ConvertString.toSemiangle(username);
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
			Toast.makeText(this, "手机号码为空或号码不正确", 100).show();
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
		
		params.put("loginname", convertusername);
		params.put("username", convertusername);
		params.put("password", passwordMd5);
		params.put("phone", phone);
		params.put("usertype", User.USERTYPE.SENDER);// 个人用户
		params.put("user.administrativediversionid", administrativediversionid);
		// /loadingdiag.setText(getString(R.string.uploading));
		// loadingdiag.show();
		PublicServiceClient.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] arg1, byte[] body,
					Throwable error) {

				btnGetVerifyCode.setText("发送验证码");
				btnGetVerifyCode.setTextColor(Color.WHITE);
				btnGetVerifyCode.setEnabled(true);
				// loadingdiag.hide();
				Toast.makeText(RegisterPersonalActivity.this,
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
						Toast.makeText(RegisterPersonalActivity.this, "注册成功",
								100).show();
						gotoActivity(LoginActivity.class, false);
					} else if (result.equals("loginname_exist")) {
						Toast.makeText(RegisterPersonalActivity.this,
								"注册失败，用户名已存在", 100).show();

					} else {
						Toast.makeText(RegisterPersonalActivity.this, "注册失败",
								100).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	private void getVerifyCode(String phone) {
		// btnGetVerifyCode.setText("60S后重新发送");
		// btnGetVerifyCode.setTextColor(Color.RED);
		// btnGetVerifyCode.setEnabled(true);

		String url = "publicservice/publicServiceAction_getCodes.htm?json=true&phone="
				+ phone;
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] arg1, byte[] body,
					Throwable error) {
				// loadingdiag.hide();
				btnGetVerifyCode.setText("发送验证码");
				btnGetVerifyCode.setTextColor(Color.WHITE);
				btnGetVerifyCode.setEnabled(true);
				Toast.makeText(RegisterPersonalActivity.this,
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
						Toast.makeText(RegisterPersonalActivity.this,
								"验证码已发送，请注意查收", 100).show();
						btnGetVerifyCode.setText("60S后重新发送");
						btnGetVerifyCode.setTextColor(Color.RED);
						btnGetVerifyCode.setEnabled(false);
						mHandler.postDelayed(myrunable, 1000);
					} else {
						Toast.makeText(RegisterPersonalActivity.this,
								"验证码发送失败", 100).show();
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

	class MyRunable implements Runnable {
		@Override
		public void run() {
			time--;
			btnGetVerifyCode.setText(time + "S后重新发送");
			btnGetVerifyCode.setTextColor(Color.RED);
			btnGetVerifyCode.setEnabled(false);
			mHandler.postDelayed(myrunable, 1000);
			if (time == 0) {
				mHandler.removeCallbacks(myrunable);
				btnGetVerifyCode.setText("发送验证码");
				btnGetVerifyCode.setTextColor(Color.WHITE);
				btnGetVerifyCode.setEnabled(true);
				time = 60;
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			    
	            switch (requestCode) {
	    		case 101:
	    			if(resultCode == Activity.RESULT_OK){
	    				String addressName =data.getStringExtra("address");
	    				textViewAddress.setText("地址："+addressName);
	    				administrativediversionid = data.getStringExtra("id");
	    				Log.i("----", "----"+administrativediversionid);
	    			}
	    			break;
	    		default:
	    			break;
	    		}
		
	}
	
}
