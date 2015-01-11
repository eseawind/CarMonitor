package com.gy.CarMonitor;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gy.DAO.OffLineDAO;
import com.gy.Entity.CarMonitorEntity;

public class CarMonitor extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public CarMonitor() {
		super();
		System.err.println("LoginServlet---------------");
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
//		System.err.println("test1----------------------");
//		System.err.println("querydays:"+querydays);		
		if( querydays ==null||querydays.equals("")  ){
//			System.err.println("querydays为空");
			querydays="1";
		}
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strdate = sd.format(new Date());
		out.append("<title>古易车辆监控</title>");
		out.append("<form  name=\"form1\" action=\"QueryCars\" method=\"post\" >" +
				"<select style='width:130px;' name=\"queryDays\" id=\"queryDays\">" +
				"<option value=\"0\">今天</option>" +
				"<option value=\"1\">昨天</option>" +
				"<option value=\"2\">前天</option>" +
				"<option value=\"3\">大前天</option>" +
				"<option value=\"4\">4天前</option>" +
				"<option value=\"5\">5天前</option>" +
				"<option value=\"6\">6天前</option>" +
				"<option value=\"7\">一周前</option>" +
				"</select>" +
				"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+
				"<input type=\"submit\" name=\"button\" id=\"button\" value=\"  查询  \">" +				
				"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+strdate +
				"</form>");
//		out.append();
		out.append("<br>");
		out.append("<script>document.getElementById(\"queryDays\").value = \""+ querydays+
				"\";</script>"  );
		if(!querydays.equals("") || querydays !=null){
//			bgcolor="#D5E4ff" 
			out.append("<table width=\"100%\" border=\"1\" cellspacing=\"1\" cellpadding=\"1\">"+
						"<tr align=\"center\"  class=\"t1\">"+
						"<td height=\"25\" bgcolor=\"#D5E4F4\"><strong>终端ID/车牌</strong></td>"+ 
				 		"<td bgcolor=\"#D5E4F4\"><strong>公司</strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong>离线次数</strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong>离线时长</strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong>上线时长</strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong>离线时间占比%</strong></td>" +
//				 		"<td bgcolor=\"#D5E4F4\"><strong>速度km/h</strong></td>" +
//				 		"<td bgcolor=\"#D5E4F4\"><strong>油量L</strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong>当日里程(米)</strong></td>" +				 		
				 		"<td bgcolor=\"#D5E4F4\"><strong>异常记录明细</strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong>停车记录明细</strong></td>" +
				 		"</tr>"   +
				 		getQueryResult(Integer.valueOf(querydays))
				 ); 
			out.append(" </table>" );  
		}else{
			out.append("error");
		}
		out.flush();
		out.close();
	}

	public String getQueryResult(int intdays){
		String result =   ""; 
		List<CarMonitorEntity> listcars= new OffLineDAO().getCarMonitorEntity(intdays);
		int line=1;
		String linecolor ="";
		for (CarMonitorEntity cartmp : listcars) {
			if (line%2==0) {
				linecolor="bgcolor=\"#D5E4ff\"";
			}else{
				linecolor="";
			}
			long curtime = System.currentTimeMillis();
			String strtime = "trunc(gtime("+ curtime+")-"+ intdays+")";
			result =result+"<tr "+linecolor+ " align=\"center\">" +
			"<td height=\"25\" align=\"left\">"+ 
			cartmp.plate_no +"/"+cartmp.ter_id + "</td><td>" +
			cartmp.cp_name +"</td><td>" +
			cartmp.offcount +"</td><td>" +
			cartmp.offstr +"</td><td>" +
			cartmp.onstr +"</td><td>" +
			cartmp.off_ratio +"</td><td>" +
			cartmp.line_dis +"</td><td>" +
			"<a href=\"http://115.29.198.101/CarMonitor/AbnormalMonitor?othercondition=and%20ter_id%3d"+cartmp.ter_id+"%20and%20crtime%3E"+strtime+" and runningtype='1'\">查看明细</a>"+"</td><td>" +			
			"<a href=\"http://115.29.198.101/CarMonitor/AbnormalMonitor?othercondition=and%20ter_id%3d"+cartmp.ter_id+"%20and%20crtime%3E"+strtime+" and runningtype in('4','3')\">查看明细</a>"+"</td>" +
			"</tr>";
			line++;
		}
//		System.err.println(result);
		return result;
	}
	public void init() throws ServletException {
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		String s = new CarMonitor().getQueryResult(1);
//		System.err.println(s);
//		System.err.println("<table width=\"100%\" border=\"1\" cellspacing=\"1\" cellpadding=\"1\">"+
//						"<tr align=\"center\"  class=\"t1\">"+
//						"<td height=\"25\" bgcolor=\"#D5E4F4\"><strong>终端ID/车牌</strong></td>"+ 
//				 		"<td bgcolor=\"#D5E4F4\"><strong>公司</strong></td>" +
//				 		"<td bgcolor=\"#D5E4F4\"><strong>离线次数</strong></td>" +
//				 		"<td bgcolor=\"#D5E4F4\"><strong>离线时长</strong></td>" +
//				 		"<td bgcolor=\"#D5E4F4\"><strong>上线时长</strong></td>" +
//				 		"<td bgcolor=\"#D5E4F4\"><strong>离线时间占比%</strong></td>" +
////				 		"<td bgcolor=\"#D5E4F4\"><strong>速度km/h</strong></td>" +
////				 		"<td bgcolor=\"#D5E4F4\"><strong>油量L</strong></td>" +
//				 		"<td bgcolor=\"#D5E4F4\"><strong>当日里程(米)</strong></td>" +				 		
//				 		"<td bgcolor=\"#D5E4F4\"><strong>异常记录明细</strong></td>" +
//				 		"<td bgcolor=\"#D5E4F4\"><strong>停车记录明细</strong></td>" +
//				 		"</tr>"   );
		System.err.println("<a href=\"http://115.29.198.101/CarMonitor/AbnormalMonitor?othercondition=and%20ter_id%3d"+"33333"+"%20and%20crtime%3Etrunc(sysdate)\">查看明细</a>");
		
	}

}
