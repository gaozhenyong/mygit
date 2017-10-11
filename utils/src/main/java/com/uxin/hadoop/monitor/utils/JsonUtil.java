package com.uxin.hadoop.monitor.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JsonUtil {

	/**
	 * 
	 * @param obj
	 * @return
	 */
	public static String toJson(Object obj) {
		if (obj == null) {
			return "null";
		}
		String jsonStr = "{";
		Class<?> cla = obj.getClass();
		Field fields[] = cla.getFields();
		for (Field field : fields) {
			try {
				// field.setAccessible(true);
				if (field.getType() == long.class) {
					jsonStr += "\"" + field.getName() + "\":"
							+ field.getLong(obj) + ",";
				} else if (field.getType() == double.class) {
					jsonStr += "\"" + field.getName() + "\":"
							+ field.getDouble(obj) + ",";
				} else if (field.getType() == float.class) {
					jsonStr += "\"" + field.getName() + "\":"
							+ field.getFloat(obj) + ",";
				} else if (field.getType() == int.class) {
					jsonStr += "\"" + field.getName() + "\":"
							+ field.getInt(obj) + ",";
				} else if (field.getType() == boolean.class) {
					jsonStr += "\"" + field.getName() + "\":"
							+ field.getBoolean(obj) + ",";
				} else if (field.getType() == Integer.class
						|| field.getType() == Boolean.class
						|| field.getType() == Double.class
						|| field.getType() == Float.class
						|| field.getType() == Long.class) {
					jsonStr += "\"" + field.getName() + "\":" + field.get(obj)
							+ ",";
				} else if (field.getType() == String.class) {
					jsonStr += "\"" + field.getName() + "\":\""
							+ field.get(obj).toString().replace("\"", "'")
							+ "\",";
				} else if (field.getType() == List.class) {
					String value = simpleListToJsonStr((List<?>) field.get(obj));
					jsonStr += "\"" + field.getName() + "\":" + value + ",";
				} else {
					jsonStr += "\"" + field.getName() + "\":\""
							+ field.get(obj) + "\",";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		jsonStr = jsonStr.substring(0, jsonStr.length() - 1);
		jsonStr += "}";
		return jsonStr;
	}

	/**
	 * 
	 * @param list
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static String simpleListToJsonStr(List<?> list)
			throws IllegalArgumentException, IllegalAccessException {
		if (list == null || list.size() == 0) {
			return "[]";
		}
		String jsonStr = "[";
		for (Object object : list) {
			jsonStr += toJson(object) + ",";
		}
		jsonStr = jsonStr.substring(0, jsonStr.length() - 1);
		jsonStr += "]";
		return jsonStr;
	}

	/**
	 * 
	 * @param jsonStr
	 * @return
	 */
	public static Object toObject(String jsonStr) {
		try {
			return JSONObject.parse(jsonStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param jsonStr
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> toList(String jsonStr) {
		try {
			List<T> list = new ArrayList<T>();
			JSONArray array = JSONArray.parseArray(jsonStr);
			for (Object arr : array) {
				list.add((T) arr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param strList
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> toList(List<String> strList, Class<T> clazz) {
		try {
			List<T> list = new ArrayList<T>();
			Field[] fs = clazz.getFields();// 获取实体类的所有属性
			for (String str : strList) {
				T t = clazz.newInstance();
				JSONObject jObj = JSONObject.parseObject(str);

				for (Field field : fs) {
					if (field.getType() == boolean.class) {
						field.set(t, jObj.getBoolean(field.getName()));
					} else if (field.getType() == Date.class) {
						field.set(t, (Date) jObj.get(field.getName()));
					} else {
						field.set(t, jObj.get(field.getName()));
					}
				}
				list.add(t);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
