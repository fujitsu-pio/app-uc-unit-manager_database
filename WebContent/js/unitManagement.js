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
function unitManagement() {
}

var uUnitManagement = new unitManagement();

/**
 * The purpose of this method is to open unit management page on click of
 * configure unit button.
 * 
 * @param unitMgmntUnitID
 * @param unitMgmntUnitName
 * @param unitMgmntAPIVersion
 * @param unitMgmntStatus
 * @param unitMgmntPlan
 * @param unitMgmntDomain
 * @param unitMgmntEnvID
 * @param unitMgmntTotalDiskSpace
 * @param unitMgmntMail
 * @param unitMgmntMailPort
 * @param unitMgmntGlobalIP
 * @param unitMgmntstorageDiskUsed
 * @param unitMgmntDatabaseUsed
 * @param unitMgmntDatabaseUsedPerc
 * @param unitMgmntTotalDatabaseUsed
 */
unitManagement.prototype.openUnitManagement = function(unitMgmntUnitID,
		unitMgmntUnitName, unitMgmntAPIVersion, unitMgmntStatus, unitMgmntPlan,
		unitMgmntDomain, unitMgmntEnvID, unitMgmntTotalDiskSpace,
		unitMgmntMail, unitMgmntMailPort, unitMgmntGlobalIP,
		unitMgmntstorageDiskUsedPerc, unitMgmntstorageDiskUsed, 
		unitMgmntDatabaseUsed, unitMgmntDatabaseUsedPerc, unitMgmntTotalDatabaseUsed) {
	unitMgmntUnitName = unitMgmntUnitName.replace(/↔/g, '"');
	$("#unitContent").hide();
	var objCommon = new common();
	var target = document.getElementById('spinner');
	var spinner = new Spinner(objCommon.optsCommon).spin(target);
	$("#unitContent")
			.load(
					contextRoot + '/htmls/'+sessionStorage.selectedLanguage+'/unitManagement.html',
					function() {
						$("#unitName").attr('title', unitMgmntUnitName);
						$("#unitName").text(unitMgmntUnitName);
						$("#unitID").text(unitMgmntUnitID);
						$("#lblUnitUrl").text(unitMgmntDomain);
						$("#lblStatus").text(unitMgmntStatus);
						if (unitMgmntStatus.toLowerCase() == "active") {
							$("#statusIcon")
									.addClass('availableStatusIconUnit');
						} else if (unitMgmntStatus.toLowerCase() == "inactive") {
							$("#statusIcon").addClass('inactiveStatusIconUnit');
						} else if (unitMgmntStatus.toLowerCase() == "changing configurations") {
							$("#statusIcon").addClass(
									'changeConfigurationStatusIconUnit');
						} else if (unitMgmntStatus.toLowerCase() == "under maintenance") {
							$("#statusIcon").addClass(
									'underMaintenanceStatusIconUnit');
						}
						$("#lblPlan").text(unitMgmntPlan);
						$("#lblDomain").text(unitMgmntDomain);
						$("#dvApiVersion").text(unitMgmntAPIVersion);
						$("#lblEnvID").text(unitMgmntEnvID);
						//$("#totalDiskSpace").text(unitMgmntTotalDiskSpace + "GB");
						//$("#lblUsedStoreageSpace").text(unitMgmntstorageDiskUsed + "GB");
						$("#totalDiskSpace").text(uUnitManagement.bytesToSize(unitMgmntTotalDiskSpace) );
						$("#lblUsedStoreageSpace").text(uUnitManagement.bytesToSize(unitMgmntstorageDiskUsed));
						
						$("#totalDatabaseSpace").text(uUnitManagement.bytesToSize(unitMgmntTotalDatabaseUsed) );
						$("#lblUsedDatabaseSpace").text(uUnitManagement.bytesToSize(unitMgmntDatabaseUsed));
						
						$("#lblServer").text(unitMgmntMail);
						$("#lblPort").text(unitMgmntMailPort);
						setDynamicUnitManagementHeight();
						setDynamicUnitNameMaxwidth();
						uUnitManagement.fetcMessageList(unitMgmntUnitID);
						uUnitManagement.displayGlobalIP(unitMgmntGlobalIP);
						$("#unitContent").show();
						uUnitManagement.displayGageChart(unitMgmntstorageDiskUsedPerc,
								unitMgmntstorageDiskUsed,
								unitMgmntTotalDiskSpace);
						uUnitManagement.displayGageChartForDataBase(unitMgmntDatabaseUsedPerc, unitMgmntDatabaseUsed, unitMgmntTotalDatabaseUsed);
						spinner.stop();
					});

};

/**
 * The purpose of this method is to validate unit name.
 * 
 * @param unitName
 * @param unitNameSpanID
 * @returns {Boolean}
 */
unitManagement.prototype.validateUnitName = function(unitName, unitNameSpanID) {
	document.getElementById(unitNameSpanID).innerHTML = "";
	var isUnitNameValid = true;
	var specialchar = /^[!@#$%^&*()+=-_`~;,.<>?'":|{}[]\]*$/;
	var lenUnitName = unitName.length;
	if (lenUnitName == 0) {
		document.getElementById(unitNameSpanID).innerHTML = getUiProps().MSG0228;
		isUnitNameValid = false;
	} else if (lenUnitName > 60) {
		document.getElementById(unitNameSpanID).innerHTML = getUiProps().MSG0229;
		isUnitNameValid = false;
	} else if (lenUnitName != 0
			&& (specialchar.toString().indexOf(unitName.substring(0, 1)) >= 0)) {
		document.getElementById(unitNameSpanID).innerHTML = getUiProps().MSG0230;
		isUnitNameValid = false;
	}
	return isUnitNameValid;
};

/**
 * The purpose of this method is to make AJAX request to servlet for update unit
 * name operation.
 * 
 * @param unitName
 * @param unitId
 */
unitManagement.prototype.postUnitName = function(unitName, unitId) {
	var csrfTokenValueUnitName = document
			.getElementById("CSRFTokenEditUnitName").value;
	$.ajax({
		dataType : 'json',
		url : '../../UnitManagement',
		type : 'POST',
		async : false,
		cache : false,
		data : {
			unitId : encodeURI(unitId),
			unitName : encodeURI(unitName),
			csrfTokenValueUnitName : csrfTokenValueUnitName
		},
		success : function(jsonData) {
			if (jsonData['editUnitName'] == "sessionTimeOut") {
				uCreateEnvironment.performLogout();
			} else if (jsonData['editUnitName'] == "success") {
				uUnitManagement.displaySuccessMessage(unitName);
			}
		},
		error : function(jsonData) {
			uCreateEnvironment.performLogout();
		}
	});
};

/**
 * The purpose of this method is to perform update unit name operation after
 * succesful validation.
 */
unitManagement.prototype.editUnitName = function() {
	var unitName = $("#txtUnitName").val();
	unitName = unitName.replace(/\s{2,}/g, ' ');
	unitName = unitName.trim();
	var unitId = $("#unitID").html();
	var unitNameSpanID = "popupUnitNameErrorMsg";
	if (this.validateUnitName(unitName, unitNameSpanID)) {
		this.postUnitName(unitName, unitId);
	}
};

/**
 * The purpose of this method is to initialize edit unit pop up with existing
 * data.
 * 
 * @param mpdelWindowID
 * @param dialogWindowID
 */
unitManagement.prototype.initializeTextWithDefaultValue = function(
		mpdelWindowID, dialogWindowID) {
	var reuestID = sessionStorage.requestId;
	$("#CSRFTokenEditUnitName").val(reuestID);
	document.getElementById('popupUnitNameErrorMsg').innerHTML = "";
	var unitName = $("#unitName").html();
	openCreateEntityModal(mpdelWindowID, dialogWindowID, 'txtUnitName');
	document.getElementById("txtUnitName").value = unitName;
};

/**
 * The purpose of this method is to display success message for update unit name
 * operation.
 * 
 * @param unitName
 */
unitManagement.prototype.displaySuccessMessage = function(unitName) {
	closeEntityModal("#editUnitNameModalWindow");
	document.getElementById('crudOperationMessageBlock').style.display = "block";
	//objCommon.centerAlignRibbonMessage("#crudOperationMessageBlock");
	objCommon.autoHideAssignRibbonMessage("crudOperationMessageBlock");
	$("#unitName").text(unitName);
};

/**
 * The purpose of this method is to fetch notification message list against
 * selected unit ID
 * 
 * @param unitMgmntUnitID
 */
unitManagement.prototype.fetcMessageList = function(unitMgmntUnitID) {
	var unitID = unitMgmntUnitID;
	$.ajax({
		dataType : 'json',
		url : '../../UnitManagement',
		type : 'GET',
		async : false,
		cache : false,
		data : {
			unitID : unitID,
		},
		success : function(jsonData) {
			if (jsonData.privilege == "sessionTimeOut") {
				uCreateEnvironment.performLogout();
			} else {
				uUnitManagement.displayMessageList(jsonData);
			}
		},
		error : function(jsonData) {
			uCreateEnvironment.performLogout();
		}
	});
};

/**
 * The purpose of this method is to bind dynamic notification message section.
 * 
 * @param jsonData
 */
unitManagement.prototype.displayMessageList = function(jsonData) {
	var dynamic = "";
	for ( var i in jsonData.arrMessageListJson) {
		document.getElementById('testDiv').style.display = "block";
		var messageDetail = jsonData.arrMessageListJson[i];
		var content = messageDetail[1];
		dynamic += '<div class="divNotificationMessageList">';
		dynamic += '<label class="messageIconUnit"></label>';
		dynamic += '<label>' + content + '</label>';
		dynamic += '</div>';
	}
	$("#divNotificationMessage").html(dynamic);
};

/**
 * The purpose of this method is to bind dynamic global IP section.
 * 
 * @param unitMgmntGlobalIP
 */
unitManagement.prototype.displayGlobalIP = function(unitMgmntGlobalIP) {
	var test = unitMgmntGlobalIP.split(',');
	var dynamic = "";
	for ( var count = 0; count < test.length; count++) {
		dynamic += ' <div class="umContentText divGlobalIPList">' + test[count]
				+ '</div>';
	}
	$("#globalIPList").html(dynamic);
};

/**
 * The purpose of this method is to display gage chart on the basis of dynamic
 * data.
 * @param unitMgmntstorageDiskUsedPerc
 * @param usedStorage
 * @param totalSpace
 */
unitManagement.prototype.displayGageChart = function(unitMgmntstorageDiskUsedPerc,usedStorage, totalSpace) {
	var maxvalue = uUnitManagement.bytesToSize(totalSpace);
	var g = new JustGage({
		id				: "gauge",
		value			: Math.round(usedStorage),
		min				: 0,
		max				: parseInt(totalSpace),
		svalue			: Math.round(unitMgmntstorageDiskUsedPerc),
		maxvalue		: maxvalue.split(" ")[0],
		sizeunit		: maxvalue.split(" ")[1],
		title			: "Visitors",
		titleFontColor	: "#00000",
		label			: "Used",
		showMinMax		: true,
		showInnerShadow	: false,
		labelFontColor	: "red",
		showMinMax		: true,
		labelFontColor	: "#aeaeae",
		gaugeColor		: "#d9d9d9"
	});
};

/**
 * The purpose of this method is to display gage chart for database uses on the basis of dynamic
 * data.
 * @param unitMgmntDatabaseUsedPerc
 * @param unitMgmntDatabaseUsed
 * @param unitMgmntTotalDatabaseUsed
 */
unitManagement.prototype.displayGageChartForDataBase = function(unitMgmntDatabaseUsedPerc, unitMgmntDatabaseUsed, unitMgmntTotalDatabaseUsed) {
	var maxvalue = uUnitManagement.bytesToSize(unitMgmntTotalDatabaseUsed);
	var g = new JustGage({
		id				: "databseGauge",
		value			: Math.round(unitMgmntDatabaseUsed),
		min				: 0,
		max				: parseInt(unitMgmntTotalDatabaseUsed),
		svalue			: Math.round(unitMgmntDatabaseUsedPerc),
		maxvalue		: maxvalue.split(" ")[0],
		sizeunit		: maxvalue.split(" ")[1],
		title			: "Visitors",
		titleFontColor	: "#00000",
		label			: "Used",
		showMinMax		: true,
		showInnerShadow	: false,
		labelFontColor	: "red",
		showMinMax		: true,
		labelFontColor	: "#aeaeae",
		gaugeColor		: "#d9d9d9"
	});
};

$(function() {
});

unitManagement.prototype.bytesToSize = function(bytes) {
	var sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
	if(bytes==undefined || bytes == null || bytes == 0) {
		return '0 Bytes';
	}
	var i = parseInt(Math.floor(Math.log(bytes) / Math.log(1024)));
	if (i == 0) {
		return bytes + ' ' + sizes[i];
	}
	return (bytes / Math.pow(1024, i)).toFixed(1) + ' ' + sizes[i];
};

