package com.mmall.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ymj
 */
public class PropertiesUtil {
	
	private static Logger logger = LoggerFactory.getLogger(Properties.class);
	
	private static Properties props;

	static {
		String fileName = "mmall.properties";
		props = new Properties();
		
			try {
				props.load(new InputStreamReader(PropertiesUtil.class.getResourceAsStream(fileName), "UTF-8"));
			}catch (IOException e) {
				logger.error("配置文件读取异常");
			}
	}
	
	public static String getProperties(String key){
		String value = props.getProperty(key.trim());
		if(StringUtils.isBlank(value)){
			return null;
		}
		return value.trim();
	}
	
	public static String getProperties(String key,String defaultValue){
		String value = props.getProperty(key);
		if(StringUtils.isBlank(value)){
			value = defaultValue;
		}
		return value.trim();
	}
}






