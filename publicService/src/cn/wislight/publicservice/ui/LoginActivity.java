package cn.wislight.publicservice.ui;

import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import cn.wislight.publicservice.PublicServiceApplication;
import cn.wislight.publicservice.R;
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.entity.User;
import cn.wislight.publicservice.ui.governmentaffairs.ServantMainActivity;
import cn.wislight.publicservice.ui.user.ChangePwdActivity;
import cn.wislight.publicservice.util.Constants;
import cn.wislight.publicservice.util.ConvertString;
import cn.wislight.publicservice.util.MD5Str;
import cn.wislight.publicservice.util.PublicServiceClient;
import cn.wislight.publicservice.voip.CCPHelper;



/**
 * 
 * 登陆界面
 * @author Administrator
 *
 */
public class LoginActivity extends BaseActivity implements OnClickListener{
	private Button login;
	private TextView register,findpassword;
	private EditText username;
	private EditText password;
	private CheckBox cbSavePassword;
	private SharedPreferences loginrecord;
	private long mExitTime;
	@Override
	public void setUpView() {
		setContentView(R.layout.activity_login);

		login=(Button)findViewById(R.id.login);
		register=(TextView)findViewById(R.id.register);
		findpassword=(TextView)findViewById(R.id.findpassword);
		username=(EditText)findViewById(R.id.username);
		password=(EditText)findViewById(R.id.password);
		cbSavePassword = (CheckBox) findViewById(R.id.cb_save_password);

		loginrecord = this.getSharedPreferences(Constants.LOGIN_RECORD, MODE_PRIVATE);
		String loginname = loginrecord.getString(Constants.LOGIN_NAME, "");
		String loginpassword  = loginrecord.getString(Constants.LOGIN_PASSWORD, "");
		String contverloginname = ConvertString.toSemiangle(loginname);
		username.setText(contverloginname);
		password.setText(loginpassword);
		
	}

	@Override
	public void setListener() {
		login.setOnClickListener(this);
		register.setOnClickListener(this);
		findpassword.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login:
			/*
			if(username.getText().toString().equals("1")){
				gotoActivity(cn.wislight.publicservice.ui.commercialtenant.CommonServerActivity.class, false);
			}else if(username.getText().toString().equals("3")){
				gotoActivity(ProxyHandleActivity.class, false);
			}else{
				gotoActivity(CommonServerActivity.class, false);
			}
			*/
			
			if ((System.currentTimeMillis() - mExitTime) > 3000) {
				login.setEnabled(true);
				checkLogin(username.getText().toString(), password.getText().toString());
				mExitTime = System.currentTimeMillis();
			} else {
				login.setEnabled(false);
			}
			
						
			break;
		case R.id.register:
			gotoActivity(RegisgerMainActivity.class, false);
			break;
		case R.id.findpassword:
			gotoActivity(GetBackPwdActivity.class, false);
			break;
		default:
			break;
		}
	}

	private void checkLogin(String name, String password) {
		String contverloginname = ConvertString.toSemiangle(name);
		final String finalname = contverloginname;
		Log.i("---zhoujg77", finalname);
		final String finalpassword = password;
         
		String md5password = MD5Str.MD5(password);
		//String md5password = password;
		String url ="publicservice/publicServiceAction_login.htm?json=true&loginname="+contverloginname+"&password="+md5password;
		System.out.println("wangting:" + url + ";base_url"
				+ PublicServiceClient.getBASE_URL());
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,	Throwable error) {
				//loadingdiag.hide();
				Toast.makeText(LoginActivity.this,
						getString(R.string.error_network), 100).show();
				System.out.println("wangting: login failed,"+ error.getMessage());
				Toast.makeText(LoginActivity.this,"网络异常，请重试", 100).show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				//loadingdiag.hide();
				String response = new String(body);
				System.out.println("login response=" + response);
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(response);
					String result = jsonObject.getString("result");
					if (result.equals("success")){
						JSONObject user = jsonObject.getJSONObject("user");
						Map map = PublicServiceApplication.getInstance().getMap();
						map.put("loginuser", user);
						String usertype = user.getString("usertype");
						Editor editor = loginrecord.edit();
						editor.putString(Constants.LOGIN_NAME, finalname);
						if(cbSavePassword.isChecked()){
							editor.putString(Constants.LOGIN_PASSWORD, finalpassword);
						} else {
							editor.putString(Constants.LOGIN_PASSWORD, "");
						}
						editor.commit();
						
						initVOIP(user);
						if(usertype.equals(User.USERTYPE.BUSINESSCOMPANY) || usertype.equals(User.USERTYPE.BUSINESSMAN)){
							gotoActivity(cn.wislight.publicservice.ui.commercialtenant.CommonServerActivity.class, false);
						}else if(usertype.equals(User.USERTYPE.SERVANT)){
							//gotoActivity(ProxyHandleActivity.class, false);
							gotoActivity(ServantMainActivity.class, false);
						}else if(usertype.equals(User.USERTYPE.SENDER)){
							gotoActivity(cn.wislight.publicservice.ui.user.CommonServerActivity.class, false);
						}else{
							Toast.makeText(LoginActivity.this,"此类型用户无权使用系统", 100).show();
							finish();
						}
					} else {
						Toast.makeText(LoginActivity.this,"用户名或密码不对", 100).show();
						/*
						if(username.getText().toString().equals("5")){
							gotoActivity(cn.wislight.publicservice.ui.commercialtenant.CommonServerActivity.class, false);
						}else if(username.getText().toString().equals("3")){
							gotoActivity(ProxyHandleActivity.class, false);
						}else{
							gotoActivity(cn.wislight.publicservice.ui.user.CommonServerActivity.class, false);
						}
						*/
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		});

	
	}
	
	
	private void initVOIP(JSONObject userInfo) {
		//String SUB_ID = "aaf98f894627423201464163e16f1131";
		//String SUB_PWD = "dd5bb46484ba4ca9aabd9b25c122c544";
		//String VOIP_ID = "81553500000002";
		//String VOIP_PWD = "wi2vy1a2";
		
		String SUB_ID = "b3a2e8fd628b11e4823dac853d9f54f2";
		String SUB_PWD = "1ae7630bf96f03580b4925767e7c717a";
		String VOIP_ID = "83128300000003";
		String VOIP_PWD = "Sn3YwAd8";

		if (userInfo != null) {
			try {
				String type = userInfo.getString("usertype");
				SUB_ID = userInfo.getString("subAccountSid");
				SUB_PWD = userInfo.getString("subToken");
				VOIP_ID = userInfo.getString("voip");
				VOIP_PWD = userInfo.getString("voipPwd");
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (SUB_ID.length() > 0 && SUB_PWD.length() > 0
					&& VOIP_ID.length() > 0 && VOIP_PWD.length() > 0) {
				CCPHelper.setSUB_ID(SUB_ID);
				CCPHelper.setSUB_PWD(SUB_PWD);
				CCPHelper.setVOIP_ID(VOIP_ID);
				CCPHelper.setVOIP_PSW(VOIP_PWD);
				CCPHelper.getInstance().createDevice();
			} else {
				Toast.makeText(this, "VOIP账号为空，无法初始化", 100).show();
			}
		}
	}
}
