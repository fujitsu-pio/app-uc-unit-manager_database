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
//* @class OData関連の各機能を生成/削除するためのクラスの抽象クラス.
//* @constructor
//*/
/**
 * It creates a new object dcc.ODataLinkManager.
 * @class This is the abstract class for generating / deleting the OData related functions.
 * @constructor
 * @param {dcc.Accessor} Accessor
 * @param {dcc.AbstractODataContext} cx
 * @param {String} classname
 */
dcc.ODataLinkManager = function(as, cx, className) {
  this.initializeProperties(this, as, cx, className);
};
dcc.DcClass.extend(dcc.ODataLinkManager, dcc.LinkManager);

///**
//* プロパティを初期化する.
//* @param {dcc.ODataLinkManager} self
//* @param {dcc.Accessor} as アクセス主体
//* @param {?} cx ターゲットオブジェクト
//*/
/**
 * This method initializes the properties of this class.
 * @param {dcc.ODataLinkManager} self
 * @param {dcc.Accessor} as Accessor
 * @param {Object} cx Target object
 */
dcc.ODataLinkManager.prototype.initializeProperties = function(self, as, cx, className) {
  this.uber = dcc.LinkManager.prototype;
  this.uber.initializeProperties(self, as, cx, className);
};

///**
//* リンクを削除.
//* @param {?} cx リンク削除するターゲットオブジェクト
//* @throws {DaoException} DAO例外
//*/
/**
 * This method is used to remove a link.
 * @param {Object} cx Target object for removing the link
 * @throws {dcc.DaoException} DAO exception
 */
dcc.ODataLinkManager.prototype.unlink = function(cx) {
  var uri = this.getLinkUrl(cx);

  var restAdapter = dcc.RestAdapterFactory.create(this.accessor);
  restAdapter.del(uri + cx.getKey());
};
