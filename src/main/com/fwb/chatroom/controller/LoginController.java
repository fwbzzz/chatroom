package fwb.chatroom.controller;

import	java.util.HashMap;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import fwb.chatroom.config.FreeMarkerListener;
import fwb.chatroom.service.AccountService;
import fwb.chatroom.utils.CommUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet(urlPatterns = "/login")
public class LoginController extends HttpServlet {
    private AccountService accountService = new AccountService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = req.getParameter("username");//获得从前台提交的username与password(index.html)
        String password = req.getParameter("password");
        resp.setContentType("text/html;charset=utf8");
        PrintWriter out = resp.getWriter();//响应
        //如果username，password为空
        if (CommUtils.strIsNull(userName) || CommUtils.strIsNull(password)) {
            // 登录失败,停留登录页面
            out.println("    <script>\n" +
                    "        alert(\"用户名或密码为空!\");\n" +
                    "        window.location.href = \"/index.html\";\n" +
                    "    </script>");
        }
        //如果用户名密码不为空
        if (accountService.userLogin(userName,password)) {
            // 登录成功,跳转到聊天页面
            // 加载chat.ftl
            //此方法在后文定义
            Template template = getTemplate(req,"/chat.ftl");
            Map<String, String> map = new HashMap<>();//用HashMap保存一些参数
            map.put("username",userName);
            try {
                template.process(map, out);//将相应的配置以及响应交给前端去处理（可以将用户名显示在前端页面）
            } catch (TemplateException e) {
                e.printStackTrace();
            }
        }else {
            // 登录失败,停留在登录页面
            out.println("    <script>\n" +
                    "        alert(\"用户名或密码不正确!\");\n" +
                    "        window.location.href = \"/index.html\";\n" +
                    "    </script>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }


    //加载chat.ftlw,传入需要加载的文件名称filename
    private Template getTemplate(HttpServletRequest req,String fileName) {
        //获取刚才配置好的cfg，通过对应的key，获取的value是configuration这个类，
        Configuration cfg = (Configuration)//默认的getAttribute、setAttribute都是Object类，需要强转
                req.getServletContext().getAttribute(FreeMarkerListener.TEMPLATE_KEY);
        try {
            //从模板缓存中检索具有给定名称的模板，如果它是没找到/旧的，则首先将其加载到缓存中
            return cfg.getTemplate(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取失败就返回空值
        return null;
    }
}
