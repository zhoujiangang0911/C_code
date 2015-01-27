package cn.wislight.meetingsystem.ui.conference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.adapter.ConfSelectApplicatorAdapter;
import cn.wislight.meetingsystem.adapter.ConfSelectConfTypeAdapter;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.MeetingSystemClient;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * @author Administrator
 *  待我表决
 */	


		
public class ConfSelectConfTypeActivity extends BaseActivity {
	private ListView conftype_listview;
	private TextView conftype_loading;
	private List<Map<String, Object>> list;
	@Override
	public void initView() {
		conftype_loading = (TextView)findViewById(R.id.conf_conftype_loading);
		conftype_listview=(ListView)findViewById(R.id.conf_conftype_listview);
		conftype_listview.setVisibility(View.GONE);		
		conftype_loading.setVisibility(View.VISIBLE);	
		
		String url = "MeetingManage/mobile/goCreateMeeting.action";
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				String response = error.getMessage();
				System.out.println("wangting:"+response);
				Toast.makeText(ConfSelectConfTypeActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                Intent data=new Intent();  
                setResult(Constants.ERROR_NETWORK, data);  
                finish();  
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				// Toast.makeText(TopicSelectApplicatorActivity.this, new String(response)+"wangting"+response.toString(), 100).show();
				System.out.println("wangting:"+response);
				if (response.contains("用户未登陆")){
					Toast.makeText(ConfSelectConfTypeActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				conftype_listview.setVisibility(View.VISIBLE);		
				conftype_loading.setVisibility(View.GONE);
				
				ConfSelectConfTypeAdapter adapter = new ConfSelectConfTypeAdapter(ConfSelectConfTypeActivity.this);
				list = new ArrayList<Map<String, Object>>(); 
				
				try {
					 JSONArray jsonArray=new JSONObject(response).getJSONArray("typelist");
					  
				     Map<String, Object> map;
				        
					 for(int i=0;i<jsonArray.length();i++){
			               JSONObject jsonObject=(JSONObject)jsonArray.get(i);
			               map = new HashMap<String, Object>();
			               //map.put("iResult", jsonObject.getInt("iResult"));
			               map.put("id", jsonObject.getString("id"));
			               map.put("join_rate", jsonObject.getString("join_rate"));
			               map.put("level", jsonObject.getString("level"));
			               map.put("mute_rate", jsonObject.getString("mute_rate"));
			               map.put("pass_rate", jsonObject.getString("pass_rate"));
			               map.put("typename", jsonObject.getString("typename"));
			               
			               list.add(map);
					 }
					
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ConfSelectConfTypeActivity.this, getString(R.string.error_dataabout), Toast.LENGTH_SHORT).show();
					Intent data=new Intent();  
	                setResult(Constants.ERROR_DATA, data);  
	                finish();  
					
				}
				adapter.setData(list);
				conftype_listview.setAdapter(adapter);
				conftype_listview.setOnItemClickListener(new OnItemClickListener(){
					 
		            @Override
		            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		                    long arg3) {
		            	
		                Intent data=new Intent();  
		                //data.putExtra("iResult", (String)list.get(arg2).get("iResult"));  
		                data.putExtra("id",  (String)list.get(arg2).get("id"));
		                //data.putExtra("join_rate", (String)list.get(arg2).get("join_rate"));  
		                data.putExtra("level",   (String)list.get(arg2).get("level"));  
		                //data.putExtra("mute_rate",  (String)list.get(arg2).get("mute_rate"));
		                //data.putExtra("pass_rate", (String)list.get(arg2).get("pass_rate"));  
		                data.putExtra("typename",   (String)list.get(arg2).get("typename"));  
		                
		                setResult(Constants.OK, data);  
		                
		                finish();  
		            }
		        });
				
			}
		});		
	}

	@Override
	public void setupView() {
		setContentView(R.layout.conference_select_conftype);
	}
	
    @Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK ) {  
            finish();  
        }            
        return false;            
    }
}
