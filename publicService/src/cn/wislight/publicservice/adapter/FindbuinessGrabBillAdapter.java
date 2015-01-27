package cn.wislight.publicservice.adapter;

import java.util.List;

import cn.wislight.publicservice.R;
import cn.wislight.publicservice.entity.GrabBillEntity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Administrator
 *	抢生意
 */
public class FindbuinessGrabBillAdapter extends BaseAdapter{
	private Context context;
	private List<GrabBillEntity> listData;
	public FindbuinessGrabBillAdapter(Context context,List<GrabBillEntity> listData){
		this.context=context;
		this.listData=listData;
	}
	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView==null){
			LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView=inflater.inflate(R.layout.item_findbuiness_grabbill_,parent,false);
		holder=new ViewHolder();
		holder.name=(TextView) convertView.findViewById(R.id.tv_grabbill_name);
		holder.des=(TextView) convertView.findViewById(R.id.tv_garbbill_des);
		holder.time=(TextView) convertView.findViewById(R.id.tv_grabbill_time);
		holder.distance=(TextView) convertView.findViewById(R.id.tv_grabbill_distance);
		holder.grabbillBtn=(TextView) convertView.findViewById(R.id.tv_grabbill_btn);
		holder.photo=(ImageView) convertView.findViewById(R.id.iv_grabbill_photo);
		convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.name.setText(listData.get(position).getName());
		holder.des.setText(listData.get(position).getIntro());
		holder.time.setText(listData.get(position).getTime());
		holder.distance.setText(listData.get(position).getDistance());
		holder.distance.setText(listData.get(position).getDistance());
		holder.photo.setBackgroundResource(listData.get(position).getPhoto());
		return convertView;
	}
	class ViewHolder{
		TextView name,des,time,distance,grabbillBtn;
		ImageView photo;
	}

}
