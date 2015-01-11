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
  	      event.getServletContext().log("终端上下线监控TerOnlineMonitorTimer已销毁");
        }
  	}

  	public void contextInitialized(ServletContextEvent event) {
        int interval = 5000;
        if (interval<1000) {
      	  event.getServletContext().log("终端上下线监控TerOnlineMonitorTimer时间隔太小"+interval);  
  		return;
  	  }
  	  timer = new Timer(true);
        event.getServletContext().log("终端上下线监控TerOnlineMonitorTimer已启动" +interval );   
        timer.schedule(new TerOnlineMonitorTimer(event.getServletContext()), 0, interval); 
        event.getServletContext().log("终端上下线监控TerOnlineMonitorTimer已添加任务"); 
  	}

   public static void main(String[] args) {
 
  }

}
