package com.gy.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.gy.CarMonitor.DBHelper;
import com.gy.Entity.DBObjectEntity;
import com.gy.Entity.DBProcLogEntity;
import com.gy.Entity.OracleProcTaskEntity;

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
		System.err.println("getDBInvalidObject"+System.currentTimeMillis());
		List<DBObjectEntity> listobject = new ArrayList<DBObjectEntity>();
		conn  = new DBHelper().getConn(); 
		 sql = "select o.STATUS,OBJECT_NAME,object_type from all_objects o where STATUS='INVALID' and o.owner like 'SA' order by object_type,OBJECT_NAME ";
//		sql = "select o.STATUS,OBJECT_NAME,object_type from all_objects o where STATUS='INVALID' ";
			try {
				stat = conn.createStatement();
//				System.err.println(sql);
				rs = stat.executeQuery(sql);
				while (rs.next()) {
					DBObjectEntity obj = new DBObjectEntity();  
					obj.OBJECT_NAME=rs.getString("OBJECT_NAME");
					obj.STATUS=rs.getString("STATUS");
					obj.object_type=rs.getString("object_type");
//					System.err.println(obj);
					listobject.add(obj );
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.err.println("getDBInvalidObject-end"+System.currentTimeMillis());
			closeDB();
			return listobject;
	}

	/**
	 * 获取定时任务配置信息 
	 * STATUS,OBJECT_NAME,object_type;
	 * */	
	public List<OracleProcTaskEntity> getDBProcTaskList(){
		System.err.println(System.currentTimeMillis());
		List<OracleProcTaskEntity> listobject = new ArrayList<OracleProcTaskEntity>();
		conn  = new DBHelper().getConn(); 
		sql="select job,what,last_date,last_sec,next_date,next_sec,lower(interval) as interval from user_jobs order by last_date desc , what ";		
			try {
				stat = conn.createStatement();
//				System.err.println(sql);
				rs = stat.executeQuery(sql);
				while (rs.next()) {
					OracleProcTaskEntity obj = new OracleProcTaskEntity();  
					obj.job=rs.getString("job");
					obj.what=rs.getString("what");
					obj.last_date=rs.getString("last_date");
					obj.last_sec=rs.getString("last_sec");
					obj.next_date=rs.getString("next_date");
					obj.next_sec=rs.getString("next_sec");
					obj.interval=rs.getString("interval"); 
//					System.err.println(obj);
					listobject.add(obj );
				}
			} catch (SQLException e) {
				e.printStackTrace(); 
			} 
			closeDB();
			return listobject;
	}	
	public List<DBProcLogEntity> getProcLogList(){
		List<DBProcLogEntity> prologlist = new ArrayList<DBProcLogEntity>();
		sql = "select " +
				"taskname," +
				"to_char(starttime ,'yyyy-mm-dd hh24:mi:ss') as starttime ," +
				"to_char(finishtime ,'yyyy-mm-dd hh24:mi:ss') as finishtime," +
				"isfinished," +
				"describtion," +
				"trunc((finishtime-starttime)*86400) as duration  from" +
				" (select taskname, starttime,finishtime,isfinished,describtion ," +
				" row_number() over( partition by taskname order by id desc ) as cnt " +
				" from sa.tbl_s_safe_task_log )where cnt <2 " +
				" order by taskname ";
		
		conn  = new DBHelper().getConn();  
		try {
			stat = conn.createStatement();
			rs = stat.executeQuery(sql);
			while (rs.next()) {
				DBProcLogEntity prolog = new DBProcLogEntity() ;
				prolog.taskname= rs.getString("taskname");
				prolog.starttime= rs.getString("starttime");
				prolog.finishtime= rs.getString("finishtime");
				prolog.isfinished= rs.getString("isfinished");
				prolog.describtion= rs.getString("describtion");
				prolog.duration= rs.getString("duration");
				prologlist.add(prolog);
			}
			
		} catch (SQLException e) { 
			e.printStackTrace(); 
		}
		closeDB();
		return prologlist;
		
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
		OracleMonitorDAO odao = new OracleMonitorDAO();
		List<DBObjectEntity>  list = odao.getDBInvalidObject();
		System.err.println(list);
	}
}
