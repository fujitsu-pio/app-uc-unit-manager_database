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
var checkLengthPositive = function () {
};
var checkLengthNegative = function () {
};
checkLengthPositive.prototype.getMinLength = function () {
    return 1;
};
checkLengthPositive.prototype.getMaxLength = function () {
    return 128;
};
checkLengthPositive.prototype.getCellLength = function (cellName) {
    return cellName.length;
};

checkLengthNegative.prototype.getMinLength = function () {
    return 1;
};
checkLengthNegative.prototype.getMaxLength = function () {
    return 128;
};
checkLengthNegative.prototype.getCellLength = function (cellName) {
    return cellName.length;
};


var commonVar = function () {};

commonVar.prototype.createUpdateDate = function (updatedDate){
	// Epoch Date Conversion
	var strPublishedDate = updatedDate.substring(6, 19);
	var numPublishedDate = parseInt(strPublishedDate);
	var epochDate = numPublishedDate;
	epochDate = parseFloat(epochDate);
	var objEpochDate = new Date(epochDate);
	var publishedDateToLocale = objEpochDate.toLocaleString();
	var finalPublishedDate = publishedDateToLocale.slice(0, 13);				
	var m_names = new Array("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
	var d = new Date(finalPublishedDate);
	var day = d.getDate();
	var curr_month = d.getMonth();
	var month = m_names[curr_month];
	var year = d.getFullYear();
	var convertedDateTime = day+'-'+month+'-'+year;
	return convertedDateTime; 
};

var roleToExtRoleMapping =  function(){};

/**
 * The purpose of this function is to create header for RoleRelationLinkTable
 * table.
 * 
 * @param dynamicTable
 */
roleToExtRoleMapping.prototype.createTableHeader = function(dynamicTable) {
	// Headers Start.
	dynamicTable += "<tr>";
	dynamicTable += "<th class='col1' width='20'><input type='checkbox'  id='checkAll' onclick='uRoleExtRoleMapping.checkAllSelect(this)' class='checkBox'/>";
	dynamicTable += "</th>";
	dynamicTable += "<th class='col2' width='200'>External Roles";
	dynamicTable += "</th>";
	dynamicTable += "<th class='col3' width='200'>Relation";
	dynamicTable += "</th>";
	dynamicTable += "<th class='col4' width='150'>Box";
	dynamicTable += "</th>";
	dynamicTable += "<th class='col5' width='150'>Date";
	dynamicTable += "</th>";
	dynamicTable += "</tr>";
	// Headers End.
	return dynamicTable;
};

/**
 * The purpose of this function is to create row for RoleRelationLink Table .
 */
roleToExtRoleMapping.prototype.createRowForRoleExtRoleLinkTable = function(
		dynamicTable, count, externalRoleURL, externalRoleURLShort, relation,
		relationShort,box, boxShort) {
	var itemVal = externalRoleURL+"-"+ relation+"-"+box;
	dynamicTable += '<td><input id="chkBox'+ count
			+ '" type="checkbox" class="case" name="case" value="'+ itemVal
			+ '"'+ '" onclick="uRoleExtRoleMapping.unCheck();"/></td>';
	dynamicTable += "<td name = 'externalRoleURL' title= '"+ externalRoleURL+ "'>"
			+ externalRoleURLShort+ "</td>";
	dynamicTable += "<td name = 'relation' title= '"+ relation+ "'>"
			+ relationShort+ "</td>";
	dynamicTable += "<td name = 'box' title= '"+ box+ "'>"
	+ boxShort+ "</td>";
	dynamicTable += "<td></td>";
	dynamicTable += "</tr>";
	return dynamicTable;
};

/**
 * The purpose of this function is to fetch the values of selected external
 * cell.
 */
roleToExtRoleMapping.prototype.getSelectedExtRole = function() {
	var selectedRelation = false;
	var dropDown = document.getElementById("ExtRoleDropDown");
	if(typeof dropDown != 'undefined' && dropDown!=null){
		if (dropDown.selectedIndex > 0) {
			selectedRelation = dropDown.options[dropDown.selectedIndex].title;
		}
	}else{
		//do nothing
	}
	return selectedRelation;
};