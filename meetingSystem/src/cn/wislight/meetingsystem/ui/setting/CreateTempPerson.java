package cn.wislight.meetingsystem.ui.setting;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpResponseHandler;

import android.content.Intent;
import android.text.InputType;
import android.text.method.NumberKeyListener;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.base.BaseActivity;
import cn.wislight.meetingsystem.util.Constants;
import cn.wislight.meetingsystem.util.Element;
import cn.wislight.meetingsystem.util.LoadingDialog;
import cn.wislight.meetingsystem.util.MeetingSystemClient;
import cn.wislight.meetingsystem.util.SliderUtil;

/**
 * @author Administrator 会议发起
 */
public class CreateTempPerson extends BaseActivity {
	private EditText etName, etOrg, etPost, etPhone, etCardId;
	private RadioGroup rgSex;
	private RadioButton rbSex1;
	private RadioButton rbSex2;
	private LoadingDialog loadingdiag;

	@Override
	public void initView() {

		etName = (EditText) findViewById(R.id.et_temp_attender_name);
		etName.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		etOrg = (EditText) findViewById(R.id.et_temp_attender_org);
		etOrg.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		etPost = (EditText) findViewById(R.id.et_temp_attender_post);
		etPost.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		etPhone = (EditText) findViewById(R.id.et_temp_attender_phone);
		etPhone.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		etPhone.setInputType(InputType.TYPE_CLASS_PHONE);
		etCardId = (EditText) findViewById(R.id.et_temp_attender_card_id);
		etCardId.setRawInputType(InputType.TYPE_CLASS_NUMBER);

		rgSex = (RadioGroup) findViewById(R.id.rg_sex);
		rbSex1 = (RadioButton) findViewById(R.id.rg_rb1);
		rbSex2 = (RadioButton) findViewById(R.id.rg_rb2);
		rbSex1.setChecked(true);

		loadingdiag = new LoadingDialog(this);
		loadingdiag.setCanceledOnTouchOutside(false);
		loadingdiag.setText(getString(R.string.uploading));
	}

	@Override
	public void setupView() {
		setContentView(R.layout.create_temp_person);
	}

	public void clickSubmit(View view) {
		if (etName.getText().toString().length() == 0) {
			Toast.makeText(CreateTempPerson.this,
					getString(R.string.error_temp_person_no_name),
					Toast.LENGTH_SHORT).show();
			return;
		}

		if (etOrg.getText().toString().length() == 0) {
			Toast.makeText(CreateTempPerson.this,
					getString(R.string.error_temp_person_no_org),
					Toast.LENGTH_SHORT).show();
			return;
		}

		if (etPost.getText().toString().length() == 0) {
			Toast.makeText(CreateTempPerson.this,
					getString(R.string.error_temp_person_no_post),
					Toast.LENGTH_SHORT).show();
			return;
		}

		if (etPhone.getText().toString().length() == 0) {
			Toast.makeText(CreateTempPerson.this,
					getString(R.string.error_temp_person_no_phone),
					Toast.LENGTH_SHORT).show();
			return;
		}

		if (etCardId.getText().toString().length() == 0) {
			Toast.makeText(CreateTempPerson.this,
					getString(R.string.error_temp_person_no_idcard),
					Toast.LENGTH_SHORT).show();
			return;
		}

		if (etCardId.getText().toString().length() < 18) {
			Toast.makeText(CreateTempPerson.this,
					getString(R.string.error_temp_person_invalid_idcard),
					Toast.LENGTH_SHORT).show();
			return;
		}

		CreatePerson();

	}

	private void CreatePerson() {
		String url = "MeetingManage/mobile/createTempPerson.action?name=";
		url += etName.getText().toString();
		url += "&org=" + etOrg.getText().toString();
		url += "&post=" + etPost.getText().toString();
		url += "&phone=" + etPhone.getText().toString();
		url += "&card_id=" + etCardId.getText().toString();
		url += "&sex="
				+ (rgSex.getCheckedRadioButtonId() == R.id.rg_rb1 ? 0 : 1);

		loadingdiag.show();
		MeetingSystemClient.get(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] body,
					Throwable error) {
				loadingdiag.hide();
				Toast.makeText(CreateTempPerson.this,
						getString(R.string.error_network), Toast.LENGTH_SHORT)
						.show();
				finish();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] body) {
				String response = new String(body);
				// Toast.makeText(TopicSelectApplicatorActivity.this, new
				// String(response)+"wangting"+response.toString(), 100).show();
				loadingdiag.hide();
				if (response.contains("用户未登陆")){
					Toast.makeText(CreateTempPerson.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
					return;
				}	
				if (response.contains("true")) {
					Toast.makeText(CreateTempPerson.this,
							getString(R.string.temp_attender_submit_success),
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(CreateTempPerson.this,
							getString(R.string.temp_attender_submit_fail),
							Toast.LENGTH_SHORT).show();
				}
				finish();
			}
		});
	}
}
