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

import com.gy.DAO.CarDAO;
import com.gy.Entity.CarMonitorEntity;

public class QueryCars extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public QueryCars() {
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
			out.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
						"<tr align=\"center\"  class=\"t1\">"+
						"<td height=\"25\" bgcolor=\"#D5E4F4\"><strong>�ն�ID/����</strong></td>"+ 
				 		"<td bgcolor=\"#D5E4F4\"><strong>��˾</strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong>���ߴ���</strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong>����ʱ��</strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong>����ʱ��</strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong>acc״̬</strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong>�ٶ�km/h</strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong>����L</strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong>�������km</strong></td>" +				 		
				 		"<td bgcolor=\"#D5E4F4\"><strong>��ȫλ��ʱ��</strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong>����λ��ʱ��</strong></td>" +
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
		List<CarMonitorEntity> listcars= new CarDAO().getCarMonitorEntity(intdays);
		int line=1;
		String linecolor ="";
		for (CarMonitorEntity cartmp : listcars) {
			if (line%2==0) {
				linecolor="bgcolor=\"#D5E4ff\"";
			}else{
				linecolor="";
			}
			result =result+"<tr "+linecolor+ " align=\"center\">" +
			"<td height=\"25\" align=\"left\">"+ 
			cartmp.PLATE_NO +"/"+cartmp.TER_ID + "</td><td>" +
			cartmp.CP_ID +"</td><td>" +
			cartmp.OFFCOUNT +"</td><td>" +
			cartmp.OFFSECONDS +"</td><td>" +
			cartmp.onlineSeconds +"</td><td>" +
			cartmp.ACCSTATUS +"</td><td>" +
			cartmp.SPEED +"</td><td>" +
			cartmp.OILMASS +"</td><td>" +			
			cartmp.MILSE +"</td><td>" +
			cartmp.pos_time +"</td><td>" +
			cartmp.pos_time0200+"</td><td>" +
			"</tr>";
			line++;
		}
		System.err.println(result);
		return result;
	}
	public void init() throws ServletException {
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new QueryCars().getQueryResult(1);
	}

}
