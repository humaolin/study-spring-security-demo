package com.pearl.security.auth.util;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/6/26
 */

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取地址类
 * 免费可用的IP归属地接口
 * 淘宝：http://ip.taobao.com/service/getIpInfo.php?ip=
 * pconline：http://whois.pconline.com.cn/ip.jsp?ip=
 * 126：http://ip.ws.126.net/ipquery?ip=
 * 百度：http://api.map.baidu.com/location/ip?ak=F454f8a5efe5e577997931cc01de3974&ip=
 * @author ruoyi
 */
public class AddressUtils {
    private static final Logger log = LoggerFactory.getLogger(AddressUtils.class);

    // IP地址查询
    public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp";

    // 未知地址
    public static final String UNKNOWN = "XX XX";

    public static String getRealAddressByIP(String ip) {
        // 内网不查询
        if (IpUtils.internalIp(ip)) {
            return "内网IP";
        }
        //ture：获取ip地址开关
        if (true) {
            try {

                String rspStr = HttpUtil.get(IP_URL + "ip=" + ip + "&json=true", 1000);
                if (StrUtil.isEmpty(rspStr)) {
                    log.error("获取地理位置异常 {}", ip);
                    return UNKNOWN;
                }
                JSONObject obj = JSONUtil.parseObj(rspStr);
                String region = obj.getStr("pro");
                String city = obj.getStr("city");
                return String.format("%s %s", region, city);
            } catch (Exception e) {
                log.error("获取地理位置异常 {}", ip);
            }
        }
        return UNKNOWN;
    }
}

