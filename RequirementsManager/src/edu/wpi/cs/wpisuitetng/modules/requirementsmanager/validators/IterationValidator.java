package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.validators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.IterationActionMode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.entititymanagers.RequirementsEntityManager;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.entititymanagers.IterationEntityManager;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;

public class IterationValidator {

	public List<ValidationIssue> validate(Session s, Iteration i,
			IterationActionMode mode, Data db) {
		List<ValidationIssue> issues = new ArrayList<ValidationIssue>();

		// If the iteration is null, then we have an error
		if (i == null) {
			issues.add(new ValidationIssue("Iteration cannot be null"));
			return issues;
		}

		// Detect if the iteration has been given a name
		if (i.getName() == null || i.getName() == "" || i.getName().isEmpty()){
			issues.add(new ValidationIssue("Iteration must have a name"));
		}
		
		// If the iteration is being created, then we must have no requirements
		if (mode == IterationActionMode.CREATE) {
			i.setRequirements(new ArrayList<Integer>());
		} 
		else {
			// If we are not creating, make sure that requirements aren't null
			if (i.getRequirements() == null) {
				i.setRequirements(new ArrayList<Integer>());
			} 
			else {
				// Loop through all the requirement ids and make sure that they
				// all exist
				List<Requirement> reqs = Arrays
						.asList(new RequirementsEntityManager(db).getAll(s));
				for (Integer id : i.getRequirements()) {
					boolean detected = false;
					for (Requirement req : reqs) {
						if (req.getrUID() == id) {
							detected = true;
						}
					}
					if (!detected){
						issues.add(new ValidationIssue("Invalid Requirement ID " + id));
					}
				}
			}
		}
		
		// Make sure that the start date is before the end date
		if(!i.validateDate()){
			issues.add(new ValidationIssue("Iteration must start before it ends"));
		}
		
		// Make sure that this iteration does not overlap with any others
		IterationEntityManager manager = new IterationEntityManager(db);
		Iteration[] iterations = new Iteration[0];
		// attempt to retrieve iterations for this session's project
		try{
			iterations = manager.getAll(s);
		}
		catch(WPISuiteException e){
			// TODO: something other than nothing
		}
		for(Iteration itr : iterations){
			// if an iteration overlaps with the one being validated
			if(overlapExists(itr, i)){
				// report the name of both iterations and that they overlap
				issues.add(new ValidationIssue(itr.toString() + " overlaps " + i.toString()));
			}			
		}

		return issues;
	}
	
	private boolean overlapExists(Iteration alpha, Iteration beta){
		
		// if iteration alpha starts before iteration beta, this will be +1
		// if they have the same start date, this will be 0
		// if iteration beta starts before iteration alpha, this will be -1
		int before = beta.getStartDate().compareTo(alpha.getStartDate());
		
		// if one iteration starts before another, it must be entirely before another
		// its start and end dates must be before another's start and end dates
		boolean betaStartAlphaStart = beta.getStartDate().compareTo(alpha.getStartDate()) == before;	// check that beta-start date has the same relation (before or after) to alpha-start date as beta-start date has to alpha-start date (self-evident)
		boolean betaStartAlphaEnd = beta.getStartDate().compareTo(alpha.getEndDate()) == before;	// check that beta-start date has the same relation to alpha-end date
		boolean betaEndAlphaStart = beta.getEndDate().compareTo(alpha.getStartDate()) == before;	// check that beta-end date has the same relation to alpha-start date
		boolean betaEndAlphaEnd = beta.getEndDate().compareTo(alpha.getEndDate()) == before;	// check that beta-end date has the same relation to alpha-end date
		
		// return false (no overlap) if and only if:
		//	if beta starts before alpha starts, all of beta's dates are before alpha's dates
		//	if alpha starts before beta starts, all of alpha's dates are before beta's dates
		return !(betaStartAlphaStart && betaStartAlphaEnd && betaEndAlphaStart && betaEndAlphaEnd);
		
	}

}
