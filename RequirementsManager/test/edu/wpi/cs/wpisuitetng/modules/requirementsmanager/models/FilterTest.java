/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 				Kyle Burns and Maddie Burris
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.FilterField;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.FilterOperation;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Priority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Type;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IterationDatabase;

public class FilterTest {
	
	Filter f1;
	Filter f2;
	Filter f3;
	Filter f4;
	Filter f5;
	Filter f6;
	Filter f7;
	Filter f8;
	Filter f9;
	Filter f10;
	Filter f11;
	Filter f12;
	Filter f13;
	Filter f14;
	Filter f15;
	Filter f16;
	Filter f17;
	Filter f18;
	Filter f19;
	Filter f20;
	Filter f21;
	Filter f22;
	Filter f23;
	Filter f24;
	Filter f25;
	Filter f26;
	Filter f27;
	Filter f28;
	Filter f29;
	Filter f30;
	Filter f31;
	Filter f32;
	Filter f33;
	Filter f34;
	Filter f35;
	Filter f36;
	Filter f37;
	Filter f38;
	Filter f39;
	Filter f40;
	FilterField name;
	
	User u1 = new User("user", "user1", "password", 0);
	User u2 = new User("user", "user2", "password", 1);
	Requirement r1;
	Requirement r2;
	Requirement r3;
	Requirement r4;
	Iteration i1, i2, i3, i4, i5;
	
	String name1 = "name1";
	String name2 = "name2";
	String releaseNum = "v1";
	String description;
	Type type;
	Status status;
	Priority priority;
	String Iname;
	Date startDate;
	Date endDate;
	int iterationID1 = 0;
	int iterationID2 = 1;
	int iterationID3 = 2;
	private final int estimate1 = 1;
	private final int estimate2 = 2;
	private final int estimate3 = 3;
	private final int effort1 = 1;
	private final int effort2 = 2;
	private final int effort3 = 3;
	private final List<Integer> reqs = new ArrayList<Integer>();
	private final List<String> assignees = new ArrayList<String>();
	private final List<Integer> subRequirements = new ArrayList<Integer>();
	private final List<Integer> pUID = new ArrayList<Integer>();
	private final List<Note> notes = new ArrayList<Note>();
	private final List<Task> tasks = new ArrayList<Task>();
	
	@Test
	public void breakCheckEnum() {
		// fieldOperation = something other than equal or not equal
		f37 = new Filter(u1.getUsername(), FilterField.TYPE,
				FilterOperation.CONTAINS, 1);
		Assert.assertFalse(f37.shouldFilter(r1));
	}
	
	@Test
	public void breakCheckInteger() {
		f39 = new Filter(u1.getUsername(), FilterField.ESTIMATE,
				FilterOperation.CONTAINS, 1);
		Assert.assertFalse(f39.shouldFilter(r1));
	}
	
	@Test
	public void breakCheckIteration() {
		// use contains for field operation
		f38 = new Filter(u1.getUsername(), FilterField.ITERATION,
				FilterOperation.CONTAINS, 1);
		Assert.assertFalse(f38.shouldFilter(r1));
	}
	
	/** Incorrect Filters for "Break" testing **/
	@Test
	public void breakCheckString() {
		// fieldOperation = something other than equal, not equal, contains,
		// starts with
		f36 = new Filter(u1.getUsername(), FilterField.NAME,
				FilterOperation.GREATER_THAN, 1);
		Assert.assertFalse(f36.shouldFilter(r1));
	}
	
	@Test
	public void FilterFieldtest() {
		Assert.assertEquals(name, FilterField.getFromString("name"));
		
	}
	
	@Before
	public void setUp() {
		i1 = new Iteration("Iteration 1", new Date(2013, 4, 18), new Date(2013,
				4, 30), 1);
		i2 = new Iteration("Iteration 2", new Date(2013, 4, 11), new Date(2013,
				4, 17), 2);
		i3 = new Iteration("Iteration 3", new Date(2013, 5, 1), new Date(2013,
				5, 10), 3);
		i4 = new Iteration("Iteration 4", new Date(2013, 4, 21), new Date(2013,
				4, 28), 4);
		i5 = new Iteration(Iname, startDate, endDate, iterationID1, reqs);
		IterationDatabase.getInstance().add(i1);
		IterationDatabase.getInstance().add(i2);
		IterationDatabase.getInstance().add(i3);
		IterationDatabase.getInstance().add(i4);
		IterationDatabase.getInstance().add(i5);
		
		f1 = new Filter(u1.getUsername());
		f2 = new Filter(u2.getUsername());
		
		/** Filters free for use **/
		// f22 free
		// f23 free
		
		r1 = new Requirement(name1, description, releaseNum, Type.USER_STORY,
				subRequirements, notes, iterationID1, estimate1, effort1,
				assignees, pUID, tasks);
		r2 = new Requirement(name2, description, "v2", Type.THEME,
				subRequirements, notes, iterationID2, estimate2, effort2,
				assignees, pUID, tasks);
		r3 = new Requirement("example", description, "3.0", Type.EPIC,
				subRequirements, notes, iterationID3, estimate3, effort3,
				assignees, pUID, tasks);
		
		r1.setPriority(Priority.HIGH);
		r2.setPriority(Priority.MEDIUM);
		r3.setPriority(Priority.LOW);
		
		r1.setStatus(Status.NEW);
		r2.setStatus(Status.BLANK);
		r3.setStatus(Status.IN_PROGRESS);
	}
	
	@Test
	public void shouldFilterEffortEqual() {
		f34 = new Filter(u1.getUsername(), FilterField.EFFORT,
				FilterOperation.EQUAL, 1);
		Assert.assertTrue(f34.shouldFilter(r1));
	}
	
	/** Effort Filters **/
	@Test
	public void shouldFilterEffortGreater() {
		f30 = new Filter(u1.getUsername(), FilterField.EFFORT,
				FilterOperation.GREATER_THAN, 1);
		Assert.assertTrue(f30.shouldFilter(r2));
		Assert.assertFalse(f30.shouldFilter(r1));
	}
	
	@Test
	public void shouldFilterEffortGreaterEqual() {
		f31 = new Filter(u1.getUsername(), FilterField.EFFORT,
				FilterOperation.GREATER_THAN_EQUAL, 2);
		Assert.assertTrue(f31.shouldFilter(r2));
		Assert.assertTrue(f31.shouldFilter(r3));
		Assert.assertFalse(f31.shouldFilter(r1));
	}
	
	@Test
	public void shouldFilterEffortLess() {
		f32 = new Filter(u1.getUsername(), FilterField.EFFORT,
				FilterOperation.LESS_THAN, 2);
		Assert.assertTrue(f32.shouldFilter(r1));
		Assert.assertFalse(f32.shouldFilter(r3));
	}
	
	@Test
	public void shouldFilterEffortLessEqual() {
		f33 = new Filter(u1.getUsername(), FilterField.EFFORT,
				FilterOperation.LESS_THAN_EQUAL, 2);
		Assert.assertTrue(f33.shouldFilter(r1));
		Assert.assertTrue(f33.shouldFilter(r2));
		Assert.assertFalse(f33.shouldFilter(r3));
	}
	
	@Test
	public void shouldFilterEffortNotEqual() {
		f35 = new Filter(u1.getUsername(), FilterField.EFFORT,
				FilterOperation.NOT_EQUAL, 1);
		Assert.assertTrue(f35.shouldFilter(r2));
		Assert.assertFalse(f35.shouldFilter(r1));
	}
	
	@Test
	public void shouldFilterEstimateEqual() {
		f25 = new Filter(u1.getUsername(), FilterField.ESTIMATE,
				FilterOperation.EQUAL, 1);
		Assert.assertTrue(f25.shouldFilter(r1));
		Assert.assertFalse(f25.shouldFilter(r2));
	}
	
	/** Estimate Filters **/
	@Test
	public void shouldFilterEstimateGreater() {
		f24 = new Filter(u1.getUsername(), FilterField.ESTIMATE,
				FilterOperation.GREATER_THAN, 2);
		Assert.assertTrue(f24.shouldFilter(r3));
		Assert.assertFalse(f24.shouldFilter(r1));
	}
	
	@Test
	public void shouldFilterEstimateGreaterEqual() {
		f29 = new Filter(u1.getUsername(), FilterField.ESTIMATE,
				FilterOperation.GREATER_THAN_EQUAL, 2);
		Assert.assertTrue(f29.shouldFilter(r2));
		Assert.assertTrue(f29.shouldFilter(r3));
		Assert.assertFalse(f29.shouldFilter(r1));
	}
	
	@Test
	public void shouldFilterEstimateLess() {
		f27 = new Filter(u1.getUsername(), FilterField.ESTIMATE,
				FilterOperation.LESS_THAN, 2);
		Assert.assertTrue(f27.shouldFilter(r1));
		Assert.assertFalse(f27.shouldFilter(r3));
	}
	
	@Test
	public void shouldFilterEstimateLessEqual() {
		f28 = new Filter(u1.getUsername(), FilterField.ESTIMATE,
				FilterOperation.LESS_THAN_EQUAL, 2);
		Assert.assertTrue(f28.shouldFilter(r1));
		Assert.assertTrue(f28.shouldFilter(r2));
		Assert.assertFalse(f28.shouldFilter(r3));
	}
	
	@Test
	public void shouldFilterEstimateNotEqual() {
		f26 = new Filter(u1.getUsername(), FilterField.ESTIMATE,
				FilterOperation.NOT_EQUAL, 1);
		Assert.assertTrue(f26.shouldFilter(r2));
		Assert.assertFalse(f26.shouldFilter(r1));
	}
	
	/*******
	 * Iteration tests below .... Iteration testing is confusing and will be
	 * worked on later
	 **********/
	
	// f18 = new Filter(u1.getUsername(), FilterField.ITERATION,
	// FilterOperation.OCCURS_BEFORE, startDate);
	// f19 = new Filter(u1.getUsername(), FilterField.ITERATION,
	// FilterOperation.OCCURS_BETWEEN, 2);
	// f20 = new Filter(u1.getUsername(), FilterField.ITERATION,
	// FilterOperation.OCCURS_AFTER, endDate);
	
	@Test
	public void shouldFilterIterationEqual() {
		f17 = new Filter(u1.getUsername(), FilterField.ITERATION,
				FilterOperation.EQUAL, 2);
		Assert.assertTrue(f17.shouldFilter(r3));
		Assert.assertFalse(f17.shouldFilter(r2));
	}
	
	@Test
	public void shouldFilterIterationNotEqual2() {
		f21 = new Filter(u1.getUsername(), FilterField.ITERATION,
				FilterOperation.NOT_EQUAL, 0);
		Assert.assertTrue(f21.shouldFilter(r3));
		Assert.assertFalse(f21.shouldFilter(r1));
	}
	
	@Test
	public void shouldFilterNameContains() {
		f5 = new Filter(u1.getUsername(), FilterField.NAME,
				FilterOperation.CONTAINS, "2");
		Assert.assertTrue(f5.shouldFilter(r2));
		Assert.assertFalse(f5.shouldFilter(r1));
	}
	
	/** Name Filters **/
	@Test
	public void shouldFilterNameEqual() {
		f3 = new Filter(u1.getUsername(), FilterField.NAME,
				FilterOperation.EQUAL, "name1");
		Assert.assertTrue(f3.shouldFilter(r1));
		Assert.assertFalse(f3.shouldFilter(r2));
	}
	
	@Test
	public void shouldFilterNameNotEqual() {
		f4 = new Filter(u1.getUsername(), FilterField.NAME,
				FilterOperation.NOT_EQUAL, "name2");
		Assert.assertTrue(f4.shouldFilter(r1));
		Assert.assertFalse(f4.shouldFilter(r2));
	}
	
	@Test
	public void shouldFilterNameStartsWith() {
		f6 = new Filter(u1.getUsername(), FilterField.NAME,
				FilterOperation.STARTS_WITH, "e");
		Assert.assertTrue(f6.shouldFilter(r3));
		Assert.assertFalse(f6.shouldFilter(r1));
	}
	
	/** Priority Filters **/
	@Test
	public void shouldFilterPriorityEqual() {
		f13 = new Filter(u1.getUsername(), FilterField.PRIORITY,
				FilterOperation.EQUAL, Priority.HIGH);
		Assert.assertTrue(f13.shouldFilter(r1));
		Assert.assertFalse(f13.shouldFilter(r3));
	}
	
	@Test
	public void shouldFilterPriorityNotEqual() {
		f14 = new Filter(u1.getUsername(), FilterField.PRIORITY,
				FilterOperation.NOT_EQUAL, Priority.HIGH);
		Assert.assertTrue(f14.shouldFilter(r3));
		Assert.assertFalse(f14.shouldFilter(r1));
	}
	
	@Test
	public void shouldFilterReleaseContains() {
		f9 = new Filter(u1.getUsername(), FilterField.RELEASE_NUMBER,
				FilterOperation.CONTAINS, "v");
		Assert.assertTrue(f9.shouldFilter(r1));
		Assert.assertFalse(f9.shouldFilter(r3));
	}
	
	/** Release Number Filters **/
	@Test
	public void shouldFilterReleaseEqual() {
		f7 = new Filter(u1.getUsername(), FilterField.RELEASE_NUMBER,
				FilterOperation.EQUAL, "v1");
		Assert.assertTrue(f7.shouldFilter(r1));
		Assert.assertFalse(f7.shouldFilter(r2));
	}
	
	@Test
	public void shouldFilterReleaseNotEqual() {
		f8 = new Filter(u1.getUsername(), FilterField.RELEASE_NUMBER,
				FilterOperation.NOT_EQUAL, "v1");
		Assert.assertTrue(f8.shouldFilter(r2));
		Assert.assertFalse(f8.shouldFilter(r1));
	}
	
	@Test
	public void shouldFilterReleaseStartsWith() {
		f10 = new Filter(u1.getUsername(), FilterField.RELEASE_NUMBER,
				FilterOperation.STARTS_WITH, "v");
		Assert.assertTrue(f10.shouldFilter(r1));
		Assert.assertFalse(f10.shouldFilter(r3));
	}
	
	/** Status Filters **/
	@Test
	public void shouldFilterStatusEqual() {
		f15 = new Filter(u1.getUsername(), FilterField.STATUS,
				FilterOperation.EQUAL, Status.NEW);
		Assert.assertTrue(f15.shouldFilter(r1));
		Assert.assertFalse(f15.shouldFilter(r3));
	}
	/***************************** End Iteration Tests ***************************************/
	
	@Test
	public void shouldFilterStatusNotEqual() {
		f16 = new Filter(u1.getUsername(), FilterField.STATUS,
				FilterOperation.NOT_EQUAL, Status.IN_PROGRESS);
		Assert.assertTrue(f16.shouldFilter(r1));
		Assert.assertFalse(f16.shouldFilter(r3));
	}
	
	/** Type Filters **/
	@Test
	public void shouldFilterTypeEqual() {
		f11 = new Filter(u1.getUsername(), FilterField.TYPE,
				FilterOperation.EQUAL, Type.USER_STORY);
		Assert.assertTrue(f11.shouldFilter(r1));
		Assert.assertFalse(f11.shouldFilter(r3));
	}
	
	@Test
	public void shouldFilterTypeNotEqual() {
		f12 = new Filter(u1.getUsername(), FilterField.TYPE,
				FilterOperation.NOT_EQUAL, Type.USER_STORY);
		Assert.assertTrue(f12.shouldFilter(r3));
		Assert.assertFalse(f12.shouldFilter(r1));
	}
	
	@Test
	public void testActive() {
		Assert.assertTrue(f1.isActive());
		f1.setActive(false);
		Assert.assertFalse(f1.isActive());
		Assert.assertFalse(f1.getActive());
	}
	
	@Test
	public void testfromJSON() {
		final String json = f1.toJSON();
		final Filter newFilter = Filter.fromJSON(json);
		newFilter.setCreator(u2.getUsername());
		Assert.assertEquals("user2", newFilter.getCreator());
		newFilter.setField(FilterField.ITERATION);
		Assert.assertEquals(FilterField.ITERATION, newFilter.getField());
		newFilter.setOperation(FilterOperation.OCCURS_AFTER);
		Assert.assertEquals(FilterOperation.OCCURS_AFTER,
				newFilter.getOperation());
		newFilter.setValue(new String("test"));
		Assert.assertEquals("test", newFilter.getValue());
	}
	
	@Test
	public void testfromJSONArray() {
		final Gson parser = new Gson();
		final Filter[] array = { f1 };
		final String json = parser.toJson(array, Filter[].class);
		final Filter[] newFilterArray = Filter.fromJSONArray(json);
		Assert.assertEquals(u1.getUsername(), newFilterArray[0].getCreator());
		Assert.assertEquals(FilterField.NAME, newFilterArray[0].getField());
		Assert.assertEquals(FilterOperation.EQUAL,
				newFilterArray[0].getOperation());
	}
	
	@Test
	public void testGetStringValue() {
		// System.out.println(f17.getStringValue());
	}
	
	@Test
	public void testIdentify() {
		Assert.assertTrue(f1.identify(f1));
		f2.setId(1);
		Assert.assertFalse(f1.identify(f2));
		Assert.assertFalse(f1.identify(new String()));
	}
	
	@Test
	public void testShouldFilterNonActive() {
		f2.setActive(false);
		Assert.assertFalse(f2.shouldFilter(r1));
	}
	
	@Test
	public void testToStringsetJsonValueandgetJsonValue() {
		f1.setJsonValue("string");
		final String JsonVal = f1.getJsonValue();
		Assert.assertEquals(f1.toString(), "[Filter ID:" + -1 + " Field:"
				+ FilterField.NAME + " Operation:" + FilterOperation.EQUAL
				+ " Value: " + JsonVal + " Active: " + true + "]");
	}	
}
