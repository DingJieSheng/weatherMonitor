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
	 * @param receiver邮件接收者
	 * @param mailtext邮件内容
	 */
	public static boolean SendMail(String receiver,String mailtext){
		// 设置邮件内容
				MimeMessageDTO mimeDTO = new MimeMessageDTO();
				mimeDTO.setSentDate(new Date());
				mimeDTO.setSubject("中国软件杯大赛——矿大");
				mimeDTO.setText(mailtext+receiver);

//				// 发送单邮件
				if (MailUtil.sendEmail(userName, password, receiver, mimeDTO)) {
//					JOptionPane.showMessageDialog(frame, "邮件发送成功！");
					return true;
				} else {
//					JOptionPane.showMessageDialog(frame, "邮件发送失败!!!");
					return false;
				}
				// 发送单邮件(附件)
//				List<String> filepath=new ArrayList<String>();
//				filepath.add("D:/temple.xls");
//				filepath.add("D:/test.xls");
//				if (MailUtil.sendEmailByFile(userName, password, receiver, mimeDTO,filepath)) {
//					JOptionPane.showMessageDialog(frame, "邮件发送成功！");
//				} else {
//					JOptionPane.showMessageDialog(frame, "邮件发送失败!!!");
//				}
				// 群发邮件
//				receiver = "*******@qq.com,************@qq.com";
//				if (MailUtil.sendGroupEmail(userName, password, receiver, mimeDTO)) {
//					JOptionPane.showMessageDialog(frame, "邮件发送成功！");
//				} else {
//					JOptionPane.showMessageDialog(frame, "邮件发送失败!!!");
//				}
//				// 群发邮件(附件)
//				if (MailUtil.sendGroupEmailByFile(userName, password, receiver, mimeDTO,filepath)) {
//					JOptionPane.showMessageDialog(frame, "邮件发送成功！");
//				} else {
//					JOptionPane.showMessageDialog(frame, "邮件发送失败!!!");
//				}
	}

}
