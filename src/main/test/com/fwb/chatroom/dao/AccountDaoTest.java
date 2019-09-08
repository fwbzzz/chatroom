//package com.fwb.chatroom.dao;
//
//import fwb.chatroom.dao.AccountDao;
//import fwb.chatroom.entity.User;
//import org.junit.Assert;
//import org.junit.Test;
//
///**
// * @program: chatroom
// * @description:
// * @author: fwb
// * @create: 2019-08-17 18:30
// **/
//public class AccountDaoTest {
//    private AccountDao accountDao = new AccountDao();
//    @Test
//    public void userLogin() {
//        User user = accountDao.userLogin("test","123");
//        System.out.println(user);
//        Assert.assertNotNull(user);
//    }
//
//    @Test
//    public void userRegister() {
//        User user = new User();
//        user.setUserName("test2");
//        user.setPassword("123");
//        boolean isSuccess = accountDao.userRegister(user);
//        Assert.assertTrue(isSuccess);
//    }
//}
