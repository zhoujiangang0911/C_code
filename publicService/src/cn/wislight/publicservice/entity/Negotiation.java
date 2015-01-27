package cn.wislight.publicservice.entity;

public class Negotiation {

	public static class STATE {
		public static final String DAIYIJIA ="1";
		public static final String YICHUJIA ="2";
		public static final String YICHENGJIAO ="3";
		public static final String SENDER_GIVIUP ="4";
		public static final String RECEIVER_GIVEUP ="5";
		public static final String OTHERS_DEALED ="6";
	}
	public static class CONFIRMTYPE {
		public static final String APP ="APP确认";
		public static final String VOIP ="VOIP确认";
	}
}
