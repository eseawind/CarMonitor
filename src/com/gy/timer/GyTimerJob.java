package com.gy.timer;

public class GyTimerJob {
	String jobName;
	long lasttime, interVal;
	int jobId;
	GyBusiJob busijob;

	public GyTimerJob() {
	}

	public GyTimerJob(String jobName2) {
		lasttime = System.currentTimeMillis();
	}

	public GyTimerJob(String jobName2, int interVal2, int jobId2) {
		lasttime = System.currentTimeMillis();
		this.setJobName(jobName2);
		this.setInterVal(interVal2);
		this.setJobId(jobId2);
		try {
			System.err.println("classname " + jobName2);
			Class ownerClass = Class.forName(jobName2);
			try {
				busijob = (GyBusiJob) ownerClass.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void flashExecTime(long lasttime) {
		this.lasttime = lasttime;
	}

	public void flashExecTime() {
		System.err.println("更新job时间" + this);
		this.lasttime = System.currentTimeMillis();
	}

	public void run() {
		System.err.println(this + "上次执行时间" + this.lasttime + "  ,本次条件"
				+ System.currentTimeMillis() + " 频率" + this.interVal);
		if (lasttime + this.interVal < System.currentTimeMillis()) { 
			System.err.println("busijob--" + busijob);
			this.busijob.run();
			flashExecTime();
		}
	}

	/**
	 * @param args
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */

	public static void main(String[] args) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		// GyTimerJob job = new
		// GyTimerJob("com.gy.timer.TerOnlineMonitorTimer",1,1);
		Class ownerClass = Class.forName("com.gy.timer.TerOnlineMonitorTimer");
		// Class ownerClass = Class.forName("com.gy.timer.Stack");
		System.err.println(ownerClass.newInstance().getClass());
	}

	public int getJobId() {
		return jobId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public long getLasttime() {
		return lasttime;
	}

	public void setLasttime(long lasttime) {
		this.lasttime = lasttime;
	}

	public long getInterVal() {
		return interVal;
	}

	public void setInterVal(long interVal) {
		this.interVal = interVal;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.jobId + "--" + jobName + "--" + this.interVal;
	}

}
