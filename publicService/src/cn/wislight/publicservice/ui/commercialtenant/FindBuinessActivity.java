package cn.wislight.publicservice.ui.commercialtenant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.string;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.wislight.publicservice.R;
import cn.wislight.publicservice.adapter.BusinessAffairListAdapter;
import cn.wislight.publicservice.adapter.ViewPagerAdapter;
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.util.Constants;
import cn.wislight.publicservice.util.PublicServiceClient;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 找生意界面
 * @author Administrator
 *
 */
@SuppressLint("ResourceAsColor")
public class FindBuinessActivity extends BaseActivity   {
	private ViewPager viewPager;
	private TextView tv_grabbill_btn;
	
	private TextView tv_qiangdan;
	private TextView tv_qiangdaode;
	private TextView tv_daifahuo;
	private TextView tv_chuliwan;	

	private List<View> titleList;
	private List<View> viewList;
	
	private ListView listviewQiangdan;
	private ListView listviewQiangdaode;
	private ListView listviewDaifahuo;
	private ListView listviewChuliwan;
	
	private List datalist_qiangdan;
	private List datalist_qiangdaode;
	private List datalist_daifahuo;
	private List datalist_chuliwan;
	
	private BusinessAffairListAdapter dataAdapter_qiangdan;
	private BusinessAffairListAdapter dataAdapter_qiangdaode;
	private BusinessAffairListAdapter dataAdapter_daifahuo;
	private BusinessAffairListAdapter dataAdapter_chuliwan;
	
	private Button btnRefreshList;
	
    private int itemLongClickSelected;
	private List datalistLongClickSelected;
	
	
	
	//private ListView listview;
	//private List datalist;
	//private BusinessAffairListAdapter dataAdapter;
	@Override
	public void setUpView() {		
		setContentView(R.layout.activity_findbuiness);
		btnRefreshList = (Button) findViewById(R.id.btn_refreshlist);
		btnRefreshList.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
			Toast.makeText(getApplicationContext(), "shuaxin", Toast.LENGTH_SHORT).show();
			fillData();
	    	fillNegotiationData();
			}
		});
		
		tv_qiangdan = (TextView)findViewById(R.id.textview_qiangdan);
		tv_qiangdaode = (TextView)findViewById(R.id.textview_qiangdaode);
		tv_daifahuo = (TextView)findViewById(R.id.textview_daifahuo);
		tv_chuliwan = (TextView)findViewById(R.id.textview_chuliwan);
		
		titleList = new ArrayList<View>();
		titleList.add(tv_qiangdan);
		titleList.add(tv_qiangdaode);
		titleList.add(tv_daifahuo);
		titleList.add(tv_chuliwan);
		
		for(int i=0;i<titleList.size();i++){
			TextView temp = (TextView) titleList.get(i);
			temp.setOnClickListener(new MyOnClickListener(i));
		}
		
		viewList=new ArrayList<View>();
		initViewList();
		viewPager=(ViewPager)findViewById(R.id.viewPager);
		viewPager.setAdapter(new ViewPagerAdapter(viewList));
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		
		viewPager.setCurrentItem(0); 
		titleList.get(0).setBackgroundColor(R.color.tab_selected_background);
		((TextView)titleList.get(0)).setTextColor(android.graphics.Color.WHITE);	
		
	}	
	



	private void initViewList() {     
    	datalist_qiangdan = new ArrayList< Map<String, String>>();
    	datalist_qiangdaode = new ArrayList< Map<String, String>>();
    	datalist_daifahuo = new ArrayList< Map<String, String>>();
    	datalist_chuliwan = new ArrayList< Map<String, String>>();    	
    	
    	fillData();
    	fillNegotiationData();
    	
    	listviewQiangdan = new ListView(this);
    	listviewQiangdaode = new ListView(this);
    	listviewDaifahuo = new ListView(this);
    	listviewChuliwan = new ListView(this);
    	
    	dataAdapter_qiangdan = new BusinessAffairListAdapter(this);
    	dataAdapter_qiangdaode = new BusinessAffairListAdapter(this);
    	dataAdapter_daifahuo = new BusinessAffairListAdapter(this);
    	dataAdapter_chuliwan = new BusinessAffairListAdapter(this);
    	
    	dataAdapter_qiangdan.setData(datalist_qiangdan);
    	dataAdapter_qiangdaode.setData(datalist_qiangdaode);
    	dataAdapter_daifahuo.setData(datalist_daifahuo);
    	dataAdapter_chuliwan.setData(datalist_chuliwan);
    	
    	listviewQiangdan.setAdapter(dataAdapter_qiangdan);
    	listviewQiangdaode.setAdapter(dataAdapter_qiangdaode);
    	listviewDaifahuo.setAdapter(dataAdapter_daifahuo);
    	listviewChuliwan.setAdapter(dataAdapter_chuliwan);
    	
    	listviewQiangdan.setOnItemClickListener(new OnItemClickListener(){	 
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                Intent data=new Intent(FindBuinessActivity.this, DetailCommercialtenantActivity.class);  
                data.putExtra(Constants.ID,   (String) ((Map) datalist_qiangdan.get(arg2)).get("id"));
                startActivity(data);                

                //gotoActivity(DetailCommercialtenantActivity.class, false);
            }
        });
    	
    	listviewQiangdaode.setOnItemClickListener(new OnItemClickListener(){	 
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                Intent data=new Intent(FindBuinessActivity.this, DetailNegotiateActivity.class);  
                data.putExtra(Constants.ID,   (String) ((Map) datalist_qiangdaode.get(arg2)).get("negotiationid"));
                startActivity(data);  
                //gotoActivity(NegotiatePriceDetailActivity.class, false);
            }
        });
    	listviewQiangdaode.setOnItemLongClickListener(new OnItemLongClickListener(){    		  
			@Override  
            public boolean onItemLongClick(AdapterView<?> parent, View view,  
                    int position, long id) {  
                itemLongClickSelected = position;                
                datalistLongClickSelected = datalist_qiangdaode;
                //Toast.makeText(context,"item["+position+":"+readItem(itemLongClickSelected).toString()+"]被长时间点击了.",Toast.LENGTH_SHORT).show();  
                return false;  
            }  
              
        });  
    	
    	listviewQiangdaode.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
                public void onCreateContextMenu(ContextMenu menu, View v,
                                ContextMenuInfo menuInfo) {
                	menu.setHeaderTitle((String)((Map) datalist_qiangdaode.get(itemLongClickSelected)).get("content"));
                        //menu.add(0,0,0,"添加");
                        menu.add(0,1,0,"删除");
                        //menu.add(0,2,0,"删除ALL");
                }
        });	
    	
    	
    	
    	listviewDaifahuo.setOnItemClickListener(new OnItemClickListener(){	 
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                Intent data=new Intent(FindBuinessActivity.this, DetailCommercialtenantActivity.class);  
                data.putExtra(Constants.ID,   (String) ((Map) datalist_daifahuo.get(arg2)).get("id"));
                startActivity(data);   
                //gotoActivity(DetailCommercialtenantActivity.class, false);
            }
        });
    	
    	
    	listviewChuliwan.setOnItemClickListener(new OnItemClickListener(){	 
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                Intent data=new Intent(FindBuinessActivity.this, DetailCommercialtenantActivity.class);  
                data.putExtra(Constants.ID,   (String) ((Map) datalist_chuliwan.get(arg2)).get("id"));
                startActivity(data); 
            }
        });   	
    	
    	listviewChuliwan.setOnItemLongClickListener(new OnItemLongClickListener(){    		  
			@Override  
            public boolean onItemLongClick(AdapterView<?> parent, View view,  
                    int position, long id) {  
                itemLongClickSelected = position;                
                datalistLongClickSelected = datalist_qiangdaode;
                //Toast.makeText(context,"item["+position+":"+readItem(itemLongClickSelected).toString()+"]被长时间点击了.",Toast.LENGTH_SHORT).show();  
                return false;  
            }  
              
        });  
    	
    	listviewChuliwan.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
                public void onCreateContextMenu(ContextMenu menu, View v,
                                ContextMenuInfo menuInfo) {
                		menu.setHeaderTitle((String)((Map) datalist_chuliwan.get(itemLongClickSelected)).get("content"));
                        //menu.add(0,0,0,"添加");
                        menu.add(0,1,0,"删除");
                        //menu.add(0,2,0,"删除ALL");
                }
        });	
    	
    	
    	
    	
    	
    	
        viewList.add(listviewQiangdan);  
        viewList.add(listviewQiangdaode);  
        viewList.add(listviewDaifahuo); 
        viewList.add(listviewChuliwan); 
	}
	
	@Override
	public void setListener() {
		// TODO Auto-generated method stub		
	}
	
	
	private class MyOnClickListener implements OnClickListener{  
        private int index=0;  
        public MyOnClickListener(int i){  
            index=i;  
        }  
        public void onClick(View v) {  
            viewPager.setCurrentItem(index);              
        }  
          
    }  


	private class MyOnPageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int arg0) {

			for(int i=0;i<titleList.size();i++){
				if(i == arg0){			
					((TextView)titleList.get(i)).setBackgroundColor(R.color.tab_selected_background);//.setBackgroundResource(R.drawable.page_indicator_focused);
					((TextView)titleList.get(i)).setTextColor(android.graphics.Color.WHITE);
					//((TextView)titleList.get(i)).setText("checked");
				}else{
					//((TextView)titleList.get(i)).setText("unchecked");
					((TextView)titleList.get(i)).setBackgroundColor(android.graphics.Color.WHITE);
					((TextView)titleList.get(i)).setTextColor(android.graphics.Color.BLACK);
					//titleList.get(i).setBackgroundResource(R.drawable.page_indicator);
                }
            }
		}
		
	};
	
	/*
	[
	    {
	        "content": "开班会",
	        "id": "3f9a1deaeb9c43b78c6ed985fb0d760a",
	        "price": "11",
	        "receiverid": "11",
	        "score": "11",
	        "senderid": "wangting",
	        "sendtime": "2014-09-29T18:57:56",
	        "state": "1",
	        "voicecontentid": null
	    }
	  ]
	*/
	private void fillData(){
		String url ="publicservice/businessaffair_list.htm?json=true";
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler(){
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				//loadingdiag.hide();
				Toast.makeText(FindBuinessActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);						
				try {							 
					 JSONArray jsonArray=new JSONArray(response);					  
				     Map<String, Object> map;
				    
				     datalist_qiangdan.clear();
				     //datalist_qiangdaode.clear();
				     datalist_daifahuo.clear();
				     datalist_chuliwan.clear();
					 for(int i=0;i<jsonArray.length();i++){
			               JSONObject jsonObject=(JSONObject)jsonArray.get(i);
//			               JSONObject senderjob = jsonObject.getJSONObject("sender");
			               JSONObject servicetypejob = jsonObject.getJSONObject("servicetype");
//			               Log.i("----", "---"+servicetypejob.getString("content"));
//			               Log.i("----", "---"+senderjob.getString("loginname"));
			               map = new HashMap<String, Object>();
			             //  map.put("loginname", senderjob.getString("loginname"));
			              map.put("servicetype", servicetypejob.getString("content"));
			               map.put("content", jsonObject.getString("content"));
			               map.put("id", jsonObject.getString("id"));
			               map.put("price", jsonObject.getString("price"));
			               map.put("score", jsonObject.getString("score"));
			               map.put("senderid", jsonObject.getString("senderid"));
			               map.put("sendtime", jsonObject.getString("sendtime")); 
			               map.put("state", jsonObject.getString("state"));
			               map.put("voicecontentid", jsonObject.getString("voicecontentid"));
			               

			               String state = jsonObject.getString("state");
			               if(state.equals("2")){
			            	   datalist_qiangdan.add(map);
			               } else if(state.equals("5")){
			            	   datalist_daifahuo.add(map);
			               }else if(state.equals("6")){
			            	   datalist_chuliwan.add(map);
			               }
					 }
					 
					 dataAdapter_qiangdan.notifyDataSetChanged();
					 dataAdapter_daifahuo.notifyDataSetChanged();
					 dataAdapter_chuliwan.notifyDataSetChanged();
				} catch (JSONException e) {
					e.printStackTrace();
					//loadingdiag.hide();
					Toast.makeText(FindBuinessActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
				}
			}
		});		
	}
	/*
[
    {
        "businessaffair": {
            "content": "王大妈买饭",
            "id": "d880984dee374dc09e21ae5d2f7bd843",
            "price": "10",
            "receiver": null,
            "receiverid": null,
            "score": "",
            "sender": {
                "address": null,
                "businesslisencepic": null,
                "businessserviceid": null,
                "companyname": null,
                "department": null,
                "id": "11f9de00943c47549cab9c1ec74041aa",
                "identity": null,
                "loginname": "wangting",
                "mapaddress": null,
                "mobile": null,
                "password": "123456",
                "phone": "18092488869",
                "postion": null,
                "usertype": "1",
                "voip": null
            },
            "senderid": null,
            "sendtime": "2014-10-28T15:44:25",
            "state": "1",
            "voicecontent": null,
            "voicecontentid": null
        },
        "businessaffairid": null,
        "callid": "",
        "comment": "",
        "confirmtype": "",
        "confirmvoiceid": "",
        "createtime": "2014-10-28T17:01:38",
        "endtime": null,
        "id": "0b76221e56ef4a21a45545101f3c0c96",
        "price": "115",
        "receiver": {
            "address": null,
            "businesslisencepic": null,
            "businessserviceid": null,
            "companyname": null,
            "department": null,
            "id": "11f9de00943c47549cab9c1ec74041aa",
            "identity": null,
            "loginname": "wangting",
            "mapaddress": null,
            "mobile": null,
            "password": "123456",
            "phone": "18092488869",
            "postion": null,
            "usertype": "1",
            "voip": null
        },
        "receiverid": null,
        "sender": null,
        "senderid": null,
        "state": "1"
    }
]
	*/
	public void fillNegotiationData() {
		String url = "publicservice/negotiation_list.htm?json=true";
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				// loadingdiag.hide();
				Toast.makeText(FindBuinessActivity.this,
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
						 JSONObject servicetypejob = businessaffair.getJSONObject("servicetype");
						Log.i("----", "---businessaffair"+businessaffair);
						String servicetypeStr =servicetypejob.getString("content");
						Map<String, Object> map;
						map = new HashMap<String, Object>();
						map.put("content", businessaffair.getString("content"));
						map.put("servicetype", servicetypeStr);
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
						
						map.put("negotiation_state", negotiation.getString("state"));
						map.put("negotiationid", negotiation.getString("id"));
						System.out.println("wangting: set negotiation_state="+negotiation.getString("state"));
						datalist_qiangdaode.add(map);
					}
					dataAdapter_qiangdaode.notifyDataSetChanged();

				} catch (JSONException e) {
					e.printStackTrace();
					// loadingdiag.hide();
					Toast.makeText(FindBuinessActivity.this,
							getString(R.string.error_network),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	public void clickQiangdan(Context mcontext, int position){
		Toast.makeText(mcontext, "click qiangdan", 100).show();
		String businessaffairId = (String) ((Map) datalist_qiangdan.get(position)).get("id");

		String url = "publicservice/negotiation_create.htm?json=true&negotiation.businessaffairid="+businessaffairId;
		System.out.println("wangting:"+url);
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				// loadingdiag.hide();
				Toast.makeText(FindBuinessActivity.this,
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
						Toast.makeText(FindBuinessActivity.this,
								"抢单成功", Toast.LENGTH_SHORT)
								.show();
						fillNegotiationData();
						viewPager.setCurrentItem(1);
						dataAdapter_qiangdaode.notifyDataSetChanged();
						
					}else {
						Toast.makeText(FindBuinessActivity.this,"抢单失败", Toast.LENGTH_SHORT)
								.show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
					// loadingdiag.hide();
					Toast.makeText(FindBuinessActivity.this,
							getString(R.string.error_network),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	
	}
	public void clickYijia(Context mcontext, int position){
		//Toast.makeText(mcontext, "click yijia", 100).show();
		String negotiationId = (String) ((Map) datalist_qiangdaode.get(position)).get("negotiationid");
		Intent intent = new Intent(this, NegotiatePriceActivity.class);
		intent.putExtra("negotiationId", negotiationId);
		startActivity(intent);
		//gotoActivity(NegotiatePriceActivity.class, false);
	}

	

	
	
	public boolean onContextItemSelected(MenuItem item) {		  
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                        .getMenuInfo();
        //MID = (int) info.id;// 这里的info.id对应的就是数据库中_id的值

        switch(item.getItemId()) {
        case 0:
                // 添加操作
                Toast.makeText(FindBuinessActivity.this,
                                "添加",
                                Toast.LENGTH_SHORT).show();
                break;
        case 1:
                // 删除操作
        		if(datalist_qiangdaode.equals(datalistLongClickSelected)){
        			deleteNegotiation();
        		}else if(datalist_chuliwan.equals(datalistLongClickSelected)){
        			deleteBusinessaffair();
        		}
                break;
        case 2:
                // 删除ALL操作
                break;
        default:
                break;
        }
        return super.onContextItemSelected(item);

	}	
	private void deleteNegotiation( ){
		Toast.makeText(FindBuinessActivity.this,
                "删除negotiation "+((Map) datalist_qiangdaode.get(itemLongClickSelected)).get("content"),
                Toast.LENGTH_SHORT).show();
		
	}
	
	private void deleteBusinessaffair(){		
		Toast.makeText(FindBuinessActivity.this,
                "删除 businessaffair "+((Map) datalist_chuliwan.get(itemLongClickSelected)).get("content"),
                Toast.LENGTH_SHORT).show();
		
	}



}
