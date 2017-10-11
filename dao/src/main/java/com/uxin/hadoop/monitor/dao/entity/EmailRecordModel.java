package com.uxin.hadoop.monitor.dao.entity;

import java.sql.Timestamp;

public class EmailRecordModel {
	public int id;
	public int taskid;
	public String toemail;
	public String emailbody;
	public Timestamp createtime;
}
