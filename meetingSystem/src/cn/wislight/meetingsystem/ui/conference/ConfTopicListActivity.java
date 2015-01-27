package cn.wislight.meetingsystem.ui.conference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.ui.topic.TopicDetailActivity;
import cn.wislight.meetingsystem.ui.topic.TopicDetailComplexViewActivity;
import cn.wislight.meetingsystem.ui.topic.TopicDetailHistoryViewActivity;
import cn.wislight.meetingsystem.ui.topic.TopicMyTopicDetailActivity;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.IntentUtil;
import cn.wislight.meetingsystem.util.MeetingSystemClient;


/**
 * @author Administrator
 *	会议发起
 */
public class ConfTopicListActivity extends BaseActivity{
	private ListView listView;
	//private EditText etKeyword;
	//private RelativeLayout flSearchBar;
	private List<Map<String, Object>> list;
	private String meetingId;
	private String confControl = "no";
	private String viewHistory = "no";
	@Override
	public void initView() {
		listView = (ListView) findViewById(R.id.conference_topic_list_listview);
		//etKeyword = (EditText) findViewById(R.id.et_keywords);
		//flSearchBar = (RelativeLayout) findViewById(R.id.search_bar);		
		//flSearchBar.setVisibility(View.GONE);
		meetingId = this.getIntent().getStringExtra(Constants.ID);
		confControl =  this.getIntent().getStringExtra(Constants.CONF_CONTROL);
		viewHistory =  this.getIntent().getStringExtra(Constants.CONF_VIEW_HISTORY);
		viewHistory = (viewHistory != null && !"".equals(viewHistory)) ? viewHistory : "no";
		
		String url = "MeetingManage/mobile/getMeetingProcList.action?meetingId="+meetingId;

		System.out.println("wangting:"+url);
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				Toast.makeText(ConfTopicListActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                Intent data=new Intent();  
                setResult(Constants.ERROR_NETWORK, data);  
                finish();  
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
		
				System.out.println("wangting"+response);
				if (response.contains("用户未登陆")){
					Toast.makeText(ConfTopicListActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				listView.setVisibility(View.VISIBLE);				
				TopicDbListAdapter adapter = new TopicDbListAdapter(ConfTopicListActivity.this);
				list = new ArrayList<Map<String, Object>>(); 				
				try {
					 JSONArray jsonArray=new JSONArray(response);					  
				     Map<String, Object> map;				        
					 for(int i=0;i<jsonArray.length();i++){
			               JSONObject jsonObject=(JSONObject)jsonArray.get(i);
			               map = new HashMap<String, Object>();
			               map.put("starttime", jsonObject.getString("staTime"));
			               map.put("endtime", jsonObject.getString("endTime"));
			               map.put("keywords", jsonObject.getString("title"));
			               map.put("summary", jsonObject.getString("remark"));
			               map.put("id", jsonObject.getString("topicno"));			
			               map.put("topicid", jsonObject.getString("id"));
			               list.add(map);
					 }
					
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ConfTopicListActivity.this, getString(R.string.error_dataabout), Toast.LENGTH_SHORT).show();
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
		                data.putExtra(Constants.TOPIC_ID,   (String)list.get(arg2).get("topicid"));
		                if (null != confControl && "yes".equals(confControl)){
		                	IntentUtil.startActivity(ConfTopicListActivity.this, TopicDetailActivity.class,  data);
		                } else if ("yes".equals(viewHistory)) {
		                	IntentUtil.startActivity(ConfTopicListActivity.this, TopicDetailHistoryViewActivity.class,  data);
		                } else {
		                	IntentUtil.startActivity(ConfTopicListActivity.this, TopicDetailComplexViewActivity.class,  data);
		                }
		            }
		        });
				
			}
		});		
	}


	@Override
	public void setupView() {
		setContentView(R.layout.conference_topic_list);

	}
	
	protected void onPause(){
		super.onPause();

	}
	
	public void clickPre(View view){
		IntentUtil.startActivity(this, ConBeginTwoActivity.class);
		finish();
	}
}

class TopicDbListAdapter extends BaseAdapter{
	private Context mcontext;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> data;
	
	public TopicDbListAdapter(Context context) {
		this.mcontext=context;
		mInflater=LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		return data.size(); 
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;  
		if(convertView==null){
			convertView=mInflater.inflate(R.layout.topic_list_common_item, null);

		    holder = new ViewHolder();  
            //根据自定义的Item布局加载布局   
            holder.summary = (TextView)convertView.findViewById(R.id.tv_summary);  
            holder.keywords = (TextView)convertView.findViewById(R.id.tv_keywords);  
            holder.time = (TextView)convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);  
			
		}else{
			holder = (ViewHolder)convertView.getTag(); 
		}
		
		 holder.summary.setText((String)data.get(position).get("summary"));  
         holder.keywords.setText((String)data.get(position).get("keywords"));  
         holder.time.setText((String)data.get(position).get("starttime") + " -- "
        		 		+  (String)data.get(position).get("endtime"));  
		
		return convertView;
	}
	
	public void setData(List<Map<String, Object>> list) {
		// TODO Auto-generated method stub
		data = list;
	}
	
	class ViewHolder  
	{  
	    public TextView summary;  
	    public TextView keywords;  
	    public TextView time;  
	};
} ;
