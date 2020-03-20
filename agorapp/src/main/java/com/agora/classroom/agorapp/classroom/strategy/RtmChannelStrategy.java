package com.agora.classroom.agorapp.classroom.strategy;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.agora.classroom.agorapp.classroom.bean.msg.ChannelMsg;
import com.agora.classroom.agorapp.classroom.bean.msg.PeerMsg;
import com.agora.classroom.agorapp.classroom.bean.user.Student;
import com.agora.classroom.agorapp.classroom.bean.user.Teacher;
import com.agora.classroom.agorasdk.listener.RtmEventListener;
import com.agora.classroom.agorasdk.manager.RtcManager;
import com.agora.classroom.agorasdk.manager.RtmManager;
import com.agora.classroom.agorasdk.manager.SdkManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class RtmChannelStrategy extends ChannelStrategy<List<RtmChannelAttribute>> {

    public RtmChannelStrategy(String channelId, Student local) {
        super(channelId, local);
        RtmManager.instance().registerListener(rtmEventListener);
    }

    @Override
    public void release() {
        super.release();
        RtmManager.instance().unregisterListener(rtmEventListener);
    }

    @Override
    public void joinChannel(String rtcToken) {
        RtmManager.instance().joinChannel(new HashMap<String, String>() {{
            put(SdkManager.CHANNEL_ID, getChannelId());
        }});
        RtcManager.instance().joinChannel(new HashMap<String, String>() {{
            put(SdkManager.TOKEN, rtcToken);
            put(SdkManager.CHANNEL_ID, getChannelId());
            put(SdkManager.USER_ID, getLocal().getUserId());
        }});
    }

    @Override
    public void leaveChannel() {
        RtmManager.instance().leaveChannel();
        RtcManager.instance().leaveChannel();
    }

    @Override
    public void queryOnlineStudentNum(@NonNull Callback<Integer> callback) {
        List<Student> students = getStudents();
        Set<String> set = new HashSet<>();
        for (Student student : students) {
            set.add(student.getUserId());
        }

        if (students.size() == 0) {
            callback.onSuccess(0);
        } else {
            RtmManager.instance().queryPeersOnlineStatus(set, new Callback<Map<String, Boolean>>() {
                @Override
                public void onSuccess(Map<String, Boolean> stringBooleanMap) {
                    int num = 0;
                    for (Map.Entry<String, Boolean> entry : stringBooleanMap.entrySet()) {
                        if (entry.getValue()) {
                            num++;
                        }
                    }
                    callback.onSuccess(num);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }
            });
        }
    }

    @Override
    public void queryChannelInfo(@Nullable Callback<Void> callback) {
        RtmManager.instance().getChannelAttributes(getChannelId(), new Callback<List<RtmChannelAttribute>>() {
            @Override
            public void onSuccess(List<RtmChannelAttribute> rtmChannelAttributes) {
                parseChannelInfo(rtmChannelAttributes);
                if (callback != null)
                    callback.onSuccess(null);
            }

            @Override
            public void onFailure(Throwable throwable) {
                if (callback != null)
                    callback.onFailure(throwable);
            }
        });
    }

    @Override
    public void parseChannelInfo(List<RtmChannelAttribute> data) {
        List<Student> students = new ArrayList<>();
        boolean hasMyself = false;
        for (RtmChannelAttribute attribute : data) {
            String value = attribute.getValue();
            if (TextUtils.equals(attribute.getKey(), "teacher")) {
                setTeacher(Teacher.fromJson(value, Teacher.class));
            } else if (TextUtils.equals(attribute.getKey(), getLocal().getUserId())) {
                hasMyself = true;
                Student local = Student.fromJson(value, Student.class);
                setLocal(local);
            } else {
                students.add(Student.fromJson(value, Student.class));
            }
        }
        if (!hasMyself) {
            Student local = getLocal();
            local.isGenerate = true;
            setLocal(local);
        }
        setStudents(students);
    }

    @Override
    public void updateLocalAttribute(Student local, @Nullable Callback<Void> callback) {
        RtmChannelAttribute attribute = new RtmChannelAttribute(String.valueOf(local.uid), local.toJsonString());
        RtmManager.instance().addOrUpdateChannelAttributes(getChannelId(), Collections.singletonList(attribute), callback);
    }

    @Override
    public void clearLocalAttribute(@Nullable Callback<Void> callback) {
        String key = getLocal().getUserId();
        RtmManager.instance().deleteChannelAttributesByKeys(getChannelId(), Collections.singletonList(key), callback);
    }

    private RtmEventListener rtmEventListener = new RtmEventListener() {
        @Override
        public void onJoinChannelSuccess(String channel) {
            queryChannelInfo(new Callback<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    if (channelEventListener != null) {
                        channelEventListener.onChannelInfoInit();
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {

                }
            });
        }

        @Override
        public void onAttributesUpdated(List<RtmChannelAttribute> list) {
            parseChannelInfo(list);
        }

        @Override
        public void onMessageReceived(RtmMessage rtmMessage, RtmChannelMember rtmChannelMember) {
            if (channelEventListener != null) {
                ChannelMsg msg = ChannelMsg.fromJson(rtmMessage.getText(), ChannelMsg.class);
                msg.isMe = TextUtils.equals(rtmChannelMember.getUserId(), getLocal().getUserId());
                channelEventListener.onChannelMsgReceived(msg);
            }
        }

        @Override
        public void onMessageReceived(RtmMessage rtmMessage, String s) {
            if (channelEventListener != null) {
                PeerMsg msg = PeerMsg.fromJson(rtmMessage.getText(), PeerMsg.class);
                channelEventListener.onPeerMsgReceived(msg);
            }
        }

        @Override
        public void onMemberCountUpdated(int i) {
            if (channelEventListener != null) {
                channelEventListener.onMemberCountUpdated(i);
            }
        }
    };

}
