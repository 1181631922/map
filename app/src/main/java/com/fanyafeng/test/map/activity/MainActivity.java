package com.fanyafeng.test.map.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.fanyafeng.test.map.BaseActivity;
import com.fanyafeng.test.map.R;
import com.fanyafeng.test.map.myweather.MyActivity;
import com.fanyafeng.test.map.weather.WeatherActivity;

public class MainActivity extends BaseActivity {
    private Button location,mylocation,mycustom,myweather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isShowIcon(false);
        initView();
        initData();
    }

    private void initView() {
        location = (Button) findViewById(R.id.location);
        location.setOnClickListener(this);
        mylocation = (Button) findViewById(R.id.mylocation);
        mylocation.setOnClickListener(this);
        mycustom = (Button) findViewById(R.id.mycustom);
        mycustom.setOnClickListener(this);
        myweather=(Button)findViewById(R.id.myweather);
        myweather.setOnClickListener(this);
    }

    private void initData() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.location:
                Intent intent = new Intent(MainActivity.this, LocationActivity.class);
                startActivity(intent);
                break;
            case R.id.mylocation:
                Intent intent1 = new Intent(MainActivity.this, WeatherActivity.class);
                startActivity(intent1);
                break;
            case R.id.mycustom:
                Intent intent2 = new Intent(MainActivity.this, MyCustomActivity.class);
                startActivity(intent2);
                break;
            case R.id.myweather:
                Intent intent3=new Intent(MainActivity.this, MyActivity.class);
                startActivity(intent3);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    protected void isBack() {
    }
}
