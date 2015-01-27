package cn.wislight.publicservice.ui.governmentaffairs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import cn.wislight.publicservice.adapter.ServantHandingListAdapter;
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.ui.commercialtenant.DetailNegotiateActivity;
import cn.wislight.publicservice.ui.commercialtenant.FindBuinessActivity;
import cn.wislight.publicservice.util.Constants;
import cn.wislight.publicservice.util.PublicServiceClient;
import cn.wislight.publicservice.util.StringToChange;


/**
 * 事物处理中
 * @author Administrator
 *
 */
public class ServantHandlingListActivity extends BaseActivity {
	private List datalist;
	private ServantHandingListAdapter adapter;
	private ListView listview;
	private String negotiationId;


	@Override
	public void setUpView() {
		setContentView(R.layout.activity_servant_handing_list);
		
		listview = (ListView) this.findViewById(R.id.immessage_history_listview);
		listview.setDividerHeight(0);
		datalist = new ArrayList<Map<String, String>>();	

		fillData();
		adapter = new ServantHandingListAdapter(this);
		adapter.setData(datalist);
		listview.setAdapter(adapter);
    	listview.setOnItemClickListener(new OnItemClickListener(){	 
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                Intent intent=new Intent(ServantHandlingListActivity.this, ServantHandlingDetailActivity.class);  
                intent.putExtra(Constants.ID,   (String) ((Map) datalist.get(arg2)).get("id"));
                startActivity(intent);  
            }
        });
    	
    	negotiationId = getIntent().getStringExtra(Constants.ID);
    	//fillMessageHistoryData();
	}

	@Override
	public void setListener() {

	}
	
	private void fillData(){
		String url ="publicservice/govermentaffair_findHandling.htm?json=true";
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler(){
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				//loadingdiag.hide();
				Toast.makeText(ServantHandlingListActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				Log.i("---zhoujg", "---"+response);
				try {
					JSONObject result = new JSONObject(response);
					if(!"success".equals(result.getString("result"))){
						Toast.makeText(ServantHandlingListActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
						return;
					}
					JSONArray jsonArray = result.getJSONArray("list");
					Map<String, Object> map;
					datalist.clear();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArray.get(i);
						
						map = new HashMap<String, Object>();
						String content =  jsonObject.getString("content");
			              
			            map.put("content",content.equals("null")?"":content );
						map.put("id", jsonObject.getString("id"));
						map.put("price", jsonObject.getString("price"));
						map.put("score", jsonObject.getString("score"));
						map.put("senderid", jsonObject.getString("senderid"));
						map.put("sendtime", StringToChange.toNoT(jsonObject.getString("sendtime")));
						map.put("state", jsonObject.getString("state"));
						map.put("voicecontentid",jsonObject.getString("voicecontentid"));
						
						 String state = jsonObject.getString("state");
			              // if(state.equals("3"))
			               {
			            	   datalist.add(map);
			               } 
					}					
					adapter.notifyDataSetChanged();
				} catch (JSONException e) {
					e.printStackTrace();
					//loadingdiag.hide();
					Toast.makeText(ServantHandlingListActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
				}
			}
		});		
	}
}
