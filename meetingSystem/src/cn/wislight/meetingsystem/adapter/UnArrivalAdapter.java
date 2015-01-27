package cn.wislight.meetingsystem.adapter;

import java.util.ArrayList;
import java.util.List;

import cn.wislight.meetingsystem.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class UnArrivalAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private List<String> data=new ArrayList<String>();

	public UnArrivalAdapter(Context mcontext) {
		this.mContext=mcontext;
		mInflater=LayoutInflater.from(mContext);
	}
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView=mInflater.inflate(R.layout.un_arrival_item, null);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}



	static class ViewHolder{
		TextView tvName;
	}

	public void addDatas(List<String> datas) {
		data.addAll(datas);
		notifyDataSetChanged();
	}

	public void clear() {
		data.clear();
		notifyDataSetChanged();
	}
}
