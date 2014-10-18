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
	class OffLineDetails {
		public String ter_id,on_off_flag;
		public Integer offduration,rownumber;
		public Long create_time ;
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
				"gintime(trunc(sysdate - "+ querydays+ 
				")) as firstime," + 
				"trunc(nvl(t.create_time-t2.create_time,t.create_time-gintime(trunc(sysdate) -" +
				querydays +
				"))) as offduration " +
				" from " +
				"(   select ter_id,on_off_flag,create_time,rownum as rownum1  from " +
				"(select distinct  ter_id, on_off_flag,create_time,at_time  " +
				"from tbl_s_terminal_online where " +
				"trunc(gtime(create_time)) = trunc(sysdate) - " +
				querydays+
				" order by ter_id,  create_time asc ,at_time asc  ))T " +
				"LEFT join " +
				"(   select ter_id,on_off_flag,create_time,rownum+1 as rownum1  " +
				"from ( select distinct  ter_id, on_off_flag,create_time,at_time  " +
				"from tbl_s_terminal_online where trunc(gtime(create_time)) = trunc(sysdate) -" +
				querydays+
				"order by ter_id,  create_time asc ,at_time asc  ))T2 " +
				"on t.ter_id = t2.ter_id and t.rownum1 = t2.rownum1 " 
//				+ " where t.ter_id=30 " ;
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
//					System.err.println("offduration:"+rs.getString("offduration"));
					offlinetmp.offduration = Integer.valueOf( rs.getString("offduration"));
					offlinetmp.create_time = Long.parseLong(rs.getString("create_time"));
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
	
	//返回处理后的离线统计结果
		public   List<OffLineStatEntity> getOffLineStat(List<OffLineDetails>  listofflinedetails,int querydays){
			 List<OffLineStatEntity> listofflinestat = new ArrayList<OffLineStatEntity>();
			 String terid_tmp ="initid";	//终端ID 
			 int offcount =0;	   // 离线次数
			 long offseconds = 0;  //离线时间秒
			 int recordNUM= 0 ;    //纪录数 
			 int duration =0  ;    //时间段长度
			 long onlineduration=0; //在线时长
			 OffLineDetails offlinedetailsTMP=null; 
			 for (int i = 0; i < listofflinedetails.size(); i++) {
				 OffLineDetails  offdetail = listofflinedetails.get(i);
				//判定终端是否结束 初始化终端ID
				 if (i==0) {
					 terid_tmp = offdetail.ter_id;
					 offlinedetailsTMP= offdetail;
				}
				 //出现新的终端
				 if (!offdetail.ter_id.equals(terid_tmp)) {
					 OffLineStatEntity offlinestat = new OffLineStatEntity();
					 offlinestat.plate_no = terid_tmp;
					 offlinestat.offcount = String.valueOf( offcount);
						if( offdetail.on_off_flag.equals("1")){  //状态为1代表上线，说明前面一个时间段为离线时长				
							onlineduration=onlineduration+getlastofftimes(listofflinedetails.get(i-1).create_time,querydays);
						}else {
							offseconds=offseconds+getlastofftimes(listofflinedetails.get(i-1).create_time,querydays);						 
						}					 
					 offlinestat.offduration= String.valueOf(offseconds);
					 offlinestat.onlineduration =String.valueOf(onlineduration);  //当日在线时长
					 listofflinestat.add(offlinestat);
					 
					 //重置统计参数
					   offcount =0;		// 离线次数
					   offseconds = 0;  	//离线时间秒
					   onlineduration=0;    //在线时长
					   recordNUM= 0 ;		//纪录数 
					   duration =0  ; //时间段长度
					   terid_tmp =  offdetail.ter_id;
				}
	
				if( offdetail.on_off_flag.equals("1")){  //状态为1代表上线，说明前面一个时间段为离线时长				
					offseconds=offseconds+offdetail.offduration;
					offcount++;
				}else {
					//在线时长
					onlineduration=onlineduration+offdetail.offduration;
				} 
				recordNUM++;
				 //最后一条数据情况
				 if (i==listofflinedetails.size()-1 ) {
					 OffLineStatEntity offlinestat = new OffLineStatEntity();
					 offlinestat.plate_no = terid_tmp;
					 System.err.println("最后一条数据情况"+offseconds);
					 offlinestat.offcount = String.valueOf( offcount);				 
						if( offdetail.on_off_flag.equals("1")){  //状态为1代表上线，说明前面一个时间段为离线时长				
							//在线时长
							System.err.println("在线："+getlastofftimes(offdetail.create_time,querydays));
							System.err.println("在线时长："+getTimes(getlastofftimes(offdetail.create_time,querydays) ));
							onlineduration=onlineduration+getlastofftimes(offdetail.create_time,querydays);
						}else {
							offseconds=offseconds+getlastofftimes(offdetail.create_time,querydays);						 
						}
					offlinestat.offduration= String.valueOf(offseconds);
					offlinestat.onlineduration = String.valueOf(onlineduration); ;  //当日在线时长	
					listofflinestat.add(offlinestat);
				}			
			}
			 
			 return listofflinestat;
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
	 * 获取终端最后离线后离统计当日的时长 返回秒 
	 * 如23时 离线 则返回3600 
	 * curtime统计天最早时间 queryday查询时间 offsends离线时间秒
	 **/
	public static long getlastofftimes(long curtime,int queryday ){
		System.err.println( "getlastofftimes-q:"+ queryday);
		if (queryday==0) {
			System.err.println(System.currentTimeMillis());
			return System.currentTimeMillis()/1000-curtime ;
		}
		else {			
			Date d=new Date();
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd"); 
			String d2 = df.format(new Date(d.getTime() - queryday * 24 * 60 * 60 * 1000));
			System.err.println("d2"+d2);
			try {
				Date date = df.parse(d2); //得到凌晨的时间
//				System.err.println(date.getTime());
				System.err.println("date.getTime():"+date.getTime()/1000);
				System.err.println("curtime:"+curtime);
				System.err.println("最后记录："+(86400+date.getTime()/1000 -curtime));
				return  (86400+date.getTime()/1000 -curtime);
			} catch (ParseException e) { 
				e.printStackTrace();
			}	
		}
		return 0;
	} 
	private static String getTimes(Long longtime){
		return longtime/3600+"时"+ (longtime%3600)/60+"分"+ longtime%60+"秒";
	}
	public static void main(String[] args) {
		TestOfflineDao off = new TestOfflineDao(); 
//		System.err.println();
		List<OffLineStatEntity> listofflinestat = off.getOffLineDetails(2);
		System.err.println(listofflinestat.size());
		for (int i = 0; i < listofflinestat.size(); i++) {
//		System.err.println(listofflinestat.get(i).plate_no +"--"+listofflinestat.get(i).offduration);
			int offseconds = Integer.valueOf(listofflinestat.get(i).offduration);
			System.err.println("终端:"+listofflinestat.get(i).plate_no  +
					" 的离线时长为"+getTimes(Long.valueOf(listofflinestat.get(i).offduration))
			+"offseconds:"+offseconds 	+ "离线次数" +	listofflinestat.get(i).offcount +
			"在线时长"+getTimes(Long.valueOf(listofflinestat.get(i).onlineduration))  
			);
		} 
//		Long i = Long.parseLong("1413360701000");
//		Long i1= Long.parseLong("1413360701000",10);
//		System.err.println(getlastofftimes(i1,2));
//		
//		System.err.println(getTimes(getlastofftimes(i1,2)));
//		
		 
	}
}


