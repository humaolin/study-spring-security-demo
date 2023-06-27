package com.pearl.log.demo.util;

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
 *
 * @author ruoyi
 */
public class AddressUtils {

    public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp";

    public static final String UNKNOWN = "未知";

    public static String getRealAddressByIP(String ip) {
        try {
            String rspStr = HttpUtil.get(IP_URL + "?ip=" + ip + "&json=true", 1000);
            if (StrUtil.isEmpty(rspStr)) {
                return UNKNOWN;
            }
            JSONObject obj = JSONUtil.parseObj(rspStr);
            String addr = obj.getStr("addr");
            return StrUtil.isNotBlank(addr) ? addr : UNKNOWN;
        } catch (Exception e) {
            return UNKNOWN;
        }
    }
}

