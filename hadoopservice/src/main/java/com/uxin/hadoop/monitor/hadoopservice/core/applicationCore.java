package com.uxin.hadoop.monitor.hadoopservice.core;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.uxin.hadoop.monitor.config.HadoopApiConfig;
import com.uxin.hadoop.monitor.hadoopservice.model.applicationModel;
import com.uxin.hadoop.monitor.hadoopservice.model.applicationUrlParamsModel;
import com.uxin.hadoop.monitor.utils.Log4jUtil;
import com.uxin.hadoop.monitor.utils.StringUtil;
import com.uxin.hadoop.monitor.utils.UrlUtil;

public class applicationCore {

	private static String applicationBaseUrl = HadoopApiConfig.applications_base_url;

	/**
	 * 
	 * @param url
	 * @param applicationName
	 * @return
	 */
	public static String getApplicationIdByName(String applicationName) {
		applicationModel appmodel = getModelByApplicationName(applicationName);
		if (appmodel != null)
			return appmodel.id;
		return null;
	}

	/***
	 * 根据name 获取model，如果有多个，获取开始时间最近的一个
	 * 
	 * @param applicationName
	 * @return
	 */
	public static applicationModel getModelByApplicationName(
			String applicationName) {
		long max = 0;
		applicationModel renturnModel = null;
		List<applicationModel> list = getListByApplicationName(applicationName);
		if (list != null && list.size() > 0) {
			for (applicationModel appliModel : list) {
				if (appliModel.startedTime > max) {
					renturnModel = appliModel;
					max = appliModel.startedTime;
				}
			}
		}
		return renturnModel;
	}

	/**
	 * 获取大于参数startTime的任务
	 * 
	 * @param applicationName
	 * @param startTime
	 * @return
	 */
	public static applicationModel getModelByApplicationNameAndStartTime(
			String applicationName, long startTime) {
		List<applicationModel> list = getListByApplicationName(applicationName);
		for (applicationModel appmodel : list) {
			if (appmodel.startedTime > startTime) {
				return appmodel;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param applicationName
	 * @param states
	 * @return
	 */
	public static List<applicationModel> getModelByApplicationNameAndStates(
			String applicationName, String states) {
		List<applicationModel> list = getListByApplicationName(applicationName);
		List<applicationModel> newList = new ArrayList<applicationModel>();
		for (applicationModel appmodel : list) {
			if (states.contains(appmodel.state)) {
				newList.add(appmodel);
			}
		}
		return newList;
	}

	/***
	 * 根据name获取List
	 * 
	 * @param applicationName
	 * @return
	 */
	public static List<applicationModel> getListByApplicationName(
			String applicationName) {
		List<applicationModel> list = RequestApplicationList(null);
		List<applicationModel> newList = new ArrayList<applicationModel>();
		if (list != null) {
			for (applicationModel appmodel : list) {
				if (applicationName.equalsIgnoreCase(appmodel.name)) {
					newList.add(appmodel);
				}
			}
		}
		return newList;
	}

	/***
	 * 
	 * @param id
	 * @return
	 */
	public static applicationModel getModelByApplicationId(String id) {
		try {
			String json = httpRequestForJson(applicationBaseUrl + id);
			if (json != null && json.length() > 0) {
				JSONObject root = JSONObject.parseObject(json);
				JSONObject app = root.getJSONObject("app");
				if (app != null) {
					return JsonToModel(app);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param parasModel
	 * @return
	 */
	public static List<applicationModel> RequestApplicationList(
			applicationUrlParamsModel parasModel) {
		List<applicationModel> list = new ArrayList<applicationModel>();
		String url2 = BuildeUrl(parasModel);
		if (url2 == null) {
			Log4jUtil.error("hadoop application接口url为null，请检查hadoop配置文件");
		}
		String json = httpRequestForJson(url2);
		if (json != null && json.length() > 0) {
			JSONObject root = JSONObject.parseObject(json);
			if (root != null) {
				JSONObject apps = root.getJSONObject("apps");
				if (apps != null) {
					JSONArray appList = apps.getJSONArray("app");
					applicationModel model = null;
					if (appList != null && appList.size() > 0) {
						list = new ArrayList<applicationModel>();
						for (int i = 0; i < appList.size(); i++) {
							try {
								JSONObject app = appList.getJSONObject(i);
								model = JsonToModel(app);
								list.add(model);
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					}
				}
			}
		}
		return list;
	}

	/***
	 * 
	 * @param parasModel
	 * @return
	 */
	private static String BuildeUrl(applicationUrlParamsModel parasModel) {
		String url = applicationBaseUrl;
		if (parasModel != null) {
			StringBuilder paramsBuffer = new StringBuilder();
			if (!StringUtil.IsNullOrEmpty(parasModel.applicationTags)) {
				paramsBuffer.append(String.format("&applicationTags=%s",
						parasModel.applicationTags));
			}
			if (!StringUtil.IsNullOrEmpty(parasModel.applicationTypes)) {
				paramsBuffer.append(String.format("&applicationTypes=%s",
						parasModel.applicationTypes));
			}
			if (!StringUtil.IsNullOrEmpty(parasModel.finalStatus)) {
				paramsBuffer.append(String.format("&finalStatus=%s",
						parasModel.finalStatus));
			}
			if (!StringUtil.IsNullOrEmpty(parasModel.queue)) {
				paramsBuffer.append(String
						.format("&queue=%s", parasModel.queue));
			}
			if (!StringUtil.IsNullOrEmpty(parasModel.state)) {
				paramsBuffer.append(String
						.format("&state=%s", parasModel.state));
			}
			if (!StringUtil.IsNullOrEmpty(parasModel.user)) {
				paramsBuffer.append(String.format("&user=%s", parasModel.user));
			}
			if (!StringUtil.IsNullOrEmpty(parasModel.states)) {
				paramsBuffer.append(String.format("&states=%s",
						parasModel.states));
			}
			if (parasModel.finishedTimeBegin > 0) {
				paramsBuffer.append(String.format("&finishedTimeBegin=%s",
						parasModel.finishedTimeBegin));
			}
			if (parasModel.finishedTimeEnd > 0) {
				paramsBuffer.append(String.format("&finishedTimeEnd=%s",
						parasModel.finishedTimeEnd));
			}
			if (parasModel.limit > 0) {
				paramsBuffer.append(String
						.format("&limit=%s", parasModel.limit));
			}
			if (parasModel.startedTimeBegin > 0) {
				paramsBuffer.append(String.format("&startedTimeBegin=%s",
						parasModel.startedTimeBegin));
			}
			if (parasModel.startedTimeEnd > 0) {
				paramsBuffer.append(String.format("&startedTimeEnd=%s",
						parasModel.startedTimeEnd));
			}
			String paramsString = paramsBuffer.toString();
			if (paramsString.length() > 0) {
				paramsString = StringUtil.TrimStart(paramsString, "&");
				url = url + "?" + paramsString;
			}
		}
		return url;
	}

	/***
	 * 
	 * @param app
	 * @return
	 */
	private static applicationModel JsonToModel(JSONObject obj) {
		applicationModel model = new applicationModel();
		try {
			model.id = obj.getString("id");
			model.user = obj.getString("user");
			model.name = obj.getString("name");
			model.queue = obj.getString("queue");
			model.state = obj.getString("state");
			model.finalStatus = obj.getString("finalStatus");
			model.trackingUI = obj.getString("trackingUI");
			model.trackingUrl = obj.getString("trackingUrl");
			model.diagnostics = obj.getString("diagnostics");
			model.applicationType = obj.getString("applicationType");
			model.applicationTags = obj.getString("applicationTags");
			model.amContainerLogs = obj.getString("amContainerLogs");
			model.amHostHttpAddress = obj.getString("amHostHttpAddress");
			String value = obj.getString("clusterId");
			if (value != null) {
				model.clusterId = Long.parseLong(value);
			}
			value = obj.getString("startedTime");
			if (value != null) {
				model.startedTime = Long.parseLong(value);
			}
			value = obj.getString("finishedTime");
			if (value != null) {
				model.finishedTime = Long.parseLong(value);
			}
			value = obj.getString("elapsedTime");
			if (value != null) {
				model.elapsedTime = Integer.valueOf(value);
			}
			value = obj.getString("allocatedMB");
			if (value != null) {
				model.allocatedMB = Integer.valueOf(value);
			}
			value = obj.getString("allocatedVCores");
			if (value != null) {
				model.allocatedVCores = Integer.valueOf(value);
			}
			value = obj.getString("runningContainers");
			if (value != null) {
				model.runningContainers = Integer.valueOf(value);
			}
			value = obj.getString("memorySeconds");
			if (value != null) {
				model.memorySeconds = Integer.valueOf(value);
			}
			value = obj.getString("vcoreSeconds");
			if (value != null) {
				model.vcoreSeconds = Integer.valueOf(value);
			}
			value = obj.getString("preemptedResourceMB");
			if (value != null) {
				model.preemptedResourceMB = Integer.valueOf(value);
			}
			value = obj.getString("preemptedResourceVCores");
			if (value != null) {
				model.preemptedResourceVCores = Integer.valueOf(value);
			}
			value = obj.getString("numNonAMContainerPreempted");
			if (value != null) {
				model.numNonAMContainerPreempted = Integer.valueOf(value);
			}
			value = obj.getString("numAMContainerPreempted");
			if (value != null) {
				model.numAMContainerPreempted = Integer.valueOf(value);
			}
			value = obj.getString("progress");
			if (value != null) {
				model.progress = Float.valueOf(value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}

	/**
	 * 
	 * @param url
	 * @return
	 */
	private static String httpRequestForJson(String url) {
		return UrlUtil.RequestJson(url, null);
	}

}
