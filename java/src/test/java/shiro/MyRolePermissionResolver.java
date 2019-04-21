package shiro;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermission;

import java.util.Arrays;
import java.util.Collection;

public class MyRolePermissionResolver implements RolePermissionResolver {
    /*
    RolePermissionResolver用于根据角色字符串来解析得到权限集合
     */
    public Collection<Permission> resolvePermissionsInRole(String s) {
        if ("role1".equals(s)) {
            return Arrays.asList((Permission) new WildcardPermission("menu:*"));
        }
        return null;
    }
}
