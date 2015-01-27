package cn.wislight.publicservice.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import cn.wislight.publicservice.R;
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.util.PublicServiceClient;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class RegisterPersonChoiceAddress extends BaseActivity implements OnClickListener {
	private TextView textViewOK;
	private TextView tvAddress;
	private ListView listviewAddress;
	private Adapter listadapter;
	private List<Map<String, String>> list =new ArrayList<Map<String,String>>();
	private static final String TAG = "";
	private StringBuilder str = new StringBuilder();
	private String addressID="";
	@Override
	public void setUpView() {
		getDataAddress();
		setContentView(R.layout.activity_choice_address);
		textViewOK = (TextView) findViewById(R.id.tv_ok);
		listviewAddress = (ListView) findViewById(R.id.lv_address);
		tvAddress = (TextView) findViewById(R.id.tv_content);
		textViewOK.setOnClickListener(this);
	
		listadapter = new SimpleAdapter(this, list, 
				R.layout.address_item, new String[]{"address"},
				new int[] {R.id.tv_addresscontent});
		
		listviewAddress.setAdapter((ListAdapter) listadapter);
		listviewAddress.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
			}
		});
		listviewAddress.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
			    String addressId = list.get(arg2).get("id");
			    addressID = addressId;
			    String s = list.get(arg2).get("address");
			    str.append(s);
			    tvAddress.setText(str);
			    getDataAddressChilder(addressId);
				
			}
		});
		
	}
       
	
	private void getDataAddress(){
		String url = "publicservice/administrativediversion_findTopNodeList.htm?json=true";
		RequestParams params = new RequestParams();
		PublicServiceClient.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] arg1, byte[] body,
					Throwable error) {
				Toast.makeText(RegisterPersonChoiceAddress.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();

			}

			@Override
			public void onSuccess(int stautsCode, Header[] arg1, byte[] body) {
				 String response = new String(body);
				 Log.i(TAG, response);
				 try {
					JSONArray jsonArray = new JSONArray(response);
					list.clear();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArray.get(i);
						Map<String, String> map1 = new HashMap<String, String>();
						map1.put("address", jsonObject.getString("name"));
						map1.put("id", jsonObject.getString("id"));
						{
						list.add(map1);
						}
					}
					((BaseAdapter) listadapter).notifyDataSetChanged();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				 
			}

		});
	}
	
	private void getDataAddressChilder(String id){
		String url = "publicservice/administrativediversion_findChildren.htm?json=true&administrativediversion.id="+id;
		RequestParams params = new RequestParams();
		
		PublicServiceClient.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] arg1, byte[] body,
					Throwable error) {
				Toast.makeText(RegisterPersonChoiceAddress.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();

			}

			@Override
			public void onSuccess(int stautsCode, Header[] arg1, byte[] body) {
				 String response = new String(body);
				 Log.i(TAG, response);
				 try {
					JSONArray jsonArray = new JSONArray(response);
					
					list.clear();
					
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArray.get(i);
						Map<String, String> map1 = new HashMap<String, String>();
						map1.put("address", jsonObject.getString("name"));
						map1.put("id", jsonObject.getString("id"));
						list.add(map1);
					}
					((BaseAdapter) listadapter).notifyDataSetChanged();
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				 
			}

		});
	}
	@Override
	public void setListener() {
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.tv_ok:
			Intent intent = new Intent();
			intent.putExtra("address", str+"");
			intent.putExtra("id", addressID);
			this.setResult(Activity.RESULT_OK, intent);
			finish();
			break;
		default:
			break;
	}
		
	}

}
