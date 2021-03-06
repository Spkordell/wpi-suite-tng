/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Nick Massa, Matt Costi
 *    
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.task;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * Action that calls
 * {@link edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.task.SaveTaskController#saveTask(int[])}
 * 
 * @author Nick Massa, Matt Costi
 */
@SuppressWarnings ("serial")
public class SaveTaskAction extends AbstractAction {
	
	private final SaveTaskController controller;
	private final int[] selectedRows;
	
	/**
	 * Construct the action
	 * 
	 * @param controller
	 *            the controller to trigger
	 */
	public SaveTaskAction(final SaveTaskController controller) {
		super("Save");
		this.controller = controller;
		selectedRows = new int[0];
	}
	
	/**
	 * Construct the action
	 * 
	 * @param controller
	 *            the controller to trigger
	 * @param selectedRows
	 *            object array of all of the tasks
	 */
	public SaveTaskAction(final SaveTaskController controller,
			final int[] selectedRows) {
		super("Save");
		this.controller = controller;
		this.selectedRows = selectedRows;
	}
	
	/**
	 * {@inheritdoc}
	 */
	@Override
	public void actionPerformed(final ActionEvent e) {
		controller.saveTask(selectedRows);
	}
	
}
