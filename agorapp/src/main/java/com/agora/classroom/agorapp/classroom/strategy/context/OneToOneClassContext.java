package com.agora.classroom.agorapp.classroom.strategy.context;

import android.content.Context;

import androidx.annotation.NonNull;

import com.agora.classroom.agorapp.classroom.bean.user.Student;
import com.agora.classroom.agorapp.classroom.bean.user.Teacher;
import com.agora.classroom.agorapp.classroom.bean.user.User;
import com.agora.classroom.agorapp.classroom.strategy.ChannelStrategy;
import com.agora.classroom.agorasdk.manager.RtcManager;


public class OneToOneClassContext extends ClassContext {

    OneToOneClassContext(Context context, ChannelStrategy strategy) {
        super(context, strategy);
    }

    @Override
    public void checkChannelEnterable(@NonNull Callback<Boolean> callback) {
        channelStrategy.queryChannelInfo(new Callback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                channelStrategy.queryOnlineStudentNum(new Callback<Integer>() {
                    @Override
                    public void onSuccess(Integer integer) {
                        callback.onSuccess(integer < EduApplication.instance.config.one2OneStudentLimit);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        callback.onFailure(throwable);
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    @Override
    void preConfig() {
        RtcManager.instance().setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
        RtcManager.instance().setClientRole(Constants.CLIENT_ROLE_BROADCASTER);
        RtcManager.instance().enableDualStreamMode(false);
    }

    @Override
    public void onChannelInfoInit() {
        super.onChannelInfoInit();
        if (channelStrategy.getLocal().isGenerate) {
            channelStrategy.updateLocalAttribute(channelStrategy.getLocal(), null);
        }
    }

    @Override
    public void onTeacherChanged(Teacher teacher) {
        super.onTeacherChanged(teacher);
        if (classEventListener instanceof OneToOneClassEventListener) {
            runListener(() -> ((OneToOneClassEventListener) classEventListener).onTeacherMediaChanged(teacher));
        }
    }

    @Override
    public void onLocalChanged(Student local) {
        super.onLocalChanged(local);
        if (local.isGenerate) return;
        if (classEventListener instanceof OneToOneClassEventListener) {
            runListener(() -> ((OneToOneClassEventListener) classEventListener).onLocalMediaChanged(local));
        }
    }

    public interface OneToOneClassEventListener extends ClassEventListener {
        void onTeacherMediaChanged(User user);

        void onLocalMediaChanged(User user);
    }

}
