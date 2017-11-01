package student.gettysburg.engine.common;

import java.util.Collection;

import gettysburg.common.BattleResolution;
import gettysburg.common.BattleResult;
import gettysburg.common.GbgUnit;
import static gettysburg.common.BattleResult.*;

public class BattleResolutionImpl implements BattleResolution {
	private BattleResult result;
	private Collection<GbgUnit> activeUnion;
	private Collection<GbgUnit> activeConf;
	private Collection<GbgUnit> elimUnion;
	private Collection<GbgUnit> elimConf;
	
	public BattleResolutionImpl(BattleResult result,Collection<GbgUnit> activeUnion,Collection<GbgUnit> activeConf,
			Collection<GbgUnit> elimUnion, Collection<GbgUnit> elimConf) {
		this.result = result;
		this.activeUnion = activeUnion;
		this.activeConf = activeConf;
		this.elimUnion = elimUnion;
		this.elimConf = elimConf;
	}
	
	@Override
	public Collection<GbgUnit> getActiveConfederateUnits() {
		return activeConf;
	}

	@Override
	public Collection<GbgUnit> getActiveUnionUnits() {
		
		return activeUnion;
	}

	@Override
	public BattleResult getBattleResult() {
		return result;
	}

	@Override
	public Collection<GbgUnit> getEliminatedConfederateUnits() {
	
		return elimConf;
	}

	@Override
	public Collection<GbgUnit> getEliminatedUnionUnits() {
		return elimUnion;
	}
	
}
