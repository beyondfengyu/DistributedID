package com.wolfbe.distributedid.client;

import java.util.concurrent.TimeUnit;

/**
 * @author laochunyu
 */
public class ClientStartup {


    public static void main(String[] args) throws InterruptedException {
        SdkClient client = new SdkClient();
        client.init();
        client.start();
        for (int i = 0; i < 2; i++) {
            client.invokeSync(2000);
            TimeUnit.SECONDS.sleep(2);
        }
    }
}
