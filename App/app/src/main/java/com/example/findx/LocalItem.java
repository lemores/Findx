package com.example.findx;

public class LocalItem {
    private String localName;
    private int localImage;
    private String localFun;

    public LocalItem(String localName, int localImage, String localFun) {
        this.localName = localName;
        this.localImage = localImage;
        this.localFun = localFun;
    }

    public String getLocalName() {
        return localName;
    }
    public int getImageLocal() {
        return localImage;
    }
    public String getLocalFun() {
        return localFun;
    }
}
