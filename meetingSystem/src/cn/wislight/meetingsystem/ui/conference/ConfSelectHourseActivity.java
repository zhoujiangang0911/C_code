package cn.wislight.meetingsystem.ui.conference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
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
 * 会议室
 */
public class ConfSelectHourseActivity extends BaseActivity {
	private ListView listView;
	private List<Map<String, Object>> list;
	private LoadingDialog loadingdiag;
	private int roomId;
	private int pos;
	@Override
	public void initView() {
		listView = (ListView) findViewById(R.id.lv_group);
	}

	protected void onResume(){
		getMeetingHouse();
		super.onResume();
	}
	
	private void getMeetingHouse() {
		// TODO Auto-generated method stub
	String url = "MeetingManage/mobile/findMeetingHourseByOrg.action";
		
		loadingdiag = new LoadingDialog(this);  
		loadingdiag.setCanceledOnTouchOutside(false); 
		loadingdiag.setText(getString(R.string.loading));
		loadingdiag.show();
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();
				Toast.makeText(ConfSelectHourseActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
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
					Toast.makeText(ConfSelectHourseActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				listView.setVisibility(View.VISIBLE);		
				
				MeetHourseListAdapter adapter = new MeetHourseListAdapter(ConfSelectHourseActivity.this);
				list = new ArrayList<Map<String, Object>>(); 
				
				try {
					 JSONArray jsonArray=new JSONObject(response).getJSONArray("glist");
					  
				     Map<String, Object> map;
				        
					 for(int i=0;i<jsonArray.length();i++){
			               JSONObject jsonObject=(JSONObject)jsonArray.get(i);
			               map = new HashMap<String, Object>();
			               map.put("count", jsonObject.getString("count"));
			               map.put("id", jsonObject.getString("id"));
			               map.put("description", jsonObject.getString("description"));
			               map.put("meetingplaces", jsonObject.getString("meetingplaces"));
			               map.put("commonType", jsonObject.getString("commonType"));

			               list.add(map);
					 }
					
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ConfSelectHourseActivity.this, getString(R.string.error_dataabout), Toast.LENGTH_SHORT).show();
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
		                data.putExtra("id",  (String)list.get(arg2).get("id"));
		                data.putExtra("fullAddr",   (String)list.get(arg2).get("meetingplaces") + "\n"
		                		                    + (String)list.get(arg2).get("description"));  
		                setResult(Constants.OK, data);  		                
		                finish();  
		            }
		        });
				/*
				listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
					public boolean onItemLongClick(AdapterView<?> parent,
							View view,  int position, long id) {
						System.out.println("Item LONG clicked. Position:"
								+ position);
						roomId = Integer.parseInt((String)list.get(position).get("id"));
						pos = position;
						
						AlertDialog.Builder builder = new Builder(
								ConfSelectHourseActivity.this);
						builder.setMessage("确定删除此会议室？");
						builder.setTitle("提示");
						builder.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										
										deleteMeetRoom();
									}


								});

						builder.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										
									}
								});
						builder.create().show();
						return true;
					}
				});
				*/
			}
		});		
	}

	@Override
	public void setupView() {
		setContentView(R.layout.meeting_room_main);
	}
	
	public void clickNewHourse(View view){
		IntentUtil.startActivity(ConfSelectHourseActivity.this, MeetHourseAddActivity.class);
	}
	
	private void deleteMeetRoom() {
		// TODO Auto-generated method stub
		String url = "MeetingManage/mobile/deleteMeetingPlceById.action?id=";
		url += roomId;
		
		loadingdiag = new LoadingDialog(this);  
		loadingdiag.setCanceledOnTouchOutside(false); 
		loadingdiag.setText(getString(R.string.deleting));
		loadingdiag.show();
		
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();
				Toast.makeText(ConfSelectHourseActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                finish();  
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				// Toast.makeText(TopicSelectApplicatorActivity.this, new String(response)+"wangting"+response.toString(), 100).show();
				loadingdiag.hide();
				if (response.contains("用户未登陆")){
					Toast.makeText(ConfSelectHourseActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				if (response.contains("true")){
					
				}else{
					try {
						JSONObject jso = new JSONObject(response);
						
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
			}
		});
		
	}
}


class MeetHourseListAdapter extends BaseAdapter{
	private Context mcontext;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> data;
	
	public MeetHourseListAdapter(Context context) {
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
			convertView=mInflater.inflate(R.layout.meet_hourse_list_item, null);

		    holder = new ViewHolder();  
            //根据自定义的Item布局加载布局   
            holder.addr = (TextView)convertView.findViewById(R.id.tv_addr);  
            holder.description = (TextView)convertView.findViewById(R.id.tv_description);  
            holder.type = (TextView)convertView.findViewById(R.id.tv_type);
            holder.count = (TextView)convertView.findViewById(R.id.tv_count);
            convertView.setTag(holder);  
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.addr.setText((String) data.get(position).get("meetingplaces"));
		holder.description.setText((String) data.get(position).get(
				"description"));
		int type = (Integer.parseInt((String)data.get(position).get("commonType")));
		String[] types = {"方形", "圆形"};
		holder.type.setText((String) types[type]);
		holder.count.setText((String) data.get(position).get("count"));
		return convertView;
	}
	
	public void setData(List<Map<String, Object>> list) {
		// TODO Auto-generated method stub
		data = list;
	}
	
	class ViewHolder  
	{  
	    public TextView  addr;  
	    public TextView  description;  
	    public TextView type;  
	    public TextView  count;
	};
} ;

