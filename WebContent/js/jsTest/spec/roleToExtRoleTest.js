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
/**
 * The purpose of this function is to fetch the values of selected external
 * cell.
 */
describe(
		"roleToExtRoleMappingTestSuite",
		function() {
			var uRoleExtRoleMapping = new roleToExtRoleMapping();
			beforeEach(function() {
			});
			
			it("getSelectedExtRole", function() {
				expect(uRoleExtRoleMapping.getSelectedExtRole()).toBe(false);
			});
			
			it("createTableHeaderColNaming", function() {
				var dynamicTable = "";
				expect(uRoleExtRoleMapping.createTableHeader(dynamicTable)).toMatch("col4");
			});
			
			it("createTableHeaderColName", function() {
				var dynamicTable = "";
				expect(uRoleExtRoleMapping.createTableHeader(dynamicTable)).toMatch("Box");
			});
			
			it("createRowChkIDpositive", function() {
				var dynamicTable = "";
				expect(uRoleExtRoleMapping.createRowForRoleExtRoleLinkTable(dynamicTable, 2, "abc.com", "a", "testRelation", "testRel", "testBox", "testB")).toMatch('id="chkBox2"');
			});
			it("createRowChkIDnegative", function() {
				var dynamicTable = "";
				expect(uRoleExtRoleMapping.createRowForRoleExtRoleLinkTable(dynamicTable, 2, "abc.com", "a", "testRelation", "testRel", "testBox", "testB")).not.toMatch('id="chkBox3"');
			});
		});
