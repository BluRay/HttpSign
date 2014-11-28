package http.sign;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

//import wrapper.Http;

@SuppressWarnings("deprecation")
public class Autosign extends TimerTask{
	public static boolean needLogin = true;
	public static boolean needSign = false;
	public static HttpClient httpclient = new DefaultHttpClient();
	@Override
	@SuppressWarnings({ "unused" })
	public void run() {
		System.out.println("-------Autosign run-------Date : " + new Date().toString());  
		
		//System.out.println(JavaRead_Ini2.getValue("aaa", "bbb", "/home/yangkee/HttpSign.ini"));
		try {
			//String charset = "utf-8";
			String hashcode = "0000";
			String url = "http://hdwing.com/validatecode.php";
	        String destfilename = "/home/esshinf/code.png";		
	        HttpGet httpget = new HttpGet(url);
	        HttpResponse response = httpclient.execute(httpget);
	        HttpEntity entity = response.getEntity();
	        InputStream in = entity.getContent();
			if (needLogin) {
				// 第一步：先下载验证码到本地		        
		        File file = new File(destfilename);
		        if (file.exists()) {
		            file.delete();
		        }
		        try {
		            FileOutputStream fout = new FileOutputStream(file);
		            int l = -1;
		            byte[] tmp = new byte[2048]; 
		            while ((l = in.read(tmp)) != -1) {
		                fout.write(tmp);
		            } 
		            fout.close();
		        } finally {
		            in.close();
		        }
		        httpget.releaseConnection();
		        
		        // 第二步：用Post方法带若干参数尝试登录，需要手工输入下载验证码中显示的字母、数字
//		        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//		        System.out.print("请输入验证码：");
//		        String codenum = br.readLine();
//		        System.out.println ("你输入的验证码为："+ codenum + ",开始登陆...");
		        System.out.print("请在配置文件中修改验证码");
		        Thread.sleep(60000);
		        String codenum = JavaRead_Ini2.getValue("sign", "code", "/home/esshinf/HttpSign.ini");
		        System.out.println ("你输入的验证码为："+ codenum + ",开始登陆...");
		        if (!("0000".equals(codenum))){
		        	HttpPost httppost = new HttpPost("http://hdwing.com/takelogin.php");
			        List<NameValuePair> params = new ArrayList<NameValuePair>();
			        params.add(new BasicNameValuePair("username", "youaccount"));
			        params.add(new BasicNameValuePair("password", "yourpassword"));
			        params.add(new BasicNameValuePair("code", codenum));
			        httppost.setEntity(new UrlEncodedFormEntity(params));
			        
			        response = httpclient.execute(httppost);
			        entity = response.getEntity();
			        // 在这里可以用Jsoup之类的工具对返回结果进行分析，以判断登录是否成功
			        String postResult = EntityUtils.toString(entity, "GBK"); 
			        // 我们这里只是简单的打印出当前Cookie值以判断登录是否成功。
			        List<Cookie> cookies = ((AbstractHttpClient)httpclient).getCookieStore().getCookies();
			        for(Cookie cookie: cookies)
			            System.out.println(cookie);
			        httppost.releaseConnection();

			        needLogin= false;	//登陆完成，下次无需再次登录
					needSign = true;	//登陆完成，可以进行签到操作
				}								
			}else{
				httpget.releaseConnection();
			}
			//调用签到程序
			if (needSign) {	
				// 第三步：打开会员页面以判断登录成功 未登录用户是打不开会员页面的
		        // 获取签到HASH值
		        String memberpage = "http://hdwing.com/bookmarks.php";
		        httpget = new HttpGet(memberpage);
		        response = httpclient.execute(httpget); // 必须是同一个HttpClient！
		        entity = response.getEntity();
		        String html = EntityUtils.toString(entity, "GBK");
		        httpget.releaseConnection();
		        //System.out.println(html);
		        hashcode = html.substring(
		        		html.indexOf("data: { hash:") + 14,
		        		html.indexOf("data: { hash:") + 22);
		        System.out.println("签到HASH值 = " + hashcode); 
				
		        if (!("l PUBLIC".equals(hashcode))){	//已签过到的HASH值为 l PUBLIC
					HttpPost httppost = new HttpPost("http://hdwing.com/usersign.php");
			        List<NameValuePair> params = new ArrayList<NameValuePair>();
			        params.add(new BasicNameValuePair("hash", hashcode));
			        httppost.setEntity(new UrlEncodedFormEntity(params));
			        response = httpclient.execute(httppost);
			        entity = response.getEntity();
			        // 在这里可以用Jsoup之类的工具对返回结果进行分析，以判断登录是否成功
			        String postResult = EntityUtils.toString(entity, "GBK"); 
			        System.out.println("签到结果 ： " + postResult); 
			        hashcode = "0000";
			        httppost.releaseConnection();
		        }
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)throws Exception {
		//String html = "data: { hash:\"tpQBmKFa\" }";
		//System.out.println(html.substring(
        //		html.indexOf("data: { hash:\"") + 14,
        //		html.indexOf("data: { hash:\"") + 22)); 
		System.out.println(new Date().toString()); 
		
		/***
		// 第一步：先下载验证码到本地
        String url = "http://hdwing.com/validatecode.php";
        String destfilename = "code.png";
        @SuppressWarnings({ "resource" })
		HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        File file = new File(destfilename);
        if (file.exists()) {
            file.delete();
        }
        
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        InputStream in = entity.getContent();
        try {
            FileOutputStream fout = new FileOutputStream(file);
            int l = -1;
            byte[] tmp = new byte[2048]; 
            while ((l = in.read(tmp)) != -1) {
                fout.write(tmp);
            } 
            fout.close();
        } finally {
            in.close();
        }
        httpget.releaseConnection();

        
        // 第二步：用Post方法带若干参数尝试登录，需要手工输入下载验证码中显示的字母、数字
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("请输入验证码：");
        String codenum = br.readLine();
        System.out.println ("你输入的验证码为："+ codenum + ",开始登陆...");
        HttpPost httppost = new HttpPost("http://hdwing.com/takelogin.php");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", "jokert"));
        params.add(new BasicNameValuePair("password", "361722"));
        params.add(new BasicNameValuePair("code", codenum));
        httppost.setEntity(new UrlEncodedFormEntity(params));
        
        response = httpclient.execute(httppost);
        entity = response.getEntity();
        // 在这里可以用Jsoup之类的工具对返回结果进行分析，以判断登录是否成功
        String postResult = EntityUtils.toString(entity, "GBK"); 
        // 我们这里只是简单的打印出当前Cookie值以判断登录是否成功。
        List<Cookie> cookies = ((AbstractHttpClient)httpclient).getCookieStore().getCookies();
        for(Cookie cookie: cookies)
            System.out.println(cookie);
        httppost.releaseConnection();

        
        // 第三步：打开会员页面以判断登录成功（未登录用户是打不开会员页面的
        // 获取签到HASH值
        String memberpage = "http://hdwing.com/bookmarks.php";
        httpget = new HttpGet(memberpage);
        response = httpclient.execute(httpget); // 必须是同一个HttpClient！
        entity = response.getEntity();
        String html = EntityUtils.toString(entity, "GBK");
        httpget.releaseConnection();
        //System.out.println(html);
        String hashcode = html.substring(
        		html.indexOf("data: { hash:") + 14,
        		html.indexOf("data: { hash:") + 22);
        System.out.println("签到HASH值 = " + hashcode); 
		***/
        
        
        
        
        
		/**
		String charset = "utf-8";  
		//登录  
		Http http1 = new Http("http://hdwing.com/takelogin.php", charset);
        System.out.println("---->Start Autosign login.");
        
        //下载验证码图片
        URL url = new URL("http://hdwing.com/validatecode.php?tm=0.8797236603251121");  //返回的是4位验证码的图片  
        File outFile = new File("code.png");  
        OutputStream os = new FileOutputStream(outFile);
  
        InputStream is = url.openStream();  
        byte[] buff = new byte[1024];  
        while(true) {
	        int readed = is.read(buff);  
	        if(readed == -1) {  
	            break;  
	        }  
        byte[] temp = new byte[readed];  
        System.arraycopy(buff, 0, temp, 0, readed);   // 这句是关键  
        os.write(temp);  
	    }  
	    is.close();   
	    os.close();        
        
        String codenum;
        InputStreamReader stdin = new InputStreamReader(System.in);// 键盘输入
		BufferedReader bufin = new BufferedReader(stdin);
		System.out.print("请输入验证码：");
		codenum = bufin.readLine();
		System.out.println ("你输入的验证码为："+ codenum + ",开始登陆...");
		
		http1.addPostData("username", "jokert");  
        http1.addPostData("password", "361722");
        http1.addPostData("code", codenum);
        http1.request();
        
        //调用签到程序 获取hash值
        Http http2 = new Http("http://hdwing.com/bookmarks.php", charset);
        //Http http2 = new Http("http://hdwing.com/usersign.php", charset);
		// http2.addPostData("data", "{hash:\"sa0FUMH7\"}");
		//http2.addPostData("hash", "IVBmUVPn");
		http2.request();
        
        System.out.println("-------response header-------");  
        @SuppressWarnings("unchecked")
		Map<String, List<String>> headersMap = http2.getHeaders();  
        for (Map.Entry<String, List<String>> entry : headersMap.entrySet()) {  
            String string = entry.getKey();  
            List<String> list = entry.getValue();  
            System.out.println(string + ": " + list.get(0));  
        }  
        //内容  
        //压缩的  
        if (http2.getHeader("Content-Encoding") != null) {  
            System.out.println("压缩的，格式为" + http2.getHeader("Content-Encoding"));  
            //使用http2.getInputStream()得到二进制流，做其它处理，解压缩、保存到文件等等。  
            return;  
        }  
        
        //非压缩的  
        System.out.println("-------response content-------");  
        InputStreamReader isr = new InputStreamReader(http2.getInputStream(), charset);  
        StringBuilder stringBuilder = new StringBuilder();  
        int len;  
        char[] cbuf = new char[1024];  
        while ((len = isr.read(cbuf)) >= 0) {  
            stringBuilder.append(cbuf, 0, len);  
        }  
        System.out.println(stringBuilder.toString()); 
        System.out.println(stringBuilder.toString().indexOf("data: { hash:")); 
        System.out.println(stringBuilder.toString().substring(
        		stringBuilder.toString().indexOf("data: { hash:"),
        		stringBuilder.toString().indexOf("data: { hash:") + 10)); 
        		
        **/
	}



}
