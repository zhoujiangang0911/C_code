package cn.wislight.meetingsystem.ui.setting;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.ui.home.TodayScheduleDetailActivity;
import cn.wislight.meetingsystem.util.LoadingDialog;
import cn.wislight.meetingsystem.util.MeetingSystemClient;
import cn.wislight.meetingsystem.util.LoadingDialog;
/**
 * @author Administrator
 * 修改密码
 */
public class MyInformationActivity extends BaseActivity {
	
	private TextView tvTel,tvZhiwu, tvDanwei, tvJuzhudi, tvName,tvId,tvZhengzhimianmao;
	private LoadingDialog loadingdiag;
	@Override
	public void initView() {
		// TODO Auto-generated method stub

		tvTel = (TextView) findViewById(R.id.system_setting_myinformation_tel);
		tvZhiwu = (TextView) findViewById(R.id.system_setting_myinformation_zhiwu);
		tvDanwei = (TextView) findViewById(R.id.system_setting_myinformation_danwei);
		tvJuzhudi = (TextView) findViewById(R.id.system_setting_myinformation_juzhudi);
		tvName = (TextView) findViewById(R.id.system_setting_myinformation_name);
		tvId = (TextView) findViewById(R.id.system_setting_myinformation_id);
		tvZhengzhimianmao = (TextView) findViewById(R.id.system_setting_myinformation_zhengzhimianmao);

/*		Intent intent = getIntent();
	    meetid = intent.getStringExtra("id");*/
		
		loadingdiag = new LoadingDialog(this);  
		loadingdiag.setCanceledOnTouchOutside(false); 
		loadingdiag.setText(getString(R.string.loading));
		
		
		getMyInfoDetail();

	}

	@Override
	public void setupView() {
		setContentView(R.layout.system_setting_myinformation);
	}
	
	private void getMyInfoDetail() {
		// TODO Auto-generated method stub
		String url = "MeetingManage/mobile/getMyInformation.action?telephone&zhiwu&danwei&juzhudi&name&id&zhengzhimianmao";
		loadingdiag.show();  
		System.out.println("wangting"+url);
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();  
				Toast.makeText(MyInformationActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
				System.out.println("wangting"+"fail"+error.getMessage());
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {				
				loadingdiag.hide();
				String response = new String(body);		
				
				if (response.contains("用户未登陆")){
					Toast.makeText(MyInformationActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				System.out.println("wangting"+"OK"+response);
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(response);				               
					tvTel.setText(jsonObject.getString("telephone"));
					tvZhiwu.setText(jsonObject.getString("zhiwu"));
					tvDanwei.setText(jsonObject.getString("danwei"));
					tvJuzhudi.setText(jsonObject.getString("juzhudi"));
					tvName.setText(jsonObject.getString("name"));
					tvId.setText(jsonObject.getString("id"));
					tvZhengzhimianmao.setText(jsonObject.getString("zhengzhimianmao"));

				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(MyInformationActivity.this,
							getString(R.string.error_dataabout),
							Toast.LENGTH_SHORT).show();
					
				}
			}
		});
	}

}
