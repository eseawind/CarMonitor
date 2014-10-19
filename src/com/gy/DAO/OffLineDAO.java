package com.gy.DAO;
 
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;  
import com.gy.CarMonitor.*;  
import com.gy.Entity.OffLineStatEntity;
public class OffLineDAO {
	//终端上下线明细类
	class OffLineDetails {
		public String ter_id,on_off_flag,cp_name;
		public Integer offduration;
		public Long create_time ; 
	}
	String id = null; 
	Statement stat = null;
	ResultSet rs = null; 
	Connection conn=null;  
	/**
	 * 获取离线统计信息 
	 * 
	 * */
	public List<OffLineStatEntity> getOffLineStat(int querydays){
		List<OffLineDetails> listofflinedetails = new ArrayList<OffLineDetails>();
		
		String sql= "select " +
				"ter2.plate_no||'-'||t.ter_id as ter_id ," +
				"ter2.cp_name," + 				
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
				"on t.ter_id = t2.ter_id and t.rownum1 = t2.rownum1 "+
				" left join  (" + 
				" select ter.id,ter.plate_no,com.cp_name from sa.tbl_s_terminal ter ,sa.tbl_company  com where ter.cp_id= com.id ) ter2 "+ 
						   " on t.ter_id = ter2.id"  
//				" order by ter2.cp_name, t.ter_id asc"
//				+ " where t.ter_id in(6,7,30) " ;
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
				offlinetmp.cp_name = rs.getString("cp_name"); 
				try { 
					offlinetmp.offduration = Integer.valueOf( rs.getString("offduration"));
					offlinetmp.create_time = Long.parseLong(rs.getString("create_time"));
				} catch (Exception e) {
					System.err.println("异常纪录"+rs.getString("ter_id"));	
					e.printStackTrace();
				}
				listofflinedetails.add( offlinetmp);
			}
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
			 int  offcount =0;	   // 离线次数
			 long offseconds = 0;  //离线时间秒
			 long onSeconds=0; //在线时长
			 int  recordNUM= 0 ;    //纪录数   
			 for (int i = 0; i < listofflinedetails.size(); i++) {
				 OffLineDetails  offdetail = listofflinedetails.get(i);
				//判定终端是否结束 初始化终端ID
				 if (i==0) {
					 terid_tmp = offdetail.ter_id; 
				} 
				 if (offdetail.ter_id.equals("闽A5C711-30")) {
					System.err.println("闽A5C711-30");
				}
				 //出现新的终端 且不是第一条记录
				 if (!offdetail.ter_id.equals(terid_tmp)& i>0) {
					 OffLineStatEntity offlinestat = new OffLineStatEntity();
					 offlinestat.plate_no = terid_tmp;
					 offlinestat.offcount = String.valueOf( offcount);		
					 offlinestat.cp_name =  listofflinedetails.get(i-1).cp_name;
					 //状态为1代表上线，说明前面一个时间段为离线时长	 
						if( listofflinedetails.get(i-1).on_off_flag.equals("1")){ 			
							//在线时长
							onSeconds=onSeconds+getlastofftimes(  listofflinedetails.get(i-1).create_time,querydays);
						}else {
							//最后一条离线时间
							offseconds=offseconds+getlastofftimes(listofflinedetails.get(i-1).create_time,querydays);						 
						}					 
					 //如果出现只有一条记录情况
					 if (offseconds==0 & onSeconds>0& querydays>0) {
						 offseconds=86400 -onSeconds;
					 }else if (offseconds>0 & onSeconds==0& querydays>0) {
						 onSeconds=86400 -offseconds;
					}
					 offlinestat.offSecends= String.valueOf(offseconds);
					 offlinestat.onSeconds =String.valueOf(onSeconds);  //当日在线时长
					 
					 listofflinestat.add(offlinestat);
					 
					 //重置统计参数
					   offcount =0;		    // 离线次数
					   offseconds = 0;  	//离线时间秒
					   onSeconds=0;    //在线时长
					   recordNUM= 0 ;		//纪录数  
					   terid_tmp =  offdetail.ter_id;
				} 
				if( offdetail.on_off_flag.equals("1")){  //状态为1代表上线，说明前面一个时间段为离线时长				
					offseconds=offseconds+offdetail.offduration;
					offcount++;
				}else {//在线时长 
					onSeconds=onSeconds+offdetail.offduration;
				} 
				recordNUM++;					
			 
	
				 //最后一条数据情况
				 if (i==listofflinedetails.size()-1 ) {
					 OffLineStatEntity offlinestat = new OffLineStatEntity();
					 offlinestat.plate_no = terid_tmp; 
					 offlinestat.offcount = String.valueOf( offcount);	
					 offlinestat.cp_name= offdetail.cp_name;
					 //状态为1代表上线，说明前面一个时间段为离线时长	 
						if( offdetail.on_off_flag.equals("1")){ 			
							//在线时长
							onSeconds=onSeconds+getlastofftimes(offdetail.create_time,querydays);
						}else {
							//最后一条离线时间
							offseconds=offseconds+getlastofftimes(offdetail.create_time,querydays);						 
						}
					offlinestat.offSecends= String.valueOf(offseconds);
					offlinestat.onSeconds = String.valueOf(onSeconds); ;  //当日在线时长	
					listofflinestat.add(offlinestat);
				}			
			}			 			 
			 return sortlist(listofflinestat);
		}
		/**
		 * 排序 
		 * */
	private List<OffLineStatEntity>  sortlist(List<OffLineStatEntity>  list){
		OffLineStatEntity[] offarray = list.toArray(new OffLineStatEntity[list.size()] );
		for (int i = 0; i < offarray.length; i++) {
			for (int j = 0; j < offarray.length; j++) {
				if (((OffLineStatEntity)offarray[i]).cp_name.compareTo(offarray[j].cp_name)>=0) {
					OffLineStatEntity tmp =  offarray[i];
					offarray[i]=offarray[j];
					offarray[j]=tmp;
				}
			}
		}
//		System.err.println("===="+Arrays.asList(offarray));
//		for (int i = 0; i < offarray.length; i++) {
////			System.err.println("===="+offarray[i].cp_name+offarray[i].plate_no);		
//		}
		return  Arrays.asList(offarray );
		
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
		if (queryday==0) {
			return System.currentTimeMillis()/1000-curtime ;
		}
		else {			
			Date d=new Date();
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd"); 
			String d2 = df.format(new Date(d.getTime() - queryday * 24 * 60 * 60 * 1000));
			try {
				Date date = df.parse(d2); //得到凌晨的时间 
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
	
	/**
	 * 
	 * */
	public static void main(String[] args) {
		OffLineDAO off = new OffLineDAO();  
		List<OffLineStatEntity> listofflinestat = off.getOffLineStat(1);
//		System.err.println(listofflinestat.size());
		for (int i = 0; i < listofflinestat.size(); i++) { 
			System.err.println("终端:"+listofflinestat.get(i).cp_name+listofflinestat.get(i).plate_no  +
					" 的离线时长为"+getTimes(Long.valueOf(listofflinestat.get(i).offSecends))+ 
			"在线时长"+getTimes(Long.valueOf(listofflinestat.get(i).onSeconds)) + "     离线次数" +	listofflinestat.get(i).offcount 
			);
		} 
 
		 
	}
}


