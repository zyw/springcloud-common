package cn.v5cn.cim.common.route.algorithm.consistenthash;

import cn.v5cn.cim.common.data.construct.SortArrayMap;

/**
 * 自定义排序 Map 实现
 * @author crossoverJie
 */
public class SortArrayMapConsistentHash extends AbstractConsistentHash {

    private SortArrayMap sortArrayMap = new SortArrayMap();

    /**
     * 虚拟节点数量
     */
    private static final int VIRTUAL_NODE_SIZE = 2;

    @Override
    protected void add(long key, String value) {
        sortArrayMap.clear();

        for(int i = 0; i < VIRTUAL_NODE_SIZE; i++) {
            long hash = super.hash("vir" + key + i);
            sortArrayMap.add(hash, value);
        }

        sortArrayMap.add(key, value);
    }

    @Override
    protected void sort() {
        sortArrayMap.sort();
    }

    @Override
    protected String getFirstNodeValue(String value) {
        long hash = super.hash(value);
        System.out.println("value=" + value + " hash = " + hash);
        return sortArrayMap.firstNodeValue(hash);
    }
}
