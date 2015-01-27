package cn.wislight.meetingsystem.ui.conference;
import android.view.View;
import android.widget.Toast;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.ui.VoteAcitivity;
import cn.wislight.meetingsystem.ui.vote.VoteManagementActivity;
import cn.wislight.meetingsystem.ui.vote.VoteResultActivity;
import cn.wislight.meetingsystem.ui.vote.VoteWillActivity;
import cn.wislight.meetingsystem.util.IntentUtil;


/**
 * @author Administrator
 *	议题详情
 */
public class ConfDetailActivity extends BaseActivity {

	@Override
	public void initView() {

	}

	@Override
	public void setupView() {
		setContentView(R.layout.conference_detail_main);
	}
/*
	public void  clickTimeupdate(View view){
		IntentUtil.startActivity(this,TopicUpdatetimeActivity.class);
		Toast.makeText(this, "timeupdate", 100).show();
	}
	public void  clickStart(View view){
		IntentUtil.startActivity(this,TopicAddOneActivity.class);
		Toast.makeText(this, "start", 100).show();
	}
	public void  clickDianming(View view){
		IntentUtil.startActivity(this,TopicUnArrivalActivity.class);
		Toast.makeText(this, "dianming", 100).show();
	}
	*/
	public void  clickStartvote(View view){
		IntentUtil.startActivity(this,VoteWillActivity.class);
		Toast.makeText(this, "startvote", 100).show();
	}
	public void  clickVoteresult(View view){
		IntentUtil.startActivity(this,VoteResultActivity.class);
		Toast.makeText(this, "voteresult", 100).show();
	}	
	
	
	
	public void  clickVotemanager(View view){
		IntentUtil.startActivity(this,VoteManagementActivity.class);
		Toast.makeText(this, "manager", 100).show();
	}
	
	public void  clickVoteacticity(View view){
		IntentUtil.startActivity(this,VoteAcitivity.class);
		Toast.makeText(this, "manager", 100).show();
	}
	/*
	public void  clickTopicvirtual(View view){
		IntentUtil.startActivity(this,TopicVirtualActivity.class);
		Toast.makeText(this, "manager", 100).show();
	}
	*/
	
	
	
	public void  clickEnd(View view){
		IntentUtil.startActivity(this,VoteManagementActivity.class);
		Toast.makeText(this, "end", 100).show();
	}
}
