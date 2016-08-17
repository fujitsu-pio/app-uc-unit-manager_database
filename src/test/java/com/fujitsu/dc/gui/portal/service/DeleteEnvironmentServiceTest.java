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
 * Test cases for Create Environment functionality
 */
package com.fujitsu.dc.gui.portal.service;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.easymock.EasyMock;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fujitsu.dc.gui.portal.HibernateUtil;
import com.fujitsu.dc.gui.portal.dao.DeleteEnvironmentDAO;

public class DeleteEnvironmentServiceTest {
	
	private DeleteEnvironmentService deleteEnvironmentService = null;
	private DeleteEnvironmentDAO mockDeleteEnvironmentDAO = null;
	
	@Before
	public void setUp() throws Exception {
		deleteEnvironmentService = new DeleteEnvironmentService();
		mockDeleteEnvironmentDAO = EasyMock
				.createStrictMock(DeleteEnvironmentDAO.class);
		deleteEnvironmentService
				.setDeleteEnvironmentDAO(mockDeleteEnvironmentDAO);
	}

	@After
	public void tearDown() {
		deleteEnvironmentService = null;
	}

	@Test
	public void testDeleteEnvironmentServicePass() throws Exception {
		String envID = "03278b4a-b2ee-4f2c-b271-80502245b042";
		String result = "success";
		EasyMock.expect(
				mockDeleteEnvironmentDAO.deleteEnvironmentRecord(envID))
				.andReturn(result);
		EasyMock.replay(mockDeleteEnvironmentDAO);
		String serviceResult = deleteEnvironmentService.deleteEnvironment(envID);
		assertEquals(serviceResult, "success");
	}
	
	@Test
	public void testDeleteEnvironmentServiceFail() throws Exception {
		String envID = "03278b4a-b2ee-4f2c-b271-80502245b04";
		String result = null;
		EasyMock.expect(
				mockDeleteEnvironmentDAO.deleteEnvironmentRecord(envID))
				.andReturn(result);
		EasyMock.replay(mockDeleteEnvironmentDAO);
		String serviceResult = deleteEnvironmentService.deleteEnvironment(envID);
		assertEquals(serviceResult, null);
	}
}