package cn.wislight.publicservice.user.fragment;


import cn.wislight.publicservice.R;
import cn.wislight.publicservice.ui.user.NearByBuinessActivity;
import cn.wislight.publicservice.ui.user.UserCommonlyBusinessmanListActivity;
import cn.wislight.publicservice.ui.user.goverment.CreateUserGovermentActivity;
import cn.wislight.publicservice.ui.user.goverment.UserCommonlyPersonListActivity;
import cn.wislight.publicservice.ui.user.goverment.UserEndGovermentAffairActivity;
import cn.wislight.publicservice.ui.user.goverment.UserGovermentSettingActivity;
import cn.wislight.publicservice.ui.user.goverment.UserHandlingGovermentAffairActivity;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
/*
 * 发起政务主页
 */

public class UserGovermentFragment extends Fragment implements OnClickListener{
	
	private LinearLayout initiateGoverment;
	private LinearLayout initiatethingGoverment;
	private LinearLayout overGoverment;
	private LinearLayout nearbyGoverment;
	private LinearLayout contactGoverment;
	private LinearLayout settingGoverment;

	@Override
	public View onCreateView(LayoutInflater inflater, 
			ViewGroup container,
			Bundle savedInstanceState) {
		View inflateGoverment = inflater.inflate(
			R.layout.user_goverment_layout,container,false);
		setViews(inflateGoverment);
		setListener();
		return inflateGoverment;
	}

	private void setListener() {
		// TODO Auto-generated method stub
		initiateGoverment.setOnClickListener(this);
		initiatethingGoverment.setOnClickListener(this);
		overGoverment.setOnClickListener(this);
		nearbyGoverment.setOnClickListener(this);
		contactGoverment.setOnClickListener(this);
		settingGoverment.setOnClickListener(this);
	}

	public void gotoActivity(Class<? extends Activity> clazz, boolean finish)
	{
		Intent intent = new Intent(getActivity(), clazz);
		startActivity(intent);
		if (finish)
			getActivity().finish();
	}
	
	private void setViews(View inflateGoverment) {
		// TODO Auto-generated method stub
		initiateGoverment=(LinearLayout)inflateGoverment.findViewById(R.id.initiateGovermentAffair);
		initiatethingGoverment=(LinearLayout)inflateGoverment.findViewById(R.id.handlingGovermentAffair);
		overGoverment=(LinearLayout)inflateGoverment.findViewById(R.id.endGovermentAffair);
		nearbyGoverment=(LinearLayout)inflateGoverment.findViewById(R.id.nearbyServants);
		contactGoverment=(LinearLayout)inflateGoverment.findViewById(R.id.commanlyServants);
		settingGoverment=(LinearLayout)inflateGoverment.findViewById(R.id.setting);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.initiateGovermentAffair:
			gotoActivity(CreateUserGovermentActivity.class, false);
			break;
		case R.id.handlingGovermentAffair:
			gotoActivity(UserHandlingGovermentAffairActivity.class, false);
			break;
		case R.id.endGovermentAffair:
			//gotoActivity(ThingHandleActivity.class, false);
			gotoActivity(UserEndGovermentAffairActivity.class, false);
			break;
		case R.id.nearbyServants:
			gotoActivity(NearByBuinessActivity.class, false);
			break;
		case R.id.commanlyServants:
			//gotoActivity(ContactActivity.class, false);
			gotoActivity(UserCommonlyPersonListActivity.class, false);
			//gotoActivity(UserCommonlyBusinessmanListActivity.class, false);
			break;
		case R.id.setting:
			gotoActivity(UserGovermentSettingActivity.class, false);
			break;
	}
}
}
