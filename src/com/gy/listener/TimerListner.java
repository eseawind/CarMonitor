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
	      event.getServletContext().log("����ʱ��������");
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
		//�ر����ݿ�
		try {
			System.err.println("�ر����ݿ�����");
			if (rs !=null) {
				System.err.println("�ر�rs");
				rs.close();
			}
			if (stat!=null) {
				System.err.println("�ر�stat");
				stat.close();
			}
			if (conn!=null) {
				System.err.println("�ر�conn");
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
    	  event.getServletContext().log("�ն�������ͳ�Ʊ���ʱ���̫С"+interval);  
		return;
	  }
	  timer = new Timer(true);
      event.getServletContext().log("gy�ն�������ͳ�ƶ�ʱ��������" +interval );   
      timer.schedule(new JobTimer(event.getServletContext()), 0, interval); 
      event.getServletContext().log("gy�ն�������ͳ�Ʊ������������"); 
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