<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="/resources/css/reset.css" />
<link href="../../resources/css/header.css" rel="stylesheet"
	type="text/css">
<link href="../../resources/css/account/changePwd.css" rel="stylesheet"
	type="text/css">
<meta charset="UTF-8">
<script src="https://kit.fontawesome.com/4602e82315.js"
	crossorigin="anonymous"></script>
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
</head>
<header>
	<nav>
		<a href="/">Primavera</a>
	</nav>
	<nav>
		<a href="#">Store</a> <a href="#">Guide</a> <a href="collectPage">PickUp</a>
		<a href="board/list">Board</a> <a href="myPage"><i
			class="fa-regular fa-user"></i></a>
	</nav>
</header>
<body>
	<main>
		<h1>이메일 인증</h1>
		<form action="/checkEmail" method="post">
			<div id="input">
				<p>아이디</p>
				<input size="15" type="text" name="id" value="${id}">
				<p>이메일</p>
				<input type="text" name="email" id="email" placeholder="이메일을 입력해주세요.">
				<div>
					<input type="button" value="인증하기" class="button" id="emailAuth">
				</div>
				<input class="form-control" placeholder="인증 코드 6자리를 입력해주세요" maxlength="6" disabled="disabled" name="authCode" id="authCode" type="text" autofocus/>
				<span id="emailAuthWarn"></span>
				<button type="submit" id="check" style="display: none;">비밀번호 재설정</button>
			</div>
		</form>
	</main>
	<script type="text/javascript">
		$("#emailAuth").click(function(){
			const email = $("#email").val();
			
			$.ajax({
				url: './EmailAuth',
				data : {
				email : email
			},
			type : 'POST',
			dataType: 'json',
			success: function(result){
				$("#authCode").attr("disabled", false);
				code=result;
				alert("인증 코드가 입력한 이메일로 전송 되었습니다.");
			}
			});
		});
		
		$('#authCode').keyup(function(){
			const inputCode = $('#authCode').val();
			
			if(Number(inputCode) === code){
				$('#emailAuthWarn').html('인증번호가 일치합니다.');
				$("#emailAuthWarn").css('color', 'green');
	    		$('#emailAuth').attr('disabled', true);
	    		$('#email').attr('readonly', true);
	    		$("#check").css("display", "");
			} else{
				$("#emailAuthWarn").html('인증번호가 불일치 합니다. 다시 확인해주세요!');
	        	$("#emailAuthWarn").css('color', 'red');
			}
		});
		
		
	</script>
</body>
</html>