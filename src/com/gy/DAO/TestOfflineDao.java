package com.gy.DAO;

import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List; 
import java.util.Map;
import com.gy.CarMonitor.*; 
import com.gy.DAO.OffLineDAO.OffLineDetails;
import com.gy.Entity.OffLineStatEntity;
public class TestOfflineDao {
	//终端上下线明细类
	class OffLineDetails {
		public String ter_id,on_off_flag;
		public Integer offduration,rownumber;
	}
	String id = null; 
	Statement stat = null;
	ResultSet rs = null; 
	Connection conn=null;
	
	public List<OffLineStatEntity> getOffLineDetails(int querydays){
		List<OffLineDetails> listofflinedetails = new ArrayList<OffLineDetails>();
		String sql= "select " +
				"t.ter_id ," +
				"t.on_off_flag," +
				"t.create_time, " +
				"trunc(nvl(t.create_time-t2.create_time,t.create_time-gintime(trunc(sysdate) - 1))) as offduration " +
				" from " +
				"(   select ter_id,on_off_flag,create_time,rownum as rownum1  from " +
				"(select distinct  ter_id, on_off_flag,create_time,at_time  " +
				"from tbl_s_terminal_online where " +
				"trunc(gtime(create_time)) = trunc(sysdate) - 1   " +
				"order by ter_id,  create_time asc ,at_time asc  ))T " +
				"LEFT join " +
				"(   select ter_id,on_off_flag,create_time,rownum+1 as rownum1  " +
				"from ( select distinct  ter_id, on_off_flag,create_time,at_time  " +
				"from tbl_s_terminal_online where trunc(gtime(create_time)) = trunc(sysdate) - 1  " +
				"order by ter_id,  create_time asc ,at_time asc  ))T2 " +
				"on t.ter_id = t2.ter_id and t.rownum1 = t2.rownum1 " ; 
		conn = new DBHelper().getConn(); 
		try{
			stat = conn.createStatement();
			System.err.println(sql);
			rs = stat.executeQuery(sql);
			while (rs.next()) {  
				OffLineDetails offlinetmp = new OffLineDetails();
				offlinetmp.ter_id = rs.getString("ter_id");
				offlinetmp.on_off_flag = rs.getString("on_off_flag");
				try { 
//					System.err.println("offduration:"+rs.getString("offduration"));
					offlinetmp.offduration = Integer.valueOf( rs.getString("offduration")); 
				} catch (Exception e) {
					System.err.println("异常纪录"+rs.getString("ter_id"));	
					e.printStackTrace();
					System.err.println(rs.getString("offduration"));
				}
//				System.err.println(rs.getString("ter_id")+"--"+rs.getString("offduration"));
				listofflinedetails.add( offlinetmp);
			}
			System.err.println(listofflinedetails);
		}
		catch(SQLException ex){
			ex.printStackTrace();
		}
		//关闭数据库
		closeDB(); 
		//返回统计后的离线结果
		return  getOffLineStat(listofflinedetails,querydays);
//		return null;
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
	//返回处理后的离线统计结果
	public   List<OffLineStatEntity> getOffLineStat(List<OffLineDetails>  listofflinedetails,int querydays){
		 List<OffLineStatEntity> listofflinestat = new ArrayList<OffLineStatEntity>();
		 String terid_tmp ="initid";	//终端ID 
		 int offcounts =0;		// 离线次数
		 int offseconds = 0;  //离线时间秒
		 int recordNUM= 0 ; //纪录数 
		 int offlineduration =0  ; //时间段长度
		 OffLineDetails offlinedetailsTMP=null;
		 System.err.println(listofflinedetails.size());
		 for (int i = 0; i < listofflinedetails.size(); i++) {
			 OffLineDetails  offdetail = listofflinedetails.get(i);
			 OffLineStatEntity offlinestat = new OffLineStatEntity();
			//判定终端是否结束 初始化终端ID
			 if (i==0) {
				 terid_tmp = offdetail.ter_id;
			}
			 //出现新的终端
			 if (!offdetail.ter_id.equals(terid_tmp)) {
				 offlinestat.plate_no = terid_tmp;
				 offlinestat.offduration= String.valueOf(offseconds);
				 offlinestat.offtimes = "1";
				 offlinestat.onlineduration = "1";  //当日在线时长
				 listofflinestat.add(offlinestat);
				 //重置统计参数
				   offcounts =0;		// 离线次数
				   offseconds = 0;  	//离线时间秒
				   recordNUM= 0 ;		//纪录数 
				   offlineduration =0  ; //时间段长度
				   terid_tmp =  offdetail.ter_id;
			}
//			System.err.println("该:"+offdetail.ter_id + "时长："+ (offdetail.offtime-offlineduration) + "当前状态:"+ offdetail.on_off_flag );
			if( offdetail.on_off_flag.equals("1")){  //状态为1代表上线，说明前面一个时间段为离线时长
//				offseconds =offseconds+( offdetail.offtime-offlineduration);
				System.err.println(terid_tmp+ "离线时间:" + offseconds);
				offseconds=offseconds+offdetail.offduration;
				offcounts++;
			}
//			offlineduration=offdetail.offtime;	 		
			recordNUM++; 
		}
		 
		 return listofflinestat;
	};

	public static void main(String[] args) {
		TestOfflineDao off = new TestOfflineDao(); 
//		System.err.println();
		List<OffLineStatEntity> listofflinestat =off.getOffLineDetails(1);
		for (int i = 0; i < listofflinestat.size(); i++) {
//			System.err.println(listofflinestat.get(i).plate_no +"--"+listofflinestat.get(i).offduration);
			int offseconds = Integer.valueOf(listofflinestat.get(i).offduration);
			System.err.println("终端:"+listofflinestat.get(i).plate_no  +
					" 的离线时长为"+offseconds/3600+"时"+ (offseconds%3600)/60+"分"+ offseconds%60+"秒");
		}
	}
}


