package cn.wislight.meetingsystem.util;

import com.loopj.android.http.*;

public class MeetingSystemClient {
	private static String BASE_URL = Constants.BASE_URL;

	public static String getBASE_URL() {
		return BASE_URL;
	}

	public static void setBASE_URL(String bASE_URL) {
		BASE_URL = bASE_URL;
	}

	private static AsyncHttpClient client = new AsyncHttpClient();

	public static void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}

	public static void post(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}

	public static AsyncHttpClient getClient() {
		return client;
	}

	public static void setCookieStore(PersistentCookieStore cookieStore) {
		client.setCookieStore(cookieStore);
	}

	private static String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}
}