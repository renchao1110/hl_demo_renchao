<%@page session="false" contentType="text/html;charset=UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
<html>
<head>
<%@ include file="/include/metalib.jsp"%>
<%@ include file="/include/metatable.jsp"%>
<!-- 导入js和CSS-->
<link rel="stylesheet" href="/js/extend/tree/jquery.treeview.css" />

<script src="/js/extend/tree/jquery.treeview.js" type="text/javascript"></script>
<title></title>
<style type="text/css">
</style>
</head>


<script type="text/javascript">
	$(document).ready(function() {
		/* 树形结构 */
		$("#tree").treeview({
			prerendered : false,
			persist : "location",
			collapsed : false,
			unique : false
		});

		/*  鼠标点击 改变字体颜色 */
		$("#tree span").click(function() {
			$("#tree span").css("color", "black");
			$(this).css("color", "#5CACEE");

		})

	});
</script>
<body style="width:100%;height:100%;">
	<ul id="tree" class="filetree"
		style="font-size: 14px;cursor:pointer;width: 100%;height: 100%; ">
		<li><span class="folder fontcolor" 
			onclick=" window.parent.selectInfo2('${id}','${departmentName}');">${departmentName}</span>
			<ul class="filetree">
				<c:forEach var="dept" items="${data}" varStatus="st">
					<li><span class="folder fontcolor"
						onclick="window.parent.selectInfo2('${dept.id}','${dept.departmentNameZh }');">${dept.departmentNameZh}</span>
						<c:if test="${dept.list!=null}">
							<ul>
								<c:forEach var="dept2" items="${dept.list}">
									<li><span class="folder fontcolor"
										onclick="window.parent.selectInfo2('${dept2.id}','${dept2.departmentNameZh }');">${dept2.departmentNameZh}</span>
										<c:if test="${dept2.list!=null}">
											<ul>
												<c:forEach var="dept3" items="${dept2.list}">
													<li><span class="folder fontcolor"
														onclick="window.parent.selectInfo2('${dept3.id}','${dept3.departmentNameZh }');">${dept3.departmentNameZh}</span>
														<c:if test="${dept3.list!=null}">
															<ul>
																<c:forEach var="dept4" items="${dept3.list}">
																	<li><span class="folder fontcolor"
																		onclick="window.parent.selectInfo2('${dept4.id}','${dept4.departmentNameZh }');">${dept4.departmentNameZh}</span>
																		<c:if test="${dept4.list!=null}">
																			<ul>
																				<c:forEach var="dept5" items="${dept4.list}">
																					<li><span class="folder fontcolor"
																						onclick="window.parent.selectInfo2('${dept5.id}','${dept5.departmentNameZh }');">${dept5.departmentNameZh}</span>
																				</c:forEach>
																			</ul>
																		</c:if></li>
																</c:forEach>
															</ul>
														</c:if></li>
												</c:forEach>
											</ul>
										</c:if>
									</li>
								</c:forEach>
							</ul>
						</c:if></li>
				</c:forEach>
			</ul>
		</li>
	</ul>

</body>
</html>

