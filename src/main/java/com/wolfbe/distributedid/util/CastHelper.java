package com.wolfbe.distributedid.util;

/**
 * @author Andy
 */
public class CastHelper {
    private volatile long start;
    private volatile long end;

    private CastHelper(){

    }

    public static CastHelper castListener(){
        CastHelper helper = new CastHelper();
        helper.start = System.currentTimeMillis();
        return helper;
    }

    public long cast(){
        end = System.currentTimeMillis();
        return end - start;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }
}
