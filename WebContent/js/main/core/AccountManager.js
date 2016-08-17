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

/*global dcc:false */

///**
//* @class AccountのCRUDを行うためのクラス.
//* @constructor
//* @augments dcc.ODataManager
//* @property {Object} uber スーパークラスのプロトタイプへの参照.
//*/
/**
 * It creates a new object dcc.AccountManager.
 * @class This class is used for performing CRUD operations of Account.
 * @constructor
 * @augments dcc.ODataManager
 * @property {Object} uber A reference to the prototype of the superclass.
 * @param {dcc.Accessor} Accessor
 */
dcc.AccountManager = function(as) {
  this.initializeProperties(this, as);
};
dcc.DcClass.extend(dcc.AccountManager, dcc.ODataManager);

///**
//* プロパティを初期化する.
//* @param {dcc.AccountManager} self
//* @param {dcc.Accessor} as アクセス主体
//*/
/**
 * This method initializes the properties of this class.
 * @param {dcc.AccountManager} self
 * @param {dcc.Accessor} as accessor
 */
dcc.AccountManager.prototype.initializeProperties = function(self, as) {
  this.uber = dcc.ODataManager.prototype;
  this.uber.initializeProperties(self, as);

///**
//* パスワード用ヘッダーキー.
//*/
  /** Password for header key. */
  self.HEADER_KEY_CREDENTIAL = "X-Dc-Credential";
};

/**
 * This method returns the URL.
 * @returns {String} URL
 */
dcc.AccountManager.prototype.getUrl = function() {
  var sb = "";
  sb += this.getBaseUrl();
  sb += this.accessor.cellName;
  // sb += this.accessor.getCurrentCell().getName();
  sb += "/__ctl/Account";
  return sb;
};

///**
//* Accountを作成.
//* @param {dcc.Account} obj Accountオブジェクト
//* @param {?} password パスワード
//* @return {dcc.Account} Accountオブジェクト
//* @throws {DaoException} DAO例外
//*/
/**
 * This method creates an account.
 * @param {dcc.Account} obj Account object
 * @param {String} password password
 * @return {dcc.Account} Account object
 * @throws {dcc.DaoException} DAO exception
 */
dcc.AccountManager.prototype.create = function(obj, password) {
  var json = null;
  var headers = {};
  var responseJson = null;
  //var objCommon = new common();
  //var accName = obj.name;
  //var shorterAccountName = objCommon.getShorterEntityName(accName);
  if (obj.getClassName !== undefined && obj.getClassName() === "Account") {
    var body = {};
    body.Name = obj.getName();
    headers[this.HEADER_KEY_CREDENTIAL] = password;// obj.getPassword();
    json = this.internalCreate(JSON.stringify(body), headers);
    // TODO: Hard coded message need to be replaced.
    /*if (json !== undefined) {
			accountRefresh();
			addSuccessClass();
			inlineMessageBlock();
			//var shorterAccountName = objCommon.getShorterEntityName(accName);
			document.getElementById("successmsg").innerHTML = "Acccount "+ shorterAccountName + " created successfully!";
			document.getElementById("successmsg").title = accName;
			$('#createAccModal, .window').hide();
			autoHideMessage();
			//window.location.reload();
		}*/
    responseJson = json.bodyAsJson().d.results;
    obj.initializeProperties(obj, this.accessor, responseJson);
    return obj;
  } else {
    if (password !== null) {
      headers[this.HEADER_KEY_CREDENTIAL] = password;
    }
    var requestBody = JSON.stringify(obj);
    json = this.internalCreate(requestBody, headers);
    /*if (json != undefined) {
			accountRefresh();
			addSuccessClass();
			inlineMessageBlock();
			//var shorterAccountName = objCommon.getShorterEntityName(accName);
			document.getElementById("successmsg").innerHTML = "Acccount "+ shorterAccountName + " created successfully!";
			document.getElementById("successmsg").title = accName;
			$('#createAccModal, .window').hide();
			autoHideMessage();
			//window.location.reload();
		}*/
    if(json.getStatusCode() >= 400){
      var response = json.bodyAsJson();//throw exception with code
      throw new dcc.DaoException(response.message.value, response.code);
    }
    responseJson = json.bodyAsJson().d.results;
    return new dcc.Account(this.accessor, responseJson);
  }
};

/*function accountRefresh() {
	var contextRoot = sessionStorage.contextRoot;
	$("#mainContent").html('');
	$("#mainContent").load(contextRoot+'/htmls/accountListView.html', function() {
		if (navigator.userAgent.indexOf("Firefox") != -1) {
			loadAccountPage();
		}
	});
}
 */
///**
//* Accountを取得.
//* @param {String} name 取得対象のAccount名
//* @return {dcc.Account} 取得したしたAccountオブジェクト
//* @throws DaoException DAO例外
//*/
/**
 * This method fetches the account information.
 * @param {String} name account name
 * @return {dcc.Account} Account objecct
 * @throws {dcc.DaoException} DAO exception
 */
dcc.AccountManager.prototype.retrieve = function(name) {
  var json = this.internalRetrieve(name);
  if (json === true) {
    return true;
  }
  else {
    // returns response in JSON format.
    return new dcc.Account(this.accessor, json);
  }
};

///**
//* Passwordを変更.
//* @param {String} name Accountの名前
//* @param {String} password Accountパスワード
//* @throws DaoException DAO例外
//*/
/**
 * This method changes the account password.
 * @param {String} name Account
 * @param {String} password Account
 * @throws {dcc.DaoException} DAO exception
 */
dcc.AccountManager.prototype.changePassword = function(name, password) {
  var headers = {};
  headers[this.HEADER_KEY_CREDENTIAL] = password;

  var body = {};
  body.Name = name;
  this.internalUpdate(name, body, "*", headers);
};

///**
//* Delete the account.
//* @param {String} accountName account name
//* @return promise
//* @throws DaoException DAO例外
//*/
/**
 * Delete the account.
 * @param {String} accountName account name
 * @return {Object} resposne
 * @throws {dcc.DaoException} DAO exception
 */
dcc.AccountManager.prototype.del = function(accountName) {

  var key = "Name='" + accountName + "'";

  var response = this.internalDelMultiKey(key, "*");
  return response;
};

/**
 * The purpose of this method is to retrieve get etag on the basis of account name.
 * @param {String} name
 * @returns {String} etag
 */
dcc.AccountManager.prototype.getEtag = function(name) {
  var json = this.internalRetrieve(name);
  return json.__metadata.etag;
};


/**
 * The purpose of this method is to perform update operation or an account.
 * @param {String} accountName
 * @param {Object} body
 * @param {String} etag
 * @param {String} password
 * @return {dcc.ODataResponse} response
 */
dcc.AccountManager.prototype.update = function(accountName, body, etag,password) {
  var response = "";
  if (password !=="") {
    var headers = {};
    headers[this.HEADER_KEY_CREDENTIAL] = password;
    response = this.internalUpdate(accountName, body, etag,headers);
    return response;
  }
  else {
    response = this.internalUpdate(accountName, body, etag);
    return response;
  }
};

