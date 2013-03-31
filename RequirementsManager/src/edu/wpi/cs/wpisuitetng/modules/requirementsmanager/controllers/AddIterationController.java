/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.AddIterationRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.IterationView;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * @author Jason Whitehouse Controller used to add a requirement to the database
 */
public class AddIterationController {

	IterationView iterationView;

	public AddIterationController(IterationView iterationView) {
		this.iterationView = iterationView;
	}

	/**
	 * Adds a requirement to the database
	 * 
	 * @param toAdd
	 *            requirement that will be added
	 */
	public void addIteration(Iteration toAdd) {
		final RequestObserver requestObserver = new AddIterationRequestObserver(
				this, iterationView); // you will probably want to pass your view
									// to the observer as well
		Request request;
		request = Network.getInstance().makeRequest(
				"requirementsmanager/iteration", HttpMethod.PUT);
		request.setBody(toAdd.toJSON());
		request.addObserver(requestObserver);
		request.send();
	}
}