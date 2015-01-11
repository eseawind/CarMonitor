package com.gy.CarMonitor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
	
	
//	Calendar calendar1 = Calendar.getInstance();
//	calendar1.add(Calendar.DAY_OF_YEAR, -1);
//	System.err.println(calendar1.getTimeInMillis());
//	long ii =calendar1.getTimeInMillis();
//	System.err.println(ii/86400);
	
//	Calendar calendar=Calendar.getInstance();   
//	   calendar.setTime(new Date()); 
//	   System.out.println(calendar.get(Calendar.ZONE_OFFSET));//今天的日期 
	
//    Date d=new Date();   
//    SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");   
//    System.out.println("今天的日期："+df.format(d)); 
//     
//    System.out.println("两天前的日期：" + df.format(new Date(d.getTime() - 2 * 24 * 60 * 60 * 1000)));  
//    System.out.println("三天后的日期：" + df.format(new Date(d.getTime() + 3 * 24 * 60 * 60 * 1000)));	

	
//	Date d=new Date();
//	SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd"); 
//	String d2 = df.format(new Date(d.getTime() - 2 * 24 * 60 * 60 * 1000)); 
//	try {
//		Date date = df.parse(d2); //得到凌晨的时间
//		System.err.println(date.getTime());
//	} catch (ParseException e) { 
//		e.printStackTrace();
//	}
//	
//	String a = "35";
//	String b ="36";
//	System.err.println(a.compareTo(b));
	
//	List<String> list = new ArrayList<String>() ;
//	list.add("古意a");
//	list.add("合利c2");
//	list.add("古意b");
//	list.add("古意c");
//	list.add("合利a2");
//	list.add("合利b2");
////	Object[] a =  list.toArray(); 
//	String[] a = (String[])list.toArray(new String[list.size()]);
//	for (int i = 0; i < a.length; i++) {
//		for (int j = 0; j < a.length; j++) {
//			if (((String)a[i]).compareTo((String)a[j])<=0) {
//				String tmp =(String) a[i];
//				a[i]=a[j];
//				a[j]=tmp;
//			}
//		}
//	}
//	System.err.println(Arrays.asList(a));
//	for (int j = 0; j < a.length; j++) {		
//			String tmp =(String) a[j];
//			System.err.println(tmp);
//	}	
//	String tmp =list.get(0).substring(1,2);
//	List<String> list2 = new ArrayList<String>() ;
////	list2.add(list.get(0));
////	list.remove(0);
//	for (int i = 0; i < list.size(); i++) {
//		if (list.get(i).contains(tmp)) {
//			list2.add(list.get(i) );
////			list.remove(i);
//		}
//	}
//	System.err.println(list2);
//	System.err.println(list);
//	list2.addAll(list);
//	System.err.println(list2);
	
//	System.err.println(		"<table width=\"140%\" border=\"1\" cellspacing=\"1\" cellpadding=\"1\">"+
//						"<tr align=\"center\"  class=\"t1\">"+
//						"<td height=\"25\" align=\"left\" bgcolor=\"#D5E4F4\"><strong>所属公司-车牌-终端ID</strong></td>"+ 
//				 		"<td bgcolor=\"#D5E4F1\"><strong>安全数据汇报时间</strong></td>" +
//				 		"<td bgcolor=\"#D5E4F4\"><strong> 增量里程 </strong></td>" +
//				 		"<td bgcolor=\"#D5E4F4\"><strong> 线速度 </strong></td>" +
//				 		"<td bgcolor=\"#D5E4F4\"><strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;GPS&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong></td>" +
//				 		"<td bgcolor=\"#D5E4F4\"><strong>&nbsp;GPS速度</strong></td>" +
//				 		"<td bgcolor=\"#D5E4F4\"><strong>百度gps</strong></td>" +
//				 		"<td bgcolor=\"#D5E4F4\"><strong>模拟油量</strong></td>" +				 		
//				 		"<td bgcolor=\"#D5E4F4\"><strong> &nbsp;实际油量(升) </strong></td>" +
//				 		"<td bgcolor=\"#D5E4F4\"><strong> &nbsp;温度&nbsp; </strong></td>" +
//				 		"<td bgcolor=\"#D5E4F4\"><strong> &nbsp;终端状态 </strong></td>" +
//				 		"<td bgcolor=\"#D5E4F4\"><strong> &nbsp;报警状态 </strong></td>" +
//				 		"<td bgcolor=\"#D5E4F4\"><strong> &nbsp;车距 </strong></td>" +
//				 		"<td bgcolor=\"#FF0000\"><strong> 国标位置时间0200 </strong></td>" +
//				 		"<td bgcolor=\"#FF0000\"><strong> 国标上报位置0200 </strong></td>" +
//				 		"<td bgcolor=\"#FF0000\"><strong> 终端状态0200 </strong></td>" +
//				 		"<td bgcolor=\"#FF0000\"><strong> 报警状态0200 </strong></td>" +
//				 		"</tr>"    
////				 		 
//				 );
String queryday ="querydayqlj";
	String sql = 
		" with qlj_online_total as (  "                                                                                                               
		+"     select "                                                                                                                             
		+"     ter_id as ter_id, "                                                                                                                  
		+"     offduaration+decode(last_flag,0,lastduration,0) as offseconds, "                                                                     
		+"     onduration+decode(last_flag,1,lastduration,0) as  onseconds , "                                                                      
		+"     getStrTime(offduaration+decode(last_flag,0,lastduration,0)) offstr, "                                                                
		+"     getStrTime(onduration+decode(last_flag,1,lastduration,0) ) as onstr, "                                                               
		+"     offcount , "                                                                                                                         
		+"     to_char((offduaration+decode(last_flag,0,lastduration,0))/ (offduaration +lastduration +onduration  )*100,'00.00') as off_ratio "            
		+"      from  "                                                                                                                             
		+"     ( "                                                                                                                                  
		+"           select  "                                                                                                                      
		+"           ter_id, "                                                                                                                      
		+"           sum( decode(on_off_flag,1,create_time- prevtime,0)) as offduaration , "                                                        
		+"           sum( decode(on_off_flag,0,create_time- prevtime,0)) as onduration,      "                                                      
		+"           max(last_flag) as last_flag, "                                                                                                 
		+"           sum(decode(on_off_flag,0,1,0)) as offcount, "                                                                                  
		+"           decode("+ queryday +", "                                                                                                       
		+"           0 , "                                                                                                                          
		+"           max(gintime(sysdate)-lastcreate_time) , "                                                                                      
		+"           max(86400- (lastcreate_time-gintime(trunc(sysdate-"+ queryday +"))) )     "                                                         
		+"           )     as lastduration "                                                                                                        
		+"           from ( "                                                                                                                       
		+"                 select t.*, "                                                                                                            
		+"                 lag(create_time, 1, gintime(trunc(sysdate-"+ queryday +")))     over(partition by ter_id order by id asc) as prevtime , "     
		+"                 max(on_off_flag) keep   (dense_rank last order by id asc ) over(partition by ter_id ) as last_flag , "                   
		+"                 max(create_time) keep   (dense_rank last order by id asc,create_time asc ) over(partition by ter_id) as lastcreate_time "
		+"                  from sa.tbl_s_terminal_online t "                                                                                       
		+"                 where trunc(gtime(create_time)) = trunc(sysdate-"+ queryday +")   "                                                           
		+"           )t  "                                                                                                                          
		+"           group by ter_id  "                                                                                                             
		+"     )t2    "                                                                                                                             
		+"     union  "                                                                                                                             
		+"     select "                                                                                                                             
		+"       ter.id, "                                                                                                                          
		+"       0 \"离线时间\", "                                                                                                                  
		+"       decode("+queryday+",0,gintime(trunc(sysdate)),86400 ) as \"上线时间\" , "                                                          
		+"       '00小时00分00秒' offstr, "                                                                                                         
		+"       getstrtime(decode("+queryday+",0,(sysdate-trunc(sysdate))*86400,86400 )) onstr, "                                                  
		+"       0  offcount, "                                                                                                                     
		+"       '00.00' as off_ratio   "                                                                                                                 
		+"      from tbl_s_terminal ter  "                                                                                                          
		+"     where not exists (select * from  tbl_s_terminal_online ol  "                                                                         
		+"     where ter.id = ol.ter_id and ol.create_time between  gintime(trunc(sysdate-"+ queryday +"))  and gintime(trunc(sysdate-"+ queryday +"+1))  "   
		+"     ) and exists ( "                                                                                                                     
		+"     select * from tbl_s_terminal_last_pos pos  "                                                                                         
		+"     where ter.id = pos.ter_id and pos.pos_time between  gintime(trunc(sysdate-"+ queryday +"))  and gintime(trunc(sysdate-"+ queryday +"+1)) "     
		+"     and rownum =1  "                                                                                                                     
		+"     ) "                                                                                                                                  
		+" ) "                                                                                                                                      
		+" select  "                                                                                                                                
		+" t2.cp_name ,t2.plate_no,tal.*,nvl(mi.line_dis ,0)as line_dis "                                                                           
		+"  from qlj_online_total  tal "                                                                                                            
		+" inner join ( "                                                                                                                           
		+" select ter.id,ter.plate_no,com.cp_name from sa.tbl_s_terminal ter ,tbl_company com  "                                                    
		+" where ter.cp_id = com.id  "                                                                                                              
		+" )t2  "                                                                                                                                   
		+"  on tal.ter_id = t2.id  "                                                                                                                
		+" left join ( select ter_id ,sum(line_dis) as line_dis from  sa.tbl_s_safe_month_mi where lasttime  "                                      
		+"      between  gintime(trunc(sysdate-"+ queryday +"))*1000    and gintime(trunc(sysdate-"+ queryday +"+1))*1000 "                                   
		+"       group by ter_id ) mi   "                                                                                                           
		+" on tal.ter_id = mi.ter_id   "                                                                                                            
		+" order by cp_name desc ,offseconds desc   ";                                                                                              
System.err.println(sql);  


}
}

