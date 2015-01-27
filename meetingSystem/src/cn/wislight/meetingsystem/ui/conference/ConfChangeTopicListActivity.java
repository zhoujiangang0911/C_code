package cn.wislight.meetingsystem.ui.conference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpResponseHandler;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.ui.topic.TopicChangeOneActivity;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.IntentUtil;
import cn.wislight.meetingsystem.util.MeetingSystemClient;

/**
 * @author Administrator 会议发起
 */
public class ConfChangeTopicListActivity extends BaseActivity implements
		topicInterface {
	private ListView listView;
	// private EditText etKeyword;
	// private RelativeLayout flSearchBar;
	private List<Map<String, Object>> list;
	private String meetNo;
	private String meetId;

	@Override
	public void initView() {
		listView = (ListView) findViewById(R.id.conference_topic_list_listview);
		// etKeyword = (EditText) findViewById(R.id.et_keywords);
		// flSearchBar = (RelativeLayout) findViewById(R.id.search_bar);
		// flSearchBar.setVisibility(View.GONE);
		meetNo = this.getIntent().getStringExtra(Constants.MEET_NO);

		getTopicList();
	}

	public void getTopicList() {
		// TODO Auto-generated method stub
		String url = "MeetingManage/mobile/getMeetingProcList.action?meetingId="
				+ meetNo;

		System.out.println("wangting:" + url);
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				Toast.makeText(ConfChangeTopicListActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
				Intent data = new Intent();
				setResult(Constants.ERROR_NETWORK, data);
				finish();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				if (response.contains("用户未登陆")){
					Toast.makeText(ConfChangeTopicListActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				System.out.println("wangting" + response);
				listView.setVisibility(View.VISIBLE);
				TopicChangeListAdapter adapter = new TopicChangeListAdapter(
						ConfChangeTopicListActivity.this);
				adapter.setTopicHandle(ConfChangeTopicListActivity.this);
				list = new ArrayList<Map<String, Object>>();
				try {
					JSONArray jsonArray = new JSONArray(response);
					Map<String, Object> map;
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArray.get(i);
						map = new HashMap<String, Object>();
						map.put("starttime", jsonObject.getString("staTime"));
						map.put("endtime", jsonObject.getString("endTime"));
						map.put("keywords", jsonObject.getString("title"));
						map.put("summary", jsonObject.getString("remark"));
						map.put("id", jsonObject.getString("topicno"));
						map.put("topicid", jsonObject.getString("id"));
						map.put("state", jsonObject.getString("statu"));
						list.add(map);
					}

				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ConfChangeTopicListActivity.this,
							getString(R.string.error_dataabout),
							Toast.LENGTH_SHORT).show();
					Intent data = new Intent();
					setResult(Constants.ERROR_DATA, data);
					finish();

				}
				adapter.setData(list);
				listView.setAdapter(adapter);

			}
		});
	}

	@Override
	public void setupView() {
		setContentView(R.layout.conference_topic_list);

	}

	protected void onPause() {
		super.onPause();

	}

	public void clickPre(View view) {
		IntentUtil.startActivity(this, ConBeginTwoActivity.class);
		finish();
	}

	@Override
	public void deleteTopic(String topicNo, String topicId) {
		String url = "MeetingManage/mobile/deleteMeetTopic.action?TopicId="
				+ topicId + "&TopicNo=" + topicNo;

		System.out.println("wangting:" + url);
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				Toast.makeText(ConfChangeTopicListActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
				Intent data = new Intent();
				setResult(Constants.ERROR_NETWORK, data);
				finish();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				
				if (response.contains("用户未登陆")){
					Toast.makeText(ConfChangeTopicListActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				if (response.contains("0")){
					Toast.makeText(ConfChangeTopicListActivity.this,
							getString(R.string.conf_delete_topic_success), Toast.LENGTH_SHORT)
							.show();
					getTopicList();
					
				}else{
					Toast.makeText(ConfChangeTopicListActivity.this,
							getString(R.string.conf_delete_topic_fail), Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

	}

	@Override
	public void showTopic(String topicNo, String topicId) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra(Constants.TOPIC_NO, topicNo);
		intent.putExtra(Constants.TOPIC_ID, topicId);
		IntentUtil.startActivity(this, TopicChangeOneActivity.class, intent);
	}
}

interface topicInterface {
	public void deleteTopic(String topicNo, String topicId);
	public void showTopic(String topicNo, String topicId);
}

class TopicChangeListAdapter extends BaseAdapter {
	private Context mcontext;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> data;
	private topicInterface interf;

	public TopicChangeListAdapter(Context context) {
		this.mcontext = context;
		mInflater = LayoutInflater.from(context);
	}

	public void setTopicHandle(topicInterface interf) {
		// TODO Auto-generated method stub
		this.interf = interf;
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
					R.layout.conf_change_topic_list_item, null);

			holder = new ViewHolder();
			// 根据自定义的Item布局加载布局
			holder.summary = (TextView) convertView
					.findViewById(R.id.tv_summary);
			holder.keywords = (TextView) convertView
					.findViewById(R.id.tv_keywords);
			holder.time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.state = (TextView) convertView.findViewById(R.id.tv_state);
			holder.btnDelete = (Button) convertView
					.findViewById(R.id.btn_delete);
			holder.btnDetail = (Button) convertView
					.findViewById(R.id.btn_detail);
			holder.btnDelete.setTag(position);
			holder.btnDetail.setTag(position);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.summary.setText((String) data.get(position).get("summary"));
		holder.keywords.setText((String) data.get(position).get("keywords"));
		holder.time.setText((String) data.get(position).get("starttime")
				+ " -- " + (String) data.get(position).get("endtime"));
		int state = Integer.parseInt((String) data.get(position).get("state"));
		String states[] = { "未开始", "进行中", "已结束" };
		holder.state.setText(states[state]);
		if (state == 0) {
			holder.btnDelete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					final int k = (Integer) v.getTag();
					String topicNo = (String) data.get(k).get("id");
					String topicId = (String) data.get(k).get("topicid");
					interf.deleteTopic(topicNo, topicId);
				}
			});

			holder.btnDetail.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					final int k = (Integer) v.getTag();
					String topicNo = (String) data.get(k).get("id");
					String topicId = (String) data.get(k).get("topicid");
					interf.showTopic(topicNo, topicId);
				}
			});
		} else {
			holder.btnDelete.setVisibility(View.GONE);
			holder.btnDetail.setVisibility(View.GONE);
		}
		return convertView;
	}

	public void setData(List<Map<String, Object>> list) {
		// TODO Auto-generated method stub
		data = list;
	}

	class ViewHolder {
		public TextView summary;
		public TextView keywords;
		public TextView time;
		public TextView state;
		public Button btnDelete;
		public Button btnDetail;
	};
};
