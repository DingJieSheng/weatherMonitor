/**
 * 
 */
package mainSystem;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;

import javax.swing.JDialog;

import org.apache.commons.logging.Log;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 * @author ʢ���ܣ��Ͼ����Զ
 *
 */
public class DataRequest {
    private static JSONArray ja=null;
    private static JSONObject jo=null;
    private static JSONArray ja_cityWeather=null;
	private static String url="http://www.pm25.in/api/querys/aqi_ranking.json?token=5j1znBVAsnSf5xQyNQyq";
	/**
	 * @return jo
	 */
	public static JSONObject getJo() {
		return jo;
	}
	/**
	 * @param jo Ҫ���õ� jo
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
	 * @param ja Ҫ���õ� ja
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
	 * @param ja_cityWeather Ҫ���õ� ja_cityWeather
	 */
	public static void setJa_cityWeather(JSONArray ja_cityWeather) {
		DataRequest.ja_cityWeather = ja_cityWeather;
	}
	public static void cityListRequest (boolean flag_internet,long currenttime)throws Exception{
		StringBuilder stb=null;
		URL url_citylist=null;
		HttpURLConnection connection=null;
		FileWriter fw_citylist=null;
		BufferedReader br_citylist=null;
		String decode=null;
		int count=0;
		if(flag_internet){
			try {
				url_citylist = new URL(url);
				connection = (HttpURLConnection) url_citylist.openConnection();
			    connection.setReadTimeout(1000*60);// ���ó�ʱʱ��Ϊ60��
				connection.setDoInput(true);// ��ȡ����
				connection.setRequestMethod("GET");// ��������ʽΪGET��ʽ
//				�����ļ����������ظ�
				fw_citylist = new FileWriter(new File("historyData/cityList"+currenttime+".txt"));
				if (connection.getResponseCode() == 200) {// ������200��ʾ����ɹ�
					br_citylist = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				} else {
					// �������Լ�����������ʧ�ܵĴ���
					throw new Exception("��������ʧ�ܣ���������룺"
							+ connection.getResponseCode());

				}
				char[] buff = new char[1024];
				stb = new StringBuilder();
				while ((count = br_citylist.read(buff)) != -1) {
					decode = new String(buff, 0, count);
					stb.append(decode);
					fw_citylist.write(decode, 0, decode.length());
				}
				// stb.deleteCharAt(0);JSONObject��һ���ַ��ǡ�{������JOSNArray�ǡ�[����
				// stb.deleteCharAt(stb.length()-1);
				ja = JSONArray.fromObject(stb.toString());
			} catch (Exception e) {
				// TODO �Զ����ɵ� catch ��
				throw e;
			} finally {
				if (fw_citylist != null) {
					try {
						fw_citylist.close();
					} catch (IOException e) {
						// TODO �Զ����ɵ� catch ��
						e.printStackTrace();
					}
				}
				if (br_citylist != null) {
					try {
						br_citylist.close();
					} catch (IOException e) {
						// TODO �Զ����ɵ� catch ��
						e.printStackTrace();
					}
				}
			}
		}
//		else{
////			��������ʧ��Ӧ�����ݿ��ȡ����---------------------------------------------------���Ľ�----------------------------
//			br_citylist = new BufferedReader(new InputStreamReader(new FileInputStream(
//					new File("historyData/cityList7.txt"))));
//			char[] buff = new char[1024];
//			stb = new StringBuilder();
//			while ((count = br_citylist.read(buff)) != -1) {
//				decode = new String(buff, 0, count);
//				stb.append(decode);
//			}
//			br_citylist.close();
//			// stb.deleteCharAt(0);JSONObject��һ���ַ��ǡ�{������JOSNArray�ǡ�[����
//			// stb.deleteCharAt(stb.length()-1);
//			ja = JSONArray.fromObject(stb.toString());
//		}
	}
    
	public static void cityWeatherRequest(String city,boolean flag_internet,long currenttime)throws Exception{
		String url="https://free-api.heweather.com/v5/weather?city="+city+"&key=66a457a6b12a4fc3b77f06ddce362d60";
		StringBuilder stb_weather=null;
		URL url_cityWeather=null;
		HttpURLConnection connection=null;
		FileWriter fw_citylist=null;
		BufferedReader br_citylist=null;
		String decode=null;
		int count=0;
		if(flag_internet){
			try {
				url_cityWeather = new URL(url);
				connection = (HttpURLConnection) url_cityWeather.openConnection();
				connection.setReadTimeout(1000*60);// ���ó�ʱʱ��Ϊ60��
				connection.setDoInput(true);// ��ȡ����
				connection.setRequestMethod("GET");// ��������ʽΪGET��ʽ
				fw_citylist = new FileWriter(new File("historyData/cityWeather"+currenttime+".txt"),true);
				if (connection.getResponseCode() == 200) {// ������200��ʾ����ɹ�
					br_citylist = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				} else {
					// �������Լ�����������ʧ�ܵĴ���
					throw new Exception("��������ʧ�ܣ���������룺"
							+ connection.getResponseCode());
				}
				char[] buff = new char[1024];
				stb_weather = new StringBuilder();
				while ((count = br_citylist.read(buff)) != -1) {
					decode = new String(buff, 0, count);
					stb_weather.append(decode);
					fw_citylist.write(buff, 0,
							count);
				}
				// stb.deleteCharAt(0);JSONObject��һ���ַ��ǡ�{������JOSNArray�ǡ�[����
				// stb.deleteCharAt(stb.length()-1);
				jo = JSONObject.fromObject(stb_weather.toString());
			} catch (Exception e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			} finally {
				if (fw_citylist != null) {
					try {
						fw_citylist.close();
					} catch (IOException e) {
						// TODO �Զ����ɵ� catch ��
						e.printStackTrace();
					}
				}
				if (br_citylist != null) {
					try {
						br_citylist.close();
					} catch (IOException e) {
						// TODO �Զ����ɵ� catch ��
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
//			// stb.deleteCharAt(0);JSONObject��һ���ַ��ǡ�{������JOSNArray�ǡ�[����
//			// stb.deleteCharAt(stb.length()-1);
//			ja_cityWeather = JSONArray.fromObject(stb_weather.toString());
//			
//		}
	}
}
