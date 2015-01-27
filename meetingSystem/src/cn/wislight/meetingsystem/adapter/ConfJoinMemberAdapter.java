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

public class ConfJoinMemberAdapter extends BaseAdapter{
		private Context mcontext;
		private LayoutInflater mInflater;
		private List<Map<String, Object>> data;
		
		public ConfJoinMemberAdapter(Context context) {
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
				convertView=mInflater.inflate(R.layout.conference_joinmember_item, null);

			    holder = new ViewHolder();  
                //根据自定义的Item布局加载布局   
                holder.name = (TextView)convertView.findViewById(R.id.conf_select_applicator_item_name);  
                holder.org = (TextView)convertView.findViewById(R.id.conf_select_applicator_item_org);  
                holder.post = (TextView)convertView.findViewById(R.id.conf_select_applicator_item_post);
                holder.seats = (TextView)convertView.findViewById(R.id.conf_seats);
                holder.meals = (TextView)convertView.findViewById(R.id.conf_meals);
                holder.accommondation = (TextView)convertView.findViewById(R.id.conf_accommondation);
                convertView.setTag(holder);  
				
			}else{
				holder = (ViewHolder)convertView.getTag(); 
			}
			
			 holder.name.setText((String)data.get(position).get("name"));  
	         holder.org.setText((String)data.get(position).get("org"));  
	         holder.post.setText((String)data.get(position).get("post"));  
	         holder.seats.setText((String)data.get(position).get("seats"));  
	         holder.meals.setText((String)data.get(position).get("meals"));  
	         holder.accommondation.setText((String)data.get(position).get("accommondation"));  

			
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
		    public TextView seats;
		    public TextView meals;
		    public TextView accommondation;
		    
		};
} ;


