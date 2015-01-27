package cn.wislight.publicservice.ui.user;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import cn.wislight.publicservice.R;
import cn.wislight.publicservice.ui.user.goverment.UserCenterSetMessageActivity;
import cn.wislight.publicservice.user.fragment.UserBusinessFragment;
import cn.wislight.publicservice.user.fragment.UserGovermentFragment;

/**
 * 公众服务平台
 * @author Administrator
 *
 */
public class CommonServerActivity extends FragmentActivity{
	

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
		setContentView(R.layout.activity_common_server_user);

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
		data.add(new UserGovermentFragment());
		data.add(new UserBusinessFragment());
		return data;
	}

	public void setListener() {
		userCenterImageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(CommonServerActivity.this,UserCenterSetMessageActivity.class);
				startActivity(intent);
			}
		});
		userGovermentRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
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
				// TODO Auto-generated method stub
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
