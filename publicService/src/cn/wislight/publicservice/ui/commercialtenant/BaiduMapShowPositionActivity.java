package cn.wislight.publicservice.ui.commercialtenant;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import cn.wislight.publicservice.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * @author Administrator
 *  发起事物
 */
public class BaiduMapShowPositionActivity extends Activity implements OnClickListener {	
	private TextView txtSubmit;
	private TextView txtPosition;
	private MapView mMapView;	
	private BaiduMap mBaiduMap;

	private String addressName;
	private Double longitude;
	private Double latitude;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_baidumap_select_position);	
		
		txtSubmit = (TextView) findViewById(R.id.baidumapPositionSubmit);
		txtPosition = (TextView) findViewById(R.id.baidumapPosition);		
		mMapView = (MapView) findViewById(R.id.baidumapView);
		txtSubmit.setOnClickListener(this);		
		
		mBaiduMap = mMapView.getMap();
		
		Intent intent = getIntent();
		addressName = intent.getStringExtra("addressName");
		longitude = Double.valueOf(intent.getStringExtra("longitude"));
		latitude =  Double.valueOf(intent.getStringExtra("latitude"));
		
		txtPosition.setText(addressName+"("+longitude+","+latitude+")");
				
		initMapPosition(latitude,longitude);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理		
		mMapView.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
	}
    
	

			
	private void initMapPosition(Double latitude, Double longitude){		
        LatLng cenpt = new LatLng(latitude, longitude); 
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
        .target(cenpt)
        .zoom(18)
        .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化

        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);     
                
        LatLng point = new LatLng(latitude, longitude);  
      //构建Marker图标  
      BitmapDescriptor bitmap = BitmapDescriptorFactory  
          .fromResource(R.drawable.imgview_point);  
      //构建MarkerOption，用于在地图上添加Marker  
      OverlayOptions option = new MarkerOptions()  
          .position(point)  
          .icon(bitmap);  
      //在地图上添加Marker，并显示  
      mBaiduMap.addOverlay(option);
      
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.baidumapPositionSubmit:
				finish();
				break;
			default:
				break;
		}
	}
}
