package cn.wislight.publicservice.ui.commercialtenant;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpResponseHandler;

import cn.wislight.publicservice.R;
import cn.wislight.publicservice.base.BaseActivity;
import cn.wislight.publicservice.ui.governmentaffairs.ServantGovermentAffairDelayActivity;
import cn.wislight.publicservice.util.Constants;
import cn.wislight.publicservice.util.PublicServiceClient;
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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class BuyserNegPriceActivity extends BaseActivity implements OnClickListener{

	private EditText editNegotiationPrice;
	private EditText editNegotiationHandovertime;
	private ImageView btnNegotiationVoicelist;
	private TextView btnNegotiationIMMessagelist;	
	private ImageView btnNegotiationSubmit;
	private ImageView btnNegotiationCancel;
	private TextView btnNegotiationDetail;
	
	
	private RadioGroup rgNegotiationConfirmType;
	
	private String negotiationId;
	private Button btnFindMessageHestory;
	private Button btnChioceDate;
	private DatePicker mDatePicker;
	private int year, month, day;
	private String strdate;
	@Override
	public void setUpView() {
		setContentView(R.layout.buyser_negprice_main);
		
		editNegotiationPrice = (EditText) findViewById(R.id.negotiationPrice);
		editNegotiationHandovertime = (EditText) findViewById(R.id.negotiationHandovertime);
		btnNegotiationVoicelist = (ImageView) findViewById(R.id.negotiationVoicelist);
		btnNegotiationIMMessagelist= (TextView) findViewById(R.id.negotiationIMMessagelist);
		btnNegotiationSubmit = (ImageView) findViewById(R.id.negotiationSubmit);
		btnNegotiationCancel = (ImageView) findViewById(R.id.negotiationCancel);
		rgNegotiationConfirmType = (RadioGroup) findViewById(R.id.negotiationConfirmType);
		btnNegotiationDetail= (TextView) findViewById(R.id.negotiationDetail);
		btnFindMessageHestory = (Button)findViewById(R.id.btnFindMessageHestory);
		btnChioceDate = (Button) findViewById(R.id.btn_chooseDate);
		
		btnNegotiationVoicelist.setOnClickListener(this);
		btnNegotiationIMMessagelist.setOnClickListener(this);
		btnChioceDate.setOnClickListener(this);
		
		btnNegotiationSubmit.setOnClickListener(this);
		btnNegotiationCancel.setOnClickListener(this);	
		btnNegotiationDetail.setOnClickListener(this);
		btnFindMessageHestory.setOnClickListener(this);
		
		negotiationId = this.getIntent().getStringExtra("negotiationId");
		
		fillData();
		
	}

	@Override
	public void setListener() {
		
	}

	private void fillData(){
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.negotiationSubmit:
			//Toast.makeText(NegotiatePriceActivity.this, "click detail content", 100).show();
			//gotoActivity(BuyserNegPriceActivity.class, false);	
			String price = editNegotiationPrice.getText().toString();
			if(price != null && price.trim().length()<1){
				Toast.makeText(BuyserNegPriceActivity.this,"价格不能为空", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			
			int checkedid = rgNegotiationConfirmType.getCheckedRadioButtonId();
			String confirmType =((RadioButton) findViewById(checkedid)).getText().toString();
			String handoverTime =  editNegotiationHandovertime.getText().toString();
			Intent intent = new Intent();
			intent.putExtra("price", price);
			intent.putExtra("confirmType", confirmType);
			intent.putExtra("handoverTime", handoverTime);
			this.setResult(Activity.RESULT_OK, intent);
			finish();
			//negotiationOfferprice(negotiationId, price);
			break;
		case R.id.negotiationCancel:
			finish();
			break;	
		case R.id.negotiationDetail:
			Intent intents = new Intent(this, DetailNegotiateActivity.class );
			intents.putExtra(Constants.ID, negotiationId);
			this.startActivity(intents);
			break;	
		case R.id.negotiationVoicelist:
			Intent voiceIntent = new Intent(this, VOIPRecordListActivity.class );
			voiceIntent.putExtra(Constants.ID, negotiationId);
			this.startActivity(voiceIntent);
			break;	
		case R.id.btnFindMessageHestory:
			Intent imIntent = new Intent(this, IMMessageHistoryActivity.class );
			imIntent.putExtra(Constants.ID, negotiationId);
			this.startActivity(imIntent);
			break;
		case R.id.btn_chooseDate:
			// 选择日期对话框
						AlertDialog.Builder builder = new Builder(
								BuyserNegPriceActivity.this);
						builder.setTitle("请选择日期");
						builder.setIcon(R.drawable.btn_date);
						LayoutInflater factory = LayoutInflater
								.from(BuyserNegPriceActivity.this);
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
										editNegotiationHandovertime.setText(strdate);
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

		default:
			break;
		}
		
	}
	
	
}
