/**
 * 
 */
package mainSystem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
//		this.iconImage=image;可能图片大小不合适有待改正
		init();
		registerListener();
	}
	private static boolean flag_firstload=true;
	/*
	 * 图标
	 */
	private Image iconImage=null;
	/*
	 * 加载提示框
	 */
	private JDialog loadingdialog=null;
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
	}
	/*
	 *初始化函数 
	 */
	public void init() {
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
		ji_export=new JMenuItem("导出数据");
		ji_hitory=new JMenuItem("历史数据");
		ji_cityList=new JMenuItem("更新城市列表");
		ji_morecityList=new JMenuItem("更新详细城市列表");
		jm_file=new JMenu("文件");
		jm_file.add(ji_export);
		jm_tools=new JMenu("工具");
		jm_tools.add(ji_cityList);
		jm_tools.add(ji_morecityList);
		jm_edit=new JMenu("编辑");
		jm_help=new JMenu("帮助");
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
		jl_suggest=new JLabel("出行建议",JLabel.CENTER);
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
		loadingdialog=new JDialog(MyFrame.this, "提示");
		Container errorcon=loadingdialog.getContentPane();
		JTextArea errorjt=new JTextArea("正在加载数据中，请稍候......");
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
		setloadingDialog();
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
//		获取当前数据的时间戳
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		SimpleDateFormat df1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Timestamp tmsp=null;
		if(internet){
			tmsp=Timestamp.valueOf(time_point_array[17]);
		}else{
			tmsp=Timestamp.valueOf(time_point_array[17]);
		}
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
								.prepareStatement("insert into weather (address, time_stamp, aqi, pm2_5, prepm2_5, weather_now, weather_forecast, suggest) values(?,?,?,?,?,?,?,?)");
						preparedStatement.setString(1, city_array[i]);
						//					df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
						df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						preparedStatement.setTimestamp(2,
								Timestamp.valueOf(time_point_array[17]));//北京市
						preparedStatement.setDouble(3, aqi_array[i]);
						preparedStatement.setDouble(4, pm2_5_array[i]);
						preparedStatement.setDouble(5, 0);//此处应该是pre_pm2_5_array[i]，预测模块实现后修改
						preparedStatement.setString(6, weather_array[i]);
						preparedStatement.setString(7, preweather_array[i]);
						preparedStatement.setString(8, null);//此处应该是suggest_array[i]，项目完善后修改
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
		loadingdialog.dispose();
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
		this.setSize(1000, 600);
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
			try {
				updateData();
			} catch (Exception e1) {
				// TODO 自动生成的 catch 块
				errorDialog(e1);
				e1.printStackTrace();
			}
		}else if(source.equals("预测数据")){
//			有待后期完善-------------------------------------------------------------------
		}else if(source.equals("发送通知")){
//			有待后期完善-------------------------------------------------------------------
		}else if(source.equals("导出数据")){
//			有待后期完善-------------------------------------------------------------------
		}else if(source.equals("历史数据")){
			checkHistoryData();
		}else if(source.equals("更新城市列表")){
			setloadingDialog();
			updatecitylist();
			loadingdialog.dispose();;
		}else if(source.equals("更新详细城市列表")){
			setloadingDialog();
			updatemorecitylist();
			loadingdialog.dispose();
		}
	}
	public void updatecitylist() {
		UpdateDataUtil.updateCityList(false);
		city_list=UpdateDataUtil.getCity_list();
		PreparedStatement pre=null;
		PreparedStatement pre1=null;
		try {
			pre1=(PreparedStatement) conn.prepareStatement("truncate table citylist;");
			pre=(PreparedStatement) conn.prepareStatement("insert into citylist(cityname) values (?);");
			pre1.execute();
		} catch (SQLException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
		Iterator<String> it_city=city_list.iterator();
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
			if(conn!=null&&!conn.isClosed()){
				conn.close();
			}
		} catch (SQLException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
	}

	public void updatemorecitylist() {
		UpdateDataUtil.updateCityList(true);
		city_list=UpdateDataUtil.getCity_list();
		belongcity_list=UpdateDataUtil.getBelongcity_list();
		PreparedStatement pre=null;
		PreparedStatement pre1=null;
		try {
			pre1=(PreparedStatement) conn.prepareCall("truncate table morecitylist;");
			pre=(PreparedStatement) conn.prepareStatement("insert into morecitylist(cityname,belongcity) values (?,?);");
			pre1.execute();
		} catch (SQLException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
		Iterator<String> it_city=city_list.iterator();
		Iterator<String> it_morecity=belongcity_list.iterator();
//		    先清空城市列表后再更新
		while (it_city.hasNext()) {
			try {
				pre.setString(1, it_city.next());
				pre.setString(2, it_morecity.next());
				pre.execute();
			} catch (SQLException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
		}
		try {
			if(conn!=null&&!conn.isClosed()){
				conn.close();
			}
		} catch (SQLException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
	}
	
//	查看历史数据
	private void checkHistoryData() {
		// TODO 自动生成的方法存根
		
	}
}











