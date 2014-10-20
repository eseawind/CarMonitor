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

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
		 
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter(); 	
 
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strdate = sd.format(new Date());
		out.append("<title>ʵʱ���</title>"); 
		out.append("<h2 align=\"left\" ><a href=\"/CarMonitor\">����</a>" +
				"  &nbsp;&nbsp;&nbsp;<a href=\"/CarMonitor/RealMonitor\">ˢ��</a>"+
				"</h2>"); 
		
		out.append("<h2 align=\"center\">--�ն�ʵʱ���ݼ��"+"--</h2 >"+"������ʱ��:"+strdate);
			out.append("<table width=\"150%\" border=\"1\" cellspacing=\"1\" cellpadding=\"1\">"+
						"<tr align=\"center\"  class=\"t1\">"+
						"<td height=\"25\" align=\"left\" bgcolor=\"#D5E4F4\"><strong>������˾-����-�ն�ID</strong></td>"+ 
						"<td bgcolor=\"#D5E4F1\"><strong>��ȫ���ݻ㱨ʱ��</strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong> ������� </strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong> ���ٶ� </strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;GPS&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong>&nbsp;GPS�ٶ�</strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong>�ٶ�gps</strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong>ģ������</strong></td>" +				 		
				 		"<td bgcolor=\"#D5E4F4\"><strong> &nbsp;ʵ������(��) </strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong> &nbsp;�¶�&nbsp; </strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong> &nbsp;�ն�״̬ </strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong> &nbsp;����״̬ </strong></td>" +
				 		"<td bgcolor=\"#D5E4F4\"><strong> &nbsp;���� </strong></td>" +
				 		"<td bgcolor=\"#FF0000\"><strong>����-�ն�ID</strong></td>"+				 		
				 		"<td bgcolor=\"#FF0000\"><strong> ����λ��ʱ��0200 </strong></td>" +
				 		"<td bgcolor=\"#FF0000\"><strong> �����ϱ�λ��0200 </strong></td>" +
				 		"<td bgcolor=\"#FF0000\"><strong> �ն�״̬0200 </strong></td>" +
				 		"<td bgcolor=\"#FF0000\"><strong> ����״̬0200 </strong></td>" +
				 		"</tr>"    
//				 		 
				 ); 
			out.append(getRealInfo());
//			out.append("<tr  align=\"center\"><td height=\"25\" align=\"left\">��A-00001/1</td><td>1</td>" +
//					"<td>2014-09-22 18:37:42.0</td><td>0</td><td>0</td><td>.04</td><td>119.2479,26.1044</td>" +
//					"<td>119.2597,26.1072</td><td>0</td><td>0</td><td>0</td><td>786435</td><td>0</td><td>0</td>" +
//					"<td>2014-10-11 18:10:33.0</td><td>119.247634,26.107976</td><td>786435</td><td>0</td><td></tr>");
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
				String strisonline ="";
				if (real.isonline.equals("1")) {
//					strisonline = "<td bgcolor=\"00a032\" align=\"center\">����</td>";
					strisonline="<td align=\"left\">"+"<font color=\"00a032\">��</font>"+real.cp_name+"-"+real.PLATENO+"</td>";
				}else {
//					strisonline = "<td bgcolor=\"ff5000\" align=\"center\">--</td>";
					strisonline="<td align=\"left\" >"+"<font color=\"c0c0c0\">��</font>"+ real.cp_name+"-"+real.PLATENO +"</td>";
				}
				result =result+"<tr "+linecolor+ " align=\"center\">" +
				strisonline+
				"<td align=\"left\">"+real.LASTTIME +"</td>" +
				"<td>" + real.LINE_DIS +"</td>" +
				"<td>" +real.LINE_SPEED +"</td>" +
				"<td>" + real.GPS.replaceAll(" ", "") +"</td>" +
				"<td>" + real.GPS_SPEED +"</td>" +
				"<td>" +real.GPS_F.replaceAll(" ", "") +"</td>" +
				"<td>" +real.OILVALUE +"</td>" +
				"<td>" +real.OILMASS +"</td>" +
				"<td>" +real.tempvalue +"</td>" +
				"<td>" +real.TER_STATUS +"</td>" +
				"<td>" +real.WARN_TYPE +"</td>" +
				"<td>" +real.DIS_FRONT +"</td>" +
				"<td>" +real.PLATENO +"</td>" +
				"<td bordercolor=\"#FF0000\"  >" +real.POSTIME0200 +"</td>" +
				"<td bordercolor=\"#FF0000\"  >" +real.GPS0200.replaceAll(" ", "") +"</td>" +
				"<td bordercolor=\"#FF0000\"  >" +real.TERSTATUS0200 +"</td>" +
				"<td bordercolor=\"#FF0000\"  >" +real.WARN0200 +"</td>" +
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
		RealMonitor rm = new RealMonitor();
		rm.getRealInfo();
	}

}
