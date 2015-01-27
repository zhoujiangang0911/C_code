package cn.wislight.meetingsystem.ui.topic;

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
import cn.wislight.meetingsystem.adapter.TopicSelectApplicatorAdapter;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.MeetingSystemClient;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * @author Administrator
 *  待我表决
 */	


		
public class TopicSelectApplicatorActivity extends BaseActivity {
	private ListView applicator_listview;
	private TextView loading;
	private List<Map<String, Object>> list;
	@Override
	public void initView() {
		loading = (TextView)findViewById(R.id.loading);
		applicator_listview=(ListView)findViewById(R.id.topic_applicator_listview);
		applicator_listview.setVisibility(View.GONE);		
		loading.setVisibility(View.VISIBLE);	
        Intent intent1 = getIntent();
        String orgno = intent1.getStringExtra(Constants.ORG_NO);  
        String pplType = intent1.getStringExtra("type");  
		String url = "MeetingManage/mobile/findRecordList.action?orgno=" + orgno + "&ppltype=" + pplType;
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				Toast.makeText(TopicSelectApplicatorActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                Intent data=new Intent();  
                setResult(Constants.ERROR_NETWORK, data);  
                finish();  
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				// Toast.makeText(TopicSelectApplicatorActivity.this, new String(response)+"wangting"+response.toString(), 100).show();
				if (response.contains("用户未登陆")){
					Toast.makeText(TopicSelectApplicatorActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				applicator_listview.setVisibility(View.VISIBLE);		
				loading.setVisibility(View.GONE);
				
				TopicSelectApplicatorAdapter adapter = new TopicSelectApplicatorAdapter(TopicSelectApplicatorActivity.this);
				list = new ArrayList<Map<String, Object>>(); 
				
				try {
					 JSONArray jsonArray=new JSONObject(response).getJSONArray("userList");
					  
				     Map<String, Object> map;
				        
					 for(int i=0;i<jsonArray.length();i++){
			               JSONObject jsonObject=(JSONObject)jsonArray.get(i);
			               map = new HashMap<String, Object>();
			               map.put("name", jsonObject.getString("fullname"));
			               map.put("org", jsonObject.getString("organizename"));
			               map.put("post", jsonObject.getString("postname"));
			               map.put("id", jsonObject.getString("userId"));
			               
			               list.add(map);
					 }
					
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(TopicSelectApplicatorActivity.this, getString(R.string.error_dataabout), Toast.LENGTH_SHORT).show();
					Intent data=new Intent();  
	                setResult(Constants.ERROR_DATA, data);  
	                finish();  
					
				}
				adapter.setData(list);
				applicator_listview.setAdapter(adapter);
				applicator_listview.setOnItemClickListener(new OnItemClickListener(){
					 
		            @Override
		            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		                    long arg3) {
		            	
		                Intent data=new Intent();  
		                data.putExtra(Constants.NAME, (String)list.get(arg2).get("name"));  
		                data.putExtra(Constants.ORG,  (String)list.get(arg2).get("org"));
		                data.putExtra(Constants.POST, (String)list.get(arg2).get("post"));  
		                data.putExtra(Constants.ID,   (String)list.get(arg2).get("id"));  
		                setResult(Constants.OK, data);  
		                
		                finish();  
		            }
		        });
				
			}
		});		
	}

	@Override
	public void setupView() {
		setContentView(R.layout.topic_select_applicator);
	}

    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK ) {  
            finish();  
        }            
        return false;            
    }
}
