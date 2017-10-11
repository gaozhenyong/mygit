package com.uxin.hadoop.monitor.dao.interf;

import java.util.List;

import com.uxin.hadoop.monitor.dao.entity.EmailRecordModel;

public interface EmailRecordDao {

	public int insert(EmailRecordModel model);

	public List<EmailRecordModel> selectByTaskid(int taskid);

	public long selectMaxCreatetimeByTaskid(int taskid);
}
