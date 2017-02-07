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
<%@ page import="io.personium.gui.PersoniumEnvironment"%>
<script type="text/javascript" src="../../js/jquery.js"></script>
<script type="text/javascript" src="../../js/main/validation/login_validation.js"></script>
<link href="../../css/homeStylesheet.css" rel="stylesheet" type="text/css">
<link href="../../css/layoutStylesheet.css" rel="stylesheet"
	type="text/css">

<script type="text/javascript">
var selectedLanguage = '<%=session.getAttribute("selectedLanguage")%>';
	sessionStorage.firstTimeUserSelectedLanguage = selectedLanguage;
	var uiProps;
	function getUiProps() {
		return uiProps;
	}

	function setUiProps(props) {
		uiProps = props;
	}
	var firstTimeUserFullName = null;
	$(function() {
		$.getJSON('../../' + sessionStorage.firstTimeUserSelectedLanguage
				+ '_uiProperties.json', function(props) {
			setUiProps(props);
			uLoginValidation.readTermsAndConditionFile(
					'../../TermsAndConditions.txt', function(callback) {
						$("#dvTermsAndCondition").text(callback);
					});
		});
		firstTimeUserFullName = document.getElementById("FirstTimeUSerFullNameHidden").value;
		firstTimeUserFullName = firstTimeUserFullName.replace(/↔/g, "'");
	});

var firstTimeUserContextRoot = '<%=session.getAttribute("contextRoot")%>';
<%-- var firstTimeUserFullName = '<%=session.getAttribute("firstTimeUserFullName")%>'; --%>
var firstTimeUserName = '<%=session.getAttribute("id")%>';
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
	
	<!-- TERMS AND CONDITIONS DIV: START -->
	<div id="divTermsAndConditionsFirstTime" style="display: block;">
		<div class="adminMgmtTxt Heading1" style="margin-top: -70px;">Terms
			& Conditions</div>
			<input type="hidden" name="CSRFTokenResetLastLogin" value='<%=session.getAttribute("requestId")%>' id = "CSRFTokenResetLastLogin">
			<input type="hidden" name="" value='<%=session.getAttribute("firstTimeUserFullName")%>' id = "FirstTimeUSerFullNameHidden">
			<div class="divMainSignUp divTermsConditions">
										<textarea id="dvTermsAndCondition" rows="" cols="" class="txtAreaTermsAndConditions termsAndConditions" spellcheck="false"
											readonly="readonly"></textarea>
									</div>
		<!-- <div id="dvTermsAndCondition" class="divMainSignUp divTermsConditions">
		

		</div>
 -->		<section style="margin-top: 46px;" class="divLogoutBtn">
			<input type="button" value="Disagree and Exit" class="btnCancelPopup"
				onclick="uLoginValidation.redirectLoginPage();" style="width: 125px;"> <input type="button"
				value="Agree and Proceed" class="redButton btnAgreeAndProceed"
				id="btnChangePassword"
				onclick="uLoginValidation.agreeAndProceed();">
		</section>
	</div>
	<!-- TERMS AND CONDITIONS DIV: END -->
	
	<!-- RESET PASSWORD DIV: START -->
	<div id="divResetPassword" style="display: none;">
		<div class="adminMgmtTxt Heading1 resetPasswordText" style="color: #b1b1b1;">
			<label>Welcome&nbsp;</label><label id ="lblFirstTimeUserFullName"></label>
			<div class="resetPasswordProceedText">To proceed,
				please reset your temporary password.</div>
			<div style="margin-top: 42px;">
			<input type="hidden" name="CSRFTokenFirstTimeResetPassword" value='<%=session.getAttribute("requestId")%>' id = "CSRFTokenFirstTimeResetPassword">
				<input type="password" autocomplete="off"
					placeholder="Temporary Password" name="passwd" id="tempPassword"
					class="txtPassword placeholderColor"
					onfocus="this.placeholder = ''"
					onblur="this.placeholder = 'Temporary Password'" /> <span
					class="newLoginAlertMessage"><aside id="spanTempPassword"
						style="width: 100%"></aside></span> <input type="password"
					autocomplete="off" placeholder="New Password" name="passwd"
					id="newPassword" class="txtPassword placeholderColor"
					onfocus="this.placeholder = ''"
					onblur="this.placeholder = 'New Password'" /> <span
					class="newLoginAlertMessage"><aside id="spanNewPassword"
						style="width: 100%"></aside></span> <input type="password"
					autocomplete="off" placeholder="Retype New Password" name="passwd"
					id="newRetypePassword" class="txtPassword placeholderColor"
					onfocus="this.placeholder = ''"
					onblur="this.placeholder = 'Retype New Password'" /> <span
					class="newLoginAlertMessage"><aside id="spanNewRetypePassword"
						style="width: 100%"></aside></span>
				<section class="divLogoutBtn" style="margin: 0px;float: left;">
					<input type="button" value="Reset Password" class="redButton resetPassword"
						id="btnChangePassword" onclick="uLoginValidation.firstTimePasswordReset();">
				</section>
			</div>
		</div>
	</div>
	<!-- RESET PASSWORD DIV: END -->
</body>
</html>