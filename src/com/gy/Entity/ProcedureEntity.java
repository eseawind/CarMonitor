package com.gy.Entity;

public class ProcedureEntity extends GyMonitorEntity{
	String proName,status,lastdate,jobid,duration,interval,nextdate,lastResult,lastLog;
	String thisdate;
	String sql ="select job,what as procname ,LAST_DATE ,next_date ,interval ,this_date,failures ,broken  from user_jobs a ";
	public String getProName() {
		return proName;
	}
	public void setProName(String proName) {
		this.proName = proName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLastdate() {
		return lastdate;
	}
	public void setLastdate(String lastdate) {
		this.lastdate = lastdate;
	}
	public String getJobid() {
		return jobid;
	}
	public void setJobid(String jobid) {
		this.jobid = jobid;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getInterval() {
		return interval;
	}
	public void setInterval(String interval) {
		this.interval = interval;
	}
	public String getNextdate() {
		return nextdate;
	}
	public void setNextdate(String nextdate) {
		this.nextdate = nextdate;
	}
	public String getLastResult() {
		return lastResult;
	}
	public void setLastResult(String lastResult) {
		this.lastResult = lastResult;
	}
	public String getLastLog() {
		return lastLog;
	}
	public void setLastLog(String lastLog) {
		this.lastLog = lastLog;
	}
	public String getThisdate() {
		return thisdate;
	}
	public void setThisdate(String thisdate) {
		this.thisdate = thisdate;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	@Override
	public String toString() { 
		return this.proName +'-'+this.lastdate+'-'+this.lastLog ;
	}
	
	 
	
}
