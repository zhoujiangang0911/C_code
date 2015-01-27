package cn.wislight.publicservice.util;


import cn.wislight.publicservice.R;
import android.app.Activity;  
import android.location.Location;  
import android.location.LocationListener;  
import android.location.LocationManager;  
import android.os.Bundle;  
import android.view.View;  
import android.view.View.OnClickListener;  
import android.widget.Button;  
import android.widget.ImageButton;
import android.widget.TextView;  
  
public class FindCurrentLocation extends Activity {  
    /** Called when the activity is first created. */  
    ImageButton button;  
    TextView textview;  
    LocationManager manager;  
    Location location;  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_find_current_location);  
        textview=(TextView)findViewById(R.id.textview);  
        button=(ImageButton)findViewById(R.id.button);  
        manager=(LocationManager)getSystemService(LOCATION_SERVICE);  
        //从GPS_PROVIDER获取最近的定位信息  
        location=manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);  
        updateView(location);  
        //判断GPS是否可用  
        System.out.println("state="+manager.isProviderEnabled(LocationManager.GPS_PROVIDER));  
        button.setOnClickListener(new OnClickListener() {  
              
            @Override  
            public void onClick(View v) {  
                // TODO Auto-generated method stub  
                //设置每60秒，每移动十米向LocationProvider获取一次GPS的定位信息  
                //当LocationProvider可用，不可用或定位信息改变时，调用updateView,更新显示  
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 10, new LocationListener() {  
                      
                    @Override  
                    public void onStatusChanged(String provider, int status, Bundle extras) {  
                        // TODO Auto-generated method stub  
                          
                    }  
                      
                    @Override  
                    public void onProviderEnabled(String provider) {  
                        // TODO Auto-generated method stub  
                        //  
                        updateView(manager.getLastKnownLocation(provider));  
                    }  
                      
                    @Override  
                    public void onProviderDisabled(String provider) {  
                        // TODO Auto-generated method stub  
                        updateView(null);  
                    }  
                      
                    @Override  
                    public void onLocationChanged(Location location) {  
                        // TODO Auto-generated method stub  
                        //location为变化完的新位置，更新显示  
                        updateView(location);  
                    }  
                });  
            }  
        });  
    }  
    //更新显示内容的方法  
    public void updateView(Location location)  
    {  
        StringBuffer buffer=new StringBuffer();  
        if(location==null)  
        {  
            textview.setText("未获得服务");  
            return;  
        }  
        buffer.append("经度："+location.getLongitude()+"\n");  
        buffer.append("纬度："+location.getLatitude()+"\n");  
        buffer.append("高度："+location.getAltitude()+"\n");  
        buffer.append("速度："+location.getSpeed()+"\n");  
        buffer.append("方向："+location.getBearing()+"\n");  
        textview.setText(buffer.toString());  
    }  
} 