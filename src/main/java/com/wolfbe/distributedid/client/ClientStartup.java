package com.wolfbe.distributedid.client;

import com.wolfbe.distributedid.exception.RemotingConnectException;
import com.wolfbe.distributedid.exception.RemotingTimeoutException;
import com.wolfbe.distributedid.exception.RemotingTooMuchRequestException;

import java.util.concurrent.TimeUnit;

/**
 * @author Andy
 */
public class ClientStartup {


    public static void main(String[] args) throws InterruptedException, RemotingTimeoutException, RemotingConnectException, RemotingTooMuchRequestException {
        SdkClient client = new SdkClient();
        client.init();
        client.start();
        for (int i = 0; i < 2000; i++) {

            client.invokeAsync(null,2000, null);
//            TimeUnit.SECONDS.sleep(2);
        }
    }
}
