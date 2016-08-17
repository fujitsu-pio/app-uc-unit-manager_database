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
function cellAcl() {
}

var arrCellACLRolePrivilegeList = [];
var isExist = false;
var uCellAcl = new cellAcl();

/**
 * The purpose of this function is to create role table corresponding to
 * selected cell.
 * 
 * @param cellName
 */
/*cellAcl.prototype.createCellACLRoleTable = function (cellName) {
	$(".dynamicCellACLRole").remove();
	$(".trCellRolesBodyEmptyMsg").remove();
	var baseUrl = getClientStore().baseURL;
	var accessor = objCommon.initializeAccessor(baseUrl, cellName, "", "");
	var objRoleManager = new dcc.RoleManager(accessor);
	var json = objRoleManager.retrieve("");
	var JSONstring = json.rawData;
	var sortJSONString = objCommon.sortByKey(JSONstring, '__updated');
	var recordSize = sortJSONString.length;
	var dynamicTable = "";
	var name = new Array();
	var boxName = new Array();
	var mainBoxValue = getUiProps().MSG0039;
	if (recordSize > 0) {
		$("#cellRoleACLEditIcon").attr('disabled', false);
		$('#cellRoleACLEditIcon').css('cursor', 'pointer');
		for ( var count = 0; count < recordSize; count++) {
			var obj = sortJSONString[count];
			name[count] = obj.Name;
			boxName[count] = obj["_Box.Name"];
			if (boxName[count] == null) {
				boxName[count] = mainBoxValue;
			}
			var roleName = "'" + name[count] + "'";
			var countBoxName = "'" + boxName[count] + "'";
			var sRoleName = objCommon.getShorterName(name[count], 8);
			var sBoxName = objCommon.getShorterName(boxName[count], 8);
			var sRoleBoxPair = sRoleName + "&nbsp;&nbsp;-&nbsp;&nbsp;" + sBoxName;
			var rName = name[count];
			var bName = boxName[count];
			var roleBoxPair = rName + "  -  " + bName;
			dynamicTable += '<tr id= "row_'
					+ count
					+ '" class = "dynamicCellACLRole" onclick = "uCellAcl.highlightRoleRow('
					+ count + ',' + roleName + ',' + countBoxName + ')">';
			dynamicTable += '<td class="roleIconAcl" style="border-bottom:1px solid #fff"></td>';
			dynamicTable += '<td id= "col_' + count
					+ '" width = "80%" name = "acc" title = "' + roleBoxPair
					+ '" class = "cellRoleAclTableTD">' + sRoleBoxPair
					+ '</td>';
			dynamicTable += "</tr>";
		}
		if (dynamicTable != "") {
			$("#cellRoleAclTable").append(dynamicTable);
			$("#cellRoleAclTable tbody").addClass('cellACLRoleScroll');
		}
		this.getFirstCellACLRoleSelected(sortJSONString[0].Name,
				sortJSONString[0]["_Box.Name"]);
	} else {
		$("#cellRoleACLEditIcon").attr('disabled', true);
		$('#cellRoleACLEditIcon').css('cursor', 'default');
		$('#cellACLPrivilegeTable input[type=checkbox]').attr("checked", false);
		$('#cellACLPrivilegeTable input[type=checkbox]').attr('disabled', true);
		var noRole = "No roles created yet";
		var displayErrorMessage = "";
		displayErrorMessage = "<tr class='trCellRolesBodyEmptyMsg'><td style='border:none;'><div id='cellRolesBody' class='noRolesCellACL'>"
				+ noRole + "</div></td></tr>";
		$("#cellRoleAclTable").html(displayErrorMessage);
	}
};*/

/**
 * The purpose of this function is to implement cell ACL i.e
 * add and remove privileges from roles.
 * 
 * @param arrCellACLRolePrivilegeList
 */
cellAcl.prototype.implementCellACL = function (arrCellACLRolePrivilegeList) {
	var json = "";
	var baseUrl = getClientStore().baseURL;
	if (!baseUrl.endsWith("/")) {
		baseUrl += "/";
	}
	var cellName = sessionStorage.selectedcell;
	var path = baseUrl + cellName;
	var accessor = objCommon.initializeAccessor(baseUrl, cellName);
	var objJDavCollection = new dcc.DavCollection(accessor, path);
	var objAce = null;
	var objJAcl = new dcc.Acl();
	var objPrincipal = new dcc.Principal();
	objJAcl.setRequireSchemaAuthz("public");

	for ( var i = 0; i < arrCellACLRolePrivilegeList.length; i++) {
		var roleBoxPair = arrCellACLRolePrivilegeList[i].role;
		var arrRoleBoxPair = roleBoxPair.split("/");
		var boxName = arrRoleBoxPair[1];
		var roleName = arrRoleBoxPair[2];
		var privilegeList = arrCellACLRolePrivilegeList[i].privilege;
		json = {
			"Name" : roleName,
			"_Box.Name" : boxName
		};
		var objJRole = new dcc.Role(accessor, json);
		objAce = new dcc.Ace();
		objAce.setRole(objJRole);
		if (privilegeList != null) {
			var arrprivilegeList = privilegeList.split(',');
			for ( var count = 0; count < arrprivilegeList.length; count++) {
				if (arrprivilegeList[count].length != 0) {
					objAce.addPrivilege(arrprivilegeList[count]);
				}
			}
			objJAcl.addAce(objAce);
		}
		//objJAcl.addAce(objAce);
	}
	var objJAclManager = new dcc.AclManager(accessor, objJDavCollection);
	var response = objJAclManager.setAsAcl(objJAcl);
	return response;
};

/**
 * The purpose of this function is to t toggle between edit and
 * save mode.
 */
/*cellAcl.prototype.cellACLToggleMode = function () {
	if ($("#cellRoleACLEditIcon").hasClass("editIconACL")) {
		var mainBoxValue = getUiProps().MSG0039;
		$('#cellACLPrivilegeTable input[type=checkbox]')
				.attr('disabled', false);
		$("#cellRoleACLEditIcon").removeClass("editIconACL");
		$("#cellRoleACLEditIcon").addClass("saveIconACL");
		document.getElementById("cellRoleACLEditIcon").title = "Save";
		arrCellACLRolePrivilegeList = JSON
				.parse(sessionStorage['cellACLRolePrivilegeSet']);
		for ( var i = 0; i < arrCellACLRolePrivilegeList.length; i++) {
			var roleName = arrCellACLRolePrivilegeList[i].role;
			var priv = arrCellACLRolePrivilegeList[i].privilege;
			var index = roleName.indexOf(".");
			if (index == -1) {
				var newRole = "../"+ mainBoxValue+ "/" + roleName;
				var json = {
					"role" : newRole,
					"privilege" : priv
				};
				arrCellACLRolePrivilegeList.splice(i, 1, json);
			}

		}
	} else if ($("#cellRoleACLEditIcon").hasClass("saveIconACL")) {
		var SUCCESSCODE = 200;
		var response = this.implementCellACL(arrCellACLRolePrivilegeList);
		if (response.httpClient.status == SUCCESSCODE) {
			arrCellACLRolePrivilegeList.length = 0;
			sessionStorage['cellACLRolePrivilegeSet'] = "";
			this.getCellACLSettings();
			$("#cellRoleACLEditIcon").removeClass("saveIconACL");
			$("#cellRoleACLEditIcon").addClass("editIconACL");
			document.getElementById("cellRoleACLEditIcon").title = "Edit";
		}
	}
};*/

/**
 * The purpose of this function is to get first role box selected.
 * 
 * @param roleName
 * @param boxName
 */
cellAcl.prototype.getFirstCellACLRoleSelected = function (roleName, boxName) {
	var mainBoxValue = getUiProps().MSG0039;
	sessionStorage.selectedCellACLRole = roleName;
	sessionStorage.selectedCellACLBoxName = boxName;
	if (boxName == null) {
		sessionStorage.selectedCellACLBoxName = mainBoxValue;
	}
	var objFirstRole = $('#row_' + 0);
	objFirstRole.siblings().removeClass('selectRowCellACLRole');
	objFirstRole.addClass('selectRowCellACLRole');
	this.getCellACLSettings();
};

/**
 * The purpose of this function is to highlight role row on click of
 * row.
 * 
 * @param count
 * @param roleName
 * @param boxName
 */
/*cellAcl.prototype.highlightRoleRow = function (count, roleName, boxName) {
	var mainBoxValue = getUiProps().MSG0039;
	sessionStorage.selectedCellACLRole = roleName;
	sessionStorage.selectedCellACLBoxName = boxName;
	if (boxName == null) {
		sessionStorage.selectedCellACLBoxName = mainBoxValue;
	}
	var rowId = '#row_' + count;
	$(rowId).siblings().removeClass('selectProfileRow');
	$(rowId).siblings().removeClass('selectRowCellACLRole');
	$(rowId).addClass('selectProfileRow');
	$(rowId).addClass('selectRowCellACLRole');

	if (sessionStorage['cellACLRolePrivilegeSet'].length > 0) {
		var savedRolePrivilegeSet = JSON
				.parse(sessionStorage['cellACLRolePrivilegeSet']);
		this.selectedRoleCellACLOperation(savedRolePrivilegeSet);
	}
	if ($("#cellRoleACLEditIcon").hasClass("saveIconACL")) {
		$('#cellACLPrivilegeTable input[type=checkbox]')
				.attr('disabled', false);
	}
};
*/
/**
 * The purpose of this function is to bind privilege table according to the
 * role selected from the cell ACL roles table.
 * 
 * @param jsonData
 */
/*cellAcl.prototype.selectedRoleCellACLOperation = function (jsonData) {
	$('#cellACLPrivilegeTable input[type=checkbox]').attr('disabled', false);
	$('#cellACLPrivilegeTable input[type=checkbox]').attr("checked", false);
	for ( var i = 0; i < jsonData.length; i++) {
		var aclRoleName = jsonData[i].role;
		var index = aclRoleName.lastIndexOf("/");
		if (index == -1) {
			aclRoleName = "../__/"+aclRoleName;
		}
		var selectedRoleBoxPair = "../" + sessionStorage.selectedCellACLBoxName + "/" + sessionStorage.selectedCellACLRole;
		
		checkedPrivilegeList = jsonData[i].privilege;
		if (aclRoleName == selectedRoleBoxPair) {
			this.checkBoxOperation(checkedPrivilegeList);
		} else {
			// $('#aclPrivilegeTable input[type=checkbox]').attr("checked",
			// false);
		}
	}
	$('#cellACLPrivilegeTable input[type=checkbox]').attr('disabled', true);
};*/

/**
 * The purpose of this function is to perform checkbox operations.
 * 
 * @param checkedPrivilegeList
 */
/*cellAcl.prototype.checkBoxOperation = function (checkedPrivilegeList) {
	var arrCheckedprivilege = null;
	var checkedPrivilege = null;
	arrCheckedprivilege = checkedPrivilegeList.split(',');
	for ( var i = 0; i < arrCheckedprivilege.length; i++) {
		checkedPrivilege = arrCheckedprivilege[i];
		$('#cellACLPrivilegeTable tr').each(
			function() {
				var privilegeCheckBoxValue = $(this).find(
						".aclSetPrivChkBox").val();
				if (checkedPrivilege == privilegeCheckBoxValue) {
					$('input[value="' + privilegeCheckBoxValue + '"]')
							.attr("checked", true);
				}
			});
	}
};*/

/**
 * The purpose of this function is to retrieve cell ACL settings by calling
 * API.
 */
cellAcl.prototype.getCellACLSettings = function () {
	var baseUrl = getClientStore().baseURL;
	if (!baseUrl.endsWith("/")) {
		baseUrl += "/";
	}
	var cellName = sessionStorage.selectedcell;
	var path = baseUrl + cellName;
	var accessor = objCommon.initializeAccessor(baseUrl, cellName);
	var objJDavCollection = new dcc.DavCollection(accessor, path);
	var objJAclManager = new dcc.AclManager(accessor, objJDavCollection);
	var response = uBoxAcl.getAclList(accessor, objJDavCollection);
	return response;
};

/**
 * The purpose of this function is to bind privileges to the cell ACL
 * privilege table.
 * 
 * @param response
 */
/*cellAcl.prototype.bindPrivilegesToCellACLPrivilegeTable = function (response) {
	$('#cellACLPrivilegeTable input[type=checkbox]').attr("checked", false);
	$('#cellACLPrivilegeTable input[type=checkbox]').attr('disabled', true);
	var recordSize = response.length;
	var checkedPrivilegeList = null;
	for ( var count = 0; count < recordSize; count++) {
		var aclRoleName = response[count].role;
		var index = aclRoleName.lastIndexOf("/");
		if (index == -1) {
			aclRoleName = "../__/"+aclRoleName;
		}
		var selectedRoleBoxPair = "../" + sessionStorage.selectedCellACLBoxName + "/" + sessionStorage.selectedCellACLRole;
		checkedPrivilegeList = response[count].privilege;
		if (aclRoleName == selectedRoleBoxPair) {
			this.checkBoxOperation(checkedPrivilegeList);
		}
	}
	$('#cellACLPrivilegeTable input[type=checkbox]').attr('disabled', true);
};*/

/**
 * The purpose of this function is to persist the privileges corresponding
 * to role box pair.
 * 
 * @param rolePrivilegeList
 */
/*cellAcl.prototype.persistPrivilege = function (rolePrivilegeList) {
	var roleName = null;
	var jsonRolePrivilegeList = null;
	roleName = sessionStorage.selectedCellACLRole;
	var boxName = sessionStorage.selectedCellACLBoxName;
	var roleBoxPair = "../" + boxName + "/" + roleName;
	jsonRolePrivilegeList = {
		"role" : roleBoxPair,
		"privilege" : rolePrivilegeList
	};
	if (arrCellACLRolePrivilegeList.length > 0) {
		for ( var i = 0; i < arrCellACLRolePrivilegeList.length; i++) {
			var arrRoleName = arrCellACLRolePrivilegeList[i].role;
			if (arrRoleName == roleBoxPair) {
				arrCellACLRolePrivilegeList.splice(i, 1);
				arrCellACLRolePrivilegeList.push(jsonRolePrivilegeList);
			} else {
				if (this.isRoleExist(roleBoxPair)) {
					isExist = false;
				} else {
					isExist = false;
					arrCellACLRolePrivilegeList.push(jsonRolePrivilegeList);
				}
			}
		}
	} else {
		arrCellACLRolePrivilegeList.push(jsonRolePrivilegeList);
	}
};*/

/**
 * The purpose of this function is to check if role box pair exists in
 * cell ACL privilege list.
 * @param roleName
 * @returns
 */
cellAcl.prototype.isRoleExist = function (roleBoxPair) {
	for ( var i = 0; i < arrCellACLRolePrivilegeList.length; i++) {
		var arrRoleBoxPair = arrCellACLRolePrivilegeList[i].role;
		if (arrRoleBoxPair == roleBoxPair) {
			isExist = true;
		}
	}
	return isExist;
};

/**
 * The purpose of this function is to get all selected privileges from
 * privilege table.
 */
cellAcl.prototype.getCheckBoxPrivilegeSelection = function () {
	var selectedPrivilege = objCommon.getMultipleSelections(
			'cellACLPrivilegeTable', 'input', 'cellRoleACLCheckBox');
	uCellAcl.persistPrivilege(selectedPrivilege);
};

/**
 * The purpose of this function is to 
 * @param chkBox
 */
cellAcl.prototype.cellACLCheckBoxSelect = function (chkBox) {
	uCellAcl.getCheckBoxPrivilegeSelection();
};

/**
 * The purpose of this function is to create role table 
 * @param cellName
 */
cellAcl.prototype.createCellACLRoleTable = function () {
	/*var cellName = sessionStorage.selectedcell;
	var baseUrl = getClientStore().baseURL;
	var accessor = objCommon.initializeAccessor(baseUrl, cellName, "", "");
	var objRoleManager = new dcc.RoleManager(accessor);
	var json = objRoleManager.retrieve("");
	var JSONstring = json.rawData;*/
	var roleCount = retrieveRoleRecordCount();
	var JSONstring = objRole.retrieveChunkedData(0, roleCount);
	var sortJSONString = objCommon.sortByKey(JSONstring, '__updated');
	var recordSize = sortJSONString.length;
	var dynamicTable = "";
	var mainBoxValue = getUiProps().MSG0039;
	if (recordSize > 0) {
		//alert(JSON.stringify(sortJSONString));
		var rolePrivList = this.getCellACLSettings();
		var test =rolePrivList.privilegeList;
		var previlegeRecordSize = test.length;
		if (previlegeRecordSize == undefined) {
			previlegeRecordSize = 0;
		}
		for ( var count = 0; count < recordSize; count++) {
			var obj = sortJSONString[count];
			roleName = obj.Name;
			boxName = obj["_Box.Name"];
			if (boxName == null) {
				boxName = mainBoxValue;
			}
			var sRoleName = objCommon.getShorterName(roleName, 25);
			var sBoxName = objCommon.getShorterName(boxName, 30);
			if (sRoleName.length>=25 && sRoleName.length>=13) {
				sRoleName = objCommon.getShorterName(roleName, 18);
				sBoxName = objCommon.getShorterName(boxName, 13);
			}
			var sRoleBoxPair = sRoleName + "&nbsp;-&nbsp;" + sBoxName;
			var roleBoxPair = roleName + "  -  " + boxName;
			var selectedRoleBoxPair = "../" + boxName + "/" + roleName;
			//alert('selectedRoleBoxPair---->' + selectedRoleBoxPair);
			var rolePrivCounter = 0;
			dynamicTable += "<tr>";
			dynamicTable += "<td class='col1 ContentTextpt1'><div class='mainTableEllipsis' title='"+ roleBoxPair +"'>"+ sRoleBoxPair +"</div></td>";
			for ( var privCount = 0; privCount < previlegeRecordSize; privCount++) {
				var aclRoleName = test[privCount].role;
				var index = aclRoleName.lastIndexOf("/");
				if (index == -1) {
					aclRoleName = "../" + mainBoxValue + "/" + aclRoleName;
				}
				var privilegeList = test[privCount].privilege;
				if (aclRoleName == selectedRoleBoxPair) {
						dynamicTable += "<td class='col2 ContentTextpt1'><div>"+ privilegeList.replace(/[, ]+/g, ', ') + "</div></td>";
					break;
				}
				rolePrivCounter++;
			}
			dynamicTable += "</tr>";
		}
		if (dynamicTable != "") {
			$("#cellInfoAdmin thead").show();
			$("#cellInfoAdminData").html(dynamicTable);
		}
	} else {
		var noRole = getUiProps().MSG0231;
		var displayErrorMessage = "";
		$("#cellInfoAdmin thead").hide();
		displayErrorMessage = "<tr class='trCellRolesBodyEmptyMsg' ><td colspan='2' style='border:none;'><div id='cellRolesBody' class='emptyTableMessage' style='width:150px;margin:8% 0% 0% 40%'>"
				+ noRole + "</div></td></tr>";
		$("#cellInfoAdminData").html(displayErrorMessage);
	}
	uCellAcl.setDynamicHeight();
};

/**
 * The purpose of this function is to bind privileges to the main box ACL
 * privilege table.
 * 
 * @param response
 *//*
cellAcl.prototype.custumizeResponse = function(stringArrRolePrivilegeSet) {
	var arrResponse = [];
	var mainBoxValue = getUiProps().MSG0039;
	arrResponse = JSON.parse(stringArrRolePrivilegeSet);
	for ( var i = 0; i < arrResponse.length; i++) {
		var roleName = arrResponse[i].role;
		var priv = arrResponse[i].privilege;
		var index = roleName.indexOf(".");
		
		if (index == -1) {
			var newRole = "../"+ mainBoxValue + "/" + roleName;
			var json = {
				"role" : newRole,
				"privilege" : priv
			};
			arrResponse.splice(i, 1, json);
		}
	}
	return arrResponse;
};*/

/**
 * Following method opens and populates the edit cell pop up window.
 * @param idDialogBox
 * @param idModalWindow
 */
cellAcl.prototype.openPopUpWindow = function(idDialogBox, idModalWindow) {
	$(idModalWindow).fadeIn(0);
	var windowHeight = $(window).height();
	var windowWidth = $(window).width();
	$(idDialogBox).css('top', windowHeight / 2 - $(idDialogBox).height() / 2);
	$(idDialogBox).css('left', windowWidth / 2 - $(idDialogBox).width() / 2);
	this.populateCellACLSettings();
	$('#lblAll0').focus();
		$('#editAclSettingsTable').scroll(
			function() {
			/*	$("#editAclSettingsTable > *").width(
						$("#editAclSettingsTable").width()
								+ $("#editAclSettingsTable").scrollLeft());*/
				$("#editAclSettingsTable > tbody").width($("#editAclSettingsTable").width()
						+ $("#editAclSettingsTable").scrollLeft());
				
				/*var tbodyWidth = $("#editAclSettingsTable").width()
				+ $("#editAclSettingsTable").scrollLeft();*/
				//$("#editCellACLGridTbody").css({ "max-width": tbodyWidth+"px" });
			});
};

/**
 * Following method fetches role and its acl settings.
 * @returns {Array} array of role and its corresponding privilege(s).
 */
cellAcl.prototype.getRoleBoxACLSettings = function() {
	var rowCount = $('#editCellACLGridTbody tr').length;
	var rolePrivilegeList = '';
	var arrCheckedRolePrivilegeList = [];
	var jsonRolePrivilegeList = null;
	var mainBoxValue = getUiProps().MSG0039;
	for ( var index = 0; index < rowCount; index++) {
		var roleBoxCombo =  '';
		var roleBoxPair ='';
		roleBoxCombo =  $('#dv' + index).attr("title");
		if (roleBoxCombo!=undefined){
			var arrRoleBoxCombo = '';
			arrRoleBoxCombo = roleBoxCombo.split(' - ');
		
		roleName = arrRoleBoxCombo[0];
		boxName = arrRoleBoxCombo[1].trim();
		if (boxName == mainBoxValue) {
			boxName = getUiProps().MSG0293;;
		}
		roleBoxPair = "../" + boxName + "/" + roleName;
		}
		//all, auth, auth-read, message, message-read, event, event-read, log, log-read, social, social-read, box, box-read, box-install, acl, acl-read, propfind.
		if ($('#chkall' + index).is(':checked')) {
			rolePrivilegeList += 'all,';
		}
		if ($('#chkauth' + index).is(':checked')) {
			rolePrivilegeList += 'auth,';
		}
		if ($('#chkauth-read' + index).is(':checked')) {
			rolePrivilegeList += 'auth-read,';
		}
		if ($('#chkmessage' + index).is(':checked')) {
			rolePrivilegeList += 'message,';
		}
		if ($('#chkmessage-read' + index).is(':checked')) {
			rolePrivilegeList += 'message-read,';
		}
		if ($('#chkevent' + index).is(':checked')) {
			rolePrivilegeList += 'event,';
		}
		if ($('#chkevent-read' + index).is(':checked')) {
			rolePrivilegeList += 'event-read,';
		}
		if ($('#chklog' + index).is(':checked')) {
			rolePrivilegeList += 'log,';
		}
		if ($('#chklog-read' + index).is(':checked')) {
			rolePrivilegeList += 'log-read,';
		}
		if ($('#chksocial' + index).is(':checked')) {
			rolePrivilegeList += 'social,';
		}
		if ($('#chksocial-read' + index).is(':checked')) {
			rolePrivilegeList += 'social-read,';
		}
		if ($('#chkbox' + index).is(':checked')) {
			rolePrivilegeList += 'box,';
		}
		if ($('#chkbox-read' + index).is(':checked')) {
			rolePrivilegeList += 'box-read,';
		}
		if ($('#chkbox-install' + index).is(':checked')) {
			rolePrivilegeList += 'box-install,';
		}
		if ($('#chkacl' + index).is(':checked')) {
			rolePrivilegeList += 'acl,';
		}
		if ($('#chkacl-read' + index).is(':checked')) {
			rolePrivilegeList += 'acl-read,';
		}
		if ($('#chkpropfind' + index).is(':checked')) {
			rolePrivilegeList += 'propfind,';
		}
		//regex below removes the last comma and extra space from the privilege list.
		if (rolePrivilegeList.length >= 1) {
		jsonRolePrivilegeList = {
				"role" : roleBoxPair,
				"privilege" : rolePrivilegeList.replace(/,\s*$/, "")
			};
		arrCheckedRolePrivilegeList.push(jsonRolePrivilegeList);
		jsonRolePrivilegeList = null;
		rolePrivilegeList='';
		}
	}
	return arrCheckedRolePrivilegeList;
};

/**
 * Following method creates for edit cell pop up.
 * @param dynamicTable html table.
 * @param id count for checkbox id.
 * @param roleBoxDisplay role box name.
 * @returns HTML table.
 */
cellAcl.prototype.createAclRows = function(dynamicTable, id, roleBoxDisplay) {
	var tabIndex = id + 1;
	
	var lblAllFocus = '"' + "#lblAll"+id + '"';
	var lblAuthFocus = '"' + "#lblAuth"+id + '"';
	var lblAuthreadFocus = '"' + "#lblAuth-read"+id + '"';
	var lblMessageFocus = '"' + "#lblMessage"+id + '"';
	var lblMessagereadFocus = '"' + "#lblMessage-read"+id + '"';
	var lblEventFocus = '"' + "#lblEvent"+id + '"';
	var lblEventreadFocus = '"' + "#lblEvent-read"+id + '"';
	var lblLogFocus = '"' + "#lblLog"+id + '"';
	var lblLogReadFocus = '"' + "#lblLogRead"+id + '"';
	var lblSocialFocus = '"' + "#lblSocial"+id + '"';
	var lblSocialReadFocus = '"' + "#lblSocialRead"+id + '"';
	var lblBoxFocus = '"' + "#lblBox"+id + '"';
	var lblBoxReadFocus = '"' + "#lblBoxRead"+id + '"';
	var lblBoxInstallFocus = '"' + "#lblBoxInstall"+id + '"';
	var lblAclFocus = '"' + "#lblAcl"+id + '"';
	var lblAclreadFocus = '"' + "#lblAcl-read"+id + '"';
	var lblPropfindFocus = '"' + "#lblPropfind"+id + '"';
	
	dynamicTable += "<tr id=tr_"
			+ id
			+ "><td  style='text-align:left;' class='borderRight'><div id=dv"
			+ id
			+ " class='editACLTableEllipsis' title='"
			+ roleBoxDisplay
			+ "'>"
			+ roleBoxDisplay
			+ "</div></td><td><input  id='chkall"
			+ id
			+ "' type='checkbox' class='case cursorHand regular-checkbox big-checkbox aclCheckboxFocus' name='case' onfocus='checkBoxCellACLFocus("+lblAllFocus+");' onblur='checkBoxCellACLBlur("+lblAllFocus+");' tabindex='"+tabIndex+"' value='all'><label id ='lblAll"
			+ id
			+ "'  for='chkall"
			+ id
			+ "' class='customChkbox checkBoxLabel aclLabelFocus'></label></td><td><input  id='chkauth"
			+ id
			+ "' type='checkbox' class='case cursorHand regular-checkbox big-checkbox aclCheckboxFocus' name='case' onfocus='checkBoxCellACLFocus("+lblAuthFocus+");' onblur='checkBoxCellACLBlur("+lblAuthFocus+");' tabindex='"+tabIndex+"'  value='auth'><label id ='lblAuth"
			+ id
			+ "' for='chkauth"
			+ id
			+ "' class='customChkbox checkBoxLabel aclLabelFocus'></label></td><td style='padding-left: 26px;'><input id = 'chkauth-read"
			+ id
			+ "' type='checkbox' class='case cursorHand regular-checkbox big-checkbox aclCheckboxFocus' name='case' onfocus='checkBoxCellACLFocus("+lblAuthreadFocus+");' onblur='checkBoxCellACLBlur("+lblAuthreadFocus+");' tabindex='"+tabIndex+"'  value='auth-read'><label id ='lblAuth-read"
			+ id
			+ "' for='chkauth-read"
			+ id
			+ "' class='customChkbox checkBoxLabel aclLabelFocus'></label></td><td style='padding-left: 35px;' ><input id = 'chkmessage"
			+ id
			+ "' type='checkbox' class='case cursorHand regular-checkbox big-checkbox aclCheckboxFocus' name='case' onfocus='checkBoxCellACLFocus("+lblMessageFocus+");' onblur='checkBoxCellACLBlur("+lblMessageFocus+");' tabindex='"+tabIndex+"'  value='message'><label id ='lblMessage"
			+ id
			+ "' for='chkmessage"
			+ id
			+ "' class='customChkbox checkBoxLabel aclLabelFocus'></label></td><td style='padding-left: 40px;' ><input id = 'chkmessage-read"
			+ id
			+ "' type='checkbox' class='case cursorHand regular-checkbox big-checkbox aclCheckboxFocus' name='case' onfocus='checkBoxCellACLFocus("+lblMessagereadFocus+");' onblur='checkBoxCellACLBlur("+lblMessagereadFocus+");' tabindex='"+tabIndex+"'  value='chkmessage-read'><label id ='lblMessage-read"
			+ id
			+ "' for='chkmessage-read"
			+ id
			+ "' class='customChkbox checkBoxLabel aclLabelFocus'></label></td><td style='padding-left: 53px;' ><input id = 'chkevent"
			+ id
			+ "' type='checkbox' class='case cursorHand regular-checkbox big-checkbox aclCheckboxFocus' name='case' onfocus='checkBoxCellACLFocus("+lblEventFocus+");' onblur='checkBoxCellACLBlur("+lblEventFocus+");' tabindex='"+tabIndex+"'  value='event'><label id ='lblEvent"
			+ id
			+ "' for='chkevent"
			+ id
			+ "' class='customChkbox checkBoxLabel aclLabelFocus'></label></td><td style='padding-left: 31px;'><input id = 'chkevent-read"
			+ id
			+ "' type='checkbox' class='case cursorHand regular-checkbox big-checkbox aclCheckboxFocus' name='case' onfocus='checkBoxCellACLFocus("+lblEventreadFocus+");' onblur='checkBoxCellACLBlur("+lblEventreadFocus+");' tabindex='"+tabIndex+"'  value='event-read'><label id ='lblEvent-read"
			+ id
			+ "' for='chkevent-read"
			+ id
			+ "' class='customChkbox checkBoxLabel aclLabelFocus'></label></td><td style='padding-left: 24px;' ><input id= 'chklog"
			+ id
			+ "' type='checkbox' class='case cursorHand regular-checkbox big-checkbox aclCheckboxFocus' name='case' onfocus='checkBoxCellACLFocus("+lblLogFocus+");' onblur='checkBoxCellACLBlur("+lblLogFocus+");' tabindex='"+tabIndex+"'  value='log'><label id ='lblLog"
			+ id
			+ "' for='chklog"
			+ id
			+ "' class='customChkbox checkBoxLabel aclLabelFocus'></label></td><td style='padding-left: 20px;'><input id= 'chklog-read"
			+ id
			+ "' type='checkbox' class='case cursorHand regular-checkbox big-checkbox aclCheckboxFocus' name='case' onfocus='checkBoxCellACLFocus("+lblLogReadFocus+");' onblur='checkBoxCellACLBlur("+lblLogReadFocus+");' tabindex='"+tabIndex+"'  value='log'><label id ='lblLogRead"
			+ id
			+ "' for='chklog-read"
			+ id
			+ "' class='customChkbox checkBoxLabel aclLabelFocus'></label></td><td style='padding-left: 24px;'><input id= 'chksocial"
			+ id
			+ "' type='checkbox' class='case cursorHand regular-checkbox big-checkbox aclCheckboxFocus' name='case' onfocus='checkBoxCellACLFocus("+lblSocialFocus+");' onblur='checkBoxCellACLBlur("+lblSocialFocus+");' tabindex='"+tabIndex+"'  value='log'><label id ='lblSocial"
			+ id
			+ "' for='chksocial"
			+ id
			+ "' class='customChkbox checkBoxLabel aclLabelFocus'></label></td><td style='padding-left: 31px;'><input id= 'chksocial-read"
			+ id
			+ "' type='checkbox' class='case cursorHand regular-checkbox big-checkbox aclCheckboxFocus' name='case' onfocus='checkBoxCellACLFocus("+lblSocialReadFocus+");' onblur='checkBoxCellACLBlur("+lblSocialReadFocus+");' tabindex='"+tabIndex+"'  value='log'><label id ='lblSocialRead"
			+ id
			+ "' for='chksocial-read"
			+ id
			+ "' class='customChkbox checkBoxLabel aclLabelFocus'></label></td><td style='padding-left: 29px;' ><input id= 'chkbox"
			+ id
			+ "' type='checkbox' class='case cursorHand regular-checkbox big-checkbox aclCheckboxFocus' name='case' onfocus='checkBoxCellACLFocus("+lblBoxFocus+");' onblur='checkBoxCellACLBlur("+lblBoxFocus+");' tabindex='"+tabIndex+"'  value='log'><label id ='lblBox"
			+ id
			+ "' for='chkbox"
			+ id
			+ "' class='customChkbox checkBoxLabel aclLabelFocus'></label></td><td style='padding-left:18px;'><input id= 'chkbox-read"
			+ id
			+ "' type='checkbox' class='case cursorHand regular-checkbox big-checkbox aclCheckboxFocus' name='case' onfocus='checkBoxCellACLFocus("+lblBoxReadFocus+");' onblur='checkBoxCellACLBlur("+lblBoxReadFocus+");' tabindex='"+tabIndex+"'  value='log'><label id ='lblBoxRead"
			+ id
			+ "' for='chkbox-read"
			+ id
			+ "' class='customChkbox checkBoxLabel aclLabelFocus'></label></td><td style='padding-left: 35px;'><input id= 'chkbox-install"
			+ id
			+ "' type='checkbox' class='case cursorHand regular-checkbox big-checkbox aclCheckboxFocus' name='case' onfocus='checkBoxCellACLFocus("+lblBoxInstallFocus+");' onblur='checkBoxCellACLBlur("+lblBoxInstallFocus+");' tabindex='"+tabIndex+"'  value='log'><label id ='lblBoxInstall"
			+ id
			+ "' for='chkbox-install"
			+ id
			+ "' class='customChkbox checkBoxLabel aclLabelFocus'></label></td><td style='padding-left: 20px;'><input id= 'chkacl"
			+ id
			+ "' type='checkbox' class='case cursorHand regular-checkbox big-checkbox aclCheckboxFocus' name='case' onfocus='checkBoxCellACLFocus("+lblAclFocus+");' onblur='checkBoxCellACLBlur("+lblAclFocus+");' tabindex='"+tabIndex+"'  value='log'><label id ='lblAcl"
			+ id
			+ "' for='chkacl"
			+ id
			+ "' class='customChkbox checkBoxLabel aclLabelFocus'></label></td><td style='padding-left: 18px;'><input id= 'chkacl-read"
			+ id
			+ "' type='checkbox' class='case cursorHand regular-checkbox big-checkbox aclCheckboxFocus' name='case' onfocus='checkBoxCellACLFocus("+lblAclreadFocus+");' onblur='checkBoxCellACLBlur("+lblAclreadFocus+");' tabindex='"+tabIndex+"'  value='log'><label id ='lblAcl-read"
			+ id
			+ "' for='chkacl-read"
			+ id
			+ "' class='customChkbox checkBoxLabel aclLabelFocus'></label></td><td style='padding-left: 29px;'><input id= 'chkpropfind"
			+ id
			+ "' type='checkbox' class='case cursorHand regular-checkbox big-checkbox aclCheckboxFocus' name='case' onfocus='checkBoxCellACLFocus("+lblPropfindFocus+");' onblur='checkBoxCellACLBlur("+lblPropfindFocus+");' tabindex='"+tabIndex+"'  value='log'><label id ='lblPropfind"
			+ id + "' for='chkpropfind" + id
			+ "' class='customChkbox checkBoxLabel aclLabelFocus'></label></td></tr>";
		return dynamicTable;
};

/**
 * Following method populates Cell Acl settings pop up with relevant privilege values.
 */
cellAcl.prototype.populateCellACLSettings = function () {
	objCommon.restScrollBar('#editCellACLGridTbody');
	var roleCount = retrieveRoleRecordCount();
	var JSONstring = objRole.retrieveChunkedData(0, roleCount);
	/*var cellName = sessionStorage.selectedcell;
	var baseUrl = getClientStore().baseURL;
	var accessor = objCommon.initializeAccessor(baseUrl, cellName, "", "");
	var objRoleManager = new dcc.RoleManager(accessor);
	var json = objRoleManager.retrieve("");
	var JSONstring = json.rawData;*/
	var sortJSONString = objCommon.sortByKey(JSONstring, '__updated');
	var recordSize = sortJSONString.length;
	var arrCheckedState =[];
	var mainBoxValue = getUiProps().MSG0039;
	//var dynamicTable = "<tr style='pointer-events:none;'><td style='border-top: 1px solid #b1b1b1;border-bottom: 1px solid #b1b1b1;text-align: left;border-right:1px solid #e6e6e6;' class='firstHeaderWidth'>Principals</td><td  colspan='20' style='border-top: 1px solid #b1b1b1;border-bottom: 1px solid #b1b1b1; text-align: left;'>Privileges</td></tr>";
	//var dynamicTable = " <tr><td style='text-align:left;min-width:147px;' class='borderRight'><div class='editACLTableEllipsis '></div></td><td ><div>All</div></td><td ><div>	Auth</div></td><td ><div class='auth-read'>Auth-Read</div></td><td ><div >Message</div></td><td ><div class='message-read'>Message-Read</div></td><td ><div class=''>Event</div></td><td ><div class='event-read'>Event-Read</div></td><td ><div>Log</div></td><td ><div class='log-read'>Log-Read</div></td><td ><div>Social</div></td><td ><div class='social-read'>Social-Read</div></td><td ><div>Box</div></td><td ><div class='box-read'>Box-Read</div></td><td><div class='box-install'>Box-Install</div></td><td ><div>Acl</div></td><td ><div class='acl-read'>Acl-Read</div></td><td ><div>propfind</div></td></tr>";
	var dynamicTable = "";
	if (recordSize > 0) {
		var rolePrivList = this.getCellACLSettings();
		var test = rolePrivList.privilegeList;
		var previlegeRecordSize = test.length;
		if (previlegeRecordSize == undefined) {
			previlegeRecordSize = 0;
		}
		for ( var count = 0; count < recordSize; count++) {
			var obj = sortJSONString[count];
			roleName = obj.Name;
			boxName = obj["_Box.Name"];
			if (boxName == null) {
				boxName = mainBoxValue;
			}
			var sRoleName = objCommon.getShorterName(roleName, 25);
			if (sRoleName.length>=25 && sRoleName.length>=13) {
				sRoleName = objCommon.getShorterName(roleName, 18);
				sBoxName = objCommon.getShorterName(boxName, 13);
			}
			var roleBoxPair = roleName + "  -  " + boxName;
			var selectedRoleBoxPair = "../" + boxName + "/" + roleName;
			var rolePrivCounter = 0;
			for ( var privCount = 0; privCount < previlegeRecordSize; privCount++) {
				var aclRoleName = test[privCount].role;
				var index = aclRoleName.lastIndexOf("/");
				if (index == -1) {
					aclRoleName = "../" + mainBoxValue + "/" + aclRoleName;
				}
				var privilegeList = test[privCount].privilege;
				if (aclRoleName == selectedRoleBoxPair) {
					var objCheckedState = {
						"RowID" : count,
						"privilegeList" : privilegeList
					};
					arrCheckedState.push(objCheckedState);
					//It creates row for a role that has privileges.
					dynamicTable = this.createAclRows(dynamicTable, count,
							roleBoxPair);
					break;
				} 
				rolePrivCounter++;
			}
			//It creates row for a role that has no privileges.
			if (rolePrivCounter == previlegeRecordSize ) {
				dynamicTable = this.createAclRows(dynamicTable, count,
						roleBoxPair);
			}
		}
	}
	$("#editCellACLGridTbody").html(dynamicTable);
	$("#btnCancelEditCellAcl").attr("tabindex", recordSize + 1);
	$("#btnEditCellAcl").attr("tabindex", recordSize + 2);
	$(".crossIconCellACLFocus").attr("tabindex", recordSize + 3);
	uBoxDetail.checkPrivleges(arrCheckedState);
};

/**
 * Following method edits cell ACL, this is invoked on the clisk of Save button from edit cell acl pop up.
 */
cellAcl.prototype.editCellAcl = function() {
	var response = this.implementCellACL(this.getRoleBoxACLSettings());
	var code = objCommon.getStatusCode(response);
	if (code == 200) {
		this.createCellACLRoleTable();
		closeEntityModal('#editCellAclSettingModal');
	}
};

cellAcl.prototype.setDynamicHeight= function() {
	var viewPortHeight = $(window).height();
	var cellInfoListHeight = viewPortHeight-200;
	if (viewPortHeight <=650) {
		cellInfoListHeight = 450;
	}
	$("#cellInfoDetail").css("max-height", cellInfoListHeight + "px");
	$("#AdministrationDetail").css("max-height", cellInfoListHeight + "px");	
};

function checkBoxCellACLFocus(id) {
	$(id).css("outline","-webkit-focus-ring-color auto 5px");
}

function checkBoxCellACLBlur(id) {
	$(id).css("outline","none");
}

$(window).resize(function(){
	uCellAcl.setDynamicHeight();
});

