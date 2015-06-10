package com.fanyafeng.test.map.weather;

import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanyafeng.test.map.util.L;
import com.fanyafeng.test.map.weather.BDLocationClient.OnLocationListener;
import com.fanyafeng.test.map.weather.WeatherSource.OnFinishListener;
import com.fanyafeng.test.map.app.GlobalConstantsApp;
import com.fanyafeng.test.map.R;

public class WeatherActivity extends Activity implements OnClickListener {

	private TextView mText;
	private ImageView mIvTemp;
	private TextView mWeather;
	private TextView mDate;
	private TextView mWeek;
	private TextView mTemp;
	private ImageView mIcon;
	private Bitmap mBitmapIcon;
	private BDLocationClient mLocationClient;
	private WeatherSource mWeatherSource;
	private WeatherHandler weatherHandler = null;
	private ArrayList<WeatherInfo> weatherInfos;
	private DrawPicNum picNum;
	private asyncDownload mDownload = null;
	private boolean _isExe = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        mLocationClient = new BDLocationClient(getApplicationContext());
        mLocationClient.setOnLocationListener(new LocationListener());
        initWidgets();
    }

    private void initWidgets() {
        mText = (TextView) findViewById(R.id.tv_text);
        mIvTemp = (ImageView) findViewById(R.id.iv_temp);
        mWeather = (TextView) findViewById(R.id.tv_weather);
        mDate = (TextView) findViewById(R.id.tv_date);
        mWeek = (TextView) findViewById(R.id.tv_week);
        mTemp = (TextView) findViewById(R.id.tv_temp);
        mIcon = (ImageView) findViewById(R.id.iv_icon);
        Button btn = (Button) findViewById(R.id.btn_request);
        btn.setOnClickListener(this);
        weatherInfos = new ArrayList<WeatherInfo>();
        picNum = new DrawPicNum(WeatherActivity.this);
        mDownload = new asyncDownload();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        if (_isExe) {
            mDownload.cancel(true); // 取消操作
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mLocationClient.stopLocation();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_request:
                mLocationClient.requestLocation();
                break;
            default:
                break;
        }
    }

    private class LocationListener implements OnLocationListener {

        @Override
        public void OnUpdateLocation(String city, String addr) {
            // TODO Auto-generated method stub
            setTitle("当前城市：" + city);
            mText.setText(addr);
            // 定位后再获取当前城市的天气
            mWeatherSource = new WeatherSource(city);
            mWeatherSource.setOnFinishListener(new FinishListener());
        }

    }

    private class FinishListener implements OnFinishListener {

        @Override
        public void OnFinish(String result) {
            // TODO Auto-generated method stub
            try {
                // 创建一个SAXParserFactory
                SAXParserFactory factory = SAXParserFactory.newInstance();
                XMLReader reader = factory.newSAXParser().getXMLReader();
                weatherHandler = new WeatherHandler();
                // 为XMLReader设置内容处理器
                reader.setContentHandler(weatherHandler);
                // 开始解析文件
                reader.parse(new InputSource(new StringReader(result)));
                parseWeather();
            } catch (Exception e) {
                System.out.println("-----------Exception");
                e.printStackTrace();
            }
        }
    }

    private void parseWeather() {
        weatherInfos = weatherHandler.getWeathers();
        WeatherInfo weather = new WeatherInfo();
        weather = weatherInfos.get(0);
        mIvTemp.setImageBitmap(picNum.getStringPic(getNumberInString(weather
                .getDate())));
        mWeather.setText(weather.getWeather() + "  " + weather.getWind());
        mDate.setText(weatherHandler.getDate());
        String date = weather.getDate();
        mWeek.setText(date.substring(0, date.lastIndexOf("(")));
        mTemp.setText(weather.getTemperature());
        if (!_isExe) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH",
                    Locale.getDefault());
            String hour = sdf.format(new Date());
            if (Integer.parseInt(hour) > 18 || Integer.parseInt(hour) < 6) {
                // 晚上
                mDownload.execute(weather.getNightPictureUrl()); // 执行异步操作
            } else {
                // 白天
                mDownload.execute(weather.getDayPictureUrl()); // 执行异步操作
            }
            System.out.println("hour = " + hour);

            _isExe = true;
        }
        weather = weatherInfos.get(1);
        TextView text = (TextView) findViewById(R.id.tv_date1);
        text.setText(weather.getDate());
        text = (TextView) findViewById(R.id.tv_temp1);
        text.setText(weather.getTemperature());
        text = (TextView) findViewById(R.id.tv_weather1);
        text.setText(weather.getWeather());
        weather = weatherInfos.get(2);
        text = (TextView) findViewById(R.id.tv_date2);
        text.setText(weather.getDate());
        text = (TextView) findViewById(R.id.tv_temp2);
        text.setText(weather.getTemperature());
        text = (TextView) findViewById(R.id.tv_weather2);
        text.setText(weather.getWeather());
        weather = weatherInfos.get(3);
        text = (TextView) findViewById(R.id.tv_date3);
        text.setText(weather.getDate());
        text = (TextView) findViewById(R.id.tv_temp3);
        text.setText(weather.getTemperature());
        text = (TextView) findViewById(R.id.tv_weather3);
        text.setText(weather.getWeather());
    }

    /**
     * 从字符串中得到数字
     *
     * @param str
     * @return
     */
    private String getNumberInString(String str) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return (m.replaceAll("").trim());
    }

    class asyncDownload extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            URL imageUrl = null;
            try {
                imageUrl = new URL(params[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                L.d(e.getMessage());
            }
            try {
                // 使用HttpURLConnection打开连接
                HttpURLConnection urlConn = (HttpURLConnection) imageUrl
                        .openConnection();
                urlConn.setDoInput(true);
                urlConn.connect();
                // 将得到的数据转化成InputStream
                InputStream is = urlConn.getInputStream();
                // 将InputStream转换成Bitmap
                mBitmapIcon = BitmapFactory.decodeStream(is);
                is.close();
                System.out.println("-------------success");
            } catch (Exception e) {
                e.printStackTrace();
                L.e(e.getMessage());
            }
            return true;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            mIcon.setImageBitmap(mBitmapIcon);
            System.out.println("-------------success--1");
            super.onPostExecute(result);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
        }
    }
}

