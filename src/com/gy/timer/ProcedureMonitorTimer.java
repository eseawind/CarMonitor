package com.gy.timer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimerTask;

import com.gy.CarMonitor.DBHelper;
import com.gy.Entity.ProcedureEntity;
import com.gy.Entity.TerminalInfoEntity;

public class ProcedureMonitorTimer  extends GyBusiJob{
	
	HashMap<String, ProcedureEntity> lastwarnedMap = new  HashMap<String, ProcedureEntity>();
	HashMap<String, ProcedureEntity> curwarnedMap = new  HashMap<String, ProcedureEntity>();
	HashMap<String, ProcedureEntity> warned4NoticeHashmap = new  HashMap<String, ProcedureEntity>();
	HashMap<String, ProcedureEntity> warnedclearHashmap = new  HashMap<String, ProcedureEntity>();

	/**
	 * 获取告警列表交集 ，比对两个map的key,返回第一个map里面的值value 
	 * */
	private HashMap<String, ProcedureEntity> getIntersectionMap(HashMap<String, ProcedureEntity> list1,HashMap<String, ProcedureEntity> list2){
		HashMap<String, ProcedureEntity> listintersection = new  HashMap<String, ProcedureEntity>();
//		listintersection.putAll(list1);	
        Iterator itr = list2.entrySet().iterator();
        while (itr.hasNext()) { 
            Map.Entry entry = (Map.Entry) itr.next();
            if (list1.get(entry.getKey())!=null ) { 
            	listintersection.put((String)entry.getKey()  ,  list1.get(entry.getKey()));            	
    			}
			}  
		return listintersection;
	} 
	/**
	 * 获取告警列表差集   返回list1的结果集
	 * */
	private HashMap<String, ProcedureEntity> getDifferenceMap(HashMap<String, ProcedureEntity> list1,HashMap<String, ProcedureEntity> list2){
		HashMap<String, ProcedureEntity> listintersection = new  HashMap<String, ProcedureEntity>();
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
	@Override
	public void run() {
		// TODO Auto-generated method stub
		curwarnedMap = getProceTaskMap();
		warned4NoticeHashmap= getDifferenceMap(curwarnedMap , lastwarnedMap);
		warnedclearHashmap= getDifferenceMap(lastwarnedMap, curwarnedMap);
		lastwarnedMap = curwarnedMap;
		System.err.println("待通知记录:"+warned4NoticeHashmap
				+"解除:"+warnedclearHashmap);		
		if (warned4NoticeHashmap.size()+ warnedclearHashmap.size()>0 ) {			
			String content=this.noticeWarnContent(warned4NoticeHashmap , warnedclearHashmap);
			insert4DB(content,"1","101",(int)(System.currentTimeMillis()/1000));
		}
	}
	
 
	private HashMap<String, ProcedureEntity> getProceTaskMap(){
		HashMap<String, ProcedureEntity> procMap = new HashMap<String, ProcedureEntity>();
		String sql = 
			"select job," +
			"lower(what) as procname ," +
			"LAST_DATE ," +
			"next_date ," +
			"interval ," +
			"this_date," +
			"failures ," +
			"broken," +
			"t.describtion as log " +
			" from user_jobs a " +
			"left join   ( " +
			"select  starttime,finishtime, describtion,taskname," +
			"row_number()over(partition by taskname order by id desc )as rowno  " +
			"from tbl_s_safe_task_log b where b.isfinished <>'success' "+ 
			"  and starttime>sysdate-2 "+
			")t "+
			"on  t.rowno = 1 and t.taskname||';' = a.WHAT " + 
			"where failures <>0 or broken='Y' OR NEXT_DATE =to_date('4000-01-01','yyyy-mm-dd') or t.describtion is not null "; 
		System.err.println(sql);
		Connection conn;
		Statement stat = null;
		ResultSet rs = null;
		conn = new DBHelper().getConn(); 
		try {
			stat = conn.createStatement();
			rs = stat.executeQuery(sql); 
			while (rs.next()) {
				ProcedureEntity proJob = new ProcedureEntity();
				proJob.setJobid(  rs.getString("job")); 
				proJob.setProName(  rs.getString("procname"));
				proJob.setLastdate(  rs.getString("LAST_DATE"));
				proJob.setNextdate(  rs.getString("next_date"));
				proJob.setInterval( rs.getString("interval") );
				proJob.setThisdate( rs.getString("this_date") );	
				proJob.setLastLog(   rs.getString("log") );
				procMap.put( proJob.getProName(), proJob);
			}
		} catch (SQLException e) { 
			e.printStackTrace();
		} catch (java.lang.NumberFormatException e ) {
			e.printStackTrace();
		}
		return procMap;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ProcedureMonitorTimer t = new ProcedureMonitorTimer();
//		System.err.println( t.getProceTaskMap());
		t.run();
		t.run();
	}

}
