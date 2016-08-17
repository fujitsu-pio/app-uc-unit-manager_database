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
 * Positive test case for getting Box Name. 
 */
describe("getBoxNamePass(uri) function", function() {
	it("getBoxNamePass", function() {
		expect(getBoxName("https://fj.baas.jp.fujitsu.com/01test12March/__ctl/Role(Name='testRoleName',_Box.Name='testBoxName')")).toBe("testBoxName");
	});
});

/**
 * Negative test case for getting Box Name. 
 */
describe("getBoxNameFail(uri) function", function() {
	it("getBoxNameFail", function() {
		expect(getBoxName("https://fj.baas.jp.fujitsu.com/01test12March/__ctl/Role(Name='testRoleName',_Box.Name='testBoxName')")).not.toBe("testBox");
	});
});

/**
 * Positive test case for getting Role Name. 
 */

describe("getRoleNamePass(uri) function", function() {
	it("getRoleNamePass", function() {
		expect(getRoleName("https://fj.baas.jp.fujitsu.com/01test12March/__ctl/Role(Name='testRoleName',_Box.Name='testBoxName')")).toBe("testRoleName");
	});
});

/**
 * /Negative test case for getting Role Name.
 */
describe("getRoleNameFail(uri) function", function() {
	it("getRoleNameFail", function() {
		expect(getRoleName("https://fj.baas.jp.fujitsu.com/01test12March/__ctl/Role(Name='testRoleName',_Box.Name='testBoxName')")).not.toBe("testRole");
	});
});

/**
 * /Positive test case for getting Box Name.
 */
describe("getBoxNameForDeletePASS(uri) function", function() {
	it("getBoxNameForDeletePASS", function() {
		expect(getBoxNameForDelete("testRole -- testBox")).toBe("testRole ");
	});
});

/**
 * /Positive test case for getting Box Name.
 */
describe("getBoxNameForDeleteFAIL(uri) function", function() {
	it("getBoxNameForDeleteFAIL", function() {
		expect(getBoxNameForDelete("testRole -- testBox")).not.toBe("testRole1");
	});
});

/**
 * /Positive test case for getting Role Name.
 */
describe("getRoleNameForDeletePASS(uri) function", function() {
	it("getRoleNameForDeletePASS", function() {
		expect(getRoleNameForDelete("testRole -- testBox")).toBe("testBox");
	});
});

/**
 * /Negative test case for getting Role Name.
 */
describe("getRoleNameForDeleteFAIL(uri) function", function() {
	it("getRoleNameForDeleteFAIL", function() {
		expect(getRoleNameForDelete("testRole -- testBox")).not.toBe("testBox1");
	});
});