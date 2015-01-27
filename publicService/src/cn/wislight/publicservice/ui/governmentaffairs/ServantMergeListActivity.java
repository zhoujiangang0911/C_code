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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.loopj.android.http.AsyncHttpResponseHandler;

import cn.wislight.publicservice.R;
import cn.wislight.publicservice.adapter.ServantMergeListAdapter;
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.util.Constants;
import cn.wislight.publicservice.util.PublicServiceClient;


/**
 * 合并事务
 * @author Administrator
 *
 */
public class ServantMergeListActivity extends BaseActivity implements OnClickListener{
	private List datalist;
	private ServantMergeListAdapter adapter;
	private ListView listview;
	private Button btnMerge;
	
	@Override
	public void setUpView() {
		setContentView(R.layout.activity_servant_merge_list);
		
		btnMerge = (Button) findViewById(R.id.merge);
		
		
		listview = (ListView) this.findViewById(R.id.servant_merge_listview);
		listview.setDividerHeight(0);
		datalist = new ArrayList<Map<String, String>>();	

		fillData();
		adapter = new ServantMergeListAdapter(this);
		adapter.setData(datalist);
		listview.setAdapter(adapter);
    	listview.setOnItemClickListener(new OnItemClickListener(){	 
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                Intent intent=new Intent(ServantMergeListActivity.this, ServantDetailGovermentAffairActivity.class);  
                intent.putExtra(Constants.ID,   (String) ((Map) datalist.get(arg2)).get("id"));
                startActivity(intent);  
            }
        });
    	
	}

	@Override
	public void setListener() {
		btnMerge.setOnClickListener(this);
	}
	
	private void fillData(){
		String url ="publicservice/govermentaffair_findHandling.htm?json=true";
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler(){
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				//loadingdiag.hide();
				Toast.makeText(ServantMergeListActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				try {
					JSONObject result = new JSONObject(response);
					if(!"success".equals(result.getString("result"))){
						Toast.makeText(ServantMergeListActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
						return;
					}
					JSONArray jsonArray = result.getJSONArray("list");
					Map<String, Object> map;

					datalist.clear();

					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArray.get(i);
						map = new HashMap<String, Object>();
						String content =  jsonObject.getString("content");
			              
			            map.put("content",content.equals("null")?"":content );
						map.put("id", jsonObject.getString("id"));
						map.put("price", jsonObject.getString("price"));
						map.put("score", jsonObject.getString("score"));
						map.put("senderid", jsonObject.getString("senderid"));
						map.put("sendtime", jsonObject.getString("sendtime"));
						map.put("state", jsonObject.getString("state"));
						map.put("voicecontentid",
								jsonObject.getString("voicecontentid"));

						map.put("isChecked", "false");

						datalist.add(map);
					}

					adapter.notifyDataSetChanged();

				} catch (JSONException e) {
					e.printStackTrace();
					// loadingdiag.hide();
					Toast.makeText(ServantMergeListActivity.this,
							getString(R.string.error_network),
							Toast.LENGTH_SHORT).show();
				}
			}
		});		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.merge:
			mergeGovermentAffair();			
			break;

		default:
				break;
		}
	}
	
	public void mergeGovermentAffair(){
		String childIds = "";
		for(Object item:datalist){
			if("true".equals(((Map) item).get("isChecked"))){
				if(childIds.length()==0){
					childIds+=((Map) item).get("id");
				} else {
					childIds+= "#" + ((Map) item).get("id");
				}
			}
		}
		if(childIds.indexOf("#")<=0){
			Toast.makeText(ServantMergeListActivity.this,
					"少于两个无法合并", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		
		String url = "publicservice/govermentaffair_merge.htm?json=true&govermentaffair.id="+childIds;
		System.out.println("wangting:"+url);
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler() {		

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				// loadingdiag.hide();
				Toast.makeText(ServantMergeListActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				try {
					JSONObject jsonObject = new JSONObject(response);
					String result = jsonObject.getString("result");
					if (result.equals("success")){
						Toast.makeText(ServantMergeListActivity.this,"合并成功", Toast.LENGTH_SHORT)
						.show();	
						finish();
					}else {
						Toast.makeText(ServantMergeListActivity.this,"合并失败", Toast.LENGTH_SHORT)
								.show();
					}					

				} catch (JSONException e) {
					e.printStackTrace();
					// loadingdiag.hide();
					Toast.makeText(ServantMergeListActivity.this,
							getString(R.string.error_network),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	
	}
}
