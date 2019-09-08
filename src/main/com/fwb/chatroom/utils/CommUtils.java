package fwb.chatroom.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

//公共方法
public class CommUtils {
    private static final Gson gson = new GsonBuilder().create();
    private CommUtils(){}

    /**
     * 根据指定的文件名加载配置文件
     * @param fileName 配置文件名
     * @return
     */
    public static Properties loadProperties(String fileName) {
        Properties properties = new Properties();
        // 获取当前配置文件夹下的文件输入流
        InputStream in = CommUtils.class.getClassLoader()
                .getResourceAsStream(fileName);
        // 加载配置文件中的所有内容
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static String objectToJson(Object obj) {
        return gson.toJson(obj);
    }

    public static Object jsonToObject(String jsonStr,Class objClass) {
        return gson.fromJson(jsonStr,objClass);
    }

    //判断输入的用户名/密码是否为空
    public static boolean strIsNull(String str) {
        return str == null || str.equals("");//必须先写前面的再写后面的，防止空指针
    }

}
