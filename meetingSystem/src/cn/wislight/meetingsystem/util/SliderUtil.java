package cn.wislight.meetingsystem.util;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

public class SliderUtil {

	public static void moveSlider(View v, int startX, int toX)
	{
		TranslateAnimation anim = new TranslateAnimation(startX, toX, 0, 0);
		anim.setDuration(200);
		anim.setFillAfter(true);
		v.startAnimation(anim);
	}

	public static void setImageViewParam(ImageView mIvSlideUnderLine,int distance) {
		LayoutParams para;  
		para = (LayoutParams) mIvSlideUnderLine.getLayoutParams();  

		Log.i("grage", "layout height0: " + para.height);  
		Log.i("grage", "layout width0: " + para.width);  
		para.width = distance;  
		mIvSlideUnderLine.setLayoutParams(para);
	}

	public static void setTextViewColor(TextView tv,int resId) {
		tv.setTextColor(resId);
	}

	public static DisplayMetrics  getPhoneWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm;
	}

	public static int  getDistance(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels / 2;
	}
}
