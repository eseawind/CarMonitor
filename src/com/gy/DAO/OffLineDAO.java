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
import com.gy.Entity.OffLineStatEntity;
public class OffLineDAO {
	//终端上下线明细类
	class OffLineDetails {
		public String ter_id,on_off_flag;
		public int offtime,rownumber;
	}
	String id = null; 
	Statement stat = null;
	ResultSet rs = null; 
	Connection conn=null;
	
	public List<OffLineStatEntity> getOffLineDetails(int querydays){
		List<OffLineDetails> listofflinedetails = new ArrayList<OffLineDetails>();
		String sql="select ter_id,on_off_flag," +
				"trunc(create_time-gintime(trunc(sysdate)-1)) as offtime"+
		",row_number() over(partition by ter_id order by create_time asc ,id asc ) as rownumber"+ 
		" from sa.tbl_s_terminal_online where trunc(gtime(create_time)) = trunc(sysdate) - 1" 
		+" and ter_id =30 "
		; 
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
					offlinetmp.offtime = Integer.valueOf( rs.getString("offtime"));
					offlinetmp.rownumber = Integer.valueOf(rs.getString("rownumber"));
				} catch (Exception e) {
					System.err.println("异常纪录"+rs.getString("ter_id"));		
				}
//				System.err.println(rs.getString("ter_id")+"--"+rs.getString("rownumber"));
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
			 System.err.println(i);
			 OffLineDetails offdetail = listofflinedetails.get(i);	 
			//初始化---------------------------------------------------
//			if ( !offdetail.ter_id.equals("initid")& !offdetail.ter_id.equals(terid_tmp)& offdetail.rownumber==1) {				
//				if (querydays>0) { //查询前几天的纪录时需要从23:59:59分扣起 如果是当天纪录则只需从当前时间算起 
//					if (offcounts>1) { 
//					}
//					offlinedetailsTMP=listofflinedetails.get(i-1);
//					if ( offlinedetailsTMP.on_off_flag.equals("0")) {
//						offlineduration=offlineduration+ (86400-offlinedetailsTMP.offtime);						
//					}
//				}else{ //统计当天情况 					
//				}
//				System.err.println("终端:"+terid_tmp + " 的离线时长为"+offseconds/3600+"时"+ (offseconds%3600)/60+"分"+ offseconds%60+"秒");
//				terid_tmp = offdetail.ter_id;
//				offcounts=0;
//				offseconds=0;
//				recordNUM=1;
//				offlineduration=0; 
//			} 
			//end 初始化------------------------------------------------
			
			System.err.println("该:"+offdetail.ter_id + "时长："+ (offdetail.offtime-offlineduration) + "当前状态:"+ offdetail.on_off_flag );
			if( offdetail.on_off_flag.equals("1")){  //状态为1代表上线，说明前面一个时间段为离线时长
				offseconds =offseconds+( offdetail.offtime-offlineduration);
				System.err.println("离线时间:" + offseconds);
				offcounts++;
			}
			offlineduration=offdetail.offtime;	 		
			recordNUM++; 
		}
		 
		 return listofflinestat;
	};

	public static void main(String[] args) {
		OffLineDAO off = new OffLineDAO(); 
		System.err.println(off.getOffLineDetails(1));
	}
}


