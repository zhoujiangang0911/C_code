package cn.wislight.meetingsystem.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.wislight.meetingsystem.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class VoteAnonymousNameAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private List<Map<String, String>> data=new ArrayList<Map<String, String>>();

	public VoteAnonymousNameAdapter(Context mcontext) {
		this.mContext=mcontext;
		mInflater=LayoutInflater.from(mContext);
	}
	public void setData(List<Map<String, String>> list) {
		data = list;
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
		final int finalPosition = position;
		ViewHolder viewHolder;
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView=mInflater.inflate(R.layout.vote_anonymous_name_item, null);
			viewHolder.tvName=(TextView) convertView.findViewById(R.id.tv_vote_name);
			viewHolder.rgResult = (RadioGroup) convertView.findViewById(R.id.vote_manager);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tvName.setText(data.get(position).get("name"));
		viewHolder.rgResult.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				System.out.println("wangting:position="+finalPosition);
				if(arg1 == R.id.vote_vote){
					data.get(finalPosition).put("value","1");
				}

				if(arg1 == R.id.vote_giveup){
					data.get(finalPosition).put("value","2");
				}				
			}
			
		});
		return convertView;
	}



	static class ViewHolder{
		TextView tvName;
		RadioGroup rgResult;
	}


	public void clear() {
		data.clear();
		notifyDataSetChanged();
	}
}
