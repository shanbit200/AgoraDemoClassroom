package com.agora.classroom.agorapp.classroom.strategy.context;


import com.agora.classroom.agorapp.classroom.bean.msg.ChannelMsg;
import com.agora.classroom.agorapp.classroom.bean.user.Teacher;
import com.agora.classroom.agorasdk.annotation.NetworkQuality;

public interface ClassEventListener {

    void onTeacherInit(Teacher teacher);

    void onNetworkQualityChanged(@NetworkQuality int quality);

    void onClassStateChanged(boolean isStart);

    void onWhiteboardIdChanged(String id);

    void onLockWhiteboard(boolean locked);

    void onMuteLocalChat(boolean muted);

    void onMuteAllChat(boolean muted);

    void onChannelMsgReceived(ChannelMsg msg);

    void onScreenShareJoined(int uid);

    void onScreenShareOffline(int uid);

}
