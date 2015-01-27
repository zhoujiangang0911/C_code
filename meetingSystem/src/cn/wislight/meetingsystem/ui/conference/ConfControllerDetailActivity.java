package cn.wislight.meetingsystem.ui.conference;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.ui.topic.TopicAddThreeActivity;
import cn.wislight.meetingsystem.ui.topic.TopicUnArrivalActivity;
import cn.wislight.meetingsystem.util.AttachmentListActivity;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.FileChooserActivity;
import cn.wislight.meetingsystem.util.IntentUtil;
import cn.wislight.meetingsystem.util.LoadingDialog;
import cn.wislight.meetingsystem.util.MeetingSystemClient;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * @author Administrator 主页 新增议题第三个
 */
public class ConfControllerDetailActivity extends BaseActivity {
	private TextView tvTitle,tvRemark, tvAddress, tvStartdate, tvEnddate;
	//private TextView tvChecker, tvSuggestedAttender, tvCreator;
	private TextView tvJoinMember, tvTopicList;
	private TextView tvAppendix;
	private TextView tvAppendixUploaded;
	private EditText etReason;
	private LoadingDialog loadingdiag;
	private LinearLayout llConfFinish;
	private LinearLayout llConfAppendix;
	
	private String checkReason;
	String meetid;
	private int meetstatu;
	private int auth_control;
	private int upLoadIndex;
	private ArrayList<UpLoadBundle> uploadList;
	
	@Override
	public void initView() {
		
		tvJoinMember=(TextView) findViewById(R.id.conf_joinmember);
		tvTopicList=(TextView) findViewById(R.id.conf_topic_list);
		tvJoinMember.setVisibility(View.VISIBLE);
		tvTopicList.setVisibility(View.VISIBLE);

		tvTitle = (TextView) findViewById(R.id.conference_title);
		tvRemark = (TextView) findViewById(R.id.conference_remark);
		tvAddress = (TextView) findViewById(R.id.conference_address);
		tvStartdate = (TextView) findViewById(R.id.tv_starttime);
		tvEnddate = (TextView) findViewById(R.id.tv_endtime);

		//tvChecker = (TextView) findViewById(R.id.tv_checker);
		//tvSuggestedAttender = (TextView) findViewById(R.id.tv_suggested_attender);
		//tvCreator = (TextView) findViewById(R.id.tv_creator);
		etReason = (EditText) findViewById(R.id.conference_conclusion);
		llConfFinish = (LinearLayout) findViewById(R.id.ll_conf_finish);
		llConfFinish.setVisibility(View.GONE);
		llConfAppendix = (LinearLayout) findViewById(R.id.ll_conf_appendix);
		llConfAppendix.setVisibility(View.GONE);
		tvAppendix = (TextView)findViewById(R.id.tv_appendix);
		tvAppendixUploaded = (TextView)findViewById(R.id.tv_appendix_uploaded);
		Intent intent = getIntent();
	    meetid = intent.getStringExtra("id");
		
	    uploadList = new ArrayList<UpLoadBundle>();
	    
		loadingdiag = new LoadingDialog(this);  
		loadingdiag.setCanceledOnTouchOutside(false); 
		
		
		getConferenceDetail(meetid);
	}

	@Override
	public void setupView() {
		setContentView(R.layout.conference_control_detail);
	}

	protected void onPause() {
		super.onPause();

	}
	
	public void clickAttachmentList(View view){
		Intent data=new Intent();  
        data.putExtra(Constants.ID, meetid);
        
        IntentUtil.startActivity(ConfControllerDetailActivity.this, AttachmentListActivity.class,  data);
	}

	
	public void clickJoinMember(View view) {
		Intent data=new Intent();  
        data.putExtra(Constants.ID, meetid);  
        IntentUtil.startActivity(ConfControllerDetailActivity.this, ConfJoinMemberListActivity.class,  data);
  
	}
	
	public void clickConfJoinMember(View view) {
		Intent data=new Intent();  
        data.putExtra(Constants.ID, meetid);  
        IntentUtil.startActivity(ConfControllerDetailActivity.this, ConfJoinConfMemberListActivity.class,  data);
	}
	
	public void clickTopicList(View view) {
		Intent data=new Intent();  
        data.putExtra(Constants.ID, meetid);
        data.putExtra(Constants.CONF_CONTROL,   "yes");

        IntentUtil.startActivity(ConfControllerDetailActivity.this, ConfTopicListActivity.class,  data);
  
	}
	public void clickRefuse(View view) {
		/*
		if (etReason.getText().toString().length() <= 0){
			Toast.makeText(ConfControllerDetailActivity.this,
				getString(R.string.error_topic_check_noreason), Toast.LENGTH_SHORT).show();
			return;
		}		
		checkReason = etReason.getText().toString();
		StorageConference(checkReason);
		*/
		Intent data=new Intent();  
        data.putExtra(Constants.ID, 5);  
        IntentUtil.startActivity(ConfControllerDetailActivity.this, TopicUnArrivalActivity.class,  data);
		
	}



	public void clickPass(View view) {
		if (etReason.getText().toString().length() <= 0){
			Toast.makeText(ConfControllerDetailActivity.this,
				getString(R.string.error_meet_noconclusion), Toast.LENGTH_SHORT).show();
			return;
		}
		checkReason = etReason.getText().toString();
		StorageConference(checkReason);
	}

	private void StorageConference(String checkReason) {
		String url = "MeetingManage/mobile/doStorageMeeting.action?meetingId="+ meetid				     
				     + "&conclusion=" + checkReason;
		loadingdiag.setText(getString(R.string.uploading));
		loadingdiag.show();  
		System.out.println("wangting"+url);
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();  
				System.out.println("wangting"+error.getMessage());
				Toast.makeText(ConfControllerDetailActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
				//finish();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				loadingdiag.hide();
				String response = new String(body);
				System.out.println("wangting"+response);
				if (response.contains("用户未登陆")){
					Toast.makeText(ConfControllerDetailActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				if (response.contains("true")){
					Toast.makeText(ConfControllerDetailActivity.this, "归档成功", Toast.LENGTH_SHORT).show();
					finish();
					/*
					Intent intent = new Intent();    
					intent.setClass((Context)ConfControllerDetailActivity.this, TopicStayActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);   
					startActivity(intent);  
					*/
				}else{
					try {
						JSONObject jso = new JSONObject(response);
						String msg = jso.getString("ErrorMsg");
						Toast.makeText(ConfControllerDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Toast.makeText(ConfControllerDetailActivity.this, "归档失败", Toast.LENGTH_SHORT).show();
				}
			}
		});		
	}
	
	private void getConferenceDetail(String id) {
		// TODO Auto-generated method stub
		String url = "MeetingManage/mobile/findMeetingDetailById.action?type=1&meetid="+id;
		loadingdiag.setText(getString(R.string.loading));

		loadingdiag.show();  
		System.out.println("wangting"+url);
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();  
				Toast.makeText(ConfControllerDetailActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
				System.out.println("wangting"+error.getMessage());
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {				
				loadingdiag.hide();
				String response = new String(body);				
				System.out.println("wangting"+response);
				if (response.contains("用户未登陆")){
					Toast.makeText(ConfControllerDetailActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(response);				               
					tvTitle.setText(jsonObject.getString("title"));
					tvRemark.setText(jsonObject.getString("remark"));
					tvAddress.setText(jsonObject.getString("address"));
					tvStartdate.setText(jsonObject.getString("startdate"));
					tvEnddate.setText(jsonObject.getString("enddate"));
					
					meetstatu = Integer.parseInt(jsonObject.getString("meetstatu")); 
					auth_control = Integer.parseInt(jsonObject.getString("auth_control"));
					
					
					if (meetstatu >=2 && auth_control == 1){
						llConfFinish.setVisibility(View.VISIBLE);
						llConfAppendix.setVisibility(View.VISIBLE);
					} else {
						llConfFinish.setVisibility(View.GONE);
						llConfAppendix.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ConfControllerDetailActivity.this,
							getString(R.string.error_dataabout),
							Toast.LENGTH_SHORT).show();
					
				}
			}
		});
	}

	public void clickUpLoadFile(View view){
		
		int count = 0;
		for (UpLoadBundle b: uploadList){
			if (b.uploadState <= 0){
				count ++;
			}
		}
		
		if (count == 0){
			Toast.makeText(ConfControllerDetailActivity.this,
					getString(R.string.error_no_document), Toast.LENGTH_SHORT).show();
			return;
		}
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
				Toast.makeText(ConfControllerDetailActivity.this,
						getString(R.string.result_success), Toast.LENGTH_SHORT).show();		
				return;
			} else {
				upLoadFile();
				return;
			}
			
		}
		
		loadingdiag.setText(getString(R.string.uploading) + ":  " 
				+ uploadList.get(upLoadIndex).filename);
		loadingdiag.show();

		String url = "MeetingManage/mobile/MobileUpLoadFileServlet?no=";
		url += meetid;
		RequestParams params = new RequestParams();
		File file = new File(uploadList.get(upLoadIndex).filepath);
		try {
			params.put("filedata", file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Toast.makeText(ConfControllerDetailActivity.this,
					getString(R.string.error_fileabout),
					Toast.LENGTH_SHORT).show();
			return;
		}

		
		MeetingSystemClient.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();
				Toast.makeText(ConfControllerDetailActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onSuccess(int stautsCode, Header[] arg1, byte[] body) {
				String result = new String(body);
				
				if (result.contains("用户未登陆")){
					Toast.makeText(ConfControllerDetailActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				if (result.contains("success")) {
					uploadList.get(upLoadIndex).uploadState = 1;
					upLoadFile();
				} else {
					loadingdiag.hide();
					if (result.contains("errorMessage")){
						try {
							JSONObject jsob =  new JSONObject(result);
							String msg = jsob.getString("errorMessage");
							Toast.makeText(ConfControllerDetailActivity.this,
									msg,
									Toast.LENGTH_SHORT).show();
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Toast.makeText(ConfControllerDetailActivity.this,
									getString(R.string.error_upload_failed),
									Toast.LENGTH_SHORT).show();
						}
						
					}else{
						Toast.makeText(ConfControllerDetailActivity.this,
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
		updateAppendixText();

	}
	
	public void onActivityResult(int requestCode , int resultCode , Intent data){
		
		if(resultCode == RESULT_CANCELED){
			return ;
		}
		if(resultCode == RESULT_OK && requestCode == Constants.CODE_FILE_SELECT_COMMON){
			//获取路径名
			String filePath = data.getStringExtra(Constants.EXTRA_FILE_CHOOSER);
			if(filePath != null){
				File f = new File(filePath);
				if (f.length() > Constants.MAX_UPLOAD_FILE_SIZE){
					Toast.makeText(ConfControllerDetailActivity.this,
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
				
			}
		}
		if(resultCode == RESULT_OK && requestCode == Constants.CODE_FILE_SELECT){
			//获取路径名
			Uri uri = data.getData();
			String filePath = getPath(this, uri);
			String fileName = filePath.substring(filePath.lastIndexOf("/")+1); 
			
			File f = new File(filePath);
			if (f.length() > Constants.MAX_UPLOAD_FILE_SIZE){
				Toast.makeText(ConfControllerDetailActivity.this,
						getString(R.string.error_filesize_toolarge),
						Toast.LENGTH_SHORT).show();
				
				return;
			}
			
			UpLoadBundle b = new UpLoadBundle();
			b.filename = fileName;
			b.filepath = filePath;
			b.uploadState = 0;
			addToUploadList(b);
			updateAppendixText();
		}
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
