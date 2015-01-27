package cn.wislight.meetingsystem.adapter;

import java.util.List;
import java.util.Map;

import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.domain.AttenderVoteElement;
import cn.wislight.meetingsystem.util.Element;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class TopicAvrVoteAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private List<AttenderVoteElement> data;


	public TopicAvrVoteAdapter(Context mcontext) {
		this.mContext = mcontext;
		mInflater = LayoutInflater.from(mContext);
	}

	public void setData(List<AttenderVoteElement> list) {
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
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.topic_avrvote_list_item,
					null);
			viewHolder.checbox1 = (CheckBox) convertView.findViewById(R.id.checkbox1);
			viewHolder.tvName = (TextView)convertView.findViewById(R.id.tv_name);
			viewHolder.tvJoinState = (TextView)convertView.findViewById(R.id.tv_join_state);
			viewHolder.voteState = (TextView)convertView.findViewById(R.id.tv_vote_state);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tvName.setText(data.get(position).name);
		int votestate = data.get(position).iVotestate;
		int joinState = data.get(position).iJoinstate;
		if (joinState == -1){
			viewHolder.tvJoinState.setText("还未点名");	
			viewHolder.tvJoinState.setTextColor(0xFFCC1111);
		}else if(joinState == 0){
			viewHolder.tvJoinState.setText("请假");
			viewHolder.tvJoinState.setTextColor(0xFFCC1111);
		}else if(joinState == 1){
			viewHolder.tvJoinState.setText("出席会议");
			viewHolder.tvJoinState.setTextColor(0xFF11CC11);
		}else if(joinState == 2){
			viewHolder.tvJoinState.setText("缺席会议");
			viewHolder.tvJoinState.setTextColor(0xFFCC1111);
		}

		if (votestate == -1){
			viewHolder.voteState.setText("还未投票");	
			viewHolder.voteState.setTextColor(0xFFCC1111);
		}
		else{
			viewHolder.voteState.setText("已经投票");	
			viewHolder.voteState.setTextColor(0xFF11CC11);
		}
		
		final AttenderVoteElement ele = data.get(position);
		viewHolder.checbox1.setChecked(ele.isCheck);
		viewHolder.checbox1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (ele.isCheck) {
					ele.isCheck = false;
				} else {
					ele.isCheck = true;
				}
			}
		});
		return convertView;
	}

	static class ViewHolder {
		CheckBox checbox1;
		TextView tvName;
		TextView tvJoinState;
		TextView voteState;
	}

	public void clear() {
		data.clear();
		notifyDataSetChanged();
	}

}
