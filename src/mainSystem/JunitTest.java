package mainSystem;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class JunitTest {

	@Test
	public void test() {
		StringBuilder stb=null;
		BufferedReader br_citylist=null;
		String decode=null;
		int count=0;
		try {
			String city="北京";
			String urlencode=URLEncoder.encode(city,"utf-8");
			String url="http://apicloud.mob.com/environment/query?key=1d42eae83af38&city="+urlencode;
//			String url2="https://github.com/";
			URL url1=new URL(url);
			HttpURLConnection connection=(HttpURLConnection) url1.openConnection();
			connection.setReadTimeout(1000 * 60);// 设置超时时间为60秒
			connection.setDoInput(true);// 读取数据
			connection.setRequestMethod("GET");// 设置请求方式为GET方式
			if (connection.getResponseCode() == 200) {// 请求码200表示请求成功
				br_citylist = new BufferedReader(
						new InputStreamReader(
								connection.getInputStream()));
			} else {
				// 后续可以继续完善请求失败的处理
				throw new Exception("网络请求失败！请求错误码："
						+ connection.getResponseCode());

			}
			char[] buff = new char[1024];
//			byte[] buff=new byte[10240];
			stb = new StringBuilder();
			while ((count = br_citylist.read(buff)) != -1) {
				decode = new String(buff, 0, count);
				stb.append(decode);
			}
			br_citylist.close();
			System.out.println(stb.toString());
			connection.disconnect();
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	@Test
	public void test1() {
		BufferedInputStream bi_citylist=null;
		BufferedOutputStream bo=null;
		int count=0;
		try {
			String url2="https://github.com/";
			URL url1=new URL(url2);
			HttpURLConnection connection=(HttpURLConnection) url1.openConnection();
			connection.setReadTimeout(1000 * 60);// 设置超时时间为60秒
			connection.setDoInput(true);// 读取数据
			connection.setRequestMethod("GET");// 设置请求方式为GET方式
			if (connection.getResponseCode() == 200) {// 请求码200表示请求成功
				bi_citylist =new BufferedInputStream(connection.getInputStream());
				bo=new BufferedOutputStream(new FileOutputStream(new File("C:\\Users\\ac\\Desktop\\github.html")));
			} else {
				// 后续可以继续完善请求失败的处理
				throw new Exception("网络请求失败！请求错误码："
						+ connection.getResponseCode());

			}
			byte[] buff=new byte[10240];
			while ((count = bi_citylist.read(buff)) != -1) {
				bo.write(buff, 0, count);
			}
			bo.close();
			bi_citylist.close();
			connection.disconnect();
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	@Test
	public void test2(){
		String tm="14摄氏度";
		String regEx="[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(tm);
		System.out.println( m.replaceAll("").trim());

	}
}
