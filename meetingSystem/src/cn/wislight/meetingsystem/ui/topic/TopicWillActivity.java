package cn.wislight.meetingsystem.ui.topic;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.ListView;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.adapter.TopicHouseAdapter;
import cn.wislight.meetingsystem.base.BaseActivity;

/**
 * @author Administrator
 * 议题库
 */
public class TopicWillActivity extends BaseActivity {
	private ListView listView;
	private TopicHouseAdapter adapter;
	@Override
	public void initView() {
		listView=(ListView) findViewById(R.id.topic_will_lv_main);
		adapter=new TopicHouseAdapter(getData(), this);
		listView.setAdapter(adapter);
	}

	private List<Bundle>  getData() {
		ArrayList<Bundle> list=new ArrayList<Bundle>();
		for (int i = 0; i < 5; i++) {
			Bundle bundle;
			if (i==0) {
				bundle=new Bundle();
				bundle.putString("title","今日新增");
				bundle.putString("desc", "议题内容");
				bundle.putString("keywords", "议题     内容");
				bundle.putString("time", "2014-08-09-7:30---2014-08-09-9:30");
				bundle.putString("desc2", "议题内容");
				bundle.putString("keywords2", "议题     内容");
				bundle.putString("time2", "2014-08-09-7:30---2014-08-09-9:30");
			}else{
				bundle=new Bundle();
				bundle.putString("title","历史议题");
				bundle.putString("desc", "议题内容");
				bundle.putString("keywords", "议题     内容");
				bundle.putString("time", "2014-08-09-7:30---2014-08-09-9:30");
				bundle.putString("desc2", "议题内容");
				bundle.putString("keywords2", "议题     内容");
				bundle.putString("time2", "2014-08-09-7:30---2014-08-09-9:30");
			}
			list.add(bundle);
		}
		return list;
	}

	@Override
	public void setupView() {
		setContentView(R.layout.topic_will_main);
		
	}

}
