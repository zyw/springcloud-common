package cn.v5cn.others.hutool;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.http.HttpUtil;

import java.time.Instant;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-03-17 15:30
 */
public class Demo {
    public static void main(String[] args) {
        String grantType = "client_credential";
        String appKey = "dbcp_sdxw";
        String appSecret = "qweasfdfa";

        Digester md5 = new Digester(DigestAlgorithm.MD5);

        String dataSign = Base64.encode(md5.digestHex(grantType + appKey + appSecret + (System.currentTimeMillis()/1000/60)));

        System.out.println("dataSign: " + dataSign);

        String result1= HttpUtil.get("https://stu.jddongxi.com/rainamn-exam-api/getToken?grant_type=client_credential&app_key=dbcp_sdxw&app_security=qweasfdfa&dataSign=" + dataSign);

        System.out.println("result: " + result1);
    }
}
