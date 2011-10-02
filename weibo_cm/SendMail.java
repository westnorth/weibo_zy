//SendMail.java
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
//import javax.activation.*;

public class SendMail {

	public SendMail()
	{
		
	}
	/**
	 * 
	 * @param customMailBoxAddress 发送方邮箱地址（如BlogJava2006@blog.com.cn.）
	 * @param username 发件人名称
	 * @param password 发件人密码
	 * @param serverMailBoxAddress 收件人邮箱地址
	 * @param strContent 信件内容
	 * @param attachmentPath 附件路径
	 * @param attachmentName 附件名
	 */
	public void send(String customMailBoxAddress, String username,
			String password, String serverMailBoxAddress, String strContent,
			String attachmentPath, String attachmentName) {
		// 这里面使用新浪作为发送邮件的邮件服务器，其他的smtp服务器可以到相关网站上查到。
		String host = "smtp.sina.com.cn";
		// 发送方邮箱地址（如BlogJava2006@blog.com.cn.）
		String from = customMailBoxAddress;
		// 收件人邮箱地址
		String to = serverMailBoxAddress;
		// 发送者的邮箱用户名
		String user = username;
		// 发送者的邮箱密码
		String ps = password;

		Properties props = new Properties();

		// 设置发送邮件的邮件服务器的属性（这里使用新浪的smtp服务器）
		props.put("mail.smtp.host", host);
		// 需要经过授权，也就是有户名和密码的校验，这样才能通过验证（一定要有//这一条）
		props.put("mail.smtp.auth", "true");

		// 用刚刚设置好的props对象构建一个session
		Session session = Session.getDefaultInstance(props);

		// 有了这句便可以在发送邮件的过程中在console处显示过程信息，供调试使
		// 用（有的时候网络连通性不够好，发送邮件可能会有延迟，在这里面会有所//提示，所以最好是加上这句，避免盲目的等待）
		session.setDebug(true);

		// 定义消息对象
		MimeMessage message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));
			message.setSubject("token");

			// 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
			Multipart multipart = new MimeMultipart();
			// 设置邮件的文本内容
			BodyPart contentPart = new MimeBodyPart();
			contentPart.setText(strContent);
			multipart.addBodyPart(contentPart);
			// 添加附件
//			BodyPart attachmentPart = new MimeBodyPart();
//			DataSource source = new FileDataSource(attachmentPath);
//			attachmentPart.setDataHandler(new DataHandler(source));
//			// 注意：下面定义的enc对象用来处理中文附件名，否则名称是中文的附件在邮箱里面显示的会是乱码，
//			sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
//			messageBodyPart.setFileName("=?GBK?B?"
//					+ enc.encode(attachmentName.getBytes()) + "?=");
//			multipart.addBodyPart(messageBodyPart);
//
//			// 将multipart对象放到message中
			message.setContent(multipart);
			// 发送邮件
			message.saveChanges();
			Transport transport = session.getTransport("smtp");
			transport.connect(host, username, password);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}