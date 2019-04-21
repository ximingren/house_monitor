package shiro;

import junit.framework.AssertionFailedError;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.*;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.Factory;
import org.apache.shiro.util.SimpleByteSource;
import org.junit.Assert;
import org.junit.Test;

public class ShiroTest {
    @Test
    public void testLogin() {
//        根据配置信息创建一个Factory
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
//        从工厂类中获取一个securityManger实例
        SecurityManager securityManager = factory.getInstance();
//        绑定给securityUtil
        SecurityUtils.setSecurityManager(securityManager);
//        获得subject
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("zhang", "123");
        try {
            subject.login(token);
            System.out.println("登录成功");
        } catch (AuthenticationException e) {
            System.out.println(e);
        }
        Assert.assertTrue(subject.isAuthenticated());
        subject.logout();
    }

    @Test
    public void testRealmLogin() {
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-realm.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("zhang", "123");
        subject.login(token);
        Assert.assertTrue(subject.isAuthenticated());
    }

    @Test
    public void testJDBCRealm() {
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-jdbc-realm.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("zhang", "1234");

        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            System.out.println(e);
        }
        Assert.assertTrue(subject.isAuthenticated());
        subject.logout();
    }

    @Test
    public void testHasRole() {
        login("classpath:shiro.ini", "zhang", "123");
        Subject subject = SecurityUtils.getSubject();
        Assert.assertTrue(subject.hasRole("role1"));
    }

    /*
    首先调用subject.ispermitted,会委托给securityManager,而securityManager会委托给anthorizer
    authorizer是真正的授权者，如果我们调用ispermitted,其首先会通过permissionResolver把字符串转换成相应的permission实例
    在进行授权以前，其会调用相应的realm获取subject相应的角色/权限用于匹配传入的角色/权限
     */
    @Test
    public void testHasPermission() {
        login("classpath:shiro-permission.ini", "zhang", "123");
        Subject subject = SecurityUtils.getSubject();
        Assert.assertTrue(subject.isPermitted("user:create"));
        subject.checkPermission("user:update");
    }

    @Test
    public void testIsPermitted() {
        login("classpath:shiro-authorizer.ini", "zhang", "123");
        Assert.assertTrue(SecurityUtils.getSubject().isPermitted("user1:update"));
    }

    @Test
    public void testEncode() {
//        Base64编码/解码
        String str = "hello";
        String base64Encoded = Base64.encodeToString(str.getBytes());
        String str1 = Base64.decodeToString(base64Encoded);
        Assert.assertEquals(str, str1);
//        16进制字符串编码/解码
        String base64Encoded1 = Hex.encodeToString(str.getBytes());
        String str2 = new String(Hex.decode(base64Encoded1));
        Assert.assertEquals(str, str2);
//        散列算法一般用于生成数据的摘要信息，是一种不可逆的算法
//        常见的有MD5,SHA
        String salt = "123";
        String md5 = new Md5Hash(str, salt).toString();
        String sha1 = new Sha256Hash(str, salt).toString();
        String simpleHash = new SimpleHash("SHA-1", str, salt).toString();
    }

    @Test
    public void testHashService() {
        DefaultHashService hashService = new DefaultHashService();
        hashService.setHashAlgorithmName("SHA-512");
        hashService.setPrivateSalt(new SimpleByteSource("123"));
        hashService.setGeneratePublicSalt(true);
        hashService.setRandomNumberGenerator(new SecureRandomNumberGenerator());
        hashService.setHashIterations(1);
        HashRequest request = new HashRequest.Builder().setAlgorithmName("MD5").setSource(ByteSource.Util.bytes("hello")).setSalt(ByteSource.Util.bytes("123")).setIterations(2).build();
        String hex = hashService.computeHash(request).toString();
        System.out.println(hex);
    }

    @Test
    public void testPasswordServiceWithMyRealm() {
        login("classpath:shiro-passwordservice.ini","wu","123");
        String salt2 = new SecureRandomNumberGenerator().nextBytes().toHex();
        System.out.println(salt2);
    }
    public void login(String configurePath, String username, String password) {
        Factory<SecurityManager> factory = new IniSecurityManagerFactory(configurePath);
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        subject.login(token);
    }
}
