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
function editUserInformation() {
}

var uEditUserInformation = new editUserInformation();

/**
 * The purpose of this function is to initialize edit user information pop up
 * with existing details.
 */
editUserInformation.prototype.initializeEditUserInfoFieldsValues = function () {
	var reuestID = sessionStorage.requestId;
	var userID = sessionStorage.userID;
	$("#CSRFToken").val(reuestID);
	this.getInformation();
	//$("#editPopupUsername" ).text(userID);
	$("#editPopupUsername" ).val(userID);
	//$("#editPopupUsername" ).attr('title', userID);
	$('#popupEditFirstNameErrorMsg').html('');
	$('#popupEditFamilyNameErrorMsg').html('');
	$('#popupEditEmailErrorMsg').html('');
	$('#popupEditReEnterEmailErrorMsg').html('');
	uEditUserInformation.removeStatusIcons("#txtEditFamilyName");
	uEditUserInformation.removeStatusIcons("#txtEditFirstName");
	uEditUserInformation.removeStatusIcons("#txtEditEmail");
	uEditUserInformation.removeStatusIcons("#txtEditEmail");
	uEditUserInformation.removeStatusIcons("#txtEditReEnterEmail");
};

/**
 * The purpose of this function is to validate first name.
 * 
 * @param firstName
 * @param firstNameSpanID
 * @returns {Boolean}
 */
editUserInformation.prototype.validateFirstName = function (firstName, firstNameSpanID, firstNameID) {
	document.getElementById(firstNameSpanID).innerHTML = "";
	var isFirstNameValid = true;
	var specialchar = /^[!@#$%^&*()+=-_`~;,.<>?'":|{}[]\]*$/;
	var lenFirstName = firstName.length;
	if (lenFirstName == 0) {
		document.getElementById(firstNameSpanID).innerHTML = getUiProps().MSG0149;
		uAdministratorManagement.showErrorIcon("#"+firstNameID);
		isFirstNameValid = false;  
	} else if (lenFirstName > 128) {
		document.getElementById(firstNameSpanID).innerHTML = getUiProps().MSG0150;
		uAdministratorManagement.showErrorIcon("#"+firstNameID);
		isFirstNameValid = false;  
	} else if(lenFirstName != 0 && (specialchar.toString().indexOf(firstName.substring(0,1))>=0)) {
		document.getElementById(firstNameSpanID).innerHTML = getUiProps().MSG0151;
		uAdministratorManagement.showErrorIcon("#"+firstNameID);
		isFirstNameValid = false;  
	} else if (!firstName.replace(/\s/g, '').length) {
		$('#txtEditFirstName').val('');
		$("#txtAdminMgmntFirstName").val('');
		$("#txtCreateNewUserFirstName").val('');
		document.getElementById(firstNameSpanID).innerHTML = getUiProps().MSG0149;
		uAdministratorManagement.showErrorIcon("#"+firstNameID);
		isFirstNameValid = false;  
	}
	if(isFirstNameValid){
		uAdministratorManagement.showValidValueIcon("#"+firstNameID);
	}
	return isFirstNameValid;
};

/**
 * The purpose of this function is to validate family name.
 * 
 * @param familyName
 * @param familyNameSpanID
 * @returns {Boolean}
 */
editUserInformation.prototype.validateFamilyName = function (familyName, familyNameSpanID, familyNameID) {
	document.getElementById(familyNameSpanID).innerHTML = "";
	var isFamilyNameValid = true;
	var specialchar = /^[!@#$%^&*()+=-_`~;,.<>?'":|{}[]\]*$/;
	var lenFamilyName = familyName.length;
	if (lenFamilyName == 0) {
		document.getElementById(familyNameSpanID).innerHTML = getUiProps().MSG0152;
		uAdministratorManagement.showErrorIcon("#"+familyNameID);
		isFamilyNameValid =  false;  
	} else if (lenFamilyName > 128) {
		document.getElementById(familyNameSpanID).innerHTML = getUiProps().MSG0153;
		uAdministratorManagement.showErrorIcon("#"+familyNameID);
		isFamilyNameValid =  false;  
	} else if (lenFamilyName != 0 && (specialchar.toString().indexOf(familyName.substring(0,1))>=0)) {
		document.getElementById(familyNameSpanID).innerHTML = getUiProps().MSG0154;
		uAdministratorManagement.showErrorIcon("#"+familyNameID);
		isFamilyNameValid =  false;  
	} else if (!familyName.replace(/\s/g, '').length) {
		$('#txtEditFamilyName').val('');
		$("#txtAdminMgmntFamilyName").val('');
		$("#txtCreateNewUserFamilyName").val('');
		document.getElementById(familyNameSpanID).innerHTML = getUiProps().MSG0152;
		uAdministratorManagement.showErrorIcon("#"+familyNameID);
		isFamilyNameValid = false;  
	}
	if(isFamilyNameValid){
		uAdministratorManagement.showValidValueIcon("#"+familyNameID);
	}
	return isFamilyNameValid;
};

/**
 * The purpose of this function is to validate email.
 * 
 * @param emailID
 * @param emailIDSpanID
 * @returns {Boolean}
 */
editUserInformation.prototype.validateEmail = function (emailID, emailIDSpanID, emailInputID) {
	document.getElementById(emailIDSpanID).innerHTML = "";
	var isEmailValid = true;
	var mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
	var specialchar = /^[-_]*$/;
	var digits = /^[0-9]+$/;
	var query = emailID.indexOf("@");
	var lenEmail = emailID.length;
	if(lenEmail == 0){
		document.getElementById(emailIDSpanID).innerHTML = getUiProps().MSG0155;
		uAdministratorManagement.showErrorIcon("#"+emailInputID);
		isEmailValid = false;
	} else if(lenEmail > 128) {
		document.getElementById(emailIDSpanID).innerHTML = getUiProps().MSG0156;
		uAdministratorManagement.showErrorIcon("#"+emailInputID);
		isEmailValid = false; 
	} else if(lenEmail != 0 && !emailID.match(mailformat)) {
		document.getElementById(emailIDSpanID).innerHTML = getUiProps().MSG0157;
		uAdministratorManagement.showErrorIcon("#"+emailInputID);
		isEmailValid = false; 
	} else if(lenEmail != 0 && (specialchar.toString().indexOf(emailID.substring(0,1)) >= 0)){
		document.getElementById(emailIDSpanID).innerHTML = getUiProps().MSG0157;
		uAdministratorManagement.showErrorIcon("#"+emailInputID);
		isEmailValid = false; 
	}
	if (query != -1) {
		var onlyNumbers = emailID.substring(0, query);
		var firstCharacter = emailID.substring(0, 1);
		if(lenEmail != 0 && (onlyNumbers.match(digits))){
			document.getElementById(emailIDSpanID).innerHTML = getUiProps().MSG0157;
			uAdministratorManagement.showErrorIcon("#"+emailInputID);
			isEmailValid = false; 
		} else if (lenEmail != 0 && (firstCharacter.match(digits))) {
			document.getElementById(emailIDSpanID).innerHTML = getUiProps().MSG0157;
			uAdministratorManagement.showErrorIcon("#"+emailInputID);
			isEmailValid = false; 
		}
	} 
	if(isEmailValid){
		uAdministratorManagement.showValidValueIcon("#"+emailInputID);
	}
	return isEmailValid;
};

editUserInformation.prototype.validateReEnterEmail = function (reEnterEmailID, reEnterEmailIDSpanID, reEnterEmailInputID, emailID) {
	document.getElementById(reEnterEmailIDSpanID).innerHTML = "";
	var isEmailValid = true;
	var lenReEnterEmail = reEnterEmailID.length;
	var lenEmail = emailID.length;
	if(lenReEnterEmail == 0){
		document.getElementById(reEnterEmailIDSpanID).innerHTML = getUiProps().MSG0311;
		uAdministratorManagement.showErrorIcon("#"+reEnterEmailInputID);
		isEmailValid = false;
	} else if(lenReEnterEmail != 0 && lenEmail != 0
			&& reEnterEmailID != emailID) {
		document.getElementById(reEnterEmailIDSpanID).innerHTML = getUiProps().MSG0312;
		uAdministratorManagement.showErrorIcon("#"+reEnterEmailInputID);
		isEmailValid = false; 
	} 
	
	if(isEmailValid){
		uAdministratorManagement.showValidValueIcon("#"+reEnterEmailInputID);
	}
	return isEmailValid;
};

/**
 * The purpose of this function is to validate all fields on
 * edit user information pop up.
 *  
 * @param firstName
 * @param firstNameSpanID
 * @param familyName
 * @param familyNameSpanID
 * @param emailID
 * @param emailIDSpanID
 * @returns {Boolean}
 */
editUserInformation.prototype.validateEditPopupFields = function (firstName, firstNameSpanID, familyName, familyNameSpanID, emailID, emailIDSpanID, familyNameID, firstNameID, emailInputID, reEnterEmailID, reEnterEmailIDSpanID, reEnterEmailInputID) {
	var isValidate = false;
	//document.getElementById(emailIDSpanID).innerHTML = "";
	//document.getElementById(familyNameSpanID).innerHTML = "";
	if (this.validateFirstName(firstName, firstNameSpanID, firstNameID)) {
		//document.getElementById(firstNameSpanID).innerHTML = "";
		if (this.validateFamilyName(familyName, familyNameSpanID, familyNameID)) {
			//document.getElementById(familyNameSpanID).innerHTML = "";
			if (this.validateEmail(emailID, emailIDSpanID, emailInputID)) {
				//document.getElementById(emailIDSpanID).innerHTML = "";
				if (this.validateReEnterEmail(reEnterEmailID, reEnterEmailIDSpanID, reEnterEmailInputID, emailID)) {
					isValidate = true;
				}
			}
		}
	}
	return isValidate;
};

/**
 * The purpose of this function is to display notification message.
 */
editUserInformation.prototype.displayNotificationMessage = function(message, dynamicMargin){
	closeEntityModal("#editUserInfoModal");
	//var message = "Information edited successfully";
	//objOdataCommon.displaySuccessMessage("#editUserInfoModal", message, "");
	uAdministratorManagement.showSuccessMessage(message, dynamicMargin);
	objCommon.centerAlignRibbonMessage("#successMsgDiv");
	objCommon.autoHideAssignRibbonMessage("successMsgDiv");
	if ($("#adminMgmtTxt").hasClass('adminMgmtTxtRed')) {
		uAdministratorManagement.fetchActiveUsersList();
	}
};

/**
 * The purpose of this function is to get existing user information. 
 */
editUserInformation.prototype.getInformation = function () {
	$.ajax({
		data : {
		},
		dataType : 'json',
		url : '../../EditUserInfo',
		type : 'GET',
		beforeSend: function (xhr) {
			xhr.setRequestHeader("Cache-Control", "no-cache, no-store, must-revalidate");
				xhr.setRequestHeader("Pragma", "no-cache");
				xhr.setRequestHeader("Expires", -1);
			},
		async : false,
		success : function(jsonData) {
			uEditUserInformation.dislayExistingInfo(jsonData);
		},
		error : function() {
		}
	});
};

/**
 * The purpose of this function is to display existing information
 * on pop up.
 * 
 * @param jsonData
 */
editUserInformation.prototype.dislayExistingInfo = function (jsonData) {
	if (jsonData['sessionTimeOut'] == "sessionTimeOut") {
		uCreateEnvironment.performLogout();
	}
	var existingFirstName = jsonData["existingFirstName"];
	var existingFamilyName = jsonData["existingFamilyName"];
	var existingEmail = jsonData["existingEmail"];
	sessionStorage.editUserInfoOldEmail = existingEmail;
	$("#txtEditFirstName").val(existingFirstName);
	$("#txtEditFamilyName").val(existingFamilyName);
	$("#txtEditEmail").val(existingEmail);
	$("#txtEditReEnterEmail").val(existingEmail);
};

/**
 * The purpose of this function is to post new information to
 * to the servlet.
 *
 * @param userName
 * @param firstName
 * @param familyName
 * @param emailID
 */
editUserInformation.prototype.postInformation = function (userName, firstName, familyName, emailID, isEmailUpdated) {
	var encodedFirstName = encodeURI(firstName);
	var encodedFamilyName = encodeURI(familyName);
	//var encodedRequestId = encodeURI(sessionStorage.requestId);
	var csrfTokenValue = document.getElementById("CSRFToken").value;
	$.ajax({
		data : {
			userName		:	userName,
			newFirstName	:	encodedFirstName,
			newFamilyName	:	encodedFamilyName,
			newEmail		:	emailID,
			csrfTokenValue : csrfTokenValue
		},
		dataType : 'json',
		url : '../../EditUserInfo',//+"?requestID="+encodedRequestId
		type : 'POST',
		beforeSend: function (xhr) {
			xhr.setRequestHeader("Cache-Control", "no-cache, no-store, must-revalidate");
				xhr.setRequestHeader("Pragma", "no-cache");
				xhr.setRequestHeader("Expires", -1);
			},
		async : false,
		success : function(jsonData) {
			if (jsonData['editUserInfo'] == "sessionTimeOut"){
				uCreateEnvironment.performLogout();
			} else if (jsonData['editUserInfo'] == "success"){
				if (isEmailUpdated) {
					uEditUserInformation.sendVerificationMail(userName, emailID, firstName, familyName);
				} else {
					var message = getUiProps().MSG0202;
					uEditUserInformation.displayNotificationMessage(message, "21%");
				}
			}
		},
		error : function() {
		}
	});
};

editUserInformation.prototype.sendVerificationMail = function(username, email, firstName, familyName) {
	var operation = "VERFICATION_MAIL";
	var CSRFTokenSendMailVerification = document.getElementById("CSRFToken").value;
	var encodedEAddress = encodeURIComponent(email);
	var encodedFirstName = encodeURI(firstName);
	var encodedFamilyName = encodeURI(familyName);
	var actionPerformed = "EDIT_USER";
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
				var message = getUiProps().MSG0314;
				uEditUserInformation.displayNotificationMessage(message, "15%");
			} else if (jsonData['verificationMail'] == "verificationMailSentFailure") {
				var message = getUiProps().MSG0315;
				uEditUserInformation.displayNotificationMessage(message, "19%");
			}
		},
		error : function() {
			var message = getUiProps().MSG0315;
			uEditUserInformation.displayNotificationMessage(message, "19%");
		}
	});
	
};

/**
 * The purpose of this function is to edit user information.
 * 
 * @param emailID
 */
editUserInformation.prototype.editUserInfo = function () {
	var firstName = $("#txtEditFirstName").val();
	var familyName = $("#txtEditFamilyName").val();
	var emailID = $("#txtEditEmail").val();
	var reEnteremailID = $("#txtEditReEnterEmail").val();
	var firstNameSpanID = "popupEditFirstNameErrorMsg";
	var familyNameSpanID = "popupEditFamilyNameErrorMsg";
	var emailIDSpanID = "popupEditEmailErrorMsg";
	var reEnteremailIDSpanID = "popupEditReEnterEmailErrorMsg";
	var familyNameID = "txtEditFamilyName";
	var firstNameID = "txtEditFirstName";
	var emailInputID = "txtEditEmail";
	var reEnteremailInputID = "txtEditReEnterEmail";
	var isEmailUpdated = false;
	if (sessionStorage.editUserInfoOldEmail != emailID) {
		isEmailUpdated = true;
	}
	var userName = sessionStorage.userID;
	if (this.validateEditPopupFields(firstName, firstNameSpanID, familyName, familyNameSpanID, emailID, emailIDSpanID, familyNameID, firstNameID, emailInputID, reEnteremailID, reEnteremailIDSpanID, reEnteremailInputID)) {
		this.postInformation(userName, firstName, familyName, emailID, isEmailUpdated);
	}
};
/**
 * The purpose of this function is to remove all icon status.
 * 
 * @param txtID
 */
editUserInformation.prototype.removeStatusIcons=function(txtId){
	$(txtId).removeClass("errorIcon");
	$(txtId).removeClass("validValueIcon");	
};

/**
 * The purpose of this function is to validate family name field .
 * 
 *
 */
editUserInformation.prototype.validateEditFamilyName = function () {
	var familyName = $("#txtEditFamilyName").val();
	var familyNameSpanID = "popupEditFamilyNameErrorMsg";
	var familyNameID = "txtEditFamilyName";
	uEditUserInformation.validateFamilyName(familyName, familyNameSpanID, familyNameID);
};

/**
 * The purpose of this function is to validate first name field .
 * 
 *
 */
editUserInformation.prototype.validateEditFirstName = function () {
	var firstName = $("#txtEditFirstName").val();
	var firstNameSpanID = "popupEditFirstNameErrorMsg";
	var firstNameID = "txtEditFirstName";
	uEditUserInformation.validateFirstName(firstName, firstNameSpanID, firstNameID);
};

/**
 * The purpose of this function is to validate email field .
 * 
 *
 */
editUserInformation.prototype.validateEditEmail = function () {
	var emailID = $("#txtEditEmail").val();
	var emailIDSpanID = "popupEditEmailErrorMsg";
	var emailInputID = "txtEditEmail";
	uEditUserInformation.validateEmail(emailID, emailIDSpanID, emailInputID);
};

/**
 * The purpose of this function is to validate re- enter email field .
 * 
 *
 */
editUserInformation.prototype.validateEditReEnterEmail = function () {
	var emailID = $("#txtEditEmail").val();
	var reEnterEmail = $("#txtEditReEnterEmail").val();
	var reEnterEmailIDSpanID = "popupEditReEnterEmailErrorMsg";
	var reEnterEmailID = "txtEditReEnterEmail";
	uEditUserInformation.validateReEnterEmail(reEnterEmail, reEnterEmailIDSpanID, reEnterEmailID, emailID);
};