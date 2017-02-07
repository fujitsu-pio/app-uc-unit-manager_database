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
describe(
		"roleToAccountMappingTestSuite",
		function() {
			var objCommon = new common();
			beforeEach(function() {
				
			});
			//Test case for pass condition.
			it("getAccountNameFromURIPass",function() {
				expect(objCommon.getAccountNameFromURI("https://demo.personium.io/01test12March/_ctl/Account('test121212')")).toEqual("test121212");
			});
			//Test case for fail condition.
			it("getAccountNameFromURIFail",	function() {
				expect(objCommon.getAccountNameFromURI("https://demo.personium.io/01test12March/_ctl/Account('test121212')")).not.toEqual("test121212def");
			});
			//Test case for pass condition.
			it("getBoxNameFromURIPass",function() {
				expect(objCommon.getBoxNameFromURI("https://test.personium.io/cellname/__ctl/Role (Name = 'rolename', _Box.Name = 'test123')")).toEqual("test123");
				});
			//Test case for fail condition
			it("getBoxNameFromURIFail",function() {
				expect(objCommon.getBoxNameFromURI("https://test.personium.io/cellname/__ctl/Role (Name = 'rolename', _Box.Name = null)")).not.toEqual("test123");
			});
			//Test case for pass condition.
			it("getRoleNameFromURIPass",function() {
				expect(objCommon.getRoleNameFromURI("https://test.personium.io/cellname/__ctl/Role (Name = 'test123', _Box.Name = null)")).toEqual("test123");
			});
			//Test case for fail condition.
			it("getRoleNameFromURIFail",function() {
				expect(objCommon.getRoleNameFromURI("https://test.personium.io/cellname/__ctl/Role (Name = 'test123', _Box.Name = null)")).not.toEqual("test1234");
			});
			
			
		});