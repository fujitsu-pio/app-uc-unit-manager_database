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

/**
 * It creates a new object dcc.ExtRoleManager.
 * @class This class performs CRUD operations for External Role.
 * @constructor
 * @augments dcc.ODataManager
 * @param {dcc.Accessor} Accessor
 */
dcc.ExtRoleManager = function(as) {
  this.initializeProperties(this, as);
};
dcc.DcClass.extend(dcc.ExtRoleManager, dcc.ODataManager);

/**
 * This method initializes the properties of this class.
 * @param {dcc.ExtRoleManager} self
 * @param {dcc.Accessor} as
 */
dcc.ExtRoleManager.prototype.initializeProperties = function(self, as) {
  this.uber = dcc.ODataManager.prototype;
  this.uber.initializeProperties(self, as);
};


/**
 * The purpose of this function is to make request URL for
 * creating External Role.
 * @return {String} URL
 */
dcc.ExtRoleManager.prototype.getUrl = function() {
  var sb = this.getBaseUrl();
  sb += this.accessor.cellName;
  sb += "/__ctl/ExtRole";
  return sb;
};

/**
 * The purpose of this function is to create External Role.
 * @param {String} extCellName
 * @param {String} extRoleName
 * @return {dcc.DcHttpClient} response
 * @throws {dcc.DaoException} Exception thrown
 */
dcc.ExtRoleManager.prototype.create = function(obj) {
  var body = {};
  body.ExtRole = obj.ExtRoleURL;
  body["_Relation.Name"] = obj.RelationName;
  body["_Relation._Box.Name"] = obj.RelationBoxName;
  var requestBody = JSON.stringify(body);
  var json = this.internalCreate(requestBody);
  if (json.getStatusCode() >= 400) {
	var response = json.bodyAsJson();// throw exception with code
	throw new dcc.DaoException(response.message.value, response.code);
  }
  return json;
};


/**
 * The purpose of this function is to retrieve External Role.
 * @param {String} id
 * @return {Object} JSON response
 */
dcc.ExtRoleManager.prototype.retrieve = function(id) {
  var json = this.internalRetrieve(id);
  return json;
};

/**
* The purpose of this function is to delete external role on the basis of key.
* @param {String} key
* @param {String} etag
* @returns {dcc.Promise} response
*/
dcc.ExtRoleManager.prototype.del = function(key,etag) {
	var response = this.internalDelMultiKey(key, etag);
	return response;
};

/**
 * The purpose of this function is to return etag value of
 * particular external role
 * @param {String} key
 * @returns {String} etag
 */
dcc.ExtRoleManager.prototype.getEtag = function(key) {
	var json = this.internalRetrieveMultikey(key);
	return json.__metadata.etag;
};
