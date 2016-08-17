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
function createEnvironment(){}

var uCreateEnvironment = new createEnvironment();

/**
 * The purpose of this method is to perform initialization opertaions on Create Environment popup.
 */
createEnvironment.prototype.initializeCreateEnvironment = function(){
	var reuestID = sessionStorage.requestId;
	$("#CSRFTokenCreateEnvironment").val(reuestID);
	uCreateEnvironment.clearPopUpFields();
	uCreateEnvironment.fetchAvailableUnitURLList();
};

/**
 * The purpose of this method is to populate and bind dropdown list for Target URL.
 * @param unitURLList
 */
createEnvironment.prototype.populateURLList = function(unitURLList){
	var options = "<option value='0' title='Select'>Select</option>";
	 for(var unitID in unitURLList ) {
		var unitData = unitURLList[unitID];
		var unitURL = unitData[1];
		var shorterURL = objCommon.getShorterName(unitURL,35);
		options = options + "<option value='"+ unitID +"' title='"+ unitURL +"'>"+ shorterURL +"</option>";
	  }
	$("#ddTarget").html(options);
};

/**
 * The purpose of this method is to fetch the list of available unit urls from database
 * for the current user.
 */
createEnvironment.prototype.fetchAvailableUnitURLList = function(){
	$.ajax({
		dataType : 'json',
		url : '../CreateEnvironment',
		type : 'GET',
		async : false,
		success : function(jsonData) {
			uCreateEnvironment.populateURLList(jsonData['uniturllist']);
		},
		error : function(jsonData) {
		}
	});
};

/**
 * The purpose of this method is to clear all fileds on popup.
 */
createEnvironment.prototype.clearPopUpFields = function(){
	var objOdataCommon = new odataCommon();
	objOdataCommon.setHTMLValue("popupTargetErrorMsg", "");
	objOdataCommon.setHTMLValue("popupEnvtNameErrorMsg", "");
	document.getElementById("txtEnvironmentName").value = "";
	$("#ddTarget").val("Select");
};

/**
 * The purpose of this method is to perform validations on popup fields. 
 * @returns {Boolean}
 */
createEnvironment.prototype.validateCreateEnvironmentFields = function(){
	var result = true;
	//The following regex finds characters in the range of 0-9,a-z(lower case) and A-Z(upper case).
	var letters = /^[0-9a-zA-Z-_]+$/;
	//The following regex finds under score(_) and hyphen(-).
	var specialCharacter = /^[-_]*$/;
	var targetURL = $("#ddTarget").val();
	var envtName = document.getElementById("txtEnvironmentName").value;
	var lenEnvtName = envtName.length;
	var objOdataCommon = new odataCommon();
	objOdataCommon.setHTMLValue("popupTargetErrorMsg", "");
	objOdataCommon.setHTMLValue("popupEnvtNameErrorMsg", "");
	if(targetURL == 0){
		objOdataCommon.setHTMLValue("popupTargetErrorMsg", getOdataUiProps().MSG0118);
		result = false;
	}else if (lenEnvtName < 1 || envtName == undefined || envtName == null || envtName == "") {
		objOdataCommon.setHTMLValue("popupEnvtNameErrorMsg", getOdataUiProps().MSG0119);
		result = false;
	} else if (lenEnvtName > 128) {
		objOdataCommon.setHTMLValue("popupEnvtNameErrorMsg", getOdataUiProps().MSG0120);
		result = false;
	} else if (lenEnvtName != 0 && !(envtName.match(letters))) {
		objOdataCommon.setHTMLValue("popupEnvtNameErrorMsg", getOdataUiProps().MSG0023);
		result = false;
	} else if(lenEnvtName != 0 && (specialCharacter.toString().indexOf(envtName.substring(0, 1)) >= 0)) {
		objOdataCommon.setHTMLValue("popupEnvtNameErrorMsg", getOdataUiProps().MSG0121);
		result = false;
	} 
	return result;
};

/**
 * The purpose of this method is to perform operations after successful environment creation.
 * @param envtName
 */
createEnvironment.prototype.performSuccessOperations = function(envtName){
	uHome.fetchAvailableUnitURLsList();
	var shorterEnvtName = objCommon.getShorterEntityName(envtName);
	var message = "Environment " + shorterEnvtName + " created successfully!";
	objOdataCommon.displaySuccessMessage("#createEnvironmentModal", message, envtName);
	uHome.getPrivilegeByID();
};

/**
 * The purpose of this method is to perform operations after some validation is not met
 * during environment creation operation.
 * @param envtName
 */
createEnvironment.prototype.performFailureOperations = function(statusMsg){
	closeEntityModal("#createEnvironmentModal");
	var message = "";
	if(statusMsg == "notAdmin"){
		message = getUiProps().MSG0195;
	}else if(statusMsg == "exceedMaxEnvt"){
		message = getUiProps().MSG0196;
	}
	objOdataCommon.displayErrorMessage("#createEnvironmentModal", message, "");
};

/**
 * The purpose of this function is to perform operations if session gets invalidated
 */
createEnvironment.prototype.performLogout = function(){
	var contextRoot = sessionStorage.contextRoot;
	window.location.href = contextRoot;
};

/**
 * The purpose of this method is to create an environment based on the data.
 */
createEnvironment.prototype.clickCreateEnvironment = function(){
	var validationSuccess = uCreateEnvironment.validateCreateEnvironmentFields();
	if(validationSuccess){
	var envtName = document.getElementById("txtEnvironmentName").value;
	var orgID = sessionStorage.organizationID;
	var unitID = $("#ddTarget :selected").val();
	var userName = sessionStorage.userID;
	var CSRFTokenCreateEnvironment = document.getElementById("CSRFTokenCreateEnvironment").value;
	$.ajax({
		data : {
			envtName : envtName,
			orgID	: orgID,
			unitID : unitID,
			userName : userName,
			CSRFTokenCreateEnvironment : CSRFTokenCreateEnvironment
		},
		dataType : 'json',
		url : '../CreateEnvironment',
		type : 'POST',
		success : function(jsonData) {
			if(jsonData['createEnvtResult'] == "sessionTimeOut"){
				uCreateEnvironment.performLogout();
			}else if(jsonData['createEnvtResult'] == "success"){
				uCreateEnvironment.performSuccessOperations(envtName);
			}else if(jsonData['createEnvtResult'] == "notAdmin"){
				uCreateEnvironment.performFailureOperations("notAdmin");
			}else if(jsonData['createEnvtResult'] == "exceedMaxEnvt"){
				uCreateEnvironment.performFailureOperations("exceedMaxEnvt");
			}else{
				$("#createEnvironmentModal").hide();
			}
		},
		error : function(jsonData) {
		}
	});
	}
};