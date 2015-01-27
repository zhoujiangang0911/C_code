package cn.wislight.publicservice.adapter;

import java.util.List;
import java.util.Map;

import cn.wislight.publicservice.R;
import cn.wislight.publicservice.entity.BusinessAffair;
import cn.wislight.publicservice.ui.commercialtenant.FindBuinessActivity;
import cn.wislight.publicservice.util.StringToChange;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BusinessAffairListAdapter  extends BaseAdapter{
	private Context mcontext;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> data;
	
	public BusinessAffairListAdapter(Context context) {
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
			convertView=mInflater.inflate(R.layout.item_businessaffair_list_qiangdan, null);

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
		
		holder.name.setText((String)data.get(position).get("servicetype"));  
		holder.des.setText((String)data.get(position).get("content"));  
		holder.price.setText("成交价格："+(String)data.get(position).get("price")); 
		String time = (String)data.get(position).get("sendtime");
		holder.time.setText(StringToChange.toNoT(time));  
		holder.distance.setText("距离:2公里"); 
		String businesaffairState = (String)data.get(position).get("state");
		String negotiation_state = (String)data.get(position).get("negotiation_state");
		//if(businesaffairState.equals("2")){
		if(businesaffairState.equals(BusinessAffair.STATE.DAIQIANGDAN)){
			System.out.println("wangting: negotiation_state="+ negotiation_state);
			if("1".equals(negotiation_state)){
				holder.grabbillBtn.setText("议价");  
				holder.grabbillBtn.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						((FindBuinessActivity) mcontext).clickYijia(mcontext, finalPosition);
					}
					
				});		
			} else if("2".equals(negotiation_state)){
				//holder.price.setVisibility(View.VISIBLE);
				holder.state.setText("已出价");  
				holder.state.setVisibility(View.VISIBLE);
				holder.grabbillBtn.setVisibility(View.GONE);	
			} else if("3".equals(negotiation_state)){
				//holder.price.setVisibility(View.VISIBLE);
				holder.state.setText("已成交");  
				holder.state.setVisibility(View.VISIBLE);
				holder.grabbillBtn.setVisibility(View.GONE);		
			 }else if("4".equals(negotiation_state)){
				//holder.price.setVisibility(View.VISIBLE);
				holder.state.setText("发单方已终止");  
				holder.state.setVisibility(View.VISIBLE);
				holder.grabbillBtn.setVisibility(View.GONE);	
		     }else if("5".equals(negotiation_state)){
				//holder.price.setVisibility(View.VISIBLE);
				holder.state.setText("接单方已终止");  
				holder.state.setVisibility(View.VISIBLE);
				holder.grabbillBtn.setVisibility(View.GONE);	
		     }else if("6".equals(negotiation_state)){
				//holder.price.setVisibility(View.VISIBLE);
				holder.state.setText("其他人已成交");  
				holder.state.setVisibility(View.VISIBLE);
				holder.grabbillBtn.setVisibility(View.GONE);	
		     }else {
				holder.grabbillBtn.setText("抢单");  
				holder.grabbillBtn.setOnClickListener(new OnClickListener(){	
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub					
						((FindBuinessActivity) mcontext).clickQiangdan(mcontext,finalPosition);
					}					
				});	
			}
		
				
			
		} else if(businesaffairState.equals(BusinessAffair.STATE.DAIFAHUO)){
			holder.price.setVisibility(View.VISIBLE);
			holder.state.setText("待发货");  
			holder.state.setVisibility(View.VISIBLE);
			holder.grabbillBtn.setVisibility(View.GONE);			
		}else if(businesaffairState.equals(BusinessAffair.STATE.DAISHOUHUO)){
			holder.price.setVisibility(View.VISIBLE);
			holder.state.setText("待收货");  	
			holder.state.setVisibility(View.VISIBLE);
			holder.grabbillBtn.setVisibility(View.GONE);
		}
		
		//holder.photo.setBackgroundResource((Integer)data.get(position).get("title"));  
		
		return convertView;
	}
	
	public void setData(List<Map<String, Object>> list) {
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
} 