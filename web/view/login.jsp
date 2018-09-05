<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8" />
		<meta http-equiv="x-ua-compatible" content="IE=edge" >
		<title>登录</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<link href="/css/assets/css/bootstrap.min.css" rel="stylesheet" />
		<link rel="stylesheet" href="/css/assets/css/font-awesome.min.css" />
		<link rel="stylesheet" href="/css/assets/css/ace-rtl.min.css" />
		<link rel="stylesheet" href="/css/assets/css/css.css" />
		
		<%@ include file="/include/metalib.jsp"%>
<%@ include file="/include/metatable.jsp"%>
<script type="text/javascript">
	
$(document).ready(function() {
	var nfy = new cusNotify();
	var oo = new cusLoader();
	getImgVer();
	/* 获取短信验证码 */
	$("#getSms").click(function(){
		var verify = $("#verify").val();
		var verifyno = $("#verifyno").val();
		var policeno = $("#policeno").val();
		if(policeno == ''){
			$("#errorr1").text("手机号不能为空");
			$("#errorr1").show();
			return ;
		}
		if(!(/^(1[3-9])\d{9}$/.test(policeno))){
			$("#errorr1").text("手机号格式错误");
			$("#errorr1").show();
			return ;
		}
		if(verify != verifyno || verify=="" || verifyno==""){
			$("#errorr1").text("图片验证码错误");
			$("#errorr1").show();
			return;
		} 
		
		$.ajax({
			type: "post",
			url: "/login/login!sendVerify.dhtml",
			data:{"_account" : policeno },
			success: function(data){
				if(data.result==1){
					getImgVer();
					$("#errorr1").text("手机号格式错误");
					$("#errorr1").show();
					return;
				}else if(data.result==2){
					getImgVer();
					$("#errorr1").text("服务器异常，请稍后重试");
					$("#errorr1").show();
					return;
				}else if(data.result == 3){
					getImgVer();
					$("#errorr1").text("验证码发送太频繁，请稍后再试");
					$("#errorr1").show();
					return;
				}else if(data.result==4){
					getImgVer();
					$("#errorr1").text("该手机号未注册");
					$("#errorr1").show();
					return;
				}else if(data.result==5){
					getImgVer();
					$("#errorr1").text("子账户不可用");
					$("#errorr1").show();
					return;
				}else if(data.result==6){
					getImgVer();
					$("#errorr1").text("今日发送验证码次数已达上限");
					$("#errorr1").show();
					return;
				}else if(data.result == 0){
					$("#phone").val(data.conPhone);
					showtime(60);
					$("#errorr1").text("短信发送成功，请注意查收...");
					$("#errorr1").show();
					return;
				}
			},
			dataType: "json",
			async: false,
		});
		
	});
	
	$("input").focus(function(){
		$("#errorr1").text("");
		$("#errorr1").hide();
	});
});

function getImgVer(){
	var imgPath = $("#verifyImg").attr("src");
	$.ajax({
		type: "get",
		url: "/login/login!getVerifyImg.dhtml",
		data:{"_path":imgPath},
		success: function(data){
			$("#verifyImg").attr("src",data.filePath);
			$("#verifyno").val(data.verifyNumber);
		},
		dataType: "json",
		async: false,
	});
}
function checkVerify(){
	var ss = $("#verify").val();
	var aa = $("#verifyno").val();
	if(ss != aa){
		$("#errorr1").show();
	}else {
		$("#errorr1").hide();
	}
}
	function removes(){
		$("#errorr1").hide();
	}
	/* 三十秒内禁止点击  */
	function showtime(t){
    	//document.getElementById("getSms").disabled=true; 
    	$("#getSms").attr("disabled",true);
	    for(i=1;i<=t;i++) {
	        window.setTimeout("update_p(" + i + ","+t+")", i * 1000); 
	    }
	    /* getImgVer(); */
	}
	function update_p(num,t) {
    	if(num == t) {
        	$("#getSms").val(" 重新发送 ");
        	$("#getSms").removeAttr("disabled");
   		}else{
        	printnr = t-num;
        	$("#getSms").val(printnr+"秒后重新发送");
    	}
	}
function login(){
	var date = new Date(2099,12,12);
	var languageSel = "zh";
	var policeno = document.getElementById("policeno").value;
	var phone1 = document.getElementById("phone").value;
	var smsVerify = document.getElementById("smsVerify").value;
	var imgPath = $("#verifyImg").attr("src");
	document.getElementById("imgPaths").value=imgPath;
	var verify = $("#verify").val();
	var verifyno = $("#verifyno").val();
	if(policeno==""){
		$("#errorr1").text("手机号不能为空");
		$("#errorr1").show();
		return ;
	}
	if(!(/^(1[3-9])\d{9}$/.test(policeno))){
		$("#errorr1").text("手机号格式错误");
		$("#errorr1").show();
		return ;
	}
	if(verify=="" || verifyno==""){
		$("#errorr1").text("验证码不能为空");
		$("#errorr1").show();
		return;
	}
	if(smsVerify==""){
		$("#errorr1").text("短信验证码不能为空");
		$("#errorr1").show();
		return ;
	}
	if(verify != verifyno){
		$("#errorr1").text("验证码错误");
		$("#errorr1").show();
		return;
	}
	/* document.form1.submit(); */
	//var formData1 = new FormData($("#form1")[0]);
	$.ajax({
		url: "/login/login!checkVerify.dhtml",
		data:{"policeno":policeno,"smsVerify":smsVerify,"imgPaths":imgPath},
		success:function(data){
			if(data == "2"){
				$("#errorr1").text("获取手机号失败，或者手机号格式不正确");
				$("#errorr1").show();
				return;
			}else if(data == "3"){
				$("#errorr1").text("获取短信验证码失败");
				$("#errorr1").show();
				return;
			}else if(data == "4"){
				$("#errorr1").text("账号不可用");
				$("#errorr1").show();
				return;
			}else if(data == "5"){
				$("#errorr1").text("账号不可用");
				$("#errorr1").show();
				return;
			}else if(data == "6"){
				$("#errorr1").text("账号被禁用");
				$("#errorr1").show();
				return;
			}else if(data == "7"){
				$("#errorr1").text("短信验证不正确");
				$("#errorr1").show();
				return;
			}else if(data == "0"){
				document.form1.submit();
			}else{
				$("#errorr1").text("系统繁忙，请稍后重试");
				$("#errorr1").show();
				return;
			}
		},error:function(){
			$("#errorr1").text("系统繁忙，请稍后重试");
			$("#errorr1").show();
			return;
		}
	});
}
function onPwFocus(event){
	if(event.keyCode==13){
		$('password').focus();
	}else if(event.keyCode==9){
		//$('imgId').focus();
	}
}
function init(){
	//document.getElementById("mainTb").style.display = ""; 
	/*if(window.addEventListener){ 
		alert("提示：请用ie浏览器登录，暂不支持其它浏览器。");
	}else if(window.attachEvent){ 
	 	document.getElementById("mainTb").style.display = ""; 
	}else{ 
		alert("请使用IE6版本以上的浏览器") ;
	}*/
}
</script>
	</head>
	<body class="login-layout">
		<div class="main-container">
			<div class="main-content">
				<div class="row">
					<div class="col-sm-10 col-sm-offset-1">
						<div class="login-container" style="margin-top: 120px;">
							<div class="center">
								<h1>
									<i class="icon-leaf green"></i>
									<span class="red">新韩银行</span>
									<!-- <div class="white">短信群发平台</div> -->
								</h1>
							</div>

							<div class="space-6"></div>

							<div class="position-relative">
								<div id="login-box" class="login-box visible widget-box no-border">
									<input type="hidden" id="verifyno"  >
									<div class="widget-body" >
										<div class="widget-main" >
											<h4 class="header blue lighter bigger">
												<i class="icon-coffee green"></i>
												短信群发平台
											</h4>

											<div class="space-6"></div>

											<form name="form1" action="/login/login!loginSys.dhtml" method="post">
												<fieldset>
													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="text" id="phone" name="phone" style="display: none"  >
															<input type="text" placeholder="请输入手机号"  class="form-control" maxlength="11"
																id="policeno" name="policeno" onKeyDown="if(event.keyCode==13||event.keyCode==9) onPwFocus(event)"
																onkeyup="this.value=this.value.replace(/[^\r\n0-9]/g,'');" 
								 								onafterpaste="this.value=this.value.replace(/[^\r\n0-9]/g,'');" />
															<i class="icon-user"></i>
														</span>
													</label>
													<label class="">
														<span class="">
															<div style="float: left">
																<input type="text" placeholder="请输入验证码"  class="form-control" 
																	id="verify" name="verify" onkeydown='if(event.keyCode==13) login();' style="width: 150px;padding-left: 1px" />
															</div>
															<div style="float: right;padding-left:20px;">
																<img src="" id="verifyImg" onclick="getImgVer()" style="width: 120px;height: 40px;border: 1px solid #D9D9D9;cursor: pointer" alt="获取验证码" >
															</div>
															<input type="text" id="imgPaths" name="imgPaths" style="display: none"  >
														</span>
													</label>
													<label class="">
														<span class="">
															<div style="float: left">
																 <input type="text" placeholder="短信验证码"  class="form-control" style="width: 150px"
																	id="smsVerify" name="smsVerify" 
																	onkeydown='if(event.keyCode==13) login();' />
															</div>
															<div style="float: right;padding-left: 20px;">
																<input type="button" id="getSms" name="getSms" style="border: 1px solid #D9D9D9;cursor: pointer" class="btn btn-sm btn-primary" value="发送短信" >
															</div>
														</span>
													</label>
													<label>
														<span id="errorr1" name="errorr1" style="display: none;margin-top: 20px;color: red;">验证码错误</span>
													</label>

													<div class="space"></div>

													<div class="clearfix">
														<button type="button"   onclick="login();" class="width-35 pull-right btn btn-sm btn-primary">
															<i class="icon-key"></i>
															登录
														</button>
														<span id="loginInfo" name="loginInfo" style="display: none;font-weight: bolder;margin-left: 50px;margin-top: 20px">验证信息中，请稍等...</span>
														<input type="checkbox" name="checkbox"
																value="checkbox" id='saveBox' checked="checked" style="display: none">
													</div>

													<div class="space-4"></div>
												</fieldset>
											</form>
										</div><!-- /widget-main -->
									</div><!-- /widget-body -->
								</div><!-- /login-box -->
							</div><!-- /position-relative -->
						</div>
					</div><!-- /.col -->
				</div><!-- /.row -->
			</div>
		</div><!-- /.main-container -->
</body>
</html>
