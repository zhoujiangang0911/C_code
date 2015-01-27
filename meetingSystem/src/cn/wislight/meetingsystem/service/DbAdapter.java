package cn.wislight.meetingsystem.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import cn.wislight.meetingsystem.bean.OrgHistoryBean;
import cn.wislight.meetingsystem.bean.OrganizeSmpBean;
import cn.wislight.meetingsystem.bean.TopicDraftBean;
import cn.wislight.meetingsystem.util.Variables;

public class DbAdapter {
	public static final String KEY_ROWID = "_id";

	private static final String TAG = "DbAdapter";
	private static final String DATABASE_NAME = "records";
	private static final String TOPIC_DRAFT_TABLE = "topic_draft";
	private static final String ORG_HISTORY_TABLE = "org_history";
	private static final String ORG_CACHE_TABLE = "org_cache";
	
	
	public static final String CONFIG_TABLE = "config";

	private static final int DATABASE_VERSION = 1;
	
	/* config */
	public static final String KEY_URL = "url";
	public static final String KEY_VOIP_URL = "voip_url";
	public static final String KEY_USERNAME = "username";
	public static final String KEY_PASSWORD = "password";
	private static final String CONFIGRATION_CREATE = "create table config (_id integer primary key autoincrement, url text, voip_url text, username text, password text);";

	/* topic draft */
	public static final String KEY_TOPIC_USERNAME = "username";
	public static final String KEY_TOPIC_KEYWORD = "keyword";
	public static final String KEY_TOPIC_SUMMARY = "summary";
	public static final String KEY_TOPIC_END_DATE = "end_time";
	public static final String KEY_TOPIC_START_DATE = "start_time";
	public static final String KEY_TOPIC_CHECKER_ID = "check_ppl_id";
	public static final String KEY_TOPIC_CHECKER_NAME = "check_name";
	public static final String KEY_TOPIC_TARGET_ORG_NO = "org_no";
	public static final String KEY_TOPIC_TARGET_ORG_NAME = "org_name";
	public static final String KEY_TOPIC_SUGGESTED_ATTENDER_LIST = "attender_list";
	public static final String KEY_TOPIC_SUGGESTED_ATTENDER_NAMES = "attender_names";
	public static final String KEY_TOPIC_SG_GROUP = "groups";
	public static final String KEY_TOPIC_SUGGESTED_GROUP_IDS = "group_ids";
	public static final String KEY_TOPIC_SC = "source";
	public static final String KEY_TOPIC_UPLOADED = "uploaded";
	public static final String KEY_TOPIC_UPLOADEABLE = "uploadable";
	public static final String KEY_TOPIC_NO = "no";
	public static final String KEY_TOPIC_ATTACHMENT = "attachment";
	
	private static final String TOPIC_DRAFT_CREATE = "create table topic_draft" + 
	   "( _id integer primary key autoincrement, username text, keyword text, summary text, start_time text,"
	   + " end_time text, check_ppl_id integer, check_name text, org_no text, org_name text, attender_list text, attender_names text,"
	   + " groups text, group_ids text, source text, no text, attachment text, uploaded integer, uploadable integer)";
	
	/* org_search_history */
	public static final String KEY_ORG_USERNAME = "username";
	public static final String KEY_ORG_KEYWORD = "keyword";
	public static final String KEY_ORG_COUNT = "count";
	private static final String ORG_HISTORY_CREATE = "create table org_history (_id integer primary key autoincrement, username text, keyword text,  count integer);";
	
	/* group */
	/* conference type */
	/* org_cache */
	public static final String KEY_ORG_CACHE_MO_MA_NO = "MO_MA_no";
	public static final String KEY_ORG_CACHE_MO_NO = "MO_no";
	public static final String KEY_ORG_CACHE_MO_name = "MO_name";
	public static final String KEY_ORG_CACHE_MO_fullname = "MO_fullname";
	private static final String ORG_CACHE_CREATE = "create table org_cache (_id integer primary key autoincrement, MO_MA_no text, MO_no text, MO_name text, MO_fullname text);";
	
	private final Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public DbAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CONFIGRATION_CREATE);
			db.execSQL(TOPIC_DRAFT_CREATE);
			db.execSQL(ORG_HISTORY_CREATE);
			db.execSQL(ORG_CACHE_CREATE);
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			if (newVersion == 4){

			}
		}
	}


	public DbAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		DBHelper.close();
	}

	
	public Cursor getOrgHistory(){
		return db
				.query(ORG_HISTORY_TABLE, new String[] { KEY_ROWID,
						KEY_ORG_KEYWORD, KEY_ORG_COUNT}
						, KEY_TOPIC_USERNAME + "=?", 
						new String[] { Variables.loginname}, 
						null, 
						null, 
						"3 desc");
	}
	
	public long insertOrgHistory(OrgHistoryBean bean) {
		/* search keyword existed or not */
		boolean key_existed = false;
		Cursor  cur = db
		.query(ORG_HISTORY_TABLE, new String[] { KEY_ROWID,
				KEY_ORG_KEYWORD, KEY_ORG_COUNT}
				, KEY_TOPIC_USERNAME + "=?" + " AND " + KEY_ORG_KEYWORD + "=?", 
				new String[] { Variables.loginname, bean.getKeyword()
				               }, null, null, null);
		
		if (cur.moveToFirst()){
			bean.setCount(Integer.parseInt(cur.getString(2)) + 1);
			bean.setId(Integer.parseInt(cur.getString(0)));
			key_existed = true;
		}else{
			bean.setCount(1);
		}
			
		if (key_existed){ // update
			ContentValues updateValues = new ContentValues();
			updateValues.put(KEY_ORG_COUNT, bean.getCount());
			
			return db.update(ORG_HISTORY_TABLE, updateValues, KEY_ROWID + "=?", 
					new String[] {bean.getId()+""});
			
		} else { // insert
			ContentValues initialValues = new ContentValues();
			initialValues.put(KEY_ORG_KEYWORD, bean.getKeyword());
			initialValues.put(KEY_ORG_COUNT, bean.getCount());
			initialValues.put(KEY_ORG_USERNAME, Variables.loginname);
			
			return db.insert(ORG_HISTORY_TABLE, null, initialValues);
		}
	}
	
	
	public long updateTopicDraft(TopicDraftBean bean){
		ContentValues initialValues = new ContentValues();
		
		initialValues.put(KEY_TOPIC_USERNAME, bean.getUsername());
		initialValues.put(KEY_TOPIC_KEYWORD, bean.getKeyword());
		initialValues.put(KEY_TOPIC_SUMMARY, bean.getSummary());
		initialValues.put(KEY_TOPIC_END_DATE, bean.getEnd_time());
		initialValues.put(KEY_TOPIC_START_DATE, bean.getStart_time());
		initialValues.put(KEY_TOPIC_CHECKER_ID, bean.getCheck_ppl_id());
		initialValues.put(KEY_TOPIC_CHECKER_NAME, bean.getCheck_name());
		initialValues.put(KEY_TOPIC_TARGET_ORG_NO, bean.getOrg_no());
		initialValues.put(KEY_TOPIC_TARGET_ORG_NAME, bean.getOrg_name());
		initialValues.put(KEY_TOPIC_SUGGESTED_ATTENDER_LIST, bean.getAttender_list());
		initialValues.put(KEY_TOPIC_SUGGESTED_ATTENDER_NAMES, bean.getAttender_names());
		initialValues.put(KEY_TOPIC_SG_GROUP, bean.getGroups());
		initialValues.put(KEY_TOPIC_SUGGESTED_GROUP_IDS, bean.getGroup_ids());
		initialValues.put(KEY_TOPIC_SC, bean.getSource());
		initialValues.put(KEY_TOPIC_UPLOADED, bean.getUploaded());
		initialValues.put(KEY_TOPIC_UPLOADEABLE, bean.getUploadable());
		initialValues.put(KEY_TOPIC_NO, bean.getNo());
		initialValues.put(KEY_TOPIC_ATTACHMENT, bean.getAttachment());
		
		return db.update(TOPIC_DRAFT_TABLE, initialValues, KEY_ROWID + "=?", 
				new String[] {bean.getId()+""});
	}
	
	public long insertTopicDraft(TopicDraftBean bean) {
		ContentValues initialValues = new ContentValues();
		
		initialValues.put(KEY_TOPIC_USERNAME, bean.getUsername());
		initialValues.put(KEY_TOPIC_KEYWORD, bean.getKeyword());
		initialValues.put(KEY_TOPIC_SUMMARY, bean.getSummary());
		initialValues.put(KEY_TOPIC_END_DATE, bean.getEnd_time());
		initialValues.put(KEY_TOPIC_START_DATE, bean.getStart_time());
		initialValues.put(KEY_TOPIC_CHECKER_ID, bean.getCheck_ppl_id());
		initialValues.put(KEY_TOPIC_CHECKER_NAME, bean.getCheck_name());
		initialValues.put(KEY_TOPIC_TARGET_ORG_NO, bean.getOrg_no());
		initialValues.put(KEY_TOPIC_TARGET_ORG_NAME, bean.getOrg_name());
		initialValues.put(KEY_TOPIC_SUGGESTED_ATTENDER_LIST, bean.getAttender_list());
		initialValues.put(KEY_TOPIC_SUGGESTED_ATTENDER_NAMES, bean.getAttender_names());
		initialValues.put(KEY_TOPIC_SG_GROUP, bean.getGroups());
		initialValues.put(KEY_TOPIC_SUGGESTED_GROUP_IDS, bean.getGroup_ids());
		initialValues.put(KEY_TOPIC_SC, bean.getSource());
		initialValues.put(KEY_TOPIC_UPLOADED, bean.getUploaded());
		initialValues.put(KEY_TOPIC_UPLOADEABLE, bean.getUploadable());
		initialValues.put(KEY_TOPIC_NO, bean.getNo());
		initialValues.put(KEY_TOPIC_ATTACHMENT, bean.getAttachment());
		
		return db.insert(TOPIC_DRAFT_TABLE, null, initialValues);
	}

	public Cursor getTopicDraftById(String id) {
		// TODO Auto-generated method stub
		return db.query(TOPIC_DRAFT_TABLE, new String[] { KEY_ROWID, KEY_TOPIC_USERNAME,
						KEY_TOPIC_KEYWORD, KEY_TOPIC_SUMMARY, KEY_TOPIC_END_DATE, KEY_TOPIC_START_DATE,
						KEY_TOPIC_CHECKER_ID, KEY_TOPIC_CHECKER_NAME, KEY_TOPIC_TARGET_ORG_NO,
						KEY_TOPIC_TARGET_ORG_NAME, KEY_TOPIC_SUGGESTED_ATTENDER_LIST,
						KEY_TOPIC_SUGGESTED_ATTENDER_NAMES,  KEY_TOPIC_SG_GROUP,
						KEY_TOPIC_SUGGESTED_GROUP_IDS, KEY_TOPIC_SC, KEY_TOPIC_UPLOADED, KEY_TOPIC_UPLOADEABLE,
						KEY_TOPIC_NO, KEY_TOPIC_ATTACHMENT}, 
						KEY_ROWID + "=?", new String[] {id },null,null,null);
	}
	
	public Cursor getTopicDrafts() {
		// TODO Auto-generated method stub
		return db
				.query(TOPIC_DRAFT_TABLE, new String[] { KEY_ROWID, KEY_TOPIC_USERNAME,
						KEY_TOPIC_KEYWORD, KEY_TOPIC_SUMMARY, KEY_TOPIC_END_DATE, KEY_TOPIC_START_DATE,
						KEY_TOPIC_CHECKER_ID, KEY_TOPIC_CHECKER_NAME, KEY_TOPIC_TARGET_ORG_NO,
						KEY_TOPIC_TARGET_ORG_NAME, KEY_TOPIC_SUGGESTED_ATTENDER_LIST,
						KEY_TOPIC_SUGGESTED_ATTENDER_NAMES,  KEY_TOPIC_SG_GROUP,
						KEY_TOPIC_SUGGESTED_GROUP_IDS, KEY_TOPIC_SC, KEY_TOPIC_UPLOADED, KEY_TOPIC_UPLOADEABLE,
						KEY_TOPIC_NO, KEY_TOPIC_ATTACHMENT},
						KEY_TOPIC_USERNAME + "=?" + " AND " + KEY_TOPIC_UPLOADED + "=?", 
						new String[] { Variables.loginname, "0"
						               }, null, null, null);
	}
	
	public Cursor getTopicDraftUnUploaded() {
		// TODO Auto-generated method stub
		return db
				.query(TOPIC_DRAFT_TABLE, new String[] { KEY_ROWID, KEY_TOPIC_USERNAME,
						KEY_TOPIC_KEYWORD, KEY_TOPIC_SUMMARY, KEY_TOPIC_END_DATE, KEY_TOPIC_START_DATE,
						KEY_TOPIC_CHECKER_ID, KEY_TOPIC_CHECKER_NAME, KEY_TOPIC_TARGET_ORG_NO,
						KEY_TOPIC_TARGET_ORG_NAME, KEY_TOPIC_SUGGESTED_ATTENDER_LIST,
						KEY_TOPIC_SUGGESTED_ATTENDER_NAMES,  KEY_TOPIC_SG_GROUP,
						KEY_TOPIC_SUGGESTED_GROUP_IDS, KEY_TOPIC_SC, KEY_TOPIC_UPLOADED, KEY_TOPIC_UPLOADEABLE,
						KEY_TOPIC_NO, KEY_TOPIC_ATTACHMENT}
						, KEY_TOPIC_USERNAME + "=?" + " AND " + KEY_TOPIC_UPLOADED + "=?" + " AND "  + KEY_TOPIC_UPLOADEABLE + "=?", 
						new String[] { Variables.loginname, 
						               "0","1" }, null, null, null);
	}
	
	public boolean setTopicDraftUploadedState(int id, int state){
		ContentValues args = new ContentValues();
		args.put(KEY_TOPIC_UPLOADED, state);
		return db.update(TOPIC_DRAFT_TABLE, args, KEY_ROWID + "=" + id, null) > 0;
	}

	public boolean setTopicDraftUploadAbleState(int id, boolean state){
		ContentValues args = new ContentValues();
		args.put(KEY_TOPIC_UPLOADEABLE, state);
		return db.update(TOPIC_DRAFT_TABLE, args, KEY_ROWID + "=" + id, null) > 0;
	}
	
	public Cursor getConfig() {
		return db.query(CONFIG_TABLE, new String[] { KEY_ROWID, KEY_URL },
				null, null, null, null, null);
	}
	
	public boolean setConfigUrl(String url) {
		ContentValues args = new ContentValues();
		args.put(KEY_URL, url);
		return db.update(CONFIG_TABLE, args, KEY_ROWID + "=1", null) > 0;
	}

	public boolean setConfigUsername(String username) {
		ContentValues args = new ContentValues();
		args.put(KEY_USERNAME, username);
		return db.update(CONFIG_TABLE, args, KEY_ROWID + "=1", null) > 0;
	}
	
	public boolean setConfigPassword(String pwd) {
		ContentValues args = new ContentValues();
		args.put(KEY_PASSWORD, pwd);
		return db.update(CONFIG_TABLE, args, KEY_ROWID + "=1", null) > 0;
	}	
	
	public boolean setConfigVoipUrl(String voipurl) {
		ContentValues args = new ContentValues();
		args.put(KEY_VOIP_URL, voipurl);
		return db.update(CONFIG_TABLE, args, KEY_ROWID + "=1", null) > 0;
	}	
	
	public String getConfigUrl() {

		Cursor cur = null;
		try {
			cur = db.query(CONFIG_TABLE, new String[] { KEY_ROWID, KEY_URL },
					null, null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		if (cur.moveToFirst()) {
			return cur.getString(1);
		}
		return null;
	}	
	
	public String getConfigUsername() {

		Cursor cur = null;
		try {
			cur = db.query(CONFIG_TABLE, new String[] { KEY_ROWID, KEY_USERNAME },
					null, null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		if (cur.moveToFirst()) {
			return cur.getString(1);
		}
		return null;
	}		
	
	public String getConfigPassword() {

		Cursor cur = null;
		try {
			cur = db.query(CONFIG_TABLE, new String[] { KEY_ROWID, KEY_PASSWORD },
					null, null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		if (cur.moveToFirst()) {
			return cur.getString(1);
		}
		return null;
	}			
	
	public String getConfigVoipUrl() {

		Cursor cur = null;
		try {
			cur = db.query(CONFIG_TABLE, new String[] { KEY_ROWID, KEY_VOIP_URL },
					null, null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		if (cur.moveToFirst()) {
			return cur.getString(1);
		}
		return null;
	}		
	
	public long insertConfig(String url, String voip_url, String username, String password) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_URL, url);
		initialValues.put(KEY_VOIP_URL, voip_url);
		initialValues.put(KEY_USERNAME, voip_url);
		initialValues.put(KEY_PASSWORD, voip_url);
		return db.insert(CONFIG_TABLE, null, initialValues);
	}

	public long insertOrgCache(OrganizeSmpBean bean) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_ORG_CACHE_MO_MA_NO, bean.getMa_no());
		initialValues.put(KEY_ORG_CACHE_MO_NO, bean.getNo());
		initialValues.put(KEY_ORG_CACHE_MO_name, bean.getName());
		initialValues.put(KEY_ORG_CACHE_MO_fullname, bean.getFullname());
												
		return db.insert(ORG_CACHE_TABLE, null, initialValues);
	}

	public void deleteAllOrgCache() {
		// TODO Auto-generated method stub
		db.delete(ORG_CACHE_TABLE, null, null);
	}

	public boolean isOrgCacheUseable() {
		// TODO Auto-generated method stub
		Cursor cur = null;
		try {
			cur = db.query(ORG_CACHE_TABLE, new String[] { KEY_ROWID },
					null, null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		if (cur.moveToNext()) {
			return true;
		}
		return false;
	}

	public Cursor findOrg(String name) {
		// TODO Auto-generated method stub
		return db
				.query(ORG_CACHE_TABLE, new String[] { KEY_ROWID, KEY_ORG_CACHE_MO_fullname,
						KEY_ORG_CACHE_MO_NO}
						, KEY_ORG_CACHE_MO_name + " like ?" , 
						new String[] { "%"+ name + "%"
						               }, null, null, null);
	}





}