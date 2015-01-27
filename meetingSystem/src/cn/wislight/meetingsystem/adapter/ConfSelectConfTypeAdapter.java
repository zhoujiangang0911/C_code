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

public class ConfSelectConfTypeAdapter extends BaseAdapter{
		private Context mcontext;
		private LayoutInflater mInflater;
		private List<Map<String, Object>> data;
		
		public ConfSelectConfTypeAdapter(Context context) {
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
				convertView=mInflater.inflate(R.layout.conference_select_conftype_item, null);

			    holder = new ViewHolder();  
                //根据自定义的Item布局加载布局   
                //holder.id = (TextView)convertView.findViewById(R.id.conf_select_conftype_item_id);  
                holder.typename = (TextView)convertView.findViewById(R.id.conf_select_conftype_item_typename);  
                //holder.level = (TextView)convertView.findViewById(R.id.conf_select_conftype_item_level);
                convertView.setTag(holder);  
				
			}else{
				holder = (ViewHolder)convertView.getTag(); 
			}
			
			//holder.id.setText((String)data.get(position).get("id"));  
	         holder.typename.setText((String)data.get(position).get("typename"));  
	         //holder.level.setText((String)data.get(position).get("level"));  
			
			return convertView;
		}
		
		public void setData(List<Map<String, Object>> list) {
			// TODO Auto-generated method stub
			data = list;
		}
		class ViewHolder  
		{  
		    //public TextView id;  
		    public TextView typename;  
		   // public TextView level;  
		};
} ;


