/**
 * 
 */
package mainSystem;

import java.util.Date;

import javax.swing.JOptionPane;
import cn.yuhi.dto.MimeMessageDTO;
import cn.yuhi.util.MailUtil;

/**
 * @author ac
 *
 */
public class SendMailUtil {
	private static String userName="sdj1996043@163.com";
	private static String password="sdj08143449";
	/**
	 * 
	 * @param receiver�ʼ�������
	 * @param mailtext�ʼ�����
	 */
	public static boolean SendMail(String receiver,String mailtext){
		// �����ʼ�����
				MimeMessageDTO mimeDTO = new MimeMessageDTO();
				mimeDTO.setSentDate(new Date());
				mimeDTO.setSubject("�й�����������������");
				mimeDTO.setText(mailtext+receiver);

//				// ���͵��ʼ�
				if (MailUtil.sendEmail(userName, password, receiver, mimeDTO)) {
//					JOptionPane.showMessageDialog(frame, "�ʼ����ͳɹ���");
					return true;
				} else {
//					JOptionPane.showMessageDialog(frame, "�ʼ�����ʧ��!!!");
					return false;
				}
				// ���͵��ʼ�(����)
//				List<String> filepath=new ArrayList<String>();
//				filepath.add("D:/temple.xls");
//				filepath.add("D:/test.xls");
//				if (MailUtil.sendEmailByFile(userName, password, receiver, mimeDTO,filepath)) {
//					JOptionPane.showMessageDialog(frame, "�ʼ����ͳɹ���");
//				} else {
//					JOptionPane.showMessageDialog(frame, "�ʼ�����ʧ��!!!");
//				}
				// Ⱥ���ʼ�
//				receiver = "*******@qq.com,************@qq.com";
//				if (MailUtil.sendGroupEmail(userName, password, receiver, mimeDTO)) {
//					JOptionPane.showMessageDialog(frame, "�ʼ����ͳɹ���");
//				} else {
//					JOptionPane.showMessageDialog(frame, "�ʼ�����ʧ��!!!");
//				}
//				// Ⱥ���ʼ�(����)
//				if (MailUtil.sendGroupEmailByFile(userName, password, receiver, mimeDTO,filepath)) {
//					JOptionPane.showMessageDialog(frame, "�ʼ����ͳɹ���");
//				} else {
//					JOptionPane.showMessageDialog(frame, "�ʼ�����ʧ��!!!");
//				}
	}

}
