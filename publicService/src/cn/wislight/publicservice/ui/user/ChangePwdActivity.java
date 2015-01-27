package cn.wislight.publicservice.ui.user;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import cn.wislight.publicservice.R;
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.ui.LoginActivity;
import cn.wislight.publicservice.ui.RegisterPersonalActivity;
import cn.wislight.publicservice.util.MD5Str;
import cn.wislight.publicservice.util.PublicServiceClient;

/**
 * 修改密码
 * @author Administrator
 *
 */
public class ChangePwdActivity extends BaseActivity implements OnClickListener{
	private ImageView btnGetVerifyCode;
	private EditText txtLoginname;
	private EditText txtPhone;
	private EditText txtVerifyCode;
	private EditText txtPassword;
	private EditText txtPasswordAgain;
	private ImageButton  comfirm;
	
	@Override
	public void setUpView() {
		setContentView(R.layout.activity_changepwd);		

		txtLoginname = (EditText) findViewById(R.id.loginname);
		txtVerifyCode = (EditText) findViewById(R.id.verifycode);
		txtPassword = (EditText) findViewById(R.id.password);
		txtPasswordAgain = (EditText) findViewById(R.id.passwordagain);
		btnGetVerifyCode = (ImageView) findViewById(R.id.getVerifyCode); 
		comfirm=(ImageButton)findViewById(R.id.comfirm);
	}

	@Override
	public void setListener() {
		comfirm.setOnClickListener(this);
		btnGetVerifyCode.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.getVerifyCode:
			String loginname = txtLoginname.getText().toString();
			if(loginname == null || loginname.length()==0){
				Toast.makeText(this, "登录名不能为空", 100).show();
				return;
			}
			getVerifyCodeByLoginname(loginname);
			break;
		case R.id.comfirm:
			changePassword();	
			break;

		default:
			break;
		}
	}
	
	
	
	private void getVerifyCodeByLoginname(String loginname){
		String url = "publicservice/publicServiceAction_getVerificationcodeByLoginname.htm?json=true&loginname=" +loginname;
		
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] arg1, byte[] body,
					Throwable error) {
				//loadingdiag.hide();
				Toast.makeText(ChangePwdActivity.this,
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
						Toast.makeText(ChangePwdActivity.this, "验证码已发送，请注意查收",
								100).show();						
					} else{
						Toast.makeText(ChangePwdActivity.this, "验证码发送失败",
								100).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}
	private void changePassword(){
		String url = "publicservice/publicServiceAction_changePasswordByLoginname.htm?json=true";
		RequestParams params = new RequestParams();
		String loginname = txtLoginname.getText().toString();
		//String phone = txtPhone.getText().toString();
		String verifyCode = txtVerifyCode.getText().toString();
		String password = txtPassword.getText().toString();		
		String passwordAgain = txtPasswordAgain.getText().toString();
		String passwordMd5 = MD5Str.MD5(password);
		
		if(loginname != null && loginname.trim().length() <1){
			Toast.makeText(this, "用户名不能为空", 100).show();
			return;
		}
		if(verifyCode != null && verifyCode.trim().length() <1){
			Toast.makeText(this, "验证码不能为空", 100).show();
			return;
		}
		if(password != null && password.trim().length() <1){
			Toast.makeText(this, "密码不能为空", 100).show();
			return;
		}
		if(!password.equals(passwordAgain)){
			Toast.makeText(this, "密码不一致", 100).show();
			return;
		}
		params.put("loginname", loginname);
		params.put("password", passwordMd5);
		params.put("verifyCode", verifyCode);	
		
		///loadingdiag.setText(getString(R.string.uploading));
		//loadingdiag.show();
		PublicServiceClient.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] arg1, byte[] body,
					Throwable error) {
				//loadingdiag.hide();
				Toast.makeText(ChangePwdActivity.this,
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
						Toast.makeText(ChangePwdActivity.this, "修改成功",
								100).show();
						finish();					
					} else{
						Toast.makeText(ChangePwdActivity.this, "修改失败",
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
