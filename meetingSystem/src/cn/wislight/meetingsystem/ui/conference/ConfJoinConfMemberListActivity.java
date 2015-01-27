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
import cn.wislight.meetingsystem.adapter.ConfJoinMemberAdapter;
import cn.wislight.meetingsystem.adapter.ConfSelectApplicatorAdapter;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.LoadingDialog;
import cn.wislight.meetingsystem.util.MeetingSystemClient;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * @author Administrator
 *  待我表决
 */	


		
public class ConfJoinConfMemberListActivity extends BaseActivity {
	private ListView applicator_listview;
	private TextView loading;
	private List<Map<String, Object>> list;
	private String meetingId;
	private LoadingDialog loadingdiag;
	@Override
	public void initView() {
		Intent intent = getIntent();
	    meetingId = intent.getStringExtra(Constants.ID);
		
		loading = (TextView)findViewById(R.id.conf_applicator_loading);
		applicator_listview=(ListView)findViewById(R.id.conf_applicator_listview);
		applicator_listview.setVisibility(View.GONE);		
		loading.setVisibility(View.GONE);	
		loadingdiag = new LoadingDialog(this);  
		loadingdiag.setCanceledOnTouchOutside(false); 
		loadingdiag.setText(getString(R.string.loading));
		loadingdiag.show();
		
		//String url = "MeetingManage/mobile/findRecordList.action";
		String url = "MeetingManage/mobile/findMeetTopicPersonByMeetNo.action?meetId="+ meetingId;
		System.out.println("wangting:"+url);
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();
				Toast.makeText(ConfJoinConfMemberListActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                System.out.println("wangting:"+error.getMessage());
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				// Toast.makeText(TopicSelectApplicatorActivity.this, new String(response)+"wangting"+response.toString(), 100).show();
				System.out.println("wangting" + response);
				
				if (response.contains("用户未登陆")){
					Toast.makeText(ConfJoinConfMemberListActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				applicator_listview.setVisibility(View.VISIBLE);		
				loading.setVisibility(View.GONE);
				loadingdiag.hide();
				ConfJoinMemberAdapter adapter = new ConfJoinMemberAdapter(ConfJoinConfMemberListActivity.this);
				list = new ArrayList<Map<String, Object>>(); 
				
				try {
					 JSONArray jsonArray=new JSONObject(response).getJSONArray("userList");
					  
				     Map<String, Object> map;
				        
					 for(int i=0;i<jsonArray.length();i++){
			               JSONObject jsonObject=(JSONObject)jsonArray.get(i);
			               map = new HashMap<String, Object>();
			               map.put("name", jsonObject.getString("name"));
			               map.put("org", jsonObject.getString("org"));
			               map.put("post", jsonObject.getString("postname"));
			               map.put("id", jsonObject.getString("id"));
			               
			               map.put("seats", jsonObject.getString("meeting_seats"));
			               String meals = "";
			               try{
			            	   meals += jsonObject.getString("daddress");
			            	   meals += "  " + jsonObject.getString("deskno");
			            	   meals += "  " + jsonObject.getString("dinnerdateEnd");
			               }catch (Exception e){
			            	   
			               }
			               map.put("meals", meals);

			               String accommondation = "";
			               try{
			            	   accommondation += jsonObject.getString("stayaddress"); 
			            	   accommondation += jsonObject.getString("roomno");
			               }catch (Exception e){
			            	   
			               }
			               map.put("accommondation", accommondation);
			               
			               list.add(map);
					 }
					
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ConfJoinConfMemberListActivity.this, getString(R.string.error_dataabout), Toast.LENGTH_SHORT).show();
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
		            	/*
		                Intent data=new Intent();  
		                data.putExtra(Constants.NAME, (String)list.get(arg2).get("name"));  
		                data.putExtra(Constants.ORG,  (String)list.get(arg2).get("org"));
		                data.putExtra(Constants.POST, (String)list.get(arg2).get("post"));  
		                data.putExtra(Constants.ID,   (String)list.get(arg2).get("id"));  
		                setResult(Constants.OK, data);  
		                */
		            	//show person info
		                
		                finish();  
		            }
		        });
				
			}
		});		
	}

	@Override
	public void setupView() {
		setContentView(R.layout.conference_select_applicator);
	}
	
    @Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK ) {  
            finish();  
        }            
        return false;            
    }

}
