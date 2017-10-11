package com.uxin.hadoop.monitor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uxin.hadoop.monitor.dao.entity.LogModel;
import com.uxin.hadoop.monitor.dao.interf.LogDao;
import com.uxin.hadoop.monitor.service.vo.ServiceReturnModel;

@Service
public class LogService {

	@Autowired
	LogDao dao;

	/**
	 * 插入记录
	 * 
	 * @param model
	 * @return
	 */
	public ServiceReturnModel Insert(LogModel model) {
		ServiceReturnModel returnModel = ServiceReturnModel
				.getDefaultFailModel();
		try {
			int newLogid = dao.insert(model);
			if (newLogid > 0) {// 插入成功
				returnModel = ServiceReturnModel.getSucessModel("成功插入一条记录。",
						newLogid);
			} else {
				returnModel = ServiceReturnModel.getFailModel("数据插入失败。");
			}
		} catch (Exception e) {
			returnModel = ServiceReturnModel.getExceptionModel(e);
		}
		return returnModel;
	}
}
