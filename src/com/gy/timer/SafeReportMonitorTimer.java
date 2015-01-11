package com.gy.timer;

import java.util.TimerTask;

import javax.servlet.ServletContext;

public class SafeReportMonitorTimer extends TimerTask {
	private ServletContext servletContext;
	private static boolean isRunning = false;
	public static int sendcount = 0;

	public SafeReportMonitorTimer(ServletContext servletContext) {
		this.servletContext = servletContext;
	} 
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.err.println("SafeOnlineMonitorTimer is running "+ System.currentTimeMillis());	
	}
}
