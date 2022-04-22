package cn.v5cn.drools.demo.entity;

public class Order {
    private int amout;// 原始的订单金额
    private int score;//积分

    public int getAmout() {
        return amout;
    }

    public void setAmout(int amout) {
        this.amout = amout;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Order{" +
                "amout=" + amout +
                ", score=" + score +
                '}';
    }
}
