<%@page contentType="text/html; charset=UTF-8" %>
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
<link href="./css/homeStylesheet.css" rel="stylesheet" type="text/css">
<link href="./css/layoutStylesheet.css" rel="stylesheet"
	type="text/css">
<title>Personium Unit Manager</title>
<script type="text/javascript">
	$(function() {
		var emailVerificationStatus = '<%=session.getAttribute("emailVerified")%>';
		if (emailVerificationStatus == "VERIFICATION_MAIL_SUCCESS") {
			$("#mailMessageHeaderContent").text("Congratulations");
			$("#divMailHeader").removeClass("emailVerificationFailure");
			$("#mailMessageHeaderContent").css("color", "#e6e6e6");
			$("#divMailHeader").addClass("emailVerificationSuccess");
			var mailSuccessContent  = $("#mailSuccessContent").html();
			$("#divMailContentBody").append(mailSuccessContent);
		} else if (emailVerificationStatus == "VERIFICATION_MAIL_LINK_EXPIRE") {
			$("#mailMessageHeaderContent").text("Email Verification Link Expired");
			$("#divMailHeader").removeClass("emailVerificationSuccess");
			$("#divMailHeader").addClass("emailVerificationFailure");
			$("#mailMessageHeaderContent").css("color", "#c80000");
			var mailSuccessContent  = $("#mailLinkExpire").html();
			$("#divMailContentBody").append(mailSuccessContent);
		} else if (emailVerificationStatus == "VERIFICATION_MAIL_ALREADY_VERIFIED") {
			$("#mailMessageHeaderContent").text("Your email address is already verified");
			$("#divMailHeader").removeClass("emailVerificationFailure");
			$("#divMailHeader").addClass("emailVerificationSuccess");
			$("#mailMessageHeaderContent").css("color", "#e6e6e6");
			var mailSuccessContent  = $("#mailAlreadyVerified").html();
			$("#divMailContentBody").append(mailSuccessContent);
		}
	});
</script>
</head>
<body>
	<div class="navigationBarService borderBottom ">
		<header class="topHeader">
			<div class="leftSection" style="cursor: default;">Personium Unit Manager</div>
			<div class="rightSection positionUserName">
				<div class="fujitsuIcon"></div>
			</div>
		</header>
	</div>
	<div id="dvSignUpForm" class="divMainSignUp"
				style="display: block; margin-top: 10%;background-color: #f6f6f6;">
				<div style="border-bottom: 1px solid #e6e6e6; height: 70px" id="divMailHeader">
				<div id="mailMessageHeaderContent" style="font-family: Segoe UI;font-size: 16pt;color: #e6e6e6;text-align: center;padding-top: 19px;"></div>
				</div>
				<div id="divMailContentBody" style="font-family:Segoe UI; font-size: 14px;color: #1b1b1b;padding: 16px;background-color: #f6f6f6">
				
				</div>
				</div>
	<div style="display: none;" id="mailSuccessContent">
	Your email has been verified. Before you can access an administrator has to approve your account.
				<br>
				You will get a notification email with login instructions once your account has been approved.
				<br>
				<br>
				Generally it takes 3-4 business days to get the account approval email. In case you do not receive the
				<br>
				approval email in 3-4 business days, please get in touch with our sales representative or concerned
				<br>
				account manager.
	</div>
	<div style="display: none;" id="mailLinkExpire">
	Sorry the email verification link has expired. Email verification is a mandatory step of our account
				<br>
				creation process. Please get in touch with our local sales representative or the concerned account
				<br>
				manager to get the email verification notification again.
	</div>
	<div style="display: none;" id="mailAlreadyVerified">
	Your email address is already verified. After verification it takes 3-4 days for the administrator to 
				<br>
				activate the account. Once your account is activated an email with login instructions will be sent to you. 
				<br>
				<br>
				If you verified your email address 3-4 business days ago and still you have not received the
				<br>
				approval email with login instructions, please get in touch with our sales representative.
	</div>
</body>
</html>