package cn.wislight.meetingsystem.ui.topic;

import java.util.ArrayList;
import java.util.List;

import android.widget.ListView;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.adapter.UnArrivalAdapter;
import cn.wislight.meetingsystem.base.BaseActivity;

public class TopicUnArrivalActivity extends BaseActivity{

	private UnArrivalAdapter mAdapter;
	private ListView mLv;
	private List<String> data;
	@Override
	public void initView() {
		mLv=(ListView) findViewById(R.id.lv_arrival);
		mAdapter=new UnArrivalAdapter(this);
		mLv.setAdapter(mAdapter);
		testData();
	}

	@Override
	public void setupView() {
		setContentView(R.layout.un_arrival_main);
	}

	private void testData() {
		data=new ArrayList<String>();
		for (int i = 0; i < 8; i++) {
			data.add(i+"");
		}
		mAdapter.addDatas(data);
		mAdapter.notifyDataSetChanged();

	}
}
