package cn.wislight.publicservice.ui.user;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.loopj.android.http.AsyncHttpResponseHandler;

import cn.wislight.publicservice.R;
import cn.wislight.publicservice.adapter.CommonlyAddressListAdapter;
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.util.PublicServiceClient;


/**
 * 事物处理中
 * @author Administrator
 *
 */
public class UserBusinessSelectCommonlyAddressActivity extends BaseActivity {
	private List datalist;
	private CommonlyAddressListAdapter adapter;
	private ListView listview;
	
	
	@Override
	public void setUpView() {
		setContentView(R.layout.activity_user_business_select_commonly_address);
		
		listview = (ListView) this.findViewById(R.id.commonlyaddress_listview);
		listview.setDividerHeight(0);
		datalist = new ArrayList();		
		adapter = new CommonlyAddressListAdapter(this);
		adapter.setData(datalist);
		listview.setAdapter(adapter);
    	listview.setOnItemClickListener(new OnItemClickListener(){	 
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                Intent intent=new Intent();  
                intent.putExtra("addressId",   (String) ((Map) datalist.get(arg2)).get("id"));                
                intent.putExtra("addressName",   (String) ((Map) datalist.get(arg2)).get("name"));
                intent.putExtra("longitude",   (String) ((Map) datalist.get(arg2)).get("longitude"));
                intent.putExtra("latitude",   (String) ((Map) datalist.get(arg2)).get("latitude"));
                setResult(Activity.RESULT_OK, intent);
				finish();
            }
        });
    	

    	fillData();
	}

	@Override
	public void setListener() {

	}

	public void fillData() {
		String url = "publicservice/address_findCommonlyAddressList.htm?json=true";
		System.out.println("wangting:" + url);
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				// loadingdiag.hide();
				Toast.makeText(UserBusinessSelectCommonlyAddressActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				System.out.println("wangting: servicetype=" + response);
				try {
					JSONObject jsonResponse = new JSONObject(response);
					String result = jsonResponse.getString("result");
					if (result.equals("success")) {
						JSONArray jsonArray = jsonResponse.getJSONArray("list");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObject = (JSONObject) jsonArray.get(i);
							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("name", jsonObject.getString("name"));
							map.put("id", jsonObject.getString("id"));
							if(jsonObject.isNull("longitude")){
								map.put("longitude", "");
							}else {
								map.put("longitude", jsonObject.getString("longitude"));
							}
							if(jsonObject.isNull("latitude")){
								map.put("latitude", "");
							}else {
								map.put("latitude", jsonObject.getString("latitude"));
							}
							datalist.add(map);
						}
						adapter.notifyDataSetChanged();
						
					} else {
						Toast.makeText(UserBusinessSelectCommonlyAddressActivity.this, "初始化失败",
								Toast.LENGTH_SHORT).show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
					// loadingdiag.hide();
					Toast.makeText(UserBusinessSelectCommonlyAddressActivity.this,
							getString(R.string.error_network),
							Toast.LENGTH_SHORT).show();
				}
			}
		});

	}
	
}
