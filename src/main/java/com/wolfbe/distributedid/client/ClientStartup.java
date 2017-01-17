package com.wolfbe.distributedid.client;

import java.util.concurrent.TimeUnit;

/**
 * @author Andy
 */
public class ClientStartup {


    public static void main(String[] args) throws InterruptedException {
        SdkClient client = new SdkClient();
        client.init();
        client.start();
        for (int i = 0; i < 2000; i++) {
            client.invokeAsync(2000, null);
//            TimeUnit.SECONDS.sleep(2);
        }
    }
}
