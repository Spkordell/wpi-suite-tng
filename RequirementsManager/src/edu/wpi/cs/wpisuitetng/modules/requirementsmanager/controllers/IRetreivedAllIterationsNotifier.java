/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author 
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;

public interface IRetreivedAllIterationsNotifier {
	/**
	 * Called when the requirements data has been received by the server
	 * 
	 * @param requirements
	 *            The received data
	 */

	public void receivedData(Iteration[] iterations);

	/**
	 * Called when a error was returned from the server, instead of data
	 * 
	 * @param RetrieveAllRequirementsRequestObserver
	 */

	public void errorReceivingData(String RetrieveAllRequirementsRequestObserver);
}
