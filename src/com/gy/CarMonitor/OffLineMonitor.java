package com.gy.CarMonitor;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gy.DAO.OffLineDAO;
import com.gy.DAO.RealMonitorDAO;
import com.gy.Entity.OffLineStatEntity;
import com.gy.Entity.RealMonitorEntity;

public class OffLineMonitor extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public OffLineMonitor() { 
	}
	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException { 
		 
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String strquerydays = request.getParameter("querydays"); 	
		System.err.println("queryDays���ݹ�����ֵΪ��"+strquerydays);
		if( strquerydays ==null||strquerydays.equals("")  ){ 
			strquerydays="0";
		}
		int querydays = Integer.valueOf(strquerydays);
		
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strdate = sd.format(new Date());
		out.append("<title>���׳������</title>"); 
		out.append("<h2 align=\"left\" ><a href=\"/CarMonitor\">����</a>" +
				"  &nbsp;&nbsp;&nbsp;<a href=\"/CarMonitor/OffLineMonitor?querydays="+strquerydays+"\">ˢ��</a>"+
				"</h2>"); 
		out.append("<h2 align=\"center\">--�ն�����ͳ��"+"--</h2 >");
		out.append("<form  name=\"form1\" action=\"OffLineMonitor\" method=\"post\" >" +
					"<select style='width:130px;' name=\"querydays\" id=\"querydays\">" +
					"<option value=\"0\">����</option>" +
					"<option value=\"1\">����</option>" +
					"<option value=\"2\">ǰ��</option>" +
					"<option value=\"3\">��ǰ��</option>" +
					"<option value=\"4\">4��ǰ</option>" +
					"<option value=\"5\">5��ǰ</option>" +
					"<option value=\"6\">6��ǰ</option>" +
					"<option value=\"7\">һ��ǰ</option>" +
					"</select>"+
					"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+
					"<input type=\"submit\" name=\"button\" id=\"button\" value=\"  ��ѯ  \">" +				
					"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+strdate +
					"</form>");		
		out.append("<script>document.getElementById(\"querydays\").value = \""+ querydays+
		"\";</script>"  );		
			out.append("<table width=\"100%\" border=\"1\" cellspacing=\"1\" cellpadding=\"1\">"+
						"<tr align=\"center\"  class=\"t1\">"+
						"<td height=\"25\" bgcolor=\"#D5E4F4\"><strong>������˾-����-�ն�ID</strong></td>"+ 
				 		"<td bgcolor=\"#D5E4F4\"  ><strong>����ʱ��</strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"  ><strong>����ʱ�� </strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"  ><strong>���ߴ��� </strong></td>"  +
				 		"</tr>"    
//				 		 
				 ); 
			out.append(getOffLineInfo(querydays));
			out.append(" </table>" );  
		out.flush();
		out.close();
	}

	public String getOffLineInfo(int querydays){
		String result="";
		OffLineDAO offlineDAO = new OffLineDAO();
		List<OffLineStatEntity> offlist= offlineDAO.getOffLineStat(querydays);
		String linecolor ="";
		int line=1;
		for (OffLineStatEntity off:offlist) {
				if (line%2==0) {
					linecolor="bgcolor=\"#D5E4ff\"";
				}else{
					linecolor="";
				}
				result =result+"<tr "+linecolor+ " align=\"center\">" +
				"<td>"+off.cp_name+"-"+off.plate_no +"</td>" +
				"<td>" +getStrTimes(off.offSecends) +"</td>" +
				"<td>" + getStrTimes(off.onSeconds )+"</td>" +
				"<td>" + off.offcount +"</td>" +
				"</tr>";
//				if (line<2) {					
////					System.err.println(result);
//				}
				line++;	 
		}
		return result;
	}
	private String getStrTimes(String strtime){
		int times=0 ;
		try {
			times = Integer.valueOf(strtime);			
		} catch (Exception e) {
			return "��ֵ�쳣";
		}
		return times/3600+"ʱ"+ (times%3600)/60+"��"+ times%60+"��";
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
