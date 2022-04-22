package cn.v5cn.others.lb;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 负载均衡中，随机加权重算法,SOFARPC（com.alipay.sofa.rpc.client.lb.RandomLoadBalancer）中的随机加权重算法，结果
 * {1=3, 2=5, 4=11, 5=12, 6=10, 7=13, 8=27, 9=19}（调用一百次的输出） 不是很准确，按说9是权重最大的，应该得到最多的的调用机会。
 * 但是，从运行结果看，不是很准确。
 * @author ZYW
 * @version 1.0
 * @date 2020-03-06 16:16
 */
public class WeightRandomLandBalancer {

    private final Random random = new Random();


    public int randomTest() {
        int result = 0;
        int[] randomNum = new int[]{1,4,6,2,8,5,7,9};

        int len = randomNum.length; // 总个数

        int totalWeight = 0; // 总权重

        for (int i : randomNum) {
            totalWeight += i;
        }

        // 假设权重不相同且权重大于0则按总权重数随机
        int offset = random.nextInt(totalWeight);

        // 并确定随机值落在哪个片断上
        for (int i = 0; i < len; i++) {
            offset -= randomNum[i];
            if(offset < 0) {
                result = randomNum[i];
                break;
            }
        }
        return result;
    }


    public static void main(String[] args) {
        WeightRandomLandBalancer wrlb = new WeightRandomLandBalancer();
        //<Integer权重值,Integer获取的调用次数>
        Map<Integer,Integer> result = new HashMap<>();
        //调用一千次，看一下，不同权重的服务的调用次数
        for(int i=0; i< 1000;i++) {
            int test = wrlb.randomTest();
            if(!result.containsKey(test)) {
                result.put(test,1);
            } else {
                Integer integer = result.get(test);
                result.put(test,integer+1);
            }
        }

        System.out.println(result);
        // 输出：{1=25, 2=53, 4=88, 5=108, 6=136, 7=177, 8=211, 9=202}
    }
}
