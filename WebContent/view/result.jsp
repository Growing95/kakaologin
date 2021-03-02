<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
</style>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script> 
<script type="text/javascript">
	$(function() {
		$("#out").empty();
		$.ajax({
			url : "/MyMember",
			method : "post",
			dataType : "json",
			success : function(data) {
				$.each(data,function(){
					var response = this['profile'];
					$.each(response,function(){
						nickname = response['nickname'];
						email2 = response['profile_image_url'];
					});
						email = this['email'];
				});
				$("#out").append(nickname+"("+email2+")님 환영합니다.")
			},
			error : function() {
				alert("읽기실패");
			}
		});
	});
</script>
<script type="text/javascript">
</script>
</head>
<body>
	<h1> 결과 보기</h1>
	<div id="out"></div>
</body>
</html>