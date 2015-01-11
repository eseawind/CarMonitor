package com.gy.DAO;

import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List; 
import java.util.Map;
import com.gy.CarMonitor.*; 
import com.gy.DAO.OffLineDAOold.OffLineDetails;
import com.gy.Entity.OffLineStatEntity;
public class TestOfflineDao {
	//终端上下线明细类
	class OffLineDetail {
		public String plate_no,cp_name,ter_id,on_off_flag;
		Long curtime;
		public Long cnt;
		public Long at_time ;
	}
	String id = null; 
	Statement stat = null;
	ResultSet rs = null; 
	Connection conn=null;  
	/**
	 * 获取离线统计信息 
	 * 
	 * */ 
	private List<OffLineDetail> getOffLineDetails(int querydays){
		List<OffLineDetail> listdetail = new ArrayList<OffLineDetail>();
		String sql= "select " +
				"plate_no," +
				"cp_name," +
				"ter_id," +
				"at_time," +
				"on_off_flag," +
				"trunc(create_time- gintime(trunc(sysdate-1))) as curtime ," +
				"sum(decode(line.on_off_flag,1,1,0,1,null,0)) cnt " +
				" from " +
				" ( select ter.id ,ter.plate_no,co.cp_name " +
				" from sa.tbl_s_terminal ter,sa.tbl_s_car_info car ,sa.tbl_company co " +
				" where ter.car_id = car.id and ter.cp_id = co.id ) ter2 " +
				" inner join sa.tbl_s_terminal_online line  " +
				" on ter2.id = line.ter_id  and " +
				" at_time between gintime(trunc(sysdate-1)) and gintime(trunc(sysdate-1))+86400" +
//				" where line.ter_id = 5 " +
				" group by  cp_name,plate_no,ter_id,at_time,on_off_flag,create_time " +
				" order by  cp_name desc,plate_no,ter_id,create_time asc,at_time asc  "  ;
		conn = new DBHelper().getConn();
		
		try{
			stat = conn.createStatement();
			System.err.println(sql);
			rs = stat.executeQuery(sql);
			while (rs.next()) {  
				OffLineDetail detail = new OffLineDetail();
				detail.plate_no = rs.getString("plate_no");
				detail.ter_id   = rs.getString("ter_id");
				detail.curtime  = Long.valueOf(rs.getString("curtime"));
				listdetail.add(detail);
//			  System.err.println(rs.getString("plate_no"));
			}
		}
		catch(SQLException ex){
			ex.printStackTrace();
		}
		//关闭数据库
		closeDB();  
		return listdetail;
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
	
	public List<OffLineStatEntity> getOffLineStat(List<OffLineDetail> offlinedetail){
		List<OffLineStatEntity>  liststat = new ArrayList<OffLineStatEntity>();
		long offseconds=0,onseconds = 0;
		String lastterid ="";
		for (int i = 0; i < offlinedetail.size(); i++) {
			OffLineDetail detail = offlinedetail.get(i);  
			OffLineStatEntity stat = new OffLineStatEntity();
			if (isNewTerminal(lastterid,detail.ter_id)) {
				System.err.println(detail.ter_id + i);
				stat.ter_id=detail.ter_id;
			}
			
			
			lastterid=detail.ter_id;
		}
		
		
		return liststat;
	}
	
	private boolean isNewTerminal(String lastterid ,String curterid){
		return !lastterid.equals(curterid);
	}
	
	public static void main(String[] args) {
		int queryday =0;
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

		System.err.println(sql);
	
	}
}


