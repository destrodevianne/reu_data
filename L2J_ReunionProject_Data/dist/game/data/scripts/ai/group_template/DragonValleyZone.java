package ai.group_template;

import java.util.HashMap;
import java.util.Map;

import ai.npc.AbstractNpcAI;

import l2r.gameserver.ThreadPoolManager;
import l2r.gameserver.datatables.SkillTable;
import l2r.gameserver.instancemanager.ZoneManager;
import l2r.gameserver.model.L2Party;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.base.ClassId;
import l2r.gameserver.model.skills.L2Skill;
import l2r.gameserver.model.zone.type.L2ScriptZone;

public class DragonValleyZone extends AbstractNpcAI
{
	public static final Map<ClassId, Double> weight = new HashMap<>();
	public static final L2ScriptZone zone = ZoneManager.getInstance().getZoneById(80005, L2ScriptZone.class);
	
	public int getBuffLevel(L2PcInstance player)
	{
		if (!player.isInParty())
		{
			return 0;
		}
		L2Party party = player.getParty();
		if (party.getMemberCount() < 5)
		{
			return 0;
		}
		for (L2PcInstance p : party.getMembers())
		{
			if (p.getLevel() < 80)
			{
				return 0;
			}
		}
		double points = 0.0D;
		int count = party.getMemberCount();
		
		for (L2PcInstance p : party.getMembers())
		{
			points += weight.get(p.getClassId()).doubleValue();
		}
		
		return (int) Math.max(0L, Math.min(3L, Math.round(points * getCoefficient(count))));
	}
	
	private double getCoefficient(int count)
	{
		double cf;
		switch (count)
		{
			case 2:
				cf = 0.1D;
				break;
			case 3:
				cf = 0.5D;
				break;
			case 4:
				cf = 0.7D;
				break;
			case 5:
				cf = 0.75D;
				break;
			case 6:
				cf = 0.8D;
				break;
			case 7:
				cf = 0.85D;
				break;
			case 8:
				cf = 0.9D;
				break;
			case 9:
				cf = 0.95D;
				break;
			default:
				cf = 1.0D;
		}
		return cf;
	}
	
	public DragonValleyZone(int questId, String name, String descr)
	{
		super(descr, descr);
		
		ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new BuffTask(), 1000L, 10000L);
	}
	
	static
	{
		weight.put(ClassId.duelist, Double.valueOf(0.2D));
		weight.put(ClassId.dreadnought, Double.valueOf(0.7D));
		weight.put(ClassId.phoenixKnight, Double.valueOf(0.5D));
		weight.put(ClassId.hellKnight, Double.valueOf(0.5D));
		weight.put(ClassId.sagittarius, Double.valueOf(0.3D));
		weight.put(ClassId.adventurer, Double.valueOf(0.4D));
		weight.put(ClassId.archmage, Double.valueOf(0.3D));
		weight.put(ClassId.soultaker, Double.valueOf(0.3D));
		weight.put(ClassId.arcanaLord, Double.valueOf(1.0D));
		weight.put(ClassId.cardinal, Double.valueOf(-0.6D));
		weight.put(ClassId.hierophant, Double.valueOf(0.0D));
		weight.put(ClassId.evaTemplar, Double.valueOf(0.8D));
		weight.put(ClassId.swordMuse, Double.valueOf(0.5D));
		weight.put(ClassId.windRider, Double.valueOf(0.4D));
		weight.put(ClassId.moonlightSentinel, Double.valueOf(0.3D));
		weight.put(ClassId.mysticMuse, Double.valueOf(0.3D));
		weight.put(ClassId.elementalMaster, Double.valueOf(1.0D));
		weight.put(ClassId.evaSaint, Double.valueOf(-0.6D));
		weight.put(ClassId.shillienTemplar, Double.valueOf(0.8D));
		weight.put(ClassId.spectralDancer, Double.valueOf(0.5D));
		weight.put(ClassId.ghostHunter, Double.valueOf(0.4D));
		weight.put(ClassId.ghostSentinel, Double.valueOf(0.3D));
		weight.put(ClassId.stormScreamer, Double.valueOf(0.3D));
		weight.put(ClassId.spectralMaster, Double.valueOf(1.0D));
		weight.put(ClassId.shillienSaint, Double.valueOf(-0.6D));
		weight.put(ClassId.titan, Double.valueOf(0.3D));
		weight.put(ClassId.dominator, Double.valueOf(0.1D));
		weight.put(ClassId.grandKhavatari, Double.valueOf(0.2D));
		weight.put(ClassId.doomcryer, Double.valueOf(0.1D));
		weight.put(ClassId.fortuneSeeker, Double.valueOf(0.9D));
		weight.put(ClassId.maestro, Double.valueOf(0.7D));
		weight.put(ClassId.doombringer, Double.valueOf(0.2D));
		weight.put(ClassId.trickster, Double.valueOf(0.5D));
		weight.put(ClassId.judicator, Double.valueOf(0.1D));
		weight.put(ClassId.maleSoulhound, Double.valueOf(0.3D));
		weight.put(ClassId.femaleSoulhound, Double.valueOf(0.3D));
	}
	
	private class BuffTask implements Runnable
	{
		public BuffTask()
		{
		}
		
		@Override
		public void run()
		{
			for (L2PcInstance pc : DragonValleyZone.zone.getPlayersInside())
			{
				int num = DragonValleyZone.this.getBuffLevel(pc);
				if (num > 0)
				{
					L2Skill skill = SkillTable.getInstance().getInfo(6885, num);
					skill.getEffects(pc, pc);
				}
			}
		}
	}
	
	public static void main(String[] args)
	{
		new DragonValleyZone(-1, DragonValleyZone.class.getSimpleName(), "ai");
	}
}