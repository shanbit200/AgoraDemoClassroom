package com.agora.classroom.agorapp.classroom.bean.msg;


import com.agora.classroom.agorapp.classroom.bean.JsonBean;

public class PeerMsg extends JsonBean {

    private int cmd;
    public String text;

    public PeerMsg(Cmd cmd) {
        this.cmd = cmd.getCode();
    }

    public Cmd getCmd() {
        return Cmd.get(cmd);
    }

}
