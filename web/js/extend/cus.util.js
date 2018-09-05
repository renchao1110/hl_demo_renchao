var cusLoader;
var cusNotify;
$(document).ready(function() {
	cusLoader = function(lname){
		var oo = $("body");
		if(lname){
			oo = $("#"+lname);
		}
	    var loader = new Object();
	    loader.show = function(){
	    	oo.loader('show','<img src="/images/extend/base/loading.gif" />');
	    };
	    loader.hide = function(){
	    	oo.loader('hide');
	    }
	    return loader;
	 };
	 
	cusNotify = function(){
		var notify = new Object();
		notify.show = function(stext,type,stitle){
			var ss = "提示"
			var stype = "notice"
			var sc = "操作成功！"
			if(stitle){
				ss = stitle;
			}
			if(stext){
				sc = stext;
			}
			if(type == 'suc'){
				stype = "success";
			}else if(type == 'err'){
				stype = "error";
			}
			new PNotify({
		    	title: ss,
		    	text: sc,
		    	icon: 'glyphicon glyphicon-envelope',
		    	type: stype,
		    	delay: 3000,
		    	mouse_reset: true,
		    	styling: 'bootstrap3'
			});
	    };
	    return notify;
	}
});
function isExitsFunction(funcName) {
    try {
        if (typeof(eval(funcName)) == "function") {
            return true;
        }
    } catch(e) {}
    return false;
}

function myBrowser(){
    var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
    var isOpera = userAgent.indexOf("Opera") > -1;
    if (isOpera) {
        return "Opera"
    }; //判断是否Opera浏览器
    if (userAgent.indexOf("Firefox") > -1) {
        return "FF";
    } //判断是否Firefox浏览器
    if (userAgent.indexOf("Chrome") > -1){
  return "Chrome";
 }
    if (userAgent.indexOf("Safari") > -1) {
        return "Safari";
    } //判断是否Safari浏览器
    if (!!window.ActiveXObject || "ActiveXObject" in window){
    	return "IE";
    }else if (userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera) {
        return "IE";
    }; //判断是否IE浏览器
}