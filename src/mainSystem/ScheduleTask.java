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
 * @author ac ʢ���ܣ��Ͼ����Զ
 *�����Ƕ�ʱ��������࣬��Ҫ������ʱ�������񣬱������ݸ��£���Ϣ֪ͨ��
 */
public class ScheduleTask {
	public static void updateScheduel(final MyFrame myFrame){
		Runnable up_Runnable=new Runnable() {
			
			@Override
			public void run() {
				// TODO �Զ����ɵķ������
				try {
					myFrame.updateData();
					myFrame.onDraw(myFrame.getContentPane());
					myFrame.prediction();
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
			}
		};
		
		Runnable ml_Runnable = new Runnable() {

			@Override
			public void run() {
				// TODO �Զ����ɵķ������
				try {
					myFrame.exportData();
					myFrame.machineLearning();
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
			}
		};
		
		ScheduledExecutorService update_Service=Executors.newSingleThreadScheduledExecutor();
		update_Service.scheduleAtFixedRate(up_Runnable, 0, 30, TimeUnit.MINUTES);//�趨�ĸ�������ʱ����Ϊ30����
		update_Service.scheduleAtFixedRate(ml_Runnable, 0, 1, TimeUnit.DAYS);//�趨��ѧϰʱ����Ϊ1��
		
	}

}
