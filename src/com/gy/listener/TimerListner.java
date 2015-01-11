package com.gy.listener;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; 
import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.gy.CarMonitor.DBHelper;

 

public class TimerListner implements ServletContextListener {
    
    private Timer timer	 ;
    static long interval ;

	public void contextDestroyed(ServletContextEvent event) {
      if (timer != null) {
	      timer.cancel();
	      event.getServletContext().log("报表定时器已销毁");
      }	
	}

	private int getInterval(){
		int interval =0;
		String sql = "select nvl(max(value),0)as interval from gy_monitor_mail where sub_type='interval' and type =0 ";
		Connection conn;
		Statement stat = null;
		ResultSet rs = null;
		conn = new DBHelper().getConn();
		try {
			stat = conn.createStatement();
			rs = stat.executeQuery(sql);
			while (rs.next()) {				
				interval = Integer.valueOf(rs.getString("interval"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		//关闭数据库
		try {
			System.err.println("关闭数据库连接");
			if (rs !=null) {
				System.err.println("关闭rs");
				rs.close();
			}
			if (stat!=null) {
				System.err.println("关闭stat");
				stat.close();
			}
			if (conn!=null) {
				System.err.println("关闭conn");
				conn.close();
			} 		
		} catch (SQLException e) { 
			e.printStackTrace();
		}		
		return interval;
	}
	
	public void contextInitialized(ServletContextEvent event) {
      int interval = getInterval();
      if (interval<1000) {
    	  event.getServletContext().log("终端上下线统计报表定时间隔太小"+interval);  
		return;
	  }
	  timer = new Timer(true);
      event.getServletContext().log("gy终端上下线统计定时器已启动" +interval );   
      timer.schedule(new JobTimer(event.getServletContext()), 0, interval); 
      event.getServletContext().log("gy终端上下线统计报表已添加任务"); 
	}

 public static void main(String[] args) {
	 System.err.println(new TimerListner().getInterval());
     int interval =new TimerListner().getInterval();
     if (interval<1000000) {
    	 System.err.println("small");
		return;
	 }
}

}