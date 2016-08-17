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
function leftNavigation() {
}

var uLeftNavigation = new leftNavigation();

/**
 * The purpose of this function is to redirect to the social graph page
 * on click of Social Graph icon in left navigation
 */
leftNavigation.prototype.clickSocialGraph  = function () {
	uLeftNavigation.showSpinnerOnSocialGraph();
	sessionStorage.isSocialGraph = "true";
	window.location.href = contextRoot+"/htmls/social.html";
};

/**
 * The purpose of this function is to redirect to the resource management page
 * on click of Resource Management icon in left navigation
 */
leftNavigation.prototype.clickResourceMgmt  = function () {
	var objCommon = new common();
	sessionStorage.tabName = "";
	sessionStorage.isSocialGraph = "false";
	sessionStorage.selectedcell = '';
	sessionStorage.contextRoot = contextRoot;
	var token = getClientStore().token;
	objCommon.cellCountCheck(token, "CLICKRESOURCEMGMNT");
};

/**
 * The purpose of this function is to redirect to the social graph page
 * on click of Social Graph icon in left navigation
 */
leftNavigation.prototype.showSpinnerOnSocialGraph  = function () {
	$("#cellListPanel").hide();  //hide the Div on environment.jsp
	$("#rightContainer").hide();
	$("#cellProfileBody").css("background", "#fff");

	document.getElementById('socialGraphSpinner').style.display = "block";
	var target = document.getElementById('socialGraphSpinner');
	var spinner = new Spinner(uLeftNavigation.opts).spin(target);
};

leftNavigation.prototype.opts = {
		lines		: 13,			// The number of lines to draw
		length		: 10,			// The length of each line
		width		: 3,			// The line thickness
		radius		: 5,			// The radius of the inner circle
		corners		: 1,			// Corner roundness (0..1)
		rotate		: 0,			// The rotation offset
		direction	: 1,			// 1: clockwise, -1: counterclockwise
		color		: '#1BDDE0',	// #rgb or #rrggbb
		speed		: 1,			// Rounds per second
		trail		: 60,			// Afterglow percentage
		shadow		: false,		// Whether to render a shadow
		hwaccel		: false,		// Whether to use hardware acceleration
		className	: 'spinner',	// The CSS class to assign to the spinner
		// zIndex	: 2e9,			// The z-index (defaults to 2000000000)
		top			: '300px',		// Top position relative to parent in px
		left		: 'auto'		// Left position relative to parent in px
};

$(function() {
	$.ajaxSetup({ cache : false });
});