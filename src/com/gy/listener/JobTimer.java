package com.gy.listener;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;

import javax.servlet.ServletContext;

import com.gy.CarMonitor.CarMonitor;
import com.gy.CarMonitor.DBHelper;
import com.gy.mail.Constant;
import com.gy.mail.SendMail;

public class JobTimer extends TimerTask {
	private ServletContext servletContext;
	private static boolean isRunning = false;
	public static int sendcount = 0;

	public JobTimer(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	private static List<String> getMailAddress() {
		List<String> listaddress = new ArrayList<String>();
		String sql = " select value from gy_monitor_mail where sub_type='to' and type=0 and switch = 1 ";
		Connection conn;
		Statement stat = null;
		ResultSet rs = null;
		conn = new DBHelper().getConn();
		try {
			stat = conn.createStatement();
			rs = stat.executeQuery(sql);
			while (rs.next()) {
				listaddress.add(rs.getString("value"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 关闭数据库
		try {
			System.err.println("关闭数据库连接");
			if (rs != null) {
				System.err.println("关闭rs");
				rs.close();
			}
			if (stat != null) {
				System.err.println("关闭stat");
				stat.close();
			}
			if (conn != null) {
				System.err.println("关闭conn");
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listaddress;
	}

	@Override
	public void run() {
		if (!isRunning) {
			isRunning = true;
			servletContext.log("稳定性报表任务开始---->" + System.currentTimeMillis());
			Connection conn;
			String sql = "{call pro_abnormalmonitor(?)}"; // 执行存储过程
			conn = new DBHelper().getConn();
			int procresult = 1;
			try {
				CallableStatement cs = conn.prepareCall(sql);
				cs.registerOutParameter(1, Types.NUMERIC);
				cs.execute();
				procresult = cs.getInt(1); // 获取执行结果0:成功 1失败
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (procresult == 0) {
				System.err.println("存储过程执行成功，准备发送邮件：");
				String contents = getMailContent();
				for (String mailaddr : getMailAddress()) {
					SimpleDateFormat formatter = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm");
					String mDateTime = formatter.format(new Date());
					new SendMail().send(mailaddr, "行车仪运营情况统计报表" + mDateTime,
							contents);
				}
			}
			isRunning = false;
			servletContext.log("本次任务结束" + "   ---->"
					+ System.currentTimeMillis());
		} else {
			servletContext.log("上次任务还在执行");
		}
		sendcount++; // 服务启动的时候不执行
	}

	private static String getMailContent() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String mDateTime = formatter.format(new Date());
		String content = "<h2 align=\"center\">--行车仪运营情况统计报表"
				+ mDateTime
				+ "--</h2 ><table width=\"100%\" border=\"1\" cellspacing=\"1\" cellpadding=\"1\"><tr align=\"center\"  class=\"t1\"><td height=\"25\" bgcolor=\"#D5E4F4\"><strong>终端ID/车牌</strong></td><td bgcolor=\"#D5E4F4\"><strong>公司</strong></td><td bgcolor=\"#D5E4F4\"><strong>离线次数</strong></td><td bgcolor=\"#D5E4F4\"><strong>离线时长</strong></td><td bgcolor=\"#D5E4F4\"><strong>上线时长</strong></td><td bgcolor=\"#D5E4F4\"><strong>离线时间占比%</strong></td><td bgcolor=\"#D5E4F4\"><strong>当日里程(米)</strong></td><td bgcolor=\"#D5E4F4\"><strong>异常记录明细</strong></td><td bgcolor=\"#D5E4F4\"><strong>停车记录明细</strong></td></tr>";
		CarMonitor carmon = new CarMonitor();
		content = content + carmon.getQueryResult(0) + "</table>";
		System.err.println(content);
		return content;
	}

	public static void main(String[] args) {
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
//		String mDateTime = formatter.format(new Date());
//		System.err.println(mDateTime);
		String strsql = "select o from EntSafeInfoMonth o "
			+ " where (o.line_speeda is null) and ter_id=" + "1"
			+ " and o.gps_lat>0.01" + " and o.gps_lon>0.01"
			+ " order by o.lasttime asc";
		System.err.println(strsql );
	}
}
