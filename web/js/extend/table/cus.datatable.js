var ttlanguage = {
                    "sProcessing": "正在加载中......",
                    "sLengthMenu": "每页显示 _MENU_ 条记录",
                    "sZeroRecords": "正在加载中......",
                    "sEmptyTable": "查询无数据！",
                    "sInfo": "当前显示 _START_ 到 _END_ 条，共 _TOTAL_ 条记录",
                    "sInfoEmpty": "显示0到0条记录",
                    "sInfoFiltered": "数据表中共为 _MAX_ 条记录",
                    "sSearch": "当前数据搜索",
                    "oPaginate": {
                        "sFirst": "首页",
                        "sPrevious": "上一页",
                        "sNext": "下一页",
                        "sLast": "末页"
                    }
                }
var ttiDisplayLength = 20;
var ttHeight =  0;
var ttfnServerParams = function(aoData){
	var nodes = $("#searchForm input[type=text],#searchForm select");
	if(nodes!=null){
		nodes.each(function(){
			if($.trim($(this).val()) !="" && $.trim($(this).attr("id")) != "" ){
				var s = $(this).val().replace(/"/g,'\\"');
				s = s.replace(/\\/g,'');
				aoData.push( { "name": $(this).attr("id"), "value": s } );
			}
		});
		var btnAll = $("#btnAll")[0]
		if(btnAll){
			btnAll.checked = false;
		}
	}
	if(isExitsFunction('etAddParamsEx')){
		etAddParamsEx(aoData);
	}
}

function etablePageChange(lname,startNum){
	 var tableSetings=$('#'+lname).dataTable().fnSettings()  
	 var pagingLen=tableSetings._iDisplayLength;//当前每页显示多少  
	 var page=tablePageDiv(parseInt(startNum, 10),pagingLen);
	 setTimeout(function () { 
		 $('#'+lname).dataTable().fnPageChange(page);
	 }, 1000);
	 
}

function tablePageDiv(exp1, exp2) {  //整除  
    var n1 = Math.round(exp1); //四舍五入     
    var n2 = Math.round(exp2); //四舍五入    
    var rslt = n1 / n2; //除    
    if (rslt >= 0) {  
        rslt = Math.floor(rslt); //返回小于等于原rslt的最大整数。     
    }  
    else {  
        rslt = Math.ceil(rslt); //返回大于等于原rslt的最小整数。     
    }  
    return rslt;  
} 

function searchBy13(){
	ttreload();
}
function getTableStartnum(tname){
	var tableSetings=$('#'+tname).dataTable().fnSettings()  
	var page_start=tableSetings._iDisplayStart;//当前页开始  
	return page_start;
}

function ttreload(tname,rurl){
	if(tname && rurl){
		$('#stable').DataTable().ajax.url( rurl ).load();
	}else if(tname){
		$('#'+tname).DataTable().ajax.reload(null,true);
	}else if(otable){
		 otable.ajax.reload(null,true);
	}
}



$(document).ready(function() {
	var sbh = 295;
	if($('.panel-heading-cus')){
		sbh = $('.panel-heading-cus').height()+150;
	}
	ttHeight = $(document).height()-sbh;
	var nodes = $("#searchForm input[type=text]");
	if(nodes!=null){
		nodes.each(function(){
			$(this).on("keyup",function(event){
				var keycode = event.which;  
			    if (keycode == 13) {  
			    	searchBy13();
			    }  
			})
		});
	}
	$('#searchBtn').on("click",function(){
		searchBy13();
	});
});


function extCheckBox(lname){
	$('#'+lname+' tbody').on( 'click', 'tr td input:checkbox', function () {
    	var ttflag = true;
        if($(this).hasClass('warning')){
        	ttflag = false;
        }
        $(this).parent().parent().toggleClass('warning')
        var oo = $(this).find("input:checkbox:first")[0];
        if(oo){
        	oo.checked=ttflag;;
        }
        
    } );
}
 
function extCheckAll(lname){
	// 全选
     $("#btnAll").bind("click", function () {
          $('#'+lname+' tbody tr ').each(function(){
         	var ff = document.getElementById("btnAll").checked;
         	var oo = $(this).find("input:checkbox:first")[0];
         	if(oo){
         		oo.checked=ff;
             	if(ff){
             		if(!$(this).hasClass('warning')){
            			$(this).toggleClass('warning')
           		 	}
             	}else{
             		if($(this).hasClass('warning')){
            			$(this).toggleClass('warning')
           		 	}
             	}
         	}
         })
     });
}
function fnInitComplete(oSettings, oData){
	if(isExitsFunction('fnInitCompleteEx')){
		fnInitCompleteEx(oSettings, oData);
	}
}
var otable;
function initOtable(tname,dataURL,columns,acolumns,checkAll,checkBox){
	otable = $('#'+tname).DataTable( {
		"oLanguage": ttlanguage, 
		"bProcessing": true,
        "bServerSide": true,
        "bFilter":false,
        "bSort":false,
        "bLengthChange":false,
        "scrollY": ttHeight,
        "scrollCollapse": true,
        "iDisplayLength":ttiDisplayLength,
        "sAjaxSource": dataURL,
        "fnServerParams":ttfnServerParams,
        "columns": columns,
        "initComplete":fnInitComplete
    });
	if(checkAll){
		extCheckAll(tname);
	}
	if(checkBox){
		extCheckBox(tname);
	}
	
	
}

function extGetChecked(tname){
	var deleteParameter=[];
	$('#'+tname+' tbody tr ').each(function(){
     	var oo = $(this).find("input:checkbox:first")[0];
     	if(oo && oo.checked){
     		deleteParameter.push("itemlist="+oo.value);
     	}
     });
	return deleteParameter.join("&");
}
function extRowDelBack(){
	if(otable){
		otable.row('.warning').remove().draw(false);
	}
}