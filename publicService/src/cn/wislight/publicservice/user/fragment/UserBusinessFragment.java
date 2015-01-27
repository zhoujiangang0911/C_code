package cn.wislight.publicservice.user.fragment;


import cn.wislight.publicservice.R;
import cn.wislight.publicservice.ui.user.CompleteHandleActivity;
import cn.wislight.publicservice.ui.user.ContactActivity;
import cn.wislight.publicservice.ui.user.CreateBusinessAffairActivity;
import cn.wislight.publicservice.ui.user.NearByBuinessActivity;
import cn.wislight.publicservice.ui.user.SettingActivity;
import cn.wislight.publicservice.ui.user.ThingHandleActivity;
import cn.wislight.publicservice.ui.user.UserCommonlyBusinessmanListActivity;
import cn.wislight.publicservice.ui.user.goverment.UserCommonlyPersonListActivity;
import cn.wislight.publicservice.ui.user.goverment.UserGovermentSettingActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
/**
 * 发起商务主界面
 * @author Administrator
 *
 */

public class UserBusinessFragment extends Fragment implements OnClickListener{
	

	private LinearLayout initiateBusiness;
	private LinearLayout initiatethingBusiness;
	private LinearLayout overBusiness;
	private LinearLayout nearbyBusiness;
	private LinearLayout contactBusiness;
	private LinearLayout settingBusiness;

	@Override
	public View onCreateView(LayoutInflater inflater, 
			ViewGroup container,
			Bundle savedInstanceState) {
		View inflateBusiness = inflater.inflate(
			R.layout.user_business_layout,container,false);
		setViews(inflateBusiness);
		setListener();
		return inflateBusiness;
	}

	private void setListener() {
		initiateBusiness.setOnClickListener(this);
		initiatethingBusiness.setOnClickListener(this);
		overBusiness.setOnClickListener(this);
		nearbyBusiness.setOnClickListener(this);
		contactBusiness.setOnClickListener(this);
		settingBusiness.setOnClickListener(this);
	}

	public void gotoActivity(Class<? extends Activity> clazz, boolean finish)
	{
		Intent intent = new Intent(getActivity(), clazz);
		startActivity(intent);
		if (finish)
			getActivity().finish();
	}
	
	private void setViews(View inflateBusiness) {
		initiateBusiness=(LinearLayout)inflateBusiness.findViewById(R.id.initiate);
		initiatethingBusiness=(LinearLayout)inflateBusiness.findViewById(R.id.initiatething);
		overBusiness=(LinearLayout)inflateBusiness.findViewById(R.id.over);
		nearbyBusiness=(LinearLayout)inflateBusiness.findViewById(R.id.nearby);
		contactBusiness=(LinearLayout)inflateBusiness.findViewById(R.id.contact);
		settingBusiness=(LinearLayout)inflateBusiness.findViewById(R.id.setting);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.initiate:
			gotoActivity(CreateBusinessAffairActivity.class, false);
			break;
		case R.id.initiatething:
			gotoActivity(ThingHandleActivity.class, false);
			break;
		case R.id.over:
			gotoActivity(CompleteHandleActivity.class, false);
			break;
		case R.id.nearby:
			gotoActivity(NearByBuinessActivity.class, false);
			break;
		case R.id.contact:
			//gotoActivity(ContactActivity.class, false);
			gotoActivity(UserCommonlyBusinessmanListActivity.class, false);			
			break;
		case R.id.setting:
			//与政务商务设置模块功能相同
			gotoActivity(UserGovermentSettingActivity.class, false);
			break;
	}
}
}
