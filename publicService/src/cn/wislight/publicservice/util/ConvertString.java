package cn.wislight.publicservice.util;

public class ConvertString {
	
	public static String toSemiangle(String input) {
		if (null != input) {
			char c[] = input.toCharArray();
			for (int i = 0; i < c.length; i++) {
				if ('\u3000' == c[i]) {
					c[i] = ' ';
				} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
					c[i] = (char) (c[i] - 65248);
				}
			}
			String dbc = new String(c);
			return dbc;
		} else {
			return null;
		}
	}
	
	
}
