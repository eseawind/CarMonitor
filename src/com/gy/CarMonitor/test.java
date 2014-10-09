package com.gy.CarMonitor;

public class test {
public static void main(String[] args) {
	int i =1;
	String sql="select to_char(MONITOR_date,'mm-dd:hh24')as MONITOR_date , t1.TER_ID,t2.CP_name as CP_ID, PLATE_NO, OFFCOUNT, nvl(t1.milse,0)as milse ,"+
	"trunc(OFFSECONDS/3600)||'时'|| trunc( mod(OFFSECONDS,3600)/60) ||'分'|| mod(OFFSECONDS,60) ||'秒' as OFFSECONDS,"+
	"trunc((86400 - OFFSECONDS)/3600)||'时'|| trunc( mod((86400 - OFFSECONDS),3600)/60) ||'分'|| mod((86400 - OFFSECONDS),60) ||'秒'as onlineSeconds,"+
      "nvl(t3.oilmass, 0) as OILMASS,nvl(t3.line_speed, 0) as SPEED,round(nvl(t3.gps_lon_f, 0), 6) || ','||round(nvl(t3.gps_lat_f, 0), 6) as GPS,"+
      "mod(nvl(T3.ter_status, 0), 2) AS ACCSTATUS "+
 "from sa.qlj_car_monitor t1,sa.tbl_company t2 , sa.tbl_s_safe_real t3 where  t1.CP_ID =t2.id and trunc(MONITOR_date) = trunc(sysdate-" +
    i+") and t1.ter_id = t3.ter_id order by cp_id desc,OFFCOUNT desc,ter_id asc";
	
	System.err.println(sql);
}
}
