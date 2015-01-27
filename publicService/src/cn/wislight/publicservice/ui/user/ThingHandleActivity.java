package cn.wislight.publicservice.ui.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.loopj.android.http.AsyncHttpResponseHandler;

import cn.wislight.publicservice.R;
import cn.wislight.publicservice.adapter.BusinessAffairListAdapter;
import cn.wislight.publicservice.adapter.NegotiationListAdapter;
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.ui.commercialtenant.DetailNegotiateActivity;
import cn.wislight.publicservice.ui.commercialtenant.FindBuinessActivity;
import cn.wislight.publicservice.util.Constants;
import cn.wislight.publicservice.util.PublicServiceClient;


/**
 * 事物处理中
 * @author Administrator
 *
 */
public class ThingHandleActivity extends BaseActivity {
	private List datalist_qiangdaode;
	private NegotiationListAdapter dataAdapter_qiangdaode;
	private ListView listviewQiangdaode;


	@Override
	public void setUpView() {
		setContentView(R.layout.activity_thinghandle);
		
		listviewQiangdaode = (ListView) this.findViewById(R.id.negotiation_listview);
		datalist_qiangdaode = new ArrayList();		
		dataAdapter_qiangdaode = new NegotiationListAdapter(this);
		dataAdapter_qiangdaode.setData(datalist_qiangdaode);
		listviewQiangdaode.setAdapter(dataAdapter_qiangdaode);
    	listviewQiangdaode.setOnItemClickListener(new OnItemClickListener(){	 
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                Intent data=new Intent(ThingHandleActivity.this, DetailNegotiateForSenderActivity.class);  
                data.putExtra(Constants.ID,   (String) ((Map) datalist_qiangdaode.get(arg2)).get("negotiationid"));
                startActivity(data);  
                //gotoActivity(NegotiatePriceDetailActivity.class, false);
            }
        });
    	fillNegotiationData();
	}

	@Override
	public void setListener() {

	}

	
	public void fillNegotiationData() {
		String url = "publicservice/negotiation_list_sendbyme.htm?json=true";
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				// loadingdiag.hide();
				Toast.makeText(ThingHandleActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				try {
					JSONArray jsonArray = new JSONArray(response);
					datalist_qiangdaode.clear();
					for (int i = 0; i < jsonArray.length(); i++) {
						
						JSONObject negotiation = (JSONObject) jsonArray.get(i);
						JSONObject businessaffair = negotiation
								.getJSONObject("businessaffair");
						
						if(!((businessaffair.getString("state")).equals("5"))){
							
						Map<String, Object> map;
						map = new HashMap<String, Object>();
						map.put("content", businessaffair.getString("content"));
						map.put("id", businessaffair.getString("id"));

						map.put("price", businessaffair.getString("price"));
						map.put("score", businessaffair.getString("score"));
						map.put("senderid",
								businessaffair.getString("senderid"));
						map.put("sendtime",
								businessaffair.getString("sendtime"));
						map.put("state", businessaffair.getString("state"));
						map.put("voicecontentid",
								businessaffair.getString("voicecontentid"));
						map.put("sendername", negotiation.getJSONObject("sender").getString("loginname"));
						map.put("receivername", negotiation.getJSONObject("receiver").getString("loginname"));
						map.put("negotiation_state", negotiation.getString("state"));
						map.put("negotiationid", negotiation.getString("id"));
						map.put("negotiationprice", negotiation.getString("price"));
						System.out.println("wangting: set negotiation_state="+negotiation.getString("state"));
						datalist_qiangdaode.add(map);
						
					}
					dataAdapter_qiangdaode.notifyDataSetChanged();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					// loadingdiag.hide();
//					Toast.makeText(ThingHandleActivity.this,
//							getString(R.string.error_network),
//							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
}
