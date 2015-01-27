package cn.wislight.publicservice.ui.commercialtenant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


import cn.wislight.publicservice.R;
import cn.wislight.publicservice.adapter.IMMessageListAdapter;
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.util.Constants;
import cn.wislight.publicservice.util.PublicServiceClient;

import com.loopj.android.http.AsyncHttpResponseHandler;




/**
 * @author Administrator
 * 虚拟会议室
 */
public class IMChatingActivity extends BaseActivity {
	//private Device device;
	private String IMGroupId;
	private EditText txTopicMessage;
	private ListView listView;
	private List<Map<String, String>> datalist;
	private IMMessageListAdapter adapter;
	private String topicId;
	private String myName;
	private String negotiationId;
	private String receiverVOIP;
	@Override
	public void setUpView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_immessage_chating);
		
    	negotiationId = getIntent().getStringExtra(Constants.ID);
    	receiverVOIP = getIntent().getStringExtra("receiverVOIP");
		//topicId = getIntent().getStringExtra(Constants.ID);
		//IMGroupId = getIntent().getStringExtra(Constants.TOPIC_IM_GROUP_ID);
		
		//device = CCPHelper.getInstance().getDeviceHelper();
		//CCPHelper.getInstance().setIMHandler(mIMChatHandler);
		
		IMGroupId = "g8001682820855";
		myName = "wangting";
		
		txTopicMessage = (EditText)findViewById(R.id.topic_message);	
		
		listView=(ListView) findViewById(R.id.lv_message_listview);
		listView.setDividerHeight(0);
		adapter = new IMMessageListAdapter(IMChatingActivity.this);
		datalist = new ArrayList<Map<String, String>>(); 
		
		Map<String,String> message = new HashMap<String,String>();
		message.put("messageId", "0");
		message.put("username", "陈维");
		message.put("message", "我要吃苹果！！！");
		datalist.add(message);	

		
		adapter.setData(datalist);
		adapter.setMyName(myName);
		listView.setAdapter(adapter);
	}

	private void setDate(){		
		listView=(ListView) findViewById(R.id.lv_message_listview);
		//String url = "MeetingManage/mobile/ModifyAttenderIsJoin.action?id="+ids;
		String url = "MeetingManage/mobile/getTopicInfoById.action?Id="+topicId;
		
		System.out.println("wangting:"+url);
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				String response = error.getMessage();
				System.out.println("wangting, fail response="+response);
				Toast.makeText(IMChatingActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                //Intent data=new Intent();  
                //setResult(Constants.ERROR_NETWORK, data);  
                //finish();  
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				System.out.println("wangting, success response="+response);
				// Toast.makeText(TopicSelectApplicatorActivity.this, new String(response)+"wangting"+response.toString(), 100).show();
				listView.setVisibility(View.VISIBLE);		
				//WaitMyCheckListAdapter adapter = new WaitMyCheckListAdapter(ConCheckActivity.this);
				adapter = new IMMessageListAdapter(IMChatingActivity.this);
				datalist = new ArrayList<Map<String, String>>(); 
				
				try {
					 JSONArray jsonArray=new JSONObject(response).getJSONArray("attenderEntity");
					 Map<String, String> map;	
					 int length = jsonArray.length();
					 System.out.println("wangting:length="+length);
					 for(int i=0;i<length;i++){
			               JSONObject jsonObject=(JSONObject)jsonArray.get(i);
			               map = new HashMap<String, String>();
			               map.put("id", jsonObject.getString("id"));
			               map.put("name",jsonObject.getString("name"));
			               map.put("value", jsonObject.getString("isjoin"));
			               //map.put("organizename", jsonObject.getString("organizename"));
			               //map.put("postname", jsonObject.getString("postname"));
			               datalist.add(map);
					 }
					
				} catch (JSONException e) {
					e.printStackTrace();
					/*
					Toast.makeText(TopicDianmingAcitivity.this, getString(R.string.error_dataabout), Toast.LENGTH_SHORT).show();
					Intent data=new Intent();  
	                setResult(Constants.ERROR_DATA, data);  	                
	                finish();  
	                */
					
				}
				adapter.setData(datalist);
				listView.setAdapter(adapter);
				listView.setOnItemClickListener(new OnItemClickListener(){					 
		            @Override
		            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		                    long arg3) {
		            	/*
		                Intent data=new Intent();  
		                data.putExtra(Constants.ID,   (String)list.get(arg2).get("id"));  
		                IntentUtil.startActivity(TopicDianmingAcitivity.this, ConfCheckDetailActivity.class,  data);
		            	*/
		            }
		        });		
				
				
			}
		});			
	
		
	}

	public void  clickSendMessage(View view){
		String message = txTopicMessage.getText().toString();
		if(message == null || message.trim().length() == 0){
			Toast.makeText(IMChatingActivity.this, "不能发送空消息", 100).show();
			return;
		}
		System.out.println("wangtin: message="+message);
		//String msgId = device.sendInstanceMessage(IMGroupId, message, null, null);
		//System.out.println("wangtin: msgId="+msgId);
		txTopicMessage.setText("");
		
		Map<String,String> message2 = new HashMap<String,String>();
		//message2.put("messageId", msgId);
		message2.put("username", myName);
		message2.put("message", message);
		datalist.add(message2);
		//adapter.setData(datalist);
		//adapter.notifyDataSetChanged();
		//System.out.println("wangtin: listView.getCount()="+listView.getCount());
		listView.setSelection(listView.getCount());		
	}
	/*
	private Handler mIMChatHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);			
			switch (msg.what) {
			case CCPHelper.WHAT_ON_IM_RECEIVE_MESSAGE:			
				
				if (msg.obj instanceof IMTextMsg) {
					IMTextMsg imTextMsg = (IMTextMsg)msg.obj;
					String messageId = imTextMsg.getMsgId();
					String messageContext = imTextMsg.getMessage();
					String messageReceiver = imTextMsg.getReceiver();
					if (messageReceiver.equals(IMGroupId)){
						System.out.println("wangting: receiver="+messageReceiver);
						return;
					}
					System.out.println("wangting: receiver message="+messageContext);
					String messageSender = imTextMsg.getSender();
					Map<String,String> message = new HashMap<String,String>();
					message.put("messageId", messageId);
					message.put("username", messageSender);
					message.put("message", messageContext);					
					datalist.add(message);
					//adapter.setData(datalist);
					//adapter.notifyDataSetChanged();			
					listView.setSelection(listView.getCount());	
				}					
				break;
			case CCPHelper.WHAT_ON_IM_SEND_MESSAGE_RESULT:
				if(msg.obj instanceof JSONObject){
					JSONObject object = (JSONObject)msg.obj;
					try {
						CloopenReason reason = (CloopenReason)object.get("CloopenReason");
						InstanceMsg message = (InstanceMsg)object.get("InstanceMsg");
						if (message instanceof IMTextMsg){
							IMTextMsg imTextMsg = (IMTextMsg)message;
							String messageId = imTextMsg.getMsgId();
							for(Map<String,String> temp: datalist){
								if(temp.get("messageId")!=null && temp.get("messageId").equals(messageId)){
									temp.put("sendStatus", imTextMsg.getStatus());
									System.out.println("wangting: imTextMsg.getStatus()="+imTextMsg.getStatus());
									adapter.notifyDataSetChanged();
								}
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}				
				break;	
			
			default:
				break;
			}
		}


	};

*/

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		
	}
}
