package fwb.chatroom.config;

/**
 * @program: chatroom
 * @description: 全局共享的一个配置，当项目启动的时候，
 * 监听器就启动了这样可以将一些全局的配置扔进去，这样各个Servlet都可以使用
 * @author: fwb
 * @create: 2019-08-18 12:26
 **/
import	java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import freemarker.template.Configuration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


/*
ServletContextListener这个接口中有一个方法：
contextInitialized
当项目启动，tomcat就会帮助我们调用这个方法
contextDestroyed
当项目终止会调用这个方法，这样可以将一些全局的资源释放放在这个里面
 */
@WebListener//加过之后这个类就具备了监听器的能力
public class FreeMarkerListener implements ServletContextListener {
    /*
    读取的时候都是kv的形式，根据相应的key值取得value值
    所以当调用TEMPLATE_KEY的时候，会取得相对应的value值，value值就是Configuration
     */
    public static final String TEMPLATE_KEY = "_template_";
    @Override
    //当项目一启动就会调用这个方法（不需要自己调动）
    public void contextInitialized(ServletContextEvent sce) {
        //配置freemarker的页面在哪个路径下

        // 配置版本
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_0);
        // 配置加载ftl的路径（指定从哪加载）
        try {
            //此处使用绝对路径
            cfg.setDirectoryForTemplateLoading(new File("F:\\Java14\\chatroom\\src\\main\\webapp"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 配置页面编码（最好与前端保持一致）
        cfg.setDefaultEncoding(StandardCharsets.UTF_8.displayName());//默认区域设置中此字符集的显示名称
        /*
        将整个配置写入上下文中，kv格式
        当项目启动的时候，就会把叫TEMPLATE_KEY，以及value注册到整个项目中
        调用的时候这样调用getServletContext().getAttribute
         */
        sce.getServletContext().setAttribute(TEMPLATE_KEY,cfg);
    }

    @Override
    //当项目终止的时候调用此方法
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
