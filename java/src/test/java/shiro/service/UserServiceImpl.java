package shiro.service;

import shiro.dao.UserDao;
import shiro.dao.UserDaoImpl;
import shiro.entity.User;

import java.util.Set;

public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDaoImpl();
    private PasswordHelper passwordHelper = new PasswordHelper();

    public User createUser(User user) {
//        加密密码
        passwordHelper.encryptPassword(user);
        return userDao.createUser(user);
    }

    public void changePassword(Long userId, String newPassword) {
        User user = userDao.findOne(userId);
        user.setPassword(newPassword);
        passwordHelper.encryptPassword(user);
        userDao.updateUser(user);
    }

    public void correlationRoles(Long userId, Long... roleIds) {
        userDao.correlationRoles(userId, roleIds);
    }

    public void uncorrelationRoles(Long userId, Long... roleIds) {
        userDao.uncorrelationRoles(userId, roleIds);
    }

    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public Set<String> findRoles(String username) {
        return userDao.findRoles(username);
    }

    public Set<String> findPermissions(String username) {
        return userDao.findPermissions(username);
    }
}
