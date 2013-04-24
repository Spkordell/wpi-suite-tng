/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @ Steve Kordell
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.Event;

/**
 * An acceptance test 
 */
public class ATest implements Event {
	private String name;
	private String description;
	private ATestStatus status;
	private int id;

	public ATest(String name, String description) {
		status = ATestStatus.BLANK;
		this.name = name;
		this.description = description;
		this.id = -1;
	}

	/**
	 * @return the completed
	 */
	public ATestStatus getStatus() {
		return status;
	}

	/**
	 * @param completed
	 *            the completed to set
	 */
	public void setStatus(ATestStatus status) {
		this.status = status;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	@Override
	public String getTitle() {
		return "<html><font size=4><b>" + getName() + "</b></html>";
	}

	/**
	 * Returns the content of this note to be displayed in the GUI, as specified
	 * by Event interface
	 * 
	 * @return The content
	 */

	@Override
	public String getContent() {
		String temp = "<i>" + parseNewLines(getDescription());
		String completeMessage;
		if (this.status == ATestStatus.PASSED) {
			completeMessage = "<br><FONT COLOR=\"blue\">PASSED</FONT COLOR>";
		} else if (this.status == ATestStatus.FAILED) {
			completeMessage = "<br><FONT COLOR=\"red\">FAILED</FONT COLOR>";
		} else {
			completeMessage = "<br><FONT COLOR=\"green\">OPEN</FONT COLOR>";
		}
		// return assembled content string;
		return temp + completeMessage + "</i>";
	}

	/**
	 * Changes the new line characters (\n) in the given string to html new line
	 * tags (<br>
	 * )
	 * 
	 * @param The
	 *            string to parse
	 * @return The new string with <br>
	 *         's
	 */

	public String parseNewLines(String text) {
		text = text.replaceAll("\n", "<br>");
		return text;

	}

	/**
	 * @return the id of the task
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

	
	public enum ATestStatus {
		BLANK,PASSED,FAILED
	}


	public boolean isPassed() {
		return this.getStatus() == ATestStatus.PASSED;
	}
}
