/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 	Mitchell Caisse
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.atest;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MakeATestListener implements KeyListener {


	/** the task panel this listens in */
	private final MakeATestPanel makeATestPanel;

	/**
	 * Creates a new IterationView Listener with the given view and component
	 * 
	 * @param iterationView
	 * @param component
	 */

	public MakeATestListener(final MakeATestPanel makeATestPanel) {
		this.makeATestPanel = makeATestPanel;
	}

	@Override
	public void keyPressed(final KeyEvent e) {
	}

	/**
	 * Triggers an update when a key is released in a field, seems to be working
	 * good
	 * 
	 */

	@Override
	public void keyReleased(final KeyEvent e) {
		update();
	}

	@Override
	public void keyTyped(final KeyEvent e) {
	}


	/**
	 * Calls the appropriate update function in MakeTask VIew
	 * 
	 * 
	 */

	private void update() {
		makeATestPanel.validateInput();
	}

}