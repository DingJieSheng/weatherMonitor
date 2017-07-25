package mainSystem;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JOptionPane;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.junit.Test;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
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
		Process proc=null;
		FileInputStream fi=null;
		Scanner sc=null;
		double[] weight=new double[4];
		int count=0;
		try {
			proc = Runtime.getRuntime().exec("python bp1.py");
			proc.waitFor();
			System.out.println("finished!");
//			fi=new FileInputStream(new File("weight.txt"));
//			sc=new Scanner(fi);
//			while(sc.hasNextLine()){
//				String result=sc.nextLine();
//				if(result!=null&&!result.isEmpty()){
//					weight[count]=Double.parseDouble(result);
//					count++;
//				}
//			}
//			System.out.println(weight[0]+"\n"+weight[1]+"\n"+weight[2]+"\n"+weight[3]);
//			fi.close();
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} 
	}
	
	@Test
	public void test3(){
		FileOutputStream fo=null;
		try {
			fo=new FileOutputStream("C:\\Users\\ac\\Desktop\\exportdata.txt");
			for (int i = 0; i <100; i++) {
				StringBuffer sb = new StringBuffer();
				sb.append(10);
				sb.append(" ");
				sb.append(10);
				sb.append(" ");
				sb.append(10);
				sb.append(" ");
				sb.append(10);
				sb.append("\r\n");
				sb.append(10);
				sb.append(" ");
				sb.append(10);
				sb.append(" ");
				sb.append(10);
				sb.append(" ");
				sb.append(10);
				sb.append("\r\n");
				sb.append(10);
				sb.append(" ");
				sb.append(10);
				sb.append(" ");
				sb.append(10);
				sb.append(" ");
				sb.append(10);
				sb.append("\r\n");
				fo.write(sb.toString().getBytes(), 0,
						sb.toString().getBytes().length);
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}finally{
			try {
				fo.close();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		
	}
    
	@Test
	public void test4(){
//		SendMailUtil.SendMail("1551499876@qq.com", "今天天气：雨 空气质量：优 温度：17摄氏度");
		StringBuffer mailtext=new StringBuffer();
		String receiver=null;
		int succeed_count=0;
		Connection conn=null;
		try {
			conn=(Connection) DatabaseUtil.getConn();
			PreparedStatement preContacts = (PreparedStatement) conn
					.prepareStatement("select * from weather,Contacts where weather.cityname=Contacts.location and weather.time_stamp=(select tmstamp.time_stamp from tmstamp where tmstamp.id>=all(select id from tmstamp));");
		    ResultSet rs=preContacts.executeQuery();
		    while(rs.next()){
		    	receiver=rs.getString(rs.findColumn("mailAddress"));
		    	mailtext.append(rs.getString(rs.findColumn("cityname"))+"天气：<br>");
		    	mailtext.append("当前天气："+rs.getString(rs.findColumn("weather_now"))+"<br>");
		    	mailtext.append("下一时间段天气："+rs.getString(rs.findColumn("weather_forecast"))+"<br>");
		    	mailtext.append("空气AQI值："+rs.getString(rs.findColumn("aqi"))+"<br>");
		    	mailtext.append("pm2.5:"+rs.getDouble(rs.findColumn("pm2_5"))+"<br>");
		    	mailtext.append("下一时间段pm2.5:"+rs.getDouble(rs.findColumn("prepm2_5"))+"<br>");
		    	mailtext.append(rs.getString(rs.findColumn("suggest"))+"<br>");
		    	mailtext.append(rs.getTimestamp(rs.findColumn("time_stamp"))+"<br>");
		    	System.out.println(mailtext);
		    	if(SendMailUtil.SendMail(receiver, mailtext.toString())){
		    		succeed_count++;
		    	}
		    }
		    JOptionPane.showMessageDialog(null, "成功发送"+succeed_count+"封邮件，"+(rs.getRow()+1-succeed_count)+"封邮件发送失败。");
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}finally{
			try {
				if(conn!=null&&!conn.isClosed()){
					conn.close();
				}
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}
    
	@Test
	public void test6(){
		int[]a=new int[]{1,5,3,65,41,8,45,7,0};
		Arrays.sort(a);
		System.out.println(a[0]);
	}
	
}
