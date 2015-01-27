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

public class IMMessageHistoryListAdapter  extends BaseAdapter{
	private Context mcontext;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> data;
	
	public IMMessageHistoryListAdapter(Context context) {
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
			convertView=mInflater.inflate(R.layout.activity_user_negotiation_list_item, null);

		    holder = new ViewHolder();  
            //根据自定义的Item布局加载布局   

			//holder.price=(TextView) convertView.findViewById(R.id.businessaffairPrice);
			holder.createtime=(TextView) convertView.findViewById(R.id.businessaffairCreatetime);
			holder.content=(TextView) convertView.findViewById(R.id.businessaffairContent);
			holder.price=(TextView) convertView.findViewById(R.id.businessaffairPrice);
			holder.negotiationPrice=(TextView) convertView.findViewById(R.id.negotiationPrice);

			holder.receivername=(TextView) convertView.findViewById(R.id.businessaffairReceiverName);
			holder.state = (TextView) convertView.findViewById(R.id.negotiationState);
			//holder.photo=(ImageView) convertView.findViewById(R.id.iv_grabbill_photo);
			
            convertView.setTag(holder);  
			
		}else{
			holder = (ViewHolder)convertView.getTag(); 
		}
		
		//holder.name.setText((String)data.get(position).get("content"));  
		//holder.des.setText((String)data.get(position).get("content"));  
		holder.content.setText((String)data.get(position).get("content")); 
		holder.receivername.setText((String)data.get(position).get("receivername"));
		holder.price.setText((String)data.get(position).get("price"));  
		holder.negotiationPrice.setText((String)data.get(position).get("negotiationprice"));
		String time = (String)data.get(position).get("sendtime");
		holder.createtime.setText(StringToChange.toNoT(time));  
		//holder.distance.setText("距离:2公里"); 
		//String state = (String)data.get(position).get("state");
		String negotiation_state = (String)data.get(position).get("negotiation_state");
		//if(state.equals("2"))
		{
			System.out.println("wangting: negotiation_state="+ negotiation_state);
			if("1".equals(negotiation_state)){
				holder.state.setText("待议价");  
				/*
				holder.state.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						((FindBuinessActivity) mcontext).clickYijia(mcontext, finalPosition);
					}
					
				});		
				*/
			} else if("2".equals(negotiation_state)){
				//holder.price.setVisibility(View.VISIBLE);
				holder.state.setText("已出价");  


			} else if("3".equals(negotiation_state)){
				//holder.price.setVisibility(View.VISIBLE);
				holder.state.setText("已成交");  
				//holder.state.setVisibility(View.VISIBLE);
				//holder.state.setVisibility(View.GONE);		
			 }else if("4".equals(negotiation_state)){
				//holder.price.setVisibility(View.VISIBLE);
				holder.state.setText("发单方已终止");  
				//holder.state.setVisibility(View.VISIBLE);
				//holder.state.setVisibility(View.GONE);	
		     }else if("5".equals(negotiation_state)){
				//holder.price.setVisibility(View.VISIBLE);
				holder.state.setText("接单方已终止");  
				//holder.state.setVisibility(View.VISIBLE);
				//holder.state.setVisibility(View.GONE);	
		     }else if("6".equals(negotiation_state)){
				//holder.price.setVisibility(View.VISIBLE);
				holder.state.setText("其他人已成交");  
				//holder.state.setVisibility(View.VISIBLE);
				//holder.state.setVisibility(View.GONE);	
		     }
		}
		
		
			
		
		//holder.photo.setBackgroundResource((Integer)data.get(position).get("title"));  
		
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
	    public TextView negotiationPrice;
	    //public ImageView photo;
	};
} ;