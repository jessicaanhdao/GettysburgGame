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
import gettysburg.common.exceptions.GbgInvalidCoordinateException;
import static gettysburg.common.Direction.*;
/**
 * Implementation of the gettysburg.common.Coordinate interface. Additional methods
 * used in this implementation are added to this class. Clients should <em>ONLY</em>
 * use the public Coordinate interface. Additional methods
 * are only for engine internal use.
 * 
 * @version Jun 9, 2017
 */
public class CoordinateImpl implements Coordinate
{
	private final int x, y;
	
	/**
	 * Private constructor that is called by the factory method.
	 * @param x
	 * @param y
	 */
	private CoordinateImpl(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Needed for JSON processing.
	 */
	public CoordinateImpl()
	{
		x = y = 0;
	}
	
	/**
	 * Factory method for creating Coordinates.
	 * @param x
	 * @param y
	 * @return
	 */
	public static CoordinateImpl makeCoordinate(int x, int y)
	{
		
		if (x < 1 || x > GbgBoard.COLUMNS || y < 1 || y > GbgBoard.ROWS) {
			throw new GbgInvalidCoordinateException(
					"Coordinates for (" + x + ", " + y + ") are out of bounds.");
		}
		return new CoordinateImpl(x, y);
	}
	/** Check if the desired coordinate is within border
	 * @param x coordinate
	 * @param y coordinate
	 * @return true if desired coordinate is within border, otherwise false
	 */
	public static boolean isWithinBorder(int x, int y) {
		if (x > 0 && x <= GbgBoard.COLUMNS && y >= 1 && y <= GbgBoard.ROWS) {
			return true;
		}
		return false;
	}
	/*
	 * @see gettysburg.common.Coordinate#directionTo(gettysburg.common.Coordinate)
	 */
	@Override
	public Direction directionTo(Coordinate coordinate)
	{
		int newX=0, newY=0;
		newX =  coordinate.getX();
		newY =  coordinate.getY();
		Direction dir = null;
		if (x > newX && y==newY) {
			dir = WEST;
		} 
		else if (x < newX && y==newY) {
			dir = EAST;
		} else if (y > newY && x==newX) {
			dir = NORTH;
		} 
		else if (y < newY && x==newX) {
			dir = SOUTH;
		}
		else if (x > newX && y > newY) {
			dir = NORTHWEST;
		}
		else if (x > newX && y < newY) {
			dir = SOUTHWEST;
		}
		else if (x < newX && y > newY) {
			dir = NORTHEAST;
		}
		else if (x < newX && y < newY) {
			dir = SOUTHEAST;
		}
		else dir = NONE;
		return dir;
	}

	/*
	 * @see gettysburg.common.Coordinate#distanceTo(gettysburg.common.Coordinate)
	 */
	@Override
	public int distanceTo(Coordinate coordinate)
	{	
		int newX=coordinate.getX(), newY = coordinate.getY();
		return Math.max(Math.abs(x - newX),Math.abs(y - newY));
	}

	/*
	 * @see gettysburg.common.Coordinate#getX()
	 */
	@Override
	public int getX()
	{
		// TODO: Implement this method.
			return x;
	}

	/*
	 * @see gettysburg.common.Coordinate#getY()
	 */
	@Override
	public int getY()
	{
		// TODO: Implement this method.
		return y;
	}
	
	

	/*
	 * We do not compare a CoordinateImpl to any object that just implements
	 * the Coordinate interface.
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
		if (!(obj instanceof CoordinateImpl)) {
			return false;
		}
		CoordinateImpl other = (CoordinateImpl) obj;
		if (x != other.x) {
			return false;
		}
		if (y != other.y) {
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return"(" + x + ", " + y + ")";
	}
	
	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + x;
	    result = prime * result + y;
	    return result;
	}

}
