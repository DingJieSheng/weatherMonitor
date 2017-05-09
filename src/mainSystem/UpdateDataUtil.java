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
 * @author ac ʢ���ܣ��Ͼ����Զ
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
//	�ñ�����Ҫ�����ļ����������ļ����ظ�
	private static long currenttime=0;
	public static boolean updateDataUtil(boolean morecity) throws IOException{
		try {
			flag_internet=true;
			currenttime=System.currentTimeMillis();
			DataRequest.airqualityRequest(flag_internet,currenttime,morecity);
		} catch (Exception e) {
			errorDialog(e);
			// TODO �Զ����ɵ� catch ��
			try {
				flag_internet=false;
//				DataRequest.cityListRequest(flag_internet);
				requestHistoryData();//ͨ�����ݿ�����ȡ��ʷ����
			} catch (Exception e1) {
				// TODO �Զ����ɵ� catch ��
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
//		if(flag_internet){//flag_internet���ͨ���������ʵʱ��������������ҲӦ���������ȡʵʱ���ݣ���֮�ӱ�����ȡ��ʷ����
//			ja=DataRequest.getJa();
//			map_data=new HashMap<>();
//			weather_array = new String[ja.size()];
//			preWeather_array = new String[ja.size()];
//			FileOutputStream fo_citylist=null;
//			try {
//				fo_citylist = new FileOutputStream(new File("historyData/cityWeather"+currenttime+".txt"),true);//��jsonarray����ʽ�����ļ���һ�鵱����ʷ���ݶ�����
//				fo_citylist.write("[".getBytes(), 0, "[".getBytes().length);
//			} catch (Exception e1) {
//				// TODO �Զ����ɵ� catch ��
//				e1.printStackTrace();
//			}
//			for(int i=0;i<ja.size();i++){
//				JSONObject jo=ja.getJSONObject(i);
//				map_data.put(jo.getString("area"), jo);
//				try {
//					DataRequest.cityWeatherRequest(jo.getString("area"), true,currenttime);
//					updateWeatherData(i);
//				} catch (Exception e) {
//					// TODO �Զ����ɵ� catch ��
//					e.printStackTrace();
//				}
//				fo_citylist.write(",".getBytes(), 0, ",".getBytes().length);
//			}
//			try {
//				fo_citylist.write("]".getBytes(), 0, "]".getBytes().length);
//			} catch (IOException e) {
//				// TODO �Զ����ɵ� catch ��
//				e.printStackTrace();
//			}finally{
//				try {
//					fo_citylist.close();
//				} catch (IOException e) {
//					// TODO �Զ����ɵ� catch ��
//					e.printStackTrace();
//				}
//			}
//		else{
//			try {
//				DataRequest.cityWeatherRequest(null, flag_internet);
//			} catch (Exception e) {
//				// TODO �Զ����ɵ� catch ��
//				e.printStackTrace();
//			}//flag_internet���ƴ�����򱾵ػ�ȡ����
//			for(int i=0;i<ja.size();i++){//�������Լ�����Ԥ��ֵ���鸳ֵ
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
		int size=0;//��������
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
	 * ���ɸ������ݵ�����
	 */
	@SuppressWarnings("finally")
	public static boolean makeArray(JSONObject jo_airquality,boolean morecity,int index) {
		if (jo_airquality.getString("msg").equals("success")) {
			try {
				JSONObject jo=jo_airquality.getJSONArray("result").getJSONObject(0);
				aqi_array[index] = jo.getInt("aqi");
				city_array[index] = jo.getString("district");
				pre_pm2_5_array[index] = 0;// �����ȡ����ʱ������pm2.5Ԥ��ֵΪ0
				suggest_array[index] = "�д����ݷ�����";
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
				// TODO �Զ����ɵ� catch ��
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
//	������ʷ����
	private static void requestHistoryData() {
		PreparedStatement preparedStatement=null;
		// TODO �Զ����ɵķ������
		try {
			conn=DatabaseUtil.getConn();
			//ѡ������ʱ�����������Ϊ��ʷ���ݶ�ȡ
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
			// TODO �Զ����ɵ� catch ��
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
		errordialog.setTitle("������Ϣ");
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
		if(flag_internet){//��������ʱ�˴�Ϊflag_internet
			if(jo_weather.getString("msg").equals("success")){//�ж�״̬���Ƿ�������������Ϊ�����ݣ����������ݣ���ͬʱ�ж����������Ƿ�Ϊ��
//				��ȡ��ǰ���е�ʵʱ�������
				try {
					weather_array[index] = jo_weather.getJSONArray("result")
							.getJSONObject(0).getString("weather");
					String weathercase = jo_weather.getJSONArray("result")
							.getJSONObject(0).getString("weather");
					if (weathercase.matches(".*��")) {
						weatherNum = 0;
					} else if (weathercase.matches(".*ѩ")) {
						weatherNum = 1;
					} else if (weathercase.matches(".*��")) {
						weatherNum = 2;
					} else if (weathercase.matches("��")) {
						weatherNum = 3;
					} else if (weathercase.matches("��")) {
						weatherNum = 4;
					} else if(weathercase.matches("��")){
						weatherNum = 5;
					}else if (weathercase.matches("��")) {
						weatherNum = 6;
					} else if (weathercase.matches("����")){
						weatherNum = 7;
					} else{
						weatherNum = -1;
					}
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					weather_array[index] = "���ݻ�ȡʧ�ܣ�";
					weatherNum = -1;
					e.printStackTrace();
				}
				try {
					m = p.matcher(jo_weather.getJSONArray("result")
							.getJSONObject(0).getString("temperature"));
					temperature_array[index] = Integer.parseInt(m
							.replaceAll("").trim());
				} catch (NumberFormatException e1) {
					// TODO �Զ����ɵ� catch ��
					temperature_array[index] = -1;
					e1.printStackTrace();
				}
				try {
					m = p.matcher(jo_weather.getJSONArray("result")
							.getJSONObject(0).getString("humidity"));
					humidity_array[index] = Integer.parseInt(m.replaceAll("")
							.trim());
				} catch (NumberFormatException e1) {
					// TODO �Զ����ɵ� catch ��
					humidity_array[index] = -1;
					e1.printStackTrace();
				}
				try {
					m = p.matcher(jo_weather.getJSONArray("result")
							.getJSONObject(0).getString("wind"));
					wind_array[index] = Integer.parseInt(m.replaceAll("")
							.trim());
				} catch (NumberFormatException e1) {
					// TODO �Զ����ɵ� catch ��
					wind_array[index] = -1;
					e1.printStackTrace();
				}
				// ��ȡ��ǰ����һСʱ�������Ԥ��ֵ
				try {
					preWeather_array[index] = jo_weather.getJSONArray("result")
							.getJSONObject(0).getJSONArray("future")
							.getJSONObject(0).getString("dayTime");
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					preWeather_array[index] = "���ݻ�ȡʧ�ܣ�";
					e.printStackTrace();
				}
				suggest_array[index] = "��װ���飺"
						+ jo_weather.getJSONArray("result").getJSONObject(0)
								.getString("dressingIndex")
						+ "\n�˶����飺"
						+ jo_weather.getJSONArray("result").getJSONObject(0)
								.getString("exerciseIndex")
						+ "\nϴˢ���飺"
						+ jo_weather.getJSONArray("result").getJSONObject(0)
								.getString("washIndex");
			}else{
				weather_array[index]="δ��ѯ�����ݣ�";
				preWeather_array[index]="δ��ѯ�����ݣ�";
				suggest_array[index]="�д����ݷ���";
				weatherNum=-1;
				temperature_array[index] = -1;
				humidity_array[index] = -1;
				wind_array[index] = -1;
			}
			weatherNum_array[index]=weatherNum;
		}//������Ƿ�������������ݿ�ȡ���ݸ�else��䲻��Ҫ
//		else{
//			ja_cityWeather=DataRequest.getJa_cityWeather();
//			if(ja_cityWeather.getJSONObject(index).getJSONArray("HeWeather5").getJSONObject(0).getString("status").equals("ok")&&
//					!ja_cityWeather.getJSONObject(index).getJSONArray("HeWeather5").getJSONObject(0).getString("now").isEmpty()&&
//					!ja_cityWeather.getJSONObject(index).getJSONArray("HeWeather5").getJSONObject(0).getString("hourly_forecast").isEmpty()){
////				��ȡ��ǰ���е�ʵʱ�������
//			weather_array[index] = ja_cityWeather.getJSONObject(index).getJSONArray("HeWeather5")
//					.getJSONObject(0).getJSONObject("now")
//					.getJSONObject("cond").getString("txt");
////				��ȡ��ǰ����һСʱ�������Ԥ��ֵ
//			preWeather_array[index] =ja_cityWeather.getJSONObject(index).getJSONArray("HeWeather5")
//					.getJSONObject(0).getJSONArray("hourly_forecast")
//					.getJSONObject(0).getJSONObject("cond").getString("txt");
//			}else{
//				weather_array[index]="δ��ѯ�����ݣ�";
//				preWeather_array[index]="δ��ѯ�����ݣ�";
//			}
//		}
	}
//	���³����б�
	/**
	 * @param more false������µ��ؼ��г����б�true�������ϸ���ؼ���
	 * @throws Exception 
	 */
    public static void updateCityList(boolean morecity) throws Exception{
    	boolean flag_success=true;
    	try {
			DataRequest.CityList();
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
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
						if (morecity) {//��ϸ�����б�
							try {
								ja1 = jo1.getJSONArray("district");
								for (int k = 1; k < ja1.size(); k++) {
									city_list.add(ja1.getJSONObject(k)
											.getString("district"));
									belongcity_list.add(jo1.getString("city"));
								}
							} catch (Exception e) {
								// TODO �Զ����ɵ� catch ��
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
	 * @param city_array Ҫ���õ� city_array
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
	 * @param aqi_array Ҫ���õ� aqi_array
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
	 * @param co_array Ҫ���õ� co_array
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
	 * @param co_24h_array Ҫ���õ� co_24h_array
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
	 * @param no2_array Ҫ���õ� no2_array
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
	 * @param no2_24h_array Ҫ���õ� no2_24h_array
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
	 * @param o3_array Ҫ���õ� o3_array
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
	 * @param o3_24h_array Ҫ���õ� o3_24h_array
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
	 * @param o3_8h_array Ҫ���õ� o3_8h_array
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
	 * @param o3_8h_24h_array Ҫ���õ� o3_8h_24h_array
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
	 * @param pm10_array Ҫ���õ� pm10_array
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
	 * @param pm10_24h_array Ҫ���õ� pm10_24h_array
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
	 * @param pm2_5_array Ҫ���õ� pm2_5_array
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
	 * @param pm2_5_24h_array Ҫ���õ� pm2_5_24h_array
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
	 * @param quality_array Ҫ���õ� quality_array
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
	 * @param level_array Ҫ���õ� level_array
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
	 * @param so2_array Ҫ���õ� so2_array
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
	 * @param so2_24h_array Ҫ���õ� so2_24h_array
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
	 * @param primary_pollutant_array Ҫ���õ� primary_pollutant_array
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
	 * @param time_point_array Ҫ���õ� time_point_array
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
	 * @param weather_array Ҫ���õ� weather_array
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
	 * @param preWeather_array Ҫ���õ� preWeather_array
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
	 * @param suggest_array Ҫ���õ� suggest_array
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
	 * @param pre_pm2_5_array Ҫ���õ� pre_pm2_5_array
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
	 * @param city_list Ҫ���õ� city_list
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
	 * @param belongcity_list Ҫ���õ� belongcity_list
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
	 * @param temperature_array Ҫ���õ� temperature_array
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
	 * @param wind_array Ҫ���õ� wind_array
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
	 * @param humidity_array Ҫ���õ� humidity_array
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
	 * @param weatherNum_array Ҫ���õ� weatherNum_array
	 */
	public static void setWeatherNum_array(int[] weatherNum_array) {
		UpdateDataUtil.weatherNum_array = weatherNum_array;
	}
	
}
