package com.agora.classroom.agorapp.classroom.mediator;


import com.agora.classroom.agorapp.classroom.bean.msg.ChannelMsg;
import com.agora.classroom.agorapp.classroom.bean.msg.Cmd;
import com.agora.classroom.agorapp.classroom.bean.msg.PeerMsg;
import com.agora.classroom.agorapp.classroom.bean.user.User;
import com.agora.classroom.agorasdk.manager.RtmManager;

public class MsgMediator {

    public static void sendMessageToPeer(User user, Cmd cmd) {
        RtmManager.instance().sendMessageToPeer(String.valueOf(user.uid), new PeerMsg(cmd).toJsonString());
    }

    public static void sendMessage(ChannelMsg msg) {
        RtmManager.instance().sendMessage(msg.toJsonString());
    }

}
