<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head> 
    <base href="<%=basePath%>">
    
    <title>古易车辆监控</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<meta http-equiv="refresh" content="0;url=./QueryCars?querydays=1">
	<link rel="stylesheet" type="text/css" href="styles.css">
	<h1 align="center" ><a href="/CarMonitor/QueryCars">QueryCars</a></h1>
	-->
	<br>
	<br>
	<br>
	<h1 align="center"  ><a href="/CarMonitor/OracleMonitor" >
	<font size="18">1.数据库监控</font></a></h1>
	<h1 align="center" ><a href="/CarMonitor/RealMonitor">
	<font size="18">2.实时监控</font></a></h1>	
	<h1 align="center" ><a href="/CarMonitor/OffLineMonitor">
	<font size="18">3.离线统计</font></a></h1>	  
    
  </head>
  
  <body>

  </body>
</html>
