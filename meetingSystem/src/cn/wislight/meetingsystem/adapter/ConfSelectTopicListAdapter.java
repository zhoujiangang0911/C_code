package cn.wislight.meetingsystem.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.domain.Topic;

public class ConfSelectTopicListAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<Topic> data = null;
	
	public ConfSelectTopicListAdapter() {

	}
	
	public void setData(ArrayList<Topic> list2) {
		data = list2;
	}

	public ConfSelectTopicListAdapter(Context mcontext) {
		this.mContext = mcontext;
		mInflater = LayoutInflater.from(mContext);
	}

	public void checkAll() {
		for (Topic ele : data) {
			ele.setCheck(true);//isCheck = true;
		}
		notifyDataSetChanged();
	}

	public void noCheckAll() {
		for (Topic ele : data) {
			//ele.isCheck = false;
			ele.setCheck(false);
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return data == null ? 0 : data.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {

			convertView = mInflater.inflate(R.layout.conference_begin_three_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.conf_begin_three_list_item_checkbox);
			viewHolder.title = (TextView)convertView.findViewById(R.id.conf_begin_three_list_item_title);  
			viewHolder.content = (TextView)convertView.findViewById(R.id.conf_begin_three_list_item_content);  
			
			viewHolder.time = (TextView)convertView.findViewById(R.id.conf_begin_three_list_item_time);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.title.setText((String)data.get(position).getTitle());  
		viewHolder.content.setText((String)data.get(position).getContent());  
		//viewHolder.keywords.setText((String)data.get(position).getKeywords());  
		viewHolder.time.setText((String)data.get(position).getTime());  
         
		final Topic ele = data.get(position);
		viewHolder.checkbox.setChecked(ele.isCheck());
		viewHolder.checkbox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (ele.isCheck()) {
					ele.setCheck(false);
				} else {
					ele.setCheck(true);
				}
			}
		});
		return convertView;
	}
	class ViewHolder {
		public CheckBox checkbox;
	    public TextView title;  
	    public TextView content;  
	    public TextView keywords;  
	    public TextView time;  
	}
}
