package cn.wislight.meetingsystem.ui.vote;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.adapter.VoteManagerAdapter;
import cn.wislight.meetingsystem.base.BaseActivity;

/**
 * @author Administrator
 * 
 * 我的表决
 */
public class VoteManagementActivity extends BaseActivity {
	private ListView vote_mystay_listView;
	@Override
	public void initView() {
		vote_mystay_listView=(ListView)findViewById(R.id.vote_mystay_listView);
		
		vote_mystay_listView.setAdapter(new VoteManagerAdapter(VoteManagementActivity.this));
	}

	@Override
	public void setupView() {
		setContentView(R.layout.mystay_main);
	}
	
}
