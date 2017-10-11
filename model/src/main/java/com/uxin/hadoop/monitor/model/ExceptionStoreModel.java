package com.uxin.hadoop.monitor.model;

import java.util.Date;

public class ExceptionStoreModel {

	public ExceptionStoreModel(String ip, String trackString) {
		this.Ip = ip;
		this.TrackString = trackString;
	}

	public ExceptionStoreModel() {
	}

	public String Ip;
	public String TrackString;
	public long CreateTime = new Date().getTime();
}
