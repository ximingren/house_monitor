package shiro;

import org.junit.Test;

public class UserRealmTest extends BaseTest {
    @Test
    public void testLoginSuccess() {
        login("classpath:shiro-login.ini", u1.getUsername(), "123");
    }
}
