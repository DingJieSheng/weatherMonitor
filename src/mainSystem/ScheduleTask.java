/**
 * 
 */
package mainSystem;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JDialog;
import javax.swing.JTextArea;

/**
 * @author ac 盛鼎杰，孟靖宇，方远
 *此类是定时任务调度类，主要管理各项定时调度任务，比如数据更新，消息通知等
 */
public class ScheduleTask {
	public static void updateScheduel(final MyFrame myFrame){
		Runnable up_Runnable=new Runnable() {
			
			@Override
			public void run() {
				// TODO 自动生成的方法存根
				try {
					myFrame.updateData();
					myFrame.onDraw(myFrame.getContentPane());
					myFrame.prediction();
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		};
		
		Runnable ml_Runnable = new Runnable() {

			@Override
			public void run() {
				// TODO 自动生成的方法存根
				try {
					myFrame.exportData();
					myFrame.machineLearning();
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		};
		
		ScheduledExecutorService update_Service=Executors.newSingleThreadScheduledExecutor();
		update_Service.scheduleAtFixedRate(up_Runnable, 0, 30, TimeUnit.MINUTES);//设定的更新数据时间间隔为30分钟
		update_Service.scheduleAtFixedRate(ml_Runnable, 0, 1, TimeUnit.DAYS);//设定的学习时间间隔为1天
		
	}

}
