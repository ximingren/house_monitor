package shiro.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import shiro.entity.Permission;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PermissionDaoImpl implements  PermissionDao {
    private JdbcTemplate jdbcTemplate =JdbcTemplateUtil.jdbcTemplate();

    public Permission createPermission(final Permission permission) {
        final String sql = "INSERT INTO sys_permissions(permission,description,available) VALUES(?,?,?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});
                preparedStatement.setString(1, permission.getPermission());
                preparedStatement.setString(2, permission.getDescription());
                preparedStatement.setBoolean(3, permission.getAvailable());
                return preparedStatement;
            }
        }, keyHolder);
        permission.setId(keyHolder.getKey().longValue());
        return permission;
    }

    public void deletePermission(Long permissionId) {
        String sql = "DELETE FROM sys_roles_permissions WHERE permission_id=?";
        jdbcTemplate.update(sql, permissionId);
        sql="DELETE FROM sys_permissions WHERE id=?";
        jdbcTemplate.update(sql, permissionId);
    }
}
