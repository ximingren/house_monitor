package shiro.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import shiro.entity.User;

import java.util.Set;

public interface UserDao {
    public User createUser(User user);

    public void updateUser(User user);

    public void deleteUser(Long userId);

    public void correlationRoles(Long userId, Long... roleIds);

    public void uncorrelationRoles(Long userId, Long... roleIds);

    User findOne(Long userId);

    User findByUsername(String username);

    Set<String> findRoles(String useranem);

    Set<String> findPermissions(String username);
}
