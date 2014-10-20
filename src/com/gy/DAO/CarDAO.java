package com.gy.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.gy.CarMonitor.DBHelper;
import com.gy.Entity.CarMonitorEntity;
 
public class CarDAO {
	String id = null;
  
	Statement stat = null;
	ResultSet rs = null;
	 
	
	public List<CarMonitorEntity> getCarMonitorEntity(int i){
		String sql="select " +
				"to_char(MONITOR_date,'mm-dd:hh24')as MONITOR_date , " +
				"t1.TER_ID,t2.CP_name as CP_ID, " +
				"PLATE_NO, OFFCOUNT, " +
				"nvl(t1.milse,0)as milse ,"+
				"trunc(OFFSECONDS/3600)||'时'|| trunc( mod(OFFSECONDS,3600)/60) ||'分'|| mod(OFFSECONDS,60) ||'秒' as OFFSECONDS,"+
				"trunc((86400 - OFFSECONDS)/3600)||'时'|| trunc( mod((86400 - OFFSECONDS),3600)/60) ||'分'|| mod((86400 - OFFSECONDS),60) ||'秒'as onlineSeconds,"+
				"nvl(t3.oilmass, 0) as OILMASS," +
				"nvl(t3.line_speed, 0) as SPEED," +
				"round(nvl(t3.gps_lon_f, 0), 6) || ','||round(nvl(t3.gps_lat_f, 0), 6) as GPS,"+
				"mod(nvl(T3.ter_status, 0), 2) AS ACCSTATUS ," +
				"to_char(gtime(t3.lasttime),'yymmdd hh24:mi:ss') as pos_time "+
				",to_char(gtime(t4.pos_time),'yymmdd hh24:mi:ss') as pos_time0200 "+
				"from sa.qlj_car_monitor t1,sa.tbl_company t2 , sa.tbl_s_safe_real t3 ,sa.tbl_s_terminal_last_pos t4 " +
				" where  t1.CP_ID =t2.id and trunc(MONITOR_date) = trunc(sysdate-" +i+") " +
						"and t1.ter_id = t3.ter_id " +
						"and t1.ter_id = t4.ter_id" +
				" order by cp_id desc,OFFCOUNT desc,ter_id asc";		
		List<CarMonitorEntity> carentities = new ArrayList<CarMonitorEntity>();
		Connection conn = new DBHelper().getConn();
		System.err.println(conn);
		try{
			stat = conn.createStatement();
			System.err.println(sql);
			rs = stat.executeQuery(sql);
			while (rs.next()) {
				CarMonitorEntity cartmp = new CarMonitorEntity();
//				id = rs.getString("Student_ID");
//				String MONITOR_date, TER_ID, CP_ID, PLATE_NO, OFFCOUNT, OFFSECONDS,
//				OILMASS, SPEED, GPS, ACCSTATUS, MILSE;
				cartmp.MONITOR_date = rs.getString("MONITOR_date");
				cartmp.TER_ID = rs.getString("TER_ID");
				cartmp.CP_ID = rs.getString("CP_ID");
				cartmp.PLATE_NO = rs.getString("PLATE_NO");
				cartmp.OFFCOUNT = rs.getString("OFFCOUNT");
				cartmp.OFFSECONDS = rs.getString("OFFSECONDS");
				cartmp.onlineSeconds = rs.getString("onlineSeconds");				
				cartmp.OILMASS = rs.getString("OILMASS");
				cartmp.SPEED = rs.getString("SPEED");
				cartmp.GPS = rs.getString("GPS");
				cartmp.ACCSTATUS = rs.getString("ACCSTATUS");
				cartmp.MILSE = rs.getString("MILSE");
				cartmp.pos_time = rs.getString("pos_time");
				cartmp.pos_time0200 = rs.getString("pos_time0200");	
				carentities.add(cartmp);
//				System.err.println("test:");
//				System.err.println(rs.getString("ter_id"));
			}
			System.err.println("carentities:"+carentities.size());
		}
		catch(SQLException ex){
			ex.printStackTrace();			
		}
		//关闭数据库
			try {
				if (rs !=null) {
				rs.close();
				}
				if (stat!=null) {
					stat.close();
				}
				if (conn!=null) {
					conn.close();
				}
								
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return carentities;
	}
	public static void main(String[] args) {
	System.err.println(	new CarDAO().getCarMonitorEntity(1));	
	}
}
