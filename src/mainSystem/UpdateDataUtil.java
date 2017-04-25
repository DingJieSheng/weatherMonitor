/**
 * 
 */
package mainSystem;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.JDialog;
import javax.swing.JTextArea;

import com.mysql.jdbc.PreparedStatement;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author ac 盛鼎杰，孟靖宇，方远
 *
 */
public class UpdateDataUtil {
	private static JSONArray ja;
	private static JSONObject jo_cityWeather;
	private static JSONArray ja_cityWeather;
	private static String[] suggest_array;
	private static double[] pre_pm2_5_array;
	private static String[] city_array;
	private static int[] aqi_array;
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
	public static boolean updateDataUtil() throws IOException{
		try {
			flag_internet=true;
			currenttime=System.currentTimeMillis();
			DataRequest.cityListRequest(flag_internet,currenttime);
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
		if(flag_internet){//flag_internet如果通过网络访问实时数据则，天气数据也应访问网络获取实时数据，反之从本地提取历史数据
			ja=DataRequest.getJa();
			map_data=new HashMap<>();
			weather_array = new String[ja.size()];
			preWeather_array = new String[ja.size()];
			FileOutputStream fo_citylist=null;
			try {
				fo_citylist = new FileOutputStream(new File("historyData/cityWeather"+currenttime+".txt"),true);//以jsonarray的形式保存文件。一遍当做历史数据读出。
				fo_citylist.write("[".getBytes(), 0, "[".getBytes().length);
			} catch (Exception e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
			for(int i=0;i<ja.size();i++){
				JSONObject jo=ja.getJSONObject(i);
				map_data.put(jo.getString("area"), jo);
				try {
					DataRequest.cityWeatherRequest(jo.getString("area"), true,currenttime);
					updateWeatherData(i);
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				fo_citylist.write(",".getBytes(), 0, ",".getBytes().length);
			}
			try {
				fo_citylist.write("]".getBytes(), 0, "]".getBytes().length);
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}finally{
				try {
					fo_citylist.close();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
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
			city_array = new String[ja.size()];
			aqi_array = new int[ja.size()];
			co_array = new double[ja.size()];
			co_24h_array = new double[ja.size()];
			no2_array = new double[ja.size()];
			no2_24h_array = new double[ja.size()];
			o3_array = new double[ja.size()];
			o3_24h_array = new double[ja.size()];
			o3_8h_array = new double[ja.size()];
			o3_8h_24h_array = new double[ja.size()];
			pm10_array = new double[ja.size()];
			pm10_24h_array = new double[ja.size()];
			pm2_5_array = new double[ja.size()];
			pm2_5_24h_array = new double[ja.size()];
			quality_array = new String[ja.size()];
			level_array = new String[ja.size()];
			so2_array = new double[ja.size()];
			so2_24h_array = new double[ja.size()];
			primary_pollutant_array = new String[ja.size()];
			time_point_array = new String[ja.size()];
			pre_pm2_5_array=new double[ja.size()];
			suggest_array=new String[ja.size()];

			for (int i = 0; i < ja.size(); i++) {
				JSONObject jo = ja.getJSONObject(i);
				aqi_array[i] = jo.getInt("aqi");
				city_array[i] = jo.getString("area");
				co_array[i] = jo.getDouble("co");
				co_24h_array[i] = jo.getDouble("co_24h");
				no2_array[i] = jo.getDouble("no2");
				no2_24h_array[i] = jo.getDouble("no2_24h");
				o3_array[i] = jo.getDouble("o3");
				o3_24h_array[i] = jo.getDouble("o3_24h");
				o3_8h_array[i] = jo.getDouble("o3_8h");
				o3_8h_24h_array[i] = jo.getDouble("o3_8h_24h");
				pm10_array[i] = jo.getDouble("pm10");
				pm10_24h_array[i] = jo.getDouble("pm10_24h");
				pm2_5_array[i] = jo.getDouble("pm2_5");
				pm2_5_24h_array[i] = jo.getDouble("pm2_5_24h");
				quality_array[i] = jo.getString("quality");
				level_array[i] = jo.getString("level");
				so2_array[i] = jo.getDouble("so2");
				so2_24h_array[i] = jo.getDouble("so2_24h");
				primary_pollutant_array[i] = jo.getString("primary_pollutant");
				pre_pm2_5_array[i]=0;//网络获取数据时先设置pm2.5预测值为0
				suggest_array[i]="有待数据分析！";
				time_point_array[i] = jo.getString("time_point");
			}
		}
		return flag_internet;
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
				city_array[count] = resultSet.getString(resultSet.findColumn("address"));
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
	public static void updateWeatherData(int index) {
		if(flag_internet){//具体运行时此处为flag_internet
			jo_cityWeather=DataRequest.getJo();
			if(jo_cityWeather.getJSONArray("HeWeather5").getJSONObject(0).getString("status").equals("ok")&&!jo_cityWeather.getJSONArray("HeWeather5")
					.getJSONObject(0).getJSONObject("now").isEmpty()&&!jo_cityWeather.getJSONArray("HeWeather5")
					.getJSONObject(0).getJSONArray("hourly_forecast").isEmpty()){//判断状态码是否正常（正常则为有数据，否则无数据），同时判断所需数据是否为空
//				获取当前城市的实时天气情况
			try {
				weather_array[index] = jo_cityWeather.getJSONArray("HeWeather5")
						.getJSONObject(0).getJSONObject("now")
						.getJSONObject("cond").getString("txt");
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				weather_array[index]="数据获取失败！";
				e.printStackTrace();
			}
//				获取当前城市一小时候的天气预测值
			try {
				preWeather_array[index] = jo_cityWeather.getJSONArray("HeWeather5")
						.getJSONObject(0).getJSONArray("hourly_forecast")
						.getJSONObject(0).getJSONObject("cond").getString("txt");
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				preWeather_array[index]="数据获取失败！";
				e.printStackTrace();
			}
			}else{
				weather_array[index]="未查询到数据！";
				preWeather_array[index]="未查询到数据！";
			}
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
}
