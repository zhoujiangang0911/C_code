package cn.wislight.publicservice.entity;

public class GovermentAffair {
	public static class STATE{
		public static final String DEFAULT = "0"; //延期
		public static final String DRAFT = "1";     //正在处理
		public static final String DAIJIESHOU = "2"; // 待接受
		public static final String CHULIZHONG = "3"; // 处理中
		public static final String YIWANJIE= "4"; // 以完结
		public static final String YIPINGJIA = "5"; // 以评价
		public static final String YIGUIDANG = "6"; // 以归档
	}
}
