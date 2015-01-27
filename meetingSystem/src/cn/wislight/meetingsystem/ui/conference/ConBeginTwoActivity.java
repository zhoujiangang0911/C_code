package cn.wislight.meetingsystem.ui.conference;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.DpTpActivity;
import cn.wislight.meetingsystem.util.Element;
import cn.wislight.meetingsystem.util.FileChooserActivity;
import cn.wislight.meetingsystem.util.IntentUtil;
import cn.wislight.meetingsystem.util.LoadingDialog;
import cn.wislight.meetingsystem.util.MeetingSystemClient;
import cn.wislight.meetingsystem.util.Variables;

/**
 * @author Administrator
 *	会议发起
 */
public class ConBeginTwoActivity extends BaseActivity implements OnClickListener{
	private SharedPreferences conference;
	
	private TextView textApplicateJoinMember;
	private TextView textApplicateControlMember;
	private TextView textApplicateAccessMember;	
	private TextView textStarttime;
	private TextView textEndtime;
	private LoadingDialog loadingdiag;
	
	/* for upload */
	private ArrayList<UpLoadBundle> uploadList = null;
	private LinearLayout llConfAppendix;
	private int upLoadIndex;
	private TextView tvAppendix;
	private TextView tvAppendixUploaded;
	private Button btnUploadAttachment;
	private String no="";
	boolean bclickUploadFile = false;
	/**/ 
	
	@Override
	public void initView() {
		conference = this.getSharedPreferences("conference"+ Variables.loginname, MODE_PRIVATE);
		
		textApplicateJoinMember = (TextView)findViewById(R.id.conf_joinmeb);
		textApplicateControlMember = (TextView)findViewById(R.id.conf_contromeb);
		textApplicateAccessMember = (TextView)findViewById(R.id.conf_accessmeb);
		textStarttime = (TextView)findViewById(R.id.conf_starttime);
		textEndtime = (TextView)findViewById(R.id.conf_endtime);
		
		
		findViewById(R.id.conf_btn_up).setOnClickListener(this);
		findViewById(R.id.conf_btn_next).setOnClickListener(this);
		findViewById(R.id.conf_joinmeb).setOnClickListener(this);
		findViewById(R.id.conf_contromeb).setOnClickListener(this);
		findViewById(R.id.conf_accessmeb).setOnClickListener(this);
		
		textApplicateJoinMember.setText(getString(R.string.conf_joinmeb) + ":    " + conference.getString("conference_joinmember_name", ""));
		textApplicateControlMember.setText(getString(R.string.conf_contromeb) + ":    " + conference.getString("conference_controlmember", ""));
		textApplicateAccessMember.setText(getString(R.string.conf_accessmeb) + ":    " + conference.getString("conference_accessmember", ""));
		
		if (!"".equals(conference.getString("conference_starttime", ""))){
			textStarttime.setText(conference.getString("conference_starttime", ""));	
		}else {
			textStarttime.setText(getString(R.string.conf_starttime));
		}
		
		if (!"".equals(conference.getString("conference_endtime", ""))){
			textEndtime.setText(conference.getString("conference_endtime", ""));
		}else {
			textEndtime.setText(getString(R.string.conf_endtime));
		}
		
		no = conference.getString("attachment_no", "");
		String confAttachment = conference.getString("attachment_list", "");
		if (null != confAttachment && !"".equals(confAttachment)){
			Gson gson = new Gson();
			uploadList = gson.fromJson(confAttachment, new TypeToken<List<UpLoadBundle>>(){}.getType());
		}else{
	        uploadList = new ArrayList<UpLoadBundle>();		
		}
		
		loadingdiag = new LoadingDialog(this);  
		loadingdiag.setCanceledOnTouchOutside(false); 
		loadingdiag.setText(getString(R.string.uploading));
		
		/* for upload */
		btnUploadAttachment = (Button)findViewById(R.id.btn_upload_attachment);
		llConfAppendix = (LinearLayout) findViewById(R.id.ll_conf_appendix);
		tvAppendix = (TextView)findViewById(R.id.tv_appendix);
		tvAppendixUploaded = (TextView)findViewById(R.id.tv_appendix_uploaded);
		if (null != uploadList){
			updateAppendixText();
		}
		
	}

	@Override
	public void setupView() {
		setContentView(R.layout.conference_begin_two);
	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.conf_btn_up:
			IntentUtil.startActivity(this,ConBeginOneActivity.class);
			finish();
			break;
		case R.id.conf_btn_next:
			clickNext();

			break;	
	
		case R.id.conf_joinmeb:
			IntentUtil.startActivityForResult(this, ConfSelectJoinMemberActivity.class, Constants.CODE_CONF_JOINMEMBER);

			break;	
		case R.id.conf_contromeb:
			Intent intent1 = new Intent();
			intent1.putExtra("type", "0");
			IntentUtil.startActivityForResult(this, ConfSelectApplicatorActivity.class, Constants.CODE_CONF_CONTROLMEMBER, intent1);
			break;	
		case R.id.conf_accessmeb:
			Intent intent2 = new Intent();
			intent2.putExtra("type", "1");
			IntentUtil.startActivityForResult(this, ConfSelectApplicatorActivity.class, Constants.CODE_CONF_ACCESSMEMBER, intent2);

			break;	
		default:
			break;
		}
	}

	private void clickNext() {
		// TODO Auto-generated method stub
		/*
		if (conference.getString("conference_joinmember_name", "").length() == 0)
		{
			Toast.makeText(ConBeginTwoActivity.this, getString(R.string.error_no_conf_joinmember), Toast.LENGTH_SHORT).show();
		    return;
		}
		*/
		if (conference.getString("conference_controlmember", "").length() == 0){
			Toast.makeText(ConBeginTwoActivity.this, getString(R.string.error_no_conf_ctrlmember), Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (conference.getString("conference_accessmember", "").length() == 0){
			Toast.makeText(ConBeginTwoActivity.this, getString(R.string.error_no_conf_accessmember), Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (conference.getString("conference_starttime", "").length() == 0){
			Toast.makeText(ConBeginTwoActivity.this, getString(R.string.error_no_conf_starttime), Toast.LENGTH_SHORT).show();
			return;			
		}
		
		if (conference.getString("conference_endtime", "").length() == 0){
			Toast.makeText(ConBeginTwoActivity.this, getString(R.string.error_no_conf_enddtime), Toast.LENGTH_SHORT).show();
			return;			
		}
		if (checkTime() == false){
			return;
		}
		if (conference.getString("conference_endtime", "").compareTo(conference.getString("conference_starttime", "")) < 0){
			Toast.makeText(ConBeginTwoActivity.this, getString(R.string.error_no_conf_vilid_time), Toast.LENGTH_SHORT).show();
			return;			
		}
		
		if (null != uploadList)
		{
			int count = 0;
			for (UpLoadBundle ub: uploadList )
			{
				if (ub.uploadState <= 0){
					count ++;
				}
			}
			if (count > 0){
				Toast.makeText(ConBeginTwoActivity.this, getString(R.string.error_attachment_unuploaded), Toast.LENGTH_SHORT).show();
				return;			
			}
		}
		
		/* save the conference start and end time */		
		SharedPreferences data = this.getSharedPreferences(Constants.CONF_TIME_NODE, MODE_PRIVATE);
		Editor editor = data.edit();
		String start = conference.getString("conference_starttime", "");
		String end = conference.getString("conference_endtime", "");
		editor.putString(Constants.CONF_START_TIME, start);
		editor.putString(Constants.CONF_END_TIME, end);
		editor.commit();
		
		IntentUtil.startActivity(this,ConBeginThreeActivity.class);
		finish();
	}
	private boolean checkTime(){
		boolean result = true;
		String topic_start_date = conference.getString("conference_starttime", "");

		String starttime = "";
        starttime = topic_start_date.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");

        //String topic_end_date = topic.getString(Constants.TOPIC_END_DATE, "");
        //String endtime = "";
        //endtime = topic_end_date.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");

        
        Calendar calendar = Calendar.getInstance();
        Integer current_year=calendar.get(Calendar.YEAR);
        Integer current_monthOfYear = calendar.get(Calendar.MONTH)+1;
        Integer current_dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        Integer current_hour = calendar.get(Calendar.HOUR_OF_DAY);
        Integer current_minute = calendar.get(Calendar.MINUTE); 
        String currenttime = formatDateTime(current_year,current_monthOfYear,current_dayOfMonth,current_hour,current_minute);
        //System.out.println("wangting current time="+currenttime);
        //System.out.println("wangting start time="+starttime);
        //System.out.println("wangting end time="+endtime);
        
        if (starttime.compareTo(currenttime) <= 0){
        	Toast.makeText(ConBeginTwoActivity.this, getString(R.string.error_no_topic_time_too_small), Toast.LENGTH_SHORT).show();
			return false;
        }   
        /*
        if (endtime.compareTo(starttime) != 1){
        	Toast.makeText(TopicAddTwoActivity.this, getString(R.string.error_no_topic_endtime_too_small), Toast.LENGTH_SHORT).show();
			return false;
        }
         */

		return result;
	}
	private String formatDateTime(int year,int month, int date, int hour, int minute){
        String datetime = String.format("%1$04d", year)
        		+ String.format("%1$02d", month) 
        		+ String.format("%1$02d", date)
        		+ String.format("%1$02d", hour)
        		+ String.format("%1$02d",minute) 
        		+ "00";          
        return datetime;
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 可以根据多个请求代码来作相应的操作
		if (Constants.CODE_CONF_JOINMEMBER == requestCode && data != null) {

        	if (Constants.OK == resultCode){
        		ArrayList<Element> selList = (ArrayList<Element>)data.getSerializableExtra(Constants.CONF_JOINMEMBER_LIST);
        		int length = selList.size();

        		String conference_joinmember = "";     
        		String conference_joinmember_name = "";
        		String conference_joinmember_list="[";
        		for (int i=0; i<length; i++){
        			Element ele = selList.get(i);
        			if (i == length-1 ){
        				conference_joinmember += ele.getType()+","+ele.getId();                       			
            			conference_joinmember_list +="{\"name\":\""+ele.getName()+"\",\"type\":\""+ele.getType()+"\",\"id\":\""+ele.getId()+"\",\"orgnizename\":\""+ele.getOrg()+"\",\"position\":\""+ele.getPost()+"\"}";
            			conference_joinmember_name += ele.getName() ;
        			} else {
        				conference_joinmember += ele.getType()+","+ele.getId()+";";  	        			     			
	        			conference_joinmember_list +="{\"name\":\""+ele.getName()+"\",\"type\":\""+ele.getType()+"\",\"id\":\""+ele.getId()+"\",\"orgnizename\":\""+ele.getOrg()+"\",\"position\":\""+ele.getPost()+"\"},";
	        			conference_joinmember_name += ele.getName() + ",";
        			}
        		}        		
        		conference_joinmember_list +="]";
        		
        		System.out.println("wangting:conference_joinmember_list="+conference_joinmember_list);
        		conference_joinmember_list = conference_joinmember_list.replaceAll("\"", "'");
        		Editor editor = conference.edit();
        		editor.putString("conference_joinmember", conference_joinmember);
        		editor.putString("conference_joinmember_list", conference_joinmember_list);
        		editor.putString("conference_joinmember_name", conference_joinmember_name);
        		editor.commit();
        		System.out.println("wangting:conference_joinmember="+conference_joinmember);
        		System.out.println("wangting:conference_joinmember_list="+conference_joinmember_list);
        		textApplicateJoinMember.setText(getString(R.string.conf_joinmeb) + ":    "  + conference_joinmember_name);
        	
        		loadingdiag.show();
        		
        		String url="MeetingManage/mobile/addMeetPersonToTmp.action";
        		RequestParams params = new RequestParams();
        		params.put("insertype", "par");
        		params.put("type", 0);
        		params.put("sInfo", conference_joinmember);
        		
        		MeetingSystemClient.post(url, params, new AsyncHttpResponseHandler()
        		{
        			@Override
        			public void onFailure(int arg0, Header[] arg1, byte[] body,
        					Throwable error) {
        				loadingdiag.hide();
        				Toast.makeText(ConBeginTwoActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
        				System.out.println("wangting, add to meet_attendrecordtemp error:"+error.getMessage());				
        			}

        			@Override
        			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
        				String response = new String(body);
        				loadingdiag.hide();
        				
						if (response.contains("用户未登陆")){
							Toast.makeText(ConBeginTwoActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
							return;
						}	
						
        				String meetnumber ="";
        				try {
							JSONObject result = new JSONObject(response);
							meetnumber = result.getString("meetNo");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
        				Editor editor = conference.edit();
                		editor.putString("conference_meetnumber", meetnumber);
                		editor.commit();
        				System.out.println("wangting, add to meet_attendrecordtemp success:"+response);
        			}
        		});	
        	}
		}
		if (Constants.CODE_CONF_CONTROLMEMBER == requestCode && data != null ) {
			if (Constants.OK == resultCode) {
				String name = data.getExtras().getString(Constants.NAME);
				textApplicateControlMember
						.setText(getString(R.string.conf_contromeb) + ":    "
								+ name);
				String id = data.getExtras().getString(Constants.ID);
        		Editor editor = conference.edit();
        		editor.putString("conference_controlmember", name);
        		editor.putString("conference_controlmember_id", id);
        		editor.commit();
			}
		}
		if (Constants.CODE_CONF_ACCESSMEMBER == requestCode && data != null) {
			if (Constants.OK == resultCode) {
				String name = data.getExtras().getString(Constants.NAME);
				textApplicateAccessMember
						.setText(getString(R.string.conf_accessmeb) + ":    "
								+ name);
				String id = data.getExtras().getString(Constants.ID);
        		Editor editor = conference.edit();
        		editor.putString("conference_accessmember", name);
        		editor.putString("conference_accessmember_id", id);
        		editor.commit();
			}
		}
        if(Constants.CODE_START_TIME == requestCode && data != null )  
        {  
        	if (Constants.OK == resultCode && null != data){
        		String dt = data.getExtras().getString(Constants.DATETIME);  
        		textStarttime.setText(dt);

        		Editor editor = conference.edit();
        		editor.putString("conference_starttime", dt);        		
        		editor.commit();
        	}
        }
        
        if(Constants.CODE_END_TIME == requestCode && data != null)  
        {  
        	if (Constants.OK == resultCode && null != data){
        		String dt = data.getExtras().getString(Constants.DATETIME);  
        		textEndtime.setText(dt);
        		
        		Editor editor = conference.edit();
        		editor.putString("conference_endtime", dt);        		
        		editor.commit();
        	}
        }
        
        if(resultCode == RESULT_CANCELED){
			return ;
		}
		if(resultCode == RESULT_OK && requestCode == Constants.CODE_FILE_SELECT_COMMON){
			//获取路径名
			String filePath = data.getStringExtra(Constants.EXTRA_FILE_CHOOSER);
			if(filePath != null){
				File f = new File(filePath);
				if (f.length() > Constants.MAX_UPLOAD_FILE_SIZE){
					Toast.makeText(ConBeginTwoActivity.this,
							getString(R.string.error_filesize_toolarge),
							Toast.LENGTH_SHORT).show();
					
					return;
				}
				
				UpLoadBundle b = new UpLoadBundle();
				String fileName = filePath.substring(filePath.lastIndexOf("/")+1); 
				b.filename = fileName;
				b.filepath = filePath;
				b.uploadState = 0;
				addToUploadList(b);
				updateAppendixText();
				savePreference();
			}
		}
		if(resultCode == RESULT_OK && requestCode == Constants.CODE_FILE_SELECT){
			//获取路径名
			Uri uri = data.getData();
			String filePath = getPath(this, uri);
			String fileName = filePath.substring(filePath.lastIndexOf("/")+1); 
			
			File f = new File(filePath);
			if (f.length() > Constants.MAX_UPLOAD_FILE_SIZE){
				Toast.makeText(ConBeginTwoActivity.this,
						getString(R.string.error_filesize_toolarge),
						Toast.LENGTH_SHORT).show();
				
				return;
			}
			
			UpLoadBundle b = new UpLoadBundle();
			b.filename = fileName;
			b.filepath = filePath;
			b.uploadState = 0;
			addToUploadList(b);
			savePreference();
			updateAppendixText();
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void clickStarttime(View view){
		Intent starttime = new Intent();
		String tempStarttime = "";
		if (textStarttime.getText() != null){
			tempStarttime = textStarttime.getText().toString();
		}
		starttime.putExtra(Constants.INIT_TIME, tempStarttime);
		IntentUtil.startActivityForResult(this, DpTpActivity.class, Constants.CODE_START_TIME, starttime);

		//IntentUtil.startActivityForResult(this, DpTpActivity.class, Constants.CODE_START_TIME);
	}
	public void clickEndtime(View view){
		Intent endtime = new Intent();
		String tempEndtime = "";
		if (textEndtime.getText() != null){
			tempEndtime = textEndtime.getText().toString();
		}
		endtime.putExtra(Constants.INIT_TIME, tempEndtime);
		IntentUtil.startActivityForResult(this, DpTpActivity.class, Constants.CODE_END_TIME,endtime);

		//IntentUtil.startActivityForResult(this, DpTpActivity.class, Constants.CODE_END_TIME);
	}
	
	
	public void clickUpLoadFile(View view){
		
		int count = 0;
		for (UpLoadBundle b: uploadList){
			if (b.uploadState <= 0){
				count ++;
			}
		}
		
		if (count == 0){
			Toast.makeText(ConBeginTwoActivity.this,
					getString(R.string.error_no_document), Toast.LENGTH_SHORT).show();
			return;
		}
		
		bclickUploadFile = true;
		beginUploadFile();
	}

	private void beginUploadFile(){
		upLoadIndex = 0;
		loadingdiag.setText(getString(R.string.uploading));
		loadingdiag.show();
		upLoadFile();
	}
	
	private void upLoadFile() {
		// TODO Auto-generated method stub
		
		updateAppendixText();
		
		if ( uploadList.get(upLoadIndex).uploadState > 0){
			upLoadIndex++;
			if (upLoadIndex > uploadList.size() - 1){
				// 上传完毕
				upLoadIndex = 0;
				updateAppendixText();
				loadingdiag.hide();
				if (bclickUploadFile){
					Toast.makeText(ConBeginTwoActivity.this,
							getString(R.string.result_success), Toast.LENGTH_SHORT).show();		
					return;	
				} else {
					return;
				}
				
			} else {
				upLoadFile();
				return;
			}
			
		}
		
		loadingdiag.setText(getString(R.string.uploading) + ":  " 
				+ uploadList.get(upLoadIndex).filename);
		loadingdiag.show();

		String url = "MeetingManage/mobile/MobileUpLoadFileServlet?no=";
		url += no;
		RequestParams params = new RequestParams();
		File file = new File(uploadList.get(upLoadIndex).filepath);
		try {
			params.put("filedata", file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Toast.makeText(ConBeginTwoActivity.this,
					getString(R.string.error_fileabout),
					Toast.LENGTH_SHORT).show();
			return;
		}

		
		MeetingSystemClient.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();
				Toast.makeText(ConBeginTwoActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onSuccess(int stautsCode, Header[] arg1, byte[] body) {
				String result = new String(body);
				
				if (result.contains("用户未登陆")){
					Toast.makeText(ConBeginTwoActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				if (result.contains("success")) {
					
					try {
						JSONObject jso = new JSONObject(result);
						no = jso.getString("no");
						if (null == no || "".equals(no)){
							Toast.makeText(ConBeginTwoActivity.this,
									getString(R.string.error_dataabout),
									Toast.LENGTH_SHORT).show();
							return;
						}
						
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Toast.makeText(ConBeginTwoActivity.this,
								getString(R.string.error_dataabout),
								Toast.LENGTH_SHORT).show();
						return;
					}
					
					uploadList.get(upLoadIndex).uploadState = 1;
					savePreference();
					upLoadFile();
				} else {
					loadingdiag.hide();
					if (result.contains("errorMessage")){
						try {
							JSONObject jsob =  new JSONObject(result);
							String msg = jsob.getString("errorMessage");
							Toast.makeText(ConBeginTwoActivity.this,
									msg,
									Toast.LENGTH_SHORT).show();
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Toast.makeText(ConBeginTwoActivity.this,
									getString(R.string.error_upload_failed),
									Toast.LENGTH_SHORT).show();
						}
						
					}else{
						Toast.makeText(ConBeginTwoActivity.this,
							getString(R.string.error_upload_failed),
							Toast.LENGTH_SHORT).show();
					}
					
				}
			}
		});
		
	}

	public void clickSelectMediaFile(View view){
	    Intent intent = new Intent(Intent.ACTION_GET_CONTENT); 
	    intent.setType("*/*"); 
	    intent.addCategory(Intent.CATEGORY_OPENABLE);
	 
	    try {
	        startActivityForResult( Intent.createChooser(intent, "选择文件"), Constants.CODE_FILE_SELECT);
	    } catch (android.content.ActivityNotFoundException ex) {
	        Toast.makeText(this, "无匹配的文件选择器",  Toast.LENGTH_SHORT).show();
	    }
	}
	
	public void clickSelectOrdinaryFile(View view){
    	if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		    startActivityForResult(new Intent(this , FileChooserActivity.class) , Constants.CODE_FILE_SELECT_COMMON);
    	else
    		Toast.makeText(this, getText(R.string.sdcard_unmonted_hint),  Toast.LENGTH_SHORT).show();		
	}
	
	public void clickClear(View view){
		
		for (int i = uploadList.size() - 1; i >= 0; i--){
			if (uploadList.get(i).uploadState <= 0){
				uploadList.remove(i);
			}
		}
		savePreference();
		updateAppendixText();
	}
	

	
	private void savePreference(){		
		Editor editor = conference.edit();
        Gson gson = new Gson();
        String beanListToJson = gson.toJson(uploadList);
		editor.putString("attachment_list", beanListToJson);
		editor.putString("attachment_no", no);
		editor.commit();
	}
	
	private String getPath(Context context, Uri uri) {
		 
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;
 
            try {
                cursor = context.getContentResolver().query(uri, projection,null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }
 
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
 
        return null;
    }
	
	private void addToUploadList(UpLoadBundle ub) {
		boolean existed = false;
		for (UpLoadBundle b : uploadList){
			if (b.filepath.equals(ub.filepath)){
				existed = true;
				break;
			}
		}
		if (!existed){
			uploadList.add(ub);
		}
	}

	private void updateAppendixText() {
		String temp = getString(R.string.to_upload);
		String temp2 = getString(R.string.already_uploaded);
		for (UpLoadBundle b : uploadList){
			if (b.uploadState <= 0){
				temp += b.filename;
				temp += "    ";
			} else {
				temp2 += b.filename;
				temp2 += "    ";
			}
		}
		
		tvAppendix.setText(temp);
		tvAppendixUploaded.setText(temp2);
	}

	class UpLoadBundle{
		String filename;
		String filepath;
		int uploadState = 0;
	}	
}
