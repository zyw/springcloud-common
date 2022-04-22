package cn.v5cn.netty.im.client;

import cn.v5cn.netty.im.client.context.RelationCache;
import cn.v5cn.netty.im.client.context.impl.MemoryRelationCache;
import cn.v5cn.netty.im.client.service.ClientRestService;
import com.google.inject.AbstractModule;

public class ClientModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(RelationCache.class).to(MemoryRelationCache.class);
        bind(ClientRestService.class).toProvider(ClientRestServiceProvider.class);
    }
}
