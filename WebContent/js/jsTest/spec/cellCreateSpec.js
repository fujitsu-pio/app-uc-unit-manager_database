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
//positive unit test case for checking Length range between minimum and maximum length
describe("checkLengthPositive", function () {
    var obj = new checkLengthPositive(); // sets initial Name   
    afterEach(function () {
        obj.getMinLength();
    });
    it("checkLengthPositive", function () {
        console.log(obj);
        expect(obj.getCellLength('sampleCell')).toBeGreaterThan(obj.getMinLength());
        expect(obj.getCellLength('sampleCell')).toBeLessThan(obj.getMaxLength());
    });
});


//Negative test case for checking Length range out of minimum and maximum length
describe("checkLengthNegative", function () {
    var obj = new checkLengthNegative(); // sets initial Name   
    afterEach(function () {
        obj.getMinLength();
    });
    it("checkLengthNegative", function () {
        console.log(obj);
        expect(obj.getCellLength('')).toBeLessThan(obj.getMinLength());
        expect(obj.getCellLength('sampleCell123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789')).toBeGreaterThan(obj.getMaxLength());
    });
});

//Positive test case for Epoch date conversion
describe("commonVar", function () {
    var obj = new commonVar(); // sets initial Name   

    it("commonVar", function () {
        console.log(obj);
        expect('1-Jan-1970').toBe(obj.createUpdateDate('1365147875'));
    });
});

//Negative test case for Epoch date conversion
describe("commonVar", function () {
    var obj = new commonVar(); // sets initial Name   

    it("commonVar", function () {
        console.log(obj);
        expect('1-Jan-2018').not.toBe(obj.createUpdateDate('1365147875'));
    });
});