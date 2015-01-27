package cn.wislight.meetingsystem.ui;



import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.CountDownTimer;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.domain.GroupElement;
import cn.wislight.meetingsystem.ui.topic.TopicSelectOrgActivity;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.Element;
import cn.wislight.meetingsystem.util.IntentUtil;
import cn.wislight.meetingsystem.util.LoadingDialog;
import cn.wislight.meetingsystem.util.MD5Str;
import cn.wislight.meetingsystem.util.MeetingSystemClient;
import cn.wislight.meetingsystem.util.Variables;

/**
 * @author Administrator
 * 注册界面
 */
public class registerActivity extends BaseActivity implements OnClickListener {
	private EditText editAccount;
	private EditText editVerifyCode;
	private EditText editIdentifyNumber;
	private EditText editOrgNumber;
	private EditText editPassword;
	private EditText editPasswordAgain;
	private TextView testVoipRegister;

	private TextView tvRegisterVerifys;
	private TextView tvVoipRegister;
	private int timerCount = 0;

	
	private String verifyCode;
	private String account_;
	
	private String orgNo;
	private String orgFullName = "";
	private LoadingDialog loadingdiag;
	@Override
	public void initView() {
		// TODO Auto-generated method stub

		editAccount = (EditText)findViewById(R.id.et_register_account);
		editVerifyCode = (EditText)findViewById(R.id.et_register_verify_code);
		editIdentifyNumber = (EditText)findViewById(R.id.et_register_identify_number);
		editOrgNumber  = (EditText)findViewById(R.id.et_register_org_number);
		editPassword = (EditText)findViewById(R.id.et_register_new_pwd);
		editPasswordAgain = (EditText)findViewById(R.id.et_register_new_pwd_again);
		testVoipRegister  = (TextView)findViewById(R.id.et_voip_register);
		editOrgNumber.setOnClickListener(this);

		editAccount.setInputType(InputType.TYPE_CLASS_PHONE);
		editAccount.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});  
		
		tvRegisterVerifys = (TextView)findViewById(R.id.tv_register_verifys);
		tvVoipRegister = (TextView)findViewById(R.id.et_voip_register);
		
		loadingdiag = new LoadingDialog(this);
		loadingdiag.setCanceledOnTouchOutside(false);
		
		Variables.loginname = "default_null";
	}

	@Override
	public void setupView() {
		setContentView(R.layout.register_main);

	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data)  
    {  
        if(Constants.CODE_SELECT_ORG == requestCode )  
        {  
        	if (Constants.OK == resultCode && null != data){
        		orgFullName = data.getExtras().getString(Constants.ORG_FNAME); 
        		orgNo = data.getExtras().getString(Constants.ORG_NO);
        		editOrgNumber.setText(orgNo);

        	}
            
        }  
        super.onActivityResult(requestCode, resultCode, data);  
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
//		verifyCode="1234";
		//IntentUtil.startActivityForResult(this, DpTpActivity.class, Constants.CODE_END_TIME);
		String url = "MeetingManage/mobile/getVerificationcode.action?telephone="+ account
				 + "&type=1";
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
				Toast.makeText(registerActivity.this,
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
					Toast.makeText(registerActivity.this, "验证码已发送。请注意查收。", Toast.LENGTH_SHORT).show();
					
					delayRetry();
					
					
					
				}else{
					Toast.makeText(registerActivity.this, "验证码发送失败", Toast.LENGTH_SHORT).show();
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
	
	public void clickVoipRegister(View view){
		if (timerCount > 0){
			return;
		}
		
		String account = editAccount.getText().toString();
		
		if(account == null || account.trim().length() != 11 ){
			Toast.makeText(this, "请输入正确的手机号码", 100).show();
			return;
		}
		//Toast.makeText(this, "获取验证码", 100).show();
//		verifyCode="1234";
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
				Toast.makeText(registerActivity.this,
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
					Toast.makeText(registerActivity.this, "验证码已发送。请注意查收。", Toast.LENGTH_SHORT).show();
					
					delayRetry();
				}else{
					Toast.makeText(registerActivity.this, "验证码已发送。请注意查收。", Toast.LENGTH_SHORT).show();
				}
			}
		});		
	}
	
	public void clickSubmit(View view){	
		String account = editAccount.getText().toString();
		if (account == null || account.trim().length()==0){
			Toast.makeText(this, "手机号不能为空", 100).show();
			return;
		}
		
		
		String identifyNumber = editIdentifyNumber.getText().toString();
		/*
		if (identifyNumber == null || identifyNumber.trim().length() != 18){
			Toast.makeText(this, "身份证号码不对", 100).show();
			return;
		}		*/
		identifyNumber = encode(identifyNumber);
		
		
		String orgNumber = editOrgNumber.getText().toString();
		
		if(orgNumber == null || orgNumber.trim().length() == 0){
			Toast.makeText(this, "机构代码不能为空", 100).show();
			return;
		}
		
		String password  = editPassword.getText().toString();
		String passwordAgain = editPasswordAgain.getText().toString();
		if(password == null || password.trim().length() == 0 ||password.trim().length() < 6){
			Toast.makeText(this, "密码不能为空，且不少于6位", 100).show();
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
				
		String url = "MeetingManage/mobile/register.action?account="+ account
			     + "&password=" + password
			     + "&identifyNumber=" + identifyNumber
			     + "&orgNubmer=" + orgNumber
			     + "&verifyCode=" + verifyCode;
		//loadingdiag.setText(getString(R.string.uploading));
		loadingdiag.show();  
		System.out.println("wangting"+url);
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();  
				String response = error.getMessage();
				System.out.println("wangting"+response);
				Toast.makeText(registerActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
				//finish();
			}
	
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				loadingdiag.hide();
				String response = new String(body);
				System.out.println("wangting"+response);
				
				try {
					JSONObject json = new JSONObject(response);
					
					String msg = json.getString("msg");
					int result = json.getInt("verify_result");
					account_ = json.getString("account");
					
					if (result == 1){
					    AlertDialog.Builder builder = new  AlertDialog.Builder(registerActivity.this);  
					    //builder.setIcon(R.drawable.alert_dialog_icon);  
					    builder.setTitle("注册成功"); 
					    builder.setMessage(msg);  
					    builder.setPositiveButton("确定",  
					            new DialogInterface.OnClickListener() {  
					                public void onClick(DialogInterface dialog, int whichButton) {  
					                	registerActivity.this.finish();
					                }
					            });  
					    builder.show();
						
					} else if (result == 0) {
						Toast.makeText(registerActivity.this, msg,  Toast.LENGTH_LONG).show();
					} else if (result == -1){
					    AlertDialog.Builder builder = new  AlertDialog.Builder(registerActivity.this);  
					    //builder.setIcon(R.drawable.alert_dialog_icon);  
					    builder.setTitle("认证失败"); 
					    builder.setMessage("是否进行人工认证？");  
					    builder.setPositiveButton("确定",  
					            new DialogInterface.OnClickListener() {  
					                public void onClick(DialogInterface dialog, int whichButton) {  
					                	ManualVerify(account_, 2);
					                }
					            });  
					    builder.setNegativeButton("取消",  
					            new DialogInterface.OnClickListener() {  
					                public void onClick(DialogInterface dialog, int whichButton) {  
					                }  
					            });  
					    builder.show();
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(registerActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
				}
			}
		});		
	}
	
	private void ManualVerify(
			String account, int state) {
		// TODO Auto-generated method stub
		
		String url = "MeetingManage/mobile/changeVerifyState.action?account="+ account + "&state_=" + state;
		loadingdiag.setText(getString(R.string.uploading));
		loadingdiag.show();  
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();  
				String response = error.getMessage();
				Toast.makeText(registerActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
				//finish();
			}
	
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				loadingdiag.hide();
				String response = new String(body);
				if (response.contains("success")){
					Toast.makeText(registerActivity.this, "人工认证请求已发送", Toast.LENGTH_SHORT).show();
					finish();
				}else {
					Toast.makeText(registerActivity.this, "人工认证请求发送失败", Toast.LENGTH_SHORT).show();
				}
			}
		});	
	} 
	
	
	private String encode(String source){
		char [] charArray = source.toCharArray();
		int length = charArray.length;
		for(int i=0;i<length;i++){
			charArray[i]=change(charArray[i]);		
		}
		return new String(charArray);
	}

	private char change(char source) {
		switch (source) {
		case '0':
			return '5';
		case '1':
			return '7';
		case '2':
			return '4';
		case '3':
			return '9';
		case '4':
			return '1';
		case '5':
			return '8';
		case '6':
			return '2';
		case '7':
			return '6';
		case '8':
			return '3';
		case '9':
			return '0';
		case 'x':
			return 'x';
		case 'X': 
			return 'X';
		default:
			return '0';

		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.et_register_org_number:
			Intent intent2 = new Intent();
			intent2.putExtra(Constants.ORG_FNAME, orgFullName);
			IntentUtil.startActivityForResult(this, TopicSelectOrgActivity.class, Constants.CODE_SELECT_ORG, intent2);			
			break;
		}
	}

}
