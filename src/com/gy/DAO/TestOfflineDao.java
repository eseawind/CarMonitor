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
	//�ն���������ϸ��
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
					System.err.println("�쳣��¼"+rs.getString("ter_id"));	
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
		//�ر����ݿ�
		closeDB(); 
		//����ͳ�ƺ�����߽��
		return  getOffLineStat(listofflinedetails,querydays);
//		return null;
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
			 OffLineDetails  offdetail = listofflinedetails.get(i);
			 OffLineStatEntity offlinestat = new OffLineStatEntity();
			//�ж��ն��Ƿ���� ��ʼ���ն�ID
			 if (i==0) {
				 terid_tmp = offdetail.ter_id;
			}
			 //�����µ��ն�
			 if (!offdetail.ter_id.equals(terid_tmp)) {
				 offlinestat.plate_no = terid_tmp;
				 offlinestat.offduration= String.valueOf(offseconds);
				 offlinestat.offtimes = "1";
				 offlinestat.onlineduration = "1";  //��������ʱ��
				 listofflinestat.add(offlinestat);
				 //����ͳ�Ʋ���
				   offcounts =0;		// ���ߴ���
				   offseconds = 0;  	//����ʱ����
				   recordNUM= 0 ;		//��¼�� 
				   offlineduration =0  ; //ʱ��γ���
				   terid_tmp =  offdetail.ter_id;
			}
//			System.err.println("��:"+offdetail.ter_id + "ʱ����"+ (offdetail.offtime-offlineduration) + "��ǰ״̬:"+ offdetail.on_off_flag );
			if( offdetail.on_off_flag.equals("1")){  //״̬Ϊ1�������ߣ�˵��ǰ��һ��ʱ���Ϊ����ʱ��
//				offseconds =offseconds+( offdetail.offtime-offlineduration);
				System.err.println(terid_tmp+ "����ʱ��:" + offseconds);
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
			System.err.println("�ն�:"+listofflinestat.get(i).plate_no  +
					" ������ʱ��Ϊ"+offseconds/3600+"ʱ"+ (offseconds%3600)/60+"��"+ offseconds%60+"��");
		}
	}
}


