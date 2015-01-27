package cn.wislight.publicservice.util;

public class StringToChange {
	public static String toKong(String input) {
			String result = input.equals("null")?"":input;
			return result;
	}
	public static String toNoContent(String input) {
		String result = input.equals("null")?"没有内容":input;
		return result;
	}
	public static String toNoT(String input) {
		String result = input.replace("T", " ");
		return result;
	}
	public static String toShort(String input) {
		String result;
		if (input.length()>8) {
			result = input.substring(0, 8);
		}else {
		 result = input;
		}
		
		return result;
	}
}
