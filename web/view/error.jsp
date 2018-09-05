<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/include/taglib.jsp"%>
<%@ include file="/include/metalib.jsp"%>
<html>
<head>
<script language="javascript">
function cbOnBeforeLoad() {}

function flipInfoDiv() {
	var oDiv = document.getElementById("idDiv");
	oDiv.style.display = oDiv.style.display == "none" ? "block" : "none";
}
</script>
<title></title>
</head>
<body>
<div class="layout_page">
	<!-- Content Start -->
	<table width="100%" border="0" cellspacing="0" cellpadding="0" >
	 <tr>
	  <td >&nbsp;
	   <table width="100%" border="0" cellspacing="0" cellpadding="0">
	    <tr valign="top">
	     <td width="140" rowspan="6"><img src="/images/icon/error.gif" width="139" height="94"></td>
	     <td > Sorry. there is unexpected error occurred. </td>
	    </tr>
	    <tr valign="top">
	     <td >&nbsp;</td>
	    </tr>
	    <tr valign="top">
	     <td class="subtitle_red">unexpected error occurred.</td>
	    </tr>
	    <tr valign="top">
	     <td >&nbsp;</td>
	    </tr>
	    <tr valign="top">
	     <td class="3depth_title">Contact Information or Troubleshooting Tips</td>
	    </tr>
	   </table></td>
	 </tr>
	 <tr>
	  <td width="98%" height="14" ></td> <!-- 832 -->
	 </tr>
	 <tr>
	  <td height="1" class="bg_gray_a"></td>
	 </tr>
	 <tr>
	  <td height="2" class="bg_gray_b"></td>
	 </tr>
	 <tr>
	  <td height="7"></td>
	 </tr>
	 <tr align="right">
	  <td height="7">
	  	<input name="submit2322" type="button" value="&lt;  返回" onclick="history.back()">
	  	<input name="submit2322" type="button" value="详细" onclick="flipInfoDiv()">
	  	<input name="submit2322" type="button" value="刷新" onclick="location.reload()">
	  	</td>
	 </tr>
	</table>
	<BR>
	<div id="idDiv" style="display:none; width: 100%" class="table_layout">
    <table width="100%" border="0" cellspacing="1" cellpadding="0"  class="table_line_complex">
      <colgroup>
      <col width="100"/>
      <col width="295"/>
      <col width="100"/>
      <col /> <!-- 295 -->
      </colgroup>
      <tr>
        <td class="table_header_complex_d">Server</td>
        <td class="table_padding"></td>
        <td class="table_header_complex_d">Client</td>
        <td class="table_padding"></td>
      </tr>
      <tr>
        <td class="table_header_complex_d">Status Code</td>
        <td class="table_padding">500 (unexpected error)</td>
        <td class="table_header_complex_d">Date  Time</td>
        <td class="table_padding"></td>
      </tr>
      <tr>
        <td class="table_padding" colspan="4">
		  <pre ID=oStackTrace STYLE="background-color:#EAEAEA;height: 230px; overflow: scroll;">
		  <ww:property value="top.getExceptionStack()" />
		  </pre>
        </td>
      </tr>
    </table>
  </div>
</div>
</body>
</html>

