package cn.wislight.meetingsystem.ui.setting;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.ui.conference.ConfSelectJoinMemberActivity;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.Element;
import cn.wislight.meetingsystem.util.IntentUtil;
import cn.wislight.meetingsystem.util.LoadingDialog;
import cn.wislight.meetingsystem.util.MeetingSystemClient;
import cn.wislight.meetingsystem.util.SliderUtil;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class CommonGroupAddMembersActivity extends BaseActivity implements
		OnClickListener {
	
	
	private ImageView mIvUnderLine;
	private TextView mTvAttenderWaitto;
	private TextView mTvAttenderHas;
	private int distance;
	private ListView mLvAttenderWaitto;
	private WaitoSelMemberListAdapter mAdapter;
	private LinearLayout mLlAttenderWaitto;
	private LinearLayout mLlAttenderHas;
	private ArrayList<Element> list = null;
	private ArrayList<Element> listHasSel = new ArrayList<Element>();
	private ImageButton mBtRemove, mBtSubmit;
	private Button mBtSel, mBtAdd;
	private ListView mLvAttenderHas;
	private WaitoSelMemberListAdapter mHasListAdapter;
	private  String orgno = "";
	
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
		mIvUnderLine = (ImageView) findViewById(R.id.iv_slide_underline);
		mTvAttenderHas = (TextView) findViewById(R.id.tv_attender_has);
		mTvAttenderWaitto = (TextView) findViewById(R.id.tv_attender_waitto);
		mLvAttenderWaitto = (ListView) findViewById(R.id.lv_attender_waitto);
		mLlAttenderWaitto = (LinearLayout) findViewById(R.id.ll_attender_waitto);
		
		mLlAttenderHas = (LinearLayout) findViewById(R.id.ll_attender_has);
		mLvAttenderHas = (ListView) findViewById(R.id.lv_attender_has);
		mBtSel = (Button)findViewById(R.id.ibtn_select_all);
		mBtAdd = (Button)findViewById(R.id.ibtn_addto);
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
        
		
		mAdapter = new WaitoSelMemberListAdapter(this);
		mHasListAdapter = new WaitoSelMemberListAdapter(this);
		
		mHasListAdapter.setData(listHasSel);
		mLvAttenderHas.setAdapter(mHasListAdapter);
		
		
		mLlAttenderWaitto.setVisibility(View.VISIBLE);
		mLlAttenderHas.setVisibility(View.GONE);
		
		
		initListener();
		
		loadingdiag = new LoadingDialog(this);  
		loadingdiag.setCanceledOnTouchOutside(false); 
		
		
		GetDefaultAttender();
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
				Toast.makeText(CommonGroupAddMembersActivity.this, 
						getString(R.string.error_search_no_org), Toast.LENGTH_SHORT).show();
				return;
			}
		}else{
			if (!(orgname.length() != 0 | pplname.length() != 0)){
				Toast.makeText(CommonGroupAddMembersActivity.this, 
						getString(R.string.error_search_atleast_one), Toast.LENGTH_SHORT).show();
				return;
			}
		}
		
		searchPersionInfo(type, orgname, pplname);
	}
	
	private void searchPersionInfo(int type, String orgname, String pplname) {
		// TODO Auto-generated method stub
		String url = "MeetingManage/mobile/findPersionInfoExt.action?";
		int types[] = {2, 1, 0};
		url += "ttype=" + types[type];
		url += "&name=" + pplname;
		url += "&org=" + orgname;
		loadingdiag.setText(getString(R.string.loading));
		loadingdiag.show();
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();
				Toast.makeText(CommonGroupAddMembersActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                finish();  
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				// Toast.makeText(TopicSelectApplicatorActivity.this, new String(response)+"wangting"+response.toString(), 100).show();
				loadingdiag.hide();
				
				if (response.contains("用户未登陆")){
					Toast.makeText(CommonGroupAddMembersActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				llSearchBar.setVisibility(View.GONE);
				mLvAttenderWaitto.setVisibility(View.VISIBLE);		
				// loading.setVisibility(View.GONE);
				
				mLlAttenderWaitto.setVisibility(View.VISIBLE);
				mLlAttenderHas.setVisibility(View.GONE);
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
			               
			               ele.setSex(jsonObject.getString("sex"));
			               ele.setPhone(jsonObject.getString("phone"));
			               ele.setCardNo(jsonObject.getString("cardNo"));
			               ele.setCheck(false);
			               list.add(ele);
					 }
					
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(CommonGroupAddMembersActivity.this, getString(R.string.error_dataabout), Toast.LENGTH_SHORT).show();
					Intent data=new Intent();  
	                setResult(Constants.ERROR_DATA, data);  
	                finish();  
					
				}
				mAdapter.setData(list);
				mLvAttenderWaitto.setAdapter(mAdapter);
				
			}
		});		

	}

	private void initListener() {
		mTvAttenderHas.setOnClickListener(this);
		mTvAttenderWaitto.setOnClickListener(this);
		mBtSel.setOnClickListener(this);
		mBtAdd.setOnClickListener(this);
		mBtRemove.setOnClickListener(this);
		mBtSubmit.setOnClickListener(this);
		distance = SliderUtil.getDistance(this);

	}

	@Override
	public void setupView() {
		setContentView(R.layout.system_setting_common_group_addtwo);
	}

	public void clickPre(View v){
		IntentUtil.startActivity(this,CommonGroupAddOneActivity.class);
		this.finish();
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_attender_waitto:
			mLlAttenderWaitto.setVisibility(View.VISIBLE);
			mLlAttenderHas.setVisibility(View.GONE);
			SliderUtil.moveSlider(mIvUnderLine, distance, 0);
			// mAdapter.clear();
			
			break;
		case R.id.tv_attender_has:
			mLlAttenderWaitto.setVisibility(View.GONE);
			mLlAttenderHas.setVisibility(View.VISIBLE);
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
						if (ele1.getCardNo().equals(ele.getCardNo())
								){
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
			mLvAttenderHas.setAdapter(mHasListAdapter);
			mHasListAdapter.notifyDataSetChanged();
			
			mLlAttenderWaitto.setVisibility(View.GONE);
			mLlAttenderHas.setVisibility(View.VISIBLE);
			SliderUtil.moveSlider(mIvUnderLine, 0, distance);
			break;
		case R.id.ibtn_submit:
			addGroupMembers();
			break;
		case R.id.ibtn_remove:
			for (int i = listHasSel.size() - 1; i>=0; i--){
				if (listHasSel.get(i).isCheck){
					listHasSel.remove(i);
				}
			}

			mHasListAdapter.setData(listHasSel);
			mLvAttenderHas.setAdapter(mHasListAdapter);
			mHasListAdapter.notifyDataSetChanged();
			break;
		default:
			break;
		}

	}
	
    private void addGroupMembers() {
		// TODO Auto-generated method stub
    	if (listHasSel.size() == 0){
    		Toast.makeText(CommonGroupAddMembersActivity.this, getString(R.string.error_no_group_member_selected), Toast.LENGTH_SHORT).show();
    		return;
    	}
    	
        Gson gson = new Gson();
        String beanListToJson = gson.toJson(listHasSel);   
		String url = "MeetingManage/mobile/addGroupMembers.action";

		RequestParams params = new RequestParams();
		params.put("groupId", getIntent().getStringExtra("GrpId"));
		params.put("groupmember", beanListToJson);
		
		
		loadingdiag.setText(getString(R.string.uploading));
		loadingdiag.show();
		MeetingSystemClient.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();
				Toast.makeText(CommonGroupAddMembersActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onSuccess(int stautsCode, Header[] arg1, byte[] body) {
				loadingdiag.hide();
				String result = new String(body);
				
				if (result.contains("用户未登陆")){
					Toast.makeText(CommonGroupAddMembersActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				if (result.contains("true")) {
					Toast.makeText(CommonGroupAddMembersActivity.this,
							getString(R.string.result_success),
							Toast.LENGTH_SHORT).show();
					finish();
				} else {
					Toast.makeText(CommonGroupAddMembersActivity.this,
							getString(R.string.error_upload_failed),
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK ) {  
            finish();  
        }            
        return false;            
    }
    
	private void GetDefaultAttender() {
		String url = "MeetingManage/mobile/findMemberInfoDefault.action";
		loadingdiag.setText(getString(R.string.loading));
		loadingdiag.show();
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();
				Toast.makeText(CommonGroupAddMembersActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                Intent data=new Intent();  
                setResult(Constants.ERROR_NETWORK, data);  
                finish();  
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				// Toast.makeText(TopicSelectApplicatorActivity.this, new String(response)+"wangting"+response.toString(), 100).show();
				loadingdiag.hide();
				
				if (response.contains("用户未登陆")){
					Toast.makeText(CommonGroupAddMembersActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				mLvAttenderWaitto.setVisibility(View.VISIBLE);		
				// loading.setVisibility(View.GONE);
				
				
				list = new ArrayList<Element>(); 
				
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
			               
			               ele.setSex(jsonObject.getString("sex"));
			               ele.setPhone(jsonObject.getString("phone"));
			               ele.setCardNo(jsonObject.getString("cardNo"));
			               ele.setCheck(false);
			               list.add(ele);
					 }
					
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(CommonGroupAddMembersActivity.this, getString(R.string.error_dataabout), Toast.LENGTH_SHORT).show();
					Intent data=new Intent();  
	                setResult(Constants.ERROR_DATA, data);  
	                finish();  
					
				}
				mAdapter.setData(list);
				mLvAttenderWaitto.setAdapter(mAdapter);
				
			}
		});		
	}
};

class WaitoSelMemberListAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<Element> data = null;
	
	public WaitoSelMemberListAdapter() {

	}
	
	public void setData(ArrayList<Element> list2) {
		// TODO Auto-generated method stub
		data = list2;
	}

	public WaitoSelMemberListAdapter(Context mcontext) {
		this.mContext = mcontext;
		mInflater = LayoutInflater.from(mContext);
	}

	public void checkAll() {
		for (Element ele : data) {
			ele.isCheck = true;
		}
		notifyDataSetChanged();
	}

	public void noCheckAll() {
		for (Element ele : data) {
			ele.isCheck = false;
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return data == null ? 0 : data.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {

			convertView = mInflater.inflate(
					R.layout.topic_select_attender_item, null);
			viewHolder = new ViewHolder();
			viewHolder.checkBox = (CheckBox) convertView
					.findViewById(R.id.checkBox1);
			viewHolder.name = (TextView)convertView.findViewById(R.id.name);  
			viewHolder.org = (TextView)convertView.findViewById(R.id.org);  
			viewHolder.post = (TextView)convertView.findViewById(R.id.post);
			
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.name.setText((String)data.get(position).getName());  
		viewHolder.org.setText((String)data.get(position).getOrg());  
		viewHolder.post.setText((String)data.get(position).getPost());  
         
		final Element ele = data.get(position);
		viewHolder.checkBox.setChecked(ele.isCheck);
		viewHolder.checkBox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (ele.isCheck) {
					ele.isCheck = false;
				} else {
					ele.isCheck = true;
				}
			}
		});
		return convertView;
	}
	
	class ViewHolder {
		public CheckBox checkBox;
	    public TextView name;  
	    public TextView org;  
	    public TextView post;  
	}
}




