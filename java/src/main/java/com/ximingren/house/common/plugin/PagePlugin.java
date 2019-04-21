package com.ximingren.house.common.plugin;

import com.ximingren.house.common.dto.PageInfoDto;
import com.ximingren.house.common.enums.MessageEnum;
import com.ximingren.house.common.exception.ExternalServiceException;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;


/**
 * 用JDBC对数据库进行操作就必须要有一个对应的Statement对象
 * Mybatis在执行Sql语句前就会产生一个包含Sql语句的Statement对象
 * 而且对应的Sql语句是在Statement之前产生的，所以可以在它生成Statement之前对用来生成Statement的Sql语句下手
 * 在Mybatis中Statement语句是通过RoutingStatementHandler对象的prepare方法生成的
 * 所以利用拦截器方法把Mybatis分页的一个思路就是拦截StatementHandler接口的prepare方法
 * 然后在拦截器方法中把Sql语句改成对应的分页查询Sql语句，之后再调用StatementHandler对象中的prepare方法
 */
@Intercepts({@Signature(method = "prepare", type = StatementHandler.class, args = {Connection.class})})
public class PagePlugin implements Interceptor {
    private static String DEFAULT_DIALECT = "mysql";
    private static String DEFAULT_PAGESQLID_SUFFIX = "WithPage";
    private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();

    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
//        下面这个，什么通过MetaObject包装一个对象后可以获取或者设置该对象的原本不可访问的属性
        MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
        while (metaStatementHandler.hasGetter("h")) {
            Object object = metaStatementHandler.getValue("h");
            metaStatementHandler = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
        }
        while (metaStatementHandler.hasGetter("target")) {
            Object object = metaStatementHandler.getValue("target");
            metaStatementHandler = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
        }
//        这个是什么configuration？
        Configuration configuration = (Configuration) metaStatementHandler.getValue("delegate.configuration");
//        下面就是从configuration中获得dialet和后缀，默认为mysql和WithPage
        String dialect = null;
        String pageSqlIdSuffix = null;
        if (null == configuration.getVariables()) {
            dialect = DEFAULT_DIALECT;
            pageSqlIdSuffix = DEFAULT_PAGESQLID_SUFFIX;
        } else {
            dialect = configuration.getVariables().getProperty("dialect");
            if (null == dialect || "".equals(dialect)) {
                dialect = DEFAULT_DIALECT;
            }
            pageSqlIdSuffix = configuration.getVariables().getProperty("pageSqlId");
            if (null == pageSqlIdSuffix || "".equals(pageSqlIdSuffix)) {
                pageSqlIdSuffix = DEFAULT_PAGESQLID_SUFFIX;
            }
        }
//        下面这个是获取相应的mapped 语句
        MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
        if (mappedStatement.getId().endsWith(pageSqlIdSuffix)) {
            BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
            Object parameterObject = boundSql.getParameterObject();
            if (parameterObject == null) {
                throw new NullPointerException(("parameterObject is null!"));
            } else {
                PageInfoDto page = (PageInfoDto) parameterObject;
                String sql = boundSql.getSql();
//                重写Sql
                String pageSql = buildPageSql(sql, page, dialect);
                metaStatementHandler.setValue("delegate.boundSql.sql", pageSql);
                Connection connection = (Connection) invocation.getArgs()[0];
//                重设分页参数里的总页数等
                setPageParameter(sql, connection, mappedStatement, boundSql, page);
            }
        }
        return invocation.proceed();
    }


    //    这个方法是干嘛的？
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {

    }

    private String buildPageSql(String sql, PageInfoDto page, String dialect) {
        if (page != null) {
            StringBuilder pageSql = new StringBuilder();
            if ("mysql".equals(dialect)) {
                pageSql = buildPageSqlForMysql(sql, page);
                sql = pageSql.toString();
            } else if ("oracle".equals(dialect)) {
                pageSql = buildPageSqlForOracle(sql, page);
                sql = pageSql.toString();
            }
        }
        return sql;
    }

    public StringBuilder buildPageSqlForMysql(String sql, PageInfoDto page) {
        StringBuilder pageSql = new StringBuilder(100);
        pageSql.append(sql);
//        分页的本质就是在这里
        pageSql.append("limit " + ((page.getPageNo() - 1) * page.getPageSize()) + "," + page.getPageSize());
        return pageSql;
    }

    public StringBuilder buildPageSqlForOracle(String sql, PageInfoDto page) {
        StringBuilder pageSql = new StringBuilder(100);
        int beginrow = page.getPageNo() - 1;
        int endrow = page.getPageNo() - 1 + page.getPageSize();
        pageSql.append("select * from (select temp.*, rownum row_id from (");
        pageSql.append(sql);
        pageSql.append(") temp where rownum<=").append(String.valueOf(endrow));
        pageSql.append(") where row_id >").append(String.valueOf(beginrow));
        return pageSql;
    }

    public void setPageParameter(String sql, Connection connection, MappedStatement mappedStatement, BoundSql boundSql, PageInfoDto page) {
//        记录总记录数？这个咋记录的？
        String countSql = "select count(0) from (" + sql + ") as total";
        PreparedStatement countStmt = null;
        ResultSet rs = null;
        try {
            countStmt = connection.prepareStatement(countSql);
            BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql, boundSql.getParameterMappings(), boundSql.getParameterObject());
            setParameters(countStmt, mappedStatement, countBS, boundSql.getParameterMappings());
            rs = countStmt.executeQuery();
            int totalCount = 0;
            if (rs.next()) {
                totalCount = rs.getInt(1);
            }
//            设置总记录数？
            page.setTotalSize(totalCount);
        } catch (SQLException e) {
            throw new ExternalServiceException(MessageEnum.DATABASE_PAGE_ERROR);
        } finally {
            try {
                rs.close();
                countStmt.close();
            } catch (SQLException e) {
                throw new ExternalServiceException(MessageEnum.DATABASE_PAGE_ERROR);
            }
        }
    }

    private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql, Object parameterObject) throws SQLException {
        ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
        parameterHandler.setParameters(ps);
    }
}
