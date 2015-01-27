package cn.wislight.publicservice.ui.governmentaffairs;

import cn.wislight.publicservice.R;
import cn.wislight.publicservice.base.BaseActivity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.widget.TextView;


/**
 * @author Administrator
 * 修改密码
 */
public class ServantAboutActivity extends BaseActivity {

	private TextView tvVersion;
	@Override
	public void setUpView() {
		setContentView(R.layout.system_setting_about);
		// TODO Auto-generated method stub
		tvVersion = (TextView)findViewById(R.id.tv_version);
		String version = getVersionName();
		tvVersion.setText("此版本为:V" + version);
	}



	private String getVersionName()  {
		PackageManager packageManager = getPackageManager();
		PackageInfo packInfo = null;
		String version = "";
		try {
			packInfo = packageManager.getPackageInfo(getPackageName(),
					0);
			version = packInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return version;
	}



	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		
	}
}
