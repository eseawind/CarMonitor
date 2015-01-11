package com.gy.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.gy.CarMonitor.DBHelper;
import com.gy.Entity.CarMonitorEntity;
 
public class OffLineDAO {
	String id = null;
	Connection conn;
	Statement stat = null;
	ResultSet rs = null; 
	public List<CarMonitorEntity> getCarMonitorEntity(int queryday){
//		String sql="select " +
//				"to_char(MONITOR_date,'mm-dd:hh24')as MONITOR_date , " +
//				"t1.TER_ID,t2.CP_name as CP_ID, " +
//				"PLATE_NO, OFFCOUNT, " +
//				"nvl(t1.milse,0)as milse ,"+
//				"trunc(OFFSECONDS/3600)||'时'|| trunc( mod(OFFSECONDS,3600)/60) ||'分'|| mod(OFFSECONDS,60) ||'秒' as OFFSECONDS,"+
//				"trunc((86400 - OFFSECONDS)/3600)||'时'|| trunc( mod((86400 - OFFSECONDS),3600)/60) ||'分'|| mod((86400 - OFFSECONDS),60) ||'秒'as onlineSeconds,"+
//				"nvl(t3.oilmass, 0) as OILMASS," +
//				"nvl(t3.line_speed, 0) as SPEED," +
//				"round(nvl(t3.gps_lon_f, 0), 6) || ','||round(nvl(t3.gps_lat_f, 0), 6) as GPS,"+
//				"mod(nvl(T3.ter_status, 0), 2) AS ACCSTATUS ," +
//				"to_char(gtime(t3.lasttime),'yymmdd hh24:mi:ss') as pos_time "+
//				",to_char(gtime(t4.pos_time),'yymmdd hh24:mi:ss') as pos_time0200 "+
//				"from sa.qlj_car_monitor t1,sa.tbl_company t2 , sa.tbl_s_safe_real t3 ,sa.tbl_s_terminal_last_pos t4 " +
//				" where  t1.CP_ID =t2.id and trunc(MONITOR_date) = trunc(sysdate-" +i+") " +
//						"and t1.ter_id = t3.ter_id " +
//						"and t1.ter_id = t4.ter_id" +
//				" order by cp_id desc,OFFCOUNT desc,ter_id asc";		
		
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
			+"     select * from tbl_s_safe_month_hour hour  "                                                                                         
			+"     where ter.id = hour.ter_id and hour.lasttime between  " 
			+" 		gintime(trunc(sysdate-"+ queryday +"))*1000  and gintime(trunc(sysdate-"+ queryday +"+1))*1000 "     
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
                                                                                                                                                  

		List<CarMonitorEntity> carentities = new ArrayList<CarMonitorEntity>();
		conn = new DBHelper().getConn();
		System.err.println("conn"+conn);
		try{
			stat = conn.createStatement();
			System.err.println(sql);
			rs = stat.executeQuery(sql);
			while (rs.next()) {
				CarMonitorEntity cartmp = new CarMonitorEntity(); 
				System.err.println(rs.getString("cp_name"));
				cartmp.cp_name = rs.getString("cp_name");
				cartmp.plate_no = rs.getString("plate_no");
				cartmp.ter_id = rs.getString("ter_id");
				cartmp.offseconds = rs.getString("offseconds");
				cartmp.onseconds = rs.getString("onseconds");
				cartmp.offstr = rs.getString("offstr");
				cartmp.onstr = rs.getString("onstr");
				cartmp.offcount = rs.getString("offcount");
				cartmp.off_ratio = rs.getString("off_ratio");
				cartmp.line_dis = rs.getString("line_dis"); 
//				id = rs.getString("Student_ID");
//				String MONITOR_date, TER_ID, CP_ID, PLATE_NO, OFFCOUNT, OFFSECONDS,
//				OILMASS, SPEED, GPS, ACCSTATUS, MILSE;
//				cartmp.MONITOR_date = rs.getString("MONITOR_date");
//				cartmp.TER_ID = rs.getString("TER_ID");
//				cartmp.CP_ID = rs.getString("CP_ID");
//				cartmp.PLATE_NO = rs.getString("PLATE_NO");
//				cartmp.OFFCOUNT = rs.getString("OFFCOUNT");
//				cartmp.OFFSECONDS = rs.getString("OFFSECONDS");
//				cartmp.onlineSeconds = rs.getString("onlineSeconds");				
//				cartmp.OILMASS = rs.getString("OILMASS");
//				cartmp.SPEED = rs.getString("SPEED");
//				cartmp.GPS = rs.getString("GPS");
//				cartmp.ACCSTATUS = rs.getString("ACCSTATUS");
//				cartmp.MILSE = rs.getString("MILSE");
//				cartmp.pos_time = rs.getString("pos_time");
//				cartmp.pos_time0200 = rs.getString("pos_time0200");	
				carentities.add(cartmp);
//				System.err.println("test:");
//				System.err.println(rs.getString("ter_id"));
			}
			System.err.println("carentities:"+carentities.size());
		}
		catch(SQLException ex){
			ex.printStackTrace();			
		} 
		closeDB(); 
		return carentities;
	}
	private void closeDB(){
		//关闭数据库
		try {
			System.err.println("关闭数据库连接");
			if (rs !=null) {
				System.err.println("关闭rs");
				rs.close();
			}
			if (stat!=null) {
				System.err.println("关闭stat");
				stat.close();
			}
			if (conn!=null) {
				System.err.println("关闭conn");
				conn.close();
			} 		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
	System.err.println(	new OffLineDAO().getCarMonitorEntity(0));	
		
	}
}
