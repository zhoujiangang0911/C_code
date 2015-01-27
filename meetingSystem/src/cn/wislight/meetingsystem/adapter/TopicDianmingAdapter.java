package cn.wislight.meetingsystem.adapter;

import java.util.List;
import java.util.Map;

import cn.wislight.meetingsystem.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class TopicDianmingAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private List<Map<String, String>> data;
	private Boolean editAble = true;

	public Boolean getEditAble() {
		return editAble;
	}

	public void setEditAble(Boolean editAble) {
		this.editAble = editAble;
	}

	public TopicDianmingAdapter(Context mcontext, boolean editable) {
		this.mContext = mcontext;
		this.editAble = editable;
		mInflater = LayoutInflater.from(mContext);
	}

	public TopicDianmingAdapter(Context mcontext) {
		this.mContext = mcontext;
		mInflater = LayoutInflater.from(mContext);
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
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.topic_dianming_list_item,
					null);
			viewHolder.tvName = (TextView) convertView
					.findViewById(R.id.tv_dianming_name);
			viewHolder.rgResult = (RadioGroup) convertView
					.findViewById(R.id.rg_diming);

			viewHolder.rg1 = (RadioButton) convertView
					.findViewById(R.id.topic_dianming_chuxi);
			viewHolder.rg2 = (RadioButton) convertView
					.findViewById(R.id.topic_dianming_qingjia);
			viewHolder.rg3 = (RadioButton) convertView
					.findViewById(R.id.topic_dianming_weichuxi);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.rgResult.setTag(position);
		viewHolder.rgResult.setOnCheckedChangeListener(null);
		viewHolder.tvName.setText(data.get(position).get("name"));
		String value = data.get(position).get("value");
		if ("0".equals(value)) {
			viewHolder.rgResult.check(R.id.topic_dianming_qingjia);
		} else if ("1".equals(value)) {
			viewHolder.rgResult.check(R.id.topic_dianming_chuxi);
		} else if ("2".equals(value)) {
			viewHolder.rgResult.check(R.id.topic_dianming_weichuxi);
		} else {
			viewHolder.rgResult.clearCheck();
		}

		if (!editAble) {
			viewHolder.rg1.setEnabled(false);
			viewHolder.rg2.setEnabled(false);
			viewHolder.rg3.setEnabled(false);

			viewHolder.rg1.setClickable(false);
			viewHolder.rg2.setClickable(false);
			viewHolder.rg3.setClickable(false);
		}

		final RadioGroup rg = viewHolder.rgResult;

		viewHolder.rgResult
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup arg0, int arg1) {
						int pos = (Integer) arg0.getTag();
						if (arg0 == rg) {
							if (arg1 == R.id.topic_dianming_chuxi) {
								data.get(pos).put("value", "1");
							}
							if (arg1 == R.id.topic_dianming_weichuxi) {
								data.get(pos).put("value", "2");
							}
							if (arg1 == R.id.topic_dianming_qingjia) {
								data.get(pos).put("value", "0");
							}
						}
					}
				});
		return convertView;
	}

	static class ViewHolder {
		TextView tvName;
		RadioGroup rgResult;
		RadioButton rg1;
		RadioButton rg2;
		RadioButton rg3;
	}

	public void clear() {
		data.clear();
		notifyDataSetChanged();
	}

}
