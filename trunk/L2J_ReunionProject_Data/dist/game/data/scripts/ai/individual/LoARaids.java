package ai.individual;

import ai.npc.AbstractNpcAI;

import l2r.gameserver.model.actor.L2Npc;
import l2r.gameserver.model.actor.instance.L2PcInstance;

public class LoARaids extends AbstractNpcAI
{
	private static final int DRAKE_LORD = 25725;
	private static final int BEHEMOTH_LEADER = 25726;
	private static final int DRAGON_BEAST = 25727;
	L2Npc DragonBeast = null;
	L2Npc BehemothLeader = null;
	L2Npc DrakeLord = null;
	
	public LoARaids(int questId, String name, String descr)
	{
		super(name, descr);
		addKillId(new int[]
		{
			25725,
			25726,
			25727
		});
		addSpawnId(new int[]
		{
			25725,
			25726,
			25727
		});
	}
	
	@Override
	public String onSpawn(L2Npc npc)
	{
		if (npc.getNpcId() == 25725)
		{
			if (this.DrakeLord != null)
			{
				this.DrakeLord.deleteMe();
			}
		}
		
		if (npc.getNpcId() == 25726)
		{
			if (this.BehemothLeader != null)
			{
				this.BehemothLeader.deleteMe();
			}
		}
		
		if (npc.getNpcId() == 25727)
		{
			if (this.DragonBeast != null)
			{
				this.DragonBeast.deleteMe();
			}
		}
		return null;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isPet)
	{
		if ((killer.getParty() != null) && (killer.getParty().getCommandChannel() != null))
		{
			if (killer.getParty().getCommandChannel().getMemberCount() < 18)
			{
				return null;
			}
		}
		
		if (npc.getNpcId() == 25725)
		{
			this.DragonBeast = addSpawn(32884, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), false, 300000L, true);
		}
		
		if (npc.getNpcId() == 25726)
		{
			this.BehemothLeader = addSpawn(32885, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), false, 300000L, true);
		}
		
		if (npc.getNpcId() == 25727)
		{
			this.DrakeLord = addSpawn(32886, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), false, 300000L, true);
		}
		return super.onKill(npc, killer, isPet);
	}
	
	/**
	 * @return the drakeLord
	 */
	public static int getDrakeLord()
	{
		return DRAKE_LORD;
	}
	
	/**
	 * @return the behemothLeader
	 */
	public static int getBehemothLeader()
	{
		return BEHEMOTH_LEADER;
	}
	
	/**
	 * @return the dragonBeast
	 */
	public static int getDragonBeast()
	{
		return DRAGON_BEAST;
	}
	
	public static void main(String[] args)
	{
		new LoARaids(-1, LoARaids.class.getSimpleName(), "ai");
	}
}