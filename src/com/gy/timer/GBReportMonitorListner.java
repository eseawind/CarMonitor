package com.gy.timer;

import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;  

public class GBReportMonitorListner  implements ServletContextListener  {
    private Timer timer	 ;
    static long interval ;
    public void contextDestroyed(ServletContextEvent event) {
        if (timer != null) {
  	      timer.cancel();
  	      event.getServletContext().log("����λ�û㱨��ʱ��GBonlineMonitorTimer������");
        }
  	}

  	public void contextInitialized(ServletContextEvent event) {
        int interval = 5000;
        if (interval<1000) {
      	  event.getServletContext().log("����λ�û㱨��ʱ��GBonlineMonitorTimerʱ���̫С"+interval);  
  		return;
  	  }
  	  timer = new Timer(true);
        event.getServletContext().log("����λ�û㱨��ʱ��GBonlineMonitorTimer������" +interval );   
        timer.schedule(new GBReportMonitorTimer(event.getServletContext()), 0, interval); 
        event.getServletContext().log("����λ�û㱨��ʱ��GBonlineMonitorTimer���������"); 
  	}

   public static void main(String[] args) {
 
  }

}
