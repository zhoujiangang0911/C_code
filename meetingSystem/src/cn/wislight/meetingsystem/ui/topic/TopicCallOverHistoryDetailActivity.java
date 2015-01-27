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

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.DialerFilter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.adapter.ConfCommonListAdapter;
import cn.wislight.meetingsystem.adapter.TopicDianmingAdapter;
import cn.wislight.meetingsystem.adapter.VoteRealNameAdapter;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.ui.conference.ConCheckActivity;
import cn.wislight.meetingsystem.ui.conference.ConfCheckDetailActivity;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.Element;
import cn.wislight.meetingsystem.util.IntentUtil;
import cn.wislight.meetingsystem.util.LoadingDialog;
import cn.wislight.meetingsystem.util.MeetingSystemClient;
import cn.wislight.meetingsystem.util.SliderUtil;


public class TopicCallOverHistoryDetailActivity extends BaseActivity {

	private ListView listView;
	private List<Map<String, String>> datalist;
	private LoadingDialog loadingdiag;
	private String topicId;
	private String version;
	@Override
	public void initView() {
		topicId = this.getIntent().getStringExtra(Constants.ID);
		version = this.getIntent().getStringExtra("version");
		
		listView=(ListView) findViewById(R.id.lv_dianming_listview);
		loadingdiag = new LoadingDialog(this);  
		loadingdiag.setCanceledOnTouchOutside(false); 
		loadingdiag.setText(getString(R.string.loading));
		loadingdiag.show();
		
		//String url = "MeetingManage/mobile/ModifyAttenderIsJoin.action?id="+ids;
		String url = "MeetingManage/mobile/GetTopicCallOverHistoryDetail.action?topicNo="+topicId;
		url += "&version=";
		url += version;
		
		System.out.println("wangting:"+url);
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				String response = error.getMessage();
				loadingdiag.hide();
				System.out.println("wangting, fail response="+response);
				Toast.makeText(TopicCallOverHistoryDetailActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                //Intent data=new Intent();  
                //setResult(Constants.ERROR_NETWORK, data);  
                //finish();  
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				System.out.println("wangting, success response="+response);
				// Toast.makeText(TopicSelectApplicatorActivity.this, new String(response)+"wangting"+response.toString(), 100).show();
				listView.setVisibility(View.VISIBLE);
				loadingdiag.hide();
				
				if (response.contains("用户未登陆")){
					Toast.makeText(TopicCallOverHistoryDetailActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				//WaitMyCheckListAdapter adapter = new WaitMyCheckListAdapter(ConCheckActivity.this);
				TopicDianmingAdapter adapter = new TopicDianmingAdapter(TopicCallOverHistoryDetailActivity.this, false);
				datalist = new ArrayList<Map<String, String>>(); 
				
				try {
					 JSONArray jsonArray=new JSONObject(response).getJSONArray("callOverDetailList");
					 Map<String, String> map;	
					 int length = jsonArray.length();
					 System.out.println("wangting:length="+length);
					 for(int i=0;i<length;i++){
			               JSONObject jsonObject=(JSONObject)jsonArray.get(i);
			               map = new HashMap<String, String>();
			               map.put("name",jsonObject.getString("name"));
			               map.put("value", jsonObject.getString("isjoin"));
			               datalist.add(map);
					 }
					
				} catch (JSONException e) {
					e.printStackTrace();
					
					Toast.makeText(TopicCallOverHistoryDetailActivity.this, getString(R.string.error_dataabout), Toast.LENGTH_SHORT).show();
					Intent data=new Intent();  
	                setResult(Constants.ERROR_DATA, data);  	                
	                finish();  
	                
					
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
		setContentView(R.layout.topic_dianming_show);	
	}
}
