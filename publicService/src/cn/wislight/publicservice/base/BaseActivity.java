package cn.wislight.publicservice.base;

import java.io.File;

import cn.wislight.publicservice.ui.LoginActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.os.StatFs;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * 公共Activity
 * @author Administrator
 *
 */
public abstract class BaseActivity extends Activity{
	
	private Handler handler_jump;
	private PowerManager.WakeLock wakeLock;
	public static String userinfo = "userinfo_pref";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setNoTitle(this);
		setUpView();
		setListener();
	}

	/**
	 * 初始化界面
	 */
	public abstract void setUpView();
	
	/**
	 * 事件监听
	 */
	public abstract void setListener();
	
	
	/**
	 *跳转界面 
	 * @param clazz  跳转的Activity
	 * @param finish  是否销毁
	 */
	public void gotoActivity(Class<? extends Activity> clazz, boolean finish)
	{
		Intent intent = new Intent(this, clazz);
		startActivity(intent);
		if (finish)
			finish();
	}

	/**
	 * 延迟跳转
	 * @param mills  延迟时间
	 * @param clazz  跳转的Activity
	 * @param finish  是否销毁
	 */
	public void CountJump(long mills, final Class<? extends Activity> clazz, final boolean finish)
	{
		this.handler_jump=new Handler();
		this.handler_jump.postDelayed(new Runnable() {

			@Override
			public void run() {
				BaseActivity.this.gotoActivity(clazz, finish);
			}
		}, mills);
	}

	/**
	 * 
	 * 传递bundle的跳转
	 * @param clazz  要跳转的Activity
	 * @param finish 是否销毁
	 * @param pBundle 携带的bundle
	 */
	public void gotoActivity(Class<? extends Activity> clazz, boolean finish, Bundle pBundle)
	{
		Intent intent = new Intent(this, clazz);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
		if (finish)
			finish();
	}

	
	/**
	 * 带切换动画的跳转
	 * @param animIn		进入动画
	 * @param animOut		跳出动画
	 */
	public void finish(int animIn, int animOut)
	{
		super.finish();
		overridePendingTransition(animIn, animOut);
	}

	/**
	 * 设置禁止弹出软键盘操作
	 */
	public void disableSoftkeyBoardAutoShow()
	{
		getWindow().setSoftInputMode(
				2);
	}

	/**
	 * 保持界面唤醒状态显示
	 */
	public void keepScreenOn()
	{
		getWindow().addFlags(128);
	}



	private static String intToIp(int i)
	{
		return (i & 0xFF) + "." + (i >> 8 & 0xFF) + "." + (i >> 16 & 0xFF) + 
				"." + (i >> 24 & 0xFF);
	}

	/**
	 * 隐藏软键盘
	 * @param view 
	 */
	public void hideSoftKeyboard(View view) {
		if (view != null) {
			InputMethodManager imm = (InputMethodManager)getSystemService("input_method");
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	/**
	 * 弹出Toast提示,其他的几个构造方法类似用法
	 * @param stringRes	设置提示的字符
	 * @param duration	间隔时间
	 */
	public void showToast(int stringRes, int duration) {
		Toast.makeText(this, stringRes, duration).show();
	}

	public void showToast(String stringRes, int duration) {
		Toast.makeText(this, stringRes, duration).show();
	}

	public void showToast(int stringRes) {
		Toast.makeText(this, stringRes, 0).show();
	}

	public void showToast(String stringRes) {
		Toast.makeText(this, stringRes, 0).show();
	}

	/**
	 * 设置SharedPreferences名,不设置时默认为userinfo
	 * @param key
	 * @param value
	 */
	public void savePreferenceBoolean(String key, boolean value)
	{
		SharedPreferences preferences = getSharedPreferences(userinfo, 
				0);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	/**
	 * 读取一个key Boolean值方法  默认为false
	 * @param key
	 * @return
	 */
	public boolean getPreferenceBoolean(String key) {
		SharedPreferences preferences = getSharedPreferences(userinfo, 
				0);
		return preferences.getBoolean(key, false);
	}

	/**
	 * 保存一个key方法
	 * @param key
	 * @param value
	 */
	public void savePreferenceString(String key, String value) {
		SharedPreferences preferences = getSharedPreferences(userinfo, 
				0);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * 删除一个key
	 * @param key
	 */
	public void deleteKey(String key) {
		SharedPreferences preferences = getSharedPreferences(userinfo, 
				0);
		SharedPreferences.Editor editor = preferences.edit();
		editor.remove(key);
		editor.commit();
	}
	
	/**
	 * 读取一个key Long值方法  默认Mode 为0
	 * @param key
	 * @return
	 */
	public void savePreferenceLong(String key, long value) {
		SharedPreferences preferences = getSharedPreferences(userinfo, 
				0);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public String getPreferenceString(String key) {
		SharedPreferences preferences = getSharedPreferences(userinfo, 
				0);
		return preferences.getString(key, "");
	}

	public long getPreferenceLong(String key) {
		SharedPreferences preferences = getSharedPreferences(userinfo, 
				0);
		return preferences.getLong(key, 0L);
	}

	/**
	 * 设置竖屏显示
	 */
	public void screenPortraitDir()
	{
		setRequestedOrientation(1);
	}

	/**
	 * 设置横屏显示
	 */
	public void screenLandscapeDir() {
		setRequestedOrientation(0);
	}

	/**
	 * 检测sdcard是否可用
	 * @return
	 */
	public static boolean sdCardIsAvailable()
	{
		String status = Environment.getExternalStorageState();

		return status.equals("mounted");
	}
	
	
	/**
	 * 检测sD 卡是否有足够空间
	 * @param updateSize
	 * @return
	 */
	public static boolean enoughSpaceOnSdCard(long updateSize)
	{
		String status = Environment.getExternalStorageState();
		if (!status.equals("mounted"))
			return false;
		return updateSize < getRealSizeOnSdcard();
	}

	/**
	 * 获取SD 卡大小
	 * @return
	 */
	public static long getRealSizeOnSdcard()
	{
		File path = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath());
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	/**
	 * 获取内存手否有足够空间
	 * @param updateSize
	 * @return
	 */
	public static boolean enoughSpaceOnPhone(long updateSize)
	{
		return getRealSizeOnPhone() > updateSize;
	}

	/**
	 * 获取内存大小
	 * @return
	 */
	public static long getRealSizeOnPhone()
	{
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		long realSize = blockSize * availableBlocks;
		return realSize;
	}

	/**
	 * 根据手机分辨率从dp转成px
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dip2px(Context context, float dpValue)
	{
		float scale = context.getResources().getDisplayMetrics().density;
		return (int)(dpValue * scale + 0.5F);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(Context context, float pxValue)
	{
		float scale = context.getResources().getDisplayMetrics().density;
		return (int)(pxValue / scale + 0.5F) - 15;
	}

	/**
	 * 获取APP的版本名：versionName
	 * @return
	 */
	public String getVersion() {
		try {
			PackageManager manager = getPackageManager();
			PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
		}return "1.0";
	}

	/**
	 * // 获取APP的版本号：versionCode
	 * @return
	 */
	public int getVersionCode()
	{
		try {
			PackageManager manager = getPackageManager();
			PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
			int versionCode = info.versionCode;
			return versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}return 1;
	}
	
	
	
	

	
	/* 
	 * 双击退出
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onKeyUp(int, android.view.KeyEvent)
	 */
	private long firstTime = 0;
//	@Override
//	public boolean onKeyUp(int keyCode, KeyEvent event) {
//
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			long secondTime = System.currentTimeMillis();
//			if (secondTime - firstTime > 800) {
//				Toast.makeText(getApplicationContext(), "再按一次返回键退出...", 1000).show();
//				firstTime = secondTime;
//				return true;
//			} else {
//				System.exit(0);
//
//			}
//		}
//		return super.onKeyUp(keyCode, event);
//	}
	
	
	/**
	 * 无标题兰
	 * @param ct
	 */
	public static void setNoTitle(Context ct)
	{
		((Activity)ct).getWindow().requestFeature(1);
	}

	/**
	 * 全屏
	 * @param ct
	 */
	public static void setFullScreen(Context ct) {
		setNoTitle(ct);
		((Activity)ct).getWindow().addFlags(
				1024);
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/*
		 * 
		 * add()方法的四个参数，依次是：
		 * 
		 * 1、组别，如果不分组的话就写Menu.NONE,
		 * 
		 * 2、Id，这个很重要，Android根据这个Id来确定不同的菜单
		 * 
		 * 3、顺序，那个菜单现在在前面由这个参数的大小决定
		 * 
		 * 4、文本，菜单的显示文本
		 */

		//menu.add(Menu.NONE, Menu.FIRST + 1, 5, "重新登录").setIcon(

		//android.R.drawable.ic_menu_edit);

		// setIcon()方法为菜单设置图标，这里使用的是系统自带的图标，同学们留意一下,以

		// android.R开头的资源是系统提供的，我们自己提供的资源是以R开头的

		menu.add(Menu.NONE, Menu.FIRST + 2, 2, "退出").setIcon(

		android.R.drawable.ic_menu_send);

		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST + 1:
			Toast.makeText(this, "重新登录", Toast.LENGTH_LONG).show();
			Intent loginIntent = new Intent(this, LoginActivity.class);
			startActivity(loginIntent);
			finish();
			break;
		case Menu.FIRST + 2:
			Toast.makeText(this, "退出", Toast.LENGTH_LONG).show();
			System.exit(0);
			break;
		}

		return false;

	}
	
	public void clickBackToFormer(View view){
		this.finish();
	}
}
