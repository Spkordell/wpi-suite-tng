/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *		Alex Gorowara
 ********************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.toolbar;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;

/**
 * Action invoked upon use of the Create Requirement key
 * Heavily adapted from CreateDefectAction in the DefectTracker module
 * 
 * Action that calls {@link MainTabController#addCreateHelpPanelTab()}, default mnemonic key is C. 
 * 
 * @author Alex Gorowara 
 */
@SuppressWarnings("serial")
public class CreateHelpPanelAction extends AbstractAction {

	private final MainTabController controller;
	
	/**
	 * Create a CreateHelpPanelAction
	 * @param controller When the action is performed, controller.addCreateDefectTab() is called
	 */
	public CreateHelpPanelAction(MainTabController controller) {
		super("User Manual");
		this.controller = controller;
		putValue(MNEMONIC_KEY, KeyEvent.VK_F1);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {		
		File input = new File("index.html"); //Html file is our input
		
		//Create a desktop type in order to launch the user's default browser
		 Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) { //If desktop was created and a browser is supported
		        try {
		            desktop.open(input); //Convert link to identifier and launch default browser
		        } catch (Exception f) {
		        	System.out.println("Error launching browser!");
		            f.printStackTrace();
		        }
		    }
	}

}
