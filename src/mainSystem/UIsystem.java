
package mainSystem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.mysql.jdbc.PreparedStatement;

/**
 * @author 盛鼎杰，孟靖宇，方远
 *
 */
public class UIsystem {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
        @SuppressWarnings("unused")
		MyFrame myFrame=new MyFrame("实时PM2.5监控系统",new ImageIcon("weatherIcon.png").getImage());
	}

}

class MyFrame extends JFrame implements ActionListener{
	/**
	 * @throws HeadlessException
	 */
	public MyFrame(String title,Image image) throws HeadlessException {
		super(title);
		this.iconImage=image;//可能图片大小不合适有待改正
		init();
		registerListener();
	}
	private static boolean flag_firstload=true;
	/*
	 * 图标
	 */
	private Image iconImage=null;
	/*
	 * 生成图表的监听器
	 */
	private CreateChartListener chartListener=null;
	/*
	 * 加载提示框
	 * 数据标签选择框
	 */
	private JDialog loadingdialog=null;
	private JDialog jd_choose=null;
	private JDialog jd_contacts=null;
	/*
	 * 数据标签
	 */
	private String[] city_label=null;
	private Timestamp[] tsp_label=null;
	/*
	 * 数据标签下拉列表
	 */
	private JComboBox<String> jc_cityname=null;
	private JComboBox<Timestamp> jc_tspstart=null;
	private JComboBox<Timestamp> jc_tspend=null;
	/*
	 * 请求访问的数据库链接
	 */
	private Connection conn=null;
	/*
	 * 菜单栏以及菜单项和菜单按钮
	 */
	private JMenuBar jmb = null;
	private JMenu jm_file = null;
	private JMenu jm_help = null;
	private JMenu jm_edit = null;
	private JMenu jm_check = null;
	private JMenu jm_tools = null;
	private JMenuItem ji_hitory=null;
	private JMenuItem ji_export=null;
	private JMenuItem ji_cityList=null;
	private JMenuItem ji_morecityList=null;
	private JMenuItem ji_machinelearning=null;
	private JMenuItem ji_importcontact=null;
	private JMenuItem ji_about=null;
	private JMenuItem ji_createChart=null;
	private JMenuItem ji_latestdata=null;
	/*
	 * 主窗口面板区
	 */
	private JScrollPane js_mainpane=null;
	private JPanel jp_citiesData = null;
	private JPanel jp_weatherItem = null;
	private JPanel jp_buttonArea = null;
	/*
	 * 天气相关属性标签（天气，PM2.5，时间戳，PM2.5预测值等）
	 */
	private JLabel jl_cityName=null;
	private JLabel jl_weather = null;
	private JLabel jl_pm=null;
	private JLabel jl_aqi=null;
	private JLabel jl_time=null;
	private JLabel jl_preWeather=null;
	private JLabel jl_prePm=null;
	private JLabel jl_suggest=null;
	private JLabel[] jl_cityList=null;
	/*
	 * 各类属性值显示框
	 */
	private JTextField []jt_weather = null;
	private JTextField []jt_pm=null;
	private JTextField []jt_aqi=null;
	private JTextField []jt_time=null;
	private JTextField []jt_preWeather=null;
	private JTextField []jt_prePm=null;
	private JTextArea []jt_suggest=null;
	private JTextField jt_mailaddress=null;
	/*
	 * 各类属性值数组
	 */
	private String[] city_array=null;
	private int[] aqi_array=null;
	private String[] weather_array=null;
	private double[] pm2_5_array=null;
	private String[] time_point_array=null;
	private String[] suggest_array=null;
	private double[] pre_pm2_5_array=null;
	private String[] preweather_array=null;
	private int[] temperature_array=null;
	private int[] wind_array=null;
	private int[] humidity_array=null;
	private int[] weatherNum_array=null;
	private ArrayList<String> city_list=null;
	private ArrayList<String> belongcity_list=null;
	/*
	 * 各类操作按钮
	 */
	JButton bt_updateData=null;
	JButton bt_prediction=null;
	JButton bt_broadcast=null;
	public void registerListener() {
		// TODO 自动生成的方法存根
		bt_updateData.addActionListener(this);
		bt_prediction.addActionListener(this);
		bt_broadcast.addActionListener(this);
		ji_export.addActionListener(this);
		ji_hitory.addActionListener(this);
		ji_cityList.addActionListener(this);
		ji_morecityList.addActionListener(this);
		ji_machinelearning.addActionListener(this);
		ji_importcontact.addActionListener(this);
		ji_about.addActionListener(this);
		ji_latestdata.addActionListener(this);
	}
	/*
	 *初始化函数 
	 */
	public void init() {
//		创建文件存放目录
		File file=new File("C:\\weathermonitor");
		if(!file.exists()){
			file.mkdir();
		}
//		加载数据库驱动程序
		try {
			DatabaseUtil.getDatebase();
		} catch (Exception e1) {
			// TODO 自动生成的 catch 块
			errorDialog(e1);
			e1.printStackTrace();
		}
		Container container = this.getContentPane();
		this.setIconImage(iconImage);
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO 自动生成的方法存根
//				try {
//					UpdateDataUtil.updateDataUtil();
////					DataRequest.cityListRequest(false);//TRUE代表通过网络访问实时数据，FALSE代表访问网络数据失败显示历史数据,先取得历史数据以免访问失败，程序无法正常运行
//				} catch (Exception e) {
//					// TODO 自动生成的 catch 块
//					errorDialog(e);
//					e.printStackTrace();
//				}
//			}
//		}).start();
		//初始化菜单栏
		ji_export=new JMenuItem("导出训练数据");
		ji_hitory=new JMenuItem("历史数据");
		ji_cityList=new JMenuItem("更新城市列表");
		ji_morecityList=new JMenuItem("更新详细城市列表");
		ji_machinelearning=new JMenuItem("训练学习数据集");
		ji_importcontact=new JMenuItem("导入联系人信息");
		ji_about=new JMenuItem("关于");
		ji_createChart=new JMenuItem("生成图表");
		ji_latestdata=new JMenuItem("刷新");
		jm_file=new JMenu("文件");
		jm_file.add(ji_export);
		jm_tools=new JMenu("工具");
		jm_tools.add(ji_cityList);
		jm_tools.add(ji_morecityList);
		jm_tools.add(ji_machinelearning);
		jm_tools.add(ji_importcontact);
		jm_edit=new JMenu("编辑");
		jm_edit.add(ji_createChart);
		jm_edit.add(ji_latestdata);
		jm_help=new JMenu("帮助");
		jm_help.add(ji_about);
		jm_check=new JMenu("查看");
		jm_check.add(ji_hitory);
		jmb=new JMenuBar();
		jmb.add(jm_file);
		jmb.add(jm_tools);
		jmb.add(jm_edit);
		jmb.add(jm_check);
		jmb.add(jm_help);
		jmb.setBackground(Color.WHITE);
		this.setJMenuBar(jmb);
	    //初始化主体窗口
		jl_cityName=new JLabel("城市",JLabel.CENTER);
		jl_aqi=new JLabel("AQI值",JLabel.CENTER);
		jl_pm=new JLabel("PM2.5浓度",JLabel.CENTER);
		jl_prePm=new JLabel("PM2.5预测值",JLabel.CENTER);
		jl_preWeather=new JLabel("天气预测值",JLabel.CENTER);
		jl_suggest=new JLabel("生活建议",JLabel.CENTER);
		jl_time=new JLabel("时间戳",JLabel.CENTER);
		jl_weather=new JLabel("天气",JLabel.CENTER);
		jp_citiesData =new JPanel();
		jp_buttonArea =new JPanel();
		jp_weatherItem =new JPanel();
		js_mainpane=new JScrollPane(jp_citiesData);
		jp_buttonArea.setBackground(Color.pink);
		jp_citiesData.setBackground(Color.lightGray);
		jp_weatherItem.setLayout(new GridLayout());
		jp_weatherItem.add(jl_cityName);
		jp_weatherItem.add(jl_time);
		jp_weatherItem.add(jl_weather);
		jp_weatherItem.add(jl_pm);
		jp_weatherItem.add(jl_aqi);
		jp_weatherItem.add(jl_preWeather);
		jp_weatherItem.add(jl_prePm);
		jp_weatherItem.add(jl_suggest);
		bt_updateData=new JButton("更新数据");
		bt_prediction=new JButton("预测数据");
		bt_broadcast=new JButton("发送通知");
		jp_buttonArea.add(bt_updateData);
		jp_buttonArea.add(bt_prediction);
		jp_buttonArea.add(bt_broadcast);
//      初始化城市列表,设置城市天气数据并且设置定时调度数据更新（每30分钟一次）
		ScheduleTask.updateScheduel(this);
//      显示用户界面,应该把该方法放到定时任务里，以免数据线程与主线程的速度不匹配
		onDraw(container);
	}
	/**
	 * @param e
	 */
	public void errorDialog(Exception e) {
		JDialog errordialog=new JDialog(MyFrame.this, "错误信息");
		Container errorcon=errordialog.getContentPane();
		JTextArea errorjt=new JTextArea(e.getMessage());
		errorjt.setEditable(false);
		errorjt.setLineWrap(true);
		errorjt.setWrapStyleWord(true);
		errorcon.add(errorjt, BorderLayout.CENTER);
		errordialog.setSize(300, 200);
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screensize = kit.getScreenSize();
		int screenwidth = screensize.width;
		int screenhight = screensize.height;
		int windowswidth = errordialog.getWidth();
		int windowshight = errordialog.getHeight();
		errordialog.setLocation((screenwidth - windowswidth) / 2,
				(screenhight - windowshight) / 2);
		errordialog.setVisible(true);
	}
	
	public void setloadingDialog(){
		if (loadingdialog==null||!loadingdialog.isVisible()) {
			loadingdialog = new JDialog(MyFrame.this, "提示",true);
			loadingdialog.setIconImage(iconImage);
			Container errorcon = loadingdialog.getContentPane();
			JTextArea errorjt = new JTextArea("正在加载数据中，请稍候......");
			errorjt.setEditable(false);
			errorjt.setLineWrap(true);
			errorjt.setWrapStyleWord(true);
			errorcon.add(errorjt, BorderLayout.CENTER);
			loadingdialog.setSize(300, 200);
			Toolkit kit = Toolkit.getDefaultToolkit();
			Dimension screensize = kit.getScreenSize();
			int screenwidth = screensize.width;
			int screenhight = screensize.height;
			int windowswidth = loadingdialog.getWidth();
			int windowshight = loadingdialog.getHeight();
			loadingdialog.setLocation((screenwidth - windowswidth) / 2,
					(screenhight - windowshight) / 2);
			loadingdialog.setResizable(false);
		}
		loadingdialog.setVisible(true);
	}
	/**
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws ParseException 
	 * @param internet判断是否从互联网获取数据，是的话更新数据库，否则不更新，表示从数据库取历史数据
	 */
	public void updateData() throws SQLException, ParseException, IOException {
		conn=DatabaseUtil.getConn();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO 自动生成的方法存根
				setloadingDialog();
			}
		}).start();
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO 自动生成的方法存根
//				JOptionPane.showMessageDialog(MyFrame.this, "开始加载数据，请稍候......","提示",JOptionPane.INFORMATION_MESSAGE);
//			}
//		}).start();
		if(flag_firstload){
			updatecitylist();//先更新城市列表，之后根据此列表请求天气以及空气质量数据,初次加载时执行
			flag_firstload=false;
		}
		boolean internet=false;
		internet=UpdateDataUtil.updateDataUtil(false);//通过返回值来判断数据来源是否是网络
		city_array=UpdateDataUtil.getCity_array();
		aqi_array=UpdateDataUtil.getAqi_array();
		time_point_array=UpdateDataUtil.getTime_point_array();
		pm2_5_array=UpdateDataUtil.getPm2_5_array();
		pre_pm2_5_array=UpdateDataUtil.getPre_pm2_5_array();
		weather_array=UpdateDataUtil.getWeather_array();
		preweather_array=UpdateDataUtil.getPreWeather_array();
		suggest_array=UpdateDataUtil.getSuggest_array();
		temperature_array=UpdateDataUtil.getTemperature_array();
		humidity_array=UpdateDataUtil.getHumidity_array();
		wind_array=UpdateDataUtil.getWind_array();
		weatherNum_array=UpdateDataUtil.getWeatherNum_array();
//		获取当前数据的时间戳
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
//		SimpleDateFormat df1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Timestamp tmsp=null;
		try {
			if(internet){
				tmsp=Timestamp.valueOf(time_point_array[17]);
			}else{
				tmsp=Timestamp.valueOf(time_point_array[17]);
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		jp_citiesData.removeAll();
		GridLayout gl=new GridLayout(0,8,1,15);
		jp_citiesData.setLayout(gl);
		// 初始化属性值显示框
		jt_aqi = new JTextField[city_array.length];
		jt_pm = new JTextField[city_array.length];
		jt_prePm = new JTextField[city_array.length];
		jt_preWeather = new JTextField[city_array.length];
		jt_suggest = new JTextArea[city_array.length];
		jt_time = new JTextField[city_array.length];
		jt_weather = new JTextField[city_array.length];
		for (int i = 0; i < city_array.length; i++) {
			jt_aqi[i] = new JTextField("AQI值");
			jt_pm[i] = new JTextField("PM2.5");
			jt_prePm[i] = new JTextField("PM2.5预测值");
			jt_preWeather[i] = new JTextField("天气预测值");
			jt_suggest[i] = new JTextArea("出行建议");
			jt_time[i] = new JTextField("时间戳");
			jt_weather[i] = new JTextField("当前天气");
		}
		if (city_array != null) {
			jl_cityList = new JLabel[city_array.length];
			conn=DatabaseUtil.getConn();
//			比较当前数据与历史数据的时间戳，若历史时间戳中存在则无需写入数据库
			PreparedStatement prestmt=(PreparedStatement) conn.prepareStatement("select time_stamp from tmstamp");
			ResultSet rs=prestmt.executeQuery();
			while(rs.next()){
				if(tmsp.equals(rs.getTimestamp(rs.findColumn("time_stamp")))){
					internet=false;
					break;
				}
			}//如果时间戳表中已存在此记录，无需重复写入
			if(internet){
				PreparedStatement preparedStatement2=(PreparedStatement) conn.prepareStatement("insert into tmstamp(time_stamp)values(?)");
				preparedStatement2.setTimestamp(1, Timestamp.valueOf(time_point_array[17]));
				preparedStatement2.execute();
			}
			for (int i = 0; i < city_array.length; i++) {
				if (city_array[i]!=null) {
					jl_cityList[i] = new JLabel(city_array[i]);
					jt_time[i].setText(time_point_array[17]);
					jt_time[i].setEditable(false);
					jt_weather[i].setText(weather_array[i]);
					jt_weather[i].setEditable(false);
					jt_pm[i].setText(pm2_5_array[i] + "");
					jt_pm[i].setEditable(false);
					jt_aqi[i].setText(aqi_array[i] + "");
					jt_aqi[i].setEditable(false);
					jt_preWeather[i].setText(preweather_array[i]);
					jt_preWeather[i].setEditable(false);
					jt_prePm[i].setText(pre_pm2_5_array[i] + "");
					jt_prePm[i].setEditable(false);
					jt_suggest[i].setText(suggest_array[i]);
					jt_suggest[i].setEditable(false);
					jp_citiesData.add(jl_cityList[i]);
					jp_citiesData.add(jt_time[i]);
					jp_citiesData.add(jt_weather[i]);
					jp_citiesData.add(jt_pm[i]);
					jp_citiesData.add(jt_aqi[i]);
					jp_citiesData.add(jt_preWeather[i]);
					jp_citiesData.add(jt_prePm[i]);
					jp_citiesData.add(jt_suggest[i]);
					if (internet) {//如果数据来源于网络则需要写入数据库
					//					向数据库写入数据操作
						PreparedStatement preparedStatement = (PreparedStatement) conn
								.prepareStatement("insert into weather (cityname, time_stamp, aqi, pm2_5, prepm2_5, weather_now, weather_forecast, suggest,temperature,humidity,wind,weatherNum) values(?,?,?,?,?,?,?,?,?,?,?,?)");
						preparedStatement.setString(1, city_array[i]);
						//					df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
//						df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						preparedStatement.setTimestamp(2,
								Timestamp.valueOf(time_point_array[17]));//北京市
						preparedStatement.setDouble(3, aqi_array[i]);
						preparedStatement.setDouble(4, pm2_5_array[i]);
						preparedStatement.setDouble(5, 0);//此处应该是pre_pm2_5_array[i]，预测模块实现后修改
						preparedStatement.setString(6, weather_array[i]);
						preparedStatement.setString(7, preweather_array[i]);
						preparedStatement.setString(8, suggest_array[i]);//此处应该是suggest_array[i]，项目完善后修改
						preparedStatement.setInt(9, temperature_array[i]);
						preparedStatement.setInt(10, humidity_array[i]);
						preparedStatement.setInt(11, wind_array[i]);
						preparedStatement.setInt(12, weatherNum_array[i]);
						if (conn != null && !conn.isClosed()) {
							preparedStatement.execute();
						}
					}
				}
			}
			if(internet&&conn!=null&&!conn.isClosed()){
				conn.close();
			}
		}
		loadingdialog.setVisible(false);
		loadingdialog.dispose();
		loadingdialog=null;
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				// TODO 自动生成的方法存根
//				JOptionPane.showMessageDialog(MyFrame.this, "数据加载完成！", "提示",
//						JOptionPane.INFORMATION_MESSAGE);
//			}
//		}).start();
	}
	/**
	 * @param container
	 */
	public void onDraw(Container container) {
		jp_citiesData.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		jp_buttonArea.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		jp_weatherItem.setBorder(BorderFactory.createLineBorder(Color.WHITE));
//		垂直滚动条在必要时出现
		js_mainpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		container.add(js_mainpane);
		container.add(jp_buttonArea, BorderLayout.SOUTH);
		container.add(jp_weatherItem, BorderLayout.NORTH);
		this.setSize(1100, 600);
		this.setMinimumSize(new Dimension(1100, 600));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screensize = kit.getScreenSize();
		int screenwidth = screensize.width;
		int screenhight = screensize.height;
		int windowswidth = this.getWidth();
		int windowshight = this.getHeight();
		this.setLocation((screenwidth - windowswidth) / 2,
				(screenhight - windowshight) / 2);
		this.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO 自动生成的方法存根
		String source=e.getActionCommand();
		if(source.equals("更新数据")){
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO 自动生成的方法存根
					try {
						if(chartListener!=null){
							ji_createChart.removeActionListener(chartListener);
							chartListener=null;
						}
						updateData();
						onDraw(MyFrame.this.getContentPane());//刷新显示,如果不调用此函数无法更新显示
					} catch (Exception e1) {
						// TODO 自动生成的 catch 块
						errorDialog(e1);
						e1.printStackTrace();
					}
				}
			}).start();
		}else if(source.equals("预测数据")){
			prediction();
		}else if(source.equals("发送通知")){
			sendMail();
		}else if(source.equals("导出训练数据")){
			exportData();
		}else if(source.equals("历史数据")){
			if(chartListener!=null){
				ji_createChart.removeActionListener(chartListener);
				chartListener=null;
			}
			historyDataSelect();
		}else if(source.equals("更新城市列表")){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO 自动生成的方法存根
					setloadingDialog();
				}
			}).start();
			updatecitylist();
			loadingdialog.setVisible(false);
			loadingdialog.dispose();
			loadingdialog=null;
		}else if(source.equals("更新详细城市列表")){
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO 自动生成的方法存根
					setloadingDialog();
				}
			}).start();
			updatemorecitylist();
			loadingdialog.setVisible(false);
			loadingdialog.dispose();
			loadingdialog=null;
		}else if(source.equals("训练学习数据集")){
			machineLearning();
		}else if(source.equals("导入联系人信息")){
			importcontacts();
		}else if(source.equals("关于")){
			aboutDialog();
		}else if(source.equals("刷新")){
			if(chartListener!=null){
				ji_createChart.removeActionListener(chartListener);
				chartListener=null;
			}
			try {
				conn=DatabaseUtil.getConn();
				reloadUI((PreparedStatement) conn
						.prepareStatement("select * from weather where weather.time_stamp=(select tmstamp.time_stamp from tmstamp where tmstamp.id>=all(select id from tmstamp));"), false);
			} catch (SQLException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
		}
	}

	public void prediction() {
		// TODO 自动生成的方法存根
		try {
			conn=DatabaseUtil.getConn();
		    PreparedStatement pre=(PreparedStatement) conn
				.prepareStatement("select * from weather where weather.time_stamp=(select tmstamp.time_stamp from tmstamp where tmstamp.id>=all(select id from tmstamp));");
		    ResultSet rs=pre.executeQuery();
			FileOutputStream fo_data=new FileOutputStream(new File("C:\\weathermonitor\\predictdata.txt"));
			while(rs.next()){
//				if (rs.getInt(rs.findColumn("pm2_5")) != -1
//						&& rs.getInt(rs.findColumn("temperature")) != -1
//						&& rs.getInt(rs.findColumn("humidity")) != -1
//						&& rs.getInt(rs.findColumn("wind")) != -1
//						&& rs.getInt(rs.findColumn("weatherNum")) != -1) {
					StringBuffer sb = new StringBuffer();
					StringBuffer sb1 = new StringBuffer();
					sb.append(rs.getInt(rs.findColumn("temperature")));
					sb.append(" ");
					sb.append(rs.getInt(rs.findColumn("humidity")));
					sb.append(" ");
					sb.append(rs.getInt(rs.findColumn("wind")));
					sb.append(" ");
					sb.append(rs.getInt(rs.findColumn("weatherNum")));
					sb.append("\r\n");
					fo_data.write(sb.toString().getBytes(), 0, sb
							.toString().getBytes().length);
//				}
			}
			rs.close();
			fo_data.close();
			conn.close();
			new Thread(new myRunnable1()).start();
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
//  “关于”对话框
	private void aboutDialog() {
		// TODO 自动生成的方法存根
        new AboutDialog(MyFrame.this, "关于—PM2.5监控系统", true);
	}
	
	class AboutDialog extends JDialog {
		private JLabel desc = null;
		private JPanel panel = null;
		private JButton btn = null;

		public AboutDialog(Frame frame, String title, boolean modal) {
			super(frame, title, modal);
			init();
		}

		public void init() {
			desc = new JLabel();
			desc.setText("<html>开发者：盛鼎杰 版本号：1.0<br/>项目正处于研发初期，还不够完善，<br/>相关功能不完备，请等待后续版本！</html>");
			desc.setHorizontalAlignment(JLabel.CENTER);
			panel = new JPanel();
			btn = new JButton("确定");
			panel.add(btn);
			this.add(desc);
			this.add(panel, BorderLayout.SOUTH);
			this.setSize(300, 200);
			registerListener();
			Toolkit kit = Toolkit.getDefaultToolkit();
			Dimension screensize = kit.getScreenSize();
			int screenwidth = screensize.width;
			int screenhight = screensize.height;
			int windowswidth = this.getWidth();
			int windowshight = this.getHeight();
			this.setLocation((screenwidth - windowswidth) / 2,
					(screenhight - windowshight) / 2);
			this.setVisible(true);
		}

		private void registerListener() {
			btn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					AboutDialog.this.dispose();
				}
			});
		}
	}

//	导入联系人信息
	public void importcontacts() {
		// TODO 自动生成的方法存根
		jd_contacts=new JDialog(MyFrame.this, "导入联系人信息");
		jd_contacts.setIconImage(iconImage);
		try {
			conn=DatabaseUtil.getConn();
			PreparedStatement pre=(PreparedStatement) conn.prepareStatement("select cityname from citylist;");
			ResultSet rs=pre.executeQuery();
			rs.last(); 
			int size= rs.getRow(); 
			rs.beforeFirst();
			int count=0;
			city_label=new String[size];
			while(rs.next()){
				city_label[count] = rs.getString(rs.findColumn("cityname"));
				count++;
			}
			jc_cityname=new JComboBox<>(city_label);
			jt_mailaddress=new JTextField();
			jt_mailaddress.setColumns(20);
			FlowLayout fl=new FlowLayout(FlowLayout.CENTER);
			jd_contacts.setLayout(fl);
			jd_contacts.add(new JLabel("所在城市："));
			jd_contacts.add(jc_cityname);
			jd_contacts.add(new JLabel("邮件地址："));
			jd_contacts.add(jt_mailaddress);
		    JButton bt_yes=new JButton("确定");
		    bt_yes.addActionListener(new myListener1());
		    JButton bt_no=new JButton("取消");
		    bt_no.addActionListener(new myListener1());
		    jd_contacts.add(bt_yes);
		    jd_contacts.add(bt_no);
		    jd_contacts.setSize(500, 100);
		    Point po=jd_contacts.getParent().getLocationOnScreen();
		    jd_contacts.setLocation(po.x+300, po.y+250);
		    jd_contacts.setResizable(false);
		    jd_contacts.setVisible(true);
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
//	发送邮件
	public void sendMail() {
		// TODO 自动生成的方法存根
		StringBuffer mailtext=new StringBuffer();
		String receiver=null;
		int succeed_count=0;
		try {
			conn=DatabaseUtil.getConn();
			PreparedStatement preContacts = (PreparedStatement) conn
					.prepareStatement("select * from weather,Contacts where weather.cityname=Contacts.location and weather.time_stamp=(select tmstamp.time_stamp from tmstamp where tmstamp.id>=all(select id from tmstamp));");
		    ResultSet rs=preContacts.executeQuery();
		    while(rs.next()){
		    	receiver=rs.getString(rs.findColumn("mailAddress"));
		    	mailtext.append(rs.getString(rs.findColumn("cityname"))+"天气：<br>");
		    	mailtext.append("当前天气："+rs.getString(rs.findColumn("weather_now"))+"<br>");
		    	mailtext.append("下一时间段天气："+rs.getString(rs.findColumn("weather_forecast"))+"<br>");
		    	mailtext.append("空气AQI值："+rs.getString(rs.findColumn("aqi"))+"<br>");
		    	mailtext.append("PM2.5:"+rs.getDouble(rs.findColumn("pm2_5"))+"<br>");
		    	mailtext.append("下一时间段PM2.5:"+rs.getDouble(rs.findColumn("prepm2_5"))+"<br>");
		    	mailtext.append(rs.getString(rs.findColumn("suggest"))+"<br>");
		    	mailtext.append(rs.getTimestamp(rs.findColumn("time_stamp"))+"<br>");
		    	if(SendMailUtil.SendMail(receiver, mailtext.toString())){
		    		succeed_count++;
		    	}
		    }
		    JOptionPane.showMessageDialog(MyFrame.this, "成功发送"+succeed_count+"封邮件，"+(rs.getRow()+1-succeed_count)+"封邮件发送失败。");
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
	public void exportData() {
		// TODO 自动生成的方法存根
		try {
			conn=DatabaseUtil.getConn();
			PreparedStatement pre=(PreparedStatement) conn.prepareStatement("select * from weather where id >(select MAX(id) from weather)-1000;");
			ResultSet rs=pre.executeQuery();
			FileOutputStream fo_data=new FileOutputStream(new File("C:\\weathermonitor\\exportdata.txt"));
			FileOutputStream fo_aimdata=new FileOutputStream(new File("C:\\weathermonitor\\aimdata.txt"));
			while(rs.next()){
				if (rs.getInt(rs.findColumn("pm2_5")) != -1
						&& rs.getInt(rs.findColumn("temperature")) != -1
						&& rs.getInt(rs.findColumn("humidity")) != -1
						&& rs.getInt(rs.findColumn("wind")) != -1
						&& rs.getInt(rs.findColumn("weatherNum")) != -1) {
					StringBuffer sb = new StringBuffer();
					StringBuffer sb1 = new StringBuffer();
					sb.append(rs.getInt(rs.findColumn("temperature")));
					sb.append(" ");
					sb.append(rs.getInt(rs.findColumn("humidity")));
					sb.append(" ");
					sb.append(rs.getInt(rs.findColumn("wind")));
					sb.append(" ");
					sb.append(rs.getInt(rs.findColumn("weatherNum")));
					sb.append("\r\n");
					fo_data.write(sb.toString().getBytes(), 0, sb
							.toString().getBytes().length);
					sb1.append(String.valueOf(rs.getInt(rs.findColumn("pm2_5"))/1000.0));
					sb1.append("\r\n");
					fo_aimdata.write(sb1.toString().getBytes(), 0, sb1.toString().getBytes().length);
				}
			}
			rs.close();
			fo_data.close();
			fo_aimdata.close();
		} catch (SQLException | IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	public void machineLearning() {
		// TODO 自动生成的方法存根
//		try {
//			conn=DatabaseUtil.getConn();
//			PreparedStatement pre=(PreparedStatement) conn.prepareStatement("select temperature,humidity,wind,weatherNum from weather;");
//			ResultSet rs=pre.executeQuery();
//			ResultSet rs_copy=rs;
//			int[][] learningdata=new int[4][4];
////			while(rs.)
//			PythonInterpreter pi=new PythonInterpreter();
//			pi.execfile("F:\\eclipse工作文件\\mlWeather\\pm25predict\\__init__.py");
//		} catch (SQLException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		}
		new Thread(new myRunnable()).start();
		
		
	}
	
	class myRunnable implements Runnable{
		@Override
		public void run() {
			// TODO 自动生成的方法存根
			Process proc=null;
			try {
				proc = Runtime.getRuntime().exec("python bp1.py");
				proc.waitFor();
//				fi=new FileInputStream(new File("C:\\Users\\ac\\Desktop\\weight.txt"));
//				sc=new Scanner(fi);
//				while(sc.hasNextLine()){
//					String result=sc.nextLine();
//					if(result!=null&&!result.isEmpty()){
//						weight[count]=Double.parseDouble(result);
//						count++;
//					}
//				}
//				fi.close();
//				insertResult();
			    InputStreamReader ir =new InputStreamReader(proc.getInputStream());
			    LineNumberReader input=new LineNumberReader(ir);
				String line;
				while((line=input.readLine())!=null){
					
				}
				input.close();
				ir.close();
				JOptionPane.showMessageDialog(MyFrame.this, "数据训练完成！", "提示", JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} 
		}
		
//		将学习结果写入数据库
//		public void insertResult(){
//			try {
//				conn=DatabaseUtil.getConn();
//				PreparedStatement pre=(PreparedStatement) conn.prepareStatement("insert into weights(weight1,weight2,weight3,weight4,learntime)values(?,?,?,?,?);");
//				pre.setDouble(1, weight[0]);
//				pre.setDouble(2, weight[1]);
//				pre.setDouble(3, weight[2]);
//				pre.setDouble(4, weight[3]);
//				pre.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
//				pre.execute();
//				JOptionPane.showMessageDialog(MyFrame.this, "数据训练完成！", "提示", JOptionPane.INFORMATION_MESSAGE);
//			} catch (SQLException e) {
//				// TODO 自动生成的 catch 块
//				e.printStackTrace();
//			}finally{
//				try {
//					if(conn!=null&&!conn.isClosed()){
//						conn.close();
//					}
//				} catch (SQLException e) {
//					// TODO 自动生成的 catch 块
//					e.printStackTrace();
//				}
//			}
//		}
	}
	
	class myRunnable1 implements Runnable{
		@Override
		public void run() {
			// TODO 自动生成的方法存根
			Process proc=null;
			FileInputStream fi=null;
			Scanner sc=null;
			int count=0;
			try {
				proc = Runtime.getRuntime().exec("python predict.py");
				proc.waitFor();
				fi=new FileInputStream(new File("C:\\weathermonitor\\result.txt"));
				sc=new Scanner(fi);
				while(sc.hasNextLine()){
					String result=sc.nextLine();
					if(result!=null&&!result.isEmpty()){
						if(Double.parseDouble(result)*1000>500){
							pre_pm2_5_array[count]=500.0;
						}else{
							pre_pm2_5_array[count]=Double.parseDouble(result)*1000;
							pre_pm2_5_array[count]=Math.round(pre_pm2_5_array[count]);
						}
						jt_prePm[count].setText(pre_pm2_5_array[count] + "");
						jt_prePm[count].setEditable(false);
						count++;
					}
				}
				fi.close();
				sc.close();
				insertResult();
				JOptionPane.showMessageDialog(MyFrame.this, "数据预测完成！", "提示", JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} 
		}
		
//		将学习结果写入数据库
		public void insertResult(){
			int count=0;
			try {
				conn = DatabaseUtil.getConn();
				PreparedStatement pre=null;
				PreparedStatement pre1=null;
				Timestamp time=null;
				pre1=(PreparedStatement) conn.prepareStatement("select tmstamp.time_stamp from tmstamp where tmstamp.id>=all(select id from tmstamp);");
				ResultSet res=pre1.executeQuery();
				while(res.next()){
					time=res.getTimestamp("time_stamp");
				}
				while (count<pre_pm2_5_array.length) {
					pre = (PreparedStatement) conn
							.prepareStatement("update weather set prepm2_5= ? where weather.time_stamp=? and weather.cityname= ? ;");
					pre.setDouble(1, pre_pm2_5_array[count]);
					pre.setTimestamp(2, time);
					pre.setString(3, city_array[count]);
					pre.executeUpdate();
					count++;
				}
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
	}
	
	private void historyDataSelect() {
		// TODO 自动生成的方法存根
		jd_choose=new JDialog(MyFrame.this, "历史数据标签选择框");
		jd_choose.setIconImage(iconImage);
		try {
			conn=DatabaseUtil.getConn();
			PreparedStatement pre=(PreparedStatement) conn.prepareStatement("select cityname from citylist;");
			PreparedStatement pre1=(PreparedStatement) conn.prepareStatement("select time_stamp from tmstamp;");
			ResultSet rs=pre.executeQuery();
			ResultSet rs1=pre1.executeQuery();
			rs.last(); 
			rs1.last();
			int size= rs.getRow(); 
			int size1=rs1.getRow();
			rs.beforeFirst();
			rs1.beforeFirst();
			int count=1;
			int count1=1;
			city_label=new String[size+1];
			tsp_label=new Timestamp[size1+1];
			city_label[0]=null;//第一个值设为null，表示查询全部数据
			tsp_label[0]=null;
			while(rs.next()){
				city_label[count] = rs.getString(rs.findColumn("cityname"));
				count++;
			}
			while(rs1.next()){
				tsp_label[count1] = rs1.getTimestamp(rs1.findColumn("time_stamp"));
				count1++;
			}
			jc_cityname=new JComboBox<>(city_label);
			jc_tspstart=new JComboBox<>(tsp_label);
			jc_tspend=new JComboBox<>(tsp_label);
			FlowLayout fl=new FlowLayout(FlowLayout.CENTER);
			jd_choose.setLayout(fl);
		    jd_choose.add(new JLabel("城市名："));
		    jd_choose.add(jc_cityname);
		    jd_choose.add(new JLabel("起始时间："));
		    jd_choose.add(jc_tspstart);
		    jd_choose.add(new JLabel("截止时间："));
		    jd_choose.add(jc_tspend);
		    JButton bt_yes=new JButton("确定");
		    bt_yes.addActionListener(new myListener());
		    JButton bt_no=new JButton("取消");
		    bt_no.addActionListener(new myListener());
		    jd_choose.add(bt_yes);
		    jd_choose.add(bt_no);
		    jd_choose.setSize(650, 100);
		    Point po=jd_choose.getParent().getLocationOnScreen();
		    jd_choose.setLocation(po.x+225, po.y+250);
		    jd_choose.setResizable(false);
		    jd_choose.setVisible(true);
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

//	联系人按钮监听类
	class myListener1 implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO 自动生成的方法存根
			if(e.getActionCommand().equals("确定")){
				insertContactData((String)jt_mailaddress.getText(),(String)jc_cityname.getSelectedItem());
			}
			jd_contacts.setVisible(false);
		}

		private void insertContactData(String text,String selectedItem) {
			// TODO 自动生成的方法存根
			try {
				PreparedStatement pre=(PreparedStatement) conn.prepareStatement("insert into contacts (mailAddress , location) values(? ,?);");
				pre.setNString(1, text);
				pre.setNString(2, selectedItem);
				pre.execute();
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
		
	}

//	数据标签按钮监听类
	class myListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO 自动生成的方法存根
			if(e.getActionCommand().equals("确定")){
				if(jc_tspstart.getSelectedIndex()>jc_tspend.getSelectedIndex()&&!(jc_tspstart.getSelectedItem()==null||jc_tspend.getSelectedItem()==null)){
					JOptionPane.showMessageDialog(MyFrame.this, "时间选择非法，起始时间不能大于截止时间！", "警告", JOptionPane.WARNING_MESSAGE);
				}else{
					checkHistoryData((String)jc_cityname.getSelectedItem(),(Timestamp)jc_tspstart.getSelectedItem(),(Timestamp)jc_tspend.getSelectedItem());
				}
			}
			jd_choose.setVisible(false);
		}
		
	}
//	更新城市列表
	public void updatecitylist() {
		boolean flag_success=true;
		try {
			UpdateDataUtil.updateCityList(false);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			flag_success=false;
			try {
				UpdateDataUtil.historyCitylist(false);
			} catch (Exception e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
			e.printStackTrace();
		}//false代表只更新大城市列表，true代表更新所有城市列表
		city_list = UpdateDataUtil.getCity_list();
		if (flag_success) {
			PreparedStatement pre = null;
			PreparedStatement pre1 = null;
			try {
				pre1 = (PreparedStatement) conn
						.prepareStatement("truncate table citylist;");
				pre = (PreparedStatement) conn
						.prepareStatement("insert into citylist(cityname) values (?);");
				pre1.execute();
			} catch (SQLException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
			Iterator<String> it_city = city_list.iterator();
			//		    先清空城市列表后再更新
			while (it_city.hasNext()) {
				try {
					pre.setString(1, it_city.next());
					pre.execute();
				} catch (SQLException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
			}
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
		}
	}

//	更新详细的城市列表
	public void updatemorecitylist() {
		boolean flag_success=true;
		try {
			UpdateDataUtil.updateCityList(true);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			flag_success=false;
			e.printStackTrace();
		}
		if (flag_success) {
			city_list = UpdateDataUtil.getCity_list();
			belongcity_list = UpdateDataUtil.getBelongcity_list();
			PreparedStatement pre = null;
			PreparedStatement pre1 = null;
			try {
				pre1 = (PreparedStatement) conn
						.prepareCall("truncate table morecitylist;");
				pre = (PreparedStatement) conn
						.prepareStatement("insert into morecitylist(cityname,belongcity) values (?,?);");
				pre1.execute();
			} catch (SQLException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
			Iterator<String> it_city = city_list.iterator();
			Iterator<String> it_belongcity = belongcity_list.iterator();
			//		    先清空城市列表后再更新
			while (it_city.hasNext()) {
				try {
					pre.setString(1, it_city.next());
					pre.setString(2, it_belongcity.next());
					pre.execute();
				} catch (SQLException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
			}
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
		}
	}
	
//	查看历史数据（根据地点，时间来选择，二者都确定或者只有其一都可）
	private void checkHistoryData(String cityname,Timestamp timestamp_start,Timestamp timestamp_end) {
		// TODO 自动生成的方法存根
		
		if(cityname!=null&&timestamp_start!=null&&timestamp_end!=null){
			try {
				conn=DatabaseUtil.getConn();
//				PreparedStatement pre=(PreparedStatement) conn.prepareStatement("select * from weather where cityname= ? and time_stamp= ?;");
				PreparedStatement pre=(PreparedStatement) conn.prepareStatement("select * from weather where cityname= ? and time_stamp>= ? and time_stamp<= ?;");
				pre.setString(1, cityname);
				pre.setTimestamp(2, timestamp_start);
				pre.setTimestamp(3, timestamp_end);
				chartListener=new CreateChartListener();
				reloadUI(pre,true);//true代表绑定“生成图表”监听器
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}else if(cityname!=null&&timestamp_start!=null){
			try {
				conn=DatabaseUtil.getConn();
				PreparedStatement pre=(PreparedStatement) conn.prepareStatement("select * from weather where cityname= ? and time_stamp>= ?;");
				pre.setString(1, cityname);
				pre.setTimestamp(2, timestamp_start);
				chartListener=new CreateChartListener();
				reloadUI(pre,true);
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}else if(cityname!=null&&timestamp_end!=null){
			try {
				conn=DatabaseUtil.getConn();
				PreparedStatement pre=(PreparedStatement) conn.prepareStatement("select * from weather where cityname= ? and time_stamp<= ?;");
				pre.setString(1, cityname);
				pre.setTimestamp(2, timestamp_end);
				chartListener=new CreateChartListener();
				reloadUI(pre,true);
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}else if(timestamp_start!=null&&timestamp_end!=null){
			try {
				conn=DatabaseUtil.getConn();
				PreparedStatement pre=(PreparedStatement) conn.prepareStatement("select * from weather where time_stamp>= ? and time_stamp<= ?;");
				pre.setTimestamp(1, timestamp_start);
				pre.setTimestamp(2, timestamp_end);
				if(chartListener!=null){
					ji_createChart.removeActionListener(chartListener);
					chartListener=null;
				}
				reloadUI(pre,false);
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}else if(timestamp_start!=null){
			try {
				conn=DatabaseUtil.getConn();
				PreparedStatement pre=(PreparedStatement) conn.prepareStatement("select * from weather where time_stamp>= ? ;");
				pre.setTimestamp(1, timestamp_start);
				if(chartListener!=null){
					ji_createChart.removeActionListener(chartListener);
					chartListener=null;
				}
				reloadUI(pre,false);
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}else if(timestamp_end!=null){
			try {
				conn=DatabaseUtil.getConn();
				PreparedStatement pre=(PreparedStatement) conn.prepareStatement("select * from weather where time_stamp<= ? ;");
				pre.setTimestamp(1, timestamp_end);
				if(chartListener!=null){
					ji_createChart.removeActionListener(chartListener);
					chartListener=null;
				}
				reloadUI(pre,false);
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}else if(cityname!=null){
			try {
				conn=DatabaseUtil.getConn();
				PreparedStatement pre=(PreparedStatement) conn.prepareStatement("select * from weather where cityname= ?;");
				pre.setTimestamp(1, timestamp_end);
				chartListener=new CreateChartListener();
				reloadUI(pre,true);
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}else{
			try {
				conn=DatabaseUtil.getConn();
				PreparedStatement pre=(PreparedStatement) conn.prepareStatement("select * from weather;");
				if(chartListener!=null){
					ji_createChart.removeActionListener(chartListener);
					chartListener=null;
				}
				reloadUI(pre,false);
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}
/**
 * @param pre要执行的查询语句
 * @throws SQLException
 */
    public void reloadUI(PreparedStatement pre,boolean flag_createable) throws SQLException {
		ResultSet rs = pre.executeQuery();
		rs.last();
		int size = rs.getRow();
		rs.beforeFirst();
		int count = 0;
		int[] aqi_array = new int[size];
		String[] city_array = new String[size];
		double[] pm2_5_array = new double[size];
		double[] pm2_5_24h_array = new double[size];
		String[] time_point_array = new String[size];
		String[] suggest_array = new String[size];
		double[] pre_pm2_5_array = new double[size];
		String[] weather_array = new String[size];
		String[] preWeather_array = new String[size];
		while (rs.next()) {
			aqi_array[count] = rs.getInt(rs.findColumn("aqi"));
			city_array[count] = rs.getString(rs.findColumn("cityname"));
			pm2_5_array[count] = rs.getDouble(rs.findColumn("pm2_5"));
			time_point_array[count] = rs.getString(rs.findColumn("time_stamp"));
			suggest_array[count] = rs.getString(rs.findColumn("suggest"));
			pre_pm2_5_array[count] = rs.getDouble(rs.findColumn("prepm2_5"));
			weather_array[count] = rs.getString(rs.findColumn("weather_now"));
			preWeather_array[count] = rs.getString(rs
					.findColumn("weather_forecast"));
			count++;
		}
		jp_citiesData.removeAll();
		GridLayout gl = new GridLayout(0, 8, 1, 15);
		jp_citiesData.setLayout(gl);
		// 初始化属性值显示框
		jl_cityList =new JLabel[city_array.length];
		jt_aqi = new JTextField[city_array.length];
		jt_pm = new JTextField[city_array.length];
		jt_prePm = new JTextField[city_array.length];
		jt_preWeather = new JTextField[city_array.length];
		jt_suggest = new JTextArea[city_array.length];
		jt_time = new JTextField[city_array.length];
		jt_weather = new JTextField[city_array.length];
		for (int i = 0; i < city_array.length; i++) {
			jt_aqi[i] = new JTextField("AQI值");
			jt_pm[i] = new JTextField("PM2.5");
			jt_prePm[i] = new JTextField("PM2.5预测值");
			jt_preWeather[i] = new JTextField("天气预测值");
			jt_suggest[i] = new JTextArea("出行建议");
			jt_time[i] = new JTextField("时间戳");
			jt_weather[i] = new JTextField("当前天气");
		}
		for (int i = 0; i < city_array.length; i++) {
			if (city_array[i] != null) {
				jl_cityList[i] = new JLabel(city_array[i]);
				jt_time[i].setText(time_point_array[i]);
				jt_time[i].setEditable(false);
				jt_weather[i].setText(weather_array[i]);
				jt_weather[i].setEditable(false);
				jt_pm[i].setText(pm2_5_array[i] + "");
				jt_pm[i].setEditable(false);
				jt_aqi[i].setText(aqi_array[i] + "");
				jt_aqi[i].setEditable(false);
				jt_preWeather[i].setText(preWeather_array[i]);
				jt_preWeather[i].setEditable(false);
				jt_prePm[i].setText(pre_pm2_5_array[i] + "");
				jt_prePm[i].setEditable(false);
				jt_suggest[i].setText(suggest_array[i]);
				jt_suggest[i].setEditable(false);
				jp_citiesData.add(jl_cityList[i]);
				jp_citiesData.add(jt_time[i]);
				jp_citiesData.add(jt_weather[i]);
				jp_citiesData.add(jt_pm[i]);
				jp_citiesData.add(jt_aqi[i]);
				jp_citiesData.add(jt_preWeather[i]);
				jp_citiesData.add(jt_prePm[i]);
				jp_citiesData.add(jt_suggest[i]);
			}
		}
		onDraw(MyFrame.this.getContentPane());
		if(flag_createable){
			chartListener=new CreateChartListener();
			chartListener.setTime_point_array(time_point_array);
			chartListener.setAqi_array(aqi_array);
			chartListener.setPm2_5_array(pm2_5_array);
			ji_createChart.addActionListener(chartListener);
		}
	}
    
    class CreateChartListener implements ActionListener{
    	private String[]time_point_array=null;
        private double[]pm2_5_array=null;
        private int[]aqi_array=null;
        
		/**
		 * @return time_point_array
		 */
		public String[] getTime_point_array() {
			return time_point_array;
		}

		/**
		 * @param time_point_array 要设置的 time_point_array
		 */
		public void setTime_point_array(String[] time_point_array) {
			this.time_point_array = time_point_array;
		}

		/**
		 * @return pm2_5_array
		 */
		public double[] getPm2_5_array() {
			return pm2_5_array;
		}

		/**
		 * @param pm2_5_array 要设置的 pm2_5_array
		 */
		public void setPm2_5_array(double[] pm2_5_array) {
			this.pm2_5_array = pm2_5_array;
		}

		/**
		 * @return aqi_array
		 */
		public int[] getAqi_array() {
			return aqi_array;
		}

		/**
		 * @param aqi_array 要设置的 aqi_array
		 */
		public void setAqi_array(int[] aqi_array) {
			this.aqi_array = aqi_array;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO 自动生成的方法存根
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO 自动生成的方法存根
					CreateView.createChart(time_point_array, pm2_5_array, aqi_array);
				}
			}).start();
		}
    	
    }
}











