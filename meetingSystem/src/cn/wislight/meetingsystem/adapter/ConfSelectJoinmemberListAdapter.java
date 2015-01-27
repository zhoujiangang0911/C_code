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
import cn.wislight.meetingsystem.util.Element;

public class ConfSelectJoinmemberListAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<Element> data = null;
	
	public ConfSelectJoinmemberListAdapter() {

	}
	
	public void setData(ArrayList<Element> list2) {
		data = list2;
	}

	public ConfSelectJoinmemberListAdapter(Context mcontext) {
		this.mContext = mcontext;
		mInflater = LayoutInflater.from(mContext);
	}

	public void checkAll() {
		for (Element ele : data) {
			ele.isCheck = true;
		}
		notifyDataSetChanged();
	}

	public void noCheckAll() {
		for (Element ele : data) {
			ele.isCheck = false;
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

			convertView = mInflater.inflate(
					R.layout.conference_select_joinmember_item, null);
			viewHolder = new ViewHolder();
			viewHolder.checkBox = (CheckBox) convertView
					.findViewById(R.id.checkBox1);
			viewHolder.name = (TextView)convertView.findViewById(R.id.name);  
			viewHolder.org = (TextView)convertView.findViewById(R.id.org);  
			viewHolder.post = (TextView)convertView.findViewById(R.id.post);
			
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.name.setText((String)data.get(position).getName());  
		viewHolder.org.setText((String)data.get(position).getOrg());  
		viewHolder.post.setText((String)data.get(position).getPost());  
         
		final Element ele = data.get(position);
		viewHolder.checkBox.setChecked(ele.isCheck);
		viewHolder.checkBox.setOnClickListener(new OnClickListener() {
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
	class ViewHolder {
		public CheckBox checkBox;
	    public TextView name;  
	    public TextView org;  
	    public TextView post;  
	}
}
