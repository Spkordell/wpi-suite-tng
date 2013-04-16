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

public class RemoveReqController {
	private final SubRequirementPanel view;
	private final Requirement model;
	private final DetailPanel ChildView;

	/**
	 * Construct the controller
	 * 
	 * @param subRequirementPanel
	 *            the panel holding this
	 * @param model
	 *            the requirement
	 * @param ChildView
	 *            the DetailPanel
	 */
	public RemoveReqController(SubRequirementPanel subRequirementPanel, Requirement model,
			DetailPanel ChildView) {
		this.view = subRequirementPanel;
		this.model = model;
		this.ChildView = ChildView;
	}

	/**
	 * Save a note to the server
	 */
	public void saveChild() {
		
		/*Requirement anReq = RequirementDatabase.getInstance().getRequirement(view.getTree().getLastSelectedPathComponent().toString());
		
		if(view.getList().getSelectedValues()!=null){
		//System.out.println("\n" + anReq.getName());
			SaveRequirementController controller = new SaveRequirementController(this.ChildView);
			anReq = RequirementDatabase.getInstance().getRequirement(view.getTree().getLastSelectedPathComponent().toString());
			
			Integer modelID = new Integer(model.getrUID());
			Integer anReqID = new Integer(anReq.getrUID());
			
			//anReq.addPUID(modelID);
			model.addSubRequirement(anReqID);
			controller.SaveRequirement(model, false);
		}*/

	}

	}