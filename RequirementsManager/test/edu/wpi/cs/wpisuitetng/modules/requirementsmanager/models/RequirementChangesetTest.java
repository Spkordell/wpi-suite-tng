package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.logging.RequirementChangeset;

public class RequirementChangesetTest {
	
	User user;
	Session session;
	
	@Before
	public void setup() {
		user = new User("User's Name", "username", "swordfish", 1);
		session = new Session(user, "SSID");
	}
	
	@Test
	public void testCreationLog() {
		final Requirement r = new Requirement();
		r.logCreation(session);
		final RequirementChangeset creationChangeset = r.getLogs().get(0);
		Assert.assertEquals(creationChangeset.getContent(),
				"Created Requirement<br></html>");
	}
	
	@Test
	public void testEmptyContent() {
		final RequirementChangeset rc = new RequirementChangeset();
		Assert.assertEquals(rc.getContent(), "");
	}
	
	@Test
	public void testTitleRetrieval() {
		final RequirementChangeset rc = new RequirementChangeset(user);
		Assert.assertTrue(rc.getTitle().contains(("User's Name")));
	}
	
}
