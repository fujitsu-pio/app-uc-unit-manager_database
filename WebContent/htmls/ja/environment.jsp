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
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Cache-Control"
	content="no-cache, no-store, must-revalidate">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Expires" content="-1">

<script type="text/javascript" src="../../js/spin.js"></script>
<script type="text/javascript" src="../../js/jquery-1.9.0.min.js"></script>
<script type="text/javascript" src="../../js/jquery.js"></script>
<script type="text/javascript" src="../../js/jquery.modalbox.js"></script>

<script type="text/javascript" src="../../js/main/core/DaoConfig.js"></script>
<script type="text/javascript" src="../../js/main/core/PersoniumContext.js"></script>
<script type="text/javascript" src="../../js/main/util/extend.js"></script>
<script type="text/javascript" src="../../js/main/core/Principal.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<script type="text/javascript" src="../../js/informationStorage.js"></script>
<script type="text/javascript" src="../../js/uCellPopup.js"></script>
<script type="text/javascript" src="../../js/uCell.js"></script>

<script type="text/javascript" src="../../js/main/util/CopyObject.js"></script>
<script type="text/javascript" src="../../js/main/util/StringUtils.js"></script>
<script type="text/javascript" src="../../js/main/util/UrlUtils.js"></script>
<script type="text/javascript"
	src="../../js/main/http/PersoniumRequestHeaderBuilder.js"></script>
<script type="text/javascript" src="../../js/main/http/RestAdapter.js"></script>
<script type="text/javascript"
	src="../../js/main/http/RestAdapterFactory.js"></script>
<script type="text/javascript" src="../../js/main/http/PersoniumResponse.js"></script>
<!-- include core functions -->
<script type="text/javascript" src="../../js/main/core/DaoException.js"></script>

<!--script type="text/javascript" src="../../js/main/core/PersoniumContext.js"></script-->
<script type="text/javascript"
	src="../../js/main/core/AbstractODataContext.js"></script>
<script type="text/javascript" src="../../js/main/core/PersoniumCollection.js"></script>
<script type="text/javascript" src="../../js/main/core/DavCollection.js"></script>
<script type="text/javascript" src="../../js/main/core/ODataManager.js"></script>
<script type="text/javascript" src="../../js/main/core/EntitySet.js"></script>
<script type="text/javascript" src="../../js/main/core/Accessor.js"></script>
<script type="text/javascript" src="../../js/main/core/DavResponse.js"></script>
<script type="text/javascript" src="../../js/main/core/Account.js"></script>
<script type="text/javascript" src="../../js/main/core/AccountManager.js"></script>
<script type="text/javascript" src="../../js/main/core/Ace.js"></script>
<script type="text/javascript" src="../../js/main/core/Acl.js"></script>
<script type="text/javascript" src="../../js/main/core/AclManager.js"></script>
<script type="text/javascript" src="../../js/main/core/AssociationEnd.js"></script>
<script type="text/javascript"
	src="../../js/main/core/AssociationEndManager.js"></script>
<script type="text/javascript" src="../../js/main/core/Box.js"></script>
<script type="text/javascript" src="../../js/main/core/BoxManager.js"></script>
<script type="text/javascript" src="../../js/main/core/Cell.js"></script>
<script type="text/javascript" src="../../js/main/core/CellManager.js"></script>
<script type="text/javascript" src="../../js/main/core/EntityType.js"></script>
<script type="text/javascript"
	src="../../js/main/core/EntityTypeManager.js"></script>
<script type="text/javascript" src="../../js/main/core/Event.js"></script>
<script type="text/javascript" src="../../js/main/core/EventManager.js"></script>
<script type="text/javascript" src="../../js/main/core/ExtCell.js"></script>
<script type="text/javascript" src="../../js/main/core/ExtCellManager.js"></script>
<script type="text/javascript" src="../../js/main/core/ExtRoleManager.js"></script>
<script type="text/javascript" src="../../js/main/core/LinkManager.js"></script>
<script type="text/javascript"
	src="../../js/main/core/MetadataLinkManager.js"></script>
<script type="text/javascript" src="../../js/main/core/OwnerAccessor.js"></script>
<script type="text/javascript" src="../../js/main/core/PersoniumQuery.js"></script>
<script type="text/javascript" src="../../js/main/core/Relation.js"></script>
<script type="text/javascript" src="../../js/main/core/RelationManager.js"></script>
<script type="text/javascript" src="../../js/main/core/Role.js"></script>
<script type="text/javascript" src="../../js/main/core/RoleManager.js"></script>
<script type="text/javascript"
	src="../../js/main/core/ServiceCollection.js"></script>
<script type="text/javascript" src="../../js/main/core/UnitManager.js"></script>
<script type="text/javascript" src="../../js/main/core/ODataCollection.js"></script>
<script type="text/javascript" src="../../js/main/core/ODataResponse.js"></script>
<script type="text/javascript" src="../../js/main/core/Message.js"></script>
<script type="text/javascript"
	src="../../js/main/core/SentMessageManager.js"></script>
<script type="text/javascript"
	src="../../js/main/core/ReceivedMessageManager.js"></script>
<script type="text/javascript" src="../../js/mainNavigation.js"></script>
<script type="text/javascript" src="../../js/commonFunctions.js"></script>
<script type="text/javascript" src="../../js/uAccount.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<script type="text/javascript" src="../../js/uBox.js"></script>
<script type="text/javascript" src="../../js/boxProfile.js"></script>
<script type="text/javascript" src="../../js/uRole.js"></script>
<script type="text/javascript" src="../../js/uRelation.js"></script>
<!-- <script type="text/javascript" src="../../js/roleToAccountMapping.js"></script> -->
<script type="text/javascript" src="../../js/externalCell.js"></script>
<script type="text/javascript"
	src="../../js/externalCellToRelationMapping.js"></script>
<script type="text/javascript"
	src="../../js/relationToExternalCellMapping.js"></script>
<script type="text/javascript" src="../../js/relationToRoleMapping.js"></script>
<script type="text/javascript" src="../../js/externalRole.js"></script>
<script type="text/javascript" src="../../js/odata.js"></script>
<script type="text/javascript" src="../../js/uCollectionOdata.js"></script>
<script type="text/javascript" src="../../js/registerFileAsService.js"></script>
<script type="text/javascript" src="../../js/cellProfile.js"></script>
<script type="text/javascript" src="../../js/boxAcl.js"></script>
<script type="text/javascript" src="../../js/cellAcl.js"></script>
<script type="text/javascript" src="../../js/mainBoxAcl.js"></script>
<script type="text/javascript" src="../../js/main/http/PersoniumHttpClient.js"></script>
<script type="text/javascript" src="../../js/main/http/Promise.js"></script>
<script type="text/javascript" src="../../js/home.js"></script>
<script type="text/javascript" src="../../js/boxDetail.js"></script>
<script type="text/javascript" src="../../js/prettify.js"></script>

<!-- <link href="../../css/stylesheet.css" rel="stylesheet" type="text/css"> -->
<!-- <link href="../../css/commonStylesheet.css" rel="stylesheet" type="text/css"> -->
<link href="../../css/cellProfileStylesheet.css" rel="stylesheet" type="text/css">
<link href="../../css/japaneseStylesheet.css" rel="stylesheet" type="text/css">
<link href="../../css/oDataStylesheet.css" rel="stylesheet" type="text/css">
<link href="../../css/sprite1Stylesheet.css" rel="stylesheet" type="text/css">
<link href="../../css/layoutStylesheet.css" rel="stylesheet" type="text/css">
<link href="../../css/prettyPrintStylesheet.css" rel="stylesheet" type="text/css">

<!-- <link href="../../css/oDataStylesheet.css" rel="stylesheet" type="text/css"> -->
<!-- <link href="../../css/homeStylesheet.css" rel="stylesheet" type="text/css"> -->

<script type="text/javascript">

$(document).ready(function(){
		var target = document.getElementById('spinnerEnvt');
		var spinner = new Spinner(opts).spin(target);
		var requestId = '<%=session.getAttribute("requestId")%>';
		$("#CSRFTokenDisplayEnvironment").val(requestId);
		var objHome = new home();
		objHome.setEnvironmentVariables();
		//$("#mainContainer").hide();
		$(document).attr('title', sessionStorage.selectedEnvName);
		var unitURL = sessionStorage.selectedUnitUrl;
		var cellName = sessionStorage.selectedcell;
		var cellURL = "";
		if (unitURL != undefined) {
			if (!unitURL.endsWith("/")) {
				unitURL = unitURL + "/";
			}
			cellURL = unitURL + cellName;
			if (!cellURL.endsWith("/")) {
				cellURL += "/";
			}
		}
		$('#cellURLHeading').html(cellURL);
		$('#cellURLHeading').attr('title', cellURL);
		$('#cellNameHeading').html(cellName);
		$('#cellNameHeading').attr('title', cellName);
	});
</script>
<title></title>
</head>
<body id="cellProfileBody" class="cellProfileBody setWhiteBackGround">
	<div id="mainContainer" style="display: none">
		<div id="contentWrapper">
			<div id="leftPanel" onclick="expandCellList();">
				<input type="button" value="" class="minCellList"
					id="btnMinCellList" tabindex="-1">
			</div>
			<div id="dvCellListContainer" class="mainellListContainer"></div>
			<div id="rightPanel" class="rightPanelDefaultHeight"
				onclick="collapseCellList();">
				<div id="header">
					<div id="leftHeading" class="leftHeading">
						<div id="cellNameHeading" class="cellNameHeading"></div>
						<div id="cellURLHeading" class="cellURLHeading"></div>
					</div>
					<div id="rightHeading" class="rightHeading">
						<input type="button" id="tokenBtn" class="tokenBtn japaneseFont"
							onclick="openCreateEntityModal('#tokenPopUpModalWindow','#tokenDialogBox', 'txtAccessToken');"
							value="トークン" tabindex="-1"/>
					</div>
				</div>
				<div id="navigationBar">
					<ul class="navList">
						<li id="boxNav" class="boxNav selected" onclick="boxListData();">
							<div id="boxTabWrapper" class="boxTabWrapper" style="width:125px">
								<div id="boxIcon" class="boxIcon"></div>
								<label id="boxText" class="boxText mainNavFontSize">Box</label>
							</div>
						</li>
						<li id="roleNav" class="roleNav" onclick="roleListData();">
							<div id="roleTabWrapper" class="roleTabWrapper">
								<div id="roleIcon" class="roleIcon"></div>
								<label id="roleText" class="roleText mainNavFontSize">ロール</label>
							</div>
						</li>
						<li id="accountNav" class="accountNav"
							onclick="accountListData();">
							<div id="accountTabWrapper" class="accountTabWrapper">
								<div id="accountIcon" class="accountIcon"></div>
								<label id="accountText" class="accountText mainNavFontSize">アカウント</label>
							</div>
						</li>
						<li id="socialNav" class="socialNav">
							<div id="socialTabWrapper" class="socialTabWrapper" style="width: 153px;">
								<div id="socialIcon" class="socialIcon"></div>
								<label id="socialText" class="socialText mainNavFontSize">ソーシャル</label>
								<div id="socialArrow" class="socialArrow"></div>
							</div>
							<ul class="socialSubMenu">
								<li class="externalCellWrapper" onclick="externalCellList();"><a
									href="#" class="externalCell mainNavFontSize">外部セル</a></li>
								<li class="relationWrapper" onclick="relationListData();"><a
									href="#" class="relation mainNavFontSize">関係</a></li>
								<li class="externalRoleWrapper" onclick="externalRoleList();"><a
									href="#" class="externalRole mainNavFontSize">外部ロール</a></li>
							</ul>
						</li>
						<li id="messageNav" class="messageNav">
							<div id="messageTabWrapper" class="messageTabWrapper" >
								<div id="messageIcon" class="messageIcon"></div>
								<label id="messageText" class="messageText mainNavFontSize">メッセージ</label>
								<div id="messageArrow" class="messageArrow"></div>
							</div>
							<ul class="messageSubMenu">
								<li class="externalCellWrapper" onclick="openReceivedMessageList();"><a
									href="#" class="externalCell mainNavFontSize">受け</a></li>
								<li class="externalRoleWrapper" onclick="uMainNavigation.openSentMessageList();">
									<a href="#" class="externalRole mainNavFontSize" >送信済み</a>
								</li>
							</ul>
						</li>
						<li id="logNav" class="logNav" onclick="uMainNavigation.logViewData();">
							<div id="logTabWrapper" class="logTabWrapper">
								<div id="logIcon" class="logIcon"></div>
								<label id="logText" class="logText mainNavFontSize">ログ</label>
							</div>
						</li>
						<li id="infoNav" class="infoNav" onclick="uMainNavigation.CellInfoNavigationData();">
							<div id="infoTabWrapper" class="infoTabWrapper" style="width: 90px;">
								<div id="infoIcon" class="infoIcon"></div>
								<label id="infoText" class="infoText mainNavFontSize">インフォ</label>
							</div>
						</li>
					</ul>
				</div>
				<div id="mainContent"
					style="padding-right: 20px; min-width: 1228px;"></div>
				<div id="mainContentWebDav"
					style="padding-right: 20px; min-width: 1228px; dispaly: none;"></div>
					<div style="display: none; line-height: 24px;"
				id="dvemptyTableMessage"
				class="emptyTableMessage jpEmptyTableMessageWidth japaneseFont"></div>
			</div>

		</div>
	</div>
	<div id="spinnerEnvt"></div>
	<div id="spinner"></div>
	<!-- <input type="text" id="source" value="exist" style="display: none;" />
	Create cell popup: start
	<div id="createCellModal" class="modelback" style="display: none;">
		<div class="modal-window block-border"
			style="display: block; height: 168px; width: 375px; background: #ffffff"
			id="createCellDialog">
			<div class="popupWrapper" id="createCellpopup">
				<a href="#" title="Close" id="modalbox-close" class="closeIcon"
					onclick="closeCreateCell();">Close</a>
				<header class="popupHeader"> Create Cell </header>
				<div class="popupContent">
					<section class="popupInnerContent">
						<form>
							<table width="100%" border="0" class="popupCellTable">
								<tr>
									<td width="50%" valign="top"><label>Cell Name</label></td>
									<td width="50%"><input name="" type="text"
										id="textCellPopup" value="" class="textBoxPopup"> <span
										class="popupAlertArea">
											<aside class="popupAlertmsg" id="popupErrorMsg"></aside>
									</span></td>
								</tr>
							</table>
						</form>
					</section>
					<section class="bottomBtn">
						<input type="submit" value="Cancel" onclick="closeCreateCell();"
							class="normalButtonGrey" id="modalbox-close12" /> <input
							type="submit" onclick="newCellCreate();" value="Create Cell"
							class="normalButtonBlue" />
					</section>
				</div>
			</div>
		</div>
	</div>
	Create cell popup: end
	Delete cell popup: start
	<div id="deleteModal" class="modelback" style="display: none;">
		<div class="modal-window block-border"
					style="border: 1px solid black;display: block; height: 325px; width: 375px; background: #ffffff" id="deleteDialog">
			<div class="popupWrapper" id="deleteAccountpopup">
				<a href="#" title="Close" id="modalbox-close" class="closeIcon" onclick="closeDeleteModalWindow();">Close</a>
				<header class="popupHeader"> Delete Cell </header>
				<div class="popupContent">
					<section class="popupInnerContent">
						<aside class="deleteAccount" style="height: 25px;text-align: left;margin-left:20px;">Do you want to delete selected Cell?</aside>
						<aside class="deleteAccount" style="text-align: left;margin-left:20px;">Please enter the verification text you see below:</aside>
						<input style="margin-right: 15%;text-align: center;font-size: 1.5em;height: 30px;margin-bottom: 11px;margin-top: 6px;letter-spacing: 7px;" type="text" id="labelCapcha" onfocus="this.blur()" class="textBoxPopup"/>
						<input type="text" id="textCapcha" class="textBoxPopup" style="margin-left: 21%;float: none;">
						<span class="popupAlertArea">
							<aside class="popupAlertmsg" id="popupErrorMsgRecursiveCellDelete" style="margin-left:69px;"></aside>
						</span>
					</section>
					<section class="popupInnerContent">
					</section>
					<section class="bottomBtn">
						<input type="button" value="Cancel" onclick="closeDeleteModalWindow();" class="normalButtonGrey" />
						<input type="button" value="Ok" onclick="deleteCell();" class="normalButtonBlue" />
					</section>
				</div>
			</div>
		</div>
	</div>
	Delete cell popup: end
	Spinner code - Start
			<div id="modalSpinnerCellProfile" class="modelbackSpinner" style="display: none;">
				<div class="modal-window block-border"
					style="display: block; height: 0; width: 0; background:transparent;position:absolute;top:50%;left:50%;border:0"
					id="dialogSpinner">
					<div id="spinnerPopUp">
					</div>
				</div>
			</div>
	Spinner code - End
	--Edit Cell Profile Start
	<div id="editCellProfileModalWindow" class="modelback"
		style="display: none;">
		<div class="modal-window block-border"
			style="display: block; height: 370px; width: 375px; background: #ffffff"
			id="editCellProfileDialogBox">
			<div class="popupWrapper" id="editCellProfilePopup">
				<a href="#" title="Close" id="modalbox-close" class="closeIcon"
					onclick="closeEntityModal('#editCellProfileModalWindow')">Close</a>
				<header class="popupHeader"> Edit Cell
					Profile </header>
				<div class="popupComplexTypePropContent" style="height:365px">
					<section class="popupInnerContent" >
						<form>
							<table width="100%" border="0" class="popupCommonTable">
								<tr>
									<td width="40%" valign="top"><label>Display Name</label></td>
									<td><input name="" type="text" value=""
										class="textBoxPopup" id="editDisplayName"> <span
										class="popupAlertArea">
											<aside class="popupAlertmsg"
												id="popupEditDisplayNameErrorMsg"></aside>
									</span></td>
								</tr>
								<tr>
									<td width="60%" valign="top"><label>Description</label></td>
									<td><textarea name="" cols="" rows="" class="textBoxPopup"
											style="height: 120px" id="editDescription"></textarea> <span
										class="popupAlertArea">
											<aside class="popupAlertmsg" style="height:0px;"
												id="popupEditDescriptionErrorMsg"></aside>
									</span></td>
								</tr>
								<tr>
									<td  valign="top"><label>Photo</label></td>
									<td>
									<input type="button" value="Browse"
										onclick="document.getElementById('idImgFile').click();"
										class="normalButtonBrowse flRt" /> 
										<input type="file" id="idImgFile" class="fileUpload"  multiple="multiple" onchange="uCellProfile.attachFile('popupEditUserPhotoErrorMsg', 'idImgFile');" />
										<span class="popupAlertArea">
										<aside class="popupAlertmsg" id="popupEditUserPhotoErrorMsg"></aside>
									</span>
								</td>
								</tr>
								<tr>
									<td></td>
									<td>
										<label id = "lblProfileImage" style="display: none;"></label>
										<a href = "#"  id = "btRremoveProfileImage" class="profileImageDeleteIcon" onclick="uCellProfile.removeImage();" style="display: none"></a>
									</td>	
								</tr>
							</table>
						</form>
					</section>
					<section class="bottomBtn">
						<input type="button" value="Cancel" class="normalButtonGrey"
							onclick="closeEntityModal('#editCellProfileModalWindow')" /> <input
							type="button" value="Ok" class="normalButtonBlue"
							id="btnEditCellProfileData" onclick="uCellProfile.updateCellProfile();"/>
					</section>
				</div>
			</div>
		</div>
	</div>
	<input type="file" id="idImgFile" style="visibility: hidden;" multiple="multiple" onchange="uCellProfile.attachFile();" />
	-Edit Cell Profile End  -->

<!-- Start: Cell Create Template and Create Popup UI -->
	<div id="createCellModal" class="modelback" style="display: none;">
		<!-- Select template popup: start -->
		<div class="modal-window block-border"
			style="display: block; height: 530px; width: 706px; background: #ffffff"
			id="createCellDialog">
			<div id="selectTemplateBody">
				<div class="popupCellHeader">
					<div class="popupCellHeading japaneseFont">セルを作成します。</div>
					<!-- <div class="crossCellIcon" id="closeCreateModal"
						onClick="cellpopup.closePopup();"></div> -->
							<div id="closeCreateModal" class="closeIconDiv">
				<a style="margin:0px;" href="#" title="クローズ" id="modalbox-close" class="popupCloseIcon"
					onclick="cellpopup.closePopup();" tabindex="2"></a>
			</div>
				</div>
				<div class="popupCellBody">
					<div class="popupCellBodyRow" id="selTemplateRow0"
						onClick="cellpopup.highlightSelRow(0,5,true);"
						onMouseOver="cellpopup.hoverSelRow(0,true)"
						onMouseOut="cellpopup.mouseOutSelRow(0,true)">
						<div class="popupCellBodyLeft japaneseFont">セル</div>
						<div class="popupCellBodyRight japaneseFont">空白のセルを作成して開始します。</div>
					</div>
				</div>
				<div style="margin-right: 10px;"></div>
				<div class="popupCellBottom" style="margin-top: 47px;">
					<div class="pageFwdIcon" onClick="cellpopup.createCellUI();" tabindex="1" id="btnFwdCellCreate" title="次"></div>
				</div>
			</div>
			<!-- Select template popup: end -->

			<!-- Create Cell Popup: start -->
			<div id="createCellBody" style="display: none;">
				<div class="popupCellHeader" style="height: 31px;">
					<div class="popupCellHeading japaneseFont">セルを作成します。</div>
					<!-- <div class="crossCellIcon" id="createCellModal"
						onClick="cellpopup.closePopup();"></div> -->
							<div id="createCellModal" class="closeIconDiv">
				<a style="margin:0px;" href="#" title="クローズ" id="modalbox-close" class="popupCloseIcon"
					onclick="cellpopup.closePopup();" tabindex="12"></a>
			</div>
				</div>
				<div class="popupCreateCellBody" style="height: 405px;">
					<div id="leftSection" class="cellPopupLeft">
						<div class="popuplabeltop japaneseFont" style="margin-top: 48px;">* セル名</div>
						<input type="text" class="popupTextBox"
							id="textCellPopup" onkeyup="cellpopup.populateCellURL();" tabindex="1"/> <span
							class="popupAlertArea">
							<aside class="cellpopupInlineComment japaneseFont LHSAlertMessage" id="popupCellNameErrorMsg"></aside>
						</span>
						<div class="popuplabel japaneseFont" id="cellURLTitle">携帯URL</div>
						<div class="popuplabelellipses" id="cellURL"></div>
						<div class="popuplabel">
							<div style="float: left;">
								<input id="chkAdmin" type="checkbox"
									class="checkBox cursorHand regular-checkbox big-checkbox"
									style="display: inline-block;opacity: 0;" tabindex="2" onfocus="checkboxFocusOnCell('#lblCellCreateAdmin');" onblur="checkboxBlurOnCell('#lblCellCreateAdmin');"> <label for="chkAdmin" style="cursor: pointer;margin-left: -17px"
									class="customChkbox" id = "lblCellCreateAdmin"></label>
							</div>
							<div style="margin-left: 23px; line-height: 16px;" class="japaneseFont">管理者ロールを持つアカウントを作成</div>
						</div>

						<div class="popuplabeldisabled japaneseFont" id="trAdmin">管理者名 <input type="text" 
								class="popupTextBox" name="txtRoleName" id="txtRoleName"
								disabled tabindex="3"/> <span class="popupAlertArea ">
								<aside class="cellpopupInlineComment japaneseFont LHSAlertMessage" id="popupRoleNameErrorMsg"></aside>
							</span>
						</div>
					</div>
					<div id="rightSection" class="cellPopupRight">
						<div class="popuplabel">
							<div style="float: left;">
								<input id="chkCreateProfile" type="checkbox"
									class="checkBox cursorHand regular-checkbox big-checkbox"
									style="display: inline-block;opacity: 0;" disabled tabindex="4" onfocus="checkboxFocusOnCell('#lblCellCreateProfile');" onblur="checkboxBlurOnCell('#lblCellCreateProfile');"/> <label style="cursor: pointer;margin-left: -17px"
									for="chkCreateProfile" class="customChkbox" id = "lblCellCreateProfile"></label>
							</div>
							<div style="margin-left: 23px; line-height: 16px;" class="japaneseFont">プロファイルの作成</div>
						</div>
					<div class="RHSElements" id="cellProfileElements">
						<div class="popuplabeltop japaneseFont">表示名</div>
						<input type="text" class="popupTextBox"
							id="trDisplayName" disabled tabindex="5"/>
							<span  style="height: 6px;" class="popupAlertArea">
							<aside class="popupAlertmsg RHSAlertMsg japaneseFont" id="popupCellDisplayNameErrorMsg"></aside>
						</span>
						<div class="popuplabel japaneseFont"><label style="float: left;width: 100px;">説明</label><!-- <textarea rows="5" cols="33" class="cellpopuplabel"
								style="resize: none;" id="trDescription" disabled></textarea> -->
								<textarea name="" cols="" rows="" style="width: 247px;padding-right: 10px;margin-top: 10px;color: #3c3c3c;" class="bigTextBox bigTextBoxHeight entityGridTbody" id="trDescription" tabindex="6"></textarea>
								<span style="height: 6px;padding-top:0px" class="popupAlertArea">
							<aside class="popupAlertmsg RHSAlertMsg japaneseFont" id="popupCellDescriptionErrorMsg"></aside>
						</span>
						</div>
						<div id="dvApplicationCellProfileImage"
						class="popuplabeltop japaneseFont" style="margin-top: 13px;margin-bottom: 10px;">アプリケーションイメージ</div>
					<section class="positionImageSection" id="imgCellProfileSection">
						<div class="floatLeft" id="dvCellProfileImage">
								<table >
									<tr>
										<td align="center" valign="middle" class="imgBorder">
											<figure id="figCellProfile" class="boxProfileImage">
													<img style="display: none; margin: auto;"
														id="imgCellProfile" src="#" alt="image" />
											</figure>
										</td>
									</tr>
								</table>
							</div>
							<div id="dvCellBrowseButtonSection" class="browseButtonSection" >
								<div>
									<label class="itemInfo positionImageInfo japaneseFont"
										id="lblCellNoFileSelected">選択されていませんファイル </label>
								</div>
								<div class="browseButton">
									<div id="dvCellBrowse" class="file-input-wrapper">
										<button title="" id="btnCellProfileBrowse" style="cursor: default" class="btn-file-input btnDisabled japaneseFont">ブラウズ</button>
										<input style="cursor: default" title=" "
											onchange="selectFile(this,'popupCellImageErrorMsg', 'idImgFileCell','#imgCellProfile','#lblCellNoFileSelected','#figCellProfile');"
											id="idImgFileCell" type="file" name="file" tabindex="7" onfocus="divOnFocusBrowseOnCell();" onblur="divOnBlurBrowseOnCell();"/>
									</div>
								</div>
							</div>
						</section>
								<section class="positionTopDiv positionApplicationImageSection">
						<div>
							<label class="floatLeft itemInfo japaneseFont" id="cellImageInfo">あなたは、サイズmaxの任意の画像をアップロードすることができます。JPEG / PNG形式の50キロバイト、次元180px X180 PX
							</label>
						</div>
						<span  style="height: 6px;padding-top: 0px;" class="popupAlertArea">
							<aside class="popupAlertmsg RHSAlertMsg japaneseFont" id="popupCellImageErrorMsg"></aside>
						</span>
					</section>
						<div class="popuplabel" id="btnPublic" style="margin-top: 31px;">
							<div style="float: left;">
								<input id="publicProfile" type="checkbox"
									class="checkBox cursorHand regular-checkbox big-checkbox"
									style="display: inline-block;opacity:0;" tabindex="8" onfocus="checkboxFocusOnCell('#lblCellCreateAllow');" onblur="checkboxBlurOnCell('#lblCellCreateAllow');"> <label for="publicProfile"
									class="customChkbox addOrRemoveCursor" style="margin-left: -17px" id ="lblCellCreateAllow"></label>
							</div>
							<div style="margin-left: 23px; line-height: 16px;" class="japaneseFont">誰もがプロファイルを読めるようにする</div>
						</div>
						<!-- <div class="popuplabel">
							Application Image
							<div class="browse"></div>
							<div class="popuplabelmessage">You can upload any image of
								size max. 50 kb, dimension 500 px * 600 px in JPEG/PNG format</div>
						</div> -->
						</div>
					</div>
				</div>
				<div class="popupCellCreateBottom">
					<div class="pagePrevIcon" onClick="cellpopup.moveToPrevPage();" tabindex="9" title="前"></div>
					<div class="createCellIcon"
						onClick="cellpopup.performCellCreate();" tabindex="11" title="作る"></div>
					<div class="cancelCellCreateIcon" onClick="cellpopup.closePopup();" tabindex="10" title="キャンセル"></div>
				</div>
			</div>
			<!-- Create Cell Popup: end -->
		</div>
	</div>
	<!-- End: Cell Create Template and Create Popup UI -->




	<!-- Token Pop UP Start -->
	<div id="tokenPopUpModalWindow" class="modelback"
		style="display: none;">
		<div class="modal-window block-border"
			style="display: block; height: 506px; width: 706px; background: #ffffff"
			id="tokenDialogBox">
			<div id="dvCloseIcon" class="closeIconDiv">
				<a style="margin:0px;" href="#" title="クローズ" id="modalbox-close" class="popupCloseIcon"
					onclick="closeEntityModal('#tokenPopUpModalWindow')" tabindex="3"></a>
			</div>
			<div class="accessTokenDiv" id="dvAccessheader">
				<label class="AccessTokenHeading japaneseFont" id="lblHeadingAccessToken">
					アクセストークン</label>
			</div>
			<div class="accessTokenLabel">
				<label class="japaneseFont">以下のトークン（CTRL+ C）とペースト（Ctrl + V）は、アクセストークンとして許可ヘッダーにコピーしやすさ、環境を管理するためのポータルと同じ権限でAPIを使用します。 </label>
			</div>
			<div class="dvTextArea">
				<div id="dvTokenTextArea" class="outerDvTextArea">
					<!-- <textarea name="" cols="" rows="" class="textAreaShowAccessToken"
						readonly="readonly" id="txtAccessToken" tabindex="1"></textarea> -->
				</div>
			</div>
			<div class="accessTokenLabel">
				<label id="lblExpiryTime" class="tokenExpiryTime"></label>
			</div>
			<div id="dvClose" class="dvClose">
				<input type="button" id="btnClose" class="btnClose japaneseFont"
					onclick="closeEntityModal('#tokenPopUpModalWindow')" value="クローズ" tabindex="2">
			</div>
		</div>
	</div>
	<!-- -Token POP up End -->

	<!-- Session Expiration Pop Up Start -->
	<div id="timeOutCellProfileModalWindow" class="modelback"
		style="display: none;">
		<div class="modal-window block-border"
			style="display: block; height: 242px; width: 466px; background: #ffffff"
			id="timeOutCellProfileDialogBox">
			<section class="headerSection">
				<div id="dvCloseIcon" class="closeIconDiv">
					<a style="margin: 0px;" href="#" title="クローズ" id="modalbox-close"
						class="popupCloseIcon" onclick="window.close();" tabindex="2"></a>
				</div>
				<div class="sessionExpiryDiv" id="dvAccessheader">
					<label class="sessionExpiredPopUpHeading japaneseFont"
						id="lblHeadingAccessToken">セッションが期限切れ</label>
				</div>
			</section>
			<div class="sessionExpiredLabel">
				<label class="japaneseFont">あなたのセッションの有効期限が切れています。もう一度ログインしてください。 </label>
			</div>
			<div id="dvClose" class="sessionPopUpCloseSection">
				<input type="button" id="btnTimeout" class="normalButton japaneseFont"
					onclick="window.close();" value="よし" tabindex="1">
			</div>
		</div>
	</div>
	<!-- Session Expiration Pop Up End  -->
	<input type="hidden" name="CSRFToken" value=""
		id="CSRFTokenDisplayEnvironment">
</body>
</html>
