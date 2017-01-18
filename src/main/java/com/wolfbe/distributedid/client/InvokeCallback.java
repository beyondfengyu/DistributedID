package com.wolfbe.distributedid.client;

import io.netty.channel.ChannelFuture;

/**
 * @author Andy
 */
public interface InvokeCallback {

    void operationComplete(ChannelFuture channelFuture);
}
