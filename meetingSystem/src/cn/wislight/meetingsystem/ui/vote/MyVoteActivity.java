package cn.wislight.meetingsystem.ui.vote;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.util.IntentUtil;

/**
 * @author Administrator
 *  表决界面
 */
public class MyVoteActivity extends BaseActivity implements OnClickListener{
	private ImageButton voteMypast,myVote,voteMyState,myControl;
	@Override
	public void initView() {
		voteMypast=(ImageButton)findViewById(R.id.voteMyPast);
		myVote=(ImageButton)findViewById(R.id.voteMyVote);
		voteMyState=(ImageButton)findViewById(R.id.voteMyStay);
		myControl=(ImageButton)findViewById(R.id.voteMyContrl);
		
		voteMypast.setOnClickListener(this);
		myVote.setOnClickListener(this);
		voteMyState.setOnClickListener(this);
		myControl.setOnClickListener(this);
	}

	@Override
	public void setupView() {
		setContentView(R.layout.my_vote);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.voteMyPast:
			IntentUtil.startActivity(MyVoteActivity.this, VoteResultActivity.class);
			break;
		case R.id.voteMyVote:
			IntentUtil.startActivity(MyVoteActivity.this, MyVoteActivity.class);
			break;
		case R.id.voteMyStay:
			IntentUtil.startActivity(MyVoteActivity.this, VoteMyStayActivity.class);
			break;
		case R.id.voteMyContrl:
			IntentUtil.startActivity(MyVoteActivity.this, MyVoteActivity.class);
			break;

		default:
			break;
		}
	}

}
