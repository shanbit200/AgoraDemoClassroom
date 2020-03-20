package com.agora.classroom.agorapp.service;

import com.agora.classroom.agorabase.BuildConfig;
import com.agora.classroom.agorapp.service.bean.response.AppConfig;
import com.agora.classroom.agorapp.service.bean.response.AppVersion;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CommonService {

    // osType 1 iOS 2 Android
    // terminalType 1 phone 2 pad
    @GET("edu/v1/app/version?osType=2&terminalType=1&appVersion=" + BuildConfig.VERSION_NAME)
    Call<ResponseBody<AppVersion>> appVersion(@Query("appCode") String appCode);

    // platform 0 web 1 iOS 2 Android
    // device 0 pc 1 phone 2 pad
    @GET("edu/v1/config?platform=2&device=1&version=" + BuildConfig.VERSION_NAME)
    Call<ResponseBody<AppConfig>> config();

}
