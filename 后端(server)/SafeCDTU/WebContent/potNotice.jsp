<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<meta name="viewport" 
		content="width=device-width,initial-scale=1">
		<title></title>
		
		
		<!-- 百度编辑器 -->
	    <script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath }/umeditor/ueditor.config.js"></script>
	    <script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath }/umeditor/ueditor.all.min.js"> </script>
    	<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath }/umeditor/lang/zh-cn/zh-cn.js"></script>
	</head>
	<body>
	<c:if test="${msg!=null }">
		<script type="text/javascript">
			var msg="${msg}"
			alert(msg);
			window.history.back();
		</script>
	</c:if>
	
		
		
		
		
			
			<article>
			<div class="Edit">
			<form action="context/potNotice" method="post"> 
				<div class="Edit_Table">
					<div class="Edit_Title_Box">
					<div class="border-input">
					<%-- title  JHNdawdFdawdSEHFdawdawfsFCSFESNU--%>	
						<input name="JHNdawdFdawdSEHFdawdawfsFCSFESNU" class="Edit_Title" type="text" placeholder="请在此处输入标题" value="${context.title }"/>
					</div>
				  <div class="border-btn">
				    <input id="submitArticle" class="SubmitArticle FontLarge" type="submit" value="发布"/>
				  </div>
				
				</div>
				
					<div id="userInfo" class="params">
				<%-- uId  FSEdfsfKfsefseFGfsfNESGfsefNfseFEJS--%>	
				<input type="text" value="<%=request.getParameter("FSEdfsfKfsefseFGfsfNESGfsefNfseFEJS") %>" name="FSEdfsfKfsefseFGfsfNESGfsefNfseFEJS"><%=request.getParameter("FSEdfsfKfsefseFGfsfNESGfsefNfseFEJS") %></input>
				
				<%-- depId or areId FSEdawdaKdwadFHNSdawdEUdawHF--%>	
				<input type="text" value="<%=request.getParameter("FSEdawdaKdwadFHNSdawdEUdawHF")%>" name="FSEdawdaKdwadFHNSdawdEUdawHF"><%=request.getParameter("FSEdawdaKdwadFHNSdawdEUdawHF")%></input>
					</div>
					
					<%-- content  HBFJfsefsSfsefEGFBfsefHfseffseJSE--%>	
					<textarea name="HBFJfsefsSfsefEGFBfsefHfseffseJSE" id="editor" class="Edit_content" type="text/plain">${context.content }</textarea>
				</div>
				</form>
				<script>UE.getEditor('editor');</script>
			</div>
			</article>
		
		
	</body>
</html>
<style media="screen">
 .params{
	display:none;
} 
  article{
   margin: 0px 10%;
  }
  .border-input{
    width: 100%;
    display: table;
    height: 37px;
  }
  .border-input{
    display: table-cell;
  }
  .Edit_Title{
    box-sizing: border-box;
    width: 100%;
    height: 100%;
    border: 1px solid gray;
    border-top-left-radius: 3px;
    border-bottom-left-radius: 3px;
    padding: 5px 10px;
    font-size: 15px;
  }
  .Edit_content{
  	min-height: 600px;
  }
  .border-btn{
    display: table-cell;
    height: 100%;
  }
  .SubmitArticle{
    box-sizing: border-box;
    width: 80px;
    height: 100%;
    border: 0px;
    background: transparent;
    color: rgb(80, 80, 80);
    font-size: 16px;
    transition: all 0.3s;
  }
  .SubmitArticle:hover{
  	border-bottom:1px solid black;
  	border-right: 1px solid black;
  }
</style>