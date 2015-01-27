package cn.wislight.publicservice.ui.user.goverment;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import cn.wislight.publicservice.R;
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.ui.GetBackPwdActivity;
import cn.wislight.publicservice.ui.LoginActivity;
import cn.wislight.publicservice.util.MD5Str;
import cn.wislight.publicservice.util.PublicServiceClient;

/**
 * 修改密码
 * @author Administrator
 *
 */
public class UserChangePwdActivity extends BaseActivity implements OnClickListener{
	private ImageButton  comfirm;
	private EditText txtOldpassword;
	private EditText txtNewpassword;
	private EditText txtNewpasswordAgain;
	@Override
	public void setUpView() {
		setContentView(R.layout.activity_user_changepwd);
		
		comfirm=(ImageButton)findViewById(R.id.comfirm);
		txtOldpassword=(EditText)findViewById(R.id.oldpassword);
		txtNewpassword=(EditText)findViewById(R.id.newpassword);
		txtNewpasswordAgain=(EditText)findViewById(R.id.newpasswordAgain);
	}

	@Override
	public void setListener() {
		comfirm.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.comfirm:
			changePasswordByOldPassword();
			break;

		default:
			break;
		}
	}
	private void changePasswordByOldPassword(){
		String url = "publicservice/publicServiceAction_changePasswordByOldPassword.htm?json=true";
		RequestParams params = new RequestParams();
		String oldpassword = txtOldpassword.getText().toString();
		String newpassword = txtNewpassword.getText().toString();		
		String newpasswordAgain = txtNewpasswordAgain.getText().toString();
		String oldpasswordMd5 = MD5Str.MD5(oldpassword);
		String newpasswordMd5 = MD5Str.MD5(newpassword);

		if(oldpassword != null && oldpassword.trim().length() <1){
			Toast.makeText(this, "旧密码不能为空", 100).show();
			return;
		}
		if(newpassword != null && newpassword.trim().length() <1){
			Toast.makeText(this, "新密码不能为空", 100).show();
			return;
		}
		if(!newpassword.equals(newpasswordAgain)){
			Toast.makeText(this, "新密码不一致", 100).show();
			return;
		}

		params.put("password", oldpasswordMd5);
		params.put("newpassword", newpasswordMd5);	
		
		///loadingdiag.setText(getString(R.string.uploading));
		//loadingdiag.show();
		PublicServiceClient.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] arg1, byte[] body,
					Throwable error) {
				//loadingdiag.hide();
				Toast.makeText(UserChangePwdActivity.this,
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
						Toast.makeText(UserChangePwdActivity.this, "修改成功",
								100).show();
						finish();					
					} else{
						Toast.makeText(UserChangePwdActivity.this, "修改失败",
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
