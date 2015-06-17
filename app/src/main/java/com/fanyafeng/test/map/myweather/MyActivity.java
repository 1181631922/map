package com.fanyafeng.test.map.myweather;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fanyafeng.test.map.BaseActivity;
import com.fanyafeng.test.map.R;
import com.fanyafeng.test.map.util.L;
import com.fanyafeng.test.map.util.PostUtil;
import com.fanyafeng.test.map.weather.BDLocationClient;
import com.fanyafeng.test.map.weather.WeatherSource;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MyActivity extends BaseActivity {
    private MyLocationClient myLocationClient;
    private TextView nowlocation;
    private Button restartlocation;
    private MySource mySource;
    private String mycity, currentCity, pm25;
    private TextView error, city, pm;
    MenuItem progressbar;
    private int err;
    private String date0, temperature0, wind0;
    private TextView date_0, temperature_0, wind_0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        initView();
        initData();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.restartlocation:
                myLocationClient.requestLocation();
                break;
        }
    }

    private void initView() {
        nowlocation = (TextView) findViewById(R.id.nowlocation);
        error = (TextView) findViewById(R.id.error);
        city = (TextView) findViewById(R.id.currentCity);
        pm = (TextView) findViewById(R.id.pm25);
        date_0 = (TextView) findViewById(R.id.date0);
        temperature_0 = (TextView) findViewById(R.id.temperature0);
        wind_0 = (TextView) findViewById(R.id.wind0);
        restartlocation = (Button) findViewById(R.id.restartlocation);
        restartlocation.setOnClickListener(this);
    }

    private void initData() {
        myLocationClient = new MyLocationClient(getApplicationContext());
        myLocationClient.setOnLocationListener(new LocationListener());
    }

    /**
     * 多线程
     * 继承父类thread覆盖run方法
     * 在类中可以进行自定义构造方法进行传参
     */
    class LoadData extends Thread {
        public LoadData(String city) {
            super(city);
        }

        public void run() {
            loadData(this.getName());
        }
    }

    /**
     * 这个是第二种方法，没试过传参
     * Thread loadThread = new Thread(new LoadThread());
     * loadThread.start();
     *
     * class LoadThread implements Runnable {
     *
     * @Override public void run() {
     * initData(k);
     * }
     * }
     */

    private void loadData(String city) {
        String path = "http://api.map.baidu.com/telematics/v3/weather?location=" + city + "&output=json&ak=640f3985a6437dad8135dae98d775a09";
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpGet request = new HttpGet(new URI(path));
            try {
                HttpResponse response = httpClient.execute(request);
                if (response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    String out = EntityUtils.toString(entity, "UTF-8");
                    try {
                        JSONObject jsonObject = new JSONObject(out);
                        err = jsonObject.getInt("error");
                        JSONArray results = jsonObject.getJSONArray("results");
                        JSONObject object = results.getJSONObject(0);
                        currentCity = object.getString("currentCity");
                        pm25 = object.getString("pm25");
                        Message message = Message.obtain();
                        message.what = 1;
                        handler.sendMessage(message);
                        JSONArray weather_data = object.getJSONArray("weather_data");
//                        for (int k=0;k<weather_data.length();k++){
//                            JSONObject object1=weather_data.getJSONObject(k);
//                            String date=object1.getString("date");
//                            String temperature=object1.getString("temperature");
//                            String wind=object1.getString("wind");
//                        }

                        JSONObject object0 = weather_data.getJSONObject(0);
                        date0 = object0.getString("date");
                        temperature0 = object0.getString("temperature");
                        wind0 = object0.getString("wind");
                        Message message0 = Message.obtain();
                        message.what = 2;
                        handler.sendMessage(message0);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    L.d(out);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private class LocationListener implements MyLocationClient.OnLocationListener {
        @Override
        public void OnUpdateLocation(String city, String addr) {
            mycity = city;
            Message message = Message.obtain();
            message.what = 0;
            handler.sendMessage(message);
            nowlocation.setText(city + addr);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    title = mycity;
                    L.d(title);
                    Thread thread = new LoadData(title);
                    thread.start();
                    break;
                case 1:
                    error.setText("返回的error值：" + err);
                    city.setText("当前城市：" + currentCity);
                    pm.setText("当前的pm值：" + pm25);
                    break;
                case 2:
                    date_0.setText(date0);
                    temperature_0.setText(temperature0);
                    wind_0.setText(wind0);
                    break;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
        progressbar = menu.findItem(R.id.refresh_loading);
        return true;
    }

    public void setLoadingState(boolean refreshing) {
        if (progressbar != null) {
            if (refreshing) {
                progressbar.setActionView(R.layout.actionbar_progress);
                progressbar.setVisible(true);
            } else {
                progressbar.setVisible(false);
                progressbar.setActionView(null);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        setLoadingState(true);
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
