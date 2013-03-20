/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.RetrieveAllRequirementsRequestObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * @author Jason Whitehouse
 * Controller to retrieve all requirements from server and return them to the central table view
 */
public class RetrieveAllRequirementsController {

	/** Requirement Notifier that will be called when the requirements have been received */
	IReceivedAllRequirementNotifier requirementNotifier;
	
	protected Requirement[] data;
	//variable connecting with view
	
	public RetrieveAllRequirementsController(IReceivedAllRequirementNotifier requirementNotifier){
		this.requirementNotifier = requirementNotifier;
	}
	
	/**
	 * Sends a request for all of the requirements
	 */
	public void getAll() {	
		final RequestObserver requestObserver = new RetrieveAllRequirementsRequestObserver(this);
		Request request;
		request = Network.getInstance().makeRequest("requirementsmanager/requirement", HttpMethod.GET);
		request.addObserver(requestObserver);
		request.send();
	}
	
	/**
	 * This method is called by the {@link RetrieveAllRequirementsRequestObserver} when the
	 * response is received
	 * 
	 * @param requirements an array of requirements returned by the server
	 */
	public void receivedData(Requirement[] requirements){
		requirementNotifier.receivedData(requirements);
	}
	
	/**
	 * This method is called by the {@link r} when an
	 * error occurs retrieving the requirements from the server.
	 */
	public void errorReceivingData(String RetrieveAllRequirementsRequestObserver) {
		requirementNotifier.errorReceivingData(RetrieveAllRequirementsRequestObserver);
	}
}
