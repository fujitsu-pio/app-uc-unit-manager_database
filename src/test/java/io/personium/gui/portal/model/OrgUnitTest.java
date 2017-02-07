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
package io.personium.gui.portal.model;

import java.util.Date;
import java.util.HashSet;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class OrgUnitTest {

	/**
	 * Check all exposed instantiation and their correctness
	 */
	@Test
	public void testInstantiation() {
		OrgUnit instance = new OrgUnit();
		final int maxEnv = 10;
		Organization organization = new Organization("1", "testOrganization",
				1, null, null, null, null, null);

		assertTrue("Instantiated Successfully", instance != null);

		/** Set items in right sequence */
		instance.setMaxEnv(maxEnv);
		instance.setOrgUnitId(new OrgUnitId());
		instance.setOrganization(organization);
		instance.setUnit(new Unit());
		instance.setCreatedAt(new Date());
		instance.setUpdateddAt(new Date());
		assertTrue("Retrieve Successfully - maxEnv",
				instance.getMaxEnv() == maxEnv);
		assertTrue("Retrieve Successfully - Organization",
				instance.getOrganization().getName().equals("testOrganization"));
		assertTrue("Retrieve Successfully - Unit",
				instance.getUnit()!=null);
		assertTrue("Retrieve Successfully - OrgUnitId",
				instance.getOrgUnitId()!=null);
		assertTrue("Retrieve Successfully - Created At Date",
				instance.getCreatedAt()!=null);
		assertTrue("Retrieve Successfully - Updated At Date",
				instance.getUpdateddAt()!=null);
	}
	
	@Test
	public void testEquality() {
		OrgUnit instance1 = new OrgUnit();
		OrgUnit instance2 = new OrgUnit();
		OrgUnitId orgUnitId1 = new OrgUnitId();
		OrgUnitId orgUnitId2 = new OrgUnitId();
		Organization organization = new Organization("ID100","testName",1,new HashSet<User>(),new HashSet<Environment>(),new HashSet<OrgUnit>(),null,null);
		Unit unit = new Unit();
		orgUnitId1.setOrganization(organization);
		orgUnitId1.setUnit(unit);
		instance1.setOrgUnitId(orgUnitId1);
		instance2.setOrgUnitId(orgUnitId1);
		assertTrue("Pass - Objects are equal ", instance1.equals(instance2));
		instance2.setOrgUnitId(orgUnitId2);
		assertTrue("Pass - Objects are unequal ", !instance1.equals(instance2));
		assertTrue("Pass - Object and Null are unequal ", !instance1.equals(null));
		assertTrue("Pass - Same Objects are equal ", instance1.equals(instance1));
		instance1.setOrgUnitId(null);
		assertTrue("Pass - Objects are unequal ", !instance1.equals(instance2));
	}
	

	@Test
	public void testHashCode() {
		Organization organization = new Organization("ID100","testName",1,new HashSet<User>(),new HashSet<Environment>(),new HashSet<OrgUnit>(),null,null);
		OrgUnit orgUnit = new OrgUnit();
		OrgUnitId orgUnitId = new OrgUnitId();
		orgUnitId.setOrganization(organization);
		orgUnit.setOrgUnitId(orgUnitId);
		assertTrue("Pass - non blank orgUnitId return non zero hashCode ", !(orgUnit.hashCode()==0));
		
		orgUnit.setOrgUnitId(null);
		assertTrue("Pass - null orgUnitId return 0 hashCode ", orgUnit.hashCode()==0);
	}
	
	@Test
	public void testEmbeddedKey() {
		Organization organization = new Organization("ID100","testName",1,new HashSet<User>(),new HashSet<Environment>(),new HashSet<OrgUnit>(),null,null);
		Organization organization1 = new Organization("ID1001","testName",1,new HashSet<User>(),new HashSet<Environment>(),new HashSet<OrgUnit>(),null,null);
		Unit unit = new Unit(1,"www.com","A",new HashSet<Environment>(),new HashSet<OrgUnit>(),1);
		Unit unit1 = new Unit(2,"www.com","A",new HashSet<Environment>(),new HashSet<OrgUnit>(),1);
		
		OrgUnitId orgUnitId1 = new OrgUnitId();
		orgUnitId1.setOrganization(organization);
		orgUnitId1.setUnit(unit);
		
		OrgUnitId orgUnitId2 = new OrgUnitId();
		orgUnitId2.setOrganization(organization);
		orgUnitId2.setUnit(unit);
		
		assertTrue("Pass - Equal object ", orgUnitId1.equals(orgUnitId2));
		assertTrue("Pass - Same object ", !orgUnitId1.equals(null));
		
		orgUnitId1.setUnit(null);
		assertTrue("Pass - Same object ", orgUnitId1.equals(orgUnitId1));
		orgUnitId2.setUnit(unit1);
		assertTrue("Pass - Different object ", !orgUnitId1.equals(orgUnitId2));
		
		orgUnitId2.setOrganization(organization1);
		assertTrue("Pass - Different object ", !orgUnitId1.equals(orgUnitId2));
		
		orgUnitId1.setOrganization(null);
		orgUnitId2.setOrganization(null);
		assertTrue("Pass - Different object ", !orgUnitId1.equals(orgUnitId2));
		
		orgUnitId1.setUnit(null);
		orgUnitId2.setUnit(null);
		assertTrue("Pass - Different object ", orgUnitId1.equals(orgUnitId2));
		
		orgUnitId1.setUnit(null);
		orgUnitId1.setOrganization(null);
		assertTrue(orgUnitId1.hashCode()==0);
		
		orgUnitId1.setUnit(new Unit());
		assertTrue(orgUnitId1.hashCode()!=0);
		
		
		
	}
}