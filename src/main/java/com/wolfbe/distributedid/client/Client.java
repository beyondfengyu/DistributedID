package com.wolfbe.distributedid.client;

import com.wolfbe.distributedid.exception.RemotingConnectException;
import com.wolfbe.distributedid.exception.RemotingTimeoutException;
import com.wolfbe.distributedid.exception.RemotingTooMuchRequestException;
import com.wolfbe.distributedid.sdks.SdkProto;


/**
 * @author Andy
 */
public interface Client {

    void start();

    void shutdown();

    SdkProto invokeSync(SdkProto proto,long timeoutMillis) throws RemotingConnectException,RemotingTimeoutException;

    void invokeAsync(SdkProto proto,long timeoutMillis, InvokeCallback invokeCallback) throws RemotingConnectException,
            RemotingTooMuchRequestException,RemotingTimeoutException;

    void invokeOneWay(SdkProto proto,long timeoutMillis) throws RemotingConnectException,RemotingTooMuchRequestException,
            RemotingTimeoutException;
}
