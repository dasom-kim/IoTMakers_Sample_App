package com.kt.gigaiot_sdk.Callback;

public interface APICallback<T> {
    void onStart();
    void onDoing(T t);
    void onFail();
}
