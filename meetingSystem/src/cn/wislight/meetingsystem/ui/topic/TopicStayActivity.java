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

import android.content.Context;
import android.content.Intent;
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
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.IntentUtil;
import cn.wislight.meetingsystem.util.MeetingSystemClient;

/**
 * @author Administrator
 * 议题库
 */
public class TopicStayActivity extends BaseActivity {
	private ListView listView;

	private List<Map<String, Object>> list;
	
	@Override
	public void initView() {
		listView=(ListView) findViewById(R.id.lv_wait_my_check);
		String url = "MeetingManage/mobile/findWaitMyCheckTopic.action";
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				Toast.makeText(TopicStayActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                Intent data=new Intent();  
                setResult(Constants.ERROR_NETWORK, data);  
                finish();  
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				// Toast.makeText(TopicSelectApplicatorActivity.this, new String(response)+"wangting"+response.toString(), 100).show();
				
				listView.setVisibility(View.VISIBLE);		
				if (response.contains("用户未登陆")){
					Toast.makeText(TopicStayActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				WaitMyCheckListAdapter adapter = new WaitMyCheckListAdapter(TopicStayActivity.this);
				list = new ArrayList<Map<String, Object>>(); 
				
				try {
					 JSONArray jsonArray=new JSONObject(response).getJSONArray("tpList");
					  
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
					Toast.makeText(TopicStayActivity.this, getString(R.string.error_dataabout), Toast.LENGTH_SHORT).show();
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
		                
		                IntentUtil.startActivity(TopicStayActivity.this, TopicCheckDetailActivity.class,  data);
		            }
		        });
				
			}
		});		
	}

	@Override
	public void setupView() {
		setContentView(R.layout.topic_my_check);
		
	}

}


class WaitMyCheckListAdapter extends BaseAdapter{
	private Context mcontext;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> data;
	
	public WaitMyCheckListAdapter(Context context) {
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
