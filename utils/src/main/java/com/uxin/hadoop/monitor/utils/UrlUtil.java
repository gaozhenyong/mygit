package com.uxin.hadoop.monitor.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public final class UrlUtil {

	public static String HttpRequest(String url, Map<String, String> headers,
			String postData) {
		StringBuilder content = new StringBuilder();
		BufferedReader in = null;
		try {
			URL req_url = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) req_url
					.openConnection();
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
			connection.setRequestProperty("Charset", "UTF-8");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			if (headers != null && headers.size() > 0)
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					connection.setRequestProperty(entry.getKey(),
							entry.getValue());
				}
			// 获取所有响应头字段
			// Map<String, List<String>> map = connection.getHeaderFields();
			if (postData != null && postData.length() > 0) {

				connection.setRequestMethod("POST"); // 设置POST方式连接
				connection.setDoOutput(true);
				connection.setDoInput(true);
				DataOutputStream dos = new DataOutputStream(
						connection.getOutputStream());
				dos.writeBytes(postData);
				dos.flush();
				dos.close();
			} else {
				// 建立实际的连接
				connection.connect();
			}
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), "utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
				content.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return content.toString();
	}

	/**
	 * 
	 * @param url
	 * @param postData
	 * @return
	 */
	public static String RequestJson(String url, String postData) {
		Map<String, String> headersMap = new HashMap<String, String>();
		headersMap.put("Content-Type", "application/json;charset=UTF-8");
		return HttpRequest(url, headersMap, postData);
	}
}
