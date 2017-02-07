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
public class MessageTest {
	/**
	 * Check all exposed instantiation and their correctness
	 */
	@Test
	public void testInstantiation() {
		Message message = new Message();
		assertTrue("Instantiate Successful", message!=null);
		message = new Message(1, "Test Message", (byte)0, new Date(), new Date(), new Date(), new Date(), new HashSet<UnitMessage>());
		assertTrue("Parameterized Instantiate Successful", message!=null);
		assertTrue("ID GET Successful", message.getMessageId()==1);
		assertTrue("CONTENT GET Successful", message.getContent().equals("Test Message"));
		assertTrue("ISGLOBAL GET Successful", message.getIsGlobal()==0);
		assertTrue("Created At Date GET Successful", message.getCreatedAt() != null);
		assertTrue("Updated At Date GET Successful", message.getUpdateddAt() != null);
		assertTrue("Deleted At Date GET Successful", message.getDeletedAt() != null);
		assertTrue("Expired At Date GET Successful", message.getExpiredAt() != null);
		assertTrue("UnitMessage GET Successful", message.getUnitMessage() != null);
		
	}
	
	/**
	 * Check all exposed instantiation and their correctness
	 */
	@Test
	public void testInstantiationUsingSetters() {
		Message message = new Message();
		assertTrue("Instantiate Successful", message!=null);
		//message = new Message(1, "Test Message", (byte)0, new Date(), new Date(), new Date(), new Date(), new HashSet<UnitMessage>());
		
		message.setMessageId(1);
		message.setContent("Test Message");
		message.setIsGlobal((byte)0);
		message.setExpiredAt(new Date());
		message.setCreatedAt(new Date());
		message.setUpdateddAt(new Date());
		message.setDeletedAt(new Date());
		message.setUnitMessage(new HashSet<UnitMessage>());
		assertTrue("Parameterized Instantiate Successful", message!=null);
		assertTrue("ID GET Successful", message.getMessageId()==1);
		assertTrue("URL GET Successful", message.getContent().equals("Test Message"));
		assertTrue("Type GET Successful", message.getIsGlobal()==0);
		assertTrue("Get environment Successful", message.getCreatedAt() != null);
		assertTrue("Get environment Successful", message.getUpdateddAt() != null);
		assertTrue("Get environment Successful", message.getDeletedAt() != null);
		assertTrue("Get environment Successful", message.getExpiredAt() != null);
		assertTrue("Get environment Successful", message.getUnitMessage() != null);
		
	}
}
