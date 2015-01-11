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
		String mail_from = Constant.mailAddress; // mailbox ���͵��� title ����
													// content �ʼ�����
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
//			"<title>���׳������</title><form  name=\"form1\" action=\"QueryCars\" method=\"post\" ><select style='width:130px;' name=\"queryDays\" id=\"queryDays\"><option value=\"0\">����</option><option value=\"1\">����</option><option value=\"2\">ǰ��</option><option value=\"3\">��ǰ��</option><option value=\"4\">4��ǰ</option><option value=\"5\">5��ǰ</option><option value=\"6\">6��ǰ</option><option value=\"7\">һ��ǰ</option></select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type=\"submit\" name=\"button\" id=\"button\" value=\"  ��ѯ  \">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2014-11-08 09:54:32</form><br><script>document.getElementById(\"queryDays\").value = \"1\";</script><table width=\"100%\" border=\"1\" cellspacing=\"1\" cellpadding=\"1\"><tr align=\"center\"  class=\"t1\"><td height=\"25\" bgcolor=\"#D5E4F4\"><strong>�ն�ID/����</strong></td><td bgcolor=\"#D5E4F4\"><strong>��˾</strong></td><td bgcolor=\"#D5E4F4\"><strong>���ߴ���</strong></td><td bgcolor=\"#D5E4F4\"><strong>����ʱ��</strong></td><td bgcolor=\"#D5E4F4\"><strong>����ʱ��</strong></td><td bgcolor=\"#D5E4F4\"><strong>����ʱ��ռ��%</strong></td><td bgcolor=\"#D5E4F4\"><strong>�������km</strong></td><td bgcolor=\"#D5E4F4\"><strong>�쳣��¼��ϸ</strong></td><td bgcolor=\"#D5E4F4\"><strong>ͣ����¼��ϸ</strong></td></tr><tr  align=\"center\"><td height=\"25\" align=\"left\">��A5C711/30</td><td>��������</td><td>8</td><td>13Сʱ20��35��</td><td>10Сʱ39��24��</td><td>55.6</td><td>550802.385</td><td>�鿴��ϸ</td><td>�鿴��ϸ</td><td></tr><tr bgcolor=\"#D5E4ff\" align=\"center\"><td height=\"25\" align=\"left\">��A5C687/34</td><td>��������</td><td>11</td><td>02Сʱ22��54��</td><td>21Сʱ37��06��</td><td>9.9</td><td>989501.279</td><td>�鿴��ϸ</td><td>�鿴��ϸ</td><td></tr><tr  align=\"center\"><td height=\"25\" align=\"left\">��A5C501/36</td><td>��������</td><td>12</td><td>01Сʱ23��07��</td><td>22Сʱ36��53��</td><td>5.8</td><td>1240142.36</td><td>�鿴��ϸ</td><td>�鿴��ϸ</td><td></tr><tr bgcolor=\"#D5E4ff\" align=\"center\"><td height=\"25\" align=\"left\">��A5F919/32</td><td>��������</td><td>7</td><td>00Сʱ03��22��</td><td>23Сʱ56��38��</td><td>.2</td><td>19051.97</td><td>�鿴��ϸ</td><td>�鿴��ϸ</td><td></tr><tr  align=\"center\"><td height=\"25\" align=\"left\">��A-00007/7</td><td>���׿Ƽ�</td><td>2</td><td>23Сʱ59��07��</td><td>00Сʱ00��53��</td><td>99.9</td><td>0</td><td>�鿴��ϸ</td><td>�鿴��ϸ</td><td></tr><tr bgcolor=\"#D5E4ff\" align=\"center\"><td height=\"25\" align=\"left\">��A-00008/8</td><td>���׿Ƽ�</td><td>2</td><td>23Сʱ59��01��</td><td>00Сʱ00��59��</td><td>99.9</td><td>0</td><td>�鿴��ϸ</td><td>�鿴��ϸ</td><td></tr><tr  align=\"center\"><td height=\"25\" align=\"left\">��A-00001/1</td><td>���׿Ƽ�</td><td>1</td><td>23Сʱ57��53��</td><td>00Сʱ02��07��</td><td>99.9</td><td>0</td><td>�鿴��ϸ</td><td>�鿴��ϸ</td><td></tr><tr bgcolor=\"#D5E4ff\" align=\"center\"><td height=\"25\" align=\"left\">��A-00005/5</td><td>���׿Ƽ�</td><td>0</td><td>11Сʱ02��56��</td><td>12Сʱ57��03��</td><td>46</td><td>20083.544</td><td>�鿴��ϸ</td><td>�鿴��ϸ</td><td></tr><tr  align=\"center\"><td height=\"25\" align=\"left\">��A00006/6</td><td>���׿Ƽ�</td><td>11</td><td>03Сʱ41��35��</td><td>20Сʱ18��25��</td><td>15.4</td><td>0</td><td>�鿴��ϸ</td><td>�鿴��ϸ</td><td></tr><tr bgcolor=\"#D5E4ff\" align=\"center\"><td height=\"25\" align=\"left\">��A00012/12</td><td>���׿Ƽ�</td><td>2</td><td>00Сʱ02��27��</td><td>23Сʱ57��33��</td><td>.2</td><td>34507.153</td><td>�鿴��ϸ</td><td>�鿴��ϸ</td><td></tr> </table>";
		"<BODY style=\"MARGIN: 10px\"><table width=\"100%\" border=\"1\" cellspacing=\"1\" cellpadding=\"1\"><tr  align=\"center\"><td height=\"25\" align=\"left\">��A5C711/30</td><td>��������</td><td>8</td><td>13Сʱ20��35��</td><td>10Сʱ39��24��</td><td> 55.60</td><td>550802.385</td><td>�鿴��ϸ</td><td>�鿴��ϸ</td><td></tr><tr bgcolor=\"#D5E4ff\" align=\"center\"><td height=\"25\" align=\"left\">��A5C687/34</td><td>��������</td><td>11</td><td>02Сʱ22��54��</td><td>21Сʱ37��06��</td><td> 09.92</td><td>989501.279</td><td>�鿴��ϸ</td><td>�鿴��ϸ</td><td></tr><tr  align=\"center\"><td height=\"25\" align=\"left\">��A5C501/36</td><td>��������</td><td>12</td><td>01Сʱ23��07��</td><td>22Сʱ36��53��</td><td> 05.77</td><td>1240142.36</td><td>�鿴��ϸ</td><td>�鿴��ϸ</td><td></tr><tr bgcolor=\"#D5E4ff\" align=\"center\"><td height=\"25\" align=\"left\">��A5F919/32</td><td>��������</td><td>7</td><td>00Сʱ03��22��</td><td>23Сʱ56��38��</td><td> 00.23</td><td>19051.97</td><td>�鿴��ϸ</td><td>�鿴��ϸ</td><td></tr><tr  align=\"center\"><td height=\"25\" align=\"left\">��A-00007/7</td><td>���׿Ƽ�</td><td>2</td><td>23Сʱ59��07��</td><td>00Сʱ00��53��</td><td> 99.94</td><td>0</td><td>�鿴��ϸ</td><td>�鿴��ϸ</td><td></tr><tr bgcolor=\"#D5E4ff\" align=\"center\"><td height=\"25\" align=\"left\">��A-00008/8</td><td>���׿Ƽ�</td><td>2</td><td>23Сʱ59��01��</td><td>00Сʱ00��59��</td><td> 99.93</td><td>0</td><td>�鿴��ϸ</td><td>�鿴��ϸ</td><td></tr><tr  align=\"center\"><td height=\"25\" align=\"left\">��A-00001/1</td><td>���׿Ƽ�</td><td>1</td><td>23Сʱ57��53��</td><td>00Сʱ02��07��</td><td> 99.85</td><td>0</td><td>�鿴��ϸ</td><td>�鿴��ϸ</td><td></tr><tr bgcolor=\"#D5E4ff\" align=\"center\"><td height=\"25\" align=\"left\">��A-00005/5</td><td>���׿Ƽ�</td><td>0</td><td>11Сʱ02��56��</td><td>12Сʱ57��03��</td><td> 46.04</td><td>20083.544</td><td>�鿴��ϸ</td><td>�鿴��ϸ</td><td></tr><tr  align=\"center\"><td height=\"25\" align=\"left\">��A00006/6</td><td>���׿Ƽ�</td><td>11</td><td>03Сʱ41��35��</td><td>20Сʱ18��25��</td><td> 15.39</td><td>0</td><td>�鿴��ϸ</td><td>�鿴��ϸ</td><td></tr><tr bgcolor=\"#D5E4ff\" align=\"center\"><td height=\"25\" align=\"left\">��A00012/12</td><td>���׿Ƽ�</td><td>2</td><td>00Сʱ02��27��</td><td>23Сʱ57��33��</td><td> 00.17</td><td>34507.153</td><td>�鿴��ϸ</td><td>�鿴��ϸ</td><td></tr></table></BODY>";
		new SendMail().send("417389915@qq.com", "java test3", contencs );
//		new SendMail().send("linym3k@126.com", "java test2", contencs );
//		new sendmail().send("gymonitor@126.com", "java2", contencs );
		System.err.println("end");
	}
}