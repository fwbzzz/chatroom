package fwb.chatroom.service;

import fwb.chatroom.entity.MessageFromClient;
import fwb.chatroom.entity.MessageToClient;
import fwb.chatroom.utils.CommUtils;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/websocket")//将当前类标记为一个websocket类

public class WebSocket {
    // 存储所有连接到后端的websocket实体
    private static CopyOnWriteArraySet<WebSocket> clients =
            new CopyOnWriteArraySet<>();
    //除了缓存webSocekt实例之外还需要缓存所有的用户列表
    //sessionID与用户名，用一个线程安全的集合类
    private static Map<String,String> names = new ConcurrentHashMap<>();
    // 绑定当前websocket会话
    private Session session;
    // 当前客户端的用户名。缓存一下名字，省的每次都要从前端取
    private String userName;

    @OnOpen//当前端websocket和后端建立连接以后调用此方法
    public void onOpen(Session session) {
        this.session = session;//绑定当前session
        //1.2
        //返回与打开此会话的请求相关联的查询字符串。
        //前端发给后端的格式username=' + '${username},按照=拆分，取得第一个值
        //getQueryString()获取chat.ftl中
        //webSocket = new WebSocket('ws://127.0.0.1:80/websocket?username=' + '${username}');？后面的
        //将key与value值全部获取过来，如果要获取相应的value需要做字符串的截取
        String userName = session.getQueryString().split("=")[1];//按照=拆分，取得第二个参数
        this.userName = userName;
        //将客户端聊天实体保存到Clients
        clients.add(this);
        // 将当前用户以及sessionID保存到用户列表
        names.put(session.getId(),userName);
        System.out.println("有新的连接SessionID为：" + session.getId()
                + "用户名为：" +  userName);
        //发送给所有在线用户一个上线通知
        //需要将信息发送给前端，就要用  MessageToClient
        MessageToClient messageToClient = new MessageToClient();
        messageToClient.setContent(userName + "上线了！");
        messageToClient.setNames(names);//将用户列表给所有人更新一下
        //将messageToClient对象变成一个字符串发送给前端
        String jsonStr  = CommUtils.objectToJson(messageToClient);
        //foreach循环给所有用户发一遍
        for (WebSocket webSocket:clients
             ) {
//            System.out.println(jsonStr);
            webSocket.sendMsg(jsonStr);
        }
    }

    @OnError
    //如果出错控制台显示错误
    public void onError(Throwable e) {
        System.err.println("websocket连接失败！");
    }


    @OnMessage//服务器收到了浏览器发来的一个信息，控制私或者群聊
    public void onMessage(String msg) {
        // 先反序列化，将msg -> MessageFromClient
        MessageFromClient messageFromClient = (MessageFromClient) CommUtils
                .jsonToObject(msg,MessageFromClient.class);
        // 群聊信息
        if (messageFromClient.getType().equals("1")) {
            String content = messageFromClient.getMsg();//取得发送内容
            //将信息发给前端
            MessageToClient messageToClient = new MessageToClient();
            messageToClient.setContent(content);//信息
            messageToClient.setNames(names);//用户列表
            // 广播发送
            for (WebSocket webSocket : clients) {
                webSocket.sendMsg(CommUtils.objectToJson(messageToClient));
            }
            //私聊
        }else if (messageFromClient.getType().equals("2")) {
            // 私聊信息
            // {"to":"0-1-2-","msg":"33333","type":2}
            // 私聊内容
            String content = messageFromClient.getMsg();
            int toL = messageFromClient.getTo().length();//得到内容长度
            //存储发送对象
            String tos[] = messageFromClient.getTo()
                    .substring(0,toL-1).split("-");//以“-”为间隔，从开始截取到length — 1。
            //数组变成一个链表，为了方便给指定的ID发送信息
            List<String> lists = Arrays.asList(tos);
            // 给指定的SessionID发送信息
            //先遍历所有webSocket遍历一遍
            for (WebSocket webSocket : clients) {
                //要发送的ID在List中并且不包括本身
                if (lists.contains(webSocket.session.getId()) &&
                        this.session.getId() != webSocket.session.getId()) {
                    // 发送私聊信息
                    MessageToClient messageToClient = new MessageToClient();
                    messageToClient.setContent(userName,content);//。。。说。。。
                    messageToClient.setNames(names);
                    webSocket.sendMsg(CommUtils.objectToJson(messageToClient));
                }
            }
        }
    }

    //用户下线
    @OnClose//关闭的方法
    public void onClose() {
        //将客户端聊天实体移除
        clients.remove(this);
        // 将当前用户移除
        names.remove(session.getId());//（k,v）
        System.out.println(userName + "用户下线了");
        // 发送给所有在线用户一个上下线通知
        MessageToClient messageToClient = new MessageToClient();
        messageToClient.setContent(userName + "下线了！");
        messageToClient.setNames(names);
        //将messageToClient对象变成一个字符串
        String jsonStr  = CommUtils.objectToJson(messageToClient);
        for (WebSocket webSocket:clients
                ) {
            webSocket.sendMsg(jsonStr);
        }
    }

    //发送信息的方法
    public void sendMsg(String msg) {
        try {
            //返回一个引用，一个表示此会话对等点的远程端点对象，该对象能够同步地向对等点发送消息。
            //getBasicRemote 此会话可以缓冲的传入文本消息的最大长度。
            //sendText 发送一条文本消息，阻塞直到所有消息都被传输。
            this.session.getBasicRemote().sendText(msg);//将msg发给浏览器
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
