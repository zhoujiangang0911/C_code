package cn.wislight.meetingsystem.ui;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import cn.wislight.meetingsystem.MainActivity;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.service.SettingsConfig;
import cn.wislight.meetingsystem.ui.setting.ChangePasswordActivity;
import cn.wislight.meetingsystem.ui.setting.NetworkSetActivity;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.IntentUtil;
import cn.wislight.meetingsystem.util.LoadingDialog;
import cn.wislight.meetingsystem.util.MD5Str;
import cn.wislight.meetingsystem.util.MeetingSystemClient;
import cn.wislight.meetingsystem.util.Variables;
import cn.wislight.meetingsystem.voip.CCPHelper;

import com.loopj.android.http.AsyncHttpResponseHandler;

import cn.wislight.meetingsystem.util.LoadingDialog;

/**
 * @author Administrator 登陆界面
 */
public class LoginActivity extends BaseActivity implements OnClickListener {

	private ImageButton mIbtnLogin;
	private ImageButton mIbtnRegister;
	private ImageButton mIbtnForPwd;
	private TextView loginName;
	private TextView password;
	private CheckBox cbSavePassword;
	private SettingsConfig config;

	private LoadingDialog loadingdiag;

	@Override
	public void setupView() {
		setContentView(R.layout.login_main);

	}

	@Override
	public void initView() {
		loginName = (TextView) findViewById(R.id.et_login_account);
		password = (TextView) findViewById(R.id.et_login_pwd);

		mIbtnLogin = (ImageButton) findViewById(R.id.ibtn_login_login);
		mIbtnRegister = (ImageButton) findViewById(R.id.ibtn_login_register);
		mIbtnForPwd = (ImageButton) findViewById(R.id.ibtn_login_forget_pwd);
		cbSavePassword = (CheckBox) findViewById(R.id.cb_save_password);

		loadingdiag = new LoadingDialog(this);
		loadingdiag.setCanceledOnTouchOutside(false);
		loadingdiag.setText(getString(R.string.loading));

		config = new SettingsConfig(this);
		String u = config.getUsername();
		String p = config.getPassword();

		if (Constants.DEBUG_VERSION) {
			if (!"".equals(u)) {
				loginName.setText(u);
				password.setText(p);
			}
		} else {
			loginName.setText(u);
			password.setText(p);
		}

		cbSavePassword.setChecked(!"".equals(password.getText().toString()));

		initListener();
	}

	private void initListener() {

		mIbtnRegister.setOnClickListener(this);
		mIbtnLogin.setOnClickListener(this);
		mIbtnForPwd.setOnClickListener(this);
		cbSavePassword.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ibtn_login_login:
			checkLogin(loginName.getText().toString(), password.getText()
					.toString());
			// checkLoginMock();
			loadingdiag.show();
			break;
		case R.id.ibtn_login_forget_pwd:
			doIntent(ForgetPasswordActivity.class);
			break;
		case R.id.ibtn_login_register:
			doIntent(registerActivity.class);
			break;
		case R.id.cb_save_password:

			break;
		default:
			break;
		}
	}

	private void doIntent(Class<? extends Activity> clazz) {
		IntentUtil.startActivity(this, clazz);
	}

	public void clickNetworkSetting(View view) {
		IntentUtil.startActivity(this, NetworkSetActivity.class);
	}

	private void checkLoginMock() {
		Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
		Toast.makeText(LoginActivity.this, "正在载入数据...", 100).show();
		startActivity(mainIntent);
		finish();
	}

	private void checkLogin(String name, String password) {
		config.setUsername(name);
		if (cbSavePassword.isChecked()) {
			config.setPassword(password);
		} else {
			config.setPassword("");
		}

		String md5password = MD5Str.MD5(password);
		// String url =
		// "http://192.168.1.111:8080/MeetingManage/mobile/login.action?username=admin&password=A65DAA2D77588F2FB99257B639871940&mobile_userId=111111&loginType=admin";
		String url = "MeetingManage/mobile/login.action?username=" + name
				+ "&password=" + md5password
				+ "&mobile_userId=111111&loginType=admin";
		System.out.println("wangting" + url + ";base_url"
				+ MeetingSystemClient.getBASE_URL());
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();
				Toast.makeText(LoginActivity.this,
						getString(R.string.error_network), 100).show();
				System.out.println("wangting: login failed,"
						+ error.getMessage());
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				loadingdiag.hide();
				String response = new String(body);
				// Toast.makeText(LoginActivity.this,new String(response) +
				// "wangting" + response.toString(), 100).show();
				System.out.println("login response=" + response);
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(response);
					if (jsonObject.getBoolean("loginSuccess")) {
						JSONObject userInfo = jsonObject
								.getJSONObject("userEntity");
						if (userInfo != null) {
							String subAccountSid = userInfo
									.getString("subAccountSid");
							String subToken = userInfo.getString("subToken");
							String voipAccount = userInfo
									.getString("voipAccount");
							String voipPwd = userInfo.getString("voipPwd");
							if (subAccountSid.length() > 0
									&& subToken.length() > 0
									&& voipAccount.length() > 0
									&& voipPwd.length() > 0) {
								CCPHelper.setSUB_ID(subAccountSid);
								CCPHelper.setSUB_PWD(subToken);
								CCPHelper.setVOIP_ID(voipAccount);
								CCPHelper.setVOIP_PSW(voipPwd);
							}

							Variables.loginname = userInfo
									.getString("loginname");
							try {
								JSONArray authArray = userInfo
										.getJSONArray("bAuth");
								int length = authArray.length();
								if (length <= 0) {
									Variables.auth = new int[300];
								} else {

									int[] Auth = new int[length];
									for (int i = 0; i < length; i++) {
										Object temp = authArray.get(i);
										Auth[i] = (Integer) temp;
									}
									Variables.auth = Auth;
								}
							} catch (Exception e) {
								e.printStackTrace();
								int[] Auth = new int[300];
								Variables.auth = Auth;
							}
						}
						Intent mainIntent = new Intent(LoginActivity.this,
								MainActivity.class);
						// Toast.makeText(LoginActivity.this,
						// "loginUseAsyncClient", 100).show();
						startActivity(mainIntent);
						finish();
					} else {
						Toast.makeText(LoginActivity.this,
								jsonObject.getString("errorMsg"), 100).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});

	}

}
