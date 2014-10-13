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
		sql ="select t2.plate_no || '/' || t1.ter_id as plateno,"+
	     "  gtime(firsttime) as firsttime,"+
	     "  gtime(lasttime) as lasttime,"+
	     "  line_dis,"+
	     "  trunc(t1.line_speed,2) as line_speed,"+
	     "  trunc(t1.gps_speed,2) as gps_speed, "+
	     "  trunc(t1.gps_lon,4)||','|| trunc(t1.gps_lat,4) as gps,"+
	     "  trunc(t1.gps_lon_f,4)||','|| trunc(t1.gps_lat_f,4) as gps_f, "+
	     "  trunc(t1.oilvalue) as oilvalue,"+
	     "  trunc(t1.oilmass,1) as oilmass, "+
	     "  t1.ter_status,"+
	     "  t1.warn_type,"+
	     "  t1.dis_front,"+
	     "  t3.postime0200,"+
	     "  t3.gps0200 ,"+
	     "  t3.ter_status as terstatus0200,"+
	     "  t3.ter_warn as warn0200, "+
	     "  t1.tempvalue as tempvalue "+
	 " from sa.tbl_s_safe_real t1"+
	 " inner join  tbl_s_terminal t2 on  t1.ter_id = t2.id "+
	 " left join ("+
	 " select ter_id,gtime(pos_time)as postime0200,pos_long||','||pos_lat as gps0200,ter_status,ter_warn,row_number() " +
	 " over(partition by ter_id order by id desc )cnt from sa.tbl_s_terminal_pos "+
	 " )t3 on t1.ter_id = t3.ter_id and t3.cnt =1  ";
		System.err.println(sql);
		conn  = new DBHelper().getConn(); 
		try {
			stat = conn.createStatement();
//			System.err.println(sql);
			rs = stat.executeQuery(sql);
			while (rs.next()) {
//PLATENO,FIRSTTIME,LASTTIME,LINE_DIS,LINE_SPEED,GPS_SPEED,GPS,GPS_F,OILVALUE,
//OILMASS,TER_STATUS,WARN_TYPE,DIS_FRONT,POSTIME0200,GPS0200,TERSTATUS0200,WARN0200
				RealMonitorEntity obj = new RealMonitorEntity();  
				obj.PLATENO=rs.getString("PLATENO");
				obj.FIRSTTIME=rs.getString("FIRSTTIME");
				obj.LASTTIME=rs.getString("LASTTIME");
				obj.LINE_DIS=rs.getString("LINE_DIS");
				obj.LINE_SPEED=rs.getString("LINE_SPEED");
				obj.GPS_SPEED=rs.getString("GPS_SPEED");
				obj.GPS=rs.getString("GPS");
				obj.GPS_F=rs.getString("GPS_F");
				if (obj.GPS_F.equals(",")) {
					obj.GPS_F="";
				}
				obj.OILVALUE=rs.getString("OILVALUE");
				obj.OILMASS=rs.getString("OILMASS");
				obj.TER_STATUS=rs.getString("TER_STATUS");
				obj.WARN_TYPE=rs.getString("WARN_TYPE");
				obj.DIS_FRONT=rs.getString("DIS_FRONT");
				obj.POSTIME0200=rs.getString("POSTIME0200");
				obj.GPS0200=rs.getString("GPS0200");
				obj.TERSTATUS0200=rs.getString("TERSTATUS0200");
				obj.WARN0200=rs.getString("WARN0200");		
				obj.tempvalue=rs.getString("tempvalue");
				list.add(obj );
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.err.println(System.currentTimeMillis());
		return list;
		 
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
