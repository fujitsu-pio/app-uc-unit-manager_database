<%@page contentType="text/html; charset=UTF-8" %>
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
<%@ page import="com.fujitsu.dc.gui.PCSEnvironment"%>
<script type="text/javascript" src="../../js/jquery.js"></script>
<script type="text/javascript" src="../../js/main/validation/login_validation.js"></script>
<link href="../../css/homeStylesheet.css" rel="stylesheet" type="text/css">
<link href="../../css/layoutStylesheet.css" rel="stylesheet"
	type="text/css">
	<link href="../../css/japaneseStylesheet.css" rel="stylesheet"
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
			<div class="leftSection japaneseFont" style="cursor: default;">富士通クラウドのPaaSペルソナコンテナ</div>
			<div class="rightSection positionUserName">
				<div class="fujitsuIcon"></div>
			</div>
		</header>
	</div>
	
	<!-- TERMS AND CONDITIONS DIV: START -->
	<div id="divTermsAndConditionsFirstTime" style="display: block;">
		<div class="adminMgmtTxt Heading1 japaneseFont" style="margin-top: -70px;">利用規約</div>
			<input type="hidden" name="CSRFTokenResetLastLogin" value='<%=session.getAttribute("requestId")%>' id = "CSRFTokenResetLastLogin">
			<input type="hidden" name="" value='<%=session.getAttribute("firstTimeUserFullName")%>' id = "FirstTimeUSerFullNameHidden">
			<div class="divMainSignUp divTermsConditions">
										<textarea id="dvTermsAndCondition" rows="" cols="" class="txtAreaTermsAndConditions termsAndConditions" spellcheck="false"
											readonly="readonly"></textarea>
									</div>
		<!-- <div id="dvTermsAndCondition" class="divMainSignUp divTermsConditions">
		

		</div>
 -->		<section style="margin-top: 46px;" class="divLogoutBtn">
			<input type="button" value="同意して終了" class="btnCancelPopup japaneseFont"
				onclick="uLoginValidation.redirectLoginPage();" style="width: 165px;"> <input type="button"
				value="同意して進んでください" class="redButton btnAgreeAndProceed japaneseFont"
				id="btnChangePassword" style="width:165px;"
				onclick="uLoginValidation.agreeAndProceed();">
		</section>
	</div>
	<!-- TERMS AND CONDITIONS DIV: END -->
	
	<!-- RESET PASSWORD DIV: START -->
	<div id="divResetPassword" style="display: none;">
		<div class="adminMgmtTxt Heading1 resetPasswordText" style="color: #b1b1b1;">
			<label class="japaneseFont">歓迎&nbsp;</label><label id ="lblFirstTimeUserFullName"></label>
			<div class="resetPasswordProceedText japaneseFont">進むには、あなたの一時的なパスワードをリセットしてください。</div>
			<div style="margin-top: 42px;">
			<input type="hidden" name="CSRFTokenFirstTimeResetPassword" value='<%=session.getAttribute("requestId")%>' id = "CSRFTokenFirstTimeResetPassword">
				<input type="password" autocomplete="off"
					placeholder="仮パスワード" name="passwd" id="tempPassword"
					class="txtPassword placeholderColor"
					onfocus="this.placeholder = ''"
					onblur="this.placeholder = '仮パスワード'" /> <span
					class="newLoginAlertMessage"><aside id="spanTempPassword" class="japaneseFont"
						style="width: 100%"></aside></span> <input type="password"
					autocomplete="off" placeholder="新しいパスワード" name="passwd"
					id="newPassword" class="txtPassword placeholderColor"
					onfocus="this.placeholder = ''"
					onblur="this.placeholder = '新しいパスワード'" /> <span
					class="newLoginAlertMessage"><aside id="spanNewPassword" class="japaneseFont"
						style="width: 100%"></aside></span> <input type="password"
					autocomplete="off" placeholder="新しいパスワードを再入力" name="passwd"
					id="newRetypePassword" class="txtPassword placeholderColor"
					onfocus="this.placeholder = ''"
					onblur="this.placeholder = '新しいパスワードを再入力'" /> <span
					class="newLoginAlertMessage"><aside id="spanNewRetypePassword" class="japaneseFont"
						style="width: 100%"></aside></span>
				<section class="divLogoutBtn" style="margin: 0px;float: left;">
					<input type="button" value="パスワードのリセット" class="redButton resetPassword japaneseFont"
						id="btnChangePassword" onclick="uLoginValidation.firstTimePasswordReset();">
				</section>
			</div>
		</div>
	</div>
	<!-- RESET PASSWORD DIV: END -->
</body>
</html>