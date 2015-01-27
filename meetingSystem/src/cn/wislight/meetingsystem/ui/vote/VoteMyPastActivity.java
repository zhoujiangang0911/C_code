package cn.wislight.meetingsystem.ui.vote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpResponseHandler;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.adapter.VoteMyStayAdapter;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.ui.topic.TopicCheckDetailActivity;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.IntentUtil;
import cn.wislight.meetingsystem.util.MeetingSystemClient;

/**
 * @author Administrator
 *  过期的表决
 */
public class VoteMyPastActivity extends BaseActivity {
	private ListView vote_mypast_listview;

	private List<Map<String, Object>> list;
	
	@Override
	public void initView() {
		vote_mypast_listview=(ListView) findViewById(R.id.vote_mypast_listview);
		String url = "MeetingManage/mobile/getVotePastedTopicList.action";
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				Toast.makeText(VoteMyPastActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                Intent data=new Intent();  
                setResult(Constants.ERROR_NETWORK, data);  
                finish();  
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				// Toast.makeText(TopicSelectApplicatorActivity.this, new String(response)+"wangting"+response.toString(), 100).show();
				if (response.contains("用户未登陆")){
					Toast.makeText(VoteMyPastActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				vote_mypast_listview.setVisibility(View.VISIBLE);		
				
				VoteMyStayAdapter adapter = new VoteMyStayAdapter(VoteMyPastActivity.this);
				list = new ArrayList<Map<String, Object>>(); 
				
				try {
					 JSONArray jsonArray=new JSONObject(response).getJSONArray("list");
					  
				     Map<String, Object> map;
				        
					 for(int i=0;i<jsonArray.length();i++){
			               JSONObject jsonObject=(JSONObject)jsonArray.get(i);
			               map = new HashMap<String, Object>();
			               map.put("starttime", jsonObject.getString("starttime"));
			               map.put("endtime", jsonObject.getString("endtime"));
			               map.put("keywords", jsonObject.getString("keywords"));
			               map.put("summary", jsonObject.getString("summary"));
			               map.put("id", jsonObject.getString("id"));
			               
			               list.add(map);
					 }
					
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(VoteMyPastActivity.this, getString(R.string.error_dataabout), Toast.LENGTH_SHORT).show();
					Intent data=new Intent();  
	                setResult(Constants.ERROR_DATA, data);  
	                finish();  
					
				}
				adapter.setData(list);
				vote_mypast_listview.setAdapter(adapter);
				
			}
		});		
	}

	@Override
	public void setupView() {
		setContentView(R.layout.vote_my_past);
		
	}

}