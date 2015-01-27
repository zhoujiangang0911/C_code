package cn.wislight.meetingsystem.ui.setting;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpResponseHandler;



import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;


import cn.wislight.meetingsystem.util.MD5Str;
import cn.wislight.meetingsystem.util.MeetingSystemClient;

/**
 * @author Administrator
 * 修改密码
 */
public class ChangePasswordActivity extends BaseActivity {
 
	private EditText editAccount;
	private EditText editVerifyCode;
	private EditText editPassword;
	private EditText editPasswordAgain;
	private TextView testVoipChangePsw;

	private TextView tvRegisterVerifys;
	private TextView tvVoipRegister;
	private int timerCount = 0;
	
	@Override
	public void initView() {
		
		editAccount = (EditText)findViewById(R.id.et_change_pwd_account);
		editVerifyCode = (EditText)findViewById(R.id.et_change_pwd_verify_code);
		editPassword = (EditText)findViewById(R.id.et_charge_pwd_new_pwd);
		editPasswordAgain = (EditText)findViewById(R.id.et_charge_pwd_new_pwd_again);
		testVoipChangePsw  = (TextView)findViewById(R.id.et_voip_changepassword);
		
		tvRegisterVerifys = (TextView)findViewById(R.id.tv_change_pwd_verifys);
		tvVoipRegister = (TextView)findViewById(R.id.et_voip_changepassword);
	}
	
	@Override
	public void setupView() {
		setContentView(R.layout.change_pwd_main);
	}


	public void clickVerifys(View view){		
		
		if (timerCount > 0){
			return;
		}
		
		String account = editAccount.getText().toString();
		if(account == null || account.trim().length() != 11 ){
			Toast.makeText(this, "请输入正确的手机号码", 100).show();
			return;
		}
		Toast.makeText(this, "获取验证码", 100).show();

		String url = "MeetingManage/mobile/getVerificationcode.action?telephone="+ account+ "&type=1";
		//loadingdiag.setText(getString(R.string.uploading));
		//loadingdiag.show();  
		System.out.println("wangting"+url);
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				//loadingdiag.hide();  
				String response = error.getMessage();
				System.out.println("wangting"+response);
				Toast.makeText(ChangePasswordActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
				//finish();
			}
	
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				//loadingdiag.hide();
				String response = new String(body);
				System.out.println("wangting"+response);
				if (response.contains("0")){
					Toast.makeText(ChangePasswordActivity.this, "验证码已发送,请注意查收。", Toast.LENGTH_SHORT).show();
					delayRetry();
				}else{
					Toast.makeText(ChangePasswordActivity.this, "请点击重新发送。", Toast.LENGTH_SHORT).show();
				}
			}
		});		
	}
	
	private void delayRetry() {
		// TODO Auto-generated method stub
		timerCount = 45;
		tvRegisterVerifys.setText(timerCount + "");
		tvVoipRegister.setText(timerCount + "");
		
		new CountDownTimer(45000, 1000) {  
		    public void onTick(long millisUntilFinished) {  
		    	timerCount--;
		    	tvRegisterVerifys.setText(timerCount + "秒后可重试");
				tvVoipRegister.setText(timerCount + "秒后可重试");
		    }  
		    public void onFinish() {  
		    	timerCount = 0;
		    	
		    	tvRegisterVerifys.setText(getString(R.string.charge_pwd_verify));
				tvVoipRegister.setText(getString(R.string.login_voip_register));
		    }  
		 }.start();  
	}
	
    public void clickVoipChangePsw(View view){
		if (timerCount > 0){
			return;
		}
		
		String account = editAccount.getText().toString();
		if (account == null || account.trim().length()==0){
			Toast.makeText(this, "账号不能为空", 100).show();
			return;
		}		
		
		Toast.makeText(this, "获取验证码", 100).show();
        //verifyCode="1234";
		//IntentUtil.startActivityForResult(this, DpTpActivity.class, Constants.CODE_END_TIME);
		String url = "MeetingManage/mobile/getVerificationcode.action?telephone="+ account
				 + "&type=2";
		//loadingdiag.setText(getString(R.string.uploading));
		//loadingdiag.show();  
		System.out.println("wangting"+url);
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				//loadingdiag.hide();  
				String response = error.getMessage();
				System.out.println("wangting"+response);
				Toast.makeText(ChangePasswordActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
				//finish();
			}
	
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				//loadingdiag.hide();
				String response = new String(body);
				System.out.println("wangting"+response);
				if (response.contains("0")){
					
					Toast.makeText(ChangePasswordActivity.this, "验证码已发送。请注意查收。", Toast.LENGTH_SHORT).show();
					delayRetry();
					
				}else{
					Toast.makeText(ChangePasswordActivity.this, "请点击重新发送。", Toast.LENGTH_SHORT).show();
				}
			}
		});		
	}
	public void clickSubmit(View view){
		String account = editAccount.getText().toString();
		if (account == null || account.trim().length()==0){
			Toast.makeText(this, "账号不能为空", 100).show();
			return;
		}			
	
		String password  = editPassword.getText().toString();
		String passwordAgain = editPasswordAgain.getText().toString();
		if(password == null || password.trim().length() == 0){
			Toast.makeText(this, "密码不能为空", 100).show();
			return;
		}
		if (!password.equals(passwordAgain)){
			Toast.makeText(this, "密码不一致", 100).show();
			return;
		}
		password = MD5Str.MD5(password);		

		String verifyCode = editVerifyCode.getText().toString();	
		if(verifyCode == null || verifyCode.trim().length() == 0){
			Toast.makeText(this, "验证码不能为空", 100).show();
			return;
		}
		
		String url = "MeetingManage/mobile/retrievePassword.action?account="+ account
			     + "&password=" + password
			     + "&verifyCode=" + verifyCode;
		//loadingdiag.setText(getString(R.string.uploading));
		//loadingdiag.show();  
		System.out.println("wangting"+url);
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				//loadingdiag.hide();  
				String response = error.getMessage();
				System.out.println("wangting"+response);
				Toast.makeText(ChangePasswordActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
				//finish();
			}
	
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				//loadingdiag.hide();
				String response = new String(body);
				System.out.println("wangting"+response);
				if (response.contains("true")){
					Toast.makeText(ChangePasswordActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
					finish();
				}else{
					Toast.makeText(ChangePasswordActivity.this, "修改失败！", Toast.LENGTH_SHORT).show();
				}
			}
		});		

	}
	
}
