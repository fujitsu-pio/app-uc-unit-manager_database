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
function socialGraph() {
}
var objSocialGraph = new socialGraph();

/**
 * The purpose of the following Ajax call is to fetch token.
 */
//TODO : The token has been fetched in graph.html therefore  duplicity must be removed. 

$.ajax({
	dataType : 'json',
	url : '../Info',
	type : 'GET',
	success : function(jsonData) {
		sessionStorage.TokenForGraph = sessionStorage.tempToken;
	},
	error : function() {
		//failure case
	}
});

/**
 * The purpose of the following method is to fetch accessor.
 * @returns
 */
socialGraph.prototype.getAccessor = function() {

var baseUrl			= sessionStorage.selectedUnitUrl; 
var objJdcContext	= new dcc.DcContext(baseUrl, "", "", "");
var accessor		= objJdcContext.withToken(sessionStorage.TokenForGraph);

return accessor;
};

/**
 * The purpose of the following method is to search records and return filtered set of cell names in the form of an array.
 * @returns {Array}
 */
socialGraph.prototype.searchCellName = function() {

var jsonString				= JSON.parse(cellList);
var singleCellInformation	= "";
var arrCellData				= [];
var totalRecords			= jsonString.length;
var toBeSearchedCellName 	= document.getElementById("txtSearch").value.toUpperCase();

for ( var count = 0; count < totalRecords; count++) {
	var cellName = jsonString[count].Name.toUpperCase();
	if (cellName.search(toBeSearchedCellName) != -1) {
		singleCellInformation = {
			"Name"	: jsonString[count].Name,
			"Date"	: jsonString[count].__published,
			"Count"	: count
		};
		arrCellData.push(singleCellInformation);
	}
}
return arrCellData;
};

/**
 * The purpose of the following method is to create header for the list.
 * @param dynamicTable
 * @returns
 */
socialGraph.prototype.createHeader  = function(dynamicTable) {
dynamicTable = "<form name='frmCellList'>";
dynamicTable += "<table id='mainCellTable' cellpadding='0' cellspacing='0' class='cellGroupList'>";
dynamicTable += "<tbody class='scrollEntityTypeList'></tbody>";
dynamicTable += "<tr style='display:none;'>"; 
dynamicTable += '<th style="display:none;"></th>';
dynamicTable += "</tr>";
return dynamicTable;
};

/**
 * The purpose of this method is to load cell profile page.
 */
socialGraph.prototype.loadCellProfile  = function() {
	//var objCommon = new common();
	sessionStorage.tabName = "";
	sessionStorage.selectedcell = '';
	sessionStorage.contextRoot = contextRoot;
	var target = sessionStorage.selectedUnitUrl;
	sessionStorage.target = target;
	var token=  sessionStorage.TokenForGraph;
	sessionStorage.token = token;
	var count = sessionStorage.cellCountCheck;
	if (count > 0) {
		window.location.href = contextRoot+"/htmls/environment.jsp";
	} else {
		window.location.href = contextRoot+"/htmls/firstCellCreate.html";
	}
	//objCommon.cellCountCheck(token, true);
	sessionStorage.isSocialGraph = "true";
	
};

/**
 * The purpose of this method is to store cell name in a session and it also loads a cell profile page.
 * @param cellName
 */
socialGraph.prototype.getSelectedCell  = function(cellName, readableCellCreatedDate) {
	sessionStorage.selectedCellNameOnGraphList =  cellName;
	sessionStorage.readableCellCreatedDate = readableCellCreatedDate;

	objSocialGraph.loadCellProfile();
};

/**
 * The purpose of the following method is to create rows for the list.
 * @param dynamicTable
 * @param selectedcelldate
 * @param shorterCellName
 * @returns
 */
socialGraph.prototype.createRows  = function(count,dynamicTable,readableCellCreatedDate,shortenedCellName,fullCellName,epochCellCreatedDate) { 
	//onClick="getselectedcell(\'' + fullCellName + '\',\'' + count + '\','+finalSelectedCellDate+')"	
	/*dynamicTable += '<tr class="iconMyCellOnGraph legendsGraph" id = "cellList'+count+'">';*/
	var finalCellCreatedDate = "'"+epochCellCreatedDate+"'";
	dynamicTable += '<tr  id = "cellList'+count+'">';
	dynamicTable += '<td title = "'+readableCellCreatedDate+'"  valign="top" style="height:37px" name = "Cell" ondblclick="objSocialGraph.getSelectedCell(\'' + fullCellName + '\','+finalCellCreatedDate+')" >'+shortenedCellName+'</td>';
	dynamicTable += "</tr>";
	return dynamicTable;
};



/**
 * The purpose of the following method is to create rows for blank.
 * @param dynamicTable
 * @param selectedcelldate
 * @param shorterCellName
 * @returns
 */
socialGraph.prototype.createRowsForBlank  = function(dynamicTable) { 
	dynamicTable += "<div style='padding-top:227px;'><div class='noCellFound'>No result to display</div><div>";
	return dynamicTable;
};


/**
 * The purpose of the following method is to populate list of searched results.
 */
socialGraph.prototype.showCellList  = function() {
var dynamicTable;
//var records = accessor.asCellOwner().unit.cell.query().top(5000).run();
var searchedRecords = objSocialGraph.searchCellName();
if(searchedRecords.length===0)
{
		
dynamicTable = objSocialGraph.createHeader(dynamicTable);	 
dynamicTable = objSocialGraph.createRowsForBlank(dynamicTable);

}
else{
 
var jsonSearchedRecords = JSON.stringify(searchedRecords);
var strSearchedRecords = JSON.parse( jsonSearchedRecords );
var totalRecords = strSearchedRecords.length;
var searchResultsSortedByDate = objCommon.sortByKey(strSearchedRecords, 'Date');
var name = new Array();
var date = new Array();
dynamicTable = objSocialGraph.createHeader(dynamicTable);
for(var count = 0; count < totalRecords; count++) {
				name[count] = searchResultsSortedByDate[count].Name;
				date[count] = searchResultsSortedByDate[count].Date;
				var epochCellCreatedDate = ""+ date[count]+"";
				var readableCellCreatedDate = objCommon.convertEpochDateToReadableFormat(epochCellCreatedDate);
				var fullCellName = name[count];
				var shortenedCellName = objCommon.getShorterEntityName(fullCellName);
				dynamicTable = objSocialGraph.createRows(count,dynamicTable,readableCellCreatedDate,shortenedCellName,fullCellName,epochCellCreatedDate);
		}
}

dynamicTable += "</table>";
dynamicTable += "</form>";
document.getElementById("dvCellList").innerHTML = dynamicTable;
};


/**
 * The purpose of the following method is to load items that are required for the fulfillment of page logic.  
 */
$(document).ready (
function() {
	var boolslideIn				= 0,
		LeftCellListWidth		= 50,
		rightPanelOpenWidth		= 270,
		padGraphContainer		= 30,
		rightPanelClosedWidth	= 40,
		fixedHidePanelWidth		= LeftCellListWidth + padGraphContainer + rightPanelClosedWidth,
		fixedShowPanelWidth		= LeftCellListWidth + padGraphContainer + rightPanelOpenWidth,
		screenWidth				= $(window).width(),
		screenHeight			= $(window).height(),
		footerMargin			= 0;
		
	$("#GraphContainer").width(screenWidth - fixedHidePanelWidth);

	windowResize();

	svgResize();

	$("#imgSearch").click(function () {
		
		var toBeSearchedCellName = document.getElementById("txtSearch").value.toUpperCase();
		if(toBeSearchedCellName!='') {
			objSocialGraph.showCellList();
		}
	});

	$("#txtSearch").keypress(function (e) {
		var code = (e.keyCode ? e.keyCode : e.which);
		if (code == 13) {
			var toBeSearchedCellName = document.getElementById("txtSearch").value.toUpperCase();
			if (toBeSearchedCellName!='') {
				objSocialGraph.showCellList();
			}
		}
	});

	$("#btnSlideIn").click(function () {
		var GraphContwidth	= parseInt($("#GraphContainer").width());
		boolslideIn			= 1;

		if (footerMargin==0) {//set value for first time. This value is not able captured at ready function.
			footerMargin = parseInt($(".innerFooter address").css("margin-right"));
		}

		$("#btnSlideIn").hide();

		GraphContwidth = GraphContwidth - rightPanelOpenWidth + rightPanelClosedWidth;
		$("#GraphContainer").width(GraphContwidth);
		$("#rightPanel").width(rightPanelOpenWidth);
		$(".innerFooter address").css("margin-right",-(rightPanelOpenWidth - rightPanelClosedWidth - footerMargin) + "px");
		document.getElementById("dvSliderAndList").style.display = "block";
		svgResize();
	});

	$("#btnSlideOut").click(function() {
		boolslideIn = 0;
		GraphContwidth = parseInt($("#GraphContainer").width());
		$("#GraphContainer").width(GraphContwidth + rightPanelOpenWidth - rightPanelClosedWidth);
		$("#rightPanel").width(rightPanelClosedWidth);
		$(".innerFooter address").css("margin-right", footerMargin + "px");
		$("#btnSlideIn").show();
		document.getElementById("dvSliderAndList").style.display = "none";
		svgResize();
	});

	$(window).resize(function(){
		windowResize();
		svgResize();
	});

	function windowResize() {
		screenHeight= parseInt($(window).height());
		screenWidth	= parseInt($(window).width());

		if (screenHeight>=650) {
			$("#GraphContainer").height(screenHeight - 85);
			$("#rightPanel").height(screenHeight - 85);
			$("#cellData").height(screenHeight - 120);
			$("#dvCellList").height(screenHeight - 120);
			$("#cellRightList").height(screenHeight - 68);//68 =$("#socialGraphHeader").height() + $("#socialGraphFooter").height()
			$("#btnSlideOut").css("margin-top", (screenHeight-68)/2);
			$("#btnSlideIn").css("margin-top", (screenHeight-68)/2);
		}

		if (screenWidth>=1280) {
			if(boolslideIn == 1) {
				$("#GraphContainer").width(screenWidth-fixedShowPanelWidth);
			} else {
				$("#GraphContainer").width(screenWidth-fixedHidePanelWidth);
			}
		} else {
			if(boolslideIn == 1) {
				$("#GraphContainer").width(930);
			} else {
				$("#GraphContainer").width(1160);
			}
		}
	}
});
