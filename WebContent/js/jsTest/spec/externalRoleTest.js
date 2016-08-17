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
		"externalRoleTestSuite",
		function() {
			var objexternalRole = new externalRole();
			beforeEach(function() {				
			});
			//Test case for pass condition.
			it("getExternalRoleNameFromURIPass",function() {
				expect(objexternalRole.getExternalRoleNameFromURI("https://fj.baas.jp.fujitsu.com/testrel1/__role/__/testrole1")).toEqual("testrole1");
			});
			//Test case for fail condition.
			it("getExternalRoleNameFromURIFail",function() {
				expect(objexternalRole.getExternalRoleNameFromURI("https://fj.baas.jp.fujitsu.com/testrel1/__role/__/testrole1")).not.toEqual("testrole123");
			});
			
			//Test case for pass condition.
			it("getBoxNameFromURIPass",function() {
				expect(objexternalRole.getBoxNameFromURI("https://fqdn/cellname/__ctl/ExtRole (ExtRole = 'doctor', _Relation.Name = 'testrelation', _Relation._Box.Name = 'testbox')")).toEqual("testbox");
			});
			//Test case for fail condition.
			it("getBoxNameFromURIFail",function() {
				expect(objexternalRole.getBoxNameFromURI("https://fqdn/cellname/__ctl/ExtRole (ExtRole = 'doctor', _Relation.Name = 'testrelation', _Relation._Box.Name = 'testbox')")).not.toEqual("testbox1");
			});;
			
			//Test case for pass condition.
			it("getRelationNameFromURIPass",function() {
				expect(objexternalRole.getRelationNameFromURI("https://fqdn/cellname/__ctl/ExtRole (ExtRole = 'doctor', _Relation.Name = 'testrelation', _Relation._Box.Name = 'testbox')")).toEqual("testrelation");
			});
			//Test case for fail condition.
			it("getRelationNameFromURIFail",function() {
				expect(objexternalRole.getRelationNameFromURI("https://fqdn/cellname/__ctl/ExtRole (ExtRole = 'doctor', _Relation.Name = 'testrelation', _Relation._Box.Name = 'testbox')")).not.toEqual("testrelation1");
			});
			
		});