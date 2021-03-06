/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Mitchell Caisse
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs;

import javax.swing.Icon;

/**
 * Class used to add a tab on the swing thread, will hopefully stop UI
 * corruption with un-closable tabs
 */

public class AddTabInvokable implements Runnable {
	
	/** The main tab view that the tab will be added in */
	private final MainTabView tabView;
	
	/** The tab's properties */
	private final String title;
	private final Icon icon;
	private final Tab tab;
	private final String toolTip;
	
	/**
	 * Creates a new instance of addTab invokable
	 * 
	 * @param tabView
	 *            The MainTabView to add the tab in
	 * @param title
	 *            The title of the tab to add
	 * @param icon
	 *            The icon of the tab to add
	 * @param tab
	 *            The component of the tab to add
	 * @param toolTip
	 *            The tooltip of the tab to add
	 */
	
	public AddTabInvokable(final MainTabView tabView, final String title,
			final Icon icon, final Tab tab, final String toolTip) {
		this.tabView = tabView;
		this.title = title;
		this.icon = icon;
		this.tab = tab;
		this.toolTip = toolTip;
	}
	
	@Override
	public void run() {
		tabView.addTab(title, icon, tab, toolTip); // add the tab to the TabView
		final int index = tabView.getTabCount() - 1; // get the index of the
														// newly
		// added tab
		tabView.setSelectedIndex(index); // set the current tab to the newly
											// added tab
	}
}
