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
 .mask {
    position: absolute; top: 0px; 
    left: 0px; width: 100%; height: 100%;
     z-index: 9999; background-color: rgba(255, 255, 255, 0.7); border-radius: 3px;
}
</style>
</head>


<script type="text/javascript">
	var otable;
	$(document).ready(
			function() {
				/* 树形结构 */
				$("#tree").treeview({
					prerendered : true,
					persist : "location",
					collapsed : false,
					unique : true
				});
				nn = new cusNotify();
				/* 修改分组名称验证 */
				$("#updateBtn").click(function(){
					var id =$("#parentId").val();
					if(id=="719"){
						$("#departmentName2").attr("readonly","readonly");
					}else{
						$("#departmentName2").removeAttr("readonly");
					}
				});
				/* 验证信息 修改分组名称 提交 按钮	 */
				$("#submitBtn2").click(function(){
					showMask();
					var id =$("#parentId").val();
					var departmentName=$("#departmentName2").val();
					var departmentName2=$("#departmentName3").val();
					if(id=="" || departmentName2==""){
						hideMask();
						$("#error_info2").text("未选中分组!!!");
						$("#error_alert2").show();
						return;
					}
					if(!checkval(departmentName)){
						hideMask();
						$("#error_info2").text("分组名称不能包含特殊字符!!!");
						$("#error_alert2").show();
						return;
					}
					if(id=="719"){
						hideMask();
						$("#error_info2").text("此分组不可修改!!!");
						$("#error_alert2").show();
						return;
					}
					if(departmentName2==departmentName){
						hideMask();
						$('#myModal2').modal('hide');
						return ;
					}
					$.ajax({
						url:"/contacts/info!updateDeptName.dhtml",
						type:"post",
						data:{"id":id,"departmentName":departmentName},
						dataType : "json",
						success : function(response) {
								hideMask();
								$('#myModal2').modal('hide');
								if(response.result==1){
									nn.show("修改成功", "提示");
									document.getElementById("treeView").src = "/contacts/info!companyFrame.dhtml";
		   							/* nn.show("权限不足", "err"); */
								}
						},
						error:function(){
								hideMask();
								$('#myModal2').modal('hide');
								nn.show("系统异常!!", "err");
						}
					
					});
					
				});
				
				
					/* 验证信息 添加分组 提交 按钮	 */
				$("#submitBtn").click(function(){
					
					var parentId =$("#parentId").val();
					var departmentName=$("#departmentName").val();
					var departmentLevel=$("#departmentLevel").val();
					if(parentId==""){
						hideMask();
						$("#error_info").text("未选中分组！");
						$("#error_alert").show();
						
						return;
					}
					if(!checkval(departmentName)){
						hideMask();
						$("#error_info").text("分组名称不能包含特殊字符！");
						$("#error_alert").show();
						return;
					}
					showMask();
					$.ajax({
						url:"/contacts/info!addDept.dhtml",
						type:"post",
						data:{"parentId":parentId,"departmentName":departmentName,"departmentLevel":departmentLevel},
						dataType : "json",
						success : function(response) {
								hideMask();
								$('#myModal').modal('hide');
								if(response.result==2){
		   							nn.show("权限不足", "err");
								}else if(response.result==1){
									nn.show("添加成功", "提示");
									document.getElementById("treeView").src = "/contacts/info!companyFrame.dhtml";
								}
						},
						error:function(){
								hideMask();
								$('#myModal').modal('hide');
								nn.show("系统异常！", "err");
						}
					
					});
					
				});
				/* 为分组添加手机号 */
				$("#upload").click(function(){
					var parentId =$("#parentId").val();
					var phoneNumber1 =$("#phoneNumber1").val();
					var efile = $("#efile").val();
					if(parentId=="" || parentId=="719"){
						hideMask();
						$("#error_info1").text("未选中分组！");
						$("#error_alert1").show();
						return;
					}
					if(phoneNumber1 =="" && (efile==null || efile=="")){
						hideMask();
						$("#error_info1").text("手机号不能为空");
						$("#error_alert1").show();
						return;
					}
					if(phoneNumber1 !="" && !(/^(1[3-9])\d{9}$/.test(phoneNumber1))){
						hideMask();
						$("#error_info1").text("手机号格式不正确");
						$("#error_alert1").show();
						return;
					}
					$("#efileName").val(efile);
					$("#parentIdss").val(parentId);
					var formData1 = new FormData($("#formm11")[0]);
					showMask();
					$.ajax({
						url:"/contacts/info!addPhoneNo.dhtml",
			   			data:formData1,
			   			type: "post",
			   			async:true,
			   			cache:false,
			   			dataType:'json',
			   			contentType:false,
			   			processData:false,
						success : function(response) {
								hideMask();
								$('#myModal1122').modal('hide');
								$("#departmentLevel").val("");
								$("#phoneNumber1").val("");
								$("#phoneScontent1").val("");
								if(response.result>=0){
									nn.show("添加成功", "提示");
									/* document.getElementById("treeView").src = "/contacts/info!companyFrame.dhtml"; */
									document.getElementById("treeView1").src = "/contacts/info!query.dhtml?id="+parentId;
								}else{
									nn.show(response.errStr, "err");
								}
						},
						error:function(){
								hideMask();
								$('#myModal1122').modal('hide');
								nn.show("系统异常！", "err");
						}
					
					});
					
				});
				selectInfo();
				document.getElementById("treeView").src = "/contacts/info!companyFrame.dhtml";
				document.getElementById("treeView1").src = "/contacts/info!query.dhtml?id=719";
				
				
/* 查询发送短信详细信息 */
	$('#editBtn').click(function(){
			document.getElementById("errSmsPage").src = "/contacts/info!query.dhtml";
	});	
	/* 删除分组中的手机号 */
	$("#sureBtn12").click(function(){
		var partId =$("#parentId").val();
		var itemlist = $("#delPhoneId").val();
		if(partId=="" || partId=="719"){
			nn.show("未选中分组!", "err");
			return ;
		}
		if(itemlist==""){
			nn.show("未选中手机号!", "err");
			return ;
		}
		showMask();
		$.ajax({
			url : "/contacts/info!delPhone.dhtml",
			type : "post",
			data:{"itemlist":itemlist,"partId":partId},
			dataType : "json",
			success : function(response) {
				hideMask();
				$("#myModal7").modal('hide');
				if (response.result >= 1) {
					nn.show("删除成功", "提示");
					document.getElementById("treeView1").src = "/contacts/info!query.dhtml?id="+partId;
				} else {
					nn.show("删除失败!", "err");
				}
			},
			error : function() {
				hideMask();
				nn.show("操作异常", "err");
			}
		});
	});
	/* 修改手机号备注信息 */
	$("#submitBtn221").click(function(){
		var partId =$("#parentId").val();
		var itemlist = $("#delPhoneId").val();
		var phoneScontent = $("#phoneScontent2").val();
		if(partId=="" || partId=="719"){
			nn.show("未选中分组!", "err");
			return ;
		}
		if(itemlist==""){
			nn.show("未选中手机号!", "err");
			return ;
		}
		showMask();
		$.ajax({
			url : "/contacts/info!updateRemark.dhtml",
			type : "post",
			data:{"itemlist":itemlist,"partId":partId,"phoneScontent":phoneScontent},
			dataType : "json",
			success : function(response) {
				hideMask();
				$("#myModal8").modal('hide');
				$("#phoneScontent2").val("");
				if (response.result >= 1) {
					nn.show("更新成功", "提示");
					document.getElementById("treeView1").src = "/contacts/info!query.dhtml?id="+partId;
				} else {
					nn.show("更新失败!", "err");
				}
			},
			error : function() {
				nn.show("操作异常", "err");
			}
		});
	});			
	/* 显示字数下标 */
	$("#scontentInfo").keyup(function() {
		var count = $(this).val().length;
		$("#font_count").text(count);
	});
	/* 点击发送短信 */
	$("#sureBtn19").click(function(){
		var itemlist = $("#delPhoneId").val();
		var scontentInfo = $("#scontentInfo").val();
		var partId =$("#parentId").val();
		var sendDate = $("#sendDate").val();
		if(itemlist==""){
			nn.show("未选中手机号!", "err");
			return ;
		}
		if(scontentInfo==""){
			nn.show("短信内容不能为空", "err");
			return ;
		}
		if(partId == ""){
			partId="719";
		}
		var ids;
		if(itemlist=="all"){
			ids = "all";
		}else if(itemlist !="" && itemlist != null){			
			ids = itemlist.replace(/itemlist=/g,"").replace(/&/g,",");
		}
		showMask();
		$.ajax({
			url : "/contacts/info!sendSmsInfo.dhtml",
			type : "post",
			data:{"ids":ids,"scontentInfo":scontentInfo,"sendDate":sendDate,"partId":partId},
			dataType : "json",
			success : function(response) {
				hideMask();
				$("#myModal9").modal('hide');
				$("#scontentInfo").val("");
				$("#sendDate").val("");
				if (response.result >= 1) {
					nn.show("提交成功", "提示");
					document.getElementById("treeView1").src = "/contacts/info!query.dhtml?id="+partId;
				}else if(response.result == -1){
					nn.show("获取手机号失败", "err");
				}else if(response.result == -2){
					nn.show("获取短信内容失败", "err");
				}else if(response.result == -3){
					nn.show("获取子账号信息失败", "err");
				}else if(response.result == -45){
					nn.show("获取分组id失败", "err");
				} else {
					nn.show("更新失败!", "err");
				}
			},
			error : function() {
				hideMask();
				nn.show("操作异常", "err");
			}
		});
	});
	/* 导出批量上传手机号样例 excel */
	$("#outExcel1").click(function(){
		var smsFrom = $('#radioDiv input[name="cFrom"]:checked').val();
		var path=$("#path1").val();
		location.href = "/common/download!downloadExcel.dhtml?_fname=为分组批量导入手机号模板&_dpath="+path;
	});
});
	/* 保存选中的手机号 */
	function savePhone(str){
		$("#delPhoneId").val(str);
		var sss = str.split("itemlist=");
		var num = sss.length-1;
		$("#phoneCount").text(num);
	}
	/* 选中全部手机号 */
	function checkAllPhone(str){
		$("#delPhoneId").val("all");
		$("#phoneCount").text(str);
	}
	/* 清除选中的手机号 */
	function clearPhone(str){
		$("#delPhoneId").val("");
		$("#phoneCount").text(0);
	}
	/* 点击点击分组显示全部手机号 */
	function selectInfo(id,lev,name) {
		$("#departmentName2").val(name);
		$("#departmentName3").val(name);
		if($("#parentId").val()==id){
			return ;
		}
		$("#parentId").val(id);
		$("#departmentLevel").val(lev);
		if(id==undefined){
			return ;
		}
		document.getElementById("treeView1").src = "/contacts/info!query.dhtml?id="+id;
	}
	
	function selectInfo2(id,lev) {
		$("#p_id").val(id);
		$("#department_lev").val(lev);
	}
	
	
	
	function getmanager(manager){
		$("#managerId").val(manager);
	}
	
	/* 	正则验证  */
	function checkval(t) {
    var re = /^[\u4e00-\u9fa5a-z0-9]{2,10}$/gi;//只能输入汉字和英文字母
    if (re.test(t)) {
        return true;
    } else {
        return false;
    }
} 
	/* 清除错误 提示  */
	function clearErr(){
		$("#error_alert1").hide();
		$("#error_info1").val("");
		
		$("#efileName").val("");
		$("#efile").empty();
		var file = $("#efile");
		file.after(file.clone().val(""));
		file.remove();
		
		$("#phoneScontent1").val("");
		$("#phoneNumber1").val("");
		$("#departmentName").val("");
		$("#error_alert").hide();
		$("#error_alert2").hide();
		$("#error_alert3").hide();
		$("#error_info").val("");
		$("#error_info2").val("");
		$("#phoneScontent2").val("");
	}
	function clearErr1(){
		$("#error_alert").hide();
		$("#error_alert1").hide();
		$("#error_alert2").hide();
		$("#error_alert3").hide();
	}
	/* 点击删除 按钮 删除 分组 */
function tdelsubBtnBack() {
	var id =$("#parentId").val();
	if(id=="" || id=="719"){
		nn.show("未选中分组!", "err");
		return ;
	}
	showMask();
	$.ajax({
		url : "/contacts/info!delDept.dhtml",
		type : "post",
		data:{"id":id},
		dataType : "json",
		success : function(response) {
			hideMask();
			if (response.result == 1) {
				nn.show("删除成功", "提示");
				document.getElementById("treeView").src = "/contacts/info!companyFrame.dhtml";
			} else if(response.result == -1){
				nn.show("获取分组失败，或者该分组不能删除", "err");
			} else if(response.result == -4){
				nn.show("该分组下包含子分组，不能直接删除", "err");
			} else if(response.result == -5){
				nn.show("该分组下包含手机号，不能直接删除", "err");
			}  else {
				nn.show("服务器异常", "err");
			}
		},
		error : function() {
			hideMask();
			nn.show("操作异常", "err");
		}
	});
}
	//显示遮罩层    
    function showMask(){     
        $("#mask").css("height",$(document).height());     
        $("#mask").css("width",$(document).width());     
        $("#mask").show();     
    }  
    //隐藏遮罩层  
    function hideMask(){
        $("#mask").hide();     
    }  
</script>
<body style="overflow:auto;overflow:-Scroll;overflow-x:hidden;">
	<div
		style="width:100%; border: 1px solid #ddd;padding-left: 10px;padding-top:10px;">
		<hl:historyToolBar></hl:historyToolBar>
	</div>
	<div style="width:270px;float: left;height:88%;font-size: 14px;border:2px solid #ddd;">
		<iframe id="treeView" src="" frameborder='0' width="100%"
			height="100%"></iframe>
	</div>
	<div id="mask" class="mask" style="display: none"></div>
	<div style="width: calc(100% - 270px);height:90%;float: left;">
		<div class="panel panel-default">
			<div class="panel-heading panel-heading-cus" style="border:1px solid #ddd;border-left: none;">
				<h3 class="panel-title">
					<form id="searchForm">
						<table class="smain">
							<tr>
								<td style="padding-right: 100px;padding-top:15px;display: none;"
									class="smain-td-30">
									<div class="input-group input-group-sm">
										<input type="text" id="parentId" name="parentId"
											style="display: none;"> <input type="text"
											id="managerId" name="managerId" style="display: none;">
										<input type="text" id="p_id" name="p_id"
											style="display: none;"> <input type="text"
											id="department_lev" name="department_lev"
											style="display: none;">
										<input type="text" id="phoneIdDel" name="phoneIdDel"
											style="display: none;"> <input type="text"
											id="delPhoneId" name="delPhoneId"
											style="display: none;">
									</div>
								</td>
								<td colspan="6" style="text-align: left;padding-left: 5px;padding-right: 10px;padding-top:15px;">
									<%-- <hl:createPermission> --%>
										<button type="button" class="btn btn-primary" id="addBtn"
											data-toggle="modal" data-target="#myModal" 
											onclick="clearErr();">添加分组</button>
										<button type="button" class="btn btn-primary" id="updateBtn"
											data-toggle="modal" data-target="#myModal2"
											onclick="clearErr();">修改分组名称</button>
										<button type="button" class="btn btn-danger" id="delBtn"
											data-toggle="modal" data-target="#delbox">删除分组</button>
									<%-- </hl:createPermission> --%>
								</td>
								<td colspan="6" style="text-align: right;padding-left: 50px;padding-right: 10px;padding-top:15px;">
									<%-- <hl:createPermission> --%>
										<button type="button" class="btn btn-primary" id="addBtn1"
											data-toggle="modal" data-target="#myModal1122"
											onclick="clearErr();">添加手机号</button>
										<button type="button" class="btn btn-primary" id="addBtn8"
											data-toggle="modal" data-target="#myModal8"
											onclick="clearErr();">修改备注</button>
										<button type="button" class="btn btn-danger" id="delPhone"
											data-toggle="modal" data-target="#myModal7"
											onclick="clearErr();">删除手机号</button>
									<%-- </hl:createPermission> --%>
								</td>
							</tr>
						</table>
					</form>
				</h3>
			</div>
		</div>
		<div style="width:50%;float: left;height:84%;font-size: 14px;border:2px solid #ddd;">
			<iframe id="treeView1" src="" frameborder='0' width="100%" style="padding-bottom: 1px;"
				height="100%"></iframe>
		</div>
		<div style="width:50%;float: right;height:84%;font-size: 14px;border:2px solid #ddd;">
			<div style="width:85%;height:5%;padding:8px;margin:0px auto;" id="">
				<span>已选中手机号：<span id="phoneCount">0</span>条</span>
			</div>
			<div style="width:85%;height:10%;padding:8px;margin:0px auto;padding-top:5%;" id="">
				<span style="color: red">*</span>短信内容
				<div class="row" id="smsDIV">
					<textarea rows="5" cols="50" class="form-control"  style="resize: none;background-color: #fff"
						maxlength="1000" placeholder="请输入短信内容" id="scontentInfo" name="scontentInfo"></textarea>
					<span id="first"
						style="position: relative;float:right;width: 50px;height: 20px;text-align:right;color: #999;z-index: 1000"><span
						id="font_count">0</span>/1000</span>
				</div>
			</div>
			<div style="width:85%;float:left;height:10%;padding:8px;margin:0px auto;padding-left:7.5%" id="">
				<span class="spclass">&nbsp;&nbsp;</span><span class="spclass1">发送时间</span>
				<input type="text" id="sendDate" class="form-control" name="sendDate" placeholder="默认立即发送" 
					readonly="readonly" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'%y-%M-%d %H:%m'})"
					style="background-color: #fff;width:80%">
			</div>
			<div style="width:85%;float: left;font-size: 14px; solid #ddd;padding:8px;padding-left:7.5%;padding-top:8%">
				<input type="button" style="float: left" value="发送短信" id="sendBut" class="btn btn-primary" data-toggle="modal" data-target="#myModal9" >
			</div>
		</div>
	</div>

	<!-- 修改分组页面 -->
	<div class="modal fade" id="myModal2" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">修改分组</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" id="deptForm2" action=""
						method="post" onkeydown="if(event.keyCode==13) return false;">
						<div class="form-group">
							<label for="departmentName2" class="col-sm-2 control-label"><span
								style="color: red">*</span>分组名称:</label>
							<div class="col-sm-10">
								<input type="text" class="form-control" id="departmentName2" onfocus="clearErr1();" 
									maxlength="10" style="width:445px;" name="departmentName2"
									placeholder="请输入分组名称，不超过10个字符"> <input type="text"
									id="departmentName3" style="display: none;"
									name="departmentName3">
							</div>
						</div>
						<div class="form-group" style="display:none; " id="error_alert2">
							<label for="" class="col-sm-2 control-label"></label>
							<div class="col-sm-10">
								<span style="color: red" id="error_info2"></span>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label"></label>
							<div class="col-sm-10">注：只能在权限范围内修改分组名称，否则不能操作！</div>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-primary" id="submitBtn2">编辑</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	<!-- 添加分组页面 -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">添加分组</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" id="deptForm" action="" method="post"
						onkeydown="if(event.keyCode==13) return false;">
						<div class="form-group">
							<label for="inputTemplateName" class="col-sm-2 control-label"><span
								style="color: red">*</span>分组名称:</label>
							<div class="col-sm-10">
								<input type="text" class="form-control" id="departmentLevel"
									name=departmentLevel style="display: none;"> <input
									type="text" class="form-control" id="departmentName"
									maxlength="10" style="width:445px;" name="departmentName" onfocus="clearErr1();" 
									placeholder="请输入分组名称，不超过10个字符">
							</div>
						</div>
						<div class="form-group" style="display:none; " id="error_alert">
							<label for="" class="col-sm-2 control-label"></label>
							<div class="col-sm-10">
								<span style="color: red" id="error_info"></span>
							</div>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-primary" id="submitBtn">添加</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	<!-- 修改手机号备注信息 -->
	<div class="modal fade" id="myModal8" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myPhone">修改手机号备注</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" id="phoneForm2" action="" method="post"
						onkeydown="if(event.keyCode==13) return false;">
						<div class="form-group">
							<label for="inputPhoneName" class="col-sm-2 control-label"><span
								style="color: red">*</span>备注信息:</label>
							<div class="col-sm-10">
								<input
									type="text" class="form-control" id="phoneScontent2" onfocus="clearErr1();" 
									maxlength="20" style="width:445px;" name="phoneScontent2"
									placeholder="请输入备注信息">
							</div>
						</div>
						<div class="form-group" style="display:none; " id="error_alert3">
							<label for="" class="col-sm-2 control-label"></label>
							<div class="col-sm-10">
								<span style="color: red" id="error_info2"></span>
							</div>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-primary" id="submitBtn221">提交</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
<!-- 删除手机号 -->
<div id="myModal7" class="modal fade bs-example-modal-sm in" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" style="display: none; padding-left: 17px;">
	<div class="modal-dialog modal-sm">
			<div class="modal-content">
				<div class="modal-header">
					<button class="close" type="button" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">×</span>
					</button>
					<h4 id="tagModalLabel" class="modal-title">提示</h4>
				</div>
				<div class="modal-body">确定要这样操作吗？</div>
					<div class="modal-footer">
					<button class="btn btn-default" type="button" data-dismiss="modal" id="falBtn">取消</button>
					<button id="sureBtn12" class="btn btn-primary" type="button">确认</button>
			</div>
		</div>
	</div>
</div>

<div id="myModal9" class="modal fade bs-example-modal-sm in" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" style="display: none; padding-left: 17px;">
	<div class="modal-dialog modal-sm">
			<div class="modal-content">
				<div class="modal-header">
					<button class="close" type="button" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">×</span>
					</button>
					<h4 id="tagModalLabel" class="modal-title">提示</h4>
				</div>
				<div class="modal-body">确定要这样操作吗？</div>
					<div class="modal-footer">
					<button class="btn btn-default" type="button" data-dismiss="modal" id="falBtn">取消</button>
					<button id="sureBtn19" class="btn btn-primary" type="button">确认</button>
			</div>
		</div>
	</div>
</div>
<!-- 为分组添加手机号 -->
<div class="modal fade" id="myModal1122" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"  aria-label="Close">
	        	<span aria-hidden="true">&times;</span>
	    </button>
	    <h4 id="tagModalLabel" class="modal-title">添加手机号</h4>
	  </div>
	    <div class="modal-body">
	    	<form action="/contacts/info!addPhoneNo.dhtml" method="post" enctype="multipart/form-data" id="formm11" class="form-horizontal">
				<div class="form-group">
					<label for="inputPhoneName" class="col-sm-2 control-label"><span
						style="color: red">*</span>手机号码:</label>
					<div class="col-sm-10">
						<input type="text" class="form-control" id="phoneNumber1" onfocus="clearErr1()"
							maxlength="11" style="width:445px;" name="phoneNumber1"
							 onkeyup="this.value=this.value.replace(/[^\r\n0-9]/g,'');" 
								 onafterpaste="this.value=this.value.replace(/[^\r\n0-9]/g,'');" 
							placeholder="请输入手机号码">
						<input type="text" style="display: none" id="parentIdss" name="parentIdss" >
						<input type="text" style="display: none" id="efileName" name="efileName" >
					</div>
				</div>
				<div class="form-group">
					<label for="phoneScontent1" class="col-sm-2 control-label"><span
						style="color: red"></span>备注信息:</label>
					<div class="col-sm-10">
						<input
							type="text" class="form-control" id="phoneScontent1"
							maxlength="20" style="width:445px;" name="phoneScontent1"
							placeholder="请输入备注信息">
					</div>
				</div>
				<div class="form-group">
					<label for="" class="col-sm-2 control-label">批量添加</label>
					<div class="col-sm-10">
						<div class="div1" style="float: left">
							<input type="file" id="efile" name="efile" onclick="clearErr1();" />
						</div>
						<div class="div1" style="float: right;padding-right: 5%">
							<input type="button" value="excel样例" class="btn btn-success btn-sm" id="outExcel1" >
							<input type="text" value="${path1 }" style="display: none;" id="path1" >
						</div>
					</div>
				</div>
				<div class="form-group" >
			        <input type="text" id="scontentFrom1" name="scontentFrom1" value="" style="display:none"/>
					<input type="text" id="efileName1" name="efileName1" style="display: none;padding-left:5%;top:0px;border:none;width:60%" readonly="readonly" />
				</div>
				<div class="form-group" style="display:none; " id="error_alert1">
					<label for="" class="col-sm-2 control-label"></label>
					<div class="col-sm-10">
						<span style="color: red" id="error_info1"></span>
					</div>
				</div>
			</form>
	    </div>
	   <div class="modal-footer">
		    <button type="button" class="btn btn-primary" id="upload">提交</button>
	        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      	</div>
    </div>
 </div>
</div>
	<!-- 点击删除按钮 弹出提示框 -->
	<hl:modalPromptBox tid="delbox" tsubBtn="delsubBtn"></hl:modalPromptBox>

</body>
</html>

