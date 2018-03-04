package com.example.notcharles.uptoblue;

public class Device {
    private String deviceName;
    private String address;
    private boolean isConnected;

    public String getDeviceName() {
        return deviceName;
    }

    public String getAddress() {
        return address;
    }

    public boolean getIsConnected() {
        return isConnected;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Device(String deviceName, String address, boolean isConnected) {
        this.deviceName = deviceName;
        this.address = address;
        this.isConnected = isConnected;
    }
}
