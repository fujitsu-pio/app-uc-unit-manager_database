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
/**
 * This method gets called on load of PCS footer
 * fetches revision number for the first time
 * thereafter manages till session expiration
 * */
$(function() {
	$.ajaxSetup({ cache : false });
	var revisionNumber = sessionStorage.revisionNumber;
	if(typeof revisionNumber != 'undefined' && revisionNumber != null && revisionNumber.length>0){
		processFooterContents(sessionStorage.revisionNumber); 
	}else {
		$.ajax({
			dataType : 'json',
			url : '../../Info',
			type : 'GET',
			success : function(jsonData) {
				if(typeof jsonData.revisionNumber != 'undefined'){
				sessionStorage.revisionNumber = jsonData.revisionNumber;
				processFooterContents(sessionStorage.revisionNumber);
			 }
			},
			error : function() {
			}
		});
	}
});

/**
 * This function is responsible for determining the revision number display on application footer based on revision number value 
 * */
function processFooterContents(revisionNumber){
	if(revisionNumber == 0 || typeof buildNumber != 'undefined' ){
		//hide build number <li> element
		document.getElementById("revisionLI").style.display = "none";
		//add delimiter to about <li>
		document.getElementById("about").className = "borderNone";
	}
	else{
		//update build number 
		document.getElementById("revision").innerHTML=revisionNumber;
		document.getElementById("revisionLI").style.display = "block";
		document.getElementById("about").className = "";
	}
}