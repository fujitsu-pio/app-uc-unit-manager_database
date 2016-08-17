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
//* @class RoleのCRUDのためのクラス.
//* @constructor
//* @augments dcc.ODataManager
//*/
/**
 * It creates a new object dcc.RoleManager.
 * @class This class performs CRUD operations for Role object.
 * @constructor
 * @augments dcc.ODataManager
 * @param {dcc.Accessor} Accessor
 */
dcc.RoleManager = function(as) {
  this.initializeProperties(this, as);
};
dcc.DcClass.extend(dcc.RoleManager, dcc.ODataManager);

///**
//* プロパティを初期化する.
//* @param {dcc.Accessor} self
//* @param {dcc.Accessor} as アクセス主体
//*/
/**
 * This method initializes the properties of this class.
 * @param {dcc.Accessor} self
 * @param {dcc.Accessor} as Accessor
 */
dcc.RoleManager.prototype.initializeProperties = function(self, as) {
  this.uber = dcc.ODataManager.prototype;
  this.uber.initializeProperties(self, as);
};

///**
//* RoleのリクエストURLを取得する.
//* @returns {String} URL
//*/
/**
 * This method gets the URL for Role operations.
 * @returns {String} URL
 */
dcc.RoleManager.prototype.getUrl = function() {
  var sb = "";
  sb += this.getBaseUrl();
  // HCL:-Changes done to get the cellName

  sb += this.accessor.cellName;
  sb += "/__ctl/Role";
  return sb;
};

///**
//* Roleを作成.
//* @param {dcc.Role} obj Roleオブジェクト
//* @return {dcc.Role} Roleオブジェクト
//* @throws {DaoException} DAO例外
//*/
/**
 * This method creates a Role.
 * @param {dcc.Role} obj Role object
 * @return {dcc.Role} Role object
 * @throws {dcc.DaoException} DAO exception
 */
dcc.RoleManager.prototype.create = function(obj) {
  var json = null;
  var responseJson = null;
  if (obj.getClassName !== undefined && obj.getClassName() === "Role") {
    var body = {};
    body.Name = obj.getName();
    body["_Box.Name"] = obj.getBoxName();
    json = this.internalCreate(JSON.stringify(body));
    responseJson = json.bodyAsJson().d.results;
    obj.initializeProperties(obj, this.accessor, responseJson);
    return obj;
  } else {
    var requestBody = JSON.stringify(obj);
    json = this.internalCreate(requestBody);
    if(json.getStatusCode() >= 400){
      var response = json.bodyAsJson();
      throw new dcc.DaoException(response.message.value, response.code);
    }
    return new dcc.Role(this.accessor, json.bodyAsJson().d.results);
  }
};

///**
//* Roleを取得(複合キー).
//* @param {String} roleName 取得対象のRole名
//* @param {String}boxName 取得対象のBox名
//* @return {dcc.Role} 取得したしたRoleオブジェクト
//* @throws {DaoException} DAO例外
//*/
/**
 * This method retrieves a Role object.
 * @param {String} roleName Role Name
 * @param {String}boxName Box name
 * @return {dcc.Role} Role object
 * @throws {dcc.DaoException} DAO exception
 */
dcc.RoleManager.prototype.retrieve = function(roleName, boxName) {
  var json = null;
  if (typeof boxName === "undefined") {
    json = this.internalRetrieve(roleName);

    //role doesn't exist and can be created.
    if (json === true) {
      return json;
    } else {
      return new dcc.Role(this.accessor, json);
    }
  }
  var key = "Name='" + roleName + "',_Box.Name='" + boxName + "'";
  json = this.internalRetrieveMultikey(key);
  //role doesn't exist and can be created.
 /* if (json === true) {
    return json;
  } else {*/
    return new dcc.Role(this.accessor, json);
  //}
};

/**
 * The purpose of this function is to update role details
 * @param {String} roleName
 * @param {String} boxName
 * @param {Object} body
 * @param {String} etag
 * @return {Object} response DcHttpClient
 */
dcc.RoleManager.prototype.update = function(roleName, boxName, body, etag) {
  var response = null;
  if (boxName !== undefined && boxName !== null) {
    var key = "Name='" + roleName + "',_Box.Name='" + boxName + "'";
    response = this.internalUpdateMultiKey(key, body, etag);
  } else {
    response = this.internalUpdate(roleName, body, etag);
  }
  return response;
};

///**
//* Roleを削除.
//* @param {String} roleName 削除対象のRole名
//* @param {String} boxName 削除対象のBox名
//* @return response promise
//* @throws {DaoException} DAO例外
//*/
/**
 * This method deletes a Role.
 * @param {String} roleName Role Name
 * @param {String} boxName Box Name
 * @return {Object} response
 * @throws {dcc.DaoException} DAO exception
 */
dcc.RoleManager.prototype.del = function(roleName, boxName) {
  var key = "Name='" + roleName + "'";
  if (boxName !== undefined && boxName !== null && boxName !=="undefined") {
    key += ",_Box.Name='" + boxName + "'";
  }
  var response = this.internalDelMultiKey(key, "*");
  return response;
};
