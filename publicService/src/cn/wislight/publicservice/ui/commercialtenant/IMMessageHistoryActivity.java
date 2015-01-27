package cn.wislight.publicservice.ui.commercialtenant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.ui.commercialtenant.DetailNegotiateActivity;
import cn.wislight.publicservice.ui.commercialtenant.FindBuinessActivity;
import cn.wislight.publicservice.util.Constants;
import cn.wislight.publicservice.util.PublicServiceClient;


/**
 * 事物处理中
 * @author Administrator
 *
 */
public class IMMessageHistoryActivity extends BaseActivity {
	private List datalist;
	private IMMessageListAdapter adapter;
	private ListView listview;
	private String negotiationId;


	@Override
	public void setUpView() {
		setContentView(R.layout.activity_immessage_history);
		
		listview = (ListView) this.findViewById(R.id.immessage_history_listview);
		listview.setDividerHeight(0);
		datalist = new ArrayList<Map<String, String>>();	
        Map<String,String> m1 = new HashMap<String,String>();
        m1.put("id","1");
        m1.put("username", "唐大良");
        m1.put("message","这是我第一次使用");
        datalist.add(m1);
        Map<String,String> m2 = new HashMap<String,String>();
        m2.put("id","2");
        m2.put("username", "刘德华");
        m2.put("message","你好吗？在干吗呢？");
        datalist.add(m2);
        Map<String,String> m3 = new HashMap<String,String>();
        m3.put("id","3");
        m3.put("username", "刘德华");
        m3.put("message","这个工具很好使用...赞！");
        datalist.add(m3);
        Map<String,String> m4 = new HashMap<String,String>();
        m4.put("id","4");
        m4.put("username", "唐大良");
        m4.put("message","今天你需要什么样的服务呢？如果有需要请电话联系我哦？亲...");
        datalist.add(m4);
        Map<String,String> m5 = new HashMap<String,String>();
        m5.put("id","5");
        m5.put("username", "刘德华");
        m5.put("message","谢谢，我会给好评的...");
        datalist.add(m5);
        fillMessageHistoryData();
		adapter = new IMMessageListAdapter(this);
		adapter.setData(datalist);
		adapter.setMyName("唐大良");
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

		//String url = "MeetingManage/mobile/ModifyAttenderIsJoin.action?id="+ids;
		String url = "MeetingManage/mobile/getTopicInfoById.action";
		
		System.out.println("wangting:"+url);
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				String response = error.getMessage();
				System.out.println("wangting, fail response="+response);
				Toast.makeText(IMMessageHistoryActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
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
				//adapter = new IMMessageListAdapter(IMMessageHistoryActivity.this);
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
