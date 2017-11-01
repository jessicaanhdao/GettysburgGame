/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2016 Gary F. Pollice
 *******************************************************************************/

package gettysburg.engine.common;

import static gettysburg.common.ArmyID.*;
import static gettysburg.common.Direction.*;
import static gettysburg.common.UnitSize.*;
import static gettysburg.common.UnitType.*;
import static gettysburg.common.GbgGameStatus.IN_PROGRESS;
import static gettysburg.common.GbgGameStep.*;
import static student.gettysburg.engine.GettysburgFactory.*;
import java.util.*;
import static org.junit.Assert.*;
import org.junit.*;
import gettysburg.common.*;
import gettysburg.common.exceptions.*;
import student.gettysburg.engine.common.GbgUnitImpl;
import student.gettysburg.engine.common.GettysburgEngine;

/**
 * Test cases for release 2.
 * @version Sep 30, 2017
 */
public class Version2MasterTests
{
	private GbgGame game;
	private TestGbgGame testGame;
	private GbgUnit gamble, rowley, schurz, devin, heth, rodes, dance, hampton;

	@Before
	public void setup()
	{
		game = testGame = makeTestGame();
		gamble = game.getUnitsAt(makeCoordinate(11, 11)).iterator().next();
		devin = game.getUnitsAt(makeCoordinate(13, 9)).iterator().next();
		heth = game.getUnitsAt(makeCoordinate(8, 8)).iterator().next();
		// These work if the student kept that GbgUnitImpl.makeUnit method
		rowley = GbgUnitImpl.makeUnit(UNION, 3, NORTHEAST, "Rowley", 2, DIVISION, INFANTRY);
		schurz = GbgUnitImpl.makeUnit(UNION, 2, NORTH, "Schurz", 2, DIVISION, INFANTRY);
		rodes = GbgUnitImpl.makeUnit(CONFEDERATE, 4, SOUTH, "Rodes", 2, DIVISION, INFANTRY);
		dance = GbgUnitImpl.makeUnit(CONFEDERATE, 2, EAST, "Dance", 4, BATTALION, ARTILLERY);
		hampton = GbgUnitImpl.makeUnit(CONFEDERATE, 1, SOUTH, "Hampton", 4, BRIGADE, CAVALRY);
		// If the previous statements fail, comment them out and try these
//		rowley = TestUnit.makeUnit(UNION, 3, NORTHEAST, "Rowley", 2);
//		schurz = TestUnit.makeUnit(UNION,  2,  NORTH, "Shurz", 2);
//		rodes = TestUnit.makeUnit(CONFEDERATE, 4, SOUTH, "Rodes", 2);
//		dance = TestUnit.makeUnit(CONFEDERATE, 2, EAST, "Dance", 4);
//		hampton = TestUnit.makeUnit(CONFEDERATE, 1, SOUTH, "Hampton", 4);
		devin.setFacing(SOUTH);
		gamble.setFacing(WEST);
		heth.setFacing(EAST);
	}
	
	// Initial setup tests taken as is from Version 1 tests
	@Test
	public void gameTurnIsOneOnInitializedGame()
	{
		assertEquals(1, game.getTurnNumber());
	}

	@Test
	public void initialGameStatusIsInProgress()
	{
		assertEquals(IN_PROGRESS, game.getGameStatus());
	}

	@Test
	public void gameStepOnInitializedGameIsUMOVE()
	{
		assertEquals(UMOVE, game.getCurrentStep());
	}

	@Test
	public void correctSquareForGambleUsingWhereIsUnit()
	{
		assertEquals(makeCoordinate(11, 11), game.whereIsUnit("Gamble", UNION));
	}

	@Test
	public void correctSquareForGambleUsingGetUnitsAt()
	{
		GbgUnit unit = game.getUnitsAt(makeCoordinate(11, 11)).iterator().next();
		assertNotNull(unit);
		assertEquals("Gamble", unit.getLeader());
	}

	@Test
	public void devinFacesSouth()
	{
		assertEquals(SOUTH, game.getUnitFacing(devin));
	}
	
	@Test(expected=Exception.class)
	public void queryInvalidSquare()
	{
		game.getUnitsAt(makeCoordinate(30, 30));
	}
	
	@Test
	public void hethFacesSouth()
	{
		game.endStep();
		game.endStep();
		game.setUnitFacing(heth, SOUTH);
		assertEquals(SOUTH, game.getUnitFacing(devin));
	}
	
	
	@Test
	public void directionToNORHWEST() {
		Coordinate gambleCoor = game.whereIsUnit(gamble);
		assertEquals(NORTHWEST,gambleCoor.directionTo(game.whereIsUnit(heth)));
	}
	@Test
	public void directionToNORTH() {
		Coordinate gambleCoor = game.whereIsUnit(gamble);
		assertEquals(NORTH,gambleCoor.directionTo(makeCoordinate(11,10)));
	}
	@Test
	public void directionToSOUTH() {
		Coordinate gambleCoor = game.whereIsUnit(gamble);
		assertEquals(SOUTH,gambleCoor.directionTo(makeCoordinate(11,12)));
	}
	@Test
	public void directionToWEST() {
		Coordinate gambleCoor = game.whereIsUnit(gamble);
		assertEquals(WEST,gambleCoor.directionTo(makeCoordinate(10,11)));
	}
	@Test
	public void directionToEAST() {
		Coordinate gambleCoor = game.whereIsUnit(gamble);
		assertEquals(EAST,gambleCoor.directionTo(makeCoordinate(12,11)));
	}
	/**test SouthEast dir**/
	@Test
	public void testSouthEast()
	{	Coordinate cor = makeCoordinate(8,8);
		assertEquals(SOUTHEAST,cor.directionTo(makeCoordinate(9,9)));
	}
	/**test SouthWest dir**/
	@Test
	public void testSouthWest()
	{	Coordinate cor = makeCoordinate(8,8);
		assertEquals(SOUTHWEST,cor.directionTo(makeCoordinate(7,9)));
	}
	
	/**test NorthEast dir**/
	@Test
	public void testNorthEast()
	{	Coordinate cor = makeCoordinate(8,8);
		assertEquals(NORTHEAST,cor.directionTo(makeCoordinate(9,7)));
	}
	@Test
	public void directionToNONE() {
		Coordinate gambleCoor = game.whereIsUnit(gamble);
		assertEquals(NONE,gambleCoor.directionTo(gambleCoor));
	}
	
	@Test
	public void distanceTo() {
		Coordinate gambleCoor = game.whereIsUnit(gamble);
		assertEquals(3,gambleCoor.distanceTo(game.whereIsUnit(heth)));
	}
	
	@Test(expected = GbgInvalidMoveException.class)
	public void confMovedWhenNotTurn()
	{
		game.moveUnit(heth, game.whereIsUnit(heth), makeCoordinate(9, 9));
	}
	@Test(expected = GbgInvalidMoveException.class)
	public void unionMovedWhenNotTurn()
	{
		game.endStep();
		game.endStep();
		game.moveUnit(gamble, game.whereIsUnit(gamble), makeCoordinate(9, 9));
	}
	
	// Game step and turn tests
	@Test
	public void unionBattleFollowsUnionMove()
	{
		game.endStep();
		assertEquals(UBATTLE, game.getCurrentStep());
	}

	@Test
	public void confederateMoveFollowsUnionBattle()
	{
		game.endStep();
		game.endStep();
		assertEquals(CMOVE, game.getCurrentStep());
	}

	@Test
	public void confederateBattleFollowsConfederateMove()
	{
		game.endStep();
		game.endStep(); //battle
		assertEquals(CBATTLE, game.endStep());
	}

	@Test
	public void turnOneDuringConfederateBattle()
	{
		game.endStep();
		game.endStep();
		game.endStep();
		assertEquals(1, game.getTurnNumber());
	}

	@Test
	public void goToTurn2()
	{
		game.endStep();
		game.endStep();
		game.endStep();
		game.endStep();
		assertEquals(2, game.getTurnNumber());
	}

	@Test
	public void startOfTurn2IsUMOVEStep()
	{
		game.endStep();
		game.endStep();
		game.endStep();
		game.endStep();
		assertEquals(UMOVE, game.getCurrentStep());
	}

	// Movement tests
	@Test
	public void gambleMovesNorth()
	{
		game.moveUnit(gamble, makeCoordinate(11, 11), makeCoordinate(11, 10));
		assertEquals(makeCoordinate(11, 10), game.whereIsUnit(gamble));
		assertNull(">> Documentation says this should be null, not an empty array",
				game.getUnitsAt(makeCoordinate(11, 11)));
	}

	@Test
	public void devinMovesSouth()
	{
		game.moveUnit(devin, makeCoordinate(13, 9), makeCoordinate(13, 11));
		assertEquals(makeCoordinate(13, 11), game.whereIsUnit(devin));
	}

	@Test
	public void hethMovesEast()
	{
		game.endStep();
		game.endStep();
		game.moveUnit(heth, makeCoordinate(8, 8), makeCoordinate(10, 8));
		assertEquals(heth, game.getUnitsAt(makeCoordinate(10, 8)).iterator().next());
	}
	
	@Test
	public void devinMovesSouthUsingANonStandardCoordinate()
	{
		game.moveUnit(devin, new TestCoordinate(13, 9), makeCoordinate(13, 11));
		assertEquals(makeCoordinate(13, 11), game.whereIsUnit(devin));
	}
	
	// Tests requiring the test double
	// Movement tests
	@Test
	public void stackedEntryUnitIsAsCorrectLocation()
	{
	    testGame.setGameTurn(8);
	    testGame.setGameStep(CBATTLE);
	    game.endStep();  // step -> UMOVE, turn -> 9
	    assertEquals(makeCoordinate(22, 22), game.whereIsUnit("Geary", UNION));
	}

	@Test
	public void stackedEntryUnitsAreNotMoved()
	{
	    testGame.setGameTurn(8);
	    testGame.setGameStep(CBATTLE);
	    game.endStep();  // step -> UMOVE, turn -> 9
	    game.endStep();  // step -> UBATTLE
	    assertNull(game.getUnitsAt(makeCoordinate(22, 22)));
	}
	
	@Test
	public void unitsStackedProperlyAtStartOfGame()
	{
		// Move units at (7, 28) during UMOVE, turn 1
		Collection<GbgUnit> units = game.getUnitsAt(makeCoordinate(7, 28));
		assertEquals(4, units.size());
	}
	
	@Test
	public void allStackedUnitsAtStartOfGameMove()
	{
		Iterator<GbgUnit> units = game.getUnitsAt(makeCoordinate(7, 28)).iterator();
		game.moveUnit(units.next(), makeCoordinate(7, 28), makeCoordinate(5, 28));
		game.moveUnit(units.next(), makeCoordinate(7, 28), makeCoordinate(6, 28));
		game.moveUnit(units.next(), makeCoordinate(7, 28), makeCoordinate(8, 28));
		game.moveUnit(units.next(), makeCoordinate(7, 28), makeCoordinate(9, 28));
		Collection<GbgUnit> remaining = game.getUnitsAt(makeCoordinate(7, 28));
		assertTrue(remaining == null || remaining.isEmpty());
	}
	
	@Test
	public void someEntryUnitsRemainAndAreRemoved()
	{
		Iterator<GbgUnit> units = game.getUnitsAt(makeCoordinate(7, 28)).iterator();
		game.moveUnit(units.next(), makeCoordinate(7, 28), makeCoordinate(5, 28));
		game.moveUnit(units.next(), makeCoordinate(7, 28), makeCoordinate(6, 28));
		Collection<GbgUnit> remaining = game.getUnitsAt(makeCoordinate(7, 28));
		//System.out.println("hiiiiii");
		assertEquals(2, remaining.size());
		
		game.endStep();
		remaining = game.getUnitsAt(makeCoordinate(7, 28));
		
		assertTrue(remaining == null || remaining.isEmpty());
	}
	
	@Test(expected=GbgInvalidMoveException.class)
	public void tryToMoveThroughEnemyZOC()
	{
		testGame.clearBoard();
		testGame.setGameTurn(2);
		testGame.putUnitAt(heth, 10, 10, SOUTH);		// ZOC = [(9, 11), (10, 11), (11, 11)]
		testGame.putUnitAt(hampton, 13, 10, SOUTH);	// ZOC = [(12, 11), (13, 11), (14, 11)]
		testGame.putUnitAt(devin, 11, 12, SOUTH);
		testGame.setGameStep(UMOVE);
		game.moveUnit(devin, makeCoordinate(11, 12), makeCoordinate(11, 9));
	}
	
	// Battle tests
	
	@Test
	public void hethDefeatsDevin()
	{
		testGame.clearBoard();
		testGame.setGameTurn(2);
		testGame.putUnitAt(heth, 5, 5, SOUTH);
		testGame.putUnitAt(devin, 5, 7, SOUTH);
		testGame.setGameStep(CMOVE);
		game.moveUnit(heth, makeCoordinate(5,5), makeCoordinate(5, 6)); //4/1=4
		game.endStep();		// CBATTLE
		BattleDescriptor battle = game.getBattlesToResolve().iterator().next();	// Only 1
		GettysburgEngine engine = (GettysburgEngine) game;
		engine.setRandomNum(1);
		assertEquals(BattleResult.DELIM, game.resolveBattle(battle).getBattleResult());
	}
	
	@Test
	public void hethDefeatsDevinAndGamble()
	{
		testGame.clearBoard();
		testGame.setGameTurn(2);
		testGame.putUnitAt(heth, 5, 5, SOUTH);
		testGame.putUnitAt(devin, 5, 7, SOUTH);
		testGame.putUnitAt(gamble, 6, 7, SOUTH);
		testGame.setGameStep(CMOVE);
		game.moveUnit(heth, makeCoordinate(5,5), makeCoordinate(5, 6));
		game.endStep();		// CBATTLE
		GettysburgEngine engine = (GettysburgEngine) game;
		engine.setRandomNum(1);
		BattleDescriptor battle = game.getBattlesToResolve().iterator().next();	// Only 1
		assertEquals(BattleResult.DELIM, game.resolveBattle(battle).getBattleResult());
	}
	
	@Test
	public void twoBattles()
	{
		testGame.clearBoard();
		testGame.setGameTurn(2);
		testGame.putUnitAt(heth, 5, 5, SOUTH);
		testGame.putUnitAt(devin, 5, 7, SOUTH);
		testGame.putUnitAt(rodes, 20, 20, NORTH);
		testGame.putUnitAt(schurz, 20, 18, SOUTHWEST);
		testGame.setGameStep(CMOVE);
		game.moveUnit(heth, makeCoordinate(5,5), makeCoordinate(5, 6));
		game.moveUnit(rodes, makeCoordinate(20, 20), makeCoordinate(20, 19));
		game.endStep();		// CBATTLE
		Collection<BattleDescriptor> battles = game.getBattlesToResolve();
		BattleDescriptor battle = battles.iterator().next();
		if (battles.size() == 1) {
			assertTrue(battle.getAttackers().contains(heth)); 
		} else {
			assertTrue(battle.getAttackers().contains(heth) || battle.getAttackers().contains(rodes));
		}
	}
	
	@Test
	public void fightTwoBattles()
	{
		testGame.clearBoard();
		testGame.setGameTurn(2);
		testGame.setGameStep(UMOVE);
		testGame.putUnitAt(heth, 5, 5, SOUTH);
		testGame.putUnitAt(rowley, 5, 7, NORTH);
		testGame.putUnitAt(hampton, 18, 18, EAST);
		testGame.putUnitAt(gamble, 20, 18, WEST);
		TestBattleDescriptor bd = new TestBattleDescriptor();
		bd.addAttacker(rowley);
		bd.addDefender(heth);
		TestBattleDescriptor bd1 = new TestBattleDescriptor();
		bd1.addAttacker(gamble);
		bd1.addDefender(hampton);
		game.moveUnit(rowley, makeCoordinate(5, 7), makeCoordinate(5, 6)); //3/4 = 0.75
		game.moveUnit(gamble, makeCoordinate(20, 18), makeCoordinate(19, 18)); // 1/1 = 1
		game.endStep();	// UBATTLE
		GettysburgEngine engine = (GettysburgEngine) game;
		engine.setRandomNum(2);
		assertEquals(BattleResult.EXCHANGE, game.resolveBattle(bd).getBattleResult());
		engine.setRandomNum(5);
		assertEquals(BattleResult.EXCHANGE, game.resolveBattle(bd1).getBattleResult());
	}
	
	@Test
	public void attackerGetsEliminated()
	{
		testGame.clearBoard();
		testGame.setGameTurn(2);
		testGame.putUnitAt(heth, 5, 5, SOUTH);
		testGame.putUnitAt(devin, 5, 7, NORTH);
		testGame.setGameStep(UMOVE);
		assertEquals(heth, game.getUnitsAt(makeCoordinate(5, 5)).iterator().next());
		game.moveUnit(devin, makeCoordinate(5,7), makeCoordinate(5, 6));
		game.endStep();		// UBATTLE
		GettysburgEngine engine = (GettysburgEngine) game;
		engine.setRandomNum(5);
		BattleDescriptor battle = game.getBattlesToResolve().iterator().next();	// Only 1
		assertEquals(BattleResult.AELIM, game.resolveBattle(battle).getBattleResult());
	}
	
	@Test(expected=GbgInvalidActionException.class)
	public void unitTriesToFightTwoBattesInSameTurn()
	{
		testGame.clearBoard();
		testGame.setGameTurn(2);
		testGame.setGameStep(UMOVE);
		testGame.putUnitAt(heth, 5, 5, SOUTH);
		testGame.putUnitAt(hampton, 7, 5, SOUTH);
		testGame.putUnitAt(schurz, 6, 7, NORTH);
		game.moveUnit(schurz, makeCoordinate(6, 7), makeCoordinate(6, 6));
		game.endStep();	// UBATTLE
		TestBattleDescriptor bd = new TestBattleDescriptor();
		bd.addAttacker(schurz);
		bd.addDefender(heth);
		TestBattleDescriptor bd1 = new TestBattleDescriptor();
		bd1.addAttacker(schurz);
		bd1.addDefender(hampton);
		game.resolveBattle(bd);
		game.resolveBattle(bd1);
	}
	
	@Test(expected=Exception.class)
	public void notAllUnitsHaveFoughtAtEndOfBattleStep()
	{
		testGame.clearBoard();
		testGame.setGameTurn(2);
		testGame.setGameStep(UMOVE);
		testGame.putUnitAt(heth, 5, 5, SOUTH);
		testGame.putUnitAt(hampton, 7, 5, SOUTH); //conf
		testGame.putUnitAt(schurz, 6, 7, NORTH);
		game.moveUnit(schurz, makeCoordinate(6, 7), makeCoordinate(6, 6));
		game.endStep();	// UBATTLE
		TestBattleDescriptor bd = new TestBattleDescriptor();
		bd.addAttacker(schurz); //union
		bd.addDefender(heth); //conf
			game.resolveBattle(bd);
		game.endStep(); 	// CMOVE: hampton did not engage
	}
	
	@Test(expected=Exception.class)
	public void notAllAttackersCanFight()
	{
		testGame.clearBoard();
		testGame.setGameTurn(2);
		testGame.setGameStep(UMOVE);
		testGame.putUnitAt(heth, 22, 5, SOUTH);
		testGame.putUnitAt(hampton, 7, 5, SOUTH);
		testGame.putUnitAt(schurz, 6, 7, NORTH);
		game.moveUnit(schurz, makeCoordinate(6, 7), makeCoordinate(6, 6));
		game.endStep();	// UBATTLE
		TestBattleDescriptor bd = new TestBattleDescriptor();
		bd.addAttacker(schurz);
		bd.addDefender(heth);
		game.resolveBattle(bd);		// heth is not in schurz' ZOC
	}
	@Test(expected=Exception.class)
	public void notAllDefendersCanFight()
	{
		testGame.clearBoard();
		testGame.setGameTurn(2);
		testGame.setGameStep(CMOVE);
		testGame.putUnitAt(heth, 4, 4, SOUTH);
		testGame.putUnitAt(devin, 5, 5, WEST);
		testGame.putUnitAt(gamble, 5, 6, NORTHWEST);
		testGame.putUnitAt(rowley, 4, 6, NORTH);
		TestBattleDescriptor bd = new TestBattleDescriptor();
		bd.addAttacker(heth);
		bd.addDefender(gamble);
		bd.addDefender(devin);
		bd.addDefender(rowley);
		game.moveUnit(heth, makeCoordinate(4, 4), makeCoordinate(4, 5)); //3/4 = 0.75
		game.endStep();	// CBATTLE
		GettysburgEngine engine = (GettysburgEngine) game;
		engine.setRandomNum(2);
		game.resolveBattle(bd).getBattleResult();
	}
	@Test
	public void attackersRetreat()
	{	
		testGame.clearBoard();
		testGame.setGameTurn(2);
		testGame.putUnitAt(heth, 5, 5, SOUTH); //conf
		testGame.putUnitAt(devin, 5, 7, NORTH); //union
		testGame.setGameStep(UMOVE);
		assertEquals(heth, game.getUnitsAt(makeCoordinate(5, 5)).iterator().next());
		game.moveUnit(devin, makeCoordinate(5,7), makeCoordinate(5, 6));
		game.endStep();		// UBATTLE
		BattleDescriptor battle = game.getBattlesToResolve().iterator().next();	// Only 1
		GettysburgEngine engine = (GettysburgEngine) game;
		engine.setRandomNum(2);
		assertEquals(BattleResult.ABACK, game.resolveBattle(battle).getBattleResult());
	}
	
	@Test
	public void locationRetreatedAttackers()
	{
		testGame.clearBoard();
		testGame.setGameTurn(2);
		testGame.putUnitAt(heth, 5, 5, SOUTH); //conf
		testGame.putUnitAt(devin, 5, 7, NORTH); //union
		testGame.setGameStep(UMOVE);
		assertEquals(heth, game.getUnitsAt(makeCoordinate(5, 5)).iterator().next());
		game.moveUnit(devin, makeCoordinate(5,7), makeCoordinate(5, 6));
		game.endStep();		// UBATTLE
		BattleDescriptor battle = game.getBattlesToResolve().iterator().next();	// Only 1
		GettysburgEngine engine = (GettysburgEngine) game;
		engine.setRandomNum(2);
		assertEquals(BattleResult.ABACK, game.resolveBattle(battle).getBattleResult());
		assertEquals(makeCoordinate(4,5),game.whereIsUnit(devin));
	}
	@Test
	public void removeEXCHANGEUnits()
	{
		testGame.clearBoard();
		testGame.setGameTurn(2);
		testGame.setGameStep(CMOVE);
		testGame.putUnitAt(heth, 4, 4, SOUTHEAST);
		testGame.putUnitAt(devin, 5, 5, WEST);
		testGame.putUnitAt(gamble, 5, 6, NORTHWEST);
		testGame.putUnitAt(rowley, 4, 6, NORTH);
		TestBattleDescriptor bd = new TestBattleDescriptor();
		bd.addAttacker(heth);
		bd.addDefender(gamble);
		bd.addDefender(devin);
		bd.addDefender(rowley);
		game.moveUnit(heth, makeCoordinate(4, 4), makeCoordinate(4, 5)); //3/4 = 0.75
		game.endStep();	// CBATTLE
		GettysburgEngine engine = (GettysburgEngine) game;
		engine.setRandomNum(2);
		BattleResolution br = game.resolveBattle(bd);
		assertEquals(BattleResult.EXCHANGE, br.getBattleResult());
		assertNull(br.getActiveConfederateUnits());
		assertEquals(bd.getAttackers(),br.getEliminatedConfederateUnits());
		assertNull(game.whereIsUnit(rowley));
		assertTrue(game.whereIsUnit(devin) == null || game.whereIsUnit(gamble)==null);
	}
	@Test
	public void removeEXCHANGEUnits2()
	{
		testGame.clearBoard();
		testGame.setGameTurn(2);
		GbgUnit wadsworth = GbgUnitImpl.makeUnit(UNION, 3, NORTH, "Wadsworth", 2, DIVISION, INFANTRY);
		GbgUnit robinson = GbgUnitImpl.makeUnit(UNION, 3, NORTH, "robinson", 2, DIVISION, INFANTRY);
		//testGame.setGameStep(CMOVE);
		testGame.putUnitAt(heth, 4, 5, WEST);
		testGame.putUnitAt(wadsworth, 5, 5, WEST);
		testGame.putUnitAt(robinson, 5, 6, NORTHWEST);
		testGame.putUnitAt(rowley, 4, 6, NORTH);
		TestBattleDescriptor bd = new TestBattleDescriptor();
		bd.addDefender(heth);
		bd.addAttacker(wadsworth);
		bd.addAttacker(robinson);
		bd.addAttacker(rowley);
//		game.moveUnit(heth, makeCoordinate(4, 4), makeCoordinate(4, 5)); 
		game.endStep();	// UBATTLE 5/4 = 1.25
		GettysburgEngine engine = (GettysburgEngine) game;
		engine.setRandomNum(2); //and 5 -> exchange
		game.resolveBattle(bd).getBattleResult();
		assertNull(game.whereIsUnit(heth));
	//	assertNull(game.whereIsUnit(rowley));
		assertTrue(game.whereIsUnit(wadsworth) == null || game.whereIsUnit(rowley) == null || game.whereIsUnit(robinson) == null);
//		assertNull(game.whereIsUnit(robinson));
	}
	
	@Test(expected = Exception.class)
	public void moveToOccupiedSquare()
	{
		testGame.clearBoard();
		testGame.putUnitAt(heth, 4, 4, SOUTH);
		testGame.putUnitAt(devin, 5, 5, WEST);
		game.moveUnit(devin, makeCoordinate(5,5), makeCoordinate(4,4));
	}
	@Test(expected=GbgInvalidMoveException.class)
	public void unitNotMoving()
	{
		game.moveUnit(gamble, makeCoordinate(11, 11), makeCoordinate(11, 11));
	}
	@Test(expected=GbgInvalidMoveException.class)
	public void devinTriesToSetFacingTwice()
	{
		game.setUnitFacing(devin, NORTHWEST);
		game.moveUnit(devin, makeCoordinate(13, 9), makeCoordinate(13, 10));
		game.setUnitFacing(devin, SOUTHEAST);
	}
	@Test(expected=GbgInvalidMoveException.class)
	public void attemptToMoveUnitTwiceInOneTurn()
	{
		game.moveUnit(gamble, makeCoordinate(11, 11), makeCoordinate(12, 10));
		game.moveUnit(gamble, makeCoordinate(12, 10), makeCoordinate(12, 11));
	}
	@Test(expected=GbgInvalidMoveException.class)
	public void moveFromEmptySquare()
	{
		game.moveUnit(gamble, makeCoordinate(11, 11), makeCoordinate(11, 10));
		game.moveUnit(gamble, makeCoordinate(10, 11), makeCoordinate(11, 11));
	}
	@Test(expected=GbgInvalidMoveException.class)
	public void enemyMovesToItsZOC()
	{
		testGame.clearBoard();
		//testGame.setGameTurn(turn);
		testGame.putUnitAt(heth, 10, 11, WEST);		
		testGame.putUnitAt(hampton, 12, 10, EAST);	
		testGame.putUnitAt(devin, 11, 13, NORTH);
		testGame.setGameStep(UMOVE);
		game.moveUnit(devin, makeCoordinate(11, 12), makeCoordinate(11, 9));
	}
	
	List<BattleResult> brList = new ArrayList<BattleResult>();
	
	@Test
	public void testSetBattleResult(){
	//	brList.add(BattleResult.EXCHANGE);
		brList.add(BattleResult.DBACK);
		
		testGame.clearBoard();
		testGame.setGameTurn(2);
		testGame.setBattleResults(brList);
		testGame.putUnitAt(devin, 5, 5, SOUTH); //conf
		testGame.putUnitAt(heth, 5, 7, NORTH); //union
		testGame.setGameStep(CMOVE);
		game.endStep();		// CBATTLE
		TestBattleDescriptor bd = new TestBattleDescriptor();
		bd.addAttacker(heth);
		bd.addDefender(devin);
		game.resolveBattle(bd);
		assertEquals(makeCoordinate(4,4) ,game.whereIsUnit(devin) );	
	}
	
	@Test
	public void getUnit() {
		assertEquals(devin,game.getUnit("Devin", UNION));
	}
	
	@Test
	public void attackersElim() {
		testGame.clearBoard();
		testGame.setGameTurn(2);
		testGame.putUnitAt(heth, 4, 4, SOUTH);
		testGame.putUnitAt(hampton, 3, 4, SOUTH);
		testGame.putUnitAt(devin, 4, 5, NORTH);
		TestBattleDescriptor bd = new TestBattleDescriptor();
		bd.addAttacker(devin);
		bd.addDefender(heth);
		bd.addDefender(hampton);
		game.endStep();	// UBATTLE 1/4 = 0.25
		GettysburgEngine engine = (GettysburgEngine) game;
		engine.setRandomNum(2);
		BattleResolution br = game.resolveBattle(bd);
		assertEquals(BattleResult.AELIM,br.getBattleResult());
		assertNull(br.getActiveUnionUnits());
		assertEquals(bd.getAttackers(),br.getEliminatedUnionUnits());
	}
	@Test
	public void attackersElim2() {
		testGame.clearBoard();
		testGame.setGameTurn(2);
		testGame.putUnitAt(heth, 4, 4, SOUTH);
		testGame.putUnitAt(hampton, 3, 4, SOUTH);
		testGame.putUnitAt(devin, 4, 5, NORTH);
		TestBattleDescriptor bd = new TestBattleDescriptor();
		bd.addAttacker(devin);
		bd.addDefender(heth);
		bd.addDefender(hampton);
		game.endStep();	// UBATTLE 1/5 = 0.2
		GettysburgEngine engine = (GettysburgEngine) game;
		engine.setRandomNum(5);
		assertEquals(BattleResult.AELIM,game.resolveBattle(bd).getBattleResult());
	}
	@Test
	public void attackersElim3() {
		testGame.clearBoard();
		testGame.setGameTurn(2);
		testGame.putUnitAt(heth, 4, 4, SOUTH);
		testGame.putUnitAt(hampton, 3, 4, SOUTH);
		testGame.putUnitAt(devin, 4, 5, NORTH);
		TestBattleDescriptor bd = new TestBattleDescriptor();
		bd.addAttacker(devin);
		bd.addDefender(heth);
		bd.addDefender(hampton);
		game.endStep();	// UBATTLE 1/5 = 0.2
		GettysburgEngine engine = (GettysburgEngine) game;
		engine.setRandomNum(6);
		assertEquals(BattleResult.AELIM,game.resolveBattle(bd).getBattleResult());
	}
	@Test
	public void attackersRetreat2() {
		testGame.clearBoard();
		testGame.setGameTurn(2);
		testGame.putUnitAt(heth, 4, 4, SOUTH);
		testGame.putUnitAt(hampton, 3, 4, SOUTH);
		testGame.putUnitAt(devin, 4, 5, NORTH);
		TestBattleDescriptor bd = new TestBattleDescriptor();
		bd.addAttacker(devin);
		bd.addDefender(heth);
		bd.addDefender(hampton);
		game.endStep();	// UBATTLE 1/5 = 0.2
		GettysburgEngine engine = (GettysburgEngine) game;
		engine.setRandomNum(1);
		assertEquals(BattleResult.ABACK,game.resolveBattle(bd).getBattleResult());
	}
	@Test
	public void attackersRetreat3() {
		testGame.clearBoard();
		testGame.setGameTurn(2);
		testGame.putUnitAt(heth, 4, 4, SOUTH);
		testGame.putUnitAt(hampton, 3, 4, SOUTH);
		testGame.putUnitAt(devin, 4, 5, NORTH);
		TestBattleDescriptor bd = new TestBattleDescriptor();
		bd.addAttacker(devin);
		bd.addDefender(heth);
		bd.addDefender(hampton);
		game.endStep();	// UBATTLE 1/5 = 0.2
		GettysburgEngine engine = (GettysburgEngine) game;
		engine.setRandomNum(3);
		assertEquals(BattleResult.ABACK,game.resolveBattle(bd).getBattleResult());
	}
	@Test
	public void attackersRetreat4() {
		testGame.clearBoard();
		testGame.setGameTurn(2);
		testGame.putUnitAt(heth, 4, 4, SOUTH);
		testGame.putUnitAt(hampton, 3, 4, SOUTH);
		testGame.putUnitAt(devin, 4, 5, NORTH);
		TestBattleDescriptor bd = new TestBattleDescriptor();
		bd.addAttacker(devin);
		bd.addDefender(heth);
		bd.addDefender(hampton);
		game.endStep();	// UBATTLE 1/5 = 0.2
		GettysburgEngine engine = (GettysburgEngine) game;
		engine.setRandomNum(4);
		assertEquals(BattleResult.ABACK,game.resolveBattle(bd).getBattleResult());
	}
	
	@Test
	public void defendersElim()
	{
		testGame.clearBoard();
		testGame.setGameTurn(2);
		testGame.putUnitAt(heth, 5, 5, SOUTH);
		testGame.putUnitAt(devin, 5, 7, SOUTH);
		testGame.setGameStep(CMOVE);
		game.moveUnit(heth, makeCoordinate(5,5), makeCoordinate(5, 6)); //4/1=4
		game.endStep();		// CBATTLE
		BattleDescriptor battle = game.getBattlesToResolve().iterator().next();	// Only 1
		GettysburgEngine engine = (GettysburgEngine) game;
		engine.setRandomNum(3);
		assertEquals(BattleResult.DELIM, game.resolveBattle(battle).getBattleResult());
	}
	@Test
	public void defendersElim2()
	{
		testGame.clearBoard();
		testGame.setGameTurn(2);
		testGame.putUnitAt(heth, 5, 5, SOUTH);
		testGame.putUnitAt(devin, 5, 7, SOUTH);
		testGame.setGameStep(CMOVE);
		game.moveUnit(heth, makeCoordinate(5,5), makeCoordinate(5, 6)); //4/1=4
		game.endStep();		// CBATTLE
		BattleDescriptor battle = game.getBattlesToResolve().iterator().next();	// Only 1
		GettysburgEngine engine = (GettysburgEngine) game;
		engine.setRandomNum(6);
		assertEquals(BattleResult.DELIM, game.resolveBattle(battle).getBattleResult());
	}
	@Test
	public void defendersRetreat()
	{
		testGame.clearBoard();
		testGame.setGameTurn(2);
		testGame.putUnitAt(heth, 5, 5, SOUTH);
		testGame.putUnitAt(devin, 5, 7, SOUTH);
		testGame.setGameStep(CMOVE);
		game.moveUnit(heth, makeCoordinate(5,5), makeCoordinate(5, 6)); //4/1=4
		game.endStep();		// CBATTLE
		BattleDescriptor battle = game.getBattlesToResolve().iterator().next();	// Only 1
		GettysburgEngine engine = (GettysburgEngine) game;
		engine.setRandomNum(4);
		assertEquals(BattleResult.DBACK, game.resolveBattle(battle).getBattleResult());
	}
	@Test
	public void defendersRetreat2()
	{
		testGame.clearBoard();
		testGame.setGameTurn(2);
		testGame.putUnitAt(heth, 5, 5, SOUTH);
		testGame.putUnitAt(devin, 5, 7, SOUTH);
		testGame.setGameStep(CMOVE);
		game.moveUnit(heth, makeCoordinate(5,5), makeCoordinate(5, 6)); //4/1=4
		game.endStep();		// CBATTLE
		BattleDescriptor battle = game.getBattlesToResolve().iterator().next();	// Only 1
		GettysburgEngine engine = (GettysburgEngine) game;
		engine.setRandomNum(5);
		assertEquals(BattleResult.DBACK, game.resolveBattle(battle).getBattleResult());
	}
	@Test(expected = GbgInvalidActionException.class)
	public void callResolveNotInBattleStep()
	{
		testGame.clearBoard();
		testGame.setGameTurn(2);
		testGame.putUnitAt(heth, 5, 6, SOUTH);
		testGame.putUnitAt(devin, 5, 7, NORTH);
	//	game.moveUnit(heth, makeCoordinate(5, 5), makeCoordinate(5, 6));
		TestBattleDescriptor bd = new TestBattleDescriptor();
		bd.addAttacker(heth);
		bd.addDefender(devin);
		game.resolveBattle(bd);
	}	
	
	@Test(expected = GbgInvalidActionException.class)
	public void notConfTurnToBattle()
	{
		testGame.clearBoard();
		testGame.setGameTurn(2);
		testGame.putUnitAt(heth, 5, 6, SOUTH);
		testGame.putUnitAt(devin, 5, 7, NORTH);
		game.endStep(); //UBATTLE
		TestBattleDescriptor bd = new TestBattleDescriptor();
		bd.addAttacker(heth); //CONF
		bd.addDefender(devin);
		game.resolveBattle(bd);
	}	
	@Test(expected = GbgInvalidActionException.class)
	public void notUnionTurnToBattle()
	{
		testGame.clearBoard();
		testGame.setGameTurn(2);
		testGame.putUnitAt(heth, 5, 6, SOUTH);
		testGame.putUnitAt(devin, 5, 7, NORTH);
		testGame.setGameStep(CMOVE);
		game.endStep(); //CBATTLE
		TestBattleDescriptor bd = new TestBattleDescriptor();
		bd.addAttacker(devin); //CONF
		bd.addDefender(heth);
		game.resolveBattle(bd);
	}	
	
	@Test
	public void coorToString() {
		makeCoordinate(5,5).toString();
	}
}

class TestCoordinate implements Coordinate
{
	private int x, y;
	
	/**
	 * @return the x
	 */
	public int getX()
	{
		return x;
	}

	/**
	 * @return the y
	 */
	public int getY()
	{
		return y;
	}

	public TestCoordinate(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
}

