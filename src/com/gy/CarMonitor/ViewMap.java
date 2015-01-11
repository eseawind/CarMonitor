package com.gy.CarMonitor;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ViewMap extends HttpServlet {

	public ViewMap() { 
	}

	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException { 
		 
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter(); 	
		String lona = request.getParameter("lona"); 
		String lata = request.getParameter("lata"); 
		String lonb = request.getParameter("lonb"); 
		String latb = request.getParameter("latb"); 
//		out.append("<title>地图查看</title>");  
		String strhtml1 ="<!DOCTYPE html>\n" +
		"<html>\n" +
		"<head>\n" +
		"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
		"<meta name=\"viewport\" content=\"initial-scale=1.0, user-scalable=no\" />\n" +
		"<style type=\"text/css\">\n" +
		"body, html,#allmap {width: 100%;height: 100%;overflow: hidden;margin:0;font-family:\"微软雅黑\";}\n" +
		"</style>\n" +
		"<script type=\"text/javascript\" src=\"http://api.map.baidu.com/api?v=2.0&ak=o22VpeTv4dQlCHlsLYpIImmo\"></script>\n" +
		"<script type=\"text/javascript\" src=\"http://developer.baidu.com/map/jsdemo/demo/convertor.js\"></script>\n" +
		"<title>地图查看</title>\n" +
		"</head>\n" +
		"<body>\n" +
		"<div id=\"allmap\"></div>\n" +
		"</body>\n" +
		"</html>\n" ;
		String strgpspoint2="";
		if (lonb==null || lonb.equals("")) {
			strgpspoint2="";
		}else {
			strgpspoint2 =" BMap.Convertor.translate(ggPoint2,0,translateCallbackB); \n" ;			
		} 
		 
		out.append(strhtml1+
				"<script type=\"text/javascript\">\n" +
//				" var x ="+ lona +";\n" +
//				" var y ="+ lata +";\n" +
//				" var ggPoint =  new BMap.Point(x,y);\n" +
				" var ggPoint = new BMap.Point("+lona + ","+lata +");\n" +
				" var ggPoint2 = new BMap.Point("+lonb + ","+latb +");\n" +
				" var bm = new BMap.Map(\"allmap\");\n" +
				" bm.enableScrollWheelZoom(); \n " +   //滚动条
				" bm.addControl(new BMap.MapTypeControl());" +  //地图类型
				" bm.addControl(new BMap.ScaleControl());                    "+// 添加比例尺控件
				" bm.centerAndZoom(ggPoint, 15);\n" +
				" bm.addControl(new BMap.NavigationControl());\n" +
				" translateCallbackA = function (point){ \n" +
				" var marker = new BMap.Marker(point);\n" +
				" bm.addOverlay(marker);\n" +
				" var label = new BMap.Label(\"起点\",{offset:new BMap.Size(20,-10)});\n" +
				" marker.setLabel(label); \n" +
				"	}\n" +
				" translateCallbackB = function (point){ \n" +
				" var marker = new BMap.Marker(point);\n" +
				" bm.addOverlay(marker);\n" +
				" var label = new BMap.Label(\"终点\",{offset:new BMap.Size(20,-10)});\n" +
				" marker.setLabel(label); \n" +
				" bm.setCenter(point);\n" +
				" }\n" +
				" BMap.Convertor.translate(ggPoint,0,translateCallbackA);\n" +
				strgpspoint2+
				" </script>\n");  
		
		out.flush();
		out.close();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
