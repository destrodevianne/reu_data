package ai.individual;

import ai.npc.AbstractNpcAI;

import l2r.gameserver.ai.CtrlEvent;
import l2r.gameserver.datatables.SkillTable;
import l2r.gameserver.model.actor.L2Npc;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.skills.L2Skill;
import l2r.gameserver.network.serverpackets.CreatureSay;
import l2r.util.Rnd;

public class Benom extends AbstractNpcAI
{
	private static final int BENOM = 29054;
	
	public Benom(int questId, String name, String descr)
	{
		super(name, descr);
		int mobs[] =
		{
			BENOM
		};
		registerMobs(mobs);
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isPet, L2Skill skill)
	{
		if (npc == null || attacker == null)
			return super.onAttack(npc, attacker, damage, isPet);
		if (npc.getNpcId() == BENOM && !npc.isCastingNow() && attacker.getClassId().isMage() && Rnd.get(1000) < 15)
		{
			SkillTable.getInstance().getInfo(4996, 1);
			npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, Integer.valueOf(50000));
			npc.setTarget(attacker);
			npc.doCast(skill);
		}
		return super.onAttack(npc, attacker, damage, isPet);
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isPet)
	{
		if (npc != null && npc.getNpcId() == BENOM)
		{
			npc.broadcastPacket(new CreatureSay(npc.getObjectId(), 0, "\u0411\u0435\u043D\u043E\u043C", "\u041D\u0430 \u0441\u0435\u0439 \u0440\u0430\u0437 \u044F \u0432\u044B\u0445\u043E\u0436\u0443 \u0438\u0437 \u0441\u0432\u043E\u0435\u0433\u043E \u0434\u043E\u043C\u0430."));
			npc.broadcastPacket(new CreatureSay(npc.getObjectId(), 0, "\u0411\u0435\u043D\u043E\u043C", "\u041D\u043E \u0412\u044B \u043D\u0435 \u043C\u043E\u0436\u0435\u0442\u0435 \u0437\u0430\u0431\u0440\u0430\u0442\u044C \u044D\u0442\u043E \u0432 \u0440\u0430\u0439."));
		}
		return super.onKill(npc, killer, isPet);
	}
	
	public static void main(String args[])
	{
		new Benom(-1, Benom.class.getSimpleName(), "ai");
	}
}
