package com.gy.listener;

public class OffTerminalInfo {
	public String plate_no,ter_id,lasttime,gps_speed ,gps_lon,gps_lat;
	public String cp_name;
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return  this.plate_no+" / "+this.ter_id+" / "+this.lasttime;
	}	
	 
}
