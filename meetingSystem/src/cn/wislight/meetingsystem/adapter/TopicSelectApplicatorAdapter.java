package cn.wislight.meetingsystem.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.wislight.meetingsystem.R;

public class TopicSelectApplicatorAdapter extends BaseAdapter{
		private Context mcontext;
		private LayoutInflater mInflater;
		private List<Map<String, Object>> data;
		
		public TopicSelectApplicatorAdapter(Context context) {
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
				convertView=mInflater.inflate(R.layout.topic_select_applicator_item, null);

			    holder = new ViewHolder();  
                //根据自定义的Item布局加载布局   
                holder.name = (TextView)convertView.findViewById(R.id.name);  
                holder.org = (TextView)convertView.findViewById(R.id.org);  
                holder.post = (TextView)convertView.findViewById(R.id.post);
                convertView.setTag(holder);  
				
			}else{
				holder = (ViewHolder)convertView.getTag(); 
			}
			
			 holder.name.setText((String)data.get(position).get("name"));  
	         holder.org.setText((String)data.get(position).get("org"));  
	         holder.post.setText((String)data.get(position).get("post"));  
			
			return convertView;
		}
		
		public void setData(List<Map<String, Object>> list) {
			// TODO Auto-generated method stub
			data = list;
		}
		class ViewHolder  
		{  
		    public TextView name;  
		    public TextView org;  
		    public TextView post;  
		};
} ;


