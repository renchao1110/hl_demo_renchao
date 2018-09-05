var otable;
$(function() {
	/* $('#myModal').modal('show'); */
	loadingInfo();
	var oo = new cusLoader();
	nn = new cusNotify();
	$('#cpnyId').change(function() {
		var va=$('#cpnyId option:selected').val();
		$('#recruitCpnyId option').each(function(){
			if($(this).val()==va){
				$(this).attr("selected",true);
			}
		})
		loadingInfo();
	});

	/* 提交 招聘 信息 内容 */
	$('#submitBtn').click(function() {
		var zJob=$('#zJob').val();
		var wJob=$('#wJob').val();
		var qJob=$('#qJob').val();
		var gJob=$('#gJob').val();
		var zResume=$('#zResume').val();
		var qResume=$('#qResume').val();
		var wResume=$('#wResume').val();
		var gResume=$('#gResume').val();
		var zAd=$('#zAd').val();
		var qAd=$('#qAd').val();
		var wBalance=$('#wBalance').val();
		var gBalance=$('#gBalance').val();
		if($('#recruitCpnyId option:selected').val()==""){
			nn.show("请选择招聘公司！", "err");
			return ;
		}
		if($('#payCpnyName option:selected').val()==""){
			nn.show("请选择付费公司！", "err");
			return ;
		}
		if(regIsNum(zJob)&&regIsNum(qJob)&&regIsNum(wJob)&&regIsNum(gJob)&&regIsNum(zResume)&&regIsNum(qResume)&&regIsNum(wResume)&&regIsNum(gResume)&&regIsNum(zAd)&&regIsNum(qAd)&&regIsDou(wBalance)&&regIsDou(gBalance)){
			$.ajax({
				url : "/recruit/info!addRInfo.dhtml",
				type : "post",
				dataType : "json",
				data : $('#recruitForm').serialize(),
				success : function(response) {
					$('#myModal').modal('hide');
					if(response.result==1){
						nn.show("添加成功!", "提示");
						loadingInfo();
					}else if(response.result==2){
						nn.show("输入有误！", "err");
					}else {
						nn.show("选择信息有误!", "err");
					}
					
					
					
				},
				error : function() {
					nn.show("系统异常！", "err");
					$('#myModal').modal('hide');
				}
			})
		}else{
			nn.show("只能输入数字！", "err");
		}
		
	});
	
	/*点击 是td 添加背景色*/
	$('#tbodyInfo').on('click','tr',function(){
		$('#tbodyInfo>tr').css('background','none');
		$(this).css('background','#ddd');
	})

});

function loadingInfo() {
	$('#tbodyInfo').empty();
	var cpnyId = $('#cpnyId option:selected').val();
	$.ajax({
				url : "/recruit/info!findObjInfo.dhtml",
				type : "post",
				dataType : "json",
				data : {
					"cpnyId" : cpnyId
				},
				success : function(response) {
					if (response.result == 1) {
						var list = response.data;
						var s_td = '';
						for ( var i = 0; i < list.length; i++) {
							s_td += '<tr>';
							/*
							 * s_td += '<td><input type="checkbox" name="ids"
							 * value="' + list[i].id + '"></td>';
							 */
							s_td += '<td>' + list[i].no + '</td>';
							s_td += '<td>' + list[i].cpnyName + '</td>';
							s_td += '<td>' + list[i].zJob + '</td>';
							s_td += '<td>' + list[i].zResume + '</td>';
							s_td += '<td>' + list[i].zAd + '</td>';
							s_td += '<td>' + list[i].qJob + '</td>';
							s_td += '<td>' + list[i].qResume + '</td>';
							s_td += '<td>' + list[i].qAd + '</td>';
							s_td += '<td>' + list[i].wJob + '</td>';
							s_td += '<td>' + list[i].wResume + '</td>';
							s_td += '<td>' + list[i].wBalance + '</td>';
							s_td += '<td>' + list[i].gJob + '</td>';
							s_td += '<td>' + list[i].gResume + '</td>';
							s_td += '<td>' + list[i].gBalance + '</td>';
							s_td += '<td>' + list[i].payCpnyName + '</td>';
							if('锁定'==list[i].ifLock){
								s_td += '<td style="color:red;">' + list[i].ifLock + '</td>';
							}else{
								s_td += '<td style="color:green;">' + list[i].ifLock + '</td>';
							}
							
							s_td += '<td><button onclick="scanInfo(\''+list[i].remark+'\');"   type="button" class="btn btn-primary" data-toggle="modal" data-target="#myModal2">详情</button></td>';
							if(list[i].ifLock=='锁定'){
								s_td += '<td><button onclick="getId(\''+list[i].id+'\');" data-toggle="modal" data-target="#delbox" type="button" class="btn btn-primary">解锁</button></td>';
							}else{
								s_td += '<td><button onclick="getId(\''+list[i].id+'\');" data-toggle="modal" data-target="#delbox" type="button" class="btn btn-primary">锁定</button></td>';
							}
							s_td += '</tr>';

						}
						s_td += '<td colspan="2">总计:</td>';
						s_td += '<td>' + response.zJob + '</td>';
						s_td += '<td>' + response.zResume + '</td>';
						s_td += '<td>' + response.zAd + '</td>';
						s_td += '<td>' + response.qJob + '</td>';
						s_td += '<td>' + response.qResume + '</td>';
						s_td += '<td>' + response.qAd + '</td>';
						s_td += '<td>' + response.wJob + '</td>';
						s_td += '<td>' + response.wResume + '</td>';
						s_td += '<td>' + response.wBalance + '</td>';
						s_td += '<td>' + response.gJob + '</td>';
						s_td += '<td>' + response.gResume + '</td>';
						s_td += '<td>' + response.gBalance + '</td>';
						s_td += '<td colspan="6">&nbsp;</td>';
						$('#tbodyInfo').append(s_td);
					}

				}

			});

}

/* 验证 是否满足 数字 */
function regIsNum(par) {
	var reg = /^\d{1,9}$/;
	if (reg.test(par)) {
		return true;
	}
	return false;
}

/* 验证满足 小数 */
function regIsDou(par){
	var reg=/^\d{1,7}(\.\d{1,2})?$/;
	if (reg.test(par)) {
		return true;
	}
	return false;
}

/* 点击禁用按钮 批量修改 删除状态 */
function tdelsubBtnBack() {
	var id=$('#getId').val();
	if ( id== "") {
		nn.show("未选中内容", "err");
		return;
	}
	$.ajax({
		url : "/recruit/info!isLock.dhtml",
		type : "post",
		data:{"id":id},
		dataType : "json",
		success : function(response) {
			if (response.count != 0) {
				nn.show("操作成功", "提示");
				loadingInfo();

			} else {
				nn.showErr();
			}
		},
		error : function() {
			nn.show("操作异常", "系统");
		}

	});
}

function scanInfo(para){
	$('#remark_p').text(para);
	
}
function selectAll(obj) {
    $('input[name=ids]').prop('checked', $(obj).prop('checked'));
}

function getId(id){
	$('#getId').val(id);
}


