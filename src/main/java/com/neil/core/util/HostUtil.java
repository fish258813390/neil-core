package com.neil.core.util;

import java.net.InetAddress;

public class HostUtil {
    public static String localAddress(){
        try {
            return InetAddress.getLocalHost().getHostAddress();
        }catch (Exception e){
            return "127.0.0.1";
        }
    }
}
