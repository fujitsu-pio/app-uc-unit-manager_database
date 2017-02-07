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
import java.util.Set;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class EnvironmentTest {
	
	/**
	 * Check all exposed instantiation and their correctness
	 */
	@Test
	public void testInstantiation(){
		Set<UserEnvironment> userEnvironments = new HashSet<UserEnvironment>(0);
		Environment environment = new Environment();
		assertTrue("Instantiation Successful", environment!=null);
		
		environment = new Environment("id100", "dev", new Organization(), new Unit());
		assertTrue("Id checked ",environment.getEnvId().equals("id100"));
		assertTrue("Env Name checked ",environment.getName().equals("dev"));
		assertTrue("Organization checked ",environment.getOrganizationenv()!=null);
		assertTrue("Unit checked ",environment.getUnit()!=null);
		
		userEnvironments.add(new UserEnvironment());
		environment = new Environment("id100", "dev", new Organization(), new Unit(),userEnvironments);
		assertTrue("Unit checked ",environment.getUserEnvironments().size()==1);
		
		environment = new Environment("id100", "dev", new Unit(), new Organization(), userEnvironments, (byte)0, new Date(), new Date(), new Date() );
		assertTrue("Id checked ",environment.getEnvId().equals("id100"));
		assertTrue("Env Name checked ",environment.getName().equals("dev"));
		assertTrue("Organization checked ",environment.getOrganizationenv()!=null);
		assertTrue("Unit checked ",environment.getUnit()!=null);
		assertTrue("Is Deleted checked ",environment.getIsDeleted()==0);
		assertTrue("Created Date checked ",environment.getCreatedAt()!=null);
		assertTrue("Updated Date checked ",environment.getUpdateddAt()!=null);
		assertTrue("Deleted Date checked ",environment.getDeletedAt()!=null);
		
		userEnvironments.add(new UserEnvironment());
		environment = new Environment("id100", "dev", new Organization(), new Unit(),userEnvironments);
		assertTrue("Unit checked ",environment.getUserEnvironments().size()==1);
	}

}
