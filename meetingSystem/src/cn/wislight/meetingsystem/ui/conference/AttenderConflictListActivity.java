package cn.wislight.meetingsystem.ui.conference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.util.Constants;

/**
 * @author Administrator 议题库
 */
public class AttenderConflictListActivity extends BaseActivity {
	private ListView listView;
	private List<Map<String, Object>> list;

	@Override
	public void initView() {
		listView = (ListView) findViewById(R.id.lv_conflict_list);
		String responce = getIntent().getStringExtra("response");

		AttenderConflictListAdapter adapter = new AttenderConflictListAdapter(
				AttenderConflictListActivity.this);
		list = new ArrayList<Map<String, Object>>();

		try {
			JSONArray jsonArray = new JSONObject(responce)
					.getJSONArray("attenderConflictList");
			Map<String, Object> map;
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				map = new HashMap<String, Object>();
				map.put("attendername", jsonObject.getString("attendername"));
				map.put("meettitle", jsonObject.getString("meettitle"));
				map.put("topictitle", jsonObject.getString("topictitle"));
				map.put("createorg", jsonObject.getString("createorg"));
				map.put("createusername",
						jsonObject.getString("createusername"));
				map.put("ttStart", jsonObject.getString("tt_Start"));
				map.put("ttEnd", jsonObject.getString("tt_End"));

				list.add(map);
			}

		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(AttenderConflictListActivity.this,
					getString(R.string.error_dataabout), Toast.LENGTH_SHORT)
					.show();
			Intent data = new Intent();
			setResult(Constants.ERROR_DATA, data);
			finish();

		}
		adapter.setData(list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

			}
		});

	}

	@Override
	public void setupView() {
		setContentView(R.layout.attender_confilict);
	}

}

class AttenderConflictListAdapter extends BaseAdapter {
	private Context mcontext;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> data;

	public AttenderConflictListAdapter(Context context) {
		this.mcontext = context;
		mInflater = LayoutInflater.from(context);
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
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.attender_conflict_list_item, null);

			holder = new ViewHolder();
			// 根据自定义的Item布局加载布局
			holder.tvAttender = (TextView) convertView
					.findViewById(R.id.tv_attender);
			holder.tvMeetTitle = (TextView) convertView
					.findViewById(R.id.tv_meet_title);
			holder.tvTopicTitle = (TextView) convertView
					.findViewById(R.id.tv_topic_title);
			holder.tvOrg = (TextView) convertView.findViewById(R.id.tv_org);
			holder.tvCreator = (TextView) convertView
					.findViewById(R.id.tv_creator);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tvAttender.setText((String) data.get(position).get("attendername"));
		holder.tvMeetTitle.setText((String) data.get(position).get("meettitle"));
		holder.tvTopicTitle
				.setText((String) data.get(position).get("topictitle"));
		holder.tvOrg.setText((String) data.get(position).get("createorg"));
		holder.tvCreator.setText((String) data.get(position).get("createusername"));
		holder.tvTime.setText((String) data.get(position).get("ttStart")
				+ " -- " + (String) data.get(position).get("ttEnd"));

		return convertView;
	}

	public void setData(List<Map<String, Object>> list) {
		// TODO Auto-generated method stub
		data = list;
	}

	class ViewHolder {
		public TextView tvAttender;
		public TextView tvMeetTitle;
		public TextView tvTopicTitle;
		public TextView tvOrg;
		public TextView tvCreator;
		public TextView tvTime;
	};
}
