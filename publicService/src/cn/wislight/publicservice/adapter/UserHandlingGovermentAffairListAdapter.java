package cn.wislight.publicservice.adapter;

import java.util.List;
import java.util.Map;

import cn.wislight.publicservice.R;
import cn.wislight.publicservice.ui.commercialtenant.FindBuinessActivity;
import cn.wislight.publicservice.util.StringToChange;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UserHandlingGovermentAffairListAdapter  extends BaseAdapter{
	private Context mcontext;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> data;
	
	public UserHandlingGovermentAffairListAdapter(Context context) {
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
		final int finalPosition = position;
		ViewHolder holder = null;  
		if(convertView==null){
			convertView=mInflater.inflate(R.layout.activity_user_govermentaffair_list_item, null);

		    holder = new ViewHolder();  
            //根据自定义的Item布局加载布局   

			holder.createtime=(TextView) convertView.findViewById(R.id.govermentaffairCreatetime);
			holder.content=(TextView) convertView.findViewById(R.id.govermentaffairContent);
			//holder.price=(TextView) convertView.findViewById(R.id.govermentaffairPrice);

			holder.receivername=(TextView) convertView.findViewById(R.id.govermentaffairReceiverName);
			//holder.state = (TextView) convertView.findViewById(R.id.state);
			
            convertView.setTag(holder);  
			
		}else{
			holder = (ViewHolder)convertView.getTag(); 
		}
		
		holder.content.setText((String)data.get(position).get("content")); 
		holder.receivername.setText((String)data.get(position).get("receivername"));
		//holder.price.setText((String)data.get(position).get("price"));  
		String time = (String)data.get(position).get("sendtime");
		holder.createtime.setText(StringToChange.toNoT(time));  
		String state = (String)data.get(position).get("state");		 
		
		return convertView;
	}
	
	public void setData(List<Map<String, Object>> list) {
		// TODO Auto-generated method stub
		data = list;
	}
	
	class ViewHolder  
	{  
	    public TextView content;  
	    public TextView receivername;  
	    public TextView price;
	    public TextView createtime; 
	    //public TextView distance;  
	    public TextView state;  
	    //public TextView negotiationPrice;
	    //public ImageView photo;
	};
} ;