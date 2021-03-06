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
		//解决中文问题
		response.setContentType("text/html;charset=gbk");
		request.setCharacterEncoding("gbk");
		//end 解决中文问题
//		response.setCharacterEncoding("gbk");
		PrintWriter out = response.getWriter(); 	
		System.err.println("curpage="+request.getParameter("curpage"));
		System.err.println("ter_id="+request.getParameter("ter_id"));
		System.err.println("othercondition="+request.getParameter("othercondition"));
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strdate = sd.format(new Date());
		String othercondition = request.getParameter("othercondition");
		if (othercondition ==null ) {
			othercondition=" 31 and duration_sub>60000 ";
		}
		out.append("<title>异常点监控</title>");
		out.append("<h2 align=\"left\" ><a href=\"/CarMonitor\">返回</a>"
						+ "  &nbsp;&nbsp;&nbsp;<a href=\"/CarMonitor/AbnormalMonitor\">刷新</a>"
						+ "</h2>");
		out.append("<h2 align=\"center\">--异常点监控"+"--</h2 >");
		//+"服务器时间:"+strdate 
		out.append("<form  name=\"form1\" action=\"AbnormalMonitor\" method=\"post\" >");
		out.append(
//				"<label>终端ID</label><input type=\"text\" name=\"ter_id\" value=\"\" id=\"ter_id\" />" +
//				"<label>每页记录数</label><input type=\"text\" name=\"pagecount\" value=\"\" id=\"pagecount\" />" +
//				"<input type=\"submit\" value=\"上一页\"/>" +
//				"<label id=\"curpage\" name=\"curpage\" >1</label>" +
//				"<input type=\"submit\" value=\"下一页\"/>" +
				"<label>查询条件</label>" +
				"<input type=\"text\" value=\""+othercondition+ "\""+
				" name=\"othercondition\" />" +
				"<input type=\"submit\" value=\"查询\"/>");
		out.append("</form>");
		out.append("<table width=\"130%\" border=\"1\" cellspacing=\"1\" cellpadding=\"1\">"+
					"<tr align=\"center\"  class=\"t1\">" +
					"<td bgcolor=\"#D5E4F1\"><strong>查看地图</strong></td>"+
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
		out.append(getAbnormalList(1,othercondition,200));
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
			String strrunningtype ="";
			int intrunningtype =Integer.valueOf(abnor.runningtype); 
			switch (intrunningtype) {
			case 1:
				strrunningtype="异常离线";
				break;
			case 2:
				strrunningtype="进出隧道";
				break;
			case 3:
				strrunningtype="停车休眠";
				break;
			case 4:
				strrunningtype="停车2+";
				break;
			case 5:
				strrunningtype="隧道口附近";
				break;					
			default:
				strrunningtype="其他";
				break;
			}
			String maphref = "<td><a href=\"/CarMonitor/ViewMap?lona=" + abnor.next_gpslon  +"&lata=" +
			abnor.next_gpslat +"&lonb="+abnor.last_gpslon + "&latb="+abnor.last_gpslat + "\">" +
			strrunningtype +
					"</a></td>";
			
			
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
