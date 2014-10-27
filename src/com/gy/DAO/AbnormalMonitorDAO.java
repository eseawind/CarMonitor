package com.gy.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.gy.CarMonitor.*;
import com.gy.Entity.AbnormalMonitorEntity;
public class AbnormalMonitorDAO {
	Statement stat = null;
	ResultSet rs = null; 
	Connection conn=null;
	
	/**
	 * 返回异常点 列表
	 * */
	public List<AbnormalMonitorEntity> getAbnormalDataList(int curpage,String ter_id ,int pagecount){
		List<AbnormalMonitorEntity> abnormalList = new ArrayList<AbnormalMonitorEntity>();
		String strbasesql ="select " +
				"ter_id,last_recid,next_recid," +
				"last_lasttime,next_lasttime," +
				"duration_sub," +
				"last_gpslon,last_gpslat," +
				"next_gpslon,next_gpslat," +
				"last_status,next_status," +
				"crtime " +
				"from sa.qlj_s_car_exce where  1=1 and ( mod(last_status,2)=1 and  mod(next_status,2)=1 and last_speed > 0 ) ";
		String strteridcondition =" ";
		String strpageCondition = " ";
		if (ter_id !=null & ter_id.length()>0) {
//			strteridcondition =" and ter_id = " +ter_id;
			strteridcondition =" " +ter_id;
		}
		if (curpage<1) {
			curpage=1;
		}
		if (pagecount<20) {
			pagecount=20;
		}
				
		String sql = strbasesql +strteridcondition + " and rownum <=" +curpage*pagecount 
		+" minus " +
		strbasesql +strteridcondition + " and rownum <=" +(curpage-1)*pagecount +
		" order by 1,4 desc ";
		System.err.println(sql);
		
		conn = new DBHelper().getConn(); 
		try {
			stat = conn.createStatement();
			rs = stat.executeQuery(sql);
			while (rs.next()) {  
				AbnormalMonitorEntity abnormal = new AbnormalMonitorEntity(); 	
				abnormal.ter_id = rs.getString("ter_id");
				abnormal.last_recid = rs.getString("last_recid");
				abnormal.next_recid = rs.getString("next_recid");
				abnormal.last_lasttime = rs.getString("last_lasttime");
				abnormal.next_lasttime = rs.getString("next_lasttime");
				abnormal.duration_sub = rs.getString("duration_sub");
				abnormal.last_gpslon = rs.getString("last_gpslon");
				abnormal.last_gpslat = rs.getString("last_gpslat");
				abnormal.next_gpslon = rs.getString("next_gpslon");
				abnormal.next_gpslat = rs.getString("next_gpslat");
				abnormal.last_status = rs.getString("last_status");
				abnormal.next_status = rs.getString("next_status");
				abnormal.crtime = rs.getString("crtime");
//				System.err.println(rs.getString("last_lasttime"));
				abnormalList.add(abnormal);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return abnormalList;
	}
	public static void main(String[] args) {
		AbnormalMonitorDAO dao = new AbnormalMonitorDAO();
		List<AbnormalMonitorEntity> lsit=dao.getAbnormalDataList(2, "", 40);
		for (AbnormalMonitorEntity rec : lsit) {
			System.err.println(rec.crtime);
		}
	}
}
