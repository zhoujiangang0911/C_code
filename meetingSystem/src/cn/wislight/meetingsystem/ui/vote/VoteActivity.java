package cn.wislight.meetingsystem.ui.vote;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.ui.topic.TopicActivity;
import cn.wislight.meetingsystem.ui.topic.TopicHouseActivity;
import cn.wislight.meetingsystem.ui.topic.TopicVoteCtrlListActivity;
import cn.wislight.meetingsystem.util.Authorize;
import cn.wislight.meetingsystem.util.IntentUtil;

/**
 * @author Administrator
 *  表决界面
 */
public class VoteActivity extends BaseActivity implements OnClickListener{
	private ImageButton voteMyPast,voteMyVote,voteMyStay,voteMyControl;
	private long mExitTime;
	@Override
	public void initView() {
		voteMyStay=(ImageButton)findViewById(R.id.voteMyStay);
		voteMyVote=(ImageButton)findViewById(R.id.voteMyVote);
		voteMyPast=(ImageButton)findViewById(R.id.voteMyPast);
		voteMyControl=(ImageButton)findViewById(R.id.voteMyContrl);
		
		voteMyStay.setOnClickListener(this);
		voteMyVote.setOnClickListener(this);
		voteMyPast.setOnClickListener(this);
		voteMyControl.setOnClickListener(this);
	}

	@Override
	public void setupView() {
		setContentView(R.layout.my_vote);

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.voteMyStay:
			IntentUtil.startActivity(VoteActivity.this, VoteMyStayActivity.class);
			break;
		case R.id.voteMyVote:
			IntentUtil.startActivity(VoteActivity.this, VoteMyVoteActivity.class);
			break;
		case R.id.voteMyPast:
			//IntentUtil.startActivity(VoteActivity.this, VoteResultActivity.class);
			IntentUtil.startActivity(VoteActivity.this, VoteMyPastActivity.class);
			break;
		case R.id.voteMyContrl:
			if(Authorize.hasAuth(Authorize.AUTH_TOPIC_DRAFT_DB)){
				IntentUtil.startActivity(this, TopicVoteCtrlListActivity.class);
			} else {
				Toast.makeText(VoteActivity.this, getString(R.string.error_no_auth), Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}

}
