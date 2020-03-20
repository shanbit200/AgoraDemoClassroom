package com.agora.classroom.agorapp.classroom.strategy;


import com.agora.classroom.agorapp.classroom.bean.msg.ChannelMsg;
import com.agora.classroom.agorapp.classroom.bean.msg.PeerMsg;
import com.agora.classroom.agorapp.classroom.bean.user.Student;
import com.agora.classroom.agorapp.classroom.bean.user.Teacher;

import java.util.List;

public interface ChannelEventListener {

    void onChannelInfoInit();

    void onLocalChanged(Student local);

    void onTeacherChanged(Teacher teacher);

    void onStudentsChanged(List<Student> students);

    void onChannelMsgReceived(ChannelMsg msg);

    void onPeerMsgReceived(PeerMsg msg);

    void onMemberCountUpdated(int count);

}
