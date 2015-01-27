package cn.wislight.publicservice.ui.commercialtenant;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.wislight.publicservice.R;
import cn.wislight.publicservice.adapter.CommonServerAdapter;
import cn.wislight.publicservice.adapter.FindbuinessGrabBillAdapter;
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.entity.GrabBillEntity;
import cn.wislight.publicservice.ui.user.goverment.UserCenterSetMessageActivity;
import cn.wislight.publicservice.ui.user.goverment.UserGovermentSettingActivity;

/**
 * 首页 公众服务平台
 * 
 * @author Administrator
 * 
 */
public class CommonServerActivity extends BaseActivity implements
		OnClickListener {
	private ViewPager mViewPager;
	private CommonServerAdapter myPagerAdapter;
	private TextView findBuiness; 
	private TextView startThing;
	private TextView myBuy;
	private TextView setting;
	private List<View> listViews;
	private View grabBill;
	private ListView gradBillList;
	// 抢单
	private FindbuinessGrabBillAdapter gradBillAdapter;
	private ArrayList<GrabBillEntity> GrabBillData;
	private GrabBillEntity grabBillEntity;
	private long mExitTime;
		
	private ImageButton imagebtnUserDetaile;

	@Override
	public void setUpView() {
		setContentView(R.layout.activity_common_server_commercialtenant);
		imagebtnUserDetaile = (ImageButton) findViewById(R.id.imagebtn_userdetatail);
				
		findBuiness = (TextView) findViewById(R.id.find_buiness);
		startThing = (TextView) findViewById(R.id.start_thing);
		myBuy = (TextView) findViewById(R.id.my_buy);
		setting = (TextView) findViewById(R.id.setting);
	}

	@Override
	public void setListener() {
		findBuiness.setOnClickListener(this);
		startThing.setOnClickListener(this);
		myBuy.setOnClickListener(this);
		setting.setOnClickListener(this);
		imagebtnUserDetaile.setOnClickListener(this);
	}

	/*
	 * 
	 * 初始化ViewPager
	 */
	private void initViewPager() {
		listViews = new ArrayList<View>();
		LayoutInflater myInflater = getLayoutInflater();
		grabBill = myInflater.inflate(R.layout.list_grabbill, null);
		listViews.add(grabBill);
		myPagerAdapter = new CommonServerAdapter(listViews);
		mViewPager.setAdapter(myPagerAdapter);
		mViewPager.setCurrentItem(0);
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		initListener();
	}

	private void initListener() {
		gradBillList = (ListView) grabBill.findViewById(R.id.listView_grabbill);
		GrabBillData = new ArrayList<GrabBillEntity>();
		for (int i = 0; i < 10; i++) {
			grabBillEntity = new GrabBillEntity();
			grabBillEntity.setName("");
		}
	}

	/* 页卡切换监听 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int position) {
			// 切换时按钮背景的变化
			// item=position;
			// switch (item) {
			// case 0:
			// tvSwitch(getResources().getColor(R.color.orange),
			// getResources().getColor(R.color.black),
			// getResources().getColor(R.color.black),
			// getResources().getColor(R.color.black),
			// getResources().getColor(R.color.black));
			// SliderUtil.moveSlider(mIvSlideUnderLine, distance, 0);
			// break;
			// case 1:
			// tvSwitch(getResources().getColor(R.color.black),
			// getResources().getColor(R.color.orange),
			// getResources().getColor(R.color.black),
			// getResources().getColor(R.color.black),
			// getResources().getColor(R.color.black));
			// SliderUtil.moveSlider(mIvSlideUnderLine, 0, distance);
			//
			// break;
			// case 2:
			// tvSwitch(getResources().getColor(R.color.black),
			// getResources().getColor(R.color.black),
			// getResources().getColor(R.color.orange),
			// getResources().getColor(R.color.black),
			// getResources().getColor(R.color.black));
			// SliderUtil.moveSlider(mIvSlideUnderLine, distance, 2*distance);
			//
			// break;
			//
			// case 3:
			//
			// if (fromView.equals("city")) {
			// tvSwitch(getResources().getColor(R.color.black),
			// getResources().getColor(R.color.black),
			// getResources().getColor(R.color.black),
			// getResources().getColor(R.color.black),
			// getResources().getColor(R.color.orange));
			// }else{
			// tvSwitch(getResources().getColor(R.color.black),
			// getResources().getColor(R.color.black),
			// getResources().getColor(R.color.black),
			// getResources().getColor(R.color.orange),
			// getResources().getColor(R.color.black));
			// }
			// SliderUtil.moveSlider(mIvSlideUnderLine, 2*distance, 3*distance);
			//
			// break;
			// case 4:
			// SliderUtil.moveSlider(mIvSlideUnderLine, 3*distance, 4*distance);
			// tvSwitch(getResources().getColor(R.color.black),
			// getResources().getColor(R.color.black),
			// getResources().getColor(R.color.black),
			// getResources().getColor(R.color.black),
			// getResources().getColor(R.color.orange));
			// break;
			// default:
			// break;
			// }
			//
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.find_buiness:
			Intent intent = new Intent(CommonServerActivity.this,
					FindBuinessActivity.class);
			startActivity(intent);
			break;
		case R.id.start_thing:
			Intent intent1 = new Intent(CommonServerActivity.this,
					InitiateBusinessAffairActivity.class);
			startActivity(intent1);
			break;
		case R.id.my_buy:
			Intent intent3 = new Intent(CommonServerActivity.this,
					FindBuinessActivity.class);
			startActivity(intent3);
			break;
		case R.id.setting:
			Intent intent4 = new Intent(CommonServerActivity.this,
					UserGovermentSettingActivity.class
					);
			startActivity(intent4);
			break;
			
		case R.id.imagebtn_userdetatail:
			Intent intent5 = new Intent(CommonServerActivity.this,
					UserCenterSetMessageActivity.class
					);
			startActivity(intent5);
			break;
		default:
			break;
		}
	}

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Object mHelperUtils;
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			} else {
				finish();
				//Toast.makeText(this, "退出程序", Toast.LENGTH_SHORT).show();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	

}
