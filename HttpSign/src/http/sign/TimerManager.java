package http.sign;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public class TimerManager {
	// 时间间隔24 * 60 * 60 * 1000
	private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;

	public TimerManager() {
		Calendar calendar = Calendar.getInstance();

		/*** 定制每日3:30执行方法 ***/

		calendar.set(Calendar.HOUR_OF_DAY, 8);
		calendar.set(Calendar.MINUTE, 27);
		calendar.set(Calendar.SECOND, 0);

		Date date = calendar.getTime(); // 第一次执行定时任务的时间

		// 如果第一次执行定时任务的时间 小于 当前的时间
		// 此时要在 第一次执行定时任务的时间 加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。
		if (date.before(new Date())) {
			date = this.addDay(date, 1); 
		}
		/**
		System.out.println("-------程序启动时登陆-------"); 
		System.out.println("---->Start Autosign login...");
		try {
			String charset = "utf-8";
			Http http1 = new Http("http://hdwing.com/takelogin.php", charset);
			// 下载验证码图片
			URL url = new URL("http://hdwing.com/validatecode.php"); // 返回的是4位验证码的图片
			File outFile = new File("/home/esshinf/code.png");
			OutputStream os = new FileOutputStream(outFile);
			// BufferedReader bf = new BufferedReader(new
			// InputStreamReader(url.openStream()));

			InputStream is = url.openStream();
			byte[] buff = new byte[1024];
			while (true) {
				int readed = is.read(buff);
				if (readed == -1) {
					break;
				}
				byte[] temp = new byte[readed];
				System.arraycopy(buff, 0, temp, 0, readed);
				os.write(temp);
			}
			is.close();
			os.close();

			String codenum;
			InputStreamReader stdin = new InputStreamReader(System.in);// 键盘输入
			BufferedReader bufin = new BufferedReader(stdin);
			System.out.print("请输入验证码：");
			codenum = bufin.readLine();
			System.out.println("你输入的验证码为： " + codenum);
			if (codenum != null){
				System.out.println("开始登陆...");
				http1.addPostData("username", "jokert");
				http1.addPostData("password", "361722");
				http1.addPostData("code", codenum);
				http1.request();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		**/
		
		Timer timer = new Timer();
		Autosign task = new Autosign();
		// 安排指定的任务在指定的时间开始进行重复的固定延迟执行。
		timer.schedule(task, date, PERIOD_DAY);
	}

	 // 增加或减少天数
	public Date addDay(Date date, int num) {
		Calendar startDT = Calendar.getInstance();
		startDT.setTime(date);
		startDT.add(Calendar.DAY_OF_MONTH, num);
		return startDT.getTime();
	}
	 
}

