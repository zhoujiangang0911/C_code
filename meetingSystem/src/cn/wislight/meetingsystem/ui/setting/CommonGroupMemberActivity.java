package cn.wislight.meetingsystem.ui.setting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.domain.GroupElement;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.Element;
import cn.wislight.meetingsystem.util.IntentUtil;
import cn.wislight.meetingsystem.util.LoadingDialog;
import cn.wislight.meetingsystem.util.MeetingSystemClient;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * @author Administrator
 * 群组设置
 */
public class CommonGroupMemberActivity extends BaseActivity {
	private ListView member_listview;
	LoadingDialog loadingdiag;
	private ArrayList<Element> list;
	private GroupMemberListAdapter mAdapter;

	private String groupId;
	
	@Override
	public void initView() {
		groupId = getIntent().getStringExtra(Constants.ID);
		
		member_listview=(ListView)findViewById(R.id.lv_group_memeber);
		loadingdiag = new LoadingDialog(this);  
		loadingdiag.setCanceledOnTouchOutside(false); 
		
		mAdapter = new GroupMemberListAdapter(this);

		GetGroupMember();
	}
	
	@Override
	public void setupView() {
		setContentView(R.layout.system_setting_common_group_member);
	}

	protected void onResume(){
		GetGroupMember();
		super.onResume();
	}
	
	public void clickRemove(View view){
		removeMember();
	}

	public void clickAddMember(View view){
		Intent intent = new Intent();
		intent.putExtra("GrpId", groupId);
		IntentUtil.startActivity(this, CommonGroupAddMembersActivity.class, intent);
	}

	private void removeMember() {
		// TODO Auto-generated method stub
		String members = "";
		int count = 0;
		int length = list.size();
		for (int i=0; i<length; i++){
			if ( list.get(i).isCheck){
				members += list.get(i).id;
				members += ",";
				count++;
			}
		}
		members += "0";
		if (count == 0){
			Toast.makeText(CommonGroupMemberActivity.this, getString(R.string.error_no_member_selected), Toast.LENGTH_SHORT).show();
			return;
		}
		
		String url = "MeetingManage/mobile/deleteGroupMember.action?member_ids="+ members;
		System.out.println("wangting:"+url);
		loadingdiag.setText(getString(R.string.deleting));
		loadingdiag.show();
		
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler(){
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();
				Toast.makeText(CommonGroupMemberActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                finish();  
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				loadingdiag.hide();
				if (response.contains("用户未登陆")){
					Toast.makeText(CommonGroupMemberActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				if(response.contains("true")){
					Toast.makeText(CommonGroupMemberActivity.this, "删除群组人员成功", Toast.LENGTH_SHORT).show();
					
					for (int i = list.size() - 1; i >= 0; i--){
						if (list.get(i).isCheck){
							list.remove(i);
						}
					}
					mAdapter.notifyDataSetChanged();
				}else{
					Toast.makeText(CommonGroupMemberActivity.this, "删除群组人员失败", Toast.LENGTH_SHORT).show();
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
    
	public void clickDeleteGroup(View view){
		new AlertDialog.Builder(this).setTitle("删除此群组?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				
				String url = "MeetingManage/mobile/deleteGroupById.action?id="+groupId;
				System.out.println("wangting:"+url);
				MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler(){
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] body,
							Throwable error) {
						loadingdiag.hide();
						Toast.makeText(CommonGroupMemberActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
		                finish();  
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] body) {
						String response = new String(body);
						loadingdiag.hide();
						
						if (response.contains("用户未登陆")){
							Toast.makeText(CommonGroupMemberActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
							return;
						}	
						
						if(response.contains("true")){
							Toast.makeText(CommonGroupMemberActivity.this, "删除群组成功", Toast.LENGTH_SHORT).show();
				            finish();  
						}else{
							Toast.makeText(CommonGroupMemberActivity.this, "删除群组失败", Toast.LENGTH_SHORT).show();
						}
					}
						
				});		
				
			}
		})
		.setNegativeButton("取消", null).show();
		

	}
	
    private void GetGroupMember() {
		String url = "MeetingManage/mobile/findGroupMembersByGrpid.action?id=";
		url += getIntent().getStringExtra(Constants.ID);
		loadingdiag.setText(getString(R.string.loading));
		loadingdiag.show();
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();
				Toast.makeText(CommonGroupMemberActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                finish();  
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				// Toast.makeText(TopicSelectApplicatorActivity.this, new String(response)+"wangting"+response.toString(), 100).show();
				loadingdiag.hide();
				if (response.contains("用户未登陆")){
					Toast.makeText(CommonGroupMemberActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				// loading.setVisibility(View.GONE);
				
				
				list = new ArrayList<Element>(); 
				
				try {
					 JSONArray jsonArray=new JSONObject(response).getJSONArray("memberList");
					  
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
					Toast.makeText(CommonGroupMemberActivity.this, getString(R.string.error_dataabout), Toast.LENGTH_SHORT).show();
	                finish();  
					
				}
				mAdapter.setData(list);
				member_listview.setAdapter(mAdapter);
				
			}
		});		
	}
}


class GroupMemberListAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<Element> data = null;
	
	public GroupMemberListAdapter() {

	}
	
	public void setData(ArrayList<Element> list2) {
		// TODO Auto-generated method stub
		data = list2;
	}

	public GroupMemberListAdapter(Context mcontext) {
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
		ViewHolderMember viewHolder;
		if (convertView == null) {

			convertView = mInflater.inflate(
					R.layout.system_setting_group_member_list_item, null);
			viewHolder = new ViewHolderMember();

			viewHolder.name = (TextView)convertView.findViewById(R.id.name);  
			viewHolder.org = (TextView)convertView.findViewById(R.id.org);  
			viewHolder.post = (TextView)convertView.findViewById(R.id.post);
			viewHolder.checkBox = (CheckBox)convertView.findViewById(R.id.checkBox1);

			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolderMember) convertView.getTag();
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
}

class ViewHolderMember {
	public CheckBox checkBox;
    public TextView name;  
    public TextView org;  
    public TextView post;  
}
