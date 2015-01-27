package cn.wislight.publicservice.ui.governmentaffairs;

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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.loopj.android.http.AsyncHttpResponseHandler;

import cn.wislight.publicservice.R;
import cn.wislight.publicservice.adapter.BusinessAffairListAdapter;
import cn.wislight.publicservice.adapter.IMMessageHistoryListAdapter;
import cn.wislight.publicservice.adapter.IMMessageListAdapter;
import cn.wislight.publicservice.adapter.NegotiationListAdapter;
import cn.wislight.publicservice.adapter.UserListAdapter;
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
public class ServantGovermentAffairFindServantListActivity extends BaseActivity {
	private List datalist;
	private UserListAdapter adapter;
	private ListView listview;
	
	private String newReceiverName;
	@Override
	public void setUpView() {
		setContentView(R.layout.activity_select_servants);
		
		newReceiverName = this.getIntent().getStringExtra("newReceiverName");
		
		listview = (ListView) this.findViewById(R.id.servants_listview);
		listview.setDividerHeight(0);
		datalist = new ArrayList();		
		adapter = new UserListAdapter(this);
		adapter.setData(datalist);
		listview.setAdapter(adapter);
    	listview.setOnItemClickListener(new OnItemClickListener(){	 
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                Intent intent=new Intent();  
                intent.putExtra("newReceiverId",   (String) ((Map) datalist.get(arg2)).get("id"));
                intent.putExtra("newReceiverName",   (String) ((Map) datalist.get(arg2)).get("content"));
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
		String url = "publicservice/publicServiceAction_findServantList.htm?json=true&user.username="+newReceiverName;
		System.out.println("wangting:" + url);
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				// loadingdiag.hide();
				Toast.makeText(ServantGovermentAffairFindServantListActivity.this,
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
							if(!jsonObject.isNull("username")){
								map.put("content", jsonObject.getString("username"));
							} else if (!jsonObject.isNull("loginname")){
								map.put("content", jsonObject.getString("loginname"));
							}
							map.put("id", jsonObject.getString("id"));
							datalist.add(map);
						}
						adapter.notifyDataSetChanged();
						
					} else {
						Toast.makeText(ServantGovermentAffairFindServantListActivity.this, "初始化失败",
								Toast.LENGTH_SHORT).show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
					// loadingdiag.hide();
					Toast.makeText(ServantGovermentAffairFindServantListActivity.this,
							getString(R.string.error_network),
							Toast.LENGTH_SHORT).show();
				}
			}
		});

	}
	
}
