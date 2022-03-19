package csi2136.project.net.util;

import java.util.regex.Pattern;

public class NetAddress {

    private String ip;
    private int port;

    public NetAddress(String ip, int port) {
        this.ip = ip.trim();
        this.port = port;
    }

    public static NetAddress fromString(String address) {
        String[] data = address.split(Pattern.quote(":"));
        if(data.length != 2)return null;

        try {return new NetAddress(data[0], Integer.parseInt(data[1].trim()));}
        catch(Exception e) {return null;}
    }


    public String getIp() {
        return this.ip;
    }

    public int getPort() {
        return this.port;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof NetAddress) {
            NetAddress netAddress = (NetAddress)obj;
            if(this.getPort() != netAddress.getPort())return false;
            if(!this.getIp().equals(netAddress.getIp()))return false;
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return this.ip + ":" + port;
    }

}
