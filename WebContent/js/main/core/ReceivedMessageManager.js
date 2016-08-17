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
 * It creates a new object dcc.ReceivedMessageManager.
 * @class This class is used for sending and receiving messages.
 * @constructor
 * @augments dcc.ODataManager
 * @param {dcc.Accessor} Accessor
 * @param {String} message
 */
dcc.ReceivedMessageManager = function(as, message) {
  this.initializeProperties(this, as, message);
};
dcc.DcClass.extend(dcc.ReceivedMessageManager, dcc.ODataManager);

///**
//* クラス名をキャメル型で取得する.
//* @return {?} ODataのキー情報
//*/
/**
 * This method gets the class name.
 * @return {String} OData Class name
 */
dcc.ReceivedMessageManager.prototype.getClassName = function() {
  return this.CLASSNAME;
};

///**
//* ReceivedMessageManagerオブジェクトのキーを取得する.
//* @return {String} ODataのキー情報
//*/
/**
 * This method returns the key.
 * @return {String} OData Key
 */
dcc.ReceivedMessageManager.prototype.getKey = function() {
  return "('" + this.message.messageId + "')";
};


///**
//* プロパティを初期化する.
//* @param {dcc.ReceivedMessageManager} self
//* @param {dcc.Accessor} as アクセス主体
//* @param {dcc.Message} メッセージオブジェクト
//*/
/**
 * This method initializes the properties of this class.
 * @param {dcc.ReceivedMessageManager} self
 * @param {dcc.Accessor} as Accessor
 * @param {dcc.Message} Message object
 */
dcc.ReceivedMessageManager.prototype.initializeProperties = function(self, as, message) {

///** クラス名. */
  /** Class name in camel case. */
  self.CLASSNAME = "ReceivedMessage";


  this.uber = dcc.ODataManager.prototype;
  this.uber.initializeProperties(self, as);
  this.message = message;
};

///**
//* URLを取得する.
//* @returns {String} URL
//*/
/**
 * This method returns the URL.
 * @returns {String} URL
 */
dcc.ReceivedMessageManager.prototype.getUrl = function() {
  var sb = "";
  sb += this.getBaseUrl();
  sb += this.accessor.getCurrentCell().getName();
  sb += "/__ctl/ReceivedMessage";
  return sb;
};

///**
//* 受信メッセージを取得.
//* @param {String} messageId メッセージID
//* @return {dcc.Message} 取得したメッセージオブジェクト
//* @throws {DaoException} DAO例外
//*/
/**
 * This method gets the received message.
 * @param {String} messageId MessageID
 * @return {dcc.Message} Message object
 * @throws {dcc.DaoException} DAO exception
 */
dcc.ReceivedMessageManager.prototype.retrieve = function(messageId) {
  var json = this.internalRetrieve(messageId);
  return new dcc.Message(this.accessor, json);
};

///**
//* ReceivedMessageManager Accountに紐づく受信メッセージ一覧または受信メッセージに紐付くAccount一覧.
//* @param {dcc.Account} account メッセージを取得するAccount
//* accountがundefinedの場合は受信メッセージに紐付くAccount一覧を取得
//* @return {dcc.ODataResponse} 一覧取得のレスポンス
//* @throws {DaoException} DAO例外
//*/
/**
 * Account list associated with their incoming messages or incoming message
 * list brute string to ReceivedMessageManager Account.
 * @param {dcc.Account} account Message Account
 * Get the Account list tied with the received message if account is undefined
 * @return {dcc.ODataResponse} Response List
 * @throws {dcc.DaoException} DAO exception
 */
dcc.ReceivedMessageManager.prototype.listOfReadStatus = function(account) {
  var linkManager;
  if(account === undefined){
    linkManager = new dcc.LinkManager(this.accessor, this, "AccountRead");
  }else{
    linkManager = new dcc.LinkManager(account.accessor, account, "ReceivedMessageRead");
  }

  // $linksのinlinecountは取得できない(coreで対応していないため)
  var res = linkManager.query().inlinecount("allpages").runAsResponse();
  return res;
};

///**
//* changeMailStatusForRead Account毎の既読.
//* @param {dcc.Account} account 既読にするAccount
//* @throws {DaoException} DAO例外
//*/
/**
 * This method reads each of changeMailStatusForRead Account.
 * @param {dcc.Account} account Account object
 * @throws {dcc.DaoException} DAO exception
 */
dcc.ReceivedMessageManager.prototype.changeMailStatusForRead = function(account) {
  var linkManager = new dcc.LinkManager(this.accessor, this, "AccountRead");
  linkManager.link(account);
};

///**
//* changeMailStatusForUnRead Account毎の未読.
//* @param {dcc.Account} account 既読にするAccount
//* @throws {DaoException} DAO例外
//*/
/**
 * This method unreads each of changeMailStatusForRead Account.
 * @param {dcc.Account} account Account object
 * @throws {dcc.DaoException} DAO exception
 */
dcc.ReceivedMessageManager.prototype.changeMailStatusForUnRead = function(account) {
  var linkManager = new dcc.LinkManager(this.accessor, this, "AccountRead");
  linkManager.unlink(account);
};

/**
 * This method delete message on the basis of messageID.
 * @param {String} MessageID
 * @param {String} etag
 * @returns {dcc.Promise} response
 */
dcc.ReceivedMessageManager.prototype.del = function(messageId, etag) {
	var key = "'" + messageId + "'";
	var response = this.internalDelMultiKey(key, etag);
	return response;
};
