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
//* @class AssociationEndのCRUDのためのクラス.
//* @constructor
//* @augments dcc.ODataManager
//*/
/**
 * It creates a new object dcc.AssociationEndManager.
 * @class This class performs the CRUD operations for Association End.
 * @constructor
 * @augments dcc.ODataManager
 * @param {dcc.Accessor} Accessor
 * @param {dcc.DcCollection} col
 */
dcc.AssociationEndManager = function(as, col) {
  this.initializeProperties(this, as, col);
};
dcc.DcClass.extend(dcc.AssociationEndManager, dcc.ODataManager);

///**
//* プロパティを初期化する.
//* @param {dcc.AssociationEndManager} self
//* @param {dcc.Accessor} as アクセス主体
//* @param {?} col ?
//*/
/**
 * This method initializes the properties of this class.
 * @param {dcc.AssociationEndManager} self
 * @param {dcc.Accessor} as accessor
 * @param {dcc.DavCollection} col 
 */
dcc.AssociationEndManager.prototype.initializeProperties = function(self, as, col) {
  this.uber = dcc.ODataManager.prototype;
  this.uber.initializeProperties(self, as, col);
};

///**
//* AssociationEndのURLを取得する.
//* @returns {String} URL
//*/
/**
 * This method returns the URL.
 * @returns {String} URL
 */
dcc.AssociationEndManager.prototype.getUrl = function() {
  var sb = "";
  sb += this.collection.getPath();
  sb += "/$metadata/AssociationEnd";
  return sb;
};

///**
//* AssociationEndを作成.
//* @param {dcc.AssociationEnd} obj AssociationEndオブジェクト
//* @return {dcc.AssociationEnd} AssociationEndオブジェクト
//* @throws {DaoException} DAO例外
//*/
/**
 * This method creates an AssociationEnd.
 * @param {dcc.AssociationEnd} obj AssociationEnd object
 * @return {dcc.AssociationEnd} AssociationEnd object
 * @throws {dcc.DaoException} DAO exception
 */
dcc.AssociationEndManager.prototype.create = function(obj) {
  var json = null;
  var responseJson = null;
  if (obj.getClassName !== undefined && obj.getClassName() === "AssociationEnd") {
    var body = {};
    body.Name = obj.getName();
    body["_EntityType.Name"] = obj.getEntityTypeName();
    body.Multiplicity = obj.getMultiplicity();
    json = this.internalCreate(JSON.stringify(body));
    obj.initializeProperties(obj, this.accessor, json);
    return obj;
  } else {
    if (!("Name" in obj)) {
      throw new dcc.DaoException("Name is required.", "PR400-OD-0009");
    }
    if (!("Multiplicity" in obj)) {
      throw new dcc.DaoException("Multiplicity is required.",
      "PR400-OD-0009");
    }
    if (!("_EntityType.Name" in obj)) {
      throw new dcc.DaoException("_EntityType.Name is required.",
      "PR400-OD-0009");
    }
    var requestBody = JSON.stringify(obj);
    json = this.internalCreate(requestBody);
    if (json.getStatusCode() >= 400) {
      var response = json.bodyAsJson();
      throw new dcc.DaoException(response.message.value, response.code);
    }
    responseJson = json.bodyAsJson().d.results;
    return new dcc.AssociationEnd(this.accessor, responseJson);
  }
};

///**
//* AssociationEndを取得.
//* @param {String} name 取得対象のAssociation名
//* @param {String} entityTypeName EntityType名
//* @return {dcc.AssociationEnd} 取得したしたAssociationEndオブジェクト
//* @throws {DaoException} DAO例外
//*/
/**
 * This method fetches the AssociationEnd.
 * @param {String} AssociationEnd name
 * @param {String} entityTypeName EntityType name
 * @return {dcc.AssociationEnd} AssociationEnd object
 * @throws {dcc.DaoException} DAO exception
 */
dcc.AssociationEndManager.prototype.retrieve = function(name, entityTypeName) {
  var key = "Name='" + name + "',_EntityType.Name='" + entityTypeName + "'";
  var json = this.internalRetrieveMultikey(key);
  return new dcc.AssociationEnd(this.accessor, json);
};

/**
 * To create url for assocend_navpro_list
 * @param {String} ascName
 * @param {String} entityTypeName
 * @returns {String} URL
 */
dcc.AssociationEndManager.prototype.getNavProListUrl = function(ascName,
    entityTypeName, associationEndView) {
  var sb = "";
  sb += this.collection.getPath();
  sb += "/$metadata/AssociationEnd";
  sb += "(Name='" + ascName + "',_EntityType.Name='" + entityTypeName + "')/";
  if (associationEndView === true) {
    sb += "$links/";
    associationEndView = false;
  }
  sb += "_AssociationEnd";
  return sb;
};

///**
//* AssociationEndを削除.
//* @param {String} name 取得対象のAssociation名
//* @param {String} entityTypeName EntityType名
//* @return {dcc.Promise} promise
//* @throws {DaoException} DAO例外
//*/
/**
 * This method deletes the AssociationEnd.
 * @param {String} AssociationEnd name
 * @param {String} entityTypeName EntityType name
 * @return {dcc.Promise} promise
 * @throws {dcc.DaoException} DAO exception
 */
dcc.AssociationEndManager.prototype.del = function(name, entityTypeName) {
  var key = "Name='" + name + "',_EntityType.Name='" + entityTypeName + "'";
  var response = this.internalDelMultiKey(key, "*");
  return response;
};

/**
 * To create assocend_navpro_list
 * @param {dcc.AssociationEnd} obj
 * @param {String} fromEntityTypeName
 * @param {String} fromAssEnd
 * @return {dcc.DcHttpClient} response
 */
dcc.AssociationEndManager.prototype.createNavProList = function(obj, fromEntityTypeName, fromAssEnd) {
  if (obj.getClassName !== undefined && obj.getClassName() === "AssociationEnd") {
    var body = {};
    body.Name = obj.getName();
    body.Multiplicity = obj.getMultiplicity();
    body["_EntityType.Name"] = obj.getEntityTypeName();
    var restAdapter = dcc.RestAdapterFactory.create(this.accessor);
    var url = this.getNavProListUrl(fromAssEnd, fromEntityTypeName);
    var response = restAdapter.post(url, JSON.stringify(body), "application/json");
    return response;
  }
};

/**
 * The purpose of this function is to create association URI 
 * for particular entityType.
 * @param {String} entityTypeName
 * @return {String} URL
 */
dcc.AssociationEndManager.prototype.getAssociationUri = function (entityTypeName) {
  var sb = "";
  sb += this.collection.getPath();
  sb += "/$metadata/EntityType(";
  sb += "'"+entityTypeName+"'";
  sb += ")/_AssociationEnd";
  return sb;
};

/**
 * The purpose of this function is to retrieve association
 * list against one entity type.
 * @param {String} entityTypeName 
 * @param {String} associationEndName
 * @return {Object} JSON
 */
dcc.AssociationEndManager.prototype.retrieveAssociationList = function (entityTypeName, associationEndName) {
  var uri = null;
  if(entityTypeName !== null && entityTypeName !== undefined) {
    uri = this.getAssociationUri(entityTypeName);
    if (associationEndName !== undefined && associationEndName !== null){
      uri = this.getNavProListUrl(associationEndName, entityTypeName, true);
    }
  }
  var restAdapter = dcc.RestAdapterFactory.create(this.accessor);
  var response = restAdapter.get(uri, "application/json");
  var json = response.bodyAsJson().d.results;
  return json;
};

/**
 * The purpose of this function is to delete association link
 * @param {String} fromAssociationName
 * @param {String} fromEntityTypeName
 * @param {String} toAssociationName
 * @param {String} toEntityTypeName
 * @return {dcc.Promise} promise
 */
dcc.AssociationEndManager.prototype.delAssociationLink = function(fromAssociationName, fromEntityTypeName, toAssociationName, toEntityTypeName) {
  var uri = this.getNavProListUrl(fromAssociationName, fromEntityTypeName, true);
  uri += "(Name='" + toAssociationName + "'";
  uri += ",_EntityType.Name='" + toEntityTypeName + "')";
  var restAdapter = dcc.RestAdapterFactory.create(this.accessor);
  var response = restAdapter.del(uri, "*","");
  return response;
};