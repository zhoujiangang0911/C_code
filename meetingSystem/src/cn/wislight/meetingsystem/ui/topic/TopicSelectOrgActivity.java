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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.bean.OrgHistoryBean;
import cn.wislight.meetingsystem.service.DbAdapter;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.LoadingDialog;
import cn.wislight.meetingsystem.util.MeetingSystemClient;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * @author Administrator 待我表决
 */

public class TopicSelectOrgActivity extends BaseActivity implements
		OnClickListener {
	private ListView org_listview;
	private ListView lvOrgHistory;
	private EditText etOrg;
	private ImageButton ibSearch;
	private List<Map<String, Object>> list;
	private List<Map<String, Object>> hisList;
	private LoadingDialog loadingdiag;

	@Override
	public void initView() {
		etOrg = (EditText) findViewById(R.id.etOrg);
		org_listview = (ListView) findViewById(R.id.topic_org_listview);
		lvOrgHistory = (ListView) findViewById(R.id.topic_orgkey_listview);
		ibSearch = (ImageButton) findViewById(R.id.ibt_search);
		ibSearch.setOnClickListener(this);

		Intent intent1 = getIntent();
		String orgname = intent1.getStringExtra(Constants.ORG_FNAME);

		if (orgname.contains("/")) {
			String[] tr = orgname.split("/");
			orgname = tr[tr.length - 1];
		}
		etOrg.setText(orgname);
		
		loadingdiag = new LoadingDialog(this);  
		loadingdiag.setCanceledOnTouchOutside(false); 
		
		loadHistoryList();
	}

	public void clickOrgEdit(View v) {
		loadHistoryList();
		lvOrgHistory.setVisibility(View.VISIBLE);
		org_listview.setVisibility(View.GONE);

	}

	private void loadHistoryList() {
		// TODO Auto-generated method stub
		TopicSelectOrgAdapter hisAdapter = new TopicSelectOrgAdapter(
				TopicSelectOrgActivity.this);
		hisList = new ArrayList<Map<String, Object>>();
		DbAdapter dbHandle = new DbAdapter(this);
		dbHandle.open();
		Map map;
		Cursor cur = dbHandle.getOrgHistory();
		if (null != cur && cur.moveToFirst()) {
			do {

				map = new HashMap<String, Object>();
				map.put("org_fullname", cur.getString(1));
				hisList.add(map);

			} while (cur.moveToNext());
		}
		dbHandle.close();

		hisAdapter.setData(hisList);
		lvOrgHistory.setAdapter(hisAdapter);
		lvOrgHistory.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				etOrg.setText((String) hisList.get(arg2).get("org_fullname"));
			}
		});
	}

	@Override
	public void setupView() {
		setContentView(R.layout.topic_select_org);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ibt_search:
			String name = etOrg.getText().toString();
			if (name.length() == 0) {
				Toast.makeText(TopicSelectOrgActivity.this,
						getString(R.string.topic_plsinput_emptytext),
						Toast.LENGTH_SHORT).show();
				return;
			}

			DbAdapter dbHandle = new DbAdapter(TopicSelectOrgActivity.this);
			dbHandle.open();
			boolean bLocalUseable = dbHandle.isOrgCacheUseable();
			dbHandle.close();
			if (bLocalUseable){
				getOrgFromlocal(name);
			}else{
				getOrg(name);
			}
			
			break;
		default:
			break;
		}

	}

	private void getOrgFromlocal(String name) {
		// TODO Auto-generated method stub
		DbAdapter dbHandle = new DbAdapter(TopicSelectOrgActivity.this);
		dbHandle.open();
		Cursor cur  = dbHandle.findOrg(name);
		Map<String, Object> map;
		list = new ArrayList<Map<String, Object>>();

		int count = 0;
		if (cur.moveToFirst()){
			do {
				map = new HashMap<String, Object>();
				map.put("org_fullname",
						cur.getString(1));
				map.put("org_no", cur.getString(2));
				list.add(map);
				count++;
			} while (cur.moveToNext());
		}
		
		if (count >= 1){
			OrgHistoryBean bean = new OrgHistoryBean();
			bean.setKeyword(name);
			dbHandle.insertOrgHistory(bean);
			dbHandle.close();
		}

		lvOrgHistory.setVisibility(View.GONE);
		org_listview.setVisibility(View.VISIBLE);
		TopicSelectOrgAdapter adapter = new TopicSelectOrgAdapter(
				TopicSelectOrgActivity.this);
		adapter.setData(list);
		org_listview.setAdapter(adapter);
		org_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				Intent data = new Intent();
				data.putExtra(Constants.ORG_FNAME,
						(String) list.get(arg2).get("org_fullname"));
				data.putExtra(Constants.ORG_NO, (String) list.get(arg2)
						.get("org_no"));
				setResult(Constants.OK, data);

				finish();
			}
		});
		
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return false;
	}

	private void getOrg(final String name) {
		// TODO Auto-generated method stub
		loadingdiag.setText(getString(R.string.loading));
		loadingdiag.show();
		
		String url = "MeetingManage/mobile/findOrg.action?name=" + name;
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				Toast.makeText(TopicSelectOrgActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
				loadingdiag.hide();
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
					Toast.makeText(TopicSelectOrgActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				// org_listview.setVisibility(View.VISIBLE);
				// loading.setVisibility(View.GONE);
				loadingdiag.hide();
				TopicSelectOrgAdapter adapter = new TopicSelectOrgAdapter(
						TopicSelectOrgActivity.this);
				list = new ArrayList<Map<String, Object>>();

				try {
					JSONArray jsonArray = new JSONObject(response)
							.getJSONArray("orgList");

					if (jsonArray.length() == 0) {
						Toast.makeText(TopicSelectOrgActivity.this,
								getString(R.string.topic_noorg),
								Toast.LENGTH_SHORT).show();
						return;
					}

					Map<String, Object> map;

					if (jsonArray.length() > 0) {
						DbAdapter dbHandle = new DbAdapter(
								TopicSelectOrgActivity.this);
						dbHandle.open();
						OrgHistoryBean bean = new OrgHistoryBean();
						bean.setKeyword(name);
						dbHandle.insertOrgHistory(bean);
						dbHandle.close();

						lvOrgHistory.setVisibility(View.GONE);
						org_listview.setVisibility(View.VISIBLE);
					}

					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArray.get(i);
						map = new HashMap<String, Object>();
						map.put("org_fullname",
								jsonObject.getString("fullName"));
						map.put("org_no", jsonObject.getString("no"));
						list.add(map);
					}

				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(TopicSelectOrgActivity.this,
							getString(R.string.error_dataabout),
							Toast.LENGTH_SHORT).show();
					Intent data = new Intent();
					setResult(Constants.ERROR_DATA, data);
					finish();

				}
				adapter.setData(list);
				org_listview.setAdapter(adapter);
				org_listview.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						Intent data = new Intent();
						data.putExtra(Constants.ORG_FNAME,
								(String) list.get(arg2).get("org_fullname"));
						data.putExtra(Constants.ORG_NO, (String) list.get(arg2)
								.get("org_no"));
						setResult(Constants.OK, data);

						finish();
					}
				});
			}
		});
	}

}

class TopicSelectOrgAdapter extends BaseAdapter {
	private Context mcontext;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> data;

	public TopicSelectOrgAdapter(Context context) {
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
			convertView = mInflater.inflate(R.layout.topic_select_org_item,
					null);

			holder = new ViewHolder();
			// 根据自定义的Item布局加载布局
			holder.name = (TextView) convertView.findViewById(R.id.name);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.name.setText((String) data.get(position).get("org_fullname"));

		return convertView;
	}

	public void setData(List<Map<String, Object>> list) {
		// TODO Auto-generated method stub
		data = list;
	}

	class ViewHolder {
		public TextView name;
	};
};