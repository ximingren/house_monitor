package shiro.dao;

import shiro.entity.Permission;

public interface PermissionDao {
    public Permission createPermission(Permission permission);

    public void deletePermission(Long permissionId);
}
