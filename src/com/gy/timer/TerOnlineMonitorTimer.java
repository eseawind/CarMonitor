package com.gy.timer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TimerTask;

import javax.servlet.ServletContext;

import com.gy.CarMonitor.DBHelper;
import com.gy.Entity.TerminalInfoEntity;
import com.gy.listener.OffTerminalInfo;

public class TerOnlineMonitorTimer extends TimerTask {
	private ServletContext servletContext;
	private static boolean isRunning = false;
	public static int sendcount = 0;
	private static int warncount =3;  //报警触发次数 超过该值则触发告警
//	List<TerminalInfoEntity>[ ] arraywarnlist = new  List[10];
	LinkedList<HashMap<String, TerminalInfoEntity>> warnLinkedlists = new LinkedList<HashMap<String, TerminalInfoEntity>>();   
	HashMap<String, TerminalInfoEntity> warnedHashmap = new  HashMap<String, TerminalInfoEntity>();
	HashMap<String, TerminalInfoEntity> warned4NoticeHashmap = new  HashMap<String, TerminalInfoEntity>();
	HashMap<String, TerminalInfoEntity> warnedclearHashmap = new  HashMap<String, TerminalInfoEntity>();
	public TerOnlineMonitorTimer(ServletContext servletContext) {
		this.servletContext = servletContext;
	} 
	/**
	 * 获取上下线列表 
	 * */
	private HashMap<String, TerminalInfoEntity> getTerOfflineMap(){
		HashMap<String, TerminalInfoEntity> warnmap = new HashMap<String, TerminalInfoEntity>();
		String sql ="select com.cp_name,"+
		"       ter.id as ter_id,"+
		"       ter.plate_no,"+
		"       lpos.pos_long,"+
		"       lpos.pos_lat,"+
		"       lpos.ter_status,"+
		"       lpos.pos_speed,"+
		"       gtime(lpos.pos_time) as lasttime "+
		"  from tbl_s_terminal ter  "+
		" inner join sa.tbl_s_terminal_last_pos lpos  "+
		"    on ter.id = lpos.ter_id  "+
		"   and ter.ter_status <> 1 "+
		" inner join sa.tbl_company com "+
		"    on ter.cp_id = com.id  "+
		" order by cp_name,plate_no  "; 
			
		Connection conn;
		Statement stat = null;
		ResultSet rs = null;
		conn = new DBHelper().getConn(); 
		try {
			stat = conn.createStatement();
			rs = stat.executeQuery(sql); 
			while (rs.next()) {
				TerminalInfoEntity terinfo = new TerminalInfoEntity();
				terinfo.setCp_name(rs.getString("cp_name"));
				terinfo.setPlate_no(rs.getString("plate_no"));
				terinfo.setTer_id(rs.getString("ter_id"));
				terinfo.setGps_lon(rs.getString("pos_long"));
				terinfo.setGps_lat(rs.getString("pos_lat"));
				terinfo.setTer_status(rs.getString("ter_status"));
				terinfo.setLasttime(rs.getString("lasttime"));
				warnmap.put(terinfo.getTer_id(), terinfo);
			}
		} catch (SQLException e) { 
			e.printStackTrace();
		} catch (java.lang.NumberFormatException e ) {
			e.printStackTrace();
		}
		return warnmap;
	}
	/**
	 * 刷新报警队列， 插入到队列先进后出
	 * */
	private void flashWarnMap(HashMap<String, TerminalInfoEntity> warnmap){
		//先进后出
		warnLinkedlists.addFirst(warnmap);
		//超过5个清空 用于扩展需要
		if (warnLinkedlists.size()>5) {
			warnLinkedlists.removeLast();  
		}
	}
	/**
	 * 获取告警列表交集 ，比对两个map的key,返回第一个map里面的值value 
	 * */
	private HashMap<String, TerminalInfoEntity> getIntersectionMap(HashMap<String, TerminalInfoEntity> list1,HashMap<String, TerminalInfoEntity> list2){
		HashMap<String, TerminalInfoEntity> listintersection = new  HashMap<String, TerminalInfoEntity>();
		listintersection.putAll(list1);	
        Iterator itr = list2.entrySet().iterator();
        while (itr.hasNext()) { 
            Map.Entry entry = (Map.Entry) itr.next();
            if (listintersection.get(entry.getKey())==null ) {
            	listintersection.remove(entry.getKey() ); 
    			}
			}  
		return listintersection;
	} 
	/**
	 * 获取告警列表差集   返回list1的结果集
	 * */
	private HashMap<String, TerminalInfoEntity> getDifferenceMap(HashMap<String, TerminalInfoEntity> list1,HashMap<String, TerminalInfoEntity> list2){
		HashMap<String, TerminalInfoEntity> listintersection = new  HashMap<String, TerminalInfoEntity>();
		listintersection.putAll(list1);	
        Iterator itr = list2.entrySet().iterator();
        while (itr.hasNext()) { 
            Map.Entry entry = (Map.Entry) itr.next();
            if (listintersection.get(entry.getKey())!=null ) {
            	listintersection.remove(entry.getKey() ); 
    			}
			}  
		return listintersection;
	} 
	/**
	 * 获取告警列表 刷新待通知清单
	 * */
	HashMap<String, TerminalInfoEntity> getWarnedMap(int warncount){
		HashMap<String, TerminalInfoEntity> newwarnedmaptmp = new  HashMap<String, TerminalInfoEntity>();
		newwarnedmaptmp= warnLinkedlists.getFirst();
		//只有告警列表超过阀值warncount后才会判断告警，
		if (warnLinkedlists.size()>=warncount) {
			int index = 0 ;
			for (HashMap<String, TerminalInfoEntity> linkedlist:warnLinkedlists){
				if (index <= warncount) {
					newwarnedmaptmp= getIntersectionMap(newwarnedmaptmp, linkedlist);
				}
				index++;
				System.err.println("newwarnedmaptmp:"+newwarnedmaptmp.size());
			}
		} 
		//跟上次告警记录比对
		if (warnLinkedlists.size()>=warncount+1) {
			
			warned4NoticeHashmap = getDifferenceMap(newwarnedmaptmp ,warnedHashmap);
			System.out.println("通知--------------------------"+warned4NoticeHashmap.size() );
		}
		return newwarnedmaptmp; 
	}
	
	HashMap<String, TerminalInfoEntity> getWarnedclearMap(int warncount){		
		HashMap<String, TerminalInfoEntity> warnedclearmaptmp = new  HashMap<String, TerminalInfoEntity>();
		//只有告警列表超过阀值warncount后才会判断告警，
		if (warnLinkedlists.size()>=warncount & this.warnedHashmap.size()>0) {
			
			//更新告警解除列表  
			warnedclearmaptmp=getDifferenceMap(this.warnedHashmap , this.warnLinkedlists.getFirst());
			//更新告警列表 
			this.warnedHashmap=getDifferenceMap(this.warnedHashmap , warnedclearmaptmp);
		}else{			
			if (this.warnedHashmap.size()==0) {				
				System.err.println("warnedHashmap告警列表无数据:"+warnedHashmap.size());
			}else{
				System.err.println("warnLinkedlists告警列表:"+warnLinkedlists.size() +"warncount"+warncount);
			}
		} 		
		return warnedclearmaptmp ; 
	}	
	@Override
	public void run() { 
		System.err.println("TerOnlineMonitorTimer is running "+ System.currentTimeMillis());
		//1.获取列表
		HashMap<String, TerminalInfoEntity> warnmap = getTerOfflineMap();
		//2.更新到告警列表
		flashWarnMap(warnmap);
		//4.获取告警解除记录
		warnedclearHashmap=getWarnedclearMap(warncount);
		//3.获取告警记录 		
		warnedHashmap=getWarnedMap(warncount);   
		System.err.println(warned4NoticeHashmap);
		System.out.println("告警解除为"+warnedclearHashmap.size()+"产生告警记录："+warnedclearHashmap.size()  
		+ " 告警通知列表"+this.warned4NoticeHashmap.size()
		+ " 终端下线列表"+this.warnedHashmap.size()
		);
		
	}
	public static void main(String[] args) {
		
		TerOnlineMonitorTimer ter = new TerOnlineMonitorTimer(null);
		ter.run();
		ter.run();
		ter.run();
		ter.run();
		ter.run();
//		ter.run();
	}
}
