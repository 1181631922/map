package com.fanyafeng.test.map;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class BaseActivity extends Activity implements View.OnClickListener {

    //相同的类型可以用一个string的，为了以后的移植都进行了单个的定义
    protected String title;
    protected String subtitle;
    protected boolean ishide = false;
    private boolean isTrue=true;
    private boolean isFalse=false;


    /**
     * getActionBar只能在onCreate进行get
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*左上角icon显示
        getActionBar().setDisplayShowHomeEnabled(false);
        左上角返回键显示
        ActionBar actionBar=getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);*/

        isShowIcon(isTrue);
        isBack();
        isShowBackIcon(isFalse);
        isShowTitle(isTrue);
        setTitleIcon(isTrue);
        setTitleBackground();

    }




    /**
     * 每个顶部menu的监听，如果横向添加的话android会自定义下拉编写，不用进行方法的重写实现
     *
     * @param item
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                BaseBack();
                break;
//            case R.id.base_action_setting1:
//                finish();
//                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 为返回监听提供重写的方法
     */
    protected void BaseBack() {
        finish();
    }

    /**
     * 为子activity的icon初始化提供重写方法
     */
    protected void isShowIcon(boolean isshowicom) {
        getActionBar().setDisplayShowHomeEnabled(isshowicom);
    }

    /**
     * 为子activity的back按钮初始化提供重写方法
     */
    protected void isBack() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * 为子activity的title标题是否显示提供重写方法
     */
    protected void isShowTitle(boolean isshowtitle) {
        getActionBar().setDisplayShowTitleEnabled(isshowtitle);
    }

    /**
     * 为子activity的返回图标是否显示提供重写方法
     */
    protected void isShowBackIcon(boolean isshowbackicom) {
        getActionBar().setDisplayHomeAsUpEnabled(isshowbackicom);
    }

    /**
     * 为子activity的icon图标提供私人定制的重写方法
     */
    protected void setTitleIcon(boolean istitleicon) {
        if (istitleicon){
        getActionBar().setIcon(getResources().getDrawable(R.drawable.title_back));}
    }

    /**
     * 为子activity的title背景提供私人定制的重写方法
     */
    protected void setTitleBackground() {
        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.customthemebackground));
    }

    /**
     * 一级标题
     *
     * @param title
     */
    protected void setTitleContent(String title) {
        getActionBar().setTitle(title);
    }

    /**
     * 二级标题
     *
     * @param subtitle
     */
    protected void setSubtitleContent(String subtitle) {
        getActionBar().setSubtitle(subtitle);
    }

    protected void isActionBarHide(boolean ishide) {
        getActionBar().hide();
    }

    /**
     * 对重新赋值的字段进行判断
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (title != null && !title.equals("")) {
            setTitleContent(title);
        }
        if (subtitle != null && !subtitle.equals("")) {
            setSubtitleContent(subtitle);
        }
        if (ishide) {
            isActionBarHide(true);
        }
    }
    @Override
    public void onClick(View v) {

    }
}
