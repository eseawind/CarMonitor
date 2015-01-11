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
  	      event.getServletContext().log("国标位置汇报定时器GBonlineMonitorTimer已销毁");
        }
  	}

  	public void contextInitialized(ServletContextEvent event) {
        int interval = 5000;
        if (interval<1000) {
      	  event.getServletContext().log("国标位置汇报定时器GBonlineMonitorTimer时间隔太小"+interval);  
  		return;
  	  }
  	  timer = new Timer(true);
        event.getServletContext().log("国标位置汇报定时器GBonlineMonitorTimer已启动" +interval );   
        timer.schedule(new GBReportMonitorTimer(event.getServletContext()), 0, interval); 
        event.getServletContext().log("国标位置汇报定时器GBonlineMonitorTimer已添加任务"); 
  	}

   public static void main(String[] args) {
 
  }

}
