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
		"odataTestSuite",
		function() {
			var uOdata = new odata ();
			beforeEach(function() {				
			});
			//Test case for pass condition.
			it("getNamePass",function() {
				expect(uOdata.getName("https://fqdn/cellname/boxname/collectionname")).toEqual("collectionname");
			});
			//Test case for fail condition.
			it("getNameFail",function() {
				expect(uOdata.getName("https://fqdn/cellname/boxname/collectionname")).not.toEqual("collectionname1");
			});
});