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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.FileChooserActivity;
import cn.wislight.meetingsystem.util.IntentUtil;
import cn.wislight.meetingsystem.util.LoadingDialog;
import cn.wislight.meetingsystem.util.MeetingSystemClient;
import cn.wislight.meetingsystem.util.Variables;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


/**
 * @author Administrator
 *	主页  新增议题第三个
 */
public class TopicMeetAddThreeActivity extends BaseActivity {
	private SharedPreferences topic;
	private SharedPreferences data;
	private TextView tvSummary, tvKeywords, tvStartdate, tvEnddate, tvTargetOrg;
	private TextView tvChecker, tvSuggestedAttender;
	private LoadingDialog loadingdiag;
	private TextView tvAttender,
		tvTopicVoteStaticer,
		tvTopicVoteController,
		tvTopicController,
		tvVoteEndtime,
		tvVoteStarttime,
		tvTopicVoteAbstain,
		tvTopicVoteType,
		tvIsMeetcall,
		tvIsVote;
	private LinearLayout llVote;
	
	/* for upload */
	private ArrayList<UpLoadBundle> uploadList = null;
	private int upLoadIndex;
	private TextView tvAppendix;
	private TextView tvAppendixUploaded;
	private String no="";
	boolean bclickUploadFile = false;
	/**/ 
	
	@Override
	public void initView() {
		topic = this.getSharedPreferences(Constants.TOPIC_ME_NODE+ Variables.loginname, MODE_PRIVATE);
		data = this.getSharedPreferences(Constants.DATA_NODE, MODE_PRIVATE);
		String topic_keys = topic.getString(Constants.TOPIC_E_KEYWORDS, "");
		String topic_summary = topic.getString(Constants.TOPIC_E_SUMMARY, "");
		String topic_start_date = topic.getString(Constants.TOPIC_E_START_DATE, "");
		String topic_end_date = topic.getString(Constants.TOPIC_E_END_DATE, "");
		String topic_target_org_name = topic.getString(Constants.TOPIC_E_TARGET_ORG_NAME, "");
		String topic_checker_name = topic.getString(Constants.TOPIC_E_CHECKER_NAME, "");
		
		String topic_attender = topic.getString(Constants.TOPIC_E_SUGGESTED_ATTENDER_NAMES, "");
		String topic_vote_staticer = topic.getString(Constants.TOPIC_E_SUMMNG_NAME, "");
		String topic_vote_controller = topic.getString(Constants.TOPIC_E_VOTEMNG_NAME, "");
		String topic_controller = topic.getString(Constants.TOPIC_E_MANAGER_NAME, "");
		String topic_vote_endtime = topic.getString(Constants.TOPIC_E_VOTE_ENDTIME, "");
		String topic_vote_starttime = topic.getString(Constants.TOPIC_E_VOTE_STARTTIME, "");
		String topic_vote_abstain = topic.getString(Constants.TOPIC_E_IS_ABSTAIN, "");
		String topic_vote_type = topic.getString(Constants.TOPIC_E_VOTETYPE, "");
		String topic_is_vote = topic.getString(Constants.TOPIC_E_IS_NEEDVOTE, "");
		String topic_is_meetcall = topic.getString(Constants.TOPIC_E_IS_NEEDMEETCALL, "");
		
		no = topic.getString(Constants.TOPIC_E_RNO, "");
		String topicAttachment = topic.getString(Constants.TOPIC_E_ATTACHMENT, "");
		if (null != topicAttachment && !"".equals(topicAttachment)){
			Gson gson = new Gson();
			uploadList = gson.fromJson(topicAttachment, new TypeToken<List<UpLoadBundle>>(){}.getType());
		}else{
	        uploadList = new ArrayList<UpLoadBundle>();		
		}
		
		tvSummary = (TextView)findViewById(R.id.tv_summary);
		tvKeywords = (TextView)findViewById(R.id.tv_keywords);
		tvStartdate = (TextView)findViewById(R.id.tv_starttime); 
		tvEnddate = (TextView)findViewById(R.id.tv_endtime);
		tvTargetOrg = (TextView)findViewById(R.id.tv_target_org);
		tvChecker = (TextView)findViewById(R.id.tv_checker); 
		
		tvAttender = (TextView)findViewById(R.id.tv_attender);
		tvTopicVoteStaticer = (TextView)findViewById(R.id.tv_topic_vote_staticer);
		tvTopicVoteController = (TextView)findViewById(R.id.tv_topic_vote_controller);
		tvTopicController = (TextView)findViewById(R.id.tv_topic_controller);
		tvVoteEndtime = (TextView)findViewById(R.id.tv_vote_endtime);
		tvVoteStarttime = (TextView)findViewById(R.id.tv_vote_starttime);
		tvTopicVoteAbstain = (TextView)findViewById(R.id.tv_topic_vote_abstain);
		tvTopicVoteType = (TextView)findViewById(R.id.tv_topic_vote_type);
		tvIsVote = (TextView)findViewById(R.id.tv_is_vote);
		tvIsMeetcall = (TextView)findViewById(R.id.tv_is_meetcall);
		llVote = (LinearLayout)findViewById(R.id.ll_vote);
		/* for upload */
		tvAppendix = (TextView)findViewById(R.id.tv_appendix);
		tvAppendixUploaded = (TextView)findViewById(R.id.tv_appendix_uploaded);
		
		if ("1".equals(topic_is_vote)){
			llVote.setVisibility(View.VISIBLE);
			tvIsVote.setText(R.string.common_yes);
		} else {
			llVote.setVisibility(View.GONE);
			tvIsVote.setText(R.string.common_no);
		}
		if ("1".equals(topic_is_meetcall)){
			tvIsMeetcall.setText(R.string.common_yes);
		} else {
			tvIsMeetcall.setText(R.string.common_no);
		}
		tvTopicVoteStaticer.setText(topic_vote_staticer);
		tvTopicVoteController.setText(topic_vote_controller);
		tvTopicController.setText(topic_controller);
		tvVoteEndtime.setText(topic_vote_endtime);
		tvVoteStarttime.setText(topic_vote_starttime);
		if ("1".equals(topic_vote_abstain)){
			tvTopicVoteAbstain.setText(R.string.common_yes);
		} else {
			tvTopicVoteAbstain.setText(R.string.common_no);
		}
		
		if ("1".equals(topic_vote_type)){
			tvTopicVoteType.setText(R.string.topic_vote_withname);
		} else {
			tvTopicVoteType.setText(R.string.topic_vote_withoutname);
		}
		
		loadingdiag = new LoadingDialog(this);  
		loadingdiag.setCanceledOnTouchOutside(false); 
		loadingdiag.setText(getString(R.string.uploading));
		
		tvSummary.setText(topic_summary);
		tvKeywords.setText(topic_keys);
		tvStartdate.setText(topic_start_date);
		tvEnddate.setText(topic_end_date);
		tvTargetOrg.setText(topic_target_org_name);
		tvChecker.setText(topic_checker_name);
		tvAttender.setText(topic_attender);
		
		if (null != uploadList){
			updateAppendixText();
		}
		
	}

	@Override
	public void setupView() {
		setContentView(R.layout.topic_me_addthree);
	}
	
	protected void onPause(){
		super.onPause();

	}
	
	
	
	
	public void  clickPre(View view){
		IntentUtil.startActivity(this,TopicEditTwoActivity.class);
		this.finish();
	}
	public void  clickSave(View view){
		Toast.makeText(this, "save", Toast.LENGTH_SHORT).show();
		this.finish();
	}
	
	public void  clickUpload(View view){
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
	
	
		
	private void uploadTopic(){
		final String topic_keys = topic.getString(Constants.TOPIC_E_KEYWORDS, "");
		
		final String topic_start_date = topic.getString(Constants.TOPIC_E_START_DATE, "");
		final String topic_end_date = topic.getString(Constants.TOPIC_E_END_DATE, "");
		String topic_vote_staticer_id = topic.getString(Constants.TOPIC_E_SUMMNG_ID, "");
		String topic_vote_controller_id = topic.getString(Constants.TOPIC_E_VOTEMNG_ID, "");
		String topic_controller_id = topic.getString(Constants.TOPIC_E_MANAGER_ID, "");
		String topic_vote_endtime = topic.getString(Constants.TOPIC_E_VOTE_ENDTIME, "");
		String topic_vote_starttime = topic.getString(Constants.TOPIC_E_VOTE_STARTTIME, "");
		String topic_vote_abstain = topic.getString(Constants.TOPIC_E_IS_ABSTAIN, "");
		String topic_vote_type = topic.getString(Constants.TOPIC_E_VOTETYPE, "");
		String topic_is_vote = topic.getString(Constants.TOPIC_E_IS_NEEDVOTE, "");
		String topic_is_meetcall = topic.getString(Constants.TOPIC_E_IS_NEEDMEETCALL, "");
		final String topic_summary = topic.getString(Constants.TOPIC_E_SUMMARY, "");
		String topic_suggested_attender_list = topic.getString(Constants.TOPIC_E_SUGGESTED_ATTENDER_LIST, "");
		String topic_sg_group = topic.getString(Constants.TOPIC_E_SUGGESTED_GROUP_IDS, "");
		
		if ("0".equals(topic_is_vote)){
			topic_vote_starttime =  topic_start_date;
			topic_vote_endtime = topic_end_date;
		}
		RequestParams params = new RequestParams();
		
		params.put("keyword", topic_keys);
		params.put("summary", topic_summary);
		params.put("suggested_attender", topic_suggested_attender_list);
		params.put("groupIds", topic_sg_group);
		params.put("sc", "mobile_user_inmeetcreate");
		
		params.put("summng_id", topic_vote_staticer_id);
		params.put("votemng_id", topic_vote_controller_id);
		params.put("mng_id", topic_controller_id);
		params.put("vote_starttime", topic_vote_starttime);
		params.put("vote_endtime", topic_vote_endtime);
		params.put("is_abstain", topic_vote_abstain);
		params.put("votetype", topic_vote_type);
		params.put("is_vote", topic_is_vote);
		params.put("is_meetcall", topic_is_meetcall);
		params.put("starttime", topic_start_date);
		params.put("endtime", topic_end_date);
		params.put("no", no);

		
		String url = "MeetingManage/mobile/addTopicForMeet.action";
		
		loadingdiag.show();
		
		MeetingSystemClient.post(url, params, new AsyncHttpResponseHandler(){
			@Override
			public void onFailure(int statusCode, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();
				Toast.makeText(TopicMeetAddThreeActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(int stautsCode, Header[] arg1, byte[] body) {
				String result = new String(body);
				if (result.contains("success")){
					loadingdiag.hide();

					Toast.makeText(TopicMeetAddThreeActivity.this, getString(R.string.result_success), Toast.LENGTH_SHORT).show();
					String id = null;
					try {
						JSONObject jsonObject = new JSONObject(result);
						id = jsonObject.getString("tpid");
						
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					Editor dataEditor = data.edit();
					dataEditor.putString(Constants.DATA_ID, id);
					dataEditor.putString(Constants.DATA_STARTDATE, topic_start_date);
					dataEditor.putString(Constants.DATA_ENDDATE, topic_end_date);
					dataEditor.putString(Constants.DATA_KEYS, topic_keys);
					dataEditor.putString(Constants.DATA_TITLE, topic_summary);
					dataEditor.putString(Constants.DATA_TYPE_NEWADDED, "true");
					
			        dataEditor.commit();
					finish();
					
			        Editor editor = topic.edit();
			        editor.clear(); 
			        editor.commit();
					
				}else{
					Toast.makeText(TopicMeetAddThreeActivity.this, getString(R.string.error_upload_failed), Toast.LENGTH_SHORT).show();
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
			Toast.makeText(TopicMeetAddThreeActivity.this,
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
					Toast.makeText(TopicMeetAddThreeActivity.this,
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
			Toast.makeText(TopicMeetAddThreeActivity.this,
					getString(R.string.error_fileabout),
					Toast.LENGTH_SHORT).show();
			return;
		}

		
		MeetingSystemClient.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();
				Toast.makeText(TopicMeetAddThreeActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onSuccess(int stautsCode, Header[] arg1, byte[] body) {
				String result = new String(body);
				
				if (result.contains("用户未登陆")){
					Toast.makeText(TopicMeetAddThreeActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				
				if (result.contains("success")) {
					
					try {
						JSONObject jso = new JSONObject(result);
						no = jso.getString("no");
						if (null == no || "".equals(no)){
							Toast.makeText(TopicMeetAddThreeActivity.this,
									getString(R.string.error_dataabout),
									Toast.LENGTH_SHORT).show();
							return;
						}
						
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Toast.makeText(TopicMeetAddThreeActivity.this,
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
							Toast.makeText(TopicMeetAddThreeActivity.this,
									msg,
									Toast.LENGTH_SHORT).show();
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Toast.makeText(TopicMeetAddThreeActivity.this,
									getString(R.string.error_upload_failed),
									Toast.LENGTH_SHORT).show();
						}
						
					}else{
						Toast.makeText(TopicMeetAddThreeActivity.this,
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
		savePreference();

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
					Toast.makeText(TopicMeetAddThreeActivity.this,
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
				Toast.makeText(TopicMeetAddThreeActivity.this,
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
	}
	
	private void savePreference(){

		Editor editor = topic.edit();
        Gson gson = new Gson();
        String beanListToJson = gson.toJson(uploadList);
		editor.putString(Constants.TOPIC_E_ATTACHMENT, beanListToJson);
		editor.putString(Constants.TOPIC_E_RNO, no);
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
