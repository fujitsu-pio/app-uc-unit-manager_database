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
 * It creates a new object dcc.SentMessageManager.
 * @class This class performs CRUD for SEnt Messages.
 * @constructor
 * @augments dcc.ODataManager
 * @param {dcc.Accessor} Accessor
 */
dcc.SentMessageManager = function(as) {
  this.initializeProperties(this, as);
};
dcc.DcClass.extend(dcc.SentMessageManager, dcc.ODataManager);

///**
//* プロパティを初期化する.
//* @param {dcc.SentMessageManager} self
//* @param {dcc.Accessor} as アクセス主体
//*/
/**
 * This method initializes the properties of this class.
 * @param {dcc.SentMessageManager} self
 * @param {dcc.Accessor} as Accessor
 */
dcc.SentMessageManager.prototype.initializeProperties = function(self, as) {
  this.uber = dcc.ODataManager.prototype;
  this.uber.initializeProperties(self, as);
};

///**
//* URLを取得する.
//* @returns {String} URL
//*/
/**
 * This method returns the URL.
 * @returns {String} URL
 */
dcc.SentMessageManager.prototype.getUrl = function() {
  var sb = "";
  sb += this.getBaseUrl();
  sb += this.accessor.getCurrentCell().getName();
  sb += "/__ctl/SentMessage";
  return sb;
};

///**
//* 送信メッセージを取得.
//* @param {String} messageId メッセージID
//* @return {dcc.Message} 取得したメッセージオブジェクト
//* @throws {DaoException} DAO例外
//*/
/**
 * This method gets the outgoing messages.
 * @param {String} messageId MessageID
 * @return {dcc.Message} Message object 
 * @throws {dcc.DaoException} DAO exception
 */
dcc.SentMessageManager.prototype.retrieve = function(messageId) {
  var json = this.internalRetrieve(messageId);
  return new dcc.Message(this.accessor, json);
};

/**
 * This method delete message on the basis of messageID
 * @param {String} messageId
 * @param {String} etag
 * @returns {dcc.Promise} response
 */
dcc.SentMessageManager.prototype.del = function(messageId, etag) {
	var key = "'" + messageId + "'";
	var response = this.internalDelMultiKey(key, etag);
	return response;
};
