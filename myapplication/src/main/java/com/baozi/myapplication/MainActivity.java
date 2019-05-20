package com.baozi.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.baozi.linfeng.NetWorkManager;
import com.baozi.linfeng.factory.JSONFactory;
import com.baozi.linfeng.location.APICallBack;
import com.baozi.linfeng.location.SimpleParams;
import com.baozi.linfeng.location.retrofit.JApi;
import com.baozi.linfeng.location.retrofit.RetrofitUtil;
import com.baozi.linfeng.location.rxandroid.NetWorkTransformer;
import com.baozi.linfeng.location.rxandroid.RxParseInfo;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetWorkManager.init("https://api.apiopen.top/", getApplication());
        NetWorkManager.addParseInfo(
                new RxParseInfo("code", "result", "message", "100")
                        .setCheckSuccess(new RxParseInfo.CheckSuccess() {
                            @Override
                            public boolean isSuccess(JsonObject jsonObject) {
                                JsonElement result = jsonObject.get("result");
                                String code = jsonObject.get("code").toString();
                                return result.isJsonObject() && "100".equals(code);
                            }
                        })
        );
        NetWorkManager.setApiCallBack(new APICallBack() {
            @Override
            public String callback(String code, String resultData) {
                if (code.equals("1")) {
                    return "状态不对";
                }
                if (code.equals("200")) {
                    return "脑子不对";
                }
                JsonElement jsonElement = JSONFactory.parseJson(resultData);
                return JSONFactory.getValue(jsonElement, "msg");
            }
        });

        Disposable journalismApi = RetrofitUtil.getApi(JApi.class)
                .BasePost("recommendPoetry",
                        SimpleParams.create()
                )
                .compose(new NetWorkTransformer())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String stringBaseResponse) throws Exception {
                        Log.i("data", stringBaseResponse);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        setContentView(R.layout.activity_main);
    }
}

