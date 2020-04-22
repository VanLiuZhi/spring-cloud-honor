package com.vanliuzhi.org.common.module.util;

import org.joda.time.DateTime;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机数生成util
 **/
public class RandomUtil {

    private static final ThreadLocalRandom random = ThreadLocalRandom.current();

    /**
     * 生成订单编号-方式一
     */
    public static String generateOrderCode() {
        // TODO:时间戳+N为随机数流水号

        return DateTime.now().toString("yyyyMMddHHmmssSS") + generateNumber(4);
    }

    // num为随机数流水号
    public static String generateNumber(final int num) {
        StringBuffer sb = new StringBuffer();
        for (int i = 1; i <= num; i++) {
            sb.append(random.nextInt(9));
        }
        return sb.toString();

    }

}
