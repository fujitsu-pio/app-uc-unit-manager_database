<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<!--
Personium
Copyright 2016 FUJITSU LIMITED

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
-->
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="./js/jquery-1.9.0.min.js"></script>
<script type="text/javascript" src="./js/main/validation/login_validation.js"></script>
<link href="./css/homeStylesheet.css" rel="stylesheet" type="text/css">
<link href="./css/layoutStylesheet.css" rel="stylesheet"
	type="text/css">
<title>Personium Unit Manager</title>
<script type="text/javascript">
var forgotPassContextRoot = '<%=application.getContextPath()%>';
</script>
</head>
<body>
	<div class="navigationBarService borderBottom ">
		<header class="topHeader">
			<div class="leftSection" style="cursor: default;">Fujitsu Cloud
				PaaS Persona Container</div>
			<div class="rightSection positionUserName">
				<div class="fujitsuIcon"></div>
			</div>
		</header>
	</div>
	

	
	<!-- RESET PASSWORD DIV: START -->
	<div id="divForgotPassword" style="display: block;">
		<div class="adminMgmtTxt Heading1 resetPasswordText" style="color: #b1b1b1;">
			<label>Forgot Password&nbsp;</label><!-- <label id ="lblFirstTimeUserFullName"></label> -->
			<div id="dvForgotPasswordInput">
			<div class="resetPasswordProceedText">To reset your password, enter the username you use to login to Personium Unit Manager.</div>
			<div style="margin-top: 42px;">
			<input type="hidden" name="CSRFTokenForgotPassword" value='<%=application.getInitParameter("TOKEN")%>' id = "CSRFTokenForgotPassword">
				<input type="text" autocomplete="off"
					placeholder="Username" name="passwd" id="txtboxForgotPassword"
					class="txtPassword placeholderColor"
					onfocus="this.placeholder = ''"
					onblur="this.placeholder = 'Username'" /> <span
					class="newLoginAlertMessage"><aside id="spanForgotPassword"
						style="width: 100%"></aside></span> 
				<section class="divLogoutBtn" style="margin: 0px;float: left;">
					<input type="button" value="Submit" class="redButton resetPassword"
						id="btnChangePassword" onclick="uLoginValidation.ForgetPasswordMail();">
				</section>
			</div>
			</div>
		</div>
	</div>
	<!-- RESET PASSWORD DIV: END -->
	
	<div id="dvForgotPasswordSuccess" class="divMainSignUp"
				style="display: none; margin-top: 19%;background-color: #f6f6f6;height: 71px;width: 536px;">
				
				<div id="divMailContentBody" style="font-family:Segoe UI; font-size: 14px;color: #1b1b1b; padding: 16px; padding-top: 0px; background-color: #f6f6f6">
				<div id="mailSuccessContent">
				
				<br>
				An email has been sent to your registered mail address containing new password.
				<br>
				<a href='<%=application.getContextPath()%>' style="font-size: 10pt;color: #c80000;display: block;text-align: center;">Please click here to re-login</a>
				
	</div>
				</div>
				</div>
				
				
</body>
</html>