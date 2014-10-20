package com.gy.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.gy.CarMonitor.DBHelper;
import com.gy.Entity.DBObjectEntity;
import com.gy.Entity.RealMonitorEntity;

public class RealMonitorDAO {
	String sql = "";
	String id = null;
	  
	Statement stat = null;
	ResultSet rs = null;
	Connection conn;
	
	/**
	 * 获取实时信息 包括安全数据和0200上报
	 * 
	 * */
	public List<RealMonitorEntity> getRealInfoList(){
		System.err.println(System.currentTimeMillis());
		List<RealMonitorEntity> list= new ArrayList<RealMonitorEntity>();
		sql ="select t2.plate_no || '-' || t1.ter_id as plateno,"+
	     "  gtime(firsttime) as firsttime,"+
	     "  to_char(gtime(lasttime),'yyyy-mm-dd hh24:mi:ss') as lasttime,"+
	     "  to_char(line_dis,'99990.99') as line_dis,"+
	     "  to_char(t1.line_speed,'99990.99') as line_speed,"+
	     "  to_char(t1.gps_speed,'99990.99') as gps_speed, "+
	     "  to_char(t1.gps_lon,'990.0000')||','|| to_char(t1.gps_lat,'00.0000') as gps,"+
	     "  to_char(t1.gps_lon_f,'990.0000')||','|| to_char(t1.gps_lat_f,'00.0000') as gps_f, "+
	     "  trunc(t1.oilvalue) as oilvalue,"+
	     "  trunc(t1.oilmass,1) as oilmass, "+
	     "  t1.ter_status,"+
	     "  t1.warn_type,"+
	     "  t1.dis_front,"+
	     "  nvl(to_char(gtime(postime0200),'yyyy-mm-dd hh24:mi:ss'),'-') as postime0200,"+
	     "  nvl(t3.gps0200,'-') as gps0200 ,"+
	     "  t3.ter_status as terstatus0200,"+
	     "  t3.ter_warn as warn0200, "+
	     "  t1.tempvalue as tempvalue, "+
	     "  nvl(t4.cp_name,'') as cp_name ," +
	     "  nvl(t2.ter_status,0) as isonline " +
	 " from sa.tbl_s_safe_real t1"+
	 " inner join  tbl_s_terminal t2 on  t1.ter_id = t2.id "+
	 " inner join  tbl_company t4 on  t2.cp_id = t4.id "+
	 " left join ("+
	 "  select ter_id,pos_time as postime0200,to_char(pos_long,'990.0000')||','||to_char(pos_lat,'990.0000') as gps0200,ter_status,ter_warn " +
	 "from sa.tbl_s_terminal_last_pos " +
	 " )t3 on t1.ter_id = t3.ter_id  " +
	 " order by t4.cp_name desc ,isonline desc ,t1.lasttime desc   ";
		System.err.println(sql);
		conn  = new DBHelper().getConn(); 
		try {
			stat = conn.createStatement();
//			System.err.println(sql);
			rs = stat.executeQuery(sql);
			while (rs.next()) {
				RealMonitorEntity obj = new RealMonitorEntity();  
				obj.PLATENO=rs.getString("PLATENO");
				obj.FIRSTTIME=rs.getString("FIRSTTIME");
				obj.LASTTIME=rs.getString("LASTTIME");
				obj.LINE_DIS=rs.getString("LINE_DIS");
				if(obj.LINE_DIS == null){
					obj.LINE_DIS="-";
				}
				obj.LINE_SPEED=rs.getString("LINE_SPEED");
				if(obj.LINE_SPEED == null){
					obj.LINE_SPEED="-";
				}				
				obj.GPS_SPEED=rs.getString("GPS_SPEED");
				if(obj.GPS_SPEED == null){
					obj.GPS_SPEED="-";
				}				
				obj.GPS=rs.getString("GPS");
				if (obj.GPS.equals(",")) {
					obj.GPS="-";
				}
				obj.GPS_F=rs.getString("GPS_F");
				if (obj.GPS_F.equals(",")) {
					obj.GPS_F="-";
				}
				obj.OILVALUE=rs.getString("OILVALUE");
				obj.OILMASS=rs.getString("OILMASS");
				obj.TER_STATUS=rs.getString("TER_STATUS");
				obj.WARN_TYPE=rs.getString("WARN_TYPE");
				obj.DIS_FRONT=rs.getString("DIS_FRONT");
				obj.POSTIME0200=rs.getString("POSTIME0200");
				obj.GPS0200=rs.getString("GPS0200");
				if (obj.GPS0200 == null||obj.GPS0200.equals(",") ) {
					obj.GPS0200="----";
				}				
				obj.TERSTATUS0200=rs.getString("TERSTATUS0200");
				obj.WARN0200=rs.getString("WARN0200");		
				obj.tempvalue=rs.getString("tempvalue");
				obj.cp_name=rs.getString("cp_name");
				obj.isonline=rs.getString("isonline");
				list.add(obj );
			}
		} catch (SQLException e) {
			e.printStackTrace();			
		}
		System.err.println(System.currentTimeMillis());
		closeDB();
		return list;
		 
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
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RealMonitorDAO odao = new RealMonitorDAO();
		List<RealMonitorEntity>  list = odao.getRealInfoList();
		System.err.println(list);
	}

}
