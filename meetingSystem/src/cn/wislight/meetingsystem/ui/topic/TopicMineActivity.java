package cn.wislight.meetingsystem.ui.topic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.service.DbAdapter;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.IntentUtil;
import cn.wislight.meetingsystem.util.MeetingSystemClient;
import cn.wislight.meetingsystem.util.SliderUtil;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * @author Administrator my topic
 */
public class TopicMineActivity extends BaseActivity implements OnClickListener {
	private ImageView mIvUnderLine;
	private TextView mTvTopicOnline;
	private TextView mTvTopicLocal;
	private int distance;
	private ListView mLvTopicOnline;

	private LinearLayout mLlTopicOnline;
	private LinearLayout mLlTopicLocal;
	private List<Map<String, Object>> mOnlielist = null;
	private List<Map<String, Object>> mLocalList = null;
	private ListView mLvTopicLocal;
	private TopicLocalListAdapter mTopicLocalListAdapter;
	private TopicOnlineListAdapter mTopicOnlineListAdapter;

	@Override
	public void initView() {
		mIvUnderLine = (ImageView) findViewById(R.id.iv_slide_underline);
		mTvTopicLocal = (TextView) findViewById(R.id.tv_topic_local);
		mTvTopicOnline = (TextView) findViewById(R.id.tv_topic_online);
		mLvTopicOnline = (ListView) findViewById(R.id.lv_topic_online);
		mLlTopicOnline = (LinearLayout) findViewById(R.id.ll_topic_online);
		mLlTopicLocal = (LinearLayout) findViewById(R.id.ll_topic_local);
		mLvTopicLocal = (ListView) findViewById(R.id.lv_topic_local);

		// mAdapter = new WaitoListAdapter(this);
		// mHasListAdapter = new WaitoListAdapter(this);
		// mLvTopicOnline.setAdapter(mAdapter);
		// mHasListAdapter.setData(listHasSel);
		// mLvTopicLocal.setAdapter(mHasListAdapter);

		mLlTopicOnline.setVisibility(View.VISIBLE);
		mLlTopicLocal.setVisibility(View.GONE);

		initListener();

		loadOnlineList();
	}

	private void loadOnlineList() {
		String url = "MeetingManage/mobile/findMyTopic.action";
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				Toast.makeText(TopicMineActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
				Intent data = new Intent();
				setResult(Constants.ERROR_NETWORK, data);
				finish();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				// Toast.makeText(TopicSelectApplicatorActivity.this, new
				// String(response)+"wangting"+response.toString(), 100).show();
				if (response.contains("用户未登陆")){
					Toast.makeText(TopicMineActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				mTopicOnlineListAdapter = new TopicOnlineListAdapter(
						TopicMineActivity.this);
				mOnlielist = new ArrayList<Map<String, Object>>();

				try {
					JSONArray jsonArray = new JSONObject(response)
							.getJSONArray("tpList");

					Map<String, Object> map;

					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArray.get(i);
						map = new HashMap<String, Object>();
						map.put("starttime", jsonObject.getString("starttime"));
						map.put("endtime", jsonObject.getString("endtime"));
						map.put("keywords", jsonObject.getString("keywords"));
						map.put("summary", jsonObject.getString("summary"));
						map.put("id", jsonObject.getString("id"));
						map.put("state", jsonObject.getString("check_state"));

						mOnlielist.add(map);
					}

				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(TopicMineActivity.this,
							getString(R.string.error_dataabout),
							Toast.LENGTH_SHORT).show();
					Intent data = new Intent();
					setResult(Constants.ERROR_DATA, data);
					finish();

				}
				mTopicOnlineListAdapter.setData(mOnlielist);
				mLvTopicOnline.setAdapter(mTopicOnlineListAdapter);
				mLvTopicOnline
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								Intent data = new Intent();
								data.putExtra(Constants.ID, (String) mOnlielist
										.get(arg2).get("id"));
								IntentUtil.startActivity(
										TopicMineActivity.this,
										TopicMyTopicDetailActivity.class, data);
							}
						});
			}

		});

	}

	private void loadLocalList() {

		DbAdapter dbhandle = new DbAdapter(this);
		dbhandle.open();
		mTopicLocalListAdapter = new TopicLocalListAdapter(
				TopicMineActivity.this);
		mLocalList = new ArrayList<Map<String, Object>>();
		Cursor cursor = dbhandle.getTopicDraftUnUploaded();
		Map<String, Object> map;

		if (cursor.moveToFirst()) {
			do {

				map = new HashMap<String, Object>();
				map.put("starttime", cursor.getString(5));
				map.put("endtime", cursor.getString(4));
				map.put("keywords", cursor.getString(2));
				map.put("summary", cursor.getString(3));
				map.put("id", cursor.getString(0));

				mLocalList.add(map);

			} while (cursor.moveToNext());
		}
		dbhandle.close();
		mTopicLocalListAdapter.setData(mLocalList);
		mLvTopicLocal.setAdapter(mTopicLocalListAdapter);
		mLvTopicLocal.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent data = new Intent();
				data.putExtra(Constants.ID,
						(String) mLocalList.get(arg2).get("id"));
				IntentUtil.startActivity(TopicMineActivity.this,
						TopicDraftDetailActivity.class, data);
			}
		});

	}

	private void initListener() {
		mTvTopicLocal.setOnClickListener(this);
		mTvTopicOnline.setOnClickListener(this);
		distance = SliderUtil.getDistance(this);

	}

	@Override
	public void setupView() {
		setContentView(R.layout.topic_mine_main);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_topic_online:
			mLlTopicOnline.setVisibility(View.VISIBLE);
			mLlTopicLocal.setVisibility(View.GONE);
			SliderUtil.moveSlider(mIvUnderLine, distance, 0);
			// mAdapter.clear();

			break;
		case R.id.tv_topic_local:
			mLlTopicOnline.setVisibility(View.GONE);
			mLlTopicLocal.setVisibility(View.VISIBLE);
			SliderUtil.moveSlider(mIvUnderLine, 0, distance);
			loadLocalList();
			break;
		case R.id.ibtn_submit:

			break;
		default:
			break;
		}

	}

};

class TopicOnlineListAdapter extends BaseAdapter {
	private Context mcontext;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> data;

	public TopicOnlineListAdapter(Context context) {
		this.mcontext = context;
		mInflater = LayoutInflater.from(context);
	}

	public void setData(List<Map<String, Object>> li) {
		// TODO Auto-generated method stub
		data = li;
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
		ViewHolderTopic holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.topic_list_common_item1,
					null);

			holder = new ViewHolderTopic();
			// 根据自定义的Item布局加载布局
			holder.summary = (TextView) convertView
					.findViewById(R.id.tv_summary);
			holder.keywords = (TextView) convertView
					.findViewById(R.id.tv_keywords);
			holder.time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.state = (TextView) convertView.findViewById(R.id.tv_state);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolderTopic) convertView.getTag();
		}

		holder.summary.setText((String) data.get(position).get("summary"));
		holder.keywords.setText((String) data.get(position).get("keywords"));
		holder.time.setText((String) data.get(position).get("starttime")
				+ " -- " + (String) data.get(position).get("endtime"));

		int checkState = Integer.parseInt((String) data.get(position).get(
				"state"));
		int[] states = { R.string.topic_status_checkreject,
				R.string.topic_status_tocheck, R.string.topic_status_checkpass };

		holder.state.setText(states[checkState]);
		return convertView;
	}

};

class TopicLocalListAdapter extends BaseAdapter {
	private Context mcontext;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> data;

	public TopicLocalListAdapter(Context context) {
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
		ViewHolderTopic holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.topic_list_common_item,
					null);

			holder = new ViewHolderTopic();
			// 根据自定义的Item布局加载布局
			holder.summary = (TextView) convertView
					.findViewById(R.id.tv_summary);
			holder.keywords = (TextView) convertView
					.findViewById(R.id.tv_keywords);
			holder.time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.state = (TextView) convertView.findViewById(R.id.tv_state);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolderTopic) convertView.getTag();
		}

		holder.summary.setText((String) data.get(position).get("summary"));
		holder.keywords.setText((String) data.get(position).get("keywords"));
		holder.time.setText((String) data.get(position).get("starttime")
				+ " -- " + (String) data.get(position).get("endtime"));

		return convertView;
	}

	public void setData(List<Map<String, Object>> list) {
		// TODO Auto-generated method stub
		data = list;
	}

};

class ViewHolderTopic {
	public TextView summary;
	public TextView state;
	public TextView keywords;
	public TextView time;
};
