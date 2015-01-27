package cn.wislight.meetingsystem.base;



import cn.wislight.meetingsystem.MainActivity;
import cn.wislight.meetingsystem.ui.LoginActivity;
import cn.wislight.meetingsystem.ui.setting.SettingActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import android.app.Service;
import cn.wislight.meetingsystem.service.*;

public abstract class BaseActivity extends Activity{
	public abstract void initView();
	public abstract void setupView();
	
	private final 	int NETCONNECTED=1;
	private final 	int DISNETCONNECTED=0;
	
	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case DISNETCONNECTED:
				Toast.makeText(BaseActivity.this, "网络以断开", 0).show();
				break;
			case NETCONNECTED:
				Toast.makeText(BaseActivity.this, "网络已连接", 0).show();
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setupView();
		initView();
	}

	public void towardActivity(Context context,Class<?> clazz,Bundle bundle){
		Intent intent=new Intent(context,clazz);
		intent.putExtras(bundle);
		startActivity(intent);
		
	}
	public void towardActivity(Context context,Class<?> clazz){
		Intent intent=new Intent(context,clazz);
		startActivity(intent);
	}
	
	public void clickBackToFormer(View view){
		this.finish();
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

		menu.add(Menu.NONE, Menu.FIRST + 1, 5, "重新登录").setIcon(

		android.R.drawable.ic_menu_edit);

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
			Intent intent = new Intent();
			intent.setClass(BaseActivity.this, restartApp.class);
			startService(intent);
			break;
		case Menu.FIRST + 2:
			Toast.makeText(this, "退出", Toast.LENGTH_LONG).show();
			System.exit(0);
			break;
		}

		return false;

	}
}
