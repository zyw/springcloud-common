package cn.v5cn.simple.rpc.demo;

/**
 * 指数RPC的输入
 * @author ZYW
 * @version 1.0
 * @date 2020-02-25 15:15
 */
public class ExpRequest {
    private int base;
    private int exp;

    public ExpRequest() {
    }

    public ExpRequest(int base, int exp) {
        this.base = base;
        this.exp = exp;
    }

    public int getBase() {
        return base;
    }

    public void setBase(int base) {
        this.base = base;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }
}
