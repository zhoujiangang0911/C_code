package cn.wislight.publicservice.ui.user.goverment;

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
import cn.wislight.publicservice.adapter.NegotiationListAdapter;
import cn.wislight.publicservice.adapter.UserHandlingGovermentAffairListAdapter;
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.ui.commercialtenant.DetailNegotiateActivity;
import cn.wislight.publicservice.ui.commercialtenant.FindBuinessActivity;
import cn.wislight.publicservice.ui.governmentaffairs.ServantHandlingListActivity;
import cn.wislight.publicservice.util.Constants;
import cn.wislight.publicservice.util.PublicServiceClient;


/**
 * 事物完结列表
 * @author Administrator
 *
 */
public class UserEndGovermentAffairActivity extends BaseActivity {
	private List datalist;
	private UserHandlingGovermentAffairListAdapter adapter;
	private ListView listview;


	@Override
	public void setUpView() {
		setContentView(R.layout.activity_user_end_govermentaffair);
		
		listview = (ListView) this.findViewById(R.id.goverment_listview);
		datalist = new ArrayList();		
		adapter = new UserHandlingGovermentAffairListAdapter(this);
		adapter.setData(datalist);
		listview.setAdapter(adapter);
    	listview.setOnItemClickListener(new OnItemClickListener(){	 
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                Intent data=new Intent(UserEndGovermentAffairActivity.this, UserEndGovermentAffairDetailActivity.class);  
                data.putExtra(Constants.ID,   (String) ((Map) datalist.get(arg2)).get("id"));
                startActivity(data);  
                //gotoActivity(NegotiatePriceDetailActivity.class, false);
            }
        });
    	//fillNegotiationData();
    	fillData();
	}

	@Override
	public void setListener() {

	}

	private void fillData(){
		String url ="publicservice/govermentaffair_list.htm?json=true";
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler(){
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				//loadingdiag.hide();
				Toast.makeText(UserEndGovermentAffairActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);					
				System.out.println("wangting: response="+response);
				try {							 
					 JSONArray jsonArray=new JSONArray(response);	
					 
					 
				     Map<String, Object> map;
				    
				     datalist.clear();

					 for(int i=0;i<jsonArray.length();i++){
			               JSONObject jsonObject=(JSONObject)jsonArray.get(i);
			               if(jsonObject.getString("state").equals("4")){
			               map = new HashMap<String, Object>();
			               map.put("content", "");
			               if(!jsonObject.isNull("content")){
			            	   map.put("content", jsonObject.getString("content"));
			               }
			               map.put("id", jsonObject.getString("id"));
			               map.put("price", jsonObject.getString("price"));
			               map.put("score", jsonObject.getString("score"));
			               map.put("senderid", jsonObject.getString("senderid"));
			               
			               map.put("receivername", "");
			               if(!jsonObject.isNull("receiver")){         	   
			            	   JSONObject receiver = jsonObject.getJSONObject("receiver");
			            	   if(!receiver.isNull("username")){
			            		   map.put("receivername", receiver.getString("username"));
			            	   }
			               }
			               map.put("sendtime", jsonObject.getString("sendtime")); 
			               map.put("state", jsonObject.getString("state"));
			               map.put("voicecontentid", jsonObject.getString("voicecontentid"));
			               

			               String state = jsonObject.getString("state");
			               //if(state.equals("2"))
			               {
			            	   datalist.add(map);
			               } 
					 }
					 
					 adapter.notifyDataSetChanged();
					 }
				} catch (JSONException e) {
					e.printStackTrace();
					//loadingdiag.hide();
					Toast.makeText(UserEndGovermentAffairActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
				}
			}
		});		
	}


}
