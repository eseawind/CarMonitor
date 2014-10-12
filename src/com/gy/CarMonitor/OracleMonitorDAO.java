package com.gy.CarMonitor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.gy.Entity.DBObjectEntity;

public class OracleMonitorDAO {
	String sql = "";
	String id = null;
	  
	Statement stat = null;
	ResultSet rs = null;
	Connection conn;

	/**
	 * 获取存储过程是否有效 
	 * STATUS,OBJECT_NAME,object_type;
	 * */	
	public List<DBObjectEntity> getDBInvalidObject(){
		List<DBObjectEntity> listobject = new ArrayList<DBObjectEntity>();
		conn  = new DBHelper().getConn(); 
		 sql = "select o.STATUS,OBJECT_NAME,object_type from all_objects o where STATUS='INVALID' and o.owner like 'SA' ";
			try {
				stat = conn.createStatement();
				System.err.println(sql);
				rs = stat.executeQuery(sql);
				while (rs.next()) {
					DBObjectEntity obj = new DBObjectEntity();  
					obj.OBJECT_NAME=rs.getString("OBJECT_NAME");
					obj.STATUS=rs.getString("STATUS");
					obj.object_type=rs.getString("object_type");
					System.err.println(obj);
					listobject.add(obj );
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return listobject;
	}
	
	public static void main(String[] args) {
		OracleMonitorDAO odao = new OracleMonitorDAO();
		List<DBObjectEntity>  list = odao.getDBInvalidObject();
		System.err.println(list);
	}
}
