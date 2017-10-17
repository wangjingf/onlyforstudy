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
	
	
	function display_alert(content){
		$('body').append(JSON.stringify(content));
	}
	function GeoLocation(){};
	GeoLocation.prototype = {
		init : function(callback){
			  var options = {
			    enableHighAccuracy: true,
			    maximumAge: 1000
			  };
			  var self = this;
			  this.callback = callback;
			  this.onSuccess = function(position){
			      var longitude = position.coords.longitude; //经度
			      var latitude = position.coords.latitude; //纬度
			      self.loadMapApi([longitude,latitude]);
			    };  
			
			 if (navigator.geolocation) {
			     navigator.geolocation.getCurrentPosition(self.onSuccess, self.onError, options); //浏览器支持geolocation
			  } else {
			    display_alert("不支持获取定位信息！");//浏览器不支持geolocation
			  }
		},	
		
		loadMapApi : function(lnglatXY){//gps坐标
			var self = this;
			AMap.convertFrom(lnglatXY, "gps",
					function(status,result){
				    var transformedLnglat=result.locations[0];
					var map = new AMap.Map("container", {
				        resizeEnable: true,
						zoom: 16,
						center:transformedLnglat
				    });   
					self.regeocoder(map,transformedLnglat);
				 display_alert("loadMapApi1");
			});
			 
			/* var onLocateComplete=function(data){
					var str=['定位成功'];
			        str.push('经度：' + data.position.getLng());
			        str.push('纬度：' + data.position.getLat());
			        if(data.accuracy){
			             str.push('精度：' + data.accuracy + ' 米');
			        }//如为IP精确定位结果则没有精度信息
			        str.push('是否经过偏移：' + (data.isConverted ? '是' : '否'));
			        //document.getElementById('tip').innerHTML = str.join('<br>');
				};
				var onLocateError=function(data){
					display_alert(data.message);
				}
			   map.plugin('AMap.Geolocation', function() {
			        geolocation = new AMap.Geolocation({
			            enableHighAccuracy: true,//是否使用高精度定位，默认:true
			            timeout: 10000,          //超过10秒后停止定位，默认：无穷大
			            buttonOffset: new AMap.Pixel(10, 20),//定位按钮与设置的停靠位置的偏移量，默认：Pixel(10, 20)
			            zoomToAccuracy: true,      //定位成功后调整地图视野范围使定位位置及精度范围视野内可见，默认：false
			            buttonPosition:'RB'
			        });
			        map.addControl(geolocation);
			        geolocation.getCurrentPosition();
			        AMap.event.addListener(geolocation, 'complete', onLocateComplete);//返回定位信息
			        AMap.event.addListener(geolocation, 'error', onLocateError);      //返回定位出错信息
			        
			    });*/
			   
			 
		},
		 regeocoder:function(map,lnglatXY,callback) {  //逆地理编码
			 map.plugin(['AMap.Geocoder'],function(){
				 var geocoder = new AMap.Geocoder({
			            radius: 1000,
			            extensions: "all"
			        });      
				 display_alert(lnglatXY);
			        geocoder.getAddress(lnglatXY, function(status, result) {
			        	display_alert("status:"+status);
			        	display_alert("result"+result.info);
			            if (status === 'complete' && result.info === 'OK') {
			            	var address = result.regeocode.formattedAddress;
			            	 var marker = new AMap.Marker({  //加点
			                     map: map,
			                     position: lnglatXY,
			    	             });
			    	     	  marker.setTitle(address);
			    	     	 display_alert(address);
			    	     	    // 设置label标签
			    	     	    marker.setLabel({//label默认蓝框白底左上角显示，样式className为：amap-marker-label
			    	     	        offset: new AMap.Pixel(0, 20),//修改label相对于maker的位置
			    	     	        content: address
			    	     	    });
			    	         	document.getElementById("result").innerHTML = address;
			    	             map.setFitView();	
			            	callback(result);
			            }
			        });
			});
	                
	    }
		
	 };
	$(function(){
		
		var loca =  new GeoLocation();
		loca.init();	
	})
	
	</script>
  <div id="container" style="width:100%;height:250px;">
  	 
  </div>
  <div id="result"></div>
  <input type="file" value="上传"/>
</body>
</html>