package cn.wislight.meetingsystem.ui.setting;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.ui.topic.TopicAddTwoActivity;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.IntentUtil;
import cn.wislight.meetingsystem.util.Variables;

/**
 * @author Administrator
 * 创建群组 step1
 */
public class CommonGroupAddOneActivity extends BaseActivity {
	private SharedPreferences group;
	private EditText etGname;
	private EditText etGremark;
	
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		group = this.getSharedPreferences(Constants.GROUP_NODE+ Variables.loginname, MODE_PRIVATE);
		etGname = (EditText)findViewById(R.id.et_gname);
		etGremark = (EditText)findViewById(R.id.et_gremark);
		
		String group_name = group.getString(Constants.GROUP_NAME, "");
		String group_remark = group.getString(Constants.GROUP_REMARK, "");	
		
		etGname.setText(group_name);
		etGremark.setText(group_remark);
		
	}

	@Override
	public void setupView() {
		setContentView(R.layout.system_setting_common_group_addone);
		
	}
	
	protected void onPause(){
		super.onPause();
        Editor editor = group.edit();

        String group_name = etGname.getText().toString();
        String group_remark = etGremark.getText().toString();
        editor.putString(Constants.GROUP_NAME, group_name);
        editor.putString(Constants.GROUP_REMARK, group_remark);
        editor.commit();
	}
	
	public void  clickNext(View view){
		if (etGname.getText().toString().length() == 0){
			Toast.makeText(CommonGroupAddOneActivity.this, getString(R.string.error_no_group_name), Toast.LENGTH_SHORT).show();
			return;
		}
		IntentUtil.startActivity(this,CommonGroupAddTwoActivity.class);
		this.finish();
	}

}
