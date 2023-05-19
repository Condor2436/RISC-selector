package dku.edu.zj82;

public class TransInfo {
    private String type;
    private String info;
    private String[] muliInfo;

    public TransInfo() {
    }

    public TransInfo(String type, String info) {
        this.type = type;
        this.info = info;
    }

    public TransInfo(String type, String info, String[] muliInfo) {
        this.type = type;
        this.info = info;
        this.muliInfo = muliInfo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String[] getMuliInfo() {
        return muliInfo;
    }

    public void setMuliInfo(String[] muliInfo) {
        this.muliInfo = muliInfo;
    }
}
