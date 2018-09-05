/**pjfox常用工具类**/
function getWidth(objId,number){
	var total=Ext.get(objId).getWidth()-20;
	return total*number/100;
	
}
function jsMap(){
	this.keys = new Array();  
	this.data = new Object(); 
	this.put = function(key, value) {  
		this.keys.push(key);
		this.data[key] = value;  
	}; 
	this.get = function(key) { 
		return this.data[key];  
	}; 
	this.size = function (){
		return this.keys.length;
	};

};
function areatextUtil(areaObj){
	this.areaObj = areaObj;
	this.areaTipMsg = '';
	this.msgCss = 'padding:5px;';
	this.setMax = function(max){
		this.max = max;
		return max;
	};
	this.setMsgCss = function(cssText){
		this.msgCss = cssText;
	}
	this.setTipMsg = function(msg){
		this.areaTipMsg = msg;
	}
	this.showMsg = function(){
		//判断是否需要显示字数
	   checkAreaShowMsg(this);
	}
}
function checkAreaShowMsg(obj){
	var content = obj.areaObj.value ;
	if (content.length > obj.max) {
		obj.areaObj.value = content.substring(0,obj.max);
		return;
	}
	var objArea = document.getElementById("tipArea"+obj.areaObj.id);
	var hasCont = 0;
    if(!objArea){
		var cont = document.createElement("div");
		cont.setAttribute('id','tipArea'+obj.areaObj.id)
		cont.setAttribute('name','tipArea'+obj.areaObj.id)
		cont.style.cssText= obj.msgCss;
		hasCont = obj.max - content.length;
		if(obj.areaTipMsg.length == 0){
			cont.innerHTML = '提示：最大限制字数为 <font color=red>'+obj.max+'</font> 个字，已写入 <font color=red>'+content.length+'</font> 个字，还剩 <font color=red>'+hasCont+'</font> 个字';
		}else{
			var tempName = obj.areaTipMsg.replace(new RegExp("#0#","gm"),obj.max);
			tempName = tempName.replace(new RegExp('#1#',"gm"),content.length);
			tempName = tempName.replace(new RegExp('#2#',"gm"),hasCont);
			cont.innerHTML = tempName;
		}
		if(obj.areaObj.parentNode){
       			if(obj.areaObj.nextSibling){
            			//前插入子结点
          			obj.areaObj.parentNode.insertBefore(cont, obj.areaObj.nextSibling);
       			}else{
       				obj.areaObj.parentNode.appendChild(cont);
       			}
    	}
	}else{
		hasCont = obj.max - content.length;
		if(obj.areaTipMsg.length == 0){
			objArea.innerHTML = '提示：最大限制字数为 <font color=red>'+obj.max+'</font> 个字，已写入 <font color=red>'+content.length+'</font> 个字，还剩 <font color=red>'+hasCont+'</font> 个字';
		}else{
			var tempName = obj.areaTipMsg.replace(new RegExp("#0#","gm"),obj.max);
			tempName = tempName.replace(new RegExp('#1#',"gm"),content.length);
			tempName = tempName.replace(new RegExp('#2#',"gm"),hasCont);
			objArea.innerHTML = tempName;
		}
	}
}

function subLength(obj,max){
	var areaObj = new areatextUtil(obj);
	areaObj.setMax(max);
	document.onkeyup = function(){
		areaObj.showMsg();
		subAfter();
	}
	document.onkeydown = function(){
		areaObj.showMsg();
		subAfter();
	}
	document.onfocusout = function(){
		areaObj.showMsg();
		subAfter();
	}
}
function subLengthEx(areaObj,obj,max){
	if(areaObj){
		document.onkeyup = function(){
			areaObj.showMsg();
			subAfter();
		}
		document.onkeydown = function(){
			areaObj.showMsg();
			subAfter();
		}
		document.onfocusout = function(){
			areaObj.showMsg();
			subAfter();
		}
	}else{
		subLength(obj,max);
	}
}
function subAfter(){
	
}

Array.prototype.in_array = function(e){
	for(i=0;i<this.length && this[i]!=e;i++);
	return !(i==this.length);
}

function regUtil(obj,reText){
	var reg = new RegExp(regObj[reText]);
	if(!reg.test(obj.value)){
		obj.value = "";
	}
}

function ajaxUtil(url,parms){
	var myAjax = new Ajax.Request(
		url,
		{
		method: 'post',
		parameters:parms,
		onComplete: ajaxResultUtil
		}
	);
}

function ajaxUtilEx(url,parms){
	Ext.Ajax.request({
            url: url,
            params: parms,
            method: 'POST',
            success: function (response, options) {
            	succmp(response, options);
            },
            failure: function (response, options) {
                failcmp(response, options)
            }
        });
}
function succmp(response, options){
	var rootobj = Ext.util.JSON.decode(response.responseText);
   	if(rootobj.result == '0'){
   		alert('操作成功。&nbsp;&nbsp;&nbsp;&nbsp;');
   	}else{
   		alert(decodeURIComponent(rootobj.msg)  );
   	}
   	sucOther();
}

function sucOther(){
	
}

function failcmp(response, options){
	Ext.MessageBox.alert('失败', '请求超时或网络故障,错误编号：' + response.status);
	if(grid){
		grid.loadMask.hide();
	}
	failOther();
}

function failOther(){
	
}
//EXTJS 扩展
if(typeof Ext != 'undefined'){
	Ext.grid.GridPanel.prototype.unSelectAll = function(){
		var view = this.getView();
		var sm = this.getSelectionModel();
		if(sm){
			sm.clearSelections();
			var hd = Ext.fly(view.innerHd);
			var c = hd.query('.x-grid3-hd-checker-on');
			if(c && c.length>0){
				Ext.fly(c[0]).removeClass('x-grid3-hd-checker-on')
			}
		}
	}
}
 

var tFont;
function shwWin(typeStr) {
   		if(tFont != null){
			tFont.hide();
		}
		tFont=new Ext.Window({
				width:256,
				height:350,
				constrain:true,
				resizable:false,
				closeAction:"hide",
				items: [{
     					region: "center",
     					html:"<iframe src='/sg/switchfigure!showImgWin.dhtml?_upType="+typeStr+"' width='256px' height='350px'></iframe>"
   	 					}],
				title:"上传图片"
			});
		tFont.show();
	}
function hideWin(){
		if(tFont != null){
			tFont.hide();
		}
		
	}
var showWins;
function shwWinEx(url,h,w,wtitle) {
		document.body.scroll = 'no';
		var hh = 500;
		if(h != null){
			hh = parseInt(h);
		}
		var ww = 1000;
		if(w != null){
			ww = w;
		}
		var wt = "";
		if(wtitle != null){
			wt = wtitle;
		}
		
   		if(showWins != null){
			showWins.hide();
		}
		showWins=new Ext.Window({
				width:ww,
				height:hh,
				constrain:true,
				resizable:false,
				modal : true,
				plain :true,
				animateTarget : 'target',
				closeAction:"hide",
				items: [{
     					region: "center",
     					html:"<iframe src='"+url+"' width='100%' height='"+hh+"px'></iframe>"
   	 					}],
				title:"<span style='margin-left: 45%'>"+wt+"</span>"
				
			});
		showWins.on("hide",function(){
			       document.body.scroll = '';
			
			     });
		showWins.show();
	}

function hideWinEx(){
		if(showWins != null){
			document.body.scroll = '';
			showWins.hide();
		}
		
	}
	
var popWindowTree
function ajaxTreeUtil(){
	this.setElname = function(elname){
		this.elname = elname;
		return elname;
	};
	this.setElId = function(elId){
		this.elId = elId;
		return elId;
	};
	this.setRootId = function(rootId){
		this.rootId = rootId;
		return rootId;
	};
	this.setWinId = function(winId){
		this.winId = winId;
		return winId;
	};
	this.setBeforeloadUrl = function(beforeloadUrl){
		this.beforeloadUrl = beforeloadUrl;
		return beforeloadUrl;
	};
	this.setTbar1 = function(tbar1){
		this.tbar1 = tbar1;
		return tbar1;
	};
	
	
	
}
var treeUtil;
function showDepartmentTreeUtilEx(setObj,obj){
    if(!popWindowTree){
    		treeUtil = new Ext.tree.TreePanel({
		        el:setObj.elId,
		        autoScroll:true,
		        rootVisible:false,
		        animate:true,
		        enableDD:true,
		        lines : false,
		        containerScroll: true, 
		        loader: new Ext.tree.TreeLoader(),
		        tbar: [
            	 setObj.tbar1==null?'':setObj.tbar1
        		]
		    });
		    var root = new Ext.tree.AsyncTreeNode({
		        text: setObj.elname,
		        draggable:false,
		        id:setObj.rootId
		    });
		    treeUtil.setRootNode(root);
			treeUtil.on("beforeload", function(node){
				subTreeBeforeload(treeUtil,setObj,obj,node,popWindowTree)
			});
			treeUtil.on("click", function(node){
				subTreeClick(setObj,obj,node,popWindowTree);
			});
		    // render the tree
		    treeUtil.render();
		    root.expand(false, /*no anim*/ false);
            popWindowTree = new Ext.Window({
                el:setObj.winId,
                layout:'fit',
                width:250,
                height:400,
                closeAction:'hide',
                plain: true,
                items: treeUtil
            });
     }
    popWindowTree.show(obj);
}

function subTreeClick(setObj,obj,node,popWin){
	
};
function subTreeBeforeload(tree,setObj,obj,node,popWin){
	
};

function getSearchFromParams(){
	var nodes = Ext.query("#searchForm input,select");
	if(nodes!=null){
		var searchParameter=[];
		nodes.each(function(obj){
			if(obj.id != 'ext-gen32' && obj.value.trim()!=""){
				var s = obj.value.replace(/"/g,'\\"');
				s = s.replace(/\\/g,'');
				searchParameter.push( obj.id+"="+s);
			}
		});
		var str = searchParameter.join("&").toString();
		return str;
	}
}
function initsearch(_start,_limit){
	var nodes = Ext.query("#searchForm input,select");
		if(nodes!=null){
			var searchParameter=[];
			nodes.each(function(obj){
				if(obj.id != 'ext-gen32' && obj.value.trim()!=""){
					var s = obj.value.replace(/"/g,'\\"');
					s = s.replace(/\\/g,'');
					searchParameter.push( obj.id+":\""+s+"\"");
					
				}
			});
			if(searchParameter.length==0){
				ds.baseParams={};
				ds.load({params:{start:_start, limit:_limit}});
				return;
			}
			searchParameter=searchParameter.join(",");
			ds.baseParams=eval('({'+searchParameter+'})');
			ds.load({params:{start:0, limit:_limit}});
	}
}
function addDate(dd,dadd){  
	var a = new Date(dd)  
	a = a.valueOf()  
	a = a + dadd * 24 * 60 * 60 * 1000  
	a = new Date(a)  
	return a;  
}  

Ext.apply(Ext.form.VTypes, {
	    daterange : function(val, field) {
	        var date = field.parseDate(val);
	        if(!date){
	            return;
	        }
	        if (field.startDateField ) {
	            var start = Ext.getCmp(field.startDateField);
	            var sdate = addDate(date,-1);
	            start.setMaxValue(sdate);
	        }else if (field.endDateField ) {
	           var end = Ext.getCmp(field.endDateField);
	            var edate = addDate(date,1);
	            end.setMinValue(edate);
	        }
	        if(field.nextDateField){
	        	 var nextDate = Ext.getCmp(field.nextDateField);
	        	 var ndate = addDate(date,1);
	        	  nextDate.setMinValue(ndate);
	        }else if(field.lastDateField){
	        	var lastDate = Ext.getCmp(field.lastDateField);
	        	var ldate = addDate(date,-1);
	        	 lastDate.setMaxValue(ldate);
	        }
	        return true;
	    }
});


Ext.form.CustomDateField = Ext.extend(Ext.form.DateField,  {
    // private
    readOnly: true,
    setValueFn:null,
    menuListeners : {
        select: function(m, d){
            this.setValue(d);
            if(this.setValueFn)
               this.setValueFn.call(this,this.formatDate(this.parseDate(d)));
        },
        show : function(){
            this.onFocus();
        },
        hide : function(){
            this.focus.defer(10, this);
            var ml = this.menuListeners;
            this.menu.un("select", ml.select,  this);
            this.menu.un("show", ml.show,  this);
            this.menu.un("hide", ml.hide,  this);
        }
    }
});
Ext.reg('customDateField', Ext.form.CustomDateField);
function toDateSelEx(dateStr,minDateStr){
	var d5222=$dp.$(dateStr);
	if(minDateStr && document.getElementById(minDateStr)
			&& document.getElementById(minDateStr).value != ''){
		setMMDateEx(minDateStr,dateStr);
		d5222.focus();
	}else{
		WdatePicker({onpicked:function(){d5222.focus();},
		    maxDate:'#F{$dp.$D(\''+dateStr+'\')}'
		    });
	}
}

function setEndDateEx(dateStr,nowDateValue){
	setMMDateEx(dateStr,nowDateValue);
}
function setMinDateEx(dateStr){
	WdatePicker({minDate:'#F{$dp.$D(\''+dateStr+'\')}'});
	
}
function setMaxDateEx(dateStr){
	WdatePicker({maxDate:'#F{$dp.$D(\''+dateStr+'\')}'});
}

function setMMDateEx(minDateStr,maxDateStr){
	if(minDateStr && maxDateStr 
		&& document.getElementById(minDateStr) && document.getElementById(maxDateStr)
		&& document.getElementById(minDateStr).value != '' && document.getElementById(maxDateStr).value != ''){
		WdatePicker({minDate:document.getElementById(minDateStr).value,maxDate:document.getElementById(maxDateStr).value});
	}else if(minDateStr && document.getElementById(minDateStr)
			&& document.getElementById(minDateStr).value != ''){
			setMinDateEx(minDateStr);
	}else if(maxDateStr && document.getElementById(maxDateStr)
			&& document.getElementById(maxDateStr).value != ''){
		setMaxDateEx(maxDateStr);
	}else{
		WdatePicker();
	}
}
 var layer;
   function SearchEmp(empid){
   var rempid = empid.trim();
   if(rempid == ''){
   	 return;
   }
	var inputBox = document.getElementById('empId');
	var iBtop  = inputBox.offsetTop;     //文本框的定位点高
	var iBheight  = inputBox.clientHeight;  //文本框本身的高
	var iBleft = inputBox.offsetLeft;    //文本框的定位点宽
	while (inputBox = inputBox.offsetParent){iBtop+=inputBox.offsetTop;iBleft+=inputBox.offsetLeft;}
	layer = document.getElementById('emp_list');
	layeri = document.getElementById('iemp');
	layer.style.top = iBtop+iBheight+6;
	layer.style.left = iBleft;  
	layeri.style.top = iBtop+iBheight+6;
	layeri.style.left = iBleft;
	layer.style.visibility='visible';
	layeri.style.visibility='visible';
    layer.style.zIndex = 2;
	layeri.style.zIndex =1;
	ajaxUtilEx('/att/semp!ajaxSemp.dhtml',{_empName:rempid});
}
/*
function succmp(response, options){
	var objs = Ext.util.JSON.decode(response.responseText);
	var data = "<table width='100%' border='0' cellpadding='0'  cellspacing='1' class='table_line_complex'>";
	data +="<tr onclick='layerClose();'  style='cursor: hand;position: relative; top: expression(this.offsetParent.scrollTop);'   title='点击关闭'> "
	data +="<td  height='25' class='info_title_01' nowrap='nowrap'>工号</td>";
	data +="<td  height='25' class='info_title_01' nowrap='nowrap'>姓名（拼音）</td>";
	data +="<td  height='25' class='info_title_01' nowrap='nowrap'>部门</td>";
	data +="<td  height='25' class='info_title_01' nowrap='nowrap'>在职状态</td>";
	data +="</tr>";
	if(objs.result == 'suc'){
		for(var i=0;i<objs.items.length;i++){
			var obj = objs.items[i];
			data +="<tr style='cursor: hand;' onClick=\"updateValue("+obj.eid+",'"+obj.empId+"','"+obj.empNameZh+"（"+obj.empNameEn+"）','"+obj.deptName+"','"+obj.status+"')\" > "
			data +="<td height='25' class='info_search_02' nowrap='nowrap'>"+obj.empId+"</td>";
			data +="<td height='25' class='info_search_02' nowrap='nowrap'>"+obj.empNameZh+"（"+obj.empNameEn+"）</td>";
			data +="<td height='25' class='info_search_02' nowrap='nowrap'>"+obj.deptName+"</td>";
			data +="<td height='25' class='info_search_02' nowrap='nowrap'>"+obj.status+"</td>";
			data +="</tr>";	
		} 
		if(objs.items.length<8){
			for(var j=0;j<8-objs.items.length;j++){
				data +="<tr style='cursor: hand;' > "
				data +="<td height='25' class='info_search_02' nowrap='nowrap'>&nbsp;</td>";
				data +="<td height='25' class='info_search_02' nowrap='nowrap'>&nbsp;</td>";
				data +="<td height='25' class='info_search_02' nowrap='nowrap'>&nbsp;</td>";
				data +="<td height='25' class='info_search_02' nowrap='nowrap'>&nbsp;</td>";
				data +="</tr>";
			}
		}
	}else{
		data +="<tr><td colspan='4'height='173px'  rowspan='8' align='center'>没有你要查询的人员！</td></tr> ";
	}
	data +="</table>";
	layer.innerHTML=data;
	var iwidth = document.getElementById('empId').style.width;
    layer.style.width = iwidth;
    layer.style.height = 198;
    layer.style.visibility = 'visible';
    layeri.style.width = iwidth;
    layeri.style.height = 200;
	layeri.style.visibility = 'visible';
	
}*/
function layerClose(){	
	layeri.style.visibility = 'hidden';
	layer.style.visibility = 'hidden';
    layer.style.zIndex = -2;
	layeri.style.zIndex =-1;
}

var popWindowMenu;
var checkdResource=new Array();
var tree;
function eachCheckedNode(node){
	if(!node.isLeaf()){
		node.eachChild(function(n){
		if(n.getUI().isChecked()){
			checkdResource.push(n.id);
		}
		eachCheckedNode(n);
		});
	}	
}
var popWindowMenu
function showMenuTree(obj){
    if(!popWindowMenu){
    		tree = new Ext.tree.TreePanel({
		        el:"menu-div",
		        autoScroll:true,
		        animate:true,
		        enableDD:true,
		        lines : true,
		        rootVisible: false,
		        containerScroll: true, 
		        loader: new Ext.tree.TreeLoader(),
		        tbar:[{
            			text:'设置',
            			tooltip:'设置',
            			iconCls:'ok',
            			handler:toggleDetails
        			  }]
		    });
		    var root = new Ext.tree.AsyncTreeNode({
		        text: "部门结构",
		        draggable:false,
		        id:"menuRoot",
		        allowDrag:false,
        		allowDrop :false
		    });
		    tree.setRootNode(root);
			tree.on("beforeload", function(node){
				tree.loader.dataUrl = "/att/semp!loadDeptTree.dhtml?parentId="+node.id;
			});
			
			tree.on("checkchange", function(node, isChecked){
				if(isChecked){
					eachToggleCheck(node,isChecked);
				}else{
					eachToggleCheck(node,isChecked);
				}
		
			});
		    // render the tree
		    tree.render();
		    root.expand(false, /*no anim*/ false);
            popWindowMenu = new Ext.Window({
                el:'menu-win',
                layout:'fit',
                width:250,
                height:400,
                closeAction:'hide',
                plain: true,
                items: tree
            });
            
            function eachToggleCheck(node,isChecked){	
		    	if(!node.isLeaf()){
		    		node.eachChild(function(n){
							n.getUI().toggleCheck(isChecked);
							eachToggleCheck(n);
					});
		    	}
  		 	}
  		 	
  		 	
     }
    tree.expandAll();
    popWindowMenu.show(obj);
    
}

function appendURL(url){
	if(url.lastIndexOf("?") == -1){
		url += url+"?"
	}else{
		url += url+"&"
	}
	return url+getSearchFromParams();
}
function appendURLWithGrid(url,grid){
	if(url.lastIndexOf("?") == -1){
		url += "?"
	}else{
		url += "&"
	}
	var pageVal = grid.getBottomToolbar().getPageData().activePage;
	var sizeVal = grid.getBottomToolbar().pageSize;
	var aparm = "start="+pageVal+"&limit="+sizeVal;
	aparm += "&"+getSearchFromParams();
	var parmStr = aparm.replace(new RegExp('&','gm'),'_a@b_');
	return url+aparm+"&_aparam ="+parmStr;
}

function linkEncodeParam(enCode){
	return enCode.replace(new RegExp('&','gm'),'_a@b_');
}

function linkURL(aurl){
	if(aurl.lastIndexOf("?") == -1){
		aurl += "?"
	}else{
		aurl += "&"
	}
	return aurl+"_setparam="+aparam;
}

function cancelUtil(toURl){
		window.location.href=linkURL(toURl);
	}