package cn.wislight.publicservice.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import cn.wislight.publicservice.R;
import cn.wislight.publicservice.adapter.RegisterServiceTypeListAdapter;
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.util.PublicServiceClient;
import cn.wislight.publicservice.util.ServiceTypeElement;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class RegisterSelectServiceTypeActivity extends BaseActivity implements
		OnClickListener {
	private List datalist;
	private RegisterServiceTypeListAdapter adapter;
	private ListView listview;
	StringBuilder namebuilder = new StringBuilder();
	StringBuilder idbuilder = new StringBuilder();
	private Button btnOK;
	private ArrayList<ServiceTypeElement> listHasSel = new ArrayList<ServiceTypeElement>();

	@Override
	public void setUpView() {
		setContentView(R.layout.activity_select_servicetype_regiest);

		listview = (ListView) this.findViewById(R.id.servicetype_listview);
		btnOK = (Button) findViewById(R.id.tv_ok_servicetype);
		adapter = new RegisterServiceTypeListAdapter(this);
		fillData();
		listview.setAdapter(adapter);
		btnOK.setOnClickListener(this);

	}

	@Override
	public void setListener() {
	}

	public void fillData() {
		String url = "publicservice/servicetype_list.htm?json=true";
		System.out.println("wangting:" + url);
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				// loadingdiag.hide();
				Toast.makeText(RegisterSelectServiceTypeActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				System.out.println("wangting: servicetype=" + response);
				try {
					JSONObject jsonResponse = new JSONObject(response);
					String result = jsonResponse.getString("result");
					if (result.equals("success")) {
						JSONArray jsonArray = jsonResponse.getJSONArray("list");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObject = (JSONObject) jsonArray
									.get(i);
							ServiceTypeElement ele = new ServiceTypeElement();
							ele.setTypename(jsonObject.getString("content"));
							ele.setTypeid(jsonObject.getString("id"));
							listHasSel.add(ele);
						}
						adapter.setData(listHasSel);
						adapter.notifyDataSetChanged();

					} else {
						Toast.makeText(RegisterSelectServiceTypeActivity.this,
								"初始化失败", Toast.LENGTH_SHORT).show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
					// loadingdiag.hide();
					Toast.makeText(RegisterSelectServiceTypeActivity.this,
							getString(R.string.error_network),
							Toast.LENGTH_SHORT).show();
				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_ok_servicetype:
			Toast.makeText(getApplicationContext(), "你选择的是", Toast.LENGTH_SHORT)
					.show();
			Intent intent = new Intent();
			for (ServiceTypeElement ele : listHasSel) {
				if (ele.isCheck) {
					namebuilder.append(ele.getTypename());
					namebuilder.append("、");
					idbuilder.append(ele.getTypeid());
					idbuilder.append("#");
				}

			}
			intent.putExtra("servicetypename", namebuilder + "");
			intent.putExtra("servicetypeid", idbuilder + "");
			this.setResult(Activity.RESULT_OK, intent);
			finish();
			break;
		default:
			break;
		}
	}

}
