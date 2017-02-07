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

public class UnitMessageTest {

	/**
	 * Check all exposed instantiation and their correctness
	 */
	@Test
	public void testInstantiation() {
		UnitMessage instance = new UnitMessage();
		UnitMessageId unitMessageId = new UnitMessageId();

		assertTrue("Instantiated Successfully", instance != null);

		/** Set items in right sequence */
		instance.setUnitMessageId(unitMessageId);
		instance.setIsHidden((byte)0);
		instance.setCreatedAt(new Date());
		instance.setUpdateddAt(new Date());
		assertTrue("Retrieve Successfully - UnitMessageId",
				instance.getUnitMessageId() != null);
		assertTrue("Retrieve Successfully - IsHidden",
				instance.getIsHidden()==0);
		assertTrue("Retrieve Successfully - Created At Date",
				instance.getCreatedAt()!=null);
		assertTrue("Retrieve Successfully - Updated At Date",
				instance.getUpdateddAt()!=null);
	}
	
	@Test
	public void testEquality() {
		UnitMessage instance1 = new UnitMessage();
		UnitMessage instance2 = new UnitMessage();
		UnitMessageId unitMessageId1 = new UnitMessageId();
		UnitMessageId unitMessageId2 = new UnitMessageId();
		//Organization organization = new Organization("ID100","testName",1,new HashSet<User>(),new HashSet<Environment>(),new HashSet<OrgUnit>(),null,null);
		Message message = new Message();
		Unit unit = new Unit();
		unitMessageId1.setMessage(message);
		unitMessageId2.setUnit(unit);
		instance1.setUnitMessageId(unitMessageId1);
		instance2.setUnitMessageId(unitMessageId1);
		assertTrue("Pass - Objects are equal ", instance1.equals(instance2));
		instance2.setUnitMessageId(unitMessageId2);
		assertTrue("Pass - Objects are unequal ", !instance1.equals(instance2));
		assertTrue("Pass - Object and Null are unequal ", !instance1.equals(null));
		assertTrue("Pass - Same Objects are equal ", instance1.equals(instance1));
		instance1.setUnitMessageId(null);
		assertTrue("Pass - Objects are unequal ", !instance1.equals(instance2));
	}
	

	@Test
	public void testHashCode() {
		//Organization organization = new Organization("ID100","testName",1,new HashSet<User>(),new HashSet<Environment>(),new HashSet<OrgUnit>(),null,null);
		Message message = new Message();
		UnitMessage untiMessage = new UnitMessage();
		UnitMessageId untiMessageId = new UnitMessageId();
		untiMessageId.setMessage(message);
		untiMessage.setUnitMessageId(untiMessageId);
		assertTrue("Pass - non blank orgUnitId return non zero hashCode ", !(untiMessage.hashCode()==0));
		
		untiMessage.setUnitMessageId(null);
		assertTrue("Pass - null orgUnitId return 0 hashCode ", untiMessage.hashCode()==0);
	}
	
	@Test
	public void testEmbeddedKey() {
		Message message = new Message(1, "Test Message", (byte)0, new Date(), new Date(), new Date(), new Date(), new HashSet<UnitMessage>());
		Message message1 = new Message(2, "Test Message", (byte)0, new Date(), new Date(), new Date(), new Date(), new HashSet<UnitMessage>());
		
		Unit unit = new Unit(1,"www.com","A",new HashSet<Environment>(),new HashSet<OrgUnit>(),1);
		Unit unit1 = new Unit(2,"www.com","A",new HashSet<Environment>(),new HashSet<OrgUnit>(),1);
		
		UnitMessageId untiMessageId1 = new UnitMessageId();
		untiMessageId1.setMessage(message);
		untiMessageId1.setUnit(unit);
		
		UnitMessageId untiMessageId2 = new UnitMessageId();
		untiMessageId2.setMessage(message);
		untiMessageId2.setUnit(unit);
		
		assertTrue("Pass - Equal object ", untiMessageId1.equals(untiMessageId2));
		assertTrue("Pass - Same object ", !untiMessageId1.equals(null));
		
		untiMessageId1.setUnit(null);
		assertTrue("Pass - Same object ", untiMessageId1.equals(untiMessageId1));
		untiMessageId2.setUnit(unit1);
		assertTrue("Pass - Different object ", !untiMessageId1.equals(untiMessageId2));
		
		untiMessageId2.setMessage(message1);
		assertTrue("Pass - Different object ", !untiMessageId1.equals(untiMessageId2));
		
		untiMessageId1.setMessage(null);
		untiMessageId2.setMessage(null);
		assertTrue("Pass - Different object ", !untiMessageId1.equals(untiMessageId2));
		
		untiMessageId1.setUnit(null);
		untiMessageId2.setUnit(null);
		assertTrue("Pass - Different object ", untiMessageId1.equals(untiMessageId2));
		
		untiMessageId1.setUnit(null);
		untiMessageId1.setMessage(null);
		assertTrue(untiMessageId1.hashCode()==0);
		
		untiMessageId1.setUnit(new Unit());
		assertTrue(untiMessageId1.hashCode()!=0);
		
		
		
	}
}