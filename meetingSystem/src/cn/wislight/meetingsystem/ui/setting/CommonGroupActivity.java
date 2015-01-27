package cn.wislight.meetingsystem.ui.setting;

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
import android.widget.ListView;
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
 * 群组设置
 */
public class CommonGroupActivity extends BaseActivity {
	private ListView listView;
	private List<Map<String, Object>> list;
	private LoadingDialog loadingdiag;
	@Override
	public void initView() {
		listView = (ListView) findViewById(R.id.lv_group);
		
		
		
		String url = "MeetingManage/mobile/findGroupByUserId.action?pageSize=100&pageNum=1";
		
		loadingdiag = new LoadingDialog(this);  
		loadingdiag.setCanceledOnTouchOutside(false); 
		loadingdiag.setText(getString(R.string.loading));
		loadingdiag.show();
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();
				Toast.makeText(CommonGroupActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                Intent data=new Intent();  
                setResult(Constants.ERROR_NETWORK, data);  
                finish();  
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				// Toast.makeText(TopicSelectApplicatorActivity.this, new String(response)+"wangting"+response.toString(), 100).show();
				loadingdiag.hide();
				if (response.contains("用户未登陆")){
					Toast.makeText(CommonGroupActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				listView.setVisibility(View.VISIBLE);		
				
				CommonGroupListAdapter adapter = new CommonGroupListAdapter(CommonGroupActivity.this);
				list = new ArrayList<Map<String, Object>>(); 
				
				try {
					 JSONArray jsonArray=new JSONArray(response);
					  
				     Map<String, Object> map;
				        
					 for(int i=0;i<jsonArray.length();i++){
			               JSONObject jsonObject=(JSONObject)jsonArray.get(i);
			               map = new HashMap<String, Object>();
			               map.put("groupName", jsonObject.getString("groupName"));
			               map.put("createDate", jsonObject.getString("createData"));
			               map.put("remark", jsonObject.getString("remark"));
			               map.put("id", jsonObject.getString("id"));
			               
			               list.add(map);
					 }
					
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(CommonGroupActivity.this, getString(R.string.error_dataabout), Toast.LENGTH_SHORT).show();
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
		                data.putExtra(Constants.ID, (String)list.get(arg2).get("id"));  
		                IntentUtil.startActivity(CommonGroupActivity.this, CommonGroupMemberActivity.class, data); 
		                
		            }
		        });
			}
		});		
	}

	@Override
	public void setupView() {
		setContentView(R.layout.system_setting_common_group_main);
	}
	
	public void clickNewGroup(View view){
		IntentUtil.startActivity(CommonGroupActivity.this, CommonGroupAddOneActivity.class);
	}
	
	
}


class CommonGroupListAdapter extends BaseAdapter{
	private Context mcontext;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> data;
	
	public CommonGroupListAdapter(Context context) {
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
			convertView=mInflater.inflate(R.layout.system_setting_common_group_list_item, null);

		    holder = new ViewHolder();  
            //根据自定义的Item布局加载布局   
            holder.groupname = (TextView)convertView.findViewById(R.id.tv_groupname);  
            holder.remark = (TextView)convertView.findViewById(R.id.tv_remark);  
            holder.createtime = (TextView)convertView.findViewById(R.id.tv_createtime);
            convertView.setTag(holder);  
			
		}else{
			holder = (ViewHolder)convertView.getTag(); 
		}
		
		 holder.groupname.setText((String)data.get(position).get("groupName"));  
         holder.remark.setText((String)data.get(position).get("remark"));  
         holder.createtime.setText((String)data.get(position).get("createDate"));
		return convertView;
	}
	
	public void setData(List<Map<String, Object>> list) {
		// TODO Auto-generated method stub
		data = list;
	}
	
	class ViewHolder  
	{  
	    public TextView groupname;  
	    public TextView remark;  
	    public TextView createtime;  
	};
} ;

