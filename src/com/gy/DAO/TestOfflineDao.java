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
import com.gy.DAO.OffLineDAO.OffLineDetails;
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
		TestOfflineDao t = new TestOfflineDao();
		t.getOffLineStat(t.getOffLineDetails(1));
 
		 
	}
}


