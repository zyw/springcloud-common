package cn.v5cn.others.gid;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * @author ZYW
 */
public class PasswordHelper {

    public PasswordHelper(){

    }

    public PasswordHelper(RandomNumberGenerator randomNumberGenerator, String algorithmName, int hashIterations){
        this.randomNumberGenerator = randomNumberGenerator;
        this.algorithmName = algorithmName;
        this.hashIterations = hashIterations;
    }

    private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

    private String algorithmName="md5";

    private int hashIterations=2;

    public void setRandomNumberGenerator(RandomNumberGenerator randomNumberGenerator) {
        this.randomNumberGenerator = randomNumberGenerator;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public void setHashIterations(int hashIterations) {
        this.hashIterations = hashIterations;
    }

    /**
     * @param original 明文密码
     * @param mix 用户名
     * @return 返回一个包含两个原始的元组，第一个是生成的盐，第二个是密码
     * */
    public TwoTuple<String,String> encrypt(String original, String mix){
        String salt = randomNumberGenerator.nextBytes().toHex();
        String pwd = new SimpleHash(algorithmName,
                original,
                ByteSource.Util.bytes(getCredentialsSalt(mix,salt)),
                hashIterations).toHex();
        return new TwoTuple<String,String>(salt,pwd);
    }

    /**
     * 根据已知的盐加密明文
     * @param original 明文密码
     * @param mix 用户名
     * @param salt 已知的盐
     * @return 返回密文密码
     */
    public String encrypt(String original,String mix,String salt){
        return new SimpleHash(algorithmName,
                original,
                ByteSource.Util.bytes(getCredentialsSalt(mix,salt)),
                hashIterations).toHex();
    }

    /**
     * 字符串混淆
     * */
    public static String getCredentialsSalt(String mix1,String mix2) {
        return mix1 + mix2;
    }

    public static PasswordHelperBuilder builder(){
        return new PasswordHelperBuilder();
    }

    public static class PasswordHelperBuilder {

        private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

        private String algorithmName="md5";

        private int hashIterations=2;

        public PasswordHelperBuilder algorithmName(String algorithmName){
            this.algorithmName = algorithmName;
            return this;
        }
        public PasswordHelperBuilder hashIterations(Integer hashIterations){
            this.hashIterations = hashIterations;
            return this;
        }
        public PasswordHelperBuilder randomNumberGenerator(RandomNumberGenerator randomNumberGenerator){
            this.randomNumberGenerator = randomNumberGenerator;
            return this;
        }
        public PasswordHelper build(){
            return new PasswordHelper(randomNumberGenerator,algorithmName,hashIterations);
        }
    }

    public static void main(String[] args) {
        TwoTuple<String, String> list = PasswordHelper.builder().build().encrypt("admin123#", "admin");
        System.out.println(list.a + "--" + list.b);
    }
}
