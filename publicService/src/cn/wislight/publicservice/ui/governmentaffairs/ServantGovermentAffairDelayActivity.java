package cn.wislight.publicservice.ui.governmentaffairs;

import java.util.Calendar;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import cn.wislight.publicservice.R;
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.util.Constants;
import cn.wislight.publicservice.util.PublicServiceClient;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class ServantGovermentAffairDelayActivity extends BaseActivity implements
		OnClickListener {
	private EditText txtManagerName;
	private EditText txtDelayReason;
	private EditText txtDelayDate;
	private Button btnChooseManager;
	private Button btnDelay;
	private Button btnChooseDate;
	private String govermentAffairId;
	private String managerId;
	private String strdate;
	private DatePicker mDatePicker;
	private int year, month, day;

	@Override
	public void setUpView() {
		setContentView(R.layout.activity_servant_govermentaffair_delay);

		txtManagerName = (EditText) findViewById(R.id.managerName);
		txtDelayReason = (EditText) findViewById(R.id.delayReason);
		txtDelayDate = (EditText) findViewById(R.id.delayDate);

		btnChooseManager = (Button) findViewById(R.id.chooseManager);
		btnDelay = (Button) findViewById(R.id.delay);
		btnChooseDate = (Button) findViewById(R.id.chooseDate);

		govermentAffairId = this.getIntent()
				.getStringExtra("govermentAffairId");
	}

	@Override
	public void setListener() {
		btnChooseManager.setOnClickListener(this);
		btnDelay.setOnClickListener(this);
		btnChooseDate.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.chooseManager:
			String managerName = txtManagerName.getText().toString();
			Intent intent = new Intent(this,
					ServantGovermentAffairFindManagerListActivity.class);
			intent.putExtra("managerName", managerName);
			startActivityForResult(intent, Constants.REQUEST_FINDMANAGER);
			break;
		case R.id.chooseDate:
			// 选择日期对话框
			AlertDialog.Builder builder = new Builder(
					ServantGovermentAffairDelayActivity.this);
			builder.setTitle("请选择日期");
			builder.setIcon(R.drawable.btn_date);
			LayoutInflater factory = LayoutInflater
					.from(ServantGovermentAffairDelayActivity.this);
			final View mDatePickerLayout = factory.inflate(
					R.layout.choice_time, null);
			builder.setView(mDatePickerLayout);
			builder.setPositiveButton("确认",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							mDatePicker = (DatePicker) mDatePickerLayout
									.findViewById(R.id.datePicker_choicetime);
							year = mDatePicker.getYear();
							month = mDatePicker.getMonth() + 1;
							day = mDatePicker.getDayOfMonth();
							strdate = year + "年" + month + "月" + day + "日";
							txtDelayDate.setText(strdate);
						}
					});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
						}
					});
			builder.create().show();
			break;

		case R.id.delay:
			delay();
			break;

		default:
			break;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == Constants.REQUEST_FINDMANAGER) {
				managerId = data.getStringExtra("managerId");
				String managerName = data.getStringExtra("managerName");
				if (managerName != null && managerName.length() > 0) {
					txtManagerName.setText(managerName);
				} else {
					txtManagerName.setText(managerId);
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void delay() {
		if (managerId == null || managerId.length() <= 0) {
			Toast.makeText(this, "管理员不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		String delayReason = txtDelayReason.getText().toString();
		if (delayReason == null || delayReason.length() <= 0) {
			Toast.makeText(this, "延期原因不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		String delayDate = txtDelayDate.getText().toString();
		if (delayDate == null || delayDate.length() <= 0) {
			Toast.makeText(this, "延期时间不能为空", Toast.LENGTH_SHORT).show();
			return;
		}

		String url = "publicservice/govermentaffair_delay.htm?json=true&govermentaffair.id="
				+ govermentAffairId
				+ "&govermentaffair.receiverid="
				+ managerId
				+ "&govermentaffair.content="
				+ delayReason
				+ "&govermentaffair.endtime=" + delayDate;
		System.out.println("wangting:" + url);
		PublicServiceClient.get(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				// loadingdiag.hide();
				Toast.makeText(ServantGovermentAffairDelayActivity.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				try {
					JSONObject jsonObject = new JSONObject(response);
					String result = jsonObject.getString("result");
					if (result.equals("success")) {
						Toast.makeText(
								ServantGovermentAffairDelayActivity.this,
								"申请延期成功", Toast.LENGTH_SHORT).show();
						finish();
					} else {
						Toast.makeText(
								ServantGovermentAffairDelayActivity.this,
								"申请延期失败", Toast.LENGTH_SHORT).show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
					// loadingdiag.hide();
					Toast.makeText(ServantGovermentAffairDelayActivity.this,
							getString(R.string.error_network),
							Toast.LENGTH_SHORT).show();
				}
			}
		});

	}
}
