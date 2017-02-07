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

import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class UserEnvironmentTest {
	/**
	 * Check all exposed instantiation and their correctness
	 */
	@Test
	public void testInstantiation() {
		UserEnvironment uEnvironment = new UserEnvironment();
		User user = new User();
		Environment env = new Environment("id100", "dev", null, new Unit());
		UserEnvironmentId id = new UserEnvironmentId();
		id.setUser(user);
		uEnvironment.setUserEnvironmentId(id);
		uEnvironment.setUser(user);
		uEnvironment.setEnvironment(env);
		
		assertTrue("Instance created", uEnvironment!=null);
		assertTrue(uEnvironment.getUser().equals(user));
		assertTrue(uEnvironment.getEnvironment().equals(env));
	}
	
	@Test
	public void testEquality() {
		UserEnvironment instance1 = new UserEnvironment();
		UserEnvironment instance2 = new UserEnvironment();
		User user = new User();
		Environment env = new Environment("id100", "dev", null, new Unit());
		UserEnvironmentId id = new UserEnvironmentId();
		user.setName("AA");
		id.setUser(user);
		
		UserEnvironmentId id1 = new UserEnvironmentId();
		id1.setUser(new User());
		
		instance1.setUserEnvironmentId(id);
		instance1.setUser(user);
		instance1.setEnvironment(env);
		
		instance2.setUserEnvironmentId(id);
		instance2.setUser(user);
		instance2.setEnvironment(env);
		
		assertTrue("Found Equal", instance1.equals(instance2));
		assertTrue("Found Equal - self comparison", instance1.equals(instance1));
		assertTrue("Found UnEqual - null object", !instance1.equals(null));
		assertTrue("Found UnEqual - other class object", !instance1.equals(new Object()));
		
		instance2.setUserEnvironmentId(id1);
		assertTrue("Found UnEqual", !instance1.equals(new Object()));
		
		instance2.setUserEnvironmentId(id);
		instance1.setUserEnvironmentId(null);
		
		instance2.setUserEnvironmentId(id1);
		assertTrue("Found UnEqual", !instance1.equals(new Object()));
	}
	
	@Test
	public void testHashCode() {
		UserEnvironment instance = new UserEnvironment();
		User user = new User();
		Environment env = new Environment("id100", "dev", null, new Unit());
		UserEnvironmentId id = new UserEnvironmentId();
		id.setUser(user);
		instance.setUserEnvironmentId(id);
		instance.setUser(user);
		instance.setEnvironment(env);
		assertTrue("HashCode non Zero", instance.hashCode()!=0);
		
		instance.setUserEnvironmentId(null);
		assertTrue("HashCode Zero", instance.hashCode()==0);
	}
	
	@Test
	public void testUEID() {
		UserEnvironmentId uenv = new UserEnvironmentId();
		UserEnvironmentId object = new UserEnvironmentId();
		uenv.setEnvironment(new Environment());
		uenv.setUser(new User());
		object.setEnvironment(new Environment());
		object.setUser(new User());
		assertTrue("Equal Check", uenv.equals(uenv));
		assertTrue("UnEqual Check", !uenv.equals(null));
		assertTrue("UnEqual Check", !uenv.equals(new Object()));
		assertTrue("UnEqual Check", !uenv.equals(object));
	}
	
	@Test
	public void testEqualityIdUser() {
		UserEnvironmentId uenv = new UserEnvironmentId();
		UserEnvironmentId object = new UserEnvironmentId();
		User userA = new User();
		User userB = new User();
		uenv.setEnvironment(new Environment());
		uenv.setUser(userA);
		object.setEnvironment(new Environment());
		object.setUser(userB);
		assertTrue("UnEqual Check", !uenv.equals(object));
		uenv.setUser(null);
		assertTrue("UnEqual Check", !uenv.equals(object));
		object.setUser(null);
		assertTrue("UnEqual Check", !uenv.equals(object));
	}
	
	@Test
	public void testEqualityIdEnvironment() {
		UserEnvironmentId uenv = new UserEnvironmentId();
		UserEnvironmentId object = new UserEnvironmentId();
		Environment env = new Environment();
		User userA = new User();
		uenv.setUser(userA);
		uenv.setEnvironment(env);
		object.setUser(userA);
		object.setEnvironment(env);
		
		assertTrue("Equal Check", uenv.equals(object));
		object.setEnvironment(new Environment());
		assertTrue("UnEqual Check", !uenv.equals(object));
		uenv.setEnvironment(null);
		object.setEnvironment(env);
		assertTrue("UnEqual Check", !uenv.equals(object));
		object.setEnvironment(null);
		assertTrue("Equal Check", uenv.equals(object));
	}
	
	@Test
	public void hashCodeID() {
		UserEnvironmentId uenv = new UserEnvironmentId();
		uenv.setUser(new User());
		uenv.setEnvironment(new Environment());
		assertTrue("For not null Item hashcode is non zero", uenv.hashCode()!=0);
		uenv.setUser(null);
		assertTrue("For not null Item hashcode is non zero", uenv.hashCode()!=0);
		uenv.setUser(new User());
		uenv.setEnvironment(null);
		assertTrue("For not null Item hashcode is non zero", uenv.hashCode()!=0);
	}
}
