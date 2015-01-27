package cn.wislight.meetingsystem.ui.conference;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpResponseHandler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.IntentUtil;
import cn.wislight.meetingsystem.util.LoadingDialog;
import cn.wislight.meetingsystem.util.MeetingSystemClient;
import cn.wislight.meetingsystem.util.Variables;

/**
 * @author Administrator 会议发起
 */
public class ConChangeOneActivity extends BaseActivity {
	private SharedPreferences conference;
	private EditText edittextConferenceTitle;
	private EditText edittextConferenceContent;
	private TextView edittextConferenceAddress;
	private TextView textConferenceType;

	private String meetNo;
	private String meetId;
	private Boolean bLoadData;
	private LoadingDialog loadingdiag;

	@Override
	public void initView() {
		conference = this.getSharedPreferences("conference_change_"
				+ Variables.loginname, MODE_PRIVATE);
		meetNo = getIntent().getStringExtra(Constants.MEET_NO);
		meetId = getIntent().getStringExtra(Constants.ID);
		bLoadData = getIntent().getBooleanExtra("loaddata", true);
		loadingdiag = new LoadingDialog(this);
		loadingdiag.setCanceledOnTouchOutside(false);

		edittextConferenceTitle = (EditText) findViewById(R.id.conf_conftitle);
		edittextConferenceContent = (EditText) findViewById(R.id.conf_confcontent);
		edittextConferenceAddress = (TextView) findViewById(R.id.conf_confaddress);
		textConferenceType = (TextView) findViewById(R.id.conf_conftype);
		if (bLoadData){
			Editor editor = conference.edit();
			editor.clear();
			editor.commit();

			getMeetInfo();

		}else{
			updateUi();
		}
		
	}

	@Override
	public void setupView() {
		setContentView(R.layout.conference_change_one);
	}

	private void updateUi(){
		edittextConferenceTitle.setText(conference.getString(
				"conference_title", ""));
		edittextConferenceContent.setText(conference.getString(
				"conference_content", ""));
		edittextConferenceAddress.setText(conference.getString(
				"conference_address", ""));
		textConferenceType.setText(getString(R.string.conf_type) + ":    "
				+ conference.getString("conference_conftype", ""));
	}
	
	private void getMeetInfo() {

		loadingdiag.setText(getString(R.string.loading));
		loadingdiag.show();

		String url = "MeetingManage/mobile/goEditMeet.action?meetId=" + meetId
				+ "&mno=" + meetNo;
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();
				Toast.makeText(ConChangeOneActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
				Intent data = new Intent();
				setResult(Constants.ERROR_NETWORK, data);
				finish();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				// Toast.makeText(TopicSelectApplicatorActivity.this, new
				// String(response)+"wangting"+response.toString(), 100).show();

				loadingdiag.hide();
				
				if (response.contains("用户未登陆")){
					Toast.makeText(ConChangeOneActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				try {
					JSONObject jso = new JSONObject(response)
							.getJSONObject("meetEntity");
					String title = jso.getString("title");
					String content = jso.getString("remark");
					String address = jso.getString("address");
					String confType = jso.getString("typename");
					String startDate = jso.getString("starTime");
					String endDate = jso.getString("endTime");
					String checkerName = jso.getString("checkusername");
					String ctrlName = jso.getString("managername");
					String AttenderName = jso.getString("meetjoinperson");
					
					String addressId = jso.getString("addressId");
					String CtrllorId = jso.getString("managerid");
					String ChekerId = jso.getString("checkuser");
					String typeId = jso.getString("typeid");
					
					String meet_no = jso.getString("meetno");
					String inmeetno = jso.getString("inmeetno");

					Editor editor = conference.edit();
					editor.putString("conference_title", title);
					editor.putString("conference_conftype", confType);
					editor.putString("conference_content", content);
					editor.putString("conference_address", address);
					editor.putString("conference_joinmember_name", AttenderName);
					editor.putString("conference_controlmember",ctrlName);
					editor.putString("conference_accessmember",checkerName);
					editor.putString("conference_starttime",startDate);
					editor.putString("conference_endtime",endDate);
					
					editor.putString("conference_address_id", addressId);
					editor.putString("conference_conftype_id", typeId);
	        		editor.putString("conference_controlmember_id", CtrllorId);
	        		editor.putString("conference_accessmember_id", ChekerId);

	        		editor.putString("meetNoZd", meet_no);
	        		editor.putString("inmeetno", inmeetno);

					editor.commit();
					updateUi();
					JSONArray jsonArray1 = new JSONObject(response)
							.getJSONArray("meetpersonlist");
					/*
					 * Map<String, Object> map1;
					 * 
					 * for(int i=0;i<jsonArray1.length();i++){ JSONObject
					 * jsonObject=(JSONObject)jsonArray1.get(i); map1 = new
					 * HashMap<String, Object>(); map1.put("starttime",
					 * jsonObject.getString("starttime")); map1.put("endtime",
					 * jsonObject.getString("endtime")); map1.put("keywords",
					 * jsonObject.getString("keywords")); map1.put("summary",
					 * jsonObject.getString("summary")); map1.put("id",
					 * jsonObject.getString("id")); map1.put("topicno",
					 * jsonObject.getString("topicno"));
					 * 
					 * }
					 */

				} catch (JSONException e) {
					e.printStackTrace();
					loadingdiag.hide();
					Toast.makeText(ConChangeOneActivity.this,
							getString(R.string.error_dataabout),
							Toast.LENGTH_SHORT).show();
					finish();

				}
			}
		});
	}

	protected void onPause() {
		super.onPause();
		Editor editor = conference.edit();
		editor.putString("conference_title", edittextConferenceTitle.getText()
				.toString());
		editor.putString("conference_content", edittextConferenceContent
				.getText().toString());
		// editor.putString("conference_conftype",
		// textConferenceType.getText().toString());
		editor.commit();
	}

	public void clickConfType(View view) {
		System.out.println("wangting:clickConfType");
		IntentUtil.startActivityForResult(this,
				ConfSelectConfTypeActivity.class, Constants.CODE_CONF_CONFTYPE);

	}

	public void clickSelectMeetHouse(View view) {
		IntentUtil.startActivityForResult(this, ConfSelectHourseActivity.class,
				Constants.CODE_CONF_HOURSE);
	}

	public void clickNext(View view) {
		if (edittextConferenceTitle.getText().toString().length() == 0) {
			Toast.makeText(ConChangeOneActivity.this,
					getString(R.string.error_no_conf_title), Toast.LENGTH_SHORT)
					.show();
			return;
		}

		if (edittextConferenceContent.getText().toString().length() == 0) {
			Toast.makeText(ConChangeOneActivity.this,
					getString(R.string.error_no_conf_content),
					Toast.LENGTH_SHORT).show();
			return;
		}

		if (edittextConferenceAddress.getText().toString().length() == 0) {
			Toast.makeText(ConChangeOneActivity.this,
					getString(R.string.error_no_conf_addr), Toast.LENGTH_SHORT)
					.show();
			return;
		}

		if (conference.getString("conference_conftype", "").length() == 0) {
			Toast.makeText(ConChangeOneActivity.this,
					getString(R.string.error_no_conf_type), Toast.LENGTH_SHORT)
					.show();
			return;
		}

		Intent intent = new Intent();
		intent.putExtra(Constants.MEET_NO, meetNo);
		intent.putExtra(Constants.ID, meetId);
		IntentUtil.startActivity(this, ConChangeTwoActivity.class, intent);
		finish();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 可以根据多个请求代码来作相应的操作
		if (Constants.CODE_CONF_CONFTYPE == requestCode) {
			if (Constants.OK == resultCode) {
				if (data != null) {
					String typename = data.getExtras().getString("typename");
					String typeid = data.getExtras().getString("id");
					Editor editor = conference.edit();
					editor.putString("conference_conftype", typename);
					editor.putString("conference_conftype_id", typeid);
					editor.commit();
					textConferenceType.setText(getString(R.string.conf_type)
							+ ":    " + typename);
				}
			}
		}
		if (Constants.CODE_CONF_HOURSE == requestCode
				&& Constants.OK == resultCode && data != null) {
			String fullAddr = data.getExtras().getString("fullAddr");
			String addrId = data.getExtras().getString("id");
			Editor editor = conference.edit();
			editor.putString("conference_address", fullAddr);
			editor.putString("conference_address_id", addrId);
			editor.commit();

			edittextConferenceAddress.setText(fullAddr);
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
}
