//import java.io.BufferedReader;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.lang.reflect.Field;
//
//public class mytest {
//
//	/**
//	 * @param args
//	 * @throws NoSuchFieldException
//	 * @throws SecurityException
//	 * @throws IllegalAccessException
//	 * @throws IllegalArgumentException
//	 * @throws InterruptedException
//	 */
//	public static void main(String[] args) throws SecurityException,
//			NoSuchFieldException, IllegalArgumentException,
//			IllegalAccessException, InterruptedException {
//		try {
//			System.out.println("start");
//			String[] cmdA = {
//					"/bin/sh",
//					"-c",
//					"/data1/xinsrv/spark-2.0.2/bin/spark-submit  --class com.xin.cbl.CheBoLeUserClickTest --master  local[4] --executor-memory 1g --driver-memory 2G --conf \"spark.locality.wait=10ms\" --conf spark.default.parallelism=1000 --num-executors 2 --executor-cores 1  /home/niweiwei/apps/test/cbl/cbl_test-1.0-SNAPSHOT.jar --batch 1 --ip 10.70.32.54 --port 9999" };
//			Process process = Runtime.getRuntime().exec(cmdA);
//			Field fPid = process.getClass().getDeclaredField("pid");
//			if (!fPid.isAccessible()) {
//				fPid.setAccessible(true);
//			}
//			System.out.println("PID:" + fPid.getInt(process));
//			InputStream in = process.getInputStream();
//			InputStream inError = process.getErrorStream();
//			BufferedReader read = new BufferedReader(new InputStreamReader(in));
//			BufferedReader readError = new BufferedReader(
//					new InputStreamReader(inError));
//			String line = null;
//			String lineError = null;
//			while ((line = read.readLine()) != null
//					|| (lineError = readError.readLine()) != null) {
//				if (line != null)
//					System.out.println(line + "======");
//				if (lineError != null)
//					System.out.println(lineError + "======xxx");
//			}
//			System.out.println("end");
//		} catch (java.io.IOException e) {
//			System.err.println("IOException " + e.getMessage());
//		}
//	}
// }
