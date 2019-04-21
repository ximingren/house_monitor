package shiro.service;

import shiro.dao.PermissionDao;
import shiro.dao.PermissionDaoImpl;
import shiro.entity.Permission;

public class PermissionServiceImpl implements PermissionService {
    private PermissionDao permissionDao = new PermissionDaoImpl();

    public Permission createPermission(Permission permission) {
        return permissionDao.createPermission(permission);
    }

    public void deletePermission(Long permissionId) {
        permissionDao.deletePermission(permissionId);
    }
}
