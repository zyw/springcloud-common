package cn.v5cn.netty.im.rest.web.task;

import cn.v5cn.netty.im.common.domain.po.Offline;
import cn.v5cn.netty.im.rest.web.service.OfflineService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CleanOfflineMsgTask {
    private static Logger logger = LoggerFactory.getLogger(CleanOfflineMsgTask.class);

    private OfflineService offlineService;

    public CleanOfflineMsgTask(OfflineService offlineService) {
        this.offlineService = offlineService;
    }

    @Scheduled(cron = "0 0/5 * * * *")
    public void cleanReadMsg() {
        List<Long> readIds = offlineService.list(new LambdaQueryWrapper<Offline>()
        .select(Offline::getId)
        .eq(Offline::getHasRead, true))
        .stream()
        .map(Offline::getId)
        .collect(Collectors.toList());

        logger.info("[clean task] clean read offline msg, size: {}", readIds.size());

        Lists.partition(readIds, 1000).forEach(offlineService::removeByIds);
    }
}
