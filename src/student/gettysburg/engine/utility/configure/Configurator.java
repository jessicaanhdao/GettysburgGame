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

import java.io.*;
import com.google.gson.Gson;

/**
 * A program that writes a board configuration file out to the 
 * JSON configuration file. This really isn't needed for setting up the
 * board since the LoadableSquares class has a method that will return
 * all of the board's squares. There are other things needed for a game
 * (starting) configuration. This file is an example of how to create a
 * JSON file if you need to use the technique, such as in saving a game.
 * @version Jun 13, 2017
 */
public class Configurator
{
	private static String configPath = "configuration/";
	private final Gson gson;
	
	/**
	 * Default constructor.
	 */
	public Configurator()
	{
		gson = new Gson();
	}
	
	/**
	 * Write out the board configuration as a JSON object to the board file.
	 */
	private void createBoardConfiguration()
	{
		final String boardConfigPath = configPath + "board.json";
		try (PrintWriter writer = new PrintWriter(boardConfigPath)) {
			squaresToJson(writer);
			writer.close();
		} catch (IOException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void squaresToJson(PrintWriter writer)
	{
		writer.println("[");
		final LoadableSquare[] squares = LoadableSquare.getSquares();
		for (int i = 0; i < squares.length; i++) {
			writer.print(gson.toJson(squares[i]));
			if (i < squares.length - 1) writer.println(",");
		}
		writer.println("\n]");
	}
	
	/**
	 * Entry to the configurator utility.
	 * @param args a list of configuration items to write. Change this to fit your needs.
	 */
	public static void main(String[] args)
	{
		final Configurator conf = new Configurator();
		for (String s : args) {
			switch (s) {
				case "board":
					conf.createBoardConfiguration();
					break;
			}
		}
	}
}
