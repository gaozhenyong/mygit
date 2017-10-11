package com.uxin.hadoop.monitor.handles.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uxin.hadoop.monitor.core.UserCore;
import com.uxin.hadoop.monitor.service.TaskService;
import com.uxin.hadoop.monitor.service.vo.ServiceReturnModel;

/**
 * 
 * @author gaozhenyong
 * 
 */
@Controller
@RequestMapping("/task")
public class TaskController {

	/***
	 * 
	 */
	@Autowired
	TaskService taskService;

	/**
	 * 编辑、添加
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ResponseBody
	public ServiceReturnModel edit(HttpServletRequest request) {
		return taskService.edit(request);
	}

	/**
	 * 获取列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public String List(HttpServletRequest request) {
		return taskService.GetList(request);
	}

	/**
	 * 删除
	 * 
	 * @param taskid
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public ServiceReturnModel delete(final int taskid,
			HttpServletRequest request) {
		return taskService.delete(taskid, UserCore.getUserAccount(request));
	}

	/**
	 * 获取单条数据
	 * 
	 * @param taskid
	 * @return
	 */
	@RequestMapping("/model/{taskid}")
	@ResponseBody
	public ServiceReturnModel model(@PathVariable("taskid") int taskid) {
		return taskService.selectByTaskid(taskid);
	}

	/**
	 * 执行任务
	 * 
	 * @param taskid
	 * @param request
	 * @return
	 */
	@RequestMapping("/process/{taskid}")
	@ResponseBody
	public ServiceReturnModel process(@PathVariable("taskid") final int taskid,
			HttpServletRequest request) {
		String account = UserCore.getUserAccount(request);
		return taskService.ProcessTask(taskid, account, false);
	}

	/**
	 * 获取任务执行过程
	 * 
	 * @param taskid
	 * @return
	 */
	@RequestMapping("/processinfo/{taskid}")
	@ResponseBody
	public ServiceReturnModel processinfo(@PathVariable("taskid") int taskid) {
		return taskService.GetProcessInfo(taskid);
	}

	/**
	 * 杀死任务
	 * 
	 * @param taskid
	 * @param request
	 * @return
	 */
	@RequestMapping("/processkill/{taskid}")
	@ResponseBody
	public ServiceReturnModel processkill(
			@PathVariable("taskid") final int taskid, HttpServletRequest request) {
		return taskService.Kill(taskid, UserCore.getUserAccount(request));
	}
}
