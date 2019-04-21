package shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.apache.shiro.util.ThreadContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import shiro.entity.Permission;
import shiro.entity.Role;
import shiro.entity.User;
import shiro.service.*;

public abstract class BaseTest {
    protected PermissionService permissionService = new PermissionServiceImpl();
    protected Permission p1;
    protected Role r1;
    protected User u1;
    protected RoleService roleService = new RoleServiceImpl();
    protected UserService userService = new UserServiceImpl();
    @Before
    public void setUp() {
        p1 = new Permission("user:create", "用户模块新增", true);
//        permissionService.createPermission(p1);
        r1 = new Role("admin", "管理员", true);
//        roleService.createRole(r1);
//        roleService.correlationPermissions(r1.getId(), p1.getId());
        u1 = new User("zhang", "123");
//        userService.createUser(u1);
//        userService.correlationRoles(u1.getId(), r1.getId());
    }

    @After
    public void tearDown() throws Exception {
//        退出时请解除subject到线程，否则对下次测试造成影响？？
        ThreadContext.unbindSubject();
    }

    protected void login(String configFile, String username, String password) {
        Factory<SecurityManager> factory = new IniSecurityManagerFactory(configFile);
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        subject.login(token);
    }
}
