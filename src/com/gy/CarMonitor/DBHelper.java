package com.gy.CarMonitor;


import java.sql.*;

public class DBHelper {	
//	private String dbUrl="jdbc:mysql://localhost:3306/sushe";
	static String dbUrl="jdbc:oracle:thin:@112.124.114.79:1521:ORCL";  	
//	static String dbUrl="jdbc:oracle:thin:@192.168.1.185:1523:LENGLIAN";
	private String dbUser="sa";
	private String dbPassword="sa";
    static String jdbcName="oracle.jdbc.driver.OracleDriver";  
//	private String jdbcName="com.mysql.jdbc.Driver";
	
	
	//连接数据库
	public Connection getConn(){
		Connection conn = null; 
		try{
			Class.forName(jdbcName); 
		}
		catch(Exception e){ 
			System.err.println(e);
		}
		try{
			conn=DriverManager.getConnection(dbUrl,dbUser,dbPassword);
		}
		catch(SQLException ex){ 
			ex.printStackTrace();
		}
		return conn;		
	}
	
//    测试
	public static void main(String[] args)
	{ 
		System.out.println(new DBHelper().getConn()); 
	}
	
}
