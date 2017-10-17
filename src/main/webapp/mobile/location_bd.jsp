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
</head>
<body>
内容变了
	<script type="text/javascript">
	function display_alert(content){
		$('body').append(JSON.stringify(content));
	}
	function Location(){};
	Location.prototype.getLocation = function(callback){
	  var options = {
	    enableHighAccuracy: true,
	    maximumAge: 1000
	  };
	  this.callback = callback;
	  this.onSuccess = function(position){
	      var longitude = position.coords.longitude; //经度
	      var latitude = position.coords.latitude; //纬度
	      self.loadMapApi(longitude,latitude);
	    };  
	  var self = this;
	  if (navigator.geolocation) {
	     navigator.geolocation.getCurrentPosition(self.onSuccess, self.onError, options); //浏览器支持geolocation
	  } else {
	    display_alert("不支持获取定位信息！");//浏览器不支持geolocation
	  }
	};
	var loadBMapScript = function(callback){
		 var oHead = document.getElementsByTagName('HEAD').item(0);
		  var oScript= document.createElement("script");
		  oScript.type = "text/javascript";
		  oScript.src="https://api.map.baidu.com/getscript?v=2.0&ak=8016NMbxXjjssqd2GPGgCAeHoRMTCC11&services=&t=20140930184510";
		  oHead.appendChild(oScript);
		  oScript.onload = callback;
	}
	Location.prototype.loadMapApi = function(longitude, latitude){
		  var self = this;
		  loadBMapScript(function(date){
		    var originalPoint = new BMap.Point(longitude, latitude);
		    translate(originalPoint,translateCallback);
		  });
	};
	Location.prototype.onError = function(error) {
	  switch (error.code) {
	    case 1:
	    	display_alert("位置服务被拒绝");
	      break;
	    case 2:
	    	display_alert("暂时获取不到位置信息");
	      break;
	    case 3:
	    	display_alert("获取信息超时");
	      break;
	    case 4:
	    	display_alert("未知错误");
	      break;
	  }
	};

    //坐标转换完之后的回调函数
   var translateCallback = function (data){
      if(data.status === 0) {
    	  var gc = new BMap.Geocoder(); 
	    	var point = data.points[0];//获取实际的点
	  	    var map = new BMap.Map("container"); // 创建地图实例
	  	 // map.addControl(new BMap.GeolocationControl());
		  	    map.centerAndZoom(point, 17);   // 初始化地图，设置中心点坐标和地图级别
		        gc.getLocation(point, function(rs) {//获取具体位置
	        	  var addComp = rs.addressComponents;
	        	  var marker = new BMap.Marker(point);
		  	      map.addOverlay(marker);
		  	      var label = new BMap.Label(JSON.stringify(addComp),{offset:new BMap.Size(20,-10)});
			      marker.setLabel(label); //添加百度label
		  	      
		  	 });
	      
      }else{
    	  display_alert('坐标转换失败！');
      }
    }
	function translate(point,callback){
		 var convertor = new BMap.Convertor();
	        var pointArr = [];
	        pointArr.push(point);
	        convertor.translate(pointArr, 1, 5,callback);
	}
	//调用
	var local = new Location();
	local.getLocation(function(res){
	  display_alert(res);
	})
	</script>
	<div id="container" style="width:100%;height:250px;;border:1px solid ;"></div>
</body>
</html>