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
//* @class EntityTypeのCRUDのためのクラス.
//* @constructor
//* @augments dcc.ODataManager
//*/
/**
 * It creates a new object dcc.EntityTypeManager.
 * @class This class performs The CRUD operations for EntityType.
 * @constructor
 * @augments dcc.ODataManager
 * @param {dcc.Accessor} Accessor
 * @param {dcc.DcCollection} collection
 */
dcc.EntityTypeManager = function(as, collection) {
  this.initializeProperties(this, as, collection);
};
dcc.DcClass.extend(dcc.EntityTypeManager, dcc.ODataManager);

///**
//* プロパティを初期化する.
//* @param {dcc.AbstractODataContext} self
//* @param {dcc.Accessor} as アクセス主体
//* @param {?} collection
//*/
/**
 * This method initializes the properties of this class.
 * @param {dcc.AbstractODataContext} self
 * @param {dcc.Accessor} as Accessor
 * @param {dcc.DcCollection} collection
 */
dcc.EntityTypeManager.prototype.initializeProperties = function(self, as, collection) {
  this.uber = dcc.ODataManager.prototype;
  this.uber.initializeProperties(self, as, collection);
};

///**
//* EntityTypeのURLを取得する.
//* @returns {String} URL
//*/
/**
 * This method generates the URL for performing EntityType operations.
 * @returns {String} URL
 */
dcc.EntityTypeManager.prototype.getUrl = function() {
  var sb = "";
  sb += this.collection.getPath();
  sb += "/$metadata/EntityType";
  return sb;
};

///**
//* EntityTypeを作成.
//* @param {dcc.EntityType} obj EntityTypeオブジェクト
//* @return {dcc.EntityType} EntityTypeオブジェクト
//* @throws {DaoException} DAO例外
//*/
/**
 * This method is used for performing create operation on EntityType.
 * @param {dcc.EntityType} obj EntityType object
 * @return {dcc.EntityType} EntityType object
 * @throws {dcc.DaoException} DAO exception
 */
dcc.EntityTypeManager.prototype.create = function(obj) {
  var json = null;
  var responseJson = null;
  if (obj.getClassName !== undefined && obj.getClassName() === "EntityType") {
    var body = {};
    body.Name = obj.getName();
    json = this.internalCreate(JSON.stringify(body));
    obj.initializeProperties(obj, this.accessor, json);
    return obj;
  } else {
    var requestBody = JSON.stringify(obj);
    json = this.internalCreate(requestBody);
    if (json.getStatusCode() >= 400) {
      var response = json.bodyAsJson();
      throw new dcc.DaoException(response.message.value, response.code);
    }
    responseJson = json.bodyAsJson().d.results;
    return new dcc.EntityType(this.accessor, responseJson);
  }
};

///**
//* EntityTypeを取得.
//* @param {String} name 取得対象のbox名
//* @return {dcc.EntityType} 取得したしたEntityTypeオブジェクト
//* @throws {DaoException} DAO例外
//*/
/**
 * This method is used for retrieve operation for EntityType.
 * @param {String} name EntityType name
 * @return {dcc.EntityType} EntityType object
 * @throws {dcc.DaoException} DAO exception
 */
dcc.EntityTypeManager.prototype.retrieve = function(name) {
  var json = this.internalRetrieve(name);
  return new dcc.EntityType(this.accessor, json);
};

/**
 * The purpose of this method is to update entity type.
 * @param {String} name entityName
 * @param {String} name body
 * @param {String} name etag
 * @return {Object} response
 */
dcc.EntityTypeManager.prototype.update = function(entityName, body, etag) {
  var response = null;
  response = this.internalUpdate(entityName, body, etag);
  return response;
};

/**
 * The purpose of this method is to return etag for
 * particular entity type.
 * @param {String} name entityName
 * @return {String} etag
 */
dcc.EntityTypeManager.prototype.getEtag = function(entityName) {
  var json = this.internalRetrieve(entityName);
  return json.__metadata.etag;
};

/**
 * The purpose of this method is to delete entity type.
 * @param {String} name entityTypeName
 * @param {String} etag
 * @return {Object} response
 */
dcc.EntityTypeManager.prototype.del = function(entityTypeName, etag) {
  var key = "Name='" + entityTypeName + "'";
  var response = this.internalDelMultiKey(key, etag);
  return response;
};