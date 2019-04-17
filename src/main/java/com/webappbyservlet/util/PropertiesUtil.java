/**
 * created by Zheng Jiateng on 2019/4/11.
 */
package com.webappbyservlet.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;


/**
 * 读取配置文件工具类
 */
public final class PropertiesUtil {

    // Logger对象一般多定义为静态常量
    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    private static Properties props;

    static {
        String fileName = "datasource.properties";
        props = new Properties();
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName), "UTF-8");
            props.load(inputStreamReader);
        } catch (IOException e) {
            logger.error("配置文件读取异常", e);
        }
    }

    /**
     * 获取属性（默认值为null）
     * @param key
     * @return
     */
    // 因为是工具类 所以方法是静态的
    public static String getProperty(String key) {
        String value = props.getProperty(key.trim());
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return value.trim();
    }

    /**
     * 获取属性（可指定默认值）
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getProperty(String key, String defaultValue) {
        String value = props.getProperty(key.trim());
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        return value.trim();
    }
}
