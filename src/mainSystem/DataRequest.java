/**
 * 
 */
package mainSystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import com.mysql.fabric.xmlrpc.base.Array;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author 盛鼎杰，孟靖宇，方远
 *
 */
public class DataRequest {

	private static JSONArray ja=null;
    private static JSONObject jo=null;
    private static JSONObject jo_cityList=null;
    private static JSONArray ja_cityWeather=null;
    private static ArrayList<String> city_list=null;
//	private static String url="http://www.pm25.in/api/querys/aqi_ranking.json?token=5j1znBVAsnSf5xQyNQyq";
	private static String url_airquality="http://apicloud.mob.com/environment/query?key=1d42eae83af38&city=";
	private static String url_weather="http://apicloud.mob.com/v1/weather/query?key=1d42eae83af38&city=";
	/**
	 * @return jo
	 */
	public static JSONObject getJo() {
		return jo;
	}
	/**
	 * @param jo 要设置的 jo
	 */
	public static void setJo(JSONObject jo) {
		DataRequest.jo = jo;
	}
	/**
	 * @return ja
	 */
	public static JSONArray getJa() {
		return ja;
	}
	/**
	 * @param ja 要设置的 ja
	 */
	public static void setJa(JSONArray ja) {
		DataRequest.ja = ja;
	}
	/**
	 * @return ja_cityWeather
	 */
	public static JSONArray getJa_cityWeather() {
		return ja_cityWeather;
	}
    /**
	 * @return jo_cityList
	 */
	public static JSONObject getJo_cityList() {
		return jo_cityList;
	}
	/**
	 * @param jo_cityList 要设置的 jo_cityList
	 */
	public static void setJo_cityList(JSONObject jo_cityList) {
		DataRequest.jo_cityList = jo_cityList;
	}
	/**
	 * @param ja_cityWeather 要设置的 ja_cityWeather
	 */
	public static void setJa_cityWeather(JSONArray ja_cityWeather) {
		DataRequest.ja_cityWeather = ja_cityWeather;
	}
	public static void airqualityRequest (boolean flag_internet,long currenttime,boolean morecity)throws Exception{
		StringBuilder stb=null;
		URL url_citylist=null;
		HttpURLConnection connection=null;
		FileWriter fw_citylist=null;
		BufferedReader br_citylist=null;
		String decode=null;
		int count=0;
		int index=0;
		city_list=UpdateDataUtil.getCity_list();
		UpdateDataUtil.initArray(morecity);
		if(flag_internet){
			if (city_list!=null&&!city_list.isEmpty()) {
				Iterator<String> it_city=city_list.iterator();
//				fw_citylist = new FileWriter(new File(
//						"historyData/cityList" + currenttime + ".txt"),true);
				while (it_city.hasNext()) {
					try {
						String cityname=it_city.next();
//						特别注意这里需要URL编码，因为城市名是中文！！！！！！
						String urlencode=URLEncoder.encode(cityname,"utf-8");
						url_citylist = new URL(url_airquality+urlencode);
						connection = (HttpURLConnection) url_citylist
								.openConnection();
						connection.setReadTimeout(1000 * 60);// 设置超时时间为60秒
						connection.setDoInput(true);// 读取数据
						connection.setRequestMethod("GET");// 设置请求方式为GET方式
						//				当做文件名，避免重复
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
						stb = new StringBuilder();
						while ((count = br_citylist.read(buff)) != -1) {
							decode = new String(buff, 0, count);
							stb.append(decode);
//							fw_citylist.write(decode, 0, decode.length());
						}
						// stb.deleteCharAt(0);JSONObject第一个字符是”{“，而JOSNArray是”[”。
						// stb.deleteCharAt(stb.length()-1);
						jo = JSONObject.fromObject(stb.toString());
						boolean flag_success=UpdateDataUtil.makeArray(jo,morecity,index);//城市下标
						cityWeatherRequest(cityname,flag_internet, currenttime,index);
						if(flag_success){
							index++;
						}
					} catch (Exception e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
						throw e;
					}
				}
//				if (fw_citylist!=null&&br_citylist!=null) {
//					fw_citylist.close();
//					br_citylist.close();
//				}
				connection.disconnect();
			}else{
			throw new Exception("网络获取失败！");
			}
		}
//		else{
////			访问网络失败应从数据库读取数据---------------------------------------------------待改进----------------------------
//			br_citylist = new BufferedReader(new InputStreamReader(new FileInputStream(
//					new File("historyData/cityList7.txt"))));
//			char[] buff = new char[1024];
//			stb = new StringBuilder();
//			while ((count = br_citylist.read(buff)) != -1) {
//				decode = new String(buff, 0, count);
//				stb.append(decode);
//			}
//			br_citylist.close();
//			// stb.deleteCharAt(0);JSONObject第一个字符是”{“，而JOSNArray是”[”。
//			// stb.deleteCharAt(stb.length()-1);
//			ja = JSONArray.fromObject(stb.toString());
//		}
	}
    
	public static void cityWeatherRequest(String cityname,boolean flag_internet,long currenttime,int index)throws Exception{
		StringBuilder stb_weather=null;
		URL url_cityWeather=null;
		HttpURLConnection connection=null;
		FileWriter fw_citylist=null;
		BufferedReader br_citylist=null;
		String decode=null;
		int count=0;
		if(flag_internet){
			try {
				String urlencode=URLEncoder.encode(cityname,"utf-8");
				url_cityWeather = new URL(url_weather+urlencode);
				connection = (HttpURLConnection) url_cityWeather.openConnection();
				connection.setReadTimeout(1000*60);// 设置超时时间为60秒
				connection.setDoInput(true);// 读取数据
				connection.setRequestMethod("GET");// 设置请求方式为GET方式
//				fw_citylist = new FileWriter(new File("historyData/cityWeather"+currenttime+".txt"),true);
				if (connection.getResponseCode() == 200) {// 请求码200表示请求成功
					br_citylist = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				} else {
					// 后续可以继续完善请求失败的处理
					throw new Exception("网络请求失败！请求错误码："
							+ connection.getResponseCode());
				}
				char[] buff = new char[1024];
				stb_weather = new StringBuilder();
				while ((count = br_citylist.read(buff)) != -1) {
					decode = new String(buff, 0, count);
					stb_weather.append(decode);
//					fw_citylist.write(buff, 0,
//							count);
				}
				// stb.deleteCharAt(0);JSONObject第一个字符是”{“，而JOSNArray是”[”。
				// stb.deleteCharAt(stb.length()-1);
				jo = JSONObject.fromObject(stb_weather.toString());
				UpdateDataUtil.updateWeatherData(jo,index);
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} finally {
//				if (fw_citylist != null) {
//					try {
//						fw_citylist.close();
//					} catch (IOException e) {
//						// TODO 自动生成的 catch 块
//						e.printStackTrace();
//					}
//				}
				if (br_citylist != null) {
					try {
						br_citylist.close();
					} catch (IOException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
				}
			}
		}
//		else{
//			br_citylist = new BufferedReader(new InputStreamReader(new FileInputStream(
//					new File("historyData/cityWeather.txt"))));
//			char[] buff = new char[1024];
//			stb_weather = new StringBuilder();
//			while ((count = br_citylist.read(buff)) != -1) {
//				decode = new String(buff, 0, count);
//				stb_weather.append(decode);
//			}
//			br_citylist.close();
//			// stb.deleteCharAt(0);JSONObject第一个字符是”{“，而JOSNArray是”[”。
//			// stb.deleteCharAt(stb.length()-1);
//			ja_cityWeather = JSONArray.fromObject(stb_weather.toString());
//			
//		}
	}

	public static void CityList() throws Exception{
		URL cityListUrl=new URL("http://apicloud.mob.com/v1/weather/citys?key=1d42eae83af38");
		HttpURLConnection connection=(HttpURLConnection) cityListUrl.openConnection();
		connection.setReadTimeout(60*1000);//60秒超时
		connection.setDoInput(true);// 读取数据
		connection.setRequestMethod("GET");// 设置请求方式为GET方式
		StringBuffer stb_citylist=new StringBuffer();
		BufferedReader br=null;
		char buff[]=new char[10240];
		int count=0;
		if(connection.getResponseCode()==200){
			br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while((count=br.read(buff))!=-1){
				stb_citylist.append(buff,0,count);
			}
			br.close();
			connection.disconnect();
			jo_cityList=JSONObject.fromObject(stb_citylist.toString());
		}else{
			throw new Exception("网络访问失败！");
		}
	}
}
