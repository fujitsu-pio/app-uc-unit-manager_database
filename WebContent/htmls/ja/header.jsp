<%@page contentType="text/html; charset=UTF-8" %>
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
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<head>
<script>

	var sessionUserName = '<%=session.getValue("id")%>'; 
	var organizationID = '<%=session.getValue("organizationID")%>';
	$('#organizationID').html(organizationID);
	$('#organizationID').attr('title', organizationID);
	$('#userID').attr('title', sessionUserName);
	var userName = getShorterUserName(sessionUserName);
	sessionStorage.logoutShorterUName = userName;
	$('#userID').html(sessionUserName);

	/**
	 * The purpose of this function is to restrict the size of the user name
	 * if no. of characters in entity name are greater than 16.
	 */
	function getShorterUserName(entityName) {
		var entityToolTip = entityName;
		if(entityToolTip != null) {
			var len = entityToolTip.length; 
			if(len > 19) {
				$('#userID').attr('title', sessionUserName);
				entityToolTip = entityToolTip.substring(0, 19) + "..";
			}
		}
		return entityToolTip;
	}

	function clickLogout() {
		/* 		parentDivID = '#logOutModalWindow';
				childDivID = '#logOutDialogBox'; */
		sessionStorage.removeItem("revisionNumber");
		var contextRoot = sessionStorage.contextRoot;
		$.ajax({
			data : {
				param : ""
			},
			dataType : 'json',
			url : '../../LogOut',
			type : 'POST',
			success : function(jsonData) {
				/* $(parentDivID).fadeIn(1000);
				var winH = $(window).height();
				var winW = $(window).width();
				$(childDivID).css('top', winH / 2 - $(childDivID).height() / 2); 
				$(childDivID).css('left', winW / 2 - $(childDivID).width() / 2); */
				sessionStorage.tabName = "";
				sessionStorage.requestId = "";
				if (contextRoot.length == 0) {
					//window.location.href = "/login.jsp";
					//"/" escapes in URL thereby not altering the URL. 
					window.location.href = "/";
				} else {
					objCommon.redirectLoginPage();
				}
			},
			error : function(jsonData) {
				if (jsonData['logout'] == null || jsonData == undefined) {
					window.location.href = contextRoot;
				}
			}
		});
	}

	/**
	 * Following method opens log out pop up.
	 */
	function openLogOutPopUp() {
		parentDivID = '#logOutModalWindow';
		childDivID = '#logOutDialogBox';
		$(parentDivID).fadeIn(1000);
		var winH = $(window).height();
		var winW = $(window).width();
		$(childDivID).css('top', winH / 2 - $(childDivID).height() / 2);
		$(childDivID).css('left', winW / 2 - $(childDivID).width() / 2);
		$("#btnCancelLogout").focus();
	}

	$(document).ready(function() {
		$(".userNameWrapper").hover(function() {
			$(".userSubMenu").css("display", "block");
		}, function() {
			$(".userSubMenu").css("display", "none");
		});
		$(".userSubMenu").hover(function() {
			$(".userSubMenu").css("display", "block");
		}, function() {
			$(".userSubMenu").css("display", "none");
		});
	});
</script>
</head>
<body>
	<div  class="navigationBarService borderBottom ">
	<header class="topHeader" >
		<div class="leftSection japaneseFont" onclick="uHome.loadHomePage();" style="min-width: 168px;">personium.io</div>
			<div id="serviceMgmtWrapper" class="serviceMgmtWrapper selected positionServiceMgmt"
				onclick="uHome.clickEnvironmentManagement();" style="min-width: 146px;">
				<div class="serviceMgmtIconRed" id="serviceMgmtIcon"></div>
				<div class="serviceMgmtTxtRed japaneseFont" id="serviceMgmtTxt">サービス範囲</div>
			</div>
			<div id="adminMgmtWrapper" class="adminMgmtWrapper positionAdminMgmt" onclick="uHome.clickAdministratorManagement();">
				<div class="adminMgmtIconGrey" id="adminMgmtIcon"></div>
				<div class="adminMgmtTxtGrey japaneseFont" id="adminMgmtTxt">管理者</div>
			</div>
		<div  class="rightSection positionUserName" >
			<div class="headerButtons">
				<!-- <div class="documentation" id="documentation">
				 <div class="documentationIcon" id="documentationIcon"></div>
					 <div class="documentationTxt" id="documentationTxt">Documentation</div>
				</div> -->
				<ul  class="userNameWrapper" style="width: 145px;">
					<li class="userNameIcon"></li>
					<li class="userNameTxt"><div id="userID" class="userID"></div></li>
					<li class="userNameArrow" style="background: url(../../images/sprite.png) no-repeat 14% -598px;"></li>
				</ul>
				<ul class="userSubMenu positionUserSubMenu">
						<li class="editMyInfoHeaderWrapper" onclick="openCreateEntityModal('#editUserInfoModal','#editUserInfoDialog', 'txtEditFirstName');" style="width:145px"><a
							href="#" class="editMyInfoHeader japaneseFont">私の情報を編集する</a></li>
						<li class="changePasswordHeaderWrapper" onclick="openCreateEntityModal('#changePasswordModal','#changePasswordDialog', 'txtCurrentPassword');" style="width:145px"><a
							href="#" class="changePasswordHeader japaneseFont"> パスワードを変更する</a></li>
						<li class="logoutHeaderWrapper" onclick="openLogOutPopUp();" style="width:145px"><a
							href="#" class="logoutHeader japaneseFont">ログアウト</a></li>
				</ul>
			</div>
			<div class="fujitsuIcon"></div>
		</div>
	</header>
</div>
</body>
</html>