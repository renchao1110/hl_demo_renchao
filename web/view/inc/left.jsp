<%@page session="false" contentType="text/html;charset=UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
<html>
<head>
<%@ include file="/include/leftlib.jsp"%>
<title></title>
<script type="text/javascript">
$(document).ready(function() {
	$('#leftMenu').metisMenu({ toggle: true });
});
function tolink(url,mid){
	$("ul li a").each(function(){
		$(this).removeClass("selbal");
	})
	$("#"+mid).addClass("selbal");
	top.mainFrame.location.href=url;
}
$(document).ready(function() {
	<c:if test="${ffMenu != null}">
		$("#${ffMenu}").trigger("click");
	</c:if>
	$("#outBtn").on("click",function(){
		if(confirm('确定要退出系统吗？')){
    		top.location.href="/login/login!logout.dhtml";
    	}
	})
})

</script>
</head>
<body style="background: #333;">
<aside class="sidebar">
<nav class="sidebar-nav">
			<ul class=metismenu id="leftMenu">
			<li class="sidebar-title" style="padding:10px 15px">
				HL CENTRAL<span style="float: right;font-size: 14px">
				<button class="btn btn-danger btn-xs" id="outBtn" >退出系统</button>
				</span>
			</li>
				<c:forEach items="${fMenu }" var="list" varStatus="index">
					<c:if test="${index.index == 0 }">
						<li class="active">
					</c:if>
					<c:if test="${index.index > 0 }">
						<li>
					</c:if>
					<a href="#" class="sidebar-nav-ul-a-menu" >${list.menuNameZh }<span class="fa arrow"></span></a>
					<ul >
						<c:forEach items="${list.indexChildSysMenus }" var="clist">
							<li ><a href="#" class="sidebar-nav-ul-a" id="${clist.menuId }" onclick="tolink('${clist.menuUrl}','${clist.menuId }')">${clist.menuNameZh }</a></li>
						</c:forEach>
					</ul>
				</li>
				</c:forEach>
			</ul>
		</nav>
</aside>
</body>
</html>

