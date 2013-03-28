package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.StringListModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.AssigneePanel;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

public class QueryUserRequestObserver implements RequestObserver {

	private AssigneePanel parentView;

	public QueryUserRequestObserver(AssigneePanel parentView) {
		this.parentView = parentView;
	}
	
	@Override
	public void responseSuccess(IRequest iReq) {
		ResponseModel response = iReq.getResponse();
		
		final StringListModel users = StringListModel.fromJson(response.getBody());
		
		System.out.println("User List:\n" + users.getUsers());
		
		this.parentView.setUnassignedUsersList(users);
	}

	@Override
	public void responseError(IRequest iReq) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fail(IRequest iReq, Exception exception) {
		// TODO Auto-generated method stub
		
	}

}