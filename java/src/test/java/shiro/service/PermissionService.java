package shiro.service;

import shiro.entity.Permission;

public interface PermissionService {
    public Permission createPermission(Permission permission);

    public void deletePermission(Long permissionId);
}
