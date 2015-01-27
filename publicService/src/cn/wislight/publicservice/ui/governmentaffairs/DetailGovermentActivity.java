package cn.wislight.publicservice.ui.governmentaffairs;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import cn.wislight.publicservice.R;
import cn.wislight.publicservice.adapter.ViewPagerAdapter;
import cn.wislight.publicservice.base.BaseActivity;

/**
 * 代办详细页界面
 * @author Administrator
 *
 */
public class DetailGovermentActivity extends BaseActivity{

	private ViewPager viewPager;

	@Override
	public void setUpView() {
		setContentView(R.layout.activity_detail_goverment);

		List<View> list=initView();
		viewPager=(ViewPager)findViewById(R.id.viewPager);

		viewPager.setAdapter(new ViewPagerAdapter(list));
	}

	private List<View> initView() {
		List<View> list=new ArrayList<View>();
		for (int i = 0; i <5; i++) {
			View inflate = getLayoutInflater().inflate(R.layout.proxyhandle_item1, null);
			list.add(inflate);
		}
		return list;
	}

	@Override
	public void setListener() {
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

}
