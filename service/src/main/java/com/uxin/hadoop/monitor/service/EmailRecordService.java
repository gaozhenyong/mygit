package com.uxin.hadoop.monitor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uxin.hadoop.monitor.dao.entity.EmailRecordModel;
import com.uxin.hadoop.monitor.dao.interf.EmailRecordDao;
import com.uxin.hadoop.monitor.service.vo.ServiceReturnModel;

@Service
public class EmailRecordService {
	@Autowired
	EmailRecordDao dao;

	/**
	 * 插入记录
	 * 
	 * @param model
	 * @return
	 */
	public ServiceReturnModel Insert(EmailRecordModel model) {
		ServiceReturnModel returnModel = ServiceReturnModel
				.getDefaultFailModel();
		try {
			int newEmailRecordid = dao.insert(model);
			if (newEmailRecordid > 0) {// 插入成功
				returnModel = ServiceReturnModel.getSucessModel("成功插入一条记录。");
			} else {
				returnModel = ServiceReturnModel.getFailModel("数据插入失败。");
			}
		} catch (Exception e) {
			returnModel = ServiceReturnModel.getExceptionModel(e);
		}
		return returnModel;
	}

	/**
	 * 
	 * @param taskid
	 * @return
	 */
	public List<EmailRecordModel> selectByTaskid(int taskid) {
		return dao.selectByTaskid(taskid);
	}

	/**
	 */
	public long selectMaxCreatetimeByTaskid(int taskid) {
		return dao.selectMaxCreatetimeByTaskid(taskid);
	}

	// private EmailRecordServiceModel ConvertToEmailRecordServiceModel(
	// EmailRecordModel emailModel) {
	// EmailRecordServiceModel emailServiceModel = new
	// EmailRecordServiceModel();
	// ReflectUtil.copyProperties(emailModel, emailServiceModel);
	// return emailServiceModel;
	// }
}
