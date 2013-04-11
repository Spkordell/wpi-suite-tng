/*******************************************************************************
* Copyright (c) 2013 -- WPI Suite: Team Swagasarus
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* * Chris Keane
* * Maddie Burris
*******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.charts;

import java.util.List;

import org.jfree.chart.JFreeChart;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;

/**
* class to contain data on how many requirements are assigned to each iteration
* note that user assignees here are stored as strings, as they are in Requirements themselves
*
*/


public class EstimateRequirementStatistics extends AbstractRequirementStatistics {

	/* (non-Javadoc)
	* @see edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.charts.IRequirementStatistics#update()
	*/
	@Override
	public void update(){
	
		List<Requirement> requirements = RequirementDatabase.getInstance().getAllRequirements();	// refresh list of requirements TODO: is there a better way to do this?
		
		// TODO: replace with a method to get all users, record them as zero-counts in the map, and then simply work through and increment
		// for each requirement
		for(Requirement requirement: requirements){
			
			data.put(requirement.getName(), requirement.getEstimate());
		
		}
	
	}
	
	public JFreeChart buildPieChart(){
		return this.buildPieChart("Requirements by Estimate");
	}
	
	public JFreeChart buildBarChart(){
		JFreeChart barChart = this.buildBarChart("Estimates by Requirements", "Requirements", "Estimate");
		return barChart;
	}

}