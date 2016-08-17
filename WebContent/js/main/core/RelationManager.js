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
//* @class RelationのCRUDのためのクラス.
//* @constructor
//* @augments dcc.ODataManager
//*/
/**
 * It creates a new object dcc.RelationManager.
 * @class This class performs CRUD operations for Relation object.
 * @constructor
 * @augments dcc.ODataManager
 * @param {dcc.Accessor} Accessor
 */
dcc.RelationManager = function(as) {
  this.initializeProperties(this, as);
};
dcc.DcClass.extend(dcc.RelationManager, dcc.ODataManager);

///**
//* プロパティを初期化する.
//* @param {dcc.AbstractODataContext} self
//* @param {dcc.Accessor} as アクセス主体
//*/
/**
 * This method initializes the properties of this class.
 * @param {dcc.AbstractODataContext} self
 * @param {dcc.Accessor} as Accessor
 */
dcc.RelationManager.prototype.initializeProperties = function(self, as) {
  this.uber = dcc.ODataManager.prototype;
  this.uber.initializeProperties(self, as);
};

///**
//* RelationのURLを取得する.
//* @returns {String} URL
//*/
/**
 * This method generates the URL for relation operations.
 * @returns {String} URL
 */
dcc.RelationManager.prototype.getUrl = function() {
  var sb = this.getBaseUrl();
  // HCL:-Changes done to get the cellName
  sb += this.accessor.cellName;
  sb += "/__ctl/Relation";
  return sb;
};

///**
//* Relationを作成.
//* @param {dcc.Relation} obj Relationオブジェクト
//* @return {dcc.Relation} Relationオブジェクト
//* @throws {DaoException} DAO例外
//*/
/**
 * This method performs create operation.
 * @param {dcc.Relation} obj Relation object
 * @return {dcc.Relation} Relation object
 * @throws {dcc.DaoException} DAO exception
 */
dcc.RelationManager.prototype.create = function(obj) {
  var json = null;
  if (obj.getClassName !== undefined && obj.getClassName() === "Relation") {
    var body = {};
    body.Name = obj.getName();
    body["_Box.Name"] = obj.getBoxName();
    json = this.internalCreate(JSON.stringify(body));
    obj.initializeProperties(obj, this.accessor, json);
    return obj;
  } else {
    var requestBody = JSON.stringify(obj);
    json = this.internalCreate(requestBody);
    if (json.getStatusCode() >= 400) {
      var response = json.bodyAsJson();// throw exception with code
      throw new dcc.DaoException(response.message.value, response.code);
    }
    return new dcc.Relation(this.accessor, json.bodyAsJson().d.results);
  }
};

///**
//* Relationを作成.
//* @param {Object} body リクエストボディ
//* @return {dcc.Relation} 作成したRelationオブジェクト
//* @throws {DaoException} DAO例外
//*/
//dcc.RelationManager.prototype.createAsMap = function(body) {
//var json = internalCreate(body);
//return new dcc.Relation(accessor, json);
//};

///**
//* Relationを取得(複合キー).
//* @param {String} relationName 取得対象のRelation名
//* @param {String}boxName 取得対象のBox名
//* @return {dcc.Relation} 取得したRelationオブジェクト
//* @throws {DaoException} DAO例外
//*/
/**
 * This method performs retrieve operation.
 * @param {String} relationName Relation name
 * @param {String} boxName Box name
 * @return {dcc.Relation} Relation object
 * @throws {dcc.DaoException} DAO exception
 */
dcc.RelationManager.prototype.retrieve = function(relationName, boxName) {
  var json = null;
  if (typeof boxName === "undefined") {
    json = this.internalRetrieve(relationName);
    //relation doesn't exist and can be created.
    if (json === true) {
      return json;
    } else {
      return new dcc.Relation(this.accessor, json);
    }
  }
  var key = "Name='" + relationName + "',_Box.Name='" + boxName + "'";
  json = this.internalRetrieveMultikey(key);
  //relation doesn't exist and can be created.
  if (json === true) {
    return json;
  } else {
    return new dcc.Relation(this.accessor, json);
  }
};

///**
//* Relation update.
//* @param {String} relationName 削除対象のRelation名
//* @param {String} boxName 削除対象のBox名
//* @param body
//* @param etag
//* @return promise
//* @throws {DaoException} DAO例外
//*/
/**
 * This method performs update operation.
 * @param {String} relationName Relation name
 * @param {String} boxName Box name
 * @param {Object} body
 * @param {String} etag
 * @return {dcc.Promise} promise
 * @throws {dcc.DaoException} DAO exception
 */
dcc.RelationManager.prototype.update = function(relationName, boxName, body, etag) {
     var response = null;
     if (boxName !== undefined && boxName !== null) {
      var key = "Name='" + relationName + "',_Box.Name='" + boxName + "'";
     response = this.internalUpdateMultiKey(key, body, etag);
    } else {
       response = this.internalUpdate(relationName, body, etag);
   }
  return response;
};

///**
//* Relationを削除(複合キー).
//* @param {String} relationName 削除対象のRelation名
//* @param {String} boxName 削除対象のBox名
//* @return promise
//* @throws {DaoException} DAO例外
//*/
/**
 * This method performs delete operation.
 * @param {String} relationName Relation name
 * @param {String} boxName Box name
 * @return {dcc.Promise} promise
 * @throws {dcc.DaoException} DAO exception
 */
dcc.RelationManager.prototype.del = function(relationName, boxName) {
  var key = "Name='"+relationName+"'";
  if (boxName !== undefined && boxName !== null && boxName !== "__") {
    key += ",_Box.Name='"+boxName+"'";
  }
  var response = this.internalDelMultiKey(key, "*");
  return response;
};

