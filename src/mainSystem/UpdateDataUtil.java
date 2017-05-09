/**
 * 
 */
package mainSystem;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JDialog;
import javax.swing.JTextArea;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.mysql.jdbc.PreparedStatement;

/**
 * @author ac 盛鼎杰，孟靖宇，方远
 *
 */
public class UpdateDataUtil {
	private static JSONArray ja;
	private static JSONObject jo_cityWeather;
	private static JSONObject jo_cityList;
	private static String[] suggest_array;
	private static double[] pre_pm2_5_array;
	private static ArrayList<String> city_list=new ArrayList<String>();
	private static ArrayList<String> belongcity_list=new ArrayList<String>();
	private static String[] city_array;
	private static int[] aqi_array;
	private static int[] temperature_array;
	private static int[] wind_array;
	private static int[] humidity_array;
	private static int[] weatherNum_array;
	private static double[] co_array;
	private static double[] co_24h_array;
	private static double[] no2_array;
	private static double[] no2_24h_array;
	private static double[] o3_array;
	private static double[] o3_24h_array;
	private static double[] o3_8h_array;
	private static double[] o3_8h_24h_array;
	private static double[] pm10_array;
	private static double[] pm10_24h_array;
	private static double[] pm2_5_array;
	private static double[] pm2_5_24h_array;
	private static String[] quality_array;
	private static String[] level_array;
	private static double[] so2_array;
	private static double[] so2_24h_array;
	private static String[] primary_pollutant_array;
	private static String[] time_point_array;
	private static String[] weather_array;
	private static String[] preWeather_array;
	private static boolean flag_internet;
	private static HashMap<String,JSONObject> map_data;
	private static Connection conn=null;
//	该变量主要用于文件名，避免文件名重复
	private static long currenttime=0;
	public static boolean updateDataUtil(boolean morecity) throws IOException{
		try {
			flag_internet=true;
			currenttime=System.currentTimeMillis();
			DataRequest.airqualityRequest(flag_internet,currenttime,morecity);
		} catch (Exception e) {
			errorDialog(e);
			// TODO 自动生成的 catch 块
			try {
				flag_internet=false;
//				DataRequest.cityListRequest(flag_internet);
				requestHistoryData();//通过数据库来获取历史数据
			} catch (Exception e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
//		if(flag_internet){//flag_internet如果通过网络访问实时数据则，天气数据也应访问网络获取实时数据，反之从本地提取历史数据
//			ja=DataRequest.getJa();
//			map_data=new HashMap<>();
//			weather_array = new String[ja.size()];
//			preWeather_array = new String[ja.size()];
//			FileOutputStream fo_citylist=null;
//			try {
//				fo_citylist = new FileOutputStream(new File("historyData/cityWeather"+currenttime+".txt"),true);//以jsonarray的形式保存文件。一遍当做历史数据读出。
//				fo_citylist.write("[".getBytes(), 0, "[".getBytes().length);
//			} catch (Exception e1) {
//				// TODO 自动生成的 catch 块
//				e1.printStackTrace();
//			}
//			for(int i=0;i<ja.size();i++){
//				JSONObject jo=ja.getJSONObject(i);
//				map_data.put(jo.getString("area"), jo);
//				try {
//					DataRequest.cityWeatherRequest(jo.getString("area"), true,currenttime);
//					updateWeatherData(i);
//				} catch (Exception e) {
//					// TODO 自动生成的 catch 块
//					e.printStackTrace();
//				}
//				fo_citylist.write(",".getBytes(), 0, ",".getBytes().length);
//			}
//			try {
//				fo_citylist.write("]".getBytes(), 0, "]".getBytes().length);
//			} catch (IOException e) {
//				// TODO 自动生成的 catch 块
//				e.printStackTrace();
//			}finally{
//				try {
//					fo_citylist.close();
//				} catch (IOException e) {
//					// TODO 自动生成的 catch 块
//					e.printStackTrace();
//				}
//			}
//		else{
//			try {
//				DataRequest.cityWeatherRequest(null, flag_internet);
//			} catch (Exception e) {
//				// TODO 自动生成的 catch 块
//				e.printStackTrace();
//			}//flag_internet控制从网络或本地获取数据
//			for(int i=0;i<ja.size();i++){//给天气以及天气预测值数组赋值
//				updateWeatherData(i);
//			}
//		}
//		}
		return flag_internet;
	}
	/**
	 * @param morecity
	 */
	public static void initArray(boolean morecity) {
		int size=0;//城市数量
		if(morecity){
			size=belongcity_list.size();
		}else{
			size=city_list.size();
		}
		city_array = new String[size];
		aqi_array = new int[size];
		co_array = new double[size];
		co_24h_array = new double[size];
		no2_array = new double[size];
		no2_24h_array = new double[size];
		o3_array = new double[size];
		o3_24h_array = new double[size];
		o3_8h_array = new double[size];
		o3_8h_24h_array = new double[size];
		pm10_array = new double[size];
		pm10_24h_array = new double[size];
		pm2_5_array = new double[size];
		pm2_5_24h_array = new double[size];
		quality_array = new String[size];
		level_array = new String[size];
		so2_array = new double[size];
		so2_24h_array = new double[size];
		primary_pollutant_array = new String[size];
		time_point_array = new String[size];
		pre_pm2_5_array=new double[size];
		suggest_array=new String[size];
		weather_array = new String[size];
		preWeather_array = new String[size];
		temperature_array =new int[size];
		wind_array =new int[size];
		weatherNum_array =new int[size];
		humidity_array =new int[size];
	}
	/**
	 * 生成各类数据的数组
	 */
	@SuppressWarnings("finally")
	public static boolean makeArray(JSONObject jo_airquality,boolean morecity,int index) {
		if (jo_airquality.getString("msg").equals("success")) {
			try {
				JSONObject jo=jo_airquality.getJSONArray("result").getJSONObject(0);
				aqi_array[index] = jo.getInt("aqi");
				city_array[index] = jo.getString("district");
				pre_pm2_5_array[index] = 0;// 网络获取数据时先设置pm2.5预测值为0
				suggest_array[index] = "有待数据分析！";
				time_point_array[index] = jo.getString("updateTime");
				quality_array[index] = jo.getString("quality");
//			co_array[index] = jo.getDouble("co");
//			co_24h_array[index] = jo.getDouble("co_24h");
				no2_array[index] = jo.getInt("no2");
//			no2_24h_array[index] = jo.getDouble("no2_24h");
//			o3_array[index] = jo.getDouble("o3");
//			o3_24h_array[index] = jo.getDouble("o3_24h");
//			o3_8h_array[index] = jo.getDouble("o3_8h");
//			o3_8h_24h_array[index] = jo.getDouble("o3_8h_24h");
//			pm10_array[index] = jo.getDouble("pm10");
//			pm10_24h_array[index] = jo.getDouble("pm10_24h");
				pm2_5_array[index] = jo.getInt("pm25");
//			pm2_5_24h_array[index] = jo.getDouble("pm2_5_24h");
//			level_array[index] = jo.getString("level");
				so2_array[index] = jo.getInt("so2");
//			so2_24h_array[index] = jo.getDouble("so2_24h");
//			primary_pollutant_array[index] = jo.getString("primary_pollutant");
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				no2_array[index]=-1;
				pm2_5_array[index]=-1;
				so2_array[index]=-1;
				e.printStackTrace();
			}finally{
				return true;
			}
		}else{
			return false;
		}
	}
//	访问历史数据
	private static void requestHistoryData() {
		PreparedStatement preparedStatement=null;
		// TODO 自动生成的方法存根
		try {
			conn=DatabaseUtil.getConn();
			//选择最新时间戳的数据作为历史数据读取
			preparedStatement = (PreparedStatement) conn
					.prepareStatement("select * from weather where weather.time_stamp=(select tmstamp.time_stamp from tmstamp where tmstamp.id>=all(select id from tmstamp));");
		    ResultSet resultSet=preparedStatement.executeQuery();
		    resultSet.last(); 
		    int size= resultSet.getRow(); 
		    resultSet.beforeFirst();
		    int count=0;
		    aqi_array=new int[size];
			city_array=new String[size];
			pm2_5_array=new double[size];
			pm2_5_24h_array=new double[size];
			time_point_array=new String[size];
			suggest_array=new String[size];
			pre_pm2_5_array=new double[size];
			weather_array=new String[size];
			preWeather_array=new String[size];
		    while(resultSet.next()){
		    	aqi_array[count]=resultSet.getInt(resultSet.findColumn("aqi"));
				city_array[count] = resultSet.getString(resultSet.findColumn("cityname"));
				pm2_5_array[count]=resultSet.getDouble(resultSet.findColumn("pm2_5"));
				time_point_array[count]=resultSet.getString(resultSet.findColumn("time_stamp"));
				suggest_array[count]=resultSet.getString(resultSet.findColumn("suggest"));
				pre_pm2_5_array[count]=resultSet.getDouble(resultSet.findColumn("prepm2_5"));
				weather_array[count]=resultSet.getString(resultSet.findColumn("weather_now"));
				preWeather_array[count]=resultSet.getString(resultSet.findColumn("weather_forecast"));
				count++;
		    }
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			errorDialog(e);
			e.printStackTrace();
		}
		
	}
	/**
	 * @param e
	 */
	public static void errorDialog(Exception e) {
		JDialog errordialog=new JDialog();
		Container errorcon=errordialog.getContentPane();
		JTextArea errorjt=new JTextArea(e.getMessage());
		errorjt.setEditable(false);
		errorjt.setLineWrap(true);
		errorjt.setWrapStyleWord(true);
		errorcon.add(errorjt, BorderLayout.CENTER);
		errordialog.setSize(300, 200);
		errordialog.setTitle("错误信息");
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screensize = kit.getScreenSize();
		int screenwidth = screensize.width;
		int screenhight = screensize.height;
		int windowswidth = errorjt.getWidth();
		int windowshight = errorjt.getHeight();
		errordialog.setLocation((screenwidth - windowswidth) / 2,
				(screenhight - windowshight) / 2);
		errordialog.setVisible(true);
	}
	/**
	 * @param index
	 */
	public static void updateWeatherData(JSONObject jo_weather,int index) {
		String regEx="[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m =null;
		int weatherNum=0;
		if(flag_internet){//具体运行时此处为flag_internet
			if(jo_weather.getString("msg").equals("success")){//判断状态码是否正常（正常则为有数据，否则无数据），同时判断所需数据是否为空
//				获取当前城市的实时天气情况
				try {
					weather_array[index] = jo_weather.getJSONArray("result")
							.getJSONObject(0).getString("weather");
					String weathercase = jo_weather.getJSONArray("result")
							.getJSONObject(0).getString("weather");
					if (weathercase.matches(".*雨")) {
						weatherNum = 0;
					} else if (weathercase.matches(".*雪")) {
						weatherNum = 1;
					} else if (weathercase.matches(".*云")) {
						weatherNum = 2;
					} else if (weathercase.matches("晴")) {
						weatherNum = 3;
					} else if (weathercase.matches("阴")) {
						weatherNum = 4;
					} else if(weathercase.matches("雾")){
						weatherNum = 5;
					}else if (weathercase.matches("霾")) {
						weatherNum = 6;
					} else if (weathercase.matches("浮尘")){
						weatherNum = 7;
					} else{
						weatherNum = -1;
					}
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					weather_array[index] = "数据获取失败！";
					weatherNum = -1;
					e.printStackTrace();
				}
				try {
					m = p.matcher(jo_weather.getJSONArray("result")
							.getJSONObject(0).getString("temperature"));
					temperature_array[index] = Integer.parseInt(m
							.replaceAll("").trim());
				} catch (NumberFormatException e1) {
					// TODO 自动生成的 catch 块
					temperature_array[index] = -1;
					e1.printStackTrace();
				}
				try {
					m = p.matcher(jo_weather.getJSONArray("result")
							.getJSONObject(0).getString("humidity"));
					humidity_array[index] = Integer.parseInt(m.replaceAll("")
							.trim());
				} catch (NumberFormatException e1) {
					// TODO 自动生成的 catch 块
					humidity_array[index] = -1;
					e1.printStackTrace();
				}
				try {
					m = p.matcher(jo_weather.getJSONArray("result")
							.getJSONObject(0).getString("wind"));
					wind_array[index] = Integer.parseInt(m.replaceAll("")
							.trim());
				} catch (NumberFormatException e1) {
					// TODO 自动生成的 catch 块
					wind_array[index] = -1;
					e1.printStackTrace();
				}
				// 获取当前城市一小时候的天气预测值
				try {
					preWeather_array[index] = jo_weather.getJSONArray("result")
							.getJSONObject(0).getJSONArray("future")
							.getJSONObject(0).getString("dayTime");
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					preWeather_array[index] = "数据获取失败！";
					e.printStackTrace();
				}
				suggest_array[index] = "着装建议："
						+ jo_weather.getJSONArray("result").getJSONObject(0)
								.getString("dressingIndex")
						+ "\n运动建议："
						+ jo_weather.getJSONArray("result").getJSONObject(0)
								.getString("exerciseIndex")
						+ "\n洗刷建议："
						+ jo_weather.getJSONArray("result").getJSONObject(0)
								.getString("washIndex");
			}else{
				weather_array[index]="未查询到数据！";
				preWeather_array[index]="未查询到数据！";
				suggest_array[index]="有待数据分析";
				weatherNum=-1;
				temperature_array[index] = -1;
				humidity_array[index] = -1;
				wind_array[index] = -1;
			}
			weatherNum_array[index]=weatherNum;
		}//如果不是访问网络则从数据库取数据该else语句不需要
//		else{
//			ja_cityWeather=DataRequest.getJa_cityWeather();
//			if(ja_cityWeather.getJSONObject(index).getJSONArray("HeWeather5").getJSONObject(0).getString("status").equals("ok")&&
//					!ja_cityWeather.getJSONObject(index).getJSONArray("HeWeather5").getJSONObject(0).getString("now").isEmpty()&&
//					!ja_cityWeather.getJSONObject(index).getJSONArray("HeWeather5").getJSONObject(0).getString("hourly_forecast").isEmpty()){
////				获取当前城市的实时天气情况
//			weather_array[index] = ja_cityWeather.getJSONObject(index).getJSONArray("HeWeather5")
//					.getJSONObject(0).getJSONObject("now")
//					.getJSONObject("cond").getString("txt");
////				获取当前城市一小时候的天气预测值
//			preWeather_array[index] =ja_cityWeather.getJSONObject(index).getJSONArray("HeWeather5")
//					.getJSONObject(0).getJSONArray("hourly_forecast")
//					.getJSONObject(0).getJSONObject("cond").getString("txt");
//			}else{
//				weather_array[index]="未查询到数据！";
//				preWeather_array[index]="未查询到数据！";
//			}
//		}
	}
//	更新城市列表
	/**
	 * @param more false代表更新到地级市城市列表，true代表更详细的县级市
	 * @throws Exception 
	 */
    public static void updateCityList(boolean morecity) throws Exception{
    	boolean flag_success=true;
    	try {
			DataRequest.CityList();
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			flag_success=false;
			errorDialog(e);
			e.printStackTrace();
			throw e;
		}
    	if (flag_success) {
			jo_cityList = DataRequest.getJo_cityList();
			if (jo_cityList.getString("msg").equals("success")) {
				ja = jo_cityList.getJSONArray("result");
				for (int i = 0; i < ja.size(); i++) {
					JSONObject jocity = ja.getJSONObject(i);
					JSONArray jacity = jocity.getJSONArray("city");
					for (int j = 0; j < jacity.size(); j++) {
						JSONObject jo1 = jacity.getJSONObject(j);
						JSONArray ja1 = null;
						city_list.add(jo1.getString("city"));
						belongcity_list.add(jo1.getString("city"));
						if (morecity) {//详细城市列表
							try {
								ja1 = jo1.getJSONArray("district");
								for (int k = 1; k < ja1.size(); k++) {
									city_list.add(ja1.getJSONObject(k)
											.getString("district"));
									belongcity_list.add(jo1.getString("city"));
								}
							} catch (Exception e) {
								// TODO 自动生成的 catch 块
								e.printStackTrace();
							}
						}
					}
				}
			}
    	}
    }
    public static void historyCitylist(boolean morecity)throws Exception{
		conn = DatabaseUtil.getConn();
		PreparedStatement pre = null;
		if (!morecity) {
			pre = (PreparedStatement) conn
					.prepareStatement("select cityname from citylist;");
		} else {
			pre = (PreparedStatement) conn
					.prepareStatement("select cityname,belongcity from morecitylist;");
		}
		ResultSet rs = pre.executeQuery();
		while (rs.next()) {
			city_list.add(rs.getString(rs.findColumn("cityname")));
			if (morecity) {
				belongcity_list.add(rs.getString(rs.findColumn("belongcity")));
			}
		}
    }
	/**
	 * @return city_array
	 */
	public static String[] getCity_array() {
		return city_array;
	}
	/**
	 * @param city_array 要设置的 city_array
	 */
	public static void setCity_array(String[] city_array) {
		UpdateDataUtil.city_array = city_array;
	}
	/**
	 * @return aqi_array
	 */
	public static int[] getAqi_array() {
		return aqi_array;
	}
	/**
	 * @param aqi_array 要设置的 aqi_array
	 */
	public static void setAqi_array(int[] aqi_array) {
		UpdateDataUtil.aqi_array = aqi_array;
	}
	/**
	 * @return co_array
	 */
	public static double[] getCo_array() {
		return co_array;
	}
	/**
	 * @param co_array 要设置的 co_array
	 */
	public static void setCo_array(double[] co_array) {
		UpdateDataUtil.co_array = co_array;
	}
	/**
	 * @return co_24h_array
	 */
	public static double[] getCo_24h_array() {
		return co_24h_array;
	}
	/**
	 * @param co_24h_array 要设置的 co_24h_array
	 */
	public static void setCo_24h_array(double[] co_24h_array) {
		UpdateDataUtil.co_24h_array = co_24h_array;
	}
	/**
	 * @return no2_array
	 */
	public static double[] getNo2_array() {
		return no2_array;
	}
	/**
	 * @param no2_array 要设置的 no2_array
	 */
	public static void setNo2_array(double[] no2_array) {
		UpdateDataUtil.no2_array = no2_array;
	}
	/**
	 * @return no2_24h_array
	 */
	public static double[] getNo2_24h_array() {
		return no2_24h_array;
	}
	/**
	 * @param no2_24h_array 要设置的 no2_24h_array
	 */
	public static void setNo2_24h_array(double[] no2_24h_array) {
		UpdateDataUtil.no2_24h_array = no2_24h_array;
	}
	/**
	 * @return o3_array
	 */
	public static double[] getO3_array() {
		return o3_array;
	}
	/**
	 * @param o3_array 要设置的 o3_array
	 */
	public static void setO3_array(double[] o3_array) {
		UpdateDataUtil.o3_array = o3_array;
	}
	/**
	 * @return o3_24h_array
	 */
	public static double[] getO3_24h_array() {
		return o3_24h_array;
	}
	/**
	 * @param o3_24h_array 要设置的 o3_24h_array
	 */
	public static void setO3_24h_array(double[] o3_24h_array) {
		UpdateDataUtil.o3_24h_array = o3_24h_array;
	}
	/**
	 * @return o3_8h_array
	 */
	public static double[] getO3_8h_array() {
		return o3_8h_array;
	}
	/**
	 * @param o3_8h_array 要设置的 o3_8h_array
	 */
	public static void setO3_8h_array(double[] o3_8h_array) {
		UpdateDataUtil.o3_8h_array = o3_8h_array;
	}
	/**
	 * @return o3_8h_24h_array
	 */
	public static double[] getO3_8h_24h_array() {
		return o3_8h_24h_array;
	}
	/**
	 * @param o3_8h_24h_array 要设置的 o3_8h_24h_array
	 */
	public static void setO3_8h_24h_array(double[] o3_8h_24h_array) {
		UpdateDataUtil.o3_8h_24h_array = o3_8h_24h_array;
	}
	/**
	 * @return pm10_array
	 */
	public static double[] getPm10_array() {
		return pm10_array;
	}
	/**
	 * @param pm10_array 要设置的 pm10_array
	 */
	public static void setPm10_array(double[] pm10_array) {
		UpdateDataUtil.pm10_array = pm10_array;
	}
	/**
	 * @return pm10_24h_array
	 */
	public static double[] getPm10_24h_array() {
		return pm10_24h_array;
	}
	/**
	 * @param pm10_24h_array 要设置的 pm10_24h_array
	 */
	public static void setPm10_24h_array(double[] pm10_24h_array) {
		UpdateDataUtil.pm10_24h_array = pm10_24h_array;
	}
	/**
	 * @return pm2_5_array
	 */
	public static double[] getPm2_5_array() {
		return pm2_5_array;
	}
	/**
	 * @param pm2_5_array 要设置的 pm2_5_array
	 */
	public static void setPm2_5_array(double[] pm2_5_array) {
		UpdateDataUtil.pm2_5_array = pm2_5_array;
	}
	/**
	 * @return pm2_5_24h_array
	 */
	public static double[] getPm2_5_24h_array() {
		return pm2_5_24h_array;
	}
	/**
	 * @param pm2_5_24h_array 要设置的 pm2_5_24h_array
	 */
	public static void setPm2_5_24h_array(double[] pm2_5_24h_array) {
		UpdateDataUtil.pm2_5_24h_array = pm2_5_24h_array;
	}
	/**
	 * @return quality_array
	 */
	public static String[] getQuality_array() {
		return quality_array;
	}
	/**
	 * @param quality_array 要设置的 quality_array
	 */
	public static void setQuality_array(String[] quality_array) {
		UpdateDataUtil.quality_array = quality_array;
	}
	/**
	 * @return level_array
	 */
	public static String[] getLevel_array() {
		return level_array;
	}
	/**
	 * @param level_array 要设置的 level_array
	 */
	public static void setLevel_array(String[] level_array) {
		UpdateDataUtil.level_array = level_array;
	}
	/**
	 * @return so2_array
	 */
	public static double[] getSo2_array() {
		return so2_array;
	}
	/**
	 * @param so2_array 要设置的 so2_array
	 */
	public static void setSo2_array(double[] so2_array) {
		UpdateDataUtil.so2_array = so2_array;
	}
	/**
	 * @return so2_24h_array
	 */
	public static double[] getSo2_24h_array() {
		return so2_24h_array;
	}
	/**
	 * @param so2_24h_array 要设置的 so2_24h_array
	 */
	public static void setSo2_24h_array(double[] so2_24h_array) {
		UpdateDataUtil.so2_24h_array = so2_24h_array;
	}
	/**
	 * @return primary_pollutant_array
	 */
	public static String[] getPrimary_pollutant_array() {
		return primary_pollutant_array;
	}
	/**
	 * @param primary_pollutant_array 要设置的 primary_pollutant_array
	 */
	public static void setPrimary_pollutant_array(String[] primary_pollutant_array) {
		UpdateDataUtil.primary_pollutant_array = primary_pollutant_array;
	}
	/**
	 * @return time_point_array
	 */
	public static String[] getTime_point_array() {
		return time_point_array;
	}
	/**
	 * @param time_point_array 要设置的 time_point_array
	 */
	public static void setTime_point_array(String[] time_point_array) {
		UpdateDataUtil.time_point_array = time_point_array;
	}
	/**
	 * @return weather_array
	 */
	public static String[] getWeather_array() {
		return weather_array;
	}
	/**
	 * @param weather_array 要设置的 weather_array
	 */
	public static void setWeather_array(String[] weather_array) {
		UpdateDataUtil.weather_array = weather_array;
	}
	/**
	 * @return preWeather_array
	 */
	public static String[] getPreWeather_array() {
		return preWeather_array;
	}
	/**
	 * @param preWeather_array 要设置的 preWeather_array
	 */
	public static void setPreWeather_array(String[] preWeather_array) {
		UpdateDataUtil.preWeather_array = preWeather_array;
	}
	/**
	 * @return suggest_array
	 */
	public static String[] getSuggest_array() {
		return suggest_array;
	}
	/**
	 * @param suggest_array 要设置的 suggest_array
	 */
	public static void setSuggest_array(String[] suggest_array) {
		UpdateDataUtil.suggest_array = suggest_array;
	}
	/**
	 * @return pre_pm2_5_array
	 */
	public static double[] getPre_pm2_5_array() {
		return pre_pm2_5_array;
	}
	/**
	 * @param pre_pm2_5_array 要设置的 pre_pm2_5_array
	 */
	public static void setPre_pm2_5_array(double[] pre_pm2_5_array) {
		UpdateDataUtil.pre_pm2_5_array = pre_pm2_5_array;
	}
	/**
	 * @return city_list
	 */
	public static ArrayList<String> getCity_list() {
		return city_list;
	}
	/**
	 * @param city_list 要设置的 city_list
	 */
	public static void setCity_list(ArrayList<String> city_list) {
		UpdateDataUtil.city_list = city_list;
	}
	/**
	 * @return belongcity_list
	 */
	public static ArrayList<String> getBelongcity_list() {
		return belongcity_list;
	}
	/**
	 * @param belongcity_list 要设置的 belongcity_list
	 */
	public static void setBelongcity_list(ArrayList<String> belongcity_list) {
		UpdateDataUtil.belongcity_list = belongcity_list;
	}
	/**
	 * @return temperature_array
	 */
	public static int[] getTemperature_array() {
		return temperature_array;
	}
	/**
	 * @param temperature_array 要设置的 temperature_array
	 */
	public static void setTemperature_array(int[] temperature_array) {
		UpdateDataUtil.temperature_array = temperature_array;
	}
	/**
	 * @return wind_array
	 */
	public static int[] getWind_array() {
		return wind_array;
	}
	/**
	 * @param wind_array 要设置的 wind_array
	 */
	public static void setWind_array(int[] wind_array) {
		UpdateDataUtil.wind_array = wind_array;
	}
	/**
	 * @return humidity_array
	 */
	public static int[] getHumidity_array() {
		return humidity_array;
	}
	/**
	 * @param humidity_array 要设置的 humidity_array
	 */
	public static void setHumidity_array(int[] humidity_array) {
		UpdateDataUtil.humidity_array = humidity_array;
	}
	/**
	 * @return weatherNum_array
	 */
	public static int[] getWeatherNum_array() {
		return weatherNum_array;
	}
	/**
	 * @param weatherNum_array 要设置的 weatherNum_array
	 */
	public static void setWeatherNum_array(int[] weatherNum_array) {
		UpdateDataUtil.weatherNum_array = weatherNum_array;
	}
	
}
