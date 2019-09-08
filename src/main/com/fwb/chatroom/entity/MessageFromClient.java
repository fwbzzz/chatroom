package fwb.chatroom.entity;

import lombok.Data;

/**
 * @program: chatroom
 * @description: 前端发送给后端的信息实体类
 * @author: fwb
 * @create: 2019-08-18 13:33
 **/

public class MessageFromClient {
    // 具体的聊天信息
    private String msg;

    // 聊天类别: 1表示群聊 2表示私聊
    private String type;

    // 私聊的对象SessionID
    private String to;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}

