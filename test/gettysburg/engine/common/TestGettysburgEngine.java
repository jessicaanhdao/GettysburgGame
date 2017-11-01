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
package gettysburg.engine.common;

import static gettysburg.common.ArmyID.*;
import static gettysburg.common.Direction.*;
import static gettysburg.common.GbgGameStatus.*;
import static gettysburg.common.GbgGameStep.*;
import static gettysburg.common.UnitSize.BATTALION;
import static gettysburg.common.UnitType.CAVALRY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static student.gettysburg.engine.GettysburgFactory.makeCoordinate;
import static student.gettysburg.engine.GettysburgFactory.makeGame;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import gettysburg.common.*;
import student.gettysburg.engine.common.GbgUnitImpl;
import student.gettysburg.engine.common.GettysburgEngine;

/**
 * Test implementation of the Gettysburg game.
 * @version Jul 31, 2017
 */
public class TestGettysburgEngine extends GettysburgEngine implements TestGbgGame
{

	/*
	 * @see gettysburg.common.TestGbgGame#clearBoard()
	 */
	@Override
	public void clearBoard()
	{
		board.clear();
		turn = 0;
		initPackage();
	}

	/*
	 * @see gettysburg.common.TestGbgGame#putUnitAt(gettysburg.common.GbgUnit, int, int, gettysburg.common.Direction)
	 */
	@Override
	public void putUnitAt(GbgUnit arg0, int arg1, int arg2, Direction arg3)
	{
		arg0.setFacing(arg3);
		board.put(arg0,makeCoordinate(arg1, arg2));
		setUnitZone();
	}

	/*
	 * @see gettysburg.common.TestGbgGame#setBattleResult(gettysburg.common.BattleDescriptor, gettysburg.common.BattleResult)
	 */
	@Override
	public void setBattleResult(BattleDescriptor arg0, BattleResult arg1)
	{
		// TODO Auto-generated method stub

	}
	@Override
	public void setBattleResults(List<BattleResult> list)
	{
		br = list.get(0);
		list.remove(0);
		resultPredetermined =true;
	}
	
	/*
	 * @see gettysburg.common.TestGbgGame#setGameStep(gettysburg.common.GbgGameStep)
	 */
	@Override
	public void setGameStep(GbgGameStep arg0)
	{
		curStep = arg0;
	}

	/*
	 * @see gettysburg.common.TestGbgGame#setGameTurn(int)
	 */
	@Override
	public void setGameTurn(int arg0)
	{
		turn = arg0;
		initAtTurn();
	}

}
