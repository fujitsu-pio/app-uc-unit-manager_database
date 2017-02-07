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
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Expires" content="-1">
<%@ page import="io.personium.gui.PersoniumEnvironment"%>
<title>FUJITSU: Persona Container Portal</title>
<script type="text/javascript" src="../../js/jquery-1.9.0.min.js"></script>
<script type="text/javascript">
var jquery1_9_0 = jQuery.noConflict();
</script>
<script type="text/javascript" src="../../js/jquery.js"></script>
<script src="../../js/commonFunctions.js"></script>

<!-- <link href="../css/commonStylesheet.css" rel="stylesheet" type="text/css">
<link href="../css/homeStylesheet.css" rel="stylesheet" type="text/css">
<link href="../css/oDataStylesheet.css" rel="stylesheet" type="text/css"> -->
<link href="../../css/sprite1Stylesheet.css" rel="stylesheet" type="text/css">
<link href="../../css/layoutStylesheet.css" rel="stylesheet" type="text/css">
<link href="../../css/homeStylesheet.css" rel="stylesheet" type="text/css">
<link href="../../css/japaneseStylesheet.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../js/spin.js"></script>
<script type="text/javascript" src="../../js/main/core/DaoConfig.js"></script>
<script type="text/javascript" src="../../js/main/core/DcContext.js"></script>
<script type="text/javascript" src="../../js/main/util/extend.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<!-- <script type="text/javascript" src="../../js/odataCommon.js"></script> -->
<script type="text/javascript" src="../../js/timeZone.js"></script>
<script type="text/javascript" src="../../js/home.js"></script>
<script type="text/javascript" src="../../js/administratorManagement.js"></script>
<script type="text/javascript" src="../../js/editUserInformation.js"></script>
<!-- <script type="text/javascript" src="../js/informationStorage.js"></script> -->
<script type="text/javascript" src="../../js/uCell.js"></script>
<script type="text/javascript" src="../../js/createEnvironment.js"></script>
<script type="text/javascript" src="../../js/changePassword.js"></script>
<script type="text/javascript" src="../../js/unitManagement.js"></script>
<script>
var selectedLanguage = '<%=session.getAttribute("selectedLanguage")%>';
sessionStorage.selectedLanguage = selectedLanguage;

var contextRoot = '<%=session.getAttribute("contextRoot")%>';
sessionStorage.contextRoot = contextRoot;
var organizationID = '<%=session.getAttribute("organizationID")%>';
sessionStorage.organizationID = organizationID;
var userID = '<%=session.getAttribute("id")%>';
sessionStorage.userID = userID;
var requestId = '<%=session.getAttribute("requestId")%>';
sessionStorage.requestId = requestId;
function addJavascript(jsname) {
	var th = document.getElementsByTagName('head')[0];
	var s = document.createElement('script');
	s.setAttribute('type','text/javascript');
	s.setAttribute('src',jsname);
	th.appendChild(s);
}
var uiProps;
function getUiProps() {
	return uiProps;
}

function setUiProps(props) {
	uiProps = props;
}
$(function() {
		$.getJSON('../../'+sessionStorage.selectedLanguage+'_uiProperties.json', function(props) {
			setUiProps(props);
			uHome.setNavigationGap();
			uHome.hoverEffectNavigationBar();
			uHome.loadHomePage();
			uHome.setDynamicServiceManagementGridtHeight();
			setRightContainerHomeWidth();
			$("#btnDeleteEnvironment").click(function() {
				uHome.deleteEnvironment();
			});
			$(".header").load(contextRoot+'/htmls/'+sessionStorage.selectedLanguage+'/header.jsp');
		});
});

$(window).resize(function () {
	uHome.setNavigationGap();
	uHome.setDynamicServiceManagementGridtHeight();
	uHome.setDynamicWidthForDomainHeaderContents();
	uHome.setDynamicWidthForUnitHeaderContents();
	uHome.setDynamicOrgNameWidth();
});

</script>
</head>

<body>
	<div id="homeMainContainer" id="homePage">
		<!-- Header Starts -->
		<div class="header">
			
		</div>
		<!-- Header Ends -->
			<!-- <div style="height:44px;" class="navigationBarService borderBottom ">
		  <div id="serviceMgmtWrapper" class="serviceMgmtWrapper selected" style="margin-right: 59px;" onclick="uHome.clickEnvironmentManagement();">
				<div class="serviceMgmtIconRed" id="serviceMgmtIcon"></div>
				<div class="serviceMgmtTxtRed" id="serviceMgmtTxt">Service Unit</div>
			</div>
			<div id="adminMgmtWrapper" class="adminMgmtWrapper" onclick="uHome.clickAdministratorManagement();">
				<div class="adminMgmtIconGrey" id="adminMgmtIcon"></div>
				<div class="adminMgmtTxtGrey" id="adminMgmtTxt">Administrator</div>
			</div>  
		</div> -->
		<!-- <div id="serviceManagmentHeader"class="Heading1" style="margin: 25px 0px 25px 20px;">Service Management</div> -->
		<div id="homePageRightContainer"></div>
		<div id="unitContent" style="margin-right: 8px;height: 554px">
		</div>
	</div>
	<!-- Content Container Ends -->

	<!-- /********************* CREATE ENVIRONMENT : START **********************/ -->
	<div id="createEnvironmentModal" class="modelback"
		style="display: none;">
		<div class="modal-window block-border"
			style="display: block; height: 213px; width: 410px; background: #ffffff"
			id="createEnvironmentDialog">
			<div class="popupWrapper" id="createEnvironmentPopup">
				<a href="#" title="Close" id="modalbox-close" class="closeIcon"
					onclick="closeEntityModal('#createEnvironmentModal')">Close</a>
				<header class="popupHeader" style="width: 400px;"> Create
					Environment </header>
				<div class="popupContent">
					<section class="popupInnerContent">
						<form>
							<input type="hidden" name="CSRFTokenCreateEnvironment" value="" id = "CSRFTokenCreateEnvironment">
							<table width="100%" border="0" class="popupCommonTable">
								<tr>
									<td width="40%" valign="top"><label>Target</label></td>
									<td width="60%" align="left"><select id="ddTarget"
										class="dropDownBigger">
											<!-- <option value="Select">Select</option> -->
									</select> <span class="popupAlertArea">
											<aside class="popupAlertmsg popUpErrMsgEnvironment"
												id="popupTargetErrorMsg"></aside>
									</span></td>
								</tr>
								<tr>
									<td width="40%" valign="top"><label>Environment
											Name</label></td>
									<td width="60%" align="left"><input type="text" name=""
										id="txtEnvironmentName" value="" class="textBoxPopup">
										<span class="popupAlertArea">
											<aside class="popupAlertmsg popUpErrMsgEnvironment"
												id="popupEnvtNameErrorMsg"></aside>
									</span></td>
								</tr>
							</table>
						</form>
					</section>
					<section class="bottomBtn">
						<input type="submit" value="Cancel" class="normalButtonGrey"
							onclick="closeEntityModal('#createEnvironmentModal')" /> <input
							type="submit" value="Create Environemnt"
							id="btnCreateEnvironment"
							class="normalButtonBlue biggestBlueButton"
							onclick="uCreateEnvironment.clickCreateEnvironment();" />
					</section>
				</div>
			</div>
		</div>
	</div>
	<!-- /********************* CREATE ENVIRONMENT : END **********************/ -->

	<!-- /********************* CHANGE PASSWORD : START **********************/ -->
	<div id="changePasswordModal" class="modelback"
		style="display: none;">
		<div class="modal-window block-border"
			style="display: block; height: 383px; width: 494px; background: #ffffff"
			id="changePasswordDialog">
			<div style="height: 231px; width: 494px;" id="changePasswordPopup">
			<div id="dvCloseIcon" class="crossDeletePropIcon" style="margin-top: 18px;margin-right: 17px;"
				onclick="closeEntityModal('#changePasswordModal')" tabindex="6" title="クローズ"></div>
				<div class="accessTokenDiv" style="padding-bottom: 15px">
				<label class="AccessTokenHeading japaneseFont">
					パスワードを変更する</label>
			</div>
				<div class="divPopupInner">
					<section class="sectionContent"
						style="height: 173px; width: 320px; margin-left: 58px;">
						<form onsubmit="return false;">
						<input type="hidden" name="CSRFToken" value="" id = "CSRFTokenChangePassword">
							<table>
								<tr>
									<td style="padding-bottom: 5px;"><label class="lblElement japaneseFont">* 現在のパスワード</label></td>
								</tr>
								<tr>
									<td><input type="password" id="txtCurrentPassword"
										onblur="uChangePassword.validateCurrentPassword();"
										class="bigTextBoxCollection" autocomplete="off" tabindex="1"> <span
										class="spanPopupErrorMessageCommon" style="margin-bottom:6px;width:295px;">
											<aside class="asidePopupErrorMessageCommon japaneseFont"
												id="popupCurrPwdErrorMsg"></aside>
									</span></td>
								</tr>
								<tr>
									<td style="padding-bottom: 5px;"><label class="lblElement japaneseFont">* 新しいパスワード</label></td>
								</tr>
								<tr>
									<td><input type="password" id="txtNewPassword"
										onblur="uChangePassword.validateNewPassword();"
										onfocus="uChangePassword.validateCurrentPassword();"
										class="bigTextBoxCollection" autocomplete="off" tabindex="2"> <span
										class="spanPopupErrorMessageCommon" style="margin-bottom:6px;width:295px;">
											<aside class="asidePopupErrorMessageCommon japaneseFont"
												id="popupNewPwdErrorMsg"></aside>
									</span></td>
								</tr>
								<tr>
									<td style="padding-bottom: 5px;"><label class="lblElement japaneseFont">* パスワードを照合する</label></td>
								</tr>
								<tr>
									<td><input type="password" id="txtVerifyPassword"
										onblur="uChangePassword.validateVerifyPassword();"
										onfocus="uChangePassword.validateNewPassword();"
										class="bigTextBoxCollection" autocomplete="off" tabindex="3"> <span
										class="spanPopupErrorMessageCommon" style="margin-bottom:6px;width:295px;">
											<aside class="asidePopupErrorMessageCommon japaneseFont"
												id="popupVerPwdErrorMsg"></aside>
									</span></td>
								</tr>
							</table>
						</form>
					</section>
					<section style="float: right;margin-top:78px;" class="divLogoutBtn japaneseFont" >
						<input type="button" value="キャンセル" class="btnCancelPopup japaneseFont"
							onclick="closeEntityModal('#changePasswordModal')" tabindex="4"> <input
							type="button" value="保存" class="btnSavePopup japaneseFont" style="margin-right:0px;" id="btnChangePassword"
							onclick="uChangePassword.clickChangePassword();" tabindex="5">
					</section>
				</div>
			</div>
		</div>
	</div>
	<!-- /********************* CHANGE PASSWORD : END **********************/ -->
	<!-- /********************* EDIT USER INFORMATION : START **********************/ -->
	<div id="editUserInfoModal" class="modelback" style="display: none;">
		<div class="modal-window block-border"
			style="display: block; height: 374px; width: 706px; background: #ffffff"
			id="editUserInfoDialog">
			<div id="dvCloseIcon" class="crossDeletePropIcon"
				onclick="closeEntityModal('#editUserInfoModal')" tabindex="8" title="クローズ"></div>
			<div class="accessTokenDiv" style="padding-bottom: 15px">
				<label class="AccessTokenHeading japaneseFont">
					私の情報を編集する</label>
			</div>
			<div id="editUserInfoPopup">
			 <input type="hidden" name="CSRFToken" value="" id = "CSRFToken">
				<table cellpadding='0' cellspacing='0' border='0' class="popupcreatePropertyTable">
					<tr>
						<td id="idEditUserName" style="width: 336px"><div style="margin-bottom: 10px;">
								<label class="lblElement japaneseFont">* ユーザー名</label>
							</div>
							<div>
								<input type="text" class="bigTextBox propertyPopUpTextBox" id = "editPopupUsername" disabled="disabled" tabindex="1"> <span
									class="spanPopupErrorMessageCommon">
									<aside class="asidePopupErrorMessageCommon japaneseFont"
										id="popupEditUserNameErrorMsg"></aside>
								</span>
							</div></td>
						<td id="idEditFamilyName">
						<div style="margin-bottom: 10px;">
								<label class="lblElement japaneseFont">* メール</label>
							</div>
							<div>
								<input type="text" class="bigTextBox propertyPopUpTextBox" id = "txtEditEmail" onblur="uEditUserInformation.validateEditEmail();" tabindex="4"> <span
									class="spanPopupErrorMessageCommon">
									<aside class="asidePopupErrorMessageCommon japaneseFont"
										id="popupEditEmailErrorMsg"></aside>
								</span>
							</div>
						</td>
					</tr>
					<tr>
						<td id="idEditFirstName" style="width: 336px"><div style="margin-bottom: 10px;">
								<label class="lblElement japaneseFont">* ファーストネーム</label>
							</div>
							<div>
								<input type="text" class="bigTextBox propertyPopUpTextBox" id = "txtEditFirstName" onblur="uEditUserInformation.validateEditFirstName();" tabindex="2"> <span
									class="spanPopupErrorMessageCommon">
									<aside class="asidePopupErrorMessageCommon japaneseFont"
										id="popupEditFirstNameErrorMsg"></aside>
								</span>
							</div></td>
						<td id="idEditEmail"><div style="margin-bottom: 10px;">
								<label class="lblElement japaneseFont">* 再入力Eメール</label>
							</div>
							<div>
								<input type="text" class="bigTextBox propertyPopUpTextBox" id = "txtEditReEnterEmail" onblur="uEditUserInformation.validateEditReEnterEmail();" tabindex="5"> <span
									class="spanPopupErrorMessageCommon">
									<aside class="asidePopupErrorMessageCommon japaneseFont"
										id="popupEditReEnterEmailErrorMsg"></aside>
								</span>
							</div></td>
					</tr>
					<tr>
						<td id="idEditFirstName" style="width: 336px"><div style="margin-bottom: 10px;">
								<label class="lblElement japaneseFont">* 苗字</label>
							</div>
							<div>
								<input type="text" class="bigTextBox propertyPopUpTextBox" id = "txtEditFamilyName" onblur="uEditUserInformation.validateEditFamilyName();" tabindex="3"> <span
									class="spanPopupErrorMessageCommon">
									<aside class="asidePopupErrorMessageCommon japaneseFont"
										id="popupEditFamilyNameErrorMsg"></aside>
								</span>
							</div></td>
						<td id="idEditEmail"></td>
					</tr>	
							
				</table>
			</div>

			<!-- Button Section Starts -->
			<section class="positionButtons" style="margin-top: 13px;">
				<div id="dvCreate" class="dvCreate">
					<input type="button" id="btnChangePassword" class="btnClose japaneseFont"
						value="保存" onclick="uEditUserInformation.editUserInfo();" tabindex="7">
				</div>
				<div id="dvCancel" class="dvCreate">
					<input type="button" id="btnCancel"
						onclick="closeEntityModal('#editUserInfoModal')"
						class="cancelBtn japaneseFont" value="キャンセル" tabindex="6">
				</div>
			</section>
			<!-- Button Section Ends -->
		</div>
	</div>
		<!-- /********************* EDIT USER INFORMATION : END **********************/ -->
	<div id="spinner"></div>

	<!-- Environment Delete Pop Up Start-->
	
		<div id="environmentDeleteModalWindow" class="modelback"
		style="display: none;">
		<div class="modal-window block-border"
			style="display: block; height: 175px; width: 375px; background: #ffffff"
			id="environmentDeleteDialogBox">
			<div class="popupWrapper" id="environmentDeletepopup">
				<a href="#" title="Close" id="modalbox-close" class="closeIcon"
					onclick="closeEntityModal('#environmentDeleteModalWindow');">Close</a>
				<header class="popupHeader">Delete Environment</header>
				<div class="popupContent">
					<section class="popupInnerContent">
						<aside class="deleteAccount">Do you want to delete selected environment ?
							 </aside>
					</section>
					<section class="bottomBtn">

						<input type="button" value="Cancel"
							onclick="closeEntityModal('#environmentDeleteModalWindow');"
							class="normalButtonGrey" /> <input type="button" id ="btnDeleteEnvironment"value="Ok"
							 class="normalButtonBlue" />
					</section>
				</div>
			</div>
		</div>
	</div>
	
	<!-- Environment Delete Pop Up End -->
	<!-- Session Expiration Pop Up Start-->
	<div id="timeOutModalWindow" class="modelback"
		style="display: none;">
		<div class="modal-window block-border"
			style="display: block; height: 242px; width: 466px; background: #ffffff"
			id="timeOutDialogBox">
			<section class="headerSection">
			<div id="dvCloseIcon" class="closeIconDiv">
				<a style="margin:0px;" href="#" title="クローズ" id="modalbox-close" class="popupCloseIcon"
					onclick="objCommon.redirectLoginPage();" tabindex="2"></a>
			</div>
			<div class="sessionExpiryDiv" id="dvAccessheader">
				<label class="sessionExpiredPopUpHeading japaneseFont" id="lblHeadingAccessToken">
					セッションが期限切れ</label>
			</div>
			</section>
			<div  class="sessionExpiredLabel">
				<label class="japaneseFont">あなたのセッションの有効期限が切れています。もう一度ログインしてください。 </label>
			</div>
		 	<div id="dvClose" class="sessionPopUpCloseSection">
				<input type="button" id="btnTimeout" class="normalButton japaneseFont" onclick="objCommon.redirectToLoginPageOnServerExpiration();"  value="よし" tabindex="1">
			</div>
			</div>
		</div>
	<!-- Session Expiration Pop Up End -->
	
	<!-- /********************* LOGOUT POPUP : START **********************/ -->
	<!-- <div id="logOutModalWindow" class="modelback" style="display: none;">
		<div class="modal-window block-border"
			style="display: block; height: 194px; width: 400px; background: #ffffff"
			id="logOutDialogBox">
			<figure style="" id="logoutDiv" class="imgLogoutSuccess">
			<label class="lblLogoutMsg">
			You have logged out successfully !
			</label>
			<figcaption></figcaption>
			</figure>
			<div class="divLogoutBtn">
				<input type="button" class="btnLogoutOk" onclick="objCommon.redirectLoginPage();" value="Ok"/>
			</div>
		</div>
	</div> -->
	<!-- /********************* LOGOUT POPUP : END **********************/ -->
	
	<!-- LOGOUT POPUP : START -->
	<div id="logOutModalWindow" class="modelback"
		style="display: none;">
		<div class="modal-window block-border"
			style="display: block; height: 202px; width: 460px; background: #ffffff"
			id="logOutDialogBox">
			<section style="margin-bottom: 5px;" class="headerSection">
			<div id="dvCloseIcon" class="closeIconDiv">
				<a style="margin:0px;" title="クローズ" id="modalbox-close" class="popupCloseIcon"
					onclick="closeEntityModal('#logOutModalWindow');" tabindex="3"></a>
			</div>
			<div class="sessionExpiryDiv" id="dvAccessheader">
				<label class="sessionExpiredPopUpHeading japaneseFont" id="lblHeadingAccessToken">
					ログアウト</label>
			</div>
			</section>
			<div  class="sessionExpiredLabel">
				<label class="japaneseFont">あなたがログアウトしてもよろしいですか？ </label>
			</div>
				<!-- Button Section Starts -->
			<section class="positionButtons" style="margin-top:0px;">
				<div id="dvCreate" class="dvCreate">
					<input type="button"onclick="clickLogout();"  class="btnClose japaneseFont"
						value="はい" tabindex="2">
				</div>
				<div id="dvCancel" class="dvCreate">
					<input type="button" id="btnCancelLogout"
						onclick="closeEntityModal('#logOutModalWindow')"
						class="cancelBtn japaneseFont" value="ノー" tabindex="1">
				</div>
			</section>
			<!-- Button Section Ends -->
			</div>
		</div>
	<!-- LOGOUT POPUP : END -->
</body>
</html>
