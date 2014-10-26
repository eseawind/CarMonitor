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

import com.gy.DAO.AbnormalMonitorDAO;
import com.gy.DAO.RealMonitorDAO;
import com.gy.Entity.AbnormalMonitorEntity;
import com.gy.Entity.RealMonitorEntity;

public class AbnormalMonitor extends HttpServlet {

	public AbnormalMonitor() {

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
		out.append("<title>异常点监控</title>");
		out.append("<h2 align=\"left\" ><a href=\"/CarMonitor\">返回</a>"
						+ "  &nbsp;&nbsp;&nbsp;<a href=\"/CarMonitor/AbnormalMonitor\">刷新</a>"
						+ "</h2>");
		out.append("<h2 align=\"center\">--异常点监控"+"--</h2 >"+"服务器时间:"+strdate);
		out.append("<table width=\"130%\" border=\"1\" cellspacing=\"1\" cellpadding=\"1\">"+
					"<tr align=\"center\"  class=\"t1\">" +
					"<td bgcolor=\"#D5E4F1\"><strong>地图</strong></td>"+
					"<td height=\"25\" align=\"left\" bgcolor=\"#D5E4F4\"><strong>ter_id</strong></td>"+ 
					"<td bgcolor=\"#D5E4F1\"><strong>last_recid</strong></td>" +
			 		"<td bgcolor=\"#D5E4F4\"><strong> next_recid </strong></td>" +
			 		"<td bgcolor=\"#D5E4F4\"><strong> last_lasttime </strong></td>" +
			 		"<td bgcolor=\"#D5E4F4\"><strong> next_lasttime</strong></td>" +
			 		"<td bgcolor=\"#D5E4F4\"><strong>duration_sub</strong></td>" +
			 		"<td bgcolor=\"#D5E4F4\"><strong>last_gpslon</strong></td>" +
			 		"<td bgcolor=\"#D5E4F4\"><strong>last_gpslat</strong></td>" +				 		
			 		"<td bgcolor=\"#D5E4F4\"><strong>next_gpslon </strong></td>" +
			 		"<td bgcolor=\"#D5E4F4\"><strong>next_gpslat</strong></td>" +
			 		"<td bgcolor=\"#D5E4F4\"><strong>last_status </strong></td>" +
			 		"<td bgcolor=\"#D5E4F4\"><strong> next_status </strong></td>" +
			 		"<td bgcolor=\"#D5E4F4\"><strong>crtime </strong></td>" +
			 		"</tr>" 
			 ); 
		out.append(getAbnormalList(1,"",10));
		out.append(" </table>" );  
		out.flush();
		out.close();
	}
	
	/**
	 * 获取记录列表
	 * */
	public String getAbnormalList(int curpage,String ter_id ,int pagecount){  
		String result="";
		int line=1; 
		String linecolor; 		
		AbnormalMonitorDAO dao = new AbnormalMonitorDAO();
		List<AbnormalMonitorEntity> abnormalList =  dao.getAbnormalDataList(curpage, ter_id, pagecount);
		for (AbnormalMonitorEntity abnor : abnormalList) {
			if (line%2==0) {
				linecolor="bgcolor=\"#D5E4ff\"";
			}else{
				linecolor="";
			}
			String maphref = "<td><a href=\"/CarMonitor/ViewMap?lona=" + abnor.last_gpslon +"&lata=" +
			abnor.last_gpslat+"&lonb="+ abnor.next_gpslon + "&latb="+ abnor.next_gpslat+ "\">地图</a></td>";
			
			
			result =result+"<tr "+linecolor+ " align=\"center\">" +
			maphref+ 
			"<td>" + abnor.ter_id +"</td>" +
			"<td>" +abnor.last_recid +"</td>" +
			"<td>" + abnor.next_recid +"</td>" +
			"<td>" + abnor.last_lasttime +"</td>" +
			"<td>" +abnor.next_lasttime +"</td>" +
			"<td>" +abnor.duration_sub +"</td>" +
			"<td>" +abnor.last_gpslon +"</td>" +
			"<td>" +abnor.last_gpslat +"</td>" +
			"<td>" +abnor.next_gpslon +"</td>" +
			"<td>" +abnor.next_gpslat +"</td>" +
			"<td>" +abnor.last_status +"</td>" +
			"<td>" +abnor.next_status +"</td>" +
			"<td>" +abnor.crtime +"</td>" +
			"</tr>";
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
