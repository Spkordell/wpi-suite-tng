/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Alex Chen
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.listeners;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;

public class ItemStateListener implements ItemListener {
	
	protected final DetailPanel panel;
	protected final JComboBox component;
	
	public ItemStateListener(final DetailPanel panel,
			final JComboBox comboBoxType) {
		this.panel = panel;
		component = comboBoxType;
	}
	
	public void checkIfUpdated(final ItemEvent e) {
		if ((panel.getTextName().getText().trim().length() > 0)
				&& (panel.getTextDescription().getText().trim().length() > 0)) {
			panel.enableSaveButton();
		}
	}
	
	@Override
	public void itemStateChanged(final ItemEvent e) {
		checkIfUpdated(e);
	}
}
