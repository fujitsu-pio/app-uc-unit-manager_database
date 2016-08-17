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
		"relationToRoleMappingTestSuite",
		function() {
			var objCommon = new common();
			beforeEach(function() {
			});

			// Test case for pass condition.
			it(
					"getShorterEntityNamePass",
					function() {
						expect(
								objCommon
										.getShorterEntityName("pcs1hcl08pcs1hcl08pcs1hcl08pcs1hcl08pcs1hcl08pcs1hcl08"))
								.toEqual("pcs1hcl08pcs1hcl08p..");
					});
			// Test case for fail condition.
			it(
					"getShorterEntityNameFail",
					function() {
						expect(objCommon
								.getShorterEntityName("pcs1hcl08pcs1hcl08pcs1hcl08pcs1hcl08pcs1hcl08pcs1hcl08")).not
								.toEqual("pcs1hcl08pcs1hcl08pc..");
					});

			// Test case for pass condition.
			it(
					"getBoxNameFromURIPass",
					function() {
						expect(
								objCommon
										.getBoxNameFromURI("https://sample.jp.fujitsu.com/cellname/__ctl/Role (Name = 'rolename', _Box.Name = 'box1')"))
								.toEqual("box1");
					});
			// Test case for fail condition
			it(
					"getBoxNameFromURIFail",
					function() {
						expect(objCommon
								.getBoxNameFromURI("https://sample.jp.fujitsu.com/cellname/__ctl/Role (Name = 'rolename', _Box.Name = 'box1')")).not
								.toEqual("box2");
					});
			// Test case for pass condition.
			it(
					"getRoleNameFromURIPass",
					function() {
						expect(
								objCommon
										.getRoleNameFromURI("https://sample.jp.fujitsu.com/cellname/__ctl/Role (Name = 'role1', _Box.Name = null)"))
								.toEqual("role1");
					});
			// Test case for fail condition.
			it(
					"getRoleNameFromURIFail",
					function() {
						expect(objCommon
								.getRoleNameFromURI("https://sample.jp.fujitsu.com/cellname/__ctl/Role (Name = 'role1', _Box.Name = null)")).not
								.toEqual("role2");
					});

		});
