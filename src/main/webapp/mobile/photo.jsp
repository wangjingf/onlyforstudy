<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
    <style>
        html,body{

            width:100%;
            height:100%;

        }

        #video{
            width:100%;
            height:100%;
        }
    </style>
</head>
<body><!-- <input type="file" accept="image/*;capture=camera"> -->aaa
<input type="file" capture="camera" accept="image/*"/>
<!-- <video id="video" autoplay=""></video>
<div><button id='picture' style="width:20%;height:20%">PICTURE</button></div>
<canvas id="canvas" width="640" height="480"></canvas>
<script type="text/javascript">
    var video = document.getElementById("video");
    var context = canvas.getContext("2d");
    var errocb = function () {
        console.log('sth wrong!');
    }

    if (navigator.getUserMedia) { // 标准的API
        navigator.getUserMedia({ "video": true }, function (stream) {
            video.src = stream;
            video.play();
        }, errocb);
    } else if (navigator.webkitGetUserMedia) { // WebKit 核心的API
        navigator.webkitGetUserMedia({ "video": true }, function (stream) {
            video.src = window.webkitURL.createObjectURL(stream);
            video.play();
        }, errocb);
    }

    document.getElementById("picture").addEventListener("click", function () {
        context.drawImage(video, 0, 0, 640, 480);
    });
</script> -->

</body>
</html>