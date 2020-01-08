package com.example.imoocdemo;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import com.example.imoocdemo.helpers.SpHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class MyJavaScriptInterface {
    private Context mContext;
    private SpHelper mSpHelper;
    public MyJavaScriptInterface(Context context) {
        this.mContext=context;
        this.mSpHelper=new SpHelper(mContext);
    }
    @JavascriptInterface
    public boolean register(String userJson){
        boolean result=false;
        try {
            JSONObject jsonObject=new JSONObject(userJson);
            result=mSpHelper.setUser(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
    @JavascriptInterface
    public String login (String userJson) {
        String result = "-1";
        try {
            JSONObject user = new JSONObject(userJson);
            String password = mSpHelper.getUser(user.getString(SpHelper.KEY_USER_NAME));
//        如果password返回为空，则表示没有该用户
            if (TextUtils.isEmpty(password)) {
                result = "1";
            } else {
                if (!password.equals(user.getString(SpHelper.KEY_PASSWORD))) {
                    result = "2";
                } else {
                    result = "0";
                    mSpHelper.setAutoUser(user.getString(SpHelper.KEY_USER_NAME));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
    @JavascriptInterface
    public boolean logout () {
        return mSpHelper.clearAutoUser();
    }

//    /**
//     * 微信支付
//     * @param payJson 支付商品信息
//     */
//    @JavascriptInterface
//    public void wxPay (String payJson) {
//        mWxPayHelper.pay(payJson);
//    }
//
//    /**
//     * 支付宝支付
//     * @param payJson 支付商品信息
//     */
//    @JavascriptInterface
//    public void aliPay (String payJson) {
//        aliPayHelper.pay(payJson);
//    }
}
