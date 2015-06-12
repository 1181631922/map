package com.fanyafeng.test.map.app;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import android.app.Application;
import android.app.Service;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;

/**
* 主Application
*/
public class LocationApplication extends Application {
	public LocationClient mLocationClient;
	public GeofenceClient mGeofenceClient;
	public MyLocationListener mMyLocationListener;

	public TextView mLocationResult,logMsg;
	public TextView trigger,exit;
	public Vibrator mVibrator;

    private GetLocation getLocation;

	@Override
	public void onCreate() {
		super.onCreate();
		mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		mGeofenceClient = new GeofenceClient(getApplicationContext());


		mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
	}


	/**
	 * 实现实位回调监听
     * 个人感觉常用的一个是
     * time时间
     * speed速度
     * addr具体的位置
     * 这个定位的时候容易出现null，而且有时候虽然定位成功
     * 但是errorcode仍会显示161
     * 可是有的时候虽然定位未成功显示确实61
     * 那个定位时间可以自己写一个计时器
     * 这样的话可以实现定位多长时间后便可以自动停止定位
	 */
    public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			//Receive Location
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
//			sb.append("\nerror code : ");
//			sb.append(location.getLocType());
//			sb.append("\nlatitude : ");
//			sb.append(location.getLatitude());
//			sb.append("\nlontitude : ");
//			sb.append(location.getLongitude());
//			sb.append("\nradius : ");
//			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation){
//				sb.append("\nspeed : ");
//				sb.append(location.getSpeed());
//				sb.append("\nsatellite : ");
//				sb.append(location.getSatelliteNumber());
//				sb.append("\ndirection : ");
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append(location.getDirection());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
                sb.append("\n城市 : ");
                sb.append(location.getCity());
                //运营商信息
//				sb.append("\noperationers : ");
//				sb.append(location.getOperators());
                getLocation.GetMyLocation(location.getAddrStr(),location.getCity());
			}
			logMsg(sb.toString());
			Log.i("BaiduLocationApiDem", sb.toString());
		}


	}


    /**
     * 显示请求字符串
     * @param str
     */
	public void logMsg(String str) {
		try {
			if (mLocationResult != null)
				mLocationResult.setText(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    public interface GetLocation{
        public void GetMyLocation(String city,String addr);
    }


}
