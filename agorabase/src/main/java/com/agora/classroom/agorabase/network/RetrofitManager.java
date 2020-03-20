package com.agora.classroom.agorabase.network;

import androidx.annotation.NonNull;

import com.agora.classroom.agorabase.Callback;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class RetrofitManager {

    private static RetrofitManager instance;

    private OkHttpClient client;

    private RetrofitManager() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.addInterceptor(chain -> {
            Request original = chain.request();
            Request request = original.newBuilder()
                    .header("Content-Type", "application/json;charset=UTF-8")
                    .method(original.method(), original.body())
                    .build();
            return chain.proceed(request);
        });
        client = builder.build();
    }

    public static RetrofitManager instance() {
        if (instance == null) {
            synchronized (RetrofitManager.class) {
                if (instance == null) {
                    instance = new RetrofitManager();
                }
            }
        }
        return instance;
    }

    public <T> T getService(String baseUrl, Class<T> tClass) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(tClass);
    }

    public static class Callback<T extends ResponseBody> implements retrofit2.Callback<T> {
        private int code;
        private com.agora.classroom.agorabase.Callback<T> callback;

        public Callback(int code, @NonNull com.agora.classroom.agorabase.Callback<T> callback) {
            this.code = code;
            this.callback = callback;
        }

        @Override
        public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
            if (response.errorBody() != null) {
                try {
                    callback.onFailure(new Throwable(response.errorBody().string()));
                } catch (IOException e) {
                    callback.onFailure(e);
                }
            } else {
                T body = response.body();
                if (body == null) {
                    callback.onFailure(new Throwable("response body is null"));
                } else {
                    if (body.code != code) {
                        callback.onFailure(new BusinessException(body.code, body.msg.toString()));
                    } else {
                        callback.onSuccess(body);
                    }
                }
            }
        }

        @Override
        public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
            callback.onFailure(t);
        }
    }

}
