package cn.wislight.meetingsystem.util;

public class Constants {
	public static final boolean DEBUG_VERSION = false;
	public static final String BASE_URL = "http://192.168.168.113:8080/";
	//public static final String BASE_URL = "http://125.71.235.132:8080/";
	
	public static final int MAX_UPLOAD_FILE_SIZE = (2 * 1024 * 1024);
	
	
	/*********************** 选择人员用到的常量 *******************/
	public static final int CODE_CHECKER = 100;
	public static final int CODE_SUGGUSTED_ATTENDER = 101;
	public static final int CODE_START_TIME = 102;
	public static final int CODE_END_TIME = 103;
	public static final int CODE_SELECT_ORG = 104;
	public static final int CODE_TOPIC_CONTROLLER = 105;
	public static final int CODE_TOPIC_VOTE_CONTROLLER = 106;
	public static final int CODE_TOPIC_VOTE_STATICCER = 107;
	public static final int CODE_VOTE_START_TIME = 108;
	public static final int CODE_VOTE_END_TIME = 109;
	public static final int CODE_SUGGUSTED_GROUP = 110;
	public static final int CODE_MENTION = 111;
	public static final int CODE_AVR_VOTE = 112;

	
	public static final int CODE_CONF_CONFTYPE =200;	
	public static final int CODE_CONF_CONTROLMEMBER =201;
	public static final int CODE_CONF_ACCESSMEMBER =202;	
	public static final int CODE_CONF_JOINMEMBER =203;	
	public static final int CODE_CONF_EDITTOPIC = 204;
	public static final int CODE_CONF_HOURSE = 205;

	public static final int CODE_FILE_SELECT = 301;
	public static final int CODE_FILE_SELECT_COMMON = 302;   
	public static final String EXTRA_FILE_CHOOSER = "file_chooser";
	
	public static final String ID = "id";
	public static final String RESULT = "result";
	public static final String TOPIC_ID = "topicId";
	public static final String TOPIC_NO = "topicno";
	public static final String POST = "post";
	public static final String ORG = "org";
	public static final String NAME = "name";
	public static final String DATETIME = "dateTime";
	public static final String ATTENDERLIST = "attender_list";
	public static final String ORG_FNAME = "org_fullname";
	public static final String ORG_NO = "org_no";
	public static final String INIT_TIME = "init_time";
	public static final String CONF_CONTROL = "conf_control";
	public static final String CONF_VIEW_HISTORY = "conf_history";
	public static final String MEET_NO = "meet_no";
	public static final String MEET_INFO = "meet_info";

	public static final String GROUPLIST = "group_list";
	public static final int OK = 0;
	public static final int ERROR_DATA = -1;
	public static final int ERROR_NETWORK = -2;
	
	//IM message
	public static final String TOPIC_IM_GROUP_ID = "groupId";
	
	/* topic share preference */
	public static final String TOPIC_NODE = "topic";
	// page1
	public static final String TOPIC_KEYWORDS = "topic_keywords";
	public static final String TOPIC_SUMMARY = "topic_summary";
	// page2
	public static final String TOPIC_START_DATE = "topic_start_date";
	public static final String TOPIC_END_DATE = "topic_end_date";
	public static final String TOPIC_TARGET_ORG_NO = "topic_target_org_no";
	public static final String TOPIC_TARGET_ORG_NAME = "topic_target_org_name";
	public static final String TOPIC_SUGGESTED_ATTENDER_NAMES = "topic_suggested_attender_names";
	public static final String TOPIC_SUGGESTED_ATTENDER_IDS = "topic_suggested_attender_ids";
	public static final String TOPIC_SUGGESTED_ATTENDER_LIST = "topic_suggested_attender_list";
	public static final String TOPIC_CHECKER_NAME = "topic_checker_name";
	public static final String TOPIC_CHECKER_ID = "topic_checker_id";
	public static final String TOPIC_SUGGESTED_GROUP_NAMES = "topic_suggested_group_names";
	public static final String TOPIC_SUGGESTED_GROUP_IDS = "topic_suggested_group_ids";
	// page3
	public static final String TOPIC_RNO = "topic_rno";
	public static final String TOPIC_ATTACHMENT = "topic_attachment";
	// draft mark
	public static final String TOPIC_DRAFT_ID = "topic_draft_id";
	public static final String TOPIC_DRAFT_RE_ID = "topic_draft_re_id";
	/* end topic share preference */

	/* topic share preference for edit/create full topic */
	public static final String TOPIC_E_NODE = "etopic";
	public static final String TOPIC_ME_NODE = "metopic"; // 在会议上添加议题
	public static final String TOPIC_C_NODE = "ctopic";

	public static final String TOPIC_E_MODE = "emode";
	public static final String TOPIC_E_MODE_EDIT = "emode_edit";
	public static final String TOPIC_E_MODE_CREATE = "emode_create";
	// page1
	public static final String TOPIC_E_KEYWORDS = "etopic_keywords";
	public static final String TOPIC_E_SUMMARY = "etopic_summary";
	// page2
	public static final String TOPIC_E_START_DATE = "etopic_start_date";
	public static final String TOPIC_E_END_DATE = "etopic_end_date";
	public static final String TOPIC_E_TARGET_ORG_NO = "etopic_target_org_no";
	public static final String TOPIC_E_TARGET_ORG_NAME = "etopic_target_org_name";
	public static final String TOPIC_E_SUGGESTED_ATTENDER_NAMES = "etopic_suggested_attender_names";
	public static final String TOPIC_E_SUGGESTED_ATTENDER_IDS = "etopic_suggested_attender_ids";
	public static final String TOPIC_E_SUGGESTED_ATTENDER_LIST = "etopic_suggested_attender_list";
	public static final String TOPIC_E_CHECKER_NAME = "etopic_checker_name";
	public static final String TOPIC_E_CHECKER_ID = "etopic_checker_id";
	public static final String TOPIC_E_CREATOR_NAME = "etopic_creator_name";
	public static final String TOPIC_E_CHECK_RESON = "etopic_check_reson";
	public static final String TOPIC_E_CHECK_STATE = "etopic_check_state";
	public static final String TOPIC_E_TOPIC_NO = "etopic_topic_no";
	public static final String TOPIC_E_MEET_NO = "etopic_meet_no";
	public static final String TOPIC_E_VOTE_STARTTIME = "etopic_vote_starttime";
	public static final String TOPIC_E_VOTE_ENDTIME = "etopic_vote_endtime";
	public static final String TOPIC_E_IS_NEEDVOTE = "etopic_is_needvote";
	public static final String TOPIC_E_IS_NEEDMEETCALL = "etopic_is_needmeetcall";
	public static final String TOPIC_E_VOTETYPE = "etopic_votetype";
	public static final String TOPIC_E_IS_ABSTAIN = "etopic_is_abstain";
	public static final String TOPIC_E_MANAGER_ID = "etopic_manager_id";
	public static final String TOPIC_E_VOTEMNG_ID = "etopic_votemng_id";
	public static final String TOPIC_E_SUMMNG_ID = "etopic_sumng_id";
	public static final String TOPIC_E_MANAGER_NAME = "etopic_manager_name";
	public static final String TOPIC_E_VOTEMNG_NAME = "etopic_votemng_name";
	public static final String TOPIC_E_SUMMNG_NAME = "etopic_summng_name";
	public static final String TOPIC_E_ID = "etopic_id";
	public static final String TOPIC_E_SUGGESTED_GROUP_NAMES = "topic_suggested_group_names";
	public static final String TOPIC_E_SUGGESTED_GROUP_IDS = "topic_suggested_group_ids";
	public static final String TOPIC_E_SOURCE = "etopic_source";

	// page3
	public static final String TOPIC_E_RNO = "etopic_rno";
	public static final String TOPIC_E_ATTACHMENT = "etopic_attachment";
	/* end topic share preference for edit/create full topic */	
	
	public static final String CONF_JOINMEMBER_LIST = "conference_joinmember_list";

	public static final int TOPIC_STATE_CHECK_REJECT = 0;
	public static final int TOPIC_STATE_TO_CHECK = 1;
	public static final int TOPIC_STATE_CHECK_PASS = 2;
	
	public static final int TOPIC_CTRL_STATE_TOSTART = 0;
	public static final int TOPIC_CTRL_STATE_STARTED = 1;
	public static final int TOPIC_CTRL_STATE_ENDED = 2;
	
	public static final String VOTE_TYPE = "vote_type";
	
	
	/* group share preference begin */
	public static final String GROUP_NODE = "group_node";
	public static final String GROUP_NAME = "group_name";
	public static final String GROUP_REMARK = "group_remark";
	public static final String GROUP_MEMBER_LIST = "group_member_list";
	/* group share preference end */
	
	/* data share preference begin , 跨越多个activity传递数据 */
	public static final String DATA_NODE = "data_node";
	public static final String DATA_ID = "data_id";
	public static final String DATA_TITLE = "data_title";
	public static final String DATA_KEYS = "data_keys";
	public static final String DATA_STARTDATE = "data_startdate";
	public static final String DATA_ENDDATE = "data_enddate";
	public static final String DATA_TYPE_NEWADDED = "data_type";
	/* data share preference end */
	public static final String CONF_TIME_NODE = "conf_time_node";
	public static final String CONF_START_TIME = "data_conf_start_time";
	public static final String CONF_END_TIME = "data_conf_end_time";
	public static final String IN_MEET_TOPIC_FLAG = "in_meet_topic_flag";
	
}
