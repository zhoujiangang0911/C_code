package cn.wislight.publicservice.adapter;

import java.util.List;
import java.util.Map;

import cn.wislight.publicservice.R;
import cn.wislight.publicservice.entity.BusinessAffair;
import cn.wislight.publicservice.entity.GovermentAffair;
import cn.wislight.publicservice.ui.commercialtenant.FindBuinessActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ServantHandingListAdapter  extends BaseAdapter{
	private Context mcontext;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> data;
	
	public ServantHandingListAdapter(Context context) {
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
			convertView=mInflater.inflate(R.layout.item_servant_handling_list, null);

		    holder = new ViewHolder();  
            //根据自定义的Item布局加载布局   
		    holder.name=(TextView) convertView.findViewById(R.id.tv_grabbill_name);
			holder.des=(TextView) convertView.findViewById(R.id.tv_garbbill_des);
			holder.price=(TextView) convertView.findViewById(R.id.tv_garbbill_price);
			holder.time=(TextView) convertView.findViewById(R.id.tv_grabbill_time);
			holder.distance=(TextView) convertView.findViewById(R.id.tv_grabbill_distance);
			holder.grabbillBtn=(TextView) convertView.findViewById(R.id.tv_grabbill_btn);
			holder.state = (TextView) convertView.findViewById(R.id.tv_grabbill_state);
			holder.photo=(ImageView) convertView.findViewById(R.id.iv_grabbill_photo);
			
            convertView.setTag(holder);  
			
		}else{
			holder = (ViewHolder)convertView.getTag(); 
		}
		String mName = (String)data.get(position).get("content");
		//当为null的时候显示空不显示null
		holder.name.setText((String)data.get(position).get("servicetype"));
		holder.des.setText(mName);
		
//		holder.name.setText((String)data.get(position).get("content"));  
//		holder.des.setText((String)data.get(position).get("content"));  
		holder.price.setText("成交价格："+(String)data.get(position).get("price"));  
		holder.time.setText(((String)data.get(position).get("sendtime")).substring(0, 10));  
		holder.distance.setText("距离:2公里"); 
		String govermentaffairState = (String)data.get(position).get("state");
		String negotiation_state = (String)data.get(position).get("negotiation_state");
		//if(businesaffairState.equals("2")){
		if(govermentaffairState.equals(GovermentAffair.STATE.DAIJIESHOU)){
			holder.state.setText("待接受");  
			holder.state.setVisibility(View.VISIBLE);		
		} else if(govermentaffairState.equals(GovermentAffair.STATE.CHULIZHONG)){
			holder.state.setText("处理中");  
			holder.state.setVisibility(View.VISIBLE);		
		} else if(govermentaffairState.equals(GovermentAffair.STATE.YIWANJIE)){
			holder.state.setText("已完成");  
			holder.state.setVisibility(View.VISIBLE);		
		} else if(govermentaffairState.equals(GovermentAffair.STATE.YIGUIDANG)){
			holder.state.setText("已归档");  
			holder.state.setVisibility(View.VISIBLE);		
		} 
		
		return convertView;
	}
	
	public void setData(List<Map<String, Object>> list) {
		// TODO Auto-generated method stub
		data = list;
	}
	
	class ViewHolder  
	{  
	    public TextView name;  
	    public TextView des;  
	    public TextView price;
	    public TextView time; 
	    public TextView distance;  
	    public TextView grabbillBtn;  
	    public TextView state;
	    public ImageView photo;  
	};
} ;