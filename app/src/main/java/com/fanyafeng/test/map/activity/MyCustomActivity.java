package com.fanyafeng.test.map.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.fanyafeng.test.map.BaseActivity;
import com.fanyafeng.test.map.R;
import com.fanyafeng.test.map.app.LocationApplication;

public class MyCustomActivity extends BaseActivity {
    private LocationClient locationClient;
    private Button getmylocation;
    private TextView mycurrentlocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_custom);

        locationClient=((LocationApplication)getApplication()).mLocationClient;
        initView();
    }

    private void initView(){
        mycurrentlocation=(TextView)findViewById(R.id.mycurrentlocation);
        ((LocationApplication)getApplication()).mLocationResult = mycurrentlocation;
        getmylocation=(Button)findViewById(R.id.getmylocation);
        getmylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initLocation();
                if (getmylocation.getText().equals("开启定位")){
                    locationClient.start();
                    getmylocation.setText("停止定位");
                }else{
                    locationClient.stop();
                    getmylocation.setText("开启定位");
                }
            }
        });
    }


    private void initLocation(){
        LocationClientOption option=new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("gcj02");
        option.setScanSpan(2000);
        option.setIsNeedAddress(true);
        locationClient.setLocOption(option);

    }

    @Override
    protected void onStop() {
        locationClient.stop();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_custom, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
