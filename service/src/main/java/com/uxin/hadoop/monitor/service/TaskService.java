package com.uxin.hadoop.monitor.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.uxin.hadoop.monitor.config.AppOfRealTimeConfig;
import com.uxin.hadoop.monitor.config.HadoopHelperConfig;
import com.uxin.hadoop.monitor.config.RedisDataKeyConfig;
import com.uxin.hadoop.monitor.config.WebConfig;
import com.uxin.hadoop.monitor.core.RedisCore;
import com.uxin.hadoop.monitor.core.UserCore;
import com.uxin.hadoop.monitor.dao.entity.LogModel;
import com.uxin.hadoop.monitor.dao.entity.TaskModel;
import com.uxin.hadoop.monitor.dao.interf.LogDao;
import com.uxin.hadoop.monitor.dao.interf.TaskDao;
import com.uxin.hadoop.monitor.hadoopservice.core.applicationCore;
import com.uxin.hadoop.monitor.hadoopservice.model.applicationModel;
import com.uxin.hadoop.monitor.hadoopservice.model.applicationUrlParamsModel;
import com.uxin.hadoop.monitor.service.vo.ServiceReturnModel;
import com.uxin.hadoop.monitor.service.vo.TaskJsonModel;
import com.uxin.hadoop.monitor.utils.FileUploadUtil;
import com.uxin.hadoop.monitor.utils.FileUploadUtil.UploadResult;
import com.uxin.hadoop.monitor.utils.IoUtil;
import com.uxin.hadoop.monitor.utils.Log4jUtil;
import com.uxin.hadoop.monitor.utils.OsUtils;
import com.uxin.hadoop.monitor.utils.ReflectUtil;
import com.uxin.hadoop.monitor.utils.RegexUtil;
import com.uxin.hadoop.monitor.utils.StringUtil;

/**
 * 协调领域层
 * 
 * @author gaozhenyong
 * 
 */
@Service
public class TaskService {

	@Autowired
	TaskDao taskDao;
	@Autowired
	LogDao logDao;

	/**
	 * 插入记录
	 * 
	 * @param model
	 * @return
	 */
	public ServiceReturnModel Insert(TaskModel model) {
		ServiceReturnModel returnModel = ServiceReturnModel
				.getDefaultFailModel();
		boolean isExsit = taskDao.exsitByTaskName(model.taskname);// 判断任务名称是否存在
		if (isExsit) {
			returnModel = ServiceReturnModel.getFailModel("任务名称已存在。");
		} else {
			try {
				taskDao.insert(model);
				int newTaskid = model.taskid;
				if (newTaskid > 0) {// 插入成功
					returnModel = ServiceReturnModel.getSucessModel(
							"成功插入一条记录。", newTaskid);
				} else {
					returnModel = ServiceReturnModel.getFailModel("数据插入失败。");
				}
			} catch (Exception e) {
				returnModel = ServiceReturnModel.getExceptionModel(e);
			}
		}
		return returnModel;
	}

	/**
	 * 删除记录，真删除。
	 * 
	 * @param taskid
	 * @return
	 */
	public ServiceReturnModel delete(int taskid, String account) {
		ServiceReturnModel returnModel = ServiceReturnModel
				.getDefaultFailModel();
		try {
			int count = taskDao.delete(taskid);
			if (count > 0) {
				TaskModel taskModel = taskDao.selectByID(taskid);
				IoUtil.FileDelete(taskModel.filepath);// 删除文件
				LogModel logmodel = new LogModel();
				logmodel.createtime = new Timestamp(System.currentTimeMillis());
				logmodel.optype = TaskOpEnum.删除.name();
				logmodel.taskid = taskid;
				logmodel.userid = account;
				logDao.insert(logmodel);
				returnModel = ServiceReturnModel.getSucessModel("删除成功。");
			} else {
				returnModel = ServiceReturnModel.getFailModel("删除失败。");
			}

		} catch (Exception e) {
			returnModel = ServiceReturnModel.getExceptionModel(e);
			Log4jUtil.error(e);
		}
		return returnModel;
	}

	/***
	 * 
	 * @param model
	 * @return
	 */
	public ServiceReturnModel update(TaskModel model, String account) {
		int count = taskDao.update(model);
		if (count > 0) {
			LogModel logmodel = new LogModel();
			logmodel.createtime = new Timestamp(System.currentTimeMillis());
			logmodel.optype = TaskOpEnum.编辑.name();
			logmodel.taskid = model.taskid;
			logmodel.userid = account;
			logDao.insert(logmodel);
		}
		return getUpdateResultModel(count);
	}

	/***
	 * 更新状态
	 * 
	 * @param model
	 * @return
	 */
	public ServiceReturnModel updateTaskStatus(int taskid,
			TaskStateEnum taskstatus, String account) {
		int count = taskDao.updateStatus(taskid, taskstatus.ordinal());
		if (count > 0) {
			LogModel logmodel = new LogModel();
			logmodel.createtime = new Timestamp(System.currentTimeMillis());
			logmodel.optype = TaskOpEnum.更新.name();
			logmodel.taskid = taskid;
			logmodel.userid = account;
			logmodel.memo = String.format("状态更新为：%s", taskstatus.name());
			logDao.insert(logmodel);
		}
		return getUpdateResultModel(count);
	}

	/**
	 * 
	 * @param taskid
	 * @param taskstatus
	 * @return
	 */
	public ServiceReturnModel updateTaskStatusV2(int taskid,
			TaskStateEnum taskstatus, String account) {
		int count = taskDao.updateStatusV2(taskid, taskstatus.ordinal());
		if (count > 0) {
			LogModel logmodel = new LogModel();
			logmodel.createtime = new Timestamp(System.currentTimeMillis());
			logmodel.optype = TaskOpEnum.更新.name();
			logmodel.taskid = taskid;
			logmodel.userid = account;
			logmodel.memo = String.format("状态更新为：%s", taskstatus.name());
			logDao.insert(logmodel);
		}
		return getUpdateResultModel(count);
	}

	/**
	 * 
	 * @param taskid
	 * @param processid
	 * @return
	 */
	@Transactional
	public ServiceReturnModel updateProcessid(int taskid, int processid,
			TaskStateEnum taskstatus) {
		int count = taskDao.updateProcessid(taskid, processid);
		taskDao.updateStatusV2(taskid, taskstatus.ordinal());
		return getUpdateResultModel(count);
	}

	/**
	 * 
	 * @param taskid
	 * @param applicationid
	 * @return
	 */
	public ServiceReturnModel updateApplicationid(int taskid, int applicationid) {
		int count = taskDao.updateApplicationid(taskid, applicationid);
		return getUpdateResultModel(count);
	}

	/***
	 * 
	 * @param taskid
	 * @return
	 */
	public ServiceReturnModel selectByTaskid(int taskid) {
		ServiceReturnModel returnModel = ServiceReturnModel
				.getDefaultFailModel();
		try {
			TaskModel taskModel = taskDao.selectByID(taskid);
			if (taskModel == null) {
				returnModel = ServiceReturnModel.getFailModel("未查到此条数据。");
			} else {
				returnModel = ServiceReturnModel.getSucessModel("SUCCESS",
						taskModel);
			}
		} catch (Exception e) {
			Log4jUtil.error(e);
			returnModel = ServiceReturnModel.getExceptionModel(e);
		}
		return returnModel;

	}

	/**
	 * 
	 * @param map
	 * @return
	 */
	public int selectCountByWhere(Map<String, Object> map) {
		return taskDao.selectCountByWhere(map);
	}

	/**
	 * 执行hadoop实时任务。把执行过程最近100条的输出存入redis。执行python文件，暂时没有找到获取输出的解决方案
	 * 
	 * @param taskid
	 * @param account
	 * @return
	 */
	public ServiceReturnModel ProcessTask(final int taskid,
			final String account, Boolean isSyncReadState) {
		ServiceReturnModel returnModel = ServiceReturnModel
				.getDefaultFailModel();

		try {
			TaskModel taskModel = taskDao.selectByID(taskid);
			if (taskModel != null) {
				List<applicationModel> applications = applicationCore
						.getModelByApplicationNameAndStates(taskModel.taskname,
								HadoopHelperConfig.SuccessStates);
				if (applications != null && applications.size() > 0) {
					returnModel = ServiceReturnModel
							.getFailModel("任务正在执行中，无法再次执行。");
				} else {
					// OsUtils.processLinux("su - hadoop", false, false);//
					// 切换到hadoop账户
					// 拼接执行命令
					StringBuilder command = new StringBuilder(
							WebConfig.SprakSubmitCommand.trim());
					if (taskModel.filetype.equalsIgnoreCase("jar")) {
						command.append(" --class ");// jar
					} else {// python
						command.append(" --name ");
					}
					command.append(taskModel.taskname);
					command.append(" ");
					if (taskModel.taskparams != null) {
						command.append(taskModel.taskparams.trim());
						command.append(" ");
					}
					command.append(taskModel.filepath);
					if (taskModel.filetype.equalsIgnoreCase("jar")// jar需要添加参数2
							&& taskModel.taskparams2 != null) {
						command.append(" ");
						command.append(taskModel.taskparams2);
					}
					Log4jUtil.debug(command.toString());
					// 以上是拼接命令，并存入debug日志。
					String[] cmdA = { "/bin/sh", "-c", command.toString() };
					long taskStartTime = new Date().getTime();// 执行开始时间
					final Process process = Runtime.getRuntime().exec(cmdA);// 开启一个执行任务进程，并在记录进程Id
					final int pid = OsUtils.getProcessIdLinux(process);// 获取进程id
					updateProcessid(taskid, pid, TaskStateEnum.开启执行);// 更新线程id和状态
					new Thread() {// 另开一个线程 获取执行任务的输出，并存入redis
						public void run() {
							InputStream in = process.getInputStream();
							InputStream inError = process.getErrorStream();
							BufferedReader read = new BufferedReader(
									new InputStreamReader(in));
							BufferedReader readError = new BufferedReader(
									new InputStreamReader(inError));
							String line = null;
							String lineError = null;
							try {
								String redisTaskKey = getTaskRedisKey(taskid);
								Jedis jedis = RedisCore.getJedis();
								if (jedis == null) {
									Log4jUtil.error("获取redis实例失败。");
								} else {
									jedis.del(redisTaskKey);// 清空之前的内容
								}
								while ((line = read.readLine()) != null
										|| (lineError = readError.readLine()) != null) {
									if (jedis != null) {
										if (line != null)
											jedis.rpush(redisTaskKey, line);
										if (lineError != null
												&& lineError.length() > 0)// 任务出现异常
										{
											jedis.rpush(redisTaskKey, lineError);
											process.destroy();// 进程退出
											// updateProcessid(taskid,
											// 0,TaskStateEnum.任务异常);// 更新状态异常
										}
										// 以下控制redis单个key内容不超过100条，防止不停的追加造成redis沾满
										List<String> list = jedis.lrange(
												redisTaskKey, 0, -1);
										int size = list.size();
										int maxlen = 100;
										if (size > maxlen) {
											jedis.ltrim(redisTaskKey, size
													- maxlen, size);
										}
										// jedis.expire(redisTaskKey,86400);//设置过期时间
										if (lineError != null
												&& lineError.length() > 0) {
											break;// 跳出循环。
										}
									}
								}
								RedisCore.returnResource(jedis);
							} catch (Exception e) {
								updateTaskStatus(taskid, TaskStateEnum.程序异常,
										account);// 更新状态异常
								e.printStackTrace();
								Log4jUtil.error(e);
							}
						}
					}.start();
					if (isSyncReadState) {// 是否需要同步读取任务是否成功执行
						applicationModel appliModel2 = null;
						while (appliModel2 == null) {
							appliModel2 = applicationCore
									.getModelByApplicationNameAndStartTime(
											taskModel.taskname, taskStartTime);
							if (appliModel2 != null
									|| taskStartTime + 120000 < new Date()
											.getTime()) {// 查到对象或 循环超过2分钟 跳出循环
								if (HadoopHelperConfig.SuccessStates
										.contains(appliModel2.state))
									returnModel = ServiceReturnModel
											.getSucessModel("任务成功执行。");
								else {// 失败
									returnModel = ServiceReturnModel
											.getFailModel("任务状态："
													+ appliModel2.state);
								}
								break;
							}
							Thread.sleep(5000);// 等待5。
						}
					} else {
						returnModel = ServiceReturnModel
								.getSucessModel("任务已开始执行。");
					}
				}
			} else {
				returnModel = ServiceReturnModel.getFailModel("未查到此条数据。");
			}
		} catch (Exception e) {
			updateTaskStatus(taskid, TaskStateEnum.程序异常, account);// 更新状态异常
			Log4jUtil.error(e);
			returnModel = ServiceReturnModel.getExceptionModel(e);
		}
		return returnModel;
	}

	/**
	 * 获取 存入redis的任务执行日志
	 * 
	 * @param taskid
	 * @return
	 */
	public ServiceReturnModel GetProcessInfo(int taskid) {
		ServiceReturnModel returnModel = ServiceReturnModel.getFailModel("");
		try {
			String redisTaskKey = getTaskRedisKey(taskid);
			Jedis jedis = RedisCore.getJedis();
			if (jedis != null) {
				List<String> redisValue = jedis.lrange(redisTaskKey, 0, -1);
				String liststring = StringUtil
						.listToString(redisValue, "<br/>");
				RedisCore.returnResource(jedis);
				returnModel = ServiceReturnModel.getSucessModel("SUCEESS",
						liststring);
			} else {
				returnModel = ServiceReturnModel
						.getFailModel("获取redis实例失败，请检查redis配置或者redis服务器是否改动。");
			}
		} catch (Exception e) {
			returnModel = ServiceReturnModel.getExceptionModel(e);
			Log4jUtil.error(e);
		}
		return returnModel;
	}

	/**
	 * 杀死正在执行的任务。两种方案：1、通过hadoop命令yarn application -kill
	 * 
	 * @param taskid
	 * @return
	 */
	public ServiceReturnModel Kill(int taskid, String account) {
		ServiceReturnModel returnViewModel = null;
		try {
			int failCount = 0;
			TaskModel taskModel = taskDao.selectByID(taskid);
			if (taskModel != null) {
				List<applicationModel> applications = applicationCore
						.getListByApplicationName(taskModel.taskname);
				if (applications != null && applications.size() > 0) {
					for (applicationModel appmodelModel : applications) {
						if (HadoopHelperConfig.SuccessStates
								.contains(appmodelModel.state)) {
							OsUtils.processLinux("yarn application -kill "
									+ appmodelModel.id, true, true);// 执行kill命令
							Thread.sleep(2000);// 线程休眠2秒
							applicationModel appModel = applicationCore
									.getModelByApplicationId(appmodelModel.id);
							if (!appModel.state.equalsIgnoreCase("KILLED")) {// 执行完命令之后,再次获取状态，确认是否kill成功
								failCount++;
							}
						}
					}
					if (failCount > 0) {
						returnViewModel = ServiceReturnModel
								.getFailModel("KILL 失败。");

					} else {
						updateTaskStatus(taskid, TaskStateEnum.KILLED, account);// 更新状态
						returnViewModel = ServiceReturnModel
								.getSucessModel("SUCEESS");
					}
				} else {// 根据taskname再hadoop接口无法获得ApplicationId，原因有可能是任务down掉。
					returnViewModel = ServiceReturnModel
							.getFailModel("KILL 失败，根据taskname无法在接口获得application。");
				}
			} else {
				returnViewModel = ServiceReturnModel.getFailModel("未查到此条数据。");
			}
		} catch (Exception e) {
			updateTaskStatus(taskid, TaskStateEnum.程序异常, account);// 更新状态异常
			Log4jUtil.error(e);
			returnViewModel = ServiceReturnModel.getExceptionModel(e);
		}
		return returnViewModel;
	}

	/**
	 * 获取JSON格式数据列表
	 * 
	 * @param request
	 * @return
	 */
	public String GetList(HttpServletRequest request) {
		String jsonString = null;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			int sEcho = 0;
			if (request.getParameter("sEcho") != null)
				sEcho = Integer.valueOf(request.getParameter("sEcho"));
			if (request.getParameter("taskname") != null)
				map.put("taskname", request.getParameter("taskname"));
			if (request.getParameter("iDisplayStart") != null)
				map.put("startindex", request.getParameter("iDisplayStart"));
			if (request.getParameter("iDisplayLength") != null)
				map.put("pagesize", request.getParameter("iDisplayLength"));
			map.put("username", UserCore.getUserAccount(request));
			map.put("orderBy", "laststarttime desc,updatetime desc");
			List<TaskModel> list = taskDao.selectByPage(map);
			List<TaskJsonModel> jsonList = ConvertToJsonModel(list);
			if (jsonList != null) {
				for (TaskJsonModel taskJsonModel : jsonList) {
					applicationModel applimodel = applicationCore
							.getModelByApplicationName(taskJsonModel.taskname);
					if (applimodel != null)
						taskJsonModel.applicationState = applimodel.state;
					else {
						taskJsonModel.applicationState = "nofound";
					}
				}
			}
			int count = taskDao.selectCountByWhere(map);
			jsonString = "{\"sEcho\":"
					+ ++sEcho
					+ ",\"iTotalRecords\":"
					+ count
					+ ",\"iTotalDisplayRecords\":"
					+ count
					+ ",\"aaData\":"
					+ JSONObject.toJSONString(jsonList,
							SerializerFeature.WriteNullStringAsEmpty) + "}";
		} catch (Exception e) {
			Log4jUtil.error(e);
			return "Exception" + e.toString();
		}
		return jsonString;
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	public ServiceReturnModel edit(HttpServletRequest request) {
		ServiceReturnModel returnViewModel = ServiceReturnModel
				.getFailModel("操作失败。");
		try {
			Map<String, String> parameters = new HashMap<String, String>();
			UploadResult result = FileUploadUtil.upload(request,
					WebConfig.FileUpLoadRoot, WebConfig.FileAcceptType, true,
					parameters);
			int taskid = RegexUtil.GetInt(parameters.get("taskid"));
			if (result.equals(UploadResult.Success)
					|| (taskid > 0 && parameters.get("filepath").length() == 0)) {// 文件上传成功或者编辑的情况下
				TaskModel taskModel = new TaskModel();
				if (parameters.get("filepath").length() > 0) {
					taskModel.filepath = parameters.get("filepath");
				}
				taskModel.taskid = taskid;
				taskModel.taskname = parameters.get("taskname").trim();
				taskModel.filetype = parameters.get("filetype").trim();
				taskModel.taskparams = parameters.get("taskparams").trim();
				taskModel.taskparams2 = parameters.get("taskparams2").trim();
				taskModel.userid = 0;
				taskModel.username = UserCore.getUserAccount(request);
				taskModel.taskstatus = TaskStateEnum.未执行.ordinal();// 初始值
				int count = 0;
				if (taskid > 0) {// 编辑
					count = taskDao.update(taskModel);
				} else {
					count = taskDao.insert(taskModel);
				}
				if (count > 0) {
					returnViewModel = ServiceReturnModel.getSucessModel("操作成功",
							taskModel.taskid);
				} else {
					returnViewModel = ServiceReturnModel.getFailModel("操作失败");
					if (taskid == 0)// 添加操作失败，删除上传文件
					{
						IoUtil.FileDelete(taskModel.filepath);// 删除文件
					}
				}
			} else {
				returnViewModel = ServiceReturnModel.getFailModel("操作失败："
						+ result);
			}
		} catch (Exception e) {
			returnViewModel = ServiceReturnModel.getExceptionModel(e);
			Log4jUtil.error(e);
		}
		return returnViewModel;
	}

	/**
	 * 实时任务监控
	 * 
	 * @return
	 */
	public void MonitorRealtimeTask() {
		applicationUrlParamsModel paramsModel = new applicationUrlParamsModel();
		paramsModel.applicationTypes = AppOfRealTimeConfig.ApplicationTypes;
		paramsModel.states = AppOfRealTimeConfig.ApplicationStates;
		List<applicationModel> applicationList = applicationCore
				.RequestApplicationList(paramsModel);// 以上代码 是 获取hadoop实时任务列表
		int[] taskStatuses = { TaskStateEnum.开启执行.ordinal(),
				TaskStateEnum.程序异常.ordinal() };
		List<TaskModel> taskList = taskDao.selectByStatus(taskStatuses);// 从数据库获取实时任务
		boolean iscontain = false;
		int count = 0;
		int sucCount = 0;
		for (TaskModel taskModel : taskList) {// 两个for循环作比对，监控任务是否正常执行
			if (applicationList != null) {
				for (applicationModel appliModel : applicationList) {
					if (taskModel.taskname.equalsIgnoreCase(appliModel.name)) {
						iscontain = true;
						break;
					}
				}
			}
			if (!iscontain) {// 通过比对，任务不在hadoop服务器运行，表明该任务down掉。
				// 重启该任务。
				count++;
				try {
					ServiceReturnModel returnModel = ProcessTask(
							taskModel.taskid, "实时任务监控程序", true);
					System.out.println(String.format(
							"===========【%s】开始重启===========",
							taskModel.taskname));
					if (returnModel.State) {
						sucCount++;
						System.out.println("重启成功。");
					} else {
						String text = "重启失败，detail：" + returnModel.message;
						System.out.println(text);
						Log4jUtil.error(text);
					}
				} catch (Exception e) {
					String text = "重启失败，程序异常：" + e.toString();
					System.out.println(text);
					Log4jUtil.error(text);
				}

			}
		}
		if (count > 0)
			System.out.println(String.format(
					"===========本轮需要重启的任务%d个，成功重启%d个。", count, sucCount));
		else {
			System.out.println("===========本轮需要重启的任务0个");
		}
	}

	// ===========以下是私有方法和枚举定义

	/**
	 * 获取task执行任务输出日志rediskey
	 * 
	 * @param taskid
	 * @return
	 */
	private String getTaskRedisKey(int taskid) {
		return String.format("%s_task_%d",
				RedisDataKeyConfig.TaskProcessInfoList.getKey(), taskid);
	}

	/**
	 * 
	 * @param count
	 * @return
	 */
	private ServiceReturnModel getUpdateResultModel(int count) {
		ServiceReturnModel returnModel = ServiceReturnModel
				.getDefaultFailModel();
		if (count > 0) {// 更新成功
			returnModel = ServiceReturnModel.getSucessModel("更新状态成功，共影响"
					+ count + "条记录", count);
		} else {
			returnModel = ServiceReturnModel.getFailModel("更新状态失败。");
		}
		return returnModel;
	}

	/**
	 * 
	 * @param list
	 * @return
	 */
	private List<TaskJsonModel> ConvertToJsonModel(List<TaskModel> list) {
		List<TaskJsonModel> jsonList = null;
		if (list != null) {
			jsonList = new ArrayList<TaskJsonModel>();
			for (TaskModel taskModel : list) {
				jsonList.add(ReflectUtil.copyProperties(taskModel,
						TaskJsonModel.class));
			}
		}
		return jsonList;
	}

	/**
	 * 
	 * @author gaozhenyong
	 * 
	 */
	public static enum TaskStateEnum {
		未执行, 开启执行, 删除, KILLED, 程序异常
	};

	/**
	 * 
	 * @author gaozhenyong
	 * 
	 */
	public static enum TaskOpEnum {
		添加, 编辑, 删除, 执行, KILL, 更新
	};
}
