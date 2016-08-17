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
//* @class Boxへアクセスするためのクラス.
//* @constructor
//* @augments dcc.DavCollection
//*/
/**
 * It creates a new object dcc.Box.
 * @class This class represents Box to access box related fields.
 * @constructor
 * @augments dcc.DavCollection
 * @param {dcc.Accessor} Accessor
 * @param {Object} json
 * @param {String} path 
 */
dcc.Box = function(as, json, path) {
  this.initializeProperties(this, as, json, path);
};
dcc.DcClass.extend(dcc.Box, dcc.DavCollection);

///**
//* プロパティを初期化する.
//* @param {dcc.Box} self
//* @param {dcc.Accessor} as アクセス主体
//* @param {Object} json JSONオブジェクト
//* @param {?} path
//*/
/**
 * This method initializes the properties of this class.
 * @param {dcc.Box} self
 * @param {dcc.Accessor} as Accessor
 * @param {Object} json JSON object
 * @param {String} path
 */
dcc.Box.prototype.initializeProperties = function(self, as, json, path) {
  this.uber = dcc.DavCollection.prototype;
  this.uber.initializeProperties(self, as, path);

  self.name = "";
  self.schema = "";
  self.acl = null;
  self.event = null;

  if (json !== undefined) {
    self.name = json.Name;
    self.schema = json.Schema;
  }
  if (as !== undefined) {
    self.acl = new dcc.AclManager(as, this);
    self.event = new dcc.EventManager(as);
  }
};

///**
//* Box名を取得.
//* @return {String} Box名
//*/
/**
 * This method gets the box name.
 * @return {String} Box name
 */
dcc.Box.prototype.getName = function() {
  return this.name;
};

///**
//* Boxを設定.
//* @param {String} value Box名
//*/
/**
 * This method sets the box name.
 * @param {String} value Box name
 */
dcc.Box.prototype.setName = function(value) {
  this.name = value;
};

///**
//* スキーマを取得.
//* @return {?} スキーマ
//*/
/**
 * This method gets the box schema.
 * @return {String} value Box schema
 */
dcc.Box.prototype.getSchema = function() {
  return this.schema;
};

///**
//* スキーマを設定.
//* @param {String} value スキーマ
//*/
/**
 * This method sets the box schema.
 * @param {String} value Box schema
 */
dcc.Box.prototype.setSchema = function(value) {
  this.schema = value;
};

///**
//* JSONオブジェクトを生成する.
//* @return {?} 生成したJSONオブジェクト
//*/
/**
 * This method generates the json for Box.
 * @return {Object} JSON object
 */
dcc.Box.prototype.toJSON = function() {
  var json = {};
  json.Name = this.name;
  json.Schema = this.schema;
  return json;
};

