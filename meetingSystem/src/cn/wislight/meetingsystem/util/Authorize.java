package cn.wislight.meetingsystem.util;

public class Authorize {
	
	
	public static int auth_col = 20;
	public static int auth_row = 15;
	public static String[][] Auth_List = {
			{ "角色管理", "添加角色", "修改角色", "删除角色", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" }, 
			{ "岗位管理", "添加职务", "修改职务", "删除职务", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" },
			{ "用户管理", "添加用户", "修改用户", "修改密码", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" }, 
			{ "会议管理", "会议发起", "会议变更", "会议审核", "会议发布", "会议控制", "会议时间修改", "会议控制人", "会议结果", "", "", "", "", "", "", "", "", "", "", "" }, 
			{ "表决管理", "会议讨论", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" },
			{ "会议类型管理", "添加会议类型", "修改会议类型", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" }, 
			{ "议题管理", "议题控制人", "表决控制人", "表决统计人", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" }, 
			{ "日志管理", "日志导出", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" },
			{ "系统设置", "组织结构管理", "行政区域管理", "添加组织机构", "添加行政区域", "停用/启用行政区域", "停用/启用组织机构", "修改组织机构", "修改行政区域", "添加下级组织机构", "讨论区设置", "", "", "", "", "", "", "", "", "", "" }, 
			{ "我的工作", "待参加的会议", "待办事项", "已办理", "参加过的会议", "草稿箱", "", "", "", "", "", "", "", "", "", "", "", "", "", "" }, 
			{ "人事管理", "人事信息添加", "修改人事信息", "查询人事信息", "添加人事信息", "职位变更", "", "", "", "", "", "", "", "", "", "", "", "", "", "" }, 
			{ "居民管理", "添加户口信息", "添加户口成员", "变更户口成员", "变更户口信息", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" }, 
			{ "外地户口管理", "外地户口添加", "外地户口修改", "外地户口查询", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" },
			{ "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" }, 
			{ "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" } 
	};
	
	// page home
	public static final String AUTH_HOME_NEW_TOPIC_DRAFT = "";
	public static final String AUTH_HOME_WAIT_MY_HANDLE = "";
	public static final String AUTH_HOME_TOPIC_DRAFT_DB = "";
	public static final String AUTH_HOME_SCHEDULE = "";

	// page topic 
	public static final String AUTH_TOPIC_NEW_DRAFT = "";
	public static final String AUTH_TOPIC_CHECK = "会议审核";
	public static final String AUTH_TOPIC_MY_CREATED = "";
	public static final String AUTH_TOPIC_DRAFT_DB = "";
	
	// page conference
	public static final String AUTH_CONF_NEW = "会议发起";
	public static final String AUTH_CONF_CHECK = "会议审核";
	public static final String AUTH_CONF_PUBLISH = "会议发布";
	public static final String AUTH_CONF_CHANGE = "会议变更";
	public static final String AUTH_CONF_SCHEDULE = "";
	public static final String AUTH_CONF_HISTORY = "";
	public static final String AUTH_CONF_CONTROL = "会议控制;会议控制人;议题控制人;表决控制人;表决统计人";
	public static final String AUTH_CONF_TOPIC_CTRL = "会议控制;表决管理;会议讨论;议题控制人;表决控制人;表决统计人";
	public static final String AUTH_CONF_VOTE_CTRL = "会议控制;表决管理;议题控制人;表决控制人;表决统计人";
	
	// page vote
	public static final String AUTH_VOTE_CTRL = "表决管理;议题控制人;表决控制人;表决统计人";
	public static boolean hasAuth(String Authname){

		if ("".equals(Authname)){
			return true;
		}
		if (Variables.auth == null){
			return false;
		}
		
		String[] sauth =Authname.split(";");
		boolean bAuth = false;
		for (String auth: sauth){
			int iIndex = GetAuthIndex(auth);
			if (iIndex <= -1) {
				bAuth = false;
			}
			if (Variables.auth[iIndex] != 1) {
				bAuth = false;
			}else{
				bAuth = true;
				break;
			}
		}
		return bAuth;
	}
	
	private static int GetAuthIndex(String optName) {
		int result = -1;
		int i1 = -1;
		int j1 = -1;
		outer: for (int i = 0; i < auth_row; i++) {
			for (int j = 0; j < auth_col; j++) {
				if (Auth_List[i][j].equals(optName)) {
					i1 = i;
					j1 = j;
					break outer;
				}
			}
		}
		if (i1 != -1 && j1 != -1) {
			result = i1 * auth_col + j1;
		}
		return result;
	}
}
