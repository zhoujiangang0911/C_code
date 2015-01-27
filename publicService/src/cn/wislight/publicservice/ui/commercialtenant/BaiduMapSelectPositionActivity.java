package cn.wislight.publicservice.ui.commercialtenant;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import cn.wislight.publicservice.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * @author Administrator
 *  百度定位
 */
public class BaiduMapSelectPositionActivity extends Activity implements OnClickListener {	
	private TextView txtSubmit;
	private TextView txtPosition;
	private MapView mMapView;
	
	private BaiduMap mBaiduMap;
	private LocationClient mLocClient;
	private GeoCoder mSearch;
	
	private boolean isFirstLoc =true;
	
	private BDLocationListener myListener;	
	private String addressName;
	private Double longitude;
	private Double latitude;
	
	private Overlay overlay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_baidumap_select_position);	
		
		txtSubmit = (TextView) findViewById(R.id.baidumapPositionSubmit);
		txtPosition = (TextView) findViewById(R.id.baidumapPosition);		
		mMapView = (MapView) findViewById(R.id.baidumapView);
		txtSubmit.setOnClickListener(this);		
		
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {  
		    public void onMapClick(LatLng point) {  		    	
		    	
		        //在此处理点击事件  
		    	System.out.println("wangting: latitude="+point.latitude + "logitude="+point.longitude);
		    	
		    	longitude = point.longitude;
		    	latitude = point.latitude;
		    	txtPosition.setText(addressName + "(" +point.latitude + ";" + point.longitude+")");
		    	markMapPosition(point.latitude, point.longitude);
		    	mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(new LatLng(point.latitude,point.longitude)));
		    }  
		    public boolean onMapPoiClick(MapPoi poi) {  
		        //在此处理底图标注点击事件  
		    	System.out.println("wangting: poi.getName()="+poi.getName()+" poi.getPosition().toString()="+poi.getPosition().toString());
		        return false;  
		    }  
		});
		
		
		myListener = new MyLocationListenner();

		// 定位初始化  
		mLocClient = new LocationClient(this.getApplicationContext());
		//mLocClient = new LocationClient(PublicServiceApplication.getInstance().getApplicationContext());		
		mLocClient.registerLocationListener(myListener);  
		
        //LatLng cenpt = new LatLng(34.21821,108.897723); 
		addressName = "科技六路中段";
		longitude = 108.897723;
		latitude = 34.21821;
				
		//initMapPosition(latitude,longitude);

		
		setCurrentLocation();
		
		mSearch = GeoCoder.newInstance();
		OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {  
		    public void onGetGeoCodeResult(GeoCodeResult result) {  
		        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {  
		            //没有检索到结果  
		        }  
		        //获取地理编码结果  
		    }  
		  
		    @Override  
		    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {  
		        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {  
		            //没有找到检索结果  
		        	System.out.println("wagnting: reverse geo failed.");
		        	
		        }  
		        addressName = result.getAddress();
		        txtPosition.setText(addressName + "(" +latitude + ";" + longitude+")");
		        System.out.println("wangting: reverse geo success="+addressName + "(" +latitude + ";" + longitude+")");
		        //获取反向地理编码结果  
		    }  
		};  
		mSearch.setOnGetGeoCodeResultListener(listener); 
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理	
		if(mLocClient != null){			
			//mLocClient.stop();
		}
		mMapView.onDestroy();
		mSearch.destroy();
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
    
	
	private void setCurrentLocation(){
		/*
		// 地图初始化
		//mMapView = (MapView) findViewById(R.id.bmapView);
		//mBaiduMap = mMapView.getMap();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型


		option.setLocationMode(LocationMode.Battery_Saving);
		option.setScanSpan(5000);
		mLocClient.setLocOption(option);
		
		mLocClient.start();
		mLocClient.requestLocation();
		*/
 
		//mBaiduMap = mMapView.getMap();  
		// 开启定位图层  
		mBaiduMap.setMyLocationEnabled(true);  
		LocationClientOption option = new LocationClientOption();  
		option.setOpenGps(true);// 打开gps  
		option.setLocationMode(LocationMode.Battery_Saving);//LocationMode.Hight_Accuracy或LocationMode.Battery_Saving或LocationMode.Device_Sensors
		option.setCoorType("bd09ll"); // 设置坐标类型  gcj02 或bd09ll或bd09
		option.setScanSpan(1000);  
		option.setIsNeedAddress(true);
		
		mLocClient.setLocOption(option);  
		mLocClient.start();
		int result = mLocClient.requestLocation();
		System.out.println("wangting:request location result ="+result);
		
	}
	
	/**
	 * 定位SDK监听函数
	*/
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			System.out.println("wangting: location success"+location.getLongitude() +";"+location.getLatitude()+";addressname"+location.getAddrStr());
			if (location == null || mMapView == null)
				return;
			addressName = location.getAddrStr();
	    	longitude = location.getLongitude();
	    	latitude = location.getLatitude();
	    	txtPosition.setText(addressName + "(" + latitude + ";" + longitude+")");
	    	//Toast.makeText(BaiduMapSelectPositionActivity.this,addressName + "(" + latitude + ";" + longitude+")" , 100).show();
	    	//mLocClient.stop();
	    	System.out.println("wangting:"+addressName + "(" + latitude + ";" + longitude+")");
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}else{				
				mLocClient.unRegisterLocationListener(myListener);//已经定位到当前位置了，取消监听
			}
				
			
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	
	
	private void initMapPosition(Double latitude, Double longitude) {
		LatLng cenpt = new LatLng(latitude, longitude);
		// 定义地图状态
		MapStatus mMapStatus = new MapStatus.Builder().target(cenpt).zoom(18)
				.build();
		// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化

		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
				.newMapStatus(mMapStatus);
		// 改变地图状态
		mBaiduMap.setMapStatus(mMapStatusUpdate);

		LatLng point = new LatLng(latitude, longitude);
		// 构建Marker图标
		BitmapDescriptor bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.imgview_point);
		// 构建MarkerOption，用于在地图上添加Marker
		OverlayOptions option = new MarkerOptions().position(point)
				.icon(bitmap);
		// 在地图上添加Marker，并显示
		mBaiduMap.addOverlay(option);

	}

	private void markMapPosition(Double latitude, Double longitude) {
		LatLng point = new LatLng(latitude, longitude);
		// 构建Marker图标
		BitmapDescriptor bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.imgview_point);
		// 构建MarkerOption，用于在地图上添加Marker
		OverlayOptions option = new MarkerOptions().position(point)
				.icon(bitmap);
		if(overlay!=null){
			overlay.remove();
		}
		// 在地图上添加Marker，并显示
		overlay = mBaiduMap.addOverlay(option);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.baidumapPositionSubmit:				
	            Intent intent = new Intent();
				intent.putExtra("addressName", addressName);
				intent.putExtra("longitude", longitude.toString());
				intent.putExtra("latitude", latitude.toString());
				this.setResult(Activity.RESULT_OK, intent);
				finish();
				
				break;
			default:
				break;
		}
	}
	
	
}
