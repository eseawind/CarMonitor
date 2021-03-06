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

import com.gy.DAO.OracleMonitorDAO;
import com.gy.Entity.CarMonitorEntity;
import com.gy.Entity.DBObjectEntity;
import com.gy.Entity.DBProcLogEntity;
import com.gy.Entity.OracleProcTaskEntity;

public class OracleMonitor extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
		out.append("<title>DB监控</title>");  
		out.append("<h1 align=\"left\" ><a href=\"/CarMonitor\">返回</a>" +
				"  &nbsp;&nbsp;&nbsp;<a href=\"/CarMonitor/OracleMonitor\">刷新</a>"+
				"</h1>"); 
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strdate = sd.format(new Date());		
		out.append("<h2 align=\"center\">--数据库定时任务监控--</h2 >"+"服务器时间:"+strdate);
		//job,what,last_date,last_sec,next_date,next_sec,interval
		out.append("<table width=\"100%\" border=\"1\" cellspacing=\"1\" cellpadding=\"1\">"+
				"<tr align=\"left\"  class=\"t1\">"+
				"<td height=\"25\" bgcolor=\"#D5E4F4\"><strong>任务ID</strong></td>"+ 
		 		"<td bgcolor=\"#D5E4F4\"><strong>任务名称</strong></td>" +
		 		"<td bgcolor=\"#D5E4F4\"><strong>上次执行时间1</strong></td>"+
		 		"<td bgcolor=\"#D5E4F4\"><strong>上次执行时间2</strong></td>"+
		 		"<td bgcolor=\"#D5E4F4\"><strong>任务执行频率</strong></td>"+
		 		"<td bgcolor=\"#D5E4F4\"><strong>下次执行时间1</strong></td>"+
		 		"<td bgcolor=\"#D5E4F4\"><strong>下次执行时间2</strong></td>"+
		 		"</tr>") ;	
		out.append(getDBTaskList()); 
		out.append("</table>");
		out.append("<h2 align=\"center\">--存储过程执行日志--</h2>");
		out.append("<table width=\"100%\" border=\"1\" cellspacing=\"1\" cellpadding=\"1\">"+
				"<tr align=\"center\"  class=\"t1\">"+
				"<td bgcolor=\"#D5E4F4\"><strong>任务名称</strong></td>"+ 
		 		"<td bgcolor=\"#D5E4F4\"><strong>执行时长</strong></td>" +
		 		"<td bgcolor=\"#D5E4F4\"><strong>开始时间</strong></td>"+
		 		"<td bgcolor=\"#D5E4F4\"><strong>结束时间</strong></td>"+
		 		"<td bgcolor=\"#D5E4F4\"><strong>执行结果</strong></td>"+
		 		"<td bgcolor=\"#D5E4F4\"><strong>备注</strong></td>"+
		 		"</tr>") ;	
		out.append(getProcLogList());  //存储过程日志		
		out.append("</table>");		
		
		//数据库对象监控 
		out.append("<h2 align=\"center\">--数据库无效对象监控--</h2>");
		out.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
				"<tr align=\"center\"  class=\"t1\">"+
				"<td height=\"25\" bgcolor=\"#D5E4F4\"><strong>状态</strong></td>"+ 
		 		"<td bgcolor=\"#D5E4F4\"><strong>对象类型</strong></td>" +
		 		"<td bgcolor=\"#D5E4F4\"><strong>对象名称</strong></td>"+ 
		 		"</tr>") ;
		out.append(getDBInvalidObject());		
		out.append("</table>");	
		out.append("<br><br><br>");	
		out.append("<h1 align=\"left\" ><a href=\"/CarMonitor\">返回</a>" +
					"&nbsp;&nbsp;&nbsp;<a href=\"/CarMonitor/OracleMonitor\">刷新</a>"+
					"</h1>"); 
		out.flush();
		out.close();		
	}
	/**
	 * 数据库无效对象监控
	 * */
	public String getDBInvalidObject(){
		OracleMonitorDAO omdao = new OracleMonitorDAO();
		List<DBObjectEntity>  listDBojb = omdao.getDBInvalidObject();
		String strresult ="";
		String linecolor="";
		int line =1;
		for (DBObjectEntity obj : listDBojb) {
			if (line%2==0) {
				linecolor="bgcolor=\"#D5E4ff\"";
			}else{
				linecolor="";
			}
			strresult =strresult+"<tr "+linecolor+ " align=\"center\">" +
			"<td height=\"25\" align=\"center\">"+ 
			obj.STATUS + "</td><td>" +
			obj.object_type +"</td><td>" +
			obj.OBJECT_NAME +"</td>" + 
			"</tr>";
			line++;
		}
		return strresult;
	} 
	/**
	 * 查看存储过程日志表 
	 * */
	public String getProcLogList(){
		OracleMonitorDAO omdao = new OracleMonitorDAO();
		List<DBProcLogEntity>  loglist = omdao.getProcLogList();
		String strresult ="";
		String linecolor="";
		int line =1;
		for (DBProcLogEntity prolog : loglist) {
			if (line%2==0) {
				linecolor="bgcolor=\"#D5E4ff\"";
			}else{
				linecolor="";
			}
			String striswarn ="";  //结果颜色
			if (prolog.isfinished.equals("success")) {
				striswarn="<td><font color=\"00a032\">" +prolog.isfinished+ "</font></td>";
			}else {
				striswarn="<td><font color=\"ff0000\">" +prolog.isfinished+ "</font></td>";
			}
			strresult=strresult+"<tr "+linecolor+ " align=\"center\">" +
			"<td>"+prolog.taskname+   "</td>"   +
			"<td>"+prolog.duration+   "</td>"   +
			"<td>"+prolog.starttime+   "</td>"  +
			"<td>"+prolog.finishtime+  "</td>" +
			striswarn+ 
			"<td>"+prolog.describtion+ "</td>"+
			"</tr>";
			;
			line++;
		}
			return strresult;
	}
	public String getDBTaskList(){
		OracleMonitorDAO omdao = new OracleMonitorDAO();
		List<OracleProcTaskEntity>  listDBojb = omdao.getDBProcTaskList();
		String strresult ="";
		String linecolor="";
		int line =1;
		for (OracleProcTaskEntity obj : listDBojb) {
			if (line%2==0) {
				linecolor="bgcolor=\"#D5E4ff\"";
			}else{
				linecolor="";
			}
			String strnextdatte ="";  //结果颜色
			if (obj.next_date.contains("4000")) {
				strnextdatte="<td><font color=\"ff0000\">" +obj.next_date+ "</font></td>";
			}else {
				strnextdatte="<td><font >" +obj.next_date+ "</font></td>";
			}		
			System.err.println("strnextdatte"+strnextdatte);
			//job,what,last_date,last_sec,next_date,next_sec,interval;
			strresult =strresult+"<tr "+linecolor+ " align=\"left\">" +
			"<td align=\"left\">"+obj.job + "</td>" +
			"<td>" +obj.what +"</td>" +
			"<td>" +obj.last_date +"</td>" +
			"<td>" +obj.last_sec +"</td>" +
			"<td>" +obj.interval +"</td>" + 
			strnextdatte + 
			"<td>"+obj.next_sec +"</td>" + 
			"</tr>";
			line++;
		}
		return strresult;
	}	
	public OracleMonitor() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new OracleMonitor().getProcLogList();
	}
	
}