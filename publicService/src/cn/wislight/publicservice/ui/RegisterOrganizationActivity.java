package cn.wislight.publicservice.ui;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpResponseHandler;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import cn.wislight.publicservice.R;
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.util.PublicServiceClient;

/**
 * 
 * 机构注册界面
 * @author Administrator
 *
 */
public class RegisterOrganizationActivity extends BaseActivity implements OnClickListener{
	private ImageButton register;
	private EditText txtPhone;
	private ImageView btnGetVerifyCode;
	@Override
	public void setUpView() {
		setContentView(R.layout.activity_register_organization);
		
		register=(ImageButton)findViewById(R.id.register);
		txtPhone = (EditText) findViewById(R.id.phone);
		btnGetVerifyCode = (ImageView) findViewById(R.id.getVerifyCode); 
		
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
			if(phone == null || phone.length()==0){
				Toast.makeText(this, "手机号码为空", 100).show();
				return;
			}
			getVerifyCode(phone);
			break;
		case R.id.register:
			gotoActivity(LoginActivity.class, false);
			break;

		default:
			break;
		}
	}
	
	
	private void getVerifyCode(String phone){
		String url = "publicservice/publicServiceAction_getCodes.htm?json=true&phone=" +phone;
		
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] arg1, byte[] body,
					Throwable error) {
				//loadingdiag.hide();
				Toast.makeText(RegisterOrganizationActivity.this,
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
						Toast.makeText(RegisterOrganizationActivity.this, "验证码已发送，请注意查收",
								100).show();						
					} else{
						Toast.makeText(RegisterOrganizationActivity.this, "验证码发送失败",
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
