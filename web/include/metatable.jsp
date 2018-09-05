<%@page session="false" contentType="text/html;charset=UTF-8"%>
<!-- import  Js -->
<script language="javascript" src="/js/extend/table/jdatatables.js?v=1"></script>
<script language="javascript" src="/js/extend/table/datatables.bootstrap.js"></script>
<script language="javascript" src="/js/extend/table/cus.datatable.js?v=2"></script>

<!-- style -->
<link href="/css/extend/table/datatables.bootstrap.css" rel="stylesheet" type="text/css" />
<style>
.tmain{
width:99%;
margin:0 auto;
}
.smain{
	width: 100%;
}
.smain > tbody > tr{
	height: 40px;
}
.smain-td-30{
	width: 30%;
}
.smain-td-5{
	width: 5%;
}
.smain-td-15{
	width: 15%;
}
/* dataTables表头居中 */
.table>thead:first-child>tr:first-child>th{
        text-align:center;
}
.table > tbody > tr > td{
    vertical-align: middle;
}
.panel-heading-cus{
	padding:5px 15px 0px 15px;
}
</style>