package cn.wislight.publicservice.ui.governmentaffairs;


import cn.wislight.publicservice.R;
import cn.wislight.publicservice.ui.user.ContactActivity;
import cn.wislight.publicservice.ui.user.InitiateThingActivity;
import cn.wislight.publicservice.ui.user.NearByBuinessActivity;
import cn.wislight.publicservice.ui.user.SettingActivity;
import cn.wislight.publicservice.ui.user.ThingHandleActivity;
import cn.wislight.publicservice.ui.user.goverment.CreateUserGovermentActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * 服务代办主页
 * @author Administrator
 *
 */
public class ServantMainPageFragment extends Fragment implements OnClickListener{
	
	private LinearLayout initiateGoverment;
	private LinearLayout initiatethingGoverment;
	private LinearLayout layoutGovermentaffairEnd;
	private LinearLayout nearbyGoverment;
	private LinearLayout contactGoverment;
	private LinearLayout settingGoverment;

	@Override
	public View onCreateView(LayoutInflater inflater, 
			ViewGroup container,
			Bundle savedInstanceState) {
		View inflateGoverment = inflater.inflate(
			R.layout.servant_main_page_fragment_layout,container,false);
		setViews(inflateGoverment);
		setListener();
		return inflateGoverment;
	}

	private void setListener() {
		// TODO Auto-generated method stub
		initiateGoverment.setOnClickListener(this);
		initiatethingGoverment.setOnClickListener(this);
		layoutGovermentaffairEnd.setOnClickListener(this);
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
		initiateGoverment=(LinearLayout)inflateGoverment.findViewById(R.id.hasHandled);
		initiatethingGoverment=(LinearLayout)inflateGoverment.findViewById(R.id.handling);
		layoutGovermentaffairEnd=(LinearLayout)inflateGoverment.findViewById(R.id.govermentaffairEnd);
		nearbyGoverment=(LinearLayout)inflateGoverment.findViewById(R.id.merge);
		contactGoverment=(LinearLayout)inflateGoverment.findViewById(R.id.delay);
		settingGoverment=(LinearLayout)inflateGoverment.findViewById(R.id.setting);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.hasHandled:
			
			gotoActivity(ServantHasHandledListActivity.class, false);
			break;
		case R.id.handling:
			gotoActivity(ServantHandlingListActivity.class, false);
			break;
		case R.id.govermentaffairEnd:
			gotoActivity(ServantGovermentAffairEndActivity.class, false);
			break;
		case R.id.merge:
			//gotoActivity(NearByBuinessActivity.class, false);
			gotoActivity(ServantMergeListActivity.class, false);
			break;
		case R.id.delay:
			//gotoActivity(ContactActivity.class, false);
			gotoActivity(ServantDelayListActivity.class, false);
			break;
		case R.id.setting:
			gotoActivity(ServantSettingActivity.class, false);
			break;
	}
}
}
