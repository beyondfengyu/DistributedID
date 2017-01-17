package com.wolfbe.distributedid.sdks;

/**
 * @author Andy
 */
public class SdkProto {

    private int rqid; //请求的ID
    private long did; //全局的ID

    public SdkProto(int rqid, long did) {
        this.rqid = rqid;
        this.did = did;
    }

    public int getRqid() {
        return rqid;
    }

    public void setRqid(int rqid) {
        this.rqid = rqid;
    }

    public long getDid() {
        return did;
    }

    public void setDid(long did) {
        this.did = did;
    }

    @Override
    public String toString() {
        return "SdkProto{" +
                "rqid=" + rqid +
                ", did=" + did +
                '}';
    }
}
