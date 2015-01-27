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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.domain.GroupElement;
import cn.wislight.meetingsystem.ui.setting.CommonGroupAddOneActivity;
import cn.wislight.meetingsystem.ui.setting.CommonGroupMemberActivity;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.Element;
import cn.wislight.meetingsystem.util.IntentUtil;
import cn.wislight.meetingsystem.util.LoadingDialog;
import cn.wislight.meetingsystem.util.MeetingSystemClient;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * @author Administrator
 * 群组设置
 */
public class TopicSelectSuggestedGroupActivity extends BaseActivity {
	private ListView listView;
	private ArrayList<GroupElement> list;
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
				Toast.makeText(TopicSelectSuggestedGroupActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
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
					Toast.makeText(TopicSelectSuggestedGroupActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				listView.setVisibility(View.VISIBLE);		
				
				GroupSelListAdapter adapter = new GroupSelListAdapter(TopicSelectSuggestedGroupActivity.this);
				list = new ArrayList<GroupElement>(); 
				
				try {
					 JSONArray jsonArray=new JSONArray(response);
					 for(int i=0;i<jsonArray.length();i++){
			               JSONObject jsonObject=(JSONObject)jsonArray.get(i);
			               GroupElement ge1 = new GroupElement();
			               ge1.setGroupName(jsonObject.getString("groupName"));
			               ge1.setCreateDate(jsonObject.getString("createData"));
			               ge1.setRemark(jsonObject.getString("remark"));
			               ge1.setId(jsonObject.getString("id"));
			               list.add(ge1);
					 }
					
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(TopicSelectSuggestedGroupActivity.this, getString(R.string.error_dataabout), Toast.LENGTH_SHORT).show();
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
		                data.putExtra(Constants.ID, (String)list.get(arg2).getId());  
		                IntentUtil.startActivity(TopicSelectSuggestedGroupActivity.this, CommonGroupMemberActivity.class, data); 
		                
		            }
		        });
			}
		});		
	}
	
	public void clickSubmit(View v){
		
		ArrayList<GroupElement> selList = new ArrayList<GroupElement>();
		
		for  (GroupElement ge: list ){
			if (ge.isCheck){
				GroupElement ge1 = new GroupElement();
				ge1.copy(ge);
				selList.add(ge1);
			}
		}
		
        Intent data=new Intent();
        data.putExtra(Constants.GROUPLIST, selList);
        setResult(Constants.OK, data);  
        finish();  
	}

	@Override
	public void setupView() {
		setContentView(R.layout.topic_add_suggested_group);
	}
	
	public void clickNewGroup(View view){
		IntentUtil.startActivity(TopicSelectSuggestedGroupActivity.this, CommonGroupAddOneActivity.class);
	}
	
	
}


class GroupSelListAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<GroupElement> data = null;
	
	public GroupSelListAdapter() {

	}
	
	public void setData(ArrayList<GroupElement> list2) {
		// TODO Auto-generated method stub
		data = list2;
	}

	public GroupSelListAdapter(Context mcontext) {
		this.mContext = mcontext;
		mInflater = LayoutInflater.from(mContext);
	}

	public void checkAll() {
		for (GroupElement ele : data) {
			ele.isCheck = true;
		}
		notifyDataSetChanged();
	}

	public void noCheckAll() {
		for (GroupElement ele : data) {
			ele.isCheck = false;
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return data == null ? 0 : data.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {

			convertView = mInflater.inflate(
					R.layout.system_setting_common_group_list_selitem, null);
			viewHolder = new ViewHolder();
			viewHolder.checkBox = (CheckBox) convertView
					.findViewById(R.id.checkBox1);
			viewHolder.groupname = (TextView)convertView.findViewById(R.id.tv_groupname);  
			viewHolder.remark = (TextView)convertView.findViewById(R.id.tv_remark);  
			viewHolder.createtime = (TextView)convertView.findViewById(R.id.tv_createtime);
			
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.groupname.setText((String)data.get(position).getGroupName());  
		viewHolder.remark.setText((String)data.get(position).getRemark());  
		viewHolder.createtime.setText((String)data.get(position).getCreateDate());  
         
		final GroupElement ele = data.get(position);
		viewHolder.checkBox.setChecked(ele.isCheck);
		viewHolder.checkBox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (ele.isCheck) {
					ele.isCheck = false;
				} else {
					ele.isCheck = true;
				}
			}
		});
		return convertView;
	}
	
	class ViewHolder  
	{   
		public CheckBox checkBox;
	    public TextView groupname;  
	    public TextView remark;  
	    public TextView createtime;  
	};
} ;

