/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Conor Geary
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class TaskTest {
	Task t1, t2, t3;
	
	@Before
	public void setup(){
		t1  = new Task("Task 1", "Desc1");
		t2  = new Task("Task 2", "Desc2");
		t3  = new Task("Task 3", "Desc3");
	}
	
	@Test
	public void testTaskIsCompleted(){
		assertFalse(t1.isCompleted());
		assertFalse(t2.isCompleted());
		assertFalse(t3.isCompleted());
	}
	
	@Test
	public void testTaskSetCompleted(){
		t1.setCompleted(true);
		t2.setCompleted(false);
		assertTrue(t1.isCompleted());
		assertFalse(t2.isCompleted());
		assertFalse(t3.isCompleted());
	}
	
	@Test
	public void testTaskGetName(){
		assertEquals("Task 1", t1.getName());
		assertEquals("Task 2", t2.getName());
	}
	
	@Test
	public void testTaskSetName(){
		t1.setName("New Name");
		t2.setName("Some Name");
		assertEquals("New Name", t1.getName());
		assertEquals("Some Name", t2.getName());
	}
	
	@Test
	public void testTaskGetDescription(){
		assertEquals("Desc1", t1.getDescription());
		assertEquals("Desc2", t2.getDescription());
	}
	
	@Test
	public void testTaskSetDescription(){
		t1.setDescription("New Desc");
		t2.setDescription("Some Desc");
		assertEquals("New Desc", t1.getDescription());
		assertEquals("Some Desc", t2.getDescription());
	}
}
