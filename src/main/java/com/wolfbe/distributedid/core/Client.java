package com.wolfbe.distributedid.core;

import io.netty.channel.Channel;

/**
 * @author Andy
 */
public interface Client {

    void start();

    void shutdown();

    void invokeSync(long timeoutMillis);

    void invokeAsync(long timeoutMillis,InvokeCallback invokeCallback);

    void invokeOneWay(long timeoutMillis);
}
