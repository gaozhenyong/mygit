package com.uxin.hadoop.monitor.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;

public class OsUtils {

	/**
	 * 
	 * @param cmdA
	 * @param iskill
	 *            ,如果为true，调用系统kill命令杀死进程树。
	 * @return
	 */
	public static boolean process(String[] cmdA, boolean waitfor, boolean iskill) {
		int exitVal = 0;
		try {
			Process process = Runtime.getRuntime().exec(cmdA);
			Map<String, String> maps = ReadStream(process);
			if (waitfor)
				exitVal = process.waitFor();
			if (maps.get("error").length() > 0)
				exitVal = -1;
			process.destroy();// 此方法只会cmd进程，无法杀死cmd.exe创建的进程，也就是说无法杀死进程树
			if (iskill) {
				if (getOsType() == OsTypeEnum.windows)
					process = Runtime.getRuntime().exec(
							getKillCmdByWindows(getProcessIdWindows(process)));// kill杀死进程树
				if (getOsType() == OsTypeEnum.linux)
					process = Runtime.getRuntime().exec(
							getKillCmdByLinux(getProcessIdLinux(process)));// kill杀死进程树
				process.destroy();
			}
		} catch (Exception e) {
			exitVal = -1;
			Log4jUtil.error(e);
		}
		return exitVal == 0 ? true : false;
	}

	/**
	 * 异步读取子进程输出内容，与同步读取的区别是 异步只读取不保存
	 * 
	 * @param process
	 */
	public static void ReadStreamAsync(final Process process) {
		new Thread() {
			public void run() {
				InputStream in = process.getInputStream();
				InputStream inError = process.getErrorStream();
				BufferedReader read = new BufferedReader(new InputStreamReader(
						in));
				BufferedReader readError = new BufferedReader(
						new InputStreamReader(inError));
				String line = null;
				String lineError = null;
				try {
					while ((line = read.readLine()) != null
							|| (lineError = readError.readLine()) != null) {
					}
				} catch (Exception e) {
				}
			}
		}.start();
	}

	/**
	 * 同步读取子进程输出内容
	 * 
	 * @param process
	 * @return
	 */
	public static Map<String, String> ReadStream(Process process) {
		Map<String, String> map = null;
		try {
			InputStream in = process.getInputStream();
			InputStream inError = process.getErrorStream();
			BufferedReader read = new BufferedReader(new InputStreamReader(in));
			BufferedReader readError = new BufferedReader(
					new InputStreamReader(inError));
			String line = null, lineError = null;
			StringBuilder sb = new StringBuilder();
			StringBuilder sbError = new StringBuilder();
			while ((line = read.readLine()) != null
					|| (lineError = readError.readLine()) != null) {
				if (line != null) {
					sb.append(line);
					System.out.println(line);
				}
				if (lineError != null) {
					sbError.append(lineError);
					System.out.println(lineError);
				}
			}
			map = new HashMap<String, String>();
			map.put("content", sb.toString());
			map.put("error", sbError.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 
	 * @param command
	 * @return
	 */
	public static boolean processLinux(String command, boolean waitfor,
			boolean iskill) {
		String[] cmdA = { "/bin/sh", "-c", command };
		return process(cmdA, waitfor, iskill);
	}

	/**
	 * 
	 * @param command
	 * @return
	 */
	public static boolean processWindows(String command, Boolean waitfor,
			boolean iskill) {
		return process(new String[] { "cmd /c " + command }, waitfor, iskill);
	}

	/**
	 * 
	 * @param process
	 * @return
	 */
	public static int getProcessIdLinux(Process process) {
		try {
			Field fPid = process.getClass().getDeclaredField("pid");
			fPid.setAccessible(true);
			return fPid.getInt(process);
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 
	 * @param process
	 * @return
	 */
	public static int getProcessIdWindows(Process process) {
		try {
			Field f = process.getClass().getDeclaredField("handle");
			f.setAccessible(true);
			long handl = f.getLong(process);
			Kernel32 kernel = Kernel32.INSTANCE;
			WinNT.HANDLE handle = new WinNT.HANDLE();
			handle.setPointer(Pointer.createConstant(handl));
			int ret = kernel.GetProcessId(handle);
			return Integer.valueOf(ret);
		} catch (Exception e) {
			return 0;
		}
	}

	/***
	 * 
	 * @param pid
	 * @return
	 */
	public static String getKillCmdByLinux(int pid) {
		return String.format("kill -9 %d ", pid);
	}

	/**
	 * 
	 * @param pid
	 * @return
	 */
	public static String getKillCmdByWindows(int pid) {
		return String.format("taskkill /pid %d /F /T", pid);
	}

	/**
	 * 
	 * @return
	 */
	public static OsTypeEnum getOsType() {
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().contains("windows")) {
			return OsTypeEnum.windows;
		} else {
			return OsTypeEnum.linux;
		}
	}

	/**
	 * 
	 * @author gaozhenyong
	 * 
	 */
	public enum OsTypeEnum {
		windows, linux
	}
}
