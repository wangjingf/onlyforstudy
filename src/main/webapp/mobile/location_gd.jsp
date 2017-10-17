<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" id="viewport" content="width=device-width, initial-scale=1"/>
<title>Insert title here</title>
<script type="text/javascript" src="<%=request.getContextPath()%>/jquery-ui/external/jquery/jquery.js"></script>
 <script type="text/javascript" src="<%=request.getContextPath()%>/resource/js/json2.js"></script>
     <script type="text/javascript" src="http://webapi.amap.com/maps?v=1.4.0&key=1a9ec7e8621021105ac963be22955b8a"></script>  
</head>
<body>
	<script type="text/javascript">
	  ;
	    AMap.event.addDomListener(document.getElementById('calc'), 'click', function() {
	        alert('两点间距离为：' + lnglat.distance([116.387271, 49.922501]) + '米');
	    });
	function countDistance(position){
		var lnglat = new AMap.LngLat(116.35088166358595, 40.0453771072088);//公司
		var result = lnglat.distance(position);
		return result;
	}
	$(function(){
		
		console.log(countDistance());	
	})
	
	</script>
  <div id="container" style="width:100%;height:250px;">
  	 
  </div>
  <div id="result"></div>
  <input type="file" value="上传"/>
</body>
</html>