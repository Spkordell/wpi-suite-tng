/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.validators;

import java.util.HashSet;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.NotImplementedException;
import edu.wpi.cs.wpisuitetng.exceptions.UnauthorizedException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.Role;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.MockData;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.entititymanagers.RequirementsEntityManager;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;

/**
 * @author Jason Whitehouse
 * 
 */
public class RequirementsEntityManagerTest {
	Data db;
	RequirementsEntityManager manager;
	Project testProject;
	Project otherProject;
	Session defaultSession;
	String mockSsid;
	User testUser;
	User existingUser;
	Session adminSession;

	Requirement goodRequirement;
	Requirement oldRequirement;
	Requirement otherRequirement;


	@Before
	public void init() throws Exception {
		User admin = new User("admin", "admin", "1234", 27);
		admin.setRole(Role.ADMIN);
		testProject = new Project("test", "1");
		otherProject = new Project("other", "2");
		mockSsid = "abc123";
		adminSession = new Session(admin, testProject, mockSsid);
		
		existingUser = new User("joe", "joe", "1234", 2);
		oldRequirement = new Requirement();
		oldRequirement.setrUID(1);
		oldRequirement.setName("Old Requirement");
		oldRequirement.setDescription("So Old...");

		
		otherRequirement = new Requirement();
		otherRequirement.setrUID(2);
		otherRequirement.setName("In another project");
		otherRequirement.setDescription("in another project, still");
		
		goodRequirement = new Requirement();
		goodRequirement.setName("Name");
		goodRequirement.setDescription("A quality description");
		
		defaultSession = new Session(existingUser, testProject, mockSsid);
		
		db = new MockData(new HashSet<Object>());
		db.save(oldRequirement, testProject);
		db.save(existingUser);
		db.save(otherRequirement, otherProject);
		db.save(admin);
		manager = new RequirementsEntityManager(db);
		
		/*User admin = new User("admin", "admin", "1234", 27);
		admin.setRole(Role.ADMIN);

		testUser = new User(null, "testUser", null, -1);
		existingUser = new User("newUser", "newUser", "1234", 2);
		testProject = new Project("test", "1");
		otherProject = new Project("other", "2");
		mockSsid = "abc123";
		defaultSession = new Session(existingUser, testProject, mockSsid);
		adminSession = new Session(admin, testProject, mockSsid);
		
		manager = new RequirementsEntityManager(db);
		
		goodRequirement = new Requirement();
		goodRequirement.setName("Name");
		goodRequirement.setDescription("A quality description");
		
		oldRequirement = new Requirement();
		oldRequirement.setrUID(1);
		oldRequirement.setName("Old Requirement");
		oldRequirement.setDescription("So Old...");
		
		otherRequirement = new Requirement();
		oldRequirement.setrUID(2);
		oldRequirement.setName("In other project");
		oldRequirement.setDescription("This is in another project");
		
		db = new MockData(new HashSet<Object>());
		db.save(oldRequirement, testProject);
		db.save(existingUser);
		db.save(otherRequirement, otherProject);
		db.save(admin);
		db.save(testUser);*/


	}
	
	@Test
	public void testMakeEntity() throws WPISuiteException {
		Requirement created = manager.makeEntity(defaultSession, goodRequirement.toJSON());
		assertEquals("Name", created.getName());
		assertSame(db.retrieve(Requirement.class, "rUID", 3).get(0), created);
	}
	
	@Test(expected=BadRequestException.class)
	public void testMakeBadEntity() throws WPISuiteException {
		Requirement r = new Requirement();
		// make sure it's being passed through the validator
		manager.makeEntity(defaultSession, r.toJSON());
	}
	
	@Test
	public void testGetEntity() throws NotFoundException {
		Requirement[] gotten = manager.getEntity(defaultSession, "1");
		assertSame(oldRequirement, gotten[0]);
	}
	
	@Test(expected=NotFoundException.class)
	public void testGetBadId() throws NotFoundException {
		manager.getEntity(defaultSession, "-1");
	}
	
	@Test(expected=NotFoundException.class)
	public void testGetMissingEntity() throws NotFoundException {
		manager.getEntity(defaultSession, "9");
	}
	
	@Test
	public void testGetAll() {
		Requirement[] gotten = manager.getAll(defaultSession);
		assertEquals(1, gotten.length);
		assertSame(oldRequirement, gotten[0]);
	}
	
	@Test
	public void testDelete() throws WPISuiteException {
		assertSame(oldRequirement, db.retrieve(Requirement.class, "rUID", 1).get(0));
		assertTrue(manager.deleteEntity(adminSession, "1"));
		assertEquals(0, db.retrieve(Requirement.class, "id", 1).size());
	}
	
	@Test(expected=NotFoundException.class)
	public void testDeleteMissing() throws WPISuiteException {
		manager.deleteEntity(adminSession, "4534");
	}
	
	@Test(expected=NotFoundException.class)
	public void testDeleteFromOtherProject() throws WPISuiteException {
		manager.deleteEntity(adminSession, Integer.toString(otherRequirement.getrUID()));
	}
	
	@Test(expected=UnauthorizedException.class)
	public void testDeleteNotAllowed() throws WPISuiteException {
		manager.deleteEntity(defaultSession, Integer.toString(oldRequirement.getrUID()));
	}
	
	@Test
	public void testDeleteAll() throws WPISuiteException {
		Requirement anotherRequirement = new Requirement();
		anotherRequirement.setrUID(4);
		anotherRequirement.setName("aName");
		anotherRequirement.setDescription("Description");
		manager.makeEntity(defaultSession, anotherRequirement.toJSON());
		assertEquals(2, db.retrieveAll(new Requirement(), testProject).size());
		manager.deleteAll(adminSession);
		assertEquals(0, db.retrieveAll(new Requirement(), testProject).size());
		// otherDefect should still be around
		assertEquals(1, db.retrieveAll(new Requirement(), otherProject).size());
	}
	
	@Test(expected=UnauthorizedException.class)
	public void testDeleteAllNotAllowed() throws WPISuiteException {
		manager.deleteAll(defaultSession);
	}
	
	@Test
	public void testDeleteAllWhenEmpty() throws WPISuiteException {
		manager.deleteAll(adminSession);
		manager.deleteAll(adminSession);
		// no exceptions
	}
	
	@Test
	public void testCount() {
		assertEquals(2, manager.Count());
	}
	
	@Test
	public void testSave() throws WPISuiteException {
		Requirement newRequirement = new Requirement();
		newRequirement.setrUID(3);
		manager.save(defaultSession, newRequirement);
		assertSame(newRequirement, db.retrieve(Requirement.class, "rUID", 3).get(0));
		assertSame(testProject, newRequirement.getProject());
	}
	
	/* TODO: Test updating once implemented
	@SuppressWarnings("unchecked")
	@Test
	public void testUpdate() throws WPISuiteException {
		Defect updated = manager.update(defaultSession, goodUpdatedDefect.toJSON());
		assertSame(existingDefect, updated);
		assertEquals(goodUpdatedDefect.getTitle(), updated.getTitle()); // make sure ModelMapper is used
		assertEquals(1, updated.getEvents().size());
		
		DefectChangeset changeset = (DefectChangeset) updated.getEvents().get(0);
		assertSame(existingUser, changeset.getUser());
		assertEquals(updated.getLastModifiedDate(), changeset.getDate());
		
		Map<String, FieldChange<?>> changes = changeset.getChanges();
		// these fields shouldn't be recorded in the changeset
		// creator was different in goodUpdatedDefect, but should be ignored
		assertFalse(changes.keySet().containsAll(Arrays.asList("events", "lastModifiedDate", "creator")));
		
		FieldChange<String> titleChange = (FieldChange<String>) changes.get("title");
		assertEquals("An existing defect", titleChange.getOldValue());
		assertEquals("A changed title", titleChange.getNewValue());
		
		// make sure events are being saved explicitly to get around a bug
		// TODO: remove this when said bug is fixed
		assertSame(updated.getEvents(), db.retrieveAll(new ArrayList<DefectEvent>()).get(0));
	}
	
	@Test(expected=BadRequestException.class)
	public void testBadUpdate() throws WPISuiteException {
		goodUpdatedDefect.setTitle("");
		manager.update(defaultSession, goodUpdatedDefect.toJSON());
	}
	
	@Test
	public void testNoUpdate() throws WPISuiteException {
		Date origLastModified = existingDefect.getLastModifiedDate();
		Defect updated = manager.update(defaultSession, existingDefect.toJSON());
		assertSame(existingDefect, updated);
		// there were no changes - make sure lastModifiedDate is same, no new events
		assertEquals(origLastModified, updated.getLastModifiedDate());
		assertEquals(0, updated.getEvents().size());
	}
	
	@Test
	public void testProjectChangeIgnored() throws WPISuiteException {
		Defect existingDefectCopy = new Defect(1, "An existing defect", "", existingUser);
		existingDefectCopy.setProject(otherProject);
		Defect updated = manager.update(defaultSession, existingDefectCopy.toJSON());
		assertEquals(0, updated.getEvents().size());
		assertSame(testProject, updated.getProject());
	}	*/

	
	@Test(expected=NotImplementedException.class)
	public void testAdvancedGet() throws WPISuiteException {
		manager.advancedGet(defaultSession, new String[0]);
	}
	
	@Test(expected=NotImplementedException.class)
	public void testAdvancedPost() throws WPISuiteException {
		manager.advancedPost(defaultSession, "", "");
	}
	
	@Test(expected=NotImplementedException.class)
	public void testAdvancedPut() throws WPISuiteException {
		manager.advancedPut(defaultSession, new String[0], "");
	}

}