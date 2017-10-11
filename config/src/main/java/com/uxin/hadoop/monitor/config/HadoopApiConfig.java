package com.uxin.hadoop.monitor.config;

import com.uxin.hadoop.monitor.utils.IoUtil;

public final class HadoopApiConfig {
	public static String applications_base_url = null;
	/**
	 * 初始化配置项
	 */
	static {
		applications_base_url = IoUtil.getPropertiesSingleValue(
				FilePathConfig.HadoopApiUrlPath, "applications_base_url");
	}
}
