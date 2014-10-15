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
	//�ն���������ϸ��
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
					System.err.println("�쳣��¼"+rs.getString("ter_id"));		
				}
//				System.err.println(rs.getString("ter_id")+"--"+rs.getString("rownumber"));
				listofflinedetails.add( offlinetmp);
			}
			System.err.println(listofflinedetails);
		}
		catch(SQLException ex){
			ex.printStackTrace();
		}
		//�ر����ݿ�
		closeDB(); 
		//����ͳ�ƺ�����߽��
		return  getOffLineStat(listofflinedetails,querydays);
	}
	
	private void closeDB(){
		//�ر����ݿ�
		try {
			System.err.println("�ر����ݿ�����");
			if (rs !=null) {
				System.err.println("�ر�rs");
				rs.close();
			}
			if (stat!=null) {
				System.err.println("�ر�stat");
				stat.close();
			}
			if (conn!=null) {
				System.err.println("�ر�conn");
				conn.close();
			} 		
		} catch (SQLException e) { 
			e.printStackTrace();
		}
	}
	//���ش���������ͳ�ƽ��
	public   List<OffLineStatEntity> getOffLineStat(List<OffLineDetails>  listofflinedetails,int querydays){
		 List<OffLineStatEntity> listofflinestat = new ArrayList<OffLineStatEntity>();
		 String terid_tmp ="initid";	//�ն�ID 
		 int offcounts =0;		// ���ߴ���
		 int offseconds = 0;  //����ʱ����
		 int recordNUM= 0 ; //��¼�� 
		 int offlineduration =0  ; //ʱ��γ���
		 OffLineDetails offlinedetailsTMP=null;
		 System.err.println(listofflinedetails.size());
		 for (int i = 0; i < listofflinedetails.size(); i++) {
			 System.err.println(i);
			 OffLineDetails offdetail = listofflinedetails.get(i);	 
			//��ʼ��---------------------------------------------------
//			if ( !offdetail.ter_id.equals("initid")& !offdetail.ter_id.equals(terid_tmp)& offdetail.rownumber==1) {				
//				if (querydays>0) { //��ѯǰ����ļ�¼ʱ��Ҫ��23:59:59�ֿ��� ����ǵ����¼��ֻ��ӵ�ǰʱ������ 
//					if (offcounts>1) { 
//					}
//					offlinedetailsTMP=listofflinedetails.get(i-1);
//					if ( offlinedetailsTMP.on_off_flag.equals("0")) {
//						offlineduration=offlineduration+ (86400-offlinedetailsTMP.offtime);						
//					}
//				}else{ //ͳ�Ƶ������ 					
//				}
//				System.err.println("�ն�:"+terid_tmp + " ������ʱ��Ϊ"+offseconds/3600+"ʱ"+ (offseconds%3600)/60+"��"+ offseconds%60+"��");
//				terid_tmp = offdetail.ter_id;
//				offcounts=0;
//				offseconds=0;
//				recordNUM=1;
//				offlineduration=0; 
//			} 
			//end ��ʼ��------------------------------------------------
			
			System.err.println("��:"+offdetail.ter_id + "ʱ����"+ (offdetail.offtime-offlineduration) + "��ǰ״̬:"+ offdetail.on_off_flag );
			if( offdetail.on_off_flag.equals("1")){  //״̬Ϊ1�������ߣ�˵��ǰ��һ��ʱ���Ϊ����ʱ��
				offseconds =offseconds+( offdetail.offtime-offlineduration);
				System.err.println("����ʱ��:" + offseconds);
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


