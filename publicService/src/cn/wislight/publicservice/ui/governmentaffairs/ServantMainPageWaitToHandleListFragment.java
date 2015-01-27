package cn.wislight.publicservice.ui.governmentaffairs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.loopj.android.http.AsyncHttpResponseHandler;

import cn.wislight.publicservice.R;
import cn.wislight.publicservice.adapter.BusinessAffairListAdapter;
import cn.wislight.publicservice.adapter.IMMessageHistoryListAdapter;
import cn.wislight.publicservice.adapter.IMMessageListAdapter;
import cn.wislight.publicservice.adapter.NegotiationListAdapter;
import cn.wislight.publicservice.adapter.ServantHandingListAdapter;
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.entity.GovermentAffair;
import cn.wislight.publicservice.ui.commercialtenant.DetailNegotiateActivity;
import cn.wislight.publicservice.ui.commercialtenant.FindBuinessActivity;
import cn.wislight.publicservice.util.Constants;
import cn.wislight.publicservice.util.PublicServiceClient;

/**
 * 事物处理中
 * 
 * @author Administrator
 * 
 */
public class ServantMainPageWaitToHandleListFragment extends Fragment implements
		OnClickListener {
	private List datalist;
	private ServantHandingListAdapter adapter;
	private ListView listview;
	private String negotiationId;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View inflateGoverment = inflater.inflate(
				R.layout.activity_main_page_waitto_handle_list, container,
				false);
		setViews(inflateGoverment);

		return inflateGoverment;
	}

	public View setViews(View inflateGoverment) {

		listview = (ListView) inflateGoverment
				.findViewById(R.id.main_page_waitto_handle_listview);
		listview.setDividerHeight(0);
		datalist = new ArrayList<Map<String, String>>();

		fillData();
		adapter = new ServantHandingListAdapter(getActivity());
		adapter.setData(datalist);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getActivity(),
						ServantHandlingDetailActivity.class);
				intent.putExtra(Constants.ID,
						(String) ((Map) datalist.get(arg2)).get("id"));
				startActivity(intent);
			}
		});
		return inflateGoverment;

	}

	private void fillData() {
		String url = "publicservice/govermentaffair_findWaitToAccept.htm?json=true";
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				// loadingdiag.hide();
				Toast.makeText(getActivity(),
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				try {
					JSONObject result = new JSONObject(response);
					if (!"success".equals(result.getString("result"))) {
						Toast.makeText(getActivity(),
								getString(R.string.error_network),
								Toast.LENGTH_SHORT).show();
						return;
					}
					JSONArray jsonArray = result.getJSONArray("list");
					Map<String, Object> map;
					datalist.clear();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArray.get(i);
						map = new HashMap<String, Object>();
						String content = jsonObject.getString("content");

						map.put("content", content.equals("null") ? ""
								: content);
						map.put("id", jsonObject.getString("id"));
						map.put("price", jsonObject.getString("price"));
						map.put("score", jsonObject.getString("score"));
						map.put("senderid", jsonObject.getString("senderid"));
						map.put("sendtime", jsonObject.getString("sendtime"));
						map.put("state", jsonObject.getString("state"));
						map.put("voicecontentid",
								jsonObject.getString("voicecontentid"));
							
						
						datalist.add(map);
						
					}
					adapter.notifyDataSetChanged();
				} catch (JSONException e) {
					e.printStackTrace();
					// loadingdiag.hide();
					Toast.makeText(getActivity(),
							getString(R.string.error_network),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}
}
