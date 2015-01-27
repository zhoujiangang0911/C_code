package cn.wislight.publicservice.ui.governmentaffairs;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.wislight.publicservice.R;
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.util.Constants;
import cn.wislight.publicservice.util.PublicServiceClient;

public class ServantGovermentAffairTransferActivity extends BaseActivity implements OnClickListener{
	private EditText txtNewReceiverName;
	private Button btnChooseServant;	
	private Button btnTransfer;
	
	private String govermentAffairId;
	private String newReceiverId;
	@Override
	public void setUpView() {
		setContentView(R.layout.activity_servant_govermentaffair_transfer);	
		
		txtNewReceiverName = (EditText) findViewById(R.id.newReceiverName);
		btnChooseServant=(Button)findViewById(R.id.chooseServant);
		btnTransfer=(Button)findViewById(R.id.transfer);		
		govermentAffairId = this.getIntent().getStringExtra("govermentAffairId");
	}

	@Override
	public void setListener() {
		btnChooseServant.setOnClickListener(this);
		btnTransfer.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.chooseServant:
			String newReceiverName = txtNewReceiverName.getText().toString();
			Intent intent = new Intent(this, ServantGovermentAffairFindServantListActivity.class);
			intent.putExtra("newReceiverName", newReceiverName);
			startActivityForResult(intent, Constants.REQUEST_FINDSERVANT);
			break;
		case R.id.transfer:
			transfer();
			break;

		default:
			break;
		}
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
		if(resultCode == Activity.RESULT_OK){
			if(requestCode == Constants.REQUEST_FINDSERVANT){
				newReceiverId = data.getStringExtra("newReceiverId");
				String newReceiverName = data.getStringExtra("newReceiverName");
				if(newReceiverName !=null && newReceiverName.length()>0){
					txtNewReceiverName.setText(newReceiverName);
				} else {
					txtNewReceiverName.setText(newReceiverId);
				}				
			}			
		}       
        super.onActivityResult(requestCode, resultCode, data);  
    }
	
	private void transfer(){
		if(newReceiverId ==null || newReceiverId.length()<=0){
			Toast.makeText(this, "人员不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		String url = "publicservice/govermentaffair_transfer.htm?json=true&govermentaffair.id="+govermentAffairId+"&govermentaffair.receiverid="+newReceiverId;
		System.out.println("wangting:"+url);
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler() {		

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				// loadingdiag.hide();
				Toast.makeText(ServantGovermentAffairTransferActivity.this,
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
						Toast.makeText(ServantGovermentAffairTransferActivity.this,"转交成功", Toast.LENGTH_SHORT)
						.show();	
						finish();
					}else {
						Toast.makeText(ServantGovermentAffairTransferActivity.this,"转交失败", Toast.LENGTH_SHORT)
								.show();
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
					// loadingdiag.hide();
					Toast.makeText(ServantGovermentAffairTransferActivity.this,
							getString(R.string.error_network),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	
	}	
}
