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

import static gettysburg.common.Terrain.*;
import static student.gettysburg.engine.common.GbgSquareDescriptorImpl.makeSquareDescriptor;
import gettysburg.common.*;
import student.gettysburg.engine.common.GbgSquareDescriptorImpl;

/**
 * <p>
 * A simple data structure that contains x and y-coordinates for each square, and the terrain for
 * that square. It also contains a static method for getting a array of the squares that do not
 * have CLEAR terrain. 
 * </p><p>
 * This class is used for initialization of the game, the board, and any representation that can
 * use it, such as a viewl ike a graphical user interface.
 * </p>
 * @version Jun 13, 2017
 */
public class LoadableSquare
{
	public int x, y;
	public GbgSquareDescriptorImpl sd;
	
	private static LoadableSquare[] squares = {
			// Row 1
			mls(1, 1, 0, FOREST), mls(2, 1, 0, RIVER), mls(3, 1, 0, RIVER), mls(5, 1, 0, ROAD),
			mls(6, 1, 1, ROAD), mls(7, 1, 1, SLOPE), mls(10, 1, 0, ROAD), mls(12, 1, 0, ROAD),
			mls(16, 1, 0, ROAD), mls(21, 1, 0, ROAD), mls(22, 1, 0, ROAD),
			// Row 2
			mls(1, 2, 0, FOREST), mls(2, 2, 0, RIVER), mls(3, 2, 0, RIVER), mls(4, 2, 0, RIVER	),
			mls(6, 2, 1, ROAD), mls(7, 2, 1, SLOPE), mls(8, 2, 2, SLOPE), mls(9, 2, 2, SLOPE),
			mls(10, 2, 1, ROAD), mls(12, 2, 0, ROAD), mls(15, 2, 0, ROAD), mls(16, 2, 0, ROAD),
			mls(17, 2, 0, FOREST), mls(20, 2, 0, ROAD), mls(21, 2, 0, ROAD), mls(22, 2, 0, ROAD),
			// Row 3
			mls(1, 3, 0, FOREST), mls(2, 3, 0, RIVER), mls(3, 3, 0, FOREST), mls(4, 3, 0, RIVER	),
			mls(5, 3, 0, ROAD), mls(6, 3, 1, SLOPE), mls(7, 3, 1, ROAD), mls(8, 3, 1, ROAD),
			mls(9, 3, 1, ROAD), mls(10, 3, 2, ROAD), mls(11, 3, 2, SLOPE), mls(12, 3, 0, ROAD),
			mls(15, 3, 0, ROAD), mls(16, 3, 0, RIVER), mls(17, 3, 0, FOREST), mls(20, 3, 0, ROAD),
			mls(21, 3, 0, ROAD), mls(22, 3, 0, ROAD),
			// Row 4
			mls(2, 4, 0, RIVER), mls(3, 4, 0, RIVER), mls(4, 4, 0, ROAD), mls(5, 4, 1, RIVER),
			mls(6, 4, 2, HILLTOP), mls(7, 4, 1, SLOPE), mls(8, 4, 2, ROAD), mls(9, 4, 2, ROAD),
			mls(10, 4, 3, HILLTOP), mls(11, 4, 3, HILLTOP), mls(12, 4, 0, ROAD), mls(13, 4, 0, ROAD),
			mls(15, 4, 0, ROAD), mls(16, 4, 0, RIVER), mls(19, 4, 0, ROAD), mls(20, 4, 0, ROAD),
			mls(21, 4, 0, FOREST), mls(22, 4, 0, FOREST),
			// Row 5
			mls(1, 5, 0, ROAD),mls(2, 5, 0, ROAD), mls(3, 5, 0, ROAD), mls(4, 5, 0, ROAD),
			mls(5, 5, 0, RIVER), mls(7, 5, 1, RIVER), mls(8, 5, 1, ROAD), mls(9, 5, 2, ROAD),
			mls(10, 5, 2, ROAD), mls(11, 5, 3, HILLTOP), mls(13, 5, 0, ROAD), mls(14, 5, 0, ROAD),
			mls(15, 5, 0, RIVER), mls(17, 5, 0, ROAD), mls(18, 5, 0, ROAD), mls(19, 5, 0, ROAD),
			mls(20, 5, 0, ROAD), mls(21, 5, 0, FOREST),
			// Row 6
			mls(1, 6, 0, FOREST), mls(2, 6, 0, FOREST), mls(3, 6, 0, ROAD), mls(4, 6, 0, ROAD),
			mls(5, 6, 0, ROAD), mls(7, 6, 1, RIVER), mls(8, 6, 2, ROAD), mls(9, 6, 1, RIVER),
			mls(10, 6, 2, ROAD), mls(13, 6, 0, ROAD), mls(14, 6, 0, ROAD), mls(15, 6, 0, RIVER),
			mls(16, 6, 0, ROAD), mls(17, 6, 0, ROAD), mls(18, 6, 0, ROAD), mls(19, 6, 0, ROAD),
			mls(20, 6, 0, ROAD), mls(21, 6, 0, ROAD), mls(22, 6, 0, ROAD),
			// Row 7
			mls(1, 7, 0, FOREST), mls(2, 7, 0, ROAD), mls(4, 7, 0, RIVER), mls(5, 7, 0, ROAD),
			mls(6, 7, 0, ROAD), mls(8, 7, 1, ROAD), mls(9, 7, 1, RIVER), mls(10, 7, 3, HILLTOP),
			mls(11, 7, 1, ROAD), mls(12, 7, 0, ROAD), mls(14, 7, 1, ROAD), mls(15, 7, 1, ROAD),
			mls(16, 7, 0, ROAD), mls(18, 7, 0, ROAD), mls(19, 7, 0, FOREST), mls(20, 7, 0, ROAD),
			mls(21, 7, 0, ROAD), mls(22, 7, 0, FOREST),
			// Row 8
			mls(1, 8, 0, ROAD), mls(2, 8, 0, ROAD), mls(3, 8, 0, RIVER), mls(4, 8, 0, RIVER),
			mls(5, 8, 0, RIVER), mls(6, 8, 2, SLOPE), mls(7, 8, 2, ROAD), mls(8, 8, 1, ROAD),
			mls(9, 8, 3, HILLTOP), mls(10, 8, 3, HILLTOP), mls(11, 8, 1, SLOPE), mls(12, 8, 0, ROAD),
			mls(13, 8, 0, ROAD), mls(14, 8, 0, ROAD), mls(15, 8, 0, ROAD), mls(16, 8, 0, RIVER),
			mls(17, 8, 0, RIVER), mls(18, 8, 0, ROAD), mls(19, 8, 0, ROAD), mls(20, 8, 0, ROAD),
			mls(21, 8, 0, ROAD), mls(22, 8, 0, ROAD),
			// Row 9
			mls(1, 9, 0, ROAD), mls(2, 9, 0, ROAD), mls(3, 9, 0, ROAD), mls(4, 9, 0, RIVER),
			mls(5, 9, 1, RIVER), mls(6, 9, 3, ROAD), mls(7, 9, 3, ROAD), mls(8, 9, 1, ROAD),
			mls(9, 9, 3, ROAD), mls(10, 9, 3, ROAD), mls(11, 9, 1, SLOPE), mls(12, 9, 0, ROAD),
			mls(13, 9, 0, ROAD), mls(14, 9, 0, ROAD), mls(15, 9, 0, ROAD), mls(16, 9, 0, RIVER),
			mls(17, 9, 0, ROAD), mls(18, 9, 0, ROAD), mls(19, 9, 0, RIVER), mls(20, 9, 0, FOREST),
			mls(21, 9, 0, RIVER), mls(22, 9, 0, ROAD),
			// Row 10
			mls(1, 10, 0, RIVER), mls(2, 10, 0, RIVER), mls(3, 10, 0, ROAD), mls(4, 10, 0, ROAD),
			mls(5, 10, 2, ROAD), mls(6, 10, 3, ROAD), mls(7, 10, 1, FOREST), mls(8, 10, 1, RIVER),
			mls(9, 10, 3, HILLTOP), mls(10, 10, 2, ROAD), mls(11, 10, 0, ROAD), mls(12, 10, 0, ROAD),
			mls(13, 10, 0, ROAD), mls(14, 10, 0, ROAD), mls(15, 10, 0, ROAD), mls(16, 10, 0, ROAD),
			mls(17, 10, 0, ROAD), mls(18, 10, 0, RIVER), mls(20, 10, 1, CLEAR), mls(21, 10, 0, RIVER),
			// Row 11
			mls(1, 11, 0, RIVER), mls(2, 11, 0, RIVER), mls(3, 11, 0, ROAD), mls(4, 11, 2, ROAD),
			mls(5, 11, 3, HILLTOP), mls(6, 11, 0, ROAD), mls(7, 11, 2, ROAD), mls(8, 11, 1, RIVER),
			mls(9, 11, 3, RIVER), mls(10, 11, 2, ROAD), mls(11, 11, 1, ROAD), mls(12, 11, 0, ROAD),
			mls(13, 11, 0, ROAD), mls(14, 11, 0, ROAD), mls(15, 11, 0, ROAD), mls(16, 11, 1, ROAD),
			mls(17, 11, 0, ROAD), mls(18, 11, 3, ROAD), mls(19, 11, 1, ROAD), mls(20, 11, 0, RIVER),
			mls(21, 11, 0, RIVER),
			// Row 12
			mls(1, 12, 0, RIVER), mls(2, 12, 0, RIVER), mls(3, 12, 0, ROAD), mls(4, 12, 2, ROAD),
			mls(5, 12, 2, ROAD), mls(6, 12, 0, ROAD), mls(7, 12, 1, FOREST), mls(8, 12, 1, ROAD),
			mls(9, 12, 3, ROAD), mls(10, 12, 2, ROAD), mls(11, 12, 0, ROAD), mls(12, 12, 0, RIVER),
			mls(13, 12, 0, ROAD), mls(14, 12, 1, ROAD), mls(15, 12, 2, ROAD), mls(16, 12, 1, ROAD),
			mls(17, 12, 2, RIVER), mls(18, 12, 3, ROAD), mls(19, 12, 0, ROAD), mls(20, 12, 0, ROAD),
			mls(22, 12, 0, FOREST),
			// Row 13
			mls(1, 13, 0, ROAD), mls(2, 13, 0, ROAD), mls(3, 13, 1, ROAD), mls(4, 13, 2, ROAD),
			mls(5, 13, 1, ROAD), mls(6, 13, 1, ROAD), mls(7, 13, 1, ROAD), mls(8, 13, 2, ROAD),
			mls(9, 13, 3, ROAD), mls(10, 13, 2, FOREST), mls(11, 13, 0, RIVER), mls(12, 13, 0, ROAD),
			mls(13, 13, 1, ROAD), mls(14, 13, 3, ROAD), mls(15, 13, 2, SLOPE), mls(16, 13, 1, ROAD),
			mls(17, 13, 1, RIVER), mls(18, 13, 2, ROAD), mls(19, 13, 0, ROAD), mls(20, 13, 2, ROAD),
			mls(21, 13, 0, ROAD), mls(22, 13, 0, ROAD),
			// Row 14
			mls(1, 14, 0, RIVER), mls(2, 14, 0, ROAD), mls(3, 14, 1, SLOPE), mls(4, 14, 2, ROAD),
			mls(5, 14, 0, ROAD), mls(6, 14, 1, ROAD), mls(7, 14, 1, RIVER), mls(8, 14, 3, HILLTOP),
			mls(9, 14, 2, RIVER), mls(10, 14, 2, HILLTOP), mls(11, 14, 0, ROAD), mls(12, 14, 0, ROAD),
			mls(13, 14, 2, ROAD), mls(14, 14, 3, ROAD), mls(15, 14, 1, ROAD), mls(16, 14, 3, HILLTOP),
			mls(17, 14, 1, RIVER), mls(18, 14, 0, ROAD), mls(19, 14, 0, ROAD), mls(20, 14, 0, ROAD),
			mls(21, 14, 0, ROAD), mls(22, 14, 0, FOREST),
			// Row 15
			mls(1, 15, 0, ROAD), mls(2, 15, 0, ROAD), mls(3, 15, 0, ROAD), mls(4, 15, 2, HILLTOP),
			mls(6, 15, 2, HILLTOP), mls(7, 15, 2, RIVER), mls(8, 15, 3, HILLTOP), mls(9, 15, 2, RIVER),
			mls(10, 15, 1, HILLTOP), mls(11, 15, 0, ROAD), mls(13, 15, 2, ROAD), mls(14, 15, 1, ROAD),
			mls(15, 15, 0, ROAD), mls(16, 15, 1, FOREST), mls(17, 15, 2, RIVER), mls(18, 15, 0, RIVER),
			mls(19, 15, 0, RIVER), mls(20, 15, 3, HILLTOP), mls(21, 15, 0, ROAD), mls(22, 15, 0, ROAD),
			// Row 16
			mls(2, 16, 0, RIVER), mls(3, 16, 0, ROAD), mls(4, 16, 0, ROAD), mls(5, 16, 1, RIVER),
			mls(6, 16, 2, RIVER), mls(7, 16, 3, HILLTOP), mls(8, 16, 2, HILLTOP), mls(9, 16, 1, RIVER),
			mls(10, 16, 1, SLOPE), mls(12, 16, 0, ROAD), mls(13, 16, 2, HILLTOP), mls(14, 16, 1, ROAD),
			mls(15, 16, 0, ROAD), mls(16, 16, 0, ROAD), mls(17, 16, 0, RIVER), mls(18, 16, 0, RIVER),
			mls(19, 16, 1, ROAD), mls(20, 16, 0, ROAD), mls(21, 16, 0, ROAD), mls(22, 16, 0, FOREST),
			// Row 17
			mls(1, 17, 0, RIVER), mls(2, 17, 0, ROAD), mls(3, 17, 0, ROAD), mls(4, 17, 0, ROAD),
			mls(5, 17, 0, ROAD), mls(6, 17, 2, ROAD), mls(7, 17, 3, HILLTOP), mls(8, 17, 0, RIVER),
			mls(9, 17, 1, HILLTOP), mls(10, 17, 1, ROAD), mls(11, 17, 1, ROAD), mls(12, 17, 0, ROAD),
			mls(13, 17, 2, HILLTOP), mls(14, 17, 1, ROAD), mls(15, 17, 0, RIVER), mls(16, 17, 1, ROAD),
			mls(17, 17, 0, ROAD), mls(18, 17, 0, ROAD), mls(19, 17, 1, FOREST), mls(20, 17, 0, RIVER),
			mls(21, 17, 0, ROAD), mls(22, 17, 0, ROAD),
			// Row 18
			mls(1, 18, 0, FOREST), mls(2, 18, 0, RIVER), mls(3, 18, 0, ROAD), mls(4, 18, 0, RIVER),
			mls(5, 18, 1, HILLTOP), mls(6, 18, 0, ROAD), mls(7, 18, 0, RIVER), mls(8, 18, 0, FOREST),
			mls(9, 18, 1, HILLTOP), mls(10, 18, 1, ROAD), mls(11, 18, 1, ROAD), mls(12, 18, 0, RIVER),
			mls(13, 18, 2, HILLTOP), mls(14, 18, 1, ROAD), mls(15, 18, 0, FOREST), mls(16, 18, 2, ROAD),
			mls(17, 18, 1, ROAD), mls(18, 18, 0, ROAD), mls(19, 18, 0, ROAD), mls(20, 18, 0, RIVER),
			mls(21, 18, 0, ROAD),
			// Row 19
			mls(2, 19, 0, RIVER), mls(3, 19, 0, RIVER), mls(4, 19, 0, RIVER), mls(5, 19, 1, HILLTOP),
			mls(6, 19, 0, RIVER), mls(7, 19, 0, ROAD), mls(8, 19, 0, FOREST), mls(9, 19, 1, ROAD),
			mls(10, 19, 1, ROAD), mls(11, 19, 1, ROAD), mls(12, 19, 0, ROAD), mls(13, 19, 2, HILLTOP),
			mls(14, 19, 2, ROAD), mls(17, 19, 0, ROAD), mls(18, 19, 0, RIVER), mls(19, 19, 0, ROAD),
			mls(20, 19, 0, ROAD), mls(21, 19, 0, ROAD), mls(22, 19, 0, ROAD),
			// Row 20
			mls(2, 20, 0, FOREST), mls(3, 20, 0, RIVER), mls(4, 20, 0, RIVER), mls(5, 20, 0, ROAD),
			mls(6, 20, 0, ROAD), mls(7, 20, 0, ROAD), mls(8, 20, 0, ROAD), mls(9, 20, 1, HILLTOP),
			mls(10, 20, 0, ROAD), mls(11, 20, 0, ROAD), mls(12, 20, 0, ROAD), mls(13, 20, 2, ROAD),
			mls(14, 20, 2, ROAD), mls(16, 20, 0, ROAD), mls(17, 20, 0, ROAD), mls(18, 20, 0, RIVER),
			mls(19, 20, 0, RIVER), mls(20, 20, 0, ROAD), mls(21, 20, 0, ROAD), mls(22, 20, 0, RIVER),
			// Row 21
			mls(2, 21, 0, RIVER), mls(3, 21, 0, FOREST), mls(4, 21, 0, FOREST), mls(5, 21, 1, RIVER),
			mls(6, 21, 0, RIVER), mls(7, 21, 1, HILLTOP), mls(8, 21, 0, ROAD), mls(9, 21, 1, ROAD),
			mls(10, 21, 0, ROAD), mls(11, 21, 0, ROAD), mls(12, 21, 0, RIVER), mls(13, 21, 1, ROAD),
			mls(14, 21, 1, ROAD), mls(15, 21, 0, ROAD), mls(16, 21, 0, ROAD), mls(17, 21, 0, RIVER),
			mls(19, 21, 0, RIVER), mls(21, 21, 0, ROAD), mls(22, 21, 0, ROAD),
			// Row 22
			mls(3, 22, 0, RIVER), mls(4, 22, 1, RIVER), mls(5, 22, 2, ROAD), mls(6, 22, 0, RIVER),
			mls(7, 22, 0, RIVER), mls(8, 22, 0, RIVER), mls(9, 22, 1, ROAD), mls(10, 22, 2, ROAD),
			mls(11, 22, 1, RIVER), mls(12, 22, 0, RIVER), mls(13, 22, 3, HILLTOP), mls(14, 22, 0, ROAD),
			mls(15, 22, 0, ROAD), mls(16, 22, 0, RIVER), mls(17, 22, 0, ROAD), mls(18, 22, 0, ROAD),
			mls(19, 22, 0, RIVER), mls(20, 22, 0, RIVER), mls(21, 22, 0, RIVER), mls(22, 22, 0, FOREST),
			// Row 23
			mls(3, 23, 0, RIVER), mls(4, 23, 0, ROAD), mls(5, 23, 0, ROAD), mls(7, 23, 0, RIVER),
			mls(8, 23, 0, RIVER), mls(9, 23, 1, ROAD), mls(10, 23, 0, ROAD), mls(11, 23, 1, RIVER),
			mls(12, 23, 2, SLOPE), mls(13, 23, 2, SLOPE), mls(14, 23, 0, ROAD), mls(15, 23, 0, ROAD),
			mls(16, 23, 0, ROAD), mls(17, 23, 0, ROAD), mls(18, 23, 0, ROAD), mls(19, 23, 0, RIVER),
			mls(21, 23, 0, RIVER),
			// Row 24
			mls(1, 24, 0, RIVER), mls(2, 24, 0, RIVER), mls(3, 24, 0, RIVER), mls(4, 24, 0, RIVER),
			mls(5, 24, 0, RIVER), mls(6, 24, 0, RIVER), mls(7, 24, 0, RIVER), mls(8, 24, 0, ROAD),
			mls(9, 24, 1, ROAD), mls(10, 24, 1, SLOPE), mls(11, 24, 2, RIVER), mls(12, 24, 3, HILLTOP),
			mls(13, 24, 2, RIVER), mls(14, 24, 0, ROAD), mls(17, 24, 0, ROAD), mls(18, 24, 0, ROAD),
			mls(19, 24, 0, RIVER), mls(21, 24, 0, RIVER),
			// Row 25
			mls(3, 25, 0, FOREST), mls(4, 25, 0, FOREST), mls(5, 25, 0, RIVER), mls(8, 25, 0, ROAD),
			mls(9, 25, 1, SLOPE), mls(10, 25, 1, SLOPE), mls(11, 25, 1, RIVER), mls(12, 25, 1, RIVER),
			mls(13, 25, 1, HILLTOP), mls(14, 25, 0, ROAD), mls(16, 25, 0, FOREST), mls(18, 25, 0, ROAD),
			mls(19, 25, 0, ROAD), mls(20, 25, 0, RIVER), mls(21, 25, 0, FOREST), mls(22, 25, 0, FOREST),
			// Row 26
			mls(3, 26, 0, FOREST), mls(4, 26, 0, FOREST), mls(5, 26, 0, RIVER), mls(8, 26, 0, ROAD),
			mls(10, 26, 1, FOREST), mls(11, 26, 0, RIVER), mls(12, 26, 1, SLOPE), mls(13, 26, 1, HILLTOP),
			mls(14, 26, 0, ROAD), mls(16, 26, 0, FOREST), mls(17, 26, 0, RIVER), mls(18, 26, 0, RIVER),
			mls(19, 26, 0, RIVER),
			// Row 27
			mls(5, 27, 0, RIVER), mls(7, 27, 0, ROAD), mls(10, 27, 0, FOREST), mls(11, 27, 0, ROAD),
			mls(12, 27, 1, ROAD), mls(13, 27, 1, SLOPE), mls(14, 27, 0, ROAD), mls(16, 27, 0, FOREST),
			mls(17, 27, 0, ROAD), mls(22, 27, 0, FOREST),
			// Row 28
			mls(5, 28, 0, RIVER), mls(7, 28, 0, ROAD), mls(10, 28, 0, FOREST), mls(11, 28, 0, FOREST),
			mls(12, 28, 0, RIVER), mls(13, 28, 0, FOREST), mls(14, 28, 0, ROAD),mls(17, 28, 0, RIVER),
			mls(21, 28, 0, FOREST), mls(22, 28, 0, FOREST),
			
	};
	
	/**
	 * Constructor
	 * @param x the x-coordinate
	 * @param y the y-coordinate
	 * @param e the elevation
	 * @param t the terrain	 */
	public LoadableSquare(int x, int y, int e, Terrain t)
	{
		this.x = x;
		this.y = y;
		this.sd = (GbgSquareDescriptorImpl) makeSquareDescriptor(e, t);
	}
	
	/**
	 * Convenience method for creating a LoadableSquare.
	 * @param x the x-coordinate
	 * @param y the y-coordinate
	 * @param e the elevation
	 * @param t the terrain
	 * @return the LoadableSquare object
	 */
	private static LoadableSquare mls(int x, int y, int e, Terrain t)
	{
		return new LoadableSquare(x, y, e, t);
	}

	/**
	 * @return the squares
	 */
	public static LoadableSquare[] getSquares()
	{
		return squares;
	}
}