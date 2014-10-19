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
		System.err.println("queryDays传递过来的值为："+strquerydays);
		if( strquerydays ==null||strquerydays.equals("")  ){ 
			strquerydays="0";
		}
		int querydays = Integer.valueOf(strquerydays);
		
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strdate = sd.format(new Date());
		out.append("<title>古易车辆监控</title>"); 
		out.append("<h2 align=\"left\" ><a href=\"/CarMonitor\">返回</a>" +
				"  &nbsp;&nbsp;&nbsp;<a href=\"/CarMonitor/OffLineMonitor?querydays="+strquerydays+"\">刷新</a>"+
				"</h2>"); 
		out.append("<h2 align=\"center\">--终端离线统计"+"--</h2 >");
		out.append("<form  name=\"form1\" action=\"OffLineMonitor\" method=\"post\" >" +
					"<select style='width:130px;' name=\"querydays\" id=\"querydays\">" +
					"<option value=\"0\">今天</option>" +
					"<option value=\"1\">昨天</option>" +
					"<option value=\"2\">前天</option>" +
					"<option value=\"3\">大前天</option>" +
					"<option value=\"4\">4天前</option>" +
					"<option value=\"5\">5天前</option>" +
					"<option value=\"6\">6天前</option>" +
					"<option value=\"7\">一周前</option>" +
					"</select>"+
					"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+
					"<input type=\"submit\" name=\"button\" id=\"button\" value=\"  查询  \">" +				
					"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+strdate +
					"</form>");		
		out.append("<script>document.getElementById(\"querydays\").value = \""+ querydays+
		"\";</script>"  );		
			out.append("<table width=\"100%\" border=\"1\" cellspacing=\"1\" cellpadding=\"1\">"+
						"<tr align=\"center\"  class=\"t1\">"+
						"<td height=\"25\" bgcolor=\"#D5E4F4\"><strong>所属公司-车牌-终端ID</strong></td>"+ 
				 		"<td bgcolor=\"#D5E4F4\"  ><strong>离线时间</strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"  ><strong>上线时间 </strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"  ><strong>离线次数 </strong></td>"  +
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
			return "数值异常";
		}
		return times/3600+"时"+ (times%3600)/60+"分"+ times%60+"秒";
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
