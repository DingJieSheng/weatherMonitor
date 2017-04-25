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
	public static void updateScheduel(MyFrame myFrame){
		Runnable up_Runnable=new Runnable() {
			
			@Override
			public void run() {
				// TODO �Զ����ɵķ������
				try {
					myFrame.updateData();
					myFrame.onDraw(myFrame.getContentPane());
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
			}
		};
		ScheduledExecutorService update_Service=Executors.newSingleThreadScheduledExecutor();
		update_Service.scheduleAtFixedRate(up_Runnable, 0, 60, TimeUnit.MINUTES);//�趨��ʱ����Ϊ60����
	}

}
