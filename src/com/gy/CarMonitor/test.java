package com.gy.CarMonitor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class test {
public static void main(String[] args) {

//	System.err.println(System.currentTimeMillis());
//	Calendar now =Calendar.getInstance();  
//	now.set(Calendar.DATE , now.get(Calendar.DATE)-1);
//	System.err.println(now.getTimeInMillis());
//    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");    
//    Date nowTime=new Date();    
//    String todayStr = format.format(nowTime);    
//    Date today;
//	try {
//		today = format.parse(todayStr);
//		long starttime=today.getTime();  
//		System.err.println(starttime);
//	} catch (ParseException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}    
	
	
//	Calendar calendar1 = Calendar.getInstance();
//	calendar1.add(Calendar.DAY_OF_YEAR, -1);
//	System.err.println(calendar1.getTimeInMillis());
//	long ii =calendar1.getTimeInMillis();
//	System.err.println(ii/86400);
	
//	Calendar calendar=Calendar.getInstance();   
//	   calendar.setTime(new Date()); 
//	   System.out.println(calendar.get(Calendar.ZONE_OFFSET));//��������� 
	
//    Date d=new Date();   
//    SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");   
//    System.out.println("��������ڣ�"+df.format(d)); 
//     
//    System.out.println("����ǰ�����ڣ�" + df.format(new Date(d.getTime() - 2 * 24 * 60 * 60 * 1000)));  
//    System.out.println("���������ڣ�" + df.format(new Date(d.getTime() + 3 * 24 * 60 * 60 * 1000)));	

	
//	Date d=new Date();
//	SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd"); 
//	String d2 = df.format(new Date(d.getTime() - 2 * 24 * 60 * 60 * 1000)); 
//	try {
//		Date date = df.parse(d2); //�õ��賿��ʱ��
//		System.err.println(date.getTime());
//	} catch (ParseException e) { 
//		e.printStackTrace();
//	}
//	
//	String a = "35";
//	String b ="36";
//	System.err.println(a.compareTo(b));
	
//	List<String> list = new ArrayList<String>() ;
//	list.add("����a");
//	list.add("����c2");
//	list.add("����b");
//	list.add("����c");
//	list.add("����a2");
//	list.add("����b2");
////	Object[] a =  list.toArray(); 
//	String[] a = (String[])list.toArray(new String[list.size()]);
//	for (int i = 0; i < a.length; i++) {
//		for (int j = 0; j < a.length; j++) {
//			if (((String)a[i]).compareTo((String)a[j])<=0) {
//				String tmp =(String) a[i];
//				a[i]=a[j];
//				a[j]=tmp;
//			}
//		}
//	}
//	System.err.println(Arrays.asList(a));
//	for (int j = 0; j < a.length; j++) {		
//			String tmp =(String) a[j];
//			System.err.println(tmp);
//	}	
//	String tmp =list.get(0).substring(1,2);
//	List<String> list2 = new ArrayList<String>() ;
////	list2.add(list.get(0));
////	list.remove(0);
//	for (int i = 0; i < list.size(); i++) {
//		if (list.get(i).contains(tmp)) {
//			list2.add(list.get(i) );
////			list.remove(i);
//		}
//	}
//	System.err.println(list2);
//	System.err.println(list);
//	list2.addAll(list);
//	System.err.println(list2);
	
//	System.err.println(		"<table width=\"140%\" border=\"1\" cellspacing=\"1\" cellpadding=\"1\">"+
//						"<tr align=\"center\"  class=\"t1\">"+
//						"<td height=\"25\" align=\"left\" bgcolor=\"#D5E4F4\"><strong>������˾-����-�ն�ID</strong></td>"+ 
//				 		"<td bgcolor=\"#D5E4F1\"><strong>��ȫ���ݻ㱨ʱ��</strong></td>" +
//				 		"<td bgcolor=\"#D5E4F4\"><strong> ������� </strong></td>" +
//				 		"<td bgcolor=\"#D5E4F4\"><strong> ���ٶ� </strong></td>" +
//				 		"<td bgcolor=\"#D5E4F4\"><strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;GPS&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong></td>" +
//				 		"<td bgcolor=\"#D5E4F4\"><strong>&nbsp;GPS�ٶ�</strong></td>" +
//				 		"<td bgcolor=\"#D5E4F4\"><strong>�ٶ�gps</strong></td>" +
//				 		"<td bgcolor=\"#D5E4F4\"><strong>ģ������</strong></td>" +				 		
//				 		"<td bgcolor=\"#D5E4F4\"><strong> &nbsp;ʵ������(��) </strong></td>" +
//				 		"<td bgcolor=\"#D5E4F4\"><strong> &nbsp;�¶�&nbsp; </strong></td>" +
//				 		"<td bgcolor=\"#D5E4F4\"><strong> &nbsp;�ն�״̬ </strong></td>" +
//				 		"<td bgcolor=\"#D5E4F4\"><strong> &nbsp;����״̬ </strong></td>" +
//				 		"<td bgcolor=\"#D5E4F4\"><strong> &nbsp;���� </strong></td>" +
//				 		"<td bgcolor=\"#FF0000\"><strong> ����λ��ʱ��0200 </strong></td>" +
//				 		"<td bgcolor=\"#FF0000\"><strong> �����ϱ�λ��0200 </strong></td>" +
//				 		"<td bgcolor=\"#FF0000\"><strong> �ն�״̬0200 </strong></td>" +
//				 		"<td bgcolor=\"#FF0000\"><strong> ����״̬0200 </strong></td>" +
//				 		"</tr>"    
////				 		 
//				 );
	
	System.err.println("1400`000".contains("4000"));
}
}

