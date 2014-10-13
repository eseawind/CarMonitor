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

import com.gy.DAO.RealMonitorDAO;
import com.gy.Entity.CarMonitorEntity;
import com.gy.Entity.RealMonitorEntity;

public class RealMonitor extends HttpServlet {

	public RealMonitor() {
		// TODO Auto-generated constructor stub
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
		String querydays = request.getParameter("queryDays"); 
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter(); 	
 
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strdate = sd.format(new Date());
		out.append("<title>古易车辆监控</title>"); 
		out.append("<h1 align=\"left\" ><a href=\"/CarMonitor\">返回</a>" +
				"  &nbsp;&nbsp;&nbsp;<a href=\"/CarMonitor/RealMonitor\">刷新</a>"+
				"</h1>"); 
		
		out.append("<h2 align=\"center\">--终端实时数据监控--</h2 >");
			out.append("<table width=\"130%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
						"<tr align=\"center\"  class=\"t1\">"+
						"<td height=\"25\" bgcolor=\"#D5E4F4\"><strong>车牌</strong></td>"+ 
				 		"<td bgcolor=\"#D5E4F4\"><strong>公司</strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong>汇报时间</strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong>增量里程</strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong>线速度</strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong>gps速度</strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong>gps</strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong>百度gps</strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong>模拟油量</strong></td>" +				 		
				 		"<td bgcolor=\"#D5E4F4\"><strong>油量L</strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong>温度°C</strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong>终端状态</strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong>报警状态</strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong>车距</strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong>位置时间0200</strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong>GPS0200</strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong>终端状态0200</strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong>报警状态0200</strong></td>" +
				 		"</tr>"    
//				 		 
				 ); 
//			out.append(getRealInfo());
			out.append("<tr  align=\"center\"><td height=\"25\" align=\"left\">闽A-00001/1</td><td>1</td>" +
					"<td>2014-09-22 18:37:42.0</td><td>0</td><td>0</td><td>.04</td><td>119.2479,26.1044</td>" +
					"<td>119.2597,26.1072</td><td>0</td><td>0</td><td>0</td><td>786435</td><td>0</td><td>0</td>" +
					"<td>2014-10-11 18:10:33.0</td><td>119.247634,26.107976</td><td>786435</td><td>0</td><td></tr>");
			out.append(" </table>" );  
		out.flush();
		out.close();
	}

	public String getRealInfo(){
		String result="";
		RealMonitorDAO realdao = new RealMonitorDAO();
		List<RealMonitorEntity> reallist= realdao.getRealInfoList();
		String linecolor ="";
		int line=1;
		for (RealMonitorEntity real:reallist) {
				if (line%2==0) {
					linecolor="bgcolor=\"#D5E4ff\"";
				}else{
					linecolor="";
				}
				result =result+"<tr "+linecolor+ " align=\"center\">" +
				"<td height=\"25\" align=\"left\">"+  
				real.PLATENO +"</td><td>" +
				1 +"</td><td>" +
				real.LASTTIME +"</td><td>" +
				real.LINE_DIS +"</td><td>" +
				real.LINE_SPEED +"</td><td>" +
				real.GPS_SPEED +"</td><td>" +
				real.GPS +"</td><td>" +
				real.GPS_F +"</td><td>" +
				real.OILVALUE +"</td><td>" +
				real.OILMASS +"</td><td>" +
				real.tempvalue +"</td><td>" +
				real.TER_STATUS +"</td><td>" +
				real.WARN_TYPE +"</td><td>" +
				real.DIS_FRONT +"</td><td>" +
				real.POSTIME0200 +"</td><td>" +
				real.GPS0200 +"</td><td>" +
				real.TERSTATUS0200 +"</td><td>" +
				real.WARN0200 +"</td><td>" +
				"</tr>";
				if (line<2) {					
					System.err.println(result);
				}
				line++;	 
		}
		return result;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
