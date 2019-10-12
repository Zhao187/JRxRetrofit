package com.baozi.myapplication;

import android.accounts.NetworkErrorException;
import android.app.Application;
import android.support.annotation.NonNull;

import com.baozi.linfeng.NetWorkManager;
import com.baozi.linfeng.factory.JSONFactory;
import com.baozi.linfeng.location.APICallBack;
import com.baozi.linfeng.location.onExceptionListener;
import com.baozi.linfeng.location.retrofit.GetCacheInterceptor;
import com.baozi.linfeng.location.retrofit.ParseInfo;
import com.baozi.linfeng.location.rxandroid.JErrorEnum;
import com.baozi.linfeng.location.rxandroid.JRxCompose;
import com.google.gson.JsonElement;

import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import okhttp3.Interceptor;
import okhttp3.Response;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NetWorkManager.init("https://api.apiopen.top/", this);
        NetWorkManager.addInterceptor(new GetCacheInterceptor());
        NetWorkManager.initKey("私钥", "公钥");//加密解密
        NetWorkManager.setDefaultRetry(5);//重试次数
        NetWorkManager.setDefaultTimeOut(20);//秒
        NetWorkManager.addParseInfo(
                new ParseInfo("code", "result", "message", "200")
        );
        NetWorkManager.setExceptionListener(throwable -> {
            if (throwable instanceof NullPointerException) {

            }
            if (throwable instanceof NetworkErrorException) {

            }
            if (throwable instanceof SocketException) {

            }
            return null;
        });
        NetWorkManager.setApiCallBack((code, msg, resultData) -> {
            if (code.equals("100")) {
                //跳转登陆页面
                return "登陆过期";
            }
            return msg;
        });
        Disposable subscribe = Observable.timer(500, TimeUnit.MILLISECONDS)
                .compose(JRxCompose.simple())
                .subscribe(t -> {

                }, JErrorEnum.toast);
    }
}
