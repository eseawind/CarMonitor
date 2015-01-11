package com.gy.timer;

import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;  

public class SafeReportMonitorListner  implements ServletContextListener  {
    private Timer timer	 ;
    static long interval ;
    public void contextDestroyed(ServletContextEvent event) {
        if (timer != null) {
  	      timer.cancel();
  	      event.getServletContext().log("��ȫ���ݻ㱨��ʱ��GBonlineMonitorTimer������");
        }
  	}

  	public void contextInitialized(ServletContextEvent event) {
        int interval = 10000;
        if (interval<1000) {
      	  event.getServletContext().log("��ȫ���ݻ㱨��ʱ��GBonlineMonitorTimerʱ���̫С"+interval);  
  		return;
  	  }
  	  timer = new Timer(true);
        event.getServletContext().log("��ȫ���ݻ㱨��ʱ��GBonlineMonitorTimer������" +interval );   
        timer.schedule(new SafeReportMonitorTimer(event.getServletContext()), 0, interval); 
        event.getServletContext().log("��ȫ���ݻ㱨��ʱ��GBonlineMonitorTimer���������"); 
  	}

   public static void main(String[] args) {
 
  }

}
