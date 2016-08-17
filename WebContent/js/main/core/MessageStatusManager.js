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
//* @class メッセージの送受信のためのクラス.
//* @constructor
//* @augments dcc.ODataManager
//*/
/**
 * It creates a new object dcc.MessageStatusManager.
 * @class This class is used for sending and receiving messages.
 * @constructor
 * @augments dcc.ODataManager
 * @param {dcc.Accessor} Accessor
 * @param {String} messageId
 */
dcc.MessageStatusManager = function(as, messageId) {
  this.initializeProperties(this, as, messageId);
};
dcc.DcClass.extend(dcc.MessageStatusManager, dcc.ODataManager);

///**
//* プロパティを初期化する.
//* @param {dcc.MessageStatusManager} self
//* @param {dcc.Accessor} as アクセス主体
//* @param {string} messageId メッセージID
//*/
/**
 * This method initializes the properties of this class.
 * @param {dcc.MessageStatusManager} self
 * @param {dcc.Accessor} as Accessor
 * @param {string} messageId messageID
 */
dcc.MessageStatusManager.prototype.initializeProperties = function(self, as, messageId) {
  this.uber = dcc.ODataManager.prototype;
  this.uber.initializeProperties(self, as);

  self.messageId = messageId;
};

///**
//* URLを取得する.
//* @returns {String} URL
//*/
/**
 * This method returns the URL for receiving messages.
 * @returns {String} URL
 */
dcc.MessageStatusManager.prototype.getUrl = function() {
  var sb = "";
  sb += this.getBaseUrl();
  sb += this.accessor.getCurrentCell().getName();
  sb += "/__message/received/";
  sb += this.messageId;
  return sb;
};

///**
//* メッセージを既読にする.
//* @return {dcc.Message} 取得したメッセージオブジェクト
//* @throws {DaoException} DAO例外
//*/
/**
 * This method is used to read a message.
 * @return {dcc.Message} Message object obtained
 * @throws {dcc.DaoException} DAO exception
 */
dcc.MessageStatusManager.prototype.changeMailStatusForRead = function() {
  var requestBody = {"Command" : "read"};
  var json = this.internalCreate(JSON.stringify(requestBody));
  return new dcc.Message(this.accessor, json);
};

///**
//* メッセージを未読にする.
//* @return {dcc.Message} 取得したメッセージオブジェクト
//* @throws {DaoException} DAO例外
//*/
/**
 * This method is used to unread a message.
 * @return {dcc.Message} Message object obtained
 * @throws {dcc.DaoException} DAO exception
 */
dcc.MessageStatusManager.prototype.changeMailStatusForUnRead = function() {
  var requestBody = {"Command" : "unread"};
  var json = this.internalCreate(JSON.stringify(requestBody));
  return new dcc.Message(this.accessor, json);
};

///**
//* メッセージを承認する.
//* @return {dcc.Message} 取得したメッセージオブジェクト
//* @throws {DaoException} DAO例外
//*/
/**
 * This method is used to approve a message.
 * @return {dcc.Message} Message object obtained
 * @throws {dcc.DaoException} DAO exception
 */
dcc.MessageStatusManager.prototype.approveConnect = function() {
  var requestBody = {"Command" : "approved"};
  var json = this.internalCreate(JSON.stringify(requestBody));
  return new dcc.Message(this.accessor, json);
};

///**
//* メッセージを拒否する.
//* @return {dcc.Message} 取得したメッセージオブジェクト
//* @throws {DaoException} DAO例外
//*/
/**
 * This method is used to reject a message.
 * @return {dcc.Message} Message object obtained
 * @throws {dcc.DaoException} DAO exception
 */
dcc.MessageStatusManager.prototype.rejectConnect = function() {
  var requestBody = {"Command" : "rejected"};
  var json = this.internalCreate(JSON.stringify(requestBody));
  return new dcc.Message(this.accessor, json);
};
