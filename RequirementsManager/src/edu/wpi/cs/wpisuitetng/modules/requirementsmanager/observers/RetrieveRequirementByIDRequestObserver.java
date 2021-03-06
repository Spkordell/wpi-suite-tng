/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Jason Whitehouse
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.notifiers.IRetreiveRequirementByIDControllerNotifier;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * Request observer for retrieving a single requirement from the server
 * by id
 */
public class RetrieveRequirementByIDRequestObserver implements RequestObserver {
	
	/** The controller managing the request */
	private final IRetreiveRequirementByIDControllerNotifier notifier;
	
	/**
	 * Construct the observer
	 * 
	 * @param notifier
	 */
	public RetrieveRequirementByIDRequestObserver(
			final IRetreiveRequirementByIDControllerNotifier notifier) {
		this.notifier = notifier;
	}
	
	@Override
	public void fail(final IRequest iReq, final Exception exception) {
		// an error occurred
		notifier.errorReceivingData("Unable to complete request: "
				+ exception.getMessage());
	}
	
	@Override
	public void responseError(final IRequest iReq) {
		// an error occurred
		notifier.errorReceivingData("Received "
				+ iReq.getResponse().getStatusCode() + " error from server: "
				+ iReq.getResponse().getStatusMessage());
	}
	
	@Override
	public void responseSuccess(final IRequest iReq) {
		// cast observable to request
		final Request request = (Request) iReq;
		
		// get the response from the request
		final ResponseModel response = request.getResponse();
		
		if (response.getStatusCode() == 200) {
			// parse the response
			final Requirement[] requirements = Requirement
					.fromJSONArray(response.getBody());
			
			RequirementDatabase.getInstance().add(requirements[0]);
			
			// notify the controller
			notifier.receivedData(requirements[0]);
		} else {
			notifier.errorReceivingData("Received "
					+ iReq.getResponse().getStatusCode()
					+ " error from server: "
					+ iReq.getResponse().getStatusMessage());
		}
	}
	
}
