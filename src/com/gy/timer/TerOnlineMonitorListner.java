package com.gy.timer;

import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;  

public class TerOnlineMonitorListner   implements ServletContextListener  {
    private Timer timer	 ;
    static long interval ;
    public void contextDestroyed(ServletContextEvent event) {
        if (timer != null) {
  	      timer.cancel();
  	      event.getServletContext().log("�ն������߼��TerOnlineMonitorTimer������");
        }
  	}

  	public void contextInitialized(ServletContextEvent event) {
        int interval = 5000;
        if (interval<1000) {
      	  event.getServletContext().log("�ն������߼��TerOnlineMonitorTimerʱ���̫С"+interval);  
  		return;
  	  }
  	  timer = new Timer(true);
        event.getServletContext().log("�ն������߼��TerOnlineMonitorTimer������" +interval );   
        timer.schedule(new TerOnlineMonitorTimer(event.getServletContext()), 0, interval); 
        event.getServletContext().log("�ն������߼��TerOnlineMonitorTimer���������"); 
  	}

   public static void main(String[] args) {
 
  }

}
