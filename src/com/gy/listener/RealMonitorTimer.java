package com.gy.listener;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import javax.servlet.ServletContext;

import com.gy.CarMonitor.CarMonitor;
import com.gy.CarMonitor.DBHelper;
import com.gy.mail.Constant;
import com.gy.mail.SendMail;

public class RealMonitorTimer extends TimerTask {
	public static int sendcount = 0 ;  
	private ServletContext servletContext;
	private static boolean isRunning = false; 
	HashMap warnter_first = new HashMap<String, OffTerminalInfo>();
	HashMap warnter_second = new HashMap<String, OffTerminalInfo>();
	HashMap warnter_third = new HashMap<String, OffTerminalInfo>();
	HashMap warned_terminal = new HashMap<String, OffTerminalInfo>();  //已报警列表
//	HashMap warnclear_terminal = new HashMap<String, OffTerminalInfo>(); 
	List<OffTerminalInfo> warnclear_terminal = new ArrayList<OffTerminalInfo>();
	public RealMonitorTimer(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	/**
	 * 从数据库获取离线列表
	 * */
	private  List<OffTerminalInfo> getOfflist(){
		warnclear_terminal.clear();  //清除解除告警列表
		List<OffTerminalInfo> warnlist = new ArrayList<OffTerminalInfo>(); 
		String sql = "select sub_type,value from gy_monitor_mail where sub_type in('warnspeed','warnsecond', 'terid') and type=1 ";	
		Connection conn;
		Statement stat = null;
		ResultSet rs = null;
		conn = new DBHelper().getConn();
		String str_realterid  = "";
		String str_warnsecond = "";
		String str_warnspeed  = ""; 
		try {
			stat = conn.createStatement();
			rs = stat.executeQuery(sql); 
			while (rs.next()) {
				String strtype =  rs.getString("sub_type");
				if(strtype!=null){
					if (strtype.equals("warnsecond") ) {
						str_warnsecond= rs.getString("value");
					}else if (strtype.equals("terid")) {
						str_realterid= rs.getString("value");
					}else if (strtype.equals("warnspeed")) {
						str_warnspeed= rs.getString("value");
					}
				} 
			} 
			//获取 报警终端列表
			sql=" select ter.plate_no, "
				+"        ra.ter_id, "
				+"        gtime(lasttime) as lasttime, "
				+"        ra.ter_status, "
				+"        ra.gps_speed*3.6 as gps_speed, "
				+"		  round(ra.gps_lon,4) as gps_lon, "
				+"        round(ra.gps_lat,4) as gps_lat, "
				+"		  cp.cp_name "
				+"   from sa.tbl_s_safe_real ra "
				+"  inner join sa.tbl_s_terminal ter "
				+"     on ra.ter_id = ter.id "
				+" inner join sa.tbl_company cp on ter.cp_id = cp.id"
				+"  where 1 = 1   and  ter_id in ("+  str_realterid +")  "
				+"    and gintime(sysdate) * 1000 - ra.lasttime > "+str_warnsecond 
				+"    and ra.gps_speed >= "+str_warnspeed
				+"  order by ter.cp_id,lasttime asc  ";
			System.err.println(sql);
			stat = conn.createStatement();
			rs = stat.executeQuery(sql);
			warnter_third.clear(  );
			while (rs.next()) {
				OffTerminalInfo info = new OffTerminalInfo(); 
				info.plate_no =rs.getString("plate_no");
				info.ter_id =rs.getString("ter_id");
				info.lasttime =rs.getString("lasttime");
				info.gps_speed =rs.getString("gps_speed");
				info.gps_lon =rs.getString("gps_lon");
				info.gps_lat =rs.getString("gps_lat"); 
				info.cp_name = rs.getString("cp_name");
				warnter_third.put(rs.getString("ter_id"),info);						
			} 
			//获取报警3次的终端列表
			warnlist = getWarnList();
			//
			fleshWarnMap();
		} catch (SQLException e) { 
			e.printStackTrace();
		} 
		// 关闭数据库
		try {
			System.err.println("关闭数据库连接");
			if (rs != null) {
//				System.err.println("关闭rs");
				rs.close();
			}
			if (stat != null) {
//				System.err.println("关闭stat");
				stat.close();
			}
			if (conn != null) {
//				System.err.println("关闭conn");
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return warnlist;
	}
	/**
	 * 刷新map 
	 * */
	private void fleshWarnMap(){	 	
		warnter_first.clear();
		warnter_first.putAll(warnter_second);
		warnter_second.clear();
		warnter_second.putAll(warnter_third);
		//解除报警 
//		HashMap warned_terminal_tmp = new HashMap<String, String>();
		Iterator itr = warned_terminal.entrySet().iterator();
        while (itr.hasNext()) { 
        	System.err.println("开始判断是否解除报警");
            Map.Entry entry = (Map.Entry) itr.next(); 
            if (warnter_third.get(entry.getKey())==null ) {
            	System.err.println("解除报警"+entry.getKey());
//            	warned_terminal.remove(entry.getKey());
//            	warned_terminal_tmp.put(entry.getKey() , "1");
//            	warnclear_terminal.put(entry.getKey() , entry.getValue());
            	warnclear_terminal.add( (OffTerminalInfo) entry.getValue());
            	itr.remove();
			} 
        }  
        //清除最近一次报警记录
        warnter_third.clear(); 
	}
	
	
	private List<OffTerminalInfo> getWarnList(){
		List<OffTerminalInfo>  warnlist = new ArrayList<OffTerminalInfo>();
        Iterator itr = warnter_third.entrySet().iterator();
        while (itr.hasNext()) { 
            Map.Entry entry = (Map.Entry) itr.next();
            if (warnter_second.get(entry.getKey())!=null ) {
                if (warnter_first.get(entry.getKey())!=null ) {
                	//已达到3次报警 
    				if ( warned_terminal.get( entry.getKey())!=null) {
    					System.err.println("已触发报警"+entry.getKey());
					}else { 
						System.err.println("触发报警"+entry.getKey());
						warnlist.add((OffTerminalInfo) entry.getValue());
						warned_terminal.put( entry.getKey(),entry.getValue() ); 
					}
    			}
			} 
        }
        System.err.println("getWarnList"+ warnlist.size());
		return warnlist;
	}
	/**
	 * 获取已解除报警列表
	 * */
	private List<OffTerminalInfo> getWarnClearList(){
		List<OffTerminalInfo>  warnclearlist = new ArrayList<OffTerminalInfo>();
        Iterator itr = warnter_third.entrySet().iterator();
        while (itr.hasNext()) { 
            Map.Entry entry = (Map.Entry) itr.next();
            if (warnter_second.get(entry.getKey())!=null ) {
                if (warnter_first.get(entry.getKey())!=null ) {
                	//已达到3次报警 
    				if ( warned_terminal.get( entry.getKey())!=null) {
    					System.err.println("已触发报警"+entry.getKey());
					}else { 
						System.err.println("触发报警"+entry.getKey());
						warnclearlist.add((OffTerminalInfo) entry.getValue());
						warned_terminal.put( entry.getKey(),entry.getValue() ); 
					}
    			}
			} 
        }
        System.err.println("getWarnList"+ warnclearlist.size());
		return warnclearlist;
	}	
	public String getRealMonitorMailContent(List<OffTerminalInfo>  warnlist,List<OffTerminalInfo>  warnclearlist){
		String content="<BODY >";
		int line=0;
		if (warnlist.size()> 0 ) {
			content = content+"<h2 align=\"center\">--离线终端报表--</h2 ><table width=\"100%\" border=\"1\" cellspacing=\"1\" cellpadding=\"1\">"+
			"<tr align=\"center\"  class=\"t1\">"+
			"<td height=\"25\" bgcolor=\"#D5E4F4\"><strong>车牌/终端ID</strong></td>"+ 
	 		"<td bgcolor=\"#D5E4F4\"><strong>公司</strong></td>" +
	 		"<td bgcolor=\"#D5E4F4\"><strong>最后一次上报时间</strong></td>" +
	 		"<td bgcolor=\"#D5E4F4\"><strong>GPS速度(公里/时)</strong></td>" +
	 		"<td bgcolor=\"#D5E4F4\"><strong>查看地图</strong></td>" +
	 		"</tr>" ;   
			for ( OffTerminalInfo info :warnlist) { 
				String linecolor="";
				if (line%2==0) {
					linecolor="bgcolor=\"#D5E4ff\"";
				}else{
					linecolor="";
				} 
				String gps_lon = info.gps_lon.replaceAll(" ", "");
				String gps_lat = info.gps_lat.replaceAll(" ", "");
				content =content+"<tr "+linecolor+ " align=\"center\">" +
				"<td height=\"25\" align=\"left\">"+ 
				info.plate_no +"/"+info.ter_id + "</td><td>" +
				info.cp_name +"</td><td>" +
				info.lasttime +"</td><td>" +
				info.gps_speed +"</td><td>" + 
				"<a href=http://115.29.198.101/CarMonitor/ViewMap?lona="+gps_lon+"&lata="+gps_lat +">" +gps_lon+","+gps_lat +"</a>"+		
				"</tr>";
				line++;
			}  
			content=content+"</table>"; 
		}
		if ( warnclearlist.size()>0) { 
			content = content+"<h2 align=\"center\">--解除离线告警终端报表--</h2 ><table width=\"100%\" border=\"1\" cellspacing=\"1\" cellpadding=\"1\">"+
			"<tr align=\"center\"  class=\"t1\">"+
			"<td height=\"25\" bgcolor=\"#D5E4F4\"><strong>车牌/终端ID</strong></td>"+ 
	 		"<td bgcolor=\"#D5E4F4\"><strong>公司</strong></td>" +
	 		"<td bgcolor=\"#D5E4F4\"><strong>最后一次上报时间</strong></td>" +
	 		"<td bgcolor=\"#D5E4F4\"><strong>GPS速度(公里/时)</strong></td>" +
	 		"<td bgcolor=\"#D5E4F4\"><strong>查看地图</strong></td>" +
	 		"</tr>" ; 
			line=0;
			//解除告警情况---------
			for ( OffTerminalInfo info :warnclearlist) { 
				String linecolor="";
				if (line%2==0) {
					linecolor="bgcolor=\"#D5E4ff\"";
				}else{
					linecolor="";
				} 
				String gps_lon = info.gps_lon.replaceAll(" ", "");
				String gps_lat = info.gps_lat.replaceAll(" ", "");
				content =content+"<tr "+linecolor+ " align=\"center\">" +
				"<td height=\"25\" align=\"left\">"+ 
				info.plate_no +"/"+info.ter_id + "</td><td>" +
				info.cp_name +"</td><td>" +
				info.lasttime +"</td><td>" +
				info.gps_speed +"</td><td>" + 
				"<a href=http://115.29.198.101/CarMonitor/ViewMap?lona="+gps_lon+"&lata="+gps_lat +">" +gps_lon+","+gps_lat +"</a>"+		
				"</tr>";
				line++;
			}
		}
		content=content+"</BODY>";
		System.err.println("离线告警邮件内容:"+content);
		return content;
	}
	
	
	@Override
		public void run() {
			if (!isRunning) {
				isRunning = true;
				servletContext.log("上下线监控本次任务开始" +"---->"+ System.currentTimeMillis()); 
				//获取告警列表
				List<OffTerminalInfo>  warnlist = getOfflist();				
				String content = getRealMonitorMailContent(warnlist,warnclear_terminal); 
				System.err.println("warnlist_size="+warnlist.size());
				
				if (warnlist.size()+warnclear_terminal.size()>0) { 
					
					if (sendcount>0) { //第一次不发送
						System.err.println("发送邮件--------");
						for (String mailaddr : getMailAddress()) {
							SimpleDateFormat formatter = new SimpleDateFormat(
							"yyMMdd-HH:mm");
							String mDateTime = formatter.format(new Date());
							new SendMail().send(mailaddr, "车辆上下线告警(" +"离线:"+warnlist.size()+"上线:"+
									warnclear_terminal.size()+") "+mDateTime, content);
						}
					}
					
					//插入数据库 
//					String sql = "select sub_type,value from gy_monitor_mail where sub_type in('warnspeed','warnsecond', 'terid') and type=1 ";	
					Connection conn;
					Statement stat = null;
					conn = new DBHelper().getConn();
//					stat.ex
					
					sendcount++;  //第一次不发送 
				}
				
				isRunning = false;
				servletContext.log("上下线监控本次任务结束" + "---->"+ System.currentTimeMillis());
			} else {
				servletContext.log("上下线监控上次任务还在执行");
			}
		}
	/**
	 * 获取邮箱地址
	 * */
	private static List<String> getMailAddress() {
		List<String> listaddress = new ArrayList<String>();
		String sql = "select value from gy_monitor_mail where sub_type='to' and type =1 and switch=1 ";
		Connection conn;
		Statement stat = null;
		ResultSet rs = null;
		conn = new DBHelper().getConn();
		try {
			stat = conn.createStatement();
			rs = stat.executeQuery(sql);
			while (rs.next()) {
				listaddress.add(rs.getString("value"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 关闭数据库
		try {
			System.err.println("关闭数据库连接");
			if (rs != null) {
				System.err.println("关闭rs");
				rs.close();
			}
			if (stat != null) {
				System.err.println("关闭stat");
				stat.close();
			}
			if (conn != null) {
				System.err.println("关闭conn");
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listaddress;
	}
	public static void main(String[] args) throws SQLException {
//		System.err.println( new RealMonitorTimer.getOfflist());
//		new RealMonitorTimer().getOfflist();
//		RealMonitorTimer ttt =new  RealMonitorTimer(null);  
//		ttt.getOfflist();
//		 ttt.getOfflist();
//		List<OffTerminalInfo>  warnlist = ttt.getOfflist();		
//		System.err.println(warnlist.size());
//		String content ="";
//		content = ttt.getRealMonitorMailContent(warnlist,ttt.warnclear_terminal);
//		System.err.println("content:"+content);
//		if (warnlist.size()>0  ) { 
//			System.err.println("发送邮件--------");
//			for (String mailaddr : getMailAddress()) {
//				SimpleDateFormat formatter = new SimpleDateFormat(
//						"yyyy-MM-dd HH:mm");
//				String mDateTime = formatter.format(new Date());
//				new SendMail().send(mailaddr, "古易车辆上下线告警通知" + mDateTime, content);
//			}
//		}
//		SimpleDateFormat formatter = new SimpleDateFormat(
//		"yyMMdd-HH:mm");
//		String mDateTime = formatter.format(new Date());
//		System.err.println(mDateTime);
		RealMonitorTimer rt = new RealMonitorTimer(null);
		List<OffTerminalInfo>  warnlist = rt.getOfflist();
		warnlist = rt.getOfflist();
		warnlist = rt.getOfflist(); 
		System.err.println( warnlist.size());
		
		Connection conn;
		Statement stat = null;
		conn = new DBHelper().getConn();
		stat = conn.createStatement();
		long crtime =  System.currentTimeMillis();
		for ( OffTerminalInfo off: warnlist ) {
//			String sql = "insert into gy_real_monitor ( ID ,TER_ID ,PLATE_NO ,CP_NAME ,LASTTIME ,GPS_SPEED ,WARNTYPE ,SENDSTATUS ,CRTIME )values  " +
//			crtime+","+off.ter_id+","
//			"( 1,'2','2','3','4','','','',sysdate  )  ";
//			stat.executeUpdate(sql); 
		}
		conn.commit();
		stat.close();
		conn.close();
		System.err.println("ee");	
	}
}
