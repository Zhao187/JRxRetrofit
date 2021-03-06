package com.baozi.linfeng.location.rxandroid;

import android.text.TextUtils;

import com.baozi.linfeng.factory.JSONFactory;
import com.baozi.linfeng.location.model.ParameterTypeImpl;

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Jlanglang on 2017/8/31 0031.
 * 简书:http://www.jianshu.com/u/6bac141ea5fe
 */
public class JsonArrayParesTransformer<T> implements ObservableTransformer<String, List<T>> {
    private Class<T> zClass;

    public JsonArrayParesTransformer(Class<T> zClass) {
        this.zClass = zClass;
    }


    @Override
    public ObservableSource<List<T>> apply(Observable<String> upstream) {
        return upstream.compose(new NetWorkTransformer())
                .observeOn(Schedulers.computation())
                .flatMap(s -> {
                    s = TextUtils.isEmpty(s) ? "[]" : s;
                    ParameterTypeImpl parameterType = new ParameterTypeImpl(List.class, new Type[]{zClass});
                    List<T> list = JSONFactory.fromJson(s, parameterType);
                    return Observable.just(list);
                })
                .observeOn(AndroidSchedulers.mainThread());
    }
}
