package cn.v5cn.cim.route.cache;

import cn.v5cn.cim.route.kit.ZKit;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 服务器节点缓存
 * @author crossoverJie
 */
@Component
public class ServerCache {

    private static Logger logger = LoggerFactory.getLogger(ServerCache.class) ;

    private LoadingCache<String, String> cache;

    private ZKit zKit;

    public ServerCache(
            LoadingCache<String, String> cache, ZKit zKit) {
        this.cache = cache;
        this.zKit = zKit;
    }

    public void addCache(String key) {
        cache.put(key, key);
    }

    /**
     * 更新所有缓存/先删除 再新增
     * @param currentChildren
     */
    public void updateCache(List<String> currentChildren) {
        cache.invalidateAll();
        for (String currentChild : currentChildren) {
            String key;
            if(currentChild.split("-").length == 2) {
                key = currentChild.split("-")[1];
            } else {
                key = currentChild;
            }
            addCache(key);
        }
    }

    /**
     * 获取所有的服务列表
     * @return
     */
    public List<String> getServerList() {
        List<String> list = new ArrayList<>();

        if(cache.size() == 0) {
            List<String> allNode = zKit.getAllNode();
            for(String node : allNode) {
                String key = node.split("-")[1];
                addCache(key);
            }
        }
        for (Map.Entry<String,String> entry : cache.asMap().entrySet()) {
            list.add(entry.getKey());
        }

        return list;
    }

    /**
     * rebuild cache list
     */
    public void rebuildCacheList() {
        updateCache(getServerList());
    }

}
