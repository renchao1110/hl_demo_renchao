<%@page session="false" contentType="text/html;charset=UTF-8"%>
<%
	String dates = request.getParameter("date");
%>
<%@ include file="/include/taglib.jsp"%>
<html>
<head>
<%@ include file="/include/metalib.jsp"%>
<%@ include file="/include/metatable.jsp"%>
<title>发送信息详情</title>
<style type="text/css">
/* #etable_info {
	display: none;
}
#etable_paginate{
	display: none;
} */
</style>
</head>
<script type="text/javascript">
	var otable;
	var oo;
	var nfy;
	$(document).ready(function() {
		nfy = new cusNotify();
		oo = new cusLoader();
		var uid = ${_id };
		window.parent.clearPhone();
		var columns = [
				{
					"sClass" : "text-right",
	                 "data": "id",
	                 "sWidth": "20%",
	                 "render" : function(data, type, full, meta) {
									return '<input type="checkbox" onclick="savePhoneId()" class="checkbox"  value="'+data+'"   />';
								}
				}, {
					"sClass" : "text-center",
					"sWidth": "30%",
					"data" : "phoneNum"
				}, {
					"sClass" : "text-center",
					"sWidth": "50%",
					"data" : "scontent"
				} ]
		initOtable('etable',"/contacts/info!queryPersonInfo.dhtml?_ids="+uid, columns,null, true, true);
		$("#checkAllId").click(function(){
			var isCheckAll = $("#isCheckAll").val();
			if(isCheckAll=="false"){
				$.ajax({
					url : "/contacts/info!queryAllPersonInfo.dhtml",
					type : "post",
					data:{"_ids":uid},
					dataType : "json",
					success : function(data) {
						if (data.count >= 0) {
							window.parent.checkAllPhone(data.count);
							$(":checkbox").prop("checked",true) ;
							$("#isCheckAll").val("true");
							$(":checkbox").attr("disabled","disabled");
							$("#etable_paginate").hide();
							$("#checkAllId").val("取消全选");
							$("#btnAll").hide();
						}
					},
					error : function() {
						nfy.show("操作异常", "err");
					}
				});
			}else{
				$("#isCheckAll").val("false");
				$(":checkbox").prop("checked",false) ;
				window.parent.clearPhone("0");
				$(":checkbox").attr("disabled",false);
				$("#etable_paginate").show();
				$("#checkAllId").val("全选");
				$("#btnAll").show();
			}
		});
	$("#btnAll").click(function(){
		window.parent.savePhone(extGetChecked("etable"));
	});
});
		function savePhoneId (){
			window.parent.savePhone(extGetChecked("etable"));
		}
</script>
<body style="overflow:auto;overflow:-Scroll;overflow-x:hidden;overflow-y:hidden">
	<table class="tmain">
				<div class="panel panel-default" style="width:99%;">
					<div style="font-size: 14px;">
						<table id="etable" class="table table-striped table-bordered" 
							width="100%" cellspacing="0">
							<thead>
								<tr>
									<th><input type="checkbox" class="checkall" id="btnAll" style="float: left"/>
										<input type="button" class="btn btn-primary btn-xs" id="checkAllId" value="全选" style="float:right"/>
										<input type="hidden" id="isCheckAll" value="false" >
									</th>
									<th class="otable-title">手机号</th>
									<th class="otable-title">备注</th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
	</table>
</body>
</html>

