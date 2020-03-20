package com.agora.classroom.agorapp.classroom.bean.channel;

import com.agora.classroom.agorapp.classroom.bean.JsonBean;
import com.agora.classroom.agorapp.classroom.bean.user.Student;
import com.agora.classroom.agorapp.classroom.bean.user.Teacher;

import java.util.ArrayList;
import java.util.List;

public class ChannelInfo extends JsonBean {

    public static int SHARE_UID = 7;

    public volatile Student local;
    public volatile Teacher teacher;
    public volatile List<Student> students;

    public ChannelInfo(Student local) {
        this.local = local;
        this.students = new ArrayList<>();
    }

}
