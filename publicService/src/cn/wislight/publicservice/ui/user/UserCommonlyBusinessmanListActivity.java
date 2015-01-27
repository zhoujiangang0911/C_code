package cn.wislight.publicservice.ui.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.loopj.android.http.AsyncHttpResponseHandler;

import cn.wislight.publicservice.PublicServiceApplication;
import cn.wislight.publicservice.R;
import cn.wislight.publicservice.adapter.BusinessAffairListAdapter;
import cn.wislight.publicservice.adapter.NegotiationListAdapter;
import cn.wislight.publicservice.adapter.UserCommonlyBusinessmanListAdapter;
import cn.wislight.publicservice.adapter.UserCommonlyPersonListAdapter;
import cn.wislight.publicservice.adapter.UserHandlingGovermentAffairListAdapter;
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.ui.commercialtenant.DetailNegotiateActivity;
import cn.wislight.publicservice.ui.commercialtenant.FindBuinessActivity;
import cn.wislight.publicservice.ui.commercialtenant.IMChatingActivity;
import cn.wislight.publicservice.ui.governmentaffairs.ServantHandlingListActivity;
import cn.wislight.publicservice.util.Constants;
import cn.wislight.publicservice.util.PublicServiceClient;
import cn.wislight.publicservice.voip.CallInActivity;

public class UserCommonlyBusinessmanListActivity extends BaseActivity {
	private List datalist;
	private UserCommonlyBusinessmanListAdapter adapter;
	private ListView listview;


	@Override
	public void setUpView() {
		setContentView(R.layout.activity_user_commonlyperson_list);
		
		listview = (ListView) this.findViewById(R.id.commonlyperson_listview);
		datalist = new ArrayList();		
		adapter = new UserCommonlyBusinessmanListAdapter(this);
		adapter.setData(datalist);
		listview.setAdapter(adapter);
    	listview.setOnItemClickListener(new OnItemClickListener(){	 
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                Intent data=new Intent(UserCommonlyBusinessmanListActivity.this, UserCommonlyBusinessmanDetailActivity.class);  
                data.putExtra(Constants.ID,   (String) ((Map) datalist.get(arg2)).get("id"));
                startActivity(data);  
            }
        });
    	fillData();
	}

	@Override
	public void setListener() {

	}


	private void fillData(){
		String url ="publicservice/commonlybusinessman_findCommonlyBusinessmanList.htm?json=true";
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler(){
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				//loadingdiag.hide();
				Toast.makeText(UserCommonlyBusinessmanListActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);						
				try {							 
					 JSONArray jsonArray=new JSONArray(response);					  
				     Map<String, Object> map;
				    
				     datalist.clear();

					 for(int i=0;i<jsonArray.length();i++){
			               JSONObject jsonObject=(JSONObject)jsonArray.get(i);
			               map = new HashMap<String, Object>();			               
	
			               if(!jsonObject.isNull("person")){
			            	   JSONObject person = jsonObject.getJSONObject("person");
			            	   map.put("id", jsonObject.getString("id"));
			            	   
			            	   
			            	   if(!person.isNull("username")){
			            		   map.put("username", person.getString("username"));
			            	   }else{
			            		   map.put("username", person.getString("loginname"));
			            	   }
			            	   if(!person.isNull("position")){
			            		   map.put("userposition", person.getString("position"));
			            	   }else{
			            		   map.put("userposition", "");
			            	   }
			            	   if(!person.isNull("phone")){
			            		   map.put("phone", person.getString("phone"));
			            	   }else{
			            		   map.put("phone", "");
			            	   }
			            	   if(!person.isNull("fixphone")){
			            		   map.put("fixphone", person.getString("fixphone"));
			            	   }else{
			            		   map.put("fixphone", "");
			            	   }
			            	   if(!person.isNull("voip")){
			            		   map.put("voip", person.getString("voip"));
			            	   }else{
			            		   map.put("voip", "");
			            	   }
			            	   
			            	   map.put("userid", person.getString("id"));
			            	   
			            	   datalist.add(map);
			               } 
					 }					 
					 adapter.notifyDataSetChanged();

				} catch (JSONException e) {
					e.printStackTrace();
					//loadingdiag.hide();
					Toast.makeText(UserCommonlyBusinessmanListActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
				}
			}
		});		
	}



	public void clickIMMessage(Context mcontext, int position){		
		String voipnumber = (String) ((Map) datalist.get(position)).get("voip");
		Toast.makeText(mcontext,
				voipnumber, Toast.LENGTH_SHORT).show();
		if(voipnumber ==null || voipnumber.trim().length()<1){
			Toast.makeText(mcontext,
					"网络电话号码为空", Toast.LENGTH_SHORT).show();
			return;
		}			
		Intent intentIM = new Intent(this, IMChatingActivity.class);   
		intentIM.putExtra("receiverVOIP", voipnumber);
        startActivity(intentIM); 
	}
	
	public void clickCallPhone(Context mcontext, int position){			
		String phonenumber = (String) ((Map) datalist.get(position)).get("phone");
		if(phonenumber ==null || phonenumber.trim().length()<1){
			phonenumber = (String) ((Map) datalist.get(position)).get("fixphone");
			if(phonenumber ==null || phonenumber.trim().length()<1){				
				Toast.makeText(mcontext,"电话号码为空", Toast.LENGTH_SHORT).show();
				return;
			}			
		}
		//用intent启动拨打电话  
        Intent intentPhone = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phonenumber));  
        startActivity(intentPhone);
	}
}
