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
function changePassword(){}

var uChangePassword = new changePassword();

/**
 * The purpose of this method is to clear all filed values of the popup.
 */
changePassword.prototype.clearPopUpFields = function(){
	var reuestID = sessionStorage.requestId;
	$("#CSRFTokenChangePassword").val(reuestID);
	objCommon.setHTMLValue("popupCurrPwdErrorMsg", "");
	objCommon.setHTMLValue("popupNewPwdErrorMsg", "");
	objCommon.setHTMLValue("popupVerPwdErrorMsg", "");
	document.getElementById("txtCurrentPassword").value = "";
	document.getElementById("txtNewPassword").value = "";
	document.getElementById("txtVerifyPassword").value = "";
	uChangePassword.removeStatusIcons("#txtCurrentPassword");
	uChangePassword.removeStatusIcons("#txtNewPassword");
	uChangePassword.removeStatusIcons("#txtVerifyPassword");
};

/**
 * The purpose of this method is to validate whether current password has been entered or not.
 * @returns {Boolean}
 */
changePassword.prototype.validateCurrentPassword = function(){
	var result = true;
	objCommon.setHTMLValue("popupCurrPwdErrorMsg", "");
	var currentPassword = document.getElementById("txtCurrentPassword").value;
	var lenCurrentPassword = currentPassword.length;
	if (lenCurrentPassword < 1 || currentPassword == undefined || currentPassword == null || currentPassword == "") {
		objCommon.setHTMLValue("popupCurrPwdErrorMsg", getUiProps().MSG0174);
		uAdministratorManagement.showErrorBigIcon("#txtCurrentPassword");
		result = false;
	}
	return result;
};

/**
 * The purpose of this method is to validate the new password as per the criteria specified.
 * Password cannot be blank.
 * Password length must be more than ten characters(Minimum 11 characters)
 * The maximum length of password cannot exceed 128 characters
 * Password must be a combination of alphabets, special characters and numbers
 * Password cannot contain username in it. 
 * @returns {Boolean}
 */
changePassword.prototype.validateNewPassword = function(){
	var result = true;
	objCommon.setHTMLValue("popupNewPwdErrorMsg", "");
	var newPassword = document.getElementById("txtNewPassword").value;
	var lenNewPassword = newPassword.length;
	//The following regex finds special characters such as @ # et al.
	var specialCharacter = /^[!@#$%^&*()+=-_`~;,.<>?'":|{}[]\]*$/;
	//The following regex finds alphabets(both lowe and upper case).
	var alphabets = /^[a-zA-Z]*$/;
	//The following regex finds digits.
	var digits = /^[0-9]*$/;
	var spclCharExist = false;
	var alphabetExist = false;
	var digitExist = false;
	var userName = sessionStorage.userID;
	var MIN_LENGTH = 10;
	var MAX_LENGTH = 128;
	if(lenNewPassword < 1 || newPassword === undefined || newPassword === null || newPassword === ""){
		objCommon.setHTMLValue("popupNewPwdErrorMsg", getUiProps().MSG0174);
		uAdministratorManagement.showErrorBigIcon("#txtNewPassword");
		result = false;
	}else if(lenNewPassword < MIN_LENGTH){
		objCommon.setHTMLValue("popupNewPwdErrorMsg", getUiProps().MSG0171);
		uAdministratorManagement.showErrorBigIcon("#txtNewPassword");
		result = false;
	}else if(lenNewPassword > MAX_LENGTH){
		objCommon.setHTMLValue("popupNewPwdErrorMsg", getUiProps().MSG0122);
		uAdministratorManagement.showErrorBigIcon("#txtNewPassword");
		result = false;
	}else if(newPassword.indexOf(userName) != -1){
		objCommon.setHTMLValue("popupNewPwdErrorMsg", getUiProps().MSG0173);
		uAdministratorManagement.showErrorBigIcon("#txtNewPassword");
		result = false;
	}
	else{	
		for(var index = 0; index < lenNewPassword; index++){
			if(newPassword.substring(index, index+1).match(specialCharacter)){
				spclCharExist = true;
				break;
			}
		}
		for(var index = 0; index < lenNewPassword; index++){
			if(newPassword.substring(index, index+1).match(alphabets)){
				alphabetExist = true;
				break;
			}
		}
		for(var index = 0; index < lenNewPassword; index++){
			if(newPassword.substring(index, index+1).match(digits)){
				digitExist = true;
				break;
			}
		}
		if(!(spclCharExist && alphabetExist && digitExist)){
			objCommon.setHTMLValue("popupNewPwdErrorMsg", getUiProps().MSG0172);
			uAdministratorManagement.showErrorBigIcon("#txtNewPassword");
			result = false;
		}
	}
	if(result)
	{
		uAdministratorManagement.showValidValueBigIcon("#txtNewPassword");
	}
	return result;
};

/**
 * The purpose of this method is to validate the confirm password with new password or if it is empty.
 * @returns {Boolean}
 */
changePassword.prototype.validateVerifyPassword = function(){
	var result = true;
	objCommon.setHTMLValue("popupVerPwdErrorMsg", "");
	var newPassword = document.getElementById("txtNewPassword").value;
	var verifyPassword = document.getElementById("txtVerifyPassword").value;
	var lenVerifyPassword = verifyPassword.length;
	if(lenVerifyPassword < 1 || verifyPassword == undefined || verifyPassword == null || verifyPassword == ""){
		objCommon.setHTMLValue("popupVerPwdErrorMsg", getUiProps().MSG0164);
		uAdministratorManagement.showErrorBigIcon("#txtVerifyPassword");
		result = false;
	}else if(newPassword != verifyPassword){
		objCommon.setHTMLValue("popupVerPwdErrorMsg", getUiProps().MSG0024);
		uAdministratorManagement.showErrorBigIcon("#txtVerifyPassword");
		result = false;
	}
	if(result)
	{
		uAdministratorManagement.showValidValueBigIcon("#txtVerifyPassword");
	}
	return result;
};

/**
 * The purpose of this method is to perform validations on popup fields
 * @returns {Boolean}
 */
changePassword.prototype.validatePasswordFields = function(){
	var result = true;
	result = uChangePassword.validateCurrentPassword();
	if(result){
		result = uChangePassword.validateNewPassword();
		if(result){
			result = uChangePassword.validateVerifyPassword();
		}
	}
	return result;
};

/**
 * The purpose of this method is to show message for incorrect current password
 * @param valid
 */
changePassword.prototype.performActionForCurrentPassword = function(valid){
	objCommon.setHTMLValue("popupCurrPwdErrorMsg", getUiProps().MSG0124);
	uAdministratorManagement.showErrorBigIcon("#txtCurrentPassword");
	};

/**
 * The purpose of this method is to perform operations after successful password update
 */
changePassword.prototype.performSuccessOperations = function(message, dynamicMargin){
	closeEntityModal("#changePasswordModal");
	uAdministratorManagement.showSuccessMessage(message, dynamicMargin);
	objCommon.centerAlignRibbonMessage("#successMsgDiv");
	objCommon.autoHideAssignRibbonMessage("successMsgDiv");
	//objOdataCommon.displaySuccessMessage("#changePasswordModal", message, "");
};

/**
 * The purpose of this method is to change the password
 * @returns
 */
changePassword.prototype.changePassword = function(){
	var currentPassword = document.getElementById("txtCurrentPassword").value;
	var newPassword = document.getElementById("txtNewPassword").value;
	var userName = sessionStorage.userID;
	var CSRFTokenChangePassword = document.getElementById("CSRFTokenChangePassword").value;
	var result = "";
	$.ajax({
		data : {
			userName 	: 	userName,
			currentPassword : currentPassword,
			newPassword	: 	newPassword,
			CSRFTokenChangePassword : CSRFTokenChangePassword
		},
		dataType : 'json',
		url : '../../ChangePassword',
		type : 'POST',
		async : false,
		success : function(jsonData) {
			if(jsonData['changePassword'] == "sessionTimeOut"){
				uCreateEnvironment.performLogout();
			}
			else if(jsonData['changePassword'] == "invalidCurrentPassword"){
				uChangePassword.performActionForCurrentPassword();
				result = false;
			}
			else if(jsonData['changePassword'] == "success"){
				uChangePassword.sendPasswordChangedMailForLoggedInUser(userName);
				result = true;
			}else{
				$("#changePasswordModal").hide();
				result = false;
			}
		},
		error : function(jsonData) {
			uCreateEnvironment.performLogout();
		}
	});
	return result;
};

/**
 * This method is called on click of Save Changes button to change the password as per validations
 */
changePassword.prototype.clickChangePassword = function(){
	objCommon.setHTMLValue("popupCurrPwdErrorMsg", "");
	objCommon.setHTMLValue("popupNewPwdErrorMsg", "");
	objCommon.setHTMLValue("popupVerPwdErrorMsg", "");
	uChangePassword.removeStatusIcons("#txtCurrentPassword");
	uChangePassword.removeStatusIcons("#txtNewPassword");
	uChangePassword.removeStatusIcons("#txtVerifyPassword");
	var validationSuccess = uChangePassword.validatePasswordFields();
	if(validationSuccess){
		uChangePassword.changePassword();
	}
};

/**
 * This function posts login credentials.
 * @param username
 * @param password
 */
changePassword.prototype.sendLoginCredentialsMailForLoggedInUser = function(username,
		password) {
	var operation = "LOGIN_CREDENTIALS";
	var CSRFTokenSendMailLoginCredential = document.getElementById("CSRFTokenChangePassword").value;
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
				var message = getUiProps().MSG0197;
				uChangePassword.performSuccessOperations(message, "15%");
			} else if (jsonData['loginCredentialsMail'] == "loginCredentialsMailSentFaliure") {
				var message = getUiProps().MSG0313;
				uChangePassword.performSuccessOperations(message, "19%");
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
 * This function posts new password.
 * @param username
 * @param password
 */
changePassword.prototype.sendPasswordChangedMailForLoggedInUser = function(username) {
	var operation = "PASSWORD_CHANGE_MAIL";
	var CSRFTokenSendPasswordChangedMail = document.getElementById("CSRFTokenChangePassword").value;
	$.ajax({
		data : {
			operation : operation,
			username : username,
			CSRFTokenSendPasswordChangedMail : CSRFTokenSendPasswordChangedMail
		},
		url : '../MailServlet',
		dataType : 'json',
		type : 'POST',
		async : false,
		cache : false,
		success : function(jsonData) {
			if (jsonData['sessionTimeOut'] == "sessionTimeOut") {
				uCreateEnvironment.performLogout();
			} else if (jsonData['passwordChangeMail'] == "passwordChangeMailSentSuccess") {
				var message = getUiProps().MSG0197;
				uChangePassword.performSuccessOperations(message, "15%");
			} else if (jsonData['passwordChangeMail'] == "passwordChangeMailSentFaliure") {
				var message = getUiProps().MSG0313;
				uChangePassword.performSuccessOperations(message, "19%");
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
 * Following method is used to remove icons from the textboxes.
 * @param txtID
 */
changePassword.prototype.removeStatusIcons = function(txtID){
	$(txtID).removeClass("errorIconBig");	
	$(txtID).removeClass("validValueIconBig");
};
