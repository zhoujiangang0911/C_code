package cn.wislight.meetingsystem.util;

import cn.wislight.meetingsystem.R;
import android.app.Activity;
import android.app.Dialog;  
import android.content.Context;  
import android.os.Bundle;  
import android.view.KeyEvent;
import android.widget.TextView;  
import android.widget.Toast;
  
public class LoadingDialog extends Dialog {  
    private TextView tv = null;  
    private String text;
    private Context cntx;
    private boolean bUpdatingDictionaty = false;
    
  
    public LoadingDialog(Context context) {  
        super(context, R.style.loadingDialogStyle);  
        cntx = context;
    }  
  
    private LoadingDialog(Context context, int theme) {  
        super(context, theme);  
        cntx = context;
    }  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.dialog_loading);  
        tv = (TextView)this.findViewById(R.id.tv);  
        tv.setText(text);  
       // LinearLayout linearLayout = (LinearLayout)this.findViewById(R.id.ll);  
       // linearLayout.getBackground().setAlpha(210);  
    }  
    
    public void setText(String text){
    	this.text = text;
    	if (null != tv)
    		tv.setText(this.text);  
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK ) {  
        	if (bUpdatingDictionaty){
        		Toast.makeText(cntx, "正在更新数据字典，请稍后", Toast.LENGTH_SHORT).show();
        	}else{
        		this.dismiss();
            	((Activity)cntx).finish();
            	return true;
        	}
        }            
        return false;            
    }

	public boolean isbUpdatingDictionaty() {
		return bUpdatingDictionaty;
	}

	public void setbUpdatingDictionaty(boolean bUpdatingDictionaty) {
		this.bUpdatingDictionaty = bUpdatingDictionaty;
	}
}  
