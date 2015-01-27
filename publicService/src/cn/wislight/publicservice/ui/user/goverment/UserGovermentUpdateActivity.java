package cn.wislight.publicservice.ui.user.goverment;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.wislight.publicservice.R;
import cn.wislight.publicservice.base.BaseActivity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;


/**
 * @author Administrator 版本更新
 */
public class UserGovermentUpdateActivity extends BaseActivity {
	protected static final int DOWN_ERROR = 0;
	protected static final int GET_UNDATAINFO_ERROR = 1;
	private static final int UPDATA_CLIENT = 2;
	private static final int UPDATA_NO_UPDATES = 3;

	//LoadingDialog loadingdiag;
	//UpdataInfo info;
	String versionname;
	String baseURL = null;
	String path = null;

	@Override
	public void setUpView() {
		setContentView(R.layout.system_setting_update);
		// TODO Auto-generated method stub
		//SettingsConfig cf = new SettingsConfig(this);
		//baseURL = cf.getBaseUrl() + "MeetingManage/";
		//path = baseURL + "apk/update.xml";
		
		//loadingdiag = new LoadingDialog(this);  
		//loadingdiag.setCanceledOnTouchOutside(false); 
		//loadingdiag.setText(getString(R.string.setting_check_update));
	}


	
	public void clickUpdate(View v) {
		
		//CheckVersionTask checkVersion=new CheckVersionTask();
	   //new Thread(checkVersion).start();
	   //loadingdiag.show();
		  
	}
	/*
	private String getVersionName() throws Exception {
		PackageManager packageManager = getPackageManager();
		PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),
				0);
		return packInfo.versionName;
	}

	public class CheckVersionTask implements Runnable {


		public void run() {
			try {
				
				URL url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setConnectTimeout(5000);
				InputStream is = conn.getInputStream();
				info = UpdataInfoParser.getUpdataInfo(is);

				if (info.getVersion().equals(getVersionName())) {
				
					// LoginMain();
					Message msg = new Message();
					msg.what = UPDATA_NO_UPDATES;
					handler.sendMessage(msg);
				} else {
					
					Message msg = new Message();
					msg.what = UPDATA_CLIENT;
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
			
				Message msg = new Message();
				msg.what = GET_UNDATAINFO_ERROR;
				handler.sendMessage(msg);
				e.printStackTrace();
			}
		}
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			loadingdiag.hide();
			
			switch (msg.what) {
			case UPDATA_CLIENT:
				
				showUpdataDialog();
				break;
			case GET_UNDATAINFO_ERROR:
				
				Toast.makeText(getApplicationContext(), "获取服务器更新信息失败", 1)
						.show();
				LoginMain();
				break;
			case DOWN_ERROR:
				
				Toast.makeText(getApplicationContext(), "下载新版本失败", 1).show();
				LoginMain();
				break;
			case UPDATA_NO_UPDATES:
				Toast.makeText(getApplicationContext(), "已经是最新版本", 1).show();
				
				break;
			}
		}
	};


	protected void showUpdataDialog() {
		AlertDialog.Builder builer = new Builder(this);
		builer.setTitle("版本升级");
		builer.setMessage(info.getDescription());
		builer.setPositiveButton("确定", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				downLoadApk();
			}
		});
		
		if (!"1".equals(info.getForceupdate())){
			builer.setNegativeButton("取消", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					LoginMain();
				}
			});
		}

		AlertDialog dialog = builer.create();
		dialog.show();
	}


	protected void downLoadApk() {
		final ProgressDialog pd; 
		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("正在下载更新");
		pd.show();
		new Thread() {
			@Override
			public void run() {
				try {
					File file = Download.getFileFromServer(baseURL + info.getUrl(), pd);
					sleep(3000);
					installApk(file);
					pd.dismiss(); 
				} catch (Exception e) {
					Message msg = new Message();
					msg.what = DOWN_ERROR;
					handler.sendMessage(msg);
					e.printStackTrace();
				}
			}
		}.start();
	}


	protected void installApk(File file) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

*/




	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		
	}
}
