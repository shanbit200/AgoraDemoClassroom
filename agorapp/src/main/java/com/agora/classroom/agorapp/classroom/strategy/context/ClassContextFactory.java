package com.agora.classroom.agorapp.classroom.strategy.context;

import android.content.Context;

import com.agora.classroom.agorapp.classroom.annotation.ClassType;
import com.agora.classroom.agorapp.classroom.bean.user.Student;
import com.agora.classroom.agorapp.classroom.strategy.RtmChannelStrategy;


public class ClassContextFactory {

    private Context context;

    public ClassContextFactory(Context context) {
        this.context = context;
    }

    public ClassContext getClassContext(@ClassType int classType, String channelId, Student local) {
        RtmChannelStrategy strategy = new RtmChannelStrategy(channelId, local);
        switch (classType) {
            case ClassType.ONE2ONE:
                return new OneToOneClassContext(context, strategy);
            case ClassType.SMALL:
                return new SmallClassContext(context, strategy);
            case ClassType.LARGE:
            default:
                return new LargeClassContext(context, strategy);
        }
    }

}
