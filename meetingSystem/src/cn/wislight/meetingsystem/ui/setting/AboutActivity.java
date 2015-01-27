package cn.wislight.meetingsystem.ui.setting;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.widget.TextView;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;

/**
 * @author Administrator
 * 修改密码
 */
public class AboutActivity extends BaseActivity {

	private TextView tvVersion;
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		tvVersion = (TextView)findViewById(R.id.tv_version);
		String version = getVersionName();
		tvVersion.setText("此版本为:V" + version);
	}

	@Override
	public void setupView() {
		setContentView(R.layout.system_setting_about);
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
}
