package cn.wislight.meetingsystem.adapter;

import java.util.List;

import cn.wislight.meetingsystem.R;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TopicHouseAdapter extends BaseAdapter{
	private List<Bundle> list=null;
	private Context context=null;
	public TopicHouseAdapter(List<Bundle>  mList,Context mContext){
		this.list=mList;
		this.context=mContext;
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if (convertView==null) {
			convertView=LayoutInflater.from(context).inflate(R.layout.topichouse_lv_item, null);
			holder=new ViewHolder();
			holder.topic_house_lvtitle=(TextView) convertView.findViewById(R.id.topic_house_lvtitle);
			holder.topic_house_lvtopicdesc=(TextView) convertView.findViewById(R.id.topic_house_lvtopicdesc);
			holder.topic_house_lvtopickeywords=(TextView) convertView.findViewById(R.id.topic_house_lvtopickeywords);
			holder.topic_house_lvtopictime=(TextView) convertView.findViewById(R.id.topic_house_lvtopictime);
			holder.topic_house_lvtopicdesc2=(TextView) convertView.findViewById(R.id.topic_house_lvtopicdesc2);
			holder.topic_house_lvtopickeywords2=(TextView) convertView.findViewById(R.id.topic_house_lvtopickeywords2);
			holder.topic_house_lvtopictime2=(TextView) convertView.findViewById(R.id.topic_house_lvtopictime2);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.topic_house_lvtitle.setText(list.get(position).getString("title"));
		holder.topic_house_lvtopicdesc.setText(list.get(position).getString("desc"));
		holder.topic_house_lvtopickeywords.setText(list.get(position).getString("keywords"));
		holder.topic_house_lvtopictime.setText(list.get(position).getString("time"));
		holder.topic_house_lvtopicdesc2.setText(list.get(position).getString("desc2"));
		holder.topic_house_lvtopickeywords2.setText(list.get(position).getString("keywords2"));
		holder.topic_house_lvtopictime2.setText(list.get(position).getString("time2"));
		return convertView;
	}
	class ViewHolder{
		TextView topic_house_lvtitle,topic_house_lvtopicdesc,topic_house_lvtopickeywords,topic_house_lvtopictime;
		TextView topic_house_lvtopicdesc2,topic_house_lvtopickeywords2,topic_house_lvtopictime2;
	}
}
