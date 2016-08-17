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
 * Test Suites for externalCellToRelationMapping library.
 */
describe(
		"External Cell To Relation Mapping Test Suite",
		function() {
			var objRelExtCellLink = new externalCellToRelationMapping();
			beforeEach(function() {
			});
			// Test case for getBoxName() valid scenaio
			it("getBoxNameValid", function() {
				expect(objRelExtCellLink.getBoxName("https://fj.baas.jp.fujitsu.com/testCell/_ctl/Relation(Name='testRel',_Box.Name='testBox')")).toBe("testBox");
			});
			// Test case for getBoxName() invalid scenaio
			it("getBoxNameNull", function() {
				expect(objRelExtCellLink.getBoxName("https://fj.baas.jp.fujitsu.com/testCell/_ctl/Relation(Name='testRel',_Box.Name=null)")).toBe('null');
			});
			// Test case for getRelationName() valid scenaio
			it("getRelationNameValid",function() {
				expect(objRelExtCellLink.getRelationName("https://fj.baas.jp.fujitsu.com/testCell/_ctl/Relation(Name='testRel',_Box.Name='testBox')")).toEqual("testRel");
			});
			// Test case for getRelationName() invalid scenaio
			it("getRelationNameInvalid", function() {
				expect(objRelExtCellLink.getRelationName("https://fj.baas.jp.fujitsu.com/testCell/_ctl/Relation(Name='testRel',_Box.Name=null)")).not.toBe('invalidRelName');
			});
			it("getSuccessMessageValid", function() {
				expect(objRelExtCellLink.getSuccessMessage("testRelation","testBoxName")).toBe("Configuration with Relation testRelation -- testBoxName deleted successfully!");
			});
			it("getSuccessMessageInValid", function() {
				expect(objRelExtCellLink.getSuccessMessage("testRelation","testBoxName")).not.toBe("Configuration with Relation testRelation  testBoxName deleted successfully!");
			});
			it("isBoxNameEmptyValid", function() {
				expect(objRelExtCellLink.isBoxNameEmpty("__")).toBe(true);
			});
			it("isBoxNameEmptyInValid", function() {
				expect(objRelExtCellLink.isBoxNameEmpty("__")).not.toBe(false);
			});
			
		});