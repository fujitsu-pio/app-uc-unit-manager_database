/*
 * Copyright 2012-2013 Fujitsu Limited all rights reserved.
 */
/*global dcc:false */

///**
//* @class ACLのCRUDを行うためのクラス.
//* @constructor
//*/
/**
 * It creates a new object dcc.AclManager.
 * @class This class performs the CRUD operations for ACL.
 * @constructor
 * @param {dcc.Accessor} Accessor
 * @param {dcc.DavCollection} dav
 */
dcc.AclManager = function(as, dav) {
  this.initializeProperties(this, as, dav);
};

///**
//* プロパティを初期化する.
//* @param {dcc.AclManager} self
//* @param {dcc.Accessor} as アクセス主体
//* @param {?} dav
//*/
/**
 * This method initializes the properties of this class.
 * @param {dcc.AclManager} self
 * @param {dcc.Accessor} as Accessor
 * @param {dcc.DcCollection} dav
 */
dcc.AclManager.prototype.initializeProperties = function(self, as, dav) {
  self.accessor = as;
///** DAVコレクション. */
  /** DAV Collection */
  self.collection = dav;
};


///**
//* ACLを登録する.
//* @param {Object} body リクエストボディ(XML形式)
//* @throws {DaoException} DAO例外
//*/
/**
 * This method registers the ACL.
 * @param {Object} body Request body (XML format)
 * @throws {dcc.DaoException} DAO exception
 */
dcc.AclManager.prototype.set = function(body) {
  var restAdapter = dcc.RestAdapterFactory.create(this.accessor);
  restAdapter.acl(this.getUrl(), body);
};

///**
//* ACLオブジェクトとしてACLをセットする.
//* @param {dcc.Acl} obj Aclオブジェクト
//* @throws {DaoException} DAO例外
//*/
/**
 * This method sets the ACL object.
 * @param {dcc.Acl} obj Acl object
 * @throws {dcc.DaoException} DAO exception
 */
dcc.AclManager.prototype.setAsAcl = function(obj) {
  var restAdapter = dcc.RestAdapterFactory.create(this.accessor);
  var response = restAdapter.acl(this.getUrl(), obj.toXmlString());
  return response;
};


///**
//* ACL情報をAclオブジェクトとして取得.
//* @return {dcc.Acl} Aclオブジェクト
//* @throws {DaoException} DAO例外
//*/
/**
 * This method gets ACL information.
 * @return {dcc.Acl} Acl object
 * @throws {dcc.DaoException} DAO exception
 */
dcc.AclManager.prototype.get = function() {
  var restAdapter = dcc.RestAdapterFactory.create(this.accessor);
  restAdapter.propfind(this.getUrl());
  return dcc.Acl.parse(restAdapter.bodyAsString());
};

///**
//* URLを生成.
//* @return {?} 現在のコレクションへのURL
//*/
/**
 * This method returns the URL.
 * @return {String} URL of current collection
 */
dcc.AclManager.prototype.getUrl = function() {
  return this.collection.getPath();
};

