package fwb.chatroom.service;

import fwb.chatroom.dao.AccountDao;
import fwb.chatroom.entity.User;

public class AccountService {
    private AccountDao accountDao = new AccountDao();

    // 用户登陆
    public boolean userLogin(String userName,String password) {
        User user = accountDao.userLogin(userName,password);
        if (user == null) {
            return false;
        }
        return true;
    }

    // 用户注册
    public boolean userRegister(String userName,String password) {
        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        return accountDao.userRegister(user);
    }
}
