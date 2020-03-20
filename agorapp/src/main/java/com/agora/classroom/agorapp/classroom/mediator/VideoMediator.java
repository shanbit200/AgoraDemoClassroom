package com.agora.classroom.agorapp.classroom.mediator;


import com.agora.classroom.agorapp.classroom.widget.RtcVideoView;
import com.agora.classroom.agorasdk.manager.RtcManager;

public class VideoMediator {

    public static void setupLocalVideo(RtcVideoView item) {
        RtcManager manager = RtcManager.instance();
        if (item.getSurfaceView() == null) {
            item.setSurfaceView(manager.createRendererView(item.getContext()));
        }
        manager.setupLocalVideo(item.getSurfaceView(), VideoCanvas.RENDER_MODE_HIDDEN);
        manager.startPreview();
    }

    public static void setupRemoteVideo(RtcVideoView item, int uid) {
        RtcManager manager = RtcManager.instance();
        if (item.getSurfaceView() == null) {
            item.setSurfaceView(manager.createRendererView(item.getContext()));
        }
        manager.setupRemoteVideo(item.getSurfaceView(), VideoCanvas.RENDER_MODE_HIDDEN, uid);
    }

}
