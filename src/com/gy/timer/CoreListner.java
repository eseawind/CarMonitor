package com.gy.timer;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class CoreListner  implements ServletContextListener  {
	   private Timer timer	 ;
	    static long interval ;
	    public void contextDestroyed(ServletContextEvent event) {
	        if (timer != null) {
	  	      timer.cancel();
	  	      event.getServletContext().log("CoreTimer已销毁");
	        }
	  	}

	  	public void contextInitialized(ServletContextEvent event) {
	        int interval = 5000;
	        if (interval<1000) {
	      	  event.getServletContext().log("CoreTimer时间隔太小"+interval);  
	  		return;
	  	  }
	  	  timer = new Timer(true);
	        event.getServletContext().log("CoreTimer已启动" +interval );   
	        timer.schedule(new CoreTimer(event.getServletContext()), 0, interval); 
	        event.getServletContext().log("CoreTimer已添加任务"); 
	  	}

	   public static void main(String[] args) {
	 
	  }
}
