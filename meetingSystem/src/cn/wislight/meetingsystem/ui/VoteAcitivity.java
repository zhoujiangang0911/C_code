package cn.wislight.meetingsystem.ui;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DialerFilter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.adapter.VoteRealNameAdapter;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.util.SliderUtil;


public class VoteAcitivity extends BaseActivity implements OnClickListener{
	private ImageView mIvUnderLine;
	private TextView mTvRealName;
	private TextView mTvAnonymous;
	private int distance;
	private ListView mLvRealName;
	private VoteRealNameAdapter mAdapter;
	private LinearLayout mLlRealName;
	private LinearLayout mLlAnonymous;
	@Override
	public void initView() {
		mIvUnderLine=(ImageView) findViewById(R.id.iv_slide_underline);
		mTvAnonymous=(TextView) findViewById(R.id.tv_vote_anonymous);
		mTvRealName=(TextView) findViewById(R.id.tv_vote_real_name);
		mLvRealName=(ListView) findViewById(R.id.lv_vote_realName);
		mLlRealName=(LinearLayout) findViewById(R.id.ll_vote_real_names);
		mLlAnonymous=(LinearLayout) findViewById(R.id.ll_vote_anonymous);
		mAdapter=new VoteRealNameAdapter(this);
		mLvRealName.setAdapter(mAdapter);
		initListener();
		initRealNameDatas();
	}

	private void initListener() {
		mTvAnonymous.setOnClickListener(this);
		mTvRealName.setOnClickListener(this);
		distance=SliderUtil.getDistance(this);

	}

	@Override
	public void setupView() {
		setContentView(R.layout.vote_main);		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_vote_anonymous:
			mLlAnonymous.setVisibility(View.VISIBLE);
			mLlRealName.setVisibility(View.GONE);
			SliderUtil.moveSlider(mIvUnderLine, 0, distance);
			break;
		case R.id.tv_vote_real_name:
			mLlAnonymous.setVisibility(View.GONE);
			mLlRealName.setVisibility(View.VISIBLE);
			SliderUtil.moveSlider(mIvUnderLine, distance, 0);
			mAdapter.clear();
			initRealNameDatas() ;
			break;

		default:
			break;
		}

	}

	private void initRealNameDatas() {
		List<String> list=new ArrayList<String>();
		list.add("tangseng");
		list.add("八戒");
		list.add("唐僧");
		list.add("啥事");
		//mAdapter.addDatas(list);
		mAdapter.notifyDataSetChanged();
	}
}
