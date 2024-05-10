<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">

<!-- jQuery library -->
<script
	src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.slim.min.js"></script>

<!-- Popper JS -->
<script
	src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>

<!-- Latest compiled JavaScript -->
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
<title>${training.trainingTitle}</title>
<style>
.board_wrap {
	margin: 30px;
}
/*기본 구조*/
.board_view {
	width: 100%;
	border-top: 2px solid #000;
	font-size: 1.4rem;
}

.board_view .title {
	padding: 20px 15px;
	border-bottom: 1px solid #ddd;
	font-size: 2rem;
}

.board_view .info {
	padding: 15px;
	border-bottom: 1px solid #999;
	font-size: 0;
}

.board_view .cont {
	padding: 15px;
	border-bottom: 2px solid #000;
	line-height: 160%;
	font-size: 1.4rem;
}

/*info 설정*/
.board_view .info dl {
	display: inline-block;
	padding: 0 20px;
	position: relative;
}

.board_view .info dl:first-child {
	padding-left: 0;
}

.board_view .info dl::before {
	content: "";
	position: absolute;
	top: 3px;
	left: 0;
	display: block;
	width: 1px;
	height: 13px;
	background-color: #ddd;
}

.board_view .info dl:first-child::before {
	display: none;
}

.board_view .info dl dd, .board_view .info dl dt {
	display: inline-block;
	font-size: 1.4rem;
}

.board_view .info dl dd {
	margin-left: 10px;
	color: #777;
}

.cont {
	word-break: break-all;
	height: 500px;
}

#uploadImg {
	float: right;
}
/* 댓글 기능 */
.reply-area {
	border: 1px solid lightslategray;
	border-radius: 10px;
	width: 100%;
}

.reply-write {
	margin: 10px;
}

.reply-write>textarea {
	border: none;
	width: 100%;
	margin-top: 10px;
}

.reply-btn {
	display: flex;
	margin: 10px;
}

.right {
	justify-content: right;
}
</style>
</head>
<body>
	<%@include file="/views/common/menubar.jsp"%>
	<div class="board_wrap" id="board_wrap">
		<div class="board_title">
			<h1>러닝일지</h1>
			<p>러닝일지 페이지입니다.</p>
		</div>
		<div class="board_view_wrap">
			<div class="board_view">
				<div class="title">${training.trainingTitle}</div>
				<div class="info">
					<%-- <input type="hidden" value="${loginUser.memberName }" id="memberName"> --%>
					<dl>
						<dt>번호</dt>
						<dd>${training.trainingNo}</dd>
					</dl>
					<dl>
						<dt>글쓴이</dt>
						<dd>${training.trainingWriter}</dd>
					</dl>
					<dl>
						<dt>작성일</dt>
						<dd>${training.recordDate}</dd>
					</dl>
					<dl>
						<dt>조회</dt>
						<dd>${training.count}</dd>
					</dl>
				</div>
				<table class="table">
					<tr>
						<th>훈련종류</th>
						<td>${training.trainingKey}</td>
						<th>착용신발</th>
						<td>
							<!-- shoes 조회 기능 구현되면 가져올 것 -->
						</td>
					</tr>
					<tr>
						<th>훈련장소</th>
						<td>${training.trainingPlace}</td>
						<th>운동거리</th>
						<td>${training.trainingDistance}km</td>
					</tr>
					<tr>
						<th>운동시간</th>
						<td>${training.trainingTime }분</td>
						<th>평균페이스</th>
						<td>${training.trainingTime div training.trainingDistance}
							분/km</td>
					</tr>
					<tr>
						<th>목표</th>
						<td>${training.trainingGoal}</td>
						<th>현재 체중</th>
						<td>${training.weight }kg</td>
					</tr>
				</table>
				<hr>
				<div class="cont">
					${training.trainingContent} <img alt="업로드이미지"
						src="${contextPath}${attachment.filePath}${attachment.changeName}"
						id="uploadImg">

				</div>
			</div>
			<br>
			<div class="bt_wrap">
				<a href="${contextPath}/list.tr?currentPage=1"
					class="btn btn-outline-secondary">목록</a> <a
					href="${contextPath}/update.tr?tno=${training.trainingNo}"
					class="btn btn-info">수정</a>
				<button class="btn btn-secondary" id="deleteTr">삭제</button>
			</div>
			<script>
				$("#deleteTr")
						.click(
								function() {
									if (confirm("정말 삭제하시겠습니까?")) {
										location.href = "${contextPath}/delete.tr?tno=${training.trainingNo}";
									}
								});
			</script>
			<br>
			<div class="reply">
				<h4>댓글</h4>
				<div class="reply-list" id="reply-list">
					<!-- 댓글 하나 -->
					<hr>
					<dl>
						<dt>러닝맨</dt>
						<dd>러닝 정말 너무 재밌어요!</dd>
						<dd>2024.04.17</dd>
					</dl>
					<!-- 댓글 하나 -->
					<hr>
					<dl>
						<dt>태양네</dt>
						<dd>온 가족 함께 공원에서 뛰었습니다. 가족 러닝 강추!</dd>
						<dd>2024.04.17</dd>
					</dl>
				</div>
			</div>
			<div class="reply-area">
				<c:if test="${empty loginUser}">
					<div class="reply-write">
						<textarea name="" id="" cols="200" rows="1" style="resize: none;"
							placeholder="로그인 후 댓글입력이 가능합니다 :&#41;"></textarea>
					</div>
				</c:if>
				<div class="reply-write">
					<b id="memberName">${loginUser.memberName }</b>
					<textarea name="reply" id="reply" cols="200" rows="1"
						style="resize: none;" placeholder="댓글을 남겨보세요 :&#41;"></textarea>
				</div>
				<div class="reply-btn right">
					<button class="btn btn-outline-info" id="reply-btn">등록</button>
				</div>
			</div>

		</div>
	</div>
	<script>
		$(function() {
			$("#reply-btn").click(function() {
				var memberName = $("#memberName").val();
				let reply = $("#reply").val();
				
 				$.ajax({
 					type: "post",
					url : "reply.tr",
					data : {
						memberNo : ${loginUser.memberNo},
 						tno : ${training.trainingNo},
						reply : reply
					},
					success : function(result) {
						$("#reply").val("");
						replyList();
					},
					error : function() {
						alert("댓글작성 실패ㅠㅠ 다시 시도해보세요");
					}
				});
			});
		});
		
		function replyList() {
			$.ajax({
				url : "reply.tr",
				data : {
						tno : ${training.trainingNo}
				},
				success : function(list){
					var dl = "";
					for(var i in list){
						dl += "<hr><dl>"
							+ "<dt>"+list[i].replyWriter+"</dt>"
							+ "<dd>"+list[i].replyContent+"</dd>"
							+ "<dd>"+list[i].createDate+"</dd>"
							+ "</dl>";
					}
					$("#reply-list").html(dl);						
				},
				error : function(){
				alert("통신오류,댓글목록 조회 실패... 다시 시도해주세요");	
				}
				});
		}
		$(function() {
			replyList();
			let open = "${training.oCStatus}"=='C';
			let writer = "${loginUser.memberName}"!="${training.trainingWriter}";
			if(open&&writer){
 				alert("작성자만 볼 수 있는 일지입니다.");
 				history.back();
			}
		});
	</script>
</body>
</html>