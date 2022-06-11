/*
 * Conditions Of Use
 *
 * This software was developed by employees of the Sigmatrix(Beijing) Corporation.
 * This software is provided by sigmatrix as a service and is expressly
 * provided "AS IS."  Sigmatrix MAKES NO WARRANTY OF ANY KIND, EXPRESS, IMPLIED
 * OR STATUTORY, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTY OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, NON-INFRINGEMENT
 * AND DATA ACCURACY.  Sigmatrix does not warrant or make any representations
 * regarding the use of the software or the results thereof, including but
 * not limited to the correctness, accuracy, reliability or usefulness of
 * the software.
 *
 * Permission to use this software is contingent upon your acceptance
 * of the terms of this agreement.
 *
 */

package cn.poverty.common.config.log;

import ch.qos.logback.core.PropertyDefinerBase;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;

/**
 
 * @projectName poverty-help-api
 * @Description: 自定义的日志上面的HOST名称
 * @date 2019-08-20
 */
public class LogbackCustomHostName extends PropertyDefinerBase {

    /**
     * LOGGER
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LogbackCustomHostName.class);
    /**
     * HOST_NAME_LENGTH
     */
    private static final int HOST_NAME_LENGTH = 15;

    /**
     * (non-Javadoc)
     * @Title: getPropertyValue
     * @Description:
     * @see ch.qos.logback.core.spi.PropertyDefiner#getPropertyValue()
     */
    @Override
    public String getPropertyValue() {

        try {

            final InetAddress netAddress = getInetAddress();
            // 获取主机名 linux多网卡无法根据环境指定具体网卡，此方法只能在windows下使用
            String hostName = getHostName(netAddress);
            if (StringUtils.isNotBlank(hostName) && hostName.length() >= HOST_NAME_LENGTH) {
                hostName = hostName.substring(hostName.length() - HOST_NAME_LENGTH);
            }
            return hostName;
        } catch (Exception e) {
            return "unknown_exception";
        }
    }

    /**
     * @return InetAddress
     */
    public static InetAddress getInetAddress() {

        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            LOGGER.error("getInetAddress is Error:{}", e);
        }
        return null;
    }

    /**
     * @param netAddress netAddress
     * @return String
     */
    public static String getHostName(InetAddress netAddress) {
        if (null == netAddress) {
            return null;
        }
        return netAddress.getHostName();
    }

}
