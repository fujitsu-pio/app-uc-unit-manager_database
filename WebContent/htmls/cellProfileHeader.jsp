<!DOCTYPE html>
<!--
Personium
Copyright 2016 FUJITSU LIMITED

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
-->
<html lang="en">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Expires" content="-1">
<head>
<script>

	var sessionUserName = '<%=session.getValue("id")%>';
	<%-- var organizationID = '<%=session.getValue("organizationID")%>'; --%>
	var envName = sessionStorage.selectedEnvName;
	var unitURL = sessionStorage.selectedUnitUrl;
	var shorterEnvName = getShorterUserName(envName, "ENV");
	var shorterUnitURL = getShorterUserName(unitURL, true);
	$('#displayUnitURL').html(unitURL);
	$('#displayUnitURL').attr('title', unitURL);
	$('#displayEnvName').html(envName);
	$('#displayEnvName').attr('title', envName);
	/* $('#organizationID').html(organizationID);
	$('#organizationID').attr('title', organizationID); */
	var userName = getShorterUserName(sessionUserName);
	sessionStorage.logoutShorterUName = userName;
	$('#userID').html(userName);
	/*hide the show token access from header in case of social graph.*/
	if(sessionStorage.isSocialGraph !== undefined && sessionStorage.isSocialGraph === "true") {
		$('#cellProfileHeaderUserID').addClass('hideElement');
	}
	//$('#userID').attr('title', sessionUserName);

	/**
	 * The purpose of this function is to restrict the size of the user name
	 * if no. of characters in entity name are greater than 16.
	 */
	function getShorterUserName(entityName, isSchema) {
		var entityToolTip = entityName;
		if(entityToolTip != null) {
			var len = entityToolTip.length; 
			if(len > 20 && isSchema != true && isSchema != "ENV") {
				$('#userID').attr('title', sessionUserName);
				entityToolTip = entityToolTip.substring(0, 19) + "..";
			} else if (isSchema == true && len > 50) {
				entityToolTip = entityToolTip.substring(0, 49) + "..";
			} else if (isSchema == "ENV" && len > 12) {
				entityToolTip = entityToolTip.substring(0, 11) + "..";
			}
		}
		return entityToolTip;
	}


	function clickLogout() {
		sessionStorage.removeItem("revisionNumber");
		var contextRoot = sessionStorage.contextRoot;
		$.ajax({
			data : {
				param : ""
			},
			dataType : 'json',
			url : '../LogOut',
			type : 'GET',
			success : function(jsonData) {
				sessionStorage.tabName = "";
				$("#logoutMsg").text(jsonData['logout']);
				localStorage.user = jsonData['logout'];
				window.location.href = contextRoot;
			},
			error : function(jsonData) {
				if (jsonData['logout'] == null || jsonData == undefined) {
					window.location.href = contextRoot;
				}
			}
		});
	}
</script>
</head>
<body>
	<header class="cellProfileHeaderContainer">
	<div style="width:50%;float:left;">
		<div style="float: left;" class="userName orgIDLbl environmentLbl">Environment : </div><div id="displayEnvName" class="userName orgIDValue env envNameHeader"></div>
	</div>	
	<div style="float:right;width:50%;">
		<ul class="userName" id="showToken" style="width:19%;">
			 <li id="cellProfileHeaderUserID"><!-- <a href="#" id="userID" class="userNameHeader" style="cursor:default;"></a>  -->
			 <a href="#" style="width: 25px;padding: 0px 0px 0px 0px;margin:4px 8px 3px 2px;float:right"><img src="../newImages/showTokenIcon.png" style="width: 25px;height: 25px;padding-top: 2px;padding-right: 1px;">
			 </a>
				 <ul style="margin-left: -8px;">
					<li><a style="margin-right: 0px;padding-left: 10px;width: 120px;height:30px;padding-top: 10px" onclick="openCreateEntityModal('#tokenPopUpModalWindow','#tokenDialogBox');" href="#">Show Access Token</a></li>
				</ul></li> 
		</ul>
		<div class="unitEnvInfo" style="float:right" >
			<div  id="displayUnitURL"  class="unitURLEnv" style="margin-right: -83px;"></div> <!-- <label  id="displayEnvName" class="unitURLEnv" style="float:right;font-size: 0.75em;padding-top: 3px;"></label> -->
			</div>
			</div>
		<!-- <ul class="userName orgIDLbl" >Organization ID : <label id="organizationID" class="userName orgIDValue">ABCDEFGH</label></ul> -->
		<!-- <ul style="float: left" class="userName orgIDLbl environmentLbl">Environment : <label id="displayEnvName" class="userName orgIDValue env"></label></ul> -->
		<div class="clearBoth"></div>
		<!-- 	<div class="userName">
		<a href="#" id="userID" class="show_hide"></a>
		<a href="#" class="show_hide"><img src="../images/down-arw.png" border="0"/> </a><a href="#"
			class="slidingDiv" onclick="clickLogout();">Logout</a>
	</div>
	<div class="clearBoth"></div> -->
	</header>
	
	<!-- Token Pop UP Start -->
	<div id="tokenPopUpModalWindow" class="modelback"
		style="display: none;">
		<div class="modal-window block-border"
			style="display: block; height: 377px; width: 587px; background: #ffffff"
			id="tokenDialogBox">
			<div class="popupWrapper" id="showTokenPopup">
				<a href="#" title="Close" id="modalbox-close" class="closeIcon"
					onclick="closeEntityModal('#tokenPopUpModalWindow')">Close</a>
				<header class="popupHeader" style="width:577px">Access Token
					 </header>
				<div class="popupComplexTypePropContent" style="height:365px">
					<section class="popupInnerContent" >
						<form>
							<table width="100%" border="0" class="popupCommonTable">
								<tr><td>
								<label width="100%" font-family: Segoe UI;font-size: 1em;>To use API with the same privilege as the portal
													for administering the environment, please copy the 
													token below (ctrl+c) and paste (ctrl+v) to the 
													authorization header as access token.</label>
								</td></tr>
								<!-- <tr><td>
								<label>This is for access token second</label>
								</td></tr> -->
								<tr style="word-wrap: break-word;">
								
									<td><textarea name="" cols="" rows="" class="textBoxPopup textAreaShowAccessToken" readonly="readonly"
											 id="txtAccessToken"  ></textarea> <span
										class="popupAlertArea">
											
									</span></td>
								</tr>
		
							</table>
						</form>
					</section>
					<label id="lblExpiryTime" class="tokenExpiryTime"></label>
					<section class="bottomBtn">
					
						<input type="button" value="Close" class="normalButtonGrey"
							onclick="closeEntityModal('#tokenPopUpModalWindow')" /> 
					</section>
				</div>
			</div>
		</div>
	</div>
	
	<!-- -Token POP up End -->
</body>
</html>