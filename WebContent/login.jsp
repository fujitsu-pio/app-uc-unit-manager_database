<!DOCTYPE html>
<%@page contentType="text/html; charset=UTF-8" %>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Expires" content="-1">
<title>Personium Unit Manager</title>
<script type="text/javascript"
	src="./js/main/validation/login_validation.js"></script>
<script type="text/javascript" src="./js/jquery-1.9.0.min.js"></script>
<script type="text/javascript" src="./js/commonFunctions.js"></script>
<script type="text/javascript" src="./js/jquery.modalbox.js"></script>
<script type="text/javascript"
	src="./js/main/validation/reg_validation.js"></script>
<script type="text/javascript" src="./js/jquery.js"></script>
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link href="./css/loginStylesheet.css" rel="stylesheet" type="text/css">

<script>
history.pushState({},"","");
//On load function called for user id
$(document).ready(function(){
	$("#cpassdid").removeAttr("disabled");
	$("#useridtext").removeAttr("disabled");
		$("#useridtext").blur(function(){
		var uname = $(this).val();
		var validUserName = userid_validation(uname);
		if(validUserName){
			if(uname.length > 0) {
				$("#userspanid").html("");
				//$("#userspanid").html("Checking availability...");
				$.ajax
				({
				type: "POST",
				dataType : 'json',
				beforeSend: function (xhr) {
					xhr.setRequestHeader("Cache-Control", "no-cache, no-store, must-revalidate");
						xhr.setRequestHeader("Pragma", "no-cache");
						xhr.setRequestHeader("Expires", -1);
					},
				url: "check",
				data: "useridtext="+ escape(uname),
				cache: false,
				success: function(response) {
					var msg = "";
					if(response['userIdMsg'] == "inUse"){
						msg = uname + " is already in use";
						$("#userspanid").removeClass('alertSuccessMsg');
						$("#userspanid").addClass('alertmsg');
						$("#ProceedButton").attr("disabled","disabled");
					}else if(response['userIdMsg'] == "available"){
						msg = uname + " is available";
						$("#userspanid").removeClass('alertmsg');
						$("#userspanid").addClass('alertSuccessMsg');
						$("#ProceedButton").removeAttr("disabled");
					}
					$("#userspanid").html(msg);
				}
				});
			}
			else{
				$("#userspanid").html("");
			}
		}
	});
		
		var errorFlag = '<%=session.getAttribute("errorFlag")%>'; 	
		var showMessage = '<%=session.getAttribute("errorMessage")%>';
		if(errorFlag == 'null' && showMessage == 'null') {
			document.getElementById("logoutDiv").style.visibility = "hidden";
		} else {			
			document.getElementById("logoutDiv").style.visibility = "visible";
			$("#logoutDiv").addClass("loginErrorMessage");
			$("#logoutMsg").text(showMessage);
			'<%session.setAttribute("errorFlag",null);%>';
			'<%session.setAttribute("errorMessage",null);%>';
		} 
		
		var user=localStorage.user;
		if(user){
			document.getElementById("logoutDiv").style.visibility = "visible";
			$("#logoutDiv").addClass("logoutSuccess");
			//var username = sessionStorage.logoutShorterUName;
			$("#logoutMsg").text("You have logged out successfully.");
			$('#logoutMsg').attr('title',user);
			localStorage.user='';
		}
		var browserLanguage = navigator.language;
		if (browserLanguage != undefined && browserLanguage.toLowerCase() == 'ja') {
			$("#ddLanguageSelector").val('Japanese(jp)');
		}
});
function clearData(){
	$('#confirm').find('input:text').val('');
	$('#confirm').find('input:password').val('');
	$("#confirm").find('input:text').css("background-color","white");
	$("#confirm").find('input:password').css("background-color","white");
	$('#userspanid').html('');
	$('#passspanid').html('');
	$('#repassspanid').html('');
	$('#firstnameid').html('');
	$('#familynameid').html('');
	$('#orgspanid').html('');
	$('#emailspanid').html('');
}

//Closing account registration popup
function closeAcctReg() {
	clearData();
	$('#modalAcctReg, .window').hide();
}

//Closing the success registration popup
function closeSuccessMsg() {
	$('#successMsgBlock, .window').hide();
}

//Validating field values
function FillBilling(f) {
	if(document.getElementById("confirm").style.display == "block"){
		document.getElementById("confirm").style.display = "none";
		document.getElementById("register").style.display = "block";
	}
	else {
		document.getElementById("confirm").style.display = "none";
		document.getElementById("register").style.display = "block";
	}
	var trimcid = f.cid.value.toString();
	trimcid = trimcid.replace(/\s/g, '');
	trimcid = trimcid.trim(); 
	var trimcpassd = f.cpassd.value.toString();
	trimcpassd = trimcpassd.replace(/\s/g, '');
	trimcpassd = trimcpassd.trim();
	var trimcfName = f.cfName.value.toString();
	trimcfName = trimcfName.replace(/\s+/g, " ");
	trimcfName = trimcfName.trim();
	var trimcfamilyName = f.cfamilyName.value.toString();
	trimcfamilyName = trimcfamilyName.replace(/\s+/g, " ");
	trimcfamilyName = trimcfamilyName.trim();
	var trimcoName = f.coName.value.toString();
	trimcoName = trimcoName.replace(/\s+/g, " ");
	trimcoName = trimcoName.trim();
	f.rid.value = trimcid;
	f.rpassd.value = trimcpassd;
	f.rrpassd.value = f.crpassd.value;
	f.rfName.value = trimcfName;
	f.rfamilyName.value = trimcfamilyName;
	f.roName.value = trimcoName;
	f.remail.value = f.cemail.value;
}
	
//Cancel functionality on view screen
function back() {
	if (document.getElementById("register").style.display == "block") {
		document.getElementById("register").style.display = "none";
		document.getElementById("confirm").style.display = "block";
	}
}

//Opening account registration popup
function openAcctReg() { 
	//$("#serverErrorMsg").hide();
	document.getElementById("logoutDiv").style.visibility = "hidden";
	$('#userId').val('');
	$('#passwd').val('');
	$('#userspan').html('');
	$('#paswdspan').html('');
	var id = '#dialogAcctReg';
	// transition effect
	$('#modalAcctReg').fadeIn(1000);
	// Get the window height and width
	var winH = $(window).height();
	var winW = $(window).width();
	// Set the popup window to center
	$(id).css('top', winH / 2 - $(id).height() / 2);
	$(id).css('left', winW / 2 - $(id).width() / 2);
}

//opening success registration popup.
function openSuccesPopUp() {
	var id = '#successMsgPopup';
	$('#successMsgBlock').fadeIn(1000);			
	var winH = $(window).height();
	var winW = $(window).width();
	$(id).css('top', winH / 2 - $(id).height() / 2);
	$(id).css('left', winW / 2 - $(id).width() / 2);
}

//For checking the value after successful registration.
$(function() {
	setDynaicBrowserCompatabilitySection();
	setDynamicLoginSection();
	if (document.getElementById("userId") != null) {
		document.querySelector("#userId").spellcheck = false;
	}
	var flag = '<%=session.getValue("flag")%>';
	if (flag == 'true') {
		openSuccesPopUp();
		<%session.putValue("flag", false);%>
	}
});
</script>
</head>

<body>
	<div class="fjIcon">
	</div>
	<div id="loginContainer" style="min-width: 530px">
		<!-- Login Content Starts -->
		<div id="loginContentWrapper">
				<div class="dvloginSection" id = "dvloginSection">
				<div class="newLoginHeading">
				<!-- //<label class="fjLogo" id="logoContainer">Fujitsu Cloud PaaS Persona Container</label> -->
<!-- 				<img alt="" src="./images/login_logo.png"> -->
				</div>
				<div class="loginWrapper">
					<div class="browserSupportedMessage" id="incompatibleBrowseMsg">
					<label style="display: table;margin: 0 auto;font-size: 13pt;color: #c80000">Your browser is not supported.</label>
					</br>
					<label style="display: table;margin: 0 auto;">To use Personium unit manager, we recommend using the latest version</label>
					</br>
					<label style="display: table;margin: 0 auto;margin-top: -14px">of Google Chrome.</label>
					</div>
					<form name="login" action="./Login" method="POST" id="loginForm" style="margin-left: 106px;">
					<input type="hidden" name="CSRFTokenLogin" value='<%=application.getInitParameter("TOKEN")%>' id = "CSRFTokenLogin">
						<div style="visibility: visible; height: 15px;margin-top: 5px;" id="logoutDiv">
							<label id="logoutMsg" class="logoutMessage"></label>
						</div>
						<dl>
							<dt class="dtUserName">
								<input type="text" autocomplete="off" placeholder="Username" 
									name="userId" id="userId" class="txtUserName" onfocus="this.placeholder = ''" onblur="this.placeholder = 'Username'"/> <span
									class="newLoginAlertMessage"><aside id="userspan"
										style="display: none; width: 100%"></aside></span>
								<div class="clearBoth"></div>
							</dt>

							<dt class="dtPassword">
								<input type="password" autocomplete="off" placeholder="Password"
									name="passwd" id="passwd" class ="txtPassword" onfocus="this.placeholder = ''" onblur="this.placeholder = 'Password'"/> <span
									class="newLoginAlertMessage"><aside id="paswdspan"
										style="display: none; width: 100%"></aside></span>
								<div class="clearBoth"></div>
							</dt>
							<dt style="margin-top: 18px;height: 20px;">
							<select id="ddLanguageSelector" name="ddLanguageSelector" class="dropDownBig" onclick="" style="float: left;margin-top: -17px;margin-right: 10px;padding-bottom: 2px;margin-left: 80px;height: 30px;width: 150px;">
						
						<option value="en">English(en)</option>
<!-- 						<option value="ja">Japanese(ja)</option> -->
					</select>
							</dt>
							<dt class="dtLoginBtn">
								<span class="loginArrow"><input type="submit" value="Sign In"
									value="" class="loginButton"
									onclick="return validateForm();"/></span>
							</dt>
							<dt style="margin-top: 13px;">
								<span class="loginArrow"><a href="./forgotPassword.jsp" style="font-size: 10pt;color: #c80000;display: block;text-align: center;">Forgot Password</a></span>
							</dt>
						</dl>
					</form>
				</div>
				</div>
				<div class="dvnewLoginFooter">
				<footer id="fullLoginFooter">
				<label class="loginFooterPolicy">Privacy Policy</label>
				<br>
				<label class="loginFootercopyright">Copyright 2016 FUJITSU</label>
				</footer>
				</div>
			<!-- </div> -->
			<!-- <div class="signUp">
				<div class="signupText">New User click here to Register</div>
				<a href="registration_popup.html" class="modalBox" rel="460,573" title="Sign Up"><input type="button" value="Sign Up" class="normalButtonYellow"></a>
				<input type="button" value="Sign Up" class="normalButtonYellow"
					onclick="openAcctReg();">
			</div> -->
		</div>
		<div id="successMsgBlock" class="modelback" style="display: none;">
			<div class="modal-window block-border"
				style="display: block; height: 573px; width: 460px; background: #ffffff"
				id="successMsgPopup">
				<div id="confirmRegs">
					<a href="#" title="Close" id="modalbox-close" class="closeIcon"
						onclick="closeSuccessMsg();">Close</a>
					<figure>Your account has been created successfully.
					</figure>
				</div>
			</div>
		</div>
		<div id="succesMsg" style="display: none">
			<!-- <a href="htmls/Account_Registration_message.html" id="forceClick"
			class="modalBox" rel="460,565"></a> -->
			<input type="button" class="normalButtonYellow"
				onclick="openAcctReg();">
		</div>
		<!-- Login Content Ends -->
			</div>
		<div id="modalAcctReg" class="modelback" style="display: none;">
		<div class="modal-window block-border"
			style="display: block; height: 577px; width: 460px;"
			id="dialogAcctReg">
			<div class="regisForm" id="accRegs">
				<a href="#" title="Close" id="modalbox-close" class="closeIcon"
					onclick="closeAcctReg();">Close</a>
				<hgroup class="registTop">
					<h1>Sign Up</h1>
					<h2>*All Fields are mandatory!</h2>
				</hgroup>
				<form name="registration" action="./RegisterServlet" method="post">
				<input type="hidden" name="CSRFTokenUserRegistration" value='<%=application.getInitParameter("TOKEN")%>' id = "CSRFTokenUserRegistration">
					<div id="confirm">
						<div>
							<label>Username</label> <input type="text" class="textBoxNormal"
								value="" name="cid" id="useridtext"
								onfocus="validateData('cid')" /><span class="status"></span> <span
								class="alertmsg" id="userspanid"><aside class="deactive">*
									Please enter User ID</aside></span>
						</div>
						<div>
							<label>Password</label> <input type="password" autocomplete="off"
								class="textBoxNormal" value="" name="cpassd" id="cpassdid"
								onkeyup="validateData('cpassd')" /> <span class="alertmsg"
								id="passspanid" style="width:50%;"><aside class="deactive">* Please
									enter Password</aside></span>
						</div>
						<div>
							<label>Retype Password</label> <input type="password" autocomplete="off"
								class="textBoxNormal" name="crpassd" value="" id="crpassdid"
								onfocus="validateData('crpassd')" /> <span class="alertmsg"
								id="repassspanid" style="width:50%;"><aside class="deactive">*
									Please retype Password</aside></span>
						</div>
						<div>
							<label>First Name</label> <input type="text"
								class="textBoxNormal" value="" name="cfName" id="cfNameid"
								onfocus="validateData('cfName')" /> <span class="alertmsg"
								id="firstnameid"><aside class="deactive">* Please
									enter First Name</aside></span>
						</div>
						<div>
							<label>Family Name</label> <input type="text"
								class="textBoxNormal" value="" name="cfamilyName"
								id="cfamilyNameid" onfocus="validateData('cfamilyName')" /> <span
								class="alertmsg" id="familynameid"><aside
									class="deactive">* Please enter family Name</aside></span>
						</div>
						<div>
							<label>Organization Name</label> <input type="text"
								class="textBoxNormal" value="" name="coName" id="coNameid"
								onfocus="validateData('coName')" /> <span class="alertmsg"
								id="orgspanid"><aside class="deactive">* Please
									enter Organization Name</aside></span>
						</div>
						<div>
							<label>Email</label> <input type="text" class="textBoxNormal"
								value="" name="cemail" id="cemailid"
								onfocus="validateData('cemail')" /> <span class="alertmsg"
								id="emailspanid"><aside class="deactive">* Please
									enter Email ID</aside></span>
						</div>
						<div class="buttonArea">
							<input type="button" class="normalButtonGrey" value="Cancel"
								onclick="closeAcctReg();" /> <input type="button"
								class="normalButtonBlue" id="ProceedButton" value="Proceed"
								onclick="formValidation(registration);" />
						</div>
						<div class="clearBoth"></div>
					</div>
					<div id="register" style="display: none;">
						<div class="regFields">
							<label>User ID</label> <input type="text" class="textBoxNormal"
								disabled="disabled" value="" name="rid" />
						</div>
						<div class="regFields">
							<label>Password</label> <input type="password" autocomplete="off"
								class="textBoxNormal" disabled="disabled" value="" name="rpassd" />
						</div>
						<div class="regFields">
							<label>Retype Password</label> <input type="password" autocomplete="off"
								class="textBoxNormal" disabled="disabled" value=""
								name="rrpassd" />
						</div>
						<div class="regFields">
							<label>First Name</label> <input type="text"
								class="textBoxNormal" disabled="disabled" value="Micke"
								name="rfName" />
						</div>
						<div class="regFields">
							<label>Family Name</label> <input type="text"
								class="textBoxNormal" disabled="disabled" value="Robert"
								name="rfamilyName" />
						</div>
						<div class="regFields">
							<label>Organization Name</label> <input type="text"
								class="textBoxNormal" disabled="disabled" value="" name="roName" />
						</div>
						<div class="regFieldsLast">
							<label>Email</label> <input type="text" class="textBoxNormal"
								disabled="disabled" value="" name="remail" />
						</div>
						<div class="regFieldsConfirmMsg">
							<span class="alertConfirmmsg"><aside>"Please
									confirm the above information"</aside></span>
						</div>
						<div class="buttonArea">
							<input type="button" class="normalButtonGrey" value="Back"
								onclick="back();" /> <input type="submit"
								class="normalButtonBlue" value="Confirm" />
						</div>
						<div class="clearBoth"></div>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>