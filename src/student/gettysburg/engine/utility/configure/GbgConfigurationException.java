/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2016-2017 Gary F. Pollice
 *******************************************************************************/
package student.gettysburg.engine.utility.configure;

/**
 * Exception that is thrown during reading or writing configuration files.
 * @version Jun 23, 2017
 */
public class GbgConfigurationException extends RuntimeException
{
	public GbgConfigurationException(String msg, Throwable cause)
	{
		super(msg, cause);
	}
}
