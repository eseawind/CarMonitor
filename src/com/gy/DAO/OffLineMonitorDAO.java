package com.gy.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.gy.CarMonitor.DBHelper;
import com.gy.Entity.DBObjectEntity;

public class OffLineMonitorDAO {
	String sql = "";
	String id = null;	  
	Statement stat = null;
	ResultSet rs = null;
	Connection conn;

	public void getOffLineDetail(){
		conn  = new DBHelper().getConn(); 
		sql="select ter_id,on_off_flag,create_time from sa.tbl_s_terminal_online where trunc(gtime(create_time))= trunc(sysdate)-1 order by ter_id,id asc ";
		try {
			stat = conn.createStatement();
//			System.err.println(sql);
			rs = stat.executeQuery(sql);
			while (rs.next()) {
				DBObjectEntity obj = new DBObjectEntity();  
				System.err.println(rs.getString("ter_id"));
//				System.err.println(obj); 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	public OffLineMonitorDAO() {
		 
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		OffLineMonitorDAO off = new OffLineMonitorDAO();
		off.getOffLineDetail();
	}

}
