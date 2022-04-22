package cn.v5cn.captcha.demo.vo;

/**
 * @author powertime
 */
public class VerificationResp {

    private VerificationRespRet ret;

    private String data;

    private String uuid;

    public VerificationRespRet getRet() {
        return ret;
    }

    public void setRet(VerificationRespRet ret) {
        this.ret = ret;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
