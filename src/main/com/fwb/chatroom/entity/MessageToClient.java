package fwb.chatroom.entity;

import java.util.Map;

/**
 * @program: chatroom
 * @description: 后端发送给前端的信息实体
 * 但是需要将这个类变成一个Json字符串（用Gson库将一个对象序列化成一个字符串）
 * @author: fwb
 * @create: 2019-08-18 13:35
 **/
public class MessageToClient {
    //聊天的内容
    private String content;
    //服务端所有登录的用户列表，前一个String是SessionID（QQ号），后一个为用户。
    private Map<String,String> names;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    //。。。说。。。
    public void setContent(String userName,String msg) {
        this.content = userName + "说:" + msg;
    }

    public Map<String, String> getNames() {
        return names;
    }

    public void setNames(Map<String, String> names) {
        this.names = names;
    }
}
