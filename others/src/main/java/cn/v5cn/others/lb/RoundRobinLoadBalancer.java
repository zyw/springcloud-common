package cn.v5cn.others.lb;

/**
 * 轮训使用一个取模运算搞定。
 * @author ZYW
 * @version 1.0
 * @date 2020-03-07 13:37
 */
public class RoundRobinLoadBalancer {
    //服务个数
    public static final int LEN = 5;

    public static void main(String[] args) {
        //i为调用次数
        for(int i = 0; i<1000; i++) {
            //在现实项目中一般使用 AtomicInteger来递增获得i，然后取模与服务个数。
            int i1 = i % LEN; //获取本次使用服务的下标
            System.out.println("轮训：" + i1);
        }
    }
}
