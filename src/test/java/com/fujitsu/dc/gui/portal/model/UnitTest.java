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
package com.fujitsu.dc.gui.portal.model;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.junit.Test;
public class UnitTest {
	/**
	 * Check all exposed instantiation and their correctness
	 */
	@Test
	public void testInstantiation() {
		Unit unit = new Unit();
		assertTrue("Instantiate Successful", unit!=null);
		unit = new Unit(1, "http://test", "A", new HashSet<Environment>(), new HashSet<OrgUnit>(),1);
		assertTrue("Parameterized Instantiate Successful", unit!=null);
		assertTrue("ID GET Successful", unit.getUnitId() == 1);
		assertTrue("URL GET Successful", unit.getUrl().equals("http://test"));
		assertTrue("Type GET Successful", unit.getType().equals("A"));
		assertTrue("Get environment Successful", unit.getEnvironments().size()==0);
		assertTrue("Get orgUnit Successful", unit.getOrgUnits().size()==0);
		assertTrue("Get Unit Available", unit.getIsAvailable()==1);
		
		/*unit = new Unit(100, "http://test", "A", new HashSet<Environment>(), new HashSet<OrgUnit>(),"testName","ID1","testPlan",100L,100L,"active",(byte)0,"testServer","8001","10.111.66.102",new Date(),new Date(),new Date());
		assertTrue("Parameterized Instantiate Successful", unit!=null);
		assertTrue("ID GET Successful", unit.getUnitId()==100);
		assertTrue("URL GET Successful", unit.getUrl().equals("http://test"));
		assertTrue("Type GET Successful", unit.getType().equals("A"));
		assertTrue("Get environment Successful", unit.getEnvironments().size()==0);
		assertTrue("Get orgUnit Successful", unit.getOrgUnits().size()==0);
		assertTrue("URL GET Successful", unit.getName().equals("testName"));
		assertTrue("URL GET Successful", unit.getVsysid().equals("ID1"));
		assertTrue("URL GET Successful", unit.getPlan().equals("testPlan"));
		assertTrue("URL GET Successful", unit.getOdataDisk() == 100L);
		assertTrue("URL GET Successful", unit.getWebdavDisk() == 100L);
		assertTrue("URL GET Successful", unit.getStatus().equals("active"));
		assertTrue("URL GET Successful", unit.getIsDeleted() == 0);
		assertTrue("URL GET Successful", unit.getMail().equals("testServer"));
		assertTrue("URL GET Successful", unit.getMailPort().equals("8001"));
		assertTrue("URL GET Successful", unit.getGlobalIP().equals("10.111.66.102"));
		assertTrue("URL GET Successful", unit.getCreatedAt() != null);
		assertTrue("URL GET Successful", unit.getUpdateddAt() != null);
		assertTrue("URL GET Successful", unit.getDeletedAt() != null);*/
	}
}
