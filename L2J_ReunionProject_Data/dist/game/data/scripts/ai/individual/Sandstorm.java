package ai.individual;

import ai.npc.AbstractNpcAI;

import l2r.gameserver.datatables.SkillTable;
import l2r.gameserver.model.actor.L2Npc;
import l2r.gameserver.model.actor.instance.L2PcInstance;

public class Sandstorm extends AbstractNpcAI
{
	private static final int Sandstorm = 32350;
	
	public Sandstorm(int questId, String name, String descr)
	{
		super(name, descr);
		super.addAttackId(Sandstorm);
	}
	
	@Override
	public String onAggroRangeEnter(L2Npc npc, L2PcInstance player, boolean isPet)
	{
		int npcId = npc.getNpcId();
		if (npcId == Sandstorm)
		{
			npc.setTarget(player);
			npc.doCast(SkillTable.getInstance().getInfo(5435, 1));
		}
		return super.onAggroRangeEnter(npc, player, isPet);
	}
	
	public static void main(String args[])
	{
		new Sandstorm(-1, Sandstorm.class.getSimpleName(), "ai");
	}
}
