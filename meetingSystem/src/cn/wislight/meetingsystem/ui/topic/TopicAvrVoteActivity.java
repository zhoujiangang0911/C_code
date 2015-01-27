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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.adapter.TopicAvrVoteAdapter;
import cn.wislight.meetingsystem.adapter.TopicDianmingAdapter;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.domain.AttenderVoteElement;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.MeetingSystemClient;

import com.loopj.android.http.AsyncHttpResponseHandler;


public class TopicAvrVoteActivity extends BaseActivity {

	private ListView listView;
	private List<AttenderVoteElement> datalist;

	private String topicId;
	@Override
	public void initView() {
		
		topicId = this.getIntent().getStringExtra(Constants.ID);
		
		listView=(ListView) findViewById(R.id.lv_listview);
		//String url = "MeetingManage/mobile/ModifyAttenderIsJoin.action?id="+ids;
		String url = "MeetingManage/mobile/getTopicInfoById.action?Id="+topicId;
		
		System.out.println("wangting:"+url);
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				String response = error.getMessage();
				System.out.println("wangting, fail response="+response);
				Toast.makeText(TopicAvrVoteActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				System.out.println("wangting, success response="+response);
				
				if (response.contains("用户未登陆")){
					Toast.makeText(TopicAvrVoteActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				// Toast.makeText(TopicSelectApplicatorActivity.this, new String(response)+"wangting"+response.toString(), 100).show();
				listView.setVisibility(View.VISIBLE);		
				//WaitMyCheckListAdapter adapter = new WaitMyCheckListAdapter(ConCheckActivity.this);
				TopicAvrVoteAdapter adapter = new TopicAvrVoteAdapter(TopicAvrVoteActivity.this);
				datalist = new ArrayList<AttenderVoteElement>(); 
				
				try {
					 JSONArray jsonArray=new JSONObject(response).getJSONArray("attenderEntity");
					 Map<String, String> map;	
					 int length = jsonArray.length();
					 System.out.println("wangting:length="+length);
					 for(int i=0;i<length;i++){
			               JSONObject jsonObject=(JSONObject)jsonArray.get(i);
			               AttenderVoteElement ele= new AttenderVoteElement();
			               ele.setId(jsonObject.getString("id"));
			               ele.setName(jsonObject.getString("name"));
			               ele.setiJoinstate(jsonObject.getInt("isjoin"));
			               ele.setiVotestate(jsonObject.getInt("voteresult"));
			               ele.setPhone(jsonObject.getString("phone"));
			               
			               datalist.add(ele);
					 }
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				adapter.setData(datalist);
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
		setContentView(R.layout.topic_avrvote_main);	
	}
	public void  clickVoipVoteSubmit(View view){
		String users = "";
		int length = datalist.size();
		for(int i=0;i<length;i++){
			AttenderVoteElement item = datalist.get(i);
			if (item.isCheck){
				if(i == length-1){
					users+=item.getId()+","+item.getName()+","+item.getPhone();
				}else{
					users+=item.getId()+","+item.getName()+","+item.getPhone()+";";
				}
			}
		}
		//String url=""+result;
		String url = "MeetingManage/mobile/toInitiate.action?msg="+users;		
		System.out.println("wangting:"+url);
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				String response = error.getMessage();
				System.out.println("wangting, fail response="+response);
				Toast.makeText(TopicAvrVoteActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				System.out.println("wangting, success response="+response);
				
				if (response.contains("用户未登陆")){
					Toast.makeText(TopicAvrVoteActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				// Toast.makeText(TopicSelectApplicatorActivity.this, new String(response)+"wangting"+response.toString(), 100).show();
				try {
					JSONObject jsonObject = new JSONObject(response);
					String result = jsonObject.getString("iResult");
					if("1".equals(result)){
						Toast.makeText(TopicAvrVoteActivity.this, "发起语音投票成功", 100).show();
						finish();
					} else {
						Toast.makeText(TopicAvrVoteActivity.this, "发起语音投票失败", 100).show();
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Toast.makeText(TopicAvrVoteActivity.this, "发起语音投票失败", 100).show();
					e.printStackTrace();
				}
				
			}
		});			

	}
}
