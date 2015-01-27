package cn.wislight.meetingsystem.ui.conference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.loopj.android.http.AsyncHttpResponseHandler;

import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.adapter.ConfChangeListAdapter;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.IntentUtil;
import cn.wislight.meetingsystem.util.LoadingDialog;
import cn.wislight.meetingsystem.util.MeetingSystemClient;

/**
 * @author Administrator 会议变更
 */
public class ConChangeActivity extends BaseActivity {

	private TextView txTitle;
	private ListView listView;

	private List<Map<String, Object>> list;
	private LoadingDialog loadingdiag;
	@Override
	public void initView() {
		listView = (ListView) findViewById(R.id.conference_common_list_listview);
		
		loadingdiag = new LoadingDialog(this);  
		loadingdiag.setCanceledOnTouchOutside(false); 
		loadingdiag.setText(getString(R.string.loading));
		loadingdiag.show();
		// String url =
		// "MeetingManage/mobile/getMyVerifyMeetingList.action?type=1";
		String url = "MeetingManage/mobile/findMeetUpdate.action";
		System.out.println("wangting" + url);
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				System.out.println("wangting" + error.getMessage());
				loadingdiag.hide();
				Toast.makeText(ConChangeActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
				finish();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				loadingdiag.hide();
				String response = new String(body);
				
				if (response.contains("用户未登陆")){
					Toast.makeText(ConChangeActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				
				if (response == null || response.length() == 0) {
					Toast.makeText(ConChangeActivity.this, "无数据", 100);
					return;
				}
				System.out.println("wangting:" + response);
				// listView.setVisibility(View.VISIBLE);

				ConfChangeListAdapter adapter = new ConfChangeListAdapter(
						ConChangeActivity.this);
				list = new ArrayList<Map<String, Object>>();

				try {
					JSONArray jsonArray = new JSONArray(response);

					Map<String, Object> map;

					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArray.get(i);
						map = new HashMap<String, Object>();

						map.put("id", jsonObject.getString("id"));
						map.put("meetno", jsonObject.getString("meetno"));
						map.put("title", jsonObject.getString("title"));
						map.put("remark", jsonObject.getString("remark"));
						map.put("starttime", jsonObject.getString("starTime"));
						map.put("type", jsonObject.getString("typename"));
						map.put("creator", jsonObject.getString("username"));

						int state = jsonObject.getInt("meetstatu");
						String strstate = getStateStr(state);
						map.put("state", strstate);

						list.add(map);
					}
					System.out.println("wangting: list size=" + list.size());
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ConChangeActivity.this,
							getString(R.string.error_dataabout),
							Toast.LENGTH_SHORT).show();
					Intent data = new Intent();
					setResult(Constants.ERROR_DATA, data);
					finish();

				}
				adapter.setData(list);
				listView.setAdapter(adapter);
				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Intent data = new Intent();
						data.putExtra(Constants.ID, (String) list.get(arg2)
								.get("id"));
						data.putExtra(Constants.MEET_NO, (String)list.get(arg2).get("meetno"));
						IntentUtil.startActivity(ConChangeActivity.this, ConChangeOneActivity.class, data);
					}
				});

			}

		});
	}

	private String getStateStr(int state) {
		// TODO Auto-generated method stub
		String strstate = null;
		switch (state) {
		case -2:
			strstate = "打回";
			break;
		case -1:
			strstate = "未发起";
			break;
		case 0:
			strstate = "发起";
			break;
		case 1:
			strstate = "审核";
			break;
		case 2:
			strstate = "发布";
			break;
		case 3:
			strstate = "进行";
			break;
		case 4:
			strstate = "归档结束";
			break;
		default:
			break;
		}
		return strstate;
	}

	@Override
	public void setupView() {
		setContentView(R.layout.conference_common_list);
		txTitle = (TextView) findViewById(R.id.conference_common_list_title);
		txTitle.setText(getString(R.string.conference_change));
	}

}
