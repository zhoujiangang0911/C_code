package cn.wislight.meetingsystem.ui.topic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpResponseHandler;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DialerFilter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.adapter.TopicDianmingAdapter;
import cn.wislight.meetingsystem.adapter.VoteAnonymousNameAdapter;
import cn.wislight.meetingsystem.adapter.VoteRealNameAdapter;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.MeetingSystemClient;
import cn.wislight.meetingsystem.util.SliderUtil;


public class TopicAdminVoteActivity extends BaseActivity  {

	private ListView listView;
	private List<Map<String, String>> datalist;
	private BaseAdapter adapter;

	private String topicId;
	private String voteType;
	@Override
	public void initView() {
		topicId = this.getIntent().getStringExtra(Constants.ID);
		voteType = this.getIntent().getStringExtra("voteType");
		
		
		listView=(ListView) findViewById(R.id.lv_admin_vote_listview);
		//String url = "MeetingManage/mobile/ModifyAttenderIsJoin.action?id="+ids;
		String url = "MeetingManage/mobile/getTopicInfoById.action?Id="+topicId;
		
		System.out.println("wangting:"+url);
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				String response = error.getMessage();

				System.out.println("wangting, success response="+response);
				Toast.makeText(TopicAdminVoteActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                //Intent data=new Intent();  
                //setResult(Constants.ERROR_NETWORK, data);  
                //finish();  
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				System.out.println("wangting, success response="+response);
				
				if (response.contains("用户未登陆")){
					Toast.makeText(TopicAdminVoteActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				// Toast.makeText(TopicSelectApplicatorActivity.this, new String(response)+"wangting"+response.toString(), 100).show();
				listView.setVisibility(View.VISIBLE);		
				//WaitMyCheckListAdapter adapter = new WaitMyCheckListAdapter(ConCheckActivity.this);
				datalist = new ArrayList<Map<String, String>>(); 
				if(voteType.equals("1")){
					adapter = new VoteRealNameAdapter(TopicAdminVoteActivity.this);
					((VoteRealNameAdapter) adapter).setData(datalist);
				}else{
					adapter = new VoteAnonymousNameAdapter(TopicAdminVoteActivity.this);
					((VoteAnonymousNameAdapter) adapter).setData(datalist);
				}				
				
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
			               map.put("value",jsonObject.getInt("voteresult")+"");
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
				
				listView.setAdapter(adapter);
				listView.setOnItemClickListener(new OnItemClickListener(){					 
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

	@Override
	public void setupView() {
		setContentView(R.layout.topic_admin_vote_main);	
	}
	public void  clickAdminVoteSubmit(View view){
		String result = "";
		int length = datalist.size();
		for(int i=0;i<length;i++){
			Map<String,String> item = datalist.get(i);
			if(i == length-1){
				result += item.get("id")+"," + item.get("value");
			}else{
				result += item.get("id")+"," + item.get("value")+";";
			}
		}
		//String url=""+result;
		String url = "MeetingManage/mobile/updateUserVoteByAdmin.action?meetProcId="+topicId+"&result="+result;		
		System.out.println("wangting:"+url);
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				String response = error.getMessage();
				System.out.println("wangting, success response="+response);
				Toast.makeText(TopicAdminVoteActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                //Intent data=new Intent();  
                //setResult(Constants.ERROR_NETWORK, data);  
                //finish();  
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				System.out.println("wangting, success response="+response);
				
				if (response.contains("用户未登陆")){
					Toast.makeText(TopicAdminVoteActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				// Toast.makeText(TopicSelectApplicatorActivity.this, new String(response)+"wangting"+response.toString(), 100).show();
				if(response.contains("true")){
					Toast.makeText(TopicAdminVoteActivity.this, "投票数据提交成功", 100).show();
					finish();
				}else{
					String msg = "";
					try {
						msg = new JSONObject(response)
								.getString("msg");
					}catch(Exception e){
						Toast.makeText(TopicAdminVoteActivity.this, "投票数据提交失败", 100).show();
					}
					Toast.makeText(TopicAdminVoteActivity.this, msg, 100).show();
					
				}
			}
		});			

	}
}
