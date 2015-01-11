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
	private static int warncount =3;  //������������ ������ֵ�򴥷��澯
//	List<TerminalInfoEntity>[ ] arraywarnlist = new  List[10];
	LinkedList<HashMap<String, TerminalInfoEntity>> warnLinkedlists = new LinkedList<HashMap<String, TerminalInfoEntity>>();   
	HashMap<String, TerminalInfoEntity> warnedHashmap = new  HashMap<String, TerminalInfoEntity>();
	HashMap<String, TerminalInfoEntity> warned4NoticeHashmap = new  HashMap<String, TerminalInfoEntity>();
	HashMap<String, TerminalInfoEntity> warnedclearHashmap = new  HashMap<String, TerminalInfoEntity>();
	public TerOnlineMonitorTimer(ServletContext servletContext) {
		this.servletContext = servletContext;
	} 
	/**
	 * ��ȡ�������б� 
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
	 * ˢ�±������У� ���뵽�����Ƚ����
	 * */
	private void flashWarnMap(HashMap<String, TerminalInfoEntity> warnmap){
		//�Ƚ����
		warnLinkedlists.addFirst(warnmap);
		//����5����� ������չ��Ҫ
		if (warnLinkedlists.size()>5) {
			warnLinkedlists.removeLast();  
		}
	}
	/**
	 * ��ȡ�澯�б��� ���ȶ�����map��key,���ص�һ��map�����ֵvalue 
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
	 * ��ȡ�澯�б�   ����list1�Ľ����
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
	 * ��ȡ�澯�б� ˢ�´�֪ͨ�嵥
	 * */
	HashMap<String, TerminalInfoEntity> getWarnedMap(int warncount){
		HashMap<String, TerminalInfoEntity> newwarnedmaptmp = new  HashMap<String, TerminalInfoEntity>();
		newwarnedmaptmp= warnLinkedlists.getFirst();
		//ֻ�и澯�б�����ֵwarncount��Ż��жϸ澯��
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
		//���ϴθ澯��¼�ȶ�
		if (warnLinkedlists.size()>=warncount+1) {
			
			warned4NoticeHashmap = getDifferenceMap(newwarnedmaptmp ,warnedHashmap);
			System.out.println("֪ͨ--------------------------"+warned4NoticeHashmap.size() );
		}
		return newwarnedmaptmp; 
	}
	
	HashMap<String, TerminalInfoEntity> getWarnedclearMap(int warncount){		
		HashMap<String, TerminalInfoEntity> warnedclearmaptmp = new  HashMap<String, TerminalInfoEntity>();
		//ֻ�и澯�б�����ֵwarncount��Ż��жϸ澯��
		if (warnLinkedlists.size()>=warncount & this.warnedHashmap.size()>0) {
			
			//���¸澯����б�  
			warnedclearmaptmp=getDifferenceMap(this.warnedHashmap , this.warnLinkedlists.getFirst());
			//���¸澯�б� 
			this.warnedHashmap=getDifferenceMap(this.warnedHashmap , warnedclearmaptmp);
		}else{			
			if (this.warnedHashmap.size()==0) {				
				System.err.println("warnedHashmap�澯�б�������:"+warnedHashmap.size());
			}else{
				System.err.println("warnLinkedlists�澯�б�:"+warnLinkedlists.size() +"warncount"+warncount);
			}
		} 		
		return warnedclearmaptmp ; 
	}	
	@Override
	public void run() { 
		System.err.println("TerOnlineMonitorTimer is running "+ System.currentTimeMillis());
		//1.��ȡ�б�
		HashMap<String, TerminalInfoEntity> warnmap = getTerOfflineMap();
		//2.���µ��澯�б�
		flashWarnMap(warnmap);
		//4.��ȡ�澯�����¼
		warnedclearHashmap=getWarnedclearMap(warncount);
		//3.��ȡ�澯��¼ 		
		warnedHashmap=getWarnedMap(warncount);   
		System.err.println(warned4NoticeHashmap);
		System.out.println("�澯���Ϊ"+warnedclearHashmap.size()+"�����澯��¼��"+warnedclearHashmap.size()  
		+ " �澯֪ͨ�б�"+this.warned4NoticeHashmap.size()
		+ " �ն������б�"+this.warnedHashmap.size()
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
