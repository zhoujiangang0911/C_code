package cn.wislight.publicservice.ui.commercialtenant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.loopj.android.http.AsyncHttpResponseHandler;
import cn.wislight.publicservice.R;
import cn.wislight.publicservice.adapter.IMMessageListAdapter;
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.util.Constants;
import cn.wislight.publicservice.util.PublicServiceClient;


/**
 * 事物处理中
 * @author Administrator
 *
 */
public class VOIPRecordListActivity extends BaseActivity {
	private List datalist;
	private IMMessageListAdapter adapter;
	private ListView listview;
	private String negotiationId;


	@Override
	public void setUpView() {
		setContentView(R.layout.activity_voip_record_list);
		
		listview = (ListView) this.findViewById(R.id.voip_record_listview);
		
		
		datalist = new ArrayList();		
		adapter = new IMMessageListAdapter(this);
		adapter.setData(datalist);
		listview.setAdapter(adapter);
    	listview.setOnItemClickListener(new OnItemClickListener(){	 
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                //Intent data=new Intent(IMMessageHistoryActivity.this, DetailNegotiateForSenderActivity.class);  
                //data.putExtra(Constants.ID,   (String) ((Map) datalist_qiangdaode.get(arg2)).get("negotiationid"));
                //startActivity(data);  
                //gotoActivity(NegotiatePriceDetailActivity.class, false);
            }
        });
    	
    	negotiationId = getIntent().getStringExtra(Constants.ID);
    	
    	//fillMessageHistoryData();
	}

	@Override
	public void setListener() {

	}

	
	public void fillMessageHistoryData() {		
		listview=(ListView) findViewById(R.id.lv_message_listview);
		//String url = "MeetingManage/mobile/ModifyAttenderIsJoin.action?id="+ids;
		String url = "MeetingManage/mobile/getTopicInfoById.action";
		
		System.out.println("wangting:"+url);
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				String response = error.getMessage();
				System.out.println("wangting, fail response="+response);
				Toast.makeText(VOIPRecordListActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                //Intent data=new Intent();  
                //setResult(Constants.ERROR_NETWORK, data);  
                //finish();  
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				System.out.println("wangting, success response="+response);
				// Toast.makeText(TopicSelectApplicatorActivity.this, new String(response)+"wangting"+response.toString(), 100).show();
				//listview.setVisibility(View.VISIBLE);		
				//WaitMyCheckListAdapter adapter = new WaitMyCheckListAdapter(ConCheckActivity.this);
				//adapter = new IMMessageListAdapter(VOIPRecordListActivity.this);
				//datalist = new ArrayList<Map<String, String>>(); 
				
				try {
					 JSONArray jsonArray=new JSONObject(response).getJSONArray("attenderEntity");
					 Map<String, String> map;	
					 int length = jsonArray.length();
					 System.out.println("wangting:length="+length);
					 for(int i=0;i<length;i++){
			               JSONObject jsonObject=(JSONObject)jsonArray.get(i);
			               map = new HashMap<String, String>();
			               map.put("id", jsonObject.getString("id"));
			               map.put("name",jsonObject.getString("name"));
			               map.put("value", jsonObject.getString("isjoin"));
			               //map.put("organizename", jsonObject.getString("organizename"));
			               //map.put("postname", jsonObject.getString("postname"));
			               datalist.add(map);
					 }
					
				} catch (JSONException e) {
					e.printStackTrace();
					/*
					Toast.makeText(TopicDianmingAcitivity.this, getString(R.string.error_dataabout), Toast.LENGTH_SHORT).show();
					Intent data=new Intent();  
	                setResult(Constants.ERROR_DATA, data);  	                
	                finish();  
	                */
					
				}
				//adapter.setData(datalist);
				//listview.setAdapter(adapter);
				listview.setOnItemClickListener(new OnItemClickListener(){					 
		            @Override
		            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		                    long arg3) {
		            	/*
		                Intent data=new Intent();  
		                data.putExtra(Constants.ID,   (String)list.get(arg2).get("id"));  
		                IntentUtil.startActivity(TopicDianmingAcitivity.this, ConfCheckDetailActivity.class,  data);
		            	*/
		            }
		        });		
				
				
			}
		});			
	
		
	}
	
}
