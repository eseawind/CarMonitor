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
  	      event.getServletContext().log("安全数据汇报定时器GBonlineMonitorTimer已销毁");
        }
  	}

  	public void contextInitialized(ServletContextEvent event) {
        int interval = 10000;
        if (interval<1000) {
      	  event.getServletContext().log("安全数据汇报定时器GBonlineMonitorTimer时间隔太小"+interval);  
  		return;
  	  }
  	  timer = new Timer(true);
        event.getServletContext().log("安全数据汇报定时器GBonlineMonitorTimer已启动" +interval );   
        timer.schedule(new SafeReportMonitorTimer(event.getServletContext()), 0, interval); 
        event.getServletContext().log("安全数据汇报定时器GBonlineMonitorTimer已添加任务"); 
  	}

   public static void main(String[] args) {
 
  }

}
