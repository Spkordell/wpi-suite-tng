/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 		
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.atest;

import java.awt.Color;

import javax.swing.JList;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.SaveRequirementController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.ATest;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;

/**
 * This controller handles saving requirement aTests to the server
 * 
 * @author Nick Massa, Matt Costi, Steve Kordell
 */
public class SaveATestController {

	private final MakeATestPanel view;
	private final Requirement model;
	private final DetailPanel parentView;
	private final JList tests;

	/**
	 * Construct the controller
	 * 
	 * @param view
	 *            the MakeaTestPanel containing the aTest field
	 * @param model
	 *            the requirement to which aTests are being added
	 * @param parentView
	 *            the DetailPanel displaying the current requirement
	 */
	public SaveATestController(MakeATestPanel view, Requirement model,
			DetailPanel parentView, JList tests) {
		this.view = view;
		this.model = model;
		this.parentView = parentView;
		this.tests = tests;
	}

	/**
	 * Save a aTest to the server
	 */
	public void saveaTest(Object[] tests) {
		final String testText = view.getaTestField().getText();
		final String testName = view.getaTestName().getText();
		if (tests == null) { // Creating a aTest!
			System.out.println("aTestS WAS NULL, ISSUE");
		} else if (tests.length < 1) {
			//aTest must have a name and description of at least one character
			if (testText.length() > 0 && testName.length() > 0) { 
				ATest tempTest = new ATest(testName, testText);
				if ((view.getUserAssigned().getSelectedItem() == ""))
					tempTest.setAssignedUser(null);
				else
					tempTest.setAssignedUser((String) view.getUserAssigned()
							.getSelectedItem());
				tempTest.setId(this.model.getTests().size() + 1);
				this.model.addTest(tempTest);
				parentView.getTestList().addElement(tempTest);
				view.getaTestName().setText("");
				view.getaTestField().setText("");
				view.getaTestField().requestFocusInWindow();
				// We want to save the aTest to the server immediately, but only
				// if the requirement hasn't been just created
				if (model.getName().length() > 0) { 
					// Save to requirement!
					SaveRequirementController controller = new SaveRequirementController(
							this.parentView);
					controller.SaveRequirement(model, false);
				}
			}
		} else {
			
			// Modifying aTests
			for (Object aTest : tests) { 
				if (tests.length == 1) { 
					// If only one is selected, edit the fields
					if (testText.length() > 0 && testName.length() > 0) {
						((ATest) aTest).setName(view.getaTestName().getText());
						((ATest) aTest).setDescription(view.getaTestField()
								.getText());
					}
					if ((view.getUserAssigned().getSelectedItem() == ""))
						((ATest) aTest).setAssignedUser(null);
					else
						((ATest) aTest).setAssignedUser((String) view
								.getUserAssigned().getSelectedItem());
				}
				// Check the completion status on the aTests
				//((ATest) aTest).setStatus(view.getaTestComplete().isSelected()); //TODO set status
			}
			
			// Save to requirement!
			if (model.getName().length() > 0) { 
				SaveRequirementController controller = new SaveRequirementController(
						this.parentView);
				controller.SaveRequirement(model, false);
			}
			view.getaTestName().setText("");
			view.getaTestField().setText("");
			view.getaTestField().requestFocusInWindow();
		}
		
		this.tests.clearSelection();
		view.getaTestStatus()
				.setText(
						"No aTests selected. Fill name and description to create a new one.");
		view.getaTestComplete().setEnabled(false);
		view.getaTestComplete().setSelected(false);
		view.getUserAssigned().setEnabled(true);
		view.getaTestField().setEnabled(true);
		view.getaTestName().setEnabled(true);
		view.getaTestField().setText("");
		view.getaTestName().setText("");
		view.getaTestField().setBackground(Color.white);
		view.getaTestName().setBackground(Color.white);
		view.getAddaTest().setEnabled(false);

	}
}
