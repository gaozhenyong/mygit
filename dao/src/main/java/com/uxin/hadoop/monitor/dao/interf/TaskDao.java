package com.uxin.hadoop.monitor.dao.interf;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.uxin.hadoop.monitor.dao.entity.TaskModel;

public interface TaskDao {

	public TaskModel selectByID(int taskid);

	public List<TaskModel> selectByStatus(int[] statuses);

	public List<TaskModel> selectByPage(Map<String, Object> map);

	public int insert(TaskModel model);

	public int update(TaskModel model);

	public int updateStatus(@Param("taskid") int taskid,
			@Param("taskstatus") int taskstatus);

	public int delete(int taskid);

	public int selectCountByWhere(Map<String, Object> map);

	public boolean exsitByTaskName(String taskName);

	public boolean exsitByTaskid(int taskid);

	public int updateProcessid(@Param("taskid") int taskid,
			@Param("processid") int processid);

	public int updateApplicationid(@Param("taskid") int taskid,
			@Param("applicationid") int applicationid);

	public int updateStatusV2(@Param("taskid") int taskid,
			@Param("taskstatus") int taskstatus);

}
