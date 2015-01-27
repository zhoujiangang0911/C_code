package cn.wislight.meetingsystem.ui.conference;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.util.IntentUtil;

/**
 * @author Administrator
 * 由我控制的会议详情
 */
public class ConMyConDetail extends BaseActivity implements OnClickListener{
	private TextView timeChange,begin,call,voteBegin,voteEnd;
	@Override
	public void initView() {
		

	}

	@Override
	public void setupView() {
		setContentView(R.layout.conference_mycon_detail);
		timeChange=(TextView) findViewById(R.id.time_change);
		begin=(TextView) findViewById(R.id.begin);
		call=(TextView) findViewById(R.id.call);
		voteBegin=(TextView) findViewById(R.id.vote_begin);
		voteEnd=(TextView) findViewById(R.id.vote_end);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.time_change:
			IntentUtil.startActivity(this, ConMyConTimeC.class);
			break;
		case R.id.begin:
			
			break;
		case R.id.call:
	
			break;
		case R.id.vote_begin:
			
			break;
		case R.id.vote_end:
	
			break;
	

		default:
			break;
		}
	}

}
