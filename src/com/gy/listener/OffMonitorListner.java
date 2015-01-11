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
			event.getServletContext().log("�����߼�ض�ʱ��������");
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
				System.err.println("�����߼�ض�ʱ��interval:" + interval);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// �ر����ݿ�
		try {
			System.err.println("�ر����ݿ�����");
			if (rs != null) {
				System.err.println("�ر�rs");
				rs.close();
			}
			if (stat != null) {
				System.err.println("�ر�stat");
				stat.close();
			}
			if (conn != null) {
				System.err.println("�ر�conn");
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
			event.getServletContext().log("�����߼�ض�ʱ���̫С" + interval);
			return;
		}
		timer = new Timer(true);
		event.getServletContext().log("gy�����߶�ʱ��������" + interval);
		timer.schedule(new RealMonitorTimer(event.getServletContext()), 0, interval);
		event.getServletContext().log("gy�����߼�ض�ʱ�����������");
	}

	public static void main(String[] args) { 
	}
}
