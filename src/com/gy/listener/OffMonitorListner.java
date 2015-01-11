package com.gy.listener;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.gy.CarMonitor.DBHelper;

public class OffMonitorListner implements ServletContextListener {

	private Timer timer;
	static long interval;

	public void contextDestroyed(ServletContextEvent event) {
		if (timer != null) {
			timer.cancel();
			event.getServletContext().log("上下线监控定时器已销毁");
		}
	}

	private int getInterval() {
		int interval = 0;
		String sql = "select nvl(max(value),0)as interval from gy_monitor_mail where sub_type='interval' and type = 1 ";
		Connection conn;
		Statement stat = null;
		ResultSet rs = null;
		conn = new DBHelper().getConn();
		try {
			stat = conn.createStatement();
			rs = stat.executeQuery(sql);
			while (rs.next()) {
				interval = Integer.valueOf(rs.getString("interval"));
				System.err.println("上下线监控定时器interval:" + interval);
			}
		} catch (SQLException e) {
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
		return interval;
	}

	public void contextInitialized(ServletContextEvent event) {
		int interval = getInterval(   );
		if (interval < 1000) {
			event.getServletContext().log("上下线监控定时间隔太小" + interval);
			return;
		}
		timer = new Timer(true);
		event.getServletContext().log("gy上下线定时器已启动" + interval);
		timer.schedule(new RealMonitorTimer(event.getServletContext()), 0, interval);
		event.getServletContext().log("gy上下线监控定时器已添加任务");
	}

	public static void main(String[] args) { 
	}
}
