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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.adapter.ConfSelectTopicListAdapter;
import cn.wislight.meetingsystem.adapter.ViewPagerAdapter;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.domain.Topic;
import cn.wislight.meetingsystem.service.DictionaryUpdater;
import cn.wislight.meetingsystem.ui.topic.TopicEditOneActivity;
import cn.wislight.meetingsystem.ui.topic.TopicMeetAddOneActivity;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.Element;
import cn.wislight.meetingsystem.util.IntentUtil;
import cn.wislight.meetingsystem.util.LoadingDialog;
import cn.wislight.meetingsystem.util.MeetingSystemClient;
import cn.wislight.meetingsystem.util.Variables;
import cn.wislight.meetingsystem.widget.UnderlinePageIndicator;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

/**
 * @author Administrator
 *	会议发起
 */
public class ConBeginThreeActivity extends BaseActivity{
	private SharedPreferences conference;
	private ViewPager mPager;
	private UnderlinePageIndicator mIndicator;
	protected List<View> mListViews;
	private ListView conference_begin_three_one_listview;
	private ListView conference_begin_three_two_listview;
	private ListView conference_begin_three_three_listview;
	
	private ArrayList<Topic> datalistOne = new ArrayList<Topic>();
	private ArrayList<Topic> datalistTwo = new ArrayList<Topic>();
	//private ArrayList<Topic> datalistThree = new ArrayList<Topic>();
	
	private ConfSelectTopicListAdapter listOneAdapter;
	private ConfSelectTopicListAdapter listTwoAdapter;
	//private ConfSelectTopicListAdapter listThreeAdapter;
	
	private LoadingDialog loadingdiag;
	
	@Override
	public void initView() {
		conference = this.getSharedPreferences("conference"+ Variables.loginname, MODE_PRIVATE);		
		
		mListViews=new ArrayList<View>();
		mPager = (ViewPager)findViewById(R.id.pager);
		mPager.setAdapter(new ViewPagerAdapter(fillData()));

		mIndicator = (UnderlinePageIndicator)findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);
	
		loadingdiag = new LoadingDialog(this);  
		loadingdiag.setCanceledOnTouchOutside(false); 
		loadingdiag.setText(getString(R.string.uploading));
	
	}

	protected void onResume(){
		
		SharedPreferences data = this.getSharedPreferences(Constants.DATA_NODE, MODE_PRIVATE);
		if (null != data){
			String newadded  = data.getString(Constants.DATA_TYPE_NEWADDED, "");
			if (null != newadded && "true".equals(newadded)){
				String startTime  = data.getString(Constants.DATA_STARTDATE, "");
				String endTime  = data.getString(Constants.DATA_ENDDATE, "");
				String keywords  = data.getString(Constants.DATA_KEYS, "");
				String title  = data.getString(Constants.DATA_TITLE, "");
				String id  = data.getString(Constants.DATA_ID, "");

				Topic t = new Topic();
				t.setHasEdited(true);
				t.setStartTime(startTime);
				t.setEndTime(endTime);
				t.setKeywords(keywords);
				t.setTitle(title);
				t.setContent(keywords);
				t.setTime(startTime + "--" + endTime);
				t.setId(id);
				datalistTwo.add(t);
				listTwoAdapter.notifyDataSetChanged();
			} else {
				if (datalistTwo.size() > 0){
					
					String id  = data.getString(Constants.DATA_ID, "");
					if (id != null && !"".equals(id)){
						for (Topic t:datalistTwo)
						{
							if (id.equals(t.getId())){
								t.setHasEdited(true);
							}
						}	
					}
				}
			}
			Editor dataEditor = data.edit();
			dataEditor.clear();
	        dataEditor.commit();
		}
		super.onResume();
	}
	
	private List<View> fillData() {
		
		listOneAdapter = new ConfSelectTopicListAdapter(this);
		listTwoAdapter = new ConfSelectTopicListAdapter(this);
		//listThreeAdapter = new ConfSelectTopicListAdapter(this);
		
		//getTopicList();
		fillDatalistOne();
		fillDatalistTwo();
		
		listOneAdapter.setData(datalistOne);
		listTwoAdapter.setData(datalistTwo);
		///listThreeAdapter.setData(datalistThree);
	
		View view_one = getLayoutInflater().inflate(R.layout.conference_begin_three_one, null);
		conference_begin_three_one_listview=(ListView) view_one.findViewById(R.id.conference_begin_three_one_listview);
		conference_begin_three_one_listview.setAdapter(listOneAdapter);
		mListViews.add(view_one);
			
		View view_two = getLayoutInflater().inflate(R.layout.conference_begin_three_two, null);
		conference_begin_three_two_listview=(ListView) view_two.findViewById(R.id.conference_begin_three_two_listview);
		conference_begin_three_two_listview.setAdapter(listTwoAdapter);
		conference_begin_three_two_listview.setOnItemClickListener(new OnItemClickListener(){
			 
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                Intent data=new Intent();  
                data.putExtra(Constants.ID,   (String)datalistTwo.get(arg2).getId());  
                
                IntentUtil.startActivity(ConBeginThreeActivity.this, TopicEditOneActivity.class,  data);
            }
        });
		
		mListViews.add(view_two);
			
		//View view_three = getLayoutInflater().inflate(R.layout.conference_begin_three_three, null);
		//conference_begin_three_three_listview=(ListView) view_three.findViewById(R.id.conference_begin_three_three_listview);
		//conference_begin_three_three_listview.setAdapter(listThreeAdapter);
		//mListViews.add(view_three);
		/*
		View view_three = getLayoutInflater().inflate(R.layout.conference_begin_three_three, null);
		conference_begin_three_three_listview=(ListView) view_three.findViewById(R.id.conference_begin_three_three_listview);
		conference_begin_three_three_listview.setAdapter(new VoteMyStayAdapter(ConBeginThreeActivity.this));
		mListViews.add(view_three);
		*/
		return mListViews;
	}

	@Override
	public void setupView() {
		setContentView(R.layout.conference_begin_three);

	}
	
	protected void onPause(){
		
		String topicString = "";
		String topicList = "[";
		int length = datalistTwo.size();
		for(int i=0;i<length;i++){
			Topic topic = datalistTwo.get(i);
			if(i == length-1){
				topicString +=topic.getId();
				topicList += "{'id':'"+topic.getId()+"','title':'"+topic.getTitle()+"','keywords':'"+topic.getKeywords()+"','content':'"+topic.getContent()+"','time':'"+topic.getTime()+"'}";
			} else {
				topicString +=topic.getId()+",";
				topicList += "{'id':'"+topic.getId()+"','title':'"+topic.getTitle()+"','keywords':'"+topic.getKeywords()+"','content':'"+topic.getContent()+"','time':'"+topic.getTime()+"'},";
			}
		}
		topicList +="]";
        Editor editor = conference.edit();
        editor.putString("conference_topic", topicString);  
        editor.putString("conference_topic_list", topicList);  
        editor.commit();
        System.out.println("wangting, save topic"+topicString +topicList);
		
		
		
		super.onPause();
	}
	
	public void clickPre(View view){
		IntentUtil.startActivity(this, ConBeginTwoActivity.class);
		finish();
	}
	public void clickAdd(View view){
		for(Topic item: datalistOne){
			if(item.isCheck()){
				boolean hastopic = false;
				for(Topic topic: datalistTwo){
					if(topic.getId().equals(item.getId())){
						hastopic = true;
						break;
					}
				}				
				if(!hastopic){
					Topic tp = new Topic();
					tp.copy(item);
					datalistTwo.add(tp);
				}
			}
		}
		listTwoAdapter.notifyDataSetChanged();
		mPager.arrowScroll(View.FOCUS_RIGHT);
		//finish();
	}
	
	
	public void clickRemoveTopic(View view){
		System.out.println("wangting, remove topic");
		//Toast.makeText(ConBeginThreeActivity.this, "remove", 100);	
		int length = datalistTwo.size();
		for(int i=length-1;i>=0;i--){
			if(datalistTwo.get(i).isCheck()){
				datalistTwo.remove(i);
			}
		}
		listTwoAdapter.notifyDataSetChanged();	
	}
	
	public boolean SaveTopic(){		
		
		int notEditedCount = 0;
		String errorTopicInfo = "";
		for (Topic tp: datalistTwo){
			if (tp.isHasEdited() == false){
				notEditedCount ++;
				errorTopicInfo += tp.getTitle() + ",";
			}
		}
		
		if (notEditedCount == datalistTwo.size()){
			Toast.makeText(ConBeginThreeActivity.this, getString(R.string.error_no_conf_topic_edited), Toast.LENGTH_LONG).show();
			return false;
		}else if (notEditedCount > 0){
			Toast.makeText(ConBeginThreeActivity.this, getString(R.string.error_conf_topics_part_edited) + errorTopicInfo,
					Toast.LENGTH_LONG).show();
			return false;
		}
		
		String topicString = "";
		String topicList = "[";
		int length = datalistTwo.size();
		for(int i=0;i<length;i++){
			Topic topic = datalistTwo.get(i);
			if(i == length-1){
				topicString +=topic.getId();
				topicList += "{'id':'"+topic.getId()+"','title':'"+topic.getTitle()+"','keywords':'"+topic.getKeywords()+"','content':'"+topic.getContent()+"','time':'"+topic.getTime()+"'}";
			} else {
				topicString +=topic.getId()+",";
				topicList += "{'id':'"+topic.getId()+"','title':'"+topic.getTitle()+"','keywords':'"+topic.getKeywords()+"','content':'"+topic.getContent()+"','time':'"+topic.getTime()+"'},";
			}
		}
		topicList +="]";
        Editor editor = conference.edit();
        editor.putString("conference_topic", topicString);  
        editor.putString("conference_topic_list", topicList);  
        editor.commit();
        System.out.println("wangting, save topic"+topicString +topicList);
        return true;
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 可以根据多个请求代码来作相应的操作
		
		if (Constants.CODE_CONF_EDITTOPIC == requestCode && data != null) {
			if (Constants.OK == resultCode) {
				String id = data.getExtras().getString(Constants.ID);
				
				for (Topic t:datalistTwo)
				{
					if (id.equals(t.getId())){
						t.setHasEdited(true);
					}
				}
				
			}
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void clickPublish(View view){
		if (!SaveTopic())
			return;
		String typeId = conference.getString("conference_conftype_id", "");//会议类型id
		String title = conference.getString("conference_title", "");//会议标题
		String address = conference.getString("conference_address_id", "");//会议地址
		String startdate = conference.getString("conference_starttime", "");//会议开始时间
		String EndDate = conference.getString("conference_endtime", "");//会议结束时间
		String managerId = conference.getString("conference_controlmember_id", "");//控制人
		String remark = conference.getString("conference_content", "");//会议摘要
		String nodifyType = conference.getString("conference_notifytype", "2");//短信0；客户端1；所有方式通知2
		String meetNo = conference.getString("conference_meetnumber", "");//手动填写的meet number可以为空
		String managerId2 = conference.getString("conference_accessmember_id", "");//审核人
		String sInfo = conference.getString("conference_joinmember", "");//参会人{人员id,人员类型}
		String topic = conference.getString("conference_topic", "");
		String attachment_no = conference.getString("attachment_no", "");
			
		/*
		String url ="MeetingManage/mobile/addMeetBasicInfo.action?typeId=14&title=测试添加会议&address=地址&" +
				"startdate=2014-09-06 11:55:12&EndDate=2024-12-22 12:21:21&managerId=1&remark=测试添加会议摘要&nodifyType=1&" +
				"meetNo=&managerId2=1&sInfo=23331,1;34123,1;23123,1";
		
		http://localhost:8080/MeetingManage/mobile/addMeetBasicInfo.action?typeId=14&title=%E4%BC%9A%E8%AE%AE%E6%A0%87%E9%A2%98&address=%E6%88%90%E9%83%BD%E5%B8%82&startdate=2014-09-06%2011:55:12&EndDate=2024-12-22%2012:21:21&managerId=1&remark=%E4%BC%9A%E8%AE%AE%E5%A4%87%E6%B3%A8&nodifyType=1&meetNo=%E8%BF%99%E6%98%AF%E4%BC%9A%E8%AE%AE%E7%BC%96%E5%8F%B7&managerId2=1&sInfo=23331,1;34123,1;23123,1
		*
		*/
		String url ="MeetingManage/mobile/addMeetBasicInfo.action?typeId="+typeId+"&title="+title+"&address="+address+"&" +
				"startdate="+startdate+"&EndDate="+EndDate+"&managerId="+managerId+"&remark="+remark+"&nodifyType="+nodifyType+"&" +
				"meetNo="+meetNo+"&managerId2="+managerId2+"&sInfo="+sInfo+"&topicid="+topic + "&attachment_no=" +attachment_no ;
		
		System.out.println("wangting:"+url);
		
		loadingdiag.show();
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler(){
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();
				Toast.makeText(ConBeginThreeActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
				System.out.println("wangting publish error:"+error.getMessage());				
                //finish();  
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				loadingdiag.hide();

				if (response.contains("用户未登陆")){
					Toast.makeText(ConBeginThreeActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}
				
				if (response.contains("SUCCESS")){
					
					
					//清除会议preference
					Editor editor = ConBeginThreeActivity.this.conference.edit();
					editor.clear();
					editor.commit();
					datalistTwo.clear();
					Toast.makeText(ConBeginThreeActivity.this, "会议添加成功", 100).show();

					JSONArray jsonArray;
					try {
						jsonArray = new JSONObject(response).getJSONArray("attenderConflictList");
						if (jsonArray.length() > 0){
							Intent intent = new Intent();
							intent.putExtra("response", response);
							IntentUtil.startActivity(ConBeginThreeActivity.this, AttenderConflictListActivity.class, intent);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					finish();					
				} else {
					Toast.makeText(ConBeginThreeActivity.this, "会议没有被添加", Toast.LENGTH_SHORT).show();
				}
			}
		});	
	}
	
	private void fillDatalistOne(){
		String url = "MeetingManage/mobile/findCheckPassedTopic.action";
		/*
		 {"tpList":[{
		 "check_reason":"",
		 "check_state":"",
		 "checker":"",			
		 "conclusion":"",
		 "creator":"",
		 "endtime":"2014-08-11 12:25:00.0",
		 "id":14,
		 "keywords":"王欢test1",
		 "meetName":"",
		 "starttime":"2014-08-11 08:25:00.0",
		 "suggested_attender":"",
		 "summary":"哈哈哈",
		 "target_org":""}]}
		*/
		MeetingSystemClient.get(url, null, new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String response,
					Throwable arg3) {
				System.out.println("get topic list error:"+arg3.getMessage());
				Toast.makeText(ConBeginThreeActivity.this, getString(R.string.error_network), 100).show();
				
			}

			@Override
			public void onSuccess(int statusCode, Header[] arg1, String response) {
				// TODO Auto-generated method stub
				if(statusCode == 200){
					try {
						JSONObject object = new JSONObject(response);
						
						if (response.contains("用户未登陆")){
							Toast.makeText(ConBeginThreeActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
							return;
						}	
						
						JSONArray topicList = object.getJSONArray("tpList");
						int length = topicList.length();
						for(int i=0;i<length;i++){
							JSONObject jsonTopic = topicList.getJSONObject(i);
							Topic topic = new Topic();
							topic.setId(jsonTopic.getString("id"));
							topic.setTitle(jsonTopic.getString("summary"));
							//topic.setKeywords(jsonTopic.getString("keywords"));
							topic.setContent(jsonTopic.getString("keywords"));
							topic.setStartTime(jsonTopic.getString("starttime"));
							topic.setEndTime(jsonTopic.getString("endtime"));
							topic.setTime(jsonTopic.getString("starttime")+"---"+jsonTopic.getString("endtime"));
							//topic.setId(jsonTopic.getString("id"));
							//topic.setId(jsonTopic.getString("id"));
							
							datalistOne.add(topic);
						}
						listOneAdapter.notifyDataSetChanged();
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		});

	}
	private void fillDatalistTwo(){
		String topicList = conference.getString("conference_topic_list", "");
		try {
			datalistTwo.clear();
			JSONArray list = new JSONArray(topicList);
			int length = list.length();
			for(int i=0;i<length;i++){
				JSONObject jsonTopic = list.getJSONObject(i);
				Topic topic = new Topic();
				topic.setId(jsonTopic.getString("id"));
				topic.setTitle(jsonTopic.getString("title"));
				topic.setKeywords(jsonTopic.getString("keywords"));
				topic.setContent(jsonTopic.getString("content"));
				topic.setTime(jsonTopic.getString("time"));
				datalistTwo.add(topic);
			}
			listTwoAdapter.notifyDataSetChanged();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void clickNewTopic(View view){
		IntentUtil.startActivity(ConBeginThreeActivity.this, TopicMeetAddOneActivity.class);
	}
}
