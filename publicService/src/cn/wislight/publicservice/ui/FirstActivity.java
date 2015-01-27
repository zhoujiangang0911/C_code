package cn.wislight.publicservice.ui;

import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpResponseHandler;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.Toast;
import cn.wislight.publicservice.PublicServiceApplication;
import cn.wislight.publicservice.R;
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.ui.governmentaffairs.ServantMainActivity;
import cn.wislight.publicservice.util.Constants;
import cn.wislight.publicservice.util.ConvertString;
import cn.wislight.publicservice.util.MD5Str;
import cn.wislight.publicservice.util.PublicServiceClient;
/*
 * 这个类用来做免登陆或者欢迎界面使用
 * 
 */
public class FirstActivity extends BaseActivity {
	private SharedPreferences loginrecord;
	@Override
	public void setUpView() {
		
		loginrecord = this.getSharedPreferences(Constants.LOGIN_RECORD, MODE_PRIVATE);
		String loginname = loginrecord.getString(Constants.LOGIN_NAME, "");
		String loginpassword  = loginrecord.getString(Constants.LOGIN_PASSWORD, "");
		String contverloginname = ConvertString.toSemiangle(loginname);
		if (loginpassword.length()==0) {
			gotoActivity(LoginActivity.class, true);
		}else{
			checkLogin(loginname, loginpassword);
		}
		
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub

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
				Toast.makeText(FirstActivity.this,
						getString(R.string.error_network), 100).show();
				System.out.println("wangting: login failed,"+ error.getMessage());
				Toast.makeText(FirstActivity.this,"网络异常，请重试", 100).show();
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
					
						if(usertype.equals("5") || usertype.equals("6")){
							gotoActivity(cn.wislight.publicservice.ui.commercialtenant.CommonServerActivity.class, false);
						}else if(usertype.equals("3")){
							//gotoActivity(ProxyHandleActivity.class, false);
							gotoActivity(ServantMainActivity.class, false);
						}else if(usertype.equals("1")){
							gotoActivity(cn.wislight.publicservice.ui.user.CommonServerActivity.class, false);
						}
						finish();
					} else {
						Toast.makeText(FirstActivity.this,"用户名或密码不对", 100).show();	
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});

	
	}
	
}
