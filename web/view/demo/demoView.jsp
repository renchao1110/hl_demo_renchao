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
								<td style="padding-right: 100px;padding-top:15px;display: none;" class="smain-td-30">
									
							</tr>
						</table>
					</form>
				</h3>
			</div>
		</div>
	</div>

</body>
</html>

