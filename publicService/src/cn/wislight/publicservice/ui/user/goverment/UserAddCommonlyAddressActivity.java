package cn.wislight.publicservice.ui.user.goverment;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpResponseHandler;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import cn.wislight.publicservice.R;
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.ui.user.AddNewLocationActivity;
import cn.wislight.publicservice.util.PublicServiceClient;

/**
 * 常用地点
 * @author Administrator
 *
 */
public class UserAddCommonlyAddressActivity extends BaseActivity implements OnClickListener{
	private ImageButton addlocation;
	private EditText newaddress;
	@Override
	public void setUpView() {
		setContentView(R.layout.activity_user_add_commonly_address);
		
		addlocation=(ImageButton) findViewById(R.id.addlocation);
		newaddress=(EditText) findViewById(R.id.newaddress);
	}

	@Override
	public void setListener() {
		addlocation.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.addlocation:
			//gotoActivity(AddNewLocationActivity.class, false);
			String address = newaddress.getText().toString();
			if(address ==null || address.trim().length()<1){
				Toast.makeText(UserAddCommonlyAddressActivity.this, "地址不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			addAddress(address);
			break;

		default:
			break;
		}
	}

	public void addAddress(String addressname){		
		String url = "publicservice/address_addCommonlyAddress.htm?json=true&address.name="+addressname;
		System.out.println("wangting:"+url);
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler() {		

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				// loadingdiag.hide();
				Toast.makeText(UserAddCommonlyAddressActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				try {
					JSONObject jsonObject = new JSONObject(response);
					String result = jsonObject.getString("result");
					if (result.equals("success")){
						Toast.makeText(UserAddCommonlyAddressActivity.this,"添加地址成功", Toast.LENGTH_SHORT)
						.show();	
						finish();
					}else {
						Toast.makeText(UserAddCommonlyAddressActivity.this,"添加地址失败", Toast.LENGTH_SHORT)
								.show();
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
					// loadingdiag.hide();
					Toast.makeText(UserAddCommonlyAddressActivity.this,
							getString(R.string.error_network),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	
	}
}
