<%@page session="false" contentType="text/html;charset=UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
<html>
  <head>
  <%@ include file="/include/metalib.jsp"%>
    <title></title>
  </head>
  <script type="text/javascript">
  	 $(document).ready(function() {
  		var loginUrl= '${_link}';
  		if(loginUrl!=""){
  			window.location.href=loginUrl;
  		};
  	});
  </script>
  <body>
    正在切换页面...
  </body>
</html>