package com.gy.CarMonitor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class test {
public static void main(String[] args) {

//	System.err.println(System.currentTimeMillis());
//	Calendar now =Calendar.getInstance();  
//	now.set(Calendar.DATE , now.get(Calendar.DATE)-1);
//	System.err.println(now.getTimeInMillis());
//    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");    
//    Date nowTime=new Date();    
//    String todayStr = format.format(nowTime);    
//    Date today;
//	try {
//		today = format.parse(todayStr);
//		long starttime=today.getTime();  
//		System.err.println(starttime);
//	} catch (ParseException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}    
	Calendar calendar1 = Calendar.getInstance();
	calendar1.add(Calendar.DAY_OF_YEAR, -1);
	System.err.println(calendar1.getTimeInMillis());
	long ii =calendar1.getTimeInMillis();
	System.err.println(ii/86400);
}
}
