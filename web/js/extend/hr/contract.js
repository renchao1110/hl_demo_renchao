var otable;
$(function() {
	var oo = new cusLoader();
	nn = new cusNotify();
	var columns = [
			{
				"sWidth" : "5%",
				"sClass" : "text-center",
				"data" : "id",
				"render" : function(data, type, full, meta) {
					return '<input type="checkbox"  class="checkbox"  value="'
							+ data + '"   />';
				}
			}, {
				"sWidth" : "10%",
				"sClass" : "text-center",
				"data" : "ename"

			}, {
				"sWidth" : "25%",
				"sClass" : "text-center",
				"data" : "company"
			}, {
				"sWidth" : "25%",
				"sClass" : "text-center",
				"data" : "department"

			}, {
				"sWidth" : "10%",
				"sClass" : "text-center",
				"data" : "gender"
			}, {
				"sWidth" : "10%",
				"sClass" : "text-center",
				"data" : "type",
				"render" : function(data, type, full, meta) {
					if (data == 0) {
						return "合同";
					} else {
						return "实习";
					}
				}
			}, {
				"sWidth" : "15%",
				"sClass" : "text-center",
				"data" : "conEnd"
			} ]
	initOtable('etable', "/con/info!getContractInfo.dhtml", columns, null,
			true, true);

	
	$('#conType').change(function(){
		extRowDelBack();
	});
	
});

/* 正则验证 */
function checkval(t) {
	var re = /^[\u4e00-\u9fa5a-z0-9]{2,10}$/gi;// 只能输入汉字和英文字母
	if (re.test(t)) {
		return true;
	} else {
		return false;
	}

}

/*  标记为 已读*/
function tdelsubBtnBack() {
	if (extGetChecked("etable") == "") {
		nn.show("未选中内容", "err");
		return;
	}
	$.ajax({
		url : "/con/info!labelContract.dhtml?" + extGetChecked("etable"),
		type : "get",
		dataType : "json",
		success : function(response) {
			if (response.count != 0) {
				nn.show("标记成功", "提示");
				extRowDelBack();

			} else {
				nn.showErr();
			}
		},
		error : function() {
			nn.show("操作异常", "系统");
		}

	});
}

