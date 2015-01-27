package cn.wislight.meetingsystem.util;

import java.io.Serializable;



import cn.wislight.meetingsystem.ui.topic.TopicSelectApplicatorActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class IntentUtil {
	public static void startActivity(Context context, Class<? extends Activity> cls){
		Intent intent = new Intent(context, cls);
		context.startActivity(intent);
	}
	public static void startActivity(Context context, Class<? extends Activity> cls, Bundle bundle)
	{
		Intent intent = new Intent(context, cls);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}
	
	public static void startActivity(Context context, Class<? extends Activity> cls, Intent intent)
	{
		intent.setClass(context, cls);
		context.startActivity(intent);
	}
	
	public static void startActivityWithObject(Context context, Class<? extends Activity> cls, String key, Serializable param)
	{
		Intent intent = new Intent(context, cls);
		Bundle bundle = new Bundle();
		bundle.putSerializable(key, param);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}

	public static void startActivityForResult(Context context, Class<? extends Activity> cls, int code){
		Intent intent = new Intent(context, cls);
		((Activity) context).startActivityForResult(intent, code);  
	}
	
	public static void startActivityForResult(Context context, Class<? extends Activity> cls, int code, Intent intent){
		intent.setClass(context, cls);
		((Activity) context).startActivityForResult(intent, code);  
	}
}
