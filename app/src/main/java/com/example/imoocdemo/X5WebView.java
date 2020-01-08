package com.example.imoocdemo;

import android.app.AlertDialog;
import android.content.Context;
import android.util.AttributeSet;

import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.Map;

public class X5WebView extends WebView {

    private Context mContext;
    private OnWebViewListener onWebViewListener;

    public X5WebView(Context context) {
        super(context);
        init(context);
    }

    public X5WebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public X5WebView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    public X5WebView(Context context, AttributeSet attributeSet, int i, boolean b) {
        super(context, attributeSet, i, b);
        init(context);
    }

    public X5WebView(Context context, AttributeSet attributeSet, int i, Map<String, Object> map, boolean b) {
        super(context, attributeSet, i, map, b);
        init(context);
    }
    private void init(Context context){
        this.mContext=context;
        addJavascriptInterface(new MyJavaScriptInterface(mContext),"androidJSBridge");
        initWebViewSettings();
        initChromeClient();
        initWebViewClient();
    }

    private void initWebViewSettings(){
        WebSettings webSettings=getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDisplayZoomControls(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
    }

    private void initWebViewClient(){
        setWebViewClient(new WebViewClient(){

        });
    }

    private void initChromeClient(){
        setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                if (onWebViewListener!=null){
                    onWebViewListener.onProgressChanged(webView,i);
                }
            }

            @Override
            public boolean onJsAlert(WebView webView, String s, String s1, JsResult jsResult) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage(s1);
                builder.setNegativeButton("原生确定", null);
                builder.create().show();
                jsResult.confirm();
                return true;
            }
        });
    }


    public void setOnWebViewListener(OnWebViewListener listener){
        this.onWebViewListener=listener;
    }
    public interface OnWebViewListener{
        void onProgressChanged(WebView webView,int progress);
    }
}
