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
//* @class ExtCellのCRUDのためのクラス.
//* @constructor
//* @augments dcc.ODataManager
//*/
/**
 * It creates a new object dcc.ExtCellManager.
 * @class This class performs CRUD operations for External Cell.
 * @constructor
 * @augments dcc.ODataManager
 * @param {dcc.Accessor} Accessor
 */
dcc.ExtCellManager = function(as) {
  this.initializeProperties(this, as);
};
dcc.DcClass.extend(dcc.ExtCellManager, dcc.ODataManager);

///**
//* プロパティを初期化する.
//* @param {dcc.ExtCellManager} self
//* @param {dcc.Accessor} as アクセス主体
//*/
/**
 * This method initializes the properties of this class.
 * @param {dcc.ExtCellManager} self
 * @param {dcc.Accessor} as Accessor object
 */
dcc.ExtCellManager.prototype.initializeProperties = function(self, as) {
  this.uber = dcc.ODataManager.prototype;
  this.uber.initializeProperties(self, as);
};

///**
//* URLを取得する.
//* @return {String} URL
//*/
/**
 * This method returns the URL for performing operations on External Cell.
 * @return {String} URL
 */
dcc.ExtCellManager.prototype.getUrl = function() {
  var sb = "";
  sb += this.getBaseUrl();
  sb += this.accessor.cellName;
  sb += "/__ctl/ExtCell";
  return sb;
};

///**
//* ExtCellを作成.
//* @param body requestBody
//* @return {dcc.ExtCell}ExtCellオブジェクト
//* @throws {DaoException} DAO例外
//*/
/**
 * This method creates an External Cell.
 * @param {Object} body requestBody
 * @return {dcc.ExtCell}ExtCell object
 * @throws {dcc.DaoException} DAO exception
 */
dcc.ExtCellManager.prototype.create = function(body) {
  var requestBody = JSON.stringify(body);
  var json = this.internalCreate(requestBody);
  //if(json.response !== undefined){if(json.response.status === 409){return json.response.status;}}
  if(json.getStatusCode() >= 400){
    var response = json.bodyAsJson();
    //throw exception with code PR409-OD-0003
    throw new dcc.DaoException(response.message.value, response.code);
  }
  else if(json !== undefined){
    //showMessage(idModalWindow,isExternalCellCreatedFromSameUser,cellName);
  }
  var responseJson = json.bodyAsJson().d.results;
  return new dcc.ExtCell(this.accessor, responseJson);
};
///**
//* The purpose of this method is to display messages on successful registration of external cell.
//*/
/*function showMessage(idModalWindow,isExternalCellCreatedFromSameUser,entity) {
	addSuccessClass();
	inlineMessageBlock();
	var objCommon = new common();
	var shorterExternalCellName = objCommon.getShorterEntityName(entity);
	if (isExternalCellCreatedFromSameUser === true) {
		document.getElementById("successmsg").innerHTML = "External Cell "+ shorterExternalCellName + " successfully registered!";
		document.getElementById("successmsg").title = entity;
	}
	else if (isExternalCellCreatedFromSameUser === false) {
		document.getElementById("successmsg").innerHTML = "External Cell 'Library' successfully registered !";
	}
	$(idModalWindow + ", .window").hide();
	autoHideMessage();
}*/
///**
//* ExtCellを作成.
//* @param body リクエストボディ
//* @return 作成したExtCellオブジェクト
//* @throws {DaoException} DAO例外
//*/
//dcc.ExtCellManager.prototype.createAsMap = function() {
//var json = internalCreate(body);
//return new dcc.ExtCell(accessor, json);
//};
///**
//* ExtCellを取得.
//* @param {String} roleId 取得対象のRoleId
//* @return {dcc.ExtCell} 取得したしたExtCellオブジェクト
//* @throws {DaoException} DAO例外
//*/
/**
 * This method performs retrieve operation on External Cell.
 * @param {String} roleId RoleId
 * @return {dcc.ExtCell} ExtCell object
 * @throws {dcc.DaoException} DAO exception
 */
dcc.ExtCellManager.prototype.retrieve = function(roleId) {
  var json = this.internalRetrieve(roleId);
  return new dcc.ExtCell(this.accessor, json);
};

/**
 * The purpose of this function is to get etag
 * @param {String} id
 * @return {String} etag
 */
dcc.ExtCellManager.prototype.getEtag = function (id) {
  var json = this.internalRetrieve(id);
  return json.__metadata.etag;
};

/**
 * The purpose of this method is to perform delete operation for external cell.
 * @param {String} externalCellUrl
 * @param {String} etag
 * return {dcc.Promise} promise
 */
dcc.ExtCellManager.prototype.del = function(externalCellUrl, etag) {
  var key = externalCellUrl;
  var response = this.internalDel(key, etag);
  return response;
};
