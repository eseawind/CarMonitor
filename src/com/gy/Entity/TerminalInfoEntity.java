package com.gy.Entity;

public class TerminalInfoEntity  extends GyMonitorEntity{
	
	String ter_id;
	String plate_no,cp_name,gps_lon,gps_lat,gps_lon0200,gps_lat0200;
	String speed,height,direct,lasttime,lasttime0200,line_dis,oilmass,oilvalue;
	String ter_status, ter_warnstatus,front_dis,temperature,ter_status0200,ter_warnstatus0200;
	public String getTer_id() {
		return ter_id;
	}
	public void setTer_id(String terId) {
		ter_id = terId;
	}
	public String getPlate_no() {
		return plate_no;
	}
	public void setPlate_no(String plateNo) {
		plate_no = plateNo;
	}
	public String getCp_name() {
		return cp_name;
	}
	public void setCp_name(String cpName) {
		cp_name = cpName;
	}
	public String getGps_lon() {
		return gps_lon;
	}
	public void setGps_lon(String gpsLon) {
		gps_lon = gpsLon;
	}
	public String getGps_lat() {
		return gps_lat;
	}
	public void setGps_lat(String gpsLat) {
		gps_lat = gpsLat;
	}
	public String getGps_lon0200() {
		return gps_lon0200;
	}
	public void setGps_lon0200(String gpsLon0200) {
		gps_lon0200 = gpsLon0200;
	}
	public String getGps_lat0200() {
		return gps_lat0200;
	}
	public void setGps_lat0200(String gpsLat0200) {
		gps_lat0200 = gpsLat0200;
	}
	public String getSpeed() {
		return speed;
	}
	public void setSpeed(String speed) {
		this.speed = speed;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getDirect() {
		return direct;
	}
	public void setDirect(String direct) {
		this.direct = direct;
	}
	public String getLasttime() {
		return lasttime;
	}
	public void setLasttime(String lasttime) {
		this.lasttime = lasttime;
	}
	public String getLasttime0200() {
		return lasttime0200;
	}
	public void setLasttime0200(String lasttime0200) {
		this.lasttime0200 = lasttime0200;
	}
	public String getLine_dis() {
		return line_dis;
	}
	public void setLine_dis(String lineDis) {
		line_dis = lineDis;
	}
	public String getOilmass() {
		return oilmass;
	}
	public void setOilmass(String oilmass) {
		this.oilmass = oilmass;
	}
	public String getOilvalue() {
		return oilvalue;
	}
	public void setOilvalue(String oilvalue) {
		this.oilvalue = oilvalue;
	}
	public String getTer_status() {
		return ter_status;
	}
	public void setTer_status(String terStatus) {
		ter_status = terStatus;
	}
	public String getTer_warnstatus() {
		return ter_warnstatus;
	}
	public void setTer_warnstatus(String terWarnstatus) {
		ter_warnstatus = terWarnstatus;
	}
	public String getFront_dis() {
		return front_dis;
	}
	public void setFront_dis(String frontDis) {
		front_dis = frontDis;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getTer_status0200() {
		return ter_status0200;
	}
	public void setTer_status0200(String terStatus0200) {
		ter_status0200 = terStatus0200;
	}
	public String getTer_warnstatus0200() {
		return ter_warnstatus0200;
	}
	public void setTer_warnstatus0200(String terWarnstatus0200) {
		ter_warnstatus0200 = terWarnstatus0200;
	}
	@Override
	public String toString() { 
		return this.cp_name+'-'+this.plate_no+'-'+this.ter_id+ " 最后时间:"+getLasttime();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TerminalInfoEntity a=new TerminalInfoEntity();
		Integer.valueOf("ax");
		a.cp_name="asdf";
		a.ter_id="1";
		a.plate_no="闽A11111";
		System.err.println(a);
		//获取 报警终端列表
		String sql=" select ter.plate_no, "
			+"        ra.ter_id, "
			+"        gtime(lasttime) as lasttime, "
			+"        ra.ter_status, "
			+"        ra.gps_speed*3.6 as gps_speed, "
			+"		  round(ra.gps_lon,4) as gps_lon, "
			+"        round(ra.gps_lat,4) as gps_lat, "
			+"		  cp.cp_name "
			+"   from sa.tbl_s_safe_real ra "
			+"  inner join sa.tbl_s_terminal ter "
			+"     on ra.ter_id = ter.id "
			+" inner join sa.tbl_company cp on ter.cp_id = cp.id"
			+"  where 1 = 1   and  ter_id in ("+  "1" +")  "
			+"    and gintime(sysdate) * 1000 - ra.lasttime > "+"1" 
			+"    and ra.gps_speed >= "+"1"
			+"  order by ter.cp_id,lasttime asc  ";
		System.err.println(sql);
		
	} 
}
