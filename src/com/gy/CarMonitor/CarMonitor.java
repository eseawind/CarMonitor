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
//			System.err.println("querydaysΪ��");
			querydays="1";
		}
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strdate = sd.format(new Date());
		out.append("<title>���׳������</title>");
		out.append("<form  name=\"form1\" action=\"QueryCars\" method=\"post\" >" +
				"<select style='width:130px;' name=\"queryDays\" id=\"queryDays\">" +
				"<option value=\"0\">����</option>" +
				"<option value=\"1\">����</option>" +
				"<option value=\"2\">ǰ��</option>" +
				"<option value=\"3\">��ǰ��</option>" +
				"<option value=\"4\">4��ǰ</option>" +
				"<option value=\"5\">5��ǰ</option>" +
				"<option value=\"6\">6��ǰ</option>" +
				"<option value=\"7\">һ��ǰ</option>" +
				"</select>" +
				"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+
				"<input type=\"submit\" name=\"button\" id=\"button\" value=\"  ��ѯ  \">" +				
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
						"<td height=\"25\" bgcolor=\"#D5E4F4\"><strong>�ն�ID/����</strong></td>"+ 
				 		"<td bgcolor=\"#D5E4F4\"><strong>��˾</strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong>���ߴ���</strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong>����ʱ��</strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong>����ʱ��</strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong>����ʱ��ռ��%</strong></td>" +
//				 		"<td bgcolor=\"#D5E4F4\"><strong>�ٶ�km/h</strong></td>" +
//				 		"<td bgcolor=\"#D5E4F4\"><strong>����L</strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong>�������(��)</strong></td>" +				 		
				 		"<td bgcolor=\"#D5E4F4\"><strong>�쳣��¼��ϸ</strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong>ͣ����¼��ϸ</strong></td>" +
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
			"<a href=\"http://115.29.198.101/CarMonitor/AbnormalMonitor?othercondition=and%20ter_id%3d"+cartmp.ter_id+"%20and%20crtime%3E"+strtime+" and runningtype='1'\">�鿴��ϸ</a>"+"</td><td>" +			
			"<a href=\"http://115.29.198.101/CarMonitor/AbnormalMonitor?othercondition=and%20ter_id%3d"+cartmp.ter_id+"%20and%20crtime%3E"+strtime+" and runningtype in('4','3')\">�鿴��ϸ</a>"+"</td>" +
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
//						"<td height=\"25\" bgcolor=\"#D5E4F4\"><strong>�ն�ID/����</strong></td>"+ 
//				 		"<td bgcolor=\"#D5E4F4\"><strong>��˾</strong></td>" +
//				 		"<td bgcolor=\"#D5E4F4\"><strong>���ߴ���</strong></td>" +
//				 		"<td bgcolor=\"#D5E4F4\"><strong>����ʱ��</strong></td>" +
//				 		"<td bgcolor=\"#D5E4F4\"><strong>����ʱ��</strong></td>" +
//				 		"<td bgcolor=\"#D5E4F4\"><strong>����ʱ��ռ��%</strong></td>" +
////				 		"<td bgcolor=\"#D5E4F4\"><strong>�ٶ�km/h</strong></td>" +
////				 		"<td bgcolor=\"#D5E4F4\"><strong>����L</strong></td>" +
//				 		"<td bgcolor=\"#D5E4F4\"><strong>�������(��)</strong></td>" +				 		
//				 		"<td bgcolor=\"#D5E4F4\"><strong>�쳣��¼��ϸ</strong></td>" +
//				 		"<td bgcolor=\"#D5E4F4\"><strong>ͣ����¼��ϸ</strong></td>" +
//				 		"</tr>"   );
		System.err.println("<a href=\"http://115.29.198.101/CarMonitor/AbnormalMonitor?othercondition=and%20ter_id%3d"+"33333"+"%20and%20crtime%3Etrunc(sysdate)\">�鿴��ϸ</a>");
		
	}

}
