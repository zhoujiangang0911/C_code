package cn.wislight.meetingsystem.ui.conference;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.adapter.ConfSelectJoinmemberListAdapter;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.ui.setting.CreateTempPerson;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.Element;
import cn.wislight.meetingsystem.util.IntentUtil;
import cn.wislight.meetingsystem.util.LoadingDialog;
import cn.wislight.meetingsystem.util.MeetingSystemClient;
import cn.wislight.meetingsystem.util.SliderUtil;
import cn.wislight.meetingsystem.util.Variables;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class ConfSelectJoinMemberActivity extends BaseActivity implements
		OnClickListener {
	private SharedPreferences conference;
	private ImageView mIvUnderLine;
	private TextView mTvJoinemberWaitto;
	private TextView mTvJoinmemberHas;
	private int distance;
	private ListView mLvJoinmemberWaitto;
	private ConfSelectJoinmemberListAdapter mAdapter;
	private LinearLayout mLlJoinmemberWaitto;
	private LinearLayout mLlJoinmemberHas;
	private ArrayList<Element> list = null;
	private ArrayList<Element> listHasSel = new ArrayList<Element>();
	private ImageButton mBtSel, mBtAdd, mBtRemove, mBtSubmit;
	private ListView mLvJoinmemberHas;
	private ConfSelectJoinmemberListAdapter mHasListAdapter;
	
	/* search bar begin */
	private LinearLayout llSearchBar;
	private Spinner mSpinner;
	String [] mStringArray;
	private ArrayAdapter<String> mSpAdapter ;
	private EditText etOrgName;
	private EditText etPplName;
	/* search bar end */
	LoadingDialog loadingdiag;
	@Override
	public void initView() {
		conference = this.getSharedPreferences("conference"+ Variables.loginname, MODE_PRIVATE);
		
		mIvUnderLine = (ImageView) findViewById(R.id.conf_joinmember_slide_underline);
		mTvJoinmemberHas = (TextView) findViewById(R.id.conf_joinmember_has);
		mTvJoinemberWaitto = (TextView) findViewById(R.id.conf_joinmember_waitto);
		mLvJoinmemberWaitto = (ListView) findViewById(R.id.lv_joinmember_waitto);
		mLlJoinmemberWaitto = (LinearLayout) findViewById(R.id.ll_joinmember_waitto);
		
		mLlJoinmemberHas = (LinearLayout) findViewById(R.id.ll_joinmember_has);
		mLvJoinmemberHas = (ListView) findViewById(R.id.lv_joinmember_has);
		mBtSel = (ImageButton)findViewById(R.id.ibtn_select_all);
		mBtAdd = (ImageButton)findViewById(R.id.ibtn_addto);
		mBtRemove = (ImageButton)findViewById(R.id.ibtn_remove);
		mBtSubmit = (ImageButton)findViewById(R.id.ibtn_submit);
		
		llSearchBar = (LinearLayout)findViewById(R.id.ll_search_bar);
		llSearchBar.setVisibility(View.GONE);
		mSpinner = (Spinner)findViewById(R.id.sp_member);
        mStringArray=getResources().getStringArray(R.array.test_string_array);
        mSpAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,mStringArray);         
        mSpAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mSpAdapter);
        etOrgName = (EditText)findViewById(R.id.et_search_org);
        etPplName = (EditText)findViewById(R.id.et_search_name);
		mAdapter = new ConfSelectJoinmemberListAdapter(this);
		mHasListAdapter = new ConfSelectJoinmemberListAdapter(this);
		// mLvJoinmemberWaitto.setAdapter(mAdapter);
		loadingdiag = new LoadingDialog(this);  
		loadingdiag.setCanceledOnTouchOutside(false); 
		loadingdiag.setText(getString(R.string.loading));
		
		String conference_joinmember_list = conference.getString("conference_joinmember_list", "");
		System.out.println("wangting:"+conference_joinmember_list);
		JSONArray joinmember_list;
		try {
			joinmember_list = new JSONArray(conference_joinmember_list);
			int length = joinmember_list.length();
			System.out.println("wangting:length="+length);
			for (int i = 0; i<length; i++){
				JSONObject jsonObject = joinmember_list.getJSONObject(i);
				Element ele = new Element();

				ele.setName(jsonObject.getString("name"));
				ele.setOrg(jsonObject.getString("orgnizename"));
				ele.setPost(jsonObject.getString("position"));
				ele.setId(jsonObject.getString("id"));
				ele.setType(jsonObject.getString("type"));
	            listHasSel.add(ele);	               
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mHasListAdapter.setData(listHasSel);
		mLvJoinmemberHas.setAdapter(mHasListAdapter);
		
		
		mLlJoinmemberWaitto.setVisibility(View.VISIBLE);
		mLlJoinmemberHas.setVisibility(View.GONE);
		initListener();
		GetDefaultAttender();
	}


	public void clickCreateTemp(View view){
		IntentUtil.startActivity(this, CreateTempPerson.class);
	}
	
	public void clickFindTemp(View view){
		searchMyCreatedTempPerson();
	}
	
	
	public void clickTitleSearch(View view){
		if (llSearchBar.getVisibility() == View.VISIBLE){
			llSearchBar.setVisibility(View.GONE);
		} else {
			llSearchBar.setVisibility(View.VISIBLE);
		}
	}
	
	public void clickBeginSearch(View view){
		int type = mSpinner.getSelectedItemPosition();
		
		String orgname = etOrgName.getText().toString();
		String pplname = etPplName.getText().toString();
		
		int types[] = {2, 1, 0};
		int ctype = types[type];
		if (ctype == 2 || ctype == 1){
			if (orgname.length() == 0){
				Toast.makeText(ConfSelectJoinMemberActivity.this, 
						getString(R.string.error_search_no_org), Toast.LENGTH_SHORT).show();
				return;
			}
		}else{
			if (!(orgname.length() != 0 | pplname.length() != 0)){
				Toast.makeText(ConfSelectJoinMemberActivity.this, 
						getString(R.string.error_search_atleast_one), Toast.LENGTH_SHORT).show();
				return;
			}
		}
		
		searchPersionInfo(type, orgname, pplname);
	}
	
	private void searchMyCreatedTempPerson() {
		// TODO Auto-generated method stub
		String url = "MeetingManage/mobile/findPersonMyCreated.action";
		loadingdiag.show();
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();
				Toast.makeText(ConfSelectJoinMemberActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				// Toast.makeText(TopicSelectApplicatorActivity.this, new String(response)+"wangting"+response.toString(), 100).show();
				loadingdiag.hide();
				if (response.contains("用户未登陆")){
					Toast.makeText(ConfSelectJoinMemberActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				llSearchBar.setVisibility(View.GONE);

				mLlJoinmemberWaitto.setVisibility(View.VISIBLE);
				mLlJoinmemberHas.setVisibility(View.GONE);
				SliderUtil.moveSlider(mIvUnderLine, distance, 0);
				
				if (list == null){
					list = new ArrayList<Element>();
				} else {
					list.clear();
				}
				 
				
				try {
					 JSONArray jsonArray=new JSONObject(response).getJSONArray("saList");
					  
					 Element ele = null;
				        
					 for(int i=0;i<jsonArray.length();i++){
			               JSONObject jsonObject=(JSONObject)jsonArray.get(i);
			               ele = new Element();
			               ele.setName(jsonObject.getString("name"));
			               ele.setOrg(jsonObject.getString("orgnizename"));
			               ele.setPost(jsonObject.getString("position"));
			               ele.setId(jsonObject.getString("id"));
			               ele.setType(jsonObject.getString("type"));

			               ele.setCheck(false);
			               list.add(ele);
					 }
					
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ConfSelectJoinMemberActivity.this, getString(R.string.error_dataabout), Toast.LENGTH_SHORT).show();
	                
					
				}
				mAdapter.setData(list);
				mLvJoinmemberWaitto.setAdapter(mAdapter);
				
			}
		});		

	}
	
	private void searchPersionInfo(int type, String orgname, String pplname) {
		// TODO Auto-generated method stub
		String url = "MeetingManage/mobile/findPersionInfoExt.action?";
		int types[] = {2, 1, 0};
		url += "ttype=" + types[type];
		url += "&name=" + pplname;
		url += "&org=" + orgname;
		loadingdiag.show();
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();
				Toast.makeText(ConfSelectJoinMemberActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                finish();  
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				// Toast.makeText(TopicSelectApplicatorActivity.this, new String(response)+"wangting"+response.toString(), 100).show();
				loadingdiag.hide();
				
				if (response.contains("用户未登陆")){
					Toast.makeText(ConfSelectJoinMemberActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				llSearchBar.setVisibility(View.GONE);

				mLlJoinmemberWaitto.setVisibility(View.VISIBLE);
				mLlJoinmemberHas.setVisibility(View.GONE);
				SliderUtil.moveSlider(mIvUnderLine, distance, 0);
				
				if (list == null){
					list = new ArrayList<Element>();
				} else {
					list.clear();
				}
				 
				
				try {
					 JSONArray jsonArray=new JSONObject(response).getJSONArray("saList");
					  
					 Element ele = null;
				        
					 for(int i=0;i<jsonArray.length();i++){
			               JSONObject jsonObject=(JSONObject)jsonArray.get(i);
			               ele = new Element();
			               ele.setName(jsonObject.getString("name"));
			               ele.setOrg(jsonObject.getString("orgnizename"));
			               ele.setPost(jsonObject.getString("position"));
			               ele.setId(jsonObject.getString("id"));
			               ele.setType(jsonObject.getString("type"));

			               ele.setCheck(false);
			               list.add(ele);
					 }
					
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ConfSelectJoinMemberActivity.this, getString(R.string.error_dataabout), Toast.LENGTH_SHORT).show();


					
				}
				mAdapter.setData(list);
				mLvJoinmemberWaitto.setAdapter(mAdapter);
				
			}
		});		

	}

	private void initListener() {
		mTvJoinmemberHas.setOnClickListener(this);
		mTvJoinemberWaitto.setOnClickListener(this);
		mBtSel.setOnClickListener(this);
		mBtAdd.setOnClickListener(this);
		mBtRemove.setOnClickListener(this);
		mBtSubmit.setOnClickListener(this);
		distance = SliderUtil.getDistance(this);

	}

	@Override
	public void setupView() {
		setContentView(R.layout.conference_select_joinmember);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.conf_joinmember_waitto:
			mLlJoinmemberWaitto.setVisibility(View.VISIBLE);
			mLlJoinmemberHas.setVisibility(View.GONE);
			SliderUtil.moveSlider(mIvUnderLine, distance, 0);
			// mAdapter.clear();
			
			break;
		case R.id.conf_joinmember_has:
			mLlJoinmemberWaitto.setVisibility(View.GONE);
			mLlJoinmemberHas.setVisibility(View.VISIBLE);
			SliderUtil.moveSlider(mIvUnderLine, 0, distance);
			break;
		case R.id.ibtn_select_all:
			mAdapter.checkAll();
			break;
		case R.id.ibtn_addto:	
			for (Element ele: list){
				if (ele.isCheck){
					boolean hasAdded = false;
					for (Element ele1: listHasSel){
						if (ele1.getId().equals(ele.getId())){
							hasAdded = true;
							break;
						}
					}
					if (!hasAdded){
						Element ele2 = new Element();
						ele2.copy(ele);
						listHasSel.add(ele2);
					}
				}
				ele.isCheck = false;
			}
			mAdapter.notifyDataSetChanged();
			
			mHasListAdapter.setData(listHasSel);
			mLvJoinmemberHas.setAdapter(mHasListAdapter);
			mHasListAdapter.notifyDataSetChanged();
			mLlJoinmemberWaitto.setVisibility(View.GONE);
			mLlJoinmemberHas.setVisibility(View.VISIBLE);
			SliderUtil.moveSlider(mIvUnderLine, 0, distance);
			break;
		case R.id.ibtn_submit:
            Intent data=new Intent();
            data.putExtra(Constants.CONF_JOINMEMBER_LIST, listHasSel);
            setResult(Constants.OK, data);  
            finish();  
			break;
		case R.id.ibtn_remove:
			for (int i = listHasSel.size() - 1; i>=0; i--){
				if (listHasSel.get(i).isCheck){
					listHasSel.remove(i);
				}
			}

			mHasListAdapter.setData(listHasSel);
			mLvJoinmemberHas.setAdapter(mHasListAdapter);
			mHasListAdapter.notifyDataSetChanged();
			break;
		default:
			break;
		}

	}

    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK ) {  
            finish();  
        }            
        return false;            
    }
	private void GetDefaultAttender() {
		//String url = "MeetingManage/mobile/findRecordList.action";
		String url = "MeetingManage/mobile/findPersionInfoDefault.action";
		loadingdiag.show();
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				String response = error.getMessage();
				System.out.println("wamgting:"+response);
				Toast.makeText(ConfSelectJoinMemberActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                Intent data=new Intent();  
                setResult(Constants.ERROR_NETWORK, data);  
                finish();  
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				System.out.println(response);
				// Toast.makeText(TopicSelectApplicatorActivity.this, new String(response)+"wangting"+response.toString(), 100).show();
				loadingdiag.hide();
				
				if (response.contains("用户未登陆")){
					Toast.makeText(ConfSelectJoinMemberActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				mLvJoinmemberWaitto.setVisibility(View.VISIBLE);		
				// loading.setVisibility(View.GONE);
				
				
				list = new ArrayList<Element>(); 
				
				try {
					 JSONArray jsonArray=new JSONObject(response).getJSONArray("saList");
					 /*
					 {"saList":[
					 {"cardNo":"510126194805175818",  //身份证号码
					 "id":10904,  
					 "name":"吴仕文",   // 姓名
					 "orgnizename":"积泉 村",  // 组织单位名称
					 "phone":"13408457904",  //联系电话
					 "position":"村民代表大会成员",  //  职务
					 "sex":0,  //性别    0 男 ，1 女
					 "type":2    -1:所有 0：临时 1：居民 2：人事
					 }]}

					  */
					 Element ele = null;
				        
					 for(int i=0;i<jsonArray.length();i++){
			               JSONObject jsonObject=(JSONObject)jsonArray.get(i);
			               ele = new Element();
			               ele.setName(jsonObject.getString("name"));
			               ele.setOrg(jsonObject.getString("orgnizename"));
			               ele.setPost(jsonObject.getString("position"));
			               ele.setId(jsonObject.getString("id"));
			               ele.setType(jsonObject.getString("type"));
			               ele.setCheck(false);
			               list.add(ele);
					 }
					
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ConfSelectJoinMemberActivity.this, getString(R.string.error_dataabout), Toast.LENGTH_SHORT).show();
					Intent data=new Intent();  
	                setResult(Constants.ERROR_DATA, data);  
	                finish();  
					
				}
				mAdapter.setData(list);
				mLvJoinmemberWaitto.setAdapter(mAdapter);
				
			}
		});		
	}
	

}




