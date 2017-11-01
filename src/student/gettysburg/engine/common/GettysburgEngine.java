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

import java.util.*;
import java.util.Map.Entry;

import gettysburg.common.*;
import gettysburg.common.exceptions.GbgInvalidActionException;
import gettysburg.common.exceptions.GbgInvalidMoveException;
import student.gettysburg.engine.utility.configure.BattleOrder;
import student.gettysburg.engine.utility.configure.UnitInitializer;
import static student.gettysburg.engine.common.CoordinateImpl.*;
import static gettysburg.common.ArmyID.*;
import static gettysburg.common.Direction.*;
import static gettysburg.common.GbgGameStep.*;
import static gettysburg.common.GbgGameStatus.*;
import static gettysburg.common.UnitSize.*;
import static gettysburg.common.UnitType.*;
import static gettysburg.common.BattleResult.*;
//import student.gettysburg.engine.common.GbgUnitImpl;
/**
 * This is the game engine master class that provides the interface to the game
 * implementation. DO NOT change the name of this file and do not change the
 * name ofthe methods that are defined here since they must be defined to implement the
 * GbgGame interface.
 * 
 * @version Jun 9, 2017
 */
public class GettysburgEngine implements GbgGame
{
	protected GbgGameStep curStep;
	GbgGameStatus curStat;
	protected int turn;
	Collection<GbgUnit> canMove;
	public static HashMap<GbgUnit,Coordinate> board = new HashMap<GbgUnit,Coordinate>();
	Coordinate finalDestination;
	Collection<GbgUnit> attackers = new ArrayList<GbgUnit>();
	Collection<GbgUnit> defenders = new ArrayList<GbgUnit>();
	boolean hasResolvedBattles;
	Collection<BattleDescriptor> unitsSupposedToBattle = new ArrayList<BattleDescriptor>();
	public GettysburgEngine() {
		board.clear();
		curStep = UMOVE;
		curStat = IN_PROGRESS;
		turn = 1;
		initUnits();
		initPackage();
	}
	/**
	 * Initialize or reset variables when a new game is created
	 */
	public void initPackage() {
		resetHasMoved();
		resetHasChangedFace();
		resetHasFought();
		hasResolvedBattles = false;
		randNum = 0;
		resultPredetermined = false;
	}
	
	/**Init units when a new game is called*/
	public void initUnits() {	
		UnitInitializer[] conf = BattleOrder.getConfederateBattleOrder();
		UnitInitializer[] union = BattleOrder.getUnionBattleOrder();
		int i=0;
		for(i=0; i < conf.length; i++ ) {
			if (conf[i].turn <= turn) {
				board.put(conf[i].unit,conf[i].where);
			}
		}
		for(i=0; i < union.length; i++ ) {
			if (union[i].turn <= turn) {
				board.put(union[i].unit,union[i].where);
			}
		}
		setUnitZone();
	}
	public void initAtTurn() {
		UnitInitializer[] conf = BattleOrder.getConfederateBattleOrder();
		UnitInitializer[] union = BattleOrder.getUnionBattleOrder();
		int i=0;
		for(i=0; i < conf.length; i++ ) {
			if (conf[i].turn == turn) {
				board.put(conf[i].unit,conf[i].where);
			}
		}
		for(i=0; i < union.length; i++ ) {
			if (union[i].turn == turn) {
				board.put(union[i].unit,union[i].where);
			}
		}
		setUnitZone();
	}
	/*
	 * @see gettysburg.common.GbgGame#endStep()
	 * get current step
	 */
	@Override
	public GbgGameStep endStep()
	{
		switch(curStep) {
			case UMOVE:
				curStep = UBATTLE;
				isStacking();
				resetHasFought();
				unitsSupposedToBattle = getBattlesToResolve();
				return curStep;
			case UBATTLE:
			 isAllUnitsHaveFought();	
			 curStep = CMOVE;
				return curStep;
			case CMOVE:
				curStep = CBATTLE;
				isStacking();
				resetHasFought();
				unitsSupposedToBattle = getBattlesToResolve();
				return curStep;
			case CBATTLE:
				isAllUnitsHaveFought();
				turn += 1;
				resetHasMoved();
				resetHasChangedFace();
				curStep = UMOVE;
				initAtTurn();
				return curStep;
		}
		return null;
	}
	
	
	
	/*
	 * @see gettysburg.common.GbgGame#getBattlesToResolve()
	 */
	@Override
	public Collection<BattleDescriptor> getBattlesToResolve()
	{	
//		if (curStep != CBATTLE && curStep != UBATTLE) {
//			throw new GbgInvalidActionException("Not in battle step");
//		}
		Collection<BattleDescriptor> battDes = new ArrayList<BattleDescriptor>();
		Iterator<Entry<GbgUnit,Coordinate>> i = board.entrySet().iterator();
		ArmyID attacker = null;
		if (curStep == CBATTLE) {
			attacker = CONFEDERATE;
		}
		if (curStep == UBATTLE) {
			attacker = UNION;
		}
		//loop through all units on board to check if any unit is attackers/defenders
		while (i.hasNext()) {
			Map.Entry<GbgUnit,Coordinate>  aUnit = (Map.Entry< GbgUnit,Coordinate>) i.next();
			
			//
			if(aUnit.getKey().getArmy().equals(attacker)) {
				isBattle(aUnit.getKey(), aUnit.getValue());
			}
			
		}
		battDes.add(new BattleDescriptorImpl(attackers,defenders));
		return battDes;
	}

	/*
	 * @see gettysburg.common.GbgGame#getCurrentStep()
	 */
	@Override
	public GbgGameStep getCurrentStep()
	{
		return curStep;
	}
	
	/*
	 * @see gettysburg.common.GbgGame#getGameStatus()
	 */
	@Override
	public GbgGameStatus getGameStatus()
	{
		return curStat;
	}
	

	/*
	 * @see gettysburg.common.GbgGame#getTurnNumber()
	 */
	@Override
	public int getTurnNumber()
	{
		return turn;
	}

	/*
	 * @see gettysburg.common.GbgGame#getUnit(String, Army)
	 */
	@Override
	public GbgUnit getUnit(String leader, ArmyID army) {
		Iterator<Entry< GbgUnit,Coordinate>> i = board.entrySet().iterator();
		while (i.hasNext()) {
			Map.Entry<GbgUnit,Coordinate>  aUnit = (Map.Entry< GbgUnit,Coordinate>) i.next();
			if (aUnit.getKey().getLeader().equals(leader) && (aUnit.getKey().getArmy().equals(army))) {
				return aUnit.getKey();
			}
		}
		return null;
	}
	
	/*
	 * @see gettysburg.common.GbgGame#getUnitFacing(int)
	 */
	@Override
	public Direction getUnitFacing(GbgUnit unit)
	{
		return unit.getFacing();
	}

	/*
	 * @see gettysburg.common.GbgGame#getUnitsAt(gettysburg.common.Coordinate)
	 */
	@Override
	public Collection<GbgUnit> getUnitsAt(Coordinate where)
	{
		Iterator<Entry< GbgUnit,Coordinate>> i = board.entrySet().iterator();
		Collection<GbgUnit> foundUnits = new ArrayList<GbgUnit>();	
		while (i.hasNext()) {
			Map.Entry<GbgUnit,Coordinate>  aUnit = (Map.Entry< GbgUnit,Coordinate>) i.next();
				if (aUnit.getValue().equals(where)) {
				foundUnits.add(aUnit.getKey());
			}
		}
		if (!foundUnits.isEmpty()) {
			return foundUnits;
		} return null;
	}
	/**
	 * Get all units whose zone is being inspected 
	 * The purpose is to check if the moving unit is moving into any enemy's ZOC
	 * @param location that is being inspected
	 * @return a collection of units whose zone is being inspected
	 */
	public Collection<GbgUnit> getUnitsAtZone(Coordinate where)
	{
		//loop through all units on board
		Iterator<Entry< GbgUnit,Coordinate>> i = board.entrySet().iterator();
		Collection<GbgUnit> foundUnits = new ArrayList<GbgUnit>();	
		while (i.hasNext()) {
			Map.Entry<GbgUnit,Coordinate>  aUnit = (Map.Entry<GbgUnit,Coordinate>) i.next();
			GbgUnitImpl gbgUnit = (GbgUnitImpl) aUnit.getKey();
			//should not be checking itself at the its location.
			if (gbgUnit.getZone().contains(where)) {
				foundUnits.add(gbgUnit);
			}
		} return foundUnits;

	}
	
	/**
	 * Check if all units have fought, after resolved battle is called
	 * throw Action Exception if there's at least one unit did not engage
	 */
	public void isAllUnitsHaveFought() {
		Iterator<Entry<GbgUnit,Coordinate>> i = board.entrySet().iterator();
		if (!unitsSupposedToBattle.isEmpty()) {
		while (i.hasNext()) {
			Map.Entry<GbgUnit,Coordinate>  aUnit = (Map.Entry<GbgUnit,Coordinate>) i.next();
			GbgUnitImpl unit = (GbgUnitImpl) aUnit.getKey();
			if ((unitsSupposedToBattle.iterator().next().getAttackers().contains(unit)  || unitsSupposedToBattle.iterator().next().getDefenders().contains(unit)) && (!unit.getHasFought())) {
				System.out.println("Unit " + unit.getLeader()+" should have fought but did not");
				throw new GbgInvalidActionException("Unit " + unit.getLeader()+" should have fought but did not");
			}
		}
		}
	}
	
	/**check if unit is moving through enemy's ZOC
	 * @param moving unit
	 * @param where unit is moving into
	 * @return true if moving unit is moving to enemy's ZOC
	 */
	public boolean isInEnemysZone(GbgUnit unit, Coordinate to) {
		//this doesn't apply for source or destination coordinate
		if (to.equals(finalDestination)) {
			return false;
		}
		Collection<GbgUnit> unitsAtZone =  getUnitsAtZone(to);
		Iterator<GbgUnit> i = unitsAtZone.iterator();
		if (!unitsAtZone.isEmpty()) { 
			while (i.hasNext()) {
				GbgUnit enemy= i.next();
				if (!enemy.getArmy().equals(unit.getArmy())) {
					return true;
				}
			}
		}
		 
		return false;
	}
	
	/**check if enemy enters moving unit's ZOC
	 * @param moving unit
	 * @param location where moving unit at
	 * @return true if enemy enters moving unit's ZOC, otherwise false
	 */
	public boolean isEnemyInItsZone(GbgUnit unit, Coordinate to) {
		if (to.equals(finalDestination)) {
			return false;
		}
		Collection<Coordinate> unitsZOC = new ArrayList<Coordinate>();
		unitsZOC.addAll(returnZone(to, unit.getFacing()));
		GbgUnitImpl unitImp = (GbgUnitImpl) unit;
		Iterator<Entry< GbgUnit,Coordinate>> j = board.entrySet().iterator();
		while (j.hasNext()) {
			Map.Entry<GbgUnit,Coordinate>  aUnit = (Map.Entry<GbgUnit,Coordinate>) j.next();
			GbgUnitImpl enemy = (GbgUnitImpl) aUnit.getKey();
			if (!enemy.getArmy().equals(unitImp.getArmy()) && unitsZOC.contains(aUnit.getValue())) {
				return true;
			}
		}
		
		return false;
	}
	
	/**check if battle is trigger. this is only checking attackers
	 * @param unit is moving
	 * @param location of that moving unit
	 * @return true if enemy's in unit's zone
	 * */
	public void isBattle(GbgUnit movingUnit,Coordinate to) {
		//check if attacker has any enemy in its zone
		GbgUnitImpl movingUnit2 = (GbgUnitImpl) movingUnit;
		Iterator<Entry< GbgUnit,Coordinate>> i = board.entrySet().iterator();
		while (i.hasNext()) {
			Map.Entry<GbgUnit,Coordinate>  aUnit = (Map.Entry<GbgUnit,Coordinate>) i.next();
			GbgUnitImpl enemy = (GbgUnitImpl) aUnit.getKey();
			if (!enemy.getArmy().equals(movingUnit2.getArmy()) && movingUnit2.getZone().contains(aUnit.getValue())) {
				if (!attackers.contains(movingUnit2)) {
					attackers.add(movingUnit2);
				}
				if (!defenders.contains(enemy)) {
					defenders.add(enemy);
				}
			}
		}
		
		//check if defender is in any of attacker's zone
	}
	
	/**
	 * Check if units are stacking after turn 0
	 * remove units that are stacking
	 */
	public void isStacking() {
		if (turn > 0) {
			int x=1,y=1;
			//loop through the board and check every square
			for (x=1; x <= GbgBoard.COLUMNS; x++) {
				for (y=1; y<= GbgBoard.ROWS; y++) {
					Collection<GbgUnit> units = getUnitsAt(makeCoordinate(x,y));
					if (units!=null && units.size() > 1) {
						Iterator<GbgUnit> i = units.iterator();
						while (i.hasNext()) {
							GbgUnit aUnit = i.next();
								board.remove(aUnit);
						}
					
					}
				}
			}
		}
	}
	
	/**Is Unit's Turn to Change Face?
	 * used in setUnitFacing() 
	 * @param unit that is in turn
	 * @return true if current turn is that army's turn, otherwise false
	 * */
	private boolean isUnitsTurnChangeDirection(GbgUnit unit) {
		if (unit.getArmy().equals(UNION) && (curStep.equals(UMOVE) || curStep.equals(UBATTLE))) {
			 return true;
		} if (unit.getArmy().equals(CONFEDERATE) && (curStep.equals(CMOVE) || curStep.equals(CBATTLE))) {
			return true;
		} 
		return false;
	}
	
	/**Is Unit's Turn to Move? throw Move Exception if not
	 * used in moveUnit() 
	 * @param unit that is in turn
	 * */
	public void isTurn2Move(GbgUnit unit) {
		if (curStep == CMOVE && unit.getArmy() == UNION) {
			throw new GbgInvalidMoveException("Not UNION's turn");
		}
		if (curStep == UMOVE && unit.getArmy() == CONFEDERATE) {
			throw new GbgInvalidMoveException("Not CONFEDERDATE's turn");
		}	
	}
	/**Is Unit's Turn to Battle? throw Action Exception if not
	 * used in resolveBattle() 
	 * @param unit that is in turn
	 * */
	public void isTurn2Battle(GbgUnit unit) {
		if (curStep == CBATTLE && unit.getArmy() == UNION) {
			System.out.println("not Unions turn");
			throw new GbgInvalidActionException("Not UNION's turn");
		}
		if (curStep == UBATTLE && unit.getArmy() == CONFEDERATE) {
			System.out.println("not Confs turn");
			throw new GbgInvalidActionException("Not CONFEDERDATE's turn");
		}	
	}
	
	/**
	 * Check if the desired squared is occupied
	 * @param coordinate of that desired square
	 * @return true if square is occupied, otherwise false
	 */	public boolean isOccupied(Coordinate where) {
		Collection<GbgUnit> occupyingUnit = new ArrayList<GbgUnit>();	
		occupyingUnit = getUnitsAt(where);
		if (occupyingUnit != null) {
			return true;
		} 
		return false;
	}
 /** making sure that all of the attacking units actually can attack all of the defending units
	 * throw Action Exception if at least one attackers or defenders isn't valid for battle
	 * @param attacker list
	 * @param defender list
	 * */
	public void isValidBattleDescriptor(Collection<GbgUnit> attackers, Collection<GbgUnit> defenders) {
		Iterator<GbgUnit> aI = attackers.iterator();

		//check if every attacker's ZOC has a defender in it
		while (aI.hasNext()) {	
			GbgUnitImpl attacker = (GbgUnitImpl) aI.next();
			boolean isValid = false; //check if every attacker is valid to participate in battle
			Iterator<GbgUnit> dI = defenders.iterator();
			while (dI.hasNext()) {
				GbgUnitImpl defender = (GbgUnitImpl) dI.next();
				Coordinate whereDefenderis = board.get(defender);
				if (attacker.getZone().contains(whereDefenderis)) {
					isValid = true;
					break;
				}
			}
			if (isValid == false) {
				throw new GbgInvalidActionException("Not all attackers can participate in battle");
			}
		}
		//check if every defender is in at least on attacker's ZOC
		Iterator<GbgUnit> dI = defenders.iterator();
		while (dI.hasNext()) {
			GbgUnitImpl defender = (GbgUnitImpl) dI.next();
			Coordinate whereDefenderis = board.get(defender);
			boolean isValid = false; 
			aI = attackers.iterator();
			while (aI.hasNext()) {	
				GbgUnitImpl attacker = (GbgUnitImpl) aI.next();
				if (attacker.getZone().contains(whereDefenderis)) {
					isValid = true;
					break;
				}
			}
			if (isValid == false) {
				throw new GbgInvalidActionException("Not all defenders can participate in battle");
			}	
		}
	}
	

	/*
	 * @see gettysburg.common.GbgGame#moveUnit(gettysburg.common.GbgUnit, gettysburg.common.Coordinate, gettysburg.common.Coordinate)
	 */
	@Override
	public void moveUnit(GbgUnit unit, Coordinate from, Coordinate to)
	{	finalDestination=to;
		CoordinateImpl newFrom = makeCoordinate(from.getX(),from.getY());
		CoordinateImpl newTo = makeCoordinate(to.getX(),to.getY());
		
		//check if it's unit's turn to move
		isTurn2Move(unit);
		if (newFrom.equals(newTo)) {
			throw new GbgInvalidMoveException("Not moving");
		}
		//check if destination is occupied
		if (isOccupied(newTo)) {
			throw new GbgInvalidMoveException("Square is occupied");
		}
		int stepMade = pathFinder(unit, newFrom, newTo);
		//check if source coordinate is empty
		if (getUnitsAt(newFrom)==null) {
			throw new GbgInvalidMoveException("Square is empty");
		}
		if (!board.containsKey(unit)) {
			throw new GbgInvalidMoveException("unit doesn't exist");
		}
		
		if (stepMade > unit.getMovementFactor()) {
			throw new GbgInvalidMoveException("Attempted step exceeds allowed Movement steps");
		}
		if (stepMade == 0) {
			throw new GbgInvalidMoveException("No path found");
		}
		GbgUnitImpl gbgUnit = (GbgUnitImpl) unit;
		if (gbgUnit.getHasMoved()) {
			throw new GbgInvalidMoveException("Unit has moved once this turn");
		}

		board.put(gbgUnit,makeCoordinate(newTo.getX(),newTo.getY()));
		gbgUnit.setHasMoved(true);
		//set unit's zone after moving
		setUnitZone();
	}
	
	/**reset all HasMoved of units to False*/
	public void resetHasMoved() {
		Iterator<Entry< GbgUnit,Coordinate>> i = board.entrySet().iterator();
		while (i.hasNext()) {
			Map.Entry<GbgUnit,Coordinate>  aUnit = (Map.Entry<GbgUnit,Coordinate>) i.next();
			GbgUnitImpl gbgU = (GbgUnitImpl) aUnit.getKey();
			gbgU.setHasMoved(false);
		}
	}
	/**reset all HasChangedFace of units to False*/
	public void resetHasChangedFace() {
		Iterator<Entry<GbgUnit,Coordinate>> i = board.entrySet().iterator();
		while (i.hasNext()) {
			Map.Entry<GbgUnit,Coordinate>  aUnit = (Map.Entry< GbgUnit,Coordinate>) i.next();
			GbgUnitImpl gbgU = (GbgUnitImpl) aUnit.getKey();
			gbgU.setHasChangedFace(false);
		}
	}
	
	/**reset all HasFought of units to False*/
	public void resetHasFought() {
		Iterator<Entry<GbgUnit,Coordinate>> i = board.entrySet().iterator();
		while (i.hasNext()) {
			Map.Entry<GbgUnit,Coordinate>  aUnit = (Map.Entry< GbgUnit,Coordinate>) i.next();
			GbgUnitImpl gbgU = (GbgUnitImpl) aUnit.getKey();
			gbgU.setHasFought(false);
		}
	}
	/** Return ZOC of a given coordinate
	 * @param given coordinate
	 * @param facing direction
	 * @return collection of zone of control
	 * */
	public Collection<Coordinate> returnZone(Coordinate coor, Direction dir) {
		Collection<Coordinate> zone = new ArrayList<Coordinate>();
		int x = coor.getX(), y = coor.getY();
		switch (dir) {
			case NORTH:	
				zone.add(isWithinBorder(x-1,y-1)? makeCoordinate(x-1,y-1) : null);
				zone.add(isWithinBorder(x+1,y-1)? makeCoordinate(x+1,y-1): null);
				zone.add(isWithinBorder(x,y-1)? makeCoordinate(x,y-1):null);
				break;
			case SOUTH:
				zone.add(isWithinBorder(x-1,y+1)? makeCoordinate(x-1,y+1):null);
				zone.add(isWithinBorder(x+1,y+1)? makeCoordinate(x+1,y+1):null);
				zone.add(isWithinBorder(x,y+1)? makeCoordinate(x,y+1):null);
				break;
			case WEST:
				zone.add(isWithinBorder(x-1,y+1)? makeCoordinate(x-1,y+1):null);
				zone.add(isWithinBorder(x-1,y-1)? makeCoordinate(x-1,y-1):null);
				zone.add(isWithinBorder(x-1,y)? makeCoordinate(x-1,y):null);
				break;
			case EAST:
				zone.add(isWithinBorder(x+1,y+1)? makeCoordinate(x+1,y+1):null);
				zone.add(isWithinBorder(x+1,y-1)? makeCoordinate(x+1,y-1):null);
				zone.add(isWithinBorder(x+1,y)? makeCoordinate(x+1,y):null);
				break;
			case NORTHWEST:
				zone.add(isWithinBorder(x-1,y-1)? makeCoordinate(x-1,y-1):null);
				zone.add(isWithinBorder(x,y-1)? makeCoordinate(x,y-1):null);
				zone.add(isWithinBorder(x-1,y)? makeCoordinate(x-1,y):null);
				break;
			case NORTHEAST:
				zone.add(isWithinBorder(x+1,y-1)? makeCoordinate(x+1,y-1):null);
				zone.add(isWithinBorder(x,y-1)? makeCoordinate(x,y-1):null);
				zone.add(isWithinBorder(x+1,y)? makeCoordinate(x+1,y):null);
				break;
			case SOUTHWEST:
				zone.add(isWithinBorder(x-1,y+1)? makeCoordinate(x-1,y+1):null);
				zone.add(isWithinBorder(x,y+1)?makeCoordinate(x,y+1):null);
				zone.add(isWithinBorder(x-1,y)?makeCoordinate(x-1,y):null);
				break;
			case SOUTHEAST:
				zone.add(isWithinBorder(x+1,y+1)? makeCoordinate(x+1,y+1):null);
				zone.add(isWithinBorder(x,y+1)? makeCoordinate(x,y+1):null);
				zone.add(isWithinBorder(x+1,y)?makeCoordinate(x+1,y):null);
				break;
			}
		return zone;
	}
	Random rand = new Random(); 
	protected int randNum = 0;
	protected BattleResult br;
	protected boolean resultPredetermined;
	/*
	 * @see gettysburg.common.GbgGame#resolveBattle(int)
	 */
	@Override
	public BattleResolution resolveBattle(BattleDescriptor battle)
	{
		if (curStep != CBATTLE && curStep != UBATTLE) {
			throw new GbgInvalidActionException("Not in battle step");
		}
		//TODO if step is CONF but attacker UNION -> exception
		hasResolvedBattles = true;
		if (randNum == 0) {
			randNum = rand.nextInt(6) + 1;
		}
		
		Collection<GbgUnit> attackerList = battle.getAttackers();
		Collection<GbgUnit> defenderList = battle.getDefenders();
	
		float resultA = 0, resultD = 0;
		Iterator<GbgUnit> iA = attackerList.iterator();
		Iterator<GbgUnit> iD = defenderList.iterator();
		
		//if battle result isn't predetermined by client then check if attackers and defenders are valid
		if (resultPredetermined==false) {
			isValidBattleDescriptor(attackerList, defenderList);
			while (iA.hasNext()) {	
				GbgUnitImpl anAttacker = (GbgUnitImpl) iA.next();
				isTurn2Battle(anAttacker);
				if (anAttacker.getHasFought()) {
					throw new GbgInvalidActionException("Unit has fought this step");
				}
				resultA += anAttacker.getCombatFactor();
				anAttacker.setHasFought(true);
			}
			while (iD.hasNext()) {	
				GbgUnitImpl aDefender = (GbgUnitImpl) iD.next();
				if (aDefender.getHasFought()) {
					throw new GbgInvalidActionException("Unit has fought this step");
				}
				resultD += aDefender.getCombatFactor();
				aDefender.setHasFought(true);
			}
		
			br = setBattleResult(resultA / resultD, randNum);
		}
		boolean attackerIsUNION = false;
		Collection<GbgUnit> activeUnion = new ArrayList<GbgUnit>();
		Collection<GbgUnit> activeConf = new ArrayList<GbgUnit>();
		Collection<GbgUnit> elimUnion = new ArrayList<GbgUnit>();
		Collection<GbgUnit> elimConf = new ArrayList<GbgUnit>();
		if (attackerList.iterator().next().getArmy() == UNION) {
			attackerIsUNION = true;
		}
		if (br == DELIM) {
			eliminateUnits(defenderList);
			if (attackerIsUNION) {
				elimConf =	defenderList;
				elimUnion = null;
				activeUnion = attackerList;
				activeConf = null;
			} else {
				elimUnion =	defenderList;
				elimConf = null;
				activeConf = attackerList;
				activeUnion = null;
			}
			return new BattleResolutionImpl(DELIM,activeUnion,activeConf,elimUnion,elimConf);
		}
		if (br == AELIM) {
			eliminateUnits(attackerList);
			if (attackerIsUNION) {
				elimUnion = attackerList;
				elimConf = null;
				activeConf = defenderList;
				activeUnion = null;
			} else {
				elimConf =	attackerList;
				elimUnion = null;
				activeUnion = defenderList;
				activeConf = null;
			}
			return new BattleResolutionImpl(AELIM,activeUnion,activeConf,elimUnion,elimConf);
		}
		if (br == DBACK) {
			if (attackerIsUNION) {
				elimConf =	retreat(defenderList);
				elimUnion = null;
				activeUnion = attackerList;
				defenderList.removeAll(elimConf);
				activeConf = defenderList;
			} else {
				elimUnion =	retreat(defenderList);
				elimConf = null;
				activeConf = attackerList;
				defenderList.removeAll(elimUnion);
				activeUnion = defenderList;
			}
			return new BattleResolutionImpl(DBACK,activeUnion,activeConf,elimUnion,elimConf);
		} 
		if (br == ABACK) {
			if (attackerIsUNION) {
				elimUnion =	retreat(attackerList);
				elimConf = null;
				activeConf = defenderList;
				attackerList.removeAll(elimUnion);
				activeUnion = attackerList;
				
			} else {
				elimConf =	retreat(attackerList);
				elimUnion = null;
				activeUnion = defenderList;
				attackerList.removeAll(elimConf);
				activeConf = attackerList;
			}
			return new BattleResolutionImpl(ABACK,activeUnion,activeConf,elimUnion,elimConf);
		}
		if (br == EXCHANGE) {
			if (resultA == resultD) {
				eliminateUnits(attackerList);
				eliminateUnits(defenderList);
				if (attackerIsUNION) {
					elimUnion =	attackerList;
					elimConf = defenderList;
				}else {
					elimUnion =	defenderList;
					elimConf = attackerList;
				}
				activeConf = null;
				activeUnion = null;
			} else if (resultA < resultD) {
				eliminateUnits(attackerList);
				if (attackerIsUNION) {
					activeUnion = null;
					elimUnion = attackerList;
					elimConf = removeMinCombatFactor(defenderList,resultA);
					defenderList.removeAll(elimConf);
					activeConf = defenderList;
				} else {
					activeConf = null;
					elimConf = attackerList;
					elimUnion = removeMinCombatFactor(defenderList,resultA);
					defenderList.removeAll(elimUnion);
					activeUnion = defenderList;
				}
				//removeMinCombatFactor(defenderList,resultA);
			} else if (resultA > resultD) {
				eliminateUnits(defenderList);
				if (attackerIsUNION) {
					activeConf = null;
					elimConf = defenderList;
					elimUnion = removeMinCombatFactor(attackerList,resultD);
					attackerList.removeAll(elimUnion);
					activeUnion = attackerList;
					
				} else {
					activeUnion = null;
					elimUnion = defenderList;
					elimConf = removeMinCombatFactor(attackerList,resultD);
					attackerList.removeAll(elimConf);
					activeConf = attackerList;
				}
			}
			return new BattleResolutionImpl(EXCHANGE,activeUnion,activeConf,elimUnion,elimConf);
		}
		
		
		return null;
	}
	/**
	 * Removed desired units when resolving battle
	 * @param list of units that needs to be moved
	 * */
	public void eliminateUnits(Collection<GbgUnit>units) {
		Iterator<GbgUnit> i = units.iterator();
		Collection<GbgUnit> elimUnits = new ArrayList<GbgUnit>();
		while (i.hasNext()) {	
			GbgUnitImpl aUnit = (GbgUnitImpl) i.next();
			elimUnits.add(aUnit);
			board.remove(aUnit);
		}
	}
		
	/** Moves unit one square adjacently
	 * If there's no available square to retreat, remove that unit
	 *@param unit that need to be retreat
	 *@return collections of units that could not retreat and were removed
	 */
	public Collection<GbgUnit> retreat(Collection<GbgUnit>units) {
		Iterator<GbgUnit> i = units.iterator();
		Collection<GbgUnit> elimUnits = new ArrayList<GbgUnit>();		
		while (i.hasNext()) {	
			GbgUnitImpl aUnit = (GbgUnitImpl) i.next();
			Coordinate location = board.get(aUnit);
			
			if (isWithinBorder(location.getX()-1,location.getY()-1) && isWithinBorder(location.getX()+1,location.getY()-1)
					&& isWithinBorder(location.getX()-1,location.getY()+1) && isWithinBorder(location.getX()+1,location.getY()+1)) {
				Coordinate northWest = makeCoordinate(location.getX()-1,location.getY()-1);
				Coordinate northEast = makeCoordinate(location.getX()+1,location.getY()-1);
				Coordinate southWest = makeCoordinate(location.getX()-1,location.getY()+1);
				Coordinate southEast = makeCoordinate(location.getX()+1,location.getY()+1);
				if (!isOccupied(northWest) && !isInEnemysZone(aUnit,northWest)) {
					board.put(aUnit, northWest);				
				} else if (!isOccupied(northEast) && !isInEnemysZone(aUnit,northEast)) {
					board.put(aUnit, northEast);				
				} else if (!isOccupied(southWest) && !isInEnemysZone(aUnit,southWest)) {
					board.put(aUnit, southWest);				
				} else if (!isOccupied(southEast) && !isInEnemysZone(aUnit,southEast)) {
					board.put(aUnit, southEast);				
				} else {
					elimUnits.add(aUnit);
					board.remove(aUnit);
				}
			} 
		}
		return elimUnits;
	}
	
	
	
	
	/////////// FIND MINIMUM COMBAT FACTOR FOR EXCHANGE BATTLE RESULT////////
    private Stack<GbgUnit> removedUnit = new Stack<GbgUnit>();
    private boolean found = false;
    /** Store the sum of current elements stored in stack */ 
    private int sumInStack = 0;
    /** Store the selected combat factor */
    private Stack<Integer> stack = new Stack<Integer>(); 
    private float curMin = 1000;
    private Stack<GbgUnit> curBest = new Stack<GbgUnit>();
       
	/** Find the minimum combined combat factors that >= the CF of the enemy
	 * in EXCHANGE battle result
	 * @param units whose combat factor is being computed
	 * @param beginning index of the unit list
	 * @param the remaining CF we are looking for
	 */
	public void findMinCF(Collection<GbgUnit> units, int fromIndex, float min) {
    	  	
    	/* If the remainder CF is 0 then return    */
        if ( min==0) {
        	found = true;
        	return	;
        } 
        /*if we haven't found the exact equal CF combination but greater than the one we need, 
         * it's the current best, store another copy of it*/
        else if (min < 0 && sumInStack < curMin) {
        	curBest = (Stack<GbgUnit>) removedUnit.clone();
        	curMin = sumInStack;
        }
        List<GbgUnit> unitList = new ArrayList(units);
   
        while (fromIndex < unitList.size() ) {
                stack.push(unitList.get(fromIndex ).getCombatFactor());
                sumInStack += unitList.get(fromIndex ).getCombatFactor();
                removedUnit.push(unitList.get(fromIndex ));
                
                /* Make the currentIndex +1, and then use recursion to proceed further. */
                findMinCF(unitList, fromIndex + 1, min-unitList.get(fromIndex).getCombatFactor());     
               
                /*if unit just added in stack isn't the right one, remove it from stack*/
                if (found==false) {
                	sumInStack -= (Integer) stack.pop();
                    removedUnit.pop();
                } else {return;}
                fromIndex ++;
        }
        /*after going thru the whole list and still haven't found the exact equal CF combination
         * return the current best combination*/
        if ((min != 0 ) ){
        	removedUnit = (Stack<GbgUnit>)curBest.clone();
        }
     }
	
	/**
	 * Remove the found units whose combat factor (CF) is >= the CF of the enemy
	 * @param units whose CF was computed and will be removed
	 * @param enemy's combat factor
	 * @return collections of units that were removed
	 * */
	public Collection<GbgUnit> removeMinCombatFactor(Collection<GbgUnit>units, float enemyCF) {
		findMinCF(units,0,enemyCF);
		Collection<GbgUnit> elimUnits = new ArrayList<GbgUnit>();
		while(!removedUnit.empty()) {
			GbgUnit unit = removedUnit.pop();
			board.remove(unit);
			elimUnits.add(unit);
		}
		return elimUnits;
	}
	
	
	/**
	 * set random number instead of letting the program generate one
	 * served as testing purpose
	 * @param desire integer*/
	public void setRandomNum(int num) {
		randNum = num;
	}
	
	/**Set Battle Result
	 * @param computer combat factors
	 * @param random number
	 * @return corresponding battle result
	 * */
	public BattleResult setBattleResult(float br, int random) {
		if(br >= 6.0) {
			 switch(random) {
				 case 1:
					 return DELIM;
					 
				 case 2:
					 return DBACK;
					 
				 case 3:
					 return DELIM;
					 
				 case 4:
					 return DELIM;
					 
				 case 5:
					 return DELIM;
					 
				 case 6:
					 return DELIM;
					 
			 }
		} else if (br < 6.0 && br >= 5.0) {
			switch(random) {
			 case 1:
				 return DELIM;
				 
			 case 2:
				 return DBACK;
				 
			 case 3:
				 return DELIM;
				 
			 case 4:
				 return DBACK;
				 
			 case 5:
				 return DELIM;
				 
			 case 6:
				 return DELIM;
				 
			}
		 } else if (br < 5.0 && br >= 4.0) {
			 switch(random) {
			 case 1:
				 return DELIM;
				 
			 case 2:
				 return EXCHANGE;
				 
			 case 3:
				 return DELIM;
				 
			 case 4:
				 return DBACK;

			 case 5:
				 return DBACK;
				 
			 case 6:
				 return DELIM;
				 
			 }
		} else if (br < 4.0 && br >= 3.0) {
			 switch(random) {
			 case 1:
				 return DELIM;
				 
			 case 2:
				 return EXCHANGE;
				 
			 case 3:
				 return DBACK;
				 
			 case 4:
				 return DBACK;
				 
			 case 5:
				 return EXCHANGE;
				 
			 case 6:
				 return DELIM;
				 
			 }
		} else if (br < 3.0 && br >= 2.0) {
			 switch(random) {
			 case 1:
				 return DELIM;
				 
			 case 2:
				 return EXCHANGE;
				 
			 case 3:
				 return DBACK;
				 
			 case 4:
				 return ABACK;
				 
			 case 5:
				 return EXCHANGE;
				 
			 case 6:
				 return AELIM;
				 
			 }
		} else if (br < 2.0 && br >= 1.0) {
			 switch(random) {
			 case 1:
				 return DELIM;
				 
			 case 2:
				 return ABACK;
				 
			 case 3:
				 return DBACK;
				 
			 case 4:
				 return ABACK;
				 
			 case 5:
				 return EXCHANGE;
				 
			 case 6:
				 return AELIM;
				 
			 }
		} else if (br < 1.0 && br >= 0.5) {
			 switch(random) {
			 case 1:
				 return DELIM;
				 
			 case 2:
				 return EXCHANGE;
				 
			 case 3:
				 return DBACK;
				 
			 case 4:
				 return ABACK;
				 
			 case 5:
				 return AELIM;
				 
			 case 6:
				 return AELIM;
				 
			 }
		} else if (br < 0.5 && br >= 0.333) {
			 switch(random) {
			 case 1:
				 return DBACK;
				 
			 case 2:
				 return EXCHANGE;
				 
			 case 3:
				 return ABACK;
				 
			 case 4:
				 return ABACK;
				 
			 case 5:
				 return AELIM;
				 
			 case 6:
				 return AELIM;
				 
			 }
		} else if (br < 0.333 && br >= 0.25) {
			 switch(random) {
			 case 1:
				 return ABACK;
				 
			 case 2:
				 return ABACK;
				 
			 case 3:
				 return ABACK;
				 
			 case 4:
				 return ABACK;
				 
			 case 5:
				 return AELIM;
				 
			 case 6:
				 return AELIM;
				 
			 }
		} else if (br < 0.25 && br >= 0.2) {
			 switch(random) {
			 case 1:
				 return ABACK;
				 
			 case 2:
				 return AELIM;
				 
			 case 3:
				 return ABACK;
				 
			 case 4:
				 return ABACK;
				 
			 case 5:
				 return AELIM;
				 
			 case 6:
				 return AELIM;
				 
			 }
		} else if (br < 0.20 && br >= 0.167) {
			 switch(random) {
			 case 1:
				 return AELIM;
				 
			 case 2:
				 return AELIM;
				 
			 case 3:
				 return ABACK;
				 
			 case 4:
				 return AELIM;
				 
			 case 5:
				 return AELIM;
				 
			 case 6:
				 return AELIM;
				 
			 }
		} else if (br < 0.167) {
			 switch(random) {
			 case 1:
				 return AELIM;
				 
			 case 2:
				 return AELIM;
				 
			 case 3:
				 return AELIM;
				 
			 case 4:
				 return AELIM;
				 
			 case 5:
				 return AELIM;
				 
			 case 6:
				 return AELIM;
				 
			 }
		}
		return null;
	}
	/*
	 * @see gettysburg.common.GbgGame#setUnitFacing(gettysburg.common.GbgUnit, gettysburg.common.Direction)
	 */
	@Override
	public void setUnitFacing(GbgUnit unit, Direction direction)
	{
		GbgUnitImpl gbgUnit = (GbgUnitImpl) unit;
		if (isUnitsTurnChangeDirection(unit) && !gbgUnit.getHasChangedFace()) {
			unit.setFacing(direction);
			gbgUnit.setHasChangedFace(true);
			setUnitZone();
		} else throw new GbgInvalidMoveException("Cannot change facing direction");
	}
	
	
	/**set zone for each unit on board*/
	public void setUnitZone() {
		Iterator<Entry< GbgUnit,Coordinate>> i = board.entrySet().iterator();
		while (i.hasNext()) {	
			Map.Entry<GbgUnit,Coordinate>  aUnit = (Map.Entry< GbgUnit,Coordinate>) i.next();
			GbgUnitImpl gbgUnit = (GbgUnitImpl) aUnit.getKey();
			Collection<Coordinate> zone = new ArrayList<Coordinate>();
			zone = returnZone(aUnit.getValue(),aUnit.getKey().getFacing());
			gbgUnit.setZone(zone);
		}
	}
	
	/*
	 * @see gettysburg.common.GbgGame#whereIsUnit(gettysburg.common.GbgUnit)
	 */
	@Override
	public Coordinate whereIsUnit(GbgUnit unit)
	{
		Iterator<Entry<GbgUnit,Coordinate>> i = board.entrySet().iterator();
		while (i.hasNext()) {
			Map.Entry<GbgUnit,Coordinate>  aUnit = (Map.Entry< GbgUnit,Coordinate>) i.next();
			if (aUnit.getKey().equals(unit)) {
				return aUnit.getValue();
			}
		}
		return null;
	}

	/*
	 * @see gettysburg.common.GbgGame#whereIsUnit(java.lang.String, gettysburg.common.ArmyID)
	 */
	@Override
	public Coordinate whereIsUnit(String leader, ArmyID army)
	{	
		Iterator<Entry<GbgUnit,Coordinate>> i = board.entrySet().iterator();
		while (i.hasNext()) {
			Map.Entry<GbgUnit,Coordinate>  aUnit = (Map.Entry<GbgUnit,Coordinate>) i.next();
				if (aUnit.getKey().getLeader().equals(leader) && (aUnit.getKey().getArmy().equals(army))) {
				return aUnit.getValue();
			}
		}
		return null;
	}

	/////////////////////PATH FINDER//////////////////////
	Set<Coordinate> unvisited ;
	Set<Coordinate> visited ;
	HashMap<Coordinate, Coordinate> path ;
	HashMap<Coordinate, Integer> minDistance ;
	int curDistance = 0;
	
	/**Find a shortest valid path 
	 * use Dijkstra's shortest path 
	 * @param moving unit
	 * @param soruce coordinate
	 * @param destination coordinate
	 * @return step
	 */
	public int pathFinder(GbgUnit unit,Coordinate from, Coordinate to) {
		unvisited = new HashSet<Coordinate>();
		visited = new HashSet<Coordinate>();
		path =  new HashMap<Coordinate, Coordinate>();
	    minDistance =  new HashMap<Coordinate, Integer>();

	    minDistance.put(from, 0);
        unvisited.add(from);
        while (unvisited.size() > 0) {
            Coordinate curCoord = getClosestCoor(unvisited);
            visited.add(curCoord);
            unvisited.remove(curCoord);
            nextLayer(unit, curCoord);
        }
        Coordinate temp=to;
        int steps = 0;
        if (path.get(temp) == null) { //get (key) return (value)
        	return 0;
        } 
        while (path.get(temp) != null ) {
        	temp = path.get(temp);
        	steps += 1;
        }
        return steps;
	}

	/**
	 * Examine the opened neighbor coordinates of current soordinate 
	 * and decide if next step is worth stepping
	 * @param moving unit
	 * @param current Coordinate
	 */
	public void nextLayer(GbgUnit unit, Coordinate curCoord) {
		curDistance += 1;
		List<Coordinate> curCoordNeighbors = getOpenedNeighbors(unit, curCoord);
		for(Coordinate aNeigh : curCoordNeighbors) {
			if(getShortestDis(aNeigh) > curDistance) { //compare: stored distance to a neighboor > current distance to a neighboor
				minDistance.put(aNeigh, curDistance);
				path.put(aNeigh, curCoord);
				if (!unvisited.contains(aNeigh)) {
					unvisited.add(aNeigh);
				}
			}
		}
	}
	
	/**
	 * Get the opened neighbor coordinates (no unit occupied, no enemy's zone or enemy in zone)
	 * @param moving unit
	 * @param current coordinate
	 * @return list of opened neighbors
	 */
	public List<Coordinate> getOpenedNeighbors(GbgUnit unit, Coordinate curCoord) {
		 List<Coordinate> neighbors = new ArrayList<Coordinate>();
		 int x,y;
		 for(x=-1;x<2;x++) {
			 for(y=-1;y<2;y++) {
				 int newX = curCoord.getX()+x, newY = curCoord.getY()+y;
				 if (isWithinBorder(newX, newY) ) {
					 Coordinate tempCoor = makeCoordinate(newX,newY);
					 if (!isOccupied(tempCoor) && !visited.contains(tempCoor) && !isInEnemysZone(unit,tempCoor) && !isEnemyInItsZone(unit,tempCoor)) {
						 	neighbors.add(tempCoor);
					 	}
				 }
			 }
 		 }

		 return neighbors;   
	}
	/**
	 * Get the closest coordinate
	 * @param list of unvisited coordinates
	 * @return the closest coordinate
	 * */
	public Coordinate getClosestCoor(Set<Coordinate> unvisited) {
		Coordinate cor = null;
		for (Coordinate visitNow : unvisited) {
			if (cor == null) {
				cor = visitNow;
			} else {
				if (getShortestDis(visitNow) < getShortestDis(cor) ) {
					cor = visitNow;
				}
			}
		}
		
		return cor;
	}
	
	/**
	 * Get the shortest distance
	 * @param destination coordinate
	 * @return distance length
	 */
	public int getShortestDis(Coordinate where) {
		Integer length = minDistance.get(where);
		if (length == null) {
			return Integer.MAX_VALUE;
		} else return length;
	}
	
	

}
