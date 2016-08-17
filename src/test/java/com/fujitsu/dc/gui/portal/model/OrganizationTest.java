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

import java.util.Date;
import java.util.HashSet;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class OrganizationTest {
	/**
	 * Check all exposed instantiation and their correctness
	 */
	@Test
	public void testInstantiation(){
		Organization object1 = new Organization();
		Organization instance = new Organization("ID100","testName",1,new HashSet<User>(),new HashSet<Environment>(),new HashSet<OrgUnit>(),new Date(),new Date());
		assertTrue("Instantiation Successful", object1!=null);
		assertTrue("Id Retrieved Successfully","ID100".equals(instance.getOrgId()));
		assertTrue("Organization Name Retrieved Successfully","testName".equals(instance.getName()));
		assertTrue("is Active Flag Retrieved Successfully",1 == instance.getIsActive());
		assertTrue("User set Retrieved Successfully",instance.getUsers()!=null);
		assertTrue("Environment Set Retrieved Successfully",instance.getEnvironments()!=null);
		assertTrue("Organization Unit Retrieved Successfully",instance.getOrgUnits()!=null);
		assertTrue("Created At Date Retrieved Successfully",instance.getCreatedAt()!=null);
		assertTrue("Updated At Date Retrieved Successfully",instance.getUpdateddAt()!=null);
	}
}
