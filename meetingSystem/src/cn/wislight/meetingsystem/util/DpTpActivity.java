package cn.wislight.meetingsystem.util;


import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.TimePicker.OnTimeChangedListener;
import cn.wislight.meetingsystem.R;
import cn.wislight.meetingsystem.ui.topic.TopicAddTwoActivity;

public class DpTpActivity extends Activity {
    private EditText dateEt=null;
    private EditText timeEt=null;
    private String date="";
    private String time="";
    private String dateTime="";
    private Button ImbBtSubmit; 
    
    /* for topic edit and add in conference */
    String confStartTime;
    String confEndtime ;
    boolean bInConfTopic = false;
    LinearLayout llConfTimeRange;
    TextView tvConfTimeRange;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.date_time_picker);
        // dateEt=(EditText)findViewById(R.id.dateEt);
        timeEt=(EditText)findViewById(R.id.timeEt);
        
        DatePicker datePicker=(DatePicker)findViewById(R.id.datePicker);
        TimePicker timePicker=(TimePicker)findViewById(R.id.timePicker);
        llConfTimeRange = (LinearLayout)findViewById(R.id.ll_conf_time_range);
        tvConfTimeRange = (TextView)findViewById(R.id.tv_conf_time_range);
        ImbBtSubmit = (Button)findViewById(R.id.ibtn_submit);
        ImbBtSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (bInConfTopic){
					if (dateTime.compareTo(confStartTime)  < 0 ||
							dateTime.compareTo(confEndtime) > 0){
			        	Toast.makeText(DpTpActivity.this, getString(R.string.error_topic_time_error), Toast.LENGTH_SHORT).show();
						return;
					}
				}
                Intent data=new Intent();  
                data.putExtra(Constants.DATETIME, dateTime);  
                setResult(Constants.OK, data);  
                finish();
			}
		});
        
        Intent intent = this.getIntent();
        String init_time = intent.getStringExtra(Constants.INIT_TIME);
        System.out.println("wangtingtea:"+init_time);

        int year= 0;
        int monthOfYear = 0;
        int dayOfMonth = 0 ;
        int hour = 0;
        int minute = 0;

        if (init_time != null && init_time.length()>16 ){
            year = Integer.parseInt(init_time.substring(0, 4));
            monthOfYear = Integer.parseInt(init_time.substring(5, 7))-1;
            dayOfMonth = Integer.parseInt(init_time.substring(8, 10));
        	hour = Integer.parseInt(init_time.substring(11, 13));
        	minute = Integer.parseInt(init_time.substring(14, 16));
        } else {
            Calendar calendar = Calendar.getInstance();
            year=calendar.get(Calendar.YEAR);
            monthOfYear = calendar.get(Calendar.MONTH);
            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
        }
        System.out.println("wangtingtes:"+year+","+monthOfYear+","+dayOfMonth+","+hour+","+minute);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);      

        datePicker.init(year, monthOfYear, dayOfMonth, new OnDateChangedListener(){

            public void onDateChanged(DatePicker view, int year,
                    int monthOfYear, int dayOfMonth) {
                
                date = String.format("%1$04d", year)
                		+ "-" 
                		+ String.format("%1$02d", monthOfYear+1) 
                		+ "-" 
                		+ String.format("%1$02d", dayOfMonth);
                
                dateTime = date + " " + time;
                timeEt.setText(getString(R.string.your_sel_time) + dateTime);
            }
            
        });
        
        timePicker.setOnTimeChangedListener(new OnTimeChangedListener(){

            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                time = String.format("%1$02d", hourOfDay)
                		+ ":" 
                		+ String.format("%1$02d", minute) 
                		+ ":" 
                		+ "00";
                dateTime = date + " " + time;
                timeEt.setText(getString(R.string.your_sel_time) + dateTime);
            }
            
        });
        
        date = String.format("%1$04d", datePicker.getYear())
        		+ "-" 
        		+ String.format("%1$02d", datePicker.getMonth()+1) 
        		+ "-" 
        		+ String.format("%1$02d", datePicker.getDayOfMonth());
        
        time = String.format("%1$02d", timePicker.getCurrentHour())
        		+ ":" 
        		+ String.format("%1$02d", timePicker.getCurrentMinute()) 
        		+ ":" 
        		+ "00";
        
        dateTime = date + " " + time;
        timeEt.setText(getString(R.string.your_sel_time) + dateTime);
        
        
		SharedPreferences data = this.getSharedPreferences(Constants.CONF_TIME_NODE, MODE_PRIVATE);
		confStartTime = data.getString(Constants.CONF_START_TIME, "");
		confEndtime = data.getString(Constants.CONF_END_TIME, "");
		bInConfTopic = getIntent().getBooleanExtra(Constants.IN_MEET_TOPIC_FLAG, false);
		if (bInConfTopic && null != confStartTime && !"".equals(confStartTime)
			 && null != confEndtime && !"".equals(confEndtime)){
			llConfTimeRange.setVisibility(View.VISIBLE);
			tvConfTimeRange.setText(confStartTime + "--" +confEndtime); 
		}
		
    }
    
    @Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK ) {  
            finish();  
        }            
        return false;            
    }
	public void clickBackToFormer(View view){
		this.finish();
	}
}