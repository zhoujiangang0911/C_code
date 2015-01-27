package cn.wislight.publicservice.ui.governmentaffairs;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import cn.wislight.publicservice.R;
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.ui.user.goverment.UserCenterSetMessageActivity;
import cn.wislight.publicservice.user.fragment.UserBusinessFragment;
import cn.wislight.publicservice.user.fragment.UserGovermentFragment;

/**
 *  服务者主页
 * @author Administrator
 *
 */
public class ServantMainActivity extends FragmentActivity{
	

	private RadioGroup userGovermentRadioGroup;
	private ViewPager userGovermentViewPager;
	private ImageButton userCenterImageButton;
	
	private long mExitTime;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setNoTitle();
		setUpView();
		setListener();
	}
	
	public void setUpView() {
		setContentView(R.layout.activity_servant_main);

		userCenterImageButton = (ImageButton)findViewById(R.id.userCenterImageButton);
        userGovermentRadioGroup = (RadioGroup)findViewById(R.id.user_goverment_radioGroup);
        RadioButton user_goverment_radioBt1 = (RadioButton)findViewById(R.id.user_goverment_radioBt1);
        user_goverment_radioBt1.setChecked(true);
        
        
        userGovermentViewPager = (ViewPager)findViewById(R.id.user_goverment_viewPager);
        List<Fragment> list = initData();
        UserViewPager adapter = new UserViewPager(getSupportFragmentManager(),list);
        userGovermentViewPager.setAdapter(adapter);
        userGovermentViewPager.setCurrentItem(0);
	}

	public List<Fragment> initData() {
		List<Fragment> data = new ArrayList<Fragment>();
		data.add(new ServantMainPageFragment());
		data.add(new ServantMainPageWaitToHandleListFragment());
		return data;
	}

	public void setListener() {
		userCenterImageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ServantMainActivity.this,UserCenterSetMessageActivity.class);
				startActivity(intent);
			}
		});
		userGovermentRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				switch (arg1) {
				case R.id.user_goverment_radioBt1:
					userGovermentViewPager.setCurrentItem(0);
					break;
				case R.id.user_goverment_radioBt2:
					userGovermentViewPager.setCurrentItem(1);
					break;

				default:
					break;
				}
			}
		});
		userGovermentViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				RadioButton rb = (RadioButton)userGovermentRadioGroup.getChildAt(arg0);
				rb.setChecked(true);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	public void setNoTitle()
	{
	     this.getWindow().requestFeature(1);
	}
	class UserViewPager extends FragmentPagerAdapter{
		
		private List<Fragment> list;

		public UserViewPager(FragmentManager fm,List<Fragment> list) {
			super(fm);
			// TODO Auto-generated constructor stub
			this.list = list;
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return list.get(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
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
