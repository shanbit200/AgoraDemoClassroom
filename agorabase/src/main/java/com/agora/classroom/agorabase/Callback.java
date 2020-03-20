package com.agora.classroom.agorabase;

public interface Callback<T> {

    void onSuccess(T res);

    void onFailure(Throwable throwable);

}
