package com.wolfbe.distributedid.util;

/**
 * @author Andy
 */
public class GlobalConfig {

    public static String DEFAULT_HOST = "localhost";
    public static int DEFAULT_PORT = 16830;

    /**
     * HTTP协议和SDK协议服务器的端口
     */
    public static int HTTP_PORT = 16830;
    public static int SDKS_PORT = 16831;

    /**
     * HTTP协议和SDK协议的请求路径
     */
    public static String HTTP_REQUEST = "getid";
    public static String SDKS_REQUEST = "getid";

    /**
     * 数据中心的标识ID，取值范围：0~31
     * 机器或进程的标识ID，取值范围：0~31
     * 两个标识ID组合在分布式环境中必须唯一
     */
    public static long DATACENTER_ID = 1;
    public static long MACHINES_SIGN = 1;

    /**
     * 流量控制，表示每秒处理的并发数
     */
    public static int HANDLE_TPS = 10000;
    public static int ACQUIRE_TIMEOUTMILLIS = 1000;

    public static String getDefaultHost() {
        return DEFAULT_HOST;
    }

    public static void setDefaultHost(String defaultHost) {
        DEFAULT_HOST = defaultHost;
    }

    public static int getDefaultPort() {
        return DEFAULT_PORT;
    }

    public static void setDefaultPort(int defaultPort) {
        DEFAULT_PORT = defaultPort;
    }

    public static int getHttpPort() {
        return HTTP_PORT;
    }

    public static void setHttpPort(int httpPort) {
        HTTP_PORT = httpPort;
    }

    public static int getSdksPort() {
        return SDKS_PORT;
    }

    public static void setSdksPort(int sdksPort) {
        SDKS_PORT = sdksPort;
    }

    public static String getHttpRequest() {
        return HTTP_REQUEST;
    }

    public static void setHttpRequest(String httpRequest) {
        HTTP_REQUEST = httpRequest;
    }

    public static String getSdksRequest() {
        return SDKS_REQUEST;
    }

    public static void setSdksRequest(String sdksRequest) {
        SDKS_REQUEST = sdksRequest;
    }

    public static long getDatacenterId() {
        return DATACENTER_ID;
    }

    public static void setDatacenterId(long datacenterId) {
        DATACENTER_ID = datacenterId;
    }

    public static long getMachinesSign() {
        return MACHINES_SIGN;
    }

    public static void setMachinesSign(long machinesSign) {
        MACHINES_SIGN = machinesSign;
    }

    public static int getHandleTps() {
        return HANDLE_TPS;
    }

    public static void setHandleTps(int handleTps) {
        HANDLE_TPS = handleTps;
    }

    public static int getAcquireTimeoutmillis() {
        return ACQUIRE_TIMEOUTMILLIS;
    }

    public static void setAcquireTimeoutmillis(int acquireTimeoutmillis) {
        ACQUIRE_TIMEOUTMILLIS = acquireTimeoutmillis;
    }
}
