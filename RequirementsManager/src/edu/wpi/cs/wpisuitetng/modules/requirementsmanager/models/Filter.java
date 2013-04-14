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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * This is a model for a filter. They are used by the UI to determine what to
 * display on the screen. We and reload them on the database so that the user
 * doesn't loose filters upon restarting Janeway
 */

public class Filter extends AbstractModel {

	/** Enum for the field to filter
	 * 
	 */
	public enum Field {
		NAME, TYPE, PRIORITY, STATUS, ITERATION, ESTIMATE, EFFORT, RELEASE_NUMBER
	}
	
	/** Enum for the filter operation 
	 * 
	 */
	public enum Operation {
		LESS_THAN, LESS_THAN_EQUAL, EQUAL, NOT_EQUAL, GREATER_THAN_EQUAL, GREATER_THAN, OCCURS_BETWEEN, OCCURS_AFTER, OCCURS_BEFORE
	}
	
	
	private int id;
	private User creator;
	private Field field;
	private Operation operation;
	private Object equalTo;

	/**
	 * Creates a blank filter with no user
	 */
	public Filter() {
		this.id = -1;
		this.creator = null;
	}

	/**
	 * Creates a blank filter with the given user
	 * 
	 * @param u
	 *            the creator of the filter
	 */
	public Filter(User u) {
		this.id = -1;
		this.creator = u;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toJSON() {
		return new Gson().toJson(this, Filter.class);
	}

	/**
	 * Converts the given Json encoded filter to an actual filter object
	 * 
	 * @param content
	 *            the Json encoded filter
	 * @return the filter instance
	 */
	public static Filter fromJSON(String content) {
		final Gson parser = new Gson();
		return parser.fromJson(content, Filter.class);
	}

	/**
	 * Converts the given Json encoded array of filters to an actual array of
	 * filter objects
	 * 
	 * @param content
	 *            the Json encoded filter array
	 * @return the filter array instance
	 */
	public static Filter[] fromJSONArray(String content) {
		final Gson parser = new Gson();
		return parser.fromJson(content, Filter[].class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean identify(Object o) {
		if (o instanceof Filter) {
			Filter f = (Filter) o;
			return f.getId() == this.id;
		} else {
			return false;
		}
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the creator
	 */
	public User getCreator() {
		return creator;
	}

	/**
	 * @param creator
	 *            the creator to set
	 */
	public void setCreator(User creator) {
		this.creator = creator;
	}
	
	

	/**
	 * @return the field
	 */
	public Field getField() {
		return field;
	}

	/**
	 * @param field the field to set
	 */
	public void setField(Field field) {
		this.field = field;
	}

	/**
	 * @return the operation
	 */
	public Operation getOperation() {
		return operation;
	}

	/**
	 * @param operation the operation to set
	 */
	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	/**
	 * @return the equalTo
	 */
	public Object getEqualTo() {
		return equalTo;
	}

	/**
	 * @param equalTo the equalTo to set
	 */
	public void setEqualTo(Object equalTo) {
		this.equalTo = equalTo;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save() {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

}
