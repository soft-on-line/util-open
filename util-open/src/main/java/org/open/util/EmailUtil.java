package org.open.util;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.mail.HtmlEmail;

/**
 * email处理类
 * @author 覃芝鹏
 * @version $Id: EmailUtil.java,v 1.5 2008/09/03 02:52:40 moon Exp $
 */
public class EmailUtil
{
	private static final Log log = LogFactory.getLog(EmailUtil.class);
	
	/**
	 * 发送邮件函数
	 * @param smtpHost 发送方地址
	 * @param smtpUsername 发送方用户名
	 * @param stmpPasswd 发送方用户密码
	 * @param sendToEmailAddress 接受方地址
	 * @param title 邮件标题
	 * @param subject 邮件主题
	 * @param content 邮件内容
	 */
	public static boolean sendHtml(
			String smtpHost,
			String smtpUsername,
			String stmpPasswd,
			String sendToEmailAddress,
			String title,
			String subject,
			String content)
	{
		return EmailUtil.sendHtml(
				smtpHost, 
				smtpUsername, 
				stmpPasswd, 
				new String[]{sendToEmailAddress}, 
				title, 
				subject, 
				content);
	}
	
	/**
	 * 发送邮件函数
	 * @param smtpHost 发送方地址
	 * @param smtpUsername 发送方用户名
	 * @param stmpPasswd 发送方用户密码
	 * @param sendToEmailAddress 接受方地址
	 * @param title 邮件标题
	 * @param subject 邮件主题
	 * @param content 邮件内容
	 */
	public static boolean sendHtml(
			String smtpHost,
			String smtpUsername,
			String stmpPasswd,
			String[] sendToEmailAddress,
			String title,
			String subject,
			String content)
	{
		try {
			HtmlEmail email = new HtmlEmail();
			email.setHostName(smtpHost);
			email.setAuthentication(smtpUsername,stmpPasswd);
			email.setFrom(smtpUsername,title);
			for(int i=0;i<sendToEmailAddress.length;i++){
				email.addTo(sendToEmailAddress[i]);
				log.info("sendHtml email to "+sendToEmailAddress[i]);
			}
			email.setSubject(subject);
			email.setHtmlMsg(content);
			email.setTextMsg(content);
			email.setCharset("gbk");
			email.send();
			return true;
		}catch (Exception e) {
			log.error("send(...) error!=>",e);
			return false;
		} 
	}
	
	/**
	 * 发送邮件函数
	 * @param smtpHost 发送方地址
	 * @param smtpUsername 发送方用户名
	 * @param stmpPasswd 发送方用户密码
	 * @param sendToEmailAddress 接受方地址
	 * @param title 邮件标题
	 * @param subject 邮件主题
	 * @param content 邮件内容
	 */
	public static boolean sendHtml(
			String smtpHost,
			String smtpUsername,
			String stmpPasswd,
			List<String> sendToEmailAddress,
			String title,
			String subject,
			String content)
	{
		try {
			HtmlEmail email = new HtmlEmail();
			email.setHostName(smtpHost);
			email.setAuthentication(smtpUsername,stmpPasswd);
			email.setFrom(smtpUsername,title);
			for(int i=0;i<sendToEmailAddress.size();i++){
				email.addTo(sendToEmailAddress.get(i));
				log.info("sendHtml email to "+sendToEmailAddress.get(i));
			}
			email.setSubject(subject);
			email.setHtmlMsg(content);
			email.setTextMsg(content);
			email.setCharset("gbk");
			email.send();
			return true;
		}catch (Exception e) {
			log.error("send(...) error!=>",e);
			return false;
		} 
	}
	
}
