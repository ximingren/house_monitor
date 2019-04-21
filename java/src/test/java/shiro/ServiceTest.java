package shiro;

import org.apache.shiro.codec.Base64;
import org.junit.Assert;
import org.junit.Test;
import shiro.entity.Role;

import java.util.Set;

public class ServiceTest extends BaseTest {

    @Test
    public void testUserRolePermissionRelation() {
        Set<String> roles = userService.findRoles(u1.getUsername());
        Assert.assertEquals(1, roles.size());

        Assert.assertTrue(roles.contains(r1.getRole()));

        Set<String> permissions = userService.findPermissions(u1.getUsername());
        Assert.assertEquals(1, permissions.size());
        Assert.assertTrue(permissions.contains(p1.getPermission()));
    }
}
