package cn.wislight.publicservice.adapter;

import java.util.List;
import java.util.Map;

import cn.wislight.publicservice.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class IMMessageListAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private List<Map<String,String>> data;
	private String myName;
	public IMMessageListAdapter(Context mcontext) {
		this.mContext=mcontext;
		mInflater=LayoutInflater.from(mContext);
	}
	public void setData(List<Map<String, String>> list) {
		data = list;
	}
	
	public String getMyName() {
		return myName;
	}
	
	public void setMyName(String myName) {
		this.myName = myName;
	}
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
//		return null;
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
			convertView=mInflater.inflate(R.layout.im_message_list_item, null);
			
			viewHolder.tvOthers=(LinearLayout) convertView.findViewById(R.id.topic_message_others);
			viewHolder.tvMime=(LinearLayout) convertView.findViewById(R.id.topic_message_mime);
			viewHolder.tvMimeName=(TextView) convertView.findViewById(R.id.topic_message_mime_name);
			viewHolder.tvOthersName=(TextView) convertView.findViewById(R.id.topic_message_others_name);
			viewHolder.tvOthersMessage=(TextView) convertView.findViewById(R.id.topic_message_others_message);
			viewHolder.tvMimeMessage=(TextView) convertView.findViewById(R.id.topic_message_mime_message);

			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		String value = data.get(finalPosition).get("username");
		if(myName.equals(value)){
			viewHolder.tvOthers.setVisibility(View.GONE);
			viewHolder.tvMime.setVisibility(View.VISIBLE);
			viewHolder.tvMimeName.setText(myName);
			viewHolder.tvMimeMessage.setText(data.get(position).get("message"));
		} else{
			viewHolder.tvOthers.setVisibility(View.VISIBLE);
			viewHolder.tvMime.setVisibility(View.GONE);
			viewHolder.tvOthersName.setText(data.get(position).get("username"));
			viewHolder.tvOthersMessage.setText(data.get(position).get("message"));
		}
		
		return convertView;
	}



	static class ViewHolder{
		LinearLayout tvOthers;
		LinearLayout tvMime;
		TextView tvMimeName;
		TextView tvOthersName;
		TextView tvOthersMessage;
		TextView tvMimeMessage;

	}


	public void clear() {
		data.clear();
		notifyDataSetChanged();
	}
}
