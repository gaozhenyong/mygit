package com.uxin.hadoop.monitor.dao.entity;

import java.sql.Timestamp;

public class TaskModel {
	public int taskid;
	public String taskname;
	public int userid;
	public String username;
	public String filetype;
	public String filepath;
	public String taskparams;
	public String taskparams2;
	public int taskstatus;// 0未执行，1执行中，2删除，3异常
	public String applicationid;
	public int processid;
	public Timestamp createtime;
	public Timestamp updatetime;
	public Timestamp laststarttime;
}
