package cn.wislight.meetingsystem.ui.topic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.IntentUtil;
import cn.wislight.meetingsystem.util.LoadingDialog;
import cn.wislight.meetingsystem.util.MeetingSystemClient;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * @author Administrator
 * 议题库
 */
public class TopicCallOverHistoryActivity extends BaseActivity {
	private ListView listView;
	private List<Map<String, Object>> list;
	private String topicNo;
	private LoadingDialog loadingdiag;
	
	@Override
	public void initView() {
		listView = (ListView) findViewById(R.id.lv_call_overs);
		topicNo = getIntent().getStringExtra(Constants.ID);
		
		loadingdiag = new LoadingDialog(this);  
		loadingdiag.setCanceledOnTouchOutside(false); 
		
		getCallOverHistory();
	}

	protected void onResume(){
		getCallOverHistory();
		super.onResume();
	}
	private void getCallOverHistory() {
		// TODO Auto-generated method stub
		loadingdiag.setText(getString(R.string.loading));
		loadingdiag.show();
		String url = "MeetingManage/mobile/GetTopicCallOverHistory.action?topicNo=" + topicNo;
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();
                finish();  
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				// Toast.makeText(TopicSelectApplicatorActivity.this, new String(response)+"wangting"+response.toString(), 100).show();
				if (response.contains("用户未登陆")){
					Toast.makeText(TopicCallOverHistoryActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				listView.setVisibility(View.VISIBLE);		
				loadingdiag.hide();

				TopicCallOverListAdapter adapter = new TopicCallOverListAdapter(TopicCallOverHistoryActivity.this);
				list = new ArrayList<Map<String, Object>>(); 
				
				try {
					 JSONArray jsonArray=new JSONObject(response).getJSONArray("callOverList");
					  
				     Map<String, Object> map;
				        
					 for(int i=0;i<jsonArray.length();i++){
			               JSONObject jsonObject=(JSONObject)jsonArray.get(i);
			               map = new HashMap<String, Object>();
			               map.put("id", jsonObject.getString("id"));
			               map.put("callover_time", jsonObject.getString("callover_time"));
			               map.put("topic_no", jsonObject.getString("topic_no"));
			               map.put("version", jsonObject.getString("version"));
			               
			               list.add(map);
					 }
					
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(TopicCallOverHistoryActivity.this, getString(R.string.error_dataabout), Toast.LENGTH_SHORT).show();
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
		                data.putExtra(Constants.ID,   (String)list.get(arg2).get("topic_no"));  
		                data.putExtra("version",   (String)list.get(arg2).get("version"));  

		                IntentUtil.startActivity(TopicCallOverHistoryActivity.this, TopicCallOverHistoryDetailActivity.class,  data);
		            }
		        });
				
			}
		});				
	}

	@Override
	public void setupView() {
		setContentView(R.layout.topic_callover_history);
	}
	
	public void clickNewCallOver(View view){
		Intent data=new Intent();  
        data.putExtra(Constants.ID, topicNo);  
        IntentUtil.startActivity(TopicCallOverHistoryActivity.this, TopicDianmingActivity.class, data);
	}
	
}


class TopicCallOverListAdapter extends BaseAdapter{
	private Context mcontext;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> data;
	
	public TopicCallOverListAdapter(Context context) {
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
			convertView=mInflater.inflate(R.layout.topic_callover_list_item, null);
		    holder = new ViewHolder();  
            //根据自定义的Item布局加载布局   
            holder.tvTime = (TextView)convertView.findViewById(R.id.tv_time);  
            convertView.setTag(holder);  
			
		}else{
			holder = (ViewHolder)convertView.getTag(); 
		}
		
		holder.tvTime.setText((String)data.get(position).get("callover_time"));  
		return convertView;
	}
	
	public void setData(List<Map<String, Object>> list) {
		// TODO Auto-generated method stub
		data = list;
	}
	
	class ViewHolder  
	{  
	    public TextView tvTime;  
	};
} ;

