package cn.wislight.meetingsystem.adapter;

import java.util.List;
import java.util.Map;

import cn.wislight.meetingsystem.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TodayScheduleAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> data;

	public TodayScheduleAdapter(Context mcontext) {
		this.mContext=mcontext;
		mInflater=LayoutInflater.from(mContext);
	}
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		//return data.get(position);
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView==null){
			holder=new ViewHolder();
			convertView=mInflater.inflate(R.layout.today_schedule_item, null);
			
            //根据自定义的Item布局加载布局   
            holder.title = (TextView)convertView.findViewById(R.id.tv_today_schedule_title);  
            holder.address = (TextView)convertView.findViewById(R.id.tv_today_schedule_address); 
            holder.remark = (TextView)convertView.findViewById(R.id.tv_today_schedule_value);             
            holder.time = (TextView)convertView.findViewById(R.id.tv_today_schedule_time);
            holder.monthdate = (TextView)convertView.findViewById(R.id.tv_today_schedule_month_date);
            holder.typename = (TextView)convertView.findViewById(R.id.tv_today_schedule_typename);

 
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		 holder.title.setText((String)data.get(position).get("title"));  
         holder.address.setText((String)data.get(position).get("address"));  
         holder.remark.setText((String)data.get(position).get("remark")); 
         holder.time.setText((String)data.get(position).get("starttime") + " -- "
        		 		+  (String)data.get(position).get("endtime"));  
		 holder.monthdate.setText((String)data.get(position).get("monthdate"));  
		 holder.typename.setText((String)data.get(position).get("typename"));  
         
		return convertView;
	}

	public void setData(List<Map<String, Object>> list) {
		data = list;
	}	
	
	class ViewHolder  
	{  
	    public TextView title;  
	    public TextView address;  
	    public TextView remark; 
	    public TextView time;  
	    public TextView monthdate;  
	    public TextView typename;  
	};
}
