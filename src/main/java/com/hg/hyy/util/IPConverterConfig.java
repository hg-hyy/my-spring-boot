package com.hg.hyy.util;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPConverterConfig extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent iLoggingEvent) {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }
}

// public class LogIpConfig extends ClassicConverter {
//     private static final Logger logger = LoggerFactory.getLogger(LogIpConfig.class);
//     private static String webIP;
//     static {
//         try {
//             webIP = InetAddress.getLocalHost().getHostAddress();
//         } catch (UnknownHostException e) {
//             logger.error("获取日志Ip异常", e);
//             webIP = null;
//         }
//     }

//     @Override
//     public String convert(ILoggingEvent event) {
//         return webIP;
//     }
// }