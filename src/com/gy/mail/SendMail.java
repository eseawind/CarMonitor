package com.gy.mail;

import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {
	public void send(String mailbox, String title, String content) {
		String mail_from = Constant.mailAddress; // mailbox 发送到哪 title 标题
													// content 邮件内容
		try {
			Properties props = new Properties();
			props.put("nail.smtp.host", Constant.mailServer);
			props.put("mail.smtp.auth", "true");
			Session s = Session.getInstance(props);
			s.setDebug(false);
			MimeMessage message = new MimeMessage(s);
			InternetAddress from = new InternetAddress(mail_from);
			message.setFrom(from);
			InternetAddress to = new InternetAddress(mailbox);
			message.setRecipient(Message.RecipientType.TO, to);
			message.setSubject(title);
			message.setText(content);
			message.setContent(content, "text/html;charset=gbk");
			message.setSentDate(new Date());
			message.saveChanges(); 
			Transport transport = s.getTransport("smtp");
			transport.connect(Constant.mailServer, Constant.mailCount,
					Constant.mailPassword);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String contencs =
//			"111111111111111111"; 
//			"<title>古易车辆监控</title><form  name=\"form1\" action=\"QueryCars\" method=\"post\" ><select style='width:130px;' name=\"queryDays\" id=\"queryDays\"><option value=\"0\">今天</option><option value=\"1\">昨天</option><option value=\"2\">前天</option><option value=\"3\">大前天</option><option value=\"4\">4天前</option><option value=\"5\">5天前</option><option value=\"6\">6天前</option><option value=\"7\">一周前</option></select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type=\"submit\" name=\"button\" id=\"button\" value=\"  查询  \">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2014-11-08 09:54:32</form><br><script>document.getElementById(\"queryDays\").value = \"1\";</script><table width=\"100%\" border=\"1\" cellspacing=\"1\" cellpadding=\"1\"><tr align=\"center\"  class=\"t1\"><td height=\"25\" bgcolor=\"#D5E4F4\"><strong>终端ID/车牌</strong></td><td bgcolor=\"#D5E4F4\"><strong>公司</strong></td><td bgcolor=\"#D5E4F4\"><strong>离线次数</strong></td><td bgcolor=\"#D5E4F4\"><strong>离线时长</strong></td><td bgcolor=\"#D5E4F4\"><strong>上线时长</strong></td><td bgcolor=\"#D5E4F4\"><strong>离线时间占比%</strong></td><td bgcolor=\"#D5E4F4\"><strong>当日里程km</strong></td><td bgcolor=\"#D5E4F4\"><strong>异常记录明细</strong></td><td bgcolor=\"#D5E4F4\"><strong>停车记录明细</strong></td></tr><tr  align=\"center\"><td height=\"25\" align=\"left\">闽A5C711/30</td><td>合利物流</td><td>8</td><td>13小时20分35秒</td><td>10小时39分24秒</td><td>55.6</td><td>550802.385</td><td>查看明细</td><td>查看明细</td><td></tr><tr bgcolor=\"#D5E4ff\" align=\"center\"><td height=\"25\" align=\"left\">闽A5C687/34</td><td>合利物流</td><td>11</td><td>02小时22分54秒</td><td>21小时37分06秒</td><td>9.9</td><td>989501.279</td><td>查看明细</td><td>查看明细</td><td></tr><tr  align=\"center\"><td height=\"25\" align=\"left\">闽A5C501/36</td><td>合利物流</td><td>12</td><td>01小时23分07秒</td><td>22小时36分53秒</td><td>5.8</td><td>1240142.36</td><td>查看明细</td><td>查看明细</td><td></tr><tr bgcolor=\"#D5E4ff\" align=\"center\"><td height=\"25\" align=\"left\">闽A5F919/32</td><td>合利物流</td><td>7</td><td>00小时03分22秒</td><td>23小时56分38秒</td><td>.2</td><td>19051.97</td><td>查看明细</td><td>查看明细</td><td></tr><tr  align=\"center\"><td height=\"25\" align=\"left\">闽A-00007/7</td><td>古易科技</td><td>2</td><td>23小时59分07秒</td><td>00小时00分53秒</td><td>99.9</td><td>0</td><td>查看明细</td><td>查看明细</td><td></tr><tr bgcolor=\"#D5E4ff\" align=\"center\"><td height=\"25\" align=\"left\">闽A-00008/8</td><td>古易科技</td><td>2</td><td>23小时59分01秒</td><td>00小时00分59秒</td><td>99.9</td><td>0</td><td>查看明细</td><td>查看明细</td><td></tr><tr  align=\"center\"><td height=\"25\" align=\"left\">闽A-00001/1</td><td>古易科技</td><td>1</td><td>23小时57分53秒</td><td>00小时02分07秒</td><td>99.9</td><td>0</td><td>查看明细</td><td>查看明细</td><td></tr><tr bgcolor=\"#D5E4ff\" align=\"center\"><td height=\"25\" align=\"left\">闽A-00005/5</td><td>古易科技</td><td>0</td><td>11小时02分56秒</td><td>12小时57分03秒</td><td>46</td><td>20083.544</td><td>查看明细</td><td>查看明细</td><td></tr><tr  align=\"center\"><td height=\"25\" align=\"left\">闽A00006/6</td><td>古易科技</td><td>11</td><td>03小时41分35秒</td><td>20小时18分25秒</td><td>15.4</td><td>0</td><td>查看明细</td><td>查看明细</td><td></tr><tr bgcolor=\"#D5E4ff\" align=\"center\"><td height=\"25\" align=\"left\">闽A00012/12</td><td>古易科技</td><td>2</td><td>00小时02分27秒</td><td>23小时57分33秒</td><td>.2</td><td>34507.153</td><td>查看明细</td><td>查看明细</td><td></tr> </table>";
		"<BODY style=\"MARGIN: 10px\"><table width=\"100%\" border=\"1\" cellspacing=\"1\" cellpadding=\"1\"><tr  align=\"center\"><td height=\"25\" align=\"left\">闽A5C711/30</td><td>合利物流</td><td>8</td><td>13小时20分35秒</td><td>10小时39分24秒</td><td> 55.60</td><td>550802.385</td><td>查看明细</td><td>查看明细</td><td></tr><tr bgcolor=\"#D5E4ff\" align=\"center\"><td height=\"25\" align=\"left\">闽A5C687/34</td><td>合利物流</td><td>11</td><td>02小时22分54秒</td><td>21小时37分06秒</td><td> 09.92</td><td>989501.279</td><td>查看明细</td><td>查看明细</td><td></tr><tr  align=\"center\"><td height=\"25\" align=\"left\">闽A5C501/36</td><td>合利物流</td><td>12</td><td>01小时23分07秒</td><td>22小时36分53秒</td><td> 05.77</td><td>1240142.36</td><td>查看明细</td><td>查看明细</td><td></tr><tr bgcolor=\"#D5E4ff\" align=\"center\"><td height=\"25\" align=\"left\">闽A5F919/32</td><td>合利物流</td><td>7</td><td>00小时03分22秒</td><td>23小时56分38秒</td><td> 00.23</td><td>19051.97</td><td>查看明细</td><td>查看明细</td><td></tr><tr  align=\"center\"><td height=\"25\" align=\"left\">闽A-00007/7</td><td>古易科技</td><td>2</td><td>23小时59分07秒</td><td>00小时00分53秒</td><td> 99.94</td><td>0</td><td>查看明细</td><td>查看明细</td><td></tr><tr bgcolor=\"#D5E4ff\" align=\"center\"><td height=\"25\" align=\"left\">闽A-00008/8</td><td>古易科技</td><td>2</td><td>23小时59分01秒</td><td>00小时00分59秒</td><td> 99.93</td><td>0</td><td>查看明细</td><td>查看明细</td><td></tr><tr  align=\"center\"><td height=\"25\" align=\"left\">闽A-00001/1</td><td>古易科技</td><td>1</td><td>23小时57分53秒</td><td>00小时02分07秒</td><td> 99.85</td><td>0</td><td>查看明细</td><td>查看明细</td><td></tr><tr bgcolor=\"#D5E4ff\" align=\"center\"><td height=\"25\" align=\"left\">闽A-00005/5</td><td>古易科技</td><td>0</td><td>11小时02分56秒</td><td>12小时57分03秒</td><td> 46.04</td><td>20083.544</td><td>查看明细</td><td>查看明细</td><td></tr><tr  align=\"center\"><td height=\"25\" align=\"left\">闽A00006/6</td><td>古易科技</td><td>11</td><td>03小时41分35秒</td><td>20小时18分25秒</td><td> 15.39</td><td>0</td><td>查看明细</td><td>查看明细</td><td></tr><tr bgcolor=\"#D5E4ff\" align=\"center\"><td height=\"25\" align=\"left\">闽A00012/12</td><td>古易科技</td><td>2</td><td>00小时02分27秒</td><td>23小时57分33秒</td><td> 00.17</td><td>34507.153</td><td>查看明细</td><td>查看明细</td><td></tr></table></BODY>";
		new SendMail().send("417389915@qq.com", "java test3", contencs );
//		new SendMail().send("linym3k@126.com", "java test2", contencs );
//		new sendmail().send("gymonitor@126.com", "java2", contencs );
		System.err.println("end");
	}
}