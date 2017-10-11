package com.uxin.hadoop.monitor.utils;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class FileUploadUtil {

	/***
	 * 
	 * @param request
	 * @param uploadPath
	 * @acceptType 验证 文件类型
	 * @isSupportCover 支持对已有文件覆盖
	 * @param parameters获取所有的参数
	 *            获取所有参数 ，如果需要获取其他参数，必须传一个已实例化的map；该参数可以为null。
	 * @return SUCCESS标识上传上传，其余均为上传失败。
	 */
	public static UploadResult upload(HttpServletRequest request,
			String uploadPath, String acceptType, Boolean isSupportCover,
			Map<String, String> parameters) {
		UploadResult result = UploadResult.Fail;
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart == true) {
			try {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				List<FileItem> items = upload.parseRequest(request);// 得到所有的文件
				Iterator<FileItem> itr = items.iterator();
				while (itr.hasNext()) {// 依次处理每个文件
					FileItem item = (FileItem) itr.next();
					String filedName = item.getFieldName();
					String value = "";
					if (!item.isFormField()) {// 文件项
						String fileName = item.getName();// 获得文件名，包括路径
						if (fileName != null && fileName.length() > 0) {
							if (fileName.contains(File.pathSeparator)) {// 如果是完整路径，获取文件名
								fileName = new File(fileName).getName();
							}
							String extension = IoUtil.getExtension(fileName);
							if (!acceptType.contains(extension)) {
								result = UploadResult.FileTypeIncorrect;
							}
							File savedFile = new File(uploadPath,
									IoUtil.getFileName() + extension);
							if (!savedFile.exists() || isSupportCover) {// 文件不存在，可以上传
								savedFile.setWritable(true, false);
								item.write(savedFile);
								result = UploadResult.Success;
							} else {
								result = UploadResult.FileAlreadyExists;
							}
							value = savedFile.getAbsolutePath();
						} else {
							result = UploadResult.NotFindFile;
						}
					} else {// 普通控件
						value = item.getString("UTF-8");
					}
					if (parameters != null) {
						parameters.put(filedName, value);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				result = UploadResult.Exception;
			}
		} else {
			result = UploadResult.NotFindFile;
		}
		return result;
	}

	/**
	 * 
	 * @author gaozhenyong
	 * 
	 */
	public enum UploadResult {
		FileTypeIncorrect, Success, FileAlreadyExists, NotFindFile, Exception, Fail
	}
}
