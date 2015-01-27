package cn.wislight.meetingsystem.ui.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpResponseHandler;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.adapter.ConfCommonListAdapter;
import cn.wislight.meetingsystem.adapter.TodayScheduleAdapter;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.ui.conference.ConJoinActivity;
import cn.wislight.meetingsystem.ui.conference.ConfJoinDetailActivity;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.IntentUtil;
import cn.wislight.meetingsystem.util.MeetingSystemClient;


/**
 * @author Administrator
 *  今日日程
 */
public class TodayScheduleActivity extends BaseActivity{
	private ListView listView;


	private List<Map<String, Object>> list;

	//private ListView mLv;
	private TodayScheduleAdapter mAdapter;
	//private ArrayList<String> list=new ArrayList<String>();
	@Override
	public void initView() {
		//mLv=(ListView) findViewById(R.id.lv_today_schedule);
		//mAdapter=new TodayScheduleAdapter(this);
		//mLv.setAdapter(mAdapter);
		//testData() ;
		

		listView=(ListView) findViewById(R.id.lv_today_schedule);

		//String url = "MeetingManage/mobile/getMyVerifyMeetingList.action?type=0";
		String url = "MeetingManage/mobile/getAttendMeetingList.action";
		
		System.out.println("wangting:"+url);
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				String response = error.getMessage();
				System.out.println("wangting, success response="+response);
				Toast.makeText(TodayScheduleActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                Intent data=new Intent();  
                setResult(Constants.ERROR_NETWORK, data);  
                finish();  
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				System.out.println("wangting, success response="+response);
				
				if (response.contains("用户未登陆")){
					Toast.makeText(TodayScheduleActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				// Toast.makeText(TopicSelectApplicatorActivity.this, new String(response)+"wangting"+response.toString(), 100).show();
				listView.setVisibility(View.VISIBLE);		
				//WaitMyCheckListAdapter adapter = new WaitMyCheckListAdapter(ConCheckActivity.this);
				//ConfCommonListAdapter adapter = new ConfCommonListAdapter(TodayScheduleActivity.this);
				TodayScheduleAdapter adapter = new TodayScheduleAdapter(TodayScheduleActivity.this);

				list = new ArrayList<Map<String, Object>>(); 
				
				try {
					 //JSONArray jsonArray=new JSONObject(response).getJSONArray("updateMeetList");	
					 JSONArray jsonArray=new JSONArray(response);
				     Map<String, Object> map;				        
					 for(int i=0;i<jsonArray.length();i++){
			               JSONObject jsonObject=(JSONObject)jsonArray.get(i);
			               map = new HashMap<String, Object>();
			               map.put("id", jsonObject.getString("meetno"));
			               map.put("title",jsonObject.getString("title"));
			               map.put("remark", jsonObject.getString("remark"));
			               
			               String temp = jsonObject.getString("starTime");
			               temp = temp.replace(".0", "");
			               map.put("starttime", temp);
			               temp = jsonObject.getString("endTime");
			               temp = temp.replace(".0", "");
			               map.put("endtime", temp);
			               map.put("address", jsonObject.getString("address"));		
			               JSONObject startDate = jsonObject.getJSONObject("startdate");
			               if (null != startDate){
			            	   map.put("monthdate", startDate.getInt("month") + 1 +"月"+startDate.getString("date")+"日");
			               }
			               map.put("typename", jsonObject.getString("typename"));
			               
			               list.add(map);
					 }
					System.out.println("wangting: list size="+list.size());
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(TodayScheduleActivity.this, getString(R.string.error_dataabout), Toast.LENGTH_SHORT).show();
					Intent data=new Intent();  
	                setResult(Constants.ERROR_DATA, data);  
	                finish();  				
				}
				adapter.setData(list);
				listView.setAdapter(adapter);
				listView.setOnItemClickListener(new OnItemClickListener(){					 
		            @Override
		            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		                    long arg3) {
		                Intent data=new Intent();  
		                data.putExtra(Constants.ID,   (String)list.get(arg2).get("id"));  
		                IntentUtil.startActivity(TodayScheduleActivity.this, TodayScheduleDetailActivity.class,  data);
		            }
		        });		
				
				
			}
		});		
	
	}
/*
	private void testData() {
		for (int i = 0; i < 6; i++) {
			list.add(""+i);
		}
		mAdapter.addDatas(list);
		mAdapter.notifyDataSetChanged();
	}
*/
	@Override
	public void setupView() {
		setContentView(R.layout.today_schedule_main);

	}

}
