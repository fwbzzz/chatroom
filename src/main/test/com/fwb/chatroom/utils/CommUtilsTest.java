//package com.fwb.chatroom.utils;
//
//import fwb.chatroom.entity.User;
//import fwb.chatroom.utils.CommUtils;
//import org.junit.Assert;
//import org.junit.Test;
//
//import java.util.Properties;
//
///**
// * @program: chatroom
// * @description:
// * @author: fwb
// * @create: 2019-08-17 18:31
// **/
//public class CommUtilsTest {
//    @Test
//    public void loadProperties() {
//        String fileName = "datasource.properties";
//        Properties properties = CommUtils.loadProperties(fileName);
//        System.out.println(properties);
//        String url = properties.getProperty("url");
//        Assert.assertNull(url);
//    }
//
//    @Test
//    public void gsonTest1() {
//        User user = new User();
//        user.setId(10);
//        user.setUserName("test");
//        user.setPassword("123");
//        String jsonStr = CommUtils.object2Json(user);
//        System.out.println(jsonStr);
//    }
//
//    @Test
//    public void gsonTest2() {
//        String jsonStr = "{\"id\":10,\"userName\":\"test\",\"password\":\"123\"}";
//        User user = (User) CommUtils.json2Object(jsonStr,User.class);
//        System.out.println(user);
//    }
//}
