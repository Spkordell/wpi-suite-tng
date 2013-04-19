/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 	Nick
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.subrequirements.subreqpopupmenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;

/**
 * Popup menu when a user doesnt click on a list item, and only one lsit item is
 * selected
 * 
 * @author Nick
 * 
 */

public class SubReqAnywherePopupMenu extends JPopupMenu implements ActionListener {

	/** The Tabcontroller used to open tabs */
	private MainTabController tabController;

	private JMenuItem itemCreateRequirement;

	/**
	 * Creates a new instance of anywhere popup menu
	 * 
	 * @param tabController
	 *            tab controller ot open tabs in
	 */

	public SubReqAnywherePopupMenu(MainTabController tabController) {
		this.tabController = tabController;

		itemCreateRequirement = new JMenuItem("New Requirement");

		itemCreateRequirement.addActionListener(this);

		add(itemCreateRequirement);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
			tabController.addCreateRequirementTab();

	}
}
