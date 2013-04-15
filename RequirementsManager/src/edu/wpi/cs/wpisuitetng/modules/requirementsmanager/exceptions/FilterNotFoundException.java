/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Fredric
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions;

/**
 * Thrown when a filter is not found in the database
 */

public class FilterNotFoundException extends Exception {

	int id;

	/**
	 * Creates a new not found exception with the given id
	 * 
	 * @param id
	 */
	public FilterNotFoundException(int id) {
		super();
		this.id = id;
	}

	/**
	 * Gets the id of the missing filter
	 * 
	 * @return the id of the filter
	 */
	public int getFilterId() {
		return this.id;
	}

}