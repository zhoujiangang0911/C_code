package cn.wislight.publicservice.ui.commercialtenant;

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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.loopj.android.http.AsyncHttpResponseHandler;

import cn.wislight.publicservice.R;
import cn.wislight.publicservice.adapter.BusinessAffairListAdapter;
import cn.wislight.publicservice.adapter.IMMessageHistoryListAdapter;
import cn.wislight.publicservice.adapter.IMMessageListAdapter;
import cn.wislight.publicservice.adapter.NegotiationListAdapter;
import cn.wislight.publicservice.adapter.ServiceTypeListAdapter;
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.entity.BusinessAffair;
import cn.wislight.publicservice.entity.Negotiation;
import cn.wislight.publicservice.ui.commercialtenant.DetailNegotiateActivity;
import cn.wislight.publicservice.ui.commercialtenant.FindBuinessActivity;
import cn.wislight.publicservice.util.Constants;
import cn.wislight.publicservice.util.PublicServiceClient;


/**
 * 事物处理中
 * @author Administrator
 *
 */
public class SelectServiceTypeActivity extends BaseActivity {
	private List datalist;
	private ServiceTypeListAdapter adapter;
	private ListView listview;
	
	@Override
	public void setUpView() {
		setContentView(R.layout.activity_select_servicetype);
		
		listview = (ListView) this.findViewById(R.id.servicetype_listview);
		listview.setDividerHeight(0);
		datalist = new ArrayList();		
		adapter = new ServiceTypeListAdapter(this);
		adapter.setData(datalist);
		listview.setAdapter(adapter);
    	listview.setOnItemClickListener(new OnItemClickListener(){	 
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                Intent intent=new Intent();  
                
                intent.putExtra("servicetypeId",   (String) ((Map) datalist.get(arg2)).get("id"));
                intent.putExtra("servicetypeContent",   (String) ((Map) datalist.get(arg2)).get("content"));
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
		String url = "publicservice/servicetype_list.htm?json=true";
		System.out.println("wangting:" + url);
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				// loadingdiag.hide();
				Toast.makeText(SelectServiceTypeActivity.this,
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
							map.put("content", jsonObject.getString("content"));
							map.put("id", jsonObject.getString("id"));
							map.put("createtime",jsonObject.getString("createtime"));
							datalist.add(map);
						}
						adapter.notifyDataSetChanged();
						
					} else {
						Toast.makeText(SelectServiceTypeActivity.this, "初始化失败",
								Toast.LENGTH_SHORT).show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
					// loadingdiag.hide();
					Toast.makeText(SelectServiceTypeActivity.this,
							getString(R.string.error_network),
							Toast.LENGTH_SHORT).show();
				}
			}
		});

	}
	
}
