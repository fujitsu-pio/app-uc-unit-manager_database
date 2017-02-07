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
function odataCommon(){}

var objOdataCommon = new odataCommon();
//Default timeout limit - 60 minutes
var IDLE_TIMEOUT_Odata = 3600000;
//Records last activity time .
var LASTACTIVITY = new Date().getTime();
var contextRoot = sessionStorage.contextRoot;

var odataClientStore;
var odataUIProps;

function getOdataClientStore() {
	return odataClientStore;
}

function setOdataClientStore(jsonData) {
	odataClientStore = jsonData;
}

function getOdataUiProps() {
	return odataUIProps;
}

function setOdataUiProps(props) {
	odataUIProps = props;
}

function hideSuccessMessage(){
	document.getElementById("odataMessageBlock").style.display = "none";
}

$.getJSON('../uiProperties.json', function(props) {
	setOdataUiProps(props);
});

/**
 * This method opens up a pop up window.
 * @param idDialogBox
 * @param idModalWindow
 * @param selectedEnvID
 */
odataCommon.prototype.openPopUpWindow = function(idDialogBox, idModalWindow,
		selectedEnvID) {
	envtID = selectedEnvID;
	$(idModalWindow).fadeIn(1000);
	var windowHeight = $(window).height();
	var windowWidth = $(window).width();
	$(idDialogBox).css('top', windowHeight / 2 - $(idDialogBox).height() / 2);
	$(idDialogBox).css('left', windowWidth / 2 - $(idDialogBox).width() / 2);
};

/**
 * This method checks idle time.
 */
odataCommon.prototype.checkIdleTime = function() {
	if (new Date().getTime() > LASTACTIVITY + IDLE_TIMEOUT_Odata) {
			objOdataCommon.openPopUpWindow("#timeOutOdataDialogBox",
				"#timeOutOdataModalWindow");
	}
};
/**
 * This method redirects to the login page.
 */
odataCommon.prototype.redirectLoginPage = function() {
	var contextRoot = sessionStorage.contextRoot;
	window.location.href = contextRoot;
};

$(function() {
	objOdataCommon.persistSessionDetailForOData();
	window.setInterval(objOdataCommon.checkIdleTime, 1000);
	document.onclick = function() {
		LASTACTIVITY = new Date().getTime();
	};
	document.onmousemove = function() {
		LASTACTIVITY = new Date().getTime();
	};
	document.onkeypress = function() {
		LASTACTIVITY = new Date().getTime();
	};
});

/*$.ajax({
		data : {
			param : ""
		},
		dataType : 'json',
		url : '../Info',
		type : 'GET',
		async : false,
		success : function(jsonData) {
			odataClientStore = jsonData;
		},
		error : function() {
		}
});*/

/**
 * The purpose of the following function is to reset persist session
 * details.
 */
odataCommon.prototype.persistSessionDetailForOData = function() { 
	var jsonData = {};
	var tempToken = sessionStorage.tempToken;
	var envName = sessionStorage.selectedEnvName;
	var unitURL = sessionStorage.selectedUnitUrl;
	var refreshToken = sessionStorage.tempRefreshToken;
	jsonData["token"] = tempToken;
	jsonData["environmentName"] = envName;
	jsonData["baseURL"] = unitURL;
	jsonData["refreshToken"] = refreshToken;
	setOdataClientStore(jsonData);
};


/**
 * This method stores new token values.
 * @param data
 */
odataCommon.prototype.storeNewTokenValues = function(data) {
	var jsonData = {};
	var accessToken = data.access_token; 
	var refreshToken = data.refresh_token;
	var expiresIn = data.expires_in;
	var envName = sessionStorage.selectedEnvName;
	var unitURL = sessionStorage.selectedUnitUrl;
	jsonData["token"] = accessToken;
	jsonData["environmentName"] = envName;
	jsonData["baseURL"] = unitURL;
	jsonData["refreshToken"] = refreshToken;
	jsonData["expiresIn"] = expiresIn;
	setOdataClientStore(jsonData);
};

/**
 * This method sets token values.
 * @param data
 */
odataCommon.prototype.setNewTokenValues = function(data) {
	objOdataCommon.storeNewTokenValues(data);
	var token = getOdataClientStore().token;
	var refreshToken = getOdataClientStore().refreshToken;
	sessionStorage.tempToken = token;
	sessionStorage.tempRefreshToken = refreshToken;
	sessionStorage.tokenCreationDateTime = Date.now();
}; 

/**
 * This method refreshes token.
 * @param paramOldRefreshToken
 */
odataCommon.prototype.refreshTokenForOdata = function(paramOldRefreshToken) {
	var paramTargetURL = sessionStorage.selectedUnitUrl;
	var paramEnvironmentID = sessionStorage.selectedEnvID ;
	var CSRFTokenDisplayEnvironment = sessionStorage.requestId;
	var refreshtokenURL = "__auth?p_target=" + paramTargetURL
		+ "&p_env=" + paramEnvironmentID + "&grant_type=refresh_token"+"&refresh_token=" + paramOldRefreshToken;
		$.ajax({
		dataType : 'json',
		url : '../'+refreshtokenURL,
		data : {
			CSRFTokenDisplayEnvironment : CSRFTokenDisplayEnvironment
			},
		type : 'POST',
		async : false,
		success : function(data) {
			objOdataCommon.setNewTokenValues(data);
		},
		error : function() {
		}
	});
};

/**
 * These are the configurable settings for spinner.
 */
odataCommon.prototype.opts = {
		  lines: 13, // The number of lines to draw
		  length: 10, // The length of each line
		  width: 3, // The line thickness
		  radius: 5, // The radius of the inner circle
		  corners: 1, // Corner roundness (0..1)
		  rotate: 0, // The rotation offset
		  direction: 1, // 1: clockwise, -1: counterclockwise
		  color: '#1BDDE0', // #rgb or #rrggbb
		  speed: 1, // Rounds per second
		  trail: 60, // Afterglow percentage
		  shadow: false, // Whether to render a shadow
		  hwaccel: false, // Whether to use hardware acceleration
		  className: 'spinner', // The CSS class to assign to the spinner
		  zIndex: 2e9, // The z-index (defaults to 2000000000)
		  top: '200', // Top position relative to parent in px
		  left: '500' // Left position relative to parent in px
};

/**
 * The purpose of this method is to initialize accessor for making API calls.
 * @param baseUrl
 * @param cellName
 * @param schemaURL
 * @param boxName
 * @returns
 */
odataCommon.prototype.initializeAccessor = function(baseUrl, cellName, schemaURL, boxName){
	if (getOdataClientStore() != undefined) {
		var token = getOdataClientStore().token;
		var objJPersoniumContext = new _pc.PersoniumContext(baseUrl, cellName, schemaURL, boxName);
		var accessor = objJPersoniumContext.withToken(token);
		return accessor;
	}
};

/**
 * The purpose of this function is to hide ribbon message after 5 second for entity type.
 */
odataCommon.prototype.odataAutoHideMessage = function() {
	var timeOut = getOdataUiProps().MSG0037;
	window.setTimeout("$('#odataMessageBlock').hide()", timeOut);
};

/**
 * The purpose of this function is to open schema management page for entity type.
 */
odataCommon.prototype.openSchemaMgmtPage = function () {
	var id = objCommon.isSessionExist();
	if (id != null) {
	sessionStorage.odataTabName = "listView";
	$("#odataMessageBlock").hide();
	var target = document.getElementById('spinner');
	var spinner = new Spinner(objOdataCommon.opts).spin(target);
	document.getElementById("editEntityIconEnabled").style.display = "block";
	document.getElementById("deleteEntityIconEnabled").style.display = "block";
	$("#dataManagementHeader").removeClass("dataManagementHeaderSelected");
	$("#dataManagementHeader").addClass("dataManagementHeaderUnSelected");
	$("#schemaManagementHeader").removeClass("schemaManagementHeaderUnSelected");
	$("#schemaManagementHeader").addClass("schemaManagementHeaderSelected");
	$("#schemaManagment").hide();
	$("#boxTableDiv").hide();
	$("#schemaManagment").load(contextRoot + '/htmls/schemaManagement.html', function() {
		$("#schemaManagment").show();
		if (sessionStorage.entityTypeCount > 0) {
			$("#dvEmptySchemaMgmtDetailMsg").hide();
			/*$("#dvemptyNoPropertyDetail").show();*/
			$("#dvNoSchemaMgmtMsg").show();
			$("#btnAddEntityTypeProp").attr("disabled",false);
			$("#btnAddEntityTypeProp").removeClass("btnAddCompTypePropDisabled");
			$("#btnAddEntityTypeProp").addClass("btnAddCompTypePropEnabled");
			uEntityTypeProperty.createPropertyTable(uEntityTypeOperations.getSelectedEntityType());
		}
		spinner.stop();
	});
	} else {
		window.location.href = contextRoot;
	}
};

/**
 * The purpose of this function is to data schema management page for entity type.
 */
odataCommon.prototype.openDataMgmtPage = function (){
	var id = objCommon.isSessionExist();
	if (id != null) {
	sessionStorage.odataTabName = "dataMgmt";
	$("#odataMessageBlock").hide();
	document.getElementById("editEntityIconEnabled").style.display = "none";
	document.getElementById("deleteEntityIconEnabled").style.display = "none";
	var target = document.getElementById('spinner');
	var spinner = new Spinner(objOdataCommon.opts).spin(target);
	$("#schemaManagementHeader").removeClass("schemaManagementHeaderSelected");
	$("#schemaManagementHeader").addClass("schemaManagementHeaderUnSelected");
	$("#dataManagementHeader").removeClass("dataManagementHeaderUnSelected");
	$("#dataManagementHeader").addClass("dataManagementHeaderSelected");
	$("#schemaManagment").hide();
	$("#schemaManagment").load(contextRoot + '/htmls/dataManagement.html', function() {
		$("#schemaManagment").show();
		$("#boxTableDiv").show();
		//var objEntityType = new entityType();
		var entityTypeList = uEntityTypeOperations.fetchEntityTypeNameList();
		if(entityTypeList.length > 0){
			var objDataManagement = new dataManagement();
			var propList = objDataManagement.getHeaderList();
			uOdataQuery.initializeTxtboxWithDefaultText(propList);
			if(propList.length > 0){
				objDataManagement.createHeaderForEntityTable(propList);
				objDataManagement.getEntityList(propList,false);
			}else{
				document.getElementById("dvNoPropertyMsg").style.display = "block";
				document.getElementById("dvEntityTable").style.display = "none";
				$("#btnCreateEntity").removeClass("createBtn");
				$("#btnCreateEntity").addClass("createBtnDisabled");
				$("#btnCreateEntity").attr("disabled","disabled");
			}
		}else{
			document.getElementById("dvNoPropertyMsg").style.display = "none";
			document.getElementById("dvEntityTable").style.display = "block";
			$("#btnCreateEntity").removeClass("createBtn");
			$("#btnCreateEntity").addClass("createBtnDisabled");
			$("#btnCreateEntity").attr("disabled","disabled");
		}
		spinner.stop();
	});
	} else {
		window.location.href = contextRoot;
	}
};

/**
 * The purpose of this method is to show success message after CRUD operation.
 * @param entityTypeName
 */
odataCommon.prototype.displaySuccessMessage = function (modalID, message, name) {
	closeEntityModal(modalID);
	document.getElementById("odataMessageBlock").style.display = "block";
	$("#odataMessageBlock").removeClass("odataErrorWrapper");
	$("#odataMessageBlock").addClass("odataSuccessWrapper");
	$("#messageIcon").removeClass("odataErrorIcon");
	$("#messageIcon").addClass("odataSuccessIcon");
	document.getElementById("successmsg").innerHTML = message;
	document.getElementById("successmsg").title = name;
	var objOdataCommon = new odataCommon();
	objOdataCommon.odataAutoHideMessage();
};

/**
 * The purpose of this method is to show error message after CRRUD operations.
 * @param modalID
 * @param message
 * @param name
 */
odataCommon.prototype.displayErrorMessage = function(modalID, message, name){
	var objOdataCommon = new odataCommon();
	closeEntityModal(modalID);
	document.getElementById("odataMessageBlock").style.display = "block";
	$("#odataMessageBlock").removeClass("odataSuccessWrapper");
	$("#odataMessageBlock").addClass("odataErrorWrapper");
	$("#messageIcon").removeClass("odataSuccessIcon");
	$("#messageIcon").addClass("odataErrorIcon");
	document.getElementById("successmsg").innerHTML = message;
	document.getElementById("successmsg").title = name;
	objOdataCommon.odataAutoHideMessage();
};

/**
 * The purpose of this function is to retrieve a complex type name from URI.
 * @param uri
 */
odataCommon.prototype.getComplexTypeNameFromURI = function(uri) {
	var COMPLEXTYPE = "_ComplexType.Name";
	var noOfCharacters = COMPLEXTYPE.length;
	var complexTypeIndex = uri.indexOf("_ComplexType.Name");
	var complexTypeNameFromURI = uri.substring(complexTypeIndex + noOfCharacters,uri.length-1);
	var complexTypeName = objCommon.getEnityNameAfterRemovingSpecialChar(complexTypeNameFromURI);
	return complexTypeName;
};

/**
 * The purpose of this function is to retrieve associationEndName from URI.
 * @param uri
 */
odataCommon.prototype.getAssociationEndNameFromURI = function(uri) {
	if (uri != null || uri != undefined) {
		var ASSOCIATIONEND = "Name";
		var noOfCharacters = ASSOCIATIONEND.length;
		var associationEndIndex = uri.indexOf("Name");
		var entityTypeIndex = uri.indexOf("_EntityType.Name");
		var associationEndFromURI = uri.substring(associationEndIndex + noOfCharacters, entityTypeIndex);
		var associationEndName = objCommon.getEnityNameAfterRemovingSpecialChar(associationEndFromURI);
		return associationEndName;
	}
};

/**
 * The purpose of this function is to retrieve entityType name from URI.
 * @param uri
 */
odataCommon.prototype.getEntityTypeNameFromURI = function(uri) {
	if (uri != null || uri != undefined) {
	var ENTITYTYPE = "_EntityType.Name";
	var noOfCharacters = ENTITYTYPE.length;
	var entityTypeIndex = uri.indexOf("_EntityType.Name");
	var entityTypeNameFromURI = uri.substring(entityTypeIndex + noOfCharacters,uri.length-1);
	var entityTypeName = objCommon.getEnityNameAfterRemovingSpecialChar(entityTypeNameFromURI);
	return entityTypeName;
	}
	};
	
/**
 * The purpose of this method is to set value of any html component
 * @param id
 * @param value
 */
odataCommon.prototype.setHTMLValue = function(id, value){
	document.getElementById(id).innerHTML = value;
};

/**
 * The purpose of this method is to generate date in format applicable to UI Date component.
 * @param readableDate
 * @returns {String}
 */
odataCommon.prototype.convertReadableDateToInputDateFormat = function(readableDate){
	var m_names = new Array("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
	var monthName = readableDate.split("-")[1];
	var monthNum = m_names.indexOf(monthName);
	monthNum = monthNum +1;
	if(monthNum < 10){
		monthNum = "0" + monthNum;
	}
	var dayNum = "";
	if(readableDate.split("-")[0].length == 1){
		dayNum = "0" + readableDate.split("-")[0];
	}
	var inputDate = readableDate.split("-")[2] + "-" + monthNum + "-" + dayNum;
	return inputDate;
};

/**
 * The purpose of this method is to create pagination.
 * @param recordSize
 * @param dynamicTable
 */
odataCommon.prototype.createPagination = function (totalRecordCount, dynamicTable,maxRows,tblMain,dvDisplayTable,
		objEntityManager,propList,json,updateMethod,lastPageIndex,type) {
	var pagination = "";
	var totalPageNo = Math.ceil(totalRecordCount / maxRows);
	sessionStorage.selectedPageIndexDataManagement = lastPageIndex;
	sessionStorage.dataSet = JSON.stringify(json);
	var objOdataCommon = new odataCommon();
	if (totalRecordCount > 0) {
		pagination = objOdataCommon.showPaginationIcons(pagination, totalPageNo);
	}
	$(dvDisplayTable).append(pagination);
	var tableID = $(tblMain);
	var valid = objOdataCommon.createUIForPagination(tableID, maxRows, totalRecordCount);
	if (!valid) { 
		return;
	}
	objOdataCommon.createPrevButton(tableID, maxRows, totalPageNo, objEntityManager, propList,json,updateMethod,type);
	objOdataCommon.createNextButton(tableID, maxRows, totalPageNo,objEntityManager,propList,json,updateMethod,type);
	objOdataCommon.createPageButton(tableID, maxRows, totalPageNo, objEntityManager,propList,json,updateMethod,type);
};

/**
 * The purpose of this method is to display pagination icons.
 */
odataCommon.prototype.showPaginationIcons = function(dynamicTable,totalPageNo) {
	dynamicTable += "<div class='pagination dynamicRow'>";
	dynamicTable += "<input type='button' id='prev' value='' class='prev'>";
	for (var count = 1; count <= totalPageNo; count++) {
			dynamicTable += "<input type='button' id='page" + count + "' value='" + count + "' class='paginationBut'>";
			if(count === objCommon.maxNoOfPages){
				break;
			}
	}
	dynamicTable += "<input type='button' id='next' value='Next' class='next'/>";
	dynamicTable += "</div>";
	return dynamicTable;
};

/**
 * This function is used to create apply css classes for pagination
 */
odataCommon.prototype.createUIForPagination = function(tableID, maxRows, totalRecordCount){
	if(totalRecordCount <= maxRows){
		$('#next').addClass('disabled');
		$("#next").attr("disabled","disabled");
		$('#next').addClass('nextDisable');
		$('#next').css('cursor','default');
	}	
	$('#page1').addClass('paginationButSelect');
//	$('#page1').attr('disabled','disabled');
	$('#page1').css('cursor','default');
	$('#prev').addClass('disabled');
	$('#prev').addClass('prevDisable');
	$('#prev').css('cursor','default');
	$("#prev").attr("disabled","disabled");
	return true;
};

/**
 * This function is used to show previous page in pagination.
 */
odataCommon.prototype.createPrevButton = function(tableID, maxRows, totalPageNo, manager, propList, json, updateMethod
		, type){
	$('#prev').click(function() {
		var selectedPage = 0;
		var selectedPageIDNumber = 0;
		for(var index = 1; index <= objCommon.maxNoOfPages; index++){
			if($("#page"+index).hasClass("paginationButSelect")){
				selectedPage = parseInt($("#page"+index).val());
				selectedPageIDNumber = index;
				break;
			}
		}
		var newSelectedPageIDNumber = selectedPageIDNumber - 1;
		var pageNoWithData = Math.floor(objCommon.noOfRecordsToBeFetched / maxRows);
		var newSelectedPage = selectedPage - 1;	
		var modCurrent = newSelectedPage % pageNoWithData;
		var recordCount = (modCurrent-1)*maxRows;
		if(parseInt($("#page1").val()) === selectedPage){
			for(var index = 1; index <= objCommon.maxNoOfPages; index++){
				$("#page"+index).val(newSelectedPage+index-1);
			}
		}
		
		if(newSelectedPage % pageNoWithData == 0){
			var index = newSelectedPage/pageNoWithData;
			if(type === "dataManagement"){
				json = manager.retrieveChunkedData(objCommon.noOfRecordsToBeFetched*(index-1),objCommon.noOfRecordsToBeFetched);
			}else if(type === "associations"){
				json = manager.retrieveChunkedData(propList, objCommon.noOfRecordsToBeFetched*(index-1),objCommon.noOfRecordsToBeFetched);
			}
			recordCount = (objCommon.maxNoOfPages - 1) * maxRows;
			sessionStorage.dataSet = JSON.stringify(json);
		}else{
			json = sessionStorage.dataSet;
		}
		updateMethod(json, propList, recordCount);
		
	$('#page'+newSelectedPageIDNumber).siblings().removeClass('paginationButSelect');
	$('#page'+newSelectedPageIDNumber).siblings().css('cursor','pointer');
	$('#page'+newSelectedPageIDNumber).addClass('paginationButSelect');
	$('#page'+newSelectedPageIDNumber).css('cursor','default');
	sessionStorage.selectedPageIndexDataManagement = newSelectedPage;
	if (newSelectedPage == 1) {
		$('#prev').addClass('disabled');
		$('#prev').addClass('prevDisable');
		$('#prev').css('cursor','default');
		$("#prev").attr("disabled","disabled");
	}
	$('#next').removeClass('disabled');
	$('#next').removeClass('nextDisable');
	$('#next').css('cursor','pointer');
	$("#next").attr("disabled",false);
	return false;
	});
};

/**
 * This function is used to show next page in pagination.
 */
odataCommon.prototype.createNextButton = function(tableID, maxRows, totalPageNo, manager, propList, json, updateMethod
		, type){
	$('#next').click(function() {
		var selectedPage = 0;
		var selectedPageIDNumber = 0;
		for(var index = 1; index <= objCommon.maxNoOfPages; index++){
			if($("#page"+index).hasClass("paginationButSelect")){
				selectedPage = parseInt($("#page"+index).val());
				selectedPageIDNumber = index;
				break;
			}
		}
		var pageNoWithData = Math.floor(objCommon.noOfRecordsToBeFetched / maxRows);
		var newSelectedPage = selectedPage +1;	
		var modCurrent = newSelectedPage % pageNoWithData;
		if(modCurrent === 0){
			modCurrent = objCommon.maxNoOfPages;
		}
		var recordCount = (modCurrent-1)*maxRows;
		if(newSelectedPage > objCommon.maxNoOfPages){
			var newValue = newSelectedPage - objCommon.maxNoOfPages;
			for(var index = 1; index <= objCommon.maxNoOfPages; index++){
				$("#page"+index).val(index+newValue);
			}
		}
		if(newSelectedPage % pageNoWithData == 1){
			var index = selectedPage/pageNoWithData;
			if(type === "dataManagement"){
				json = manager.retrieveChunkedData(objCommon.noOfRecordsToBeFetched*(index),objCommon.noOfRecordsToBeFetched);
			}else if(type === "associations"){
				json = manager.retrieveChunkedData(propList, objCommon.noOfRecordsToBeFetched*(index),objCommon.noOfRecordsToBeFetched);
			}
			sessionStorage.dataSet = JSON.stringify(json);
			recordCount = 0;
		}else{
			json = sessionStorage.dataSet;
		}
		updateMethod(json, propList, recordCount);
		sessionStorage.selectedPageIndexDataManagement = newSelectedPage;
		var newSelectedPageIDNumber = selectedPageIDNumber + 1;
		$('#page'+newSelectedPageIDNumber).siblings().removeClass('paginationButSelect');
		$('#page'+newSelectedPageIDNumber).siblings().css('cursor','pointer');
		$('#page'+newSelectedPageIDNumber).addClass('paginationButSelect');
		$('#page'+newSelectedPageIDNumber).css('cursor','default');
		if(newSelectedPage == totalPageNo){
			$('#next').addClass('disabled');
			$('#next').addClass('nextDisable');
			$('#next').css('cursor','default');
			$("#next").attr("disabled","disabled");
		}
		$('#prev').removeClass('disabled');
		$('#prev').removeClass('prevDisable');
		$('#prev').css('cursor','pointer');
		$("#prev").attr("disabled",false);
		return false;
	});
};

/**
 * This function is used to show the page when a page is selected.
 */
odataCommon.prototype.createPageButton = function(tableID, maxRows, totallPageNo, manager,propList, json, updateMethod
		, type){
	$('.paginationBut').click(function() {
		var selectedPage = this.value;
		var selectedPageIDNumber = this.id;
		var pageNoWithData = Math.floor(objCommon.noOfRecordsToBeFetched / maxRows);
		var modLast = parseInt(sessionStorage.selectedPageIndexDataManagement)%parseInt(pageNoWithData);
		var modCurrent = parseInt(selectedPage) % parseInt(pageNoWithData);
		var mod = modCurrent;
		if(mod === 0){
			mod = parseInt(objCommon.maxNoOfPages);
		}
		var recordCount = (mod-1)*maxRows;
		if(parseInt(selectedPage) < parseInt(sessionStorage.selectedPageIndexDataManagement)){
			if(modCurrent === 0){
				var index = Math.floor(selectedPage/pageNoWithData);
					if(selectedPage%pageNoWithData === 0){
						index = index - 1;
					}
					if(type === "dataManagement"){
						json = manager.retrieveChunkedData(objCommon.noOfRecordsToBeFetched*(index),objCommon.noOfRecordsToBeFetched);
					}else if(type === "associations"){
						json = manager.retrieveChunkedData(propList, objCommon.noOfRecordsToBeFetched*(index),objCommon.noOfRecordsToBeFetched);
					}
					sessionStorage.dataSet = JSON.stringify(json);
			}
			else if(modLast != 0 && modCurrent > modLast){
				var index = Math.floor(selectedPage/pageNoWithData);
				if(selectedPage%pageNoWithData === 0){
					index = index - 1;
				}
				if(type === "dataManagement"){
					json = manager.retrieveChunkedData(objCommon.noOfRecordsToBeFetched*(index),objCommon.noOfRecordsToBeFetched);
				}else if(type === "associations"){
					json = manager.retrieveChunkedData(propList, objCommon.noOfRecordsToBeFetched*(index),objCommon.noOfRecordsToBeFetched);
				}
				sessionStorage.dataSet = JSON.stringify(json);
			}else{
				json = sessionStorage.dataSet;
			}
		}else if(parseInt(selectedPage) > parseInt(sessionStorage.selectedPageIndexDataManagement)){
			if(modLast === 0){
				var index = Math.floor(selectedPage/pageNoWithData);
				if(selectedPage%pageNoWithData === 0){
					index = index - 1;
				}
				if(type === "dataManagement"){
					json = manager.retrieveChunkedData(objCommon.noOfRecordsToBeFetched*(index),objCommon.noOfRecordsToBeFetched);
				}else if(type === "associations"){
					json = manager.retrieveChunkedData(propList, objCommon.noOfRecordsToBeFetched*(index),objCommon.noOfRecordsToBeFetched);
				}
				sessionStorage.dataSet = JSON.stringify(json);
			}
			else if(modCurrent != 0 && modCurrent < modLast){
				var index = Math.floor(selectedPage/pageNoWithData);
				if(selectedPage%pageNoWithData === 0){
					index = index - 1;
				}
				if(type === "dataManagement"){
					json = manager.retrieveChunkedData(objCommon.noOfRecordsToBeFetched*(index),objCommon.noOfRecordsToBeFetched);
				}else if(type === "associations"){
					json = manager.retrieveChunkedData(propList, objCommon.noOfRecordsToBeFetched*(index),objCommon.noOfRecordsToBeFetched);
				}
				sessionStorage.dataSet = JSON.stringify(json);
			}else{
				json = sessionStorage.dataSet;
			}
		}else{
			json = sessionStorage.dataSet;
		}
		updateMethod(json, propList, recordCount);
		
	sessionStorage.selectedPageIndexDataManagement = selectedPage;
	$('#'+selectedPageIDNumber).siblings().removeClass('paginationButSelect');
	$('#'+selectedPageIDNumber).siblings().css('cursor','pointer');
	$('#'+selectedPageIDNumber).addClass('paginationButSelect');
	$('#'+selectedPageIDNumber).css('cursor','default');
	if(selectedPage==1) {
		$('#prev').addClass('disabled');
		$('#prev').addClass('prevDisable');
		$('#prev').css('cursor','default');
		$("#prev").attr("disabled","disabled");
		$('#next').removeClass('disabled');
		$('#next').removeClass('nextDisable');
		$('#next').css('cursor','pointer');
		$("#next").attr("disabled",false);
		if(selectedPage == totallPageNo){
			$('#next').addClass('disabled');
			$('#next').addClass('nextDisable');
			$('#next').css('cursor','default');
			$("#next").attr("disabled","disabled");
		}
	}
	else {
		$('#prev').removeClass('disabled');
		$('#prev').removeClass('prevDisable');
		$('#prev').css('cursor','pointer');
		$("#prev").attr("disabled",false);
		if(selectedPage == totallPageNo){
			$('#next').addClass('disabled');
			$('#next').addClass('nextDisable');
			$('#next').css('cursor','default');
			$("#next").attr("disabled","disabled");
		}else{
			$('#next').removeClass('disabled');
			$('#next').removeClass('nextDisable');
			$('#next').css('cursor','pointer');
			$("#next").attr("disabled",false);
		}
	}
	return false;
	});
};