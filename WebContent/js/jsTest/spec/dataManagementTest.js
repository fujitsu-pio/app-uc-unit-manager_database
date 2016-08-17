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
		"dataManagementTestSuite",
		function() {
			var uDataManagement = new dataManagement();
			beforeEach(function() {				
			});
			//Test case for pass condition.
			it("getListDataPass",function() {
				var result = new Array();
				result.push("test1");
				result.push("test2");
				result.push("test3");
				expect(uDataManagement.getListData("test1,test2,test3")).toEqual(result);
			});
			//Test case for fail condition.
			it("getListDataFail",function() {
				var result = new Array();
				result.push("test1");
				result.push("test2");
				result.push("test3");
				expect(uDataManagement.getListData("test1,test2")).not.toEqual(result);
			});
});