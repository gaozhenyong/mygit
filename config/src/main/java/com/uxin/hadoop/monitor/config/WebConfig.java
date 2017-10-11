package com.uxin.hadoop.monitor.config;

import com.uxin.hadoop.monitor.utils.IoUtil;

public class WebConfig {
	public static String FileUpLoadRoot = null;
	public static String FileAcceptType = null;
	public static String SprakSubmitCommand = "spark-submit";
	public static String SparkSuHadoop = null;
	public static String DomainLoginApi = null;
	static {

		if (IoUtil.FileIsExsit(FilePathConfig.WebConfigPath)) {
			FileUpLoadRoot = IoUtil.getPropertiesSingleValue(
					FilePathConfig.WebConfigPath, "FileUpLoadRoot");
			FileAcceptType = IoUtil.getPropertiesSingleValue(
					FilePathConfig.WebConfigPath, "FileAcceptType");
			SprakSubmitCommand = IoUtil.getPropertiesSingleValue(
					FilePathConfig.WebConfigPath, "SprakSubmitCommand");
			SparkSuHadoop = IoUtil.getPropertiesSingleValue(
					FilePathConfig.WebConfigPath, "SparkSuHadoop");
			DomainLoginApi = IoUtil.getPropertiesSingleValue(
					FilePathConfig.WebConfigPath, "DomainLoginApi");
		}

	}

}
