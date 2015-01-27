package cn.wislight.meetingsystem.ui.topic;

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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DialerFilter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.adapter.VoteAnonymousNameAdapter;
import cn.wislight.meetingsystem.adapter.VoteRealNameAdapter;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.LoadingDialog;
import cn.wislight.meetingsystem.util.MeetingSystemClient;
import cn.wislight.meetingsystem.util.SliderUtil;


public class TopicAdminControlVoteActivity extends BaseActivity implements OnClickListener{
	private ImageView mIvUnderLine;
	private TextView mTvRealName;
	private TextView mTvAnonymous;
	private int distance;
	private ListView mLvRealName;
	private VoteRealNameAdapter mAdapter;
	private LinearLayout mLlRealName;
	private LinearLayout mLlAnonymous;
	
	private TextView tv_vote_people_num;
	private TextView tv_vote_people_num_r;
	
	private EditText et_vote_agree;
	private EditText et_vote_unagree;
	private EditText et_vote_give_up;
	
	
	private ListView listView;
	private List<Map<String, String>> datalist;
	private BaseAdapter adapter;

	private String topicId;
	private int voteType;
	private int joinedCount = 0;
	private int unVotedCount = 0;
	private LoadingDialog loadingdiag;
	@Override
	public void initView() {
		tv_vote_people_num=(TextView) findViewById(R.id.tv_vote_people_num);
		tv_vote_people_num_r = (TextView) findViewById(R.id.tv_vote_people_num_r);
		et_vote_agree=(EditText) findViewById(R.id.et_vote_agree);
		et_vote_unagree=(EditText) findViewById(R.id.et_vote_unagree);
		et_vote_give_up=(EditText) findViewById(R.id.et_vote_give_up);
		
		
		mIvUnderLine=(ImageView) findViewById(R.id.iv_slide_underline);
		mTvAnonymous=(TextView) findViewById(R.id.tv_vote_anonymous);
		mTvRealName=(TextView) findViewById(R.id.tv_vote_real_name);
		listView = mLvRealName=(ListView) findViewById(R.id.lv_vote_realName);
		mLlRealName=(LinearLayout) findViewById(R.id.ll_vote_real_names);
		mLlAnonymous=(LinearLayout) findViewById(R.id.ll_vote_anonymous);
		mAdapter=new VoteRealNameAdapter(this);
		mLvRealName.setAdapter(mAdapter);
		
		topicId = this.getIntent().getStringExtra(Constants.ID);
		voteType = this.getIntent().getIntExtra(Constants.VOTE_TYPE, 0);
		initListener();
		
		loadingdiag = new LoadingDialog(this);  
		loadingdiag.setCanceledOnTouchOutside(false); 
		loadingdiag.setText(getString(R.string.loading));
		
		if (1 == voteType){
			initRealNameDatas();
		} else {
			mLlAnonymous.setVisibility(View.VISIBLE);
			mLlRealName.setVisibility(View.GONE);
			SliderUtil.moveSlider(mIvUnderLine, 0, distance);
			initAnonymousDatas();
		}
	}

	private void initAnonymousDatas() {
		// TODO Auto-generated method stub
		loadingdiag.show();
		String url = "MeetingManage/mobile/getTopicInfoById.action?Id="+topicId;
		
		System.out.println("wangting:"+url);
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				String response = error.getMessage();
				Toast.makeText(TopicAdminControlVoteActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
				loadingdiag.hide();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				loadingdiag.hide();
				if (response.contains("用户未登陆")){
					Toast.makeText(TopicAdminControlVoteActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				try {
					 JSONArray jsonArray=new JSONObject(response).getJSONArray("attenderEntity");
					 int length = jsonArray.length();
					 for(int i=0;i<length;i++){
			               JSONObject jsonObject=(JSONObject)jsonArray.get(i);
			               int isJoin = Integer.parseInt((String)jsonObject.getString("isjoin"));
			               int voteresult = Integer.parseInt((String)jsonObject.getString("voteresult"));
			               if (isJoin  == 1){
			            	   joinedCount++;
			               }
			               
			               if (isJoin == 1 && voteresult == -1){
			            	   unVotedCount++;
			               }
			               //map.put("organizename", jsonObject.getString("organizename"));
			               //map.put("postname", jsonObject.getString("postname"));
					 }
					 tv_vote_people_num.setText(unVotedCount + "");
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(TopicAdminControlVoteActivity.this, getString(R.string.error_dataabout), Toast.LENGTH_SHORT).show();
	                finish();  
				}
				
			}
		});
		
	}

	private void initListener() {
		mTvAnonymous.setOnClickListener(this);
		mTvRealName.setOnClickListener(this);
		distance=SliderUtil.getDistance(this);

	}

	@Override
	public void setupView() {
		setContentView(R.layout.vote_main);		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/*
		case R.id.tv_vote_anonymous:
			mLlAnonymous.setVisibility(View.VISIBLE);
			mLlRealName.setVisibility(View.GONE);
			SliderUtil.moveSlider(mIvUnderLine, 0, distance);
			break;
		case R.id.tv_vote_real_name:
			mLlAnonymous.setVisibility(View.GONE);
			mLlRealName.setVisibility(View.VISIBLE);
			SliderUtil.moveSlider(mIvUnderLine, distance, 0);
			mAdapter.clear();
			initRealNameDatas() ;
			break;
		*/
		default:
			break;
		}

	}

	public void clickGiveUpAdd(View view){
		int count;
		String strCount = et_vote_give_up.getText().toString();
		if (null != strCount && !"".equals(strCount)){
			count = Integer.parseInt(strCount);
		} else {
			count = 0;
		}
		
		if (count < joinedCount){
			count++;
			et_vote_give_up.setText(count + "");
		}
	}
	
	public void clickGiveUpSub(View view){
		int count;
		String strCount = et_vote_give_up.getText().toString();
		if (null != strCount && !"".equals(strCount)){
			count = Integer.parseInt(strCount);
		} else {
			count = 0;
		}
		
		if (count > 0){
			count--;
		}		
		et_vote_give_up.setText(count + "");
	}
	
	public void clickUbAgreeAdd(View view){
		int count;
		String strCount = et_vote_unagree.getText().toString();
		if (null != strCount && !"".equals(strCount)){
			count = Integer.parseInt(strCount);
		} else {
			count = 0;
		}
		
		if (count < joinedCount){
			count++;
			et_vote_unagree.setText(count + "");
		}
	}
	public void clickUbAgreeSub(View view){
		int count;
		String strCount = et_vote_unagree.getText().toString();
		if (null != strCount && !"".equals(strCount)){
			count = Integer.parseInt(strCount);
		} else {
			count = 0;
		}
		
		if (count > 0){
			count--;
		}		
		et_vote_unagree.setText(count + "");

	}

	
	public void clickAgreeAdd(View view){
		int count;
		String strCount = et_vote_agree.getText().toString();
		if (null != strCount && !"".equals(strCount)){
			count = Integer.parseInt(strCount);
		} else {
			count = 0;
		}
		
		if (count < joinedCount){
			count++;
			et_vote_agree.setText(count + "");
		}
	}
	
	public void clickAgreeSub(View view){
		int count;
		String strCount = et_vote_agree.getText().toString();
		if (null != strCount && !"".equals(strCount)){
			count = Integer.parseInt(strCount);
		} else {
			count = 0;
		}
		
		if (count > 0){
			count--;
		}		
		et_vote_agree.setText(count + "");

	}	
	
	private void initRealNameDatas() {
		//listView=(ListView) findViewById(R.id.lv_admin_vote_listview);
		//String url = "MeetingManage/mobile/ModifyAttenderIsJoin.action?id="+ids;
		String url = "MeetingManage/mobile/getTopicInfoById.action?Id="+topicId;
		
		System.out.println("wangting:"+url);
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				String response = error.getMessage();

				System.out.println("wangting, success response="+response);
				Toast.makeText(TopicAdminControlVoteActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                //Intent data=new Intent();  
                //setResult(Constants.ERROR_NETWORK, data);  
                //finish();  
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				System.out.println("wangting, success response="+response);
				
				if (response.contains("用户未登陆")){
					Toast.makeText(TopicAdminControlVoteActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				// Toast.makeText(TopicSelectApplicatorActivity.this, new String(response)+"wangting"+response.toString(), 100).show();
				listView.setVisibility(View.VISIBLE);		
				//WaitMyCheckListAdapter adapter = new WaitMyCheckListAdapter(ConCheckActivity.this);
				datalist = new ArrayList<Map<String, String>>(); 

					adapter = new VoteRealNameAdapter(TopicAdminControlVoteActivity.this);
					((VoteRealNameAdapter) adapter).setData(datalist);
		
				
				try {
					 JSONArray jsonArray=new JSONObject(response).getJSONArray("attenderEntity");
					 Map<String, String> map;	
					 int length = jsonArray.length();
					 System.out.println("wangting:length="+length);
					 for(int i=0;i<length;i++){
			               JSONObject jsonObject=(JSONObject)jsonArray.get(i);
			               map = new HashMap<String, String>();

			               int isJoin = Integer.parseInt((String)jsonObject.getString("isjoin"));
			               if (isJoin  == 1){
			            	   joinedCount++;
			            	   
				               map.put("id", jsonObject.getString("id"));
				               map.put("name",jsonObject.getString("name"));
				               map.put("value", jsonObject.getString("voteresult"));
				               datalist.add(map);
			               }
			               //map.put("organizename", jsonObject.getString("organizename"));
			               //map.put("postname", jsonObject.getString("postname"));
					 }
					 tv_vote_people_num_r.setText(joinedCount + "");
					 
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(TopicAdminControlVoteActivity.this, getString(R.string.error_dataabout), Toast.LENGTH_SHORT).show();
	                finish();  
				}
				
				listView.setAdapter(adapter);
				listView.setOnItemClickListener(new OnItemClickListener(){					 
		            @Override
		            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		                    long arg3) {
		            	/*
		                Intent data=new Intent();  
		                data.putExtra(Constants.ID,   (String)list.get(arg2).get("id"));  
		                IntentUtil.startActivity(TopicAdminControlVoteActivity.this, ConfCheckDetailActivity.class,  data);
		            	*/
		            }
		        });		
				
				
			}
		});			
	
	}
	public void  clickRealNameVoteSubmit(View view){
		String result = "";
		int length = datalist.size();
		for(int i=0;i<length;i++){
			Map<String,String> item = datalist.get(i);
			if(i == length-1){
				result += item.get("id")+"," + item.get("value");
			}else{
				result += item.get("id")+"," + item.get("value")+";";
			}
		}
		loadingdiag.setText(getString(R.string.upload_votes));
		loadingdiag.show();
		//String url=""+result;
		String url = "MeetingManage/mobile/updateUserVoteByAdmin.action?meetProcId="+topicId+"&result="+result;		
		System.out.println("wangting:"+url);
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				String response = error.getMessage();
				loadingdiag.hide();
				System.out.println("wangting, success response="+response);
				Toast.makeText(TopicAdminControlVoteActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                //Intent data=new Intent();  
                //setResult(Constants.ERROR_NETWORK, data);  
                //finish();  
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				System.out.println("wangting, success response="+response);
				// Toast.makeText(TopicSelectApplicatorActivity.this, new String(response)+"wangting"+response.toString(), 100).show();
				loadingdiag.hide();
				if (response.contains("用户未登陆")){
					Toast.makeText(TopicAdminControlVoteActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				if(response.contains("true")){
					Toast.makeText(TopicAdminControlVoteActivity.this, "实名投票数据提交成功", 100).show();
					finish();
				}else{
					String msg = "";
					try {
						msg = new JSONObject(response)
								.getString("msg");
					}catch(Exception e){
						Toast.makeText(TopicAdminControlVoteActivity.this, "实名投票数据提交失败", 100).show();
					}
					Toast.makeText(TopicAdminControlVoteActivity.this, msg, 100).show();
				}
			}
		});			

	}
	public void  clickAnonymousNameVoteSubmit(View view){
		String giveUpCount = et_vote_give_up.getText().toString();
		if (null == giveUpCount || "".equals(giveUpCount)){
			giveUpCount = "0";
		}
		String agreeCount = et_vote_agree.getText().toString();
		if (null == agreeCount || "".equals(agreeCount)){
			agreeCount = "0";
		}
		String unagreeCount = et_vote_unagree.getText().toString();
		if (null == unagreeCount || "".equals(unagreeCount)){
			unagreeCount = "0";
		}
		
		if (Integer.parseInt(giveUpCount) + Integer.parseInt(agreeCount) + Integer.parseInt(unagreeCount) != unVotedCount){
			Toast.makeText(TopicAdminControlVoteActivity.this, getString(R.string.error_votecount_exceeding), 100).show();
			return;
		}
		//String url=""+result;
		String url = "MeetingManage/mobile/getAnonymousVoting.action?topicid="+topicId
				+"&giveUpCount="+giveUpCount
				+"&agreeCount="+agreeCount
				+"&unagreeCount="+unagreeCount;		
		System.out.println("wangting:"+url);
		
		loadingdiag.setText(getString(R.string.upload_votes));
		loadingdiag.show();
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				String response = error.getMessage();
				loadingdiag.hide();
				System.out.println("wangting, success response="+response);
				Toast.makeText(TopicAdminControlVoteActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                //Intent data=new Intent();  
                //setResult(Constants.ERROR_NETWORK, data);  
                //finish();  
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				System.out.println("wangting, success response="+response);
				loadingdiag.hide();
				
				if (response.contains("用户未登陆")){
					Toast.makeText(TopicAdminControlVoteActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				// Toast.makeText(TopicSelectApplicatorActivity.this, new String(response)+"wangting"+response.toString(), 100).show();
				if(response.contains("true")){
					Toast.makeText(TopicAdminControlVoteActivity.this, "匿名投票数据提交成功", 100).show();
					finish();
				}else{
					
					String msg = "";
					try {
						msg = new JSONObject(response)
								.getString("msg");
					}catch(Exception e){
						Toast.makeText(TopicAdminControlVoteActivity.this, "匿名投票数据提交失败", 100).show();
					}
					Toast.makeText(TopicAdminControlVoteActivity.this, msg, 100).show();
					
					
				}
			}
		});			

	}
}
