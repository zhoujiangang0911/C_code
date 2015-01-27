package cn.wislight.meetingsystem.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.wislight.meetingsystem.R;


public class ConfCommonListAdapter  extends BaseAdapter{
	private Context mcontext;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> data;
	
	public ConfCommonListAdapter(Context context) {
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
			convertView=mInflater.inflate(R.layout.conference_common_list_item, null);

		    holder = new ViewHolder();  
            //根据自定义的Item布局加载布局   
            holder.title = (TextView)convertView.findViewById(R.id.conference_common_list_item_title);  
            holder.address = (TextView)convertView.findViewById(R.id.conference_common_list_item_address); 
            holder.remark = (TextView)convertView.findViewById(R.id.conference_common_list_item_remark);             
            holder.time = (TextView)convertView.findViewById(R.id.conference_common_list_item_time);
            convertView.setTag(holder);  
			
		}else{
			holder = (ViewHolder)convertView.getTag(); 
		}
		
		 holder.title.setText((String)data.get(position).get("title"));  
         holder.address.setText((String)data.get(position).get("address"));  
         holder.remark.setText((String)data.get(position).get("remark")); 
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
	    public TextView title;  
	    public TextView address;  
	    public TextView remark; 
	    public TextView time;  
	};
} ;