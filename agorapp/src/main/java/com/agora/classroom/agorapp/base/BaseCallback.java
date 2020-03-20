package com.agora.classroom.agorapp.base;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.agora.classroom.agorabase.ToastManager;
import com.agora.classroom.agorabase.network.BusinessException;
import com.agora.classroom.agorabase.network.RetrofitManager;

import java.util.Locale;
import java.util.Map;
import com.agora.classroom.agorapp.service.bean.ResponseBody;
import com.agora.classroom.agorabase.Callback;


public class BaseCallback<T> extends RetrofitManager.Callback<ResponseBody<T>> {

    public BaseCallback(@NonNull final SuccessCallback<T> callback) {
        super(0, new Callback<ResponseBody<T>>() {
            @Override
            public void onSuccess(ResponseBody<T> res) {
                callback.onSuccess(res.data);
            }

            @Override
            public void onFailure(Throwable throwable) {
                checkError(throwable);
            }
        });
    }

    public BaseCallback(@NonNull final SuccessCallback<T> success, @Nullable final FailureCallback failure) {
        super(0, new Callback<ResponseBody<T>>() {
            @Override
            public void onSuccess(ResponseBody<T> res) {
                success.onSuccess(res.data);
            }

            @Override
            public void onFailure(Throwable throwable) {
                checkError(throwable);
                if (failure != null) {
                    failure.onFailure(throwable);
                }
            }
        });
    }

    private static void checkError(Throwable throwable) {
        String message = throwable.getMessage();
        if (throwable instanceof BusinessException) {
            int code = ((BusinessException) throwable).getCode();
          //  Map<String, Map<Integer, String>> languages = EduApplication.instance.config.multiLanguage;
            Map<String, Map<Integer, String>> languages = null;
            if (languages != null) {
                Locale locale = Locale.getDefault();
                if (!Locale.SIMPLIFIED_CHINESE.toString().equals(locale.toString())) {
                    locale = Locale.US;
                }
                String key = String.format("%s-%s", locale.getLanguage(), locale.getCountry()).toLowerCase();
                Map<Integer, String> stringMap = languages.get(key);
                if (stringMap != null) {
                    String string = stringMap.get(code);
                    if (!TextUtils.isEmpty(string)) {
                        message = string;
                    }
                }
            }
            if (TextUtils.isEmpty(message)) {
                //message = EduApplication.instance.getString(R.string.request_error, code);
            }
        }
        if (!TextUtils.isEmpty(message)) {
            ToastManager.showShort(message);
        }
    }

    public interface SuccessCallback<T> {
        void onSuccess(T data);
    }

    public interface FailureCallback {
        void onFailure(Throwable throwable);
    }

}
