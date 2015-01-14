package com.gy.timer;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.gy.CarMonitor.DBHelper;
import com.gy.Entity.GyMonitorEntity;
import com.gy.Entity.TerminalInfoEntity;

public class GyBusiJob<T,F,G>  { 
	public void getTest(HashMap<T,G> list){
		System.err.println(list);		
	}
	
	
	void insert4DB (String contenct,String warntype,String warnsubtype,int crtime){
		String sql ="";
		sql= "insert into tbl_m_warn_main(id,warntype,warnsubtype,status,content,crtime,info_flag) values "+
		"( seq_m_warn_main.nextval ,"+
		warntype + ","+ warnsubtype +",0 ,"+ "'"+contenct +"',"+
		crtime+
		",0)";
		System.err.println("sql"+sql);
		Connection conn;
		Statement stat = null;		
		conn = new DBHelper().getConn(); 		
		try {
			stat = conn.createStatement();
			stat.executeUpdate(sql); 
		} catch (SQLException e) { 
			e.printStackTrace();
		}
		try {
			stat.close();
			conn.close();
		} catch (SQLException e) { 
			e.printStackTrace(); 
		}finally{
			try {
				stat.close();
				conn.close();
			} catch (SQLException e) { 
				e.printStackTrace();
			}
		}		
	}
	public void run(){
		
	}
	
	/**
	 * 获取告警列表交集 ，比对两个map的key,返回第一个map里面的值value 
	 **/ 
	@SuppressWarnings("unchecked")
	private HashMap<T, F> getIntersectionMap(HashMap<T, F> list1,HashMap<T, F> list2){
		HashMap<T, F> listintersection = new  HashMap<T, F>();
//		listintersection.putAll(list1);	 
        Iterator<?> itr = list2.entrySet().iterator();
        while (itr.hasNext()) { 
            Map.Entry entry = (Map.Entry) itr.next();
            if (list1.get(entry.getKey())!=null ) { 
            	listintersection.put((T)entry.getKey()  ,  list1.get(entry.getKey()));            	
    			}
			}  
		return listintersection;
	} 
	
	/**
	 * 生成通知内容
	 * */
	@SuppressWarnings("unchecked")
	String noticeWarnContent(HashMap<String,    GyMonitorEntity> warnedmap ,HashMap<String, GyMonitorEntity> warncleanMap){
		if (warnedmap.size()+warncleanMap.size()==0 ) {
			return "";
		}
		String content="离线告警:;";
        Iterator itr = warnedmap.entrySet().iterator();
        while (itr.hasNext()) { 
            Map.Entry entry = (Map.Entry) itr.next(); 
            GyMonitorEntity terinfo =  (GyMonitorEntity) entry.getValue();
            content =content+terinfo+";"; 
			}
        Iterator itr2 = warncleanMap.entrySet().iterator();
        content=content+"告警解除:;";
        while (itr2.hasNext()) { 
            Map.Entry entry = (Map.Entry) itr2.next(); 
            GyMonitorEntity terinfo =  (GyMonitorEntity) entry.getValue();
            content =content+terinfo+";"; 
			}        
        return content;
	}
	public static void main(String[] args) {
		GyBusiJob<String,String,String> t = new GyBusiJob<String,String,String>();
		HashMap<String, String> map = new HashMap<String ,String>(); 
		t.getTest(map );
	}
}
