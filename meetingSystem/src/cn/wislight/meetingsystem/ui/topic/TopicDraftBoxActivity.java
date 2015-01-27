package cn.wislight.meetingsystem.ui.topic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.service.DbAdapter;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.IntentUtil;

/**
 * @author Administrator
 * 议题库
 */
public class TopicDraftBoxActivity extends BaseActivity {
	private ListView listView;

	private List<Map<String, Object>> list;
	
	@Override
	public void initView() {
		listView=(ListView) findViewById(R.id.lv_wait_my_check);
		
		
		loadDraftBoxList();
		
	}

	@Override
	public void setupView() {
		setContentView(R.layout.topic_draft_box);
		
	}
	
	private void loadDraftBoxList() {

		DbAdapter dbhandle = new DbAdapter(this);
		dbhandle.open();
		DraftBoxListAdapter mAdapter = new DraftBoxListAdapter(
				TopicDraftBoxActivity.this);
		list = new ArrayList<Map<String, Object>>();
		Cursor cursor = dbhandle.getTopicDrafts();
		Map<String, Object> map;

		if (cursor.moveToFirst()) {
			do {

				map = new HashMap<String, Object>();
				map.put("starttime", cursor.getString(5));
				map.put("endtime", cursor.getString(4));
				map.put("keywords", cursor.getString(2));
				map.put("summary", cursor.getString(3));
				map.put("id", cursor.getString(0));

				list.add(map);

			} while (cursor.moveToNext());
		}
		dbhandle.close();
		mAdapter.setData(list);
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent data = new Intent();
				data.putExtra(Constants.ID,
						(String) list.get(arg2).get("id"));
				IntentUtil.startActivity(TopicDraftBoxActivity.this,
						TopicAddOneActivity.class, data);
				TopicDraftBoxActivity.this.finish();
			}
		});

	}

}


class DraftBoxListAdapter extends BaseAdapter{
	private Context mcontext;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> data;
	
	public DraftBoxListAdapter(Context context) {
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
