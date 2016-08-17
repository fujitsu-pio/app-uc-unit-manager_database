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
 * It creates a new object dcc.ComplexTypePropertyManager.
 * @class This class is used for performing CRUD operations on ComplexTypeProperty.
 * @constructor
 * @augments jEntitySet
 * @param {dcc.Accessor} Accessor
 * @param {dcc.DcCollection} Collection
 */
dcc.ComplexTypePropertyManager = function(as, collection) {
  this.initializeProperties(this, as, collection);
};
dcc.DcClass.extend(dcc.ComplexTypePropertyManager, dcc.ODataManager);

/** The purpose of this function is to initialize properties.
 * @param {Object} self
 * @param {dcc.Accessor} as
 * @param {dcc.DcCollection} collection
 */
dcc.ComplexTypePropertyManager.prototype.initializeProperties = function(self, as,
    collection) {
  this.uber = dcc.ODataManager.prototype;
  this.uber.initializeProperties(self, as, collection);
};

/**
 * The purpose of this function is to create URL.
 * @return {String} URL
 */
dcc.ComplexTypePropertyManager.prototype.getUrl = function() {
  var sb = "";
  sb = this.getBaseUrl();
  sb += this.accessor.cellName;
  sb +="/";
  sb += this.accessor.boxName;
  sb +="/";
  sb += this.collection;
  sb += "/$metadata/ComplexTypeProperty";
  return sb;
};

/**
 * The purpose of this function is to get complex type URI.
 * @param {String} complexTypeName
 * @return {String} URL
 */
dcc.ComplexTypePropertyManager.prototype.getComplexTypeUri = function (complexTypeName) {
  var sb = "";
  sb = this.getBaseUrl();
  sb += this.accessor.cellName;
  sb +="/";
  sb += this.accessor.boxName;
  sb +="/";
  sb += this.collection;
  sb += "/$metadata/ComplexType(";
  sb += "'"+complexTypeName+"'";
  //sb += escape("'"+complextypeName+"'");
  sb += ")/_ComplexTypeProperty";
  return sb;
};

/**
 * The purpose of this function is to create ComplexTypeProperty.
 * @param {Object} obj
 * @return {Object} JSON
 */
dcc.ComplexTypePropertyManager.prototype.create = function (obj) {
  var json = null;
  json = this.internalCreate(obj);
  return json;
};

/**
 * The purpose of this function is to retrieve ComplexTypeProperty.
 * @param {String} complextypeName
 * @param {String} complexTypePropertyName
 * @return {Object} JSON
 */
dcc.ComplexTypePropertyManager.prototype.retrieve = function (complextypeName, complexTypePropertyName) {
  var json = null;
  var key = null;
  key = "Name='"+complexTypePropertyName+"',_ComplexType.Name='"+complextypeName+"'";
  if (complexTypePropertyName !== undefined && complextypeName !== undefined) {
    json = this.internalRetrieveMultikey(key);
  }
  return json;
};

/**
 * The purpose of this function is to retrieve ComplexTypePropertyList.
 * @param {String} complextypeName
 * @return {Object} JSON
 */
dcc.ComplexTypePropertyManager.prototype.retrieveComplexTypePropertyList = function (complextypeName) {
  if(complextypeName !== null || complextypeName !== undefined) {
    var uri = this.getComplexTypeUri(complextypeName);
    var restAdapter = dcc.RestAdapterFactory.create(this.accessor);
    var response = restAdapter.get(uri, "application/json");
    var json = response.bodyAsJson().d.results;
    return json;
  }
};

/**
 * The purpose of this function is to delete ComplexTypeProperty
 * @param {String} key
 * @param {String} etag
 * @returns {dcc.Promise} response
 */
dcc.ComplexTypePropertyManager.prototype.del = function(key, etag) {
  if (typeof etag === "undefined") {
    etag = "*";
  }
  var response = this.internalDelMultiKey(key, etag);
  return response;
};