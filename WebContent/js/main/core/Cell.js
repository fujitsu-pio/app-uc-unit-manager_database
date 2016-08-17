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
//* @class Cellへアクセスするためのクラス.
//* @constructor
//* @augments dcc.AbstractODataContext
//*/
/**
 * It creates a new object dcc.Cell.
 * @class This class represents Cell object to perform cell related operations.
 * @constructor
 * @augments dcc.AbstractODataContext
 * @param {dcc.Accessor} Accessor
 * @param {String} key
 */
dcc.Cell = function(as, key) {
  this.initializeProperties(this, as, key);
};
dcc.DcClass.extend(dcc.Cell, dcc.AbstractODataContext);

///**
//* プロパティを初期化する.
//* @param {dcc.Cell} self
//* @param {dcc.Accessor} as アクセス主体
//* @param {Object} body
//*/
/**
 * This method initializes the properties of this class.
 * @param {dcc.Cell} self
 * @param {dcc.Accessor} as Accessor
 * @param {Object} body
 */
dcc.Cell.prototype.initializeProperties = function(self, as, body) {
  this.uber = dcc.AbstractODataContext.prototype;
  this.uber.initializeProperties(self, as);

///** キャメル方で表現したクラス名. */
  /** Class name for Cell. */
  self.CLASSNAME = "Cell";

///** Cell名 (string). */
  /** Cell name (string). */
  self.name = "";
  if (typeof body === "string") {
    self.name = body;
  }
  if (typeof body === "undefined" && self.accessor !== undefined) {
    self.name = self.accessor.getCellName();
  }

  /** location. */
  self.location = null;

///** CellレベルACLへアクセスするためのクラス. */
  /** To access cell level ACL. */
  self.acl = null;
///** メンバーへアクセスするためのクラスインスタンス。cell().accountでアクセス. */
  /** Class instance to access Account. */
  self.account = null;
///** BoxのCRUDを行うマネージャクラス. */
  /** Manager class to perform CRUD of Box. */
  self.box = null;
///** メッセージ送受信を行うマネージャクラス. */
  /** Manager classes for sending and receiving messages. */
  self.message = null;
///** Relation へアクセスするためのクラス. */
  /** Class to access the Relation. */
  self.relation = null;
///** Role へアクセスするためのクラス. */
  /** Class to access the Role. */
  self.role = null;
///** ExtRole へアクセスするためのクラス. */
  /** Class to access the External Role. */
  self.extRole = null;
///** ExtCell へアクセスするためのクラス. */
  /** Class to access the External Cell. */
  self.extCell = null;
///** Event へアクセスするためのクラス. */
  /** Class to access the Event. */
  self.event = null;

//if (this.json !== null) {
//this.rawData = this.json;
//this.name = this.json.Name;
//this.location = this.json.__metadata.uri;
//}

  if (self.accessor !== undefined) {
    self.accessor.setCurrentCell(this);
    self.relation = new dcc.RelationManager(self.accessor);
    self.role = new dcc.RoleManager(self.accessor);
    self.message = new dcc.MessageManager(self.accessor);
//  this.acl = new AclManager(this.accessor);
    self.account = new dcc.AccountManager(self.accessor);
    self.box = new dcc.BoxManager(self.accessor);
//  this.extRole = new ExtRoleManager(this.accessor);
    self.extCell = new dcc.ExtCellManager(self.accessor);
    self.event = new dcc.EventManager(self.accessor);
  }
};

///**
//* Cell名を取得.
//* @return {String} Cell名
//*/
/**
 * This method gets the Cell name.
 * @return {String} Cell name
 */
dcc.Cell.prototype.getName = function() {
  return this.name;
};

///**
//* Cell名を設定.
//* @param {String} value Cell名
//*/
/**
 * This method sets the Cell name.
 * @param {String} value Cell name
 */
dcc.Cell.prototype.setName = function(value) {
  if (typeof value !== "string") {
    throw new dcc.DaoException("InvalidParameter");
  }
  this.name = value;
};


///**
//* CellのURLを取得する.
//* @return {String} 取得した CellのURL
//*/
/**
 * This method gets the URL for performing cell related operations.
 * @return {String} URL of the cell
 */
dcc.Cell.prototype.getUrl = function() {
  return this.accessor.getBaseUrl() + encodeURI(this.name) + "/";
};

///**
//* アクセストークンを取得.
//* @return {?} アクセストークン
//* @throws {DaoException} DAO例外
//*/
/**
 * This method gets the access token.
 * @return {String} Access Token
 * @throws {dcc.DaoException} DAO exception
 */
dcc.Cell.prototype.getAccessToken = function() {
  if (this.accessor.getAccessToken() !== null) {
    return this.accessor.getAccessToken();
  } else {
    throw new dcc.DaoException.create("Unauthorized");
  }
};

///**
//* アクセストークンの有効期限を取得.
//* @return {?} アクセストークンの有効期限
//*/
/**
 * This method gets the expiration date of the access token.
 * @return {String} expiration date of the access token
 */
dcc.Cell.prototype.getExpiresIn = function() {
  return this.accessor.getExpiresIn();
};

///**
//* アクセストークンのタイプを取得.
//* @return {?} アクセストークンのタイプ
//*/
/**
 * This method gets the access token type.
 * @return {String} access token type
 */
dcc.Cell.prototype.getTokenType = function() {
  return this.accessor.getTokenType();
};

///**
//* リフレッシュトークンを取得.
//* @return {?} リフレッシュトークン
//* @throws DaoException DAO例外
//*/
/**
 * This method gets the refresh token.
 * @return {String} Refreash token
 * @throws {dcc.DaoException} DAO exception
 */
dcc.Cell.prototype.getRefreshToken = function() {
  if (this.accessor.getRefreshToken() !== null) {
    return this.accessor.getRefreshToken();
  } else {
    throw new dcc.DaoException("Unauthorized");
  }
};

///**
//* リフレッシュの有効期限を取得.
//* @return {?} リフレッシュトークンの有効期限
//*/
/**
 * This method gets the expiration date of the refresh token.
 * @return {String} expiration date of the refresh token
 */
dcc.Cell.prototype.getRefreshExpiresIn = function() {
  return this.accessor.getRefreshExpiresIn();
};

/**
 * This method returns the location.
 * @return {String} location
 */
dcc.Cell.prototype.getLocation = function() {
  return this.location;
};

///**
//* CellのownerRepresentativeAccountsを設定.
//* @param user アカウント名
//* @throws DaoException DAO例外
//*/
//dcc.Cell.prototype.setOwnerRepresentativeAccounts = function(user) {
//var value = "<dc:account>" + user + "</dc:account>";
//RestAdapter rest = (RestAdapter) RestAdapterFactory.create(this.accessor);
//rest.proppatch(this.getUrl(), "dc:ownerRepresentativeAccounts", value);
//};

///**
//* CellのownerRepresentativeAccountsを設定(複数アカウント登録).
//* @param accountName アカウント名の配列
//* @throws DaoException DAO例外
//*/
//public void setOwnerRepresentativeAccounts(String[] accountName) throws DaoException {
//dcc.Cell.prototype.setOwnerRepresentativeAccounts = function(accountName) {
//StringBuilder sb = new StringBuilder();
//for (Object an : accountName) {
//sb.append("<dc:account>");
//sb.append(an);
//sb.append("</dc:account>");
//}
//RestAdapter rest = (RestAdapter) RestAdapterFactory.create(this.accessor);
//rest.proppatch(this.getUrl(), "dc:ownerRepresentativeAccounts", sb.toString());
//};

///**
//* Boxへアクセスするためのクラスを生成.
//* @param {?} boxName Box Name
//* @param {?} schemaValue スキーマ名
//* @return {dcc.Box} 生成したBoxインスタンス
//* @throws {DaoException} DAO例外
//*/
/**
 * This method generates classes to access the Box.
 * @param {String} boxName Box Name
 * @param {String} schemaValue Schema value
 * @return {dcc.Box} Box object
 * @throws {dcc.DaoException} DAO exception
 */
dcc.Cell.prototype.boxCtl = function(boxName, schemaValue) {
  this.accessor.setBoxName(boxName);
  var url = dcc.UrlUtils.append(this.accessor.getCurrentCell().getUrl(), this.accessor.getBoxName());
  return new dcc.Box(this.accessor, {"Name":boxName, "Schema":schemaValue}, url);
};

///**
//* BaseUrl を取得.
//* @return {String} baseUrl 基底URL文字列
//*/
/**
 * This method gets the Base URL.
 * @return {String} baseUrl Base URL
 */
dcc.Cell.prototype.getBaseUrlString = function() {
  return this.accessor.getBaseUrl();
};

///**
//* ODataのキーを取得する.
//* @return {String} ODataのキー情報
//*/
/**
 * This method gets the key of OData.
 * @return {String} OData key
 */
dcc.Cell.prototype.getKey = function() {
  return "('" + this.name + "')";
};

///**
//* クラス名をキャメル型で取得する.
//* @return {?} ODataのキー情報
//*/
/**
 * This method gets the class name.
 * @return {String} OData class name
 */
dcc.Cell.prototype.getClassName = function() {
  return this.CLASSNAME;
};

/**
 * Get the cookie peer key.
 * @returns {String} Cookie Peer key
 */
dcc.Cell.prototype.getCookiePeer = function(){
  return this.accessor.getCookiePeer();
};