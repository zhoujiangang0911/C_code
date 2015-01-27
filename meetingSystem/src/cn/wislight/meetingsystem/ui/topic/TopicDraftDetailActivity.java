package cn.wislight.meetingsystem.ui.topic;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.service.DbAdapter;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.FileChooserActivity;
import cn.wislight.meetingsystem.util.IntentUtil;
import cn.wislight.meetingsystem.util.LoadingDialog;
import cn.wislight.meetingsystem.util.MeetingSystemClient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * @author Administrator 我的议题 -> 议题详情
 */
public class TopicDraftDetailActivity extends BaseActivity {
	private TextView tvSummary, tvKeywords, tvStartdate, tvEnddate,
			tvTargetOrg;
	private TextView tvChecker, tvSuggestedAttender;
	
	private LoadingDialog loadingdiag;

	String tpid;
	
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

		tvSummary = (TextView) findViewById(R.id.tv_summary);
		tvKeywords = (TextView) findViewById(R.id.tv_keywords);
		tvStartdate = (TextView) findViewById(R.id.tv_starttime);
		tvEnddate = (TextView) findViewById(R.id.tv_endtime);
		tvTargetOrg = (TextView) findViewById(R.id.tv_target_org);
		tvChecker = (TextView) findViewById(R.id.tv_checker);
		tvSuggestedAttender = (TextView) findViewById(R.id.tv_suggested_attender);
		// tvState = (TextView) findViewById(R.id.tv_title_state);

		Intent intent = getIntent();
	    tpid = intent.getStringExtra("id");
		
		/* for upload */
		btnUploadAttachment = (Button)findViewById(R.id.btn_upload_attachment);
		llConfAppendix = (LinearLayout) findViewById(R.id.ll_conf_appendix);
		tvAppendix = (TextView)findViewById(R.id.tv_appendix);
		tvAppendixUploaded = (TextView)findViewById(R.id.tv_appendix_uploaded);
	    
		loadingdiag = new LoadingDialog(this);  
		loadingdiag.setCanceledOnTouchOutside(false); 
		loadingdiag.setText(getString(R.string.loading));
		
		getTopicDetail(tpid);
	}

	@Override
	public void setupView() {
		setContentView(R.layout.topic_draft_detail);
	}

	protected void onPause() {
		super.onPause();

	}
	
	private void getTopicDetail(String id) {
		loadingdiag.show();  
		DbAdapter dbHandle = new DbAdapter(this);
		dbHandle.open();
		Cursor cursor = dbHandle.getTopicDraftById(id);
		
		if (cursor.moveToFirst())
		{
			String topic_keys = cursor.getString(2);
			String topic_summary = cursor.getString(3);
			String topic_start_date = cursor.getString(5);
			String topic_end_date = cursor.getString(4);
			String topic_target_org_name = cursor.getString(9);
			String topic_suggested_attender_names = cursor
					.getString(11);
			String topic_checker = cursor.getString(7);
			no = cursor.getString(17);
			String topic_attachment = cursor.getString(18);
			//String topic_creator = cursor.getString();
			//String topic_check_reason = cursor.getString();
			// tvState.setText(R.string.topic_status_toupload);
			
			tvSummary.setText(topic_summary);
			tvKeywords.setText(topic_keys);
			tvStartdate.setText(topic_start_date);
			tvEnddate.setText(topic_end_date);
			tvTargetOrg.setText(topic_target_org_name);
			tvChecker.setText(topic_checker);
			tvSuggestedAttender.setText(topic_suggested_attender_names);
			
			if (null != topic_attachment && !"".equals(topic_attachment)){
				Gson gson = new Gson();
				uploadList = gson.fromJson(topic_attachment, new TypeToken<List<UpLoadBundle>>(){}.getType());
			}else{
		        uploadList = new ArrayList<UpLoadBundle>();		
			}
			
			if (null != uploadList){
				updateAppendixText();
			}
			//tvCreator.setText(topic_creator);
			//tvReason.setText(topic_check_reason);
		}
		dbHandle.close();

		loadingdiag.hide();  
	}
	
	private void uploadTopic() {
		DbAdapter dbHandle = new DbAdapter(this);
		dbHandle.open();
		Cursor cursor = dbHandle.getTopicDraftById(tpid);
		loadingdiag.setText(getString(R.string.uploading));
		loadingdiag.show();
		
		if (cursor.moveToFirst())
		{
			String topic_keys = cursor.getString(2);
			String topic_summary = cursor.getString(3);
			String topic_start_date = cursor.getString(5);
			String topic_end_date = cursor.getString(4);
			//String topic_creator = cursor.getString();
			//String topic_check_reason = cursor.getString();
			// tvState.setText(R.string.topic_status_toupload);
			
			String topic_target_org_no = cursor.getString(8);
			String topic_suggested_attender_list = cursor.getString(10);
			String topic_checker_id = cursor.getString(6);
			String topic_sg_group = cursor.getString(13);
			String topic_sc =  cursor.getString(14);
			dbHandle.close();

			String url = "MeetingManage/mobile/addTopic.action";
			
			RequestParams params = new RequestParams();
			params.put("keyword", topic_keys);
			params.put("summary", topic_summary);
			params.put("startTime2", topic_start_date);
			params.put("endTime2", topic_end_date);
			params.put("check_ppl_id", topic_checker_id);
			params.put("target_org", topic_target_org_no);
			params.put("suggested_attender", topic_suggested_attender_list);
			params.put("groupIds", topic_sg_group);
			params.put("sc", topic_sc);
			params.put("no", no);
			
			MeetingSystemClient.post(url, params, new AsyncHttpResponseHandler() {
				@Override
				public void onFailure(int statusCode, Header[] arg1, byte[] body,
						Throwable error) {
					loadingdiag.hide();
					Toast.makeText(TopicDraftDetailActivity.this,
							getString(R.string.error_network), Toast.LENGTH_SHORT)
							.show();
				}

				@Override
				public void onSuccess(int stautsCode, Header[] arg1, byte[] body) {
					String result = new String(body);
					loadingdiag.hide();
					if (result.contains("用户未登陆")){
						Toast.makeText(TopicDraftDetailActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
						return;
					}	
					if (result.contains("success")) {
						Toast.makeText(TopicDraftDetailActivity.this,
								getString(R.string.result_success),
								Toast.LENGTH_SHORT).show();
						
						DbAdapter dbHandle = new DbAdapter(TopicDraftDetailActivity.this);
						dbHandle.open();
						dbHandle.setTopicDraftUploadedState(Integer.parseInt(tpid), 1);
						dbHandle.close();
						
						Intent intent = new Intent();    
						intent.setClass((Context)TopicDraftDetailActivity.this, TopicMineActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);   
						startActivity(intent);  
						
						finish();


					} else {
						Toast.makeText(TopicDraftDetailActivity.this,
								getString(R.string.error_upload_failed),
								Toast.LENGTH_SHORT).show();
					}
				}
			});
			
		} else {
			dbHandle.close();
			Toast.makeText(TopicDraftDetailActivity.this,
					getString(R.string.error_dataabout),
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		
	}
	
	public void clickUpload(View view) {
		// IntentUtil.startActivity(this,TopicAddOneActivity.class);
		loadingdiag.show();  
		int count = 0;
		for (UpLoadBundle b: uploadList){
			if (b.uploadState <= 0){
				count ++;
			}
		}
		if (count > 0){
			bclickUploadFile = false;
			beginUploadFile();
		} else {
			uploadTopic();	
		}
	}
	
public void clickUpLoadFile(View view){
		
		int count = 0;
		for (UpLoadBundle b: uploadList){
			if (b.uploadState <= 0){
				count ++;
			}
		}
		
		if (count == 0){
			Toast.makeText(TopicDraftDetailActivity.this,
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
					Toast.makeText(TopicDraftDetailActivity.this,
							getString(R.string.result_success), Toast.LENGTH_SHORT).show();		
					return;	
				} else {
					// continue to upload topic 
					uploadTopic();
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
			Toast.makeText(TopicDraftDetailActivity.this,
					getString(R.string.error_fileabout),
					Toast.LENGTH_SHORT).show();
			return;
		}

		
		MeetingSystemClient.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();
				Toast.makeText(TopicDraftDetailActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onSuccess(int stautsCode, Header[] arg1, byte[] body) {
				String result = new String(body);
				if (result.contains("用户未登陆")){
					Toast.makeText(TopicDraftDetailActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				if (result.contains("success")) {
					
					try {
						JSONObject jso = new JSONObject(result);
						no = jso.getString("no");
						if (null == no || "".equals(no)){
							Toast.makeText(TopicDraftDetailActivity.this,
									getString(R.string.error_dataabout),
									Toast.LENGTH_SHORT).show();
							return;
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Toast.makeText(TopicDraftDetailActivity.this,
								getString(R.string.error_dataabout),
								Toast.LENGTH_SHORT).show();
						return;
					}
					
					uploadList.get(upLoadIndex).uploadState = 1;
					upLoadFile();
				} else {
					loadingdiag.hide();
					if (result.contains("errorMessage")){
						try {
							JSONObject jsob =  new JSONObject(result);
							String msg = jsob.getString("errorMessage");
							Toast.makeText(TopicDraftDetailActivity.this,
									msg,
									Toast.LENGTH_SHORT).show();
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Toast.makeText(TopicDraftDetailActivity.this,
									getString(R.string.error_upload_failed),
									Toast.LENGTH_SHORT).show();
						}
						
					}else{
						Toast.makeText(TopicDraftDetailActivity.this,
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
					Toast.makeText(TopicDraftDetailActivity.this,
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
				Toast.makeText(TopicDraftDetailActivity.this,
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

	public void clickEditTopic(View view){
		this.finish();
		Intent data = new Intent();
		data.putExtra(Constants.ID, tpid);
		IntentUtil.startActivity(TopicDraftDetailActivity.this,
				TopicAddOneActivity.class, data);
	}
}
