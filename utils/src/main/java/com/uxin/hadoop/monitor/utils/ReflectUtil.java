package com.uxin.hadoop.monitor.utils;

import java.lang.reflect.Field;

public class ReflectUtil {

	public static <T> T copyProperties(Object s, Class<T> clazz) {
		T t = null;
		if (s != null) {
			try {
				t = clazz.newInstance();
				Field[] sfields = s.getClass().getFields();// 源
				Field[] tfields = clazz.getFields();// 目标
				L: for (Field sfield : sfields) {
					sfield.setAccessible(true);
					for (Field tfield : tfields) {
						if (sfield.getName().equals(tfield.getName())) {
							tfield.setAccessible(true);
							try {
								tfield.set(t, sfield.get(s));
							} catch (Exception e) {
								continue L;
							}
						}
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return t;
	}
}
