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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.subrequirements;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.SaveRequirementController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.note.MakeNotePanel;

public class AssignParentController {
	private final SubRequirementPanel view;
	private final Requirement model;
	private final DetailPanel parentView;

	/**
	 * Construct the controller
	 * 
	 * @param subRequirementPanel
	 *            the panel holding this
	 * @param model
	 *            the requirement
	 * @param parentView
	 *            the DetailPanel
	 */
	public AssignParentController(SubRequirementPanel subRequirementPanel, Requirement model,
			DetailPanel parentView) {
		this.view = subRequirementPanel;
		this.model = model;
		this.parentView = parentView;
	}

	/**
	 * Save a note to the server
	 */
	public void saveParent() {
		String selectedIndex = (String) view.getList().getSelectedValue();
		Requirement anReq = RequirementDatabase.getInstance().getRequirement(selectedIndex);

		Integer modelID = new Integer(model.getrUID());
		Integer anReqID = new Integer (anReq.getrUID());
		
		model.addPUID(anReqID);
		anReq.addSubRequirement(modelID);
		SaveRequirementController controller = new SaveRequirementController(this.parentView);
		controller.SaveRequirement(model, false);
		controller = new SaveRequirementController(new SaveOtherRequirement());
		controller.SaveRequirement(anReq, false);
		
		view.refreshSubReqPanel();
		view.refreshReqPanel();
	}

	}