/**
 * Personium
 * Copyright 2016 FUJITSU LIMITED
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
function administratorManagement() {
}

$(function() {
	$.ajaxSetup({ cache : false });
});

var uAdministratorManagement = new administratorManagement();

var sbSuccessfulUserDeleteCount = 0;
var sbSuccessfulUserActivateCount = 0;
var arrDeletedUserName = [];
var arrSuccessfulDeletedUsernames = [];
/**
 * This variable holds availability value for user name.
 */
administratorManagement.prototype.isUserNameValueAvailable = false;
/**
 * Administration Management modal pop up IDs.
 */
var editUserAdminMgmntModal = "editUserAdminMgmntModal";
var editUserAdminMgmntDialog = "editUserAdminMgmntDialog";
var changePassAdminMgmntModal = "changePassAdminMgmntModal";
var changePassAdminMgmntDialog = "changePassAdminMgmntDialog";
var userDeleteAdminMgmntModalWindow = "userDeleteAdminMgmntModalWindow";
var userDeleteAdminMgmntDialogBox = "userDeleteAdminMgmntDialogBox";

/* ***************** ADMINISTRATOR MANAGEMENT: CREATE USER: START ******************/

/**
 * The purpose of this function is to empty all field values of create user pop
 * up.
 */
administratorManagement.prototype.emptyCreateUserPopupFieldValues = function() {
	uAdministratorManagement.checkSessionExistence();
	var reuestID = sessionStorage.requestId;
	$("#CSRFTokenCreateUser").val(reuestID);
	$("#CSRFTokenSendMailVerification").val(reuestID);
	$("#txtCreateNewUserUserName").val('');
	$("#txtCreateNewUserPassword").val('');
	$("#txtCreateNewUserRetypePassword").val('');
	$("#txtCreateNewUserFirstName").val('');
	$("#txtCreateNewUserFamilyName").val('');
	$("#txtCreateNewUserEmail").val('');
	$("#txtCreateNewUserReEnterEmail").val('');
	$("#chkAdmin").attr('checked', false);
	document.getElementById("rdYes").checked = true;
	$('#popupUsernameErrorMsg').html('');
	$('#popupPasswordErrorMsg').html('');
	$('#popupRetypePasswordErrorMsg').html('');
	$('#popupFirstNameErrorMsg').html('');
	$('#popupFamilyNameErrorMsg').html('');
	$('#popupEmailErrorMsg').html('');
	$("#rdYes").attr('checked', true);
	$('#popupReEnterEmailErrorMsg').html('');
	$("#txtCreateNewUserUserName").removeClass('errorIcon');
	$("#txtCreateNewUserPassword").removeClass('errorIcon');
	$("#txtCreateNewUserRetypePassword").removeClass('errorIcon');
	$("#txtCreateNewUserFirstName").removeClass('errorIcon');
	$("#txtCreateNewUserFamilyName").removeClass('errorIcon');
	$("#txtCreateNewUserEmail").removeClass('errorIcon');
	$("#txtCreateNewUserReEnterEmail").removeClass('errorIcon');
	$("#txtCreateNewUserUserName").removeClass('validValueIcon');
	$("#txtCreateNewUserPassword").removeClass('validValueIcon');
	$("#txtCreateNewUserRetypePassword").removeClass('validValueIcon');
	$("#txtCreateNewUserFirstName").removeClass('validValueIcon');
	$("#txtCreateNewUserFamilyName").removeClass('validValueIcon');
	$("#txtCreateNewUserEmail").removeClass('validValueIcon');
	$("#txtCreateNewUserReEnterEmail").removeClass('validValueIcon');
};

/**
 * The purpose of this method is to show valid value icon in input text boxes.
 * @param txtID
 */
administratorManagement.prototype.showValidValueIcon = function (txtID) {
	$(txtID).removeClass("errorIcon");	
	$(txtID).addClass("validValueIcon");
};

/**
 * The purpose of this method is to show error value icon in input text boxes.
 * @param txtID
 */
administratorManagement.prototype.showErrorIcon = function (txtID) {
	$(txtID).removeClass("validValueIcon");
	$(txtID).addClass("errorIcon");	
};

/**
 * The purpose of this method is to show valid value icon in big input text boxes.
 * @param txtID
 */
administratorManagement.prototype.showValidValueBigIcon = function (txtID) {
	$(txtID).removeClass("errorIconBig");	
	$(txtID).addClass("validValueIconBig");
};

/**
 * The purpose of this method is to show error value icon in big input text boxes.
 * @param txtID
 */
administratorManagement.prototype.showErrorBigIcon = function (txtID) {
	$(txtID).removeClass("validValueIconBig");
	$(txtID).addClass("errorIconBig");	
};

/**
 * The purpose of this function is to validate user name.
 * 
 * @param username
 * @param userameSpanID
 * @returns {Boolean}
 */
administratorManagement.prototype.validateUsername = function(username,
		userameSpanID) {
	var isUsernameValid = true;
	//The following regex finds special characters.
	var specialchar = /^[!@#$%^&*()+=-_`~;,.<>?'":|{}[]\]*$/;
	//The following regex finds characters in the range of 0-9,a-z(lower case) and A-Z(upper case).
	var letters = /^[0-9a-zA-Z-_]+$/;
	var lenUsername = username.length;
	if (lenUsername == 0) {
		$("#popupUsernameErrorMsg").removeClass('asidePopupSuccessMessageCommon');
		$("#popupUsernameErrorMsg").addClass('asidePopupErrorMessageCommon');
		document.getElementById(userameSpanID).innerHTML = getUiProps().MSG0160;
		uAdministratorManagement.showErrorIcon("#txtCreateNewUserUserName");
		isUsernameValid = false;
	} else if (lenUsername > 128) {
		$("#popupUsernameErrorMsg").removeClass('asidePopupSuccessMessageCommon');
		$("#popupUsernameErrorMsg").addClass('asidePopupErrorMessageCommon');
		document.getElementById(userameSpanID).innerHTML = getUiProps().MSG0161;
		uAdministratorManagement.showErrorIcon("#txtCreateNewUserUserName");
		isUsernameValid = false;
	} else if (lenUsername != 0 && !(username.match(letters))) {
		$("#popupUsernameErrorMsg").removeClass('asidePopupSuccessMessageCommon');
		$("#popupUsernameErrorMsg").addClass('asidePopupErrorMessageCommon');
		document.getElementById(userameSpanID).innerHTML = getUiProps().MSG0162;
		uAdministratorManagement.showErrorIcon("#txtCreateNewUserUserName");
		isUsernameValid = false;
	} else if (lenUsername != 0
			&& (specialchar.toString().indexOf(username.substring(0, 1)) >= 0)) {
		$("#popupUsernameErrorMsg").removeClass('asidePopupSuccessMessageCommon');
		$("#popupUsernameErrorMsg").addClass('asidePopupErrorMessageCommon');
		document.getElementById(userameSpanID).innerHTML = getUiProps().MSG0163;
		uAdministratorManagement.showErrorIcon("#txtCreateNewUserUserName");
		isUsernameValid = false;
	}
	return isUsernameValid;
};

/**
 * The purpose of this function is to validate retype password.
 * 
 * @param retypePassword
 * @param retypePasswordSpanID
 * @param password
 * @returns {Boolean}
 */
administratorManagement.prototype.validateRetypePassword = function(
		retypePassword, retypePasswordSpanID, password, retypePasswordID) {
	document.getElementById(retypePasswordSpanID).innerHTML = "";
	var isRetypePasswordValid = false;
	var lenRetypePassword = retypePassword.length;
	var lenPassword = password.length;
	if (lenRetypePassword == 0) {
		document.getElementById(retypePasswordSpanID).innerHTML = getUiProps().MSG0164;
		if(retypePasswordID == "txtCreateNewUserRetypePassword"){
			uAdministratorManagement.showErrorIcon("#" + retypePasswordID);
		}else if(retypePasswordID == "txtPassSettingsRetypePassword"){
			uAdministratorManagement.showErrorBigIcon("#" + retypePasswordID);
		}
		isRetypePasswordValid = false;
	} else if (lenRetypePassword != lenPassword) {
		document.getElementById(retypePasswordSpanID).innerHTML = getUiProps().MSG0165;
		if(retypePasswordID == "txtCreateNewUserRetypePassword"){
			uAdministratorManagement.showErrorIcon("#" + retypePasswordID);
		}else if(retypePasswordID == "txtPassSettingsRetypePassword"){
			uAdministratorManagement.showErrorBigIcon("#" + retypePasswordID);
		}
		isRetypePasswordValid = false;
	} else if (lenRetypePassword != 0 && lenPassword != 0
			&& retypePassword != password) {
		document.getElementById(retypePasswordSpanID).innerHTML = getUiProps().MSG0165;
		if(retypePasswordID == "txtCreateNewUserRetypePassword"){
			uAdministratorManagement.showErrorIcon("#" + retypePasswordID);
		}else if(retypePasswordID == "txtPassSettingsRetypePassword"){
			uAdministratorManagement.showErrorBigIcon("#" + retypePasswordID);
		}
		isRetypePasswordValid = false;
	} else {
		if(retypePasswordID == "txtCreateNewUserRetypePassword"){
			uAdministratorManagement.showValidValueIcon("#" + retypePasswordID);
		}else if(retypePasswordID == "txtPassSettingsRetypePassword"){
			uAdministratorManagement.showValidValueBigIcon("#" + retypePasswordID);
		}
		isRetypePasswordValid = true;
	}
	return isRetypePasswordValid;
};

administratorManagement.prototype.validateReEnterEmail = function(
		reEnterEmail, reEnterEmailSpanID, email, reEnterEmailID) {
	document.getElementById(retypePasswordSpanID).innerHTML = "";
	var isRetypePasswordValid = false;
	var lenRetypePassword = retypePassword.length;
	var lenPassword = password.length;
	if (lenRetypePassword == 0) {
		document.getElementById(retypePasswordSpanID).innerHTML = getUiProps().MSG0164;
		if(retypePasswordID == "txtCreateNewUserRetypePassword"){
			uAdministratorManagement.showErrorIcon("#" + retypePasswordID);
		}else if(retypePasswordID == "txtPassSettingsRetypePassword"){
			uAdministratorManagement.showErrorBigIcon("#" + retypePasswordID);
		}
		isRetypePasswordValid = false;
	} else if (lenRetypePassword != lenPassword) {
		document.getElementById(retypePasswordSpanID).innerHTML = getUiProps().MSG0165;
		if(retypePasswordID == "txtCreateNewUserRetypePassword"){
			uAdministratorManagement.showErrorIcon("#" + retypePasswordID);
		}else if(retypePasswordID == "txtPassSettingsRetypePassword"){
			uAdministratorManagement.showErrorBigIcon("#" + retypePasswordID);
		}
		isRetypePasswordValid = false;
	} else if (lenRetypePassword != 0 && lenPassword != 0
			&& retypePassword != password) {
		document.getElementById(retypePasswordSpanID).innerHTML = getUiProps().MSG0165;
		if(retypePasswordID == "txtCreateNewUserRetypePassword"){
			uAdministratorManagement.showErrorIcon("#" + retypePasswordID);
		}else if(retypePasswordID == "txtPassSettingsRetypePassword"){
			uAdministratorManagement.showErrorBigIcon("#" + retypePasswordID);
		}
		isRetypePasswordValid = false;
	} else {
		if(retypePasswordID == "txtCreateNewUserRetypePassword"){
			uAdministratorManagement.showValidValueIcon("#" + retypePasswordID);
		}else if(retypePasswordID == "txtPassSettingsRetypePassword"){
			uAdministratorManagement.showValidValueBigIcon("#" + retypePasswordID);
		}
		isRetypePasswordValid = true;
	}
	return isRetypePasswordValid;
};

/**
 * The purpose of this function is to validate password.
 * 
 * @param password
 * @param passwordSpanID
 * @returns {Boolean}
 */
administratorManagement.prototype.validatePassword = function(password,
		passwordSpanID, userName, passwordID) {
	var result = true;
	document.getElementById(passwordSpanID).innerHTML = "";
	var lenPassword = password.length;
	//The following regex finds special characters example,@# et al.
	var specialCharacter = /^[!@#$%^&*()+=-_`~;,.<>?'":|{}[]\]*$/; 
	//The following regex finds aplhabets a-z(lower case) and A-Z(upper case).
	var alphabets = /^[a-zA-Z]*$/;
	//The following regex finds digits.
	var digits = /^[0-9]*$/;
	var spclCharExist = false;
	var alphabetExist = false;
	var digitExist = false;
	var MIN_LENGTH = 10;
	var MAX_LENGTH = 128;
	if(lenPassword < 1 || password === undefined || password === null || password === ""){
		objCommon.setHTMLValue(passwordSpanID, getUiProps().MSG0174);
		if(passwordID == "txtCreateNewUserPassword"){
			uAdministratorManagement.showErrorIcon("#" + passwordID);
		}else if(passwordID == "txtPassSettingsNewPassword"){
			uAdministratorManagement.showErrorBigIcon("#" + passwordID);
		}
		result = false;
	}else if(lenPassword < MIN_LENGTH){
		objCommon.setHTMLValue(passwordSpanID, getUiProps().MSG0171);
		if(passwordID == "txtCreateNewUserPassword"){
			uAdministratorManagement.showErrorIcon("#" + passwordID);
		}else if(passwordID == "txtPassSettingsNewPassword"){
			uAdministratorManagement.showErrorBigIcon("#" + passwordID);
		}
		result = false;
	}else if(lenPassword > MAX_LENGTH){
		objCommon.setHTMLValue(passwordSpanID, getUiProps().MSG0122);
		if(passwordID == "txtCreateNewUserPassword"){
			uAdministratorManagement.showErrorIcon("#" + passwordID);
		}else if(passwordID == "txtPassSettingsNewPassword"){
			uAdministratorManagement.showErrorBigIcon("#" + passwordID);
		}
		result = false;
	}else if(password.indexOf(userName) != -1){
		objCommon.setHTMLValue(passwordSpanID, getUiProps().MSG0173);
		if(passwordID == "txtCreateNewUserPassword"){
			uAdministratorManagement.showErrorIcon("#" + passwordID);
		}else if(passwordID == "txtPassSettingsNewPassword"){
			uAdministratorManagement.showErrorBigIcon("#" + passwordID);
		}
		result = false;
	}
	else{	
		for(var index = 0; index < lenPassword; index++){
			if(password.substring(index, index+1).match(specialCharacter)){
				spclCharExist = true;
				break;
			}
		}
		for(var index = 0; index < lenPassword; index++){
			if(password.substring(index, index+1).match(alphabets)){
				alphabetExist = true;
				break;
			}
		}
		for(var index = 0; index < lenPassword; index++){
			if(password.substring(index, index+1).match(digits)){
				digitExist = true;
				break;
			}
		}
		if(!(spclCharExist && alphabetExist && digitExist)){
			objCommon.setHTMLValue(passwordSpanID, getUiProps().MSG0172);
			if(passwordID == "txtCreateNewUserPassword"){
				uAdministratorManagement.showErrorIcon("#" + passwordID);
			}else if(passwordID == "txtPassSettingsNewPassword"){
				uAdministratorManagement.showErrorBigIcon("#" + passwordID);
			}
			result = false;
		}
	}
	if(result){
		if(passwordID == "txtCreateNewUserPassword"){
			uAdministratorManagement.showValidValueIcon("#" + passwordID);
		}else if(passwordID == "txtPassSettingsNewPassword"){
			uAdministratorManagement.showValidValueBigIcon("#" + passwordID);
		}
	}
	return result;
};

/**
 * The purpose of this function is to check user name existence.
 * 
 * @param username
 */
administratorManagement.prototype.isUsernameAvailable = function(username) {
	if (username.length > 0) {
		$("#popupUsernameErrorMsg").html("");
		$.ajax({
			type : "POST",
			/*beforeSend: function (xhr) {
				xhr.setRequestHeader("Cache-Control", "no-cache, no-store, must-revalidate");
					xhr.setRequestHeader("Pragma", "no-cache");
					xhr.setRequestHeader("Expires", 0);
				},*/
			dataType : 'json',
			url : "../../check",
			cache : false,
			data : "useridtext=" + escape(username),
			success : function(response) {
				var msg = "";
				var shorterUserName = username;
				if(username.length > 33) {
					shorterUserName = username.substring(0, 34) + "..";
				}
				if (response['userIdMsg'] == "inUse") {
					uAdministratorManagement.isUserNameValueAvailable = false;
					msg = shorterUserName + " is already in use";
					$("#popupUsernameErrorMsg").removeClass('asidePopupSuccessMessageCommon');
					$("#popupUsernameErrorMsg").addClass('asidePopupErrorMessageCommon');
					uAdministratorManagement.showErrorIcon("#txtCreateNewUserUserName");
					$("#ProceedButton").attr("disabled", "disabled");
				} else if (response['userIdMsg'] == "available") {
					uAdministratorManagement.isUserNameValueAvailable = true;
					msg = shorterUserName + " is available";
					$("#popupUsernameErrorMsg").removeClass('asidePopupErrorMessageCommon');
					$("#popupUsernameErrorMsg").addClass('asidePopupSuccessMessageCommon');
					uAdministratorManagement.showValidValueIcon("#txtCreateNewUserUserName");
					$("#ProceedButton").removeAttr("disabled");
				}
				$('#popupUsernameErrorMsg').attr('title', username);
				$("#popupUsernameErrorMsg").html(msg);
			}
		});
	} else {
		$("#popupUsernameErrorMsg").html("");
	}
};

/**
 * The purpose of this function is to validate fields of create user pop up on
 * focus.
 * 
 * @param id
 */
administratorManagement.prototype.validate = function(id) {
	var username = $("#txtCreateNewUserUserName").val();
	/*var password = $("#txtCreateNewUserPassword").val();
	var retypePassword = $("#txtCreateNewUserRetypePassword").val();*/
	var firstName = $("#txtCreateNewUserFirstName").val();
	var familyName = $("#txtCreateNewUserFamilyName").val();
	var newPwdOnPasswordSettings = $("#txtPassSettingsNewPassword").val();
	var retypePwdOnPasswordSettings = $("#txtPassSettingsRetypePassword").val();
	if (id == 'txtCreateNewUserUserName') {
		var isUsernameValid = uAdministratorManagement.validateUsername(username,
				"popupUsernameErrorMsg");
		if(isUsernameValid && uAdministratorManagement.isUserNameValueAvailable){
			uAdministratorManagement.showValidValueIcon("#txtCreateNewUserUserName");
		}
	}
	/*if (id == 'txtCreateNewUserPassword') {
		uAdministratorManagement.validatePassword(password,
				"popupPasswordErrorMsg", username, "txtCreateNewUserPassword");
	}
	if (id == 'txtCreateNewUserRetypePassword') {
		isValid = uAdministratorManagement.validateRetypePassword(
				retypePassword, "popupRetypePasswordErrorMsg", password, "txtCreateNewUserRetypePassword");
	}*/
	if (id == 'txtCreateNewUserFirstName') {
		uEditUserInformation.validateFirstName(firstName,
				"popupFirstNameErrorMsg", "txtCreateNewUserFirstName");
	}
	if (id == 'txtCreateNewUserFamilyName') {
		uEditUserInformation.validateFamilyName(familyName,
				"popupFamilyNameErrorMsg", "txtCreateNewUserFamilyName");
	}
	if (id == 'txtPassSettingsNewPassword') {
		var userNamePassSettings = sessionStorage.usernamePassSettings;
		uAdministratorManagement.validatePassword(newPwdOnPasswordSettings,
				"popupPassSettingsNewPassErrorMsg", userNamePassSettings, "txtPassSettingsNewPassword");
	}
	if (id == 'txtPassSettingsRetypePassword') {
		uAdministratorManagement.validateRetypePassword(
				retypePwdOnPasswordSettings, "popupPassSettingsRetypePassErrorMsg", newPwdOnPasswordSettings, "txtPassSettingsRetypePassword");
	}
};

/**
 * The purpose of this function is to validate all fields.
 * 
 * @param username
 * @param password
 * @param retypePassword
 * @param firstName
 * @param familyName
 * @param email
 * @returns {Boolean}
 */
administratorManagement.prototype.validateNewUserPopupFields = function(
		username, password, retypePassword, firstName, familyName, email, reEnterEmail) {
	var isValidate = false;
	var isUsernameValid = this.validateUsername(username, "popupUsernameErrorMsg");
	if (isUsernameValid && uAdministratorManagement.isUserNameValueAvailable) {
		uAdministratorManagement.showValidValueIcon("#txtCreateNewUserUserName");
		//if (this.validatePassword(password, "popupPasswordErrorMsg",username,"txtCreateNewUserPassword")) {
			//if (this.validateRetypePassword(retypePassword,
					//"popupRetypePasswordErrorMsg", password, "txtCreateNewUserRetypePassword")) {
				if (uEditUserInformation.validateFirstName(firstName,
						"popupFirstNameErrorMsg", "txtCreateNewUserFirstName")) {
					if (uEditUserInformation.validateFamilyName(familyName,
							"popupFamilyNameErrorMsg","txtCreateNewUserFamilyName")) {
						if (uEditUserInformation.validateEmail(email,
								"popupEmailErrorMsg", "txtCreateNewUserEmail")) {
							if (uEditUserInformation.validateReEnterEmail(reEnterEmail, "popupReEnterEmailErrorMsg", "txtCreateNewUserReEnterEmail", email))
							isValidate = true;
						}
					}
				}
			//}
		//}
	}
	return isValidate;
};

/**
 * The purpose of this function is to create new user.
 */
administratorManagement.prototype.createNewUser = function() {
	var username = $("#txtCreateNewUserUserName").val();
	var password = $("#txtCreateNewUserPassword").val();
	var retypePassword = $("#txtCreateNewUserRetypePassword").val();
	var firstName = $("#txtCreateNewUserFirstName").val();
	var familyName = $("#txtCreateNewUserFamilyName").val();
	var email = $("#txtCreateNewUserEmail").val();
	var reEnterEmail = $("#txtCreateNewUserReEnterEmail").val();
	var operation = "CREATE_USER";
	var privilege = getUiProps().MSG0158;
	//if ($("#chkAdmin").is(':checked')) {
	if($('#rdYes').is(':checked')){
		privilege = getUiProps().MSG0159;
	}
	var isDetailValid = this.validateNewUserPopupFields(username, password,
			retypePassword, firstName, familyName, email, reEnterEmail);
	var encodedFirstName = encodeURI(firstName);
	var encodedFamilyName = encodeURI(familyName);
	var CSRFTokenCreateUser = document.getElementById("CSRFTokenCreateUser").value;
	var encodedEAddress = encodeURIComponent(email);
	if (isDetailValid) {
		$.ajax({
			data : {
				operation	:	operation,
				username	:	username,
				password	:	password,
				firstName	:	encodedFirstName,
				familyName	:	encodedFamilyName,
				encodedEAddress : encodedEAddress,
				privilege	:	privilege,
				CSRFTokenCreateUser : CSRFTokenCreateUser
			},
			
			url : '../../AdministratorManagement',
			dataType : 'json',
			type : 'POST',
			/*beforeSend: function (xhr) {
				xhr.setRequestHeader("Cache-Control", "no-cache, no-store, must-revalidate");
					xhr.setRequestHeader("Pragma", "no-cache");
					xhr.setRequestHeader("Expires", 0);
				},*/
			async : false,
			cache : false,
			success : function(jsonData) {
				if (jsonData['sessionTimeOut'] == "sessionTimeOut") {
					uCreateEnvironment.performLogout();
				} else if (jsonData['createNewUser'] == "createSuccess") {
					/*uAdministratorManagement.displayNotificationMessage(
							"CREATE", "createNewUserModal");*/
					uAdministratorManagement.sendVerificationMail(username, email, firstName, familyName, false);
				}
			},
			error : function() {
			}
		});
	}
};

/**
 * The purpose of this function is to send verification mail to newly created user.
 */
administratorManagement.prototype.sendVerificationMail = function(username, email, firstName, familyName, isEdit) {
	var operation = "VERFICATION_MAIL";
	var actionPerformed = "CREATE_USER";
	var CSRFTokenSendMailVerification = document.getElementById("CSRFTokenSendMailVerification").value;
	if (isEdit) {
		CSRFTokenSendMailVerification = document.getElementById("CSRFTokenEditUser").value;
		actionPerformed = "EDIT_USER";
	}
	var encodedEAddress = encodeURIComponent(email);
	var encodedFirstName = encodeURI(firstName);
	var encodedFamilyName = encodeURI(familyName);
	$.ajax({
		data : {
			operation	:	operation,
			username	:	username,
			encodedEAddress : encodedEAddress,
			firstName	:	encodedFirstName,
			familyName	:	encodedFamilyName,
			CSRFTokenSendMailVerification : CSRFTokenSendMailVerification,
			actionPerformed : actionPerformed
		},
		
		url : '../../MailServlet',
		dataType : 'json',
		type : 'POST',
		async : false,
		cache : false,
		success : function(jsonData) {
			if (jsonData['sessionTimeOut'] == "sessionTimeOut") {
				uCreateEnvironment.performLogout();
			} else if (jsonData['verificationMail'] == "verificationMailSentSuccess") {
				var operationMode = "CREATE";
				var modalID = "createNewUserModal";
				var mailSentSuccess = "createAndMailSentSuccess";
				var marginLeft = "16%";
				if (isEdit) {
					operationMode = "EDIT";
					modalID = "editUserAdminMgmntModal";
					mailSentSuccess = "editMailAndMailSentSuccess";
					marginLeft = "15%";
				}
				uAdministratorManagement.displayNotificationMessage(operationMode, modalID, mailSentSuccess, marginLeft);
			} else if (jsonData['verificationMail'] == "verificationMailSentFailure") {
				var operationMode = "CREATE";
				var modalID = "createNewUserModal";
				var mailSentSuccess = "createSuccessMailSentFailure";
				var marginLeft = "21%";
				if (isEdit) {
					operationMode = "EDIT";
					modalID = "editUserAdminMgmntModal";
					mailSentSuccess = "editMailAndMailSentFailure";
					marginLeft = "19%";
				}
				uAdministratorManagement.displayNotificationMessage(operationMode, modalID, mailSentSuccess, marginLeft);
			}
		},
		error : function() {
			var operationMode = "CREATE";
			var modalID = "createNewUserModal";
			var mailSentSuccess = "createSuccessMailSentFailure";
			if (isEdit) {
				operationMode = "EDIT";
				modalID = "editUserAdminMgmntModal";
				mailSentSuccess = "editMailAndMailSentFailure";
			}
			uAdministratorManagement.displayNotificationMessage(operationMode, modalID, mailSentSuccess, "16%");
		}
	});
	
};

administratorManagement.prototype.updateMultipleUserStatusActive = function() {
	var paramArray = uAdministratorManagement.getUserDetails();
	var noOfRecords = paramArray.length;
	for(var index = 0; index < noOfRecords; index++){
		var username = paramArray[index]["UserName"];
		uAdministratorManagement.updateUserStatusActive(username);
	}
	uAdministratorManagement.displayNotificationMessage(
			"ACTIVATE_USER", "", "", "21%");
};

administratorManagement.prototype.updateUserStatusActive = function(username) {
	var operation = "SET_USER_STATUS";
	var status = "active";
	var CSRFTokenActivateUser = document.getElementById("CSRFTokenActivateUser").value;
	$.ajax({
		data : {
			operation	:	operation,
			username	:	username,
			status 		:	status,
			CSRFTokenActivateUser : CSRFTokenActivateUser
		},
		
		url : '../../MailServlet',
		dataType : 'json',
		type : 'POST',
		async : false,
		cache : false,
		success : function(jsonData) {
			if (jsonData['sessionTimeOut'] == "sessionTimeOut") {
				uCreateEnvironment.performLogout();
			} else if (jsonData['updateUserStatus'] == "userStatusUpdateSuccess") {
				sbSuccessfulUserActivateCount++;
			}
		},
		error : function() {
		}
	});
	
};

/* ***************** ADMINISTRATOR MANAGEMENT: CREATE USER: END ******************/

/* ***************** ADMINISTRATOR MANAGEMENT: VIEW USER DETAILS: START ******************/

/**
 * The purpose of this method is to set the column widths of admin management table dynamically
 * as per screen size.
 */
administratorManagement.prototype.setColumnEllipsisWidth = function(){
	var width = $(window).width();
	var tableWidth = (width - 68); 
	if (width>1280) {
		var col1 = (9.34426/100)*tableWidth;
		var col2 = (8.36065/100)*tableWidth;
		var col3 = (8.36065/100)*tableWidth;
		var col4 = (15.24590/100)*tableWidth;
		var col5 = (6.22950/100)*tableWidth;
		var col6 = (8.27868/100)*tableWidth;
		var col7 = (10.24590/100)*tableWidth;
		var col8 = (10.24590/100)*tableWidth;
		var col9 = (8.27868/100)*tableWidth;
		$("#adminMgmtTable tbody tr td:nth-child(1)").css('max-width', col1 + "px");
		$("#adminMgmtTable tbody tr td:nth-child(2)").css('max-width', col2 + "px");
		$("#adminMgmtTable tbody tr td:nth-child(3)").css('max-width', col3 + "px");
		$("#adminMgmtTable tbody tr td:nth-child(4)").css('max-width', col4 + "px");
		$("#adminMgmtTable tbody tr td:nth-child(5)").css('max-width', col5 + "px");
		$("#adminMgmtTable tbody tr td:nth-child(6)").css('max-width', col6 + "px");
		$("#adminMgmtTable tbody tr td:nth-child(7)").css('max-width', col7 + "px");
		$("#adminMgmtTable tbody tr td:nth-child(8)").css('max-width', col8 + "px");
		$("#adminMgmtTable tbody tr td:nth-child(9)").css('max-width', col9 + "px");
	}else{
		$("#adminMgmtTable tbody tr td:nth-child(1)").css('max-width', "114px");
		$("#adminMgmtTable tbody tr td:nth-child(2)").css('max-width', "102px");
		$("#adminMgmtTable tbody tr td:nth-child(3)").css('max-width', "102px");
		$("#adminMgmtTable tbody tr td:nth-child(4)").css('max-width', "186px");
		$("#adminMgmtTable tbody tr td:nth-child(5)").css('max-width', "76px");
		$("#adminMgmtTable tbody tr td:nth-child(6)").css('max-width', "101px");
		$("#adminMgmtTable tbody tr td:nth-child(7)").css('max-width', "125px");
		$("#adminMgmtTable tbody tr td:nth-child(8)").css('max-width', "125px");
		$("#adminMgmtTable tbody tr td:nth-child(9)").css('max-width', "101px");
	}
};

/**
 * The purpose of this method is to set the left margin for Maximum User Limit message
 * to align it to the center of the screen.
 */
administratorManagement.prototype.setMarginForMaxUserLimitMsg = function(){
	var width = $(window).width();
	if (width>1280) {
		var adminMgmtHeadingWidth = 371;
		var msgWidth = 230;
		var marginLeft = ((width/2) - adminMgmtHeadingWidth - (msgWidth/2) - 10);
		$("#maxUserMsg").css("margin-left",marginLeft + "px");
	} else{
		$("#maxUserMsg").css("margin-left", "144px");
	}
};

/**
 * The purpose of this method is to center align the user privilege message.
 */
administratorManagement.prototype.setPositionForUserPrivilegeMsg = function(){
	var viewportHeight = $(window).height();
	var messagePosition = (viewportHeight/2) - 75;
		if (viewportHeight > 650) {
			$("#dvemptyTableUserPrivilegeMessage").css("margin-top", messagePosition);
		} else if (viewportHeight <= 650) {
			$("#dvemptyTableUserPrivilegeMessage").css("margin-top", "250px");
		}
};

/**
 * The purpose of this method is to center align the active/inactive message.
 */
administratorManagement.prototype.setPositionForInactiveUserMsg = function(){
	var viewportHeight = $(window).height();
	var messagePosition = (viewportHeight/2) - 130;
		if (viewportHeight > 650) {
			$("#dvemptyTableMessage").css("margin-top", messagePosition);
		} else if (viewportHeight <= 650) {
			$("#dvemptyTableMessage").css("margin-top", "195px");
		}
};

/**
 * The purpose of this method is to set dynamic height for administrator management
 * area as per view port size.
 */
administratorManagement.prototype.setScrollableHeightForAdminMgmt = function(){
	var viewPortHeight = $(window).height();
	var gridHeight = viewPortHeight - 105;
	if (viewPortHeight > 650) {
		$("#adminMgmtScroll").css("max-height", gridHeight);	
	} else if (viewPortHeight <= 650) {
		$("#adminMgmtScroll").css("max-height", '545px');
	}
};

/**
 * The purpose of this method is to set the left margin for Success message
 * to align it to the center of the screen.
 */
/*administratorManagement.prototype.setMarginForSuccessMsg = function(){
	var width = $(window).width();
	if (width>1280) {
		var adminMgmtHeadingWidth = 371;
		var msgWidth = 172;
		var marginLeft = ((width/2) - adminMgmtHeadingWidth - (msgWidth/2) - 10);
		$("#successMsgDiv").css("margin-left",marginLeft + "px");
	} else{
		$("#successMsgDiv").css("margin-left", "173.5px");
	}
};*/

/**
 * The purpose of this function is to fetch list of active users.
 */
administratorManagement.prototype.fetchActiveUsersList = function() {
	$.ajax({
		dataType : 'json',
		url : '../../AdministratorManagement',
		type : 'GET',
		/*beforeSend: function (xhr) {
			xhr.setRequestHeader("Cache-Control", "no-cache, no-store, must-revalidate");
				xhr.setRequestHeader("Pragma", "no-cache");
				xhr.setRequestHeader("Expires", 0);
			},*/
		async : false,
		cache : false,
		success : function(jsonData) {
			if (jsonData['sessionTimeOut'] == "sessionTimeOut") {
				uCreateEnvironment.performLogout();
			} else {
				sessionStorage.adminGridData = JSON.stringify(jsonData["activeUsersList"]);
				sessionStorage.adminGridDataLength = jsonData["activeUsersList"].length;
				uAdministratorManagement.createUserDetailsTable(jsonData);
			}
		},
		error : function(jsonData) {
		}
	});
};

/**
 * The purpose of this function is to create rows of user detail table.
 * 
 * @param dynamicUserList
 * @param username
 * @param firstName
 * @param familyName
 * @param email
 * @param privilege
 * @param registrationDate
 * @param updatedDate
 * @returns
 */
administratorManagement.prototype.createRowsForUsersDetailsTable = function(
		dynamicUserList, username, firstName, familyName, email, privilege,
		registrationDate, updatedDate, lockStatus, count, name, verificationSendAtDate, userStatus, userOrgID, verifiedAtDate, lastLoginAtDate) {
	var operationsDiv = "";
	var ADMIN_PRIVILEGE = getUiProps().MSG0159;
	var SUBSCRIBER_PRIVILEGE = getUiProps().MSG0169;
	var MAX_LOCK_STATUS = getUiProps().MSG0175;
	var loggedInUserPrivilege = sessionStorage.loggedInUserPrivilege;
	var loggedInUsername = sessionStorage.userID;
	var isEmailVerified = 'No';
	//Following regex escapes apostrohe.
	var quotesEscapedFirstName = firstName.replace(/\"/g,'\\"');
	//Followng regex escapes double quotes.
	quotesEscapedFirstName = quotesEscapedFirstName.replace(/\'/g, "&#39;");
	var quotesEscapedFamilyName = familyName.replace(/\"/g,'\\"');
	quotesEscapedFamilyName = quotesEscapedFamilyName.replace(/\'/g, "&#39;");
	var selectedUsername = '"' + username + '"';
	var selectedFirstName = '"' + quotesEscapedFirstName + '"';
	var selectedFamilyName = '"' + quotesEscapedFamilyName + '"';
	var selectedEmail = '"' + email + '"';
	var selectedPrivilege = '"' + privilege + '"';
	if (verifiedAtDate != null) {
		isEmailVerified = 'Yes';
	}
	if (lastLoginAtDate == null) {
		lastLoginAtDate = 'Never';
	}
	var loggedInUsername = sessionStorage.userID;
	if (username == loggedInUsername) {
		dynamicUserList += "<td style='width:1%' class='noRowSelect noRowSelectLoggedInUser'><input id = 'chkBox"
				+ count
				+ "' type = 'checkbox' class = 'case cursorHand regular-checkbox big-checkbox chkClass' name = 'case' /><label style='display:none' for='chkBox"
				+ count
				+ "' class='customChkbox customChkbox checkBoxLabel'></label></td>";
	} else if ((loggedInUserPrivilege == ADMIN_PRIVILEGE && privilege == SUBSCRIBER_PRIVILEGE)) {
		dynamicUserList += "<td style='width:1%' class='noRowSelect'><input id = 'chkBox"
			+ count
			+ "' type = 'checkbox' class = 'case cursorHand regular-checkbox big-checkbox chkClass' name = 'case' /><label style='display:none' for='chkBox"
			+ count
			+ "' class='customChkbox customChkbox checkBoxLabel'></label></td>";
	} else {
		dynamicUserList += "<td style='width:1%'><input id = 'chkBox"
				+ count
				+ "' value='"
				+ userStatus
				+ "' type = 'checkbox' class = 'case cursorHand regular-checkbox big-checkbox chkClass' name = 'case' /><label for='chkBox"
				+ count
				+ "' class='customChkbox customChkbox checkBoxLabel'></label></td>";
	}
	dynamicUserList += "<td style='width: 15%;max-width:49px'><div class = 'adminMgmtTableEllipsis'><label title='"+ name + "' class='userFullName'>" + name + "</label></div></td>";
	dynamicUserList += "<td style='width: 15%;'>" + lastLoginAtDate + "</td>";
	dynamicUserList += "<td style='width: 15%;max-width: 87px'><div class = 'adminMgmtTableEllipsis' ><label title='"+ username + "' id='dvUserNameAdminTable"
			+ count
			+ "'>"
			+ username + "</label></div></td>";
	dynamicUserList += "<td style='width: 17%;max-width: 34px'><div class = 'adminMgmtTableEllipsis' ><label title='"
			+ email
			+ "' id='dvEmailAdminTable"
			+ count
			+ "'>"
			+ email
			+ "</label></div></td>";

	dynamicUserList += "<td style='width: 9%;' id='dvPrivilegeAdminTable"
			+ count + "'>" + privilege + "</td>";
	dynamicUserList += "<td style='width: 9%;'>" + userStatus + "</td>";
	dynamicUserList += "<td style='width: 9%;' id='dvEmailVerifiedAdminTable"+count+"'>" + isEmailVerified + "</td>";
	// dynamicUserList += "<td style='width: 10%;'>";

	/*
	 * if (username != loggedInUsername) { operationsDiv = "<div><span
	 * class='adminMgmntEditIcon' title='Edit'
	 * onclick='uAdministratorManagement.openPopUpWindow(" +
	 * editUserAdminMgmntDialog + "," + editUserAdminMgmntModal + "," +
	 * selectedUsername + "," + '"EDIT"' +"," +selectedFirstName +","
	 * +selectedFamilyName +"," +selectedEmail +"," +selectedPrivilege +");'></span><span
	 * class='adminMgmntDeleteIcon' title='Delete'
	 * onclick='uAdministratorManagement.openPopUpWindow(" +
	 * userDeleteAdminMgmntDialogBox + "," + userDeleteAdminMgmntModalWindow +
	 * "," + selectedUsername + "," + '"DELETE_USER"' + ");'></span><span
	 * class='adminMgmntPassSettingsIcon' title='Password Settings'
	 * onclick='uAdministratorManagement.openPopUpWindow(" +
	 * changePassAdminMgmntDialog + "," + changePassAdminMgmntModal + "," +
	 * selectedUsername + "," + '"PASSWORD_SETTINGS"' + ");'></span></div>"; }
	 * else { operationsDiv = "<div style='display:none;'></div>"; }
	 */
	/*
	 * if ((loggedInUserPrivilege == ADMIN_PRIVILEGE && privilege ==
	 * SUBSCRIBER_PRIVILEGE)) { operationsDiv = "<div style='display:none;'></div>"; }
	 */
	// dynamicUserList += operationsDiv;
	// dynamicUserList += "</td>";
	dynamicUserList += "<td style='width: 10%;'>";
	if (lockStatus >= MAX_LOCK_STATUS) {
		dynamicUserList += "<span class='adminMgmntLockIcon'></span>";
	}
	dynamicUserList += "</td>";
	dynamicUserList += "</tr>";
	return dynamicUserList;

};

/**
 * The purpose of this method is to show success message.
 */
administratorManagement.prototype.showSuccessMessage = function(message, dynamicMargin){
	/*document.getElementById("successMsgDiv").style.display = "inline-block";
	$("#successMsg").text(message);*/
	var isAdminTabSelected = $("#adminMgmtTxt").hasClass('adminMgmtTxtRed');
	if (sessionStorage.loggedInUserPrivilege.toLowerCase() == 'user' && isAdminTabSelected) {
		document.getElementById("successMsgDiv1").style.display = "inline-block";
		$("#successMsg1").text(message);
	} else {
		document.getElementById("successMsgDiv").style.display = "inline-block";
		$("#successMsg").text(message);
	}
	if (dynamicMargin != undefined && isAdminTabSelected == true) {
		$("#successMsgDiv").css("margin-left", dynamicMargin);
	}
};

/**
 * The purpose of this method is to hide success message.
 */
administratorManagement.prototype.hideSuccessMessage = function(){
	document.getElementById("successMsgDiv").style.display = "none";
};

/**
 * The purpose of this function is to enable create user button.
 */
administratorManagement.prototype.enableCreateUserBtn = function() {
	document.getElementById("maxUserMsg").style.display = "none";
	$("#createUser").removeAttr("disabled");
};

/**
 * The purpose of this function is to disbale create user button.
 */
administratorManagement.prototype.disableCreateUserBtn = function() {
	document.getElementById("maxUserMsg").style.display = "inline-block";
	//$("#createUser").attr("disabled", "disabled");
	$("#adminMgmntCreateIcon").attr('disabled', true);
	$("#adminMgmntCreateIcon").removeClass("createIcon");
	$("#adminMgmntCreateIcon").addClass("createIconDisabled");
};

/**
 * The purpose of this function is to create user detail table.
 * 
 * @param jsonData
 */
administratorManagement.prototype.createUserDetailsTable = function(jsonData, isFilterMode, filterType) {
	$("#chkSelectallAdmin").attr('checked', false);
	uAdministratorManagement.disableButton("#adminMgmntEditIcon", "editIconEnabled", "editIconDisabled");
	uAdministratorManagement.disableButton("#adminMgmntDeleteIcon", "deleteIconEnabled", "deleteIconDisabled");
	uAdministratorManagement.disableButton("#adminMgmntPasswordSettingsIcon", "adminMgmntPassSettingsIcon", "adminMgmntPassSettingsIconDisabled");
	uAdministratorManagement.disableButton("#adminMgmntActivateIcon", "activateUserIconEnabled", "activateUserIconDisabled");
	$("#chkSelectallAdmin").attr('disabled', false);
	$("#ddFilterByStatus").attr('disabled', false);
	document.getElementById("dvemptyTableMessage").style.display = "none";
	var dynamicUserList = "";
	var activeUsersList = null;
	if (isFilterMode) {
		activeUsersList = jsonData;
		if (activeUsersList.length == 0) {
			if (filterType == "Inactive") {
				$("#dvemptyTableMessage").text(getUiProps().MSG0406);
			} else if (filterType == "Locked") {
				$("#dvemptyTableMessage").text(getUiProps().MSG0407);
			}
			$("#dvemptyTableMessage").css("width",187 + "px");
			if (sessionStorage.selectedLanguage == 'jp') {
				$("#dvemptyTableMessage").addClass('japaneseFont');
				$("#dvemptyTableMessage").css('width', '278px');
				$("#dvemptyTableMessage").css('line-height', '24px');
			}
			document.getElementById("dvemptyTableMessage").style.display = "block";
			uAdministratorManagement.setPositionForInactiveUserMsg();
		}
	} else {
		$("#ddFilterByStatus").val('Filter');
		activeUsersList = jsonData["activeUsersList"];
	}
	activeUsersList = objCommon.sortByKey(activeUsersList,
	'updatedDate');
	var lenActiveUsersList = activeUsersList.length;
	$("#totalAvailableUserCount").text(lenActiveUsersList + " " + getUiProps().LBL0027);
	uAdministratorManagement.enableCreateUserBtn();
	var maxUserLimit = getUiProps().MSG0170;
	var loggedInUsername = sessionStorage.userID;
	var totalUserLength = sessionStorage.adminGridDataLength;
	if (totalUserLength >= maxUserLimit) {
		uAdministratorManagement.disableCreateUserBtn();
	}
	for ( var count = 0; count < lenActiveUsersList; count++) {
		var username = activeUsersList[count].username;
		var firstName = activeUsersList[count].firstName;
		var familyName = activeUsersList[count].familyName;
		
		var name = firstName + " " + familyName;
		var verificationSendAtDate = activeUsersList[count].verificationSendAtDate;
		var userStatus = activeUsersList[count].userStatus;
		var userOrgID = activeUsersList[count].userOrgID;
		var verifiedAtDate = activeUsersList[count].verifiedAtDate;
		var lastLoginAtDate = activeUsersList[count].lastLoginAtDate;
		var selectedUsername1 = "'" + username + "'";
		var email = activeUsersList[count].email;
		var privilege = activeUsersList[count].privilege;
		var userPrivilege = "'" + privilege + "'";
		var lockStatus = activeUsersList[count].lockStatus;
		if (privilege == "user") {
			privilege = "";
		}
		if (totalUserLength == 1 && username == loggedInUsername) {
			$("#chkSelectallAdmin").attr('disabled', true);
			$("#ddFilterByStatus").attr('disabled', true);
		}
		var registrationDate = activeUsersList[count].registrationDate;
		var updatedDate = activeUsersList[count].updatedDate;
		var registrationSeparator = registrationDate.lastIndexOf(".");
		var registrationDBDateFormat = registrationDate.substring(0, registrationSeparator);
		var updatedSeparator = updatedDate.lastIndexOf(".");
		var updatedDBDateFormat = updatedDate.substring(0, updatedSeparator);
		if (lastLoginAtDate != null) {
			var lastSignedInSeparator = lastLoginAtDate.lastIndexOf(".");
			var lastSignedInDBDateFormat = lastLoginAtDate.substring(0, lastSignedInSeparator);
			lastLoginAtDate = lastSignedInDBDateFormat;
		}
		var modifiedFamilyName = familyName.replace(/"/g, "↔");
		var modifiedFirstName = firstName.replace(/"/g, "↔");
		dynamicUserList += '<tr id="adminRowID_'+count+'" onclick="uAdministratorManagement.rowSelectGrid('+ count +',' + lenActiveUsersList + ','+ "'#adminMgmntDeleteIcon'" + ' ,'+ "'#adminMgmntEditIcon'" + ', '+ "'#adminMgmntPasswordSettingsIcon'" + ','+selectedUsername1+','+userPrivilege+');"><input type="hidden" id = "dvFamilyNameAdminTable'+count+'" value="'+modifiedFamilyName+'"/><input type="hidden" id = "dvFirstNameAdminTable'+count+'" value="'+modifiedFirstName+'"/>';
		//dynamicUserList += '<tr id="adminRowID_'+count+'" onclick="objCommon.rowSelect(this,'+ "'adminRowID_'" +','+ "'chkBox'"+','+ "'row'" +','+ "'adminMgmntDeleteIcon'" +','+ "'chkSelectallAdmin'" +','+ count +',' + lenActiveUsersList + ','+ "'adminMgmntEditIcon'" + ','+"''"+','+"''"+','+undefined+','+"'adminMgmtTable'"+');">';
		dynamicUserList = this.createRowsForUsersDetailsTable(dynamicUserList,
				username, firstName, familyName, email, privilege,
				registrationDBDateFormat, updatedDBDateFormat, lockStatus, count, name, verificationSendAtDate, userStatus, userOrgID, verifiedAtDate, lastLoginAtDate);
	}
	$("#adminMgmtTable tbody").html(dynamicUserList);
	$(".noRowSelectLoggedInUser").parent().addClass('loggedInUser');
};

/* ***************** ADMINISTRATOR MANAGEMENT: VIEW USER DETAILS: END ******************/

/* ***************** ADMINISTRATOR MANAGEMENT: EDIT USER DETAILS: START ******************/

/**
 * The purpose of this function is to get existing user information.
 */
administratorManagement.prototype.getUserInformation = function(username, firstName, familyName, email, privilege) {
	uAdministratorManagement.dislayExistingUserInfo(username, firstName, familyName, email, privilege);
	//changes done to fix query parameter issue for AdministratorManagement case
	/*var operation = "GET_EDIT_USER_DETAILS";
	$.ajax({
		data : {
			operation : operation,
			username : username
		},
		dataType : 'json',
		url : '../AdministratorManagement',
		type : 'GET',
		beforeSend: function (xhr) {
			xhr.setRequestHeader("Cache-Control", "no-cache, no-store, must-revalidate");
				xhr.setRequestHeader("Pragma", "no-cache");
				xhr.setRequestHeader("Expires", 0);
			},
		async : false,
		cache : false,
		success : function(jsonData) {
			if (jsonData['sessionTimeOut'] == "sessionTimeOut") {
				uCreateEnvironment.performLogout();
			} else {
				uAdministratorManagement.dislayExistingUserInfo(jsonData,
						username);
			}
		},
		error : function() {
		}
	});*/
};

/**
 * The purpose of this function is to empty all field values of edit user pop
 * up.
 */
administratorManagement.prototype.emptyEditUserPopupFieldValues = function() {
	uAdministratorManagement.checkSessionExistence();
	$('#popupAdminMgmntEditFirstNameErrorMsg').html('');
	$('#popupAdminMgmntEditFamilyNameErrorMsg').html('');
	$('#popupAdminMgmntEditEmailErrorMsg').html('');
	$('#popupAdminMgmntEditReEnterEmailErrorMsg').html('');
	$("#txtAdminMgmntFirstName").removeClass('errorIcon');
	$("#txtAdminMgmntFamilyName").removeClass('errorIcon');
	$("#txtAdminMgmntEmail").removeClass('errorIcon');
	$("#txtAdminMgmntReEnterEmail").removeClass('errorIcon');
	$("#txtAdminMgmntFirstName").removeClass('validValueIcon');
	$("#txtAdminMgmntFamilyName").removeClass('validValueIcon');
	$("#txtAdminMgmntEmail").removeClass('validValueIcon');
	$("#txtAdminMgmntReEnterEmail").removeClass('validValueIcon');
};

/**
 * The purpose of this function is to display existing user information on edit
 * pop up.
 * 
 * @param jsonData
 * @param username
 */
administratorManagement.prototype.dislayExistingUserInfo = function(username, firstName, familyName, email, privilege) {
	/*if (jsonData['sessionTimeOut'] == "sessionTimeOut") {
		uCreateEnvironment.performLogout();
	}*/
	uAdministratorManagement.emptyEditUserPopupFieldValues();
	var ADMIN_PRIVILEGE = getUiProps().MSG0159;
	var existingUserFirstName = firstName;//jsonData["existingUserFirstName"];
	var existingUserFamilyName = familyName;//jsonData["existingUserFamilyName"];
	var existingUserEmail = email;//jsonData["existingUserEmail"];
	var existingUserPrivilege = privilege;//jsonData["existingUserPrivilege"];
	var oFamilyName = existingUserFamilyName.replace(/↔/g, '"');
	var oFirstName = existingUserFirstName.replace(/↔/g, '"');
	$("#adminMgmntEditPopupUsername").val(username);
	$("#txtAdminMgmntFirstName").val(oFirstName);
	$("#txtAdminMgmntFamilyName").val(oFamilyName);
	$("#txtAdminMgmntEmail").val(existingUserEmail);
	$("#txtAdminMgmntReEnterEmail").val(existingUserEmail);
	//$("#checkboxEditkAdminMgmnt").attr('checked', false);
	if (existingUserPrivilege == ADMIN_PRIVILEGE) {
		$("#rdEditYes").attr('checked', true);
	}else{
		$("#rdEditNo").attr('checked', true);
	}
};

/**
 * The purpose of this function is to post new information for a user to the
 * server.
 * 
 * @param userName
 * @param firstName
 * @param familyName
 * @param emailID
 * @param privilege
 */
administratorManagement.prototype.postEditUserInformation = function(userName,
		firstName, familyName, emailID, privilege, isEmailChanged) {
	var encodedFirstName = encodeURI(firstName);
	var encodedFamilyName = encodeURI(familyName);
	var operation = "UPDATE_USER_DETAILS";
	var CSRFTokenEditUser = document.getElementById("CSRFTokenEditUser").value;
	var encodedNewEAddress = encodeURIComponent(emailID);
	$.ajax({
		data : {
			operation : operation,
			userName : userName,
			newFirstName : encodedFirstName,
			newFamilyName : encodedFamilyName,
			encodedNewEAddress : encodedNewEAddress,
			newPrivilege : privilege,
			CSRFTokenEditUser : CSRFTokenEditUser
		},
		dataType : 'json',
		url : '../../AdministratorManagement',
		type : 'POST',
		/*beforeSend: function (xhr) {
			xhr.setRequestHeader("Cache-Control", "no-cache, no-store, must-revalidate");
				xhr.setRequestHeader("Pragma", "no-cache");
				xhr.setRequestHeader("Expires", 0);
			},*/
		async : false,
		cache : false,
		success : function(jsonData) {
			if (jsonData['sessionTimeOut'] == "sessionTimeOut") {
				uCreateEnvironment.performLogout();
			} else if (jsonData['adminMgmntEditUserInfo'] == "success") {
				if (isEmailChanged) {
					uAdministratorManagement.sendVerificationMail(userName, emailID, firstName, familyName, true);
				} else {
					uAdministratorManagement.displayNotificationMessage("EDIT", "editUserAdminMgmntModal", "", "21%");
				}
			}
		},
		error : function() {
		}
	});
};

/**
 * The purpose of this function is to edit user information.
 */
administratorManagement.prototype.editAdminMgmntUserInfo = function() {
	var userName = $("#adminMgmntEditPopupUsername").val();
	var firstName = $("#txtAdminMgmntFirstName").val();
	var familyName = $("#txtAdminMgmntFamilyName").val();
	var emailID = $("#txtAdminMgmntEmail").val();
	var reEnterEmailID = $("#txtAdminMgmntReEnterEmail").val();
	var firstNameSpanID = "popupAdminMgmntEditFirstNameErrorMsg";
	var familyNameSpanID = "popupAdminMgmntEditFamilyNameErrorMsg";
	var emailIDSpanID = "popupAdminMgmntEditEmailErrorMsg";
	var reEntermailIDSpanID = "popupAdminMgmntEditReEnterEmailErrorMsg";
	var familyNameID = "txtAdminMgmntFamilyName";
	var firstNameID = "txtAdminMgmntFirstName";
	var emailInputID = "txtAdminMgmntEmail";
	var reEnterEmailInputID = "txtAdminMgmntReEnterEmail";
	var privilege = getUiProps().MSG0158;
	var isEmailChanged = false;
	if ($("#rdEditYes").is(':checked')) {
		privilege = getUiProps().MSG0159;
	}
	if (sessionStorage.oldEmail != emailID) {
		isEmailChanged = true;
	}
	var objEditUserInformation = new editUserInformation();
	if (objEditUserInformation.validateEditPopupFields(firstName,
			firstNameSpanID, familyName, familyNameSpanID, emailID,
			emailIDSpanID, familyNameID, firstNameID, emailInputID, reEnterEmailID, reEntermailIDSpanID, reEnterEmailInputID)) {
		uAdministratorManagement.postEditUserInformation(userName, firstName,
				familyName, emailID, privilege, isEmailChanged);
	}
};

/* ***************** ADMINISTRATOR MANAGEMENT: EDIT USER DETAILS: END ******************/

/* ***************** ADMINISTRATOR MANAGEMENT: PASSWORD SETTINGS : START ******************/

/**
 * The purpose of this function is to empty password settings pop up field
 * values.
 */
administratorManagement.prototype.emptyPassSettingsPopupFieldValues = function() {
	uAdministratorManagement.checkSessionExistence();
	$("#txtPassSettingsNewPassword").val('');
	$("#txtPassSettingsRetypePassword").val('');
	$('#popupPassSettingsNewPassErrorMsg').html('');
	$('#popupPassSettingsRetypePassErrorMsg').html('');
	$("#txtPassSettingsNewPassword").removeClass('errorIconBig');
	$("#txtPassSettingsRetypePassword").removeClass('errorIconBig');
	$("#txtPassSettingsNewPassword").removeClass('validValueIconBig');
	$("#txtPassSettingsRetypePassword").removeClass('validValueIconBig');
};

/**
 * The purpose of this function is to change password for existing user.
 */
administratorManagement.prototype.passwordSettings = function() {
	var newPassword = $("#txtPassSettingsNewPassword").val();
	var newRetypePassword = $("#txtPassSettingsRetypePassword").val();
	var newPasswordSpanID = "popupPassSettingsNewPassErrorMsg";
	var newRetypePasswordSpanID = "popupPassSettingsRetypePassErrorMsg";
	var userName = sessionStorage.usernamePassSettings;
	document.getElementById(newRetypePasswordSpanID).innerHTML = "";
	if (uAdministratorManagement.validatePassword(newPassword,
			newPasswordSpanID, userName, "txtPassSettingsNewPassword")) {
		document.getElementById(newPasswordSpanID).innerHTML = "";
		if (uAdministratorManagement.validateRetypePassword(newRetypePassword,
				newRetypePasswordSpanID, newPassword, "txtPassSettingsRetypePassword")) {
			document.getElementById(newRetypePasswordSpanID).innerHTML = "";
			uAdministratorManagement.postNewPassword(userName, newPassword);

		}
	}
};

/**
 * The purpose of this function is to post new password for a user to the
 * server.
 * 
 * @param userName
 * @param newPassword
 */
administratorManagement.prototype.postNewPassword = function(userName,
		newPassword) {
	var operation = "PASSWORD_SETTINGS";
	var CSRFTokenPassword = document.getElementById("CSRFTokenPassword").value;
	$.ajax({
		data : {
			operation : operation,
			userName : userName,
			newPassword : newPassword,
			CSRFTokenPassword : CSRFTokenPassword
		},
		dataType : 'json',
		url : '../../AdministratorManagement',
		type : 'POST',
		/*beforeSend: function (xhr) {
			xhr.setRequestHeader("Cache-Control", "no-cache, no-store, must-revalidate");
				xhr.setRequestHeader("Pragma", "no-cache");
				xhr.setRequestHeader("Expires", 0);
			},*/
		async : false,
		cache : false,
		success : function(jsonData) {
			if (jsonData['sessionTimeOut'] == "sessionTimeOut") {
				uCreateEnvironment.performLogout();
			} else if (jsonData['passwordUpdateStatus'] == true) {
				/*uAdministratorManagement.displayNotificationMessage(
						"PASSWORD_SETTINGS", "changePassAdminMgmntModal");*/
				uAdministratorManagement.sendLoginCredentialsMail(userName, newPassword);
			}
		},
		error : function() {
		}
	});
};


 /* ***************** ADMINISTRATOR MANAGEMENT: PASSWORD SETTINGS : END ******************/

/* ******************* ADMINISTRATOR MANAGEMENT: DELETE USER : START ********************/

/**
 * The purpose of this function is to delete selected user.
 */
administratorManagement.prototype.deleteSelectedUser = function(deleteUsername) {
	//var deleteUsername = $("#deleteUsername").text();
	var length  = arrDeletedUserName.length;
	for (var i= 0; i < length; i++ ) {
		uAdministratorManagement.postDeleteUsername(arrDeletedUserName[i]);
	}
	/*uAdministratorManagement.displayNotificationMessage(
			"DELETE_USER", "userDeleteAdminMgmntModalWindow");*/
	uAdministratorManagement.sendDeletedUserList(arrSuccessfulDeletedUsernames);
	
};

/**
 * The purpose of this function is to post delete username to the server.
 */
administratorManagement.prototype.postDeleteUsername = function(deleteUsername) {
	var operation = "DELETE_USER";
	var CSRFTokenDeleteUser = document.getElementById("CSRFTokenDeleteUser").value;
	$.ajax({
		data : {
			operation : operation,
			deleteUsername : deleteUsername,
			CSRFTokenDeleteUser : CSRFTokenDeleteUser
		},
		dataType : 'json',
		url : '../../AdministratorManagement',
		type : 'POST',
		/*beforeSend: function (xhr) {
			xhr.setRequestHeader("Cache-Control", "no-cache, no-store, must-revalidate");
				xhr.setRequestHeader("Pragma", "no-cache");
				xhr.setRequestHeader("Expires", 0);
			},*/
		async : false,
		cache : false,
		success : function(jsonData) {
			if (jsonData['sessionTimeOut'] == "sessionTimeOut") {
				uCreateEnvironment.performLogout();
			} else if (jsonData['deleteStatus'] == true) {
				arrSuccessfulDeletedUsernames.push(deleteUsername);
				sbSuccessfulUserDeleteCount++;
				/*uAdministratorManagement.displayNotificationMessage(
						"DELETE_USER", "userDeleteAdminMgmntModalWindow");*/
			}
		},
		error : function() {
		}
	});
};

/**
 * ***************** ADMINISTRATOR MANAGEMENT: DELETE USER : END
 * *****************
 */

/**
 * The purpose of this function is to display notification message.
 */
administratorManagement.prototype.displayNotificationMessage = function(
		operation, modalId, createSuccessOrFailure, dynamicMargin) {
	if (operation == "CREATE" && modalId == "createNewUserModal") {
		var message = null;
		if (createSuccessOrFailure != undefined) {
			if (createSuccessOrFailure == "createAndMailSentSuccess") {
				message = getUiProps().MSG0201;
			} else if (createSuccessOrFailure == "createSuccessMailSentFailure") {
				message = getUiProps().MSG0306;
			}
		}
		$("#createNewUserModal").hide();
		//var message = getUiProps().MSG0201;
		uAdministratorManagement.showSuccessMessage(message, dynamicMargin);
		//objCommon.displaySuccessMessage("#" + modalId, message, "");
		uAdministratorManagement.fetchActiveUsersList();
		//objCommon.centerAlignRibbonMessage("#successMsgDiv");
		objCommon.autoHideAssignRibbonMessage("successMsgDiv");
	}
	if (operation == "EDIT" && modalId == "editUserAdminMgmntModal") {
		$("#editUserAdminMgmntModal").hide();
		var message = getUiProps().MSG0202;
		if (createSuccessOrFailure != undefined) {
			if (createSuccessOrFailure == "editMailAndMailSentSuccess") {
				message = getUiProps().MSG0314;
			} else if (createSuccessOrFailure == "editMailAndMailSentFailure") {
				message = getUiProps().MSG0315;
			}
		}
		uAdministratorManagement.showSuccessMessage(message, dynamicMargin);
		//objCommon.displaySuccessMessage("#" + modalId, message, "");
		uAdministratorManagement.fetchActiveUsersList();
		//objCommon.centerAlignRibbonMessage("#successMsgDiv");
		objCommon.autoHideAssignRibbonMessage("successMsgDiv");
	}
	if (operation == "PASSWORD_SETTINGS"
			&& modalId == "changePassAdminMgmntModal") {
		var message = null;
		if (createSuccessOrFailure != undefined) {
			if (createSuccessOrFailure == "changePasswordAndMailSentSuccess") {
				message = getUiProps().MSG0203;
			} else if (createSuccessOrFailure == "changePasswordSuccessAndMailSentFailure") {
				message = getUiProps().MSG0308;
			}
		}
		$("#changePassAdminMgmntModal").hide();
		uAdministratorManagement.showSuccessMessage(message, dynamicMargin);
		//objCommon.displaySuccessMessage("#" + modalId, message, "");
		uAdministratorManagement.fetchActiveUsersList();
		//objCommon.centerAlignRibbonMessage("#successMsgDiv");
		objCommon.autoHideAssignRibbonMessage("successMsgDiv");
		objCommon.autoHideAssignRibbonMessage("successMsgDiv1");
	}
	if (operation == "DELETE_USER"
			&& modalId == "userDeleteAdminMgmntModalWindow") {
		var message = null;
		if (createSuccessOrFailure != undefined) {
			if (createSuccessOrFailure == "deletedUserMailSentSuccess") {
				message = getUiProps().MSG0309;
			} else if (createSuccessOrFailure == "deletedUserMailSentFailure") {
				message = getUiProps().MSG0310;
			}
		}
		$("#userDeleteAdminMgmntModalWindow").hide();
		//var message = getUiProps().MSG0204;
		uAdministratorManagement.showSuccessMessage(sbSuccessfulUserDeleteCount+" "+message, dynamicMargin);
		//objCommon.displaySuccessMessage("#" + modalId, message, "");
		uAdministratorManagement.fetchActiveUsersList();
		//objCommon.centerAlignRibbonMessage("#successMsgDiv");
		objCommon.autoHideAssignRibbonMessage("successMsgDiv");
		$("#chkSelectallAdmin").attr('checked', false);
		arrDeletedUserName.length = 0;
		sbSuccessfulUserDeleteCount = 0;
		arrSuccessfulDeletedUsernames.length = 0;
	}
	if (operation == "ACTIVATE_USER") {
	$("#userActivateAdminMgmntModalWindow").hide();
	var message = getUiProps().MSG0307;
	uAdministratorManagement.showSuccessMessage(sbSuccessfulUserActivateCount+" "+message, dynamicMargin);
	uAdministratorManagement.fetchActiveUsersList();
	objCommon.centerAlignRibbonMessage("#successMsgDiv");
	objCommon.autoHideAssignRibbonMessage("successMsgDiv");
	$("#chkSelectallAdmin").attr('checked', false);
	sbSuccessfulUserActivateCount = 0;
}
};

/**
 * The purpose of this function is to check session existence.
 */
administratorManagement.prototype.checkSessionExistence = function() {
	var id = objCommon.isSessionExist();
	if (id == null) {
		var contextRoot = sessionStorage.contextRoot;
		window.location.href = contextRoot;
	}
};

/**
 * The purpose of this function is to open modal pop up.
 * 
 * @param idDialogBox
 * @param idModalWindow
 * @param username
 * @param operationType
 */
administratorManagement.prototype.openPopUpWindow = function(idDialogBox,
		idModalWindow, operationType) {
	//, username, firstName, familyName, email, privilege
	var paramArray = uAdministratorManagement.getUserDetails();
	var noOfRecords = paramArray.length;
	if (operationType == "EDIT") {
		var username = null;
		var email = null;
		var firstName = null;
		var familyName = null;
		var privilege = null;
		for(var index = 0; index < noOfRecords; index++){
			username = paramArray[index]["UserName"];
			email = paramArray[index]["Email"];
			firstName = paramArray[index]["FirstName"];
			familyName = paramArray[index]["FamilyName"];
			privilege = paramArray[index]["Privilege"];
			sessionStorage.oldEmail = email;
		}
		uAdministratorManagement.checkSessionExistence();
		var reuestID = sessionStorage.requestId;
		$("#CSRFTokenEditUser").val(reuestID);
		uAdministratorManagement.getUserInformation(username, firstName, familyName, email, privilege);
		$("#txtAdminMgmntFirstName").focus();
	}
	if (operationType == "PASSWORD_SETTINGS") {
		var reuestID = sessionStorage.requestId;
		$("#CSRFTokenPassword").val(reuestID);
		$("#CSRFTokenSendMailLoginCredential").val(reuestID);
		var deletedUsername = paramArray[0]["UserName"];
		var firstName = paramArray[0]["FirstName"];
		var familyName = paramArray[0]["FamilyName"];
		var emailVerified = paramArray[0]["EmailVerified"];
        if (emailVerified == "No") {
               $("#lblChangePasswordWarning").text(getUiProps().MSG0408);
        } else {
               $("#lblChangePasswordWarning").text(" ");
        }

		var displayName = firstName + " " + familyName + " (" + deletedUsername + ")";
		$("#passwordSettingUsername").text(displayName);
		$("#passwordSettingUsername").attr('title', displayName);
		sessionStorage.usernamePassSettings = deletedUsername;
		uAdministratorManagement.emptyPassSettingsPopupFieldValues();
		uAdministratorManagement.defaultStateResetPassword();
	}
	if (operationType == "DELETE_USER") {
		var reuestID = sessionStorage.requestId;
		$("#CSRFTokenDeleteUser").val(reuestID);
		uAdministratorManagement.checkSessionExistence();
		for(var index = 0; index < noOfRecords; index++){
			var deletedUsername = paramArray[index]["UserName"];
			arrDeletedUserName.push(deletedUsername);
		}
		//$("#deleteUsername").text(deletedUsername);
	} if (operationType == "ACTIVATE_USER") {
		var reuestID = sessionStorage.requestId;
		$("#CSRFTokenActivateUser").val(reuestID);
		$("#dynamicActivationText").css("margin-bottom", "64px");
		$("#dvDynamicActivateUserStatusMsg").hide();
        for(var index = 0; index < noOfRecords; index++){
               if (paramArray[index]["EmailVerified"] == "No") {
            	   $("#dvDynamicActivateUserStatusMsg").show();
            	   $("#dynamicActivationText").css("margin-bottom","");
               }
        }
	}
	$("#" + idModalWindow).fadeIn(1000);
	var windowHeight = $(window).height();
	var windowWidth = $(window).width();
	$("#" + idDialogBox).css('top',
			windowHeight / 2 - $("#" + idDialogBox).height() / 2);
	$("#" + idDialogBox).css('left',
			windowWidth / 2 - $("#" + idDialogBox).width() / 2);
	if (operationType == "EDIT") {
		$("#txtAdminMgmntFirstName").focus();
	}
	if (operationType == "PASSWORD_SETTINGS") {
		$("#txtPassSettingsNewPassword").focus();
	}
	if (operationType == "DELETE_USER") {
		$("#btnCancelDeleteUser").focus();
	}
	if (operationType == "ACTIVATE_USER") {
		$("#btnCancelAdminActivateUser").focus();
	}
};

administratorManagement.prototype.adminCheckAll = function (cBox) {
	if (cBox.checked == true) {
		$("#adminMgmtTable tbody tr ").addClass('selectRow');
		$(".chkClass").attr('checked', true); 
		$(".noRowSelect").parent().removeClass('selectRow');
		var chkAllArray = [];
		$(".chkClass:checked").each(function() {
			chkAllArray.push($(this).val());
		});
		var isInactive = $.inArray("inactive", chkAllArray) > -1;
		var isActive = $.inArray("active", chkAllArray) > -1;
		uAdministratorManagement.enableDeleteButton("#adminMgmntDeleteIcon", "deleteIconEnabled", "deleteIconDisabled", isInactive);
		uAdministratorManagement.enableActivateButton("#adminMgmntActivateIcon", "activateUserIconEnabled", "activateUserIconDisabled", isActive);
		uAdministratorManagement.disableButton("#adminMgmntEditIcon", "editIconEnabled", "editIconDisabled");
		uAdministratorManagement.disableButton("#adminMgmntPasswordSettingsIcon", "adminMgmntPassSettingsIcon", "adminMgmntPassSettingsIconDisabled");
	} else {
		$("#adminMgmtTable tbody tr ").removeClass('selectRow');
		$(".chkClass").attr('checked', false);
		uAdministratorManagement.disableButton("#adminMgmntDeleteIcon", "deleteIconEnabled", "deleteIconDisabled");
		uAdministratorManagement.disableButton("#adminMgmntEditIcon", "editIconEnabled", "editIconDisabled");
		uAdministratorManagement.disableButton("#adminMgmntPasswordSettingsIcon", "adminMgmntPassSettingsIcon", "adminMgmntPassSettingsIconDisabled");
		uAdministratorManagement.disableButton("#adminMgmntActivateIcon", "activateUserIconEnabled", "activateUserIconDisabled");
	}
	
	/*var buttonId = '#adminMgmntDeleteIcon';
	objCommon.checkBoxSelect(cBox, buttonId,'#adminMgmntEditIcon');
	objCommon.showSelectedRow(document.getElementById("chkSelectallAdmin"),"row","adminRowID_");
	$("#adminMgmntEditIcon").removeClass();
	$("#adminMgmntEditIcon").addClass('editIconDisabled');*/
	var noOfRecords = $("#adminMgmtTable > tbody > tr").length;
	if ($("#chkSelectallAdmin").is(':checked')) {
		if (noOfRecords == 1) {
			uAdministratorManagement.enableButton("#adminMgmntEditIcon", "editIconEnabled", "editIconDisabled");
			uAdministratorManagement.enableButton("#adminMgmntPasswordSettingsIcon", "adminMgmntPassSettingsIcon", "adminMgmntPassSettingsIconDisabled");
		}
	}
};

/**
 * The purpose of the following method is to perform as row select event in the grid. 
 * @param count
 * @param totalRecordsize
 */
administratorManagement.prototype.rowSelectGrid = function(count, totalRecordsize, deleteButtonID, editButtonID, changePasswordID, selectedUsername, userPrivilege) {
	var loggedInUsername = sessionStorage.userID;
	var SUBSCRIBER_PRIVILEGE = getUiProps().MSG0169;
	if (selectedUsername != loggedInUsername && userPrivilege != SUBSCRIBER_PRIVILEGE) {
		if (event.target.tagName.toUpperCase() !== "INPUT") {
			if ($(event.target).is('.customChkbox')	&& event.target.tagName.toUpperCase() === "LABEL") {
				var chkID = $("#chkBox" + count);
				var rowID = $("#adminRowID_" + count);
				var obj = rowID;
				if ($(chkID).is(':checked')) {
					obj.removeClass('selectRow');
					$(chkID).attr('checked', true);
				} else {
					obj.addClass('selectRow');
					$(chkID).attr('checked', false);
				}
				
			} else {
				var chkID = $("#chkBox" + count);
				var rowID = $("#adminRowID_" + count);
				var obj = rowID;
				obj.siblings().removeClass('selectRow');
				obj.addClass('selectRow');
				for ( var index = 0; index < totalRecordsize; index++) {
					if (index != count) {
						$("#chkBox" + index).attr('checked', false);
					}
				}
				$(chkID).attr('checked', true);
				
			}
		}
		var chkArray = [];
		$(".chkClass:checked").each(function() {
			chkArray.push($(this).val());
		});
		var numberOfChecked = 0;
		for ( var index = 0; index < totalRecordsize; index++) {
			if ($("#chkBox" + index).is(':checked')) {
				numberOfChecked++;
			}
		}
		if (totalRecordsize == numberOfChecked) {
			$("#chkSelectallAdmin").attr('checked', true);
		} else {
			$("#chkSelectallAdmin").attr('checked', false);
		}
		if (numberOfChecked > 0) {
			var isInactive = $.inArray("inactive", chkArray) > -1;
			var isActive = $.inArray("active", chkArray) > -1;
			//uAdministratorManagement.enableButton(deleteButtonID, "deleteIconEnabled", "deleteIconDisabled");
			uAdministratorManagement.enableDeleteButton(deleteButtonID, "deleteIconEnabled", "deleteIconDisabled", isInactive);
			uAdministratorManagement.enableActivateButton("#adminMgmntActivateIcon", "activateUserIconEnabled", "activateUserIconDisabled", isActive);
			if (numberOfChecked == 1) {
				uAdministratorManagement.enableButton(editButtonID, "editIconEnabled", "editIconDisabled");
				uAdministratorManagement.enableButton(changePasswordID, "adminMgmntPassSettingsIcon", "adminMgmntPassSettingsIconDisabled");
			} else if (numberOfChecked > 1) {
				uAdministratorManagement.disableButton(editButtonID, "editIconEnabled", "editIconDisabled");
				uAdministratorManagement.disableButton(changePasswordID, "adminMgmntPassSettingsIcon", "adminMgmntPassSettingsIconDisabled");
			}
		} else {
			uAdministratorManagement.disableButton(deleteButtonID, "deleteIconEnabled", "deleteIconDisabled");
			uAdministratorManagement.disableButton(editButtonID, "editIconEnabled", "editIconDisabled");
			uAdministratorManagement.disableButton(changePasswordID, "adminMgmntPassSettingsIcon", "adminMgmntPassSettingsIconDisabled");
			uAdministratorManagement.disableButton("#adminMgmntActivateIcon", "activateUserIconEnabled", "activateUserIconDisabled");
		}
	}
};

/**
 * This function posts login credentials.
 * @param username
 * @param password
 */
administratorManagement.prototype.sendLoginCredentialsMail = function(username,
		password) {
	var operation = "LOGIN_CREDENTIALS";
	var CSRFTokenSendMailLoginCredential = document
			.getElementById("CSRFTokenSendMailLoginCredential").value;
	$.ajax({
		data : {
			operation : operation,
			username : username,
			password : password,
			CSRFTokenSendMailLoginCredential : CSRFTokenSendMailLoginCredential
		},
		url : '../../MailServlet',
		dataType : 'json',
		type : 'POST',
		async : false,
		cache : false,
		success : function(jsonData) {
			if (jsonData['sessionTimeOut'] == "sessionTimeOut") {
				uCreateEnvironment.performLogout();
			} else if (jsonData['loginCredentialsMail'] == "loginCredentialsMailSentSuccess") {
				uAdministratorManagement.displayNotificationMessage(
						"PASSWORD_SETTINGS",
						"changePassAdminMgmntModal",
						"changePasswordAndMailSentSuccess", "16%");
			} else if (jsonData['loginCredentialsMail'] == "loginCredentialsMailSentFaliure") {
				uAdministratorManagement.displayNotificationMessage(
						"PASSWORD_SETTINGS",
						"changePassAdminMgmntModal",
						"changePasswordSuccessAndMailSentFailure", "19%");
			}
		},
		error : function() {
			uAdministratorManagement.displayNotificationMessage(
					"PASSWORD_SETTINGS", "changePassAdminMgmntModal",
					"changePasswordSuccessAndMailSentFailure", "16%");
		}
	});

};

/**
 * This functions posts deleted user list.
 * 
 * @param deletedUserList
 */
administratorManagement.prototype.sendDeletedUserList = function(
		deletedUserList) {
	var operation = "DELETED_USERS_LIST_MAIL";
	var CSRFTokenDeleteUser = document.getElementById("CSRFTokenDeleteUser").value;
	$.ajax({
		data : {
			operation : operation,
			deletedUserList : deletedUserList,
			CSRFTokenDeleteUser : CSRFTokenDeleteUser
		},
		url : '../../MailServlet',
		dataType : 'json',
		type : 'POST',
		async : false,
		cache : false,
		success : function(jsonData) {
			if (jsonData['sessionTimeOut'] == "sessionTimeOut") {
				uCreateEnvironment.performLogout();
			} else if (jsonData['isDeletedUserMailSentSuccess'] == "deletedUserMailSentSuccess") {
				uAdministratorManagement.displayNotificationMessage(
						"DELETE_USER",
						"userDeleteAdminMgmntModalWindow",
						"deletedUserMailSentSuccess", "16%");
			} else if (jsonData['isDeletedUserMailSentSuccess'] == "deletedUserMailSentFailure") {
				uAdministratorManagement.displayNotificationMessage(
						"DELETE_USER",
						"userDeleteAdminMgmntModalWindow",
						"deletedUserMailSentFailure","20%");
			}
		},
		error : function() {
			uAdministratorManagement.displayNotificationMessage(
					"PASSWORD_SETTINGS", "changePassAdminMgmntModal",
					"changePasswordSuccessAndMailSentFailure","16%");
		}
	});

};

/**
 * This functions disables Delete Icon.
 */
administratorManagement.prototype.enableButton = function(id, enableClass, disableClass) {
  $(id).removeAttr("disabled");
  $(id).removeClass(disableClass);
  $(id).addClass(enableClass);
};

/**
 * The purpose of this function is to disable delete button for multiple delete
 */
administratorManagement.prototype.disableButton = function(id, enableClass, disableClass) {
  $(id).attr('disabled', true);
  $(id).removeClass(enableClass);
  $(id).addClass(disableClass);
};

/**
 * The purpose of this function is to get role information for multiple delete.
 */
administratorManagement.prototype.getUserDetails = function() { 
	var noOfRecords = $("#adminMgmtTable tr").length - 1;
	var userDetailsArray = [];
	var loggedInUsername = sessionStorage.userID;
	for (var index = 0; index < noOfRecords; index++) {
		var body = {};
		if($("#chkBox"+index).is(":checked")){
			body["UserName"] = document.getElementById("dvUserNameAdminTable" + index).title;
			body["Email"] = document.getElementById("dvEmailAdminTable" + index).title;
			body["FirstName"] = document.getElementById("dvFirstNameAdminTable" + index).value;
			body["FamilyName"] = document.getElementById("dvFamilyNameAdminTable" + index).value;
			body["Privilege"] = document.getElementById("dvPrivilegeAdminTable" + index).innerHTML;
			body["EmailVerified"] = document.getElementById("dvEmailVerifiedAdminTable" + index).innerHTML;
			if (body["UserName"] != loggedInUsername) {
				userDetailsArray.push(body);
			}
		}
	}
	return userDetailsArray;
};

/**
 * This function auto-generates password.
 */
administratorManagement.prototype.autoGeneratePassword = function() {
	if($("#divAutoGenPassword").hasClass("selectedPasswordSetting")) {
		$("#divAutoGenPassword").removeClass("selectedPasswordSetting");
		$("#divAutoGenPassword").addClass("unselectedPasswordSetting");
		$("#divSetPassword").removeClass("unselectedPasswordSetting");
		$("#divSetPassword").addClass("selectedPasswordSetting");
		uAdministratorManagement.emptyPassSettingsPopupFieldValues();
		var text = "";
		//var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%&*_-";
		var characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		var numbers = "0123456789";
		var specialCharacters = "!@#$%&*_-";
		for( var i=0; i < 7; i++ ) {
			text += characters.charAt(Math.floor(Math.random() * characters.length));
		}
		for( var k=0; k < 2; k++ ) {
			text += specialCharacters.charAt(Math.floor(Math.random() * numbers.length));
		}
		for( var j=0; j < 3; j++ ) {
			text += numbers.charAt(Math.floor(Math.random() * specialCharacters.length));
		}
		$("#txtPassSettingsNewPassword").val(text);
		$("#txtPassSettingsRetypePassword").val(text);
	}
};

/**
* This functions enable/disable Delete Icon.
*/
administratorManagement.prototype.enableDeleteButton = function(id, enableClass, disableClass, isInactive) {
	if (isInactive == false) {
		$(id).removeAttr("disabled");
		$(id).removeClass(disableClass);
		$(id).addClass(enableClass);
	} else {
		$(id).attr('disabled', true);
		$(id).removeClass(enableClass);
		$(id).addClass(disableClass);
	}
};

/**
* This functions enable/disable Activate Icon.
*/
administratorManagement.prototype.enableActivateButton = function(id, enableClass, disableClass, isActive) {
	if (isActive == false) {
		$(id).removeAttr("disabled");
		$(id).removeClass(disableClass);
		$(id).addClass(enableClass);
	} else {
		$(id).attr('disabled', true);
		$(id).removeClass(enableClass);
		$(id).addClass(disableClass);
	}
};

/**
 * This function modifies popup on click of set pasword
 * option.
 */
administratorManagement.prototype.setPassword = function() {
	$("#divAutoGenPassword").removeClass("unselectedPasswordSetting");
	$("#divAutoGenPassword").addClass("selectedPasswordSetting");
	$("#divSetPassword").removeClass("selectedPasswordSetting");
	$("#divSetPassword").addClass("unselectedPasswordSetting");
	//$("#txtPassSettingsNewPassword").val('');
	//$("#txtPassSettingsRetypePassword").val('');
	uAdministratorManagement.emptyPassSettingsPopupFieldValues();
};

/**
 * This function set default state of change password popup.
 */
administratorManagement.prototype.defaultStateResetPassword = function() {
	$("#divSetPassword").removeClass("selectedPasswordSetting");
	$("#divSetPassword").addClass("unselectedPasswordSetting");
	$("#divAutoGenPassword").removeClass("unselectedPasswordSetting");
	$("#divAutoGenPassword").addClass("selectedPasswordSetting");
};
/**
* This functions perform filter operation.
* @param filterType
*/
administratorManagement.prototype.filterData = function(filterType) {
	var jsonObj = [];
	var json = sessionStorage.adminGridData;
	if(typeof json === "string"){
		json = JSON.parse(json);
		if(typeof json === "string"){
			json = JSON.parse(json);
		}
	}
	if (filterType == "Inactive") {
		for (var i=0; i<json.length; i++) {
			if (json[i].userStatus == "inactive") {
				jsonObj.push(json[i]);
			}
		}
	} else if (filterType == "Active") {
		for (var i=0; i<json.length; i++) {
			if (json[i].userStatus == "active") {
				jsonObj.push(json[i]);
			}
		}
	} else if (filterType == "Locked") {
		var MAX_LOCK_STATUS = getUiProps().MSG0175;
		for (var i=0; i<json.length; i++) {
			if (json[i].lockStatus >= MAX_LOCK_STATUS) {
				jsonObj.push(json[i]);
			}
		}
	} else if (filterType == "All") {
		jsonObj = json;
	}
	uAdministratorManagement.createUserDetailsTable(jsonObj, true, filterType);
};

$(document).ready(
	function() {
		$("#txtCreateNewUserUserName").blur(
			function() {
				var username = $(this).val();
				var isUsernameValid = uAdministratorManagement
						.validateUsername(username,
								"popupUsernameErrorMsg");
				if (isUsernameValid) {
					uAdministratorManagement
							.isUsernameAvailable(username);
				}
		});
});

/** On Blur Events for validation START **/

/**
 * Following method checks validation of first name (edit) on blur event.
 */

$("#txtAdminMgmntFirstName").blur(
		function() {
			var firstName = $("#txtAdminMgmntFirstName").val();
			var firstNameSpanID = "popupAdminMgmntEditFirstNameErrorMsg";
			var firstNameID = "txtAdminMgmntFirstName";
			if (uEditUserInformation.validateFirstName(firstName,
					firstNameSpanID, firstNameID))
				document.getElementById(firstNameSpanID).innerHTML = "";
		});

/**
 * Following method checks validation of family name (edit) on blur event.
 */
$("#txtAdminMgmntFamilyName").blur(
		function() {
			var familyName = $("#txtAdminMgmntFamilyName").val();
			var familyNameSpanID = "popupAdminMgmntEditFamilyNameErrorMsg";
			var familyNameID = "txtAdminMgmntFamilyName";
			if (uEditUserInformation.validateFamilyName(familyName,
					familyNameSpanID, familyNameID))
				document.getElementById(familyNameSpanID).innerHTML = "";
		});

/**
 * Following method checks validation of email (edit) on blur event.
 */
$("#txtAdminMgmntEmail").blur(
		function() {
			var emailID = $("#txtAdminMgmntEmail").val();
			var emailIDSpanID = "popupAdminMgmntEditEmailErrorMsg";
			var emailInputID = "txtAdminMgmntEmail";
			if (uEditUserInformation.validateEmail(emailID, emailIDSpanID,
					emailInputID))
				document.getElementById(emailIDSpanID).innerHTML = "";
		});

/**
 * Following method checks validation of password on blur event.
 */
$("#txtPassSettingsNewPassword").blur(
		function() {
			var newPassword = $("#txtPassSettingsNewPassword").val();
			var newPasswordSpanID = "popupPassSettingsNewPassErrorMsg";
			var newRetypePasswordSpanID = "popupPassSettingsRetypePassErrorMsg";
			document.getElementById(newRetypePasswordSpanID).innerHTML = "";
			if (uAdministratorManagement.validatePassword(newPassword,
					newPasswordSpanID, userName, "txtPassSettingsNewPassword"))
				document.getElementById(newPasswordSpanID).innerHTML = "";
		});

/**
 * Following method checks validation of retype-password on blur event.
 */
$("#txtPassSettingsRetypePassword")
		.blur(
				function() {
					var newRetypePassword = $("#txtPassSettingsRetypePassword").val();
					var newRetypePasswordSpanID = "popupPassSettingsRetypePassErrorMsg";
					var newPassword = $("#txtPassSettingsNewPassword").val();
					if (uAdministratorManagement.validateRetypePassword(
							newRetypePassword, newRetypePasswordSpanID,
							newPassword, "txtPassSettingsRetypePassword"))
						document.getElementById(newRetypePasswordSpanID).innerHTML = "";
				});

/**
 * Following method checks validation of email on blur event.
 */
$("#txtCreateNewUserEmail").blur(
		function() {
			var emailID = $("#txtCreateNewUserEmail").val();
			var emailIDSpanID = "popupEmailErrorMsg";
			var emailInputID = "txtCreateNewUserEmail";
			if (uEditUserInformation.validateEmail(emailID, emailIDSpanID,
					emailInputID))
				document.getElementById(emailIDSpanID).innerHTML = "";
		});

/**
 * Following method checks validation of email on blur event.
 */
$("#txtCreateNewUserReEnterEmail").blur(
		function() {
			var emailID = $("#txtCreateNewUserEmail").val();
			var reEnterEmail = $("#txtCreateNewUserReEnterEmail").val();
			var reEnterEmailIDSpanID = "popupReEnterEmailErrorMsg";
			var reEnterEmailID = "txtCreateNewUserReEnterEmail";
			if (uEditUserInformation.validateReEnterEmail(reEnterEmail, reEnterEmailIDSpanID, reEnterEmailID, emailID))
				document.getElementById(reEnterEmailIDSpanID).innerHTML = "";
		});

/**
 * Following method checks validation of email (edit) on blur event.
 */
$("#txtAdminMgmntReEnterEmail").blur(
		function() {
			var emailID = $("#txtAdminMgmntEmail").val();
			var reEnterEmail = $("#txtAdminMgmntReEnterEmail").val();
			var reEnterEmailIDSpanID = "popupAdminMgmntEditReEnterEmailErrorMsg";
			var reEnterEmailID = "txtAdminMgmntReEnterEmail";
			if (uEditUserInformation.validateReEnterEmail(reEnterEmail, reEnterEmailIDSpanID, reEnterEmailID, emailID))
				document.getElementById(reEnterEmailIDSpanID).innerHTML = "";
		});

/** On Blur Events for validation END * */

function radioBtnYesFocus(id) {
	$(id).css("outline","-webkit-focus-ring-color auto 5px");
}

function radioBtnYesBlur(id) {
	$(id).css("outline","none");
}

/*function radioBtnNoFocus() {
	$("#noRadioBtn").css("outline","-webkit-focus-ring-color auto 5px");
}

function radioBtnNoBlur() {
	$("#noRadioBtn").css("outline","none");
}
*/
$(window).resize(function(){
	uAdministratorManagement.setScrollableHeightForAdminMgmt();
	//uAdministratorManagement.setColumnEllipsisWidth();
	uAdministratorManagement.setMarginForMaxUserLimitMsg();
	uAdministratorManagement.setPositionForUserPrivilegeMsg();
	uAdministratorManagement.setPositionForInactiveUserMsg();
	//uAdministratorManagement.setMarginForSuccessMsg();
});