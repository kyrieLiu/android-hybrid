package com.example.imoocdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.example.imoocdemo.helpers.SpHelper;
import com.example.imoocdemo.utils.FullScreenUtils;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebView;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_CODE = 1002;

    private X5WebView mWebView;

    private SpHelper mSpHelper;

    private long endTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new GlobalLayoutUtil(this);
        init();
    }
    private void init(){
        requestPermission();
        FullScreenUtils.setFullscreen(this, getResources().getColor(R.color.stateBarColor));
        mSpHelper = new SpHelper(this);

//        初始化视图
        initView();
    }

    private void initView(){
        mWebView = findViewById(R.id.web_view);

        mWebView = findViewById(R.id.web_view);
        mWebView.loadUrl(UrlConstants.BASE_WEB_URL);
        mWebView.setOnWebViewListener(new X5WebView.OnWebViewListener() {
            @Override
            public void onProgressChanged(WebView webView, int progress) {
                if (progress == 100) {
                    isAutoUser();
                }
            }
        });

    }
    private void isAutoUser(){
//        String username = mSpHelper.getAutoUser();
//        if (TextUtils.isEmpty(username)) {
//            return;
//        }
        String username="刘隐";
        mWebView.evaluateJavascript("javascript:nativeFunctionUserLogin('" + username + "')", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                builder.setMessage(s);
//                builder.setNegativeButton("确定", null);
//                builder.create().show();
            }
        });
    }
    private void requestPermission() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, PERMISSIONS_REQUEST_CODE);

        } else {
//            支付宝 SDK 已有所需的权限
//            Toast.makeText(this, "支付宝 SDK 已有所需的权限", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 权限获取回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {

                // 用户取消了权限弹窗
                if (grantResults.length == 0) {
                    Toast.makeText(this, "无法获取支付宝 SDK 所需的权限, 请到系统设置开启", Toast.LENGTH_SHORT).show();;
                    return;
                }

                // 用户拒绝了某些权限
                for (int x : grantResults) {
                    if (x == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(this, "无法获取支付宝 SDK 所需的权限, 请到系统设置开启", Toast.LENGTH_SHORT).show();;
                        return;
                    }
                }

                // 所需的权限均正常获取
                Toast.makeText(this, "支付宝 SDK 所需的权限已经正常获取", Toast.LENGTH_SHORT).show();;
            }
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        监听 android 后退按钮点击事件。
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            1、首先判断当前网页是否还可以进行后退页面的操作，如果可以的话那么就后退网页。
            if (mWebView.canGoBack() &&
                    !UrlConstants.BASE_WEB_URL.equals(mWebView.getUrl())) {
                mWebView.goBack();
                return true;
            }
//            2、如果网页已经不可以进行后退操作了（即：网页在首页中，虚拟任务栈中，只包含了 imooc 。）
//          在这种情况下，则会提示 "再按一次退出程序" ， 用户 两秒内再次点击后退按钮，则退出应用
            if (System.currentTimeMillis() - endTime > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                endTime = System.currentTimeMillis();
            } else {
                finish();
            }
        }
        return true;
    }
}
