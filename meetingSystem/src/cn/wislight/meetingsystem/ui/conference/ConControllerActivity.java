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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.loopj.android.http.AsyncHttpResponseHandler;

import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.adapter.ConfCommonListAdapter;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.IntentUtil;
import cn.wislight.meetingsystem.util.MeetingSystemClient;

/**
 * @author Administrator
 *  会议控制
 */
public class ConControllerActivity extends BaseActivity {

	private ListView listView;
	private TextView txTitle;

	private List<Map<String, Object>> list;
	
	@Override
	public void initView() {
		listView=(ListView) findViewById(R.id.conference_common_list_listview);
		//String url = "MeetingManage/mobile/findWaitMyCheckTopic.action";
		//String url = "MeetingManage/mobile/getMyVerifyMeetingList.action?type=0";
		String url = "MeetingManage/mobile/findGoingMeeting.action?type=1";
		System.out.println("wangting:"+url);
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				System.out.println("wangting, success response="+error.getMessage());
				Toast.makeText(ConControllerActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                Intent data=new Intent();  
                setResult(Constants.ERROR_NETWORK, data);  
                finish();  
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				
				if (response.contains("用户未登陆")){
					Toast.makeText(ConControllerActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				System.out.println("wangting, success response="+response);
				// Toast.makeText(TopicSelectApplicatorActivity.this, new String(response)+"wangting"+response.toString(), 100).show();
				listView.setVisibility(View.VISIBLE);		
				//WaitMyCheckListAdapter adapter = new WaitMyCheckListAdapter(ConCheckActivity.this);
				ConfCommonListAdapter adapter = new ConfCommonListAdapter(ConControllerActivity.this);
				list = new ArrayList<Map<String, Object>>(); 
				
				try {
					 //JSONArray jsonArray=new JSONObject(response).getJSONArray("updateMeetList");	
					 JSONArray jsonArray=new JSONArray(response);
				     Map<String, Object> map;				        
					 for(int i=0;i<jsonArray.length();i++){
			               JSONObject jsonObject=(JSONObject)jsonArray.get(i);
			               map = new HashMap<String, Object>();
			               map.put("id", jsonObject.getString("meetno"));
			               map.put("title",jsonObject.getString("title"));
			               map.put("remark", jsonObject.getString("remark"));
			               map.put("starttime", jsonObject.getString("starTime"));
			               map.put("endtime", jsonObject.getString("endTime"));
			               map.put("address", jsonObject.getString("address"));			               
			               
			               list.add(map);
					 }
					System.out.println("wangting: list size="+list.size());
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ConControllerActivity.this, getString(R.string.error_dataabout), Toast.LENGTH_SHORT).show();
					Intent data=new Intent();  
	                setResult(Constants.ERROR_DATA, data);  
	                finish();  				
				}
				adapter.setData(list);
				listView.setAdapter(adapter);
				listView.setOnItemClickListener(new OnItemClickListener(){					 
		            @Override
		            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		                    long arg3) {
		                Intent data=new Intent();  
		                data.putExtra(Constants.ID,   (String)list.get(arg2).get("id"));  
		                IntentUtil.startActivity(ConControllerActivity.this, ConfControllerDetailActivity.class,  data);
		            }
		        });		
				
				
			}
		});		
	}

	@Override
	public void setupView() {
		setContentView(R.layout.conference_common_list);
		txTitle = (TextView)findViewById(R.id.conference_common_list_title);
		txTitle.setText(getString(R.string.conference_control));
	}

}
