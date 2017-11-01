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
package student.gettysburg.engine.common;

import gettysburg.common.*;

/**
 * Implementation of the GbgSquareDescriptor interface. This implementation uses
 * lazy methods to create only one instance of any type of square implementation.
 * 
 * @version Jun 13, 2017
 */
public class GbgSquareDescriptorImpl implements GbgSquareDescriptor
{
	private int elevation;
	private Terrain terrain;
	
	/**
	 * Factory method for making a square. We use lazy creation of the objects and
	 * guarantee that only one instance of every possible square configureation is
	 * created. This is an efficiency concern.
	 * @param elevation
	 * @param terrain
	 * @return
	 */
	public static GbgSquareDescriptor makeSquareDescriptor(int elevation, Terrain terrain)
	{
			GbgSquareDescriptorImpl sd = new GbgSquareDescriptorImpl();
			sd.elevation = elevation;
			sd.terrain = terrain;
			return sd;
	}
	
	/**
	 * Default constructor needed for JSON processing. When creating
	 * a unit directly, use the factory method. 
	 */
	public GbgSquareDescriptorImpl()
	{
		this.elevation = -1;
		this.terrain = null;
	}
	
	/*
	 * @see gettysburg.common.GbgSquareDescriptor#getElevation()
	 */
	@Override
	public int getElevation()
	{
		return elevation;
	}

	/*
	 * @see gettysburg.common.GbgSquareDescriptor#getTerrain()
	 */
	@Override
	public Terrain getTerrain()
	{
		return terrain;
	}

	// Change equals and hashCode if you need to
	/*
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + elevation;
		result = prime * result + ((terrain == null) ? 0 : terrain.hashCode());
		return result;
	}

	/*
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof GbgSquareDescriptorImpl)) {
			return false;
		}
		GbgSquareDescriptorImpl other = (GbgSquareDescriptorImpl) obj;
		if (elevation != other.elevation) {
			return false;
		}
		if (terrain != other.terrain) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString()
	{
		// TODO: implement this method
		return null;
	}
}
